package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.StringTokenizer;
import java.util.concurrent.Semaphore;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import Client.ChatClient;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;
import java.awt.event.ActionEvent;


public class ChatClient extends Thread{
	
	Socket sock;
	int serverPort;
	InetAddress serverIP;
	int port;
	byte buff[] ;
	DatagramSocket socket;
	OutputStream os;
	InputStream is;
	String sndMSG;
	String rcvMSG;
	private DataInputStream dis;
	private DataOutputStream dos;
	private JTextArea chatContents;
	private JTextArea sendText;
	private JScrollPane scrollPane;
	private JFrame frame;
	private Vector <String> chatMember;
	private JTextField namefield;
	private JButton name;
	String nickname;
	JButton sendButton;
	public ChatClient (InetAddress serverIP,int serverPort)
	{
		this.serverIP = serverIP;
		this.serverPort = serverPort;
		buff= new byte[25];
		sndMSG = null;
		rcvMSG = null;
	}

	public void run()
	{
		recvSignal();
		initialize();
		while(true)
		{
			try 
			{
				rcvMSG=dis.readUTF();
				if(rcvMSG != null) 
				{
					System.out.println("rcvMSG:"+rcvMSG);
					chatContents.append("\n");
					chatContents.append(rcvMSG);
				}
			} catch (IOException e) 
			{
				//e.printStackTrace();
			}
			rcvMSG=null;
		}
	}
	
	public void recvSignal()
	{
		try 
		{
			sock = new Socket(serverIP,serverPort);
			sock.setSoTimeout(50);
			is = sock.getInputStream();
			os = sock.getOutputStream();
			dis = new DataInputStream(is);
			dos = new DataOutputStream(os);
			System.out.println("tcp connect");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public String getrcvMSG()
	{
		return rcvMSG;
	}
	
	public void setsndMSG(String sndMSG)
	{
		System.out.println("set");
		this.sndMSG = sndMSG;
	}
	
	private void initialize() 
	{
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(224, 255, 255));
		frame.setBounds(100, 100, 333, 596);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		scrollPane=new JScrollPane ();
		scrollPane.setBounds(0, 0, 333, 467);
        frame.getContentPane().add(scrollPane);
		sendText = new JTextArea();
		sendText.setBackground(new Color(255, 250, 240));
		sendText.setBounds(0, 498, 273, 44);
		sendText.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e){

				if(e.getKeyCode()==10){
				sendButton.doClick();

				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				if(e.getKeyCode()==10)
				{
					sendButton.doClick();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		frame.getContentPane().add(sendText);
		
		sendButton = new JButton("SEND");
		sendButton.setBackground(new Color(255, 250, 205));
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try 
				{
					dos.writeUTF(nickname+":"+sendText.getText());
					dos.flush();
					sendText.setText("");
				} catch (IOException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		sendButton.setBounds(273, 498, 60, 44);
		frame.getContentPane().add(sendButton);
		
		chatContents = new JTextArea();
		chatContents.setBounds(0, 0, 333, 467);
		frame.getContentPane().add(chatContents);
		scrollPane.setViewportView(chatContents);
		
		namefield = new JTextField();
		namefield.setBounds(0, 546, 134, 28);
		frame.getContentPane().add(namefield);
		namefield.setColumns(10);
		
		JButton name = new JButton("별명");
		name.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nickname = namefield.getText();
			}
		});
		name.setBounds(133, 547, 117, 29);
		frame.getContentPane().add(name);
		frame.setVisible(true);
	}
	
	

}
