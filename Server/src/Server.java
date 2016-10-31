
import java.lang.instrument.Instrumentation;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.Object;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import java.util.Vector;

import Serialize.LoginInfo;

public class Server 
{
	private int chatport =50000;
	static final int BUFSIZE = 4000;
	static final int PORT = 16000;
	static int servicePort = 20000; 
	byte buff [];
	DatagramSocket socket ;
	DatagramPacket packet;
	InetAddress server;
	Vector<PlayServer>thread;
	Vector<LoginInfo>loginInfo;
	int vtIndex=0;
	public Server()//soket을 만들고 그다음 아이피 설정후 
	{
		try 
		{
			socket = new DatagramSocket(PORT);
			thread = new Vector<PlayServer>();
			loginInfo = new Vector<LoginInfo>();
		} 
		
		catch (SocketException e) 
		{
			System.out.println("Error Get Socket");
			e.printStackTrace();
		}
	}

	
	public  void acceptLogin() throws IOException
	{
			String flag,strBuf,compID,compPW;
			int tmp = -1, tmp_port = 0;
			buff=new byte[BUFSIZE];
		 	BufferedReader br = new BufferedReader(new FileReader("login.txt"));
		 	if(br == null) System.out.println("null");
		 	packet = new DatagramPacket(buff,BUFSIZE);
		 	socket.receive(packet);
		 	buff = (packet.getData());
		 	System.out.println("recv:"+new String(buff,"UTF-8")+" leng:"+buff.length);
		 	strBuf =  new String(buff,"UTF-8");
			System.out.println("Server="+strBuf);
		 	StringTokenizer tokens = new StringTokenizer(strBuf);
		 	tokens.nextToken("|") ;
		 	compID=tokens.nextToken("|") ; 
		 	compPW=",";
		 	compPW+=tokens.nextToken("|") ;
		 	System.out.println("compID: "+compID+"comppassword:"+compPW);
	        while(true) 
	        {
	            String line = br.readLine();
	            if(line == null)
	            {
	            	 flag = "false";
	            	 System.out.println("search fail");
	            	 break;
	            }
	            
	            tokens = new StringTokenizer(line);
	            String ID=tokens.nextToken(",") ; 
	            String PW=tokens.nextToken("\n") ;    
	           
	            if (compID.equals(ID)&&compPW.equals(PW)) 
	            {
	            	flag="true";
	            	flag += "|";
	     	        flag += String.valueOf(servicePort);
	     	        tmp_port = servicePort;
	     	        tmp = 1;
	     	        servicePort++;
	            	break;
	            }
	        }
	        
	        br.close();
	        buff=new byte[BUFSIZE];
	        flag += "|";
	        buff = flag.getBytes("UTF-8");
	        packet = new DatagramPacket(buff, buff.length, packet.getAddress(),  packet.getPort());
	        socket.send(packet);
	        
	        if(tmp == 1)
	        {
	        	 thread.add(new PlayServer(tmp_port,packet.getAddress(), packet.getPort(),this));
	        	 loginInfo.add(new LoginInfo(packet.getAddress(),packet.getPort(),compID,compPW));
	        	 thread.lastElement().start();
	        	 tmp = -1;
	        }     
	        
	 }
	
	
	
	 public void remove(int idx)
	 {
		 loginInfo.remove(idx);
	 }
	 public Vector<LoginInfo> getLoginVector()
	 {
		 return loginInfo;
	 }
	 
	 public void makeChatServer()
	 {
		 
	 }


	public int getChatPort()
	{
		
		// TODO Auto-generated method stub
		chatport++;
		return chatport;
	}
	
}
