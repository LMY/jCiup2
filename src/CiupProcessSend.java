
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



public class CiupProcessSend extends CiupProcessNetwork
{
	private File file;
	
	private String host;
	private int port;



	public CiupProcessSend(ProcessPanel processpanel, String host, int port, File file)
	{
		super(processpanel, null);
		this.file = file;
		this.host = host;
		this.port = port;
		
		filelabel.setText("File: "+file.getName());
		statuslabel.setText("Status: waiting");
		sizelabel.setText("Size: "+Utility.size2string(file.length()));
	}



	public CiupProcessSend(ProcessPanel processpanel, Socket sock, File file)
	{
		super(processpanel, sock);
		this.file = file;
		this.host = "";
		this.port = 0;
		
		filelabel.setText("File: "+file.getName());
		statuslabel.setText("Status: waiting");
		sizelabel.setText("Size: "+Utility.size2string(file.length()));
	}



	protected void initialize_panel()
	{
		super.initialize_panel();
		
		lowerpanel.add(progressbar, BorderLayout.CENTER);
		lowerpanel.add(closebutton, BorderLayout.EAST);
	}



	public void run()
	{
		if (sock == null) {
			statuslabel.setText("Status: connecting to "+host+" ("+port+")");
			try {
				sock = new Socket(host, port);
				open_socket();
			}
			catch (IOException e) {
				statuslabel.setText("Status: can't connect to "+host+" ("+port+")");
				error_gui();
				close_socket();
				return;
			}
		}
		
		
		long filesize;
		String remote_nick = "";
		String ciup_line = "";
		int offset = 0;
		
		try {
			// Send HI
			sendString("CIUP "+Config.getInstance().getNick()+"\n");

			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			
			// Read the HI (and get the nick)
			ciup_line = reader.readLine();
			remote_nick = ciup_line.substring(5);
			
			if (remote_nick.equals("") || !ciup_line.startsWith("CIUP ")) {
				statuslabel.setText("Error: User did not register.");
				close_socket();
				error_gui();
				return;
			}

				

			filesize = file.length();
			String filename = file.getName();
			sendString("SEND "+filesize+" "+filename+"\n");

			// now, read ACCEPT or REFUSE
			ciup_line = reader.readLine();

			if (!ciup_line.startsWith("ACCEPT")) {
				statuslabel.setText("Status: Refused.");
				close_socket();
				error_gui();
				return;
			}
			else {
				yStringTokenizer tokenizer = new yStringTokenizer(ciup_line);
				tokenizer.nextToken();
				try {
					offset = Integer.parseInt(tokenizer.getTheRest());
				}
				catch (NumberFormatException e) { offset = 0; }
			}
		}
		catch (IOException e) {
			statuslabel.setText("Status: I/O Error.");
			error_gui();
			close_socket();
			return;
		}



		state = 1;
		statuslabel.setText("Status: Sending to "+remote_nick+".");

		
		FileInputStream fis;

		try {
			// Ok, open the file and seek at the right resume point
			fis = new FileInputStream(file);
			
			if (offset > 0) {
				fis.skip(offset);
				progressbar.setValue((int) (100*(double) offset/filesize));
				System.out.println("starting from byte: "+offset);
			}
		}
		catch (IOException e) {
			statuslabel.setText("Status: Error opening file. aborted.");
			error_gui();
			close_socket();
			return;
		}			


		
		// Now, send the data.
		try {
			byte[] b = new byte[32*1024];
			int dim;
			int total=0;
			long t2, t1 = System.currentTimeMillis();
			long tinit = t1;
			

					
			while((dim=fis.read(b)) != -1) {
				out.write(b, 0, dim);

				total += dim;
				t2 = System.currentTimeMillis();
				if (t1 != t2) {
					speedlabel.setText("Speed: "+Utility.speed2String(dim, t2-t1));
					etalabel.setText("Eta: "+Utility.eta2String(filesize, offset, total, t2-tinit));
					t1 = t2;
				}
				
				progressbar.setValue((int) (100*(double) (offset+total)/filesize));
				percentagelabel.setText("%: "+Utility.transfer2String((offset+total), filesize));
			}
	
			fis.close();
			long tend = System.currentTimeMillis();
			speedlabel.setText("Speed: "+Utility.speed2String(total, tend-tinit));
			etalabel.setText("Eta: -");



			// Everything done. Close, state=2
			close_socket();

			if (offset+total == filesize) {
				statuslabel.setText("Status: sent to "+remote_nick+".");
			
				progressbar.setValue(100);
				percentagelabel.setText("%: 100%");
			}
			else
				statuslabel.setText("Status: connection terminated.");

			closebutton.setText("Close");
			state = 2;			
		}
		catch (IOException e) {
			statuslabel.setText("Status: connection terminated.");
			close_socket();
			return;
		}
		catch (NullPointerException e) {
			statuslabel.setText("Status: connection terminated.");
			close_socket();
			return;
		}



		if (Config.getInstance().getAutocloseSend())
			close_connection();
	}
}
