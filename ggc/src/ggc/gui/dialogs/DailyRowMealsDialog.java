package ggc.gui.dialogs;

import ggc.core.data.DailyValuesRow;
import ggc.core.util.DataAccess;
import ggc.core.util.I18nControl;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.atech.graphics.components.JDecimalTextField;
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
 *  Filename:     DailyRowMealsDialog
 *  Description:  Dialog for adding foods entries to dailyrow
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */


public class DailyRowMealsDialog extends JDialog implements ActionListener, HelpCapable
{

    private static final long serialVersionUID = 6763016271693781911L;

    private I18nControl m_ic = I18nControl.getInstance();
    private DataAccess m_da = DataAccess.getInstance();
//    private GGCProperties props = m_da.getSettings();

    JLabel label_title;
    JDecimalTextField ftf_ch;
    JTextArea text_area;
    
    //DateTimeComponent dtc;
    //JButton AddButton;
    //String sDate = null;
    //DailyValues dV = null;
    DailyValuesRow m_dailyValuesRow = null;

    //NumberFormat bg_displayFormat, bg_editFormat;

    JComponent components[] = new JComponent[9];

    Font f_normal = m_da.getFont(DataAccess.FONT_NORMAL);
    Font f_bold = m_da.getFont(DataAccess.FONT_NORMAL);
    boolean in_process;
    boolean debug = true;
    JButton help_button = null;
    JPanel main_panel = null;

    //private Container m_parent = null;

    /**
     * Constructor
     * 
     * @param row
     * @param dialog
     */
    public DailyRowMealsDialog(DailyValuesRow row, JDialog dialog)
    {
        super(dialog, "", true);

        //m_parent = dialog;
        this.m_dailyValuesRow = row;
        
        init();
        load();
        m_da.centerJDialog(this);

        this.setVisible(true);
    }



    private void load()
    {
        this.text_area.setText(this.m_dailyValuesRow.getFoodDescription());

        String ch = this.m_dailyValuesRow.getFoodDescriptionCH();
        
        if (ch!=null)
        {
            
        
            System.out.println("CH:" + ch);
            
            ch = ch.replace(DataAccess.false_decimal, DataAccess.real_decimal);
            
            ch = ch.replace(",", ".");
    
            try
            {
                float f = Float.parseFloat(ch);
                System.out.println("CH:" + f);
                
                this.ftf_ch.setValue(f);
            }
            catch(Exception ex)
            {
                System.out.println("load ex: " + ex);
            }
        }

    }

    private void save()
    {
        this.m_dailyValuesRow.setFoodDescription(this.text_area.getText());
        
        System.out.println(this.ftf_ch.getValue());
        System.out.println(this.ftf_ch.getCurrentValue());
        
        float ch = m_da.getFloatValue(this.ftf_ch.getCurrentValue()); 
        //ch = ch.replace(DataAccess.false_decimal, DataAccess.real_decimal);

        System.out.println("Save.Float= " + ch);
        
        if (ch>0)
            this.m_dailyValuesRow.setFoodDescriptionCH("" + ch); //DataAccess.Decimal2Format.format(ch));
        else
            this.m_dailyValuesRow.setFoodDescriptionCH("");
        
    }
    
    
    

    private void init()
    {
        m_da.addComponent(this);
        
        ATSwingUtils.initLibrary();
        
        
        int x = 0;
        int y = 0;
        int width = 400;
        int height = 340;

        /*
        Rectangle bnd = m_parent.getBounds();

        x = (bnd.width / 2) + bnd.x - (width / 2);
        y = (bnd.height / 2) + bnd.y - (height / 2);
        */
        
        this.setBounds(x, y, width, height);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, width, height);
        panel.setLayout(null);

        main_panel = panel;

        this.getContentPane().add(panel);

        setTitle(m_ic.getMessage("FOOD_BY_DESCRIPTION_TITLE"));
        label_title = ATSwingUtils.getTitleLabel(m_ic.getMessage("FOOD_BY_DESCRIPTION_TITLE"), 
            0, 15, 400, 35, panel, 
            ATSwingUtils.FONT_BIG_BOLD);
        
        ATSwingUtils.getLabel(m_ic.getMessage("DESCRIBE_FOODS"), 
            40, 65, 310, 50, panel);
        
        this.text_area = new JTextArea();
        this.text_area.setLineWrap(true);
        JScrollPane scr = new JScrollPane(text_area);
        scr.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER|JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scr.setBounds(40, 115, 310, 75);
        panel.add(scr);
        
        ATSwingUtils.getLabel(m_ic.getMessage("CH_LONG") +":", 
            40, 210, 100, 25, panel);
        
        this.ftf_ch = ATSwingUtils.getNumericTextField(2, 2, 
            new Float(0.0f), 160, 210, 55, 25, panel); 
            
        ATSwingUtils.getButton(m_ic.getMessage("CALCULATE"), 
            230, 210, 120, 25, 
            panel, ATSwingUtils.FONT_NORMAL, null, "calculate", this, m_da);
        
        ATSwingUtils.getButton(m_ic.getMessage("OK"), 
            30, 260, 110, 25, 
            panel, ATSwingUtils.FONT_NORMAL, "ok.png", "ok", this, m_da);
        
        ATSwingUtils.getButton(m_ic.getMessage("CANCEL"), 
            145, 260, 110, 25, 
            panel, ATSwingUtils.FONT_NORMAL, "cancel.png", "cancel", this, m_da);
        
        help_button = m_da.createHelpButtonByBounds(260, 260, 110, 25, this);
        panel.add(help_button);
        m_da.enableHelp(this);

    }


    private void calculateCH()
    {
        String txt = this.text_area.getText();
        
        if ((txt.length()==0) || (!txt.contains("[")))
        {
            JOptionPane.showMessageDialog(this, 
                m_ic.getMessage("NO_DESCRIPTIONS_WITH_CH"), 
                m_ic.getMessage("WARNING"), JOptionPane.WARNING_MESSAGE);
        }
        else
        {
            float sum = this.calculationInternal(this.text_area.getText());
            System.out.println("doing calcualtion: " + sum);
            
            this.ftf_ch.setValue(new Float(sum));
        }
            
    }
    
    private float calculationInternal(String text)
    {
        float sum = 0.0f;
        
        StringTokenizer strtok = new StringTokenizer(text, "[");
        strtok.nextToken();
        
        while(strtok.hasMoreTokens())
        {
            String t = strtok.nextToken();
            
            System.out.println("Token: " + t);
            
            if (t.indexOf("]")==-1)
                continue;
                
            t = t.substring(0, t.indexOf("]"));
            System.out.println("Bef: " + t);
            
            t = t.replace(",", "."); //DataAccess.false_decimal, DataAccess.real_decimal);

            System.out.println("False dec: " + DataAccess.false_decimal + " rwal=" + DataAccess.real_decimal);

            System.out.println("Aff: " + t);
            try
            {
                float f = Float.parseFloat(t);
                System.out.println("entry: " + f);
                sum += f;
            }
            catch(Exception ex)
            {
                System.out.println("Ex: " + ex);
            }
        }
        
        return sum;
        
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
            save();
            m_da.removeComponent(this);
            this.dispose();
        }
        else if (action.equals("calculate"))
        {
            calculateCH();
        }
        else
            System.out.println("DailyRowMealsDialog::unknown command: " + action);

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
        return this.help_button;
    }

    /**
     * getHelpId - get id for Help
     */
    public String getHelpId()
    {
        return "pages.GGC_BG_Daily_Add_Food_Desc";
    }
    
}
