package ggc.plugin.device.impl.minimed.data.decoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.BitUtils;

import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.data.MinimedDataPage;
import ggc.plugin.device.impl.minimed.data.MinimedHistoryEntry;
import ggc.plugin.device.impl.minimed.enums.RecordDecodeStatus;
import ggc.plugin.device.impl.minimed.util.MinimedUtil;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
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
 *  Filename:     DeviceIdentification
 *  Description:  Class for display of Device Identification.
 *
 *  Author: Andy {andy@atech-software.com}
 */
public abstract class MinimedHistoryDecoder
{

    private static final Logger LOG = LoggerFactory.getLogger(MinimedHistoryDecoder.class);

    protected OutputWriter outputWriter;
    protected BitUtils bitUtils;
    DataAccessPlugInBase dataAccess;

    // STATISTICS (remove at later time or not)
    protected boolean statisticsEnabled = true;
    protected Map<Integer, Integer> unknownOpCodes;
    protected Map<RecordDecodeStatus, Map<String, String>> mapStatistics;


    public MinimedHistoryDecoder(DataAccessPlugInBase dataAccess)
    {
        this.dataAccess = dataAccess;
        this.bitUtils = MinimedUtil.getBitUtils();
    }


    public abstract RecordDecodeStatus decodeRecord(MinimedHistoryEntry record);


    public abstract void postProcess();


    public abstract void refreshOutputWriter();


    public boolean decodePage(MinimedDataPage dataPage) throws PlugInBaseException
    {
        refreshOutputWriter();

        List<? extends MinimedHistoryEntry> minimedHistoryRecords = processPageAndCreateRecords(dataPage);

        for (MinimedHistoryEntry record : minimedHistoryRecords)
        {
            decodeRecord(record);
        }

        runPostDecodeTasks();

        return true;
    }


    protected abstract void runPostDecodeTasks();


    // TODO_ extend this to also use bigger pages (for now we support only 1024
    // pages)
    public List<Byte> checkPage(MinimedDataPage page) throws PlugInBaseException
    {
        List<Byte> byteList = new ArrayList<Byte>();

        if (page.rawDataPage.length != page.commandType.getRecordLength())
        {
            LOG.error(String.format("Page size is not correct. Size should be %d, but it was %d instead.",
                page.commandType.getRecordLength(), page.rawDataPage.length));
            // throw exception perhaps
            return byteList;
        }

        if (MinimedUtil.getDeviceType() == null)
        {
            LOG.error("Device Type is not defined.");
            return byteList;
        }

        byte[] data = bitUtils.getByteSubArray(page.rawDataPage, 0, 1022);

        LOG.debug("RawData: {}", bitUtils.getHex(page.rawDataPage));
        LOG.debug("Data: {}", bitUtils.getHex(data));

        int crcCalculated = bitUtils.computeCRC16CCITT(data, 0, 1022);
        int crcStored = bitUtils.toInt(page.rawDataPage[1022], page.rawDataPage[1023]);

        if (crcCalculated != crcStored)
        {
            LOG.error(String.format("Stored CRC (%d) is different than calculated (%d), but ignored for now.",
                crcStored, crcCalculated));
            // return byteList;
        }
        else
        {
            if (MinimedUtil.isLowLevelDebug())
                LOG.debug("CRC ok.");
        }

        return bitUtils.getListFromByteArray(data);
    }


    public abstract List<? extends MinimedHistoryEntry> processPageAndCreateRecords(MinimedDataPage page)
            throws PlugInBaseException;


    public OutputWriter getOutputWriter()
    {
        return outputWriter;
    }


    public void setOutputWriter(OutputWriter outputWriter)
    {
        this.outputWriter = outputWriter;
    }


    protected void prepareStatistics()
    {
        if (!statisticsEnabled)
            return;

        unknownOpCodes = new HashMap<Integer, Integer>();
        mapStatistics = new HashMap<RecordDecodeStatus, Map<String, String>>();

        for (RecordDecodeStatus stat : RecordDecodeStatus.values())
        {
            mapStatistics.put(stat, new HashMap<String, String>());
        }
    }


    protected void addToStatistics(MinimedHistoryEntry pumpHistoryEntry, RecordDecodeStatus status, Integer opCode)
    {
        if (!statisticsEnabled)
            return;

        if (opCode != null)
        {
            if (!unknownOpCodes.containsKey(opCode))
            {
                unknownOpCodes.put(opCode, opCode);
            }
            return;
        }

        if (!mapStatistics.get(status).containsKey(pumpHistoryEntry.getEntryType().name()))
        {
            mapStatistics.get(status).put(pumpHistoryEntry.getEntryType().name(), "");
        }
    }


    protected void showStatistics()
    {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry unknownEntry : unknownOpCodes.entrySet())
        {
            DataAccessPlugInBase.appendToStringBuilder(sb, "" + unknownEntry.getKey(), ", ");
        }

        LOG.debug("STATISTICS OF PUMP DECODE");

        if (unknownOpCodes.size() > 0)
        {
            LOG.debug("Unknown Op Codes: {}", sb.toString());
        }

        for (Map.Entry<RecordDecodeStatus, Map<String, String>> entry : mapStatistics.entrySet())
        {
            sb = new StringBuilder();

            if (entry.getKey() != RecordDecodeStatus.OK)
            {
                if (entry.getValue().size() == 0)
                    continue;

                for (Map.Entry<String, String> entrysub : entry.getValue().entrySet())
                {
                    DataAccessPlugInBase.appendToStringBuilder(sb, entrysub.getKey(), ", ");
                }

                String spaces = StringUtils.repeat(" ", 14 - entry.getKey().name().length());

                LOG.debug("    {}{} - {}. Elements: {}", entry.getKey().name(), spaces, entry.getValue().size(),
                    sb.toString());
            }
            else
            {
                LOG.debug("    {}             - {}", entry.getKey().name(), entry.getValue().size());
            }
        }
    }


    private int getUnsignedByte(byte value)
    {
        if (value < 0)
            return value + 256;
        else
            return value;
    }


    protected int getUnsignedInt(int value)
    {
        if (value < 0)
            return value + 256;
        else
            return value;
    }


    public String getFormattedFloat(float value, int decimals)
    {
        return dataAccess.getFormatedValueUS(value, decimals);
    }

}
