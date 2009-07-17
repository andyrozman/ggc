package ggc.gui.panels.prefs;

import ggc.gui.dialogs.PropertiesDialog;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.atech.help.HelpCapable;
import com.atech.utils.ATSwingUtils;

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


public class PrefLanguagePane extends AbstractPrefOptionsPanel implements HelpCapable
{
    private static final long serialVersionUID = -7951563510571694100L;

    //private JTextField fieldUserName;
    //private JComboBox langBox, cb_database, cb_lf_type, cb_lf_type_class;
    //private JTextField tf_lf;
    //private JButton b_browse;


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
        
        // m_da.enableHelp(this);
        // parent = this;
    }

    private void init()
    {
        this.setLayout(null);
        
        JPanel panel = new JPanel();
        panel.setBounds(0, 10, 470, 450);
        panel.setLayout(null);

        

        //ATSwingUtils
        
        ATSwingUtils.getTitleLabel(m_ic.getMessage("LANGUAGE"), 
                                   0, 0, 500, 36, panel, ATSwingUtils.FONT_BIG_BOLD);
        
        /*
        JLabel label = new JLabel(ic.getMessage("LANGUAGE"));
        label.setFont(font_big);
        label.setBounds(0, 05, 420, 36);
        label.setHorizontalAlignment(JLabel.CENTER);
        panel.add(label); */

        ATSwingUtils.getLabel(m_ic.getMessage("LANGPACK_VERSION") + ":", 
                              40, 50, 360, 25, 
                              panel, ATSwingUtils.FONT_NORMAL_BOLD);

        /* XA
        label = new JLabel(ic.getMessage("LANGPACK_VERSION") + ":");
        label.setFont(font_normal_b);
        label.setBounds(40, 50, 360, 25);
        panel.add(label); */
/*
        label = new JLabel("v" + langInfo.lp_version + " (" + langInfo.lp_release + ")");
        label.setFont(font_normal);
        label.setBounds(180, 50, 360, 25);
        panel.add(label);
*/
        
        ATSwingUtils.getLabel(m_ic.getMessage("LANG_WITH_HELP") + ":", 
            60, 70, 360, 25, 
            panel, ATSwingUtils.FONT_NORMAL_BOLD);
        
        
/*        label = new JLabel(ic.getMessage("LANG_WITH_HELP") + ":");
        label.setFont(font_normal_b);
        label.setBounds(60, 70, 360, 25);
        panel.add(label); */
/*
        label = new JLabel("" + langInfo.lp_langs_with_help);
        label.setFont(font_normal);
        label.setBounds(200, 70, 360, 25);
        panel.add(label);
*/

        ATSwingUtils.getLabel(m_ic.getMessage("LANG_WITHOUT_HELP") + ":", 
            60, 90, 360, 25, 
            panel, ATSwingUtils.FONT_NORMAL_BOLD);
        
        
/*        label = new JLabel(ic.getMessage("LANG_WITHOUT_HELP") + ":");
        label.setFont(font_normal_b);
        label.setBounds(60, 90, 360, 25);
        panel.add(label); */
/*
        label = new JLabel("" + langInfo.lp_langs_without_help);
        label.setFont(font_normal);
        label.setBounds(200, 90, 360, 25);
        panel.add(label);
*/

        ATSwingUtils.getLabel(m_ic.getMessage("LANG_DESC"), 
            40, 100, 430, 200, 
            panel, ATSwingUtils.FONT_NORMAL);
        
        ATSwingUtils.getLabel(m_ic.getMessage("NOTE_RESTART"), 
            40, 280, 360, 50, 
            panel, ATSwingUtils.FONT_NORMAL);
/*
        ATSwingUtils.getLabel(m_ic.getMessage("SELECT_LANGUAGE"), 
            40, 345, 340, 25,
            panel, ATSwingUtils.FONT_NORMAL_BOLD);
*/        

/*
        cb_DBs = new JComboBox(langInfo.availableLang);
        cb_DBs.setBounds(40, 280, 300, 25);
        cb_DBs.setSelectedIndex(m_da.getSelectedLangIndex() - 1);
        cb_DBs.setFont(font_normal);
        cb_DBs.addItemListener(this);
        cb_DBs.setActionCommand("select_lang");
        panel.add(cb_DBs);
*/
        
        ATSwingUtils.getLabel(m_ic.getMessage("NOTE_LANG_FEATURE_NOT_WORKING"), 
            40, 325, 430, 70,
            panel, ATSwingUtils.FONT_NORMAL_BOLD);
        
        
        
        this.add(panel);

    }


    
    /**
     * Save Properties
     * 
     * @see ggc.gui.panels.prefs.AbstractPrefOptionsPanel#saveProps()
     */
    @Override
    public void saveProps()
    {
        System.out.println("PrefLanguagePane::saveProps(): N/A");
        /*
        settings.setUserName(fieldUserName.getText());
        settings.setLanguage(langBox.getSelectedItem().toString());

        this.m_dbc.setSelectedDatabaseIndex(this.cb_database.getSelectedIndex());
        this.m_dbc.setSelectedLF(this.cb_lf_type.getSelectedIndex(), this.tf_lf.getText());
        */
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
        return "pages.GGC_Prefs_General";
    }

}
