package ggc.pump.defs.device;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.plugin.data.enums.DeviceCompanyDefinition;
import ggc.plugin.data.enums.DeviceHandlerType;
import ggc.plugin.data.enums.DevicePortParameterType;
import ggc.plugin.data.enums.DeviceProgressStatus;
import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;
import ggc.plugin.device.impl.minimed.enums.MinimedDeviceType;
import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.protocol.DeviceConnectionProtocol;

public enum PumpDeviceDefinition implements DeviceDefinition
{

    // Animas / One Touch Devices

    Animas_IR1000(40001, "Animas IR 1000", "", "", AnimasDeviceType.Animas_IR1000,
            DeviceImplementationStatus.NotPlanned, DeviceCompanyDefinition.Animas, DeviceHandlerType.NoHandler,
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge,
            DeviceProgressStatus.Special, "", 0.1f, 0.1f, null, -1, 0, null),

    Animas_IR1200(40002, "Animas IR 1200", "an_ir1200.jpg", "INSTRUCTIONS_ANIMAS_V2", AnimasDeviceType.Animas_IR1200,
            DeviceImplementationStatus.Done, DeviceCompanyDefinition.Animas, DeviceHandlerType.AnimasV2PumpHandler,
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge,
            DeviceProgressStatus.Special, "", 0.1f, 0.1f, PumpTBRDefinition.AnimasTBR, -1, 0,
            PumpProfileDefinition.AnimasProfile),

    Animas_IR1250(40003, "Animas IR 1250", "an_ir1250.jpg", "INSTRUCTIONS_ANIMAS_V2", AnimasDeviceType.Animas_IR1250,
            DeviceImplementationStatus.Done, DeviceCompanyDefinition.Animas, DeviceHandlerType.AnimasV2PumpHandler,
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge,
            DeviceProgressStatus.Special, "", 0.1f, 0.1f, PumpTBRDefinition.AnimasTBR, -1, 0,
            PumpProfileDefinition.AnimasProfile),

    Animas_2200(40004, "Animas IR 2020", "an_ir2020.jpg", "INSTRUCTIONS_ANIMAS_V2", AnimasDeviceType.Animas_2200,
            DeviceImplementationStatus.Done, DeviceCompanyDefinition.Animas, DeviceHandlerType.AnimasV2PumpHandler,
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge,
            DeviceProgressStatus.Special, "", 0.05f, 0.05f, PumpTBRDefinition.AnimasTBR, -1, 0,
            PumpProfileDefinition.AnimasProfile),

    OneTouchPing(40005, "OneTouch Ping", "an_ping.jpg", "INSTRUCTIONS_ANIMAS_V2", AnimasDeviceType.Animas_Ping,
            DeviceImplementationStatus.Done, DeviceCompanyDefinition.Animas, DeviceHandlerType.AnimasV2PumpHandler,
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge,
            DeviceProgressStatus.Special, "", 0.05f, 0.025f, PumpTBRDefinition.AnimasTBR, -1, 0,
            PumpProfileDefinition.AnimasProfile),

    OneTouchVibe(40006, "OneTouch Vibe", "an_vibe.gif", "INSTRUCTIONS_ANIMAS_V2", AnimasDeviceType.Animas_Vibe,
            DeviceImplementationStatus.Done, DeviceCompanyDefinition.Animas, DeviceHandlerType.AnimasV2PumpHandler,
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge,
            DeviceProgressStatus.Special, "", 0.05f, 0.025f, PumpTBRDefinition.AnimasTBR, -1, 0,
            PumpProfileDefinition.AnimasProfile),

    // Deltec

    DeltecCosmo1700(50001, "Cosmo 1700", "de_cosmo1700.jpg", "INSTRUCTIONS_DELTEC_COSMO_1700", null,
            DeviceImplementationStatus.NotPlanned, DeviceCompanyDefinition.Deltec, DeviceHandlerType.NoHandler,
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.None, DeviceProgressStatus.Normal, "",
            0.1f, 0.1f, null, -1, 0, null), //

    DeltecCosmo1800(50002, "Cosmo 1800", "de_cosmo1800.jpg", "INSTRUCTIONS_DELTEC_COSMO_1800", null,
            DeviceImplementationStatus.NotPlanned, DeviceCompanyDefinition.Deltec, DeviceHandlerType.NoHandler,
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.None, DeviceProgressStatus.Normal, "",
            0.1f, 0.1f, null, -1, 0, null), //

    // Insulet

    Insulet_Omnipod(60001, "Omnipod", "in_omnipod.jpg", "INSTRUCTIONS_INSULET_OMNIPOD", null,
            DeviceImplementationStatus.Done, DeviceCompanyDefinition.Insulet, DeviceHandlerType.InsuletOmnipodHandler,
            DevicePortParameterType.NoParameters, DeviceConnectionProtocol.FileImport, DeviceProgressStatus.Special, "",
            0.1f, 0.05f, null, -1, 0, null),

    // Minimed (not done yet)

    Minimed_508_508c(10001, "Minimed 508/508c", "mm_508.jpg", "INSTRUCTIONS_MINIMED",
            MinimedDeviceType.Minimed_508_508c, DeviceImplementationStatus.NotPlanned, DeviceCompanyDefinition.Minimed,
            DeviceHandlerType.MinimedPumpHandler, DevicePortParameterType.PackedParameters,
            DeviceConnectionProtocol.Serial_USBBridge, DeviceProgressStatus.Special, "", 0.1f, 0.1f, null, -1, 0,
            PumpProfileDefinition.MinimedProfile), //

    Minimed_511(10003, "Minimed 511", "mm_515_715.jpg", "INSTRUCTIONS_MINIMED", MinimedDeviceType.Minimed_511,
            DeviceImplementationStatus.Planned, DeviceCompanyDefinition.Minimed, DeviceHandlerType.MinimedPumpHandler,
            DevicePortParameterType.PackedParameters, DeviceConnectionProtocol.Serial_USBBridge,
            DeviceProgressStatus.Special, "", 0.1f, 0.1f, null, -1, 0, PumpProfileDefinition.MinimedProfile), //

    Minimed_512_712(10004, "Minimed 512/712", "mm_515_715.jpg", "INSTRUCTIONS_MINIMED",
            MinimedDeviceType.Minimed_512_712, DeviceImplementationStatus.Planned, DeviceCompanyDefinition.Minimed,
            DeviceHandlerType.MinimedPumpHandler, DevicePortParameterType.PackedParameters,
            DeviceConnectionProtocol.Serial_USBBridge, DeviceProgressStatus.Special, "", 0.1f, 0.1f, null, -1, 0,
            PumpProfileDefinition.MinimedProfile), //

    Minimed_515_715(10005, "Minimed 515/715", "mm_515_715.jpg", "INSTRUCTIONS_MINIMED",
            MinimedDeviceType.Minimed_515_715, DeviceImplementationStatus.Planned, DeviceCompanyDefinition.Minimed,
            DeviceHandlerType.MinimedPumpHandler, DevicePortParameterType.PackedParameters,
            DeviceConnectionProtocol.Serial_USBBridge, DeviceProgressStatus.Special, "", 0.1f, 0.1f, null, -1, 0,
            PumpProfileDefinition.MinimedProfile), //

    Minimed_522_722(10006, "Minimed 522/722", "mm_522_722.jpg", "INSTRUCTIONS_MINIMED",
            MinimedDeviceType.Minimed_522_722, DeviceImplementationStatus.Planned, DeviceCompanyDefinition.Minimed,
            DeviceHandlerType.MinimedPumpHandler, DevicePortParameterType.PackedParameters,
            DeviceConnectionProtocol.Serial_USBBridge, DeviceProgressStatus.Special, "", 0.1f, 0.1f, null, -1, 0,
            PumpProfileDefinition.MinimedProfile), //

    Minimed_523_723(10007, "Minimed 523/723", "mm_522_722.jpg", "INSTRUCTIONS_MINIMED",
            MinimedDeviceType.Minimed_523_723, DeviceImplementationStatus.Planned, DeviceCompanyDefinition.Minimed,
            DeviceHandlerType.MinimedPumpHandler, DevicePortParameterType.PackedParameters,
            DeviceConnectionProtocol.Serial_USBBridge, DeviceProgressStatus.Special, "", 0.1f, 0.1f, null, -1, 0,
            PumpProfileDefinition.MinimedProfile), //

    Minimed_553_753_Revel(10008, "Minimed 553/753 (Revel)", "mm_554_veo.jpg", "INSTRUCTIONS_MINIMED",
            MinimedDeviceType.Minimed_553_753_Revel, DeviceImplementationStatus.Planned,
            DeviceCompanyDefinition.Minimed, DeviceHandlerType.MinimedPumpHandler,
            DevicePortParameterType.PackedParameters, DeviceConnectionProtocol.Serial_USBBridge,
            DeviceProgressStatus.Special, "", 0.1f, 0.1f, null, -1, 0, PumpProfileDefinition.MinimedProfile), //

    Minimed_554_754_Veo(10009, "Minimed 554/754 (Veo)", "mm_554_veo.jpg", "INSTRUCTIONS_MINIMED",
            MinimedDeviceType.Minimed_554_754_Veo, DeviceImplementationStatus.Planned, DeviceCompanyDefinition.Minimed,
            DeviceHandlerType.MinimedPumpHandler, DevicePortParameterType.PackedParameters,
            DeviceConnectionProtocol.Serial_USBBridge, DeviceProgressStatus.Special, "", 0.1f, 0.1f, null, -1, 0,
            PumpProfileDefinition.MinimedProfile), //

    Minimed_640G(10010, "Minimed 640G", "pic", "INSTRUCTIONS_MINIMED", MinimedDeviceType.Minimed_640G,
            DeviceImplementationStatus.Planned, DeviceCompanyDefinition.Minimed, DeviceHandlerType.NoHandler,
            DevicePortParameterType.PackedParameters, DeviceConnectionProtocol.USB_Hid, DeviceProgressStatus.Special,
            "", 0.1f, 0.1f, null, -1, 0, null),

    // Roche / Accu-Chek (bridged old implementation)

    DisetronicDTron(20001, "D-Tron (Disetronic)", "ac_dtron.jpg", "INSTRUCTIONS_ACCUCHEK_SPIRIT", null,
            DeviceImplementationStatus.Done, DeviceCompanyDefinition.Roche, DeviceHandlerType.AccuChekPumpHandler,
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.MassStorageXML,
            DeviceProgressStatus.Normal, "", 0.1f, 0.1f, PumpTBRDefinition.RocheTBR, -1, 1000,
            PumpProfileDefinition.RocheProfile), //

    AccuChekDTron(20002, "D-Tron", "ac_dtron.jpg", "INSTRUCTIONS_ACCUCHEK_SPIRIT", null,
            DeviceImplementationStatus.Done, DeviceCompanyDefinition.Roche, DeviceHandlerType.AccuChekPumpHandler,
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.MassStorageXML,
            DeviceProgressStatus.Normal, "", 0.1f, 0.1f, PumpTBRDefinition.RocheTBR, -1, 1000,
            PumpProfileDefinition.RocheProfile), //

    AccuChekSpirit(20003, "Spirit", "ac_spirit.jpg", "INSTRUCTIONS_ACCUCHEK_SPIRIT", null,
            DeviceImplementationStatus.Done, DeviceCompanyDefinition.Roche, DeviceHandlerType.AccuChekPumpHandler,
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.MassStorageXML,
            DeviceProgressStatus.Normal, "", 0.1f, 0.1f, PumpTBRDefinition.RocheTBR, -1, 2000,
            PumpProfileDefinition.RocheProfile), //

    AccuChekCombo(20004, "Combo", "ac_combo.jpg", "INSTRUCTIONS_ACCUCHEK_SPIRIT", null, DeviceImplementationStatus.Done,
            DeviceCompanyDefinition.Roche, DeviceHandlerType.AccuChekPumpHandler,
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.MassStorageXML,
            DeviceProgressStatus.Normal, "", 0.1f, 0.01f, PumpTBRDefinition.RocheTBR, -1, 2000,
            PumpProfileDefinition.RocheProfile), //

    // Sooil (Dana) (bridged old implementation)

    DanaDiabecare_II(70001, "Diabcare II", "so_danaII.jpg", null, null,
            DeviceImplementationStatus.DoesntSupportDownload, DeviceCompanyDefinition.Sooil,
            DeviceHandlerType.NoSupportInDevice, DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.None,
            DeviceProgressStatus.Normal, "", 0.1f, 0.1f, PumpTBRDefinition.DanaTBR, -1, -1,
            PumpProfileDefinition.DanaProfile), //

    DanaDiabecare_IIS(70002, "Diabcare II S/SG", "so_danaIISG.jpg", null, null,
            DeviceImplementationStatus.DoesntSupportDownload, DeviceCompanyDefinition.Sooil,
            DeviceHandlerType.NoSupportInDevice, DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.None,
            DeviceProgressStatus.Normal, "", 0.1f, 0.1f, PumpTBRDefinition.DanaTBR, -1, -1,
            PumpProfileDefinition.DanaProfile),

    DanaDiabecare_III_R(70003, "Diabcare II R (III)", "so_danaIII.jpg", "INSTRUCTIONS_DANA_III_R", null, //
            DeviceImplementationStatus.Done, DeviceCompanyDefinition.Sooil, DeviceHandlerType.DanaPumpHandler,
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.BlueTooth_Serial, //
            0.1f, null, 0.1f, null, //
            PumpTBRDefinition.DanaTBR, PumpProfileDefinition.DanaProfile),

    // Asante

    AsanteSnap(80001, "Asante Snap", null, null, null, //
            DeviceImplementationStatus.NotAvailable, DeviceCompanyDefinition.Asante, DeviceHandlerType.NoHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge, //
            0.1f, null, 0.1f, null, //
            PumpTBRDefinition.AsanteTBR, PumpProfileDefinition.AsanteProfile), //

    // Tandem

    TandemTSlim(90001, "t:slim", "tan_tslim.jpg", null, null, //
            DeviceImplementationStatus.NotAvailable, DeviceCompanyDefinition.Tandem, DeviceHandlerType.NoHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge, //
            0.1f, 0.05f, 0.05f, 0.05f, //
            PumpTBRDefinition.TandemTBR, PumpProfileDefinition.TandemProfile), //

    TandemTFlex(90002, "t:flex", "tan_tflex.jpg", null, null, //
            DeviceImplementationStatus.NotAvailable, DeviceCompanyDefinition.Tandem, DeviceHandlerType.NoHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge, //
            0.1f, 0.05f, 0.05f, 0.05f, //
            PumpTBRDefinition.TandemTBR, PumpProfileDefinition.TandemProfile), //

    TandemTSlimG4(90003, "t:slim G4", "tan_tslimG4.jpg", null, null, //
            DeviceImplementationStatus.NotAvailable, DeviceCompanyDefinition.Tandem, DeviceHandlerType.NoHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge, //
            0.1f, 0.05f, 0.05f, 0.05f, //
            PumpTBRDefinition.TandemTBR, PumpProfileDefinition.TandemProfile), //

    ;

    // Tandem

    static List<DeviceDefinition> allDevices;
    static List<DeviceDefinition> supportedDevices;
    private static final Logger LOG = LoggerFactory.getLogger(PumpDeviceDefinition.class);

    static
    {
        allDevices = new ArrayList<DeviceDefinition>();
        supportedDevices = new ArrayList<DeviceDefinition>();

        for (PumpDeviceDefinition pdd : values())
        {
            allDevices.add(pdd);

            if (pdd.isSupportedDevice())
            {
                supportedDevices.add(pdd);
            }
        }

        // LOG.info("Pump Devices V2 (registered: " + allDevices.size() +
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
    Float basalStep;
    Float bolusStep;
    PumpTBRDefinition tempBasalType;
    Integer monthsStored;
    Integer maxRecords;
    PumpProfileDefinition profileDefinition;
    Float basalMinPerH;
    Float bolusMin;


    PumpDeviceDefinition()
    {
        throw new NotImplementedException();
    }


    @Deprecated
    PumpDeviceDefinition(int id, String name, String iconName, String instructions, Object internalDefinition,
            DeviceImplementationStatus implementationStatus, DeviceCompanyDefinition companyDefinition,
            DeviceHandlerType deviceHandlerType, DevicePortParameterType portParameterType,
            DeviceConnectionProtocol connectionProtocol, DeviceProgressStatus progressStatus, String specialComment,
            Float basalStep, Float bolusStep, PumpTBRDefinition tempBasalType, Integer monthsStored, Integer maxRecords,
            PumpProfileDefinition profileDefinition)
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
        this.basalStep = basalStep;
        this.bolusStep = bolusStep;
        this.tempBasalType = tempBasalType;
        this.monthsStored = monthsStored;
        this.maxRecords = maxRecords;
        this.profileDefinition = profileDefinition;
    }


    PumpDeviceDefinition(int id, String name, String iconName, String instructions, Object internalDefinition,
            DeviceImplementationStatus implementationStatus, DeviceCompanyDefinition companyDefinition,
            DeviceHandlerType deviceHandlerType, DevicePortParameterType portParameterType,
            DeviceConnectionProtocol connectionProtocol, Float basalStep, Float basalMinH, Float bolusStep,
            Float bolusMin, PumpTBRDefinition tempBasalType, PumpProfileDefinition profileDefinition)

    {
        this(id, name, iconName, instructions, internalDefinition, implementationStatus, companyDefinition,
                deviceHandlerType, portParameterType, connectionProtocol, basalStep, basalMinH, bolusStep, bolusMin,
                tempBasalType, profileDefinition, DeviceProgressStatus.Special, -1, -1, null);
    }


    PumpDeviceDefinition(int id, String name, String iconName, String instructions, Object internalDefinition,
            DeviceImplementationStatus implementationStatus, DeviceCompanyDefinition companyDefinition,
            DeviceHandlerType deviceHandlerType, DevicePortParameterType portParameterType,
            DeviceConnectionProtocol connectionProtocol, Float basalStep, Float basalMinPerH, Float bolusStep,
            Float bolusMin, PumpTBRDefinition tempBasalType, PumpProfileDefinition profileDefinition,
            DeviceProgressStatus progressStatus, Integer monthsStored, Integer maxRecords, String specialComment)
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
        this.basalStep = basalStep;
        this.bolusStep = bolusStep;
        this.tempBasalType = tempBasalType;
        this.monthsStored = monthsStored;
        this.maxRecords = maxRecords;
        this.profileDefinition = profileDefinition;
        this.basalMinPerH = basalMinPerH;
        this.bolusMin = bolusMin;
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


    public static boolean isSupportedDevice(PumpDeviceDefinition pumpDeviceDefinition)
    {
        return DeviceImplementationStatus.isSupportedDevice(pumpDeviceDefinition.getDeviceImplementationStatus());
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


    @Override
    public String toString()
    {
        return new ToStringBuilder(this).append("deviceId", deviceId).append("deviceName", deviceName)
                .append("iconName", iconName).append("instructions", instructions)
                .append("internalDefintion", internalDefintion).append("implementationStatus", implementationStatus)
                .append("companyDefinition", companyDefinition).append("deviceHandlerType", deviceHandlerType)
                .append("devicePortParameterType", devicePortParameterType)
                .append("deviceConnectionProtocol", deviceConnectionProtocol)
                .append("deviceProgressStatus", deviceProgressStatus).append("specialComment", specialComment)
                .toString();
    }


    public Float getBasalStep()
    {
        return basalStep;
    }


    public void setBasalStep(Float basalStep)
    {
        this.basalStep = basalStep;
    }


    public Float getBolusStep()
    {
        return bolusStep;
    }


    public void setBolusStep(Float bolusStep)
    {
        this.bolusStep = bolusStep;
    }


    public PumpTBRDefinition getTempBasalType()
    {
        return tempBasalType;
    }


    public void setTempBasalType(PumpTBRDefinition tempBasalType)
    {
        this.tempBasalType = tempBasalType;
    }


    public Integer getMonthsStored()
    {
        return monthsStored;
    }


    public void setMonthsStored(Integer monthsStored)
    {
        this.monthsStored = monthsStored;
    }


    public Integer getMaxRecords()
    {
        return maxRecords;
    }


    public void setMaxRecords(Integer maxRecords)
    {
        this.maxRecords = maxRecords;
    }


    public PumpProfileDefinition getProfileDefinition()
    {
        return profileDefinition;
    }


    public void setProfileDefinition(PumpProfileDefinition profileDefinition)
    {
        this.profileDefinition = profileDefinition;
    }
}
