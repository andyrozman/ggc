package ggc.meter.gui;

import ggc.core.db.hibernate.DayValueH;
import ggc.meter.device.MeterInterface;
import ggc.meter.manager.MeterManager;
import ggc.meter.plugin.MeterPlugInServer;
import ggc.meter.util.DataAccessMeter;
import ggc.plugin.cfg.DeviceConfigEntry;
import ggc.plugin.device.DeviceInterface;
import ggc.plugin.protocol.ConnectionProtocols;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.atech.db.DbDataReaderAbstract;
import com.atech.db.DbDataReadingFinishedInterface;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.TimeZoneUtil;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Meter Tool (support for Meter devices)
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


public class MeterInstructionsDialog extends JDialog implements ActionListener, DbDataReadingFinishedInterface
{

    private static final long serialVersionUID = 7159799607489791137L;

    
    private DataAccessMeter m_da = DataAccessMeter.getInstance();
    I18nControlAbstract m_ic = m_da.getI18nControlInstance();
    
    JButton button_start;
    JLabel label_waiting;
    
    Hashtable<String,DayValueH> meter_data = null;


    int x,y;

    JFrame parentMy;

    
    
    MeterInterface meter_interface;
    DeviceConfigEntry configured_meter;
    DbDataReaderAbstract reader;
    MeterPlugInServer server;
/*
    public MeterInstructionsDialog(JFrame owner, MeterInterface mi)
    {
        super(owner);
        m_da.addComponent(this);
        meter_interface = mi;
        this.parentMy = owner;
        
        dialogPreInit();
    }
*/
/*
    public MeterInstructionsDialog(MeterInterface mi)
    {
        super();
        m_da.addComponent(this);
        meter_interface = mi;
        
        dialogPreInit();
    }
*/

    public MeterInstructionsDialog()
    {
        super();
        loadConfiguration();
        init();

        this.setVisible(true);
    }

    
    public MeterInstructionsDialog(DbDataReaderAbstract reader, MeterPlugInServer server)
    {
        super();
        
        if (!loadConfiguration())
        {
            JOptionPane.showMessageDialog(this, m_ic.getMessage("OOPS_DEVICE_NOT_CONFIGURED"), m_ic.getMessage("ERROR"), JOptionPane.ERROR_MESSAGE);
            
            
            return;
        }
        
        //m_da.addContainer(server.)
        
       
        
        this.reader = reader;
        this.server = server;
        init();

        this.setVisible(true);
    }
    
    
    
    private boolean loadConfiguration()
    {
        //MeterConfiguration mc = new MeterConfiguration(false);
        
        this.configured_meter = this.m_da.getDeviceConfiguration().getSelectedDeviceInstance(); //mc.getDefaultMeter();
        
        System.out.println("configured meter: " + this.configured_meter);
        
        if (this.configured_meter==null)
            return false;
        
        /*
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
        */
        /*
        tzu.setTimeZone("Europe/Prague");
        tzu.setWinterTimeChange(0);
        tzu.setSummerTimeChange(+1);
        */
        
        //System.out.println(this.configured_meter.meter_company + " " + this.configured_meter.meter_device);
        
        
        DeviceInterface mi = MeterManager.getInstance().getMeterDevice(this.configured_meter.device_company, this.configured_meter.device_device);
        
        this.meter_interface = (MeterInterface)mi;
        
        return true;
        
    }
    
    


    private ImageIcon getMeterIcon()
    {
        
        String root = "/icons/meters/";
        
        
        if (this.meter_interface == null)
        {
            return m_da.getImageIcon(root, "no_meter.gif");
        }
        else
        {
            if (this.meter_interface.getIconName()==null)
                return m_da.getImageIcon(root, "no_meter.gif");
            else
                return m_da.getImageIcon(root, this.meter_interface.getIconName());
        }
    }
    
    
    public static final int METER_INTERFACE_PARAM_CONNECTION_TYPE = 1;
    public static final int METER_INTERFACE_PARAM_STATUS = 2;
    
    
    public String getMeterInterfaceParameter(int param)
    {
        switch(param)
        {
            case MeterInstructionsDialog.METER_INTERFACE_PARAM_CONNECTION_TYPE:
            {
                if (this.meter_interface==null)
                {
                    return m_ic.getMessage("UNKNOWN");
                }
                else
                {
                    return m_ic.getMessage(ConnectionProtocols.connectionProtocolDescription[this.meter_interface.getConnectionProtocol()]);
                }
            } 

            case MeterInstructionsDialog.METER_INTERFACE_PARAM_STATUS:
            {
                if (this.meter_interface==null)
                {
                    return m_ic.getMessage("ERROR_IN_CONFIG");
                }
                else
                {
                    return m_ic.getMessage("READY"); 
                }
            } 
            
            default:
                return m_ic.getMessage("UNKNOWN");
        }
    }
    


    protected void init()
    {
        m_da.addComponent(this);

        setTitle(m_ic.getMessage("CONFIGURED_METER_INSTRUCTIONS"));

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setSize(600, 700);

        JLabel label;

        /*
        int xkor=300;
        int ykor=300;

        if (this.parentMy!=null)
        {
            Rectangle rec = this.parentMy.getBounds();
            xkor = rec.x + (rec.width/2);
            ykor = rec.y + (rec.height/2);
        }
*/
        Font normal = m_da.getFont(DataAccessMeter.FONT_NORMAL);
        Font bold = m_da.getFont(DataAccessMeter.FONT_NORMAL);

        
        //setBounds(xkor-250, ykor-250, 650, 600);
        setBounds(0, 0, 650, 600);
        
        m_da.centerJDialog(this);
        
        //dWindowListener(new CloseListener());

        //setBounds(300, 300, 300, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        getContentPane().add(panel, BorderLayout.CENTER);

  
        
        // panel icon
        JPanel panel_icon = new JPanel();
        panel_icon.setBorder(new TitledBorder(m_ic.getMessage("METER_ICON")));
        panel_icon.setBounds(10, 10, 280, 380);
        panel.add(panel_icon);
        
        label = new JLabel(this.getMeterIcon());
        label.setBounds(10, 20, 260, 350);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        //infoIcon.setBackground(Color.blue);
        panel_icon.add(label);
        
        
        
        // panel configured device
        JPanel panel_device = new JPanel();
        panel_device.setBorder(new TitledBorder(m_ic.getMessage("CONFIGURED_DEVICE")));
        panel_device.setBounds(300, 10, 330, 170);
        panel_device.setLayout(null);
        panel.add(panel_device);
        
        label = new JLabel(m_ic.getMessage("MY_DEVICE_NAME") + ":" );
        label.setBounds(15, 20, 320, 25);
        label.setFont(bold);
        panel_device.add(label);
        
        label = new JLabel(this.configured_meter.name);
        label.setBounds(130, 20, 320, 25);
        label.setFont(normal);
        panel_device.add(label);
        
        label = new JLabel(m_ic.getMessage("METER_COMPANY") + ":" );
        label.setBounds(15, 40, 320, 25);
        label.setFont(bold);
        panel_device.add(label);
        
        label = new JLabel(this.configured_meter.device_company);
        label.setBounds(130, 40, 320, 25);
        label.setFont(normal);
        panel_device.add(label);

        label = new JLabel(m_ic.getMessage("METER_NAME") + ":" );
        label.setBounds(15, 60, 320, 25);
        label.setFont(bold);
        panel_device.add(label);
        
        label = new JLabel(this.configured_meter.device_device);
        label.setBounds(130, 60, 320, 25);
        label.setFont(normal);
        panel_device.add(label);
        
        label = new JLabel(m_ic.getMessage("CONNECTION_TYPE") + ":" );
        label.setBounds(15, 80, 320, 25);
        label.setFont(bold);
        panel_device.add(label);
        
        label = new JLabel(this.getMeterInterfaceParameter(METER_INTERFACE_PARAM_CONNECTION_TYPE));
        label.setBounds(130, 80, 320, 25);
        label.setFont(normal);
        panel_device.add(label);
        
        label = new JLabel(m_ic.getMessage("CONNECTION_PARAMETER") + ":" );
        label.setBounds(15, 100, 320, 25);
        label.setFont(bold);
        panel_device.add(label);
        
        label = new JLabel(this.configured_meter.communication_port);
        label.setBounds(130, 100, 320, 25);
        label.setFont(normal);
        panel_device.add(label);

        label = new JLabel(m_ic.getMessage("DAYLIGHTSAVINGS_FIX") + ":" );
        label.setBounds(15, 120, 320, 25);
        label.setFont(bold);
        panel_device.add(label);
        
        label = new JLabel();
        label.setBounds(130, 120, 320, 25);
        label.setFont(normal);
        panel_device.add(label);
        
        if (this.configured_meter.ds_fix)
        {
            label.setText(this.configured_meter.getDayLightFix());
            label.setToolTipText(this.configured_meter.getDayLightFixLong());
        }
        else
        {
            label.setText(this.configured_meter.getDayLightFix());
        }
        
        

        label = new JLabel(m_ic.getMessage("STATUS") + ":" );
        label.setBounds(15, 140, 320, 25);
        label.setFont(bold);
        panel_device.add(label);
        
        label = new JLabel(this.getMeterInterfaceParameter(METER_INTERFACE_PARAM_STATUS));
        label.setBounds(130, 140, 320, 25);
        label.setFont(normal);
        panel_device.add(label);
        
        
        // meter instructions
        JPanel panel_instruct = new JPanel();
        panel_instruct.setBorder(new TitledBorder(m_ic.getMessage("INSTRUCTIONS")));
        panel_instruct.setBounds(300, 190, 330, 200);
        panel.add(panel_instruct);
        
        

        label = new JLabel(m_ic.getMessage(this.meter_interface.getInstructions())); //this.m_mim.getName());
        label.setBounds(40, 0, 280, 180);
        label.setVerticalAlignment(JLabel.TOP);
        label.setFont(normal);
        //label.setHorizontalAlignment(SwingConstants.CENTER);
        panel_instruct.add(label);
        
        
        
        
        // bottom 
        label = new JLabel(m_ic.getMessage("INSTRUCTIONS_DESC")); //this.m_mim.getName());
        label.setBounds(20, 400, 590, 120);
        label.setVerticalAlignment(JLabel.TOP);
        label.setFont(normal);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label);
        

        
        JButton button = m_da.createHelpButtonByBounds(30, 520, 130, 25, this);
        button.setFont(normal);
        panel.add(button);
        
        
        button = new JButton(m_ic.getMessage("CANCEL"));
        button.setBounds(170, 520, 130, 25);
        button.setFont(normal);
        button.setActionCommand("cancel");
        button.addActionListener(this); //.setFont(normal);
        panel.add(button);
        		
        
        label_waiting = new JLabel(m_ic.getMessage("WAIT_UNTIL_OLD_DATA_IS_READ")); //this.m_mim.getName());
        label_waiting.setBounds(310, 495, 300, 25);
        label_waiting.setVerticalAlignment(JLabel.TOP);
        label_waiting.setHorizontalAlignment(JLabel.RIGHT);
        //label_waiting.setFont(bold);
        //label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label_waiting);
        
        
        button_start = new JButton(m_ic.getMessage("START_DOWNLOAD"));
        button_start.setBounds(370, 520, 240, 25);
        button_start.setFont(normal);
        button_start.setActionCommand("start_download");
        button_start.addActionListener(this); //.setFont(normal);
        panel.add(button_start);
        
        
        if (reader!=null)
        {
            if (reader.isFinished())
            {
                this.readingFinished();
            }
            else
            {
                this.reader.setReadingFinishedObject(this);
            }
        }
        else
            this.readingFinished();
        
    }



    



    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e) 
    {
        String action = e.getActionCommand();

        
        
        if (action.equals("cancel")) 
        {
            this.dispose();
        }
        else if (action.equals("start_download"))
        {

            System.out.println("Conf. meter: " + this.configured_meter);
            if (this.configured_meter.ds_fix)
            {
                TimeZoneUtil  tzu = TimeZoneUtil.getInstance();
                
                tzu.setTimeZone(this.configured_meter.ds_area);
                tzu.setWinterTimeChange(this.configured_meter.ds_winter_change);
                tzu.setSummerTimeChange(this.configured_meter.ds_summer_change);
            }
            
            this.dispose();
            
            if (this.meter_data==null)
            {
                new MeterDisplayDataDialog(this.configured_meter);
            }
            else
            {
                new MeterDisplayDataDialog(this.configured_meter, this.meter_data, this.server);
            }
            
        }
        else
            System.out.println("MeterInstructionsDialog::Unknown command: " + action);

    }

    
    /* 
     * readingFinished
     */
    @SuppressWarnings("unchecked")
    public void readingFinished()
    {
        this.button_start.setEnabled(true);
        this.label_waiting.setText("");
        
        if (this.reader==null)
        {
            this.meter_data = null;
        }
        else
        {
            this.meter_data = (Hashtable<String,DayValueH>)this.reader.getData();
        }
        
        
    }
    
    

    public static void main(String[] args)
    {
        new MeterInstructionsDialog();
    }

    


}
