package ggc.plugin.device.impl.animas.data;

import ggc.plugin.data.DeviceValuesWriter;
import ggc.plugin.data.enums.ClockModeType;
import ggc.plugin.data.enums.GlucoseUnitType;
import ggc.plugin.device.impl.animas.comm.AnimasCommProtocolAbstract;
import ggc.plugin.device.impl.animas.data.dto.PumpConnectorInfo;
import ggc.plugin.device.impl.animas.data.dto.PumpInfo;
import ggc.plugin.device.impl.animas.handler.AnimasDataWriter;
import ggc.plugin.device.impl.animas.enums.AnimasDataType;
import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;
import ggc.plugin.device.impl.animas.enums.AnimasTransferType;

import java.util.HashMap;

import ggc.plugin.output.OutputWriter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
 *  Filename:     AnimasDeviceData
 *  Description:  Abstract class for Animas Device Data
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public abstract class AnimasDeviceData
{

    public static final Log LOG = LogFactory.getLog(AnimasDeviceData.class);

    protected AnimasCommProtocolAbstract pumpCommunicationInterface;
    public PumpConnectorInfo pumpConnectorInfo;
    protected AnimasTransferType transferType;
    protected HashMap<String, String> concurrentOperation = new HashMap<String, String>();
    public PumpInfo pumpInfo;
    //protected DeviceValuesWriter dvw = null;
    protected boolean debugDataSaving = true;
    protected AnimasDataWriter animasDeviceDataWriter;
    protected HashMap<AnimasDataType, AnimasDataType> dataReceived = new HashMap<AnimasDataType, AnimasDataType>();


    public AnimasDeviceData(AnimasDataWriter writer)
    {
        pumpConnectorInfo = new PumpConnectorInfo();
        pumpInfo = new PumpInfo();
        animasDeviceDataWriter = writer;

        //this.dvw = writer.getDeviceValuesWriter();
    }

    public AnimasCommProtocolAbstract getPumpCommunicationInterface()
    {
        return pumpCommunicationInterface;
    }

    public void setPumpCommunicationInterface(AnimasCommProtocolAbstract pumpCommunicationInterface)
    {
        this.pumpCommunicationInterface = pumpCommunicationInterface;
    }


    public void debug(String debugData)
    {
        if (this.debugDataSaving)
        {
            LOG.info(debugData);
        }
    }

    public void setSerialNumber(String serialNumber)
    {
        this.pumpInfo.serialNumber = serialNumber;
        debug("Serial Number: " + serialNumber);
    }

    public void setBgUnit(boolean isMmolL)
    {
        this.pumpInfo.glucoseUnitType = isMmolL ? GlucoseUnitType.mmol_L : GlucoseUnitType.mg_dL;
        debug("bg Unit: " + this.pumpInfo.glucoseUnitType.getDescription());
        loadBgUnitIntoLocalSettings();
    }

    protected abstract void loadBgUnitIntoLocalSettings();

    public boolean isModel1200()
    {
        return (this.pumpInfo.deviceType == AnimasDeviceType.Animas_IR1200);
    }

    public boolean isModel1200p()
    {
        return (this.pumpInfo.deviceType == AnimasDeviceType.Animas_IR1200p);
    }

    public boolean isModel1250()
    {
        return (this.pumpInfo.deviceType == AnimasDeviceType.Animas_IR1250);
    }

    public boolean isModelBefore2020()
    {
        return (!isModel2020OrHigher());
    }

    public boolean isModelPing()
    {
        return AnimasDeviceType.isAnimasPingFamily(this.pumpInfo.deviceType);
    }

    public boolean isModel2020OrHigher()
    {
        return (AnimasDeviceType.isAnimas2020Family(this.pumpInfo.deviceType) || isModelPingOrHigher());
    }

    public boolean isModelPingOrHigher()
    {
        return (AnimasDeviceType.isAnimasPingFamily(this.pumpInfo.deviceType) || isModelVibe());
    }

    public boolean isModelVibe()
    {
        return (this.pumpInfo.deviceType == AnimasDeviceType.Animas_Vibe);
    }


    public boolean isBGinMgDL()
    {
        boolean mgdl = this.pumpInfo.glucoseUnitType == GlucoseUnitType.mg_dL;

        debug("isBGinMgDL: " + mgdl);

        return (this.pumpInfo.glucoseUnitType == GlucoseUnitType.mg_dL);
    }


    public void addConcurrentOperation(String operation)
    {
        if (!this.concurrentOperation.containsKey(operation))
        {
            this.concurrentOperation.put(operation, operation);
        }
    }


    public boolean doWeHaveConcurrentOperation(String operation)
    {
        return this.concurrentOperation.containsKey(operation);
    }

    public void removeConcurrentOperation(String operation)
    {
        if (this.concurrentOperation.containsKey(operation))
        {
            this.concurrentOperation.remove(operation);
        }
    }


    public boolean isSoftwareCodeSet()
    {
        return StringUtils.isNotEmpty(this.pumpInfo.softwareCode);
    }


    public AnimasTransferType getTransferType()
    {
        return transferType;
    }


    public void setTransferType(AnimasTransferType transferType)
    {
        this.transferType = transferType;
    }


    public boolean isCommandAllowedForDeviceType(AnimasDataType dataType)
    {
        if (this.pumpInfo.deviceType == null)
            return true;
        else
            return (dataType.isCommandAllowedForDeviceType(this.pumpInfo.deviceType));
    }


    public boolean isDownloaderSerialNumberSet()
    {
        return StringUtils.isNotBlank(this.pumpConnectorInfo.rawSerialNumber);
    }


    public void addAllDataReceived(AnimasDataType dataTypeObject)
    {
        if (!dataReceived.containsKey(dataTypeObject))
        {
            dataReceived.put(dataTypeObject, dataTypeObject);
        }
    }


    public void processReceivedData(AnimasDevicePacket animasDevicePacket)
    {
        for (AnimasPreparedDataEntry apde : animasDevicePacket.preparedData)
        {
            if (apde.value != null)
            {
                this.animasDeviceDataWriter.getDeviceValuesWriter().writeObject(apde.key, apde.dateTime, apde.value);
            }
            else
            {
                this.animasDeviceDataWriter.getDeviceValuesWriter().writeObject(apde.key, apde.dateTime, null);
            }
        }
    }


    public void setClockMode(short clockMode)
    {
        // FIXME
        LOG.error("clock mode: " + clockMode);

        ClockModeType cmt = ClockModeType.getEnum(clockMode);
        LOG.warn("setClockMode: " + cmt.name());

        this.pumpInfo.clockMode = cmt;
    }


    public abstract void debugAllSettings(Log log);


    public abstract void postProcessReceivedData(AnimasDevicePacket adp);


    public abstract void writeSettings(OutputWriter outputWritter);

}
