/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: DailyRowDialog
 *
 *  Purpose:  Dialog for adding entry for day and time
 *
 *  Author:   andyrozman {andy@atech-software.com}
 *
 */
package ggc.pump.gui.manual;

import ggc.core.db.hibernate.pump.PumpDataExtendedH;
import ggc.pump.util.DataAccessPump;
import ggc.pump.util.I18nControl;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.atech.graphics.components.JDecimalTextField;
import com.atech.help.HelpCapable;

// fix this

public class PumpDataAdditionalWizardOne extends JDialog implements ActionListener, HelpCapable
{

    private static final long serialVersionUID = -5592188365225021329L;
    private I18nControl m_ic = I18nControl.getInstance();
    private DataAccessPump m_da = DataAccessPump.getInstance();


    JLabel label_title = new JLabel();

//    JComponent components[] = new JComponent[9];

    Font f_normal = m_da.getFont(DataAccessPump.FONT_NORMAL);
    Font f_bold = m_da.getFont(DataAccessPump.FONT_NORMAL);
    boolean debug = true;
    JButton help_button = null;
    JPanel main_panel = null;
    private Container m_parent = null;
    
    private Hashtable<String,PumpDataExtendedH> old_data = null;
    

    public PumpDataAdditionalWizardOne(Hashtable<String,PumpDataExtendedH> data, JDialog parent)
    {
        super(parent, "", true);

        this.old_data = data;
        m_parent = parent;
        init();
    }




    private void init()
    {
        int width = 320;
        int height = 300;

        this.setSize(width,height);
        
        this.m_da.centerJDialog(this, this.m_parent);
        
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, width, height);
        panel.setLayout(null);

        main_panel = panel;

        this.getContentPane().add(panel);

        label_title.setFont(m_da.getFont(DataAccessPump.FONT_BIG_BOLD));
        label_title.setHorizontalAlignment(JLabel.CENTER);
        label_title.setBounds(0, 35, width, 35);
        panel.add(label_title);

        
        setTitle(m_ic.getMessage("ADD_PARAMETER"));
        label_title.setText(m_ic.getMessage("ADD_PARAMETER"));
        

        JLabel label = new JLabel(m_ic.getMessage("SELECT_ADDITONAL_DATA"));
        label.setBounds(30, 100, 250, 25);
        label.setFont(f_bold);
        panel.add(label);
        
        
        
        
        JComboBox cb_type = new JComboBox(createItems());
        cb_type.setBounds(30, 135, 240, 25);
        panel.add(cb_type);
        
        

        
        String button_command[] = { 
                                    "cancel", m_ic.getMessage("CANCEL"),
                                    "next", m_ic.getMessage("NEXT"),
//                                    "help", m_ic.getMessage("HELP")
        };
        
        

        
        // button
        String button_icon[] = { "cancel.png", "nav_right_blue.png" }; 
                                 //null, "ok.png", "cancel.png" };

        int button_coord[] = { 30, 230, 120, 1,
                               160, 230, 130, 1, 
        //                       170, 190, 140, 1, 
//                               30, 620, 110, 1, 
//                               145, 620, 110, 1,
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
                addComponent(button, button_coord[i], button_coord[i + 1],
                        button_coord[i + 2], panel);
            else
                addComponent(button, button_coord[i], button_coord[i + 1],
                        button_coord[i + 2], 25, false, panel);
        }

        help_button = m_da.createHelpButtonByBounds(170, 195, 120, 25, this);
        panel.add(help_button);

//        m_da.enableHelp(this);

    }

    public Object[] createItems()
    {
        ArrayList<String> items = new ArrayList<String>();
        
        if (!this.old_data.containsKey("ACTIVITY"))
        {
            items.add(m_ic.getMessage("ACTIVITY"));
        }

        if (!this.old_data.containsKey("COMMENT"))
        {
            items.add(m_ic.getMessage("COMMENT"));
        }
        
        if (!this.old_data.containsKey("BLOOD_GLUCOSE"))
        {
            items.add(m_ic.getMessage("BLOOD_GLUCOSE"));
        }
        
        if (!this.old_data.containsKey("URINE"))
        {
            items.add(m_ic.getMessage("URINE"));
        }
        
        if (!this.old_data.containsKey("CH"))
        {
            items.add(m_ic.getMessage("CH"));
        }
        
        if (!this.old_data.containsKey("FOOD"))
        {
            items.add(m_ic.getMessage("FOOD"));
        }
        
        
        return items.toArray();
        
    }
    
    
    public boolean isMealSet()
    {
        return false;
        /*
        if ((this.m_dailyValuesRow.meals == null) || (this.m_dailyValuesRow.meals.trim().length() == 0))
            return false;
        else
            return true; */
    }

    public JFormattedTextField getTextField(int columns, int decimal_places,
            Object value, int x, int y, int width, int height, Container cont)

    {
        JDecimalTextField tf = new JDecimalTextField(value, decimal_places);
        tf.setBounds(x, y, width, height);
//        tf.addKeyListener(this);
        
        cont.add(tf);
        
        return tf;
        
        /*
        NumberFormat displayFormat, editFormat;

        displayFormat = NumberFormat.getNumberInstance();
        displayFormat.setMinimumFractionDigits(0);
        displayFormat.setMaximumFractionDigits(decimal_places);

        editFormat = NumberFormat.getNumberInstance();
        editFormat.setMinimumFractionDigits(0);
        editFormat.setMaximumFractionDigits(decimal_places);

        JFormattedTextField ftf = new JFormattedTextField(
                new DefaultFormatterFactory(new NumberFormatter(displayFormat),
                        new NumberFormatter(displayFormat),
                        new NumberFormatter(editFormat)));

        ftf.setValue(value);
        ftf.setBounds(x, y, width, height);
        ftf.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
        ftf.addKeyListener(this);
        cont.add(ftf);

        return ftf;
*/
    }


    public void addLabel(String text, int posY, JPanel parent)
    {
        JLabel label = new JLabel(text);
        label.setBounds(30, posY, 100, 25);
        label.setFont(f_bold);
        parent.add(label);
        // a.add(new JLabel(m_ic.getMessage("DATE") + ":",
        // SwingConstants.RIGHT));

    }

    public void addLabel(String text, int posX, int posY, JPanel parent)
    {
        JLabel label = new JLabel(text);
        label.setBounds(posX, posY, 100, 25);
        label.setFont(f_bold);
        parent.add(label);
        // a.add(new JLabel(m_ic.getMessage("DATE") + ":",
        // SwingConstants.RIGHT));

    }

    public void addComponent(JComponent comp, int posX, int posY, int width,
            JPanel parent)
    {
        addComponent(comp, posX, posY, width, 23, true, parent);
    }

    public void addComponent(JComponent comp, int posX, int posY, int width,
            int height, boolean change_font, JPanel parent)
    {
        comp.setBounds(posX, posY, width, height);
//        comp.addKeyListener(this);
        parent.add(comp);
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
        else if (action.equals("ok"))
        {
            cmdOk();
        }
/*        else if (action.equals("edit_food"))
        {
            
            DailyValuesMealSelectorDialog dvms = new DailyValuesMealSelectorDialog(
                    m_da, this.m_dailyValuesRow.getMealsIds());

            if (dvms.wasAction())
            {
                this.m_dailyValuesRow.setMealsIds(dvms.getStringForDb());
                this.ftf_ch.setValue(new Float(dvms.getCHSum()
                        .replace(',', '.')));

                if (!dvms.getStringForDb().equals(""))
                {
                    this.cb_food_set.setSelected(true);
                }
        }
        else if (action.equals("update_ch"))
        {
            PanelMealSelector pms = new PanelMealSelector(this, null,
                    this.m_dailyValuesRow.getMealsIds());
            this.ftf_ch.setValue(new Float(pms.getCHSumString().replace(',',
                    '.')));
        }
        else if (action.equals("bolus_helper"))
        {
            BolusHelper bh = new BolusHelper(this, m_da.getJFormatedTextValueFloat(ftf_bg2), m_da.getJFormatedTextValueFloat(this.ftf_ch), this.dtc.getDateTime());
            
            if (bh.hasResult())
            {
                this.ftf_ins1.setValue(bh.getResult());
            }
        } */
        else
            System.out.println("PumpDataRowDialog::unknown command: " + action);

    }


    private void cmdOk()
    {
        // TODO: 
        
/*        
        if (this.m_add_action)
        {
            // add

            if (debug)
                System.out.println("dV: " + dV);

            // this.m_dailyValuesRow = new DailyValuesRow();

            this.m_dailyValuesRow.setDateTime(this.dtc.getDateTime());

            // if (isFieldSet(BGField.getText()))

            float f = m_da.getJFormatedTextValueFloat(ftf_bg1);

            if (f > 0.0)
            {
                //this.m_dailyValuesRow.setBG(this.cob_bg_type.getSelectedIndex(
                // )+1, f);
                this.m_dailyValuesRow.setBG(1, f);
            }

            this.m_dailyValuesRow.setIns1(m_da
                    .getJFormatedTextValueInt(this.ftf_ins1));
            this.m_dailyValuesRow.setIns2(m_da
                    .getJFormatedTextValueInt(this.ftf_ins2));
            // checkDecimalFields(Ins1Field.getText()));
            //this.m_dailyValuesRow.setIns2(checkDecimalFields(Ins2Field.getText
            // ()));
            //this.m_dailyValuesRow.setCH(checkDecimalFields(BUField.getText()))
            // ;
            this.m_dailyValuesRow.setCH(m_da
                    .getJFormatedTextValueFloat(this.ftf_ch));
            this.m_dailyValuesRow.setActivity(ActField.getText());
            this.m_dailyValuesRow.setUrine(UrineField.getText());
            this.m_dailyValuesRow.setComment(CommentField.getText());
            // this.m_dailyValuesRow.setMealIdsList(null);

            dV.setNewRow(this.m_dailyValuesRow);
            this.m_actionDone = true;
            this.dispose();
        }
        else
        {

            // edit
            this.m_dailyValuesRow.setDateTime(this.dtc.getDateTime());

            float f = m_da.getJFormatedTextValueFloat(ftf_bg1);

            if (f > 0.0)
            {
                //this.m_dailyValuesRow.setBG(this.cob_bg_type.getSelectedIndex(
                // )+1, f);
                this.m_dailyValuesRow.setBG(1, f);
            }

            // if (isFieldSet(BGField.getText()))
            //this.m_dailyValuesRow.setBG(this.cob_bg_type.getSelectedIndex()+1,
            // checkDecimalFields(BGField.getText()));
            this.m_dailyValuesRow.setIns1(m_da
                    .getJFormatedTextValueInt(this.ftf_ins1));
            this.m_dailyValuesRow.setIns2(m_da
                    .getJFormatedTextValueInt(this.ftf_ins2));

            //this.m_dailyValuesRow.setIns1(checkDecimalFields(Ins1Field.getText
            // ()));
            //this.m_dailyValuesRow.setIns2(checkDecimalFields(Ins2Field.getText
            // ()));
            this.m_dailyValuesRow.setCH(m_da
                    .getJFormatedTextValueFloat(this.ftf_ch));
            //this.m_dailyValuesRow.setCH(checkDecimalFields(BUField.getText()))
            // ;
            this.m_dailyValuesRow.setActivity(ActField.getText());
            this.m_dailyValuesRow.setUrine(UrineField.getText());
            this.m_dailyValuesRow.setComment(CommentField.getText());
            // this.m_dailyValuesRow.setMealIdsList(null);

            // mod.fireTableChanged(null);
            // clearFields();
            this.m_actionDone = true;
            this.dispose();
        }
*/
    }

    public boolean isFieldSet(String text)
    {
        if ((text == null) || (text.trim().length() == 0))
            return false;
        else
            return true;
    }


    public void keyTyped(KeyEvent e)
    {
    }

    public void keyPressed(KeyEvent e)
    {
    }

    /**
     * Invoked when a key has been released. See the class description for
     * {@link KeyEvent} for a definition of a key released event.
     */
    public void keyReleased(KeyEvent e)
    {
/*
        if ((e.getSource().equals(this.ftf_bg1)) || (e.getSource().equals(this.ftf_bg2)))
        {
            focusProcess(e.getSource());
        }
        
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            cmdOk();
        }
*/
    }

    // ****************************************************************
    // ****** HelpCapable Implementation *****
    // ****************************************************************

    /*
     * getComponent - get component to which to attach help context
     */
    public Component getComponent()
    {
        return this.getRootPane();
    }

    /*
     * getHelpButton - get Help button
     */
    public JButton getHelpButton()
    {
        return this.help_button;
    }

    /*
     * getHelpId - get id for Help
     */
    public String getHelpId()
    {
        return "pages.GGC_Pump_Add_Parameter_Wizard1";
    }

}
