/*
 * Created on 12.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */

package gui;


import data.imports.ImportException;
import data.imports.SerialMeterImport;
import datamodels.DailyValuesRow;
import datamodels.GlucoTableModel;
import datamodels.GlucoValues;
import event.ImportEvent;
import event.ImportEventListener;
import util.GGCProperties;

import javax.comm.NoSuchPortException;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * @author stephan
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public class ReadMeterDialog extends JDialog
{

    private static ReadMeterDialog singleton = null;

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
    private ReadMeterDialog(Frame owner)
    {
        super(owner);

        setTitle("Read Meter Data");
        initialize();
    }

    /**
     * Method getInstance.
     * @return ReadMeterDialog
     */
    public static ReadMeterDialog getInstance()
    {
        return singleton;
    }

    /**
     * Method showMe.
     * @param owner
     */
    public static void showMe(Frame owner)
    {
        if (singleton == null)
            singleton = new ReadMeterDialog(owner);
        singleton.setLocationRelativeTo(owner);
        singleton.show();
    }

    /**
     * Method close.
     */
    public void close()
    {
        dispose();
    }

    /**
     * Method close.
     */
    public void dispose()
    {
        singleton = null;
        super.dispose();
    }

    public void addLogText(String s)
    {
        logText.append(s + "\n");
    }

    public GlucoValues getGlucoValues()
    {
        return glucoValues;
    }

    protected void initialize()
    {

        String meterClassName = SerialMeterImport.getMeterClassName(GGCProperties.getInstance().getMeterType());
        if (meterClassName == null || meterClassName.equals(""))
            throw new NullPointerException("No class for meter definiened.");

        try {
            meterImport = (SerialMeterImport)Class.forName(meterClassName).newInstance();
        } catch (Exception exc) {
            System.out.println(exc);
        }

        //meterImport = new EuroFlashImport();
        //meterImport = new FreeStyleImport();
        //meterImport = new GlucoCardImport();

        meterImport.addImportEventListener(startImportAction);

        this.addWindowListener(new CloseListener());

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

        logText = new JTextArea("log:\n", 8, 35);
        logText.setAutoscrolls(true);
        JScrollPane sp = new JScrollPane(logText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        resTable = new GlucoTable(model);

        JScrollPane sp2 = new JScrollPane(resTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        tabPane = new JTabbedPane();
        //tabPane.add("Values", sp2);
        tabPane.add("Values", GlucoTable.createGlucoTable(model));
        tabPane.add("Log", sp);

        progress = new JProgressBar(0, 100);
        progress.setPreferredSize(new Dimension(100, 8));

        startButton = new JButton(startImportAction);
        saveButton = new JButton(new SaveAction());
        saveButton.setEnabled(false);

        JPanel importButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        importButtonPanel.add(startButton);
        importButtonPanel.add(saveButton);

        JPanel dialogButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton(new CloseAction());
        dialogButtonPanel.add(closeButton);

        JPanel buttonPanel = new JPanel(new BorderLayout(0, 0));
        buttonPanel.add(importButtonPanel, "West");
        buttonPanel.add(dialogButtonPanel, "East");

        JPanel infoPanel = new JPanel(new BorderLayout());
        //JLabel infoIcon = new JLabel(new ImageIcon(getClass().getResource("/icons/euroflash.png")));
        //JLabel infoIcon = new JLabel(new ImageIcon(getClass().getResource("/icons/freestyle.png")));
        JLabel infoIcon = new JLabel((meterImport.getImage() == null)
                                     ? new ImageIcon(getClass().getResource("/icons/noMeter.png"))
                                     : meterImport.getImage());
        JLabel infoDescription = new JLabel((meterImport.getUseInfoMessage() == null) ? "No to use information" : meterImport.getUseInfoMessage());
        infoDescription.setVerticalAlignment(JLabel.TOP);
        infoPanel.add(infoIcon, "North");
        infoPanel.add(infoDescription, "Center");
        infoPanel.setBorder(new TitledBorder(meterImport.getName()));
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
    }

    private class CloseListener extends WindowAdapter
    {
        public void windowClosing(WindowEvent e)
        {
            close();
        }
    }


    //////////////////////////////////////////////////////////////
    // Action classes
    protected class SaveAction extends AbstractAction
    {
        public SaveAction()
        {
            super();
            putValue(Action.NAME, "Save");
            putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_S));
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
            super("Close");
            putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_C));
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
         */
        public void actionPerformed(ActionEvent e)
        {
            meterImport.stopImport();
            meterImport.removeImportEventListener(startImportAction);
            meterImport.close();
            ReadMeterDialog.this.close();
        }

    }

    protected class StartImportAction extends AbstractAction implements ImportEventListener
    {

        public StartImportAction()
        {
            super();
            putValue(Action.NAME, "Import");
            putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_I));
        }

        public void actionPerformed(ActionEvent e)
        {

            tabPane.setSelectedIndex(1);

            try {
                meterImport.setPort(GGCProperties.getInstance().getMeterPort());

                progress.setIndeterminate(true);
                meterImport.open();

                meterImport.importData();
            } catch (NoSuchPortException exc) {
                progress.setIndeterminate(false);
                addLogText("no such COM-Port found.");
            } catch (ImportException exc) {
                progress.setIndeterminate(false);
                addLogText("Exception while import:");
                addLogText(exc.getMessage());
            }

        }

        /**
         * @see data.imports.ImportEventListener#importChanged(ImportEvent)
         */
        public void importChanged(ImportEvent event)
        {
            switch (event.getType()) {
                case ImportEvent.PROGRESS:
                    progress.setIndeterminate(false);
                    progress.setValue(event.getProgress());
                    break;

                case ImportEvent.PORT_OPENED:
                    addLogText("Port to meter opened");
                    break;

                case ImportEvent.PORT_CLOSED:
                    addLogText("Port to meter closed");
                    break;

                case ImportEvent.IMPORT_FINISHED:
                    meterImport.close();
                    DailyValuesRow[] data = meterImport.getImportedData();
                    addLogText("Had read values from meter: " + data.length);
                    for (int i = 0; i < data.length; i++) {
                        DailyValuesRow dailyValuesRow = data[i];
                        getGlucoValues().setNewRow(dailyValuesRow);
                    }

                    progress.setIndeterminate(false);
                    tabPane.setSelectedIndex(0);
                    break;

                case ImportEvent.TIMEOUT:
                    progress.setIndeterminate(false);
                    meterImport.close();
                    addLogText("Timeout: No data was sended from meter.\n" + "Please check the cable!!");
                    break;
            }
        }

    }
}
