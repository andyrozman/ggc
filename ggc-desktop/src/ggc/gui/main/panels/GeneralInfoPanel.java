package ggc.gui.main.panels;

import java.awt.*;
import java.util.Hashtable;

import javax.swing.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ggc.core.data.defs.GlucoseUnitType;
import ggc.core.util.DataAccess;
import ggc.core.util.GGCSoftwareMode;

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
 *  Filename:     GeneralInfoPanel  
 *  Description:  Panel to display general info
 *
 *  Author: schultd
 *          andyrozman {andy@atech-software.com}  
 */

public class GeneralInfoPanel extends AbstractInfoPanel
{

    private static final long serialVersionUID = 6567668717839226425L;

    private JLabel lblName = new JLabel();
    private JLabel lblIns1 = new JLabel();
    private JLabel lblIns2 = new JLabel();
    private JLabel lblUnit = new JLabel();
    private JLabel lblTarget = new JLabel();
    private JLabel lbl_pump_insulin = new JLabel();

    private GGCSoftwareMode currentMode = null;
    private Hashtable<String, JLabel> list_elems;
    private static Log log = LogFactory.getLog(GeneralInfoPanel.class);

    String[] pen_mode = { "name", "name_value", "bolus", "bolus_value", "basal", "basal_value", "bg_unit",
                          "bg_unit_value", "bg_target", "bg_target_value" };

    String[] pump_mode = { "name", "name_value", "pump_insulin", "pump_insulin_value", "bg_unit", "bg_unit_value",
                           "bg_target", "bg_target_value" };


    /**
     * Constructor
     */
    public GeneralInfoPanel()
    {
        super("GENERAL_INFORMATION");
        init();
        refreshInfo();
    }


    private void init()
    {
        setLayout(new GridLayout(0, 2));
        list_elems = new Hashtable<String, JLabel>();

        addToList("name", new JLabel(m_ic.getMessage("YOUR_NAME") + ":"));
        addToList("name_value", lblName);
        addToList("bolus", new JLabel(m_ic.getMessage("BOLUS_INSULIN") + ":"));
        addToList("bolus_value", lblIns1);
        addToList("basal", new JLabel(m_ic.getMessage("BASAL_INSULIN") + ":"));
        addToList("basal_value", lblIns2);
        addToList("bg_unit", new JLabel(m_ic.getMessage("BG_UNIT") + ":"));
        addToList("bg_unit_value", lblUnit);
        addToList("bg_target", new JLabel(m_ic.getMessage("BG_TARGET") + ":"));
        addToList("bg_target_value", lblTarget);
        addToList("pump_insulin", new JLabel(m_ic.getMessage("PUMP_INSULIN") + ":"));
        addToList("pump_insulin_value", this.lbl_pump_insulin);

        // add(new JLabel(i18nControl.getMessage("NUTRITION_PLUGIN")+":"));
        // add(lblNutri);
        /*
         * add(new JLabel(i18nControl.getMessage("METERS_PLUGIN")+":"));
         * add(lblMeter);
         * add(new JLabel(i18nControl.getMessage("PUMPS_PLUGIN")+":"));
         * add(lblPumps);
         * add(new JLabel(i18nControl.getMessage("CGMS_PLUGIN")+":"));
         * add(lblCGMS);
         */
        // add(new JLabel());
        // add(new JLabel());

        changeMode(GGCSoftwareMode.PEN_INJECTION_MODE);

    }


    private void changeMode(GGCSoftwareMode newMode)
    {
        if (newMode == null)
            return;

        if ((this.currentMode != null) && (newMode.equals(this.currentMode)))
            return;

        this.removeAll();
        this.currentMode = newMode;

        String[] sel_elements = null;

        if (newMode.equals(GGCSoftwareMode.PEN_INJECTION_MODE))
        {
            sel_elements = this.pen_mode;
        }
        else if (newMode.equals(GGCSoftwareMode.PUMP_MODE))
        {
            sel_elements = this.pump_mode;
        }
        else
        {
            log.warn("Error setting mode in General Panel: " + newMode);
            return;
        }

        for (String sel_element : sel_elements)
        {
            this.add(this.list_elems.get(sel_element));
        }

    }


    private void addToList(String keyword, JLabel item)
    {
        this.list_elems.put(keyword, item);
    }


    /**
     * Refresh Information 
     */
    @Override
    public void refreshInfo()
    {
        if (!this.m_da.isDatabaseInitialized())
            return;

        lblName.setText(configurationManagerWrapper.getUserName());
        lblIns1.setText(getInsulins(DataAccess.INSULIN_DOSE_BOLUS));
        lblIns2.setText(getInsulins(DataAccess.INSULIN_DOSE_BASAL));

        GlucoseUnitType glucoseUnitType = configurationManagerWrapper.getGlucoseUnit();

        lblUnit.setText(glucoseUnitType.getTranslation());

        // int unit = configurationManagerWrapper.getBG_unit();

        float min, max;

        if (glucoseUnitType == GlucoseUnitType.mg_dL)
        {
            min = configurationManagerWrapper.getBG1TargetLow();
            max = configurationManagerWrapper.getBG1TargetHigh();
        }
        else
        {
            min = configurationManagerWrapper.getBG2TargetLow();
            max = configurationManagerWrapper.getBG2TargetHigh();
        }

        float avg = (float) ((min + max) / 2.0);

        // String s = dataAccess.getConfigurationManager().getFloatValue(key)

        lblTarget.setText(DataAccess.Decimal1Format.format(min) + " - " + DataAccess.Decimal1Format.format(max) + " ["
                + DataAccess.Decimal1Format.format(avg) + "]");

        this.lbl_pump_insulin.setText(configurationManagerWrapper.getPumpInsulin());

        changeMode(DataAccess.getInstance().getSoftwareMode());

    }


    private String getInsulins(int type)
    {
        String values = "";

        if (configurationManagerWrapper.getIns1Type() == type)
        {
            values += configurationManagerWrapper.getIns1Name() + " [1]";
        }

        if (configurationManagerWrapper.getIns2Type() == type)
        {
            values = addString(values, configurationManagerWrapper.getIns2Name() + " [2]");
        }

        if (configurationManagerWrapper.getIns3Type() == type)
        {
            values = addString(values, configurationManagerWrapper.getIns3Name() + " [3]");
        }

        return values;
    }


    private String addString(String full, String entry)
    {
        if (full.length() > 0)
        {
            full += ", ";
        }

        full += entry;

        return full;
    }


    /**
     * Get Tab Name
     * 
     * @return name as string
     */
    @Override
    public String getTabName()
    {
        return "GeneralInfo";
    }


    /**
     * Do Refresh - This method can do Refresh
     */
    @Override
    public void doRefresh()
    {
        refreshInfo();
    }


    /**
     * Get Panel Id
     * 
     * @return id of panel
     */
    @Override
    public int getPanelId()
    {
        return InfoPanelsIds.INFO_PANEL_GENERAL;
    }

}
