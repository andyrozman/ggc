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

package gui;


import javax.swing.*;
import java.awt.*;


public class StatusBar extends JPanel
{
    private JLabel lblMessage;
    private JLabel lblDataSource;

    private static StatusBar singleton = null;

    public StatusBar()
    {
        lblMessage = new JLabel();
        lblMessage.setBorder(BorderFactory.createLineBorder(Color.gray, 1));

        lblDataSource = new JLabel();
        lblDataSource.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        lblDataSource.setPreferredSize(new Dimension(300, 14));

        setLayout(new BorderLayout(2, 2));
        this.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        add(lblMessage, BorderLayout.CENTER);
        add(lblDataSource, BorderLayout.EAST);
    }

    public static StatusBar getInstance()
    {
        if (singleton == null)
            singleton = new StatusBar();
        return singleton;
    }

    public void setStatusMessage(String msg)
    {
        lblMessage.setText(" " + msg);
    }

    public void setDataSourceText(String text)
    {
        lblDataSource.setText(" " + text);
    }
}
