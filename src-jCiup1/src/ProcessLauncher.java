
import java.net.*;



class ProcessLauncher
{
// Singleton code

	private static ProcessLauncher instance = null;



	public static void initialize(JCiupPanelTransfers transfers)
	{
		if (instance == null)
			instance = new ProcessLauncher(transfers);
		// else, already initialized
	}


	public static ProcessLauncher get()
	{
		return instance;
	}





// ProcessLauncher class
	private JCiupPanelTransfers transfers;
	private CiupProcessServer server;
	

	private ProcessLauncher(JCiupPanelTransfers transfers)
	{
		this.transfers = transfers;
		this.server = new CiupProcessServer(transfers);
	}


	public void launchServer()
	{
		server.start();
	}


	public void launchConnectionHandler(Socket sock)
	{
		System.out.println("connection attempt.");
		
		CiupProcessConnectionHandler child = new CiupProcessConnectionHandler(transfers, sock);
		child.start();
	}
}
