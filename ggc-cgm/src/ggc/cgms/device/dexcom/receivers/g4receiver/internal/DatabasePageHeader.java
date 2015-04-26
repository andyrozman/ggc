package ggc.cgms.device.dexcom.receivers.g4receiver.internal;

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
public class DatabasePageHeader
{

    public int firstRecordIndex;
    public int numberOfRecords;
    public ReceiverRecordType recordType = ReceiverRecordType.Aberration;
    public byte revision;
    public int pageNumber;
    public int reserved2;
    public int reserved3;
    public int reserved4;
    public int crc;


    public int getFirstRecordIndex()
    {
        return firstRecordIndex;
    }


    public void setFirstRecordIndex(int firstRecordIndex)
    {
        this.firstRecordIndex = firstRecordIndex;
    }


    public int getNumberOfRecords()
    {
        return numberOfRecords;
    }


    public void setNumberOfRecords(int numberOfRecords)
    {
        this.numberOfRecords = numberOfRecords;
    }


    public ReceiverRecordType getRecordType()
    {
        return recordType;
    }


    public void setRecordType(ReceiverRecordType recordType)
    {
        this.recordType = recordType;
    }


    public byte getRevision()
    {
        return revision;
    }


    public void setRevision(byte revision)
    {
        this.revision = revision;
    }


    public int getPageNumber()
    {
        return pageNumber;
    }


    public void setPageNumber(int pageNumber)
    {
        this.pageNumber = pageNumber;
    }


    public int getReserved2()
    {
        return reserved2;
    }


    public void setReserved2(int reserved2)
    {
        this.reserved2 = reserved2;
    }


    public int getReserved3()
    {
        return reserved3;
    }


    public void setReserved3(int reserved3)
    {
        this.reserved3 = reserved3;
    }


    public int getReserved4()
    {
        return reserved4;
    }


    public void setReserved4(int reserved4)
    {
        this.reserved4 = reserved4;
    }


    public int getCrc()
    {
        return crc;
    }


    public void setCrc(int crc)
    {
        this.crc = crc;
    }
}
