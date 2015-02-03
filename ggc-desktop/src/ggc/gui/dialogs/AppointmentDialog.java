package ggc.gui.dialogs;

import ggc.core.util.DataAccess;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATDataAccessAbstract;

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

public class AppointmentDialog extends JDialog implements ActionListener
{

    private static final long serialVersionUID = -1721590187912615702L;
    private DataAccess m_da = DataAccess.getInstance();
    private I18nControlAbstract m_ic = m_da.getI18nControlInstance();

    // private boolean m_actionDone = false;

    @SuppressWarnings("unused")
    private JTextField tfName;
    private JComboBox cb_template = null;
    // x private String[] schemes_names = null;

    GregorianCalendar gc = null;
    JSpinner sl_year = null, sl_month = null;

    Font font_normal, font_normal_bold;

    /**
     * Constructor
     * 
     * @param frame
     * @param type
     */
    public AppointmentDialog(JFrame frame, int type)
    {
        super(frame, "", true);

        Rectangle rec = frame.getBounds();
        int x = rec.x + rec.width / 2;
        int y = rec.y + rec.height / 2;

        setBounds(x - 175, y - 150, 350, 320);
        this.setLayout(null);

        font_normal = m_da.getFont(ATDataAccessAbstract.FONT_NORMAL);
        font_normal_bold = m_da.getFont(ATDataAccessAbstract.FONT_NORMAL_BOLD);

        gc = new GregorianCalendar();

        init();

        this.cb_template.setSelectedIndex(type - 1);

        this.setVisible(true);
    }

    private void init()
    {

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 350, 350);
        panel.setLayout(null);

        this.getContentPane().add(panel);

        JLabel label = new JLabel(m_ic.getMessage("PRINTING"));
        label.setFont(m_da.getFont(ATDataAccessAbstract.FONT_BIG_BOLD));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBounds(0, 20, 350, 35);
        panel.add(label);

        label = new JLabel(m_ic.getMessage("TYPE_OF_REPORT") + ":");
        label.setFont(this.font_normal_bold);
        label.setBounds(40, 75, 280, 25);
        panel.add(label);
        /*
         * cb_template = new JComboBox(report_types);
         * cb_template.setFont(this.font_normal);
         * cb_template.setBounds(40, 105, 230, 25);
         * panel.add(cb_template);
         */
        // int year = dataAccess.getC

        int year = gc.get(Calendar.YEAR);
        int month = gc.get(Calendar.MONTH) + 1;
        new GregorianCalendar().get(Calendar.YEAR);
        /*
         * JSlider sl = new JSlider();
         * BoundedRangeModel model = new BoundedRangeModel();
         * model.setMinimum(1970);
         * model.setMaximum(year);
         * model.setValue(year);
         * sl.setModel(model);
         * sl.setBounds(120, 210, 30, 25);
         */

        label = new JLabel(m_ic.getMessage("SELECT_YEAR_AND_MONTH") + ":");
        label.setFont(this.font_normal_bold);
        label.setBounds(40, 155, 180, 25);
        panel.add(label);
        /*
         * tfName = new JTextField();
         * tfName.setBounds(120, 205, 160, 25);
         * tfName.setFont(dataAccess.getFont(DataAccess.FONT_NORMAL));
         * //panel.add(sl);
         */

        sl_year = new JSpinner();
        SpinnerNumberModel model = new SpinnerNumberModel(year, 1970, year + 1, 1);
        sl_year.setModel(model);
        sl_year.setEditor(new JSpinner.NumberEditor(sl_year, "#"));
        sl_year.setFont(this.font_normal);
        sl_year.setBounds(40, 185, 60, 25);
        panel.add(sl_year);

        sl_month = new JSpinner();
        SpinnerNumberModel model_m = new SpinnerNumberModel(month, 1, 12, 1);
        sl_month.setModel(model_m);
        // sl_month.setEditor(new JSpinner.NumberEditor(sl_month, "#"));
        sl_month.setFont(this.font_normal);
        sl_month.setBounds(120, 185, 40, 25);
        panel.add(sl_month);

        JButton button = new JButton(m_ic.getMessage("OK"));
        button.setFont(m_da.getFont(ATDataAccessAbstract.FONT_NORMAL));
        button.setActionCommand("ok");
        button.addActionListener(this);
        button.setBounds(100, 240, 80, 25);
        panel.add(button);

        button = new JButton(m_ic.getMessage("CANCEL"));
        button.setFont(m_da.getFont(ATDataAccessAbstract.FONT_NORMAL));
        button.setActionCommand("cancel");
        button.addActionListener(this);
        button.setBounds(190, 240, 80, 25);
        panel.add(button);

    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
        String action = e.getActionCommand();

        if (action.equals("cancel"))
        {
            // m_actionDone = false;
            this.dispose();
        }
        else if (action.equals("ok"))
        {}
        else
        {
            System.out.println("AppointmentDialog: Unknown command: " + action);
        }

    }

    /*
     * public boolean actionSuccesful()
     * {
     * return m_actionDone;
     * }
     * public String[] getActionResult()
     * {
     * String[] res = new String[3];
     * if (m_actionDone)
     * res[0] = "1";
     * else
     * res[0] = "0";
     * res[1] = this.tfName.getText();
     * res[2] = this.cb_template.getSelectedItem().toString();
     * return res;
     * }
     */
}
