package ggc.connect.defs;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileFilter;

import com.atech.i18n.I18nControlAbstract;

import ggc.connect.data.ConnectHandlerParameters;
import ggc.connect.data.plugin.PluginHandlerConfigEntry;
import ggc.connect.enums.ConnectDataType;
import ggc.connect.enums.ConnectHandlerConfiguration;
import ggc.connect.gui.config.DefaultConnectConfigurationPanel;
import ggc.connect.util.DataAccessConnect;
import ggc.core.plugins.GGCPluginType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.device.v2.DeviceHandlerAbstract;
import ggc.plugin.output.OutputWriter;

public abstract class ConnectHandler extends DeviceHandlerAbstract
{

    protected DataAccessConnect dataAccessConnect;
    protected I18nControlAbstract i18nControl;
    protected Map<String, PluginHandlerConfigEntry> pluginConfigEntries;


    public ConnectHandler()
    {
        dataAccessConnect = DataAccessConnect.getInstance();
        // System.out.println("Connect Handler: " + dataAccessConnect);
        // this.i18nControl = dataAccessConnect.getI18nControlInstance();
    }


    /**
     * Check if DataAccess Set, if not set it.
     */
    public void checkIfDataAccessSet()
    {
        if (dataAccessConnect == null)
        {
            dataAccessConnect = DataAccessConnect.getInstance();
            this.i18nControl = dataAccessConnect.getI18nControlInstance();
        }
    }


    public void readDeviceData(DeviceDefinition definition, Object connectionParameters, OutputWriter outputWriter)
            throws PlugInBaseException
    {
    }


    public void readConfiguration(DeviceDefinition definition, Object connectionParameters, OutputWriter outputWriter)
            throws PlugInBaseException
    {
    }


    public void closeDevice() throws PlugInBaseException
    {
    }


    public GGCPluginType getGGCPluginType()
    {
        return GGCPluginType.ConnectToolPlugin;
    }


    public abstract FileFilter[] getFileFilters(ConnectHandlerConfiguration config);


    protected FileFilter createFileFilter(final String extension, final String descriptionKey)
    {
        return new FileFilter()
        {

            @Override
            public boolean accept(File f)
            {
                if (f.isDirectory())
                    return true;

                return f.getName().toLowerCase().endsWith("." + extension);
            }


            @Override
            public String getDescription()
            {
                return i18nControl.getMessage(descriptionKey) + " (*." + extension + ")";
            }

        };
    }


    public abstract Map<ConnectDataType, List<String>> downloadSummary(ConnectHandlerParameters parameters)
            throws PlugInBaseException;


    public abstract void downloadData(ConnectHandlerParameters parameters, OutputWriter outputWriter)
            throws PlugInBaseException;


    public abstract boolean hasSimpleConfiguration();


    public DefaultConnectConfigurationPanel getConnectConfiguratonPanel()
    {
        return null;
    }


    public Map<String, PluginHandlerConfigEntry> getConfigurationEntries()
    {
        return null;
    }

}
