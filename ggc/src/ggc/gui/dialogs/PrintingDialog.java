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
 * Filename: PrintingDialog
 * 
 * Purpose: This is printing selector, for printing. For selecting type of
 * printing and date (month and year).
 * 
 * Author: andyrozman {andy@atech-software.com}
 */
package ggc.gui.dialogs;

import ggc.core.data.MonthlyValues;
import ggc.core.print.PrintExtendedMonthlyReport;
import ggc.core.print.PrintSimpleMonthlyReport;
import ggc.core.util.DataAccess;
import ggc.core.util.I18nControl;

import java.awt.Component;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import com.atech.graphics.dialogs.ActionExceptionCatchDialog;

// fix this

public class PrintingDialog extends ActionExceptionCatchDialog // extends
                                                               // JDialog
                                                               // implements
                                                               // ActionListener
                                                               // , HelpCapable
{

    /**
     * 
     */
    private static final long serialVersionUID = 2693207247071685559L;
    private I18nControl m_ic = I18nControl.getInstance();
    private DataAccess m_da = DataAccess.getInstance();

    private boolean m_actionDone = false;

    private JTextField tfName;
    private JComboBox cb_template = null;
    // x private String[] schemes_names = null;

    GregorianCalendar gc = null;
    JSpinner sl_year = null, sl_month = null;
    JButton help_button;

    public String[] report_types = { m_ic.getMessage("SIMPLE_MONTHLY_REPORT"),
                                    m_ic.getMessage("EXTENDED_MONTHLY_REPORT") };

    Font font_normal, font_normal_bold;

    public PrintingDialog(JFrame frame, int type) // throws Exception
    {
        super(DataAccess.getInstance(), "printing_dialog");
        // super(frame, "", true);

        Rectangle rec = frame.getBounds();
        int x = rec.x + (rec.width / 2);
        int y = rec.y + (rec.height / 2);

        setBounds(x - 175, y - 150, 350, 320);
        this.setLayout(null);

        font_normal = m_da.getFont(DataAccess.FONT_NORMAL);
        font_normal_bold = m_da.getFont(DataAccess.FONT_NORMAL_BOLD);

        gc = new GregorianCalendar();
        setTitle(m_ic.getMessage("PRINTING"));

        init();

        this.cb_template.setSelectedIndex(type - 1);

        this.setVisible(true);
    }

    private void init() // throws Exception
    {

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 350, 350);
        panel.setLayout(null);

        this.getContentPane().add(panel);

        JLabel label = new JLabel(m_ic.getMessage("PRINTING"));
        label.setFont(m_da.getFont(DataAccess.FONT_BIG_BOLD));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBounds(0, 20, 350, 35);
        panel.add(label);

        label = new JLabel(m_ic.getMessage("TYPE_OF_REPORT") + ":");
        label.setFont(this.font_normal_bold);
        label.setBounds(40, 75, 280, 25);
        panel.add(label);

        cb_template = new JComboBox(report_types);
        cb_template.setFont(this.font_normal);
        cb_template.setBounds(40, 105, 230, 25);
        panel.add(cb_template);

        // int year = m_da.getC

        int year = gc.get(Calendar.YEAR);
        int month = gc.get(Calendar.MONTH) + 1;
        // (new GregorianCalendar()).get(Calendar.YEAR);

        label = new JLabel(m_ic.getMessage("SELECT_YEAR_AND_MONTH") + ":");
        label.setFont(this.font_normal_bold);
        label.setBounds(40, 155, 180, 25);
        panel.add(label);

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

        JButton button = new JButton("   " + m_ic.getMessage("OK"));
        // button.setFont(m_da.getFont(DataAccess.FONT_NORMAL));
        button.setActionCommand("ok");
        button.addActionListener(this);
        button.setIcon(m_da.getImageIcon_22x22("ok.png", this));
        button.setBounds(40, 240, 125, 25);
        panel.add(button);

        button = new JButton("   " + m_ic.getMessage("CANCEL"));
        // button.setFont(m_da.getFont(DataAccess.FONT_NORMAL));
        button.setActionCommand("cancel");
        button.setIcon(m_da.getImageIcon_22x22("cancel.png", this));
        button.addActionListener(this);
        button.setBounds(175, 240, 125, 25);
        panel.add(button);

        help_button = m_da.createHelpButtonByBounds(185, 210, 115, 25, this);
        panel.add(help_button);

        m_da.enableHelp(this);

        /*
         * new JButton(m_ic.getMessage("CANCEL"));
         * button.setFont(m_da.getFont(DataAccess.FONT_NORMAL));
         * button.setActionCommand("cancel"); button.addActionListener(this);
         * button.setBounds(190, 240, 110, 25); panel.add(button);
         */

    }

    /*
     * Invoked when an action occurs.
     */
    /*
     * public void actionPerformed(ActionEvent e) { String action =
     * e.getActionCommand();
     * 
     * if (action.equals("cancel")) { m_actionDone = false; this.dispose(); }
     * else if (action.equals("ok")) { int yr =
     * ((Integer)sl_year.getValue()).intValue(); int mnth =
     * ((Integer)sl_month.getValue()).intValue();
     * 
     * 
     * MonthlyValues mv = m_da.getDb().getMonthlyValues(yr, mnth);
     * 
     * if (this.cb_template.getSelectedIndex()==0) { PrintSimpleMonthlyReport
     * psm = new PrintSimpleMonthlyReport(mv); displayPDF(psm.getName());
     * 
     * } else { PrintExtendedMonthlyReport psm = new
     * PrintExtendedMonthlyReport(mv); displayPDF(psm.getName()); }
     * 
     * 
     * / if (this.tfName.getText().trim().equals("")) {
     * JOptionPane.showMessageDialog(this, m_ic.getMessage("TYPE_NAME_BEFORE"),
     * m_ic.getMessage("ERROR"), JOptionPane.ERROR_MESSAGE); return; }
     * m_actionDone = true;
     */
    /*
     * this.dispose(); } else
     * System.out.println("PrintingDialog: Unknown command: " + action);
     * 
     * }
     */

    /*
     * performAction
     */
    @Override
    public void performAction(ActionEvent e) throws Exception
    {
        String action = e.getActionCommand();
        if (action.equals("cancel"))
        {
            m_actionDone = false;
            this.dispose();
        }
        else if (action.equals("ok"))
        {
            int yr = ((Integer) sl_year.getValue()).intValue();
            int mnth = ((Integer) sl_month.getValue()).intValue();

            MonthlyValues mv = m_da.getDb().getMonthlyValues(yr, mnth);

            this.dispose();
            
            if (this.cb_template.getSelectedIndex() == 0)
            {
                PrintSimpleMonthlyReport psm = new PrintSimpleMonthlyReport(mv);
                displayPDF(psm.getName());

            }
            else
            {
                PrintExtendedMonthlyReport psm = new PrintExtendedMonthlyReport(mv);
                displayPDF(psm.getName());
            }
        }
    }

    public void displayPDF(String name) throws Exception
    {
        File fl = new File(".." + File.separator + "data" + File.separator + "temp" + File.separator);

        // y System.out.println(fl.getAbsolutePath());
        // System.out.println(File.separator);

        String pdf_viewer = m_da.getSettings().getPdfVieverPath().replace('\\', '/');
        String file_path = fl.getAbsolutePath().replace('\\', '/');

        this.setErrorMessages(m_ic.getMessage("PRINTING_SETTINGS_NOT_SET"), m_ic.getMessage("PRINTING_SETTINGS_NOT_SET_SOL"));

        
        if (pdf_viewer.equals(""))
        {
            throw new Exception(m_ic.getMessage("PRINTING_SETTINGS_NOT_SET"));
        }

        // ySystem.out.println(pdf_viewer);
        // y System.out.println(file_path);

        File acr = new File(pdf_viewer);
        
        if (!acr.exists())
        {
            throw new Exception(m_ic.getMessage("PRINTING_SETTINGS_NOT_SET"));
        }

        try
        {
            // String pathToAcrobat =
            // "c:\\Program Files\\Utils\\Acrobat 7.0\\Reader\\AcroRd32.exe ";
            // Runtime.getRuntime().e
            // Runtime.getRuntime().exec(acr.getAbsoluteFile() + " " +
            // fl.getAbsolutePath() + File.separator + name);
            // Runtime.getRuntime().exec(pdf_viewer + " " + file_path + "/" +
            // name);

            // System.out.println("Runtime: " + acr.getAbsoluteFile() + " " +
            // fl.getAbsolutePath() + File.separator + name);

            Runtime.getRuntime().exec(
                acr.getAbsoluteFile() + " \"" + fl.getAbsolutePath() + File.separator + name + "\"");
            System.out.println(pdf_viewer + " " + file_path + File.separator + name);

            // Runtime.getRuntime().exec("\"" + pdf_viewer + "\" " + file_path +
            // "/" + name);

            // Runtime.getRuntime().exec(
            // "c:/Program Files (x86)/Utils/Adobe Reader 8.0/Reader/AcroRd32.exe"
            // );

        }
        catch (RuntimeException ex)
        {
            this.setErrorMessages(m_ic.getMessage("PDF_VIEVER_RUN_ERROR"), null);
            System.out.println("RE running AcrobatReader: " + ex);
            throw ex;
        }
        catch (Exception ex)
        {
            this.setErrorMessages(m_ic.getMessage("PDF_VIEVER_RUN_ERROR"), null);
            System.out.println("Error running AcrobatReader: " + ex);
            throw ex;
            
        }
        /*
         * System.out.println(acr.getAbsoluteFile());
         * 
         * 
         * try { // String pathToAcrobat =
         * "c:\\Program Files\\Utils\\Acrobat 7.0\\Reader\\AcroRd32.exe ";
         * //Runtime.getRuntime().e
         * Runtime.getRuntime().exec(acr.getAbsoluteFile() + " " +
         * fl.getAbsolutePath() + File.separator + name); }
         * catch(RuntimeException ex) {
         * System.out.println("RE running AcrobatReader: " + ex); } catch
         * (Exception ex) { System.out.println("Error running AcrobatReader: " +
         * ex);
         * 
         * }
         */
    }

    public boolean actionSuccesful()
    {
        return m_actionDone;
    }

    public String[] getActionResult()
    {
        String[] res = new String[3];

        if (m_actionDone)
            res[0] = "1";
        else
            res[0] = "0";

        res[1] = this.tfName.getText();
        res[2] = this.cb_template.getSelectedItem().toString();

        return res;
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
        return "pages.GGC_Print_Selector";
    }

    /*
     * getObject
     */
    @Override
    public Object getObject()
    {
        return this;
    }

}