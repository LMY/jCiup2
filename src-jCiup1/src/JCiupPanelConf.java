
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
//import java.util.*;


class JCiupPanelConf extends JCiupPanel implements ActionListener
{
	private JTextArea	portField;
	private JTextArea	incomingField;
	private JCheckBox	alwaysacceptBox;
	private JButton		saveButton;
	
	
	public JCiupPanelConf()
	{
		super("Config");
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));


		Config conf = Config.get();
		
		if (conf == null)
			return;

		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel();
		
		panel1.setLayout(new FlowLayout());
		panel2.setLayout(new FlowLayout());
		panel3.setLayout(new FlowLayout());

		portField = new JTextArea(""+conf.getServerPort());
		incomingField = new JTextArea(""+conf.getIncomingDirectory());
		alwaysacceptBox = new JCheckBox("Auto-accept incoming files", conf.getAutoAccept());
		saveButton = new JButton("Save Config");
		
		saveButton.addActionListener(this);


		panel1.add(new JLabel("ServerPort:"));
		panel1.add(portField);
		
		panel2.add(new JLabel("Incoming Directory:"));
		panel2.add(incomingField);
		
		panel3.add(alwaysacceptBox);
		
		add(panel1);
		add(panel2);
		add(panel3);
		add(saveButton);
	}



	public void actionPerformed(ActionEvent event)
	{
		Config conf = Config.get();
		
		if (conf == null)
			return;

		try {
			conf.setServerPort(Integer.parseInt(portField.getText()));
		}
		catch (NumberFormatException e) {
			portField.setText(""+conf.getServerPort());
		}
		conf.setIncomingDirectory(incomingField.getText());
		conf.setAutoAccept(alwaysacceptBox.isSelected());
		
		conf.save();
	}

}
