package ggc.plugin.device.impl.accuchek;

import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;

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
 *  Filename:     ----  
 *  Description:  ----
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public abstract class AccuChekSmartPixReaderAbstract
{
    protected OutputWriter output_writer;
    protected AccuChekSmartPix parent;
    protected DataAccessPlugInBase m_da;
    protected String drive_path = "/media/SMART_PIX/";

    
    /**
     * Constructor
     * 
     * @param da
     * @param ow
     * @param par
     */
    public AccuChekSmartPixReaderAbstract(DataAccessPlugInBase da, OutputWriter ow, AccuChekSmartPix par)
    {
        this.m_da = da;
        this.output_writer = ow;
        this.parent = par;
        this.drive_path = par.getRootDrive();
    }
    
    
    /**
     * Constructor
     */
    public AccuChekSmartPixReaderAbstract()
    {
    }
    
    
    /**
     * Read Device
     */
    public abstract void readDevice();
    

    
    protected void setStatus(int st_proc, String stat_msg)
    {
        if (stat_msg!=null)
            this.parent.writeStatus(stat_msg);
        
        if (st_proc>-1)
            this.output_writer.setSpecialProgress(st_proc);
    }
    
    
    
    /**
     * Pre Init Device - Does preinit
     * 
     * @see hasPreInit
     */
    public abstract void preInitDevice();
    
    
    
    
}
