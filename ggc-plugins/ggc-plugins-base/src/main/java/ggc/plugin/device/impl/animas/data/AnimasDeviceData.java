package ggc.plugin.device.impl.animas.data;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.ATechDate;

import ggc.core.data.defs.ClockModeType;
import ggc.core.data.defs.GlucoseUnitType;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.impl.animas.comm.AnimasCommProtocolAbstract;
import ggc.plugin.device.impl.animas.data.dto.PumpConnectorInfo;
import ggc.plugin.device.impl.animas.data.dto.PumpInfo;
import ggc.plugin.device.impl.animas.enums.AnimasDataType;
import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;
import ggc.plugin.device.impl.animas.enums.AnimasTransferType;
import ggc.plugin.device.impl.animas.handler.AnimasDataWriter;

/**
 * Application:   GGC - GNU Gluco Control
 * Plug-in:       GGC PlugIn Base (base class for all plugins)
 * <p/>
 * See AUTHORS for copyright information.
 * <p/>
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p/>
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * <p/>
 * Filename:     AnimasDeviceData
 * Description:  Abstract class for Animas Device Data
 * <p/>
 * Author: Andy Rozman {andy@atech-software.com}
 */

public abstract class AnimasDeviceData
{

    private static final Logger LOG = LoggerFactory.getLogger(AnimasDeviceData.class);

    protected AnimasCommProtocolAbstract pumpCommunicationInterface;
    public PumpConnectorInfo pumpConnectorInfo;
    protected AnimasTransferType transferType;
    protected HashMap<String, String> concurrentOperation = new HashMap<String, String>();
    public PumpInfo pumpInfo;
    protected boolean debugDataSaving = false;
    protected AnimasDataWriter animasDeviceDataWriter;
    protected HashMap<AnimasDataType, AnimasDataType> dataReceived = new HashMap<AnimasDataType, AnimasDataType>();


    public AnimasDeviceData(AnimasDataWriter writer)
    {
        pumpConnectorInfo = new PumpConnectorInfo();
        pumpInfo = new PumpInfo();
        animasDeviceDataWriter = writer;
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
        loadSerialNumberIntoLocalSettings();
    }


    public void setBgUnit(boolean isMmolL)
    {
        this.pumpInfo.glucoseUnitType = isMmolL ? GlucoseUnitType.mmol_L : GlucoseUnitType.mg_dL;
        debug("bg Unit: " + this.pumpInfo.glucoseUnitType.getTranslation());
        loadBgUnitIntoLocalSettings();
    }


    protected abstract void loadBgUnitIntoLocalSettings();


    protected abstract void loadSerialNumberIntoLocalSettings();


    protected abstract void loadClockModeIntoLocalSettings();


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
        {
            return true;
        }
        else
        {
            return (dataType.isCommandAllowedForDeviceType(this.pumpInfo.deviceType));
        }
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
            writeData(apde.key, apde.dateTime, apde.value);
        }
    }


    public void writeData(String key, ATechDate dateTime, String value)
    {
        this.animasDeviceDataWriter.getDeviceValuesWriter().writeObject(key, dateTime, value);
    }


    public AnimasDataWriter getDataWriter()
    {
        return this.animasDeviceDataWriter;
    }


    public void setClockMode(short clockMode)
    {
        ClockModeType cmt = ClockModeType.getByCode(clockMode);
        debug("setClockMode: " + cmt.name());

        this.pumpInfo.clockMode = cmt;
        loadClockModeIntoLocalSettings();
    }


    public abstract void debugAllSettings(Logger log);


    public abstract void postProcessReceivedData(AnimasDevicePacket adp);


    public abstract void writeSettings(AnimasDataType outputWritter);


    public void setSoftwareCode(String swCode)
    {
        this.pumpInfo.softwareCode = swCode;
        loadSoftwareCodeIntoLocalSettings();
    }


    protected abstract void loadSoftwareCodeIntoLocalSettings();


    public void writeIdentification()
    {
        DeviceIdentification di = this.animasDeviceDataWriter.getOutputWriter().getDeviceIdentification();

        if (di == null)
        {
            return;
        }

        di.company = "Animas/One Touch";
        di.device_selected = "Vibe";

        di.deviceSerialNumber = this.pumpInfo.serialNumber;
        di.deviceSoftwareVersion = this.pumpInfo.softwareCode;

        this.animasDeviceDataWriter.getOutputWriter().writeDeviceIdentification();
    }


    public void resetRawSerialData()
    {
        // if (this.pumpConnectorInfo != null)
        {
            this.pumpConnectorInfo.rawSerialNumber = null;
        }
    }

}
