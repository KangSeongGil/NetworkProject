package Controller;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Vector;

import Client.Client;

public class MainController 
{
	private LoginController loginCT;
	private JoinController joinCT;
	private PlayerController plCT;	
	Client client;
	public MainController()
	{
		client = new Client ("127.0.0.1");
		LoginController loginCT = new LoginController(this,client);
	}
	
	public void signal(int flag ,int port) throws IOException
	{
		//flag == 1 login
		//     == 2 join
		if(flag == 1)
		{
			loginCT = new LoginController(this,client);
		}
		else if(flag == 2)
		{
			joinCT = new JoinController(this);
		}
		else if(flag ==3)
		{
			System.out.println("flag = 3");
			plCT =  new PlayerController (this,client);
		}	
		else if(flag == 4)
		{
			System.out.println("flag =4");
			client.rqChat(plCT.getchatMember());
		}
	}
	
}
