package ggc.plugin.gui;

import ggc.plugin.device.DeviceAbstract;
import ggc.plugin.output.OutputUtil;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
 *  Filename:     DeviceReaderRunner
 *  Description:  This is separate thread class to get current data from database in order to 
 *                compare it later.
 * 
 *  Author: Andy {andy@atech-software.com}
 */

// Try to assess possibility of super-classing

public class DevicePreInitRunner extends Thread // extends JDialog implements
                                                // ActionListener
{

    private static Log log = LogFactory.getLog(DevicePreInitRunner.class);

    DataAccessPlugInBase m_da;
    DeviceAbstract device_instance;
    DeviceInstructionsDialog device_instruction_dialog;

    /**
     * Constructor
     * 
     * @param da 
     * @param dev_inst 
     * @param did 
     */
    public DevicePreInitRunner(DataAccessPlugInBase da, DeviceAbstract dev_inst, DeviceInstructionsDialog did)
    {
        this.m_da = da;
        this.device_instance = dev_inst;
        this.device_instruction_dialog = did;
    }

    /** 
     * Thread running method
     */
    @Override
    public void run()
    {

        // dataAccess.sleepMS(1000);

        try
        {
            log.debug("preInit Device - Start");
            this.device_instance.preInitDevice();
            log.debug("preInit Device - End");
        }
        catch (Exception ex)
        {
            log.error("Error on PreInit. Ex: " + ex, ex);
        }
        finally
        {
            this.device_instruction_dialog.preInitDone(true);
        }

    }

    /**
     * Write log entry
     * 
     * @param entry_type
     * @param message
     */
    public void writeLog(int entry_type, String message)
    {
        getOutputWriter().writeLog(entry_type, message);
    }

    /**
     * Write log entry
     * 
     * @param entry_type
     * @param message
     * @param ex
     */
    public void writeLog(int entry_type, String message, Exception ex)
    {
        getOutputWriter().writeLog(entry_type, message, ex);
    }

    /** 
     * Get Output Writer
     * 
     * @return 
     */
    public OutputWriter getOutputWriter()
    {
        return this.device_instance.getOutputWriter();
    }

    /** 
     * Get Output Util
     * 
     * @return 
     */
    public OutputUtil getOutputUtil()
    {
        return getOutputWriter().getOutputUtil();
    }

}
