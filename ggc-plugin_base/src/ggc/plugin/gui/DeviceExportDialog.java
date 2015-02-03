package ggc.plugin.gui;

import ggc.plugin.data.DeviceDataHandler;
import ggc.plugin.util.DataAccessPlugInBase;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import com.atech.graphics.components.StatusReporterInterface;
import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATDataAccessAbstract;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
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
 *  Filename:     DeviceExportDialog
 *  Description:  For exporting data into database (or somewhere else)
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class DeviceExportDialog extends JDialog implements ActionListener, StatusReporterInterface, HelpCapable
{

    private static final long serialVersionUID = -5673838593827489827L;

    private DataAccessPlugInBase m_da; // = DataAccessMeter.getInstance();
    I18nControlAbstract m_ic; // = dataAccess.getI18nControlInstance();

    private JProgressBar progress = null;

    private JButton bt_close, bt_start, help_button;
    JLabel lbl_status;
    DeviceDataHandler m_ddh;
    boolean reading_finsihed = false;

    /**
     * Constructor 
     * 
     * @param da
     * @param parent
     * @param ddh
     */
    public DeviceExportDialog(DataAccessPlugInBase da, JDialog parent, DeviceDataHandler ddh)
    {
        super(parent, true);
        this.m_da = da;
        this.m_ic = da.getI18nControlInstance();

        this.m_ddh = ddh;
        this.m_da.centerJDialog(this, parent);

        dialogPreInit(!ddh.isOutputWriterSet());
    }

    private void dialogPreInit(boolean start)
    {
        setTitle(String.format(m_ic.getMessage("EXPORT_DEVICE_DATA"), m_ic.getMessage("DEVICE_NAME_BIG")));

        m_da.addComponent(this);

        init();

        if (start)
        {
            this.bt_start.setVisible(false);
            setStart();
        }

        this.setVisible(true);
    }

    private void setStart()
    {
        this.started = true;
        this.bt_close.setEnabled(false);

        // Andy [6.3.2010] - we do this in thread now
        // this.m_ddh.executeExport(this);

        DeviceWriterRunner dwr = new DeviceWriterRunner(this, this.m_ddh);
        dwr.start();

        // this.server.setReturnData(this.meter_data, this);
        this.lbl_status.setText(m_ic.getMessage("EXPORT_STATUS_EXPORTING"));
        /*
         * if ((!this.isVisible()) && (reading_finsihed))
         * {
         * this.bt_close.setEnabled(true);
         * }
         */
    }

    protected void init()
    {

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setSize(320, 380);

        JLabel label;

        setBounds(0, 0, 320, 380);
        m_da.centerJDialog(this);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().add(panel, BorderLayout.CENTER);

        label = new JLabel(String.format(m_ic.getMessage("EXPORT_DEVICE_DATA"), m_ic.getMessage("DEVICE_NAME_BIG")));
        label.setFont(m_da.getFont(ATDataAccessAbstract.FONT_BIG_BOLD));
        label.setBounds(0, 15, 320, 35);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label);

        Font normal = m_da.getFont(ATDataAccessAbstract.FONT_NORMAL);
        Font bold = m_da.getFont(ATDataAccessAbstract.FONT_NORMAL_BOLD);

        label = new JLabel(m_ic.getMessage("EXPORT_OUTPUT") + ":");
        label.setBounds(30, 70, 310, 25);
        label.setFont(bold);
        panel.add(label);

        String[] exp = { m_ic.getMessage("GGC_APPLICATION") };

        JComboBox cb = new JComboBox(exp);
        cb.setBounds(30, 95, 250, 23);
        // cb.setEnabled(false);
        panel.add(cb);

        // progress
        label = new JLabel(m_ic.getMessage("EXPORT_PROGRESS") + ":");
        label.setBounds(30, 155, 200, 25);
        label.setFont(bold);
        panel.add(label);

        lbl_status = new JLabel(m_ic.getMessage("EXPORT_STATUS_READY"));
        lbl_status.setBounds(130, 155, 150, 25);
        lbl_status.setHorizontalAlignment(SwingConstants.RIGHT);
        lbl_status.setFont(normal);
        panel.add(lbl_status);

        this.progress = new JProgressBar();
        this.progress.setBounds(30, 180, 250, 20);
        this.progress.setStringPainted(true);
        // this.progress.setBorderPainted(true);
        this.progress.setBorder(new LineBorder(Color.black));
        this.progress.setForeground(Color.black);
        panel.add(this.progress);

        bt_start = new JButton(m_ic.getMessage("START"));
        bt_start.setBounds(50, 250, 100, 25);
        // bt_break.setEnabled(this.m_mim.isStatusOK());
        bt_start.setActionCommand("start");
        bt_start.addActionListener(this);
        panel.add(bt_start);

        help_button = m_da.createHelpButtonByBounds(30, 310, 110, 25, this);
        panel.add(help_button);

        bt_close = new JButton(m_ic.getMessage("CLOSE"));
        bt_close.setBounds(170, 250, 100, 25);
        bt_close.setEnabled(false);
        bt_close.setActionCommand("close");
        bt_close.addActionListener(this);
        panel.add(bt_close);

        m_da.enableHelp(this);
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
        String action_cmd = e.getActionCommand();

        if (action_cmd.equals("close"))
        {
            if (started)
            {
                this.action = true;
            }

            this.m_da.removeComponent(this);
            this.dispose();
        }
        else
        {
            System.out.println("DeviceExportDialog::Unknown command: " + action);
        }

    }

    boolean started = false;
    boolean action = false;

    /**
     * Was action succesful
     * @return
     */
    public boolean wasAction()
    {
        return this.action;
    }

    /**
     * setStatus
     */
    public void setStatus(int status)
    {
        if (this.progress != null)
        {
            this.progress.setValue(status);
        }

        // System.out.println("setStatus: " + status);

        if (status == 100)
        {
            // System.out.println("setStatusXXXX: " + status);
            bt_close.setEnabled(true);
            this.lbl_status.setText(m_ic.getMessage("EXPORT_STATUS_FINISHED"));
            bt_close.setEnabled(true);
            this.action = true;
        }
    }

    /**
     * Set reading finsihed
     */
    public void setReadingFinished()
    {
        setCloseEnabled(true);
    }

    /**
     * Set Close Enabled
     * 
     * @param cls
     */
    public void setCloseEnabled(boolean cls)
    {
        bt_close.setEnabled(cls);
    }

    // ****************************************************************
    // ****** HelpCapable Implementation *****
    // ****************************************************************

    /** 
     * getComponent - get component to which to attach help context
     * 
     * @return 
     */
    public Component getComponent()
    {
        return this.getRootPane();
    }

    /** 
     * getHelpButton - get Help button
     * 
     * @return 
     */
    public JButton getHelpButton()
    {
        return this.help_button;
    }

    /** 
     * getHelpId - get id for Help
     * 
     * @return 
     */
    public String getHelpId()
    {
        // return dataAccess.getDeviceConfigurationDefinition().getHelpPrefix() +
        // "Export_Data";
        return "DeviceTool_Export_Data";
    }

    /*
     * public static void main(String[] args)
     * {
     * // MeterReadDialog mrd =
     * new DeviceExportDialog(); // new AscensiaContour("COM12", new
     * // ConsoleOutputWriter()));
     * }
     */

}
