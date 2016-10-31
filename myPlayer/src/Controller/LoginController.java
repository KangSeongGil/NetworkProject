package Controller;

import View.Login;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import Client.Client;

public class LoginController
{

	
	String eventToken;
	private int loginFlag;
	BufferedReader br;
	Login lg;
	MainController mainCT;
	Client client;
	String ID;
	
	public LoginController (MainController mainCT,Client client)
	{
		this.mainCT = mainCT;
		this.client = client;
		lg=new Login(this);
	}
	
	
	
	public boolean loginAction(String ID,char[] password) throws
	IOException
	{
		int port;
		if((port=client.requestLogin(ID, password))>0)
		{
			System.out.println("login");
			mainCT.signal(3,port);
			return true;
		}
		else return false;
	}
	

	
	public  void joinAction() throws IOException 
	{
		mainCT.signal(2,0);
	}
	
	public String getID()
	{
		return ID;
	}
	
	
	
		
}
