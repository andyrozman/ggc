/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: ggc.java
 *  Purpose:  loads the gui and sets some look and feel properties.
 *
 *  Author:   schultd
 *
 *  Version 0.1 (schultd)
 *  - basic implementation
 *
 *  Version 0.1.1 (andyrozman)
 *  - added skin load
 *  - nutrition data
 */

package ggc;


import ggc.gui.MainFrame;

import javax.swing.*;
import java.awt.*;


public class GGC
{
    //fields
    private static GGC s_theApp;
    private static MainFrame s_mainWindow;

    // version information
    // is stored in MainFrame

    public static void main(String[] args)
    {
	/*
        try 
	{
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } 
	catch (Exception ex) 
	{
	    System.out.println("Error loading L&F: " + ex);
        }
        */
        boolean dev = false;

        if (args.length>0) 
            dev = true;

        s_theApp = new GGC();
        s_theApp.init(dev);
    }

    public void init(boolean dev)
    {
        s_mainWindow = new MainFrame("GGC - GNU Gluco Control", dev);
        Toolkit theKit = s_mainWindow.getToolkit();
        Dimension wndSize = theKit.getScreenSize();

	int x, y; 
	x = wndSize.width/2 - 400;
	y = wndSize.height/2 - 300;

	s_mainWindow.setBounds(x, y, 800, 600);
        s_mainWindow.setVisible(true);
    }

}