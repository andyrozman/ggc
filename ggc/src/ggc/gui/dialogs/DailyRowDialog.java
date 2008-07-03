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
package ggc.gui.dialogs;

import ggc.core.data.DailyValues;
import ggc.core.data.DailyValuesRow;
import ggc.core.nutrition.dialogs.DailyValuesMealSelectorDialog;
import ggc.core.nutrition.panels.PanelMealSelector;
import ggc.core.util.DataAccess;
import ggc.core.util.GGCProperties;
import ggc.core.util.I18nControl;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.NumberFormat;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.atech.graphics.components.DateTimeComponent;
import com.atech.graphics.components.JDecimalTextField;
import com.atech.help.HelpCapable;

// fix this

public class DailyRowDialog extends JDialog implements ActionListener,
        KeyListener, HelpCapable, FocusListener
{

    /**
     * 
     */
    private static final long serialVersionUID = 6763016271693781911L;

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
        focusProcess(ev.getSource());
    }

    private void focusProcess(Object src)
    {
        
        if (in_action)
            return;

        in_action = true;

        if (src.equals(this.ftf_bg1))
        {
            int val = m_da.getJFormatedTextValueInt(ftf_bg1);
            float v_2 = m_da.getBGValueDifferent(DataAccess.BG_MGDL, val);
            this.ftf_bg2.setValue(new Float(v_2));
        }
        else if (src.equals(this.ftf_bg2))
        {
            // System.out.println("focus lost: bg2");
            float val = m_da.getJFormatedTextValueFloat(ftf_bg2);
            int v_2 = (int) m_da.getBGValueDifferent(DataAccess.BG_MMOL, val);
            this.ftf_bg1.setValue(new Integer(v_2));
        }
        else
            System.out.println("focus lost: unknown");

        in_action = false;
        
        
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

    JFormattedTextField ftf_ins1, ftf_ins2, ftf_bg1, ftf_ch, ftf_bg2;
    // JTextFieldFormatted

    JLabel label_title = new JLabel();
    JLabel label_food;
    JCheckBox cb_food_set;

    DateTimeComponent dtc;

    JButton AddButton;

    String sDate = null;

    DailyValues dV = null;
    DailyValuesRow m_dailyValuesRow = null;

    NumberFormat bg_displayFormat, bg_editFormat;

    JComponent components[] = new JComponent[9];

    Font f_normal = m_da.getFont(DataAccess.FONT_NORMAL);
    Font f_bold = m_da.getFont(DataAccess.FONT_NORMAL);
    boolean in_process;
    boolean debug = true;
    JButton help_button = null;
    JPanel main_panel = null;

    private boolean m_add_action = true;
    private Container m_parent = null;

    public DailyRowDialog(DailyValues ndV, String nDate, JDialog dialog)
    {
        super(dialog, "", true);

        m_parent = dialog;
        initParameters(ndV, nDate);
    }

    public DailyRowDialog(DailyValues ndV, String nDate, JFrame frame)
    {
        super(frame, "", true);
        m_parent = frame;
        initParameters(ndV, nDate);
    }

    public DailyRowDialog(DailyValuesRow ndr, JDialog dialog)
    {
        super(dialog, "", true);
        m_parent = dialog;
        initParameters(ndr);
    }

    public DailyRowDialog(DailyValuesRow ndr, JFrame frame)
    {
        super(frame, "", true);
        m_parent = frame;
        initParameters(ndr);
    }

    public void initParameters(DailyValues ndV, String nDate)
    {
        // if (add)
        setTitle(m_ic.getMessage("ADD_ROW"));
        label_title.setText(m_ic.getMessage("ADD_ROW"));

        // else
        // setTitle(m_ic.getMessage("EDIT_NEW_ROW"));

        System.out.println(props.getBG_unit());

        sDate = nDate;
        dV = ndV;
        this.m_add_action = true;
        // mod = m;
        init();
        setDate();
        // load();

        if (this.m_dailyValuesRow == null)
        {
            this.m_dailyValuesRow = new DailyValuesRow();
        }

        this.setVisible(true);
    }

    public void initParameters(DailyValuesRow ndr)
    {
        setTitle(m_ic.getMessage("EDIT_ROW"));
        label_title.setText(m_ic.getMessage("EDIT_ROW"));

        sDate = ndr.getDateAsString();
        this.m_dailyValuesRow = ndr;

        this.m_add_action = false;
        init();
        load();

        this.setVisible(true);
    }

    public void setDate()
    {
        // System.out.println("Date: " + sDate);

        StringTokenizer strtok = new StringTokenizer(sDate, ".");

        String day = strtok.nextToken();
        String month = strtok.nextToken();
        String year = strtok.nextToken();
        
        GregorianCalendar gc = new GregorianCalendar();

        //int hour = gc.get(GregorianCalendar.HOUR_OF_DAY);
        
        
        
        String dt = year + m_da.getLeadingZero(month, 2)
                + m_da.getLeadingZero(day, 2) 
                + m_da.getLeadingZero(gc.get(GregorianCalendar.HOUR_OF_DAY), 2)  
                + m_da.getLeadingZero(gc.get(GregorianCalendar.MINUTE), 2)
                ;

        //System.out.println("sDate: " + sDate);

        this.dtc.setDateTime(Long.parseLong(dt));

    }

    public void load()
    {
        this.dtc.setDateTime(this.m_dailyValuesRow.getDateTime());

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

    }

    /*
     * private void save() {
     * 
     * }
     */

    private void init()
    {
        int x = 0;
        int y = 0;
        int width = 400;
        int height = 500;

        Rectangle bnd = m_parent.getBounds();

        x = (bnd.width / 2) + bnd.x - (width / 2);
        y = (bnd.height / 2) + bnd.y - (height / 2);

        this.setBounds(x, y, width, height);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, width, height);
        panel.setLayout(null);

        main_panel = panel;

        this.getContentPane().add(panel);

        label_title.setFont(m_da.getFont(DataAccess.FONT_BIG_BOLD));
        label_title.setHorizontalAlignment(JLabel.CENTER);
        label_title.setBounds(0, 15, 400, 35);
        panel.add(label_title);

        addLabel(m_ic.getMessage("DATE") + ":", 78, panel);
        addLabel(m_ic.getMessage("TIME") + ":", 108, panel);
        addLabel(m_ic.getMessage("BLOOD_GLUCOSE") + ":", 138, panel);
        addLabel(props.getIns1Name() + " (" + props.getIns1Abbr() + ") :", 198,
                panel);
        addLabel(props.getIns2Name() + " (" + props.getIns2Abbr() + "):", 228,
                panel);
        addLabel(m_ic.getMessage("CH_LONG") + ":", 258, panel);
        addLabel(m_ic.getMessage("FOOD") + ":", 288, panel);
        addLabel(m_ic.getMessage("URINE") + ":", 318, panel);
        addLabel(m_ic.getMessage("ACTIVITY") + ":", 348, panel);
        addLabel(m_ic.getMessage("COMMENT") + ":", 378, panel);

        addLabel("mg/dL", 140, 138, panel);
        addLabel("mmol/L", 140, 168, panel);

        this.dtc = new DateTimeComponent(this.m_ic,
                DateTimeComponent.ALIGN_VERTICAL, 5);
        dtc.setBounds(140, 75, 100, 35);
        panel.add(dtc);

        this.ftf_bg1 = getTextField(2, 0, new Integer(0), 190, 138, 55, 25,
                panel);
        this.ftf_bg2 = getTextField(2, 1, new Float(0.0f), 190, 168, 55, 25,
                panel);

        this.ftf_ins1 = getTextField(2, 0, new Integer(0), 140, 198, 55, 25,
                panel);
        this.ftf_ins2 = getTextField(2, 0, new Integer(0), 140, 228, 55, 25,
                panel);
        this.ftf_ch = getTextField(2, 2, new Float(0.0f), 140, 258, 55, 25,
                panel);

        this.ftf_bg1.addFocusListener(this);
        this.ftf_bg2.addFocusListener(this);

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
                    JFormattedTextField tf = (JFormattedTextField) ke
                            .getSource();
                    String s = tf.getText();
                    s = s.replace('.', ',');
                    tf.setText(s);
                }
            }

        });
*/
        addComponent(cb_food_set = new JCheckBox(" "
                + m_ic.getMessage("FOOD_SET")), 120, 290, 200, panel);
        addComponent(UrineField = new JTextField(), 120, 318, 240, panel);
        addComponent(ActField = new JTextField(), 120, 348, 240, panel);
        addComponent(CommentField = new JTextField(), 120, 378, 240, panel);

        /*
         * this.cob_bg_type.setSelectedIndex(props.getBG_unit()-1);
         * cob_bg_type.addItemListener(new ItemListener(){ / Invoked when an
         * item has been selected or deselected by the user. The code written
         * for this method performs the operations that need to occur when an
         * item is selected (or deselected).
         */
        /*
         * public void itemStateChanged(ItemEvent e) { try { long now =
         * System.currentTimeMillis(); //System.out.println("last=" +
         * last_change + ",now=" + now);
         * 
         * if ((now - last_change) < 500) { return; }
         * 
         * last_change = now;
         * 
         * int prev = 0;
         * 
         * if (cob_bg_type.getSelectedIndex()==1) { prev = 1; } else prev = 2;
         * 
         * String s = ftf_bg.getText(); s = s.replace(',', '.');
         * 
         * float v = 0.0f;
         * 
         * if (!s.equals("")) { v = Float.parseFloat(s); }
         * 
         * //float v = Float.parseFloat(s); //BGField.getText());
         * 
         * //System.out.println("Item state vhanged: value_old=" + v +
         * ", value_new=" + m_da.getBGValueDifferent(prev, v));
         * 
         * setBGTextField(); //setBGElementSettings();
         * 
         * if (prev==2) { //ftf_bg.setText("" +
         * (int)m_da.getBGValueDifferent(prev, v)); ftf_bg.setValue(new
         * Integer((int)m_da.getBGValueDifferent(prev, v)));
         * //BGField.setText("" + (int)m_da.getBGValueDifferent(prev, v)); }
         * else ftf_bg.setValue(new Float(m_da.getBGValueDifferent(prev, v)));
         * //ftf_bg.setText("" + m_da.getBGValueDifferent(prev, v)); //
         * BGField.setText("" + m_da.getBGValueDifferent(prev, v));
         * 
         * //fixDecimals(); } catch(Exception ex) {
         * System.out.println("Error with change of BG Value: " + ex); } } });
         */
        // cb_food_set.setDisabledIcon(cb_food_set.getIcon());
        // cb_food_set.setDisabledSelectedIcon(cb_food_set.getSelectedIcon());
        // cb_food_set.setEnabled(false);
        cb_food_set.setMultiClickThreshhold(500);

        // cb_food_set.setEnabled(false);
        cb_food_set.addChangeListener(new ChangeListener()
        {
            /**
             * Invoked when the target of the listener has changed its state.
             * 
             * @param e
             *            a ChangeEvent object
             */
            public void stateChanged(ChangeEvent e)
            {
                if (in_process)
                    return;

                in_process = true;
                // System.out.println("State: " + cb_food_set.isSelected());
                cb_food_set.setSelected(isMealSet());
                in_process = false;
            }
        });

        String button_command[] = { "update_ch",
                m_ic.getMessage("UPDATE_FROM_FOOD"), "edit_food",
                m_ic.getMessage("EDIT_FOOD"), "ok", m_ic.getMessage("OK"),
                "cancel", m_ic.getMessage("CANCEL"),
        // "help", m_ic.getMessage("HELP")
        };

        String button_icon[] = { null, null, "ok.png", "cancel.png" };

        int button_coord[] = { 210, 228, 140, 1, 210, 258, 140, 1, 30, 420,
                110, 1, 145, 420, 110, 1,
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

        help_button = m_da.createHelpButtonByBounds(260, 420, 110, 25, this);

        panel.add(help_button);

        m_da.enableHelp(this);

    }

    public boolean isMealSet()
    {
        if ((this.m_dailyValuesRow.meals == null)
                || (this.m_dailyValuesRow.meals.trim().length() == 0))
            return false;
        else
            return true;
    }

    public JFormattedTextField getTextField(int columns, int decimal_places,
            Object value, int x, int y, int width, int height, Container cont)

    {
        JDecimalTextField tf = new JDecimalTextField(value, decimal_places);
        tf.setBounds(x, y, width, height);
        tf.addKeyListener(this);
        
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

    /*
     * public void setBGTextField() { int digs = 0;
     * 
     * if (m_da.getBGMeasurmentType()==DataAccess.BG_MMOL) { digs = 1; }
     * 
     * bg_displayFormat = NumberFormat.getNumberInstance();
     * bg_displayFormat.setMinimumFractionDigits(digs);
     * bg_displayFormat.setMaximumFractionDigits(digs);
     * 
     * bg_editFormat = NumberFormat.getNumberInstance();
     * bg_editFormat.setMinimumFractionDigits(digs);
     * bg_editFormat.setMaximumFractionDigits(digs);
     * 
     * 
     * this.ftf_bg = new JFormattedTextField( new DefaultFormatterFactory( new
     * NumberFormatter(bg_displayFormat), new NumberFormatter(bg_displayFormat),
     * new NumberFormatter(bg_editFormat)));
     * 
     * 
     * / MaskFormatter mask = null; try { // // Create a MaskFormatter for
     * accepting phone number, the # symbol accept // only a number. We can also
     * set the empty value with a place holder // character. // mask = new
     * MaskFormatter("##,#"); mask.setPlaceholderCharacter('_'); } catch
     * (ParseException e) { e.printStackTrace(); }
     * 
     * 
     * this.ftf_bg = new JFormattedTextField(mask);
     */

    // ftf.setValue(value);
    // if (columns != 0)
    // ftf.setColumns(columns);
    /*
     * this.ftf_bg.setBounds(140, 138, 55, 25);
     * this.ftf_bg.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
     * main_panel.add(this.ftf_bg);
     * 
     * //setBGElementSettings();
     * 
     * //return ftf;
     * 
     * }
     */

    /*
     * protected void setBGElementSettings() {
     * 
     * if (m_da.getBGMeasurmentType()==DataAccess.BG_MGDL) {
     * bg_displayFormat.setMinimumFractionDigits(0);
     * bg_displayFormat.setMaximumFractionDigits(0);
     * bg_editFormat.setMinimumFractionDigits(0);
     * bg_editFormat.setMaximumFractionDigits(0);
     * 
     * //this.ftf_bg.setFormatterFactory(new MaskFormatter("###"));
     * //this.ftf_bg.set
     * 
     * //bg_displayFormat.setRoundingMode(); //new RoundingMode(); } else {
     * bg_displayFormat.setMinimumFractionDigits(1);
     * bg_displayFormat.setMaximumFractionDigits(1);
     * bg_editFormat.setMinimumFractionDigits(1);
     * bg_editFormat.setMaximumFractionDigits(1);
     * 
     * } }
     */

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
        comp.addKeyListener(this);
        parent.add(comp);
    }

    /*
     * private void init_old() { this.setBounds(150, 150, 300, 150);
     * 
     * JPanel a = new JPanel(new GridLayout(0, 1)); a.add(new
     * JLabel(m_ic.getMessage("DATE") + ":", SwingConstants.RIGHT)); a.add(new
     * JLabel(m_ic.getMessage("BG") + ":", SwingConstants.RIGHT)); a.add(new
     * JLabel(m_da.getSettings().getIns1Abbr() + ":", SwingConstants.RIGHT));
     * a.add(new JLabel(m_ic.getMessage("ACT") + ":", SwingConstants.RIGHT));
     * 
     * JPanel b = new JPanel(new GridLayout(0, 1)); DateField = new
     * JTextField(10); if (sDate != null) { DateField.setText(sDate);
     * DateField.setEditable(false); } b.add(DateField);
     * 
     * 
     * b.add(BGField = new JTextField()); components[1] = BGField;
     * BGField.addKeyListener(this);
     * 
     * b.add(Ins1Field = new JTextField()); components[3] = Ins1Field;
     * Ins1Field.addKeyListener(this);
     * 
     * 
     * b.add(ActField = new JTextField()); components[5] = ActField;
     * ActField.addKeyListener(this);
     * 
     * 
     * JPanel c = new JPanel(new GridLayout(0, 1)); c.add(new
     * JLabel(m_ic.getMessage("TIME") + ":", SwingConstants.RIGHT)); c.add(new
     * JLabel(m_ic.getMessage("BU") + ":", SwingConstants.RIGHT)); c.add(new
     * JLabel(m_da.getSettings().getIns2Abbr() + ":", SwingConstants.RIGHT));
     * c.add(new JLabel(m_ic.getMessage("COMMENT") + ":",
     * SwingConstants.RIGHT));
     * 
     * JPanel d = new JPanel(new GridLayout(0, 1)); d.add(TimeField = new
     * JTextField(10)); components[0] = TimeField;
     * TimeField.addKeyListener(this);
     * 
     * 
     * d.add(BUField = new JTextField()); components[2] = BUField;
     * BUField.addKeyListener(this);
     * 
     * 
     * d.add(Ins2Field = new JTextField()); components[4] = Ins2Field;
     * Ins2Field.addKeyListener(this);
     * 
     * 
     * d.add(CommentField = new JTextField()); components[6] = CommentField;
     * CommentField.addKeyListener(this);
     * 
     * 
     * Box e = Box.createHorizontalBox(); e.add(a); e.add(b); e.add(c);
     * e.add(d);
     * 
     * Box g = Box.createHorizontalBox(); AddButton = new
     * JButton(m_ic.getMessage("OK")); components[7] = AddButton;
     * AddButton.addKeyListener(this); AddButton.setActionCommand("ok");
     * AddButton.addActionListener(this);
     * 
     * g.add(Box.createHorizontalGlue());
     * getRootPane().setDefaultButton(AddButton);
     * 
     * g.add(AddButton); JButton CloseButton = new
     * JButton(m_ic.getMessage("CANCEL")); components[8] = CloseButton;
     * CloseButton.addKeyListener(this); CloseButton.setActionCommand("close");
     * CloseButton.addActionListener(this);
     * 
     * 
     * g.add(Box.createHorizontalStrut(10)); g.add(CloseButton);
     * g.add(Box.createHorizontalGlue()); this.getContentPane().add(g,
     * BorderLayout.SOUTH);
     * 
     * getContentPane().add(e, BorderLayout.NORTH); getContentPane().add(g,
     * BorderLayout.SOUTH);
     * 
     * }
     */

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
        else if (action.equals("edit_food"))
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
        }
        else if (action.equals("update_ch"))
        {
            PanelMealSelector pms = new PanelMealSelector(this, null,
                    this.m_dailyValuesRow.getMealsIds());
            this.ftf_ch.setValue(new Float(pms.getCHSumString().replace(',',
                    '.')));
        }
        else
            System.out.println("DailyRowDialog::unknown command: " + action);

    }

    /*
     * String button_command[] = { "update_ch",
     * m_ic.getMessage("UPDATE_FROM_FOOD"), "edit_food",
     * m_ic.getMessage("EDIT_FOOD"), "ok", m_ic.getMessage("OK"), "cancel",
     * m_ic.getMessage("CANCEL"), // "help", m_ic.getMessage("HELP")
     */

    private void cmdOk()
    {
        // to-do
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
            /*
             * dV.setNewRow(new DailyValuesRow(this.dtc.getDateTime(),
             * checkDecimalFields(BGField.getText()),
             * checkDecimalFields(Ins1Field.getText()),
             * checkDecimalFields(Ins2Field.getText()),
             * checkDecimalFields(BUField.getText()), ActField.getText(),
             * UrineField.getText(), CommentField.getText(), null)); // List of
             * ids //mod.fireTableChanged(null); //clearFields();
             */
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

            /*
             * float f = m_da.getJFormatedTextValueFloat(ftf_bg);
             * 
             * if (f>0.0)
             * this.m_dailyValuesRow.setBG(this.cob_bg_type.getSelectedIndex
             * ()+1, f);
             */
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

    /**
     * Invoked when a key has been released. See the class description for
     * {@link KeyEvent} for a definition of a key released event.
     */
    public void keyReleased(KeyEvent e)
    {

        if ((e.getSource().equals(this.ftf_bg1)) || (e.getSource().equals(this.ftf_bg2)))
        {
            focusProcess(e.getSource());
        }
        
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            cmdOk();
        }

    }

    /*
     * private void fixDecimals() { if (m_da.isEmptyOrUnset(BGField.getText()))
     * return;
     * 
     * String s = BGField.getText().trim().replace(",", ".");
     * 
     * if (this.cob_bg_type.getSelectedIndex()==1) { try {
     * 
     * //System.out.println(s);
     * 
     * float f = Float.parseFloat(s); String ss =
     * DataAccess.MmolDecimalFormat.format(f); ss = ss.replace(",", ".");
     * this.BGField.setText(ss); } catch(Exception ex) {
     * System.out.println("fixDecimals: " + ex); } } //MmolDecimalFormat }
     */

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
        return "pages.GGC_BG_Daily_Add";
    }

}