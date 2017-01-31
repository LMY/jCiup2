
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
import javax.swing.border.*;
import java.net.*;
import java.io.*;



public class SendPanel extends JPanel implements ActionListener
{
	private ProcessPanel processpanel;

	private JTextField ipfield;
	private JLabel sizelabel;
	private JLabel namelabel;
	private JButton searchbutton;
	private JButton sendbutton;
	
	private File selectedfile;


	
	public SendPanel(ProcessPanel processpanel)
	{
		super();
		
		setLayout(new BorderLayout());
		
		selectedfile = null;
		this.processpanel = processpanel;

		ipfield = new JTextField("");
		
		sendbutton = new JButton("Send");
		searchbutton = new JButton("Select File");
		sendbutton.addActionListener(this);
		searchbutton.addActionListener(this);


        JPanel p2 = new JPanel();
        p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
        JPanel p4 = new JPanel();
        p4.setLayout(new BorderLayout());


        JPanel ippanel = new JPanel();
		Border titledBdr = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Address ");
		ippanel.setBorder(titledBdr);
		ippanel.setLayout(new BorderLayout());
		ippanel.add(ipfield, BorderLayout.CENTER);
	
       	add(p2, BorderLayout.CENTER);
       	add(sendbutton, BorderLayout.EAST);	
	
       	JPanel infopanel = new JPanel();
       	infopanel.setLayout(new BoxLayout(infopanel, BoxLayout.Y_AXIS));
       	namelabel = new JLabel("Name: -");
       	sizelabel = new JLabel("Size: -");
       	infopanel.add(namelabel);
       	infopanel.add(sizelabel);	


       	Border etchedBdr2 = BorderFactory.createEtchedBorder();
		Border titledBdr2 = BorderFactory.createTitledBorder(etchedBdr2, "File ");
		Border emptyBdr2  = BorderFactory.createEmptyBorder(3,3,3,3);
		Border compoundBdr2=BorderFactory.createCompoundBorder(titledBdr2, emptyBdr2);
		p4.setBorder(compoundBdr2);
       	p4.add(infopanel, BorderLayout.CENTER);
       	p4.add(searchbutton, BorderLayout.EAST);
       	
       	p2.add(ippanel);
       	p2.add(p4);
	}



	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource().equals(searchbutton)) {
			JFileChooser chooser = new JFileChooser(new File("."));
			int returnVal = chooser.showOpenDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				selectedfile = chooser.getSelectedFile();
				
				namelabel.setText("Name: "+selectedfile.getName());
				sizelabel.setText("Size: "+Utility.size2string(selectedfile.length()));
			}
		}
		else if (e.getSource().equals(sendbutton)) {
			if (selectedfile == null) {
				JOptionPane.showMessageDialog(null, "E scegliere un file da mandare prima?");
				searchbutton.requestFocusInWindow();
				return;
			}
			if (ipfield.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "E mettere un indirizzo?");
				ipfield.requestFocusInWindow();
				return;
			}
			
			try {
				String Address = ipfield.getText();
				
				if (Address.startsWith("ciup://"))
					Address = Address.substring(7);
				
				
				int duepunti = Address.indexOf(":");
				int port = 7777;
				
				if (duepunti > 0) {
					port = Integer.parseInt(Address.substring(duepunti+1, Address.length()));
					Address = Address.substring(0, duepunti);
				}

				processpanel.launchSender(Address, port, selectedfile);
			}
			catch (NumberFormatException eccheccazzo) {
				JOptionPane.showMessageDialog(null, "Invalid port information, please correct.");
				ipfield.requestFocusInWindow();
			}
		}
	}	
}
