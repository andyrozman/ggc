package ggc.cgms.device.dexcom.receivers.g4receiver.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
public class PartitionInfo
{

    int schemaVersion;
    int pageHeaderVersion;
    int pageDataLength;
    private List<Partition> partitions = new ArrayList<Partition>();
    private HashMap<ReceiverRecordType, Partition> partitionsMap = new HashMap<ReceiverRecordType, Partition>();


    public int getSchemaVersion()
    {
        return this.schemaVersion;
    }


    public void setSchemaVersion(int schemaVersion)
    {
        this.schemaVersion = schemaVersion;
    }


    public int getPageHeaderVersion()
    {
        return this.pageHeaderVersion;
    }


    public void setPageHeaderVersion(int pageHeaderVersion)
    {
        this.pageHeaderVersion = pageHeaderVersion;
    }


    public int getPageDataLength()
    {
        return this.pageDataLength;
    }


    public void setPageDataLength(int pageDataLength)
    {
        this.pageDataLength = pageDataLength;
    }


    public List<Partition> getPartitions()
    {
        return this.partitions;
    }


    public void addPartition(Partition p)
    {
        this.partitions.add(p);
        this.partitionsMap.put(p.getRecordType(), p);
    }


    public Partition getPartitionByRecordType(ReceiverRecordType recordType)
    {
        return this.partitionsMap.get(recordType);
    }

}
