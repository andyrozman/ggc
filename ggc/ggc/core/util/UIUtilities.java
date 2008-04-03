

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
 *  Filename: UIUtilities
 *  
 *  Purpose:  Used for utility works and static data handling (this is singelton
 *      class which holds all our definitions, so that we don't need to create them
 *      again for each class.      
 *
 *  Author:   stephan
 *  
 *  Created on 16.08.2002
 */


package ggc.util;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;


public class UIUtilities
{

    /**
     * Constructor for UIUtilities.
     */
    public UIUtilities()
    {
        super();
    }

    public static JButton addToolBarButton(JToolBar toolBar, Action action)
    {
        final JButton button = toolBar.add(action);

        button.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        button.setFocusPainted(false);

        button.setPreferredSize(new Dimension(24, 24));

        button.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent e)
            {
                if (button.isEnabled()) 
		{
                    button.setBorder(BorderFactory.createLineBorder(new Color(8, 36, 106), 1));
                    button.setBackground(new Color(180, 190, 213));
                }
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                button.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
                button.setBackground(new Color(213, 210, 205));
            }
/*
            public void mouseClicked(MouseEvent e) 
	    { 
	    }

            public void mousePressed(MouseEvent e)
            {
            }

            public void mouseReleased(MouseEvent e)
            {
            } */

        });
        return button;
    }

}
