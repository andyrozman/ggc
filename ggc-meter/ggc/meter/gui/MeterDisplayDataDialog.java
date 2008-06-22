package ggc.meter.gui;

import ggc.meter.data.MeterValuesEntry;
import ggc.meter.data.MeterValuesTable;
import ggc.meter.data.MeterValuesTableModel;
import ggc.meter.data.cfg.MeterConfigEntry;
import ggc.meter.device.DeviceIdentification;
import ggc.meter.device.MeterInterface;
import ggc.meter.output.OutputUtil;
import ggc.meter.output.OutputWriter;
import ggc.meter.util.DataAccessMeter;
import ggc.meter.util.I18nControl;
import ggc.meter.util.TimerThread;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;


public class MeterDisplayDataDialog extends JDialog implements ActionListener, OutputWriter
{
	static final long serialVersionUID = 0;


    private JLabel infoIcon = null;
//x    private JLabel infoDescription = null;

    private I18nControl m_ic = I18nControl.getInstance();        



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

    private JButton bt_get, bt_import, bt_break;

    
    private JTabbedPane tabPane;
    //private SerialMeterImport meterImport = null;
    //private StartImportAction startImportAction = new StartImportAction();

    //private MeterInterface meterDevice = null;
   // private MeterImportManager m_mim = null;

    JLabel lbl_status;

    JTextArea ta_info = null;
    
    TimerThread m_timer = null;

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


    MeterReaderRunner mrr;
    
    Thread thr;
    
    public MeterDisplayDataDialog()
    {
        super();
        
        this.loadConfiguration();
        
        System.out.println(this.configured_meter);
        
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
//a        setTitle(m_ic.getMessage("READ_METER_DATA") + "  [" + this.meter_interface.getName() + " on " + this.meter_interface.getPort() + "]");

//        m_mim = new MeterImportManager();
//        initMeter();
        init();
        postInit();
        this.mrr.start();

        this.setVisible(true);

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
        tabPane.add(m_ic.getMessage("DATA"), MeterValuesTable.createMeterValuesTable(this.table));
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
        
        lbl_status = new JLabel("Empty");
        lbl_status.setBounds(130, 415, 150, 25);
        panel.add(lbl_status);
        
        
        this.progress = new JProgressBar();
        this.progress.setBounds(30, 450, 400, 15);
        //this.progress.setIndeterminate(true);
        panel.add(this.progress);
        
        
        
        bt_break = new JButton(m_ic.getMessage("BREAK_COMMUNICATION"));
        bt_break.setBounds(170, 490, 250, 25);
//        bt_break.setEnabled(this.m_mim.isStatusOK());
        bt_break.setActionCommand("break_communication");
        bt_break.addActionListener(this);
        panel.add(bt_break);
        
        JButton help_button = m_da.createHelpButtonByBounds(30, 490, 120, 25, this);
        panel.add(help_button);
   
        
        
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

    public void postInit()
    {
        /*
        resTable.setModel(model);


        infoIcon.setIcon((meterImport.getImage() == null)
                                     ? new ImageIcon(getClass().getResource("/icons/noMeter.gif"))
                                     : meterImport.getImage());

        this.infoDescription.setText((meterImport.getUseInfoMessage() == null) ? m_ic.getMessage("NO_TO_USE_INFORMATION") : meterImport.getUseInfoMessage());
        */
    }


    private void setStatus()
    {
        /*
        if (this.m_mim.isStatusOK())
            lbl_status.setText("<html><font color=\"green\" >" + m_ic.getMessage("OK") + "</font></html>");
        else
            lbl_status.setText("<html><font color=\"red\" >" + m_ic.getMessage("ERROR") + "</font></html>");
        */
/*        
        if (meterDevice.isStatusOK())
            lbl_status.setText("<html>" + m_ic.getMessage("METER_STATUS") + ": " + "<font color=\"green\" >" + m_ic.getMessage("OK") + "</font></html>");
        else
            lbl_status.setText(m_ic.getMessage("METER_STATUS") + ": " + "<html><font color=\"red\" >" + m_ic.getMessage("ERROR") + "</font></html>");
           */
    }

    


    //////////////////////////////////////////////////////////////
    // Action classes
 /*   protected class SaveAction extends AbstractAction
    {
    	static final long serialVersionUID = 0;
        public SaveAction()
        {
            super();

            putValue(Action.NAME, m_ic.getMessageWithoutMnemonic("ME_SAVE"));
            
            char ch = m_ic.getMnemonic("ME_SAVE");

            if (ch!='0') 
                putValue(Action.MNEMONIC_KEY, ""+ch);

            
        }

        public void actionPerformed(ActionEvent e)
        {
            glucoValues.saveValues();
        }

    }

    protected class CloseAction extends AbstractAction
    {
    	
    	static final long serialVersionUID = 0;
    	
        public CloseAction()
        {
            super();

            putValue(Action.NAME, m_ic.getMessageWithoutMnemonic("ME_CLOSE"));
            
            char ch = m_ic.getMnemonic("ME_CLOSE");

            if (ch!='0') 
                putValue(Action.MNEMONIC_KEY, ""+ch);
            

        }

        public void actionPerformed(ActionEvent e)
        {
            meterImport.stopImport();
            meterImport.removeImportEventListener(startImportAction);
            meterImport.close();
        //    ReadMeterDialog.this.close();
        }

    }

    protected class StartImportAction extends AbstractAction implements ImportEventListener
    {
    	static final long serialVersionUID = 0;

        public StartImportAction()
        {
            super();
            putValue(Action.NAME, m_ic.getMessageWithoutMnemonic("ME_IMPORT"));
            
            char ch = m_ic.getMnemonic("ME_IMPORT");

            if (ch!='0') 
                putValue(Action.MNEMONIC_KEY, ""+ch);
        }

        public void actionPerformed(ActionEvent e)
        {

            tabPane.setSelectedIndex(1);

            try 
            {
                meterImport.setPort(m_da.getSettings().getMeterPort());

                progress.setIndeterminate(true);
                meterImport.open();

                meterImport.importData();
            } 
            catch (NoSuchPortException exc) 
            {
                progress.setIndeterminate(false);
                addLogText(m_ic.getMessage("NO_SUCH_COM_PORT_FOUND"));
            } 
            catch (ImportException exc) 
            {
                progress.setIndeterminate(false);
                addLogText(m_ic.getMessage("EXCEPTION_ON_IMPORT")+":");
                addLogText(exc.getMessage());
            }

        }

        public void importChanged(ImportEvent event)
        {
            switch (event.getType()) 
            {
                case ImportEvent.PROGRESS:
                    progress.setIndeterminate(false);
                    progress.setValue(event.getProgress());
                    break;

                case ImportEvent.PORT_OPENED:
                    addLogText(m_ic.getMessage("PORT_TO_METER_OPENED"));
                    break;

                case ImportEvent.PORT_CLOSED:
                    addLogText(m_ic.getMessage("PORT_TO_METER_CLOSED"));
                    break;

                case ImportEvent.IMPORT_FINISHED:
                    meterImport.close();
                    DailyValuesRow[] data = meterImport.getImportedData();
                    addLogText(m_ic.getMessage("HAD_READ_VALUES_FROM_METER")+": " + data.length);
                    for (int i = 0; i < data.length; i++) 
                    {
                        DailyValuesRow dailyValuesRow = data[i];
                        getGlucoValues().setNewRow(dailyValuesRow);
                    }

                    progress.setIndeterminate(false);
                    tabPane.setSelectedIndex(0);
                    break;

                case ImportEvent.TIMEOUT:
                    progress.setIndeterminate(false);
                    meterImport.close();
                    addLogText(m_ic.getMessage("TIMEOUT_NO_DATA_SENT_FROM_METER")+"\n" + m_ic.getMessage("PLEASE_CHECK_CABLE"));
                    break;
            }
        }

    }
*/

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e) 
    {
        String action = e.getActionCommand();

        //if (action.equals("")) 
        //{
        //}
        //else
            System.out.println("ReadDataDialog::Unknown command: " + action);

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
    
    

    public static void main(String[] args)
    {
        //MeterReadDialog mrd = 
        new MeterDisplayDataDialog(); //new AscensiaContour("COM12", new ConsoleOutputWriter()));
    }

    


}
