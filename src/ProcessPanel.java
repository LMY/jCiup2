
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



public class ProcessPanel extends JPanel
{
	private CiupProcessServer server;
	
	
	public ProcessPanel()
	{
		super();
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		server = null;
		startServer();
	}
	
	
	public void stop(CiupProcess process)
	{
		remove(process.getPanel());
		repaint();
	}
	
	
	public void launchHandler(Socket sock)
	{
		CiupProcessConnection client = new CiupProcessConnection(this, sock);
		client.start();
		add(client.getPanel());
		repaint();
	}
	
	
	public void startServer()
	{
		server = new CiupProcessServer(this, Config.getInstance().getServerPort());
		add(server.getPanel());
	}
	
	
	public void updateServerport()
	{
		server.change_port(Config.getInstance().getServerPort());
	}


	public void launchSender(String address, int port, File file)
	{
		CiupProcessSend send = new CiupProcessSend(this, address, port, file);
		send.start();
		add(send.getPanel());
		repaint();
	}
}
