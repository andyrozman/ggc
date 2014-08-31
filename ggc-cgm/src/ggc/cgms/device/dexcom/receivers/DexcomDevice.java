package ggc.cgms.device.dexcom.receivers;

public enum DexcomDevice {
    Dexcom_G4(ReceiverApiType.G4_Api, "Dexcom G4"),

    Dexcom_7_Receiver(ReceiverApiType.R2_Api, "Dexcom 7"),
    Dexcom_7_Plus(ReceiverApiType.R2_Api, "Dexcom 7 Plus"),

    ;

    private ReceiverApiType api;
    private String description;


    private DexcomDevice(ReceiverApiType api, String desc) {
        this.api = api;
        this.description = desc;
    }


    public ReceiverApiType getApi() {
        return api;
    }


    public void setApi(ReceiverApiType api) {
        this.api = api;
    }


    public String getDescription()
    {
        return description;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }

}
