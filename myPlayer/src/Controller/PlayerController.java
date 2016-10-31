package Controller;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import Client.Client;
import Serialize.LoginInfo;
import View.VideoApplication;
import View.VideoApplication;

public class PlayerController 
{
	MainController mainCT;
	VideoApplication audio;
	int port;
	Client client;
	soundControll sc ;
	int mode;
	Vector<String>chatMember;
	
	public PlayerController(MainController mainCT,Client client) throws IOException
	{
		this.mainCT = mainCT;
		audio= new VideoApplication(this);
		this.client = client;
		client.connect();
	}
	
	
	public void play(String name) throws IOException
	{
		client.getPlayData(name);
		if(sc!=null)
		{
			sc.close();
			sc.setFile(name);
			sc.play();
		}
		if(sc == null)
		{
			sc=new soundControll(name);
		}
	}
	
	public void istStop()
	{
		sc.istStop();
	}
	
	public void istPlay()
	{
		sc.istPlay();
	}
	
	public void SearchList() 
	{
		 Vector <String>sl = new Vector <String>();
		 sl=client.searchList();
		 audio.setSearchResult(sl);
	}
	public void rcList()
	{
		 Vector <String>sl = new Vector <String>();
		 sl=client.rcList();
		 System.out.println("result"+sl.get(0));
		 audio.setrcResult(sl);
	}
	
	public void closeWindow()
	{
		try {
			client.close_soket();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	class soundControll implements Runnable
	{
		File Clap;
		Clip clip ;
		public soundControll(String name)
		{
				setFile(name);
				run();
		}
		
		public void run()
		{
			System.out.println("play");
			play();
		}
		
		public void setFile(String name)
		{
			Clap=new File(name);
		}
		
		public void play()
		{
			try
			{
				clip = AudioSystem.getClip();
				clip.open(AudioSystem.getAudioInputStream(Clap));
				clip.start();
				//Thread.sleep(clip.getMicrosecondLength()/1000);
			}
			catch(Exception e)
			{
				
			}
		}
		
		public int getMode()
		{
			return mode;
		}
		
		public void close()
		{
			clip.close();
		}
		
		public void istStop()
		{
			clip.stop();
		}
		
		public void istPlay()
		{
			clip.start();
		}
	}
	public Vector <LoginInfo> getUserInfo()
	{
		Vector<LoginInfo> loginInfo = client.getUserInfo();
		System.out.println("Controller LoginInfo lenght"+loginInfo.size());
		return loginInfo;
	}
	
	public void exit()
	{
		 client.exit();
	}
	
	public void clickChatbutton(Vector<String>chatMember)
	{
		try 
		{
			this.chatMember = chatMember;
			System.out.println("player chatMeber : "+this.chatMember.get(0));
			mainCT.signal(4,0);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public Vector<String> getchatMember()
	{
		return chatMember;
	}
}

