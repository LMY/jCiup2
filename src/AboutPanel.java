
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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;



public class AboutPanel extends JPanel
{
	public AboutPanel()
	{
		super();
		
		JImage image = new JImage("Ciup.gif");
		JLabel ciup_label = new JLabel(JCiup2.version);
		
		JTextArea textarea = new JTextArea();
		textarea.setEditable(false);
		
		Font font = new Font(textarea.getFont().getName(), Font.PLAIN, 8);
		textarea.setFont(font);
		
		load_license_file(textarea);
		
		JScrollPane textareascroll = new JScrollPane(textarea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        textareascroll.setWheelScrollingEnabled(true);

		setLayout(new BorderLayout());
		
		JPanel p1 = new JPanel();
		p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
		p1.add(image);
		p1.add(Box.createRigidArea(new Dimension(10,10)));
		p1.add(ciup_label);

		add(p1, BorderLayout.NORTH);
		add(textareascroll, BorderLayout.CENTER);
	}
	
	
	public void load_license_file(JTextArea textarea)
	{
		try {
			BufferedReader fis = new BufferedReader(new InputStreamReader(new FileInputStream(new File("License.txt"))));
			String line;
			StringBuffer buffer = new StringBuffer();
			
			while (true) {
				line = fis.readLine();
				
				if (line == null)
					break;
				
				buffer.append(line+"\n");
			}
			
			textarea.setText(buffer.toString());
			fis.close();
		}
		catch (IOException e) {}
	}
}
