package ggc.connect.data.config;

/**
 * Created by andy on 3/30/18.
 */
public class ConnectConfigManager  {

    private static ConnectConfigManager staticConnectConfigManager;

    private ConnectConfigManager()
    {

    }





    static ConnectConfigManager getInstance()
    {
        return staticConnectConfigManager;
    }

}