


class Shares
{
// Singleton code

	private static Shares instance = null;
	


	public static void initialize()
	{
		if (instance == null)
			instance = new Shares();
		// else, already initialized			
	}

	
	public static Shares get()
	{
		return instance;
	}






// Shares class
	
	private Shares()
	{
	
	}
}
