package ggc.gui.main;

import java.awt.*;
import java.util.Observable;

import javax.swing.*;

import com.atech.i18n.I18nControlAbstract;
import com.atech.misc.refresh.EventObserverInterface;
import com.atech.utils.ATSwingUtils;
import ggc.core.util.DataAccess;

/**
 *  Application:   GGC - GNU Gluco Control
 *
 *  See AUTHORS for copyright information.
 * 
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 * 
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 *  Filename:     StatusBar  
 *  Description:  For creation of status bar.
 * 
 *  Author: schultd
 *          Andy {andy@atech-software.com}  
 */

public class StatusBar extends JPanel implements EventObserverInterface
{

    private static final long serialVersionUID = 1184879736050179885L;

    private JLabel lblMessage = null;
    private JLabel lblLed = null;
    private JLabel lblName = null;
    DataAccess da = DataAccess.getInstance();
    private I18nControlAbstract m_ic = da.getI18nControlInstance();

    private ImageIcon[] statusIcons = null;
    MainFrame m_frame;


    /**
     * Constructor
     * 
     * @param frame 
     */
    public StatusBar(MainFrame frame)
    {
        this.m_frame = frame;
        statusIcons = new ImageIcon[4];

        ATSwingUtils.initLibrary();

        statusIcons[0] = new ImageIcon(getClass().getResource("/icons/led_red.gif"));
        statusIcons[1] = new ImageIcon(getClass().getResource("/icons/led_yellow.gif"));
        statusIcons[2] = ATSwingUtils.getImageIcon("led_blue.gif", 14, 14, this, da);

        // new ImageIcon(getClass().getResource("/icons/led_blue.gif"));
        statusIcons[3] = new ImageIcon(getClass().getResource("/icons/led_green.gif"));

        lblMessage = new JLabel();
        lblMessage.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        lblMessage.setPreferredSize(new Dimension(400, 18));

        JPanel pan = new JPanel();
        pan.setPreferredSize(new Dimension(385, 18));
        pan.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        SpringLayout layout = new SpringLayout();
        pan.setLayout(layout); // new java.awt.FlowLayout());

        lblName = new JLabel();
        setDatabaseName(m_ic.getMessage("UNKNOWN"));
        pan.add(lblName);

        lblLed = new JLabel(statusIcons[0]);
        pan.add(lblLed, BorderLayout.EAST);

        layout.putConstraint(SpringLayout.WEST, lblName, 5, SpringLayout.WEST, pan);

        layout.putConstraint(SpringLayout.EAST, lblLed, -10, SpringLayout.EAST, pan);

        layout.putConstraint(SpringLayout.NORTH, lblLed, 1, SpringLayout.NORTH, pan);

        setLayout(new BorderLayout(2, 2));
        this.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        add(lblMessage, BorderLayout.WEST);
        add(pan, BorderLayout.EAST);

        da.addObserver(DataAccess.OBSERVABLE_STATUS, this);

    }


    /**
     * Set Status message
     * @param msg
     */
    public void setStatusMessage(String msg)
    {
        lblMessage.setText(" " + msg);
    }

    /**
     * Db Status: Stopped
     */
    public static final int DB_STOPPED = 0;

    /**
     * Db Status: Init done
     */
    public static final int DB_INIT_DONE = 1;

    /**
     * Db Status: Base done
     */
    public static final int DB_BASE_DONE = 2;

    /**
     * Db Status: Extended done
     */
    public static final int DB_EXTENDED_DONE = 3;

    /**
     * Db Status: Loaded
     */
    public static final int DB_LOADED = 3;

    // red status
    // 1 - init
    // yellow
    // 2 - load configuration
    // 3 - load statistics for display, apointments
    // blue
    // 4 - load doctors data
    // 5 - load nutrition(1) root data
    // 6 - load nutrition(2) root data
    // 7 - load meals root data


    // 99 - loading complete
    // green

    /**
     * Set Db Status
     * 
     * @param status
     */
    public void setDbStatus(int status)
    {
        this.lblLed.setIcon(statusIcons[status]);
        // this.m_frame.setMenusByDbLoad(status);
    }


    /**
     * Set Database Name
     * 
     * @param dbName
     */
    public void setDatabaseName(String dbName)
    {
        lblName.setText(m_ic.getMessage("DATABASE") + " [" + dbName + "]:");
    }


    /**
     * update - Observer method
     */
    public void update(Observable obj, Object arg)
    {
        // System.out.println("update status");

        if (arg instanceof Integer)
        {
            Integer i = (Integer) arg;
            this.setDbStatus(i.intValue());
        }
        else
        {
            String s = (String) arg;

            if (s.startsWith("DB_NAME="))
            {
                this.setDatabaseName(s.substring(8));
            }
            else
            {
                this.setStatusMessage(s);
            }
        }
    }

}
