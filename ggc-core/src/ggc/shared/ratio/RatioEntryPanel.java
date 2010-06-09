package ggc.shared.ratio;

import ggc.core.util.DataAccess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import com.atech.graphics.components.JDecimalTextField;
import com.atech.i18n.I18nControlAbstract;
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
 *  Filename:     RatioBaseDialog  
 *  Description:  This is dialog for setting Base ratio (one for whole day). This is usable
 *                for pen/injection therapy, for pump therapy you will need to use extended 
 *                dialog.
 * 
 *  Author: Andy {andy@atech-software.com}  
 */


public class RatioEntryPanel extends JPanel implements ActionListener, KeyListener, FocusListener
{

    private static final long serialVersionUID = 4974215928928541220L;

    JDecimalTextField dtf_ch_ins, dtf_ins_bg, dtf_bg_ch;
    

    boolean in_action = false;
    
    private DataAccess m_da = null;
    private I18nControlAbstract m_ic = null;

    RatioEntry ratio_entry = null;
    int left_position = 30;
    int space_between_lines = 40;
    

    /**
     * Constructor
     * 
     * @param da 
     * @param left_pos 
     * @param space_elements 
     */
    public RatioEntryPanel(DataAccess da, int left_pos, int space_elements) 
    {
        super();
        m_da = da;
        m_ic = da.getI18nControlInstance();
        this.left_position = left_pos;
        this.space_between_lines = space_elements;
        
        init();
    }

    
    /**
     * Constructor
     * 
     * @param da 
     * @param re 
     */
    public RatioEntryPanel(DataAccess da, RatioEntry re) 
    {
        this(da, 30, 40);
        ratio_entry = re;
        setRatios(re.ch_insulin, re.bg_insulin, 0.0f);
    }
    

    /**
     * Constructor
     * 
     * @param da 
     * @param re 
     * @param left_pos 
     * @param space_elements 
     */
    public RatioEntryPanel(DataAccess da, RatioEntry re, int left_pos, int space_elements) 
    {
        this(da, left_pos, space_elements);
        ratio_entry = re;
        setRatios(re.ch_insulin, re.bg_insulin, 0.0f);
    }
    
    
    
    /**
     * Set Editable
     * 
     * @param can_edit
     */
    public void setEditable(boolean can_edit)
    {
        this.dtf_bg_ch.setEditable(can_edit);
        this.dtf_ch_ins.setEditable(can_edit);
        this.dtf_ins_bg.setEditable(can_edit);
    }
    
    
    
    /**
     * Set Ratio's
     * 
     * @param ch_ins
     * @param bg_ins
     * @param bg_ch
     */
    public void setRatios(float ch_ins, float bg_ins, float bg_ch)
    {
        this.dtf_ch_ins.setValue(ch_ins);
        this.dtf_ins_bg.setValue(bg_ins);
        calculateRatio(RATIO_BG_CH);
    }
    
    
    /**
     * Get Ratio's
     * 
     * @return
     */
    public float[] getRatios()
    {
        float[] f = new float[2];
        
        f[0] = m_da.getFloatValue(this.dtf_ch_ins.getCurrentValue());
        f[1] = m_da.getFloatValue(this.dtf_ins_bg.getCurrentValue());
        
        return f;
    }
    
    /**
     * Calculate Ratio
     * 
     * @param daily
     * @param procent
     */
    public void calculateRatio(float daily, float procent)
    {
        RatioCalculatorDialog rcd = new RatioCalculatorDialog();
        //float[] res = rcd.getRatios(daily, procent);
        float[] res = rcd.getRatios(daily, procent);
        setRatios(res[0], res[1], 0.0f);
        //setRatios((float)(res[0]*(procent/100.0)), (float)(res[1]*(procent/100.0)), 0.0f);
    }
    
    
    /**
     * Set Position
     * 
     * @param x
     * @param y
     */
    public void setPosition(int x, int y)
    {
        this.setBounds(x, y, 300, 400);
    }
    
    
    /**
     * Load data
     */
    /*private void load()
    {
        this.dtf_ch_ins.setValue(this.m_da.getSettings().getRatio_CH_Insulin());
        this.dtf_ins_bg.setValue(this.m_da.getSettings().getRatio_BG_Insulin());
        calculateRatio(RATIO_BG_CH);
    }*/

    
    /**
     * Save data
     */
    @SuppressWarnings("unused")
    private void save()
    {
        this.m_da.getSettings().setRatio_CH_Insulin(m_da.getFloatValue(this.dtf_ch_ins.getCurrentValue()));
        this.m_da.getSettings().setRatio_BG_Insulin(m_da.getFloatValue(this.dtf_ins_bg.getCurrentValue()));
        
        this.m_da.getSettings().save();
    }


    private void init()
    {
        
        ATSwingUtils.initLibrary();
        
        setBounds(0, 0, 300, 400);
        setLayout(null);

        int y_pos = 80;
        
        
        ATSwingUtils.getLabel(m_ic.getMessage("INSULIN_CARB_RATIO"), left_position, y_pos, 150, 25, this, ATSwingUtils.FONT_NORMAL_BOLD);
        dtf_ch_ins = ATSwingUtils.getNumericTextField(3, 2, new Float(0.0f), 180, y_pos, 80, 25, this);
        dtf_ch_ins.addFocusListener(this);
        dtf_ch_ins.addKeyListener(this);
        
        y_pos += this.space_between_lines;
        
        ATSwingUtils.getLabel(m_ic.getMessage("SENSITIVITY_FACTOR_LONG"), left_position, y_pos, 150, 45, this, ATSwingUtils.FONT_NORMAL_BOLD);
        dtf_ins_bg = ATSwingUtils.getNumericTextField(3, 2, new Float(0.0f), 180, 130, 80, 25, this);
        dtf_ins_bg.addFocusListener(this);
        dtf_ins_bg.addKeyListener(this);

        ATSwingUtils.getLabel(m_ic.getMessage("BG_OH_RATIO"), left_position, 180, 150, 25, this, ATSwingUtils.FONT_NORMAL_BOLD);
        dtf_bg_ch = ATSwingUtils.getNumericTextField(3, 2, new Float(0.0f), 180, 180, 80, 25, this);
        dtf_bg_ch.addFocusListener(this);
        dtf_bg_ch.addKeyListener(this);
        
    }

    private static final int RATIO_CH_INSULIN = 1;
    private static final int RATIO_BG_INSULIN = 2;
    private static final int RATIO_BG_CH = 3;

    
    private void calculateRatio(Object obj)
    {
        if (obj.equals(this.dtf_ch_ins))
        {
            calculateRatio(RATIO_CH_INSULIN);
        }
        else if (obj.equals(this.dtf_ins_bg))
        {
            calculateRatio(RATIO_BG_INSULIN);
        }
        else if (obj.equals(this.dtf_bg_ch))
        {
            calculateRatio(RATIO_BG_CH);
        }
    }
    
    
    private void calculateRatio(int type)
    {
        //System.out.println("calculate Ratio: " + type);
        
        float v1 = this.m_da.getFloatValue(this.dtf_ch_ins.getCurrentValue());
        float v2 = this.m_da.getFloatValue(this.dtf_ins_bg.getCurrentValue());
        float v3 = this.m_da.getFloatValue(this.dtf_bg_ch.getCurrentValue());

        
        if (type==RATIO_CH_INSULIN)
        {
            if (checkSet(v1,v2))
            {
                float v4 = v1/v2;
//                System.out.println("calculate Ratio [type=" + type + ",check=1,2;value=" + v4);
                this.dtf_bg_ch.setValue(new Float(v4));
            }
            else if (checkSet(v1,v3))
            {
                float v4 = v1/v3;
//                System.out.println("calculate Ratio [type=" + type + ",check=1,3;value=" + v4);
                this.dtf_ins_bg.setValue(new Float(v4));
            }
//            else
//                System.out.println("calculate Ratio [type=" + type + ",check NO");

        }
        else if (type==RATIO_BG_INSULIN)
        {
            if (checkSet(v2,v1))
            {
                float v4 = v1/v2;
                //System.out.println("calculate Ratio [type=" + type + ",check=2,1;value=" + v4);
                this.dtf_bg_ch.setValue(new Float(v4));
            }
            else if (checkSet(v2,v3))
            {
                float v4 = v2*v3;
                //System.out.println("calculate Ratio [type=" + type + ",check=2,3;value=" + v4);
                this.dtf_ch_ins.setValue(new Float(v4));
            }
//            else
//                System.out.println("calculate Ratio [type=" + type + ",check NO");
        }
        else
        {
            if (checkSet(v1,v2))
            {
                float v4 = v1/v2;
                //System.out.println("calculate Ratio [type=" + type + ",check=3,1;value=" + v4);
                this.dtf_bg_ch.setValue(v4);
            }
            else if (checkSet(v3,v2))
            {
                float v4 = v2*v3;
                //System.out.println("calculate Ratio [type=" + type + ",check=3,2;value=" + v4);
                this.dtf_ch_ins.setValue(new Float(v4));
            }
//            else
//                System.out.println("calculate Ratio [type=" + type + ",check NO");
        }
        
        
//        JDecimalTextField dtf_ch_ins, dtf_ins_bg, dtf_bg_ch;

    }
    
    private boolean checkSet(float v1, float v2)
    {
//        System.out.println("checkSet [v1=" + v1 + ",v2=" + v2 + "]");
        if ((v1!=0.0f) && (v2!=0.0f))
            return true;
        else
            return false;
    }
    

    


    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
        @SuppressWarnings("unused")
        String action = e.getActionCommand();

        /*
        else if (action.equals("ok"))
        {
            float v1 = this.m_da.getFloatValue(this.dtf_ch_ins.getCurrentValue());
            float v2 = this.m_da.getFloatValue(this.dtf_ins_bg.getCurrentValue());

            if (this.checkSet(v1, v2))
            {
                m_da.removeComponent(this);
                cmdOk();
                this.dispose();
            }
            else
            {
                JOptionPane.showMessageDialog(this, m_ic.getMessage("FILL_RATIO_ENTRIES"), 
                    m_ic.getMessage("ERROR"), JOptionPane.ERROR_MESSAGE);
            }
            
        }
        else*/ 
/*        if (action.equals("calculator"))
        {
            RatioCalculatorDialog rcd = new RatioCalculatorDialog(this);

            if (rcd.actionSuccesful())
            {
                float[] res = rcd.getResults();
                
                this.dtf_ch_ins.setValue(res[0]);
                this.dtf_ins_bg.setValue(res[1]);
                this.dtf_bg_ch.setValue(res[2]);
            }
        }
        else
            System.out.println("RatioBaseDialog::unknown command: " + action);
*/
    }

/*
    String button_command[] = { "update_ch", m_ic.getMessage("UPDATE_FROM_FOOD"),
            "edit_food", m_ic.getMessage("EDIT_FOOD"),
            "ok", m_ic.getMessage("OK"),
            "cancel", m_ic.getMessage("CANCEL"),
//                                  "help", m_ic.getMessage("HELP")
    
  */  
    
    
    
    /**
     * Set Ratio CH/Ins
     * 
     * @param val
     */
    public void setRatioChIns(float val)
    {
        this.dtf_ch_ins.setValue(val);
    }
    
    /**
     * Set Ratio BG/Ins
     * 
     * @param val
     */
    public void setRatioInsBg(float val)
    {
        this.dtf_ins_bg.setValue(val);
    }
    
    
    /**
     * Set Ratio CH/BG
     * 
     * @param val
     */
    public void setRatioBgCh(float val)
    {
        this.dtf_bg_ch.setValue(val);
    }
    
    
    
    /*
    private void cmdOk()
    {
        this.save();
    }*/

/*
    public boolean isFieldSet(String text)
    {
    	if ((text == null) || (text.trim().length()==0))
    	    return false;
    	else
    	    return true;
    } */
    
   


    /**
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
    public void keyTyped(KeyEvent e) {}
    
    /**
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    public void keyPressed(KeyEvent e) {}

    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     */
    public void keyReleased(KeyEvent e)
    {

        //System.out.println("key released [" + in_action + "]");
        
        if (in_action)
            return;
    
        in_action = true;
        
        //System.out.println("key released [" + in_action + "]");
        calculateRatio(e.getSource());
        
        in_action = false;

        //System.out.println("key released [" + in_action + "]");
        
        /*
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            cmdOk();
        }*/

    }


    /** 
     * Focus Lost
     */
    public void focusLost(FocusEvent fe)
    {
        if (in_action)
            return;

        in_action = true;
        calculateRatio(fe.getSource());
        in_action = false;
    }

    
    /** 
     * Focus Gained
     */
    public void focusGained(FocusEvent fe)
    {
    }
    
    
    
}
