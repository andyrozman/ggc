/*
 * GGC - GNU Gluco Control
 * 
 * A pure java app to help you manage your diabetes.
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
 * Filename: DailyRowDialog
 * 
 * Purpose: Dialog for adding entry for day and time
 * 
 * Author: andyrozman {andy@atech-software.com}
 */
package ggc.gui.dialogs;

import ggc.core.data.DailyValues;
import ggc.core.data.DailyValuesRow;
import ggc.core.util.DataAccess;
import ggc.core.util.GGCProperties;
import ggc.core.util.I18nControl;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import com.atech.graphics.components.TimeComponent;
import com.atech.help.HelpCapable;
import com.atech.utils.ATechDate;

// fix this

public class BolusHelper extends JDialog implements ActionListener, KeyListener, HelpCapable, FocusListener
{

    /**
     * 
     */
    private static final long serialVersionUID = 5048286134436536838L;
    
    private float curr_bg;
    private float curr_ch;
    private long time;
    private int calc_insulin;
    
    JLabel lbl_bg_oh, lbl_correction, lbl_carb_dose, lbl_together, lbl_time;

    String bg_unit;
    
    

    /*
     * focusGained
     */
    public void focusGained(FocusEvent arg0)
    {
    }

    boolean in_action = false;

    /*
     * focusLost
     */
    public void focusLost(FocusEvent ev)
    {
        if (in_action)
            return;

        in_action = true;

        // JFormattedTextField targ = (JFormattedTextField)ev.
/*
        if (ev.getSource().equals(this.ftf_bg1))
        {
            // System.out.println("focus lost: bg1");
            int val = m_da.getJFormatedTextValueInt(ftf_bg1);
            float v_2 = m_da.getBGValueDifferent(DataAccess.BG_MGDL, val);
            this.ftf_bg2.setValue(new Float(v_2));
        }
        else if (ev.getSource().equals(this.ftf_bg2))
        {
            // System.out.println("focus lost: bg2");
            float val = m_da.getJFormatedTextValueFloat(ftf_bg2);
            int v_2 = (int) m_da.getBGValueDifferent(DataAccess.BG_MMOL, val);
            this.ftf_bg1.setValue(new Integer(v_2));
        }
        else
            System.out.println("focus lost: unknown");

        in_action = false;
        */
    }

    private I18nControl m_ic = I18nControl.getInstance();
    private DataAccess m_da = DataAccess.getInstance();
    private GGCProperties props = m_da.getSettings();

    private boolean m_actionDone = false;

    // private long last_change = 0;

    // static AddRowFrame singleton = null;

    JTextField DateField, TimeField, /* BGField, Ins1Field, Ins2Field, BUField, */
    ActField, CommentField, UrineField;

    JComboBox cob_bg_type; // = new JComboBox();

    JFormattedTextField ftf_ch_ins, ftf_bg_ins;
    // JTextFieldFormatted

    JLabel label_title = new JLabel();
    JLabel label_food;
    JCheckBox cb_food_set;

    TimeComponent tc;

    JButton AddButton;

    String sDate = null;

    DailyValues dV = null;
    DailyValuesRow m_dailyValuesRow = null;

    NumberFormat bg_displayFormat, bg_editFormat;

    JComponent components[] = new JComponent[9];

    Font f_normal = m_da.getFont(DataAccess.FONT_NORMAL);
    Font f_bold = m_da.getFont(DataAccess.FONT_NORMAL_BOLD);
    boolean in_process;
    boolean debug = true;
    JButton help_button = null;
    JPanel main_panel = null;

    private Container m_parent = null;

    
    public BolusHelper(JDialog dialog, float bg, float ch, long time)
    {
        super(dialog, "", true);
        m_parent = dialog;
        
        this.curr_bg = bg;
        this.curr_ch = ch;
        this.time = time;

        this.init();
        this.readRatios();
        this.calculateInsulin();
        this.setVisible(true);
    }

    
    public BolusHelper(JFrame frame)
    {
        super(frame, "", true);
        //m_parent = dialog;
        init();
        this.readRatios();
        this.setVisible(true);
    }
    
    
    


    /*
    public void setDate()
    {
        // System.out.println("Date: " + sDate);

        StringTokenizer strtok = new StringTokenizer(sDate, ".");

        String day = strtok.nextToken();
        String month = strtok.nextToken();
        String year = strtok.nextToken();

        String dt = year + m_da.getLeadingZero(month, 2) + m_da.getLeadingZero(day, 2) + "0000";

        System.out.println("sDate: " + sDate);

        this.dtc.setDateTime(Long.parseLong(dt));

    }*/

    public void load()
    {
//        this.dtc.setDateTime(this.m_dailyValuesRow.getDateTime());
/*
        if (m_dailyValuesRow.getBG() > 0)
        {
            this.ftf_bg1.setValue(new Integer((int) m_dailyValuesRow.getBGRaw()));
            this.ftf_bg2.setValue(new Float(m_da.getBGValueDifferent(DataAccess.BG_MGDL, m_dailyValuesRow.getBGRaw())));
        }

        this.ftf_ins1.setValue(new Integer((int) this.m_dailyValuesRow.getIns1()));
        this.ftf_ins2.setValue(new Integer((int) this.m_dailyValuesRow.getIns2()));
        this.ftf_ch.setValue(new Float(this.m_dailyValuesRow.getCH()));

        ActField.setText(this.m_dailyValuesRow.getActivity());
        UrineField.setText(this.m_dailyValuesRow.getUrine());

        this.cb_food_set.setEnabled(false);
        this.cb_food_set.setSelected(this.m_dailyValuesRow.areMealsSet());
        this.cb_food_set.setEnabled(true);

        CommentField.setText(this.m_dailyValuesRow.getComment());
*/
    }

    /*
     * private void save() {
     * 
     * }
     */

    private void init()
    {
        /*
        int x = 0;
        int y = 0;
        int width = 400;
        int height = 500;

        Rectangle bnd = m_parent.getBounds();

        x = (bnd.width / 2) + bnd.x - (width / 2);
        y = (bnd.height / 2) + bnd.y - (height / 2);

        this.setBounds(x, y, width, height);*/

        int width = 400;
        int height = 430;
        
        this.setResizable(false);
        this.setBounds(0, 0, width, height);
        m_da.centerJDialog(this);
        

        setTitle(m_ic.getMessage("BOLUS_HELPER"));
        label_title.setText(m_ic.getMessage("BOLUS_HELPER"));
        
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, width, height);
        panel.setLayout(null);

        main_panel = panel;

        this.getContentPane().add(panel);

        label_title.setFont(m_da.getFont(DataAccess.FONT_BIG_BOLD));
        label_title.setHorizontalAlignment(JLabel.CENTER);
        label_title.setBounds(0, 15, 400, 35);
        panel.add(label_title);

        if (m_da.getBGMeasurmentType()==DataAccess.BG_MMOL)
            this.bg_unit = "mmol/L";
        else
            this.bg_unit = "mg/dL";
        
        
//        addLabel(m_ic.getMessage("DATE") + ":", 78, panel);
        addLabel(m_ic.getMessage("TIME") + ":", 30, 78, panel);
//        addLabel(m_ic.getMessage("BLOOD_GLUCOSE") + ":", 138, panel);
        // 138 168 198 228 258
        addLabel(m_ic.getMessage("CH_INSULIN_RATIO") + ":", 30, 138, 200, panel);
        addLabel(m_ic.getMessage("BG_INSULIN_RATIO") + ":", 30, 168, 200, panel);
        addLabel(m_ic.getMessage("BG_OH_RATIO") + ":", 30, 198, 200, panel);

        addLabel(m_ic.getMessage("CORRECTION_DOSE") + ":", 30, 243, 200, panel);
        addLabel(m_ic.getMessage("CARB_DOSE") + ":", 30, 268, 200, panel);
        addLabel(m_ic.getMessage("TOGETHER") + ":", 30, 298, 200, panel);
        

        
        addComponent(this.lbl_bg_oh = new JLabel(m_ic.getMessage("BG_OH_RATIO") + ":"), 180, 198, 200, panel);
        

        addComponent(lbl_correction = new JLabel(m_ic.getMessage("NO_BG_MEASURE")) , 200, 243, 200, panel);
        addComponent(lbl_carb_dose = new JLabel(m_ic.getMessage("NO_CARBS_DEFINED") ), 200, 268, 200, panel);
        addComponent(lbl_together = new JLabel("0 E"), 200, 298, 200, panel);
        
        
/*
        this.tc = new TimeComponent();
        tc.setBounds(140, 75, 100, 35);
        panel.add(tc);
  */      
        addComponent(new JLabel(ATechDate.getTimeString(ATechDate.FORMAT_DATE_AND_TIME_MIN, this.time)), 140, 78, 100, panel);

        this.ftf_ch_ins = getTextField(2, 2, new Float(0.0f), 180, 138, 45, 25, panel);
        this.ftf_bg_ins = getTextField(2, 2, new Float(0.0f), 180, 168, 45, 25, panel);

        
        addComponent(new JLabel(" g " + m_ic.getMessage("CH") + "  =  1 " + m_ic.getMessage("UNIT_SHORT") + " " + m_ic.getMessage("INSULIN")), 230, 140, 200, panel);
        addComponent(new JLabel(" " + this.bg_unit + "  =  1 " + m_ic.getMessage("UNIT_SHORT") + " " + m_ic.getMessage("INSULIN")), 230, 170, 200, panel);
        
        
//        this.lbl_bg_oh.setText("1 mmol/L  =  " + DataAccess.MmolDecimalFormat.format(cal_r) + " g " + m_ic.getMessage("CH"));
        
        
        //        this.ftf_oh_bg = getTextField(2, 0, new Integer(0), 140, 198, 55, 25, panel);

/*        
        this.ftf_ins2 = getTextField(2, 0, new Integer(0), 140, 228, 55, 25, panel);
        this.ftf_ch = getTextField(2, 2, new Float(0.0f), 140, 258, 55, 25, panel);

        this.ftf_bg1.addFocusListener(this);
        this.ftf_bg2.addFocusListener(this);
*/
        // this.ftf_bg1.addKeyListener(this);
        // this.ftf_bg2.addKeyListener(this);
/*
        this.ftf_bg2.addKeyListener(new KeyListener()
        {

            public void keyPressed(KeyEvent arg0)
            {
            }

            public void keyTyped(KeyEvent arg0)
            {
            }

            public void keyReleased(KeyEvent ke)
            {
                if (ke.getKeyCode() == KeyEvent.VK_PERIOD)
                {
                    JFormattedTextField tf = (JFormattedTextField) ke.getSource();
                    String s = tf.getText();
                    s = s.replace('.', ',');
                    tf.setText(s);
                }
            }

        });
*/
//        addComponent(cb_food_set = new JCheckBox(" " + m_ic.getMessage("FOOD_SET")), 120, 290, 200, panel);
//        addComponent(UrineField = new JTextField(), 120, 318, 240, panel);
//        addComponent(ActField = new JTextField(), 120, 348, 240, panel);
//        addComponent(CommentField = new JTextField(), 120, 378, 240, panel);


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

        help_button = m_da.createHelpButtonByBounds(260, 350, 110, 25, this);
        panel.add(help_button);
        m_da.enableHelp(this);

    }


    public JFormattedTextField getTextField(int columns, int decimal_places, Object value, int x, int y, int width,
            int height, Container cont)

    {

        NumberFormat displayFormat, editFormat;

        displayFormat = NumberFormat.getNumberInstance();
        displayFormat.setMinimumFractionDigits(0);
        displayFormat.setMaximumFractionDigits(decimal_places);

        editFormat = NumberFormat.getNumberInstance();
        editFormat.setMinimumFractionDigits(0);
        editFormat.setMaximumFractionDigits(decimal_places);

        JFormattedTextField ftf = new JFormattedTextField(
                new DefaultFormatterFactory(new NumberFormatter(displayFormat), new NumberFormatter(displayFormat),
                        new NumberFormatter(editFormat)));

        ftf.setValue(value);
        ftf.setBounds(x, y, width, height);
        ftf.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
        ftf.addKeyListener(this);
        cont.add(ftf);

        return ftf;

    }



    public void addLabel(String text, int posY, JPanel parent)
    {
        JLabel label = new JLabel(text);
        label.setBounds(30, posY, 100, 23);
        label.setFont(f_bold);
        parent.add(label);
        // a.add(new JLabel(m_ic.getMessage("DATE") + ":",
        // SwingConstants.RIGHT));

    }

    public void addLabel(String text, int posX, int posY, JPanel parent)
    {
        JLabel label = new JLabel(text);
        label.setBounds(posX, posY, 100, 23);
        label.setFont(f_bold);
        parent.add(label);
        // a.add(new JLabel(m_ic.getMessage("DATE") + ":",
        // SwingConstants.RIGHT));

    }

    
    public void addLabel(String text, int posX, int posY, int width, JPanel parent)
    {
        JLabel label = new JLabel(text);
        label.setBounds(posX, posY, width, 25);
        label.setFont(f_bold);
        parent.add(label);
        // a.add(new JLabel(m_ic.getMessage("DATE") + ":",
        // SwingConstants.RIGHT));

    }
    
    
    public void addComponent(JComponent comp, int posX, int posY, int width, JPanel parent)
    {
        addComponent(comp, posX, posY, width, 23, true, parent);
    }

    public void addComponent(JComponent comp, int posX, int posY, int width, int height, boolean change_font,
            JPanel parent)
    {
        comp.setBounds(posX, posY, width, height);
        comp.addKeyListener(this);
        parent.add(comp);
    }

    private void readRatios()
    {
        this.ftf_bg_ins.setValue(1.25f);
        this.ftf_ch_ins.setValue(6.25f);
        
        float cal_r = 6.25f / 1.25f;
        
        this.lbl_bg_oh.setText("1 " + this.bg_unit + "  =  " + DataAccess.MmolDecimalFormat.format(cal_r) + " g " + m_ic.getMessage("CH"));
        
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
            this.lbl_correction.setText(DataAccess.MmolDecimalFormat.format(cu_fix) + "  " + m_ic.getMessage("UNIT_SHORT"));
            
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
            this.lbl_carb_dose.setText(DataAccess.MmolDecimalFormat.format(ch_fix) + "  " + m_ic.getMessage("UNIT_SHORT"));
            
            sum += ch_fix;
        }
        else
        {
            this.lbl_carb_dose.setText(m_ic.getMessage("NO_CARBS_DEFINED"));
        }
        
        this.lbl_together.setText(DataAccess.MmolDecimalFormat.format(sum) + "  " + m_ic.getMessage("UNIT_SHORT"));
        
        this.calc_insulin = Math.round(sum);
        
    }
    
    
    boolean res = false;
    
    public boolean hasResult()
    {
        return res;
    }
    
    public int getResult()
    {
        return this.calc_insulin;
    }
    
    

    /*
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
     * String button_command[] = { "update_ch",
     * m_ic.getMessage("UPDATE_FROM_FOOD"), "edit_food",
     * m_ic.getMessage("EDIT_FOOD"), "ok", m_ic.getMessage("OK"), "cancel",
     * m_ic.getMessage("CANCEL"), // "help", m_ic.getMessage("HELP")
     */

    private void cmdOk()
    {
        System.out.println("cmdOk not implemented");
    }

    public boolean isFieldSet(String text)
    {
        if ((text == null) || (text.trim().length() == 0))
            return false;
        else
            return true;
    }

    public boolean actionSuccesful()
    {
        return m_actionDone;
    }

    public void keyTyped(KeyEvent e)
    {
    }

    public void keyPressed(KeyEvent e)
    {
    }

    /*
     * Invoked when a key has been released. See the class description for
     * {@link KeyEvent} for a definition of a key released event.
     */
    public void keyReleased(KeyEvent e)
    {

        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            cmdOk();
        }

    }


    public String checkDecimalFields(String field)
    {
        field = field.replace(',', '.');
        return field;
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
        return "pages.GGC_BG_Daily_Bolus_Helper";
    }

}
