
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



public class CiupProcessConnection extends CiupProcessNetwork
{
	private JButton acceptbutton;
	private JButton resumebutton;
	private JButton refusebutton;
	
	private long offset;



	public CiupProcessConnection(ProcessPanel processpanel, Socket sock)
	{
		super(processpanel, sock);
		
		offset = 0;
	}



	protected void initialize_panel()
	{
		super.initialize_panel();
		
		acceptbutton = new JButton("Accept");
		acceptbutton.addActionListener(this);
		resumebutton = new JButton("Resume");
		resumebutton.addActionListener(this);
		refusebutton = new JButton("Refuse");
		refusebutton.addActionListener(this);


		lowerpanel.add(acceptbutton, BorderLayout.WEST);
		lowerpanel.add(refusebutton, BorderLayout.EAST);
	}
	
	
	
	
	public void run()
	{
		statuslabel.setText("Status: running");
		
		String filename;
		int filesize;
		String remote_nick = "";
		File file = null;
		FileOutputStream fos = null;

		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			
			// Read the HI (and get the nick)
			String ciup_line = reader.readLine();
			remote_nick = ciup_line.substring(5);
			
			if (remote_nick.equals("") || !ciup_line.startsWith("CIUP ")) {
				log("Error: User did not register.");
				close_socket();
				error_gui();
				return;
			}
			
			// Reply with HI			
			sendString("CIUP "+Config.getInstance().getNick()+"\n");


			// Now, it will follow "SEND $filesize $filename"
			ciup_line = reader.readLine();
			yStringTokenizer tokenizer = new yStringTokenizer(ciup_line);
			
			String command = tokenizer.nextToken();
			String sizestring = tokenizer.nextToken();
			
			try {
				filesize = Integer.parseInt(sizestring);
			}
			catch (NumberFormatException e) {
				filesize = -1;
			}
			
			if (filesize <= 0) {
				log("Error: Invalid filesize.");
				close_socket();
				error_gui();
				return;				
			}
			
			filename = tokenizer.getTheRest();
			
			if (!command.equalsIgnoreCase("send")) {
				log("Error: Invalid command from "+remote_nick+".");
				close_connection();
				return;
			}
			else if (filename.contains("/")) {
				log("Error: Invalid filename from "+remote_nick+".");
				close_connection();
				return;
			}
			else {
				filelabel.setText("File: "+filename);
				sizelabel.setText("Size: "+Utility.size2string(sizestring));
				
				statuslabel.setText("Status: connected to "+remote_nick+".");
				
				
				// Create the file in the directory (a lot of if-then-else, just to support individual incoming dirs and stuff
				if (Config.getInstance().getDirForeachUser()) {
					File dir = new File(Config.getInstance().getIncomingDirectory()+"/"+remote_nick);
					
					if (!dir.exists()) {
						if (!dir.mkdirs())
							file = new File(Config.getInstance().getIncomingDirectory()+"/incomplete-"+filename);
						else
							file = new File(Config.getInstance().getIncomingDirectory()+"/"+remote_nick+"/incomplete-"+filename);
					}
					else
						file = new File(Config.getInstance().getIncomingDirectory()+"/"+remote_nick+"/incomplete-"+filename);
				}
				else					
					file = new File(Config.getInstance().getIncomingDirectory()+"/incomplete-"+filename);
				// till here. :P
				
				
				if (file.exists() && filesize > file.length()) {
					offset = file.length();		// resume.
					
					lowerpanel.add(resumebutton, BorderLayout.CENTER);
					acceptbutton.setText("Overwrite");
					lowerpanel.repaint();
				}
			}
		}
		catch (IOException e) {
			statuslabel.setText("Status: I/O Error.");
			error_gui();
			return;
		}
		
		
		// Wait for user to press accept.
		// If autoaccept is activated, accept but only if we don't have to resume.
		if (Config.getInstance().getAutoAccept()/* && offset == 0*/)
			accept(offset);
		else
			while (state == 0)
				try {
					sleep(250);
				}
				catch (InterruptedException e) {}

	
		// Create the file
		try {
			// state=1.
			statuslabel.setText("Status: Receiving from "+remote_nick+".");
				
			fos = new FileOutputStream(file, (offset > 0  ?  true  :  false));
			if (offset > 0)
				progressbar.setValue((int) (100*(double) offset/filesize));
		}
		catch (IOException e) {
			statuslabel.setText("Status: Error creating file. aborted.");
			
			if (fos != null) { try { fos.close(); } catch (IOException ee) {} fos = null; }

			close_socket();
			error_gui();
			return;
		}


		// And receive the data
		try {
			int dim;
			int total=0;
			long t2, t1 = System.currentTimeMillis();
			long tinit = t1;
			byte[] b = new byte[32*1024];

			while((dim=in.read(b)) != -1) {
				fos.write(b, 0, dim);
				fos.flush();

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

			
			fos.close();
			fos = null;
			long tend = System.currentTimeMillis();
			speedlabel.setText("Speed: "+Utility.speed2String(total, tend-tinit));
			etalabel.setText("Eta: -");


			// Everything done. Rename the incomplete file and close. state=2
			close_socket();
			
			if (offset+total == filesize) {
				File file2 = new File(file.getPath().replaceFirst("incomplete-", ""));
				
				String first_piece;
				String last_piece;

				int duepunti = file2.getPath().lastIndexOf(".");
				
				if (duepunti < 0  ||  duepunti < file2.getPath().lastIndexOf("/")  ||  duepunti < file2.getPath().lastIndexOf("\\")) {
					first_piece = file2.getPath()+" (";
					last_piece = ")";
				}
				else {
					first_piece = file2.getPath().substring(0, duepunti)+" (";
					last_piece = ")"+file2.getPath().substring(duepunti, file2.getPath().length());
				}


				for (int i=1; file2.exists(); i++)
					file2 = new File(first_piece+i+last_piece);

				file.renameTo(file2);
				
				
				statuslabel.setText("Status: Received from "+remote_nick+".");
				progressbar.setValue(100);
				percentagelabel.setText("%: 100%");
			}
			else
				statuslabel.setText("Status: Connection terminated.");
			
			closebutton.setText("Close");
			state = 2;			
		}
		catch (IOException e) {
			statuslabel.setText("Status: Connection terminated.");
			
			if (fos != null) { try { fos.close(); } catch (IOException ee) {} fos = null; }

			close_socket();
			return;
		}
		catch (NullPointerException e) {
			statuslabel.setText("Status: Connection terminated.");

			if (fos != null) { try { fos.close(); } catch (IOException ee) {} fos = null; }

			close_socket();
			return;
		}
		
		if (Config.getInstance().getAutocloseRecv())
			close_connection();
	}


	private void accept(long sel_offset)
	{
		try {
			sendString(sel_offset == 0  ? "ACCEPT\n"  :  "ACCEPT "+sel_offset+"\n");

			offset = sel_offset;

			lowerpanel.removeAll();
			lowerpanel.setLayout(new BorderLayout());
			lowerpanel.add(progressbar, BorderLayout.CENTER);
			closebutton.setText("Cancel");
			lowerpanel.add(closebutton, BorderLayout.EAST);
			lowerpanel.repaint();

			state = 1;
		}
		catch (IOException uatta) { statuslabel.setText("Status: error"); }				
	}



	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource().equals(acceptbutton))
			accept(0);
		else if (e.getSource().equals(refusebutton)) {
			try {
				sendString("REFUSE\n");
			}
			catch (IOException uatta) {}
			close_connection();
		}
		else if (e.getSource().equals(resumebutton)) {
			accept(offset);
		}
		else
			super.actionPerformed(e);
	}
}
