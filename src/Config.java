
/*
	Copyright (C) 2007 Miro Salvagni.
	
	This file is part of jCiup.
	
	jCiup is free software; you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation; either version 2 of the License, or
	(at your option) any later version.
	
	jCiup is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with jCiup; if not, write to the Free Software
	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/

import java.io.*;



public class Config
{
	public static String conf_filename = "JCiup2.conf";
	
	// Singleton
	private static Config instance = null;
	
	public static Config getInstance()	{ return instance; }
	public static void initialize()		{ if (instance == null) instance = new Config(); }



	// Variables
	private String nick;
	private String incomingDirectory;
	private boolean auto_accept;
	private boolean autoclose_send;
	private boolean autoclose_recv;
	private boolean dir_foreach_user;
	private boolean start_minimized;
	private int server_port;


	private Config()
	{
		incomingDirectory = ".";
		auto_accept = false;
		nick = "ciup";
		server_port = 7777;
		
		autoclose_send = false;
		autoclose_recv = false;
		dir_foreach_user = false;
		start_minimized = false;
		
		load();
		adjust_incoming_directory();
	}
	
	
	// Getters and Setters
	public String getIncomingDirectory()	{ return incomingDirectory; }
	public boolean getAutoAccept()			{ return auto_accept; }
	public String getNick()					{ return nick; }
	public int getServerPort()				{ return server_port; }
	public boolean getAutocloseSend()		{ return autoclose_send; }
	public boolean getAutocloseRecv()		{ return autoclose_recv; }
	public boolean getDirForeachUser()		{ return dir_foreach_user; }
	public boolean getStartMinimized()		{ return start_minimized; }


	
	public void setIncomingDirectory(String s)	{ incomingDirectory = s; adjust_incoming_directory(); }
	public void setAutoAccept(boolean b)		{ auto_accept = b; }
	public void setNick(String s)				{ nick = s; }
	public void setServerPort(int v)			{ server_port = v; }
	public void setAutocloseSend(boolean b)		{ autoclose_send = b; }
	public void setAutocloseRecv(boolean b)		{ autoclose_recv = b; }
	public void setDirForeachUser(boolean b)	{ dir_foreach_user = b; }
	public void setStartMinimized(boolean b)	{ start_minimized = b; }
	
	
	public boolean load(String filename)
	{
		try {
			BufferedReader fis = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filename))));
			String line = fis.readLine();
			
			while (line != null) {
				
				line = line.trim();
				
				if (line.startsWith("<config>") || line.startsWith("</config>"))
					;
				else if (line.startsWith("<autoaccept>")) {
					line = line.replaceAll("<autoaccept>", "").replaceAll("</autoaccept>", "");
					
					setAutoAccept(line.equalsIgnoreCase("true"));
				}
				else if (line.startsWith("<incomingdirectory>")) {
					line = line.replaceAll("<incomingdirectory>", "").replaceAll("</incomingdirectory>", "");
					
					setIncomingDirectory(line);
				}				
				else if (line.startsWith("<nick>")) {
					line = line.replaceAll("<nick>", "").replaceAll("</nick>", "");
					
					setNick(line);
				}
				else if (line.startsWith("<server_port>")) {
					line = line.replaceAll("<server_port>", "").replaceAll("</server_port>", "");
					
					try {
						setServerPort(Integer.parseInt(line));
					}
					catch (NumberFormatException e) {}
				}
				else if (line.startsWith("<autoclosesend>")) {
					line = line.replaceAll("<autoclosesend>", "").replaceAll("</autoclosesend>", "");
					
					setAutocloseSend(line.equalsIgnoreCase("true"));
				}
				else if (line.startsWith("<autocloserecv>")) {
					line = line.replaceAll("<autocloserecv>", "").replaceAll("</autocloserecv>", "");
					
					setAutocloseRecv(line.equalsIgnoreCase("true"));
				}
				else if (line.startsWith("<dirforeachuser>")) {
					line = line.replaceAll("<dirforeachuser>", "").replaceAll("</dirforeachuser>", "");
					
					setDirForeachUser(line.equalsIgnoreCase("true"));
				}
				else if (line.startsWith("<startminimized>")) {
					line = line.replaceAll("<startminimized>", "").replaceAll("</startminimized>", "");
					
					setStartMinimized(line.equalsIgnoreCase("true"));
				}
								
				line = fis.readLine();
			}
			
		}
		catch (IOException e) { return false; }
		
		return true;
	}



	public boolean save(String filename)
	{
		try {
			BufferedWriter fout = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(filename))));
			
			fout.write("<config>\n");
			
			fout.write("\t<autoaccept>"+(auto_accept  ?  "true"  :  "false")+"</autoaccept>\n");
			fout.write("\t<incomingdirectory>"+incomingDirectory+"</incomingdirectory>\n");
			fout.write("\t<nick>"+nick+"</nick>\n");
			fout.write("\t<server_port>"+server_port+"</server_port>\n");
			fout.write("\t<autoclosesend>"+(autoclose_send  ?  "true"  :  "false")+"</autoclosesend>\n");
			fout.write("\t<autocloserecv>"+(autoclose_recv  ?  "true"  :  "false")+"</autocloserecv>\n");
			fout.write("\t<dirforeachuser>"+(dir_foreach_user  ?  "true"  :  "false")+"</dirforeachuser>\n");
			fout.write("\t<startminimized>"+(start_minimized  ?  "true"  :  "false")+"</startminimized>\n");
			
			fout.write("</config>\n");
			
			fout.close();
		
		}
		catch (IOException e) { return false; }
		
		return true;
	}

	public boolean save()	{ return save(conf_filename); }
	public boolean load()	{ return load(conf_filename); }




	private void adjust_incoming_directory()
	{
		File f = new File(incomingDirectory);
		
		if (!f.exists())
			if (!f.mkdirs())
				incomingDirectory = ".";
	}
}
