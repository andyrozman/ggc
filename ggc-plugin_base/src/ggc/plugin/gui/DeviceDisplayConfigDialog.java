package ggc.plugin.gui;

import ggc.plugin.data.DeviceDataHandler;
import ggc.plugin.data.DeviceValuesConfigTable;
import ggc.plugin.data.DeviceValuesConfigTableModel;
import ggc.plugin.data.DeviceValuesEntryInterface;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.output.AbstractOutputWriter;
import ggc.plugin.output.OutputUtil;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.output.OutputWriterData;
import ggc.plugin.util.DataAccessPlugInBase;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.TableColumn;

import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Meter Tool (support for Meter devices)
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
 *  Filename:      DeviceDisplayDataDialog
 *  Description:   This is dialog for displaying data as it's been downloaded. 
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class DeviceDisplayConfigDialog extends JDialog implements ActionListener, OutputWriter, HelpCapable
{

    private static final long serialVersionUID = -3480394909832672125L;

    DeviceReaderRunner mrr;

    private DataAccessPlugInBase m_da; // = DataAccessMeter.getInstance();
    I18nControlAbstract m_ic; // = m_da.getI18nControlInstance();

    /**
     * 
     */
    public JProgressBar progress = null;
    private JProgressBar progress_old = null;

    private DeviceValuesConfigTableModel model = null;
    private DeviceValuesConfigTable table = null;

    private JButton bt_close, bt_import, bt_break;
    private JTabbedPane tabPane;

    JLabel lbl_status, lbl_comment;
    JTextArea ta_info = null;
    JTextArea logText = null;
    JButton help_button;
    DeviceDataHandler m_ddh;
    
    
    /*
    public String statuses[] =  
    { 
        m_ic.getMessage("STATUS_NONE"),
        m_ic.getMessage("STATUS_READY"),
        m_ic.getMessage("STATUS_DOWNLOADING"),
        m_ic.getMessage("STATUS_STOPPED_DEVICE"),
        m_ic.getMessage("STATUS_STOPPED_USER"),
        m_ic.getMessage("STATUS_DOWNLOAD_FINISHED"),
        m_ic.getMessage("STATUS_READER_ERROR"),
    };*/ 
    
    
    
    
    
    
    /*
    public DeviceDisplayDataDialog(DataAccessPlugInBase da)
    {
        super();

        this.m_da = da;
        this.m_ic = da.getI18nControlInstance();
        //this.loadConfiguration();

        this.mrr = new DeviceReaderRunner(m_da, this.configured_meter, this);

        dialogPreInit();
    }*/


    /**
     * Constructor
     *  
     * @param da 
     * @param ddh 
     */
    public DeviceDisplayConfigDialog(DataAccessPlugInBase da, DeviceDataHandler ddh)
    {
        super();
        
        this.m_da = da;
        this.m_ic = da.getI18nControlInstance();
        
        this.m_ddh = ddh;

//        this.configured_meter = mce;

        this.mrr = new DeviceReaderRunner(m_da, this.m_ddh.getConfiguredDevice(), this);

        dialogPreInit();
    }
    

    /**
     * Constructor (for testing GUI)
     *  
     * @param da 
     * @param ddh 
     * @param is_debug 
     */
    public DeviceDisplayConfigDialog(DataAccessPlugInBase da, DeviceDataHandler ddh, boolean is_debug)
    {
        super();
        
        this.m_da = da;
        this.m_ic = da.getI18nControlInstance();
        
        this.m_ddh = ddh;
        //this.mrr = new DeviceReaderRunner(m_da, this.m_ddh.getConfiguredDevice(), this);

        dialogPreInit();
    }
    
    
    
    /*
    public DeviceDisplayDataDialog(DataAccessPlugInBase da, DeviceConfigEntry mce)
    {
        super();
        
        this.m_da = da;
        this.m_ic = da.getI18nControlInstance();

        this.configured_meter = mce;

        this.mrr = new DeviceReaderRunner(m_da, this.configured_meter, this);

        dialogPreInit();
    }*/
    
/*
    public DeviceDisplayDataDialog(DataAccessPlugInBase da, DeviceConfigEntry mce, Hashtable<String,?> meter_data, DevicePlugInServer server)
    {
        super();

        this.m_da = da;
        this.m_ic = da.getI18nControlInstance();
        
        this.configured_meter = mce;
        this.meter_data = meter_data;

        this.mrr = new DeviceReaderRunner(m_da, this.configured_meter, this);

        this.server = server;
        dialogPreInit();
    }
  */  
    

    private void dialogPreInit()
    {
        if (m_ddh!=null)
            setTitle(String.format(m_ic.getMessage("READ_DEVICE_CONFIG_TITLE"), 
                this.m_ddh.getConfiguredDevice().device_device, 
                this.m_ddh.getConfiguredDevice().communication_port));
            

        m_da.addComponent(this);
        
        init();

        if (this.mrr!=null)
            this.mrr.start();

        this.setVisible(true);

    }
    
    
    /**
     * If we have special status progress defined, by device, we need to set progress, by ourselves, this is 
     * done with this method.
     * @param value
     */
    public void setSpecialProgress(int value)
    {
        this.progress.setValue(value);
    }
    
    

    private void addLogText(String s)
    {
        logText.append(s + "\n");
    }


    protected void init()
    {
        model = new DeviceValuesConfigTableModel(m_da, m_da.getSourceDevice());
        model.clearData();
        
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setSize(600, 450);

        JLabel label;
 
        int wide_add = 0;

        if (m_da.isDataDownloadSceenWide())
            wide_add = 100; 
        
        Font normal = m_da.getFont(DataAccessPlugInBase.FONT_NORMAL);
        Font normal_b = m_da.getFont(DataAccessPlugInBase.FONT_NORMAL_BOLD);
        
        setBounds(0, 0, 480+wide_add, 460);

        m_da.centerJDialog(this);
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().add(panel, BorderLayout.CENTER);

        // TabControl with two tabs: log and data

        logText = new JTextArea(m_ic.getMessage("LOG__") + ":\n", 8, 35);
        logText.setAutoscrolls(true);
        JScrollPane sp = new JScrollPane(logText, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        addLogText( m_ic.getMessage("LOG_IS_CURRENTLY_NOT_IMPLEMENTED"));

        this.table = new DeviceValuesConfigTable(m_da, model);

        tabPane = new JTabbedPane();
        tabPane.add(m_ic.getMessage("DATA"), this.createTablePanel(this.table));
        tabPane.add(m_ic.getMessage("LOG"), sp);
        tabPane.setBounds(30, 15, 410+wide_add, 250); // 410
        panel.add(tabPane);

        // Info
        /*label = new JLabel(String.format(m_ic.getMessage("DEVICE_INFO"), m_ic.getMessage("DEVICE_NAME_BIG")) + ":");
        label.setBounds(30, 310, 310, 25);
        label.setFont(normal_b);
        panel.add(label);
*/
        
        /*
        // reading old data
        label = new JLabel(m_ic.getMessage("READING_OLD_DATA") + ":");
        label.setBounds(30, 425, 250, 25);  // 420
        label.setFont(normal_b);
        panel.add(label);
        
        this.progress_old = new JProgressBar();
        this.progress_old.setBounds(30, 450, 410+wide_add, 20);  // 450
        this.progress_old.setStringPainted(true);
        // this.progress.setIndeterminate(true);
        panel.add(this.progress_old);
        */
        
        
        // device status
        label = new JLabel(m_ic.getMessage("ACTION") + ":");
        label.setBounds(30, 290, 100, 25);  // 420
        label.setFont(normal_b);
        panel.add(label);

        lbl_status = new JLabel(m_ic.getMessage("READY"));
        lbl_status.setBounds(110, 290, 330, 25);  // 420
        //lbl_status.setBorder(new LineBorder(Color.red));
        lbl_status.setFont(normal);
        panel.add(lbl_status);

        this.progress = new JProgressBar();
        this.progress.setBounds(30, 330, 410+wide_add, 20);  // 450
        this.progress.setStringPainted(true);
        // this.progress.setIndeterminate(true);
        panel.add(this.progress);

        bt_break = new JButton(m_ic.getMessage("BREAK_COMMUNICATION"));
        bt_break.setBounds(150+wide_add, 380, 170, 25);
        // bt_break.setEnabled(this.m_mim.isStatusOK());
        bt_break.setActionCommand("break_communication");
        bt_break.addActionListener(this);
        panel.add(bt_break);

        help_button = m_da.createHelpButtonByBounds(30, 380, 110, 25, this);
        panel.add(help_button);

        bt_close = new JButton(m_ic.getMessage("CLOSE"));
        bt_close.setBounds(330+wide_add, 380, 110, 25);
        bt_close.setEnabled(false);
        bt_close.setActionCommand("close");
        bt_close.addActionListener(this);
        panel.add(bt_close);
/*
        bt_import = new JButton(m_ic.getMessage("EXPORT_DATA"));
        bt_import.setBounds(270+wide_add, 300, 170, 25);  // 270
        bt_import.setActionCommand("export_data");
        bt_import.addActionListener(this);
        bt_import.setEnabled(false);

        // button.setEnabled(meterDevice.isStatusOK());

        panel.add(bt_import);
*/
        m_da.enableHelp(this);
        
    }

    
    
    private JPanel createTablePanel(DeviceValuesConfigTable table_in)
    {

        JScrollPane scroller = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // final AddRowAction addRowAction = new AddRowAction(table);
        // final DeleteRowAction deleteRowAction = new DeleteRowAction(table);

        JToolBar toolBar = new JToolBar();
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
        toolBar.setFloatable(false);

        
        //int[] cw = { 110, 80, 70, 80, 30 };

        TableColumn column = null;
        for (int i = 0; i < 2; i++)
        {
            column = table_in.getColumnModel().getColumn(i);
            column.setPreferredWidth(this.model.getColumnWidth(i, table_in.getWidth()));
        }

        JPanel container = new JPanel(new BorderLayout());
        container.add(toolBar, "North");
        container.add(scroller, "Center");

        return container;

    }

    
    @SuppressWarnings("unused")
    private JButton createButton(String command_text, String tooltip, String image_d)
    {
        JButton b = new JButton();
        b.setIcon(m_da.getImageIcon(image_d, 15, 15, this));
        b.addActionListener(this);
        b.setActionCommand(command_text);
        b.setToolTipText(tooltip);
        return b;
    }


    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
        String action = e.getActionCommand();

        if (action.equals("break_communication"))
        {
            this.setStatus(AbstractOutputWriter.STATUS_STOPPED_USER);
            this.setReadingStop();
        }
        else if (action.equals("close"))
        {
            m_da.removeComponent(this);
            this.dispose();
        }
        else if (action.equals("export_data"))
        {
            //Hashtable<String,ArrayList<?>> ht = this.model.getCheckedEntries();
            
            DeviceExportDialog med = new DeviceExportDialog(m_da, this, this.m_ddh);
            
            if (med.wasAction())
            {
                this.bt_import.setEnabled(false);
            }
            
        }
        
        
        
        else
            System.out.println("MeterDisplayDataDialog::Unknown command: " + action);

    }

    /**
     * endOutput
     */
    public void endOutput()
    {
        //System.out.println("endOutput()");
    }

    DeviceIdentification device_ident;

    /**
     * getDeviceIdentification
     */
    public DeviceIdentification getDeviceIdentification()
    {
        return device_ident;
    }
    
    String sub_status = null;
    
    
    /**
     * Set Sub Status
     * 
     * @see ggc.plugin.output.OutputWriter#setSubStatus(java.lang.String)
     */
    public void setSubStatus(String sub_status)
    {
        this.sub_status = sub_status;
        refreshStatus();
    }
    
    
    /**
     * Get Sub Status
     * 
     * @see ggc.plugin.output.OutputWriter#getSubStatus()
     */
    public String getSubStatus()
    {
        return this.sub_status;
    }
    
    

    OutputUtil output_util = OutputUtil.getInstance(this);

    /**
     * getOutputUtil
     */
    public OutputUtil getOutputUtil()
    {
        return this.output_util;
    }

    /**
     * interruptCommunication
     */
    public void interruptCommunication()
    {
        System.out.println("interComm()");

    }

    /**
     * setBGOutputType
     */
    public void setBGOutputType(int bg_type)
    {
        //System.out.println("setBGOutput()");
        this.output_util.setBGMeasurmentType(bg_type);
    }

    /**
     * setDeviceIdentification
     */
    public void setDeviceIdentification(DeviceIdentification di)
    {
        this.device_ident = di;
    }

    int count = 0;


    /**
     * writeDeviceIdentification
     */
    public void writeDeviceIdentification()
    {
        this.ta_info.setText(this.device_ident.getShortInformation());
    }

    /**
     * writeHeader
     */
    public void writeHeader()
    {
    }

    /**
     * writeRawData
     * 
     * @param input 
     * @param is_bg_data 
     */
    public void writeRawData(String input, boolean is_bg_data)
    {
    }

    boolean device_should_be_stopped = false;

    /**
     * User can stop readings from his side (if supported)
     */
    public void setReadingStop()
    {
        this.device_should_be_stopped = true;
    }

    /**
     * This should be queried by device implementation, to see if it must stop
     * reading
     */
    public boolean isReadingStopped()
    {
        return this.device_should_be_stopped;
    }

    int reading_status = AbstractOutputWriter.STATUS_READY;

    
    
    
    /**
     * This is status of device and also of GUI that is reading device (if we
     * have one) This is to set that status to see where we are. Allowed
     * statuses are: 1-Ready, 2-Downloading, 3-Stopped by device, 4-Stoped by
     * user,5-Download finished,6-Reader error
     */
    public void setStatus(int status)
    {
        if ((this.reading_status == AbstractOutputWriter.STATUS_STOPPED_DEVICE) || 
            (this.reading_status == AbstractOutputWriter.STATUS_STOPPED_USER) || 
            (this.reading_status == AbstractOutputWriter.STATUS_READER_ERROR))
            return;

        this.reading_status = status;
        setGUIStatus(status);
    }

    /**
     * Get Status
     * 
     * @see ggc.plugin.output.OutputWriter#getStatus()
     */
    public int getStatus()
    {
        return this.reading_status;
    }

    /**
     * Refresh Status
     */
    public void refreshStatus()
    {
        setGUIStatus(current_status);
    }
    
    
    private int current_status = 0;
    
    
    /**
     * Set GUI Status
     * 
     * @param status
     */
    public void setGUIStatus(int status)
    {
        
        current_status = status;
        
        if ((this.sub_status==null) || (this.sub_status.length()==0))
        {
            this.lbl_status.setText(this.m_da.getReadingStatuses()[status]);
        }
        else
        {
            this.lbl_status.setText(this.m_da.getReadingStatuses()[status] + " - " + m_ic.getMessage(this.sub_status));
        }

        switch (status)
        {

        
            case AbstractOutputWriter.STATUS_DOWNLOADING: // downloading
                {
                    this.bt_break.setEnabled(true);
                    this.bt_close.setEnabled(false);
                    //this.bt_import.setEnabled(false);
                } break;
                
            case AbstractOutputWriter.STATUS_DOWNLOAD_FINISHED: // finished
                {
                    this.bt_break.setEnabled(false);
                    this.bt_close.setEnabled(true);
                    //this.bt_import.setEnabled(true);
                    //filter_combo.setEnabled(true);
                    //sel_all.setEnabled(true);
                    //unsel_all.setEnabled(true);
                }
                break;

            case AbstractOutputWriter.STATUS_READER_ERROR: // error
            {
                this.bt_break.setEnabled(false);
                this.bt_close.setEnabled(true);
                //this.bt_import.setEnabled(false);
                //filter_combo.setEnabled(false);
                //sel_all.setEnabled(false);
                //unsel_all.setEnabled(false);
            }
            break;
                
                
            case AbstractOutputWriter.STATUS_STOPPED_DEVICE: // stopped - device 
            case AbstractOutputWriter.STATUS_STOPPED_USER: // stoped - user
                {
                    this.bt_break.setEnabled(false);
                    this.bt_close.setEnabled(true);
                    //this.bt_import.setEnabled(false);
                }
                break;
    
            case AbstractOutputWriter.STATUS_READY:  // ready
            //case 0:  // none
            default:
                {
                    this.bt_break.setEnabled(false);
                    this.bt_close.setEnabled(false);
                    //this.bt_import.setEnabled(false);
                } break;
        }

    }


    /**
     * Set Device Comment
     * 
     * @param text
     */
    public void setDeviceComment(String text)
    {
        //this.lbl_comment.setText(m_ic.getMessage(text));
    }
    
    
    /*
    public static void main(String[] args)
    {
        JFrame f = new JFrame();
        f.setSize(800,600);
        
        DataAccessMeter.getInstance().addComponent(f);
        
        // MeterReadDialog mrd =
        new MeterDisplayDataDialog(); // new AscensiaContour("COM12", new
                                      // ConsoleOutputWriter()));
    }
*/

    /**
     * Write Data to OutputWriter
     * 
     * @param data
     */
    public void writeData(OutputWriterData data)
    {
        count++;
        this.model.addEntry((DeviceValuesEntryInterface)data);
    }

    
    /**
     * Write log entry
     * 
     * @param entry_type
     * @param message
     */
    public void writeLog(int entry_type, String message)
    {
        this.addLogText(message);
    }


    /**
     * Write log entry
     * 
     * @param entry_type
     * @param message
     * @param ex
     */
    public void writeLog(int entry_type, String message, Exception ex)
    {
        this.addLogText(message);
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
        return m_da.getDeviceConfigurationDefinition().getHelpPrefix() + "Config_Reading_View";
    }
    
    

    /**
     * Set old data reading progress
     * 
     * @param value
     */
    public void setOldDataReadingProgress(int value)
    {
        this.progress_old.setValue(value);
    }


    /**
     * Can old data reading be initiated (if module in current running mode supports this, this is
     * intended mostly for usage outside GGC)
     * 
     * @param value
     */
    public void canOldDataReadingBeInitiated(boolean value)
    {
    }
    

    String device_source;
    
    /**
     * Set Device Source
     * 
     * @param dev
     */
    public void setDeviceSource(String dev)
    {
        this.device_source = dev;
    }
    
    
    /**
     * Set Device Source
     * 
     * @return 
     */
    public String getDeviceSource()
    {
        return this.device_source;
    }
    

    /**
     * setIndeterminateProgress - if we cannot trace progress, we set this and JProgressBar will go
     *    into indeterminate mode
     */
    public void setIndeterminateProgress()
    {
    }
    
    
    
}
