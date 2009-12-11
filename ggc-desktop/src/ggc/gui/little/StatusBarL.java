package ggc.gui.little;

import ggc.core.util.DataAccess;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Observable;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import com.atech.i18n.I18nControlAbstract;
import com.atech.misc.refresh.EventObserverInterface;

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
 *  Filename:     StatusBarL
 *  Description:  StatusBar for the Little Application.
 *
 *  Author: andyrozman {andy@atech-software.com}  
 */


public class StatusBarL extends JPanel implements EventObserverInterface
{
    private static final long serialVersionUID = -8864931592538137782L;
    DataAccess da = DataAccess.getInstance();
    private I18nControlAbstract m_ic = da.getI18nControlInstance();
    
    private JLabel lblMessage = null;
    //private JLabel lblDataSource = null;
    private JLabel lblLed = null;
    private JLabel lblName = null;

    private ImageIcon[] statusIcons = null;
    GGCLittle little;
    
    /**
     * Constructor
     * 
     * @param little 
     */
    public StatusBarL(GGCLittle little)
    {
        this.little = little;
        statusIcons = new ImageIcon[4];

        statusIcons[0] = new ImageIcon(getClass().getResource("/icons/led_red.gif"));
        statusIcons[1] = new ImageIcon(getClass().getResource("/icons/led_yellow.gif"));
        statusIcons[2] = da.getImageIcon( "led_blue.gif", 14, 14, this);
            //new ImageIcon(getClass().getResource("/icons/led_blue.gif"));
        statusIcons[3] = new ImageIcon(getClass().getResource("/icons/led_green.gif"));

        lblMessage = new JLabel();
        lblMessage.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        lblMessage.setPreferredSize(new Dimension(350, 18));

        JPanel pan = new JPanel();
        pan.setPreferredSize(new Dimension(275, 18));
        pan.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        SpringLayout layout = new SpringLayout();
        pan.setLayout(layout); 

        lblName = new JLabel();
        setDatabaseName(m_ic.getMessage("UNKNOWN"));
        //lblName.setText("Database [Unknown]: ");
        pan.add(lblName);


        lblLed = new JLabel(statusIcons[0]);
        pan.add(lblLed, BorderLayout.EAST);

        layout.putConstraint(SpringLayout.WEST, lblName,
                     5,
                     SpringLayout.WEST, pan);

        layout.putConstraint(SpringLayout.EAST, lblLed,
                     -10,
                     SpringLayout.EAST, pan);

        layout.putConstraint(SpringLayout.NORTH, lblLed,
                     1,
                     SpringLayout.NORTH, pan);


        setLayout(new BorderLayout(2, 2));
        this.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        add(lblMessage, BorderLayout.WEST);
        add(pan, BorderLayout.EAST);

        da.addObserver(DataAccess.OBSERVABLE_STATUS, this);
    }


    /**
     * Set Status Message
     * 
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
     * Db Status: Init
     */
    public static final int DB_INIT = 0;
    /**
     * Db Status: OK
     */
    public static final int DB_INIT_OK = 1;
    /**
     * Db Status: Load
     */
    public static final int DB_LOAD = 2;

    
    /**
     * Set Db Status
     * @param status
     */
    public void setDbStatus(int status)
    {
        this.lblLed.setIcon(statusIcons[status]);
        //this.little.setMenusByDbLoad(status);
        //((MainFrame)m_da.getMainParent()).setMenusByDbLoad(status);
    }

    /**
     * Set Database Name
     * 
     * @param dbName
     */
    public void setDatabaseName(String dbName)
    {
        lblName.setText(m_ic.getMessage("DATABASE") + " [" + dbName +"]:");
    }


    /**
     * update - Observer method
     */
    public void update(Observable obj, Object arg)
    {
        if (arg instanceof Integer)
        {
            Integer i = (Integer)arg;
            this.setDbStatus(i.intValue());
        }
        else
        {
            String s = (String)arg;
            
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
