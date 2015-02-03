package ggc.cgms.device.animas;

import ggc.cgms.device.AbstractCGMS;
import ggc.cgms.util.DataAccessCGMS;
import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.SerialProtocol;

import gnu.io.SerialPortEvent;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       CGMS Tool (support for CGMS devices)
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
 *  Filename:     AnimasCGMS
 *  Description:  Animas CGMS abstract class
 *
 *  Author: Andy {andy@atech-software.com}
 */

public abstract class AnimasCGMS extends AbstractCGMS
{

    //String communicationPort;
    //OutputWriter output_writer;
    //AbstractDeviceCompany device_company;





    /**
     * Constructor
     *
     * @param communicationPort
     * @param writer
     */
    public AnimasCGMS(String communicationPort, OutputWriter writer)
    {
        super(communicationPort, writer);
    }


    /**
     * Constructor
     *
     * @param cmp
     */
    public AnimasCGMS(AbstractDeviceCompany cmp)
    {
        super(cmp);
    }
    


    public void dispose()
    {
    }


    public String getComment()
    {
        return null;
    }


    public String getDeviceSpecialComment()
    {
        return null; //"Test implementation";
    }


    public int getDownloadSupportType()
    {
        return 0;
    }


    public GGCPlugInFileReaderContext[] getFileDownloadTypes()
    {
        return null;
    }


    public boolean hasIndeterminateProgressStatus()
    {
        return false;
    }


    public boolean hasSpecialProgressStatus()
    {
        return true;
    }


    public boolean isDeviceCommunicating()
    {
        return false;
    }


    public boolean isFileDownloadSupported()
    {
        return false;
    }


    public boolean isReadableDevice()
    {
        return false;
    }


    public void readConfiguration() throws PlugInBaseException
    {
        
    }


    public void readDeviceDataFull() throws PlugInBaseException
    {
    }


    public void readDeviceDataPartitial() throws PlugInBaseException
    {
    }


    public void readInfo() throws PlugInBaseException
    {
    }


    public long getItemId()
    {
        return 0;
    }


    public abstract AnimasDeviceType getAnimasDeviceType();


}
