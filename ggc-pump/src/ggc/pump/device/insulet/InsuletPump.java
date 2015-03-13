package ggc.pump.device.insulet;

import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;
import ggc.pump.device.AbstractPump;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
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
 *  Filename:     AccuChekSpirit  
 *  Description:  Accu Chek Spirit Pump Implementation
 * 
 *  Author: Andy {andy@atech-software.com}
 */

// FIXME

public abstract class InsuletPump extends AbstractPump
{

    /**
     * Constructor 
     */
    public InsuletPump()
    {
        super();
    }

    /**
     * Constructor 
     * 
     * @param params 
     * @param writer 
     */
    public InsuletPump(String params, OutputWriter writer)
    {
        super(params, writer);
    }

    /**
     * Constructor
     * 
     * @param params
     * @param writer
     * @param da 
     */
    public InsuletPump(String params, OutputWriter writer, DataAccessPlugInBase da)
    {
        super(params, writer, da);
    }

    /**
     * Constructor
     * 
     * @param cmp
     */
    public InsuletPump(AbstractDeviceCompany cmp)
    {
        super(cmp);
    }

    // ************************************************
    // *** Meter Identification Methods ***
    // ************************************************

    /**
     * getComment - Get Comment for device 
     * 
     * @return comment or null
     */
    public String getComment()
    {
        return null;
    }

    /**
     * getImplementationStatus - Get Implementation Status 
     * 
     * @return implementation status as number
     * @see ggc.plugin.manager.DeviceImplementationStatus
     */
    public DeviceImplementationStatus getImplementationStatus()
    {
        return DeviceImplementationStatus.NotAvailable;
    }

    /**
     * Open
     */
    public boolean open() throws PlugInBaseException
    {
        return true;
    }

    /**
     * Close
     */
    public void close() throws PlugInBaseException
    {
    }

    /** 
     * This is method for reading configuration, in case that dump doesn't give this information.
     * 
     * @throws PlugInBaseException
     */
    @Override
    public void readConfiguration() throws PlugInBaseException
    {
    }

    /**
     * readDeviceDataFull - This is method for reading data from device. All reading from actual device should 
     * be done from here. Reading can be done directly here, or event can be used to read data. Usage of events 
     * is discouraged because reading takes 3-4x more time.
     * 
     * @throws PlugInBaseException 
     */
    public void readDeviceDataFull() throws PlugInBaseException
    {
    }

    /**
     * This is method for reading partial data from device. This can be used if your device can be read partialy 
     * (from some date to another)
     * 
     * @throws PlugInBaseException 
     */
    @Override
    public void readDeviceDataPartitial() throws PlugInBaseException
    {
    }

    /**
     * This is for reading device information. This should be used only if normal dump doesn't retrieve this
     * information (most dumps do).
     *  
     * @throws PlugInBaseException
     */
    @Override
    public void readInfo() throws PlugInBaseException
    {
    }

    /**
     * getDeviceInfo - get Device info (firmware and software revision)
     * @return 
     */
    @Override
    public DeviceIdentification getDeviceInfo()
    {
        return null;
    }


    /** 
     * Dispose
     */
    public void dispose()
    {
    }

    /**
     * getConnectionPort - connection port data
     * 
     * @return connection port as string
     */
    public String getConnectionPort()
    {
        return null;
    }



    /**
     * Is Device Communicating
     * 
     * @return
     */
    public boolean isDeviceCommunicating()
    {
        return false;
    }

    /**
     * Is Device Readable (there are some devices that are not actual devices, but are used to get some
     * sort of specific device data - in most cases we call them generics, and they don't have ability
     * to read data)
     * 
     * @return
     */
    @Override
    public boolean isReadableDevice()
    {
        return false;
    }

    /**
     * Get Download Support Type
     * 
     * @return
     */
    @Override
    public DownloadSupportType getDownloadSupportType()
    {
        return DownloadSupportType.NoDownloadSupport;
    }

}

/*
 * Title: Java Servlet Programming
 * Second Edition: April 2001
 * ISBN: 0-596-00040-5
 * http://www.oreilly.com/catalog/jservlet2/
 */
// Copyright (C) 1999-2001 by Jason Hunter <jhunter_AT_acm_DOT_org>.
// All rights reserved. Use of this class is limited.
// Please see the LICENSE for more information.
/*
 * package com.oreilly.servlet.multipart;
 * import java.io.OutputStream;
 * import java.io.FilterOutputStream;
 * import java.io.IOException;
 * /**
 * A <code>MacBinaryDecoderOutput</code> filters MacBinary files to normal
 * files on the fly; optimized for speed more than readability.
 * @author Jason Hunter
 */
/*
 * private class MacBinaryDecoderOutputStream extends FilterOutputStream {
 * private int bytesFiltered = 0;
 * private int dataForkLength = 0;
 * public MacBinaryDecoderOutputStream(OutputStream out) {
 * super(out);
 * }
 * public void write(int b) throws IOException {
 * // Bytes 83 through 86 are a long representing the data fork length
 * // Check <= 86 first to short circuit early in the common case
 * if (bytesFiltered <= 86 && bytesFiltered >= 83) {
 * int leftShift = (86 - bytesFiltered) * 8;
 * dataForkLength = dataForkLength | (b & 0xff) << leftShift;
 * }
 * // Bytes 128 up to (128 + dataForkLength - 1) are the data fork
 * else if (bytesFiltered < (128 + dataForkLength) && bytesFiltered >= 128) {
 * out.write(b);
 * }
 * bytesFiltered++;
 * }
 * public void write(byte b[]) throws IOException {
 * write(b, 0, b.length);
 * }
 * public void write(byte b[], int off, int len) throws IOException {
 * // If the write is for content past the end of the data fork, ignore
 * if (bytesFiltered >= (128 + dataForkLength)) {
 * bytesFiltered += len;
 * }
 * // If the write is entirely within the data fork, write it directly
 * else if (bytesFiltered >= 128 &&
 * (bytesFiltered + len) <= (128 + dataForkLength)) {
 * out.write(b, off, len);
 * bytesFiltered += len;
 * }
 * // Otherwise, do the write a byte at a time to get the logic above
 * else {
 * for (int i = 0 ; i < len ; i++) {
 * write(b[off + i]);
 * }
 * }
 * }
 * }
 */
