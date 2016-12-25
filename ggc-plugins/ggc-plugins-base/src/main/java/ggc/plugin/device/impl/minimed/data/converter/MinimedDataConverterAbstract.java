package ggc.plugin.device.impl.minimed.data.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATDataAccessAbstract;
import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;
import com.atech.utils.data.BitUtils;

import ggc.core.data.defs.GlucoseUnitType;
import ggc.plugin.device.impl.minimed.data.MinimedCommandReply;
import ggc.plugin.device.impl.minimed.enums.MinimedCommandType;
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
 *  Filename:     MinimedDataConverterAbstract
 *  Description:  Minimed Data Converter Abstract.
 *
 *  Author: Andy {andy@atech-software.com}
 */

public abstract class MinimedDataConverterAbstract implements MinimedDataConverter
{

    private static final Logger LOG = LoggerFactory.getLogger(MinimedDataConverterAbstract.class);

    protected DataAccessPlugInBase dataAccess;
    protected BitUtils bitUtils = MinimedUtil.getBitUtils();
    protected OutputWriter outputWriter;
    protected I18nControlAbstract i18nControl;
    public static String templateTimeAmount;
    public static String optionOff;
    protected boolean dataDebug = false;


    public MinimedDataConverterAbstract(DataAccessPlugInBase dataAccess)
    {
        this.dataAccess = dataAccess;
        this.i18nControl = dataAccess.getI18nControlInstance();
        templateTimeAmount = getMessage("CFG_BASE_FROM") + "=%s, " + getMessage("CFG_BASE_AMOUNT") + "=";
        optionOff = getMessage("CCFG_OPTION_OFF");
        this.dataDebug = MinimedUtil.isLowLevelDebugData();
    }


    protected String parseResultEnable(int i)
    {
        switch (i)
        {
            case 0:
                return getMessage("NO");
            case 1:
                return getMessage("YES");
            default:
                return String.format(getMessage("CFG_BASE_INVALID_VALUE"), i);
        }
    }


    protected ATechDate getTimeFrom30MinInterval(int interval)
    {
        ATechDate atd;

        if (interval % 2 == 0)
        {
            atd = new ATechDate(0, 0, 0, interval / 2, 0, 0, ATechDateType.TimeOnlyMin);
        }
        else
        {
            atd = new ATechDate(0, 0, 0, (interval - 1) / 2, 30, 0, ATechDateType.TimeOnlyMin);
        }

        return atd;
    }


    /**
     * Get Unknown Settings
     *
     * @param minimedReply minimedReply instance
     * @return Object instance
     */
    public Object getUnknownSettings(MinimedCommandReply minimedReply)
    {
        debugResult(minimedReply);
        return null;
    }


    public void debugResult(MinimedCommandReply minimedReply)
    {
        LOG.warn("Decoding of Unsupported Command: {}", minimedReply.getCommandType().name());
        LOG.warn("[{}]", bitUtils.getHex(minimedReply.getRawData()));
        LOG.warn("IntArray: {}" + bitUtils.getByteArrayShow(minimedReply.getRawData()));
    }


    public void debugConverterResponse(MinimedCommandReply reply)
    {
        if (dataDebug)
        {
            LOG.debug("*******************************************************************");
            LOG.debug("     Command:  {}", reply.getCommandType().name());
            LOG.debug("*******************************************************************");
            LOG.debug("  Raw Reply: {}", bitUtils.getHex(reply.getRawData()));
            LOG.debug("*******************************************************************");
        }
    }


    protected String stringWithElementCount(MinimedCommandReply minimedReply)
    {
        byte[] rd = minimedReply.getRawData();

        byte arr[] = new byte[rd[0]];
        System.arraycopy(rd, 1, arr, 0, arr.length);

        return this.bitUtils.makeString(arr);
    }


    protected String createString(MinimedCommandReply minimedReply)
    {
        String tmp_str = new String(minimedReply.getRawData());
        tmp_str = tmp_str.trim();
        return tmp_str;
    }


    protected String getMessage(String key)
    {
        return i18nControl.getMessage(key);
    }


    protected String getFormatedTimedValue(String timeString, Float floatValue, Short shortValue, String unit)
    {
        if (floatValue != null)
        {
            return String.format(templateTimeAmount + "%6.4f %s", timeString, floatValue, unit);
        }
        else
        {
            return String.format(templateTimeAmount + "%d %s", timeString, shortValue, unit);
        }

    }


    protected double decodeDecimalUnit(int i)
    {
        return (double) i / 10.0d;
    }


    protected Object getCorrectBGValue(GlucoseUnitType glucoseUnitType, int bgValue)
    {
        if (glucoseUnitType == GlucoseUnitType.mg_dL)
            return bgValue;
        else
            return bgValue / 10.0d;
    }


    protected String getCorrectBGValueFormatted(GlucoseUnitType glucoseUnitType, int bgValue)
    {
        if (glucoseUnitType == GlucoseUnitType.mg_dL)
        {
            return String.format("%d", bgValue);
        }
        else
        {
            double val = bgValue / 10.0d;
            return String.format("%6.4f", val);
        }
    }


    protected String getTimeFromMinutes(int minutes)
    {
        int h = minutes / 60;
        int m = minutes - h * 60;

        return ATDataAccessAbstract.getLeadingZero(h, 2) + ":" + ATDataAccessAbstract.getLeadingZero(m, 2);
    }


    public void historyDataConversionNote(Logger log, MinimedCommandType commandType)
    {
        log.error("History data (" + commandType.name()
                + ") can't be decoded with this converter. History data is decoded by MinimedHistoryDecoder instance class.");
    }

}
