package ggc.plugin.device;

import com.atech.i18n.I18nControlAbstract;

import ggc.plugin.device.v2.DeviceDefinition;

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
 *  Filename:     DeviceIdentification  
 *  Description:  Class for display of Device Identification.
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class DeviceIdentification
{

    private I18nControlAbstract m_ic = null;

    private DeviceDefinition deviceDefinition;

    /**
     * Company
     */
    public String company = null;

    /**
     * Device Selected 
     */
    public String device_selected = null;

    /**
     * Device Identified
     */
    public String device_identified = null;

    /**
     * Device Family 
     */
    public String device_family = null;

    /**
     * Device Software Version
     */
    public String deviceSoftwareVersion = null;

    /**
     * Device Hardware Version 
     */
    public String deviceHardwareVersion = null;
    /**
     * 
     */
    public String deviceSerialNumber = null;

    /**
     * Is File Import
     */
    public boolean is_file_import = false;

    /**
     * File Import:  File Name
     */
    public String fi_file_name = "";

    /**
     * File Import:  Additional Info
     */
    public String fi_additional_info = "";

    // DataAccessPlugInBase da


    // I18nControlAbstract i18nControl;

    /**
     * Constructor 
     * 
     * @param ic
     */
    public DeviceIdentification(I18nControlAbstract ic)
    {
        this.m_ic = ic;
    }


    /**
     * Constructor 
     * 
     * @param i18nControlAbstract
     */
    /*
     * public DeviceIdentification()
     * {
     * }
     */

    /**
     * Get Information
     * 
     * @param prefix
     * @return
     */
    public String getInformation(String prefix)
    {
        StringBuffer sb = new StringBuffer();
        // I18nControlAbstract i18nControl =
        // DataAccess.getInstance().getI18nControlInstance();

        sb.append(prefix);
        sb.append(" " + m_ic.getMessage("DEVICE_NAME_BIG") + " Company: " + this.company);
        sb.append("  Selected Device: " + this.device_selected);
        sb.append("\n");

        sb.append(prefix + " ");
        appendParameter(this.device_identified, "Device identified", sb);
        appendParameter(this.device_family, "Device family", sb);
        appendParameter(this.deviceSerialNumber, "S/N", sb);
        sb.append("\n");

        if (this.deviceHardwareVersion != null || this.deviceSoftwareVersion != null)
        {
            sb.append(prefix + " ");

            if (this.deviceSoftwareVersion != null)
            {
                appendParameter(this.deviceSoftwareVersion, "Sw version", sb);
            }

            if (this.deviceHardwareVersion != null)
            {
                appendParameter(this.deviceHardwareVersion, "Hw version", sb);
            }
            sb.append("\n");
        }
        return sb.toString();
    }


    /**
     * Get Short Information
     * 
     * @return
     */
    public String getShortInformation()
    {
        if (this.is_file_import)
            return this.getShortInformationFile();

        StringBuffer sb = new StringBuffer();
        // I18nControlAbstract i18nControl =
        // DataAccess.getInstance().getI18nControlInstance();

        sb.append(this.company);
        sb.append(" - " + this.device_selected);
        sb.append("\n");

        // sb.append(prefix + " ");

        if (this.device_identified != null)
        {
            sb.append(this.device_identified + " ");
        }

        appendParameter(this.deviceSerialNumber, "S/N", sb);
        sb.append("\n");

        if (this.deviceHardwareVersion != null || this.deviceSoftwareVersion != null)
        {
            // sb.append(prefix + " ");

            if (this.deviceSoftwareVersion != null)
            {
                appendParameter(this.deviceSoftwareVersion, m_ic.getMessage("SW_VERSION"), sb);
            }

            if (this.deviceHardwareVersion != null)
            {
                appendParameter(this.deviceHardwareVersion, m_ic.getMessage("HW_VERSION"), sb);
            }

            sb.append("\n");
        }
        return sb.toString();
    }


    private String getShortInformationFile()
    {
        StringBuffer sb = new StringBuffer();
        // I18nControlAbstract i18nControl =
        // DataAccess.getInstance().getI18nControlInstance();

        sb.append(this.company);
        sb.append(" - " + this.device_selected);
        sb.append("\n");

        appendParameter(this.fi_file_name, m_ic.getMessage("IMPORT_FILE"), sb);
        sb.append("\n");

        if (this.fi_additional_info != null)
        {
            sb.append(this.fi_additional_info);
        }

        return sb.toString();
    }


    private void appendParameter(String param, String text, StringBuffer sb)
    {
        if (param != null)
        {
            sb.append(text + ": " + param + "  ");
        }

    }


    public void setDeviceDefinition(DeviceDefinition deviceDefinition)
    {
        this.deviceDefinition = deviceDefinition;

        this.company = deviceDefinition.getDeviceCompany().getName();
        this.device_selected = deviceDefinition.getDeviceName();
    }

}
