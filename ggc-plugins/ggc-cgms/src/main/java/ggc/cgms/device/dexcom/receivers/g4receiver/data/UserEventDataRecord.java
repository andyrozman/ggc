//
// Translated by CS2J (http://www.cs2j.com): 15.8.2014 0:18:14
//

package ggc.cgms.device.dexcom.receivers.g4receiver.data;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import ggc.cgms.device.dexcom.receivers.g4receiver.enums.ReceiverRecordType;
import ggc.cgms.device.dexcom.receivers.g4receiver.enums.UserEvent;
import ggc.cgms.device.dexcom.receivers.g4receiver.internal.GenericReceiverRecordAbstract;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomUtils;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomUtils.DexcomDateParsing;
import ggc.core.data.defs.ExerciseStrength;
import ggc.core.data.defs.Health;

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
 *  Filename:     Dexcom 7
 *  Description:  Dexcom 7 implementation (just settings)
 *
 *  Author: Andy {andy@atech-software.com}
 */
public class UserEventDataRecord extends GenericReceiverRecordAbstract
{

    // Version Size: 20
    // public int SystemSeconds; 4
    // public int DisplaySeconds; 4
    // public UserEvent EventType; 1
    // public byte EventSubType; 1
    // public int EventTime; 4
    // public int EventValue; 4
    // public ushort m_crc; 2

    public UserEvent eventType;
    public byte eventSubType;
    public int eventTime;
    public int eventValue;


    public UserEventDataRecord()
    {
    }


    // public XmlElement toXml(XmlDocument xOwner) throws Exception
    // {
    // XObject obj2 = new XObject("EventRecord", xOwner);
    //
    // obj2.SetAttribute("EventType", this.getEventType().ToString());
    // obj2.setAttribute("EventSubType", this.getEventSubType());
    // obj2.setAttribute("EventTime", this.getEventTime());
    // obj2.setAttribute("EventValue", this.getEventValue());
    // obj2.setAttribute("EventTypeDescription",
    // this.getEventTypeDescription());
    // obj2.setAttribute("EventValueDescription",
    // this.getEventValueDescription());
    // return obj2.getElement();
    // }

    public byte getEventSubType()
    {
        return this.eventSubType;
    }


    public Date getEventTime()
    {
        return DexcomUtils.getDateFromSeconds(this.eventTime, DexcomDateParsing.DateWithDifference);
    }


    public UserEvent getEventType()
    {
        return this.eventType;
    }


    public String getEventTypeDescription()
    {
        if (this.eventType == null)
            return "Unknown";

        switch (eventType)
        {
            case Carbs:
                return "Carbs";
            case Insulin:
                return "Insulin";
            case Health:
                {
                    Health health = Health.getByCode(this.eventSubType);

                    return health.getTranslation();
                }
            case Exercise:
                {
                    ExerciseStrength exercise = ExerciseStrength.getByCode(this.eventSubType);

                    return exercise.getTranslation();
                }
            default:
                break;

        }
        return "Unknown";
    }


    public int getEventValue()
    {
        return this.eventValue;
    }


    public String getEventValueDescription()
    {
        if (this.eventType == null)
            return null;

        switch (this.eventType)
        {
            case Carbs:
                return String.format("%s %s", this.eventValue, "grams");

            case Insulin:
                {
                    double num = this.eventValue / 100.0;
                    return String.format("%5.2f %s", num, "units");
                }

            case Health:
                return "";

            case Exercise:
                return String.format("%s %s", this.eventValue, "minutes");

            default:
                return "";

        }

    }


    public ReceiverRecordType getRecordType()
    {
        return ReceiverRecordType.UserEventData;
    }


    public int getImplementedRecordSize()
    {
        return 20;
    }


    @Override
    public int getImplementedRecordVersion()
    {
        return 1;
    }


    public void setEventType(UserEvent eventType)
    {
        this.eventType = eventType;
    }


    public void setEventSubType(byte eventSubType)
    {
        this.eventSubType = eventSubType;
    }


    public void setEventTime(int eventTime)
    {
        this.eventTime = eventTime;
    }


    public void setEventValue(int eventValue)
    {
        this.eventValue = eventValue;
    }


    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("UserEventData [");
        sb.append("DisplaySeconds=" + DexcomUtils.getDateTimeString(this.getDisplayDate()));
        sb.append(", EventTypeDesc=" + this.getEventTypeDescription());

        String desc = getEventValueDescription();

        if (StringUtils.isNotBlank(desc))
        {
            sb.append(", EventValueDesc=" + this.getEventValueDescription());
        }

        sb.append("]");

        return sb.toString();
    }

}
