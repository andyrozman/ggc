package ggc.pump.device.minimed.data.converter;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.ATechDate;

import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.MedtronicCnlSession;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.MessageUtils;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.enums.SendMessageType;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.tmp.PumpStatusEvent;
import ggc.plugin.device.impl.minimed.data.MinimedCommandReply;
import ggc.plugin.device.impl.minimed.util.MinimedUtil;
import ggc.pump.data.defs.PumpConfigurationGroup;
import ggc.pump.data.defs.PumpSettingsType;
import ggc.pump.data.defs.RatioType;
import ggc.pump.data.dto.BasalPatternEntryDTO;
import ggc.pump.data.dto.PumpSettingsDTO;
import ggc.pump.data.dto.RatioDTO;
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
 *  Filename:     Minimed523PumpDataConverter
 *  Description:  Data converter class. This will decode Configuration of Pump device (for 523/723 and later)
 *
 *  Author: Andy {andy@atech-software.com}
 */

/**
 * Based on 600SeriesAndroidUploader implementation. Message for 5xx devices by Andy
 */

public class Minimed640PumpDataConverter extends Minimed511PumpDataConverter
{

    private static final Logger LOG = LoggerFactory.getLogger(Minimed640PumpDataConverter.class);

    PumpSettingsDTO pumpSettingsDTO = new PumpSettingsDTO();


    public Minimed640PumpDataConverter(DataAccessPump dataAccess)
    {
        super(dataAccess);
    }


    @Override
    public void convertData(MinimedCommandReply minimedReply, SendMessageType commandType)
    {
        debugConverterResponse(minimedReply, commandType);

        switch (commandType)
        {
            case TIME:
                {
                    decodePumpTime(minimedReply);
                }
                break;

            case READ_PUMP_STATUS:
                {
                    decodePumpStatus(minimedReply);
                }
                break;

            case READ_BASAL_PATTERN_REQUEST:
                {
                    decodeBasalPattern(minimedReply);
                }
                break;

            case READ_BOLUS_WIZARD_CARB_RATIOS:
                {
                    decodeBolusWizardCarbRatios(minimedReply);
                }
                break;

            case READ_BOLUS_WIZARD_SENSITIVITY_FACTORS:
                {
                    decodeBolusWizardSensitivityFactors(minimedReply);
                }
                break;

            case READ_BOLUS_WIZARD_BG_TARGETS:
                {
                    decodeBolusWizardBgTargets(minimedReply);
                }
                break;

            case DEVICE_STRING:
                {
                    decodeDeviceString(minimedReply);
                }
                break;

            case DEVICE_CHARACTERISTICS:
                {
                    decodeDeviceCharacteristics(minimedReply);
                }
                break;

            case READ_HISTORY:
            case READ_HISTORY_INFO:
                LOG.error("Converter method for %s not implemented yet.", commandType.name());
                break;

            // ignored response
            case NO_TYPE:
            case BEGIN_EHSM_SESSION:
            case END_EHSM_SESSION:
            case END_HISTORY_TRANSMISSION:
            case UNMERGED_HISTORY_RESPONSE:
            case INITIATE_MULTIPACKET_TRANSFER:
            case MULTIPACKET_SEGMENT_TRANSMISSION:
            case MULTIPACKET_RESEND_PACKETS:
            case ACK_MULTIPACKET_COMMAND:
                break;

            default: // ignored paths

        }
    }


    public void debugConverterResponse(MinimedCommandReply reply, SendMessageType commandType)
    {
        if (canBeDebugged(commandType))
        {
            debugConverterResponse(reply);
        }
    }


    private boolean canBeDebugged(SendMessageType commandType)
    {
        switch (commandType)
        {
            case TIME:
            case READ_PUMP_STATUS:
            case READ_BASAL_PATTERN_REQUEST:
            case READ_BOLUS_WIZARD_CARB_RATIOS:
            case READ_BOLUS_WIZARD_SENSITIVITY_FACTORS:
            case READ_BOLUS_WIZARD_BG_TARGETS:
            case DEVICE_STRING:
            case DEVICE_CHARACTERISTICS:
            case READ_HISTORY:
            case READ_HISTORY_INFO:
                return true;

            case END_HISTORY_TRANSMISSION:
            case UNMERGED_HISTORY_RESPONSE:
            case INITIATE_MULTIPACKET_TRANSFER:
            case MULTIPACKET_SEGMENT_TRANSMISSION:
            case MULTIPACKET_RESEND_PACKETS:
            case ACK_MULTIPACKET_COMMAND:
            case NO_TYPE:
            case BEGIN_EHSM_SESSION:
            case END_EHSM_SESSION:
            default:
                return false;

        }
    }


    @Deprecated
    private void decodeDeviceCharacteristics(MinimedCommandReply minimedReply)
    {
        // class DeviceCharacteristicsResponse extends TransmitPacketResponse {
        // constructor(payload, pumpSession) {
        // super(payload, pumpSession);
        //
        // if (this.decryptedPayload.length < 13) {
        // throw new InvalidMessageError('Received invalid
        // DeviceCharacteristicsResponse message.');
        // }
        // }
        //
        // get serial() {
        // return this.decryptedPayload.slice(0x03, 0x0D).toString();
        // }
        //
        // get MAC() {
        // return this.decryptedPayload.slice(0x0D, 0x15).toString('binary');
        // }
        //
        // get comDVersion() {
        // const majorNumber = this.decryptedPayload.readUInt8(0x15);
        // const minorNumber = this.decryptedPayload.readUInt8(0x16);
        // const alpha = String.fromCharCode(65 +
        // this.decryptedPayload.readUInt8(0x17));
        // return `${majorNumber}.${minorNumber}${alpha}`;
        // }
        //
        // get telDVersion() {
        // /* eslint-disable no-bitwise */
        // const majorNumber = this.decryptedPayload.readUInt8(0x18) >> 3;
        // const minorNumber = (this.decryptedPayload.readUInt8(0x19) >> 5) |
        // ((this.decryptedPayload.readUInt8(0x18) << 29) >> 26);
        // const alpha = String.fromCharCode(64 +
        // ((this.decryptedPayload.readUInt8(0x19) << 3) >> 3));
        // /* eslint-enable no-bitwise */
        // return `${majorNumber}.${minorNumber}${alpha}`;
        // }
        //
        // get model() {
        // const modelMajorNumber = this.decryptedPayload.readUInt16BE(0x1A);
        // const modelMinorNumber = this.decryptedPayload.readUInt16BE(0x1C);
        // return `${modelMajorNumber}.${modelMinorNumber}`;
        // }
        //
        // get firmwareVersion() {
        // const majorNumber = this.decryptedPayload.readUInt8(0x29);
        // const minorNumber = this.decryptedPayload.readUInt8(0x2A);
        // const alpha =
        // String.fromCharCode(this.decryptedPayload.readUInt8(0x2B));
        // return `${majorNumber}.${minorNumber}${alpha}`;
        // }
        //
        // get motorAppVersion() {
        // const majorNumber = this.decryptedPayload.readUInt8(0x2C);
        // const minorNumber = this.decryptedPayload.readUInt8(0x2D);
        // const alpha =
        // String.fromCharCode(this.decryptedPayload.readUInt8(0x2E));
        // return `${majorNumber}.${minorNumber}${alpha}`;
        // }
        //
        // // TODO - we need to confirm that this is indeed BG UNITS before we
        // can use them in `settings`,
        // // and updating NGPHistoryParser:buildSettingsRecords()
        // get units() {
        // // See NGPUtil.NGPConstants.BG_UNITS
        // return this.decryptedPayload.readUInt8(0x35);
        // }
        // }

    }


    @Deprecated
    private void decodeDeviceString(MinimedCommandReply minimedReply)
    {
        // class DeviceStringResponse extends TransmitPacketResponse {
        // constructor(payload, pumpSession) {
        // super(payload, pumpSession);
        //
        // if (this.decryptedPayload.length < 96) {
        // throw new InvalidMessageError('Received invalid DeviceStringResponse
        // message.');
        // }
        // }
        //
        // get MAC() {
        // return this.decryptedPayload.slice(0x03, 0x0B).toString('binary');
        // }
        //
        // get stringType() {
        // return this.decryptedPayload.readUInt16BE(0x0B);
        // }
        //
        // get language() {
        // return this.decryptedPayload.readUInt8(0x0D);
        // }
        //
        // get string() {
        // const deviceStringUtf16 = this.decryptedPayload.slice(0x0E, 0x5E);
        // // We have to strip the nulls ourselves, because the payload doesn't
        // give us string size.
        // return iconv.decode(deviceStringUtf16, 'utf16-be').replace(/\0/g,
        // '');
        // }
        // }
    }


    private void decodeBolusWizardBgTargets(MinimedCommandReply minimedReply)
    {
        byte[] data = minimedReply.getDecryptedPayload();

        int numItems = data[0x05];

        for (int i = 0; i < numItems; i++)
        {
            int high = bitUtils.toInt(data, 0x06 + (i * 9)); // in mg/dL
            int low = bitUtils.toInt(data, 10 + (i * 9)); // in mg/dL
            ATechDate time = getTimeFrom30MinInterval(data[14 + (i * 9)]);

            RatioDTO ratio = new RatioDTO(RatioType.BGTargetRange, i, time, (short) low, (short) high);

            pumpSettingsDTO.addSettingTimeValueEntry(ratio);
        }

        pumpSettingsDTO.writeSettingsToGGC(PumpSettingsType.BGTargets);
    }


    private void decodeBolusWizardSensitivityFactors(MinimedCommandReply minimedReply)
    {
        // Bytes 3 and 4 are a CCITT checksum of the sentivities bytes.
        byte[] data = minimedReply.getDecryptedPayload();

        int numItems = data[5];

        for (int i = 0; i < numItems; i++)
        {
            int amount = bitUtils.toShortAsInt(data, 6 + (i * 5)); // in mg/dL
            // bitUtils.toInt(data, 0x08 + (i * 5)) / 10.0; // mmol/L
            // this.decryptedPayload.readUInt16BE(
            ATechDate time = getTimeFrom30MinInterval(data[10 + (i * 5)]);

            RatioDTO ratio = new RatioDTO(RatioType.InsulinBGRatio, i, time, amount);

            pumpSettingsDTO.addSettingTimeValueEntry(ratio);
        }
    }


    private void decodeBolusWizardCarbRatios(MinimedCommandReply minimedReply)
    {
        // Bytes 3 and 4 are a CCITT checksum of the ratios bytes.
        byte[] data = minimedReply.getDecryptedPayload();

        int numItems = data[5];

        for (int i = 0; i < numItems; i++)
        {
            int amount = bitUtils.toInt(data, 6 + (i * 9)); // in mg/dL
            // empty value (4 bits)
            ATechDate time = getTimeFrom30MinInterval(data[14 + (i * 9)]);

            RatioDTO ratio = new RatioDTO(RatioType.InsulinCHRatio, i, time, amount);

            pumpSettingsDTO.addSettingTimeValueEntry(ratio);
        }
    }


    private void decodeBasalPattern(MinimedCommandReply minimedCommandReply)
    {
        byte[] data = minimedCommandReply.getDecryptedPayload();

        int basalPatternNumber = data[3];
        int nrItems = data[4];

        for (int i = 0; i < nrItems; i++)
        {
            BasalPatternEntryDTO entry = new BasalPatternEntryDTO();
            entry.setStarTime(getTimeFrom30MinInterval(data[9 + (i * 5)]));
            entry.setRate(bitUtils.toInt(data, 5 + (i * 5)) / 10000.0f);

            pumpSettingsDTO.addBasalPatternEntry(basalPatternNumber, entry);
        }

        pumpSettingsDTO.writeSettingsToGGC(PumpSettingsType.BasalPatterns);
    }


    private void decodePumpTime(MinimedCommandReply minimedCommandReply)
    {
        MedtronicCnlSession session = MinimedUtil.getMedtronicCnlSession();

        if (minimedCommandReply.getRawData().length < (61 + 8))
        {
            LOG.error("Invalid message received for getPumpTime");
            session.setPumpTime(null);
        }

        ByteBuffer dateBuffer = ByteBuffer.allocate(8);
        dateBuffer.order(ByteOrder.BIG_ENDIAN);
        dateBuffer.put(minimedCommandReply.getRawData(), 0x3d, 8);
        long rtc = dateBuffer.getInt(0) & 0x00000000ffffffffL;
        long offset = dateBuffer.getInt(4);

        session.setPumpTimeOffset(offset);

        LOG.debug("getPumpTime with date " + MessageUtils.decodeDateTime(rtc, offset));
        session.setPumpTime(MessageUtils.decodeDateTime(rtc, offset));
    }


    @Deprecated
    private void decodePumpStatus(MinimedCommandReply minimedCommandReply)
    {

        if (minimedCommandReply.getRawData().length < (57 + 96))
        {
            LOG.error("Invalid message received for getPumpStatus");
            return;
        }

        long pumpTimeOffset = MinimedUtil.getMedtronicCnlSession().getPumpTimeOffset();

        ByteBuffer statusBuffer = ByteBuffer.allocate(96);
        statusBuffer.order(ByteOrder.BIG_ENDIAN);
        statusBuffer.put(minimedCommandReply.getRawData(), 0x39, 96);

        PumpStatusEvent pumpRecord = new PumpStatusEvent();

        // Status Flags
        pumpRecord.setSuspended((statusBuffer.get(0x03) & 0x01) != 0x00);
        pumpRecord.setBolusing((statusBuffer.get(0x03) & 0x02) != 0x00);
        pumpRecord.setDeliveringInsulin((statusBuffer.get(0x03) & 0x10) != 0x00);
        pumpRecord.setTempBasalActive((statusBuffer.get(0x03) & 0x20) != 0x00);
        pumpRecord.setCgmActive((statusBuffer.get(0x03) & 0x40) != 0x00);

        // Active basal pattern
        pumpRecord.setActiveBasalPattern(statusBuffer.get(0x1a));

        // Normal basal rate
        long rawNormalBasal = statusBuffer.getInt(0x1b);
        pumpRecord.setBasalRate(
            new BigDecimal(rawNormalBasal / 10000f).setScale(3, BigDecimal.ROUND_HALF_UP).floatValue());

        // Temp basal rate
        // TODO - need to figure this one out
        // long rawTempBasal = statusBuffer.getShort(0x21) & 0x0000ffff;
        // pumpRecord.setTempBasalRate(new BigDecimal(rawTempBasal /
        // 10000f).setScale(3, BigDecimal.ROUND_HALF_UP).floatValue());

        // Temp basal percentage
        pumpRecord.setTempBasalPercentage(statusBuffer.get(0x23));

        // Temp basal minutes remaining
        pumpRecord.setTempBasalMinutesRemaining((short) (statusBuffer.getShort(0x24) & 0x0000ffff));

        // Units of insulin delivered as basal today
        // TODO - is this basal? Do we have a total Units delivered elsewhere?
        pumpRecord.setBasalUnitsDeliveredToday(statusBuffer.getInt(0x26));

        // Pump battery percentage
        pumpRecord.setBatteryPercentage((statusBuffer.get(0x2a)));

        // Reservoir amount
        long rawReservoirAmount = statusBuffer.getInt(0x2b);
        pumpRecord.setReservoirAmount(
            new BigDecimal(rawReservoirAmount / 10000f).setScale(3, BigDecimal.ROUND_HALF_UP).floatValue());

        // Amount of insulin left in pump (in minutes)
        byte insulinHours = statusBuffer.get(0x2f);
        byte insulinMinutes = statusBuffer.get(0x30);
        pumpRecord.setMinutesOfInsulinRemaining((short) ((insulinHours * 60) + insulinMinutes));

        // Active insulin
        long rawActiveInsulin = statusBuffer.getShort(0x33) & 0x0000ffff;
        pumpRecord.setActiveInsulin(
            new BigDecimal(rawActiveInsulin / 10000f).setScale(3, BigDecimal.ROUND_HALF_UP).floatValue());

        // CGM SGV
        pumpRecord.setSgv(statusBuffer.getShort(0x35) & 0x0000ffff); // In
        // mg/DL. 0
        // means no
        // CGM
        // reading

        // SGV Date
        long rtc;
        long offset;
        if ((pumpRecord.getSgv() & 0x200) == 0x200)
        {
            // Sensor error. Let's reset. FIXME - solve this more elegantly
            // later
            pumpRecord.setSgv(0);
            rtc = 0;
            offset = 0;
            pumpRecord.setCgmTrend(PumpStatusEvent.CGM_TREND.NOT_SET);
        }
        else
        {
            rtc = statusBuffer.getInt(0x37) & 0x00000000ffffffffL;
            offset = statusBuffer.getInt(0x3b);
            // pumpRecord.setCgmTrend(getTrend(statusBuffer.get(0x40)));
        }
        // TODO - this should go in the sgvDate, and eventDate should be the
        // time of this poll.
        pumpRecord.setEventDate(new Date(MessageUtils.decodeDateTime(rtc, offset).getTime() - pumpTimeOffset));

        // Predictive low suspend
        // TODO - there is more status info in this byte other than just a
        // boolean yes/no
        pumpRecord.setLowSuspendActive(statusBuffer.get(0x3f) != 0);

        // Recent Bolus Wizard BGL
        pumpRecord.setRecentBolusWizard(statusBuffer.get(0x48) != 0);
        pumpRecord.setBolusWizardBGL(statusBuffer.getShort(0x49) & 0x0000ffff); // In
        // mg/DL

        LOG.debug("Finished getPumpStatus");

    }


    public void decodeCurrentSettings(MinimedCommandReply minimedReply)
    {
        writeSetting("PCFG_BOLUS_SCROLL_STEP_SIZE", "" + minimedReply.getRawDataAsInt(21),
            PumpConfigurationGroup.Bolus);

        this.decodeEnableSetting("PCFG_CAPTURE_EVENT_ENABLE", minimedReply, 22, PumpConfigurationGroup.Other);
        this.decodeEnableSetting("PCFG_OTHER_DEVICE_ENABLE", minimedReply, 23, PumpConfigurationGroup.Other);
        this.decodeEnableSetting("PCFG_OTHER_DEVICE_PAIRED_STATE", minimedReply, 24, PumpConfigurationGroup.Other);
    }

}
