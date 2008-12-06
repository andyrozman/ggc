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


package ggc.plugin.manager; 


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
 *  Filename:     MgrDevice  
 *  Description:  Device used in manager.
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class MgrDevice
{


    private String id;
    private String group;
    private String name;
    private String picture;
    private String class_name;
    @SuppressWarnings("unused")
    private String unpacked_status;
    

    /**
     * Constructor 
     * 
     * @param id
     * @param group
     * @param name
     * @param picture
     * @param class_name
     * @param status
     */
    public MgrDevice(String id, String group, String name, String picture, String class_name, String status)
    {
    	this.id = id;
    	this.group = group;
    	this.name = name;
    	this.picture = picture;
    	this.class_name = class_name;
    	this.unpacked_status = status;
    }
    
    
    /**
     * Get Full Name
     * 
     * @return full name
     */
    public String getFullName()
    {
    	return group + "_" + id;
    }



    /**
     * Gets the image name
     * 
     * @return name of image
     */
    public String getDeviceImageName()
    {
        return this.picture;
    }


    /**
     * Gets the name
     * 
     * @return Returns a String
     */
    public String getDeviceName()
    {
        return this.name;
    }


    /**
     * Get Device Class Name
     * 
     * @return device class name
     */
    public String getDeviceClassName()
    {
        return this.class_name;
    }


}
