package ggc.plugin.cfg;

import java.util.List;

import com.atech.data.user_data_dir.UserDataDirectory;
import com.atech.graphics.dialogs.selector.SelectableInterface;

import com.atech.graphics.dialogs.selector.SelectableInterfaceV2;
import ggc.plugin.manager.DeviceManager;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 *
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 *
 *  Filename:      DeviceConfigurationDefinitionAbstract
 *  Description:   Abstract class for DeviceConfigurationDefinition interface.
 *
 *  Author: Andy {andy@atech-software.com}
 */

public abstract class DeviceConfigurationDefinitionAbstract implements DeviceConfigurationDefinition
{

    protected UserDataDirectory userDataDirectory;
    protected DeviceManager deviceManager;
    protected boolean supportTimeFix = false;
    protected String devicePrefix;
    String configurationFilename;
    String helpPrefix;


    public DeviceConfigurationDefinitionAbstract(boolean supportTimeFix, //
                                                 DeviceManager deviceManager, //
                                                 String devicePrefix, //
                                                 String configurationFilename, //
                                                 String helpPrefix)
    {
        userDataDirectory = UserDataDirectory.getInstance();

        this.supportTimeFix = supportTimeFix;
        this.deviceManager = deviceManager;
        this.devicePrefix = devicePrefix;
        this.configurationFilename = configurationFilename;
        this.helpPrefix = helpPrefix;
    }


    /**
     * Keyword used through configuration and configuration file describing device (for meter plugin, this
     * would be word METER).
     *
     * @return keyword representing device
     */
    public String getDevicePrefix()
    {
        return this.devicePrefix;
    }

    /**
     * Name of configuration file.
     */
    public String getConfigurationFilename()
    {
        return this.configurationFilename;
    }


    /**
     * Get path to Configuration file as string
     *
     * @return path to configuration file
     */
    public String getDevicesConfigurationFile()
    {
        return userDataDirectory.getParsedUserDataPath("%USER_DATA_DIR%/tools/" + getConfigurationFilename());
    }



    /**
     * Returns prefix for help context
     *
     * @return help context prefix
     */
    public String getHelpPrefix()
    {
        return this.helpPrefix;
    }


    /**
     * Only certain devices support manual time fix for application (meters do, other's don't).
     *
     * @return true if time fix is supported, false otherwise
     */
    public boolean doesDeviceSupportTimeFix()
    {
        return false;
    }


    /**
     * Returns list of all supported devices for plugin. Needed for device selection
     *
     * @return
     */
    public List<SelectableInterfaceV2> getSupportedDevices()
    {
        checkAndResetDeviceManager();

        return this.deviceManager.getSupportedDevicesForSelector();
    }


    private void checkAndResetDeviceManager() {
        if (this.deviceManager==null)
            initDeviceManager();
    }

    
    public abstract void initDeviceManager();


    public Object getSpecificDeviceInstance(String company, String deviceName)
    {
        checkAndResetDeviceManager();

        Object device = this.deviceManager.getDeviceV2(company, deviceName);

        if (device == null)
        {
            device = this.deviceManager.getDeviceV1(company, deviceName);
        }

        return device;
    }

}
