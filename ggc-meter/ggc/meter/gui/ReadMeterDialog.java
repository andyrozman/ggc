/*
 * Created on 12.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */

package ggc.meter.gui;


import ggc.data.event.ImportEvent;
import ggc.data.event.ImportEventListener;
import ggc.data.imports.ImportException;
import ggc.data.imports.SerialMeterImport;
import ggc.data.meter.MeterManager;
import ggc.meter.data.DailyValuesRow;
import ggc.meter.data.GlucoTable;
import ggc.meter.data.GlucoTableModel;
import ggc.meter.data.GlucoValues;
import ggc.util.DataAccess;
import ggc.util.I18nControl;
import gnu.io.NoSuchPortException;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
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
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;


/**
 * @author stephan
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * 
 * 
 * Andy:
 *  - changed this to use gnu.io.*;
 *  - uses meter manager for managing meters
 * 
 */
public class ReadMeterDialog extends JDialog implements ActionListener
{

	static final long serialVersionUID = 0;

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

    private JTabbedPane tabPane;
    private SerialMeterImport meterImport = null;
    private StartImportAction startImportAction = new StartImportAction();

    /**
     * Constructor for ReadMeterDialog.
     * @param owner
     * @throws HeadlessException
     */
    public ReadMeterDialog(JFrame owner)
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


        logText = new JTextArea(m_ic.getMessage("LOG__")+":\n", 8, 35);
        logText.setAutoscrolls(true);
        JScrollPane sp = new JScrollPane(logText, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        resTable = new GlucoTable();

//        JScrollPane sp2 = new JScrollPane(resTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        tabPane = new JTabbedPane();
        //tabPane.add("Values", sp2);
        tabPane.add(m_ic.getMessage("VALUES"), GlucoTable.createGlucoTable(model));
        tabPane.add(m_ic.getMessage("LOG"), sp);

        progress = new JProgressBar(0, 100);
        progress.setPreferredSize(new Dimension(100, 8));


	//AbstractAction start = new StartImportAction();

	AbstractAction start =new StartImportAction();
	startButton = new JButton(start);
	//    m_ic.getMessage("START")); // startImportAction
        //startButton.setActionCommand("start");
        saveButton = createButton("ME_SAVE", "save");
	    //new JButton(m_ic.getMessage("SAVE"));  // new SaveAction()
        //saveButton.setActionCommand("save");
        saveButton.setEnabled(false);

        JPanel importButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        importButtonPanel.add(startButton);
        importButtonPanel.add(saveButton);

        JPanel dialogButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

	JButton closeButton = createButton("ME_CLOSE", "close");
	    //new JButton(m_ic.getMessage("CLOSE"));  // new CloseAction()
	
        //closeButton.setActionCommand("save");
        dialogButtonPanel.add(closeButton);

        JPanel buttonPanel = new JPanel(new BorderLayout(0, 0));
        buttonPanel.add(importButtonPanel, "West");
        buttonPanel.add(dialogButtonPanel, "East");

        JPanel infoPanel = new JPanel(new BorderLayout());
        //JLabel infoIcon = new JLabel(new ImageIcon(getClass().getResource("/icons/euroflash.png")));
        //JLabel infoIcon = new JLabel(new ImageIcon(getClass().getResource("/icons/freestyle.png")));
        infoIcon = new JLabel();
        infoDescription = new JLabel();
        infoDescription.setVerticalAlignment(SwingConstants.TOP);
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
        	static final long serialVersionUID = 0;

            @Override
            public Insets getInsets()
            {
                return new Insets(8, 8, 8, 8);
            }

            @Override
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
    }


    public void initMeter()
    {

	int mtype = m_da.getMeterManager().getSelectedMeterIndex(MeterManager.METER_INTERFACE_1, m_da.getSettings().getMeterType());

	System.out.println(mtype);


//        String meterClassName = m_da.getMeterManager().meter_classes[m_da.getSettings().getMeterType()];
	String meterClassName = m_da.getMeterManager().meter_classes[mtype];


        if (meterClassName == null || meterClassName.equals(""))
            throw new NullPointerException(m_ic.getMessage("NO_CLASS_FOR_METER_DEFINED"));

        try 
        {
            meterImport = (SerialMeterImport)Class.forName(meterClassName).newInstance();
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
        resTable.setModel(model);


        infoIcon.setIcon((meterImport.getImage() == null)
                                     ? new ImageIcon(getClass().getResource("/icons/m_noMeter.gif"))
                                     : meterImport.getImage());

        this.infoDescription.setText((meterImport.getUseInfoMessage() == null) ? m_ic.getMessage("NO_TO_USE_INFORMATION") : meterImport.getUseInfoMessage());
        
    }
    


    protected JButton createButton(String command_name, String action_command)
    {

	JButton b = new JButton(m_ic.getMessageWithoutMnemonic(command_name));
	b.setMnemonic(m_ic.getMnemonic(command_name));
	b.setToolTipText(m_ic.getMessage(command_name + "_DESC"));
	b.setActionCommand(action_command);
	b.addActionListener(this);

	return b;
    }



    //////////////////////////////////////////////////////////////
    // Action classes
    protected class SaveAction extends AbstractAction
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
    	
    	static final long serialVersionUID = 0;
    	
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
    	static final long serialVersionUID = 0;

        public StartImportAction()
        {
            super();
            putValue(Action.NAME, m_ic.getMessageWithoutMnemonic("ME_IMPORT"));
            
            char ch = m_ic.getMnemonic("ME_IMPORT");

	    

            if (ch!='0') 
	    {
		//Character c = new Character(ch);
		int i = (int)ch;
		//putValue(Action.MNEMONIC_KEY, ""+ch);
		putValue(Action.MNEMONIC_KEY, new Integer(i));
	    }

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

        if (action.equals("close")) 
        {
	    meterImport.stopImport();
	    meterImport.removeImportEventListener(startImportAction);
	    meterImport.close();
	    this.dispose();
        }
	else if (action.equals("save"))
	{
	    glucoValues.saveValues();
	}
        else
	    
            System.out.println("ReadMeterDialog::Unknown command: " + action);

    }





}