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

import gui.MainFrame;

import javax.swing.*;
import java.awt.*;


public class ggc
{
    //fields
    private static ggc theApp;
    private static MainFrame mainWindow;

    public static void main(String[] args)
    {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception e) {
        }
        UIDefaults UIDef = UIManager.getDefaults();

        Color bgGray = new Color(213,210,205);

        Font stdFont = new Font("Dialog", Font.PLAIN,12);

        UIDef.put("Label.font", stdFont);
        UIDef.put("Label.foreground", Color.black);

        UIDef.put("Button.font",stdFont);
        UIDef.put("Menu.font",stdFont);
        UIDef.put("MenuItem.font",stdFont);
        UIDef.put("ComboBox.font", stdFont);
        UIDef.put("TitledBorder.font", stdFont);
        //UIDef.put("TitledBorder.fontcolor", Color.black);

        UIDef.put("Panel.background", bgGray);
        UIDef.put("Menu.background", bgGray);
        UIDef.put("MenuItem.background", bgGray);
        UIDef.put("Frame.background", bgGray);
        UIDef.put("Button.background", bgGray);
        UIDef.put("ComboBox.background", bgGray);
        //UIDef.put("Table.background", bgGray);
        UIDef.put("ToolBar.background", bgGray);
        UIDef.put("MenuBar.background", bgGray);
        UIDef.put("Pane.background", bgGray);
        UIDef.put("OptionPane.background", bgGray);

        theApp = new ggc();
        theApp.init();
    }

    public void init()
    {
        mainWindow = new MainFrame("GGC - GNU Gluco Control");
        Toolkit theKit = mainWindow.getToolkit();
        Dimension wndSize = theKit.getScreenSize();

        mainWindow.setBounds(wndSize.width / 4, wndSize.height / 4, wndSize.width / 2, wndSize.height / 2);
        mainWindow.setVisible(true);
    }
}