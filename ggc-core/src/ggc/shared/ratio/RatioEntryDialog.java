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

import com.atech.graphics.components.JDecimalTextField;
import com.atech.graphics.components.TimeComponent;
import com.atech.help.HelpCapable;
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

    private boolean m_actionDone = false;

    JLabel label_title = new JLabel();

    boolean in_process;
    boolean debug = true;
    JButton help_button = null;
    //JPanel main_panel = null;
    RatioEntry ratio_entry = null;
    JSpinner procents = null;    
    RatioEntryPanel rep = null;
    
    TimeComponent tc_from, tc_to;
    
    
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
        
        //m_parent = dialog;
        m_da.addComponent(this);

        init();
        load();
        
        this.setVisible(true);

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
        // FIXME
        /*
        this.dtf_ch_ins.setValue(this.m_da.getSettings().getRatio_CH_Insulin());
        this.dtf_ins_bg.setValue(this.m_da.getSettings().getRatio_BG_Insulin());
        calculateRatio(RATIO_BG_CH);
        */
    }

    
    /**
     * Save data
     */
    private void save()
    {
//        this.m_da.getSettings().setRatio_CH_Insulin(m_da.getFloatValue(this.dtf_ch_ins.getCurrentValue()));
//        this.m_da.getSettings().setRatio_BG_Insulin(m_da.getFloatValue(this.dtf_ins_bg.getCurrentValue()));
        
        this.m_da.getSettings().save();
    }


    private void init()
    {
        
        ATSwingUtils.initLibrary();
        this.setBounds(0, 0, 350, 450);
        
        m_da.centerJDialog(this);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 350, 500);
        panel.setLayout(null);

        this.getContentPane().add(panel);

        
        label_title = ATSwingUtils.getTitleLabel("", 0, 25, 350, 35, panel, ATSwingUtils.FONT_BIG_BOLD);
        
        setTitle(m_ic.getMessage("RATIO_ENTRY"));
        label_title.setText(m_ic.getMessage("RATIO_ENTRY"));

        
        ATSwingUtils.getLabel(m_ic.getMessage("TIME_FROM"), 40, 75, 150, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD);
        tc_from = new TimeComponent();
        tc_from.setBounds(205, 75, 100, 25);
        panel.add(tc_from);

        
        ATSwingUtils.getLabel(m_ic.getMessage("TIME_TILL"), 40, 115, 150, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD);
        tc_to = new TimeComponent();
        tc_to.setBounds(205, 115, 100, 25);
        panel.add(tc_to);
        
        
        ATSwingUtils.getLabel(m_ic.getMessage("PROCENT_OF_BASE"), 40, 155, 150, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD);
        SpinnerNumberModel listDaysModel = new SpinnerNumberModel(100, 0, 1000, 1);
        procents = new JSpinner(listDaysModel);
        procents.addChangeListener(this);
        procents.setFont(ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL));
        procents.setBounds(220, 155, 70, 25); // 85
        panel.add(procents);
        
        
        rep = new RatioEntryPanel(m_da, 10);
        rep.setBounds(30, 120, 350, 210);  // 55
        rep.setEditable(false);
        panel.add(rep);

        
        ATSwingUtils.getButton("  " + m_ic.getMessage("OK"), 
                               60, 375, 110, 25, panel, ATSwingUtils.FONT_NORMAL, 
                               "ok.png", 
                               "ok", this, m_da);

        ATSwingUtils.getButton("  " + m_ic.getMessage("CANCEL"), 
                               180, 375, 110, 25, panel, ATSwingUtils.FONT_NORMAL, 
                               "cancel.png", 
                               "cancel", this, m_da);
        
        
        help_button = m_da.createHelpButtonByBounds(180, 345, 110, 25, this);

        panel.add(help_button);

        m_da.enableHelp(this);
        
        rep.calculateRatio(65, m_da.getIntValue(this.procents.getValue()) );
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
            
        }
        
        else
            System.out.println("RatioBaseDialog::unknown command: " + action);

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
        return m_actionDone;
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
        return "GGC_Ratio_Base";
    }


    public void stateChanged(ChangeEvent e)
    {
        //this.procents.getValue()
        rep.calculateRatio(65, m_da.getIntValue(this.procents.getValue()) );
    }
    
    
    
    
}
