package ggc.plugin.device.v2;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.graphics.dialogs.selector.ColumnSorter;
import com.atech.graphics.dialogs.selector.SelectableInterface;
import com.atech.graphics.dialogs.selector.SelectableInterfaceV2;

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

/**
 * Created by andy on 10.02.15.
 */
public class DeviceInstanceWithHandler implements DeviceInterfaceV2
{

    private static final Logger LOG = LoggerFactory.getLogger(DeviceInstanceWithHandler.class);

    protected DataAccessPlugInBase dataAccessPlugInBase;
    protected DeviceDefinition deviceDefinitionBase;
    protected DeviceHandler deviceHandler;
    protected String deviceSourceName;


    public DeviceInstanceWithHandler(DeviceDefinition deviceDefinition, DataAccessPlugInBase dataAccessPlugInBase)
    {
        this.deviceDefinitionBase = deviceDefinition;
        this.deviceHandler = DeviceHandlerManager.getInstance()
                .getDeviceHandler(deviceDefinition.getDeviceHandlerKey());
        this.deviceSourceName = this.getCompany().getName() + " / " + this.getName();
        this.dataAccessPlugInBase = dataAccessPlugInBase;

        if (this.deviceHandler != null)
        {
            this.deviceHandler.registerSpecialConfig();
        }

        // System.out.println("Def key: " +
        // deviceDefinition.getDeviceHandlerKey());

        // DeviceHandlerManager.getInstance()

        // System.out.println("Device Handler: " + this.deviceHandler);

        // initLocal();
    }


    // public abstract void initLocal();

    public String getName()
    {
        return this.getDeviceDefinitionBase().getDeviceName();
    }


    public String getIconName()
    {
        return this.getDeviceDefinitionBase().getIconName();
    }


    public int getDeviceId()
    {
        return this.getDeviceDefinitionBase().getDeviceId();
    }


    public String getInstructions()
    {
        return this.getDeviceDefinitionBase().getInstructionsI18nKey();
    }


    public DeviceImplementationStatus getImplementationStatus()
    {
        return this.getDeviceDefinitionBase().getDeviceImplementationStatus();
    }


    public String getDeviceSpecialComment()
    {
        return this.getDeviceDefinitionBase().getSpecialComment();
    }


    public DeviceProgressStatus getDeviceProgressStatus()
    {
        return this.getDeviceDefinitionBase().getDeviceProgressStatus();
    }


    public DeviceConnectionProtocol getConnectionProtocol()
    {
        return this.getDeviceDefinitionBase().getConnectionProtocol();
    }


    public boolean validateConnectionParameters(String param)
    {
        return (!StringUtils.isBlank(param));
    }


    public DevicePortParameterType getDevicePortParameterType()
    {
        return this.getDeviceDefinitionBase().getDevicePortParameterType();
    }


    private void checkIfOperationIsAllowed(DownloadSupportType downloadSupportType) throws PlugInBaseException
    {
        // FIXME
    }


    public DownloadSupportType getDownloadSupportType()
    {
        return this.getDeviceDefinitionBase().getDeviceHandlerKey().getDownloadSupportType();
    }


    public String getDeviceSourceName()
    {
        return this.deviceSourceName;
    }


    public DeviceCompanyDefinition getCompany()
    {
        return this.getDeviceDefinitionBase().getDeviceCompany();
    }


    // -------------------------------------------------------
    // ----- Has Special Config (Can be overriden if required )
    // -------------------------------------------------------

    public boolean hasSpecialConfig()
    {
        return StringUtils.isNotBlank(this.deviceHandler.getSpecialConfigKey());
    }


    public DeviceSpecialConfigPanelInterface getSpecialConfigPanel()
    {
        String specialKey = this.deviceHandler.getSpecialConfigKey();

        if (!hasSpecialConfig())
        {
            return null;
        }
        else
        {
            return this.deviceHandler.getSpecialConfigPanel(this);

            //
            // if
            // (DataAccessPlugInBase.specialConfigPanels.containsKey(specialKey))
            // {
            // return DataAccessPlugInBase.specialConfigPanels.get(specialKey);
            // }
            // else
            // {
            // LOG.warn("There is special key ({}) defined, but there is not
            // panel registered.", specialKey);
            // return null;
            // }
        }
    }


    public void initSpecialConfig()
    {

    }


    public boolean hasDefaultParameter()
    {
        return (this.deviceDefinitionBase.getDevicePortParameterType() != DevicePortParameterType.NoParameters)
                && (this.deviceDefinitionBase
                        .getDevicePortParameterType() != DevicePortParameterType.PackedParametersWithoutDefaultParameter);
    }


    // -------------------------------------------------------
    // ----- Handler Methods
    // -------------------------------------------------------

    public void readDeviceData(Object connectionParameters, OutputWriter outputWriter) throws PlugInBaseException
    {
        checkIfOperationIsAllowed(DownloadSupportType.DownloadData);

        try
        {
            this.deviceHandler.readDeviceData(getDeviceDefinitionBase(), connectionParameters, outputWriter);
        }
        catch (PlugInBaseException e)
        {
            throw e;
        }
        finally
        {
            this.closeDevice();
        }
    }


    public void readConfiguration(Object connectionParameters, OutputWriter outputWriter) throws PlugInBaseException
    {
        checkIfOperationIsAllowed(DownloadSupportType.DownloadConfig);
        try
        {
            this.deviceHandler.readConfiguration(getDeviceDefinitionBase(), connectionParameters, outputWriter);
        }
        catch (PlugInBaseException e)
        {
            throw e;
        }
        finally
        {
            this.closeDevice();
        }
    }


    public void closeDevice()
    {
        try
        {
            this.deviceHandler.closeDevice();
        }
        catch (PlugInBaseException ex)
        {
            LOG.warn("Error closing device: {}", ex.getMessage(), ex);
        }

    }


    public List<GGCPlugInFileReaderContext> getFileDownloadContexts(DownloadSupportType downloadSupportType)
    {
        System.out.println("deviceHandler: " + this.deviceHandler);
        return this.deviceHandler.getFileDownloadContexts(downloadSupportType);
    }


    // -------------------------------------------------------
    // ----- Has Pre Init (Can be overriden if required )
    // -------------------------------------------------------

    public boolean hasPreInit()
    {
        return false;
    }


    public void preInitDevice()
    {
    }


    // -------------------------------------------------------
    // ----- Selectable Interface
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


    public String getToolTipValue(int index)
    {
        return dataAccessPlugInBase.getPluginDeviceUtil().getTooltipValue(index, this);
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
        // return this.deviceSourceName.contains(text);
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
        return this.dataAccessPlugInBase.getPluginDeviceUtil().compareTo(this, o);
    }


    public int compareTo(SelectableInterfaceV2 o)
    {
        return this.dataAccessPlugInBase.getPluginDeviceUtil().compareTo(this, o);
    }


    public DeviceDefinition getDeviceDefinitionBase()
    {
        return deviceDefinitionBase;
    }

    // ----------
    // --- Add to DeviceDefintion
    // -----------

    // ----------
    // --- Compliance with Device Interface , should be removed when we remove
    // old device framework
    // -----------

}
