
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
import java.io.*;



public class ConfigPanel extends JPanel implements ActionListener
{
	private JTextField nickfield;
	private JTextField incomingfield;
	private JCheckBox autoacceptcheckbox;
	private JCheckBox autoclosendcheckbox;
	private JCheckBox autoclorecvcheckbox;
	private JCheckBox dirforeachusercheckbox;
	private JCheckBox startminimizedcheckbox;
	private JTextField serverportfield;
		
	private JButton save;
	private JButton reload;
	private JButton select;
	
	private ProcessPanel processpanel;



	public ConfigPanel(ProcessPanel processpanel)
	{
		super();
		
		this.processpanel = processpanel;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel p1 = new JPanel();
		JPanel p2 = new JPanel();
		JPanel p3 = new JPanel();
		JPanel p4 = new JPanel();
		
		p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
		p2.setLayout(new BoxLayout(p2, BoxLayout.X_AXIS));
		p3.setLayout(new BoxLayout(p3, BoxLayout.X_AXIS));
		p4.setLayout(new BoxLayout(p4, BoxLayout.X_AXIS));


		nickfield = new JTextField();
		incomingfield = new JTextField();
		autoacceptcheckbox = new JCheckBox("Auto-accept incoming files");
		autoclosendcheckbox = new JCheckBox("Auto-close complete sends");
		autoclorecvcheckbox = new JCheckBox("Auto-close complete recvs");
		dirforeachusercheckbox = new JCheckBox("Incoming dir for each user");
		startminimizedcheckbox = new JCheckBox("Start minimized");
		serverportfield = new JTextField();


		save = new JButton("Save");
		reload = new JButton("Reload");
		select = new JButton("Select");
		save.addActionListener(this);
		reload.addActionListener(this);
		select.addActionListener(this);



		reloadFromConf();

		p1.add(new JLabel("Nick: "));
		p1.add(nickfield);
		p1.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));


		p2.add(new JLabel("Incoming Directory: "));
		p2.add(incomingfield);
		p2.add(select);

		p3.add(new JLabel("Server Port: "));
		p3.add(serverportfield);
		
		p4.add(Box.createHorizontalGlue());
		p4.add(save);
		p4.add(Box.createRigidArea(new Dimension(45,0)));
		p4.add(reload);
		p4.add(Box.createHorizontalGlue());


		p1.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
		p2.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
		p3.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));

		
		add(Box.createRigidArea(new Dimension(0,5)));
		add(p1);
		add(Box.createRigidArea(new Dimension(0,5)));
		add(p2);
		add(Box.createRigidArea(new Dimension(0,5)));
		add(p3);
		add(Box.createRigidArea(new Dimension(0,5)));
		add(autoacceptcheckbox);
		add(autoclosendcheckbox);
		add(autoclorecvcheckbox);
		add(dirforeachusercheckbox);
		add(startminimizedcheckbox);
		
		add(Box.createVerticalGlue());
		add(p4);
	}



	public void reloadFromConf()
	{
		Config conf = Config.getInstance();

		nickfield.setText(conf.getNick());
		incomingfield.setText(conf.getIncomingDirectory());
		autoacceptcheckbox.setSelected(conf.getAutoAccept());
		serverportfield.setText(""+conf.getServerPort());
		
		autoclosendcheckbox.setSelected(conf.getAutocloseSend());
		autoclorecvcheckbox.setSelected(conf.getAutocloseRecv());
		dirforeachusercheckbox.setSelected(conf.getDirForeachUser());
		startminimizedcheckbox.setSelected(conf.getStartMinimized());
	}



	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource().equals(save)) {
			Config conf = Config.getInstance();
			
			conf.setIncomingDirectory(incomingfield.getText());
			conf.setNick(nickfield.getText());
			conf.setAutoAccept(autoacceptcheckbox.isSelected());
			
			conf.setAutocloseSend(autoclosendcheckbox.isSelected());
			conf.setAutocloseRecv(autoclorecvcheckbox.isSelected());
			conf.setDirForeachUser(dirforeachusercheckbox.isSelected());
			conf.setStartMinimized(startminimizedcheckbox.isSelected());
			
			try {
				int server_port = Integer.parseInt(serverportfield.getText());

				if (server_port != conf.getServerPort()) {
					conf.setServerPort(server_port);
					processpanel.updateServerport();
				}
			}
			catch (NumberFormatException eccheccazzo) {}

			conf.save();
		}
		else if (e.getSource().equals(reload))
			reloadFromConf();
		else if (e.getSource().equals(select)) {
			JFileChooser chooser = new JFileChooser(".");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			
			if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				File selecteddir = chooser.getSelectedFile();
				
				incomingfield.setText(selecteddir.getPath());
			}
		}
	}	
}
