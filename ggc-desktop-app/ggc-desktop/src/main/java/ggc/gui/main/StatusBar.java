package ggc.gui.main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;

import javax.swing.*;

import com.atech.i18n.I18nControlAbstract;
import com.atech.misc.refresh.EventObserverInterface;
import com.atech.utils.ATSwingUtils;

import ggc.core.enums.UpgradeCheckStatus;
import ggc.core.util.DataAccess;
import info.clearthought.layout.TableLayout;

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

public class StatusBar extends JPanel implements EventObserverInterface, ActionListener
{

    private static final long serialVersionUID = 1184879736050179885L;

    DataAccess dataAccess = DataAccess.getInstance();
    private I18nControlAbstract m_ic = dataAccess.getI18nControlInstance();

    private JLabel lblMessage = null;
    private JLabel lblDbName = null;
    private JLabel lblDbLed = null;
    private JLabel lblNetwork = null;

    private ImageIcon[] statusIcons = null;
    private ImageIcon[] networkIcons = null;
    MainFrame m_frame;
    private UpgradeCheckStatus upgradeCheckStatus = UpgradeCheckStatus.NotChecked;


    public StatusBar(MainFrame frame)
    {
        this.m_frame = frame;
        ATSwingUtils.initLibrary();

        initIcons();
        initStatusBars();

        dataAccess.addObserver(DataAccess.OBSERVABLE_STATUS, this);
    }


    private void initIcons()
    {
        statusIcons = new ImageIcon[4];
        statusIcons[0] = new ImageIcon(getClass().getResource("/icons/led_red.gif"));
        statusIcons[1] = new ImageIcon(getClass().getResource("/icons/led_yellow.gif"));
        statusIcons[2] = ATSwingUtils.getImageIcon("led_blue.gif", 14, 14, this, dataAccess);
        statusIcons[3] = new ImageIcon(getClass().getResource("/icons/led_green.gif"));

        networkIcons = new ImageIcon[5];
        // no state
        networkIcons[0] = ATSwingUtils.getImageIcon("net_no_state.png", 23, 23, this, dataAccess);
        // success
        networkIcons[1] = ATSwingUtils.getImageIcon("net_connected.png", 22, 22, this, dataAccess);
        // error
        networkIcons[2] = ATSwingUtils.getImageIcon("net_error.png", 22, 22, this, dataAccess);
        // news - update
        networkIcons[3] = ATSwingUtils.getImageIcon("net_news.png", 22, 22, this, dataAccess);
        // download (not planned for now)
        networkIcons[3] = ATSwingUtils.getImageIcon("net_download.png", 22, 22, this, dataAccess);
    }


    private JPanel createDbStatusPanel()
    {
        // Db Name
        JPanel panelDb = new JPanel();
        panelDb.setBorder(BorderFactory.createLineBorder(Color.gray, 1));

        double sizes[][] = { { 4, TableLayout.FILL, 20, 5 }, //
                             { 20, 2 } };

        panelDb.setLayout(new TableLayout(sizes));

        lblDbName = new JLabel();
        setDatabaseName(m_ic.getMessage("UNKNOWN"));
        lblDbName.setVerticalAlignment(JLabel.CENTER);
        panelDb.add(lblDbName, "1, 0");

        lblDbLed = new JLabel(statusIcons[0]);
        lblDbLed.setPreferredSize(new Dimension(20, 20));

        panelDb.add(lblDbLed, "2, 0");

        return panelDb;
    }


    private JPanel createCombinedStatusPanel()
    {
        JPanel statusMainPanel = new JPanel();

        double sizes[][] = { { 0.5, 2, TableLayout.FILL, 2 }, //
                             { 2, 20 } };

        statusMainPanel.setLayout(new TableLayout(sizes));

        // Status message
        lblMessage = new JLabel();
        lblMessage.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        lblMessage.setText("ufufuu");
        lblMessage.setBackground(Color.red);

        statusMainPanel.add(lblMessage, "0, 1");

        JPanel dbStatusPanel = createDbStatusPanel();
        statusMainPanel.add(dbStatusPanel, "2, 1");

        return statusMainPanel;
    }


    private void initStatusBars()
    {
        double sizes[][] = { { 3, TableLayout.FILL, 24, 3 }, //
                             { 24 } };

        this.setLayout(new TableLayout(sizes));

        JPanel statusMainPanel = createCombinedStatusPanel();

        this.add(statusMainPanel, "1, 0");

        // Update Connection Panel
        JPanel connectionPanel = new JPanel();
        connectionPanel.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        connectionPanel.setBackground(Color.blue);

        connectionPanel.setPreferredSize(new Dimension(24, 24));
        connectionPanel.setMinimumSize(new Dimension(24, 24));
        connectionPanel.setMaximumSize(new Dimension(24, 24));

        lblNetwork = new JLabel(networkIcons[0]);
        lblNetwork.setVerticalAlignment(JLabel.CENTER);
        lblNetwork.setHorizontalAlignment(JLabel.CENTER);
        lblNetwork.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        lblNetwork.addMouseListener(new MouseAdapter()
        {

            public void mouseClicked(MouseEvent e)
            {
                if (e.getClickCount() == 2)
                {
                    displayUpgradeDialog();
                }
            }
        });

        connectionPanel.add(lblNetwork);

        this.add(lblNetwork, "2, 0");

    }


    private void displayUpgradeDialog()
    {
        dataAccess.showDialog(dataAccess.getMainParent(), ATSwingUtils.DIALOG_INFO,
            "<html><b>There is no update available.</b><br><br>Functionality to check for new version and to<br>upgrade"
                    + " from server will become available in<br>version 0.8.</html>");
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
        this.lblDbLed.setIcon(statusIcons[status]);
    }


    /**
     * Set Database Name
     * 
     * @param dbName
     */
    public void setDatabaseName(String dbName)
    {
        lblDbName.setText(m_ic.getMessage("DATABASE") + " [" + dbName + "]:");
    }


    public void setUpgradeCheckStatus(UpgradeCheckStatus status)
    {
        this.upgradeCheckStatus = status;
    }


    /**
     * update - Observer method
     */
    public void update(Observable obj, Object arg)
    {
        // System.out.println("update status: " + arg);

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
            else if (s.startsWith("UPDATE_STATUS="))
            {
                // TODO
            }
            else
            {
                this.setStatusMessage(s);
            }
        }
    }


    public void actionPerformed(ActionEvent e)
    {
        System.out.println("Clicked: " + this.upgradeCheckStatus);
    }
}
