package ggc.plugin.util;

import ggc.plugin.device.DeviceInterface;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.protocol.ConnectionProtocols;

import com.atech.i18n.I18nControlAbstract;

public class PlugInDeviceUtil
{

    DataAccessPlugInBase m_da = null;
    I18nControlAbstract m_ic = null;
    float device_columns_width[] = { 0.2f, 0.3f, 0.25f, 0.25f };
    String device_columns[] = null;
    I18nControlAbstract m_ic_core = null;
    
    
    
    public PlugInDeviceUtil(DataAccessPlugInBase da)
    {
        this.m_da = da;
        this.m_ic = da.getI18nControlInstance();
        this.m_ic_core = da.getParentI18nControlInstance();
    }
    
    
    /** 
     * getColumnValue
     */
    public String getColumnValue(int num, DeviceInterface di)
    {
        //System.out.println("Num: " + num);
        switch(num)
        {
            case 1:
                return di.getDeviceCompany().getShortName();

            case 2:
                return di.getName();
                
            case 3:
                return this.m_ic_core.getMessage(ConnectionProtocols.connectionProtocolDescription[di.getConnectionProtocol()]);

            case 4:
                
                String dd = "";
                
                //System.out.println("DST: " + di.getDownloadSupportType());
                
                if ((di.getDownloadSupportType() & DownloadSupportType.DOWNLOAD_FROM_DEVICE) == DownloadSupportType.DOWNLOAD_FROM_DEVICE)
                {
                    dd = appendToString(dd, this.m_ic_core.getMessage("DOWNLOAD_DEVICE"), "/");
                }
                
                if ((di.getDownloadSupportType() & DownloadSupportType.DOWNLOAD_FROM_DEVICE_FILE) == DownloadSupportType.DOWNLOAD_FROM_DEVICE_FILE)
                {
                    dd = appendToString(dd, this.m_ic_core.getMessage("DOWNLOAD_FILE"), "/");
                }

                if ((di.getDownloadSupportType() & DownloadSupportType.DOWNLOAD_CONFIG_FROM_DEVICE) == DownloadSupportType.DOWNLOAD_CONFIG_FROM_DEVICE)
                {
                    dd = appendToString(dd, this.m_ic_core.getMessage("DOWNLOAD_CONFIG"), "/");
                }
                
                
                if (dd.length()==0)
                {

                    if ((di.getDownloadSupportType() & DownloadSupportType.DOWNLOAD_SUPPORT_NO) == DownloadSupportType.DOWNLOAD_SUPPORT_NO)
                    {
                        dd = this.m_ic_core.getMessage("DOWNLOAD_NOT_SUPPORTED_GGC");
                    }
                    else if ((di.getDownloadSupportType() & DownloadSupportType.DOWNLOAD_SUPPORT_NA_DEVICE) == DownloadSupportType.DOWNLOAD_SUPPORT_NA_DEVICE)
                    {
                        dd = this.m_ic_core.getMessage("DOWNLOAD_NOT_SUPPORTED_BY_DEVICE");
                    }
                    else
                        dd= "";
                }
                
                
                return dd;
                /*
                if (this.getDownloadSupportType()==DownloadSupportType.DOWNLOAD_YES)
                    return DataAccessPump.getInstance().getYesNoOption(true);
                else
                    return DataAccessPump.getInstance().getYesNoOption(false);
                */
                
            //case 5:
                //return "Bo/Ba/Tbr";
            //    return this.m_da.getYesNoOption(false);
                
                
            default:                 
                return "N/A: " + num;
        }
    }
    
    
    public String appendToString(String input, String add, String delim)
    {
        if (input.length()>0)
        {
            input += delim + add;
        }
        else
            input = add;
        
        return input;
    }

    
    
    
    /** 
     * Get Column Count
     */
    public int getColumnCount()
    {
        return this.device_columns_width.length;
    }

    
    /** 
     * getColumnName
     */
    public String getColumnName(int num)
    {
        if (device_columns==null)
        {
            this.device_columns = new String[4];
            device_columns[0] = this.m_ic_core.getMessage("DEVICE_COMPANY");
            device_columns[1] = this.m_ic_core.getMessage("DEVICE_DEVICE");
            device_columns[2] = this.m_ic_core.getMessage("DEVICE_PROTOCOL");
            device_columns[3] = this.m_ic_core.getMessage("DEVICE_DOWNLOAD");
        }
        
        return device_columns[num-1];
    }
    
    
    /** 
     * Get Column Width
     */
    public int getColumnWidth(int num, int width)
    {
        return (int)(this.device_columns_width[num-1] * width);
    }
    
    
    
    //public abstract static DataAccessPlugInBase getPlugInDataAccess();
    
    
}
