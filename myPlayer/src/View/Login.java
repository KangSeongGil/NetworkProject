package View;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import Controller.LoginController;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.SwingConstants;

public class Login extends JFrame implements ActionListener
{

	private JPanel LoginPanel;
	private JTextField idField;
	private JPasswordField pwField;
	private LoginController LoginCT;
	private JButton JoinButton;
	private JButton LoginButton;
	private LoginController logcon;
	JLabel error;
	private ImageIcon icon ;
	public Login(LoginController logcon) 
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 499, 285);
		this.logcon = logcon;
		 icon=new ImageIcon("image/Login.jpg");
		//패널 
		LoginPanel = new JPanel(){
 		   public void paintComponent(Graphics g) {
               //  Approach 1: Dispaly image at at full size
               g.drawImage(icon.getImage(), 0, 0, null);
               //  Approach 2: Scale image to size of component
               // Dimension d = getSize();
               // g.drawImage(icon.getImage(), 0, 0, d.width, d.height, null);
               // Approach 3: Fix the image position in the scroll pane
               // Point p = scrollPane.getViewport().getViewPosition();
               // g.drawImage(icon.getImage(), p.x, p.y, null);
               setOpaque(false);
               super.paintComponent(g);
           }
       
     
		};
		LoginPanel.setForeground(Color.WHITE);
		LoginPanel.setBackground(Color.DARK_GRAY);
		LoginPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(LoginPanel);
		LoginPanel.setLayout(null);
		
		//아이디 필드 
		idField = new JTextField(); 
		idField.setBounds(173, 67, 240, 30);
		LoginPanel.add(idField);
		idField.setColumns(10);
		
		//패스워드 필드 
		pwField = new JPasswordField();
		pwField.setColumns(10);
		pwField.setBounds(173, 110, 240, 30);
		LoginPanel.add(pwField);
		
		//아이디 라벨 
		JLabel lblId = new JLabel("ID");
		lblId.setForeground(Color.WHITE);
		lblId.setBounds(72, 67, 61, 30);
		LoginPanel.add(lblId);
		
		//패스워드 라벨 
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setBounds(72, 110, 61, 30);
		LoginPanel.add(lblPassword);
		
		error = new JLabel();
		error.setHorizontalAlignment(SwingConstants.CENTER);
		error.setForeground(Color.WHITE);
		error.setBounds(214,152, 120, 30);
		LoginPanel.add(error);
		
		//로그인 버튼 
		LoginButton = new JButton("Login");
		LoginButton.setBounds(72, 209, 117, 29);
		LoginPanel.add(LoginButton);
		LoginButton.addActionListener(this);
		LoginButton.setActionCommand("Login");
		
		
		//회원가입 버튼 
		JoinButton = new JButton("Join");
		JoinButton.setBounds(309, 209, 117, 29);
		LoginPanel.add(JoinButton);
		JoinButton.addActionListener(this);
		JoinButton.setActionCommand("Join");
		
		
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		boolean LoginRtn;
		//액션 리스너 재정의
		if (e.getSource().equals(LoginButton))
		{
			System.out.println("LoginButton");
			try 
			{
				LoginRtn=logcon.loginAction(idField.getText(),pwField.getPassword());
				if(LoginRtn == false)
				{
					error.setText("다시 입력해 주세요 ");
					error.setForeground(Color.YELLOW);
				}
				else
				{
					error.setText("");
					System.out.println("창내리");
					this.setVisible(false);
				}
			}
			catch (IOException e1)
			{
				
			}
			
		}
		else if((e.getSource().equals(JoinButton)))
		{
			try {
				logcon.joinAction();
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
	}
	
	public String getID()
	{
		return idField.getText();
	}
	
	public char[] getPassword()
	{
		return pwField.getPassword();
	}
	
	
	
}
