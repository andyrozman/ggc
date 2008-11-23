package ggc.meter.gui.config;

import ggc.meter.util.DataAccessMeter;
import ggc.plugin.cfg.DeviceConfigurationDialog;

import javax.swing.JFrame;




/**
 * For testing only, will be removed.
 * 
 * @author Andy
 *
 */
public class SimpleConfiguration_v2_Dialog //extends JDialog implements ActionListener, ChangeListener, ItemListener //, HelpCapable
{

    
    
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        DataAccessMeter da = DataAccessMeter.getInstance();
        
        JFrame d1 = new JFrame();
        da.addComponent(d1);
        
        new DeviceConfigurationDialog(d1, da);
        /*SimpleConfiguration_v2_Dialog d =*/ //new SimpleConfiguration_v2_Dialog(d1, da);
        
    }

    
}
