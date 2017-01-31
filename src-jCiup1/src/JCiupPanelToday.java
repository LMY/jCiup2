
import java.awt.*;
import javax.swing.*;


class JCiupPanelToday extends JCiupPanel
{
	public JCiupPanelToday()
	{
		super("Today");
		
		JPanel rPanel = new JPanel();
		JPanel lPanel = new JPanel();
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		rPanel.setLayout(new GridLayout(2,2));
		
		
		rPanel.add(new JButton("Transfers"));
		rPanel.add(new JButton("My Shares"));
		rPanel.add(new JButton("Options"));
		rPanel.add(new JButton("About"));
		
		lPanel.add(new JLabel("qui ci va la foto di ciup."));
			
		add(rPanel);
		add(lPanel);
	}
}
