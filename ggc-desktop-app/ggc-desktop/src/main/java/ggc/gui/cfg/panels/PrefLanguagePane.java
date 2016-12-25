package ggc.gui.cfg.panels;

import javax.swing.*;

import com.atech.utils.ATSwingUtils;
import ggc.gui.cfg.PropertiesDialog;

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

public class PrefLanguagePane extends AbstractPrefOptionsPanel
{

    private static final long serialVersionUID = -7951563510571694100L;


    /**
     * Constructor
     * 
     * @param dia
     */
    public PrefLanguagePane(PropertiesDialog dia)
    {
        super(dia);
        ATSwingUtils.initLibrary();
        init();

        // dataAccess.enableHelp(this);
        // parent = this;
    }


    private void init()
    {
        this.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(0, 10, 470, 450);
        panel.setLayout(null);

        // ATSwingUtils

        ATSwingUtils.getTitleLabel(i18nControl.getMessage("LANGUAGE"), 0, 0, 500, 36, panel,
            ATSwingUtils.FONT_BIG_BOLD);

        /*
         * JLabel label = new
         * JLabel(i18nControlAbstract.getMessage("LANGUAGE"));
         * label.setFont(font_big);
         * label.setBounds(0, 05, 420, 36);
         * label.setHorizontalAlignment(JLabel.CENTER);
         * panel.add(label);
         */

        ATSwingUtils.getLabel(i18nControl.getMessage("LANGPACK_VERSION") + ":", 40, 50, 360, 25, panel,
            ATSwingUtils.FONT_NORMAL_BOLD);

        /*
         * XA
         * label = new JLabel(i18nControlAbstract.getMessage("LANGPACK_VERSION")
         * + ":");
         * label.setFont(font_normal_b);
         * label.setBounds(40, 50, 360, 25);
         * panel.add(label);
         */
        /*
         * label = new JLabel("v" + langInfo.languagePackVersion + " (" +
         * langInfo.languagePackRelease + ")");
         * label.setFont(font_normal);
         * label.setBounds(180, 50, 360, 25);
         * panel.add(label);
         */

        ATSwingUtils.getLabel(i18nControl.getMessage("LANG_WITH_HELP") + ":", 60, 70, 360, 25, panel,
            ATSwingUtils.FONT_NORMAL_BOLD);

        /*
         * label = new JLabel(i18nControlAbstract.getMessage("LANG_WITH_HELP") +
         * ":");
         * label.setFont(font_normal_b);
         * label.setBounds(60, 70, 360, 25);
         * panel.add(label);
         */
        /*
         * label = new JLabel("" + langInfo.languagePackLanguagesWithHelp);
         * label.setFont(font_normal);
         * label.setBounds(200, 70, 360, 25);
         * panel.add(label);
         */

        ATSwingUtils.getLabel(i18nControl.getMessage("LANG_WITHOUT_HELP") + ":", 60, 90, 360, 25, panel,
            ATSwingUtils.FONT_NORMAL_BOLD);

        /*
         * label = new
         * JLabel(i18nControlAbstract.getMessage("LANG_WITHOUT_HELP") + ":");
         * label.setFont(font_normal_b);
         * label.setBounds(60, 90, 360, 25);
         * panel.add(label);
         */
        /*
         * label = new JLabel("" + langInfo.languagePackLanguagesWithoutHelp);
         * label.setFont(font_normal);
         * label.setBounds(200, 90, 360, 25);
         * panel.add(label);
         */

        ATSwingUtils.getLabel(i18nControl.getMessage("LANG_DESC"), 40, 100, 430, 200, panel, ATSwingUtils.FONT_NORMAL);

        ATSwingUtils.getLabel(i18nControl.getMessage("NOTE_RESTART"), 40, 280, 360, 50, panel,
            ATSwingUtils.FONT_NORMAL);
            /*
             * ATSwingUtils.getLabel(i18nControl.getMessage("SELECT_LANGUAGE"),
             * 40, 345, 340, 25,
             * panel, ATSwingUtils.FONT_NORMAL_BOLD);
             */

        /*
         * cb_DBs = new JComboBox(langInfo.availableLang);
         * cb_DBs.setBounds(40, 280, 300, 25);
         * cb_DBs.setSelectedIndex(dataAccess.getSelectedLangIndex() - 1);
         * cb_DBs.setFont(font_normal);
         * cb_DBs.addItemListener(this);
         * cb_DBs.setActionCommand("select_lang");
         * panel.add(cb_DBs);
         */

        ATSwingUtils.getLabel(i18nControl.getMessage("NOTE_LANG_FEATURE_NOT_WORKING"), 40, 325, 430, 70, panel,
            ATSwingUtils.FONT_NORMAL_BOLD);

        this.add(panel);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void saveProps()
    {
        System.out.println("PrefLanguagePane::saveProps(): N/A");
        /*
         * ggcProperties.setUserName(fieldUserName.getText());
         * ggcProperties.setLanguage(langBox.getSelectedItem().toString());
         * this.m_dbc.setSelectedDatabaseIndex(this.cb_database.
         * getSelectedIndex(
         * ));
         * this.m_dbc.setSelectedLF(this.cb_lf_type.getSelectedIndex(),
         * this.tf_lf.getText());
         */
    }


    /**
     * {@inheritDoc}
     */
    public String getHelpId()
    {
        return "GGC_Prefs_Language";
    }

}
