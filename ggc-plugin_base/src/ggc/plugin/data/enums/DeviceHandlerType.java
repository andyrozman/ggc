package ggc.plugin.data.enums;

/**
 * Created by andy on 10.02.15.
 */
public enum DeviceHandlerType
{
    NoHandler(false, false, false, false),

    // Meters
    AscensiaUsbHandler(true, false, false, false),

    // Pumps
    AnimasV2CGMSHandler(true, true, false, false),
    AnimasV2PumpHandler(true, true, false, false),

    // CGMSes

    ;

    boolean downloadData;
    boolean downloadConfiguration;
    boolean importDataFile;
    boolean importConfigFile;


    DeviceHandlerType(boolean downloadData, boolean downloadConfiguration, boolean importDataFile, boolean importConfigFile)
    {
        this.downloadData = downloadData;
        this.downloadConfiguration = downloadConfiguration;
        this.importDataFile = importDataFile;
        this.importConfigFile = importConfigFile;
    }



    public boolean canDownloadData()
    {
        return this.downloadData;
    }

    public boolean canDownloadConfiguration()
    {
        return this.downloadConfiguration;
    }

    public boolean canImportDataFile()
    {
        return this.importDataFile;
    }

    public boolean canImportConfigFile()
    {
        return this.importConfigFile;
    }

}
