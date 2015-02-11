package ggc.plugin.device.v2;

import com.atech.graphics.dialogs.selector.ColumnSorter;
import com.atech.graphics.dialogs.selector.SelectableInterface;
import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.data.enums.DeviceCompanyDefinition;
import ggc.plugin.device.DeviceDefinition;
import ggc.plugin.device.DeviceInterface;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.mgr.DeviceHandlerManager;
import ggc.plugin.gui.DeviceSpecialConfigPanelInterface;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.manager.company.AbstractDeviceCompany;

/**
 * Created by andy on 10.02.15.
 */
public class DeviceInstanceWithHandler implements DeviceInterface
{
    DeviceDefinition deviceDefinitionBase;
    DeviceHandler deviceHandler;

    public DeviceInstanceWithHandler(DeviceDefinition deviceDefinition)
    {
        this.deviceDefinitionBase = deviceDefinition;

        this.deviceHandler = DeviceHandlerManager.getInstance().getDeviceHandler(deviceDefinition.getDeviceHandler());

//        initLocal();
    }


//    public abstract void initLocal();


    public boolean canDownloadData()
    {
        return this.deviceDefinitionBase.getDeviceHandler().canDownloadData();
    }


    public boolean canDownloadConfiguration()
    {
        return this.deviceDefinitionBase.getDeviceHandler().canDownloadConfiguration();
    }

    public boolean canImportDataFile()
    {
        return this.deviceDefinitionBase.getDeviceHandler().canImportDataFile();
    }


    public boolean canImportConfigFile()
    {
        return this.deviceDefinitionBase.getDeviceHandler().canImportConfigFile();
    }


    public String getName()
    {
        return null;
    }

    public String getIconName()
    {
        return null;
    }

    public int getDeviceId()
    {
        return 0;
    }

    public String getInstructions()
    {
        return null;
    }


    public DeviceImplementationStatus getImplementationStatus()
    {
        return null;
    }

    public String getDeviceClassName()
    {
        return null;
    }


    public void downloadData() throws PlugInBaseException
    {
        this.deviceHandler.readDeviceData(deviceDefinitionBase, null, null);
    }


    public void downloadConfiguration() throws PlugInBaseException
    {

    }


    public DeviceCompanyDefinition getCompany()
    {
        return this.deviceDefinitionBase.getDeviceCompany();
    }


    // ----------
    // --- Add to DeviceDefintion
    // -----------



    public String getDeviceSpecialComment()
    {
        return null;
    }

    public boolean hasSpecialProgressStatus()
    {
        return false;
    }

    public boolean hasIndeterminateProgressStatus()
    {
        return false;
    }

    public boolean hasPreInit()
    {
        return false;
    }

    public void preInitDevice()
    {

    }

    // ----------
    // --- Compliance with Device Interface , should be removed when we remove old device framework
    // -----------


    public void dispose()
    {
    }


    public String getComment()
    {
        return null;
    }


    public void readDeviceDataFull() throws PlugInBaseException
    {
        this.downloadData();
    }


    public void readConfiguration() throws PlugInBaseException
    {
        this.downloadConfiguration();
    }



    public boolean isDeviceCommunicating()
    {
        return false;
    }

    public boolean canReadData()
    {
        return this.canDownloadData();
    }


    public boolean canReadConfiguration()
    {
        return this.canDownloadConfiguration();
    }



    public int getConnectionProtocol()
    {
        return 0;
    }

    public String getConnectionPort()
    {
        return null;
    }

    public String getConnectionParameters()
    {
        return null;
    }

    public void setConnectionParameters(String param)
    {

    }

    public boolean areConnectionParametersValid()
    {
        return false;
    }

    public boolean areConnectionParametersValid(String param)
    {
        return false;
    }

    public boolean hasNoConnectionParameters()
    {
        return false;
    }

    public void setDeviceCompany(AbstractDeviceCompany company)
    {
    }

    public AbstractDeviceCompany getDeviceCompany()
    {
        return null;
    }

    public boolean isReadableDevice()
    {
        return false;
    }

    public int getDownloadSupportType()
    {
        return 0;
    }

    public String getDeviceSourceName()
    {
        return null;
    }

    public boolean isFileDownloadSupported()
    {
        return false;
    }

    public GGCPlugInFileReaderContext[] getFileDownloadTypes()
    {
        return new GGCPlugInFileReaderContext[0];
    }

    public boolean hasSpecialConfig()
    {
        return false;
    }

    public DeviceSpecialConfigPanelInterface getSpecialConfigPanel()
    {
        return null;
    }

    public void initSpecialConfig()
    {

    }

    public boolean hasDefaultParameter()
    {
        return false;
    }



    public long getItemId()
    {
        return 0;
    }

    public String getShortDescription()
    {
        return null;
    }

    public int getColumnCount()
    {
        return 0;
    }

    public String getColumnName(int num)
    {
        return null;
    }

    public String getColumnValue(int num)
    {
        return null;
    }

    public Object getColumnValueObject(int num)
    {
        return null;
    }

    public int getColumnWidth(int num, int width)
    {
        return 0;
    }

    public boolean isFound(String text)
    {
        return false;
    }

    public boolean isFound(int value)
    {
        return false;
    }

    public boolean isFound(int from, int till, int state)
    {
        return false;
    }

    public void setSearchContext()
    {

    }

    public void setColumnSorter(ColumnSorter cs)
    {

    }

    public int compareTo(SelectableInterface o)
    {



        return 0;
    }
}
