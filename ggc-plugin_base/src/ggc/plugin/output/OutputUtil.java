package ggc.plugin.output;

import com.atech.utils.TimerControlAbstract;

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
 *  Filename:     OutputUtil  
 *  Description:  Utility class to write to Output Writer
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class OutputUtil extends TimerControlAbstract
{

    private int max_records = 1;

    private OutputWriter writer;

    /**
     * Singelton instance of this class
     */
    public static OutputUtil s_outputUtil;


    private OutputUtil()
    {
        // a setAllowedChangeTime(10);
    }


    /**
     * Get Instance
     * 
     * @return OutputUtil instance
     */
    public static OutputUtil getInstance()
    {
        if (OutputUtil.s_outputUtil == null)
        {
            OutputUtil.s_outputUtil = new OutputUtil();
        }

        return OutputUtil.s_outputUtil;

    }


    /**
     * Get Instance
     * 
     * @param writer OutputWriter instance
     * @return OutputUtil instance
     */
    public static OutputUtil getInstance(OutputWriter writer)
    {
        if (OutputUtil.s_outputUtil == null)
        {
            OutputUtil.s_outputUtil = new OutputUtil();
        }

        OutputUtil.s_outputUtil.setOutputWriter(writer);
        return OutputUtil.s_outputUtil;
    }


    /**
     * Set Output Writer
     * 
     * @param writer OutputWriter instance
     */
    public void setOutputWriter(OutputWriter writer)
    {
        this.writer = writer;
    }


    /**
     * Stop Action
     * 
     * @see com.atech.utils.TimerControlAbstract#stopAction()
     */
    @Override
    public void stopAction()
    {

        this.writer.endOutput();

        // System.exit(0);

    }


    /**
     * Set Max Memory Records
     * 
     * @param val number of records
     */
    public void setMaxMemoryRecords(int val)
    {
        this.max_records = val;
    }


    /**
     * Get Max Memory Records
     * 
     * @return max records supported by device
     */
    public int getMaxMemoryRecords()
    {
        return this.max_records;
    }

}
