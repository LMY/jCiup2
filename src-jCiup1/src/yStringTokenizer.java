

import java.util.*;



class yStringTokenizer  extends StringTokenizer
{
	public yStringTokenizer(String s) 
	{
		super(s, " \t", true);
	}





	public String getTheRest()
	{
		StringBuffer sb = new StringBuffer(nextToken());

        while (hasMoreTokens())
            sb.append(super.nextToken());

		return sb.toString();
	}



	public String nextToken()
	{
		String s;
		
		do {
			try {
				s = super.nextToken();	
			}
			catch (NoSuchElementException exception) {
				return new String();
			}
		} while (s.equals(" ") || s.equals("\t"));

		return s;
	}
}
