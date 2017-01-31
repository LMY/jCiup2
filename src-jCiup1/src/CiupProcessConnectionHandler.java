
import java.io.*;
import java.net.*;


class CiupProcessConnectionHandler extends CiupProcess
{
	private Socket socket;



	public CiupProcessConnectionHandler(JCiupPanelTransfers transfers, Socket socket)
	{
		super(transfers);
		
		this.socket = socket;
	}
	
	


	public void terminate()
	{
		super.terminate();

		try {
			socket.close();
		}
		catch (IOException e) {}

		socket = null;
	}
	


	public void run()
	{
		System.out.println("OK, Running...");
	}

}
