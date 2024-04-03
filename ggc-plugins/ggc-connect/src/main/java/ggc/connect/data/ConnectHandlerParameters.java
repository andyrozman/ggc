package ggc.connect.data;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ggc.connect.enums.ConnectConfigType;
import ggc.connect.enums.ConnectHandlerConfiguration;
import ggc.connect.enums.ConnectOperationType;
import ggc.plugin.data.enums.DeviceHandlerType;

public class ConnectHandlerParameters
{

    private static final long serialVersionUID = 8531600959335491806L;

    private DeviceHandlerType deviceHandlerType;
    private ConnectHandlerConfiguration handlerConfiguration;

    private ConnectOperationType actionType;
    private Set<String> selectedItems;
    // private List<String> selectedItems;

    Map<String, Object> customParameters = new HashMap<String, Object>();


    public void setFileName(String filename)
    {
        this.customParameters.put("fileName", filename);
    }


    public String getFileName()
    {
        return (String) this.customParameters.get("fileName");
    }


    public void setActionType(ConnectOperationType actionType)
    {
        this.actionType = actionType;
    }


    public ConnectOperationType getActionType()
    {
        return this.actionType;
    }


    public void setSelectedItems(Set<String> selectedItems)
    {
        this.selectedItems = selectedItems;
    }


    public Set<String> getSelectedItems()
    {
        return this.selectedItems;
    }


    public void setDeviceHandlerType(DeviceHandlerType deviceHandlerType)
    {
        this.deviceHandlerType = deviceHandlerType;
    }


    public DeviceHandlerType getDeviceHandlerType()
    {
        return deviceHandlerType;
    }


    public void setHandlerConfiguration(ConnectHandlerConfiguration handlerConfiguration)
    {
        this.handlerConfiguration = handlerConfiguration;
    }


    public ConnectHandlerConfiguration getHandlerConfiguration()
    {
        return handlerConfiguration;
    }


    public String getHandlerTarget()
    {
        if (this.handlerConfiguration.getConfigType() == ConnectConfigType.File)
        {
            String filename = getFileName();
            File f = new File(filename);

            return f.getPath();
        }

        return null;
    }
}
