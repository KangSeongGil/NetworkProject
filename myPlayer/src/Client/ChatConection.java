package Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class ChatConection extends Thread{
	
	private DatagramSocket socket;
	private int port ;
	byte buff[] ;
	InetAddress serverIP;
	int serverPort;
	public ChatConection(int port,DatagramSocket socket)
	{
		this.socket = socket;
		this.port = port;
		buff = new byte [20];
	}
	
	public void run()
	{
		while(true)
		{
			DatagramPacket packet = new DatagramPacket(buff, buff.length);
			System.out.println("chat Signal 대기중 ");
			try 
			{
				socket.receive(packet);
			} catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			packet.getData();
			serverIP=packet.getAddress();
			serverPort = packet.getPort();
			System.out.println("ServerIP: "+serverIP+"serverPort:"+serverPort);
			try 
			{
				Thread.sleep(1000);
			} 
			catch (InterruptedException e)
			{// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new ChatClient(serverIP,serverPort).start();
			
		}
	}

}
