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

package ggc.gui.prefPane;


import ggc.db.MySQLHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;


public class prefMySQLSetupPane extends AbstractPrefOptionsPanel
{
    private JTextField fieldHost;
    private JTextField fieldPort;
    private JTextField fieldUser;
    private JTextField fieldPass;
    private JTextField fieldDB;
    private JCheckBox chkOpenDefault;
    private JLabel lblState;
    private JTextArea errorText;

    public prefMySQLSetupPane()
    {
        init();
    }

    private void init()
    {
        JPanel a = new JPanel(new GridLayout(0, 1));
        a.add(new JLabel(m_ic.getMessage("HOST")+":"));
        a.add(new JLabel(m_ic.getMessage("PORT")+":"));
        a.add(new JLabel(m_ic.getMessage("USERNAME")+":"));
        a.add(new JLabel(m_ic.getMessage("PASSWORD")+":"));
        a.add(new JLabel(m_ic.getMessage("DEFAULT_DATABASE")+":"));
        a.add(new JLabel(""));


        JPanel b = new JPanel(new GridLayout(0, 1));
        b.add(fieldHost = new JTextField(props.getMySQLHost(), 10));
        b.add(fieldPort = new JTextField(props.getMySQLPort(), 10));
        b.add(fieldUser = new JTextField(props.getMySQLUser(), 10));
        b.add(fieldPass = new JTextField(props.getMySQLPass(), 10));
        b.add(fieldDB = new JTextField(props.getMySQLDBName(), 10));
        b.add(chkOpenDefault = new JCheckBox(m_ic.getMessage("OPEN_DEFAULT_DATABASE_UPON_CONNECT"), props.getMySQLOpenDefaultDB()));

        fieldHost.getDocument().addDocumentListener(this);
        fieldPort.getDocument().addDocumentListener(this);
        fieldUser.getDocument().addDocumentListener(this);
        fieldPass.getDocument().addDocumentListener(this);
        fieldDB.getDocument().addDocumentListener(this);
        chkOpenDefault.addActionListener(this);

        Box optionBox = Box.createHorizontalBox();
        optionBox.add(a);
        optionBox.add(b);

        JPanel optionPanel = new JPanel(new BorderLayout(5, 5));
        optionPanel.setBorder(BorderFactory.createTitledBorder("MySQL" + m_ic.getMessage("OPTIONS")));
        optionPanel.add(optionBox, BorderLayout.CENTER);

        JButton testButton = new JButton(m_ic.getMessage("CONNECT"));
        testButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                doTest();
            }
        });
        lblState = new JLabel(m_ic.getMessage("STATE")+":");
        errorText = new JTextArea(3, 10);
        errorText.setEditable(false);
        errorText.setLineWrap(true);
        errorText.setBorder(BorderFactory.createLineBorder(Color.black, 1));

        JPanel headerPanel = new JPanel(new BorderLayout(5, 5));
        headerPanel.add(testButton, BorderLayout.WEST);
        headerPanel.add(lblState, BorderLayout.CENTER);

        JPanel connectionPanel = new JPanel(new BorderLayout(5, 5));
        connectionPanel.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("TEST_CONNECTION")));
        connectionPanel.add(headerPanel, BorderLayout.NORTH);
        connectionPanel.add(errorText, BorderLayout.CENTER);

        Box myBox = Box.createVerticalBox();
        myBox.add(optionPanel);
        myBox.add(connectionPanel);

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
        props.set("MySQLOpenDefaultDB", chkOpenDefault.isSelected());
    }

    private void doTest()
    {
        MySQLHandler myH = new MySQLHandler();

        boolean b = false;
        String db = "";

        if (chkOpenDefault.isSelected())
            db = fieldDB.getText();

        errorText.setText("");

        try {
            b = myH.testDb(fieldHost.getText(), fieldPort.getText(), db, fieldUser.getText(), fieldPass.getText());
        } catch (ClassNotFoundException e) {
            errorText.setText(e.toString());
            b = false;
        } catch (SQLException e) {
            errorText.setText(e.toString());
            b = false;
        }
        if (b)
            lblState.setText("Connection Successfully");
        else
            lblState.setText("Error During Connecting");

        myH = null;
    }
}
