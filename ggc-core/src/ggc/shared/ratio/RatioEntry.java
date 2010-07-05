package ggc.shared.ratio;

/**
 *  Application:   GGC - GNU Gluco Control
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
 *  Filename:     RatioEntry  
 *  Description:  
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class RatioEntry
{
    int from;
    int to;
    
    float ch_insulin = 0.0f;
    float bg_insulin = 0.0f;
    float ch_bg = 0.0f;
    
    float procent = 100.0f;
    
    
    /**
     * Get Column Value
     * 
     * @param column
     * @return
     */
    public String getColumnValue(int column)
    {
        switch(column)
        {
        case 1:
            return "" + this.from;
        case 2:
            return "" + this.to;
        case 3:
            return "" + this.procent;
            default:
                return "";
        }
        
        
    }
    
    
    /**
     * Get Columns
     * 
     * @return
     */
    public int getColumns()
    {
        return 3;
    }
    
    
    
    
    
}
