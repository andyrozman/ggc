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


package ggc.data.imports;

import ggc.data.DailyValuesRow;
import ggc.util.GGCProperties;
import ggc.util.I18nControl;

import javax.swing.ImageIcon;

public class MeterManager
{

    protected I18nControl m_ic = I18nControl.getInstance();

    public String[] meter_names = {
        m_ic.getMessage("NONE"),
        "GlucoCard", 
        "EuroFlash",
        "FreeStyle"
    };

    public String[] meter_classes = {
        null,
        "ggc.data.imports.GlucoCardImport",
        "ggc.data.imports.EuroFlashImport",
        "ggc.data.imports.FreeStyleImport",
    };


    public ImageIcon[] meter_pictures = {
        new ImageIcon(getClass().getResource("/icons/noMeter.gif")),
        new ImageIcon(getClass().getResource("/icons/glucocard.gif")),
        new ImageIcon(getClass().getResource("/icons/euroflash.gif")),
        new ImageIcon(getClass().getResource("/icons/freestyle.gif")),
    };



    /**
     * Constructor for SerialMeterImport.
     */
    public MeterManager()
    {
    }




    /**
     * Gets the image
     * @return Returns a ImageIcon
     */
    public ImageIcon getMeterImage(int index)
    {
        return this.meter_pictures[index];
    }


    /**
     * Gets the name
     * @return Returns a String
     */
    public String getMeterName(int index)
    {
        return this.meter_names[index];
    }


    public String[] getAvailableMeters()
    {
        return this.meter_names;
    }


    public String getMeterClassName(int index)
    {
        return this.meter_classes[index];
    }


    public String getMeterClassName(String name)
    {
	int index = 0;

	for (int i=0; i<this.meter_names.length; i++)
	{
	    if (this.meter_names.equals(name))
	    {
		index = i;
		break;
	    }
	}

	return this.meter_classes[index];
    }


/*
    public Object[] getAvailableMetersCombo()
    {
        Object oo[] = new Object[this.meter_names.length+1];
        oo[0] = this.m_ic.getMessage("NONE");

        for (int i=0; i<this.meter_names.length; i++) 
        {
            oo[i+1] = this.meter_names[i];
        }

        return oo;
    }
*/

}
