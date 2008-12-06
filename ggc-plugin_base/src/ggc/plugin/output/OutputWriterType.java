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
 *  Filename:     OutputWriterType
 *  Description:  Types of Output Writers
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public abstract class OutputWriterType 
{
    
    /**
     * Output Writer Type: None
     */
    public static final int NONE = 0;
    
    /**
     * Output Writer Type: Console
     */
    public static final int CONSOLE = 1;
    
    /**
     * Output Writer Type: Dummy
     */
    public static final int DUMMY = 2;
    
    /**
     * Output Writer Type: File
     */
    public static final int FILE = 3;
    
    /**
     * Output Writer Type: GGC File Export
     */
    public static final int GGC_FILE_EXPORT = 4;
    
    /**
     * Output Writer Type: Extended
     */
    public static final int EXTENDED = 10;
    
    
}