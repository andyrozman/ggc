package ggc.shared.bolushelper;

import ggc.core.util.DataAccess;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;
import com.atech.utils.ATechDate;

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
 *  Filename:     BolusHelper  
 *  Description:  Bolus Helper for Daily Values
 * 
 *  Author:  andyrozman {andy@atech-software.com}  
 */


public class BolusHelper extends JDialog implements ActionListener, HelpCapable
{

    private static final long serialVersionUID = 5048286134436536838L;
    
    private float curr_bg;
    private float curr_ch;
    private long time;
    //private double calc_insulin;
    private double calc_insulin_rnd;
    
    JLabel lbl_bg_oh, lbl_correction, lbl_carb_dose, lbl_together, lbl_together_rnd, lbl_time;

    String bg_unit;
    
    
    
   

    boolean in_action = false;

    private DataAccess m_da = DataAccess.getInstance();
    private I18nControlAbstract m_ic = m_da.getI18nControlInstance();
    private boolean m_actionDone = false;

    JFormattedTextField ftf_ch_ins, ftf_bg_ins;
    JLabel label_title = new JLabel();
    JLabel label_food;
    JCheckBox cb_food_set;

    //JButton AddButton;

    //String sDate = null;

    //DailyValues dV = null;
    //DailyValuesRow m_dailyValuesRow = null;

    NumberFormat bg_displayFormat, bg_editFormat;

//    JComponent components[] = new JComponent[9];

//    Font f_normal = m_da.getFont(DataAccess.FONT_NORMAL);
//    Font f_bold = m_da.getFont(DataAccess.FONT_NORMAL_BOLD);
    boolean in_process;
    boolean debug = true;
    JButton help_button = null;
    JPanel main_panel = null;
    int insulin_type;

    //private Container m_parent = null;

    
    /**
     * Constructor
     * 
     * @param dialog
     * @param bg
     * @param ch
     * @param time
     * @param time_format 1 = min, 2 = s
     * @param insulin_type 
     */
    public BolusHelper(JDialog dialog, float bg, float ch, long time, int time_format, int insulin_type)
    {
        super(dialog, "", true);
        //m_parent = dialog;
        
        this.curr_bg = bg;
        this.curr_ch = ch;
        this.time = time;
        this.insulin_type = insulin_type;
        
        if (time_format==2)
        {
            this.time /= 100;
        }
        
        this.init();
        this.readRatios();
        this.calculateInsulin();
        this.setVisible(true);
    }

    
    /**
     * Constructor
     * 
     * @param frame
     * @param insulin_type 
     */
    public BolusHelper(JFrame frame, int insulin_type)
    {
        super(frame, "", true);
        //m_parent = dialog;
        this.insulin_type = insulin_type;

        init();
        this.readRatios();
        this.setVisible(true);
    }
    
    

    private void init()
    {
        int width = 400;
        int height = 455;
        
        m_da.addComponent(this);
        
        ATSwingUtils.initLibrary();
        
        this.setResizable(false);
        this.setBounds(0, 0, width, height);
        m_da.centerJDialog(this);
   

        
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, width, height);
        panel.setLayout(null);

        main_panel = panel;

        this.getContentPane().add(panel);

        /*
        label_title.setFont(m_da.getFont(DataAccess.FONT_BIG_BOLD));
        label_title.setHorizontalAlignment(JLabel.CENTER);
        label_title.setBounds(0, 15, 400, 35);
        panel.add(label_title);*/

        label_title = ATSwingUtils.getTitleLabel("", 0, 15, 400, 35, 
            panel, ATSwingUtils.FONT_BIG_BOLD);

        setTitle(m_ic.getMessage("BOLUS_HELPER"));
        label_title.setText(m_ic.getMessage("BOLUS_HELPER"));
        
        if (m_da.getBGMeasurmentType()==DataAccess.BG_MMOL)
            this.bg_unit = "mmol/L";
        else
            this.bg_unit = "mg/dL";
        
        
        ATSwingUtils.getLabel(m_ic.getMessage("TIME") + ":", 30, 78, 100,23, panel, ATSwingUtils.FONT_NORMAL_BOLD);
        ATSwingUtils.getLabel(m_ic.getMessage("CH_INSULIN_RATIO") + ":", 30, 138, 200, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD);
        ATSwingUtils.getLabel(m_ic.getMessage("BG_INSULIN_RATIO") + ":", 30, 168, 200, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD);
        ATSwingUtils.getLabel(m_ic.getMessage("BG_OH_RATIO") + ":", 30, 198, 200, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD);
        ATSwingUtils.getLabel(m_ic.getMessage("CORRECTION_DOSE") + ":", 30, 243, 200, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD);
        ATSwingUtils.getLabel(m_ic.getMessage("CARB_DOSE") + ":", 30, 268, 200, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD);
        ATSwingUtils.getLabel(m_ic.getMessage("TOGETHER") + ":", 30, 298, 200, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD);
        ATSwingUtils.getLabel(m_ic.getMessage("TOGETHER_ROUNDED") + ":", 30, 328, 200, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD);
        
        this.lbl_bg_oh = ATSwingUtils.getLabel(m_ic.getMessage("BG_OH_RATIO") + ":", 180, 198, 200, 25, panel, ATSwingUtils.FONT_NORMAL);

        lbl_correction = ATSwingUtils.getLabel(m_ic.getMessage("NO_BG_MEASURE") , 200, 243, 200, 25, panel, ATSwingUtils.FONT_NORMAL);
        lbl_carb_dose = ATSwingUtils.getLabel(m_ic.getMessage("NO_CARBS_DEFINED"), 200, 268, 200, 25, panel, ATSwingUtils.FONT_NORMAL);
        lbl_together = ATSwingUtils.getLabel("0 E", 200, 298, 200, 25, panel, ATSwingUtils.FONT_NORMAL);
        ATSwingUtils.getLabel(ATechDate.getTimeString(ATechDate.FORMAT_DATE_AND_TIME_MIN, this.time), 140, 78, 100, 25, panel, ATSwingUtils.FONT_NORMAL);
        lbl_together_rnd = ATSwingUtils.getLabel("0 E", 200, 328, 200, 25, panel, ATSwingUtils.FONT_NORMAL);

        this.ftf_ch_ins = ATSwingUtils.getNumericTextField(2, 2, new Float(0.0f), 
                180, 138, 45, 25, panel); 
            
        this.ftf_bg_ins = ATSwingUtils.getNumericTextField(2, 2, new Float(0.0f), 
                180, 168, 45, 25, panel);

        ATSwingUtils.getLabel(" g " + m_ic.getMessage("CH") + "  =  1 " + m_ic.getMessage("UNIT_SHORT") + " " + m_ic.getMessage("INSULIN"), 230, 140, 200, 25, panel, ATSwingUtils.FONT_NORMAL);
        ATSwingUtils.getLabel(" " + this.bg_unit + "  =  1 " + m_ic.getMessage("UNIT_SHORT") + " " + m_ic.getMessage("INSULIN"), 230, 170, 200, 25, panel, ATSwingUtils.FONT_NORMAL);
        
        ATSwingUtils.getButton(m_ic.getMessage("READ_RATIOS"), 210, 110, 150, 25, 
                               panel, ATSwingUtils.FONT_NORMAL, null, "read_ratios", this, m_da);
        
        ATSwingUtils.getButton(m_ic.getMessage("OK"), 30, 375, 110, 25, 
            panel, ATSwingUtils.FONT_NORMAL, "ok.png", "ok", this, m_da);
        
        ATSwingUtils.getButton(m_ic.getMessage("CANCEL"), 145, 375, 110, 25, 
            panel, ATSwingUtils.FONT_NORMAL, "cancel.png", "cancel", this, m_da);

        help_button = m_da.createHelpButtonByBounds(260, 375, 110, 25, this);
        panel.add(help_button);
        m_da.enableHelp(this);
        
        
        /*
        
        String button_command[] = { "read_ratios", m_ic.getMessage("READ_RATIOS"), 
                                    "ok", m_ic.getMessage("OK"), 
                                    "cancel", m_ic.getMessage("CANCEL")
        };

        String button_icon[] = { null, "ok.png", "cancel.png" };

        int button_coord[] = { 210, 112, 150, 1, 
                               30, 350, 110, 1, 
                               145, 350, 110, 1,
        // 250, 390, 80, 0
        };

        JButton button;
        // int j=0;
        for (int i = 0, j = 0, k = 0; i < button_coord.length; i += 4, j += 2, k++)
        {
            button = new JButton("   " + button_command[j + 1]);
            button.setActionCommand(button_command[j]);
            // button.setFont(m_da.getFont(DataAccess.FONT_NORMAL));
            button.addActionListener(this);

            if (button_icon[k] != null)
            {
                button.setIcon(m_da.getImageIcon_22x22(button_icon[k], this));
            }

            if (button_coord[i + 3] == 0)
            {
                button.setEnabled(false);
            }

            if (k <= 1)
                addComponent(button, button_coord[i], button_coord[i + 1], button_coord[i + 2], panel);
            else
                addComponent(button, button_coord[i], button_coord[i + 1], button_coord[i + 2], 25, false, panel);

        }
*/

    }


    
    private void readRatios()
    {
        this.ftf_bg_ins.setValue(m_da.getSettings().getRatio_BG_Insulin());
        this.ftf_ch_ins.setValue(m_da.getSettings().getRatio_CH_Insulin());
        
        float cal_r = m_da.getSettings().getRatio_CH_Insulin() / m_da.getSettings().getRatio_BG_Insulin();
        
        this.lbl_bg_oh.setText("1 " + this.bg_unit + "  =  " + DataAccess.Decimal1Format.format(cal_r) + " g " + m_ic.getMessage("CH"));
    }
    
    private void calculateInsulin()
    {
        float sum = 0.0f;
        
        // calculate correction dose
        if (this.curr_bg>0)
        {
            float tg_bg = (this.m_da.getSettings().getBG_TargetHigh() + this.m_da.getSettings().getBG_TargetLow())/2.0f;
            
            //System.out.println("target: " + tg_bg);
            
            float cu = this.curr_bg - tg_bg;
            //System.out.println("difference: " + cu);
            
            float cu_fix = cu / m_da.getJFormatedTextValueFloat(this.ftf_bg_ins);
            this.lbl_correction.setText(DataAccess.Decimal2Format.format(cu_fix) + "  " + m_ic.getMessage("UNIT_SHORT"));
            
            sum = cu_fix;
        }
        else
        {
            lbl_correction.setText(m_ic.getMessage("NO_BG_MEASURE"));
        }
        
        // ch dose
        if (this.curr_ch>0)
        {
            float ch_fix = this.curr_ch / m_da.getJFormatedTextValueFloat(this.ftf_ch_ins);
            this.lbl_carb_dose.setText(DataAccess.Decimal2Format.format(ch_fix) + "  " + m_ic.getMessage("UNIT_SHORT"));
            
            sum += ch_fix;
        }
        else
        {
            this.lbl_carb_dose.setText(m_ic.getMessage("NO_CARBS_DEFINED"));
        }
        
        this.lbl_together.setText(DataAccess.Decimal2Format.format(sum) + "  " + m_ic.getMessage("UNIT_SHORT"));
        
        //this.calc_insulin = sum; //Math.round(sum);
        
        
        this.calc_insulin_rnd = m_da.reformatInsulinAmountToCorrectValue(this.insulin_type, DataAccess.INSULIN_DOSE_BOLUS, sum);

        //System.out.println("Calc Insulin Rnd: " + this.calc_insulin_rnd);
        
        this.lbl_together_rnd.setText(m_da.reformatInsulinAmountToCorrectValueString(this.insulin_type, DataAccess.INSULIN_DOSE_BOLUS, sum) 
                                  + "  " + m_ic.getMessage("UNIT_SHORT"));
    }
    
    
    boolean res = false;
    
    /**
     * Has Result
     * 
     * @return true if result present
     */
    public boolean hasResult()
    {
        return res;
    }
    
    
    /**
     * Get Result
     * 
     * @return calculated insulin
     */
    public double getResult()
    {
        return this.calc_insulin_rnd;
    }
    

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
        String action = e.getActionCommand();

        
        if (action.equals("cancel"))
        {
            this.dispose();
        }
        else if (action.equals("read_ratios"))
        {
            readRatios();
        }
        else if (action.equals("ok"))
        {
            this.res = true;
            this.dispose();
        }
        else
            System.out.println("BolusHelper::unknown command: " + action);

    }


    /*
    public boolean isFieldSet(String text)
    {
        if ((text == null) || (text.trim().length() == 0))
            return false;
        else
            return true;
    }*/

    /**
     * Action Sucessful
     * 
     * @return
     */
    public boolean actionSuccesful()
    {
        return m_actionDone;
    }


/*
    public String checkDecimalFields(String field)
    {
        field = field.replace(',', '.');
        return field;
    }
*/
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
        return this.help_button;
    }

    /**
     * getHelpId - get id for Help
     */
    public String getHelpId()
    {
        return "pages.GGC_BG_Bolus_Helper";
    }

}
