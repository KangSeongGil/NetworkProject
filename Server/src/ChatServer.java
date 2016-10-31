import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Vector;
import Serialize.LoginInfo;
public class ChatServer extends Thread{
	
	private Vector <LoginInfo> userInfo;
	private String ServerID;
	private int amount;
	private InetAddress serverIP;
	Socket ip_socket[];
	Socket op_socket[];
	ServerSocket serverSocket;
	Vector<String> member;
	LoginInfo[] memberInfo;
	DatagramSocket socket ;
	DatagramPacket packet;
	InputStream is[];
	OutputStream os[];
	private DataInputStream dis[] ;
	private DataOutputStream dos[];
	int port;
	

	
	public ChatServer (Vector <LoginInfo> userInfo,int amount,Vector<String> member,int port)
	{
		this.userInfo = userInfo;
		this.amount = amount;
		ip_socket = new Socket[amount];
		op_socket = new Socket[amount]; 
		this.member = member;
		memberInfo = new LoginInfo[amount];
		dis = new DataInputStream[amount];
		dos = new DataOutputStream[amount];
		this.port =port;
	}
	
	public void run()
	{
		System.out.println("run");
		searchMember();
		connect();
		while(true)
			workComunication();
	}
	
	public void searchMember()
	{
		
		for(int i=0;i<member.size();i++)
		{
			String ID = member.get(i);
			for(int j = 0 ; j<userInfo.size();j++)
			{
				if(ID.equals(userInfo.get(j).getID()))
				{
					memberInfo[i]=userInfo.get(j);
				}
			}
		}
	}
	
	public void connect()
	{
		byte[] buff=new byte [20]; 
		ip_socket = new Socket[amount];
		is = new InputStream[amount];
		os = new OutputStream[amount];
		
		System.out.println("connect 중 ...");
		try
		{
			System.out.println("port:"+port);
			socket=new DatagramSocket(port);
			for(int i=0;i<amount;i++)
			{
				packet = new DatagramPacket(buff,buff.length,memberInfo[i].getIP(),memberInfo[i].getPort());
				socket.send(packet);
			}
			socket.close();
			serverSocket = new ServerSocket(port);
			for(int i=0;i<amount;i++)
			{			
				ip_socket[i]= serverSocket.accept();
				ip_socket[i].setSoTimeout(50);
				is[i]=ip_socket[i].getInputStream();
				dis[i]=new DataInputStream(is[i]);
				os[i]=ip_socket[i].getOutputStream();
				dos[i]=new DataOutputStream(os[i]); 
			}
			System.out.println("connect 완!");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		port++;
	}
	
	public void workComunication() 
	{
		String Message;
		for(int i=0;i<amount;i++)
		{
			try 
			{
				 Message=dis[i].readUTF();
				 InetAddress ip =ip_socket[i].getInetAddress();
				 int port = ip_socket[i].getPort();
				 String ID="";
				// System.out.println("oMessage:"+Message);
				 for(int k=0;i<userInfo.size();i++)
				 {
					 if(userInfo.get(k).getIP()==ip&&userInfo.get(k).getPort()==port)
					 {
						 ID = userInfo.get(k).getID();
					 }
				 }
				 if(Message!=null)
				 {
					 for(int j=0;j<amount;j++)
					 {
						 System.out.println("Message:"+Message);
						 dos[j].writeUTF(Message);
						 dos[j].flush();
					 }
				 }
			}
			catch(IOException e)
			{
				//e.printStackTrace();
			}
			Message=null;
		}
	}

}
