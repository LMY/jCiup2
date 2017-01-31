
import java.awt.*;
import javax.swing.*;



class JCiupTabs extends JTabbedPane
{
	public JCiupTabs()
	{
		super();
	
		setTabPlacement(JTabbedPane.BOTTOM);
		
		
		addPanel(new JCiupPanelToday());
		addPanel(new JCiupPanelTransfers());
		addPanel(new JCiupPanelConf());
	}


	
	
	public void addPanel(JCiupPanel panel)
	{
		add(panel, panel.getName());
	}
	
	
	
	
	public String getActive()
	{
		int activeTab = getSelectedIndex();

		if (activeTab < 0)
			return new String();
		else
			return getTitleAt(activeTab);
	}



	public boolean exists(String name)		{ return (getWindow(name) != null); }



	public boolean close(String name)
	{
		int index = indexOfTab(name);
		
		if (index >= 0)
			remove(index);

		return true;
	}

	
	
	public void select(String win)
	{
		int index = indexOfTab(win);

		if (index >= 0)
			setSelectedIndex(index);
	}



	public void select(int index)
	{
		if (index >= 0 && index < getTabCount())
			setSelectedIndex(index);
	}





	protected JCiupPanel getWindow(String name)
	{
		int index = indexOfTab(name);

		if (index >= 0)
			return (JCiupPanel) getComponentAt(index);
		else
			return null;
	}
}
