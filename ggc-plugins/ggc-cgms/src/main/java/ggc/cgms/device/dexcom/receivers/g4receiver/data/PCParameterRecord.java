package ggc.cgms.device.dexcom.receivers.g4receiver.data;

import ggc.cgms.device.dexcom.receivers.g4receiver.enums.ReceiverRecordType;

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
public class PCParameterRecord extends XmlRecord
{

    // Version 1 Size = 500
    // public int SystemSeconds; (4)
    // public int DisplaySeconds; (4)
    // [MarshalAs(UnmanagedType.ByValTStr, SizeConst=490)] (490)
    // public string XmlData;
    // public ushort m_crc; (2)

    public PCParameterRecord()
    {
    }


    public ReceiverRecordType getRecordType()
    {
        return ReceiverRecordType.PCSoftwareParameter;
    }


    @Override
    public XmlRecord createObject()
    {
        return new PCParameterRecord();
    }

}
