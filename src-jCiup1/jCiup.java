
package jCiup;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;



class jCiup extends JFrame
{
	public static void main(String[] args) 
	{
		new jCiup();
	}

	
	
	public static final long serialVersionUID = 24362462L;

	public static final int XDim = 480;
	public static final int YDim = 300;






	public jCiup()
	{
		super("jCiup");


		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception exception) {
			System.out.println("invalid look and feel.");
		}

		Container content = this.getContentPane();
		content.setLayout(new BorderLayout());

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int posX = (dim.width - XDim) / 2;
		int posY = (dim.height- YDim) / 2;

		setSize(XDim, YDim);
		setPreferredSize(new Dimension(XDim, YDim));
		setLocation(posX, posY);

		JTabbedPane tabs = new JTabbedPane();
		tabs.setTabPlacement(JTabbedPane.BOTTOM);


		tabs.add("What Today?", new Label("orcodiis"));
		tabs.add("Sends/Recvs", new Button("lool"));
		tabs.add("Configuration", new Label("conf"));
		tabs.add("About", new Label("ciup"));


		content.add(tabs);

		setVisible(true);
	}

}
