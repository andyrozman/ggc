package ggc.gui.dialogs.config.panels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.io.File;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import com.atech.data.user_data_dir.UserDataDirectory;
import com.atech.utils.ATSwingUtils;

import ggc.core.db.tool.DbToolApplicationGGC;
import ggc.gui.dialogs.config.PropertiesDialog;

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

public class PrefGeneralPane extends AbstractPrefOptionsPanel
{

    private static final long serialVersionUID = -5776476963040139761L;

    private JTextField fieldUserName;
    @SuppressWarnings("rawtypes")
    private JComboBox langBox, cb_database, cb_lf_type, cb_lf_type_class;
    private JTextField tf_lf;
    private JButton b_browse;
    DbToolApplicationGGC m_dbc = dataAccess.getDbConfig();
    UserDataDirectory userDataDirectory = UserDataDirectory.getInstance();


    /**
     * Constructor
     * 
     * @param dia
     */
    public PrefGeneralPane(PropertiesDialog dia)
    {
        super(dia);
        init();
        // dataAccess.enableHelp(this);
        // parent = this;
    }


    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void init()
    {
        this.setLayout(null);
        ATSwingUtils.initLibrary();

        JLabel label = new JLabel();
        label.setBounds(20, 5, 480, 25);
        label.setText(i18nControl.getMessage("ALL_SETTINGS_ON_PAGE_NEED_RESTART"));
        label.setFont(ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL_BOLD));
        this.add(label);

        // general ggcProperties
        JPanel a = new JPanel();
        a.setBounds(10, 30, 490, 90); // 40 = 10
        a.setLayout(null);
        a.setBorder(new TitledBorder(i18nControl.getMessage("GENERAL_SETTINGS")));

        label = new JLabel();
        label.setBounds(20, 20, 100, 25);
        label.setText(i18nControl.getMessage("YOUR_NAME") + ":");
        a.add(label);

        fieldUserName = new JTextField(configurationManagerWrapper.getUserName(), 10);
        fieldUserName.setBounds(170, 20, 300, 25);
        fieldUserName.getDocument().addDocumentListener(this);
        a.add(fieldUserName);

        label = new JLabel();
        label.setBounds(20, 50, 100, 25);
        label.setText(i18nControl.getMessage("YOUR_LANGUAGE") + ":");
        a.add(label);

        langBox = new JComboBox(dataAccess.getAvailableLanguages());
        langBox.setSelectedIndex(dataAccess.getSelectedLanguageIndex());
        langBox.setBounds(170, 50, 300, 25);
        a.add(langBox);

        this.add(a);

        // database ggcProperties
        JPanel p2 = new JPanel();
        p2.setBounds(10, 130, 490, 160);
        p2.setLayout(null);
        p2.setBorder(new TitledBorder(i18nControl.getMessage("DATABASE_SETTINGS")));

        label = new JLabel(i18nControl.getMessage("DATABASE_SETTINGS_DESC"));
        label.setBounds(20, 25, 440, 150);
        label.setVerticalAlignment(SwingConstants.TOP);
        p2.add(label);

        label = new JLabel(i18nControl.getMessage("SELECTED_DATABASE") + ":");
        label.setBounds(20, 120, 180, 25);
        p2.add(label);

        this.cb_database = new JComboBox(this.dataAccess.getDbConfig().getAllDatabasesNamesPlusAsArray());
        this.cb_database.setBounds(170, 120, 300, 25);
        this.cb_database.setSelectedIndex(this.dataAccess.getDbConfig().getSelectedDatabaseIndex());
        p2.add(this.cb_database);

        this.add(p2);

        JPanel p3 = new JPanel(/* new GridLayout(2, 1) */);
        p3.setBorder(new TitledBorder(i18nControl.getMessage("LAF_SETTINGS")));
        p3.setBounds(10, 300, 490, 115);
        p3.setLayout(null);

        addLabel(p3, i18nControl.getMessage("SELECTED_LAF_TYPE_NAME") + ":", 20, 20, 150, 25);
        addLabel(p3, i18nControl.getMessage("SELECTED_LAF_TYPE_CLASS") + ":", 20, 50, 150, 25);
        addLabel(p3, i18nControl.getMessage("SELECTED_SKINLF_DEF") + ":", 20, 80, 150, 25);

        this.cb_lf_type = new JComboBox(m_dbc.getAvailableLFs());
        this.cb_lf_type.setBounds(170, 20, 300, 23);
        this.cb_lf_type.addItemListener(this);
        p3.add(this.cb_lf_type);

        this.cb_lf_type_class = new JComboBox(m_dbc.getAvailableLFsClass());
        this.cb_lf_type_class.setBounds(170, 50, 300, 23);
        this.cb_lf_type_class.addItemListener(this);
        p3.add(this.cb_lf_type_class);

        this.tf_lf = new JTextField();
        this.tf_lf.setBounds(170, 80, 170, 23);
        this.tf_lf.setText(this.m_dbc.getSelectedLFSkin());
        this.tf_lf.setEditable(false);
        p3.add(this.tf_lf);

        // 20
        this.b_browse = new JButton(i18nControl.getMessage("BROWSE") + "...");
        this.b_browse.setBounds(350, 80, 120, 23);
        this.b_browse.addActionListener(new ActionListener()
        {

            public void actionPerformed(ActionEvent e)
            {
                File f = new File(userDataDirectory.getParsedUserDataPath("%USER_DATA_DIR%/skinlf_themes"));

                // System.out.println(f);
                // System.out.println(f.getAbsolutePath());

                JFileChooser jfc = new JFileChooser();
                jfc.setCurrentDirectory(f);
                jfc.setDialogTitle(i18nControl.getMessage("SELECT_SKINLF_SKIN"));
                jfc.setDialogType(JFileChooser.CUSTOM_DIALOG);
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

                processJFileChooser(jfc);

                int res = jfc.showDialog(parent, i18nControl.getMessage("SELECT"));

                if (res == JFileChooser.APPROVE_OPTION)
                {
                    tf_lf.setText(jfc.getSelectedFile().getName());
                }

            }
        });

        p3.add(this.b_browse);

        int idx = this.m_dbc.getSelectedLFIndex();
        this.cb_lf_type.setSelectedIndex(idx);

        this.add(p3);

        registerForChange(cb_database);

    }


    private void addLabel(JPanel panel, String text, int x, int y, int width, int height)
    {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        panel.add(label);
    }

    boolean in_change = false;


    /**
     * {@inheritDoc}
     */
    @Override
    public void itemStateChanged(ItemEvent e)
    {
        if (in_change)
            return;
        else
        {
            in_change = true;
        }

        JComboBox cb = (JComboBox) e.getSource();

        int index = cb.getSelectedIndex();

        if (this.cb_lf_type.equals(cb))
        {
            this.cb_lf_type_class.setSelectedIndex(index);
        }
        else
        {
            this.cb_lf_type.setSelectedIndex(index);
        }

        boolean skin = m_dbc.isSkinLFSelected(index);

        // this.tf_lf.setEnabled(skin);
        this.b_browse.setEnabled(skin);

        in_change = false;

        this.changed = true;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void saveProps()
    {
        configurationManagerWrapper.setUserName(fieldUserName.getText());
        ggcProperties.setLanguage(langBox.getSelectedItem().toString());

        this.m_dbc.setSelectedDatabaseIndex(this.cb_database.getSelectedIndex());
        this.m_dbc.setSelectedLF(this.cb_lf_type.getSelectedIndex(), this.tf_lf.getText());
    }


    @SuppressWarnings("rawtypes")
    private void processJFileChooser(Container c)
    {
        Component[] comps = c.getComponents();

        for (Component comp : comps)
        {

            if (comp instanceof JPanel)
            {
                processJFileChooser((Container) comp);
            }

            if (comp instanceof JButton)
            {
                JButton b = (JButton) comp;

                String ttText = b.getToolTipText();
                // x String buttonText = b.getText();

                if (ttText != null
                        && (ttText.equals("Create New Folder") || ttText.equals("Desktop") || ttText
                                .equals("Up One Level")))
                {
                    b.setEnabled(false);
                }
            }

            if (comp instanceof JComboBox)
            {
                JComboBox box = (JComboBox) comp;
                String s = box.getSelectedItem().toString();
                if (s.indexOf("skinlf_themes") != -1)
                {
                    box.setEnabled(false);
                }
            }

        }
    }


    /**
     * {@inheritDoc}
     */
    public String getHelpId()
    {
        return "GGC_Prefs_General";
    }

}
