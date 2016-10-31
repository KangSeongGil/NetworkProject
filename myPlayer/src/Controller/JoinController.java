package Controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import View.Join;


public class JoinController 
{
	private MainController mainCT;
	private BufferedReader br;
	private Join joininterface ;
	FileOutputStream output;
	
	public JoinController(MainController mainCT)
	{
		this.mainCT=mainCT;
		joininterface = new Join(this);
		
	}
	
	
	
	public boolean joinAction(String ID,char[] password) throws IOException
	{
		int flag;
		String strPW = new String(password);
		try 
		{
			br = new BufferedReader(new FileReader("login.txt"));
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        while(true) 
        {
            String line = br.readLine();
            System.out.println(" line:"+ line);
            if (line==null) 
            {
            	flag=-1;
            	break;
            }
            StringTokenizer tokens = new StringTokenizer(line);
          
            if((ID.equals(tokens.nextToken(",")))||(strPW.equals(tokens.nextToken(","))))
            {
            	flag=1;
            	break;
            }
        }
       
        System.out.println(flag);
        
        if(flag==1)
        {
        	return false;
        }
        else 
        {
        	 output= new FileOutputStream("login.txt",true);
             String data="\r\n"+ID+","+strPW;
             output.write(data.getBytes());
             output.close();
             return true;
        }
	}

}
