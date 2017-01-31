
import java.io.*;
import java.util.*;


class Config
{
// Singleton code

	private static Config instance = null;



	public static void initialize()
	{
		if (instance == null)
			instance = new Config();
		// else, already initialized
	}


	public static Config get()
	{
		return instance;
	}






// Config class
	private int ServerPort;
	private String IncomingDirectory;
	private boolean AutoAccept;


	private Config()
	{
		ServerPort = 51020;
		IncomingDirectory = "./incoming";
		AutoAccept = false;
		
		readFromFile();
		
		File in = new File(IncomingDirectory);
		
		if (!in.exists())
			in.mkdir();
	}



	public int getServerPort()				{ return ServerPort;	}
	public String getIncomingDirectory()	{ return IncomingDirectory; }
	public boolean getAutoAccept()			{ return AutoAccept; }
	
	
	public void setServerPort(int port)		{ ServerPort = port; }
	public void setIncomingDirectory(String s)	{ IncomingDirectory = s; }
	public void setAutoAccept(boolean b)	{ AutoAccept = b; }
	
	public boolean readFromFile()			{ return readFromFile("jCiup.conf"); }
	public boolean readFromFile(String filename)
	{
		String s;
		String field;
		String value;
		yStringTokenizer tokenizer;
		
		try {
			
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
		
			while ((s = in.readLine()) != null) {
				
				s = s.trim();
				
				if (s.equals("") || s.startsWith("//"))
					continue;
					
				tokenizer = new yStringTokenizer(s);
				field = tokenizer.nextToken();
				value = tokenizer.getTheRest().trim();
				
				if (field.equalsIgnoreCase("port"))
					try {
						ServerPort = Integer.parseInt(value);
					}
					catch (NumberFormatException e) {
						System.out.println("invalid line in conf ("+s+")");
						ServerPort = 51020;
					}
				else if (field.equalsIgnoreCase("IncomingDirectory"))
					IncomingDirectory = value;
				else if (field.equalsIgnoreCase("AutoAccept"))
					AutoAccept = value.equalsIgnoreCase("yes");					
				else {
					System.out.println("invalid line in conf ("+s+")");
					ServerPort = 51020;
					IncomingDirectory = "./incoming";
					AutoAccept = false;
					
					return false;
				}
			}			
		}
		catch (Exception e) {
			// set defaults
			ServerPort = 51020;
			IncomingDirectory = "./incoming";
			AutoAccept = false;
			
			return false;			
		}

		return true;
	}
	
	
	public boolean save()		{ return save("jCiup.conf"); }
	public boolean save(String filename)
	{
		try {
			PrintWriter out = new PrintWriter(new File(filename));
		
			out.println("IncomingDirectory"+" "+IncomingDirectory);
			out.println("AutoAccept"+" "+(AutoAccept?"yes":"no"));
			out.println("port"+" "+ServerPort);
			
			out.close();
		}
		catch (IOException e) {
			System.out.println("Error writing conf file.");
			return false;
		}
		
		return true;
	}
}
