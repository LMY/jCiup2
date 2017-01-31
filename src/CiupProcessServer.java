
/*
	Copyright (C) 2007 Miro Salvagni.
	
	This file is part of jCiup.
	
	jCiup is free software; you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation; either version 2 of the License, or
	(at your option) any later version.
	
	jCiup is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with jCiup; if not, write to the Free Software
	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.net.*;
import java.io.*;



public class CiupProcessServer extends CiupProcess implements ActionListener
{
	private int port;
	
	private boolean running;
	private ServerSocket sock;
	
	private JPanel panel;
	private JLabel status;
	private JButton toggle;


	
	public CiupProcessServer(ProcessPanel processpanel, int port)
	{
		super(processpanel);
		
		this.port = port;
		sock = null;
		
		initialize_panel();
		
		start();
	}


	
	public void run()
	{
		open_server();
		
		Socket client;
		
		while (true) {
			try {
				client = sock.accept();
			
				if (client != null)
					processpanel.launchHandler(client);
			}
			catch (IOException e) {}
			catch (NullPointerException e) {
				while (sock == null)
					try {
						Thread.sleep(1000);
					}
					catch (InterruptedException e2) {}
			}
		}
	}



	public void change_port(int newport)
	{
		close_server();
		port = newport;
		open_server();
	}



	public void open_server()
	{
		try {
			sock = new ServerSocket(port);
			running = true;
			status.setText("Status: running ("+Utility.getMyIp()+":"+port+")");
			toggle.setText("Close");
		}
		catch (IOException e) { 
			running = false;
			sock = null;
			status.setText("Status: closed");
			toggle.setText("Open");			
		}
	}
	
	
	public void close_server()
	{
		running = false;
		status.setText("Status: closed");
		toggle.setText("Open");			
		
		if (sock != null)
			try {
				sock.close();
				sock = null;
			}
			catch (IOException e) { status.setText("Status: error"); }				
	}
	
	
	public JPanel getPanel()	{ return panel; }



	public void initialize_panel()
	{
		panel = new JPanel();
		panel.setLayout(new BorderLayout());

		panel.setMinimumSize(new Dimension(0, 24));
		panel.setMaximumSize(new Dimension(Short.MAX_VALUE, 24));		
		
		status = new JLabel();
		toggle = new JButton();
		toggle.addActionListener(this);
		
		panel.add(status, BorderLayout.CENTER);
		panel.add(toggle, BorderLayout.EAST);
	}
	
	
	
	public void actionPerformed(ActionEvent e)
	{
		if (running)
			close_server();
		else
			open_server();
	}
}
