package ggc.cgms.defs.device;

import java.util.ArrayList;
import java.util.List;

import ggc.cgms.device.dexcom.receivers.DexcomDevice;
import ggc.plugin.data.enums.DeviceCompanyDefinition;
import ggc.plugin.data.enums.DeviceHandlerType;
import ggc.plugin.data.enums.DevicePortParameterType;
import ggc.plugin.data.enums.DeviceProgressStatus;
import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;
import ggc.plugin.device.impl.minimed.enums.MinimedDeviceType;
import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.protocol.DeviceConnectionProtocol;

/**
 *  Application: GGC - GNU Gluco Control
 *  Plug-in: CGMS Tool (support for CGMS devices)
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
 *  Filename: CGMSDeviceDefinition
 *  Description: CGMS Device Definition
 *
 *  Author: Andy {andy@atech-software.com}
 */

public enum CGMSDeviceDefinition implements DeviceDefinition
{
    // Abbott

    AbbottFreeStyleNavigatorV1(10001, "Abbott FreeStyle Navigator V1", null, null, //
            null, DeviceImplementationStatus.NotAvailable, //
            DeviceCompanyDefinition.Abbott, DeviceHandlerType.NoHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge, //
            DeviceProgressStatus.Special, ""), // TODO

    AbbottFreeStyleNavigatorV2(10002, "Abbott FreeStyle Navigator V2", null, null, //
            null, DeviceImplementationStatus.NotAvailable, //
            DeviceCompanyDefinition.Abbott, DeviceHandlerType.NoHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge, //
            DeviceProgressStatus.Special, ""), // TODO

    AbbottFreeStyleNeo(10003, "Abbott FreeStyle Neo", null, null, //
            null, DeviceImplementationStatus.NotAvailable, //
            DeviceCompanyDefinition.Abbott, DeviceHandlerType.NoHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge, //
            DeviceProgressStatus.Special, ""), // TODO

    // Animas

    OneTouchVibe(40006, "OneTouch Vibe", "an_vibe_cgms.jpg", "INSTRUCTIONS_ANIMAS_V2", //
            AnimasDeviceType.Animas_Vibe, DeviceImplementationStatus.Done, //
            DeviceCompanyDefinition.Animas, DeviceHandlerType.AnimasV2CGMSHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge, //
            DeviceProgressStatus.Special, ""),

    // Dexcom

    Dexcom7(20001, "Dexcom 7", null, null, //
            DexcomDevice.Dexcom7, DeviceImplementationStatus.NotAvailable, //
            DeviceCompanyDefinition.Dexcom, DeviceHandlerType.NoHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge, //
            DeviceProgressStatus.Special, ""), // TODO

    Dexcom7Plus(20002, "Dexcom 7 Plus", null, null, //
            DexcomDevice.Dexcom7, DeviceImplementationStatus.NotAvailable, //
            DeviceCompanyDefinition.Dexcom, DeviceHandlerType.NoHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge, //
            DeviceProgressStatus.Special, ""), // TODO

    DexcomG4(20003, "Dexcom G4", "dx_dexcomG4.jpg", "DEXCOM_INSTRUCTIONS_DL_SERIAL_USB", //
            DexcomDevice.Dexcom_G4, DeviceImplementationStatus.Done, //
            DeviceCompanyDefinition.Dexcom, DeviceHandlerType.DexcomHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge, //
            DeviceProgressStatus.Special, ""),

    DexcomG5(20004, "Dexcom G5", null, null, //
            DexcomDevice.DexcomG5, DeviceImplementationStatus.NotAvailable, //
            DeviceCompanyDefinition.Dexcom, DeviceHandlerType.NoHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge, //
            DeviceProgressStatus.Special, ""), // TODO

    // Minimed

    MinimedCGMSGold(30001, "Minimed CGMS Gold", "mm_522_722.jpg", "INSTRUCTIONS_MINIMED",
            MinimedDeviceType.MinimedCGMSGold, DeviceImplementationStatus.Planned, //
            DeviceCompanyDefinition.Minimed, DeviceHandlerType.MinimedPumpHandler, //
            DevicePortParameterType.PackedParameters, DeviceConnectionProtocol.Multiple, //
            DeviceProgressStatus.Special, ""), //

    MinimedGuradianRealTime(30002, "Minimed Guardian RealTime", "mm_522_722.jpg", "INSTRUCTIONS_MINIMED",
            MinimedDeviceType.MinimedGuradianRealTime, DeviceImplementationStatus.Planned, //
            DeviceCompanyDefinition.Minimed, DeviceHandlerType.MinimedPumpHandler, //
            DevicePortParameterType.PackedParameters, DeviceConnectionProtocol.Multiple, //
            DeviceProgressStatus.Special, ""), //

    Minimed_522_722(30003, "Minimed 522/722", "mm_522_722.jpg", "INSTRUCTIONS_MINIMED",
            MinimedDeviceType.Minimed_522_722, DeviceImplementationStatus.Planned, //
            DeviceCompanyDefinition.Minimed, DeviceHandlerType.MinimedPumpHandler, //
            DevicePortParameterType.PackedParameters, DeviceConnectionProtocol.Multiple, //
            DeviceProgressStatus.Special, ""), //

    Minimed_523_723(30004, "Minimed 523/723", "mm_522_722.jpg", "INSTRUCTIONS_MINIMED", //
            MinimedDeviceType.Minimed_523_723, DeviceImplementationStatus.Planned, //
            DeviceCompanyDefinition.Minimed, DeviceHandlerType.MinimedPumpHandler, //
            DevicePortParameterType.PackedParameters, DeviceConnectionProtocol.Multiple, //
            DeviceProgressStatus.Special, ""), //

    Minimed_553_753_Revel(30005, "Minimed 553/753 (Revel)", "mm_554_veo.jpg", "INSTRUCTIONS_MINIMED", //
            MinimedDeviceType.Minimed_553_753_Revel, DeviceImplementationStatus.Planned, //
            DeviceCompanyDefinition.Minimed, DeviceHandlerType.MinimedPumpHandler, //
            DevicePortParameterType.PackedParameters, DeviceConnectionProtocol.Multiple, //
            DeviceProgressStatus.Special, ""), //

    Minimed_554_754_Veo(30006, "Minimed 554/754 (Veo)", "mm_554_veo.jpg", "INSTRUCTIONS_MINIMED",
            MinimedDeviceType.Minimed_554_754_Veo, DeviceImplementationStatus.Planned, //
            DeviceCompanyDefinition.Minimed, DeviceHandlerType.MinimedPumpHandler, //
            DevicePortParameterType.PackedParameters, DeviceConnectionProtocol.Multiple, //
            DeviceProgressStatus.Special, ""), //

    Minimed_640G(30007, "Minimed 640G", "pic", "INSTRUCTIONS_MINIMED", //
            MinimedDeviceType.Minimed_640G, DeviceImplementationStatus.Planned, //
            DeviceCompanyDefinition.Minimed, DeviceHandlerType.NoHandler, //
            DevicePortParameterType.PackedParameters, DeviceConnectionProtocol.Multiple, //
            DeviceProgressStatus.Special, ""), //

    // Tandem

    TandemTSlimG4(50001, "t:slim G4", "tan_tslimG4.jpg", null, //
            null, DeviceImplementationStatus.NotAvailable, //
            DeviceCompanyDefinition.Tandem, DeviceHandlerType.NoHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge, //
            DeviceProgressStatus.Special, ""), // TODO

    ;

    static List<DeviceDefinition> allDevices;
    static List<DeviceDefinition> supportedDevices;

    static
    {
        allDevices = new ArrayList<DeviceDefinition>();
        supportedDevices = new ArrayList<DeviceDefinition>();

        for (CGMSDeviceDefinition cdd : values())
        {
            allDevices.add(cdd);

            if (cdd.isSupportedDevice())
            {
                supportedDevices.add(cdd);
            }
        }

        // LOG.info("CGMS Devices V2 (registered: " + allDevices.size() +
        // ", supported: " + supportedDevices.size() + ")");

    }

    // we need to extend this to all values currently in DeviceImplementation
    int deviceId;
    String deviceName;
    String iconName;
    String instructions;
    Object internalDefintion;
    DeviceImplementationStatus implementationStatus;
    DeviceCompanyDefinition companyDefinition;
    DeviceHandlerType deviceHandlerType = DeviceHandlerType.NullHandler;
    DevicePortParameterType devicePortParameterType;
    DeviceConnectionProtocol deviceConnectionProtocol;
    DeviceProgressStatus deviceProgressStatus;
    String specialComment;


    CGMSDeviceDefinition(int id)
    {
        this.deviceId = id;
    }


    CGMSDeviceDefinition(int id, String name, String iconName, String instructions, Object internalDefinition,
            DeviceImplementationStatus implementationStatus, DeviceCompanyDefinition companyDefinition,
            DeviceHandlerType deviceHandlerType, DevicePortParameterType portParameterType,
            DeviceConnectionProtocol connectionProtocol, DeviceProgressStatus progressStatus, String specialComment)
    {
        this.deviceId = id;
        this.deviceName = name;
        this.iconName = iconName;
        this.instructions = instructions;
        this.internalDefintion = internalDefinition;
        this.implementationStatus = implementationStatus;
        this.companyDefinition = companyDefinition;
        this.deviceHandlerType = deviceHandlerType;
        this.devicePortParameterType = portParameterType;
        this.deviceConnectionProtocol = connectionProtocol;
        this.deviceProgressStatus = progressStatus;
        this.specialComment = specialComment;
    }


    public int getDeviceId()
    {
        return deviceId;
    }


    public String getDeviceName()
    {
        return this.deviceName;
    }


    public String getIconName()
    {
        return this.iconName;
    }


    public String getInstructionsI18nKey()
    {
        return this.instructions;
    }


    public DeviceImplementationStatus getDeviceImplementationStatus()
    {
        return this.implementationStatus;
    }


    public Object getInternalDefintion()
    {
        return this.internalDefintion;
    }


    public DeviceCompanyDefinition getDeviceCompany()
    {
        return this.companyDefinition;
    }


    public DeviceHandlerType getDeviceHandler()
    {
        return this.deviceHandlerType;
    }


    public static List<DeviceDefinition> getSupportedDevices()
    {
        return supportedDevices;
    }


    public static List<DeviceDefinition> getAllDevices()
    {
        return allDevices;
    }


    public boolean isSupportedDevice()
    {
        return isSupportedDevice(this);
    }


    public static boolean isSupportedDevice(CGMSDeviceDefinition cgmsDeviceDefinition)
    {
        return DeviceImplementationStatus.isSupportedDevice(cgmsDeviceDefinition.getDeviceImplementationStatus());
    }


    /**
     * Get Device handler key
     * @return
     */
    public DeviceHandlerType getDeviceHandlerKey()
    {
        return this.deviceHandlerType;
    }


    /**
     * Device Port Parameter Type
     * @return
     */
    public DevicePortParameterType getDevicePortParameterType()
    {
        return this.devicePortParameterType;
    }


    /**
     * Get Connection Protocol
     *
     * @return
     */
    public DeviceConnectionProtocol getConnectionProtocol()
    {
        return this.deviceConnectionProtocol;
    }


    /**
     * Get Device Progress Status. It determines how device progress is determined. In most casess we use Special
     * progress which is then implemented by Handler.
     *
     * @return
     */
    public DeviceProgressStatus getDeviceProgressStatus()
    {
        return this.deviceProgressStatus;
    }


    public String getSpecialComment()
    {
        return this.specialComment;
    }

}
