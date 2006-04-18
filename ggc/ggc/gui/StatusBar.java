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
 *  Filename: StatusBar.java
 *  Purpose:  A StatusBar for the MainFrame.
 *
 *  Author:   schultd
 */

package ggc.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.Border;

import org.hibernate.Hibernate;


public class StatusBar extends JPanel
{
    private JLabel lblMessage = null;
    private JLabel lblDataSource = null;
    private JLabel lblLed = null;
    private JLabel lblName = null;

    //private static StatusBar singleton = null;

    private ImageIcon[] statusIcons = null;

    public StatusBar()
    {

	statusIcons = new ImageIcon[3];

        statusIcons[0] = new ImageIcon(getClass().getResource("/icons/led_red.gif"));
        statusIcons[1] = new ImageIcon(getClass().getResource("/icons/led_yellow.gif"));
        statusIcons[2] = new ImageIcon(getClass().getResource("/icons/led_green.gif"));


        lblMessage = new JLabel();
        lblMessage.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        lblMessage.setPreferredSize(new Dimension(400, 18));

	/*
        lblDataSource = new JLabel();
        lblDataSource.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        lblDataSource.setPreferredSize(new Dimension(200, 18));
	*/

//        ImageIcon ii = new ImageIcon(getClass().getResource("/icons/led_red.gif"));

        JPanel pan = new JPanel();
        pan.setPreferredSize(new Dimension(385, 18));
        pan.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        SpringLayout layout = new SpringLayout();
        pan.setLayout(layout); //new java.awt.FlowLayout());


        lblName = new JLabel();
        lblName.setText("Database [Unknown]: ");
        pan.add(lblName);



        lblLed = new JLabel(statusIcons[0]);
        //lblLed.Se
        //(lblLed.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        //lblLed.setIcon(ii);
        //lblLed.setDisabledIcon(ii);
        //lblLed.setEnabledIcon(ii);
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


//        ImageIcon ii = new ImageIcon("/icons/led_gray.gif");
        //System.out.println(ii);

        //blLed.setText("");
        //lblLed.setPreferredSize(new Dimension(80, 18));


        setLayout(new BorderLayout(2, 2));
        this.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        add(lblMessage, BorderLayout.WEST);
        //add(lblDataSource, BorderLayout.CENTER);
        //add(lblLed, BorderLayout.EAST);
        add(pan, BorderLayout.EAST);

	//DataBaseHandler.getInstance().setStatus();

    }

    /*
    public static StatusBar getInstance()
    {
        if (singleton == null)
            singleton = new StatusBar();

        return singleton;
    }
    */

    public void setStatusMessage(String msg)
    {
        lblMessage.setText(" " + msg);
    }

    public void setDataSourceText(String text)
    {
        //lblDataSource.setText(" " + text);
    }

    public static final int DB_STOPPED = 0;
    public static final int DB_INIT = 0;
    public static final int DB_INIT_OK = 1;
    public static final int DB_LOAD = 2;

    public void setDbStatus(int status)
    {
        this.lblLed.setIcon(statusIcons[status]);
    }

    public void setDatabaseName(String dbName)
    {
	lblName.setText("Database [" + dbName +"]:");
    }

}
