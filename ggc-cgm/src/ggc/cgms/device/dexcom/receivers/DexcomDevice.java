package ggc.cgms.device.dexcom.receivers;

public enum DexcomDevice {
    Dexcom_G4(ReceiverApiType.G4_Api),

    Dexcom_7_Receiver(ReceiverApiType.R2_Api),
    Dexcom_7_Plus(ReceiverApiType.R2_Api),

    ;

    private ReceiverApiType api;


    private DexcomDevice(ReceiverApiType api) {
        this.api = api;
    }


    public ReceiverApiType getApi() {
        return api;
    }


    public void setApi(ReceiverApiType api) {
        this.api = api;
    }

}
