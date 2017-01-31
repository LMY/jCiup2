


abstract class CiupProcess extends Thread
{
	private JCiupPanelTransfers transfers;



	public CiupProcess(JCiupPanelTransfers transfers)
	{
		super();

		this.transfers = transfers;

		transfers.notify(this, "register", 0);
	}



	protected void terminate()
	{
		transfers.notify(this, "unregister", 0);
	}
}
