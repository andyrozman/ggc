package ggc.cgms.device.minimed.data.decoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;

import ggc.cgms.data.writer.CGMSValuesWriter;
import ggc.cgms.device.minimed.data.CGMSHistoryEntry;
import ggc.cgms.device.minimed.data.enums.CGMSHistoryEntryType;
import ggc.cgms.util.DataAccessCGMS;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.data.MinimedDataPage;
import ggc.plugin.device.impl.minimed.data.MinimedHistoryEntry;
import ggc.plugin.device.impl.minimed.data.decoder.MinimedHistoryDecoder;
import ggc.plugin.device.impl.minimed.enums.RecordDecodeStatus;
import ggc.plugin.device.impl.minimed.util.MedtronicUtil;

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
 *  Filename:     MinimedCGMSHistoryDecoder
 *  Description:  Decoder for CGMS history data. For now we support just data from GlucoseHistory command.
 *       ISIGHistory and VCntrHistory are IGNORED for now.
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class MinimedCGMSHistoryDecoder extends MinimedHistoryDecoder
{

    private static final Logger LOG = LoggerFactory.getLogger(MinimedCGMSHistoryDecoder.class);

    CGMSValuesWriter cgmsValuesWriter = null;


    public MinimedCGMSHistoryDecoder()
    {
        super(DataAccessCGMS.getInstance());
        cgmsValuesWriter = CGMSValuesWriter.getInstance(outputWriter);
    }


    public RecordDecodeStatus decodeRecord(MinimedHistoryEntry entryIn)
    {
        CGMSHistoryEntry precord = (CGMSHistoryEntry) entryIn;
        try
        {
            return decodeRecord(precord, false);
        }
        catch (Exception ex)
        {
            LOG.error("     Error decoding: type={}, ex={}", precord.getEntryType().name(), ex.getMessage(), ex);
            return RecordDecodeStatus.Error;
        }
    }


    public RecordDecodeStatus decodeRecord(CGMSHistoryEntry entry, boolean x)
    {
        // FIXME
        // TODO
        // CGMSHistoryEntry entry = (CGMSHistoryEntry) entryIn;

        if (entry.getDateTimeLength() > 0)
        {
            ATechDate dt = parseDate(entry);
            System.out.println("DT: " + dt);
        }

        // LOG.debug("decodeRecord: type={}", entry.getEntryType());

        switch ((CGMSHistoryEntryType) entry.getEntryType())
        {

            case SensorWeakSignal:
                break;
            case SensorCal:
                break;
            case SensorTimestamp:
                break;
            case BatteryChange:
                break;
            case SensorStatus:
                break;
            case DateTimeChange:
                break;
            case SensorSync:
                break;
            case CalBGForGH:
                break;
            case SensorCalFactor:
                LOG.debug("{}", entry.toString());
                break;
            case Something10:
                break;
            case Something19:
                break;

            case None:
            case DataEnd:
                break;
        }

        return RecordDecodeStatus.NotSupported;
    }


    @Override
    public void postProcess()
    {

    }


    @Override
    public List<? extends MinimedHistoryEntry> processPageAndCreateRecords(MinimedDataPage page)
            throws PlugInBaseException
    {
        List<Byte> dataClear = checkPage(page);
        return createRecords(dataClear);
    }


    private List<? extends MinimedHistoryEntry> createRecords(List<Byte> dataClearInput)
    {
        // List<MinimedHistoryEntry> listRecords = new
        // ArrayList<MinimedHistoryEntry>();

        LOG.debug("createRecords not implemented... WIP");
        // return listRecords;

        List<Byte> dataClear = reverseList(dataClearInput);

        System.out.println("Reversed:" + bitUtils.getHex(bitUtils.getByteArrayFromList(dataClear)));

        prepareStatistics();

        int counter = 0;
        int record = 0;

        List<MinimedHistoryEntry> outList = new ArrayList<MinimedHistoryEntry>();

        // create CGMS entries (without dates)
        do
        {
            int opCode = getUnsignedInt(dataClear.get(counter));
            counter++;

            CGMSHistoryEntryType entryType;

            if (opCode == 0)
            {
                // continue;
            }
            else if ((opCode > 0) && (opCode < 20))
            {
                entryType = CGMSHistoryEntryType.getByCode(opCode);

                if (entryType == CGMSHistoryEntryType.None)
                {
                    this.unknownOpCodes.put(opCode, opCode);
                    // System.out.println("Unknown code: " + opCode);

                    CGMSHistoryEntry pe = new CGMSHistoryEntry();
                    pe.setEntryType(CGMSHistoryEntryType.None);
                    pe.setOpCode(opCode);

                    // List<Byte> listRawData = new ArrayList<Byte>();
                    // listRawData.add((byte) opCode);

                    // pe.setOpCode(opCode);
                    pe.setData(Arrays.asList((byte) opCode), false);

                    // System.out.println("Record: " + pe);

                    outList.add(pe);
                }
                else
                {
                    List<Byte> listRawData = new ArrayList<Byte>();
                    listRawData.add((byte) opCode);

                    for (int j = 0; j < (entryType.getTotalLength() - 1); j++)
                    {
                        listRawData.add(dataClear.get(counter));
                        counter++;
                    }

                    CGMSHistoryEntry pe = new CGMSHistoryEntry();
                    pe.setEntryType(entryType);

                    pe.setOpCode(opCode);
                    pe.setData(listRawData, false);

                    // System.out.println("Record: " + pe);

                    outList.add(pe);
                }
            }
            else
            {
                CGMSHistoryEntry pe = new CGMSHistoryEntry();
                pe.setEntryType(CGMSHistoryEntryType.GlucoseSensorData);

                // List<Byte> listRawData = new ArrayList<Byte>();
                // listRawData.add((byte) opCode);

                // pe.setOpCode(opCode);
                pe.setData(Arrays.asList((byte) opCode), false);

                // System.out.println("Record: " + pe);

                outList.add(pe);
            }

        } while (counter < dataClear.size());

        int i = outList.size() - 1;

        for (; i >= 0; i--)
        {
            CGMSHistoryEntryType type = (CGMSHistoryEntryType) outList.get(i).getEntryType();

            if (type.getDateLength() > 0)
            {
                System.out.println("Recordccc2: " + outList.get(i));

                decodeRecord(outList.get(i));
                // 2015-04-11T14:02:00

                break;
            }

            // System.out.println("Record2: " + outList.get(i));

        }

        // System.exit(1);

        // FIXME
        for (; i >= 0; i--)
        {
            // System.out.println("Record2: " + outList.get(i));

            CGMSHistoryEntry che = (CGMSHistoryEntry) outList.get(i);

            parseDate(che);

            System.out.println("Record2: " + outList.get(i));

        }

        return outList;

    }


    private List<Byte> reverseList(List<Byte> dataClearInput)
    {

        List<Byte> outList = new ArrayList<Byte>();

        for (int i = dataClearInput.size() - 1; i > 0; i--)
        {
            outList.add(dataClearInput.get(i));
        }

        return outList;
    }


    private int parseMinutes(int one)
    {
        // minute = (one & 0b111111 )
        // return minute

        // int yourInteger = Integer.parseInt("100100101", 2);

        return (one & Integer.parseInt("0111111", 2));
    }


    private int parseHours(int one)
    {
        // def parse_hours (one):
        // return (one & 0x1F )

        return (one & 0x1F);
    }


    private int parseDay(int one)
    {
        // def parse_day (one):
        // return one & 0x1F
        return one & 0x1F;
    }


    private int parseMonths(int first_byte, int second_byte)
    {
        // def parse_months (first_byte, second_byte):
        // first_two_bits = first_byte >> 6
        // second_two_bits = second_byte >> 6
        // return (first_two_bits << 2) + second_two_bits

        int first_two_bits = first_byte >> 6;
        int second_two_bits = second_byte >> 6;

        return (first_two_bits << 2) + second_two_bits;
    }


    private int parseYear(int year)
    {
        // def parse_years_lax(year):
        // """
        // simple mask plus correction
        // """
        // y = (year & Mask.year) + 2000
        // return y
        return (year & 0x0F) + 2000;

    }


    private ATechDate parseDate(CGMSHistoryEntry entry)
    {
        if (entry.getEntryType().hasDate())
            return null;

        int data[] = entry.getDatetime();

        // def parse_date (data, unmask=False, strict=False,
        // minute_specific=False):
        // """
        // Some dates are formatted/stored down to the second (Sensor
        // CalBGForPH) while
        // others are stored down to the minute (CGM SensorTimestamp dates).
        // """
        // data = data[:]
        // seconds = 0
        // minutes = 0
        // hours = 0
        //
        // year = times.parse_years(data[0])
        // day = parse_day(data[1])
        // minutes = parse_minutes(data[2])
        //
        // hours = parse_hours(data[3])2015parse
        //
        // month = parse_months(data[3], data[2])

        // 2015-04-11T14:02:00

        // date is reversed

        if (entry.getEntryType().getDateType() == CGMSHistoryEntryType.DateType.MinuteSpecific)
        {
            ATechDate date = new ATechDate(parseDay(data[2]), parseMonths(data[0], data[1]), parseYear(data[3]),
                    parseHours(data[0]), parseMinutes(data[1]), 0, ATechDateType.DateAndTimeSec);

            // ATechDate date = new ATechDate(parseDay(data[0]),
            // parseMonths(data[2], data[1]), parseYear(data[2]),
            // parseHours(data[2]), parseMinutes(data[1]), 0,
            // ATechDateType.DateAndTimeSec);

            entry.setATechDate(date);

            return date;

        }
        else if (entry.getEntryType().getDateType() == CGMSHistoryEntryType.DateType.SecondSpecific)
        {
            LOG.warn("parseDate for SecondSpecific type is not implemented.");
            throw new RuntimeException();
            // return null;
        }
        else
            return null;

    }


    @Override
    public void refreshOutputWriter()
    {
        this.cgmsValuesWriter.setOutputWriter(MedtronicUtil.getOutputWriter());
    }


    @Override
    protected void runPostDecodeTasks()
    {
        this.showStatistics();
    }

}
