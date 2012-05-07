package ggc.gui.panels.prefs;

import ggc.core.util.DataAccess;
import ggc.gui.dialogs.PropertiesDialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;

/**
 * Application: GGC - GNU Gluco Control
 * 
 * See AUTHORS for copyright information.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Filename:     PrefMedicalDataPane 
 * Description:  Medical Options such as insulin names and BG-Values.
 * 
 *  Author: schultd
 *          andyrozman {andy@atech-software.com}  
 */

public class PrefMedicalDataPane extends AbstractPrefOptionsPanel implements HelpCapable
{
    
    private static final long serialVersionUID = 4645565336749757311L;

    // insulins
    private JTextField fieldIns1Name, fieldIns2Name, tb_ins3_name, tb_pump_insulin; //, fieldIns1Abbr, fieldIns2Abbr;

    // bg (mg/dl)
    private JTextField fieldHighBG1, fieldLowBG1, fieldTargetHighBG1, fieldTargetLowBG1;

    // bg (mmol/l)
    private JTextField fieldHighBG2, fieldLowBG2, fieldTargetHighBG2, fieldTargetLowBG2;

    
    private JComboBox cb_ins1_type, cb_ins2_type, cb_ins3_type, cb_ratio_type;
    
    I18nControlAbstract ic = DataAccess.getInstance().getI18nControlInstance();
    
    
    private String[] insulin_settings = {
                                         m_ic.getMessage("NOT_USED"),
                                         m_ic.getMessage("BASAL_INSULIN"),
                                         m_ic.getMessage("BOLUS_INSULIN")
    };
    
    JComboBox cbUnit = null;

    /**
     * Constructor
     * 
     * @param dia
     */
    public PrefMedicalDataPane(PropertiesDialog dia)
    {
        super(dia);
        init();
        // m_da.enableHelp(this);
    }

    private void init()
    {

        setLayout(new BorderLayout());

        // JPanel ful = new JPanel(new GridLayout(4,0));

        JPanel a = new JPanel(new GridLayout(0, 2));
        a.setBorder(new TitledBorder(m_ic.getMessage("INSULIN_SETTINGS")));
        a.add(new JLabel(m_ic.getMessage("INSULIN") + " 1 " + m_ic.getMessage("NAME") + ":"));
        a.add(fieldIns1Name = new JTextField(m_da.getSettings().getIns1Name(), 20));
        a.add(new JLabel(m_ic.getMessage("INSULIN") + " 1 " + m_ic.getMessage("SETTINGS") + ":"));
        //a.add(fieldIns1Abbr = new JTextField(m_da.getSettings().getIns1Abbr(), 20));
        a.add(this.cb_ins1_type = new JComboBox(this.insulin_settings));
        this.cb_ins1_type.setSelectedIndex(m_da.getSettings().getIns1Type());
        
        a.add(new JLabel(m_ic.getMessage("INSULIN") + " 2 " + m_ic.getMessage("NAME") + ":"));
        a.add(fieldIns2Name = new JTextField(m_da.getSettings().getIns2Name(), 20));
        a.add(new JLabel(m_ic.getMessage("INSULIN") + " 2 " + m_ic.getMessage("SETTINGS") + ":"));
        //a.add(fieldIns2Abbr = new JTextField(m_da.getSettings().getIns2Abbr(), 20));
        a.add(this.cb_ins2_type = new JComboBox(this.insulin_settings));
        this.cb_ins2_type.setSelectedIndex(m_da.getSettings().getIns2Type());
        //this.cb_ins1_type
        
        a.add(new JLabel(m_ic.getMessage("INSULIN") + " 3 " + m_ic.getMessage("NAME") + ":"));
        a.add(this.tb_ins3_name = new JTextField(m_da.getSettings().getIns3Name(), 20));
        a.add(new JLabel(m_ic.getMessage("INSULIN") + " 3 " + m_ic.getMessage("SETTINGS") + ":"));
        a.add(this.cb_ins3_type = new JComboBox(this.insulin_settings));
        this.cb_ins3_type.setSelectedIndex(m_da.getSettings().getIns3Type());

        //a.add(this.tb_ins3_abbr = new JTextField(m_da.getSettings().getIns3Abbr(), 20));
        a.add(new JLabel(m_ic.getMessage("PUMP_INSULIN") + ":"));
        a.add(this.tb_pump_insulin = new JTextField(m_da.getSettings().getPumpInsulin(), 20));
        
        fieldIns1Name.getDocument().addDocumentListener(this);
        fieldIns2Name.getDocument().addDocumentListener(this);
        //fieldIns1Abbr.getDocument().addDocumentListener(this);
        //fieldIns2Abbr.getDocument().addDocumentListener(this);
        tb_pump_insulin.getDocument().addDocumentListener(this);
        //tb_ins3_abbr.getDocument().addDocumentListener(this);
        tb_ins3_name.getDocument().addDocumentListener(this);
        

        JLabel l;
        
        // bg 1
        JPanel c = new JPanel(new GridLayout(0, 3));
        c.setBorder(new TitledBorder(m_ic.getMessage("BLOOD_GLUCOSE_SETTINGS_1")));
        c.add(new JLabel());
        c.add(l = new JLabel("mg/dL"));
        l.setHorizontalAlignment(JLabel.CENTER);
        c.add(l = new JLabel("mmol/L"));
        l.setHorizontalAlignment(JLabel.CENTER);
        
        c.add(new JLabel(m_ic.getMessage("HIGH_BG") + ":"));
        c.add(fieldHighBG1 = new JTextField("" + m_da.getSettings().getBG1_High(), 10));
        c.add(fieldHighBG2 = new JTextField("" + m_da.getSettings().getBG2_High(), 10));
        
        c.add(new JLabel(m_ic.getMessage("LOW_BG") + ":"));
        c.add(fieldLowBG1 = new JTextField("" + m_da.getSettings().getBG1_Low(), 10));
        c.add(fieldLowBG2 = new JTextField("" + m_da.getSettings().getBG2_Low(), 10));
        
        c.add(new JLabel(m_ic.getMessage("TARGET_HIGH_BG") + ":"));
        c.add(fieldTargetHighBG1 = new JTextField("" + m_da.getSettings().getBG1_TargetHigh(), 10));
        c.add(fieldTargetHighBG2 = new JTextField("" + m_da.getSettings().getBG2_TargetHigh(), 10));
        
        c.add(new JLabel(m_ic.getMessage("TARGET_LOW_BG") + ":"));
        c.add(fieldTargetLowBG1 = new JTextField("" + m_da.getSettings().getBG1_TargetLow(), 10));
        c.add(fieldTargetLowBG2 = new JTextField("" + m_da.getSettings().getBG2_TargetLow(), 10));

        fieldHighBG1.getDocument().addDocumentListener(this);
        fieldLowBG1.getDocument().addDocumentListener(this);
        fieldTargetHighBG1.getDocument().addDocumentListener(this);
        fieldTargetLowBG1.getDocument().addDocumentListener(this);
        fieldHighBG2.getDocument().addDocumentListener(this);
        fieldLowBG2.getDocument().addDocumentListener(this);
        fieldTargetHighBG2.getDocument().addDocumentListener(this);
        fieldTargetLowBG2.getDocument().addDocumentListener(this);

        
        
/*        
        // bg 1
        JPanel c = new JPanel(new GridLayout(0, 2));
        c.setBorder(new TitledBorder(m_ic.getMessage("BLOOD_GLUCOSE_SETTINGS_1")));
        c.add(new JLabel(m_ic.getMessage("HIGH_BG") + ":"));
        c.add(fieldHighBG1 = new JTextField("" + m_da.getSettings().getBG1_High(), 10));
        c.add(new JLabel(m_ic.getMessage("LOW_BG") + ":"));
        c.add(fieldLowBG1 = new JTextField("" + m_da.getSettings().getBG1_Low(), 10));
        c.add(new JLabel(m_ic.getMessage("TARGET_HIGH_BG") + ":"));
        c.add(fieldTargetHighBG1 = new JTextField("" + m_da.getSettings().getBG1_TargetHigh(), 10));
        c.add(new JLabel(m_ic.getMessage("TARGET_LOW_BG") + ":"));
        c.add(fieldTargetLowBG1 = new JTextField("" + m_da.getSettings().getBG1_TargetLow(), 10));

        fieldHighBG1.getDocument().addDocumentListener(this);
        fieldLowBG1.getDocument().addDocumentListener(this);
        fieldTargetHighBG1.getDocument().addDocumentListener(this);
        fieldTargetLowBG1.getDocument().addDocumentListener(this);

        // bg 2
        JPanel b = new JPanel(new GridLayout(0, 2));
        b.setBorder(new TitledBorder(m_ic.getMessage("BLOOD_GLUCOSE_SETTINGS_2")));
        b.add(new JLabel(m_ic.getMessage("HIGH_BG") + ":"));
        b.add(fieldHighBG2 = new JTextField("" + m_da.getSettings().getBG2_High(), 10));
        b.add(new JLabel(m_ic.getMessage("LOW_BG") + ":"));
        b.add(fieldLowBG2 = new JTextField("" + m_da.getSettings().getBG2_Low(), 10));
        b.add(new JLabel(m_ic.getMessage("TARGET_HIGH_BG") + ":"));
        b.add(fieldTargetHighBG2 = new JTextField("" + m_da.getSettings().getBG2_TargetHigh(), 10));
        b.add(new JLabel(m_ic.getMessage("TARGET_LOW_BG") + ":"));
        b.add(fieldTargetLowBG2 = new JTextField("" + m_da.getSettings().getBG2_TargetLow(), 10));

        fieldHighBG2.getDocument().addDocumentListener(this);
        fieldLowBG2.getDocument().addDocumentListener(this);
        fieldTargetHighBG2.getDocument().addDocumentListener(this);
        fieldTargetLowBG2.getDocument().addDocumentListener(this);
*/
        // bg unit
        JPanel d = new JPanel(new GridLayout(0, 2));
        d.setBorder(new TitledBorder(m_ic.getMessage("BLOOD_GLUCOSE_UNIT_SETTING")));
        d.add(new JLabel(m_ic.getMessage("BG_UNIT") + ":"));
        d.add(cbUnit = new JComboBox(m_da.bg_units_config));
        
        if (settings.getBG_unit() < 1)
        {
            // XXX: a -1 is returned in case of error; that should be handled properly
            cbUnit.setSelectedIndex(0);
        }
        else if (settings.getBG_unit() <= 2)
            cbUnit.setSelectedIndex(settings.getBG_unit()-1);
        else
            cbUnit.setSelectedIndex(0);

        
        
        
        // ratio type
        
        String[] rat_type = { ic.getMessage("RATIO_MODE_BASE"), ic.getMessage("RATIO_MODE_EXTENDED") };
        
        JPanel e = new JPanel(new GridLayout(0, 2));
        e.setBorder(new TitledBorder(m_ic.getMessage("RATIO_TYPE_SETTING")));
        e.add(new JLabel(m_ic.getMessage("RATIO_TYPE") + ":"));
        e.add(cb_ratio_type = new JComboBox(rat_type));
        
        if ((settings.getRatioMode().equals("")) || (settings.getRatioMode().equals("Base")))
            cb_ratio_type.setSelectedIndex(0);
        else 
            cb_ratio_type.setSelectedIndex(1);
        
        
        //d.add(cbUnit = new JComboBox(m_da.bg_units));
        //cbUnit.setSelectedIndex(settings.getBG_unit() - 1);

        Box i = Box.createVerticalBox();
        i.add(a);
        i.add(c);
        i.add(d);
        i.add(e);

        add(i, BorderLayout.NORTH);

    }

    /**
     * Save Properties
     * 
     * @see ggc.gui.panels.prefs.AbstractPrefOptionsPanel#saveProps()
     */
    @Override
    public void saveProps()
    {
        // insulins
        settings.setIns1Name(fieldIns1Name.getText());
        settings.setIns1Type(this.cb_ins1_type.getSelectedIndex());
        
        settings.setIns2Name(fieldIns2Name.getText());
        settings.setIns2Type(this.cb_ins2_type.getSelectedIndex());
        //settings.setIns1Abbr(fieldIns1Abbr.getText());
        //settings.setIns2Abbr(fieldIns2Abbr.getText());

        settings.setIns3Name(this.tb_ins3_name.getText());
        settings.setIns3Type(this.cb_ins3_type.getSelectedIndex());

        //settings.setIns3Abbr(this.tb_ins3_abbr.getText());
        settings.setPumpInsulin(this.tb_pump_insulin.getText());
        
        // bg 1
        settings.setBG1_High(m_da.getFloatValue(fieldHighBG1.getText()));
        settings.setBG1_Low(m_da.getFloatValue(fieldLowBG1.getText()));
        settings.setBG1_TargetHigh(m_da.getFloatValue(fieldTargetHighBG1.getText()));
        settings.setBG1_TargetLow(m_da.getFloatValue(fieldTargetLowBG1.getText()));

        // bg 2
        settings.setBG2_High(m_da.getFloatValue(fieldHighBG2.getText()));
        settings.setBG2_Low(m_da.getFloatValue(fieldLowBG2.getText()));
        settings.setBG2_TargetHigh(m_da.getFloatValue(fieldTargetHighBG2.getText()));
        settings.setBG2_TargetLow(m_da.getFloatValue(fieldTargetLowBG2.getText()));

        // unit
        settings.setBG_unit(cbUnit.getSelectedIndex()+ 1);
        
        // ratio mode
        if (this.cb_ratio_type.getSelectedIndex()==0)
            settings.setRatioMode("Base");
        else
            settings.setRatioMode("Extended");
        

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
        return "GGC_Prefs_MedicalData";
    }

}
