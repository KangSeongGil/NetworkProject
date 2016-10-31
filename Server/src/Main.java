

import java.io.IOException;

public class Main {

	public static void main(String[] args)  
	{
		// TODO Auto-generated method stub
		Server server = new Server();
		while(true)
		{
			try
			{
				server.acceptLogin();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

}


















