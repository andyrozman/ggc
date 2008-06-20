/*
 * Created on 12.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */

package ggc.meter.gui;

import ggc.meter.data.cfg.MeterConfigEntry;
import ggc.meter.device.MeterInterface;
import ggc.meter.manager.MeterManager;
import ggc.meter.protocol.ConnectionProtocols;
import ggc.meter.util.DataAccess;
import ggc.meter.util.I18nControl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;


/**
 * @author stephan
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public class MeterInstructionsDialog extends JDialog implements ActionListener
{


    /**
     * 
     */
    private static final long serialVersionUID = 7159799607489791137L;

    private JLabel infoIcon = null;
//x    private JLabel infoDescription = null;

    private I18nControl m_ic = I18nControl.getInstance();        
    private DataAccess m_da = DataAccess.getInstance();



    int x,y;

    JFrame parentMy;

    
    
    MeterInterface meter_interface;
    MeterConfigEntry configured_meter;
    
    
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
        m_da.addComponent(this);
        loadConfiguration();
        init();

        this.setVisible(true);
    }
    
    
    private void loadConfiguration()
    {
        // TODO: this should be read from config
        
        this.configured_meter = new MeterConfigEntry();
        this.configured_meter.id =1;
        this.configured_meter.communication_port = "COM9";
        this.configured_meter.name = "My Countour";
        this.configured_meter.meter_company = "Ascensia/Bayer";
        this.configured_meter.meted_device = "Contour";
        
        MeterInterface mi = MeterManager.getInstance().getMeterDevice(this.configured_meter.meter_company, this.configured_meter.meted_device);
        
        this.meter_interface = mi;
        
    }
    
    
    
    private void dialogPreInit()
    {
        

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


    public ImageIcon getMeterIcon()
    {
        if (this.meter_interface == null)
        {
            return m_da.getImageIcon("/icons/meters/", "no_meter.gif");
        }
        else
        {
            return m_da.getImageIcon("/icons/meters/", this.meter_interface.getIconName());
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
        setTitle(m_ic.getMessage("CONFIGURED_METER_INSTRUCTIONS"));

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setSize(600, 700);

        JLabel label;

        int xkor=300;
        int ykor=300;

        if (this.parentMy!=null)
        {
            Rectangle rec = this.parentMy.getBounds();
            xkor = rec.x + (rec.width/2);
            ykor = rec.y + (rec.height/2);
        }

        Font normal = m_da.getFont(DataAccess.FONT_NORMAL);
        Font bold = m_da.getFont(DataAccess.FONT_NORMAL);

        
        setBounds(xkor-250, ykor-250, 650, 600);
        //dWindowListener(new CloseListener());

        //setBounds(300, 300, 300, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        getContentPane().add(panel, BorderLayout.CENTER);

  
        
        // panel icon
        JPanel panel_icon = new JPanel();
        panel_icon.setBorder(new TitledBorder(m_ic.getMessage("METER_ICON")));
        panel_icon.setBounds(10, 10, 280, 380);
        panel.add(panel_icon);
        
        infoIcon = new JLabel(this.getMeterIcon());
        infoIcon.setBounds(10, 20, 260, 350);
        infoIcon.setHorizontalAlignment(SwingConstants.CENTER);
        infoIcon.setVerticalAlignment(SwingConstants.CENTER);
        infoIcon.setBackground(Color.blue);
        panel_icon.add(infoIcon);
        
        
        
        // panel configured device
        JPanel panel_device = new JPanel();
        panel_device.setBorder(new TitledBorder(m_ic.getMessage("CONFIGURED_DEVICE")));
        panel_device.setBounds(300, 10, 330, 170);
        panel_device.setLayout(null);
        panel.add(panel_device);
        
        label = new JLabel(m_ic.getMessage("MY_DEVICE_NAME") + ":" );
        label.setBounds(15, 20, 320, 25);
        panel_device.add(label);
        
        label = new JLabel(this.configured_meter.name);
        label.setBounds(130, 20, 320, 25);
        label.setFont(normal);
        panel_device.add(label);
        
        label = new JLabel(m_ic.getMessage("METER_COMPANY") + ":" );
        label.setBounds(15, 40, 320, 25);
        panel_device.add(label);
        
        label = new JLabel(this.configured_meter.meter_company);
        label.setBounds(130, 40, 320, 25);
        label.setFont(normal);
        panel_device.add(label);

        label = new JLabel(m_ic.getMessage("METER_NAME") + ":" );
        label.setBounds(15, 60, 320, 25);
        panel_device.add(label);
        
        label = new JLabel(this.configured_meter.meted_device);
        label.setBounds(130, 60, 320, 25);
        label.setFont(normal);
        panel_device.add(label);
        
        label = new JLabel(m_ic.getMessage("CONNECTION_TYPE") + ":" );
        label.setBounds(15, 80, 320, 25);
        panel_device.add(label);
        
        label = new JLabel(this.getMeterInterfaceParameter(METER_INTERFACE_PARAM_CONNECTION_TYPE));
        label.setBounds(130, 80, 320, 25);
        label.setFont(normal);
        panel_device.add(label);
        
        label = new JLabel(m_ic.getMessage("CONNECTION_PARAMETER") + ":" );
        label.setBounds(15, 100, 320, 25);
        panel_device.add(label);
        
        label = new JLabel(this.configured_meter.communication_port);
        label.setBounds(130, 100, 320, 25);
        label.setFont(normal);
        panel_device.add(label);

        label = new JLabel(m_ic.getMessage("DAYLIGHTSAVINGS_FIX") + ":" );
        label.setBounds(15, 120, 320, 25);
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
        panel_device.add(label);
        
        label = new JLabel(this.getMeterInterfaceParameter(METER_INTERFACE_PARAM_STATUS));
        label.setBounds(130, 140, 320, 25);
        label.setFont(normal);
        panel_device.add(label);
        
        
        
        JPanel panel_instruct = new JPanel();
        panel_instruct.setBorder(new TitledBorder(m_ic.getMessage("INSTRUCTIONS")));
        panel_instruct.setBounds(300, 190, 330, 200);
        panel.add(panel_instruct);

        
        // bottom 
        
        label = new JLabel(m_ic.getMessage("INSTRUCTIONS_DESC")); //this.m_mim.getName());
        label.setBounds(20, 400, 590, 120);
        label.setVerticalAlignment(JLabel.TOP);
        label.setFont(normal);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label);
        

        
        
        
        
        
        
        
        
        
        
        
        
        
        

        
        label = new JLabel("Name"); //this.m_mim.getName());
        label.setBounds(10, 230, 150, 25);
        label.setHorizontalAlignment(SwingConstants.CENTER);
//        panel.add(label);

/*
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

        m_timer = new TimerThread(m_da, label, label_1, this.m_mim.getTimeDifference());
        m_timer.start();
  */      

        
        /*
        // meter status
        label = new JLabel(m_ic.getMessage("METER_STATUS") + ":");
        label.setBounds(182, 410, 100, 25);
        panel.add(label);
        
        lbl_status = new JLabel();
        lbl_status.setBounds(320, 410, 150, 25);
        panel.add(lbl_status);

        setStatus();
*/


        
        
        
        
        
        
        
        
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
            System.out.println("MeterInstructionsDialog::Unknown command: " + action);

    }


    public static void main(String[] args)
    {
        new MeterInstructionsDialog();
    }

    


}
