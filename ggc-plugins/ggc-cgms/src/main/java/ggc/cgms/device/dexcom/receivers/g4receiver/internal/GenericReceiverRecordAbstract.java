package ggc.cgms.device.dexcom.receivers.g4receiver.internal;

import java.util.Date;

import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomUtils;
import ggc.plugin.device.PlugInBaseException;

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
public abstract class GenericReceiverRecordAbstract implements IGenericReceiverRecord
{

    // private static final Log log =
    // LogFactory.getLog(GenericReceiverRecordAbstract.class);

    protected int systemSeconds;
    protected int displaySeconds;
    protected int crc;


    public abstract int getImplementedRecordVersion();


    public void checkRecordVersionAndSize()
    {
        // // FIXME
        // int currentVersion = 0; // read from partition info
        //
        // if (this.getImplementedRecordVersion() != getCurrentRecordVersion())
        // {
        // if (this.getImplementedRecordSize() != this.getCurrentRecordSize())
        // {
        // // FIXME
        // log.warn(String.format("Versions and sizes of object %s differ, possible data problem "
        // + "(versions/record size: implemented: %s/%s, device: %s/%s", //
        // this.getRecordType().name(), //
        // this.getImplementedRecordVersion(), this.getImplementedRecordSize(),
        // //
        // this.getCurrentRecordVersion(), this.getCurrentRecordSize()));
        // }
        // else
        // {
        // log.warn(String.format("Versions of object %s differ, possible data problem "
        // + "(versions: implemented: %s, device: %s", //
        // this.getRecordType().name(), //
        // this.getImplementedRecordVersion(), //
        // this.getCurrentRecordVersion()));
        // }
        //
        // }

    }


    public Date getSystemDate()
    {
        return DexcomUtils.getDateFromSeconds(this.systemSeconds);
    }


    public Date getDisplayDate()
    {
        return DexcomUtils.getDateFromSeconds(this.displaySeconds);
    }


    public int getCrc()
    {
        return this.crc;
    }


    public int getCurrentRecordSize() throws PlugInBaseException
    {
        return DexcomUtils.getPartition(this.getRecordType()).getRecordLength();
    }


    public int getCurrentRecordVersion() throws PlugInBaseException
    {
        return DexcomUtils.getPartition(this.getRecordType()).getRecordRevision();
    }


    public int getSystemSeconds()
    {
        return systemSeconds;
    }


    public void setSystemSeconds(int systemSeconds)
    {
        this.systemSeconds = systemSeconds;
    }


    public int getDisplaySeconds()
    {
        return displaySeconds;
    }


    public void setDisplaySeconds(int displaySeconds)
    {
        this.displaySeconds = displaySeconds;
    }


    public void setCrc(int crc)
    {
        this.crc = crc;
    }

}
