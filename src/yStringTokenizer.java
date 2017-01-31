
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
