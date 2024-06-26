package ggc.gui.main.panels;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static Logger log = LoggerFactory.getLogger(GeneralInfoPanel.class);
    private static final long serialVersionUID = 6567668717839226425L;

    private JLabel lblName = new JLabel();
    private JLabel lblIns1 = new JLabel();
    private JLabel lblIns2 = new JLabel();
    private JLabel lblUnit = new JLabel();
    private JLabel lblTarget = new JLabel();
    private JLabel lblPumpInsulin = new JLabel();

    private GGCSoftwareMode currentMode = null;
    private Map<String, JLabel> elementsMap;

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
        elementsMap = new HashMap<String, JLabel>();

        addToList("name", new JLabel(i18nControl.getMessage("YOUR_NAME") + ":"));
        addToList("name_value", lblName);
        addToList("bolus", new JLabel(i18nControl.getMessage("BOLUS_INSULIN") + ":"));
        addToList("bolus_value", lblIns1);
        addToList("basal", new JLabel(i18nControl.getMessage("BASAL_INSULIN") + ":"));
        addToList("basal_value", lblIns2);
        addToList("bg_unit", new JLabel(i18nControl.getMessage("BG_UNIT") + ":"));
        addToList("bg_unit_value", lblUnit);
        addToList("bg_target", new JLabel(i18nControl.getMessage("BG_TARGET") + ":"));
        addToList("bg_target_value", lblTarget);
        addToList("pump_insulin", new JLabel(i18nControl.getMessage("PUMP_INSULIN") + ":"));
        addToList("pump_insulin_value", this.lblPumpInsulin);

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
            this.add(this.elementsMap.get(sel_element));
        }

    }


    private void addToList(String keyword, JLabel item)
    {
        this.elementsMap.put(keyword, item);
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
     * Do Refresh - This method can do Refresh
     */
    @Override
    public void doRefresh()
    {
        if (!this.dataAccess.isDatabaseInitialized())
            return;

        lblName.setText(configurationManagerWrapper.getUserName());
        lblIns1.setText(getInsulins(DataAccess.INSULIN_DOSE_BOLUS));
        lblIns2.setText(getInsulins(DataAccess.INSULIN_DOSE_BASAL));

        GlucoseUnitType glucoseUnitType = configurationManagerWrapper.getGlucoseUnit();

        lblUnit.setText(glucoseUnitType.getTranslation());

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

        lblTarget.setText(dataAccess.getFloatAsString(min, glucoseUnitType == GlucoseUnitType.mg_dL ? 0 : 1) //
                + " - " + dataAccess.getFloatAsString(max, glucoseUnitType == GlucoseUnitType.mg_dL ? 0 : 1) //
                + " [" + dataAccess.getFloatAsString(avg, glucoseUnitType == GlucoseUnitType.mg_dL ? 0 : 1) + "]");

        this.lblPumpInsulin.setText(configurationManagerWrapper.getPumpInsulin());

        changeMode(DataAccess.getInstance().getSoftwareMode());

    }


    @Override
    public InfoPanelType getPanelType()
    {
        return InfoPanelType.General;
    }

}
