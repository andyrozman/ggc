package ggc.plugin.util;

import com.atech.graphics.dialogs.selector.SelectableInterface;
import com.atech.i18n.I18nControlAbstract;

import ggc.plugin.device.DeviceAbstract;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.DeviceInterface;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.v2.DeviceInstanceWithHandler;
import ggc.plugin.device.v2.DeviceInterfaceV2;
import ggc.plugin.output.OutputWriter;

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
 *  Filename:     PlugInDeviceUtil
 *  Description:  
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class PlugInDeviceUtil
{

    DataAccessPlugInBase m_da = null;
    I18nControlAbstract m_ic = null;
    float device_columns_width[] = { 0.25f, 0.3f, 0.22f, 0.23f };
    String device_columns[] = null;
    I18nControlAbstract m_ic_core = null;


    /**
     * Constructor
     * 
     * @param da
     */
    public PlugInDeviceUtil(DataAccessPlugInBase da)
    {
        this.m_da = da;
        this.m_ic = da.getI18nControlInstance();
        this.m_ic_core = da.getParentI18nControlInstance();
    }


    /** 
     * Get Column Value
     * 
     * @param num 
     * @param di 
     * @return 
     */
    public String getColumnValue(int num, DeviceInterface di)
    {
        // System.out.println("Num: " + num);
        // System.out.println("DeviceInterface: " + di);
        switch (num)
        {
            case 1:
                // System.out.println("Di:" + di + ", company: " +
                // di.getDeviceCompany() + ", shortName: ");
                return di.getDeviceCompany().getName();

            case 2:
                return di.getName();

            case 3:
                return this.m_ic.getMessage(di.getConnectionProtocol().getI18nKey());

            case 4:
                return determineValueForDownloadSupportType(di.getDownloadSupportType(), false);

            default:
                return "N/A: " + num;
        }
    }


    /**
     * Get Column Value
     *
     * @param num
     * @param di
     * @return
     */
    public String getColumnValue(int num, DeviceInterfaceV2 di)
    {
        // System.out.println("Num: " + num);
        // System.out.println("DeviceInterface: " + di);
        switch (num)
        {
            case 1:
                return di.getCompany().getName();

            case 2:
                return di.getName();

            case 3:
                return m_ic.getMessage(di.getConnectionProtocol().getI18nKey());

            case 4:
                return determineValueForDownloadSupportType(di.getDownloadSupportType(), false);

            default:
                return "N/A: " + num;
        }
    }


    public String getTooltipValue(int index, DeviceInterface deviceInterface)
    {
        switch (index)
        {
            case 4:
                return determineValueForDownloadSupportType(deviceInterface.getDownloadSupportType(), true);

            default:
                return getColumnValue(index, deviceInterface);
        }
    }


    public String getTooltipValue(int index, DeviceInterfaceV2 deviceInterfaceV2)
    {
        switch (index)
        {
            case 4:
                return determineValueForDownloadSupportType(deviceInterfaceV2.getDownloadSupportType(), true);

            default:
                return getColumnValue(index, deviceInterfaceV2);

        }
    }


    private String determineValueForDownloadSupportType(DownloadSupportType downloadSupportType, boolean isTooltip)
    {
        StringBuilder sb = new StringBuilder();

        if (downloadSupportType.isNotSupported())
        {
            if (downloadSupportType == DownloadSupportType.NotSupportedByGGC)
            {
                return getTranslationWithRoot("DOWNLOAD_NOT_SUPPORTED_GGC", isTooltip);
            }
            else if (downloadSupportType == DownloadSupportType.NotSupportedByGGCYet)
            {
                return getTranslationWithRoot("DOWNLOAD_NOT_SUPPORTED_GGC_YET", isTooltip);
            }
            else
            {
                return getTranslationWithRoot("DOWNLOAD_NOT_SUPPORTED_BY_DEVICE", isTooltip);
            }
        }

        if (DownloadSupportType.isOptionSet(downloadSupportType, DownloadSupportType.DownloadData))
        {
            DataAccessPlugInBase.appendToStringBuilder(sb, getTranslationWithRoot("DOWNLOAD_DATA", isTooltip), "/");
        }

        if (DownloadSupportType.isOptionSet(downloadSupportType, DownloadSupportType.DownloadDataFile))
        {
            DataAccessPlugInBase.appendToStringBuilder(sb, getTranslationWithRoot("DOWNLOAD_DATA_FILE", isTooltip),
                "/");
        }

        if (DownloadSupportType.isOptionSet(downloadSupportType, DownloadSupportType.DownloadConfig))
        {
            DataAccessPlugInBase.appendToStringBuilder(sb, getTranslationWithRoot("DOWNLOAD_CONFIG", isTooltip), "/");
        }

        if (DownloadSupportType.isOptionSet(downloadSupportType, DownloadSupportType.DownloadConfigFile))
        {
            DataAccessPlugInBase.appendToStringBuilder(sb, getTranslationWithRoot("DOWNLOAD_CONFIG_FILE", isTooltip),
                "/");
        }

        return sb.toString();
    }


    private String getTranslationWithRoot(String rootKey, boolean isTooltip)
    {
        return this.m_ic.getMessage(isTooltip ? rootKey : rootKey + "_SHORT");
    }


    /** 
     * Get Column Count
     * 
     * @return 
     */
    public int getColumnCount()
    {
        return this.device_columns_width.length;
    }


    /** 
     * Get Column Name
     * 
     * @param num 
     * @return 
     */
    public String getColumnName(int num)
    {
        if (device_columns == null)
        {
            this.device_columns = new String[4];
            device_columns[0] = this.m_ic_core.getMessage("DEVICE_COMPANY");
            device_columns[1] = this.m_ic_core.getMessage("DEVICE_DEVICE");
            device_columns[2] = this.m_ic_core.getMessage("DEVICE_PROTOCOL");
            device_columns[3] = this.m_ic_core.getMessage("DEVICE_DOWNLOAD");
        }

        return device_columns[num];
    }


    /** 
     * Get Column Width
     * 
     * @param num 
     * @param width 
     * @return 
     */
    public int getColumnWidth(int num, int width)
    {
        return (int) (this.device_columns_width[num] * width);
    }


    // public abstract static DataAccessPlugInBase getPlugInDataAccess();

    public void prepareDeviceIdentification(OutputWriter outputWriter)
    {
        DeviceIdentification di = outputWriter.getDeviceIdentification();

        setDeviceToDeviceIdentification(di);

        outputWriter.setDeviceIdentification(di);
    }


    public void setDeviceToDeviceIdentification(DeviceIdentification di)
    {
        Object deviceInterfaceObject = this.m_da.getSelectedDeviceInstance();

        if (deviceInterfaceObject instanceof DeviceInstanceWithHandler)
        {
            DeviceInstanceWithHandler deviceInterfaceV2 = (DeviceInstanceWithHandler) deviceInterfaceObject;
            di.company = deviceInterfaceV2.getCompany().getName();
            di.device_selected = deviceInterfaceV2.getName();
        }
        else
        {
            DeviceInterface deviceInterfaceV1 = (DeviceInterface) deviceInterfaceObject;
            di.company = deviceInterfaceV1.getDeviceCompany().getName();
            di.device_selected = deviceInterfaceV1.getName();
        }
    }


    public int compareTo(DeviceInterface firstElement, SelectableInterface secondElement)
    {
        if (secondElement instanceof DeviceAbstract)
        {
            DeviceAbstract in = (DeviceAbstract) secondElement;
            if (firstElement.getDeviceCompany().equals(in.getDeviceCompany()))
            {
                return firstElement.getName().compareTo((in.getName()));
            }
            else
            {
                return firstElement.getDeviceCompany().getName().compareTo(in.getDeviceCompany().getName());
            }
        }
        else if (secondElement instanceof DeviceInterfaceV2)
        {
            DeviceInterfaceV2 dd = (DeviceInterfaceV2) secondElement;

            if (firstElement.getDeviceCompany().getName().equals(dd.getCompany().getName()))
            {
                return firstElement.getName().compareTo((dd.getName()));
            }
            else
            {
                return firstElement.getDeviceCompany().getName().compareTo(dd.getCompany().getName());
            }
        }

        return 0;

    }


    public int compareTo(DeviceInterfaceV2 firstElement, SelectableInterface secondElement)
    {
        if (secondElement instanceof DeviceAbstract)
        {
            DeviceAbstract in = (DeviceAbstract) secondElement;
            if (firstElement.getCompany().equals(in.getDeviceCompany()))
            {
                return firstElement.getName().compareTo((in.getName()));
            }
            else
            {
                return firstElement.getCompany().getName().compareTo(in.getDeviceCompany().getName());
            }
        }
        else if (secondElement instanceof DeviceInterfaceV2)
        {
            DeviceInterfaceV2 dd = (DeviceInterfaceV2) secondElement;

            if (firstElement.getCompany().getName().equals(dd.getCompany().getName()))
            {
                return firstElement.getName().compareTo((dd.getName()));
            }
            else
            {
                return firstElement.getCompany().getName().compareTo(dd.getCompany().getName());
            }
        }

        return 0;

    }

}
