
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



public abstract class CiupProcessNetwork extends CiupProcess implements ActionListener
{
	protected int state;
	// state=0 -> accept | refuse
	// state=1 -> progressbar
	// state=2 -> closebutton

	protected JPanel panel;

	protected JLabel filelabel;
	protected JLabel statuslabel;
	protected JLabel sizelabel;
	protected JLabel speedlabel;
	protected JLabel percentagelabel;
	protected JLabel etalabel;
	protected JProgressBar progressbar;
	protected JButton closebutton;
	protected JPanel lowerpanel;	
	
	protected Socket sock;
	protected DataInputStream in;
	protected DataOutputStream out;





	public CiupProcessNetwork(ProcessPanel processpanel, Socket sock)
	{
		super(processpanel);
		
		this.sock = sock;
		this.in = null;
		this.out = null;
		
		panel = new JPanel();
		panel.setMinimumSize(new Dimension(0, 60));
		panel.setMaximumSize(new Dimension(Short.MAX_VALUE, 60));
		initialize_panel();

		if (sock != null)
			try {
				open_socket();
			}
			catch (IOException e) {
				log("ERROR");
			}
	}



	public JPanel getPanel()	{ return panel; }



	protected void open_socket() throws IOException
	{
		if (sock == null)
			return;
		
		in = new DataInputStream(sock.getInputStream());
		out = new DataOutputStream(sock.getOutputStream());
	}


	protected void close_socket()
	{
		try {
			if (in != null) {
				in.close();
				in = null;
			}
			if (out != null) {
				out.close();
				out = null;
			}
			if (sock != null) {
				sock.close();
				sock = null;
			}
		}
		catch (IOException uatta) {}
	}




	protected void close_connection()
	{
		log("closing");
		close_socket();
		processpanel.stop(this);
	}



	protected void log(String text)
	{
		statuslabel.setText("Status: "+text);
	}
	
	
	
	protected void initialize_panel()
	{
		filelabel = new JLabel("File: -");
		statuslabel = new JLabel("Status: -");
		sizelabel = new JLabel("Size: -");
		speedlabel = new JLabel("Speed: -");
		percentagelabel = new JLabel("%: -");
		etalabel = new JLabel("Eta: -");
		
		progressbar = new JProgressBar();
		progressbar.setMinimum(0);
		progressbar.setMaximum(100);

		closebutton = new JButton("Cancel");
		closebutton.addActionListener(this);

		panel.setLayout(new GridLayout(3,1));

		JPanel p1 = new JPanel();
		JPanel p2 = new JPanel();
		JPanel p3 = new JPanel();
		JPanel p4 = new JPanel();
		lowerpanel = new JPanel();
		p1.setLayout(new BorderLayout());
		p2.setLayout(new BorderLayout());
		p3.setLayout(new GridLayout(1,2));
		p4.setLayout(new GridLayout(1,2));
		lowerpanel.setLayout(new BorderLayout());
		
		p3.add(percentagelabel);
		p3.add(sizelabel);

		p4.add(etalabel);
		p4.add(speedlabel);
		
		
		p1.add(filelabel, BorderLayout.CENTER);
		p1.add(p3, BorderLayout.EAST);

		p2.add(statuslabel, BorderLayout.CENTER);
		p2.add(p4, BorderLayout.EAST);
		
		panel.add(p1);
		panel.add(p2);
		panel.add(lowerpanel);
	}



	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource().equals(closebutton))
			close_connection();
	}
	
	
	
	protected void sendString(String s) throws IOException
	{
		if (out == null)
			return;
		
		byte[] b = s.getBytes();
		out.write(b, 0, b.length);
	}
	
	
	
	protected void error_gui()
	{
		lowerpanel.removeAll();
		closebutton.setText("Close");
		lowerpanel.add(closebutton);
		lowerpanel.repaint();
	}
}
