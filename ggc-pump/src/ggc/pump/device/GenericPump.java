package ggc.pump.device;

import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;

import java.util.Hashtable;

import javax.swing.ImageIcon;

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


public class GenericPump extends AbstractPump //implements PumpInterface 
{

    //protected I18nControl ic = I18nControl.getInstance();



    public GenericPump()
    {
        super();
    }
    
    
    public GenericPump(OutputWriter ow)
    {
        super(ow);
    }

    
    
    

    
    
    
    
    public boolean open()
    {
    	return false;
    	
    }
    
    /*
    public GenericMeter(int meter_type, String portName)
    {

	super(meter_type,
	      9600, 
	      SerialPort.DATABITS_8, 
	      SerialPort.STOPBITS_1, 
	      SerialPort.PARITY_NONE);

	data = new ArrayList<DailyValuesRow>();

	try
	{
	    this.setPort(portName);

	    if (!this.open())
	    {
		this.m_status = 1;
	    }
	}
	catch(Exception ex)
	{
	    System.out.println("AscensiaMeter -> Error adding listener: " + ex);
	    ex.printStackTrace();
	}
    }
*/
    /**
     * Used for opening connection with device.
     * @return boolean - if connection established
     */
    //@Override
/*    public boolean open() throws MeterException
    {
        return super.open();
	//return false;
        //return true;
    }
*/

    /**
     * Will be called, when the import is ended and freeing resources.
     */
    //@Override
    public void close()
    {
        return;
    }


    public int getPumpIndex()
    {
        return 0;
    }

    public ImageIcon getIcon()
    {
        return null;
    }

    public String getName()
    {
        return "Generic device";
    }

    

    //************************************************
    //***                    Test                  ***
    //************************************************

    public void test()
    {
    }



    public String getComment()
    {
        // TODO Auto-generated method stub
        return null;
    }





    public int getCompanyId()
    {
        // TODO Auto-generated method stub
        return 0;
    }





    public String getConnectionPort()
    {
        // TODO Auto-generated method stub
        return null;
    }





    public int getConnectionProtocol()
    {
        // TODO Auto-generated method stub
        return 0;
    }





    public String getDeviceClassName()
    {
        // TODO Auto-generated method stub
        return null;
    }





    public DeviceIdentification getDeviceInfo()
    {
        // TODO Auto-generated method stub
        return null;
    }





    public String getIconName()
    {
        // TODO Auto-generated method stub
        return null;
    }





    public int getImplementationStatus()
    {
        // TODO Auto-generated method stub
        return 0;
    }





    public String getInstructions()
    {
        // TODO Auto-generated method stub
        return null;
    }





    public int getMaxMemoryRecords()
    {
        // TODO Auto-generated method stub
        return 0;
    }





    public AbstractDeviceCompany getDeviceCompany()
    {
        // TODO Auto-generated method stub
        return null;
    }




    /**
     * getDeviceId - Get Device Id, within MgrCompany class 
     * Should be implemented by device class.
     * 
     * @return id of device within company
     */
    public int getDeviceId()
    {
        // TODO Auto-generated method stub
        return 0;
    }





    public void readConfiguration() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }





    public void readDeviceDataFull() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }





    public void readDeviceDataPartitial() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }





    public void readInfo() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }





    public void setDeviceAllowedActions(boolean can_read_data, boolean can_read_partitial_data,
            boolean can_read_device_info, boolean can_read_device_configuration)
    {
        // TODO Auto-generated method stub
        
    }





    public void setDeviceCompany(AbstractDeviceCompany company)
    {
        // TODO Auto-generated method stub
        
    }



    /**
     * loadPumpSpecificValues - should be called from constructor of any AbstractPump classes and should
     *      create, AlarmMappings and EventMappings and any other pump constants.
     */
    public void loadPumpSpecificValues()
    {
    }
    
    
    /**
     * Map pump specific alarms to PumpTool specific alarm codes
     * @return
     */
    public Hashtable<String,Integer> getAlarmMappings()
    {
        return null;
    }
    
    
    /**
     * Map pump specific events to PumpTool specific event codes
     * @return
     */
    public Hashtable<String,Integer> getEventMappings()
    {
        return null;
    }










    public String getDeviceSpecialComment()
    {
        // TODO Auto-generated method stub
        return null;
    }





    public boolean hasSpecialProgressStatus()
    {
        // TODO Auto-generated method stub
        return false;
    }


    public void dispose()
    {
        // TODO Auto-generated method stub
        
    }

    
    




}
