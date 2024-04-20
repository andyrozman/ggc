package ggc.meter.device;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListDataListener;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;

import ggc.core.util.DataAccess;
import ggc.meter.defs.MeterPluginDefinition;
import ggc.meter.defs.device.MeterDeviceDefinition;
import ggc.meter.util.DataAccessMeter;
import ggc.plugin.data.DeviceDataHandler;
import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.util.DataAccessPlugInBase;

/**
 * 
 * @author andy
 *
 */
public class TestDeviceInstructionsMeterEnum extends JDialog implements ActionListener
{

    private DataAccessPlugInBase m_da;
    I18nControlAbstract m_ic = null;

    // private DeviceInterfaceVersion deviceInterfaceVersion;

    // DeviceInterface deviceInterfaceV1;
    // DeviceInstanceWithHandler deviceInterfaceV2;

    // DeviceConfigEntry configured_device;
    DeviceDataHandler deviceDataHandler;
    JFrame m_parent = null;
    JButton button_start, help_button;
    JLabel label_waiting;
    boolean reading_old_done = false;
    boolean is_preinit_done = true;
    private String unknownTranslation;

    MeterDeviceDefinition deviceDefinition = MeterDeviceDefinition.AccuChekActive;

    JTextArea ta_html;
    JLabel label_ins;

    JLabel lblDeviceCompany, lblDeviceName, lblDevicePicture;
    JLabel labelInstructions;

    // String textInstructions = "<html><li>Attach Carelink USB device
    // (or</li></html>";


    public TestDeviceInstructionsMeterEnum(Container parent, DataAccessPlugInBase da)
    {

        super((JFrame) parent, "", true);

        this.m_da = da;
        this.m_ic = da.getI18nControlInstance();
        this.m_parent = (JFrame) parent;

        m_da.addComponent(this);

        unknownTranslation = m_ic.getMessage("UNKNOWN");

        button_start = new JButton(m_ic.getMessage("START_DOWNLOAD"));

        init();

        // ATSwingUtils.centerJDialog(this, parent);
        this.setResizable(false);
        this.setVisible(true);

    }


    protected void init()
    {
        ATSwingUtils.initLibrary();

        setTitle(String.format(m_ic.getMessage("CONFIGURED_DEVICE_INSTRUCTIONS"), m_ic.getMessage("DEVICE_NAME_BIG")));

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setSize(600, 700);

        JLabel label;

        Font normal = ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL);

        setBounds(0, 0, 650, 600);
        // dataAccess.centerJDialog(this);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().add(panel, BorderLayout.CENTER);

        // panel icon
        JPanel panel_icon = ATSwingUtils.getPanel(10, 10, 280, 380, null,
            new TitledBorder(String.format(m_ic.getMessage("DEVICE_ICON"), m_ic.getMessage("DEVICE_NAME_BIG"))), panel);

        lblDevicePicture = new JLabel(this.getDeviceIcon());
        lblDevicePicture.setBounds(10, 20, 260, 350);
        lblDevicePicture.setHorizontalAlignment(SwingConstants.CENTER);
        lblDevicePicture.setVerticalAlignment(SwingConstants.CENTER);
        panel_icon.add(lblDevicePicture);

        // panel configured device
        JPanel panel_device = ATSwingUtils.getPanel(300, 10, 330, 170, null,
            new TitledBorder(m_ic.getMessage("CONFIGURED_DEVICE")), panel);

        ATSwingUtils.getLabel(m_ic.getMessage("MY_DEVICE_NAME") + ":", 15, 20, 320, 25, panel_device,
            ATSwingUtils.FONT_NORMAL_BOLD);

        ATSwingUtils.getLabel(this.deviceDefinition.getDeviceName(), 130, 20, 320, 25, panel_device,
            ATSwingUtils.FONT_NORMAL);

        ATSwingUtils.getLabel(m_ic.getMessage("DEVICE_COMPANY") + ":", 15, 40, 320, 25, panel_device,
            ATSwingUtils.FONT_NORMAL_BOLD);

        lblDeviceCompany = ATSwingUtils.getLabel(this.deviceDefinition.getDeviceCompany().getName(), 130, 40, 320, 25,
            panel_device, ATSwingUtils.FONT_NORMAL);

        ATSwingUtils.getLabel(m_ic.getMessage("DEVICE_NAME_") + ":", 15, 60, 320, 25, panel_device,
            ATSwingUtils.FONT_NORMAL_BOLD);

        lblDeviceName = ATSwingUtils.getLabel(this.deviceDefinition.getDeviceName(), 130, 60, 320, 25, panel_device,
            ATSwingUtils.FONT_NORMAL);

        ATSwingUtils.getLabel(m_ic.getMessage("CONNECTION_TYPE") + ":", 15, 80, 320, 25, panel_device,
            ATSwingUtils.FONT_NORMAL_BOLD);

        ATSwingUtils.getLabel(this.getDeviceInterfaceParameter(DEVICE_INTERFACE_PARAM_CONNECTION_TYPE), 130, 80, 320,
            25, panel_device, ATSwingUtils.FONT_NORMAL);

        ATSwingUtils.getLabel(m_ic.getMessage("CONNECTION_PARAMETER") + ":", 15, 100, 320, 25, panel_device,
            ATSwingUtils.FONT_NORMAL_BOLD);

        ATSwingUtils.getLabel("/dev/ttyS01", 130, 100, 320, 25, panel_device, ATSwingUtils.FONT_NORMAL);

        ATSwingUtils.getLabel(m_ic.getMessage("STATUS") + ":", 15, 120, 320, 25, panel_device,
            ATSwingUtils.FONT_NORMAL_BOLD);

        label = ATSwingUtils.getLabel(this.getDeviceInterfaceParameter(DEVICE_INTERFACE_PARAM_STATUS), 130, 120, 320,
            25, panel_device, ATSwingUtils.FONT_NORMAL);

        JLabel dsFixName = ATSwingUtils.getLabel(m_ic.getMessage("DAYLIGHTSAVINGS_FIX") + ":", 15, 140, 320, 25,
            panel_device, ATSwingUtils.FONT_NORMAL_BOLD);

        label = ATSwingUtils.getLabel("", 130, 140, 320, 25, panel_device, ATSwingUtils.FONT_NORMAL);

        if (m_da.getDeviceConfigurationDefinition().doesDeviceSupportTimeFix())
        {
            // if (this.configured_device.ds_fix)
            // {
            // label.setText(this.configured_device.getDayLightFix());
            // label.setToolTipText(this.configured_device.getDayLightFixLong());
            // }
            // else
            // {
            // label.setText(this.configured_device.getDayLightFix());
            // }
        }
        else
        {
            // label.setText(i18nControl.getMessage("DEVICE_DOESNT_SUPPORT_DS_FIX"));
            // label.setToolTipText(i18nControl.getMessage("DEVICE_DOESNT_SUPPORT_DS_FIX"));
            label.setVisible(false);
            dsFixName.setVisible(false);
        }

        // device instructions
        JPanel panel_instruct = ATSwingUtils.getPanel(300, 190, 330, 200, new FlowLayout(),
            new TitledBorder(m_ic.getMessage("INSTRUCTIONS")), panel);

        labelInstructions = ATSwingUtils.getLabel(
            m_ic.getMessage(getDeviceInterfaceParameter(DEVICE_INTERFACE_PARAM_INSTRUCTIONS)), 5, 0, 280, 180,
            panel_instruct, ATSwingUtils.FONT_NORMAL_SMALLER);
        labelInstructions.setVerticalAlignment(SwingConstants.TOP);
        labelInstructions.setHorizontalAlignment(SwingConstants.LEFT);

        // bottom
        // label =
        // ATSwingUtils.getLabel(i18nControl.getMessage("INSTRUCTIONS_DESC"),
        // 20, 400, 590, 120, panel,
        // ATSwingUtils.FONT_NORMAL);
        // label.setVerticalAlignment(SwingConstants.TOP);
        // label.setHorizontalAlignment(SwingConstants.CENTER);

        help_button = ATSwingUtils.createHelpButtonByBounds(30, 520, 130, 25, this, ATSwingUtils.FONT_NORMAL, m_da);
        help_button.setFont(normal);
        panel.add(help_button);

        // dataAccess.enableHelp(this);

        ComboBoxModel<DeviceDefinition> model = new ComboBoxModel<DeviceDefinition>()
        {

            java.util.List<DeviceDefinition> listElements = MeterDeviceDefinition.getAllDevices();
            DeviceDefinition selectedItem = null;


            public void setSelectedItem(Object anItem)
            {

                this.selectedItem = (DeviceDefinition) anItem;

                setSelectedItemInDisplay(this.selectedItem);

                System.out.println("Selected " + this.selectedItem);
            }


            public Object getSelectedItem()
            {
                return this.selectedItem;
            }


            public int getSize()
            {
                return listElements.size();
            }


            public DeviceDefinition getElementAt(int index)
            {
                return listElements.get(index);
            }


            public void addListDataListener(ListDataListener l)
            {

            }


            public void removeListDataListener(ListDataListener l)
            {

            }
        };

        JComboBox<DeviceDefinition> comboBox = new JComboBox<>(model);
        comboBox.setBounds(40, 430, 250, 30);
        panel.add(comboBox);

        ATSwingUtils.getButton(m_ic.getMessage("CANCEL"), 170, 520, 130, 25, panel, ATSwingUtils.FONT_NORMAL, null,
            "cancel", this, m_da);

        label_waiting = ATSwingUtils.getLabel(m_ic.getMessage("WAIT_UNTIL_DEVICE_PREINIT"), 310, 498, 300, 25, panel,
            ATSwingUtils.FONT_NORMAL);
        label_waiting.setVerticalAlignment(SwingConstants.TOP);
        label_waiting.setHorizontalAlignment(SwingConstants.RIGHT);

        button_start.setBounds(370, 520, 240, 25);
        button_start.setFont(ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL));
        button_start.setActionCommand("start_download");
        button_start.addActionListener(this);
        // button_start.setEnabled(true);
        panel.add(button_start);

        // this.preInitDone(false);
    }


    public void setSelectedItemInDisplay(DeviceDefinition deviceDefinition)
    {
        this.deviceDefinition = (MeterDeviceDefinition) deviceDefinition;
        System.out.println("Selected: " + this.deviceDefinition);

        try
        {
            lblDeviceCompany.setText(deviceDefinition.getDeviceCompany().getName());
            lblDeviceName.setText(deviceDefinition.getDeviceName());
            lblDevicePicture.setIcon(this.getDeviceIcon());
            labelInstructions.setText(m_ic.getMessage(deviceDefinition.getInstructionsI18nKey()));
        }
        catch (Exception ex)
        {
            System.out.println("    Error selecting device: " + this.deviceDefinition);
        }

    }


    private ImageIcon getDeviceIcon() {

        String root = m_da.getDeviceImagesRoot();

        if (this.deviceDefinition == null)
            return m_da.getImageIcon(root, "no_device.gif");
        else {
            return m_da.getImageIcon(root, getDeviceInterfaceParameter(DEVICE_INTERFACE_PARAM_ICON_NAME));
        }
    }

    private static final int DEVICE_INTERFACE_PARAM_CONNECTION_TYPE = 1;
    private static final int DEVICE_INTERFACE_PARAM_STATUS = 2;
    private static final int DEVICE_INTERFACE_PARAM_INSTRUCTIONS = 3;
    private static final int DEVICE_INTERFACE_PARAM_ICON_NAME = 4;


    private String getDeviceInterfaceParameter(int param)
    {
        String value = unknownTranslation;

        switch (param)
        {
            case DEVICE_INTERFACE_PARAM_CONNECTION_TYPE:
                {
                    value = this.deviceDefinition.getConnectionProtocol().getTranslation();

                    // if (deviceDataHandler.getDeviceInterfaceV2() != null)
                    // {
                    // value = i18nControl.getMessage(
                    // deviceDataHandler.getDeviceInterfaceV2().getConnectionProtocol().getI18nKey());
                    // }
                    // else if (deviceDataHandler.getDeviceInterfaceV1() !=
                    // null)
                    // {
                    // value = i18nControl.getMessage(
                    // deviceDataHandler.getDeviceInterfaceV1().getConnectionProtocol().getI18nKey());
                    // }

                    return value;
                }

            case DEVICE_INTERFACE_PARAM_STATUS:
                {
                    value = m_ic.getMessage("ERROR_IN_CONFIG");

                    // if ((deviceDataHandler.getDeviceInterfaceV1() != null)
                    // || (deviceDataHandler.getDeviceInterfaceV2() != null))
                    {
                        value = m_ic.getMessage("READY");
                    }

                    return value;
                }

            case DEVICE_INTERFACE_PARAM_INSTRUCTIONS:
                {

                    value = m_ic.getMessage(this.deviceDefinition.getInstructionsI18nKey());

                    // if (deviceDataHandler.getDeviceInterfaceV2() != null)
                    // {
                    // value =
                    // i18nControl.getMessage(deviceDataHandler.getDeviceInterfaceV2().getInstructions());
                    // }
                    // else if (deviceDataHandler.getDeviceInterfaceV1() !=
                    // null)
                    // {
                    // value =
                    // i18nControl.getMessage(deviceDataHandler.getDeviceInterfaceV1().getInstructions());
                    // }

                    return value;
                }

            case DEVICE_INTERFACE_PARAM_ICON_NAME:
                {
                    // if (deviceDataHandler.getDeviceInterfaceV2() != null)
                    {
                        value = deviceDefinition.getIconName(); // deviceDataHandler.getDeviceInterfaceV2().getIconName();
                    }
                    // else if (deviceDataHandler.getDeviceInterfaceV1() !=
                    // null)
                    // {
                    // value =
                    // deviceDataHandler.getDeviceInterfaceV1().getIconName();
                    // }
                    // else
                    // {
                    // value = "no_device.gif";
                    // }

                    return value;
                }

            default:
                return value;
        }
    }


    /**
     * Constructor
     */
    public TestDeviceInstructionsMeterEnum()
    {
        JFrame mainFrame = new JFrame("HTML");
        mainFrame.setLayout(null);
        mainFrame.setBounds(20, 20, 700, 700);
        ATSwingUtils.initLibrary();

        ta_html = new JTextArea();
        ta_html.setBounds(20, 20, 600, 175);
        ta_html.setLineWrap(true);

        mainFrame.add(ta_html, null);

        JButton button1 = new JButton("Refresh");
        /*
         * JLabel label1 = new
         * JLabel("<html><font color='#FF0000'>Red Text</font>"
         * + "<br /><font color='#00FF00'>Blue Text</font>" +
         * "<br /><font color='#0000FF'>Green Text</font>"
         * + "<br /><font color='#000000'>Black Text</font>"
         * + "<br /><font color='#FFFFFF'>Blue Text</font></html>");
         */
        button1.setBounds(20, 320, 100, 25);
        button1.addActionListener(this);

        mainFrame.add(button1, null);

        // ATSwingUtils.getPanel(300, 190, 330, 200, (plugin instructions)

        JPanel panel_instruct = ATSwingUtils.getPanel(200, 220, 330, 200, new FlowLayout(),
            new TitledBorder("INSTRUCTIONS"), mainFrame.getContentPane());

        label_ins = ATSwingUtils.getLabel("", 5, 0, 280, 180, panel_instruct, ATSwingUtils.FONT_NORMAL_SMALLER);
        label_ins.setVerticalAlignment(SwingConstants.TOP);
        label_ins.setHorizontalAlignment(SwingConstants.LEFT);
        label_ins.setBackground(Color.green);

        // label =
        // ATSwingUtils.getLabel(i18nControl.getMessage(getDeviceInterfaceParameter(DEVICE_INTERFACE_PARAM_INSTRUCTIONS)),
        // 5, 0, 280, 180,
        // panel_instruct, ATSwingUtils.FONT_NORMAL_SMALLER);
        // label.setVerticalAlignment(SwingConstants.TOP);
        // label.setHorizontalAlignment(SwingConstants.LEFT);

        // mainFrame.add(button1);
        mainFrame.add(panel_instruct);

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
        // mainFrame.pack();
    }


    /**
     * @param args
     */
    public static void main(String[] args)
    {
        System.out.println(System.getProperty("user.home"));

        JFrame f = new JFrame();

        DataAccess.dontLoadIcons = true;

        DataAccess da = DataAccess.createInstance(f);

        DataAccessPlugInBase dam = DataAccessMeter.createInstance(new MeterPluginDefinition(da.getLanguageManager()));

        dam.setMainParent(f);

        // Container parent, DataAccessPlugInBase da
        new TestDeviceInstructionsMeterEnum(f, dam);
    }


    public void actionPerformed(ActionEvent e)
    {
        this.label_ins.setText(ta_html.getText());
    }
}
