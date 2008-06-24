package ggc.meter.gui;

import ggc.meter.data.MeterValuesEntry;
import ggc.meter.data.MeterValuesTable;
import ggc.meter.data.MeterValuesTableModel;
import ggc.meter.data.cfg.MeterConfigEntry;
import ggc.meter.device.DeviceIdentification;
import ggc.meter.device.MeterInterface;
import ggc.meter.output.AbstractOutputWriter;
import ggc.meter.output.OutputUtil;
import ggc.meter.output.OutputWriter;
import ggc.meter.util.DataAccessMeter;
import ggc.meter.util.I18nControl;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.TableColumn;

import com.atech.utils.ATechDate;


public class MeterDisplayDataDialog extends JDialog implements ActionListener, OutputWriter
{
	static final long serialVersionUID = 0;


    //private JLabel infoIcon = null;
//x    private JLabel infoDescription = null;

    private I18nControl m_ic = I18nControl.getInstance();        
    MeterReaderRunner mrr;



    private DataAccessMeter m_da = DataAccessMeter.getInstance();

//    private static ReadMeterDialog singleton = null;

    private JTextArea logText = null;
    public JProgressBar progress = null;

    //private GlucoValues glucoValues = null;
    private MeterValuesTableModel model = null;
    private MeterValuesTable table = null;
    
    
//    private GlucoTable resTable;

//    private JButton startButton;
//    private JButton saveButton;

    private JButton bt_close, bt_import, bt_break;

    
    private JTabbedPane tabPane;
    //private SerialMeterImport meterImport = null;
    //private StartImportAction startImportAction = new StartImportAction();

    //private MeterInterface meterDevice = null;
   // private MeterImportManager m_mim = null;

    JLabel lbl_status;

    JTextArea ta_info = null;
    
    //TimerThread m_timer = null;

    int x,y;

    JFrame parentMy;
    
    MeterInterface meter_interface;



    /**
     * Constructor for ReadMeterDialog.
     * @param owner
     * @throws HeadlessException
     */
    public MeterDisplayDataDialog(JFrame owner, MeterInterface mi)
    {
        super(owner);
        m_da.addComponent(this);
        meter_interface = mi;
        this.parentMy = owner;
        
        dialogPreInit();
    }


    public MeterDisplayDataDialog(MeterInterface mi)
    {
        super();
        m_da.addComponent(this);
        meter_interface = mi;
        
        dialogPreInit();
    }


    
    public MeterDisplayDataDialog()
    {
        super();
        
        this.loadConfiguration();
        
        this.mrr = new MeterReaderRunner(this.configured_meter, this); 
        
        m_da.addComponent(this);
        //meter_interface = mi;
        
        dialogPreInit();
    }
    
    
    MeterConfigEntry configured_meter = null;
    
    private void loadConfiguration()
    {
        // TODO: this should be read from config
        
        this.configured_meter = new MeterConfigEntry();
        this.configured_meter.id =1;
        this.configured_meter.communication_port = "COM9";
        this.configured_meter.name = "My Countour";
        this.configured_meter.meter_company = "Ascensia/Bayer";
        this.configured_meter.meter_device = "Contour";
        this.configured_meter.ds_area= "Europe/Prague";
        this.configured_meter.ds_winter_change = 0;
        this.configured_meter.ds_summer_change = 1;
        this.configured_meter.ds_fix = true;
        
        /*
        tzu.setTimeZone("Europe/Prague");
        tzu.setWinterTimeChange(0);
        tzu.setSummerTimeChange(+1);
        */
        
//        MeterInterface mi = MeterManager.getInstance().getMeterDevice(this.configured_meter.meter_company, this.configured_meter.meter_device);
        
//        this.meter_interface = mi;
        
    }
    
    
    
    private void dialogPreInit()
    {
        setTitle(m_ic.getMessage("READ_METER_DATA") + "  [" + this.configured_meter.meter_device + " on " + this.configured_meter.communication_port + "]");

        init();
        //postInit();

        
//        this.mrr.start();
        guiTest();
        
        this.setVisible(true);

    }


    private void guiTest()
    {
        MeterValuesEntry mve = new MeterValuesEntry();
        mve.setBgUnit(OutputUtil.BG_MMOL);
        mve.setBgValue("8.7");
        mve.setDateTime(new ATechDate(200806121233L));

        this.model.addEntry(mve);
        

        mve = new MeterValuesEntry();
        mve.setBgUnit(OutputUtil.BG_MMOL);
        mve.setBgValue("10.1");
        mve.setDateTime(new ATechDate(200806121456L));

        this.model.addEntry(mve);
        
        mve = new MeterValuesEntry();
        mve.setBgUnit(OutputUtil.BG_MMOL);
        mve.setBgValue("10.1");
        mve.setDateTime(new ATechDate(200806121456L));

        this.model.addEntry(mve);

        mve = new MeterValuesEntry();
        mve.setBgUnit(OutputUtil.BG_MMOL);
        mve.setBgValue("10.1");
        mve.setDateTime(new ATechDate(200806121456L));

        this.model.addEntry(mve);

        mve = new MeterValuesEntry();
        mve.setBgUnit(OutputUtil.BG_MMOL);
        mve.setBgValue("10.1");
        mve.setDateTime(new ATechDate(200806121456L));

        this.model.addEntry(mve);
        
        
    }
    

    /**
     * Method getInstance.
     * @return ReadMeterDialog
     */
    /*public static ReadMeterDialog getInstance()
    {
        return singleton;
    }*/

    /**
     * Method showMe.
     * @param owner
     */
   /* public static void showMe(Frame owner)
    {
        if (singleton == null)
            singleton = new ReadMeterDialog(owner);
        singleton.setLocationRelativeTo(owner);
        singleton.setVisible(true);
    }*/



    public void addLogText(String s)
    {
        logText.append(s + "\n");
    }

    /*
    public GlucoValues getGlucoValues()
    {
        return glucoValues;
    }*/

    protected void init()
    {
        
        model = new MeterValuesTableModel();

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setSize(600, 600);

        JLabel label;

        int xkor=300;
        int ykor=300;

        if (this.parentMy!=null)
        {
            Rectangle rec = this.parentMy.getBounds();
            xkor = rec.x + (rec.width/2);
            ykor = rec.y + (rec.height/2);
        }

        setBounds(xkor-250, ykor-250, 480, 580);
        //dWindowListener(new CloseListener());

        //setBounds(300, 300, 300, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        getContentPane().add(panel, BorderLayout.CENTER);

        
        // TabControl with two tabs: log and data

        logText = new JTextArea(m_ic.getMessage("LOG__")+":\n", 8, 35);
        logText.setAutoscrolls(true);
        JScrollPane sp = new JScrollPane(logText, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

//x        resTable = new GlucoTable();

//x        JScrollPane sp2 = new JScrollPane(resTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        
        
        this.table = new MeterValuesTable(model);

        
//        this.table = MeterValuesTable.createMeterValuesTable(model);
        
        tabPane = new JTabbedPane();
        //tabPane.add("Values", sp2);
        tabPane.add(m_ic.getMessage("DATA"), this.createTablePanel(this.table));
        tabPane.add(m_ic.getMessage("LOG"), sp);
        tabPane.setBounds(30, 15, 400, 250);
        panel.add(tabPane);

        // Info

        label = new JLabel(m_ic.getMessage("METER_INFO") + ":");
        label.setBounds(30, 310, 300, 25);
        panel.add(label);
        
        ta_info = new JTextArea();
        JScrollPane sp3 = new JScrollPane(ta_info);
        sp3.setBounds(30, 340, 400, 50);
        panel.add(sp3);

        ta_info.setText(""); //this.meter_interface.getDeviceInfo().getInformation(""));

        


        

        
        // meter status
        label = new JLabel(m_ic.getMessage("ACTION") + ":");
        label.setBounds(30, 415, 100, 25);
        panel.add(label);
        
        lbl_status = new JLabel(m_ic.getMessage("READY"));
        lbl_status.setBounds(130, 415, 150, 25);
        panel.add(lbl_status);
        
        
        this.progress = new JProgressBar();
        this.progress.setBounds(30, 450, 400, 15);
        //this.progress.setIndeterminate(true);
        panel.add(this.progress);
        
        
        
        bt_break = new JButton(m_ic.getMessage("BREAK_COMMUNICATION"));
        bt_break.setBounds(160, 490, 170, 25);
//        bt_break.setEnabled(this.m_mim.isStatusOK());
        bt_break.setActionCommand("break_communication");
        bt_break.addActionListener(this);
        panel.add(bt_break);
        
        JButton help_button = m_da.createHelpButtonByBounds(30, 490, 110, 25, this);
        panel.add(help_button);
   
        bt_close = new JButton(m_ic.getMessage("CLOSE"));
        bt_close.setBounds(380, 490, 110, 25);
        bt_close.setEnabled(false);
        bt_close.setActionCommand("close");
        bt_close.addActionListener(this);
        panel.add(bt_close);
        
        
        bt_import = new JButton(m_ic.getMessage("IMPORT_TO_DB"));
        bt_import.setBounds(260, 280, 170, 25);
        bt_import.setEnabled(false);
        
        //button.setEnabled(meterDevice.isStatusOK());
        
        panel.add(bt_import);
        
    }


    public void initMeter()
    {
/*
        String meterClassName = m_da.getMeterManager().meter_classes[m_da.getSettings().getMeterType()];

        if (meterClassName == null || meterClassName.equals(""))
            throw new NullPointerException(m_ic.getMessage("NO_CLASS_FOR_METER_DEFINED"));

        try 
        {
            meterImport = (SerialMeterImport)Class.forName(meterClassName).newInstance();
            meterDevice = (MeterInterface)Class.forName(m_da.getMeterManager().meter_device_classes[m_da.getSettings().getMeterType()]).newInstance();
        } 
        catch (Exception exc) 
        {
            System.out.println(exc);
        }
*/
        //infoPanel.setBorder(new TitledBorder(meterImport.getName()));
/*
        meterImport.addImportEventListener(startImportAction);

//        this.addWindowListener(new CloseListener());

        glucoValues = new GlucoValues();
        model = new GlucoTableModel(glucoValues);
        model.addTableModelListener(new TableModelListener()
        {
            public void tableChanged(TableModelEvent e)
            {
                //System.out.println(e);
                if (model.getRowCount() == 0 && saveButton.isEnabled())
                    saveButton.setEnabled(false);
                if (model.getRowCount() > 0 && !saveButton.isEnabled())
                    saveButton.setEnabled(true);
            }
        });
*/

    }

    String[] filter_states = { 
            m_ic.getMessage("FILTER_ALL"), 
            m_ic.getMessage("FILTER_NEW"), 
            m_ic.getMessage("FILTER_CHANGED"), 
            m_ic.getMessage("FILTER_EXISTING"), 
            m_ic.getMessage("FILTER_UNKNOWN"), 
            m_ic.getMessage("FILTER_NEW_CHANGED"), 
            m_ic.getMessage("FILTER_ALL_BUT_EXISTING") 
    };
    
    
    public JPanel createTablePanel(MeterValuesTable table)
    {
        
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(false);
        
        JScrollPane scroller = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

//        final AddRowAction addRowAction = new AddRowAction(table);
//        final DeleteRowAction deleteRowAction = new DeleteRowAction(table);

        JToolBar toolBar = new JToolBar();
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
        toolBar.setFloatable(false);
        
        toolBar.add(new JLabel(m_ic.getMessage("FILTER")+":   "));
        toolBar.add(new JComboBox(this.filter_states));
        toolBar.add(new JLabel("   "));
        toolBar.add(this.createButton("select_all", m_ic.getMessage("SELECT_ALL"), "element_selection.png"));
        toolBar.add(new JLabel(" "));
        toolBar.add(this.createButton("deselect_all", m_ic.getMessage("DESELECT_ALL"), "element_selection_delete.png"));
        
        //toolBar.add(addRowAction);
        //toolBar.add(deleteRowAction);
        //UIUtilities.addToolBarButton(toolBar, addRowAction);
        //UIUtilities.addToolBarButton(toolBar, deleteRowAction);
        //toolBar.add(addRowAction);
        //toolBar.add(deleteRowAction);

        int[] cw = { 110, 80, 70, 90,20 };
        
        TableColumn column = null;
        for (int i = 0; i < 5; i++) {
            column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(cw[i]);
        }        
        

        JPanel container = new JPanel(new BorderLayout());
        container.add(toolBar, "North");
        container.add(scroller, "Center");

        return container;
        
        
    }
    
    
    public JButton createButton(String command_text, String tooltip, String image_d)
    {
        JButton b = new JButton();
        b.setIcon(m_da.getImageIcon(image_d, 15, 15, this));
        b.addActionListener(this);
        b.setActionCommand(command_text);
        b.setToolTipText(tooltip);
        return b;
    }
    
    
    


    


    //////////////////////////////////////////////////////////////
    // Action classes

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
            this.dispose();
        }
        else
            System.out.println("MeterDisplayDataDialog::Unknown command: " + action);

    }

    
    /* 
     * endOutput
     */
    public void endOutput()
    {
        System.out.println("endOutput()");
        // TODO Auto-generated method stub
        
    }

    
    DeviceIdentification device_ident;

    /* 
     * getDeviceIdentification
     */
    public DeviceIdentification getDeviceIdentification()
    {
        return device_ident;
    }

    OutputUtil output_util = new OutputUtil(this);
    
    
    /* 
     * getOutputUtil
     */
    public OutputUtil getOutputUtil()
    {
        return this.output_util;
    }


    /* 
     * interruptCommunication
     */
    public void interruptCommunication()
    {
        System.out.println("interComm()");
        
    }


    /* 
     * setBGOutputType
     */
    public void setBGOutputType(int bg_type)
    {
        // TODO Auto-generated method stub
        System.out.println("setBGOutput()");
    }


    /* 
     * setDeviceIdentification
     */
    public void setDeviceIdentification(DeviceIdentification di)
    {
        this.device_ident = di;
    }

    int count =0;
    
    /* 
     * writeBGData
     */
    public void writeBGData(MeterValuesEntry mve)
    {
        count++;
        this.model.addEntry(mve);
        
        //this.progress.setValue((int)(count/500));
        //this.progress.repaint();
        
        //this.table.invalidate();
        //this.table.repaint();
        //this.table.setModel(this.model);
        //this.table.
        // TODO Auto-generated method stub
        System.out.println("writeBGData()");
    }


    /* 
     * writeDeviceIdentification
     */
    public void writeDeviceIdentification()
    {
        // TODO Auto-generated method stub
        System.out.println("writeDeviceIndentification()");
        
    }


    /* 
     * writeHeader
     */
    public void writeHeader()
    {
    }


    /* 
     * writeRawData
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
     * This should be queried by device implementation, to see if it must stop reading
     */
    public boolean isReadingStopped()
    {
        return this.device_should_be_stopped;
    }
    
    int reading_status = 1;
    
    /**
     * This is status of device and also of GUI that is reading device (if we have one)
     * This is to set that status to see where we are. Allowed statuses are: 1-Ready, 2-Downloading,
     * 3-Stopped by device, 4-Stoped by user,5-Download finished,6-Reader error
     */
    public void setStatus(int status)
    {
        if ((this.reading_status==3) || (this.reading_status==4))
            return;
        
        this.reading_status = status;
        setGUIStatus(status);
    }
    
    public int getStatus()
    {
        return this.reading_status;
    }
    
    
    public String statuses[] =  { 
            m_ic.getMessage("STATUS_NONE"),
            m_ic.getMessage("STATUS_READY"),
            m_ic.getMessage("STATUS_DOWNLOADING"),
            m_ic.getMessage("STATUS_STOPPED_DEVICE"),
            m_ic.getMessage("STATUS_STOPPED_USER"),
            m_ic.getMessage("STATUS_DOWNLOAD_FINISHED"),
            m_ic.getMessage("STATUS_READER_ERROR"),
            }; 
    
    
    public void setGUIStatus(int status)
    {
        this.lbl_status.setText(statuses[status]);
        
        switch(status)
        {
            case 2:
            case 3:
            case 5:
                {
                    this.bt_break.setEnabled(false);
                    this.bt_close.setEnabled(true);
                } break;
            
                
            case 4:
                {
                    this.bt_break.setEnabled(false);
                    this.bt_close.setEnabled(true);
                    this.bt_import.setEnabled(true);
                } break;
            
            case 1:
            case 0:
            default:
                break;
        }
        
        
    }
    
    

    public static void main(String[] args)
    {
        //MeterReadDialog mrd = 
        new MeterDisplayDataDialog(); //new AscensiaContour("COM12", new ConsoleOutputWriter()));
    }

    


}
