
import java.awt.*;
import javax.swing.*;



class Userlist extends JList
{
	private DefaultListModel userlistModel;
	
	
	public Userlist()
	{
		super();
		
		userlistModel = new DefaultListModel();
		
		setModel(userlistModel);
		userlistModel.addElement("Chiara");
		userlistModel.addElement("LMY");		
	}
}
