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


package ggc.cgm.manager; 

import ggc.cgm.util.DataAccessCGM;
import ggc.cgm.util.I18nControl;

import java.util.Hashtable;

public class CGMCompany
{

    protected I18nControl m_ic = I18nControl.getInstance();
    protected DataAccessCGM m_da = DataAccessCGM.getInstance();
    
    public String id = "";
    public String name = "";
    public int index = 0;
    
    Hashtable<String,CGMDevice> devices = new Hashtable<String,CGMDevice>();


    public CGMCompany(int index, String id, String name)
    {
    	this.index = index;
    	this.id = id;
    	this.name = name;
    }


    public void addDevice(CGMDevice md)
    {
    	this.devices.put(md.id, md);
    }

    public String toString()
    {
    	return "Meter Company [index=" + index + ",id=" + id + ",name=" + name + "]";
    }
    
    
}
