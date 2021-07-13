package ggc.connect.data;

import com.atech.utils.file.PropertiesFile;

import ggc.connect.defs.ConnectTargetDefinition;

/**
 * Created by andy on 3/11/18.
 */
public class ConnectConfigurationManager
{
    static ConnectConfigurationManager connectConfigurationManager = new ConnectConfigurationManager();

    PropertiesFile pluginConfiguration;
    PropertiesFile pluginData;

    // Module_ENABLED
    // Module_CONFIGURATION
    // Module_DATA

    private ConnectConfigurationManager()
    {
        loadConfiguration();
    }

    public static ConnectConfigurationManager getInstance()
    {
        return connectConfigurationManager;
    }

    public void loadConfiguration()
    {
        pluginConfiguration = new PropertiesFile("../data/tools/ConnectConfiguration.properties", false);

        rebuildConfiguration();
    }

    private void rebuildConfiguration() {

        String[] configKeys = { "_ENABLED", "_CONFIGURATION", "_DATA" };

        for (ConnectTargetDefinition connectTargetDefinition : ConnectTargetDefinition.values())
        {

            String key = connectTargetDefinition.name();

            for (String configKey : configKeys) {

                if (!pluginConfiguration.containsKey(key + configKey))
                {
                    if (configKey.equals("_ENABLED")) {
                        pluginConfiguration.put(key + configKey, "false");
                    }
                    else
                    {
                        pluginConfiguration.put(key + configKey, "{}");
                    }
                }
            }
        }
    }

    public void saveConfiguration()
    {

    }


    // MODULES_ENABLED



} 