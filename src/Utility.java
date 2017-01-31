
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

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;



public class Utility
{
	public static String size2string(long size)
	{
		String[] sizes = { "b", "Kb", "Mb", "Gb", "Tb" };
		int index = 0;
		double dsize = size;
		
		while (dsize > 1024) {
			dsize /= 1024;
			index++;
		}
		
		int idsize = (int) dsize;
		int dec = (int) ((dsize - idsize)*100);
		
		return (index < sizes.length  ? idsize+"."+dec+" "+sizes[index]  :  "BIG");
	}
	
	
	public static String size2string(String sizestring)
	{
		try {
			return size2string(Integer.parseInt(sizestring));
		}
		catch (NumberFormatException e) {
			return new String("Unknown");
		}
	}
	
	
	
	public static String speed2String(long trafficbytes, long time)
	{
		if (time <= 0)
			return new String("infinite?!");
		
		double speed = (double) trafficbytes/time;
		int intero = (int) speed;
		int dec = (int) ((speed - intero)*100);
		
		return intero+"."+dec+" Kb/s";
	}



	public static String eta2String(long filesize, long offset, long trafficbytes, long time)
	{
		if (time <= 0 || trafficbytes <= 0)
			return "boh";


//		double meanspeed = (double) trafficbytes/time;
//		long etasec = (long) ((filesize-offset-trafficbytes)/meanspeed)/1000;
		

		long etasec = (long) (((double) (filesize-offset)/trafficbytes - 1)*time/1000);
		
		if (etasec > 24*3600)
			return (etasec/24/3600)+" days";
		else if (etasec > 3600)
			return (etasec/3600)+" hrs";
		else if (etasec > 60)
			return (etasec/60)+" mins";
		else if (etasec >= 0)
			return etasec+" secs";
		else
			return "boh";
	}
	
	
	
	public static String transfer2String(long trafficbytes, long filesize)
	{
		double percentage = (filesize >= 0  ?  (double) 100*trafficbytes/filesize  :  0);
		
		return ((int)percentage)+"%";
	}



	public static String getMyIp()
	{
		StringBuffer ip = new StringBuffer();
		String current_ip;

		try {
			Enumeration<NetworkInterface> e1 = (Enumeration<NetworkInterface>) NetworkInterface.getNetworkInterfaces();
			
			while(e1.hasMoreElements()) {
				NetworkInterface ni = e1.nextElement();
				
				if (ni.getName().equals("lo"))
					continue;
	
				Enumeration<InetAddress> e2 = ni.getInetAddresses();
				while (e2.hasMoreElements()) {
					InetAddress ia = e2.nextElement();
					
					current_ip = ia.toString();
					if (current_ip.startsWith("/"))
						current_ip = current_ip.substring(1);
	
					ip.append(current_ip);
	
					if (e2.hasMoreElements())
						ip.append(", ");						
				}
			}
		}
		catch (Exception e) {}
		
		return ip.toString();
	}	
}
