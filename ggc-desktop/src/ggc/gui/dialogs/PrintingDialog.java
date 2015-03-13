package ggc.gui.dialogs;

import com.atech.utils.ATSwingUtils;
import ggc.core.data.MonthlyValues;
import ggc.core.print.PrintExtendedMonthlyReport;
import ggc.core.print.PrintSimpleMonthlyReport;
import ggc.core.util.DataAccess;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
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

import com.atech.graphics.components.DateComponent;
import com.atech.graphics.dialogs.ActionExceptionCatchDialog;
import com.atech.i18n.I18nControlAbstract;
import com.atech.print.engine.PrintProcessor;
import com.atech.print.engine.PrintRequester;
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
 *  Filename:     PrintingDialog  
 *  Description:  Dialog for Printing
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class PrintingDialog extends ActionExceptionCatchDialog implements PrintRequester
{
    private static final long serialVersionUID = 2693207247071685559L;
    private DataAccess dataAccess = DataAccess.getInstance();
    private I18nControlAbstract i18nControl = dataAccess.getI18nControlInstance();

    private boolean actionDone = false;

    private JTextField tfName;
    private JComboBox comboBoxTemplate = null;
    JSpinner spinnerYear = null, spinnerMonth = null;
    JButton helpButton;
    DateComponent dateComponentTo, dateComponentFrom;
    Font fontNormal, fontNormalBold;

    GregorianCalendar gc = null;
    PrintProcessor printProcessor;

    private String[] reportTypes = { i18nControl.getMessage("SIMPLE_MONTHLY_REPORT"),
                                    i18nControl.getMessage("EXTENDED_MONTHLY_REPORT") };

    /**
     * Dialog Options: Year and Month Option
     */
    public static final int PRINT_DIALOG_YEAR_MONTH_OPTION = 1;

    /**
     * Dialog Options: Range with day option
     */
    public static final int PRINT_DIALOG_RANGE_DAY_OPTION = 2;

    private int master_type = 1;

    /**
     * Constructor 
     * 
     * @param frame
     * @param type
     * @param master_type
     */
    public PrintingDialog(JFrame frame, int type, int master_type)
    {
        super(DataAccess.getInstance(), "printing");
        this.setLayout(null);

        ATSwingUtils.initLibrary();

        fontNormal = ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL);
        fontNormalBold = ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL_BOLD);

        gc = new GregorianCalendar();
        setTitle(i18nControl.getMessage("PRINTING"));

        this.master_type = master_type;

        if (master_type == PrintingDialog.PRINT_DIALOG_YEAR_MONTH_OPTION)
        {
            init();
            // else
            // initRange();
        }

        this.comboBoxTemplate.setSelectedIndex(type - 1);

        this.setVisible(true);
    }

    private void init()
    {
        ATSwingUtils.initLibrary();

        setSize(350, 320);
        this.dataAccess.centerJDialog(this);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 350, 350);
        panel.setLayout(null);

        this.getContentPane().add(panel);

        JLabel label = new JLabel(i18nControl.getMessage("PRINTING"));
        label.setFont(ATSwingUtils.getFont(ATSwingUtils.FONT_BIG_BOLD));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBounds(0, 20, 350, 35);
        panel.add(label);

        label = new JLabel(i18nControl.getMessage("TYPE_OF_REPORT") + ":");
        label.setFont(this.fontNormalBold);
        label.setBounds(40, 75, 280, 25);
        panel.add(label);

        comboBoxTemplate = new JComboBox(reportTypes);
        comboBoxTemplate.setFont(this.fontNormal);
        comboBoxTemplate.setBounds(40, 105, 230, 25);
        panel.add(comboBoxTemplate);

        int year = gc.get(Calendar.YEAR);
        int month = gc.get(Calendar.MONTH) + 1;

        label = new JLabel(i18nControl.getMessage("SELECT_YEAR_AND_MONTH") + ":");
        label.setFont(this.fontNormalBold);
        label.setBounds(40, 155, 180, 25);
        panel.add(label);

        spinnerYear = new JSpinner();
        SpinnerNumberModel model = new SpinnerNumberModel(year, 1970, year + 1, 1);
        spinnerYear.setModel(model);
        spinnerYear.setEditor(new JSpinner.NumberEditor(spinnerYear, "#"));
        spinnerYear.setFont(this.fontNormal);
        spinnerYear.setBounds(40, 185, 60, 25);
        panel.add(spinnerYear);

        spinnerMonth = new JSpinner();
        SpinnerNumberModel model_m = new SpinnerNumberModel(month, 1, 12, 1);
        spinnerMonth.setModel(model_m);
        // sl_month.setEditor(new JSpinner.NumberEditor(sl_month, "#"));
        spinnerMonth.setFont(this.fontNormal);
        spinnerMonth.setBounds(120, 185, 40, 25);
        panel.add(spinnerMonth);

        JButton button = new JButton("   " + i18nControl.getMessage("OK"));
        button.setActionCommand("ok");
        button.addActionListener(this);
        button.setIcon(ATSwingUtils.getImageIcon_22x22("ok.png", this, dataAccess));
        button.setBounds(40, 240, 125, 25);
        panel.add(button);

        button = new JButton("   " + i18nControl.getMessage("CANCEL"));
        button.setActionCommand("cancel");
        button.setIcon(ATSwingUtils.getImageIcon_22x22("cancel.png", this, dataAccess));
        button.addActionListener(this);
        button.setBounds(175, 240, 125, 25);
        panel.add(button);

        helpButton = ATSwingUtils.createHelpButtonByBounds(185, 210, 115, 25, this, ATSwingUtils.FONT_NORMAL, dataAccess);
        panel.add(helpButton);

        dataAccess.enableHelp(this);

        printProcessor = new PrintProcessor(i18nControl, this);
    }

    /*
     * protected void initRange() // throws Exception
     * {
     * setSize(350, 420);
     * this.dataAccess.centerJDialog(this);
     * JPanel panel = new JPanel();
     * panel.setBounds(0, 0, 350, 400);
     * panel.setLayout(null);
     * this.getContentPane().add(panel);
     * JLabel label = new JLabel(i18nControl.getMessage("PRINTING"));
     * label.setFont(dataAccess.getFont(ATDataAccessAbstract.FONT_BIG_BOLD));
     * label.setHorizontalAlignment(SwingConstants.CENTER);
     * label.setBounds(0, 20, 350, 35);
     * panel.add(label);
     * label = new JLabel(i18nControl.getMessage("TYPE_OF_REPORT") + ":");
     * label.setFont(this.font_normal_bold);
     * label.setBounds(40, 75, 280, 25);
     * panel.add(label);
     * comboBoxTemplate = new JComboBox(report_types_2);
     * comboBoxTemplate.setFont(this.font_normal);
     * comboBoxTemplate.setBounds(40, 105, 230, 25);
     * panel.add(comboBoxTemplate);
     * label = new JLabel(i18nControl.getMessage("SELECT_STARTING_RANGE") +
     * ":");
     * label.setFont(this.font_normal_bold);
     * label.setBounds(40, 155, 180, 25);
     * panel.add(label);
     * dateComponentFrom = new DateComponent(dataAccess);
     * dateComponentFrom.setBounds(40, 180, 120, 25);
     * panel.add(dateComponentFrom);
     * label = new JLabel(i18nControl.getMessage("SELECT_ENDING_RANGE") + ":");
     * label.setFont(this.font_normal_bold);
     * label.setBounds(40, 225, 180, 25);
     * panel.add(label);
     * dateComponentTo = new DateComponent(dataAccess);
     * dateComponentTo.setBounds(40, 250, 120, 25);
     * panel.add(dateComponentTo);
     * JButton button = new JButton("   " + i18nControl.getMessage("OK"));
     * // button.setFont(dataAccess.getFont(DataAccess.FONT_NORMAL));
     * button.setActionCommand("ok");
     * button.addActionListener(this);
     * button.setIcon(dataAccess.getImageIcon_22x22("ok.png", this));
     * button.setBounds(40, 340, 125, 25);
     * panel.add(button);
     * button = new JButton("   " + i18nControl.getMessage("CANCEL"));
     * // button.setFont(dataAccess.getFont(DataAccess.FONT_NORMAL));
     * button.setActionCommand("cancel");
     * button.setIcon(dataAccess.getImageIcon_22x22("cancel.png", this));
     * button.addActionListener(this);
     * button.setBounds(175, 340, 125, 25);
     * panel.add(button);
     * help_button = dataAccess.createHelpButtonByBounds(185, 310, 115, 25,
     * this);
     * panel.add(help_button);
     * dataAccess.enableHelp(this);
     * }
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public void performAction(ActionEvent e) throws Exception
    {
        String action = e.getActionCommand();
        if (action.equals("cancel"))
        {
            actionDone = false;
            this.dispose();
        }
        else if (action.equals("ok"))
        {

            if (this.master_type == PrintingDialog.PRINT_DIALOG_YEAR_MONTH_OPTION)
            {
                int yr = ((Integer) spinnerYear.getValue()).intValue();
                int mnth = ((Integer) spinnerMonth.getValue()).intValue();

                MonthlyValues mv = dataAccess.getDb().getMonthlyValues(yr, mnth);

                this.dispose();

                if (this.comboBoxTemplate.getSelectedIndex() == 0)
                {
                    PrintSimpleMonthlyReport psm = new PrintSimpleMonthlyReport(mv);
                    // System.out.println("PSM: " +
                    // psm.getRelativeNameWithPath());
                    displayPDF(psm.getRelativeNameWithPath());
                }
                else
                {
                    PrintExtendedMonthlyReport psm = new PrintExtendedMonthlyReport(mv);
                    // System.out.println("PESM: " +
                    // psm.getRelativeNameWithPath());
                    displayPDF(psm.getRelativeNameWithPath());
                }
            }
        }
    }

    public boolean actionSuccessful()
    {
        return actionDone;
    }

    public String[] getActionResults()
    {
        String[] res = new String[3];

        if (actionDone)
        {
            res[0] = "1";
        }
        else
        {
            res[0] = "0";
        }

        res[1] = this.tfName.getText();
        res[2] = this.comboBoxTemplate.getSelectedItem().toString();

        return res;
    }

    public void displayPDF(String name) throws Exception
    {
        printProcessor.displayPDF(name);
    }

    // ****************************************************************
    // ****** HelpCapable Implementation *****
    // ****************************************************************

    /**
     * {@inheritDoc}
     */
    public Component getComponent()
    {
        return this.getRootPane();
    }

    /**
     * {@inheritDoc}
     */
    public JButton getHelpButton()
    {
        return this.helpButton;
    }

    /**
     * {@inheritDoc}
     */
    public String getHelpId()
    {
        return "GGC_PenInj_Print";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getObject()
    {
        return this;
    }

    // ****************************************************************
    // ****** PrintRequester Implementation *****
    // ****************************************************************

    /**
     * {@inheritDoc}
     */
    public String getExternalPdfViewer()
    {
        return DataAccess.getInstance().getSettings().getExternalPdfVieverPath().replace('\\', '/');
    }

    /**
     * {@inheritDoc}
     */
    public String getExternalPdfViewerParameters()
    {
        return DataAccess.getInstance().getSettings().getExternalPdfVieverParameters();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isExternalPdfViewerActivated()
    {
        return DataAccess.getInstance().getSettings().getUseExternalPdfViewer();
    }

    /**
     * {@inheritDoc}
     */
    public boolean disableLookAndFeelSettingForInternalPdfViewer()
    {
        return true;
    }

}
