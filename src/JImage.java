
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
import javax.swing.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;



public class JImage extends JPanel
{
	private BufferedImage image;



	public JImage(String filename)
	{
		super();
		setImage(filename);
	}



	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		if (image != null)
			((Graphics2D) g).drawImage(image, null, 0, 0);
	}



	public void setImage(String filename)
	{
		try {
			image = ImageIO.read(new File(filename));
			setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
			setMaximumSize(new Dimension(image.getWidth(), image.getHeight()));
			setMinimumSize(new Dimension(image.getWidth(), image.getHeight()));
		}
		catch (IOException e) {
			image = null;
		}
	}
}
