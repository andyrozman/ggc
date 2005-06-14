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
 */

package ggc;


import ggc.gui.MainFrame;

import javax.swing.*;
import java.awt.*;


public class GGC
{
    //fields
    private static GGC theApp;
    private static MainFrame mainWindow;
    private static String version = "0.0.2";

    public static void main(String[] args)
    {
        try 
	{
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } 
	catch (Exception ex) 
	{
	    System.out.println("Error loading L&F: " + ex);
        }
        
	/*
	UIDefaults UIDef = UIManager.getDefaults();

        Color bgGray = new Color(213, 210, 205);

        Font stdFont = new Font("Dialog", Font.PLAIN, 12);

        UIDef.put("Label.font", stdFont);
        UIDef.put("Label.foreground", Color.black);

        UIDef.put("Button.font", stdFont);
        UIDef.put("Menu.font", stdFont);
        UIDef.put("MenuItem.font", stdFont);
        UIDef.put("ComboBox.font", stdFont);
        UIDef.put("CheckBox.font", stdFont);
        UIDef.put("RadioButton.font", stdFont);
        UIDef.put("TitledBorder.font", stdFont);
        //UIDef.put("TitledBorder.fontcolor", Color.black);

        UIDef.put("Panel.background", bgGray);
        UIDef.put("Menu.background", bgGray);
        UIDef.put("MenuItem.background", bgGray);
        UIDef.put("Frame.background", bgGray);
        UIDef.put("Button.background", bgGray);
        UIDef.put("ComboBox.background", bgGray);
        UIDef.put("RadioButton.background", bgGray);
        UIDef.put("CheckBox.background", bgGray);
        //UIDef.put("Table.background", bgGray);
        UIDef.put("ToolBar.background", bgGray);
        UIDef.put("MenuBar.background", bgGray);
        UIDef.put("Pane.background", bgGray);
        UIDef.put("OptionPane.background", bgGray);
	*/

        boolean dev = false;

        if (args.length>0) 
        {
            dev = true;
        }


        theApp = new GGC();
        theApp.init(dev);

    }

    public void init(boolean dev)
    {
        mainWindow = new MainFrame("GGC - GNU Gluco Control", dev);
        Toolkit theKit = mainWindow.getToolkit();
        Dimension wndSize = theKit.getScreenSize();

        mainWindow.setBounds(wndSize.width / 4, wndSize.height / 4, (int)(wndSize.width * 0.66), (int)(wndSize.height * 0.66));
        mainWindow.setVisible(true);
    }

    public static String getVersion()
    {
        return theApp.version;
    }
}