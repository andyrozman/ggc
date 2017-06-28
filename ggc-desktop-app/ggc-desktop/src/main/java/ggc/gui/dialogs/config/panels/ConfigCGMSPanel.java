package ggc.gui.dialogs.config.panels;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import com.atech.utils.ATSwingUtils;

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
 *  Filename:     PrefModePane
 *  Description:  Mode Preferences: Software mode and Pen/Injection and Pen ggcProperties
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class ConfigCGMSPanel extends AbstractPrefOptionsPanel
{

    private static final long serialVersionUID = 2814137743820948729L;

    JCheckBox[] chkFields;

    private static final int GRAPH_SHOW_IN_PEN_DAILY = 0;
    private static final int GRAPH_SHOW_IN_PUMP_DAILY = 1;


    /**
     * Constructor
     *
     * @param dia
     */
    public ConfigCGMSPanel(PropertiesDialog dia)
    {
        super(dia);
        ATSwingUtils.initLibrary();
        init();
    }


    private void init()
    {
        this.setLayout(null);

        chkFields = new JCheckBox[2];

        // Graphs

        JPanel p1 = new JPanel();
        p1.setBounds(10, 5, 490, 90);
        p1.setLayout(null);
        p1.setBorder(new TitledBorder(i18nControl.getMessage("CGMS_GRAPHS")));

        chkFields[GRAPH_SHOW_IN_PEN_DAILY] = ATSwingUtils.getCheckBox(
            "   " + i18nControl.getMessage("SHOW_CGMS_IN_PEN_INJECTION_DAILY"), 25, 25, 400, 25, p1,
            ATSwingUtils.FONT_NORMAL);
        chkFields[GRAPH_SHOW_IN_PEN_DAILY]
                .setSelected(this.configurationManagerWrapper.getUseCGMSDataInPenDailyGraph());

        chkFields[GRAPH_SHOW_IN_PUMP_DAILY] = ATSwingUtils.getCheckBox(
            "   " + i18nControl.getMessage("SHOW_CGMS_IN_PUMP_DAILY"), 25, 55, 400, 25, p1, ATSwingUtils.FONT_NORMAL);
        chkFields[GRAPH_SHOW_IN_PUMP_DAILY]
                .setSelected(this.configurationManagerWrapper.getUseCGMSDataInPumpDailyGraph());

        this.add(p1);

        registerForChange(chkFields);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void saveProps()
    {
        configurationManagerWrapper.setUseCGMSDataInPenDailyGraph(chkFields[GRAPH_SHOW_IN_PEN_DAILY].isSelected());
        configurationManagerWrapper.setUseCGMSDataInPumpDailyGraph(chkFields[GRAPH_SHOW_IN_PUMP_DAILY].isSelected());
    }


    /**
     * {@inheritDoc}
     */
    public String getHelpId()
    {
        return "GGC_Prefs_Mode";
    }

}
