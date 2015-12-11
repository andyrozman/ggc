package ggc.pump.device.minimed.data.converter;

import ggc.plugin.device.impl.minimed.data.MinimedCommandReply;
import ggc.pump.data.defs.PumpConfigurationGroup;
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

public class Minimed523PumpDataConverter extends Minimed515PumpDataConverter
{

    public Minimed523PumpDataConverter(DataAccessPump dataAccess)
    {
        super(dataAccess);
    }


    @Override
    public void convertData(MinimedCommandReply minimedReply)
    {
        switch (minimedReply.getCommandType())
        {
            case Settings: // 192
                debugConverterResponse(minimedReply);
                decodeCurrentSettings(minimedReply);
                break;

            default:
                super.convertData(minimedReply);
        }
    }


    public void decodeCurrentSettings(MinimedCommandReply minimedReply)
    {
        super.decodeCurrentSettings(minimedReply);

        writeSetting("PCFG_BOLUS_SCROLL_STEP_SIZE", "" + minimedReply.getRawDataAsInt(21),
            PumpConfigurationGroup.Bolus);

        this.decodeEnableSetting("PCFG_CAPTURE_EVENT_ENABLE", minimedReply, 22, PumpConfigurationGroup.Other);
        this.decodeEnableSetting("PCFG_OTHER_DEVICE_ENABLE", minimedReply, 23, PumpConfigurationGroup.Other);
        this.decodeEnableSetting("PCFG_OTHER_DEVICE_PAIRED_STATE", minimedReply, 24, PumpConfigurationGroup.Other);
    }


    @Override
    int getSettingIndexMaxBasal()
    {
        return 7;
    }


    @Override
    int getSettingIndexTimeDisplayFormat()
    {
        return 9;
    }


    @Override
    public double decodeMaxBolus(byte ai[])
    {
        return decodeBolusInsulin(this.bitUtils.makeInt(ai[5], ai[6]));
    }

}
