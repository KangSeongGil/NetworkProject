package View;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Controller.JoinController;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class Join  extends JFrame{

	private JFrame frame;
	private JTextField idField;
	private JoinController JoinCT;
	private JLabel perror;
	private JPasswordField passwordField;
	public Join(JoinController JoinCT) 
	{
		this.JoinCT = JoinCT;
		initialize();
	}

	private void initialize() 
	{
		System.out.println("Join");
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(0, 0, 51));
		frame.setForeground(new Color(0, 0, 51));
		frame.setBackground(new Color(0, 0, 51));
		frame.setBounds(100, 100, 380, 178);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel ID = new JLabel("ID");
		ID.setForeground(new Color(255, 255, 255));
		ID.setBounds(67, 37, 61, 16);
		frame.getContentPane().add(ID);
		
		JLabel Password = new JLabel("Password");
		Password.setForeground(Color.WHITE);
		Password.setBounds(67, 67, 61, 16);
		frame.getContentPane().add(Password);
		
		idField = new JTextField();
		idField.setBounds(140, 37, 115, 16);
		frame.getContentPane().add(idField);
		idField.setColumns(10);
		
		JButton JoinButton = new JButton("Join");
		JoinButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				boolean flag=false;
				try 
				{
					flag=JoinCT.joinAction(idField.getText(),passwordField.getPassword());
				} catch (IOException e1) 
				{
					System.out.println("Read Write error\n");
				}
				
				if(flag == false)
				{
					System.out.println("return false");
					perror.setText("다시 입력해 주세요 ");
					perror.setForeground(Color.YELLOW);
				}
				else
				{
					System.out.println("return true");
					frame.setVisible(false);
				}
				
			}
		});
		JoinButton.setBounds(267, 49, 67, 22);
		frame.getContentPane().add(JoinButton);
		
		perror = new JLabel("");
		perror.setForeground(new Color(255, 204, 0));
		perror.setBounds(72, 110, 262, 16);
		frame.getContentPane().add(perror);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(140, 67, 115, 16);
		frame.getContentPane().add(passwordField);
		frame.setVisible(true);
	}
	
}
