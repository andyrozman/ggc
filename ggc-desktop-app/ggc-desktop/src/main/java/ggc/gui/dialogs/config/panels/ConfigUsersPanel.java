package ggc.gui.dialogs.config.panels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import com.atech.db.hibernate.hdb_object.User;
import com.atech.db.hibernate.hdb_object.UserH;
import com.atech.db.table.DbUserTable;
import com.atech.gui_fw.config.ConfigUserDialog;
import com.atech.help.HelpCapable;
import com.atech.utils.ATSwingUtils;

import ggc.gui.dialogs.config.PropertiesDialog;

/**
 *  This file is part of ATech Tools library.
 *  
 *  ConfigDbPanel - This class is for setting database
 *  
 *  Copyright (C) 2008  Andy (Aleksander) Rozman (Atech-Software)
 *  
 *  
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA 
 *  
 *  
 *  For additional information about this project please visit our project site on 
 *  http://atech-tools.sourceforge.net/ or contact us via this emails: 
 *  andyrozman@users.sourceforge.net or andy@atech-software.com
 *  
 *  @author Andy
 *
*/

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
 *  Filename:     PrefGeneralPane
 *  Description:  General Preferences: Name, Database selection, Look & Feel
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class ConfigUsersPanel extends AbstractPrefOptionsPanel implements HelpCapable, ActionListener
{

    private static final long serialVersionUID = -6552718578105574894L;

    // private JComboBox cb_database;
    // private JTextField tf_lf;
    // private JButton b_browse;
    // DbToolApplicationAbstract m_dbc = null; //dataAccess.getDbConfig();
    JList jListUsers = null;
    ArrayList<User> m_listUsers = null;


    /**
     * Constructor
     *
     * @param dia
     */
    public ConfigUsersPanel(PropertiesDialog dia)
    {
        super(dia);
        ATSwingUtils.initLibrary();
        init();
    }


    private void init()
    {

        // JPanel panel = new JPanel();
        // panel.setBounds(140, 30, 420, 330);
        this.setLayout(null);

        ATSwingUtils.getTitleLabel(i18nControl.getMessage("CFG_USERS"), 0, 35, 520, 36, this,
            ATSwingUtils.FONT_BIG_BOLD);

        // JLabel label = ATSwingUtils.getLabel(i18nControl.getMessage("USERS"),
        // 0, 25,
        // 520, 36, this, ATSwingUtils.FONT_BIG_BOLD);
        // label.setHorizontalAlignment(JLabel.CENTER);

        jListUsers = new JList();
        jListUsers.setFont(ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL));

        JScrollPane pane = new JScrollPane(jListUsers);
        pane.setBounds(60, 100, 390, 180);
        this.add(pane);

        addButton("ADD", 110, 300, 90, 65, "usr_add.png", "users_add", "ADD_USER");
        addButton("EDIT", 210, 300, 90, 65, "usr_edit.png", "users_edit", "EDIT_USER");
        addButton("REMOVE", 310, 300, 90, 65, "usr_delete.png", "users_remove", "REMOVE_DESC");
        // addButton("DEFAULT", 360, 300, 90, 65, "usr_default.png",
        // "users_set_default", "DEFAULT_DESC");

        // JButton b = ATSwingUtils.getButton("ADD", 60, 270, 100, 75, this,
        // ATSwingUtils.FONT_NORMAL, "usr_add.png",
        // "users_add", this, dataAccess);
        // b.setHorizontalTextPosition(JButton.CENTER);
        // b.setVerticalTextPosition(JButton.BOTTOM);

        JButton button = new JButton(i18nControl.getMessage("ADD"));
        button.setBounds(60, 270, 100, 25);
        button.setActionCommand("users_add");
        button.setFont(ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL));
        button.addActionListener(this);
        // this.add(button);

        button = new JButton(i18nControl.getMessage("EDIT"));
        button.setBounds(160, 270, 100, 25);
        button.setActionCommand("users_edit");
        button.setFont(ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL));
        button.addActionListener(this);
        // this.add(button);

        button = new JButton(i18nControl.getMessage("REMOVE"));
        button.setBounds(260, 270, 100, 25);
        button.setActionCommand("users_remove");
        button.setFont(ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL));
        button.addActionListener(this);
        // this.add(button);

        // panel.setVisible(false);
        // panels[4] = panel;

    }

    int[] iconSize = { 35, 35 };


    private void addButton(String i18nKey, int x, int y, int width, int height, String icon, String actionCommand,
            String tooltip)
    {
        JButton b = ATSwingUtils.getButton(i18nControl.getMessage(i18nKey), x, y, width, height, this,
            ATSwingUtils.FONT_NORMAL, icon, actionCommand, this, dataAccess, iconSize);
        b.setHorizontalTextPosition(JButton.CENTER);
        b.setVerticalTextPosition(JButton.BOTTOM);
        b.setToolTipText(i18nControl.getMessage(tooltip));
    }


    @Override
    public void actionPerformed(ActionEvent ae)
    {
        changed = true;
        String action = ae.getActionCommand();

        if (action.equals("users_add"))
        {
            ConfigUserDialog ud = new ConfigUserDialog(dataAccess, this.getParentDialog());

            if (ud.wasOperationSuccessful())
            {
                m_listUsers = this.getUserDb().getUsers();
                ATSwingUtils.populateJListExtended(jListUsers, m_listUsers, UserH.class);
            }

        }
        else if (action.equals("users_edit"))
        {

            if (jListUsers.isSelectionEmpty())
            {
                JOptionPane.showConfirmDialog(this, i18nControl.getMessage("SELECT_ITEM_FIRST"),
                    i18nControl.getMessage("ERROR"), JOptionPane.CLOSED_OPTION);
                return;
            }

            // System.out.println("Index: " + getSelectedUserIndex());

            User us = m_listUsers.get(getSelectedUserIndex());
            ConfigUserDialog ud = new ConfigUserDialog(dataAccess, this.getParentDialog(), us, true);

            if (ud.wasOperationSuccessful())
            {
                m_listUsers = this.getUserDb().getUsers();
                populateJListExtended(1, m_listUsers);
            }

        }
        else if (action.equals("users_remove"))
        {

            if (jListUsers.isSelectionEmpty())
            {
                JOptionPane.showConfirmDialog(this, i18nControl.getMessage("SELECT_ITEM_FIRST"),
                    i18nControl.getMessage("ERROR"), JOptionPane.CLOSED_OPTION);
                return;
            }

            int ii = JOptionPane.showConfirmDialog(this, i18nControl.getMessage("ARE_YOU_SURE_DELETE"),
                i18nControl.getMessage("WARNING"), JOptionPane.YES_NO_OPTION);

            if (ii == JOptionPane.YES_OPTION)
            {
                User us = m_listUsers.get(jListUsers.getSelectedIndex());
                dataAccess.getHibernateDb().delete(us);
                m_listUsers = this.getUserDb().getUsers();
                populateJListExtended(1, m_listUsers);
            }
            else
                return;

        }

    }


    @Override
    public void saveProps()
    {
        // ATSwingUtils
    }


    /**
     * Populate JLists
     */
    private void populateJListExtended(int type, ArrayList<?> input)
    {

        DefaultListModel newListModel = new DefaultListModel();

        for (int i = 0; i < input.size(); i++)
        {

            boolean add = false;

            if (input.get(i) instanceof User)
            {
                add = true;
            }
            else
            {
                System.out.println("ConfigurationDialog::Unknown type: " + input.get(i));
            }

            if (type == 1)
            {
                User u = (User) input.get(i);

                if (u.default_user)
                {
                    add = false;
                }

            }

            if (add)
            {
                newListModel.addElement(input.get(i));
            }
        }

        if (type == 1)
        {
            jListUsers.setModel(newListModel);
        }

    }


    private int getSelectedUserIndex()
    {

        String name = jListUsers.getSelectedValue().toString();

        name = name.substring(0, name.indexOf("("));

        // System.out.println("Name: '" + name + "'");

        for (int i = 0; i < this.m_listUsers.size(); i++)
        {
            User us = m_listUsers.get(i);

            if (us.getUsername().equals(name))
                return i;
        }

        return -1;
    }

    // /**
    // * Save Properties
    // *
    // * @see ggc.gui.panels.prefs.AbstractPrefOptionsPanel#saveProps()
    // */
    // @Override
    // public void saveConfig()
    // {
    // // settings.setUserName(fieldUserName.getText());
    // // settings.setLanguage(langBox.getSelectedItem().toString());
    //
    // //
    // this.m_dbc.setSelectedDatabaseIndex(this.cb_database.getSelectedIndex());
    // // this.m_dbc.setSelectedLF(this.cb_lf_type.getSelectedIndex(),
    // // this.tf_lf.getText());
    // }


    /*
     * private void processJFileChooser(Container c)
     * {
     * Component[] comps = c.getComponents();
     * for (int i = 0; i < comps.length; i++)
     * {
     * if (comps[i] instanceof JPanel)
     * {
     * processJFileChooser((Container) comps[i]);
     * }
     * if (comps[i] instanceof JButton)
     * {
     * JButton b = (JButton) comps[i];
     * String ttText = b.getToolTipText();
     * // x String buttonText = b.getText();
     * if ((ttText != null)
     * && (ttText.equals("Create New Folder") || ttText.equals("Desktop") ||
     * ttText
     * .equals("Up One Level")))
     * {
     * b.setEnabled(false);
     * }
     * }
     * if (comps[i] instanceof JComboBox)
     * {
     * JComboBox box = (JComboBox) comps[i];
     * String s = box.getSelectedItem().toString();
     * if (s.indexOf("skinlf_themes") != -1)
     * {
     * box.setEnabled(false);
     * }
     * }
     * }
     * }
     */

    private DbUserTable getUserDb()
    {
        return (DbUserTable) dataAccess.getHibernateDb();
    }


    // ****************************************************************
    // ****** HelpCapable Implementation *****
    // ****************************************************************

    /**
     * getComponent - get component to which to attach help context
     */
    public Component getComponent()
    {
        return this.getRootPane();
    }


    /**
     * getHelpButton - get Help button
     */
    public JButton getHelpButton()
    {
        return this.parent.getHelpButton();
    }


    /**
     * getHelpId - get id for Help
     */
    public String getHelpId()
    {
        return "GGC_Prefs_General";
    }

}
