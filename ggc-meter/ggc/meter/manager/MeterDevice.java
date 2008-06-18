/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: MeterManager.java
 *  Purpose:  This class contains all definitions for Meters. This includes:
 *        meter names, classes that handle meter and all other relevant data.
 *
 *  Author:   andyrozman
 */


package ggc.meter.manager; 

import ggc.meter.util.DataAccess;
import ggc.meter.util.I18nControl;

import javax.swing.ImageIcon;

public class MeterDevice
{

    protected I18nControl m_ic = I18nControl.getInstance();
    protected DataAccess m_da = DataAccess.getInstance();
    



    public String id;
    public String group;
    public String name;
    public String picture;
    public String class_name;
    public String unpacked_status;
    

    public MeterDevice(String id, String group, String name, String picture, String class_name, String status)
    {
    	this.id = id;
    	this.group = group;
    	this.name = name;
    	this.picture = picture;
    	this.class_name = class_name;
    	this.unpacked_status = status;
    }
    
    
    public String getFullName()
    {
    	return group + "_" + id;
    }



    /**
     * Gets the image
     * @return Returns a ImageIcon
     */
    public ImageIcon getMeterImage()
    {
        //return this.picture;
        
        return null;
    }


    /**
     * Gets the name
     * @return Returns a String
     */
    public String getMeterName()
    {
        return this.name;
    }


    public String getMeterClassName()
    {
        return this.class_name;
    }


}
