package ggc.meter.defs.device;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.meter.device.MeterDisplayInterfaceType;
import ggc.plugin.data.enums.DeviceCompanyDefinition;
import ggc.plugin.data.enums.DeviceHandlerType;
import ggc.plugin.data.enums.DevicePortParameterType;
import ggc.plugin.data.enums.DeviceProgressStatus;
import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.protocol.DeviceConnectionProtocol;

/**
 * Created by andy on 06.02.15.
 */
public enum MeterDeviceDefinition implements DeviceDefinition
{

    // --------------------
    // Ascensia
    // --------------------
    AscensiaContourUsb(10007, "Contour USB", "ascensia_contour_usb.jpg", //
            "INSTRUCTIONS_ASCENSIA_CONTOUR_USB", null, DeviceImplementationStatus.Done, //
            DeviceCompanyDefinition.Ascensia, DeviceHandlerType.AscensiaUsbHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.USB_Hid, //
            DeviceProgressStatus.Special, "", 2000, //
            MeterDisplayInterfaceType.Simple),

    AscensiaContourNext(10008, "Contour Next", "ascensia_contour_next.jpg", "INSTRUCTIONS_ASCENSIA_CONTOUR_USB", null,
            DeviceImplementationStatus.Done, DeviceCompanyDefinition.Ascensia, DeviceHandlerType.AscensiaUsbHandler,
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.USB_Hid, DeviceProgressStatus.Special, "",
            800, MeterDisplayInterfaceType.Simple),

    AscensiaContourNextUsb(10009, "Contour Next USB", "ascensia_contour_nextusb.jpg",
            "INSTRUCTIONS_ASCENSIA_CONTOUR_USB", null, DeviceImplementationStatus.Done,
            DeviceCompanyDefinition.Ascensia, DeviceHandlerType.AscensiaUsbHandler,
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.USB_Hid, DeviceProgressStatus.Special, "",
            2000, MeterDisplayInterfaceType.Simple),

    AscensiaContourNextLink(10010, "Contour Next Link", "ascensia_contour_nextlink.jpg", //
            "INSTRUCTIONS_ASCENSIA_CONTOUR_USB", null, DeviceImplementationStatus.Done, //
            DeviceCompanyDefinition.Ascensia, DeviceHandlerType.AscensiaUsbHandler,
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.USB_Hid, DeviceProgressStatus.Special, "",
            1000, MeterDisplayInterfaceType.Simple),

    AscensiaContourNextOne(10011, "Contour Next One", "ascensia_contour_next_one.png", //
            "INSTRUCTIONS_ASCENSIA_CONTOUR_USB", null, DeviceImplementationStatus.Done, //
            DeviceCompanyDefinition.Ascensia, DeviceHandlerType.AscensiaUsbHandler,
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.USB_Hid, DeviceProgressStatus.Special, "",
            800, MeterDisplayInterfaceType.Simple),

    // -- Missing: Contour Next EZ, Contour Next Link 2.4, Didget, Contour XT ?
    // -- Planned: Contour Next Link 2.4 (Test with current usb code)
    // -- Not planned: Contour Next EZ, Didget, Contour XT (this might actually work without any
    // changes)

    // --------------------
    // AccuChek
    // --------------------
    AccuChekSmartPixGeneric(20001, "SmartPix", "ac_active.jpg", //
            "INSTRUCTIONS_ACCUCHEK_ACTIVE", null, DeviceImplementationStatus.Done, //
            DeviceCompanyDefinition.Roche, DeviceHandlerType.AccuChekMeterHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.MassStorageXML, //
            DeviceProgressStatus.Normal, "", 1, MeterDisplayInterfaceType.Simple), //

    AccuChekActive(20002, "Active", "ac_active.jpg", //
            "INSTRUCTIONS_ACCUCHEK_ACTIVE", null, DeviceImplementationStatus.Done, //
            DeviceCompanyDefinition.Roche, DeviceHandlerType.AccuChekMeterHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.MassStorageXML, //
            DeviceProgressStatus.Normal, "", 480, MeterDisplayInterfaceType.Simple), //

    AccuChekAdvantage(20003, "Advantage", "ac_advantage.jpg", //
            "INSTRUCTIONS_ACCUCHEK_ADVANTAGE", null, DeviceImplementationStatus.Done, //
            DeviceCompanyDefinition.Roche, DeviceHandlerType.AccuChekMeterHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.MassStorageXML, //
            DeviceProgressStatus.Normal, "", 480, MeterDisplayInterfaceType.Simple), //

    AccuChekAviva(20004, "Aviva", "ac_aviva.jpg", //
            "INSTRUCTIONS_ACCUCHEK_AVIVA", null, DeviceImplementationStatus.Done, //
            DeviceCompanyDefinition.Roche, DeviceHandlerType.AccuChekMeterHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.MassStorageXML, //
            DeviceProgressStatus.Normal, "", 500, MeterDisplayInterfaceType.Simple), //

    AccuChekComfort(20005, "Comfort", "ac_comfort.jpg", //
            "INSTRUCTIONS_ACCUCHEK_COMFORT", null, DeviceImplementationStatus.Done, //
            DeviceCompanyDefinition.Roche, DeviceHandlerType.AccuChekMeterHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.MassStorageXML, //
            DeviceProgressStatus.Normal, "", 100, MeterDisplayInterfaceType.Simple), //

    AccuChekCompact(20006, "Compact", "ac_compact.jpg", //
            "INSTRUCTIONS_ACCUCHEK_COMPACT", null, DeviceImplementationStatus.Done, //
            DeviceCompanyDefinition.Roche, DeviceHandlerType.AccuChekMeterHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.MassStorageXML, //
            DeviceProgressStatus.Normal, "", 100, MeterDisplayInterfaceType.Simple), //

    AccuChekCompactPlus(20007, "CompactPlus", "ac_compact_plus.jpg", //
            "INSTRUCTIONS_ACCUCHEK_COMPACTPLUS", null, DeviceImplementationStatus.Done, //
            DeviceCompanyDefinition.Roche, DeviceHandlerType.AccuChekMeterHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.MassStorageXML, //
            DeviceProgressStatus.Normal, "", 300, MeterDisplayInterfaceType.Simple), //

    AccuChekGo(20008, "Go", "ac_go.jpg", //
            "INSTRUCTIONS_ACCUCHEK_GO", null, DeviceImplementationStatus.Done, //
            DeviceCompanyDefinition.Roche, DeviceHandlerType.AccuChekMeterHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.MassStorageXML, //
            DeviceProgressStatus.Normal, "", 480, MeterDisplayInterfaceType.Simple), //

    AccuChekIntegra(20009, "Integra", "ac_integra.jpg", //
            "INSTRUCTIONS_ACCUCHEK_INTEGRA", null, DeviceImplementationStatus.Done, //
            DeviceCompanyDefinition.Roche, DeviceHandlerType.AccuChekMeterHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.MassStorageXML, //
            DeviceProgressStatus.Normal, "", 100, MeterDisplayInterfaceType.Simple), //

    AccuChekPerforma(20010, "Performa", "ac_performa.jpg", //
            "INSTRUCTIONS_ACCUCHEK_PERFORMA", null, DeviceImplementationStatus.Done, //
            DeviceCompanyDefinition.Roche, DeviceHandlerType.AccuChekMeterHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.MassStorageXML, //
            DeviceProgressStatus.Normal, "", 500, MeterDisplayInterfaceType.Simple), //

    AccuChekSensor(20011, "Sensor", "ac_sensor.jpg", //
            "INSTRUCTIONS_ACCUCHEK_SENSOR", null, DeviceImplementationStatus.Done, //
            DeviceCompanyDefinition.Roche, DeviceHandlerType.AccuChekMeterHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.MassStorageXML, //
            DeviceProgressStatus.Normal, "", 480, MeterDisplayInterfaceType.Simple), //

    AccuChekNano(20012, "Aviva Nano", "ac_nano.jpg", //
            "INSTRUCTIONS_ACCUCHEK_NANO", null, DeviceImplementationStatus.Done, //
            DeviceCompanyDefinition.Roche, DeviceHandlerType.AccuChekMeterHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.MassStorageXML, //
            DeviceProgressStatus.Normal, "", 500, MeterDisplayInterfaceType.Simple), //

    AccuChekAvivaCombo(20013, "Aviva Combo", "ac_combo.jpg", //
            "INSTRUCTIONS_ACCUCHEK_AVIVA_COMBO", null, DeviceImplementationStatus.Done, //
            DeviceCompanyDefinition.Roche, DeviceHandlerType.AccuChekMeterHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.MassStorageXML, //
            DeviceProgressStatus.Normal, "", 500, MeterDisplayInterfaceType.Extended), //

    // -- Missing: Mobile, Connect, ...Connect

    // --------------------
    // Meanrini
    // --------------------
    MenariniGlucoFixId(50003, "GlucoFix ID", "mn_glucofix_id.png", //
            "INSTRUCTIONS_MENARINI", null, DeviceImplementationStatus.Planned, //
            DeviceCompanyDefinition.Menarini, DeviceHandlerType.MenariniMeterHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge, //
            DeviceProgressStatus.Special, "", 250, MeterDisplayInterfaceType.Simple), //

    MenariniGlucoFixMio(50001, "GlucoFix Mio", "mn_glucofix_mio_plus.png", //
            "INSTRUCTIONS_MENARINI", null, DeviceImplementationStatus.Planned, //
            DeviceCompanyDefinition.Menarini, DeviceHandlerType.MenariniMeterHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge, //
            DeviceProgressStatus.Special, "", 400, MeterDisplayInterfaceType.Simple), //

    MenariniGlucoFixMioPlus(50002, "GlucoFix Mio Plus", "mn_glucofix_mio_plus.png", //
            "INSTRUCTIONS_MENARINI", null, DeviceImplementationStatus.Planned, //
            DeviceCompanyDefinition.Menarini, DeviceHandlerType.MenariniMeterHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge, //
            DeviceProgressStatus.Special, "", 400, MeterDisplayInterfaceType.Extended), // ket,

    MenariniGlucoFixPremium(50004, "GlucoFix Premium", "mn_glucofix_premium.png", //
            "INSTRUCTIONS_MENARINI", null, DeviceImplementationStatus.Planned, //
            DeviceCompanyDefinition.Menarini, DeviceHandlerType.MenariniMeterHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge, //
            DeviceProgressStatus.Special, "", 400, MeterDisplayInterfaceType.Extended), // ket,

    MenariniGlucoFixTech(50005, "GlucoFix Tech", "mn_glucofix_tech.png", //
            "INSTRUCTIONS_MENARINI", null, DeviceImplementationStatus.Planned, //
            DeviceCompanyDefinition.Menarini, DeviceHandlerType.MenariniMeterHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge, //
            DeviceProgressStatus.Special, "", 730, MeterDisplayInterfaceType.Simple), //

    MenariniGlucoMenVisio(50006, "GlucoMen Visio", "mn_glucomen_visio.png", //
            "INSTRUCTIONS_MENARINI", null, DeviceImplementationStatus.Planned, //
            DeviceCompanyDefinition.Menarini, DeviceHandlerType.MenariniMeterHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge, //
            DeviceProgressStatus.Special, "", 250, MeterDisplayInterfaceType.Simple), //

    MenariniGlucoMenGM(50007, "GlucoMen GM", "mn_glucomen_gm.png", //
            "INSTRUCTIONS_MENARINI", null, DeviceImplementationStatus.Testing, //
            DeviceCompanyDefinition.Menarini, DeviceHandlerType.MenariniMeterHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge, //
            DeviceProgressStatus.Special, "", 250, MeterDisplayInterfaceType.Simple), //

    MenariniGlucoMenPC(50014, "GlucoMen PC", "mn_glucomen_gm.png", //
            "INSTRUCTIONS_MENARINI", null, DeviceImplementationStatus.Testing, //
            DeviceCompanyDefinition.Menarini, DeviceHandlerType.MenariniMeterHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge, //
            DeviceProgressStatus.Special, "", 250, MeterDisplayInterfaceType.Simple), //

    MenariniGlucoMenLxPlus(50008, "GlucoMen Lx Plus", "mn_glucomen_lx_plus.png", //
            "INSTRUCTIONS_MENARINI", null, DeviceImplementationStatus.Testing, //
            DeviceCompanyDefinition.Menarini, DeviceHandlerType.MenariniMeterHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge, //
            DeviceProgressStatus.Special, "", 400, MeterDisplayInterfaceType.Extended), // ket,

    MenariniGlucoMenLx2(50009, "GlucoMen Lx 2", "mn_glucomen_lx2.png", //
            "INSTRUCTIONS_MENARINI", null, DeviceImplementationStatus.Testing, //
            DeviceCompanyDefinition.Menarini, DeviceHandlerType.MenariniMeterHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge, //
            DeviceProgressStatus.Special, "", 400, MeterDisplayInterfaceType.Extended), // ket

    MenariniGlucoMenMendor(50010, "GlucoMen Mendor", "mn_glucomen_ready.png", //
            "INSTRUCTIONS_MENARINI", null, DeviceImplementationStatus.Testing, //
            DeviceCompanyDefinition.Menarini, DeviceHandlerType.MenariniMeterHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge, //
            DeviceProgressStatus.Special, "", 500, MeterDisplayInterfaceType.Simple), //

    MenariniGlucoMenReady(50011, "GlucoMen Ready", "mn_glucomen_ready.png", //
            "INSTRUCTIONS_MENARINI", null, DeviceImplementationStatus.Testing, //
            DeviceCompanyDefinition.Menarini, DeviceHandlerType.MenariniMeterHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge, //
            DeviceProgressStatus.Special, "", 500, MeterDisplayInterfaceType.Simple), //

    MenariniGlucoMenAreo(50012, "GlucoMen Areo", "mn_glucomen_areo.png", //
            "INSTRUCTIONS_MENARINI", null, DeviceImplementationStatus.Testing, //
            DeviceCompanyDefinition.Menarini, DeviceHandlerType.MenariniMeterHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge, //
            DeviceProgressStatus.Special, "", 730, MeterDisplayInterfaceType.Simple), //

    MenariniGlucoMenAreo2k(50013, "GlucoMen Areo 2K", "mn_glucomen_areo_2k.png", //
            "INSTRUCTIONS_MENARINI", null, DeviceImplementationStatus.Testing, //
            DeviceCompanyDefinition.Menarini, DeviceHandlerType.MenariniMeterHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge, //
            DeviceProgressStatus.Special, "", 830, MeterDisplayInterfaceType.Extended), // ket,

    // --------------------
    // Arkray
    // --------------------
    ArkrayGlucoXMeter(60003, "Arkray Glucocard X-Meter", "ark_glucocard_sm.png", //
            "INSTRUCTIONS_ARKRAY", null, DeviceImplementationStatus.Testing, //
            DeviceCompanyDefinition.Arkray, DeviceHandlerType.ArkrayMeterHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.USB_Hid, //
            DeviceProgressStatus.Special, "", 0, MeterDisplayInterfaceType.Simple), //

    ArkrayGT_1810(60004, "Arkray Glucocard G+ (1810)", "ark_glucocard_gplus.png", //
            "INSTRUCTIONS_ARKRAY", null, DeviceImplementationStatus.Testing, //
            DeviceCompanyDefinition.Arkray, DeviceHandlerType.ArkrayMeterHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge, //
            DeviceProgressStatus.Special, "", 450, MeterDisplayInterfaceType.Simple), //

    ArkrayGT_1820(60005, "Arkray Glucocard G+ (1820)", "ark_glucocard_gplus.png", //
            "INSTRUCTIONS_ARKRAY", null, DeviceImplementationStatus.Testing, //
            DeviceCompanyDefinition.Arkray, DeviceHandlerType.ArkrayMeterHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge, //
            DeviceProgressStatus.Special, "", 450, MeterDisplayInterfaceType.Simple), //

    ArkrayGlucoCardMX(60006, "Arkray Glucocard MX", "ark_glucocard_mx.png", //
            "INSTRUCTIONS_ARKRAY", null, DeviceImplementationStatus.Testing, //
            DeviceCompanyDefinition.Arkray, DeviceHandlerType.ArkrayMeterHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge, //
            DeviceProgressStatus.Special, "", 500, MeterDisplayInterfaceType.Simple), //

    ArkrayGlucoCardSM(60007, "Arkray Glucocard SM", "ark_glucocard_sm.png", //
            "INSTRUCTIONS_ARKRAY", null, DeviceImplementationStatus.Testing, //
            DeviceCompanyDefinition.Arkray, DeviceHandlerType.ArkrayMeterHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.USB_Hid, //
            DeviceProgressStatus.Special, "", 500, MeterDisplayInterfaceType.Simple), //

    // Missing: Implementations there but incomplete: ArkrayGlucoCardPlus() [450],
    // ArkrayGlucoCard(), ArkrayGlucoCardMemoryPC(),


    // --------------------
    // Abbott
    // --------------------
    AbbottFreeStyleOptiumNeo(50003, "GlucoFix ID", "mn_glucofix_id.png", //
            "INSTRUCTIONS_MENARINI", null, DeviceImplementationStatus.Planned, //
            DeviceCompanyDefinition.Menarini, DeviceHandlerType.MenariniMeterHandler, //
            DevicePortParameterType.SimpleParameter, DeviceConnectionProtocol.Serial_USBBridge, //
            DeviceProgressStatus.Special, "", 250, MeterDisplayInterfaceType.Simple), //



    ;

    static List<DeviceDefinition> allDevices;
    static List<DeviceDefinition> supportedDevices;
    private static final Logger LOG = LoggerFactory.getLogger(MeterDeviceDefinition.class);

    static
    {
        allDevices = new ArrayList<DeviceDefinition>();
        supportedDevices = new ArrayList<DeviceDefinition>();

        for (MeterDeviceDefinition mdd : values())
        {
            allDevices.add(mdd);

            if (mdd.isSupportedDevice())
            {
                supportedDevices.add(mdd);
            }
        }

        // LOG.info("Meter Devices V2 (registered: " + allDevices.size() +
        // ", supported: " + supportedDevices.size() + ")");
    }

    // we need to extend this to all values currently in DeviceImplementation
    int deviceId;
    String deviceName = "no name";
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
    int maxRecords;
    MeterDisplayInterfaceType displayInterfaceType;


    @Deprecated
    MeterDeviceDefinition()
    {

    }


    MeterDeviceDefinition(int id, String name, String iconName, String instructions, Object internalDefinition,
            DeviceImplementationStatus implementationStatus, DeviceCompanyDefinition companyDefinition,
            DeviceHandlerType deviceHandlerType, DevicePortParameterType portParameterType,
            DeviceConnectionProtocol connectionProtocol, DeviceProgressStatus progressStatus, String specialComment,
            int maxRecords, MeterDisplayInterfaceType displayInterfaceType)
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
        this.maxRecords = maxRecords;
        this.displayInterfaceType = displayInterfaceType;
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


    public List<DeviceDefinition> getSupportedDevices()
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


    public static boolean isSupportedDevice(MeterDeviceDefinition meterDeviceDefinition)
    {
        return DeviceImplementationStatus.isSupportedDevice(meterDeviceDefinition.getDeviceImplementationStatus());
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


    public MeterDisplayInterfaceType getMeterDisplayInterfaceType()
    {
        return this.displayInterfaceType;
    }


    public int getMaxRecords()
    {
        return this.maxRecords;
    }

}
