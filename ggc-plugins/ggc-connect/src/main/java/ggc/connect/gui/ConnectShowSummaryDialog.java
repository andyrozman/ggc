package ggc.connect.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.graphics.components.jlist.CheckBoxList;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;

import ggc.connect.data.ConnectHandlerParameters;
import ggc.connect.data.retrieval.SummaryDataRetriever;
import ggc.connect.defs.ConnectHandler;
import ggc.connect.enums.ConnectDataType;
import ggc.connect.enums.ConnectHandlerConfiguration;
import ggc.connect.enums.ConnectOperationType;
import ggc.connect.util.DataAccessConnect;
import ggc.plugin.data.enums.DeviceHandlerType;
import ggc.plugin.device.mgr.DeviceHandlerManager;
import ggc.plugin.util.DataAccessPlugInBase;

// button of unsupported_items - show dialog
// error on reading - button - show dialog

public class ConnectShowSummaryDialog extends JDialog implements ActionListener, ItemListener
{

    private static final long serialVersionUID = -4620972246237384499L;

    private static final Logger LOG = LoggerFactory.getLogger(ConnectShowSummaryDialog.class);

    DataAccessPlugInBase dataAccess = null;
    I18nControlAbstract i18nControl = null;
    JDialog parentDialog = null;
    JFrame parentFrame;

    DeviceHandlerType deviceHandlerType;
    ConnectHandler connectHandler;
    ConnectHandlerConfiguration configuration;

    int posY = 0;
    private JButton helpButton, cancelButton, previousButton, nextButton;
    private JComboBox actionCombo;
    private CheckBoxList configurationItemsJList;
    private CheckBoxList importItemsJList;

    ConnectHandlerParameters parameters;
    Map<ConnectDataType, List<String>> dataRetrieved;
    private JLabel loadingLabel, errorLabel;

    boolean errorOnSummaryRetrieval = true;
    private JButton errorButton;
    private JPanel panelActionSelector;


    /**
     * Constructor
     *
     */
    public ConnectShowSummaryDialog(JFrame parent, DeviceHandlerType deviceHandlerType,
            ConnectHandlerConfiguration configuration, ConnectHandlerParameters parameters)
    {
        // FIXME remove
        super(parent, "", true);
        dataAccess = DataAccessConnect.getInstance();
        i18nControl = dataAccess.getI18nControlInstance();
        this.parentFrame = parent;
        // this.previous = previous_panel;
        // this.m_frc = frc;

        this.deviceHandlerType = deviceHandlerType;
        this.connectHandler = (ConnectHandler) DeviceHandlerManager.getInstance()
                .getDeviceHandler(this.deviceHandlerType);
        this.connectHandler.checkIfDataAccessSet();
        this.configuration = configuration;

        this.parameters = parameters;

        // init();

        doInit();
    }


    public ConnectShowSummaryDialog(JFrame parent, ConnectHandlerParameters parameters)
    {
        super(parent, "", true);
        dataAccess = DataAccessConnect.getInstance();
        i18nControl = dataAccess.getI18nControlInstance();
        this.parentFrame = parent;
        // this.previous = previous_panel;
        // this.m_frc = frc;

        this.deviceHandlerType = parameters.getDeviceHandlerType();
        this.connectHandler = (ConnectHandler) DeviceHandlerManager.getInstance()
                .getDeviceHandler(this.deviceHandlerType);
        this.connectHandler.checkIfDataAccessSet();
        this.configuration = parameters.getHandlerConfiguration();

        this.parameters = parameters;

        doInit();
    }


    public void doInit()
    {
        init();
        // setSize(getSize());
        ATSwingUtils.centerJDialog(this, this.parentDialog != null ? this.parentDialog : this.parentFrame);

        initDataRetriever();

        this.setVisible(true);
    }


    private void initDataRetriever()
    {
        SummaryDataRetriever dataRetriever = new SummaryDataRetriever(this, this.parameters, this.connectHandler);

        new Thread(dataRetriever).start();
    }


    public void init()
    {
        ATSwingUtils.initLibrary();
        this.setLayout(null);
        dataAccess.addComponent(this);

        this.setTitle(i18nControl.getMessage(configuration.getName()) + //
                " - " + i18nControl.getMessage("SHOW_SUMMARY") + //
                " - " + i18nControl.getMessage("STEP") + " 2");

        ImageIcon imageIcon = ATSwingUtils.getImageIcon(this.configuration.getIconName(), this, dataAccess);

        posY += 10 + imageIcon.getIconHeight() + 30;

        JPanel panelLogo = new JPanel();
        panelLogo.setBounds(10, 10, 520, imageIcon.getIconHeight() + 20);
        panelLogo.setLayout(new BorderLayout());

        JLabel label = new JLabel(imageIcon);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        panelLogo.add(label);

        this.add(panelLogo);

        panelActionSelector = new JPanel();
        panelActionSelector.setBorder(new TitledBorder(i18nControl.getMessage("SELECT_ACTION")));
        panelActionSelector.setBounds(10, posY, 520, 65);
        panelActionSelector.setLayout(null);

        actionCombo = ATSwingUtils.getComboBox(configuration.getOperationType().getObjectsWithNone(), 40, 25, 300, 25,
            panelActionSelector, ATSwingUtils.FONT_NORMAL);
        actionCombo.addItemListener(this);
        actionCombo.setVisible(false);

        loadingLabel = ATSwingUtils.getLabel(i18nControl.getMessage("LOADING_SUMMARY_DATA"), 60, 25, 300, 25,
            panelActionSelector, ATSwingUtils.FONT_NORMAL_BOLD);
        loadingLabel.setVisible(true);

        errorLabel = ATSwingUtils.getLabel(i18nControl.getMessage("ERROR_LOADING_SUMMARY_LBL"), 60, 25, 300, 25,
            panelActionSelector, ATSwingUtils.FONT_NORMAL_BOLD);
        errorLabel.setForeground(Color.red);
        errorLabel.setVisible(false);

        int[] size = new int[] { 33, 33 };

        errorButton = ATSwingUtils.getButton("", 450, 14, 42, 42, this, ATSwingUtils.FONT_NORMAL, "information.png",
            "error", this, dataAccess, size);
        // panelActionSelector.add(errorButton);

        this.add(panelActionSelector);

        posY += 65;

        JPanel panelConfigurationItems = new JPanel();
        panelConfigurationItems.setBorder(new TitledBorder(i18nControl.getMessage("CONFIGURATION_ITEMS")));
        panelConfigurationItems.setBounds(10, posY, 260, 120);
        panelConfigurationItems.setLayout(null);
        this.add(panelConfigurationItems);

        configurationItemsJList = new CheckBoxList();
        configurationItemsJList.addItemListener(this);
        JScrollPane scrollPane = new JScrollPane(configurationItemsJList);
        scrollPane.setBounds(15, 20, 230, 90);
        panelConfigurationItems.add(scrollPane);

        JPanel panelImportItems = new JPanel();
        panelImportItems.setBorder(new TitledBorder(i18nControl.getMessage("IMPORT_ITEMS")));
        panelImportItems.setBounds(270, posY, 260, 120);
        panelImportItems.setLayout(null);
        this.add(panelImportItems);

        importItemsJList = new CheckBoxList();
        importItemsJList.addItemListener(this);
        scrollPane = new JScrollPane(importItemsJList);
        scrollPane.setBounds(15, 20, 230, 90);
        panelImportItems.add(scrollPane);

        posY += 130;

        this.helpButton = ATSwingUtils.createHelpIconByBounds(30, posY, 50, 40, this, ATSwingUtils.FONT_NORMAL,
            dataAccess);
        this.add(helpButton);

        cancelButton = ATSwingUtils.getButton(" " + i18nControl.getMessage("CANCEL"), 90, posY, 120, 40, this,
            ATSwingUtils.FONT_NORMAL, "cancel.png", "cancel", this, dataAccess, size);

        previousButton = ATSwingUtils.getButton("", 355, posY, 70, 40, this, ATSwingUtils.FONT_NORMAL,
            "arrow_left_blue.png", "previous", this, dataAccess, size);

        nextButton = ATSwingUtils.getButton("", 440, posY, 70, 40, this, ATSwingUtils.FONT_NORMAL,
            "arrow_right_blue.png", "next", this, dataAccess, size); // nav_right_blue.png
        nextButton.setEnabled(false);

        this.setBounds(0, 0, 550, (posY + 85));

        // FIXME
        // dataAccess.enableHelp(this);

    }


    public void setDataRetrieved(Map<ConnectDataType, List<String>> data)
    {
        this.dataRetrieved = data;

        if (data != null)
        {
            loadingLabel.setVisible(false);
            actionCombo.setVisible(true);
            errorOnSummaryRetrieval = false;

            populateData();
        }
        else
        {
            loadingLabel.setVisible(false);
            panelActionSelector.add(errorButton);
            this.errorLabel.setVisible(true);
        }
    }


    /**
     * Populates CheckBoxList component
     */
    public void populateData()
    {
        if (this.dataRetrieved.containsKey(ConnectDataType.ConfigurationItems))
        {
            this.configurationItemsJList.addCheckBoxTexts(this.dataRetrieved.get(ConnectDataType.ConfigurationItems));
        }

        if (this.dataRetrieved.containsKey(ConnectDataType.DataItems))
        {
            this.importItemsJList.addCheckBoxTexts(this.dataRetrieved.get(ConnectDataType.DataItems));
        }

        if (this.dataRetrieved.containsKey(ConnectDataType.UnsupportedItems))
        {
            String text = StringUtils.join(this.dataRetrieved.get(ConnectDataType.UnsupportedItems), ", ");

            String textWidth = ATSwingUtils.createHtmlTextWithWidth(text, 410, 15, 15);

            JLabel label = new JLabel(textWidth);
            label.setBorder(new TitledBorder("  " + i18nControl.getMessage("UNSUPPORTED_ITEMS") + "  "));
            label.setVerticalAlignment(JLabel.TOP);

            Dimension dimension = ATSwingUtils.calculateJLabelSizeWithText(label, textWidth);

            int labelHeight = Math.max(72, (int) dimension.getHeight());

            label.setBounds(10, posY - 5, 520, labelHeight);
            this.add(label);

            int y = labelHeight + 10;

            errorButton.setBounds(470, posY + 15, 40, 40);
            this.add(errorButton);

            setButtonLocation(cancelButton, posY + y);
            setButtonLocation(nextButton, posY + y);
            setButtonLocation(previousButton, posY + y);
            setButtonLocation(helpButton, posY + y);

            int possY = this.getY() - (int) (y / 2.0f);

            if (possY < 0)
                possY = 0;

            this.setBounds(this.getX(), possY, this.getWidth(), this.getHeight() + y);
        }

    }


    public void setButtonLocation(JButton button, int location)
    {
        Rectangle bnds = button.getBounds();
        bnds.y = location;

        button.setBounds(bnds);

    }


    public void dispose()
    {
        super.dispose();
        dataAccess.removeComponent(this);
    }


    public void actionPerformed(ActionEvent ae)
    {
        String action = ae.getActionCommand();

        if (action.equals("cancel"))
        {
            this.dispose();
        }
        else if (action.equals("previous"))
        {
            this.dispose();

            new ConnectSelectImportMethodDialog(this.parentFrame, deviceHandlerType, configuration, parameters);
        }
        else if (action.equals("error"))
        {
            ATSwingUtils.showErrorDialog(this, //
                this.errorOnSummaryRetrieval ? "ERROR_SUMMARY_RETRIEVAL" : "ERROR_DATA_NOT_SUPPORTED", //
                i18nControl);
        }
        else if (action.equals("next"))
        {
            setSelectionValues();

            this.dispose();

            if (this.parameters.getActionType() == ConnectOperationType.ViewConfiguration)
            {
                new ConnectDisplayConfigDialog(this.parentFrame, this.parameters, this.connectHandler);
            }
            else
            {
                new ConnectDisplayDataDialog(this.parentFrame, this.parameters, this.connectHandler);
            }

            LOG.warn("Next not implemented.");
            // if (this.deviceDataHandler.selected_file_context.hasSpecialSelectorDialog())
            // {
            // this.deviceDataHandler.selected_file_context.goToNextDialog(this);
            // }
            // else
            // {
            // this.dispose();
            // dataAccess.removeComponent(this);
            //
            // if (downloadSupportType == DownloadSupportType.DownloadDataFile)
            // {
            // new DeviceDisplayDataDialog(dataAccess.getMainParent(), dataAccess,
            // deviceDataHandler);
            // }
            // else
            // {
            // new DeviceDisplayConfigDialog(dataAccess.getMainParent(), dataAccess,
            // deviceDataHandler);
            // }
            // }
        }
        // nn
    }


    // FIXME
    public String getHelpId()
    {
        return "DeviceTool_File_Import";
    }


    @Override
    public Dimension getSize()
    {
        return new Dimension(550, posY);
    }


    public void itemStateChanged(ItemEvent e)
    {
        if (e.getSource() instanceof JComboBox)
        {
            if (e.getStateChange() == ItemEvent.SELECTED)
            {
                selectionChanged();
            }
        }
        else
        {
            selectionChanged();
        }
    }


    public void selectionChanged()
    {
        boolean selectedCorrectly = false;

        if (this.actionCombo.getSelectedItem() == ConnectOperationType.ImportData)
        {
            selectedCorrectly = this.importItemsJList.getSelectedItemsSet().size() > 0;
        }
        else if (this.actionCombo.getSelectedItem() == ConnectOperationType.ViewConfiguration)
        {
            selectedCorrectly = this.configurationItemsJList.getSelectedItemsSet().size() > 0;
        }

        this.nextButton.setEnabled(selectedCorrectly);
    }


    private void setSelectionValues()
    {
        this.parameters.setActionType((ConnectOperationType) this.actionCombo.getSelectedItem());

        if (this.actionCombo.getSelectedItem() == ConnectOperationType.ImportData)
        {
            this.parameters.setSelectedItems(this.importItemsJList.getSelectedItemsSet());
        }
        else if (this.actionCombo.getSelectedItem() == ConnectOperationType.ViewConfiguration)
        {
            this.parameters.setSelectedItems(this.configurationItemsJList.getSelectedItemsSet());
        }

        this.parameters.setHandlerConfiguration(this.configuration);

    }

}
