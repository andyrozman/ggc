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
package ggc.nutri.gui.print;

import ggc.core.data.DayValuesData;
import ggc.core.print.PrintAbstract;
import ggc.core.util.DataAccess;
import ggc.nutri.print.PrintFoodMenuBase;
import ggc.nutri.print.PrintFoodMenuExt1;
import ggc.nutri.print.PrintFoodMenuExt2;
import ggc.nutri.print.PrintFoodMenuExt3;
import ggc.nutri.util.DataAccessNutri;

import java.awt.Font;
import java.io.File;

import javax.swing.JFrame;

import com.atech.i18n.I18nControlAbstract;
import com.atech.print.PrintDialogRange;

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

// fix this

public class PrintFoodDialog extends PrintDialogRange 

//ActionExceptionCatchDialog // extends
// JDialog
// implements
// ActionListener
// , HelpCapable
{

    
    
    private static final long serialVersionUID = -7482208504803865975L;

    private I18nControlAbstract ic = DataAccessNutri.getInstance().getParentI18nControlInstance();
    
    /*
    private static final long serialVersionUID = 2693207247071685559L;
    private DataAccessNutri m_da = DataAccessNutri.getInstance();
    private I18nControlAbstract m_ic = m_da.getParentI18nControlInstance();

    private boolean m_actionDone = false;

    private JTextField tfName;
    private JComboBox cb_template = null;
    // x private String[] schemes_names = null;

    DateComponent dc_to, dc_from;
    
    GregorianCalendar gc = null;
    JSpinner sl_year = null, sl_month = null;
    JButton help_button;

    private String[] report_types_1 = { m_ic.getMessage("SIMPLE_MONTHLY_REPORT"),
                                      m_ic.getMessage("EXTENDED_MONTHLY_REPORT") };
*/
    private String[] report_types_2 = { ic.getMessage("FOOD_MENU_BASE"),
                                       ic.getMessage("FOOD_MENU_EXT_I"),
                                       ic.getMessage("FOOD_MENU_EXT_II"),
//                                       m_ic.getMessage("FOOD_MENU_EXT_III")
                                       };

    Font font_normal, font_normal_bold;

    /**
     * Dialog Options: Year and Month Option
     */
    public static final int PRINT_DIALOG_YEAR_MONTH_OPTION = 1;

    /**
     * Dialog Options: Range with day option
     */
    public static final int PRINT_DIALOG_RANGE_DAY_OPTION = 2;

//    private int master_type = 1;

    
    /**
     * Constructor
     * 
     * @param frame
     * @param type
     */
    public PrintFoodDialog(JFrame frame, int type)
    {
        super(frame, type, DataAccessNutri.getInstance(),  DataAccessNutri.getInstance().getParentI18nControlInstance(), true);
    }
    
    
    /**
     * Constructor 
     * 
     * @param frame
     * @param type
     * @param master_type
     */
/*    public PrintFoodDialog(JFrame frame, int type, int master_type) // throws
                                                                   // Exception
    {
        super(DataAccessNutri.getInstance(), "printing_dialog");
        // super(frame, "", true);
        this.setLayout(null);

        font_normal = m_da.getFont(DataAccessNutri.FONT_NORMAL);
        font_normal_bold = m_da.getFont(DataAccessNutri.FONT_NORMAL_BOLD);

        gc = new GregorianCalendar();
        setTitle(m_ic.getMessage("PRINTING"));

//        this.master_type = master_type;

        //if (master_type == PrintingDialog.PRINT_DIALOG_YEAR_MONTH_OPTION)
        //    init();
        //else
            initRange();

        this.cb_template.setSelectedIndex(type - 1);

        this.setVisible(true);
    }
*/
    
    
  /*  private void init() // throws Exception
    {

        setSize(350, 320);
        // setBounds(x - 175, y - 150, 350, 320);

        this.m_da.centerJDialog(this);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 350, 350);
        panel.setLayout(null);

        this.getContentPane().add(panel);

        JLabel label = new JLabel(m_ic.getMessage("PRINTING"));
        label.setFont(m_da.getFont(DataAccessNutri.FONT_BIG_BOLD));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBounds(0, 20, 350, 35);
        panel.add(label);

        label = new JLabel(m_ic.getMessage("TYPE_OF_REPORT") + ":");
        label.setFont(this.font_normal_bold);
        label.setBounds(40, 75, 280, 25);
        panel.add(label);

        cb_template = new JComboBox(report_types_1);
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
        // button.setFont(m_da.getFont(DataAccessNutri.FONT_NORMAL));
        button.setActionCommand("ok");
        button.addActionListener(this);
        button.setIcon(m_da.getImageIcon_22x22("ok.png", this));
        button.setBounds(40, 240, 125, 25);
        panel.add(button);

        button = new JButton("   " + m_ic.getMessage("CANCEL"));
        // button.setFont(m_da.getFont(DataAccessNutri.FONT_NORMAL));
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
         * button.setFont(m_da.getFont(DataAccessNutri.FONT_NORMAL));
         * button.setActionCommand("cancel"); button.addActionListener(this);
         * button.setBounds(190, 240, 110, 25); panel.add(button);
         */

    //}

    /*
    private void initRange() // throws Exception
    {

        setSize(350, 420);
        this.m_da.centerJDialog(this);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 350, 400);
        panel.setLayout(null);

        this.getContentPane().add(panel);

        JLabel label = new JLabel(m_ic.getMessage("PRINTING"));
        label.setFont(m_da.getFont(DataAccessNutri.FONT_BIG_BOLD));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBounds(0, 20, 350, 35);
        panel.add(label);

        label = new JLabel(m_ic.getMessage("TYPE_OF_REPORT") + ":");
        label.setFont(this.font_normal_bold);
        label.setBounds(40, 75, 280, 25);
        panel.add(label);

        cb_template = new JComboBox(report_types_2);
        cb_template.setFont(this.font_normal);
        cb_template.setBounds(40, 105, 230, 25);
        panel.add(cb_template);

        // int year = m_da.getC

//        int year = gc.get(Calendar.YEAR);
//        int month = gc.get(Calendar.MONTH) + 1;
        // (new GregorianCalendar()).get(Calendar.YEAR);

        /*
         * sl_year = new JSpinner(); SpinnerNumberModel model = new
         * SpinnerNumberModel(year, 1970, year + 1, 1); sl_year.setModel(model);
         * sl_year.setEditor(new JSpinner.NumberEditor(sl_year, "#"));
         * sl_year.setFont(this.font_normal); sl_year.setBounds(40, 185, 60,
         * 25); panel.add(sl_year);
         * 
         * sl_month = new JSpinner(); SpinnerNumberModel model_m = new
         * SpinnerNumberModel(month, 1, 12, 1); sl_month.setModel(model_m); //
         * sl_month.setEditor(new JSpinner.NumberEditor(sl_month, "#"));
         * sl_month.setFont(this.font_normal); sl_month.setBounds(120, 185, 40,
         * 25); panel.add(sl_month);
         */
/*
        label = new JLabel(m_ic.getMessage("SELECT_STARTING_RANGE") + ":");
        label.setFont(this.font_normal_bold);
        label.setBounds(40, 155, 180, 25);
        panel.add(label);

        dc_from = new DateComponent(m_ic);
        dc_from.setBounds(40, 180, 120, 25);
        panel.add(dc_from);

        label = new JLabel(m_ic.getMessage("SELECT_ENDING_RANGE") + ":");
        label.setFont(this.font_normal_bold);
        label.setBounds(40, 225, 180, 25);
        panel.add(label);

        dc_to = new DateComponent(m_ic);
        dc_to.setBounds(40, 250, 120, 25);
        panel.add(dc_to);

        JButton button = new JButton("   " + m_ic.getMessage("OK"));
        // button.setFont(m_da.getFont(DataAccessNutri.FONT_NORMAL));
        button.setActionCommand("ok");
        button.addActionListener(this);
        button.setIcon(m_da.getImageIcon_22x22("ok.png", this));
        button.setBounds(40, 340, 125, 25);
        panel.add(button);

        button = new JButton("   " + m_ic.getMessage("CANCEL"));
        // button.setFont(m_da.getFont(DataAccessNutri.FONT_NORMAL));
        button.setActionCommand("cancel");
        button.setIcon(m_da.getImageIcon_22x22("cancel.png", this));
        button.addActionListener(this);
        button.setBounds(175, 340, 125, 25);
        panel.add(button);

        help_button = m_da.createHelpButtonByBounds(185, 310, 115, 25, this);
        panel.add(help_button);

        m_da.enableHelp(this);

        /*
         * new JButton(m_ic.getMessage("CANCEL"));
         * button.setFont(m_da.getFont(DataAccessNutri.FONT_NORMAL));
         * button.setActionCommand("cancel"); button.addActionListener(this);
         * button.setBounds(190, 240, 110, 25); panel.add(button);
         */

  //  }



    /**
     * Display PDF
     * 
     * @param name name must be full path to file name (not just name as it was in previous versions)
     * @throws Exception
     */
/*    public void displayPDF(String name) throws Exception
    {
        //Thread.sleep(2000);
        
        //File f = new File(".");
        //File f2 = f.getParentFile();
        
        //System.out.println("Parent: " + f2);
        
        //File fl = new File(".." + File.separator + "data" + File.separator + "temp" + File.separator);
        File file = new File(name);

        String pdf_viewer = DataAccess.getInstance().getSettings().getPdfVieverPath().replace('\\', '/');
        //String file_path = fl.getAbsolutePath().replace('\\', '/');

        this.setErrorMessages(m_ic.getMessage("PRINTING_SETTINGS_NOT_SET"), m_ic
                .getMessage("PRINTING_SETTINGS_NOT_SET_SOL"));

        if (pdf_viewer.equals(""))
        {
            throw new Exception(m_ic.getMessage("PRINTING_SETTINGS_NOT_SET"));
        }

        File acr = new File(pdf_viewer);

        if (!acr.exists())
        {
            throw new Exception(m_ic.getMessage("PRINTING_SETTINGS_NOT_SET"));
        }

        try
        {
            if (System.getProperty("os.name").toUpperCase().contains("WIN"))
            {
                //System.out.println("Windows found");
                Runtime.getRuntime().exec(
                    acr.getAbsoluteFile() + " \"" + file.getAbsolutePath() + "\"");
//                Runtime.getRuntime().exec(
//                    acr.getAbsoluteFile() + " \"" + fl.getAbsolutePath() + File.separator + name + "\"");
            }
            else
            {
                //System.out.println("Non-Windows found");
                Runtime.getRuntime().exec(
                    acr.getAbsoluteFile() + " \"" + file.getAbsolutePath() + "\"");
//                Runtime.getRuntime().exec(
//                    acr.getAbsoluteFile() + " " + fl.getAbsolutePath() + File.separator + name);
            }
            
            /*
            Runtime.getRuntime().exec(
                acr.getAbsoluteFile() + " \"" + fl.getAbsolutePath() + File.separator + name + "\""); */
            //System.out.println(pdf_viewer + " " + file_path + File.separator + name);
/*            System.out.println(pdf_viewer + " \"" + file.getAbsolutePath() + "\"");
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
    }
*/
    /**
     * Display PDF External (static method)
     * 
     * @param name
     */
    public static void displayPDFExternal(String name)
    {
        I18nControlAbstract ic = DataAccess.getInstance().getI18nControlInstance();

        File fl = new File(".." + File.separator + "data" + File.separator + "temp" + File.separator);

        String pdf_viewer = DataAccess.getInstance().getSettings().getPdfVieverPath().replace('\\', '/');
        String file_path = fl.getAbsolutePath().replace('\\', '/');

        if (pdf_viewer.equals(""))
        {
            System.out.println(ic.getMessage("PRINTING_SETTINGS_NOT_SET"));
            return;
        }

        File acr = new File(pdf_viewer);

        if (!acr.exists())
        {
            System.out.println(ic.getMessage("PRINTING_SETTINGS_NOT_SET"));
            return;
        }

        try
        {
            Runtime.getRuntime().exec(
                acr.getAbsoluteFile() + " \"" + fl.getAbsolutePath() + File.separator + name + "\"");
            System.out.println(pdf_viewer + " " + file_path + File.separator + name);
        }
        catch (RuntimeException ex)
        {
            // this.setErrorMessages(m_ic.getMessage("PDF_VIEVER_RUN_ERROR"),
            // null);
            System.out.println("RE running AcrobatReader: " + ex);
            // throw ex;
        }
        catch (Exception ex)
        {
            // this.setErrorMessages(m_ic.getMessage("PDF_VIEVER_RUN_ERROR"),
            // null);
            System.out.println("Error running AcrobatReader: " + ex);
            // throw ex;

        }
    }

    

    // ****************************************************************
    // ****** HelpCapable Implementation *****
    // ****************************************************************


    

    /**
     * getHelpId - get id for Help
     */
    public String getHelpId()
    {
        return "GGC_Food_Print";
    }


    @Override
    public String[] getReportTypes()
    {
        if (this.report_types_2==null)
        {
            report_types_2 = new String[3]; 
            report_types_2[0] = m_ic.getMessage("FOOD_MENU_BASE");
            report_types_2[1] = m_ic.getMessage("FOOD_MENU_EXT_I");
            report_types_2[2] = m_ic.getMessage("FOOD_MENU_EXT_II");
            
            /*
            { ic.getMessage("FOOD_MENU_BASE"),
                                                ic.getMessage("FOOD_MENU_EXT_I"),
                                                ic.getMessage("FOOD_MENU_EXT_II"),
//                                                m_ic.getMessage("FOOD_MENU_EXT_III")
                                                };
            */
            
            
            
        }
        
        
        
        System.out.println("rep_types: " + this.report_types_2 );
        
        return report_types_2;
    }

    @Override
    public void startPrintingAction() throws Exception
    {
        System.out.println(this.dc_from.getDate() + " " + this.dc_to.getDate());
        
        DataAccessNutri da = (DataAccessNutri)m_da;
        
        
        System.out.println("da: " + da);
        System.out.println("da.getDb: " + da.getDb());
        
        DayValuesData dvd = da.getNutriDb().getDayValuesData(this.dc_from.getDate(), this.dc_to.getDate()); //.getMonthlyValues(yr, mnth);
        
        
        PrintAbstract pa = null;
        
        if (this.cb_template.getSelectedIndex() == 0)
        {
            pa = new PrintFoodMenuBase(dvd);
        }
        else if (this.cb_template.getSelectedIndex() == 1)
        {
            pa = new PrintFoodMenuExt1(dvd);
        }
        else if (this.cb_template.getSelectedIndex() == 2)
        {
            pa = new PrintFoodMenuExt2(dvd);
        }
        else if (this.cb_template.getSelectedIndex() == 3)
        {
            pa = new PrintFoodMenuExt3(dvd);
        }
        
        
        displayPDF(pa.getNameWithPath());
        
    }

    
    /**
     * We have Secondary Type choice 
     * 
     * @return
     */
    public boolean weHaveSecondaryType()
    {
        return false;
    }
    
    
    
    /**
     * Get Pdf Viewer (path to software)
     * 
     * @return
     */
    @Override
    public String getPdfViewer()
    {
        return DataAccess.getInstance().getSettings().getPdfVieverPath().replace('\\', '/');
    }
    
    @Override
    public String getPdfViewerParameters()
    {
        // FIXME
        return "";
    }    
    
    
}
