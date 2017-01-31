

import java.io.*;
import java.net.*;



class CiupProcessServer extends CiupProcess
{
	private ServerSocket server;



	public CiupProcessServer(JCiupPanelTransfers transfers)
	{
		super(transfers);

		server = null;
	}



	public void run()
	{
		try {
			server = new ServerSocket(Config.get().getServerPort());
		}
		catch (IOException exc) {
			return;
		}


		Socket sock;

		try {
			while (true) {
				sock = server.accept();

				ProcessLauncher.get().launchConnectionHandler(sock);

				sock = null;
			}
		}
		catch (IOException e) {
			server = null;
		}
	}



	public void terminate()
	{
		super.terminate();

		try {
			server.close();
		}
		catch (IOException e) {}

		server = null;
	}
}
