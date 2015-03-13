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
 *  Filename: <filename>
 *
 *  Purpose:  <enter purpose here>
 *
 *  Author:   andyrozman {andy@atech-software.com}
 *
 */
package ggc.gui.dialogs;

import ggc.core.data.DailyValues;
import ggc.core.data.DailyValuesRow;
import ggc.core.util.DataAccess;
import ggc.core.util.GGCProperties;

import java.awt.Container;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.atech.graphics.components.DateTimeComponent;
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
 *  Filename:     zzz  
 *  Description:  zzz
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

// fix this

public class DoctorDialog extends JDialog implements ActionListener
{

    private static final long serialVersionUID = -8276157027834968535L;
    private DataAccess m_da = DataAccess.getInstance();
    private I18nControlAbstract m_ic = m_da.getI18nControlInstance();
    private GGCProperties props = m_da.getSettings();

    private boolean m_actionDone = false;

    JTextField DateField, TimeField, BGField, Ins1Field, Ins2Field, BUField, ActField, CommentField, UrineField;

    JComboBox cob_bg_type;

    JLabel label_title = new JLabel();
    JLabel label_food;
    JCheckBox cb_food_set;
    DateTimeComponent dtc;
    JButton AddButton;
    String sDate = null;
    DailyValues dV = null;
    DailyValuesRow m_dailyValuesRow = null;
    JComponent components[] = new JComponent[9];
    Font f_normal;
    Font f_bold;
    boolean in_process;
    boolean debug = false;

    // private boolean m_add_action = true;
    private Container m_parent = null;

    public DoctorDialog(DailyValues ndV, String nDate, JDialog dialog)
    {
        super(dialog, "", true);
        m_parent = dialog;
        // initParameters(ndV,nDate);
        init();
    }

    @SuppressWarnings("unused")
    private void load()
    {
        this.dtc.setDateTime(this.m_dailyValuesRow.getDateTime());

        // System.out.println(props.getBG_unit());

        // which format
        this.cob_bg_type.setSelectedIndex(props.getBG_unit() - 1);
        this.BGField.setText("" + this.m_dailyValuesRow.getBGAsString());

        if (debug)
        {
            System.out.println("Db value (cuurent): " + this.m_dailyValuesRow.getBG());
            System.out.println("Db value (mg/dL): " + this.m_dailyValuesRow.getBG(1));
            System.out.println("Display value (" + props.getBG_unitString() + "): "
                    + this.m_dailyValuesRow.getBG(props.getBG_unit()));
        }

        Ins1Field.setText("" + this.m_dailyValuesRow.getIns1AsString());
        Ins2Field.setText("" + this.m_dailyValuesRow.getIns2AsString());
        BUField.setText("" + this.m_dailyValuesRow.getCHAsString());

        ActField.setText(this.m_dailyValuesRow.getActivity());
        UrineField.setText(this.m_dailyValuesRow.getUrine());

        this.cb_food_set.setEnabled(false);
        this.cb_food_set.setSelected(this.m_dailyValuesRow.areMealsSet());
        this.cb_food_set.setEnabled(true);

        CommentField.setText(this.m_dailyValuesRow.getComment());

        /*
         * addComponent(cb_food_set = new JCheckBox(" " +
         * m_ic.getMessage("FOOD_SET")), 110, 240, 200, panel);
         * addComponent(UrineField = new JTextField(), 110, 268, 220, panel);
         * addComponent(ActField = new JTextField(), 110, 298, 220, panel);
         */

        /*
         * this.dtc = new DateTimeComponent(this.m_ic,
         * DateTimeComponent.ALIGN_VERTICAL, 5);
         * dtc.setBounds(160, 55, 100, 35);
         * panel.add(dtc);
         * addComponent(BGField = new JTextField(), 160, 118, 35, panel);
         * addComponent(Ins2Field = new JTextField(), 160, 148, 35, panel);
         * addComponent(Ins2Field = new JTextField(), 160, 178, 35, panel);
         * addComponent(BUField = new JTextField(), 160, 208, 35, panel);
         * addComponent(cb_food_set = new JCheckBox(" " +
         * m_ic.getMessage("FOOD_SET")), 110, 240, 200, panel);
         * addComponent(UrineField = new JTextField(), 110, 268, 220, panel);
         * addComponent(ActField = new JTextField(), 110, 298, 220, panel);
         * addComponent(CommentField = new JTextField(), 110, 328, 220, panel);
         */
    }

    /*
     * private void save()
     * {
     * }
     */

    private void init()
    {
        ATSwingUtils.initLibrary();

        this.f_normal = ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL);
        this.f_bold = ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL_BOLD); // FONT_NORMAL

        int x = 0;
        int y = 0;
        int width = 400;
        int height = 480;

        Rectangle bnd = m_parent.getBounds();

        x = bnd.width / 2 + bnd.x - width / 2;
        y = bnd.height / 2 + bnd.y - height / 2;

        this.setBounds(x, y, width, height);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, width, height);
        panel.setLayout(null);

        this.getContentPane().add(panel);

        label_title.setFont(ATSwingUtils.getFont(ATSwingUtils.FONT_BIG_BOLD));
        label_title.setHorizontalAlignment(SwingConstants.CENTER);
        label_title.setBounds(0, 15, 400, 35);
        panel.add(label_title);

        addLabel(m_ic.getMessage("DATE") + ":", 78, panel);
        addLabel(m_ic.getMessage("TIME") + ":", 108, panel);
        addLabel(m_ic.getMessage("BLOOD_GLUCOSE") + ":", 138, panel);
        addLabel(props.getIns1Name() + " (" + props.getIns1Abbr() + ") :", 168, panel);
        addLabel(props.getIns2Name() + " (" + props.getIns2Abbr() + "):", 198, panel);
        addLabel(m_ic.getMessage("CH_LONG") + ":", 228, panel);
        addLabel(m_ic.getMessage("FOOD") + ":", 258, panel);
        addLabel(m_ic.getMessage("URINE") + ":", 288, panel);
        addLabel(m_ic.getMessage("ACTIVITY") + ":", 318, panel);
        addLabel(m_ic.getMessage("COMMENT") + ":", 348, panel);

        this.dtc = new DateTimeComponent(this.m_da, DateTimeComponent.ALIGN_VERTICAL, 5);
        dtc.setBounds(140, 75, 100, 35);
        panel.add(dtc);

        addComponent(cob_bg_type = new JComboBox(this.m_da.bg_units), 220, 138, 80, panel);
        addComponent(BGField = new JTextField(), 140, 138, 55, panel);
        addComponent(Ins1Field = new JTextField(), 140, 168, 55, panel);
        addComponent(Ins2Field = new JTextField(), 140, 198, 55, panel);
        addComponent(BUField = new JTextField(), 140, 228, 55, panel);
        addComponent(cb_food_set = new JCheckBox(" " + m_ic.getMessage("FOOD_SET")), 110, 260, 200, panel);
        addComponent(UrineField = new JTextField(), 110, 288, 220, panel);
        addComponent(ActField = new JTextField(), 110, 318, 220, panel);
        addComponent(CommentField = new JTextField(), 110, 348, 220, panel);

        this.cob_bg_type.setSelectedIndex(props.getBG_unit() - 1);

        cb_food_set.setMultiClickThreshhold(500);

        // System.out.println(cb_food_set.get

        // cb_food_set.setEnabled(false);
        cb_food_set.addChangeListener(new ChangeListener()
        {
            /**
             * Invoked when the target of the listener has changed its state.
             *
             * @param e  a ChangeEvent object
             */
            public void stateChanged(ChangeEvent e)
            {
                if (in_process)
                    return;

                in_process = true;
                System.out.println("State: " + cb_food_set.isSelected());
                cb_food_set.setSelected(!cb_food_set.isSelected());
                in_process = false;
            }
        });

        String button_command[] = { "update_ch", m_ic.getMessage("UPDATE_FROM_FOOD"), "edit", m_ic.getMessage("EDIT"),
                                   "ok", m_ic.getMessage("OK"), "cancel", m_ic.getMessage("CANCEL"), "help",
                                   m_ic.getMessage("HELP") };

        int button_coord[] = { 210, 228, 120, 0, 230, 258, 100, 0, 50, 390, 80, 1, 140, 390, 80, 1, 250, 390, 80, 0 };

        JButton button;
        // int j=0;
        for (int i = 0, j = 0; i < button_coord.length; i += 4, j += 2)
        {
            button = new JButton(button_command[j + 1]);
            button.setActionCommand(button_command[j]);
            button.addActionListener(this);

            if (button_coord[i + 3] == 0)
            {
                button.setEnabled(false);
            }

            addComponent(button, button_coord[i], button_coord[i + 1], button_coord[i + 2], panel);

        }

    }

    private void addLabel(String text, int posY, JPanel parent)
    {
        JLabel label = new JLabel(text);
        label.setBounds(30, posY, 100, 25);
        label.setFont(f_bold);
        parent.add(label);
        // a.add(new JLabel(m_ic.getMessage("DATE") + ":",
        // SwingConstants.RIGHT));

    }

    private void addComponent(JComponent comp, int posX, int posY, int width, JPanel parent)
    {
        // JLabel label = new JLabel(text);
        comp.setBounds(posX, posY, width, 23);
        comp.setFont(f_normal);
        parent.add(comp);
    }

    public void actionPerformed(ActionEvent e)
    {
        String action = e.getActionCommand();

        if (action.equals("cancel"))
        {
            this.dispose();
        }
        else if (action.equals("ok"))
        {}
    }

    public boolean actionSuccessful()
    {
        return m_actionDone;
    }

}
