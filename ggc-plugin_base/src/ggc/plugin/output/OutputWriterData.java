package ggc.plugin.output;

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
 *  Filename:     OutputWriterData  
 *  Description:  Data for Output Writer.
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public interface OutputWriterData
{

    /**
     * Get Data As String
     * 
     * @return data as string
     */
    public abstract String getDataAsString();

    /**
     * Is Data BG
     * 
     * @return true if data is BG
     */
    public abstract boolean isDataBG();

    /**
     * Set Output Type
     * 
     * @param type output type as defined in OutputWriterType
     */
    public abstract void setOutputType(int type);

}
