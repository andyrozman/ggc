package ggc.plugin.device.mgr;

import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.file.ClassFinder;
import com.atech.utils.file.ClassTypeDefinition;

import ggc.core.plugins.GGCPluginType;
import ggc.plugin.data.enums.DeviceHandlerType;
import ggc.plugin.defs.DevicePluginDefinitionAbstract;
import ggc.plugin.device.v2.DeviceHandler;

public class DeviceHandlerManager
{

    private static final Logger LOG = LoggerFactory.getLogger(DeviceHandlerManager.class);

    private static DeviceHandlerManager deviceHandlerManager = null;

    static Map<DeviceHandlerType, DeviceHandler> deviceHandlers = new HashMap<DeviceHandlerType, DeviceHandler>();

    static Map<GGCPluginType, List<DeviceHandler>> handlersDiscoveredByType = new HashMap<GGCPluginType, List<DeviceHandler>>();
    static Set<Class<?>> unDynamicalClasses = new HashSet<Class<?>>();
    boolean dynamicFailed = false;

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
            deviceHandlers.put(handler.getDeviceHandlerKey(), handler);
            LOG.debug("Device Handler registered [type={}, name={}, dynamic={}]", handler.getGGCPluginType(),
                handler.getDeviceHandlerKey(), dynamicRegister);
        }
    }


    public DeviceHandler getDeviceHandler(DeviceHandlerType deviceHandlerType)
    {
        if (deviceHandlerType.getPluginType()==null)
            return null;

        if (deviceHandlers.containsKey(deviceHandlerType))
        {
            return deviceHandlers.get(deviceHandlerType);
        }
        else
        {
            LOG.error("Handler " + deviceHandlerType.name() + " could not be found.");
            return null;
        }
    }


    public void registerDeviceHandlersDynamically(GGCPluginType pluginType)
    {

        for(DeviceHandlerType handlerType : DeviceHandlerType.values())
        {
            if (pluginType==handlerType.getPluginType())
            {
                if (StringUtils.isNotBlank(handlerType.getClassName()) && handlerType.isDynamicallyLoad()) {

                    Class clazz = null;

                    try {
                        clazz = Class.forName(handlerType.getClassName());

                        DeviceHandler handler = (DeviceHandler) clazz.newInstance();
                        addDeviceHandler(handler, true);
                    }
                    catch (Exception ex)
                    {
                        LOG.warn("Problem instantiating DeviceHandler [{}]. Exception: {}", handlerType.getClassName(),
                                ex.getMessage(), ex);
                    }
                }
            }
        }

    }


    @Deprecated
    public void registerDeviceHandlersDynamicallyOld(GGCPluginType pluginType, DevicePluginDefinitionAbstract pluginInstance)
    {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! Register Devices Handler Dynamicaly: " + pluginType.name());

        if (handlersDiscoveredByType.size() == 0 && !dynamicFailed)
        {
            registerDeviceHandlersDynamicallyOld();
        }

        if (dynamicFailed)
        {
            //pluginInstance.getHandlerClasses();
        }
        else {

            if (handlersDiscoveredByType.containsKey(pluginType)) {
                List<DeviceHandler> deviceHandlers = handlersDiscoveredByType.get(pluginType);
                // System.out.println("Device Handlers: " + deviceHandlers);

                for (DeviceHandler handler : deviceHandlers) {
                    addDeviceHandler(handler, true);
                }
            }
        }
    }

    @Deprecated
    public void registerDeviceHandlersDynamically()
    {
        try
        {
            ClassFinder finder = new ClassFinder("ggc-plugins");

            Vector<Class<?>> subclasses = finder.findSubclasses(DeviceHandler.class.getName(),
                    ClassTypeDefinition.EndClass);

            System.out.println("Subclasses found: " + subclasses.size() + ", List: " + subclasses);

            if (subclasses.size()==0)
            {
                System.out.println("Errors: " + finder.getErrors());
                dynamicFailed = true;
            }

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
                            ex.getMessage(), ex);
                }

            }

            for (GGCPluginType pluginType : Arrays.asList(GGCPluginType.MeterToolPlugin, GGCPluginType.PumpToolPlugin,
                    GGCPluginType.CGMSToolPlugin, GGCPluginType.ConnectToolPlugin))
            {
                checkAndLogPluginHandlers(pluginType);
            }

        }
        catch (Exception ex)
        {
            LOG.warn("Error loading dynamic device handlers. " + ex, ex);
        }

    }


    public void showInstantiatedHandlers()
    {
        for (GGCPluginType pluginType : Arrays.asList(GGCPluginType.MeterToolPlugin, GGCPluginType.PumpToolPlugin,
                GGCPluginType.CGMSToolPlugin, GGCPluginType.ConnectToolPlugin))
        {
            checkAndLogPluginHandlers(pluginType);
        }
    }


    public void registerDeviceHandlersDynamicallyOld()
    {
        try
        {
            ClassFinder finder = new ClassFinder("ggc-plugins");

            Vector<Class<?>> subclasses = finder.findSubclasses(DeviceHandler.class.getName(),
                ClassTypeDefinition.EndClass);

            System.out.println("Subclasses found: " + subclasses.size() + ", List: " + subclasses);

            if (subclasses.size()==0)
            {
                System.out.println("Errors: " + finder.getErrors());
                dynamicFailed = true;
            }

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
                        ex.getMessage(), ex);
                }

            }

            for (GGCPluginType pluginType : Arrays.asList(GGCPluginType.MeterToolPlugin, GGCPluginType.PumpToolPlugin,
                GGCPluginType.CGMSToolPlugin, GGCPluginType.ConnectToolPlugin))
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
                unDynamicalClasses.add(clazz);
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
