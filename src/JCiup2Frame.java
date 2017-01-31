
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
import javax.swing.*;

import snoozesoft.systray4j.*;



public class JCiup2Frame extends JFrame implements SysTrayMenuListener
{
	private ProcessPanel processpanel;
	private SendPanel sendpanel;
	private ConfigPanel configpanel;
	
	private JTabbedPane tabs;
	
	private SysTrayMenu systraymenu;


    public JCiup2Frame()
    {
		super("JCiup");
        setSize(new Dimension(420, 420));
        center();

     	// Look and Feel
     	//setDefaultCloseOperation(EXIT_ON_CLOSE);
        try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception exception) {
			System.out.println("Invalid look and feel.");
		}


		ImageIcon icon = new ImageIcon("Ciup.gif");
		if (icon != null)
			setIconImage(icon.getImage());
		
		

        Container content = this.getContentPane();
		content.setLayout(new BorderLayout());

		processpanel = new ProcessPanel();
		sendpanel = new SendPanel(processpanel);
		configpanel = new ConfigPanel(processpanel);

		JScrollPane listScrollerprocesspanel = new JScrollPane(processpanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        listScrollerprocesspanel.setWheelScrollingEnabled(true);


		JPanel connectionspanel = new JPanel();
		connectionspanel.setLayout(new BorderLayout());
		connectionspanel.add(sendpanel, BorderLayout.NORTH);
		connectionspanel.add(listScrollerprocesspanel, BorderLayout.CENTER);


		// Add Tabs
		tabs = new JTabbedPane(JTabbedPane.TOP);
		tabs.addTab("Transfers", connectionspanel);
		tabs.addTab("Config", configpanel);
		tabs.addTab("About", new AboutPanel());

        content.add(tabs, BorderLayout.CENTER);
        
        if (!Config.getInstance().getStartMinimized())
        	setVisible(true);
        
        // systray menu
        createMenu();
    }

    



	public void center()
	{
		Dimension screen_dim = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension window_dim = getSize();
		
		int posX = (screen_dim.width - window_dim.width) / 2;
		int posY = (screen_dim.height- window_dim.height) / 2;
		
		setLocation(posX, posY);
	}
	
	
	
	
	//
	// systray4java functions
	//
	
	
	public void createMenu()
	{
		final SysTrayMenuIcon tryaicon =  new SysTrayMenuIcon("Ciup");
		tryaicon.addSysTrayMenuListener(this);
		systraymenu = new SysTrayMenu(tryaicon);
		
        SysTrayMenuItem itemExit = new SysTrayMenuItem("Exit", "exit");
        itemExit.addSysTrayMenuListener(this);

        SysTrayMenuItem itemShow = new SysTrayMenuItem("Show", "show");
        itemShow.addSysTrayMenuListener(this);
                
        systraymenu.addItem(itemExit);
		systraymenu.addSeparator();
        systraymenu.addItem(itemShow);
	}
	
	
	public void iconLeftDoubleClicked(SysTrayMenuEvent e)
	{
		if (isVisible())
			setVisible(false);
		else
			setVisible(true);
	}
	
	public void iconLeftClicked(SysTrayMenuEvent e)
	{}
	
	public void menuItemSelected( SysTrayMenuEvent e )
    {
        if (e.getActionCommand().equals("exit"))
        	System.exit(0);
        if (e.getActionCommand().equals("show"))
        	setVisible(true);
    }
}
