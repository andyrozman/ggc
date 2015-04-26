package ggc.cgms.device.dexcom.receivers.g4receiver.converter.data;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.utils.file.BytesFileUtil;

import ggc.cgms.device.dexcom.receivers.g4receiver.enums.ReceiverRecordType;
import ggc.cgms.device.dexcom.receivers.g4receiver.internal.DatabasePage;

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
public class DataPageToFileConverter
{

    Log log = LogFactory.getLog(DataPageToFileConverter.class);


    public void convert(DatabasePage page, int pageNumber, ReceiverRecordType recordType)
    {
        try
        {
            File f = new File(recordType.name() + "_page_" + pageNumber + "_header.bin");
            BytesFileUtil.writeBytesToFile(f, page.pageHeaderRaw);

            f = new File(recordType.name() + "_page_" + pageNumber + "_page.bin");
            BytesFileUtil.writeBytesToFile(f, page.pageData);

        }
        catch (IOException ex)
        {
            log.error("Error writing to file. Ex.:" + ex, ex);
        }
    }
}
