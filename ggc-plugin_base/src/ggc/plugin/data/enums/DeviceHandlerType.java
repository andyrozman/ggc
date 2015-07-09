package ggc.plugin.data.enums;

import ggc.plugin.device.DownloadSupportType;

/**
 * Created by andy on 10.02.15.
 */
public enum DeviceHandlerType
{
    NoHandler(DownloadSupportType.NoDownloadSupport), //

    // Meters
    AscensiaUsbHandler(DownloadSupportType.DownloadData), //

    // Pumps
    AnimasV2PumpHandler(DownloadSupportType.Download_Data_Config), //
    MinimedPumpHandler(DownloadSupportType.Download_Data_Config), //
    InsuletOmnipodHandler(DownloadSupportType.Download_File_Data_Config), //

    // CGMSes
    AnimasV2CGMSHandler(DownloadSupportType.Download_Data_Config), //
    DexcomHandler(DownloadSupportType.Download_Data_Config), //
    MinimedCGMSHandler(DownloadSupportType.Download_Data_Config), //

    ;

    DownloadSupportType downloadSupportType;


    DeviceHandlerType(DownloadSupportType downloadSupportType)
    {
        this.downloadSupportType = downloadSupportType;
    }


    public DownloadSupportType getDownloadSupportType()
    {
        return downloadSupportType;
    }

}
