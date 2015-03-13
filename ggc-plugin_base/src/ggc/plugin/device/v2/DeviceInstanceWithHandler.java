package ggc.plugin.device.v2;


import com.atech.graphics.dialogs.selector.ColumnSorter;
import com.atech.graphics.dialogs.selector.SelectableInterface;
import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.data.enums.DeviceCompanyDefinition;
import ggc.plugin.data.enums.DevicePortParameterType;
import ggc.plugin.data.enums.DeviceProgressStatus;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.mgr.DeviceHandlerManager;
import ggc.plugin.gui.DeviceSpecialConfigPanelInterface;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.DeviceConnectionProtocol;
import ggc.plugin.util.DataAccessPlugInBase;
import org.apache.commons.lang.StringUtils;


/**
 * Created by andy on 10.02.15.
 */
public class DeviceInstanceWithHandler implements DeviceInterfaceV2
{
    DataAccessPlugInBase dataAccessPlugInBase;
    DeviceDefinition deviceDefinitionBase;
    DeviceHandler deviceHandler;
    String deviceSourceName;

    public DeviceInstanceWithHandler(DeviceDefinition deviceDefinition, DataAccessPlugInBase dataAccessPlugInBase)
    {
        this.deviceDefinitionBase = deviceDefinition;
        this.deviceHandler = DeviceHandlerManager.getInstance().getDeviceHandler(deviceDefinition.getDeviceHandlerKey());
        this.deviceSourceName = this.getCompany().getName() + " / " + this.getName();
        this.dataAccessPlugInBase = dataAccessPlugInBase;

        //System.out.println("Def key: " + deviceDefinition.getDeviceHandlerKey());

        //DeviceHandlerManager.getInstance()



        //System.out.println("Device Handler: " + this.deviceHandler);

//        initLocal();
    }


//    public abstract void initLocal();


    public String getName()
    {
        return this.deviceDefinitionBase.getDeviceName();
    }

    public String getIconName()
    {
        return this.deviceDefinitionBase.getIconName();
    }

    public int getDeviceId()
    {
        return this.deviceDefinitionBase.getDeviceId();
    }

    public String getInstructions()
    {
        return this.deviceDefinitionBase.getInstructionsI18nKey();
    }


    public DeviceImplementationStatus getImplementationStatus()
    {
        return this.deviceDefinitionBase.getDeviceImplementationStatus();
    }



    public String getDeviceSpecialComment()
    {
        return this.deviceDefinitionBase.getSpecialComment();
    }

    public DeviceProgressStatus getDeviceProgressStatus()
    {
        return this.deviceDefinitionBase.getDeviceProgressStatus();
    }

    public DeviceConnectionProtocol getConnectionProtocol()
    {
        return this.deviceDefinitionBase.getConnectionProtocol();
    }

    public boolean validateConnectionParameters(String param)
    {
        return (!StringUtils.isBlank(param));
    }

    public DevicePortParameterType getDevicePortParameterType()
    {
        return this.deviceDefinitionBase.getDevicePortParameterType();
    }


    private void checkIfOperationIsAllowed(DownloadSupportType downloadSupportType) throws PlugInBaseException
    {
        // FIXME
    }

    public DownloadSupportType getDownloadSupportType()
    {
        return this.deviceDefinitionBase.getDeviceHandlerKey().getDownloadSupportType();
    }

    public String getDeviceSourceName()
    {
        return this.deviceSourceName;
    }


    public DeviceCompanyDefinition getCompany()
    {
        return this.deviceDefinitionBase.getDeviceCompany();
    }



    // -------------------------------------------------------
    // -----      Has Special Config  (Can be overriden if required )
    // -------------------------------------------------------


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


    // -------------------------------------------------------
    // -----             Handler Methods
    // -------------------------------------------------------

    public void readDeviceData(Object connectionParameters, OutputWriter outputWriter) throws PlugInBaseException
    {
        checkIfOperationIsAllowed(DownloadSupportType.DownloadData);
        this.deviceHandler.readDeviceData(deviceDefinitionBase, connectionParameters, outputWriter);
    }


    public void readConfiguration(Object connectionParameters, OutputWriter outputWriter) throws PlugInBaseException
    {
        checkIfOperationIsAllowed(DownloadSupportType.DownloadConfig);
        this.deviceHandler.readConfiguration(deviceDefinitionBase, connectionParameters, outputWriter);
    }

    public GGCPlugInFileReaderContext[] getFileDownloadContext(DownloadSupportType downloadSupportType)
    {
        //return this.deviceDefinitionBase.getDeviceHandlerKey().getDownloadSupportType();

        return this.deviceHandler.getFileDownloadContext(downloadSupportType);
    }


    // -------------------------------------------------------
    // -----                Has Pre Init  (Can be overriden if required )
    // -------------------------------------------------------


    public boolean hasPreInit()
    {
        return false;
    }

    public void preInitDevice()
    {
    }


    // -------------------------------------------------------
    // -----             Selectable Interface
    // -------------------------------------------------------


    public long getItemId()
    {
        return 0;
    }

    /**
     * getShortDescription
     */
    public String getShortDescription()
    {
        return this.getName();
    }

    /**
     * getColumnCount - return number of displayable columns
     *
     * @return number of displayable columns
     */
    public int getColumnCount()
    {
        return dataAccessPlugInBase.getPluginDeviceUtil().getColumnCount();
    }

    /**
     * getColumnName - return name of specified column
     *
     * @param num number of column
     * @return string displaying name of column (usually this is I18N version of string
     */
    public String getColumnName(int num)
    {
        return dataAccessPlugInBase.getPluginDeviceUtil().getColumnName(num);
    }

    /**
     * getColumnValue - return value of specified column
     *
     * @param num number of column
     * @return string value of column
     */
    public String getColumnValue(int num)
    {
        return dataAccessPlugInBase.getPluginDeviceUtil().getColumnValue(num, this);
    }

    /**
     * getColumnValueObject - return value of specified column
     *
     * @param num number of column
     * @return string value of column
     */
    public Object getColumnValueObject(int num)
    {
        return this.getColumnValue(num);
    }

    /**
     * getColumnWidth - return width of specified column
     *
     * @param num number of column
     * @param width total width of table
     * @return width in int of column
     */
    public int getColumnWidth(int num, int width)
    {
        return dataAccessPlugInBase.getPluginDeviceUtil().getColumnWidth(num, width);
    }



    public boolean isFound(String text)
    {
        //return this.deviceSourceName.contains(text);
        return true;
    }

    public boolean isFound(int value)
    {
        return true;
    }

    public boolean isFound(int from, int till, int state)
    {
        return true;
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




    // ----------
    // --- Add to DeviceDefintion
    // -----------






    // ----------
    // --- Compliance with Device Interface , should be removed when we remove old device framework
    // -----------




}
