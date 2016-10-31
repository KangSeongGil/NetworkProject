package Client;
import java.lang.instrument.Instrumentation;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.Object;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.Vector;

import Serialize.LoginInfo;

public class Client extends Thread implements Serializable
{
	static final int BUFSIZE = 4000;
	static int SERVERPORT = 16000;
	static final int PORT = 4333;
	int chatPort = 5000;
	byte buff [];
	DatagramSocket socket ;
	DatagramPacket packet;
	InetAddress server;
	ServerSocket serverSocket;
	Socket ip_sock;
	Socket op_sock;
	InputStream in;
	OutputStream out;
	DataOutputStream dos ;
	DataInputStream dis ;
	private ObjectInputStream FromServer;
	private ChatConection chConection;

	
	public Client(String hostIP)//soket을 만들고 그다음 아이피 설정후 
	{
		try 
		{
			server = InetAddress.getByName(hostIP);
			socket = new DatagramSocket(PORT);
			ObjectInputStream FromServer ;
		} 
		catch (UnknownHostException e) 
		{
			// TODO Auto-generated catch block
			System.out.println("Error Get server IP address");
			e.printStackTrace();
		} 
		catch (SocketException e) 
		{
			System.out.println("Error Get Socket");
			e.printStackTrace();
		}
		 catch (IOException e) 
		{
				// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public  int requestLogin(String ID,char [] PW) throws IOException
	{
		String sendMSG  = new String ("1|");
		String recvMSG;
		sendMSG += ID;
		sendMSG += "|";
		String temp=new String(PW,0,PW.length);
		sendMSG+=temp;
		sendMSG += "|";
		buff = new byte [BUFSIZE] ;
		buff = sendMSG.getBytes("UTF-8");
		packet = new DatagramPacket(buff,buff.length,server,SERVERPORT);
		socket.send(packet);
		buff = new byte [BUFSIZE] ;
		packet = new DatagramPacket(buff, buff.length);
		socket.receive(packet);
		recvMSG=new String(packet.getData(),"UTF-8");
		StringTokenizer tokens = new StringTokenizer(recvMSG);
		recvMSG=tokens.nextToken("|");
		System.out.println("recive:"+recvMSG+" length:"+recvMSG.length());
		if(recvMSG.equals("true"))
		{
			System.out.println("login true");
			recvMSG=tokens.nextToken("|");
			System.out.println("recive:"+recvMSG+" length:"+recvMSG.length());
			SERVERPORT = Integer.parseInt(recvMSG);
			return SERVERPORT;
		}
		else
		{
			System.out.println("login false");
			return 0;
		}
		
	}
	

	public void connect() throws IOException
	{
		System.out.println("server ip:"+server +"server port :"+SERVERPORT);
		op_sock = new Socket(server, SERVERPORT);
		out = op_sock.getOutputStream();
		dos = new DataOutputStream(out);
		System.out.println("client port : "+PORT);
		serverSocket=new ServerSocket(PORT);
		ip_sock = serverSocket.accept();
		in =ip_sock.getInputStream(); 
		dis = new DataInputStream(in) ;
		chConection = new ChatConection(PORT,socket);
		chConection.start();
	}
	
	public void close_soket() throws IOException
	{
		ip_sock.close();
		op_sock.close();
	}
	
	
	public void getPlayData(String name) throws IOException
	{	
		dos.writeUTF("2");
		dos.writeUTF(name);
		dos.flush();
		File f= new File (name);
		FileOutputStream fos = new FileOutputStream(f);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		byte [] buf = new byte[1024];
		int n=0;
		System.out.println("파일 수신중....");
		String length=dis.readUTF();
		int fileSize = Integer.parseInt(length);
		System.out.println(fileSize+"bytes크기의 파일!");
		int len = 0;
		int lentotal=0;
		
		do
		{
			len=dis.read(buf);
			bos.write(buf,0,len);
			lentotal+=len;
			System.out.println(lentotal+"bytes크기의 파일수신!");
		}while(lentotal<fileSize);
		
		fos.close();
		System.out.println(fileSize+"bytes크기의 파일수신 완료!");
	}
	
	public Vector<String> searchList()
	{
		String songName="start";
		Vector<String> List = new Vector<String>();
		try 
		{
			dos.writeUTF("1");
			dos.flush();
			System.out.println("노래 검색 요청중 ... ");
			while(!songName.equals("end"))
			{
				songName=dis.readUTF();
				List.addElement(songName);
				System.out.println(songName);
			}
			System.out.println("끝 "+ List.size());
			return List;
		} 
		catch (IOException e) 
		{
			return null;
		}
	}
	
	public Vector<String> rcList()
	{
		String songName="start";
		Vector<String> List = new Vector<String>();
		try 
		{
			dos.writeUTF("3");
			dos.flush();
			System.out.println("노래 추 요청중 ... ");
			while(!songName.equals("end"))
			{
				songName=dis.readUTF();
				List.addElement(songName);
				System.out.println(songName);
			}
			System.out.println("끝 "+ List.size());
			return List;
		} 
		catch (IOException e) 
		{
			return null;
		}
	}
	
	public Vector <LoginInfo> getUserInfo()
	{
		Vector<LoginInfo> loginInfo = new Vector<LoginInfo>();
		System.out.println("client getUserInfo()");
		Object object;
		int i=0;
		try 
		{
			dos.writeUTF("4");
			dos.flush();
			sleep(1500);
			FromServer = new ObjectInputStream(ip_sock.getInputStream());
			while(((LoginInfo)(object = FromServer.readObject())).getID()!=null)
			{
				System.out.println(((LoginInfo)(object)).getID());
				loginInfo.add((LoginInfo)object);
			}
			System.out.println("로그인 정보 획득\n vector Size:"+loginInfo.size());
		} 
		catch (ClassNotFoundException | IOException | InterruptedException e)	
		{
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		 return loginInfo;
	}
	
	public void exit()
	{
		try 
		{
			dos.writeUTF("5");
			dos.close();
			dis.close();
			ip_sock.close();
			op_sock.close();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void rqChat(Vector <String> chatMember)
	{
		try 
		{
			dos.writeUTF("6");
			
			for(int i=0;i<chatMember.size();i++)
			{
				dos.writeUTF(chatMember.get(i));
			}
			dos.writeUTF("end");
			
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
}
