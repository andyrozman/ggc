/*
 * Created on 12.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */

package ggc.gui.dialogs;

import java.awt.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.comm.NoSuchPortException;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import ggc.data.DailyValuesRow;
import ggc.data.GlucoTableModel;
import ggc.data.GlucoValues;
import ggc.data.event.ImportEvent;
import ggc.data.event.ImportEventListener;
import ggc.data.imports.ImportException;
import ggc.data.imports.SerialMeterImport;
import ggc.data.meter.device.MeterInterface;
import ggc.gui.*;
import ggc.gui.calendar.TimerThread;
import ggc.util.DataAccess;
import ggc.util.GGCProperties;
import ggc.util.I18nControl;


/**
 * @author stephan
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public class MeterReadDialog extends JDialog implements ActionListener
{


    private JLabel infoIcon = null;
    private JLabel infoDescription = null;

    private I18nControl m_ic = I18nControl.getInstance();        
    private DataAccess m_da = DataAccess.getInstance();

//    private static ReadMeterDialog singleton = null;

    private JTextArea logText = null;
    private JProgressBar progress = null;

    private GlucoValues glucoValues = null;
    private GlucoTableModel model = null;
    private GlucoTable resTable;

    private JButton startButton;
    private JButton saveButton;

    private JButton bt_get, bt_import;


    
    private JTabbedPane tabPane;
    private SerialMeterImport meterImport = null;
    private StartImportAction startImportAction = new StartImportAction();

    private MeterInterface meterDevice = null;

    JLabel lbl_status;

    JTextArea ta_info = null;
    
    TimerThread m_timer = null;
    /**
     * Constructor for ReadMeterDialog.
     * @param owner
     * @throws HeadlessException
     */
    public MeterReadDialog(JFrame owner)
    {
        super(owner);

        setTitle(m_ic.getMessage("READ_METER_DATA"));

        initMeter();
        init();
        postInit();

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

    /**
     * Method close.
     */
/*    public void close()
    {
        dispose();
    } */

    /**
     * Method close.
     */
    /*public void dispose()
    {
        singleton = null;
        super.dispose();
    } */

    public void addLogText(String s)
    {
        logText.append(s + "\n");
    }

    public GlucoValues getGlucoValues()
    {
        return glucoValues;
    }

    protected void init()
    {

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setSize(500, 500);

        JLabel label;
        
        Rectangle rec = this.getParent().getBounds();
        int x = rec.x + (rec.width/2);
        int y = rec.y + (rec.height/2);

        setBounds(x-250, y-250, 500, 500);
        //dWindowListener(new CloseListener());

        //setBounds(300, 300, 300, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().add(panel, BorderLayout.CENTER);

        
        // TabControl with two tabs: log and data

        logText = new JTextArea(m_ic.getMessage("LOG__")+":\n", 8, 35);
        logText.setAutoscrolls(true);
        JScrollPane sp = new JScrollPane(logText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        resTable = new GlucoTable();

        JScrollPane sp2 = new JScrollPane(resTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        tabPane = new JTabbedPane();
        //tabPane.add("Values", sp2);
        tabPane.add(m_ic.getMessage("DATA"), GlucoTable.createGlucoTable(model));
        tabPane.add(m_ic.getMessage("LOG"), sp);
        tabPane.setBounds(180, 20, 300, 250);
        panel.add(tabPane);

        // Info

        label = new JLabel(m_ic.getMessage("METER_INFO") + ":");
        label.setBounds(182, 275, 300, 25);
        panel.add(label);
        
        ta_info = new JTextArea();
        JScrollPane sp3 = new JScrollPane(ta_info);
        sp3.setBounds(180, 300, 300, 50);
        panel.add(sp3);

        ta_info.setText(this.meterDevice.getInfo());



        infoIcon = new JLabel(meterDevice.getIcon());
        infoIcon.setBounds(10, 20, 150, 200);
        infoIcon.setHorizontalAlignment(JLabel.CENTER);
        panel.add(infoIcon);
        
        label = new JLabel(meterDevice.getName());
        label.setBounds(10, 230, 150, 25);
        label.setHorizontalAlignment(JLabel.CENTER);
        panel.add(label);


        // time
        label = new JLabel(m_ic.getMessage("COMPUTER_TIME") + ":");
        label.setBounds(182, 360, 150, 25);
        panel.add(label);
        
        label = new JLabel(m_ic.getMessage("METER_TIME") + ":");
        label.setBounds(182, 380, 150, 25);
        panel.add(label);

        label = new JLabel("00:00:00");
        label.setBounds(320, 360, 100, 25);
        panel.add(label);

        JLabel label_1 = new JLabel("00:00:00");
        label_1.setBounds(320, 380, 100, 25);
        panel.add(label_1);

        m_timer = new TimerThread(m_da, label, label_1, meterDevice.getTimeDifference());
        m_timer.start();
        

        // meter status
        label = new JLabel(m_ic.getMessage("METER_STATUS") + ":");
        label.setBounds(182, 410, 100, 25);
        panel.add(label);
        
        lbl_status = new JLabel();
        lbl_status.setBounds(320, 410, 150, 25);
        panel.add(lbl_status);

        setStatus();


        // buttons
        JButton button = new JButton(m_ic.getMessage("REFRESH_CONNECTION"));
        button.setBounds(20, 280, 140, 50);
        button.setActionCommand("refresh_connection");
        button.addActionListener(this);
        panel.add(button);
        /*
        button = new JButton(m_ic.getMessage("REFRESH_INFO"));
        button.setBounds(30, 280, 150, 25);
        panel.add(button);

        button = new JButton(m_ic.getMessage("REFRESH_TIME"));
        button.setBounds(30, 320, 150, 25);
        panel.add(button);
*/
        bt_get = new JButton(m_ic.getMessage("GET_DATA"));
        bt_get.setBounds(20, 360, 140, 25);
        bt_get.setEnabled(meterDevice.isStatusOK());
        bt_get.setActionCommand("get_data");
        bt_get.addActionListener(this);
        panel.add(bt_get);

        bt_import = new JButton(m_ic.getMessage("IMPORT_TO_DB"));
        bt_import.setBounds(20, 410, 140, 25);
        bt_import.setEnabled(false);
        
        //button.setEnabled(meterDevice.isStatusOK());
        
        panel.add(bt_import);
/*        
REFRESH_CONNECTION=Refresh Connection
REFRESH_INFO=Refresh Info
REFRESH_TIME=Refresh Time
GET_DATA=Load Data
IMPORT_TO_DB=Import to Db
*/

        
        
        /*
        
        progress = new JProgressBar(0, 100);
        progress.setPreferredSize(new Dimension(100, 8));

        startButton = new JButton("Start"); // startImportAction
        startButton.setActionCommand("start");
        saveButton = new JButton("Save");  // new SaveAction()
        saveButton.setActionCommand("save");
        saveButton.setEnabled(false);

        JPanel importButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        importButtonPanel.add(startButton);
        importButtonPanel.add(saveButton);

        JPanel dialogButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("Close");  // new CloseAction()
        closeButton.setActionCommand("save");
        dialogButtonPanel.add(closeButton);

        JPanel buttonPanel = new JPanel(new BorderLayout(0, 0));
        buttonPanel.add(importButtonPanel, "West");
        buttonPanel.add(dialogButtonPanel, "East");

        JPanel infoPanel = new JPanel(new BorderLayout());
        //JLabel infoIcon = new JLabel(new ImageIcon(getClass().getResource("/icons/euroflash.png")));
        //JLabel infoIcon = new JLabel(new ImageIcon(getClass().getResource("/icons/freestyle.png")));
        infoIcon = new JLabel();
        infoDescription = new JLabel();
        infoDescription.setVerticalAlignment(JLabel.TOP);
        infoPanel.add(infoIcon, "North");
        infoPanel.add(infoDescription, "Center");
        infoPanel.setBorder(new TitledBorder(meterImport.getName()));
//        infoPanel.setBorder(new TitledBorder(""));
        infoPanel.setPreferredSize(new Dimension(160, 250));

        JPanel importContent = new JPanel(new BorderLayout(5, 10));
        importContent.add(buttonPanel, "North");
        importContent.add(tabPane, "Center");
        importContent.add(progress, "South");

        JPanel content = new JPanel(new BorderLayout(5, 15))
        {
            public Insets getInsets()
            {
                return new Insets(8, 8, 8, 8);
            }

            public Insets getInsets(Insets insets)
            {
                insets.top = 8;
                insets.left = 8;
                insets.bottom = 8;
                insets.right = 8;
                return insets;
            }
        };
        setContentPane(content);

        content.add(importContent, "Center");
        content.add(infoPanel, "West");
        content.add(buttonPanel, "South");

        pack();
        */
    }


    public void initMeter()
    {

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

        //infoPanel.setBorder(new TitledBorder(meterImport.getName()));

        
        //meterImport = new EuroFlashImport();
        //meterImport = new FreeStyleImport();
        //meterImport = new GlucoCardImport();

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
        if (meterDevice.isStatusOK())
            lbl_status.setText("<html><font color=\"green\" >" + m_ic.getMessage("OK") + "</font></html>");
        else
            lbl_status.setText("<html><font color=\"red\" >" + m_ic.getMessage("ERROR") + "</font></html>");

/*        
        if (meterDevice.isStatusOK())
            lbl_status.setText("<html>" + m_ic.getMessage("METER_STATUS") + ": " + "<font color=\"green\" >" + m_ic.getMessage("OK") + "</font></html>");
        else
            lbl_status.setText(m_ic.getMessage("METER_STATUS") + ": " + "<html><font color=\"red\" >" + m_ic.getMessage("ERROR") + "</font></html>");
           */
    }

    


    //////////////////////////////////////////////////////////////
    // Action classes
    protected class SaveAction extends AbstractAction
    {
        public SaveAction()
        {
            super();

            putValue(Action.NAME, m_ic.getMessageWithoutMnemonic("ME_SAVE"));
            
            char ch = m_ic.getMnemonic("ME_SAVE");

            if (ch!='0') 
                putValue(Action.MNEMONIC_KEY, ""+ch);

            
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
         */
        public void actionPerformed(ActionEvent e)
        {
            glucoValues.saveValues();
        }

    }

    protected class CloseAction extends AbstractAction
    {
        public CloseAction()
        {
            super();

            putValue(Action.NAME, m_ic.getMessageWithoutMnemonic("ME_CLOSE"));
            
            char ch = m_ic.getMnemonic("ME_CLOSE");

            if (ch!='0') 
                putValue(Action.MNEMONIC_KEY, ""+ch);
            

        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
         */
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

        /**
         * @see data.imports.ImportEventListener#importChanged(ImportEvent)
         */
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
            System.out.println("ReadMeterDialog::Unknown command: " + action);

    }


}
