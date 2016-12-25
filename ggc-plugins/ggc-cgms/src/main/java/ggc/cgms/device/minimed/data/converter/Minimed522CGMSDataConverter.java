package ggc.cgms.device.minimed.data.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.BitUtils;

import ggc.cgms.data.defs.CGMSConfigurationGroup;
import ggc.cgms.util.DataAccessCGMS;
import ggc.core.data.defs.GlucoseUnitType;
import ggc.plugin.data.DeviceValueConfigEntry;
import ggc.plugin.device.impl.minimed.data.MinimedCommandReply;
import ggc.plugin.device.impl.minimed.data.converter.MinimedDataConverterAbstract;
import ggc.plugin.device.impl.minimed.enums.MinimedCommandType;
import ggc.plugin.device.impl.minimed.util.MinimedUtil;

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
 *  Filename:     Minimed522CGMSDataConverter
 *  Description:  Data converter class. This will decode Configuration of CGMS device (for 522/722)
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class Minimed522CGMSDataConverter extends MinimedDataConverterAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(Minimed522CGMSDataConverter.class);


    public Minimed522CGMSDataConverter()
    {
        super(DataAccessCGMS.getInstance());
        this.bitUtils = MinimedUtil.getBitUtils();
        this.outputWriter = MinimedUtil.getOutputWriter();
    }


    public void convertData(MinimedCommandReply minimedReply)
    {
        switch (minimedReply.getCommandType())
        {

            case CalibrationFactor: // 156
                debugConverterResponse(minimedReply);
                decodeCalibrationFactor(minimedReply);
                break;

            case SensorSettings_522: // 153
                debugConverterResponse(minimedReply);
                decodeSensorSettings(minimedReply);
                break;

            case GlucoseHistory: // 154
                historyDataConversionNote(LOG, MinimedCommandType.GlucoseHistory);
                break;

            case ISIGHistory: // 155
                historyDataConversionNote(LOG, MinimedCommandType.ISIGHistory);
                decodeISIGHistory();
                break;

            default:
                LOG.warn("Unknown command type [" + minimedReply.getCommandType().name() + "] for decoding");
        }
    }


    public Object getReplyValue(MinimedCommandReply minimedReply)
    {
        return null;
    }


    private void decodeISIGHistory()
    {
        LOG.error("Decode ISIG History not implemented.");
    }


    public void refreshOutputWriter()
    {
        this.outputWriter = MinimedUtil.getOutputWriter();
    }


    public void decodeCalibrationFactor(MinimedCommandReply minimedReply)
    {
        // util.decoder.debugResult(cmd);

        double cal_f = (double) minimedReply.getRawDataBytesAsInt(0, 1) / 1000.0d;
        // m_calibrationFactor = (double)i / 1000D;
        // log.info( (new
        // StringBuilder()).append("decodeCalibrationFactor:
        // factor=").append(m_calibrationFactor).toString());

        writeSetting("SENSOR_CALIBRATION_FACTOR", "" + cal_f, CGMSConfigurationGroup.General);

    }


    public void decodeSensorSettings(MinimedCommandReply minimedReply)
    {
        BitUtils hu = this.bitUtils;
        // int data[] = cmd.reply.raw_data;

        LOG.warn("VERIFY 522 DEVICE !!!!!!!!!!!!!!!!!!!!!!!!!");

        decodeEnableSetting("CCFG_SENSOR_ENABLED", minimedReply, 0, CGMSConfigurationGroup.General);

        // int bg_units = data[22];

        GlucoseUnitType glucoseUnitType = GlucoseUnitType.getByCode(minimedReply.getRawDataAsInt(22));

        writeSetting("CCFG_SENSOR_BG_UNITS", glucoseUnitType.getTranslation(), CGMSConfigurationGroup.General);

        decodeEnableSetting(String.format("CCFG_X_LIMIT_ENABLED", getMessage("PCFG_SOUND_HIGH_ALERT")), //
            minimedReply, 1, CGMSConfigurationGroup.Warnings);

        writeSetting(String.format("CCFG_X_WARNING_ABOVE", getMessage("PCFG_SOUND_HIGH_ALERT")), //
            getCorrectBGValueFormatted(glucoseUnitType, minimedReply.getRawDataBytesAsInt(2, 3)), //
            CGMSConfigurationGroup.Warnings);

        writeSetting(String.format("CCFG_X_SNOOZE_TIME", getMessage("PCFG_SOUND_HIGH_ALERT")), //
            "" + minimedReply.getRawDataBytesAsInt(4, 5), CGMSConfigurationGroup.Warnings);

        decodeEnableSetting(String.format("CCFG_X_LIMIT_ENABLED", getMessage("PCFG_SOUND_LOW_ALERT")), //
            minimedReply, 6, CGMSConfigurationGroup.Warnings);

        writeSetting(String.format("CCFG_X_WARNING_BELOW", getMessage("PCFG_SOUND_LOW_ALERT")), //
            getCorrectBGValueFormatted(glucoseUnitType, minimedReply.getRawDataBytesAsInt(7, 8)), //
            CGMSConfigurationGroup.Warnings);

        writeSetting(String.format("CCFG_X_SNOOZE_TIME", getMessage("PCFG_SOUND_LOW_ALERT")), //
            "" + minimedReply.getRawDataBytesAsInt(9, 10), CGMSConfigurationGroup.Warnings);

        writeSetting("CCFG_SENSOR_ALARM_SNOOZE_TIME", "" + minimedReply.getRawDataBytesAsInt(14, 15),
            CGMSConfigurationGroup.Warnings);

        decodeEnableSetting("CCFG_SENSOR_CALIBRATION_REMINDER_ENABLE", minimedReply, 16,
            CGMSConfigurationGroup.Transmiter);

        writeSetting("CCFG_SENSOR_CALIBRATION_REMINDER_TIME", "" + minimedReply.getRawDataBytesAsInt(17, 18),
            CGMSConfigurationGroup.Transmiter);

        writeSetting("CCFG_SENSOR_TRANSMITER_ID", "" + minimedReply.getRawDataBytesAsInt(19, 20, 21),
            CGMSConfigurationGroup.Transmiter);

        writeSetting("CCFG_SENSOR_WEAK_SIGNAL_TIME", "" + minimedReply.getRawDataBytesAsInt(23, 24),
            CGMSConfigurationGroup.Transmiter);

    }


    protected void decodeEnableSetting(String key, MinimedCommandReply minimedReply, CGMSConfigurationGroup pcg)
    {
        decodeEnableSetting(key, minimedReply, 0, pcg);
    }


    protected void decodeEnableSetting(String key, int value, CGMSConfigurationGroup pcg)
    {
        writeSetting(key, parseResultEnable(value), pcg);
    }


    protected void decodeEnableSetting(String key, MinimedCommandReply minimedReply, int bit,
            CGMSConfigurationGroup pcg)
    {
        writeSetting(key, parseResultEnable(minimedReply.getRawDataAsInt(bit)), pcg);
    }


    protected void writeSetting(String key, String value, Object rawValue, CGMSConfigurationGroup group)
    {
        if (rawValue != null)
        {
            outputWriter.writeConfigurationData(new DeviceValueConfigEntry(i18nControl.getMessage(key), value, group));
        }
    }


    protected void writeSetting(String key, String value, CGMSConfigurationGroup group)
    {
        outputWriter.writeConfigurationData(new DeviceValueConfigEntry(i18nControl.getMessage(key), value, group));
    }

}
