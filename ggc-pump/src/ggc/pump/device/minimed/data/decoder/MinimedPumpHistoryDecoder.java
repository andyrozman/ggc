package ggc.pump.device.minimed.data.decoder;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;
import com.atech.utils.data.CodeEnumWithTranslation;

import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.data.MinimedDataPage;
import ggc.plugin.device.impl.minimed.data.MinimedHistoryEntry;
import ggc.plugin.device.impl.minimed.data.decoder.MinimedHistoryDecoder;
import ggc.plugin.device.impl.minimed.enums.MinimedDeviceType;
import ggc.plugin.device.impl.minimed.enums.RecordDecodeStatus;
import ggc.plugin.device.impl.minimed.util.MinimedUtil;
import ggc.pump.data.defs.*;
import ggc.pump.data.dto.BolusDTO;
import ggc.pump.data.dto.BolusWizardDTO;
import ggc.pump.data.dto.TemporaryBasalRateDTO;
import ggc.pump.data.writer.PumpValuesWriter;
import ggc.pump.device.minimed.data.PumpHistoryEntry;
import ggc.pump.device.minimed.data.enums.PumpHistoryEntryType;
import ggc.pump.util.DataAccessPump;

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
 *  Filename:     MinimedPumpHistoryDecoder
 *  Description:  Decoder for history data.
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class MinimedPumpHistoryDecoder extends MinimedHistoryDecoder
{

    private static final Logger LOG = LoggerFactory.getLogger(MinimedPumpHistoryDecoder.class);

    PumpValuesWriter pumpValuesWriter = null;

    // DataAccessPlugInBase dataAccess = DataAccessPump.getInstance();

    // Temporary records for processing
    private PumpHistoryEntry tbrPreviousRecord;
    private PumpHistoryEntry changeTimeRecord;


    public MinimedPumpHistoryDecoder()
    {
        super(DataAccessPump.getInstance());
    }


    public void createPumpValuesWriter()
    {
        if (pumpValuesWriter == null)
        {
            pumpValuesWriter = PumpValuesWriter.getInstance(outputWriter);
        }
    }


    public List<? extends MinimedHistoryEntry> processPageAndCreateRecords(MinimedDataPage page)
            throws PlugInBaseException
    {
        createPumpValuesWriter();
        List<Byte> dataClear = checkPage(page);
        return createRecords(dataClear);
    }


    public List<? extends MinimedHistoryEntry> createRecords(List<Byte> dataClear)
    {
        prepareStatistics();

        int counter = 0;
        int record = 0;

        List<MinimedHistoryEntry> outList = new ArrayList<MinimedHistoryEntry>();

        do
        {
            int opCode = dataClear.get(counter);
            boolean special = false;

            if (opCode == 0)
            {
                counter++;
                continue;
            }

            PumpHistoryEntryType entryType = PumpHistoryEntryType.getByCode(opCode);

            PumpHistoryEntry pe = new PumpHistoryEntry();
            pe.setEntryType(entryType);

            // System.out.println("OpCode: " + opCode);

            counter++;

            List<Byte> listRawData = new ArrayList<Byte>();
            listRawData.add((byte) opCode);

            if (entryType == PumpHistoryEntryType.UnabsorbedInsulin)
            {
                int elements = dataClear.get(counter);
                listRawData.add((byte) elements);
                counter++;

                int els = getUnsignedInt(elements);

                for (int k = 0; k < (els - 2); k++)
                {
                    listRawData.add((byte) dataClear.get(counter));
                    counter++;
                }

                special = true;
            }
            else
            {

                for (int j = 0; j < (entryType.getTotalLength() - 1); j++)
                {
                    listRawData.add(dataClear.get(counter));
                    counter++;
                }

            }

            if (entryType == PumpHistoryEntryType.None)
            {
                LOG.error("Error in code. We should have not come intpo this branch.");
                // System.out.println("!!! Unknown Entry: 0x" +
                // bitUtils.getCorrectHexValue(opCode) + "[" + opCode + "]");
                //
                // addToStatistics(null, null, opCode);
                // counter += 6; // we assume this is unknown packet with size
                // // 2,5,0 (standard packet)
                //
                // pe.setEntryType(PumpHistoryEntryType.UnknownBasePacket);
                // pe.setOpCode(opCode);

            }
            else
            {

                // System.out.println(pe.getEntryType());

                if (pe.getEntryType() == PumpHistoryEntryType.UnknownBasePacket)
                {
                    pe.setOpCode(opCode);
                }

                if (entryType.getHeadLength() == 0)
                    special = true;

                pe.setData(listRawData, special);

                RecordDecodeStatus decoded = decodeRecord(pe);

                // if (pe.getEntryType() ==
                // PumpHistoryEntryType.UnknownBasePacket)
                // {
                // decoded = RecordDecodeStatus.Unknown;
                // }
                // else
                // {
                // decoded = decodeRecord(pe);
                // }

                // System.out.println("Found entry: " + entryType.name() +
                // " ["
                // + bitUtils.getDebugByteListHex(listRawData) + "] ");

                // FIXME

                // if (!decoded)
                // System.out.println("#" + record + " " + pe);

                // if (decoded)
                // LOG.info("#" + record + " " + pe);
                // else
                // LOG.warn("#" + record + " BAD: " + pe);

                if ((decoded == RecordDecodeStatus.OK) || (decoded == RecordDecodeStatus.Ignored))
                {
                    LOG.info("#" + record + " " + decoded.getDescription() + " " + pe);
                }
                else
                {
                    LOG.warn("#" + record + " " + decoded.getDescription() + "  " + pe);
                }

                addToStatistics(pe, decoded, null);

                record++;

                outList.add(pe);
            }

            // System.out.println("Counter: " + counter);

            // if (counter > 100)
            // break;

        } while (counter < dataClear.size());

        return outList;
    }


    public RecordDecodeStatus decodeRecord(MinimedHistoryEntry entryIn)
    {
        PumpHistoryEntry precord = (PumpHistoryEntry) entryIn;
        try
        {
            return decodeRecord(entryIn, false);
        }
        catch (Exception ex)
        {
            LOG.error("     Error decoding: type={}, ex={}", precord.getEntryType().name(), ex.getMessage(), ex);
            return RecordDecodeStatus.Error;
        }
    }


    public RecordDecodeStatus decodeRecord(MinimedHistoryEntry entryIn, boolean x)
    {
        // FIXME
        // TODO
        PumpHistoryEntry entry = (PumpHistoryEntry) entryIn;

        if (entry.getDateTimeLength() > 0)
        {
            decodeDateTime(entry);
        }

        // LOG.debug("decodeRecord: type={}", entry.getEntryType());

        switch ((PumpHistoryEntryType) entry.getEntryType())
        {
            // not implemented
            case ChangeBasalProfile_NewProfile:
            case ChangeBasalProfile_OldProfile:
            case SelectBasalProfile:
            case Model522ResultTotals:
            case Ian69:
            case Ian50:
            case Ian54:
            case IanA8:
            case Sara6E:
                return RecordDecodeStatus.NotSupported;

            case AndyB4:
                return RecordDecodeStatus.NotSupported;

            // WORK IN PROGRESS

            // POSSIBLY READY

            case BasalProfileStart:
                return decodeBasalProfileStart(entry);

            // **** Implemented records ****

            case ChangeTime:
                changeTimeRecord = entry;
                return RecordDecodeStatus.OK;

            case NewTimeSet:
                decodeChangeTime(entry);
                return RecordDecodeStatus.OK;

            case CalBGForPH:
                decodeCalBGForPH(entry);
                return RecordDecodeStatus.OK;

            case TempBasalDuration:
                decodeTempBasal(entry);
                return RecordDecodeStatus.OK;

            case TempBasalRate:
                decodeTempBasal(entry);
                return RecordDecodeStatus.OK;

            case Bolus:
                decodeBolus(entry);
                return RecordDecodeStatus.OK;

            case EndResultTotals:
                decodeEndResultTotals(entry);
                return RecordDecodeStatus.OK;

            case BatteryActivity:
                decodeBatteryActivity(entry);
                return RecordDecodeStatus.OK;

            case LowReservoir:
                decodeLowReservoir(entry);
                return RecordDecodeStatus.OK;

            case LowBattery:
                this.writeData(PumpBaseType.Event, PumpEventType.BatteryLow, entry.getATechDate());
                return RecordDecodeStatus.OK;

            case PumpSuspend:
                this.writeData(PumpBaseType.Event, PumpEventType.BasalStop, entry.getATechDate());
                return RecordDecodeStatus.OK;

            case PumpResume:
                this.writeData(PumpBaseType.Event, PumpEventType.BasalRun, entry.getATechDate());
                return RecordDecodeStatus.OK;

            case Rewind:
                this.writeData(PumpBaseType.Event, PumpEventType.CartridgeRewind, entry.getATechDate());
                return RecordDecodeStatus.OK;

            case ChangeRemoteId:
                this.writeData(PumpBaseType.Event, PumpEventType.ChangeRemoteId, entry.getATechDate());
                return RecordDecodeStatus.OK;

            case NoDeliveryAlarm:
                this.writeData(PumpBaseType.Alarm, PumpAlarms.NoDelivery, entry.getATechDate());
                return RecordDecodeStatus.OK;

            case BolusWizard:
                decodeBolusWizard(entry);
                return RecordDecodeStatus.OK;

            case Prime:
                decodePrime(entry);
                return RecordDecodeStatus.OK;

            // **** Ignored Records - PUMP ****
            case ClearAlarm:
            case ChangeTimeDisplay:
            case ChangeAlarmNotifyMode: // ChangeUtility:
            case ToggleRemote:
            case UnabsorbedInsulin:
            case None:
                // LOG.debug(" -- ignored Pump Entry: " +
                // entry.getEntryType().name());
                return RecordDecodeStatus.Ignored;

            // **** Ignored Records - CGMS ****
            case BGReceived: // Ian3F:
            case SensorAlert: // Ian08

                // LOG.debug(" -- ignored CGMS Entry: " +
                // entry.getEntryType().name());
                return RecordDecodeStatus.Ignored;

            default:
                return RecordDecodeStatus.NotSupported;

        }
    }


    private void decodeChangeTime(PumpHistoryEntry entry)
    {
        if (changeTimeRecord == null)
            return;

        String timeChange = String.format(PumpEventType.DateTimeChanged.getValueTemplate(),
            this.changeTimeRecord.getATechDate().getDateTimeString(), entry.getATechDate().getDateTimeString());

        writeData(PumpBaseType.Event, PumpEventType.DateTimeChanged, timeChange, entry.getATechDate());

        this.changeTimeRecord = null;
    }


    private void decodeCalBGForPH(PumpHistoryEntry entry)
    {
        int high = (entry.getDatetime()[4] & 0x80) >> 7;
        int bg = bitUtils.makeInt(high, getUnsignedInt(entry.getHead()[0]));

        writeData(PumpBaseType.AdditionalData, PumpAdditionalDataType.BloodGlucose, "" + bg, entry.getATechDate());
    }


    // masks = [ ( 0x80, 7), (0x40, 6), (0x20, 5), (0x10, 4) ]
    // nibbles = [ ]
    // for mask, shift in masks:
    // nibbles.append( ( (year & mask) >> shift ) )
    // return nibbles

    private void decodeBatteryActivity(PumpHistoryEntry entry)
    {
        this.writeData(PumpBaseType.Event,
            entry.getHead()[0] == 0 ? PumpEventType.BatteryRemoved : PumpEventType.BatteryReplaced,
            entry.getATechDate());
    }


    private void decodeEndResultTotals(PumpHistoryEntry entry)
    {
        float totals = bitUtils.makeInt(entry.getHead()[2], entry.getHead()[3]) * 0.025f;

        this.writeData(PumpBaseType.Report, PumpReport.InsulinTotalDay, getFormattedFloat(totals, 2),
            entry.getATechDate());
    }


    private RecordDecodeStatus decodeBasalProfileStart(PumpHistoryEntry entry)
    {
        int[] body = entry.getBody();
        // int bodyOffset = headerSize + timestampSize;
        int offset = body[0] * 1000 * 30 * 60;
        Float rate = null;
        int index = body[2];

        if (MinimedDeviceType.isSameDevice(MinimedUtil.getDeviceType(), MinimedDeviceType.Minimed_523andHigher))
        {
            rate = body[1] * 0.025f;
        }

        if (rate == null)
        {
            LOG.warn("Basal Profile Start (ERROR): offset={}, rate={}, index={}, body_raw={}", offset, rate, index,
                body);
            return RecordDecodeStatus.Error;
        }
        else
        {
            writeData(PumpBaseType.Basal, PumpBasalType.ValueChange, getFormattedFloat(rate, 3), entry.getATechDate());
            return RecordDecodeStatus.OK;
        }

    }


    private void decodeBolusWizard(PumpHistoryEntry entry)
    {
        int[] body = entry.getBody();

        BolusWizardDTO dto = new BolusWizardDTO();

        float bolus_strokes = 10.0f;

        if (MinimedDeviceType.isSameDevice(MinimedUtil.getDeviceType(), MinimedDeviceType.Minimed_523andHigher))
        {
            // https://github.com/ps2/minimed_rf/blob/master/lib/minimed_rf/log_entries/bolus_wizard.rb#L102
            bolus_strokes = 40.0f;

            dto.carbs = ((body[1] & 0x0c) << 6) + body[0];

            dto.bloodGlucose = ((body[1] & 0x03) << 8) + entry.getHead()[0];
            dto.carbRatio = body[1] / 10.0f;
            // carb_ratio (?) = (((self.body[2] & 0x07) << 8) + self.body[3]) /
            // 10.0s
            dto.insulinSensitivity = new Float(body[4]);
            dto.bgTargetLow = body[5];
            dto.bgTargetHigh = body[14];
            dto.correctionEstimate = (((body[9] & 0x38) << 5) + body[6]) / bolus_strokes;
            dto.foodEstimate = ((body[7] << 8) + body[8]) / bolus_strokes;
            dto.unabsorbedInsulin = ((body[10] << 8) + body[11]) / bolus_strokes;
            dto.bolusTotal = ((body[12] << 8) + body[13]) / bolus_strokes;
        }
        else
        {
            dto.bloodGlucose = (((body[1] & 0x0F) << 8) | entry.getHead()[0]);
            dto.carbs = body[0];
            dto.carbRatio = new Float(body[2]);
            dto.insulinSensitivity = new Float(body[3]);
            dto.bgTargetLow = body[4];
            dto.bgTargetHigh = body[12];
            dto.bolusTotal = body[11] / 10.0f;
            dto.foodEstimate = body[6] / 10.0f;
            dto.unabsorbedInsulin = body[9] / 10.0f;
            dto.bolusTotal = body[11] / 10.0f;
            dto.correctionEstimate = (body[7] + (body[5] & 0x0F)) / 10.0f;
        }

        this.writeData(PumpBaseType.Event, PumpEventType.BolusWizard, dto.getValue(), entry.getATechDate());

    }


    private void decodeLowReservoir(PumpHistoryEntry entry)
    {
        float amount = (getUnsignedInt(entry.getHead()[0]) * 1.0f / 10.0f);
        this.writeData(PumpBaseType.Event, PumpEventType.ReservoirLowDesc, getFormattedFloat(amount, 1),
            entry.getATechDate());
    }


    private void decodePrime(PumpHistoryEntry entry)
    {
        float amount = bitUtils.makeInt(entry.getHead()[2], entry.getHead()[3]) / 10.0f;
        float fixed = bitUtils.makeInt(entry.getHead()[0], entry.getHead()[1]) / 10.0f;

        this.writeData(PumpBaseType.Event, PumpEventType.PrimeInfusionSet,
            fixed > 0 ? getFormattedFloat(fixed, 1) : getFormattedFloat(amount, 1), entry.getATechDate());
    }


    @Override
    public void postProcess()
    {
        if (bolusEntry != null)
        {
            writeBolus(bolusEntry);
            bolusEntry = null;
        }
    }


    @Override
    public void refreshOutputWriter()
    {
        this.pumpValuesWriter.setOutputWriter(MinimedUtil.getOutputWriter());
    }


    @Override
    protected void runPostDecodeTasks()
    {
        this.showStatistics();
    }

    BolusDTO bolusEntry;


    private void decodeBolus(PumpHistoryEntry entry)
    {
        BolusDTO bolus = new BolusDTO();

        int[] data = entry.getHead();

        if (MinimedDeviceType.isSameDevice(MinimedUtil.getDeviceType(), MinimedDeviceType.Minimed_523andHigher))
        {
            bolus.setRequestedAmount(bitUtils.makeInt(data[0], data[1]) / 40.0f);
            bolus.setDeliveredAmount(bitUtils.makeInt(data[2], data[3]) / 10.0f);
            bolus.setInsulinOnBoard(bitUtils.makeInt(data[4], data[5]) / 40.0f);
            bolus.setDuration(data[6] * 30);
        }
        else
        {
            bolus.setRequestedAmount(data[0] / 40.0f);
            bolus.setDeliveredAmount(data[1] / 10.0f);
            bolus.setDuration(data[2] * 30);
        }

        bolus.setBolusType(
            (bolus.getDuration() != null && (bolus.getDuration() > 0)) ? PumpBolusType.Extended : PumpBolusType.Normal);
        bolus.setATechDate(entry.getATechDate());

        if (bolusEntry != null)
        {
            if (bolus.getBolusType() == PumpBolusType.Extended)
            {
                if (bolusEntry.getATechDate().equals(bolus.getATechDate()))
                {
                    bolus.setImmediateAmount(bolusEntry.getDeliveredAmount());
                    bolus.setBolusType(PumpBolusType.Multiwave);

                    writeBolus(bolus);
                    bolusEntry = null;
                }
                else
                {
                    // FIXME ths might not be correct
                    writeBolus(bolusEntry);
                    bolusEntry = null;
                }
            }
            else
            {
                writeBolus(bolusEntry);
                bolusEntry = null;
            }
        }

        bolusEntry = bolus;
    }


    private void writeBolus(BolusDTO bolus)
    {
        writeData(PumpBaseType.Bolus, bolus.getBolusType(), bolus.getValue(), bolus.getATechDate());
    }


    private void decodeTempBasal(PumpHistoryEntry entry)
    {
        if (this.tbrPreviousRecord == null)
        {
            // LOG.debug(this.tbrPreviousRecord.toString());
            this.tbrPreviousRecord = entry;
            return;
        }

        PumpHistoryEntry tbrRate = null, tbrDuration = null;

        if (entry.getEntryType() == PumpHistoryEntryType.TempBasalRate)
        {
            tbrRate = entry;
        }
        else
        {
            tbrDuration = entry;
        }

        if (tbrRate != null)
        {
            tbrDuration = tbrPreviousRecord;
        }
        else
        {
            tbrRate = tbrPreviousRecord;
        }

        // LOG.debug("Rate: " + tbrRate.toString());
        // LOG.debug("Durration: " + tbrDuration.toString());

        TemporaryBasalRateDTO tbr = new TemporaryBasalRateDTO();

        tbr.setAmount((float) tbrRate.getHead()[0]);
        tbr.setDuration(tbrDuration.getHead()[0] * 30);

        // System.out.println(
        // "TBR: amount=" + tbr.getAmount() + ", duration=" + tbr.getDuration()
        // + " min. Packed: " + tbr.getValue());

        // FIXME set Unit

        if (tbr.getDuration() > 0)
        {
            writeData(PumpBaseType.Basal, PumpBasalType.TemporaryBasalRate, tbr.getValue(), entry.getATechDate());
        }
        else
        {
            writeData(PumpBaseType.Basal, PumpBasalType.TemporaryBasalRateCanceled, "", entry.getATechDate());
        }

        tbrPreviousRecord = null;
    }


    private void decodeDateTime(PumpHistoryEntry entry)
    {
        int[] dt = entry.getDatetime();

        if (entry.getDateTimeLength() == 5)
        {
            int seconds = dt[0] & 0x3F;
            int month = ((dt[0] & 0xC0) >> 6) | ((dt[1] & 0xC0) >> 4);
            int minutes = dt[1] & 0x3F;
            int hour = dt[2] & 0x1F;
            int dayOfMonth = dt[3] & 0x1F;
            int year = fix2DigitYear(dt[4] & 0x3F);

            ATechDate atdate = new ATechDate(dayOfMonth, month, year, hour, minutes, seconds,
                    ATechDateType.DateAndTimeSec);

            entry.setATechDate(atdate);
        }
        else if (entry.getDateTimeLength() == 2)
        {
            int dayOfMonth = dt[0] & 0x1F;
            int month = (((dt[0] & 0xE0) >> 4) + ((dt[1] & 0x80) >> 7));
            int year = fix2DigitYear(dt[1] & 0x3F);

            ATechDate atdate = new ATechDate(dayOfMonth, month, year, 0, 0, 0, ATechDateType.DateAndTimeSec);
            // atdate.add(Calendar.DAY_OF_YEAR, 1);

            entry.setATechDate(atdate);
        }
        else
        {
            LOG.warn("Unknown datetime format: " + entry.getDateTimeLength());
        }
        // return new DateTime(year + 2000, month, dayOfMonth, hour, minutes,
        // seconds);

    }


    private int fix2DigitYear(int year)
    {
        if (year > 90)
        {
            year += 1900;
        }
        else
        {
            year += 2000;
        }

        return year;
    }


    // WRITE DATA

    private void writeData(PumpBaseType baseType, CodeEnumWithTranslation subType, ATechDate aTechDate)
    {
        this.pumpValuesWriter.writeObject(baseType.name() + "_" + subType.getName(), aTechDate);
    }


    private void writeData(PumpBaseType baseType, CodeEnumWithTranslation subType, String value, ATechDate aTechDate)
    {
        this.pumpValuesWriter.writeObject(baseType.name() + "_" + subType.getName(), aTechDate, value);
    }

}
