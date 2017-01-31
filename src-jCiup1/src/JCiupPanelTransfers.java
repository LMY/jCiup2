
import java.awt.*;
import javax.swing.*;
import java.util.*;



class JCiupPanelTransfers extends JCiupPanel
{
	private JList incomingList;
	private DefaultListModel incomingModel;
	private JList outgoingList;
	private DefaultListModel outgoingModel;

	private LinkedList<CiupProcess> processes;



	public JCiupPanelTransfers()
	{
		super("Transfers");

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		incomingModel = new DefaultListModel();
		incomingList = new JList(incomingModel);
		outgoingModel = new DefaultListModel();
		outgoingList = new JList(outgoingModel);

		JScrollPane listScroller = new JScrollPane(incomingList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(listScroller);
		listScroller = new JScrollPane(outgoingList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(listScroller);

		processes = new LinkedList<CiupProcess>();

		ProcessLauncher.initialize(this);
		ProcessLauncher.get().launchServer();
	}




	private void registerProcess(CiupProcess process)	{ processes.add(process); }
	private void unregisterProcess(CiupProcess process)	{ processes.remove(process); }



	public void notify(CiupProcess process, String status, double percent)
	{
		if (status.equals("register"))
			registerProcess(process);

		else if (status.equals("unregister"))
			unregisterProcess(process);

		else if (status.equals("going")) {
			// adjourn percemt
		}
	}
}
