package ggc.cgms.device.animas.impl.data.dto;

import com.atech.utils.data.ATechDate;
import ggc.cgms.device.animas.impl.data.enums.AnimasCGMSWarningType;

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
 *  Filename:     AnimasDexcomWarning
 *  Description:  Animas Dexcom Warning
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class AnimasDexcomWarning
{
    public ATechDate dateTime;
    public AnimasCGMSWarningType warningType = AnimasCGMSWarningType.None;

    public short warningCode;

    public short[] dataBits;



    public boolean isDataEmpty()
    {
        for(short sh : dataBits)
        {
            if (sh != 0)
            {
                return false;
            }
        }

        return true;
    }



    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("AnimasCGMSWarning [");

        if (warningType == AnimasCGMSWarningType.None)
        {
            sb.append("Not Set !]");
            return sb.toString();
        }

        sb.append("dateTime=" + dateTime.getDateTimeString());

        if (warningType == AnimasCGMSWarningType.Unknown)
        {
            sb.append(", type=Unknown, typeCode=" + this.warningCode);
        }
        else
        {
            sb.append(", type=" + warningType.name());
        }


        if (!isDataEmpty())
        {
            sb.append(", dataBits=[");

            for(short sh : dataBits)
            {
                sb.append(sh + " ");
            }
        }

        sb.append("]");

        return sb.toString();
    }

}
