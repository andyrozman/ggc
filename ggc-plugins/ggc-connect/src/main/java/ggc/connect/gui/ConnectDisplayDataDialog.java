package ggc.connect.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.*;

import com.atech.utils.ATSwingUtils;

import ggc.connect.data.ConnectHandlerParameters;
import ggc.connect.data.retrieval.ConnectDataRetriever;
import ggc.connect.defs.ConnectHandler;
import ggc.connect.util.DataAccessConnect;
import ggc.plugin.data.DeviceValuesEntryInterface;
import ggc.plugin.data.DeviceValuesTable;
import ggc.plugin.gui.DeviceDisplayDataDialog;
import ggc.plugin.gui.DeviceExportDialog;
import ggc.plugin.output.AbstractOutputWriter;
import ggc.plugin.output.ErrorMessageDto;
import ggc.plugin.output.OutputWriterConfigData;
import ggc.plugin.output.OutputWriterData;

/**
 * Application: GGC - GNU Gluco Control 
 * Plug-in: GGC PlugIn Base (base class for all plugins)
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
 * Filename: DeviceDisplayDataDialog
 * Description: This is dialog for displaying data as it's been downloaded.
 * 
 * Author: Andy {andy@atech-software.com}
 */
@Deprecated
public class ConnectDisplayDataDialog extends DeviceDisplayDataDialog
{

    private static final long serialVersionUID = 3365114423740706212L;

    // private DataAccessPlugInBase dataAccess;
    // I18nControlAbstract i18nControl;

    // public JProgressBar progress = null;
    // private JProgressBar progressOld = null;

    // private DeviceValuesTableModel model = null;
    // private DeviceValuesTable table = null;

    // private JButton btnClose, btnImport, btnBreak;
    // private JTabbedPane tabPane;

    // JLabel lblStatus, lblComment;
    // JTextArea taInfo = null;
    // JTextArea taLogText = null;
    // JButton btnHelp;
    // DeviceDataHandler deviceDataHandler;
    // JFrame m_parent = null;
    // JDialog m_parent_d = null;

    // boolean indeterminate = false;

    // new
    ConnectHandlerParameters parameters;
    ConnectHandler connectHandler;
    ConnectDataRetriever deviceReaderRunner;


    /**
     * Constructor
     *
     * @param parent
     * @param parameters
     * @param connectHandler
     */
    public ConnectDisplayDataDialog(JFrame parent, ConnectHandlerParameters parameters, ConnectHandler connectHandler)
    {
        super(parent, DataAccessConnect.getInstance(), null, false);

        this.parameters = parameters;
        this.connectHandler = connectHandler;
        // this.deviceDataHandler = dataAccess.getDeviceDataHandler();

        initReader();

        dialogPreInit();
    }


    protected void initReader()
    {
        this.deviceDataHandler = dataAccess.getDeviceDataHandler();
        this.deviceReaderRunner = new ConnectDataRetriever(this, this.parameters, this.connectHandler);
    }


    protected void startReader()
    {
        addLogText(i18nControl.getMessage("READ_DEVICE_DATA") + " - "
                + //
                i18nControl.getMessage(this.parameters.getHandlerConfiguration().getName())
                + //
                "\n    " + i18nControl.getMessage("CFG_BASE_FROM").toLowerCase() + " "
                + this.parameters.getHandlerTarget());

        if (this.deviceReaderRunner != null)
        {
            this.deviceReaderRunner.start();
        }
    }


    protected void setTitle()
    {
        setTitle(String.format(i18nControl.getMessage("READ_DEVICE_DATA_TITLE"),
            i18nControl.getMessage(this.parameters.getHandlerConfiguration().getName())));
    }


    // FIXME this one might be the same (as DeviceDisplayDataDialog)
    protected void init()
    {
        ATSwingUtils.initLibrary();

        model = this.deviceDataHandler.getDeviceValuesTableModel();
        model.clearData();

        // System.out.println("Model: " + model);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setSize(700, 600);

        JLabel label;

        int wide_add = 0;

        // FIXME
        // if (dataAccess.isDataDownloadScreenWide())
        {
            wide_add = 200;
        }

        Font normal = ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL);
        Font normal_b = ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL_BOLD);

        setBounds(0, 0, 480 + wide_add, 660);

        dataAccess.centerJDialog(this);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().add(panel, BorderLayout.CENTER);

        // TabControl with two tabs: log and data

        taLogText = new JTextArea(i18nControl.getMessage("LOG__") + ":\n", 8, 35);
        taLogText.setAutoscrolls(true);
        JScrollPane sp = new JScrollPane(taLogText, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        addLogText(i18nControl.getMessage("LOG_IS_CURRENTLY_NOT_IMPLEMENTED"));

        this.table = new DeviceValuesTable(dataAccess, model);
        // this.table.a

        tabPane = new JTabbedPane();
        tabPane.add(i18nControl.getMessage("DATA"), this.createTablePanel(this.table));
        tabPane.add(i18nControl.getMessage("LOG"), sp);
        tabPane.setBounds(30, 15, 410 + wide_add, 250); // 410
        panel.add(tabPane);

        // Info
        label = new JLabel(String.format(i18nControl.getMessage("DEVICE_INFO"),
            i18nControl.getMessage("DEVICE_NAME_BIG"))
                + ":");
        label.setBounds(30, 320, 280, 25);
        label.setFont(normal_b);
        panel.add(label);

        taInfo = new JTextArea();

        JScrollPane sp3 = new JScrollPane(taInfo);
        sp3.setBounds(30, 345, 410 + wide_add, 70);
        panel.add(sp3);

        taInfo.setText("");

        lblComment = new JLabel("");
        lblComment.setBounds(40, 270, 305 + wide_add, 45);
        lblComment.setFont(normal);
        panel.add(lblComment);

        ATSwingUtils.getIconButton(355 + wide_add, 295, 40, 40, //
                i18nControl.getMessage("GET_DL_INFO"), "document_info.png", 32, 32, //
                "show_info", this, panel, dataAccess);

        btnErrors = ATSwingUtils.getIconButton(400 + wide_add, 295, 40, 40, //
                i18nControl.getMessage("GET_DL_ERRORS"), "document_warning.png", 31, 31, //
                "show_errors", this, panel, dataAccess);

        // reading old data
        label = new JLabel(i18nControl.getMessage("READING_OLD_DATA") + ":");
        label.setBounds(30, 425, 250, 25); // 420
        label.setFont(normal_b);
        panel.add(label);

        this.progressOld = new JProgressBar();
        this.progressOld.setBounds(30, 450, 410 + wide_add, 20); // 450
        this.progressOld.setStringPainted(true);
        // this.progress.setIndeterminate(true);
        panel.add(this.progressOld);

        // device status
        label = new JLabel(i18nControl.getMessage("ACTION") + ":");
        label.setBounds(30, 490, 100, 25); // 420
        label.setFont(normal_b);
        panel.add(label);

        lblStatus = new JLabel(i18nControl.getMessage("READY"));
        lblStatus.setBounds(110, 490, 330, 25); // 420
        // lblStatus.setBorder(new LineBorder(Color.red));
        lblStatus.setFont(normal);
        panel.add(lblStatus);

        this.progress = new JProgressBar();
        this.progress.setBounds(30, 520, 410 + wide_add, 20); // 450
        this.progress.setStringPainted(true);
        // this.progress.setIndeterminate(true);
        panel.add(this.progress);

        btnBreak = new JButton(i18nControl.getMessage("BREAK_COMMUNICATION"));
        btnBreak.setBounds(150 + wide_add, 565, 170, 40);
        btnBreak.setActionCommand("break_communication");
        btnBreak.addActionListener(this);
        panel.add(btnBreak);

        btnHelp = ATSwingUtils.createHelpButtonByBounds(30, 565, 110, 40, this, ATSwingUtils.FONT_NORMAL, dataAccess);
        panel.add(btnHelp);

        btnClose = new JButton(i18nControl.getMessage("CLOSE"));
        btnClose.setBounds(330 + wide_add, 565, 110, 40);
        btnClose.setEnabled(false);
        btnClose.setFont(normal);
        btnClose.setActionCommand("close");
        btnClose.addActionListener(this);
        panel.add(btnClose);

        dataAccess.enableHelp(this);

    }


    // JComboBox filter_combo;
    // JButton sel_all, unsel_all;

    // protected JPanel createTablePanel(DeviceValuesTable table_in)
    // {
    //
    // JScrollPane scroller = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
    // ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    //
    // // final AddRowAction addRowAction = new AddRowAction(table);
    // // final DeleteRowAction deleteRowAction = new DeleteRowAction(table);
    //
    // JToolBar toolBar = new JToolBar();
    // toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
    // toolBar.setFloatable(false);
    //
    // toolBar.add(new JLabel(i18nControl.getMessage("FILTER") + ":   "));
    // toolBar.add(filter_combo = new JComboBox(this.dataAccess.getFilteringStates()));
    // toolBar.add(new JLabel("   "));
    // toolBar.add(sel_all = this.createButton("select_all", i18nControl.getMessage("SELECT_ALL"),
    // "element_selection.png"));
    // toolBar.add(new JLabel(" "));
    // toolBar.add(unsel_all = this.createButton("deselect_all",
    // i18nControl.getMessage("DESELECT_ALL"),
    // "element_selection_delete.png"));
    //
    // filter_combo.setSelectedIndex(ConnectDisplayDataDialog.FILTER_NEW_CHANGED);
    // filter_combo.setEnabled(false);
    //
    // sel_all.setEnabled(false);
    // unsel_all.setEnabled(false);
    //
    // filter_combo.addItemListener(new ItemListener()
    // {
    //
    // /**
    // * itemStateChanged
    // */
    // public void itemStateChanged(ItemEvent ev)
    // {
    // model.setFilter(filter_combo.getSelectedIndex());
    // }
    //
    // });
    //
    // JPanel container = new JPanel(new BorderLayout());
    // container.add(toolBar, "North");
    // container.add(scroller, "Center");
    //
    // return container;
    //
    // }

    private JButton createButton(String command_text, String tooltip, String image_d)
    {
        JButton b = new JButton();
        b.setIcon(ATSwingUtils.getImageIcon(image_d, 15, 15, this, dataAccess));
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
            dataAccess.removeComponent(this);
            this.dispose();
        }
        else if (action.equals("select_all"))
        {
            this.model.selectAll();
        }
        else if (action.equals("deselect_all"))
        {
            this.model.deselectAll();
        }
        else if (action.equals("export_data"))
        {
            // Hashtable<String,ArrayList<?>> ht =
            // this.model.getCheckedEntries();

            DeviceExportDialog med = new DeviceExportDialog(dataAccess, this, this.deviceDataHandler);

            if (med.wasAction())
            {
                this.btnImport.setEnabled(false);
            }

        }
        else if (action.equals("show_info"))
        {

        }
        else if (action.equals("show_errors"))
        {

        }else
        {
            System.out.println("DeviceDisplayDataDialog::Unknown command: " + action);
        }

    }


    /**
     * endOutput
     */
    public void endOutput()
    {
        // System.out.println("endOutput()");
        if (this.indeterminate)
        {
            this.progress.setIndeterminate(false);
            this.progress.setStringPainted(true);
            this.progress.setValue(50);
            this.progress.setValue(100);
            this.progress.setString("100 %");
            this.progress.repaint();
        }
    }






    /**
     * writeDeviceIdentification
     */
    public void writeDeviceIdentification()
    {
        // FIXME
        // if (this.deviceIdentification.company == null)
        // {
        // this.deviceIdentification.company = dataAccess.getCompanyNameForSelectedDevice();
        // }
        //
        // if (this.deviceIdentification.device_selected == null)
        // {
        // this.deviceIdentification.device_selected = dataAccess.getDeviceNameForSelectedDevice();
        // }
        //
        // this.taInfo.setText(this.deviceIdentification.getShortInformation());
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



//    /**
//     * Set GUI Status
//     *
//     * @param status
//     */
//    public void setGUIStatus(int status)
//    {
//
//        currentStatus = status;
//
//        if (this.subStatus == null || this.subStatus.length() == 0)
//        {
//            this.lblStatus.setText(this.dataAccess.getReadingStatuses()[status]);
//        }
//        else
//        {
//            this.lblStatus.setText(this.dataAccess.getReadingStatuses()[status] + " - "
//                    + i18nControl.getMessage(this.subStatus));
//        }
//
//        switch (status)
//        {
//
//            case AbstractOutputWriter.STATUS_DOWNLOADING: // downloading
//                {
//                    this.btnBreak.setEnabled(true);
//                    this.btnClose.setEnabled(false);
//                    this.btnImport.setEnabled(false);
//                }
//                break;
//
//            case AbstractOutputWriter.STATUS_DOWNLOAD_FINISHED: // finished
//                {
//                    this.btnBreak.setEnabled(false);
//                    this.btnClose.setEnabled(true);
//                    this.btnImport.setEnabled(true);
//                    filter_combo.setEnabled(true);
//                    sel_all.setEnabled(true);
//                    unsel_all.setEnabled(true);
//                    // this.progress.setIndeterminate(false);
//
//                }
//                break;
//
//            case AbstractOutputWriter.STATUS_READER_ERROR: // error
//                {
//                    this.btnBreak.setEnabled(false);
//                    this.btnClose.setEnabled(true);
//                    this.btnImport.setEnabled(false);
//                    filter_combo.setEnabled(false);
//                    sel_all.setEnabled(false);
//                    unsel_all.setEnabled(false);
//                }
//                break;
//
//            case AbstractOutputWriter.STATUS_STOPPED_DEVICE: // stopped - device
//            case AbstractOutputWriter.STATUS_STOPPED_USER: // stoped - user
//                {
//                    this.btnBreak.setEnabled(false);
//                    this.btnClose.setEnabled(true);
//                    this.btnImport.setEnabled(false);
//                }
//                break;
//
//            case AbstractOutputWriter.STATUS_READY: // ready
//                // case 0: // none
//            default:
//                {
//                    this.btnBreak.setEnabled(false);
//                    this.btnClose.setEnabled(false);
//                    this.btnImport.setEnabled(false);
//                }
//                break;
//        }
//
//    }


    /**
     * Set Device Comment
     * 
     * @param text
     */
    public void setDeviceComment(String text)
    {
        this.lblComment.setText(i18nControl.getMessage(text));
    }


    /**
     * Write Data to OutputWriter
     * 
     * @param data
     */
    public void writeData(OutputWriterData data)
    {
        count++;
        this.model.addEntry((DeviceValuesEntryInterface) data);
    }


    public void writeConfigurationData(OutputWriterConfigData configData)
    {
    }




    // ****************************************************************
    // ****** HelpCapable Implementation *****
    // ****************************************************************




    /**
     * getHelpId - get id for Help
     */
    public String getHelpId()
    {
        // return dataAccess.getDeviceConfigurationDefinition().getHelpPrefix()
        // +
        // "Reading_View";
        return "DeviceTool_Reading_View";
    }




    public void addErrorMessage(ErrorMessageDto msg)
    {
        // TODO Auto-generated method stub

    }


    public int getErrorMessageCount()
    {
        // TODO Auto-generated method stub
        return 0;
    }


    public List<ErrorMessageDto> getErrorMessages()
    {
        // TODO Auto-generated method stub
        return null;
    }


    public void setSpecialNote(int noteType, String note)
    {
        System.out.println("Special Note [" + noteType + "]: " + note);
    }

}
