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
 *  Filename: prefMySQLSetupPane.java
 *  Purpose:  Setup a connection to a MySQL database.
 *
 *  Author:   schultd
 */

package gui.prefPane;


import javax.swing.*;
import java.awt.*;


public class prefMySQLSetupPane extends AbstractPrefOptionsPanel
{
    private JTextField fieldHost;
    private JTextField fieldPort;
    private JTextField fieldUser;
    private JTextField fieldPass;
    private JTextField fieldDB;

    public prefMySQLSetupPane()
    {
        init();
    }

    private void init()
    {
        JPanel a = new JPanel(new GridLayout(0, 1));
        a.add(new JLabel("Host:"));
        a.add(new JLabel("Port:"));
        a.add(new JLabel("Username:"));
        a.add(new JLabel("Password:"));
        a.add(new JLabel("Default DataBase:"));


        JPanel b = new JPanel(new GridLayout(0, 1));
        b.add(fieldHost = new JTextField(props.getMySQLHost(), 10));
        b.add(fieldPort = new JTextField(props.getMySQLPort(), 10));
        b.add(fieldUser = new JTextField(props.getMySQLUser(), 10));
        b.add(fieldPass = new JTextField(props.getMySQLPass(), 10));
        b.add(fieldDB   = new JTextField(props.getMySQLDBName(), 10));

        fieldHost.getDocument().addDocumentListener(this);
        fieldPort.getDocument().addDocumentListener(this);
        fieldUser.getDocument().addDocumentListener(this);
        fieldPass.getDocument().addDocumentListener(this);
        fieldDB.getDocument().addDocumentListener(this);

        Box myBox = Box.createHorizontalBox();
        myBox.add(a);
        myBox.add(b);

        setLayout(new BorderLayout());

        add(myBox, BorderLayout.NORTH);
    }

    public void saveProps()
    {
        props.set("MySQLHost", fieldHost.getText());
        props.set("MySQLPort", fieldPort.getText());
        props.set("MySQLUser", fieldUser.getText());
        props.set("MySQLPass", fieldPass.getText());
        props.set("MySQLDBName", fieldDB.getText());
    }
}
