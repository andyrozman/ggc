/*
 * Created on 16.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */

package ggc.util;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


/**
 * @author stephan
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
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

        button.addMouseListener(new MouseListener()
        {
            public void mouseEntered(MouseEvent e)
            {
                if (button.isEnabled()) {
                    button.setBorder(BorderFactory.createLineBorder(new Color(8, 36, 106), 1));
                    button.setBackground(new Color(180, 190, 213));
                }
            }

            public void mouseExited(MouseEvent e)
            {
                button.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
                button.setBackground(new Color(213, 210, 205));
            }

            public void mouseClicked(MouseEvent e)
            {
            }

            public void mousePressed(MouseEvent e)
            {
            }

            public void mouseReleased(MouseEvent e)
            {
            }

        });
        return button;
    }

}
