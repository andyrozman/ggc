package ggc.shared.ratio;

import ggc.core.util.DataAccess;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.atech.graphics.components.TimeComponent;
import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATDataAccessAbstract;
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


public class RatioEntryDialog extends JDialog implements HelpCapable, ChangeListener, ActionListener
{

    /**
     * 
     */
    private static final long serialVersionUID = -4693097199373180587L;




    //JDecimalTextField dtf_ch_ins, dtf_ins_bg, dtf_bg_ch;
    



    boolean in_action = false;
    
    
    
    private DataAccess m_da = DataAccess.getInstance();
    private I18nControlAbstract m_ic = m_da.getI18nControlInstance();

    private boolean m_action_done = false;

    JLabel label_title = new JLabel();

    boolean in_process;
    boolean debug = true;
    JButton help_button = null;
    //JPanel main_panel = null;
    RatioEntry ratio_entry = null;
    JSpinner procents = null;    
    RatioEntryPanel rep = null;
    float tdd = 0;
    boolean not_process = false;
    
    TimeComponent tc_from, tc_to;
    
    /**
     * Action: Add
     */
    public static final int ACTION_ADD = 1;

    /**
     * Action: Edit
     */
    public static final int ACTION_EDIT = 2;
    
    
    /**
     * Action
     */
    public int action = ACTION_ADD;
    
    
    /**
     * Constructor
     * 
     * @param dialog
     * @param tdd 
     * @param proc 
     */
    public RatioEntryDialog(JDialog dialog, float tdd, float proc) 
    {
        super(dialog, "", true);
        
        System.out.println("RatioEntryDialog");

        //m_parent = dialog;
        m_da.addComponent(this);
        this.tdd = tdd;

        initGUI();
        load();
        
        action = ACTION_ADD;
        setTitle();
        
        this.setVisible(true);
    }

    
    /**
     * Constructor
     * 
     * @param dialog
     * @param tdd 
     * @param re 
     */
    public RatioEntryDialog(JDialog dialog, float tdd, RatioEntry re) 
    {
        super(dialog, "", true);
        
        System.out.println("RatioEntryDialog");
        
        
        //m_parent = dialog;
        m_da.addComponent(this);
        this.tdd = tdd;

        this.ratio_entry = re;
        initGUI();
        load();
        
        action = ACTION_EDIT;
        setTitle();
        
        this.setVisible(true);
    }
    
    
    public RatioEntryDialog()
    {
    }
    
    
    /**
     * Constructor
     * 
     * @param dialog
     * @param re 
     */
/*    public RatioEntryDialog(JDialog dialog, RatioEntry re) 
    {
        super(dialog, "", true);
        
        //m_parent = dialog;
        m_da.addComponent(this);

        init();
        load();
        
        this.setVisible(true);

    }
  */  
    
    
    

    /**
     * Load data
     */
    private void load()
    {
        if (this.ratio_entry==null)
            return;
        
        
        // FIXME
        //this.ratio_entry.bg_insulin = this.ratio_entry.bg_insulin;
        
        this.rep.setRatioEntry(this.ratio_entry);
         
        this.tc_from.setTime(this.ratio_entry.from);
        this.tc_to.setTime(this.ratio_entry.to);
        
        not_process = true;
        this.procents.setValue(this.ratio_entry.procent);
        not_process = false;
        
    }

    
    /**
     * Save data
     */
    private boolean save()
    {
        
        if (this.tc_from.getTime() == this.tc_to.getTime())
        {
            m_da.showDialog(this, ATDataAccessAbstract.DIALOG_ERROR, "RATIO_TIMERANGE_NOT_SET_CORRECTLY");
            return false;
        }
        
        
        if (this.ratio_entry == null)
            this.ratio_entry = new RatioEntry();
        
        this.ratio_entry.from = this.tc_from.getTime();
        this.ratio_entry.to = this.tc_to.getTime();
        
        //Float f = (Float)this.procents.getValue();
        
        this.ratio_entry.procent = (Integer)this.procents.getValue();
        
        this.ratio_entry.bg_insulin = m_da.getJFormatedTextValueFloat(this.rep.dtf_ins_bg);
        this.ratio_entry.ch_insulin = m_da.getJFormatedTextValueFloat(this.rep.dtf_ch_ins);
        
        
//        this.m_da.getSettings().setRatio_CH_Insulin(m_da.getFloatValue(this.dtf_ch_ins.getCurrentValue()));
//        this.m_da.getSettings().setRatio_BG_Insulin(m_da.getFloatValue(this.dtf_ins_bg.getCurrentValue()));
        
        //this.m_da.getSettings().save();
        
        
        return true;
    }

    
    private void setTitle()
    {
        String key;
        
        if (action==ACTION_ADD)
            key = m_ic.getMessage("RATIO_ENTRY_ADD");
        else
            key = m_ic.getMessage("RATIO_ENTRY_EDIT");

        setTitle(key);
        label_title.setText(key);
        
    }
    

    private void initGUI()
    {
        
        ATSwingUtils.initLibrary();
        this.setBounds(0, 0, 380, 400);
        
        m_da.centerJDialog(this);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 380, 430);
        panel.setLayout(null);

        this.getContentPane().add(panel);

        
        label_title = ATSwingUtils.getTitleLabel("", 0, 20, 380, 35, panel, ATSwingUtils.FONT_BIG_BOLD);
        
//        setTitle(m_ic.getMessage("RATIO_ENTRY"));
//        label_title.setText(m_ic.getMessage("RATIO_ENTRY"));

        
        ATSwingUtils.getLabel(m_ic.getMessage("TIME") + ":", 40, 85, 150, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD);
        
        tc_from = new TimeComponent();
        //tc_from.setBounds(205, 75, 100, 25);
        tc_from.setBounds(100, 85, 70, 25);
        tc_from.setActionCommand("time_from");
        tc_from.addActionListener(this);
        //tc_from.
        panel.add(tc_from);

        
        //ATSwingUtils.getLabel(m_ic.getMessage("TIME_TILL"), 40, 115, 150, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD);
        ATSwingUtils.getLabel(" -> ", 210, 85, 150, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD);
        
        
        tc_to = new TimeComponent();
        tc_to.setBounds(235, 85, 100, 25);
        tc_to.setActionCommand("time_to");
        tc_to.addActionListener(this);
        panel.add(tc_to);
        
        
        ATSwingUtils.getLabel(m_ic.getMessage("PROCENT_OF_BASE") + ":", 40, 135, 150, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD);
        
        SpinnerNumberModel listDaysModel = new SpinnerNumberModel(100, 0, 1000, 1);
        procents = new JSpinner(listDaysModel);
        procents.addChangeListener(this);
        procents.setFont(ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL));
        procents.setBounds(260, 135, 70, 25); // 85
        panel.add(procents);
        
        
        rep = new RatioEntryPanel(m_da, 10, 15, 40);
        rep.setBounds(30, 100, 350, 210);  // 55
        //rep.set.space_between_lines = 10;
        rep.setEditable(false);
        panel.add(rep);

        
        ATSwingUtils.getButton("  " + m_ic.getMessage("OK"), 
                               25, 320, 105, 25, panel, ATSwingUtils.FONT_NORMAL, 
                               "ok.png", 
                               "ok", this, m_da);

        ATSwingUtils.getButton("  " + m_ic.getMessage("CANCEL"), 
                               135, 320, 105, 25, panel, ATSwingUtils.FONT_NORMAL, 
                               "cancel.png", 
                               "cancel", this, m_da);
        
        help_button = m_da.createHelpButtonByBounds(245, 320, 105, 25, this);

        panel.add(help_button);

        m_da.enableHelp(this);
        
        rep.calculateRatio(tdd, m_da.getIntValue(this.procents.getValue()) );
    }


    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
        String action = e.getActionCommand();

        if (action.equals("cancel"))
        {
            m_da.removeComponent(this);
            this.dispose();
        }
        else if (action.equals("ok"))
        {
            if (this.save())
            {
                this.m_action_done = true;
                m_da.removeComponent(this);
                this.dispose();
            }
        }
        else if (action.equals("time_from"))
        {
            if (tc_from.getTime() > tc_to.getTime())
                tc_to.setTime(tc_from.getTime());
            //System.out.println("RatioEntryDialog::unknown command: Time From"+ action);
        }
        else if (action.equals("time_to"))
        {
            if (tc_to.getTime() < tc_from.getTime() )
                tc_from.setTime(tc_to.getTime());
            //System.out.println("RatioEntryDialog::unknown command: Time To " + action);
        }
        else
            System.out.println("RatioEntryDialog::unknown command: " + action);

    }

/*
    String button_command[] = { "update_ch", m_ic.getMessage("UPDATE_FROM_FOOD"),
            "edit_food", m_ic.getMessage("EDIT_FOOD"),
            "ok", m_ic.getMessage("OK"),
            "cancel", m_ic.getMessage("CANCEL"),
//                                  "help", m_ic.getMessage("HELP")
    
  */  
    
    
    @SuppressWarnings("unused")
    private void cmdOk()
    {
        this.save();
    }

/*
    public boolean isFieldSet(String text)
    {
    	if ((text == null) || (text.trim().length()==0))
    	    return false;
    	else
    	    return true;
    } */
    
    /**
     * Action Succesful
     * 
     * @return
     */
    public boolean actionSuccesful()
    {
        return m_action_done;
    }

    
    /**
     * Get Result Object
     * 
     * @return
     */
    public RatioEntry getResultObject()
    {
        
        return this.ratio_entry;
    }
    
    
    
    // ****************************************************************
    // ******              HelpCapable Implementation             *****
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
        return "GGC_Ratio_Entry";
    }


    public void stateChanged(ChangeEvent e)
    {
        if (not_process)
            return;
        //this.procents.getValue()
        rep.calculateRatio(this.tdd, m_da.getIntValue(this.procents.getValue()) );
    }
    
    
    
    
}
