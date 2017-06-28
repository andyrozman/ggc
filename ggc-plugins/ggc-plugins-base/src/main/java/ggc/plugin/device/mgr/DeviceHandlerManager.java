package ggc.plugin.device.mgr;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.file.ClassFinder;
import com.atech.utils.file.ClassTypeDefinition;

import ggc.core.plugins.GGCPluginType;
import ggc.plugin.data.enums.DeviceHandlerType;
import ggc.plugin.device.v2.DeviceHandler;

public class DeviceHandlerManager
{

    private static final Logger LOG = LoggerFactory.getLogger(DeviceHandlerManager.class);

    private static DeviceHandlerManager deviceHandlerManager = null;

    Map<DeviceHandlerType, DeviceHandler> deviceHandlers = new HashMap<DeviceHandlerType, DeviceHandler>();

    Map<GGCPluginType, List<DeviceHandler>> handlersDiscoveredByType = new HashMap<GGCPluginType, List<DeviceHandler>>();
    Set<Class<?>> unDynamicalClasses = new HashSet<Class<?>>();


    public static DeviceHandlerManager getInstance()
    {
        if (deviceHandlerManager == null)
        {
            deviceHandlerManager = new DeviceHandlerManager();
        }

        return deviceHandlerManager;
    }


    private DeviceHandlerManager()
    {
        this.registerUnDynamicalClasses();
    }


    public void addDeviceHandler(DeviceHandler handler)
    {
        addDeviceHandler(handler, false);
    }


    public void addDeviceHandler(DeviceHandler handler, boolean dynamicRegister)
    {
        if (!deviceHandlers.containsKey(handler.getDeviceHandlerKey()))
        {
            this.deviceHandlers.put(handler.getDeviceHandlerKey(), handler);
            LOG.debug("Device Handler registered [type={}, name={}, dynamic={}]", handler.getGGCPluginType(),
                handler.getDeviceHandlerKey(), dynamicRegister);
        }
    }


    public DeviceHandler getDeviceHandler(DeviceHandlerType deviceHandlerType)
    {
        if ((deviceHandlerType == DeviceHandlerType.NoHandler) || //
                (deviceHandlerType == DeviceHandlerType.NullHandler) || //
                (deviceHandlerType == DeviceHandlerType.NoSupportInDevice))
            return null;

        if (this.deviceHandlers.containsKey(deviceHandlerType))
        {
            return this.deviceHandlers.get(deviceHandlerType);
        }
        else
        {
            LOG.error("Handler " + deviceHandlerType.name() + " could not be found.");
            return null;
        }
    }


    public void registerDeviceHandlersDynamically(GGCPluginType pluginType)
    {
        if (this.handlersDiscoveredByType.size() == 0)
        {
            registerDeviceHandlersDynamically();
        }

        if (handlersDiscoveredByType.containsKey(pluginType))
        {
            List<DeviceHandler> deviceHandlers = handlersDiscoveredByType.get(pluginType);

            for (DeviceHandler handler : deviceHandlers)
            {
                addDeviceHandler(handler, true);
            }
        }
    }


    public void registerDeviceHandlersDynamically()
    {
        try
        {
            ClassFinder finder = new ClassFinder();

            Vector<Class<?>> subclasses = finder.findSubclasses(DeviceHandler.class.getName(),
                ClassTypeDefinition.EndClass);

            for (Class c : subclasses)
            {
                try
                {
                    if (unDynamicalClasses.contains(c))
                        continue;

                    DeviceHandler handler = (DeviceHandler) c.newInstance();

                    if (handler.isEnabled())
                    {
                        if (handlersDiscoveredByType.containsKey(handler.getGGCPluginType()))
                        {
                            handlersDiscoveredByType.get(handler.getGGCPluginType()).add(handler);
                        }
                        else
                        {
                            List<DeviceHandler> listHandlers = new ArrayList<DeviceHandler>();
                            listHandlers.add(handler);

                            handlersDiscoveredByType.put(handler.getGGCPluginType(), listHandlers);
                        }
                    }
                }
                catch (Exception ex)
                {
                    LOG.warn("Problem instantiating DeviceHandler [{}]. Exception: {}", c.getSimpleName(),
                        ex.getMessage());
                }

            }

            for (GGCPluginType pluginType : Arrays.asList(GGCPluginType.MeterToolPlugin, GGCPluginType.PumpToolPlugin,
                GGCPluginType.CGMSToolPlugin))
            {
                checkAndLogPluginHandlers(pluginType);
            }

        }
        catch (Exception ex)
        {
            LOG.warn("Error loading dynamic device handlers. " + ex, ex);
        }

    }


    private void checkAndLogPluginHandlers(GGCPluginType pluginType)
    {
        if (handlersDiscoveredByType.containsKey(pluginType))
        {
            LOG.debug("{} - {} Handlers found.", pluginType.name(), handlersDiscoveredByType.get(pluginType).size());
        }
        else
        {
            LOG.debug("{} - No Handlers found", pluginType.name());
        }
    }


    public void registerUnDynamicalClasses()
    {
        String classes[] = { "ggc.pump.device.minimed.MinimedPumpDeviceHandler", //
                             "ggc.cgms.device.minimed.MinimedCGMSDeviceHandler" };

        for (String clazzName : classes)
        {
            Class<?> clazz = getClass(clazzName);

            if (clazz != null)
                this.unDynamicalClasses.add(clazz);
        }

    }


    public Class<?> getClass(String className)
    {
        Class<?> clazz = null;
        try
        {
            clazz = Class.forName(className);
        }
        catch (ClassNotFoundException e)
        {
            LOG.warn("Class {} not found.", className);
            // e.printStackTrace();
        }

        return clazz;
    }

}
