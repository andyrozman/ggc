package ggc.pump.gui;

import ggc.core.db.hibernate.DayValueH;
import ggc.plugin.cfg.DeviceConfigEntry;
import ggc.plugin.output.OutputWriter;
import ggc.pump.data.PumpValuesEntry;
import ggc.pump.plugin.PumpPlugInServer;
import ggc.pump.util.DataAccessPump;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.LineBorder;

import com.atech.graphics.components.StatusReporterInterface;
import com.atech.i18n.I18nControlAbstract;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
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
 *  Filename:  ###---###  
 *  Description:
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class PumpExportDialog extends JDialog implements ActionListener, StatusReporterInterface
{

    private static final long serialVersionUID = 3742517721328690327L;
    PumpPlugInServer server;
    
    
    /* 
     * setStatus
     */
    public void setStatus(int status)
    {
        if (this.progress!=null)
            this.progress.setValue(status);
        
        if (status==100)
        {
            bt_close.setEnabled(true);
            this.lbl_status.setText(m_ic.getMessage("EXPORT_STATUS_FINISHED"));

        }
    }



    private DataAccessPump m_da = DataAccessPump.getInstance();
    I18nControlAbstract m_ic = m_da.getI18nControlInstance();

    public JProgressBar progress = null;


    
    private Hashtable<String,ArrayList<DayValueH>> meter_data = null;
    DeviceConfigEntry configured_device;
    
    
    private JButton bt_close, bt_start;
    JLabel lbl_status;

    
    
    
    
    
    
    
    /*
     * Constructor for ReadMeterDialog.
     * 
     * @param owner
     * 
     * @throws HeadlessException
     */
/*    public MeterDisplayDataDialog(JFrame owner, MeterInterface mi)
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
*/
    
    public PumpExportDialog()
    {
        super();
        dialogPreInit(false);
    }

    
    public PumpExportDialog(ArrayList<PumpValuesEntry> lst, OutputWriter writer)
    {
        super();
        dialogPreInit(false);
    }
    

    
    public PumpExportDialog(DeviceConfigEntry mce)
    {
        super();

        this.configured_device = mce;
        dialogPreInit(false);
    }
    

    public PumpExportDialog(JDialog parent, Hashtable<String,ArrayList<DayValueH>> meter_data, PumpPlugInServer server)
    {
        super();

        this.meter_data = meter_data;
        this.m_da.centerJDialog(this, parent);
        this.server = server;
        
        dialogPreInit(true);
    }
    
    
    


    private void dialogPreInit(boolean start)
    {
        setTitle(m_ic.getMessage("EXPORT_METER_DATA"));

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
        this.server.setReturnData(this.meter_data, this);
        this.bt_close.setEnabled(false);
        this.lbl_status.setText(m_ic.getMessage("EXPORT_STATUS_EXPORTING"));
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

        
        label = new JLabel(m_ic.getMessage("EXPORT_METER_DATA"));
        label.setFont(m_da.getFont(DataAccessPump.FONT_BIG_BOLD));
        label.setBounds(0, 15, 320, 35);
        label.setHorizontalAlignment(JLabel.CENTER);
        panel.add(label);
        
        
        Font normal = m_da.getFont(DataAccessPump.FONT_NORMAL);
        Font bold = m_da.getFont(DataAccessPump.FONT_NORMAL_BOLD);


        label = new JLabel(m_ic.getMessage("EXPORT_OUTPUT") + ":");
        label.setBounds(30, 70, 310, 25);
        label.setFont(bold);
        panel.add(label);

        String[] exp = 
        {
             m_ic.getMessage("GGC_APPLICATION")
        };
        
        
        JComboBox cb = new JComboBox(exp);
        cb.setBounds(30,95,250,23);
        //cb.setEnabled(false);
        panel.add(cb);
        
        // progress
        label = new JLabel(m_ic.getMessage("EXPORT_PROGRESS") + ":");
        label.setBounds(30, 155, 200, 25);
        label.setFont(bold);
        panel.add(label);


        lbl_status = new JLabel(m_ic.getMessage("EXPORT_STATUS_READY"));
        lbl_status.setBounds(130, 155, 150, 25);
        lbl_status.setHorizontalAlignment(JLabel.RIGHT);
        lbl_status.setFont(normal);
        panel.add(lbl_status);
        
        
        this.progress = new JProgressBar();
        this.progress.setBounds(30, 180, 250, 20);
        this.progress.setStringPainted(true);
        //this.progress.setBorderPainted(true);
        this.progress.setBorder(new LineBorder(Color.black));
        this.progress.setForeground(Color.black);
        panel.add(this.progress);

        
        
        
        bt_start = new JButton(m_ic.getMessage("START"));
        bt_start.setBounds(50, 250, 100, 25);
        // bt_break.setEnabled(this.m_mim.isStatusOK());
        bt_start.setActionCommand("start");
        bt_start.addActionListener(this);
        panel.add(bt_start);

        JButton help_button = m_da.createHelpButtonByBounds(30, 310, 110, 25, this);
        panel.add(help_button);

        bt_close = new JButton(m_ic.getMessage("CLOSE"));
        bt_close.setBounds(170, 250, 100, 25);
        bt_close.setEnabled(false);
        bt_close.setActionCommand("close");
        bt_close.addActionListener(this);
        panel.add(bt_close);

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


    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
        String action_cmd = e.getActionCommand();

        if (action_cmd.equals("close"))
        {
            if (started)
                this.action = true;
            
            this.m_da.removeComponent(this);
            this.dispose();
        }
        else
            System.out.println("MeterExportDialog::Unknown command: " + action);

    }

    boolean started = false;
    boolean action = false;
    
    public boolean wasAction()
    {
        return this.action;
        
    }
    
    
    
    public static void main(String[] args)
    {
        // MeterReadDialog mrd =
        new PumpExportDialog(); // new AscensiaContour("COM12", new
                                      // ConsoleOutputWriter()));
    }


}
