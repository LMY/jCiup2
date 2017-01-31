
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;



public class JCiupFrame extends JFrame
{
	private JCiupTabs tabs;
	private Userlist userlist;



     public JCiupFrame()
     {
        super("jCiup");

       	try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception exception) {
			System.out.println("Invalid look and feel.");
		}

		Config.initialize();
		Shares.initialize();


		Container content = this.getContentPane();
		content.setLayout(new BorderLayout());

		setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(new Dimension(400, 300));


		tabs = new JCiupTabs();
	//	userlist = new Userlist();
		
		center();

		content.add(tabs, BorderLayout.CENTER);
	//	content.add(userlist, BorderLayout.WEST);
    }


	public void center()
	{
		Dimension screen_dim = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension window_dim = getSize();

		int posX = (screen_dim.width - window_dim.width) / 2;
		int posY = (screen_dim.height- window_dim.height) / 2;

		setLocation(posX, posY);		
	}

}
