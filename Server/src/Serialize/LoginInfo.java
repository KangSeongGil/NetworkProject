package Serialize;
import java.io.Serializable;
import java.net.InetAddress;

public class LoginInfo implements Serializable  {
	
	private InetAddress client_AD;
	private int client_PT;
	private String ID;
	transient private String PW;
	
	public LoginInfo(InetAddress client_AD,int client_PT,String ID,String PW)
	{
		this.client_AD =  client_AD;
		this.client_PT = client_PT;
		this.ID = ID;
		this.PW = PW;
	}
	
	
	public InetAddress getIP()
	{
		return client_AD;
	}
	
	public int getPort()
	{
		return client_PT;
	}
	
	public String getID()
	{
		return ID;
	}
	
	public String getPW()
	{
		return PW;
	}

}
