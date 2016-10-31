import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.text.html.HTMLDocument.Iterator;

import Serialize.LoginInfo;

public class PlayServer extends Thread implements Serializable
{
	private int port ;
	private int chatport ;
	private static final int BUFSIZE = 4000;
	private DatagramSocket socket ;
	private InetAddress client_AD;
	private int client_PT;
	private int flag;
	private Socket ip_sock;
	private Socket op_sock;
	private InputStream in;
	//private BufferedReader br;
	private OutputStream out;
//	private PrintWriter pw;
	private DataInputStream dis ;
	private DataOutputStream dos;
	private String mode;
	private Server initServer;
	private ObjectOutputStream ToClient;
	private Vector <ChatServer> ChatServer;
	private Vector <String>member;
	
	public PlayServer(int port,InetAddress client_AD,int client,Server initServer) 
	{
		this.port=port;
		this.client_AD =  client_AD;
		client_PT=client;
		this.initServer = initServer;
		this.chatport = chatport;
		ChatServer = new Vector<ChatServer>();
	}
	
	public void run() 
	{
		try 
		{
			System.out.println("PlayServer");
			System.out.println("port"+port);
			socket=new DatagramSocket(port);
			
			ServerSocket serverSocket = new ServerSocket(port);
			ip_sock = serverSocket.accept();
			System.out.println("PlayServer");
			in =ip_sock.getInputStream(); 
			System.out.println("client ad:"+client_AD+"client port:"+client_PT);
			op_sock = null;
			try {
				this.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			op_sock = new Socket(client_AD, client_PT);
			out = op_sock.getOutputStream();
			dis=new DataInputStream(in) ;
			dos = new DataOutputStream(out);
			sellect();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	public void sellect() 
	{
		while(true)
		{
			System.out.println("모드요청 중 ....");
			try 
			{
				mode=dis.readUTF();
			} catch (IOException e) 
			{
				
					exit();
					System.out.println("포트닫기 ");
					break;
				
			
			}
			System.out.println("sellect :"+mode);
			if(mode.equals("1"))
			{
				searchList();
			}
			else if(mode.equals("2"))
			{
				try 
				{
					sndSong();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			else if(mode.equals("3"))
			{
				 rcList();
			}
			else if(mode.equals("4"))
			{
				userInfo();
			}
			else if(mode.equals("5"))
			{
				exit();
			}
			else if(mode.equals("6"))
			{
				settingChat();
			}
		}
		
	}
	
	public void settingChat()
	{
		member = new Vector <String>();
		String serverID;
		String getID;
		int i=0;
		try 
		{
			while(true)
			{
				getID=dis.readUTF();
				System.out.println("getID:"+getID);
				if(!getID.equals("end"))
				{
					member.add(getID);
					i++;
				}
				else
				{
					break;
				}
			}
			System.out.println("member size:"+initServer.getLoginVector().size());
			ChatServer.add(new ChatServer(initServer.getLoginVector(),i,member,initServer.getChatPort()));
			ChatServer.lastElement().start();
			chatport++;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		chatport++;
	}
	
	public void exit()
	{
		Vector <LoginInfo> loginInfo=initServer.getLoginVector();
		int length = loginInfo.size();
		
		for(int i = 0 ; i <length ; i++ )
		{
			
			if(loginInfo.get(i).getIP()==client_AD)
			{
				initServer.remove(i);
				try 
				{
					dis.close();
					dos.close();
					op_sock.close();
					ip_sock.close();
					System.out.println("종료 !");
				} 
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		}
	}
	
	public void rcList()
	{
		 try{
		      BufferedReader in = new BufferedReader(new FileReader("./recommend.txt"));
		      String s;

		      while ((s = in.readLine()) != null) 
		      {
		    		dos.writeUTF(s);
		      }
		      in.close();
		      dos.writeUTF("end");
		  
		    } catch (IOException e) {
		        System.err.println(e); // 에러가 있다면 메시지 출력
		        System.exit(1);
		    }
		 
	}
	
	public void sndSong() throws IOException
	{ 
		String name=dis.readUTF();
		System.out.println(name+"파일 전송 요");
		File file = new File("./Music/"+name);
		FileInputStream fin = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fin);
		byte[] buf = new byte[1024];
		int n=0;
		
		long fileSize = 0;
		fileSize=file.length();
		String length=Long.toString(file.length());
		System.out.println(length+"bytes 크기의 파일전송!");
		dos.writeUTF(length);
		
		int len;
		
		while((len=bis.read(buf))!=-1)
		{
			dos.write(buf,0,len);
		}
		
		dos.flush();
		fin.close();
		System.out.println("파일전송 완료!");
	}
	
	public void searchList()
	{
		File dir = new File("Music");
		File[] fileList = dir.listFiles(); 
		
		for(int i = 0 ; i < fileList.length ; i++)
		{
			try 
			{
				dos.flush();
			} 
			catch (IOException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			File file = fileList[i]; 
			if(file.isFile())
			{
				try 
				{
					dos.writeUTF(file.getName());
					System.out.println("\t 파일 이름 = " + file.getName());
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
					System.out.println("전송 에러 ");
				}
			}
		}
		try 
		{
			dos.writeUTF("end");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		System.out.println("파일 리스트 전송 완료 ");
	}

	public void userInfo()
	{
		System.out.println("userInfoMode");
		try 
		{
			ToClient = new ObjectOutputStream(op_sock.getOutputStream());
			System.out.println("login vector size:"+initServer.getLoginVector().size());
			Vector <LoginInfo> loginInfo = initServer.getLoginVector();
			int length = loginInfo.size();
			
			for(int i = 0 ;i<length;i++)
			{
				ToClient.reset();
				ToClient.writeObject(loginInfo.elementAt(i));
				System.out.println(loginInfo.elementAt(i).getID()+"정보 전송!");
			}
			ToClient.reset();
			ToClient.writeObject(new LoginInfo(null,0,null,null));
			System.out.println("로그인 정보 백터 객체 전송 성공 ");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
	}
	
	
	
	
	
}
