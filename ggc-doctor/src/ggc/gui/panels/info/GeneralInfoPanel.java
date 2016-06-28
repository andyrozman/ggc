package ggc.gui.panels.info;

import java.util.Hashtable;

import ggc.core.util.DataAccess;
import ggc.core.util.GGCProperties;

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
    // private JLabel lblNutri = new JLabel();
    // private JLabel lblMeter = new JLabel();
    // private JLabel lblPumps = new JLabel();
    // private JLabel lblCGMS = new JLabel();
    private int current_mode = -1;
    private Hashtable<String, JLabel> list_elems;
    private static Logger LOG = LoggerFactory.getLogger(GeneralInfoPanel.class);

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

        // add(new JLabel(m_ic.getMessage("NUTRITION_PLUGIN")+":"));
        // add(lblNutri);
        /*
         * add(new JLabel(m_ic.getMessage("METERS_PLUGIN")+":"));
         * add(lblMeter);
         * add(new JLabel(m_ic.getMessage("PUMPS_PLUGIN")+":"));
         * add(lblPumps);
         * add(new JLabel(m_ic.getMessage("CGMS_PLUGIN")+":"));
         * add(lblCGMS);
         */
        // add(new JLabel());
        // add(new JLabel());

        changeMode(DataAccess.GGC_MODE_PEN_INJECTION);

    }


    private void changeMode(int new_mode)
    {
        if (this.current_mode == new_mode)
            return;

        this.removeAll();
        this.current_mode = new_mode;

        String[] sel_elements = null;

        if (new_mode == DataAccess.GGC_MODE_PEN_INJECTION)
        {
            sel_elements = this.pen_mode;
        }
        else if (new_mode == DataAccess.GGC_MODE_PUMP)
        {
            sel_elements = this.pump_mode;
        }
        else
        {
            LOG.warn("Error setting mode in General Panel: " + new_mode);
            return;
        }

        for (int i = 0; i < sel_elements.length; i++)
        {
            this.add(this.list_elems.get(sel_elements[i]));
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

        lblName.setText(m_da.getSettings().getUserName());
        lblIns1.setText(getInsulins(DataAccess.INSULIN_DOSE_BOLUS));
        lblIns2.setText(getInsulins(DataAccess.INSULIN_DOSE_BASAL));
        // lblIns2.setText(m_da.getSettings().getIns2Name() + " (" +
        // m_da.getSettings().getIns2Abbr() + ")");
        lblUnit.setText(m_da.getSettings().getBG_unitString());

        int unit = m_da.getSettings().getBG_unit();

        float min, max;

        if (unit == 1)
        {
            min = m_da.getSettings().getBG1_TargetLow();
            max = m_da.getSettings().getBG1_TargetHigh();

        }
        else
        {
            min = m_da.getSettings().getBG2_TargetLow();
            max = m_da.getSettings().getBG2_TargetHigh();
        }

        float avg = (float) ((min + max) / 2.0);

        // String s = m_da.getConfigurationManager().getFloatValue(key)

        lblTarget.setText(DataAccess.Decimal1Format.format(min) + " - " + DataAccess.Decimal1Format.format(max) + " ["
                + DataAccess.Decimal1Format.format(avg) + "]");

        this.lbl_pump_insulin.setText(m_da.getSettings().getPumpInsulin());

        changeMode(DataAccess.getInstance().getSoftwareMode());

    }


    private String getInsulins(int type)
    {
        GGCProperties gp = DataAccess.getInstance().getSettings();
        String values = "";

        if (gp.getIns1Type() == type)
            values += gp.getIns1Name() + " [1]";

        if (gp.getIns2Type() == type)
            values = addString(values, gp.getIns2Name() + " [2]");

        if (gp.getIns3Type() == type)
            values = addString(values, gp.getIns3Name() + " [3]");

        return values;
    }


    private String addString(String full, String entry)
    {
        if (full.length() > 0)
            full += ", ";

        full += entry;

        return full;
    }


    /**
     * Get Tab Name
     * 
     * @return name as string
     */
    public String getTabName()
    {
        return "GeneralInfo";
    }


    /**
     * Do Refresh - This method can do Refresh
     */
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
