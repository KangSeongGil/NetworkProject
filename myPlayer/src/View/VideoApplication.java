package View;

import  java.awt.*;
import  java.awt.event.*;
import  java.io.*;
import  java.net.*;
import java.util.Vector;

import  javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Controller.PlayerController;
import Serialize.LoginInfo;

import  javax.media.*;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public  class  VideoApplication  extends  JFrame  {
	
        private static final ListSelectionListener ListSelectionEvent = null;
		private  Player  player;  
        private  Container  container;  
        private  File  mediaFile; 
        private  PlayerController  playerCT;
        private  URL  fileURL;
        private  JButton search ;
        private  Vector<String> searchList;
        private  Vector<String> playList;
        private  Vector<LoginInfo> LoginList;
        private JList search_ls;
        private JList play_ls ;
        private JList tmplist;
        private int flag = 0;
        private JButton play_button;
        private JButton exit_button;
        private JButton rc_button;
        private JButton withPlay;
        private JList UserList;
        private Vector <JCheckBox> usercheck;
        private JButton openChat;
        private JButton addChatList;
        private JList chatList;
        private JScrollPane scrollPane_3;
        private Vector<String> chatMember = new Vector<String>(); 
        private DefaultListModel listModel = new DefaultListModel();
        private DefaultListModel listModel_2 = new DefaultListModel();
        private ImageIcon icon ;
        int chatListSize =0;
        public  VideoApplication(PlayerController playerCT)  
        {  //  생성자
                super("Video  Application  player");
                this.playerCT = playerCT;
                playList=new Vector<String>();
                setTitle("Water Melon");
                
                container  =  getContentPane();
                getContentPane().setLayout(null);
                
                search = new JButton();
                search.setText("Search");
                search.setForeground(Color.BLACK);
                search.setBackground(Color.WHITE);
                search.addActionListener(new ActionListener() 
                {
                	public void actionPerformed(ActionEvent e) 
                	{
                		
							playerCT.SearchList();
                	}
                });
                search.setBounds(16, 6, 117, 29);
                getContentPane().add(search);
                
                JScrollPane scrollPane_1 = new JScrollPane();
                scrollPane_1.setBounds(0, 199, 400, 94);
                getContentPane().add(scrollPane_1);
							
               
                
                search_ls = new JList();
                scrollPane_1.setViewportView(search_ls);
                search_ls.setBackground(new Color(204, 255, 255));
                search_ls.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
               
                JScrollPane scrollPane = new JScrollPane();
                scrollPane.setBounds(0, 80, 400, 94);
                getContentPane().add(scrollPane);
                play_ls = new JList();
                play_ls.setBackground(new Color(204, 255, 204));
                scrollPane.setViewportView(play_ls);
                play_ls.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
              
                
                play_button = new JButton("play");
                play_button.setSelectedIcon(null);
                play_button.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
                		String mode = play_button.getText();
                		
                		if(mode.equals("stop"))
                		{
                			playerCT.istStop();
                			play_button.setText("play");
                		}
                		else
                		{
                			playerCT.istPlay();
                			play_button.setText("stop");
                		}
                	}
                });
                play_button.setBounds(145, 6, 117, 29);
                getContentPane().add(play_button);
                
                exit_button = new JButton("exit");
                exit_button.setBounds(217, 39, 117, 29);
                getContentPane().add(exit_button);
                
                rc_button = new JButton();
                rc_button.setText("recommend");
                rc_button.addActionListener(new ActionListener() 
                {
                	public void actionPerformed(ActionEvent e)
                	{
                		playerCT.rcList();
                	}
                });
                
                rc_button.setBounds(274, 6, 117, 29);
                getContentPane().add(rc_button);
                
                withPlay = new JButton();
                withPlay.setText("Search User");
                withPlay.setBounds(82, 39, 117, 29);
                getContentPane().add(withPlay);
                
                JScrollPane scrollPane_2 = new JScrollPane();
                scrollPane_2.setBounds(0, 318, 184, 78);
                getContentPane().add(scrollPane_2);
                
                UserList = new JList(listModel_2);
                UserList.setForeground(Color.WHITE);
                UserList.setBackground(new Color(204, 204, 255));
                UserList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                
              
                scrollPane_2.setViewportView(UserList);
                
                openChat = new JButton("채팅열기");
                openChat.setEnabled(false);
                openChat.addActionListener(new ActionListener() 
                {
                	public void actionPerformed(ActionEvent e) 
                    {
                		playerCT.clickChatbutton(chatMember);
                		System.out.println("chatMemberSize:"+chatMember.size());
                		listModel.removeAllElements();
                		chatListSize=0;
                		System.out.println("UserList length :"+LoginList.size());
                		chatMember = new Vector<String>();
                    }
                       
                });
                openChat.setBounds(257, 404, 117, 29);
                getContentPane().add(openChat);
                
                addChatList = new JButton("목록추가");
                addChatList.setEnabled(false);
                addChatList.addActionListener(new ActionListener()
                {
                	int flag=0;
                	public void actionPerformed(ActionEvent e) 
                	{
                		
						Object sellect = UserList.getSelectedValue();
						System.out.println("add sellec:"+sellect);
						if(chatMember!=null)
						{
                			for(int i=0 ; i<chatMember.size();i++)
                			{
                				if(chatMember.get(i).equals((String)sellect))
                				{
                					flag=1;
                				}
                				
                			}
                		}
						if(flag == 0)
						{
							listModel.addElement((String)sellect); 
	                		chatMember.add((String)sellect);
	                		openChat.setEnabled(true);
	                		chatListSize++;
						}
                	}
                });
                
                addChatList.setBounds(32, 408, 117, 29);
                getContentPane().add(addChatList);
                
                scrollPane_3 = new JScrollPane();
                scrollPane_3.setBounds(232, 320, 168, 78);
                getContentPane().add(scrollPane_3);
                
                chatList = new JList(listModel);
                scrollPane_3.setViewportView(chatList);
                chatList.setForeground(Color.WHITE);
                chatList.setBackground(new Color(204, 204, 255));
                
                JPanel panel = new JPanel(){
                		   public void paintComponent(Graphics g) {
                               //  Approach 1: Dispaly image at at full size
                              
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
                panel.setBackground(Color.BLACK);
                panel.setBounds(0, 0, 400, 466);
                getContentPane().add(panel);
                
                
                
                withPlay.addActionListener(new ActionListener()
                {
                	public void actionPerformed(ActionEvent e) 
                	{
                		LoginList=playerCT.getUserInfo();
                    	System.out.println("action length = "+LoginList.size());
                		setFindUserResult(LoginList);
                		addChatList.setEnabled(true);
                	}
                });
                
                
                
                exit_button.addActionListener(new ActionListener() {
                	public void actionPerformed(ActionEvent e) {
            			playerCT.closeWindow();
            			playerCT.exit();
            			System.exit(0);
                	}
                });
                
                JListHandler playHandle = new JListHandler();
                play_ls.addListSelectionListener(playHandle);
                
                JListHandler searchHandle = new JListHandler();
                search_ls.addListSelectionListener(searchHandle);
                
                //  메뉴의  Open  File  클릭
                
                //  파일  열기  getMediaComponents()  
                Manager.setHint(Manager.LIGHTWEIGHT_RENDERER,  Boolean.TRUE);
                this.setSize(400,  488);
                this.setLocation(400,  400);
                this.setVisible(true);
                
        }
        

        public void setFindUserResult(Vector <LoginInfo> LoginInfo)
        {
        	
        	searchList = new Vector <String>();
        	int length = LoginInfo.size();
        	System.out.println("length = "+length);
        	for(int i=0;i<length; i++)
        	{
        		searchList.add(LoginInfo.elementAt(i).getID());
        	}
        	UserList.setListData(searchList);
        }
        
        public void setSearchResult(Vector <String> searchList)
        {
        	this.searchList=searchList;
        	this.searchList.remove(0);
        	this.searchList.remove(this.searchList.remove(this.searchList.size()-1));
        	System.out.println(this.searchList.get(0));
        	search_ls.setListData(this.searchList);
        	
        }
        
        public void setrcResult(Vector <String> searchList)
        {
        		
        	this.searchList=searchList;
        	System.out.println(this.searchList.get(0));
        	this.searchList.remove(searchList.size()-1);
        	search_ls.setListData(this.searchList);
        }
        
        private class JListHandler implements ListSelectionListener
        {
        	int flag=0,mode=0;
	        public void valueChanged(ListSelectionEvent event)
	        {
	        	if(event.getSource().equals(play_ls))
	        	{
	        		if(flag==0)
	        		{
	        			mode =1;
	        			flag++;
	        		}
	        		else if(flag ==1 && mode==1)
	        		{
	        			
	        			String fileName = (String)play_ls.getSelectedValue();
						try
						{
							play_button.setText("stop");
							playerCT.play(fileName);
						} 
						catch (IOException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
	        			mode=0;
	        			flag=0;
	        		}
	        	}
	        	else if(event.getSource().equals(search_ls))
	        	{
	        		if(flag==0)
	        		{
	        			mode=2;
	        			flag++;
	        		}
	        		else if(flag ==1 && mode==2)
	        		{
	        			String value = (String) search_ls.getSelectedValue();
	        			playList.add(value);
	        			play_ls.setListData(playList);
	        			mode=0;
	        			flag=0;
	        		}
	        	}
	        }
	        
        }
}
