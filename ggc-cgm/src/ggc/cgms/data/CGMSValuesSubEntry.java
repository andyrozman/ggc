package ggc.cgms.data;

import java.util.ArrayList;

import org.hibernate.Session;

import com.atech.utils.ATechDate;

import ggc.core.db.hibernate.GGCHibernateObject;
import ggc.plugin.data.DeviceValuesEntry;

public class CGMSValuesSubEntry extends DeviceValuesEntry
{
    
    public static final int CGMS_BG_READING = 1;
    public static final int METER_CALIBRATION_READING = 2;
    
    
    public long datetime = 0L;

    public int date = 0;
    public int time = 0;
    public int value = 0;
    public int type = 0;
    
    public String getSubEntryValue()
    {
        return time + "=" + value;
    }
    
    public void setDateTime(long dt)
    {
        date = (int)(dt/1000000);
        time = (int)(datetime - date);
//      System.out.println(dt/1000000);
        //return dt/1000000;
        
        
        
    }
    
    
    
    public String toString()
    {
        return datetime + " = " + value;
    }


    @Override
    public Object getColumnValue(int index)
    {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public long getDateTime()
    {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public int getDateTimeFormat()
    {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public ATechDate getDateTimeObject()
    {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public ArrayList<? extends GGCHibernateObject> getDbObjects()
    {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public void prepareEntry()
    {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void setDateTimeObject(ATechDate dt)
    {
        // TODO Auto-generated method stub
        
    }


    public String getDVEName()
    {
        // TODO Auto-generated method stub
        return null;
    }


    public long getId()
    {
        // TODO Auto-generated method stub
        return 0;
    }


    public String getSource()
    {
        // TODO Auto-generated method stub
        return null;
    }


    public String getSpecialId()
    {
        // TODO Auto-generated method stub
        return null;
    }


    public Object getTableColumnValue(int index)
    {
        // TODO Auto-generated method stub
        return null;
    }


    public String getValue()
    {
        // TODO Auto-generated method stub
        return null;
    }


    public void setId(long idIn)
    {
        // TODO Auto-generated method stub
        
    }


    public void setSource(String src)
    {
        // TODO Auto-generated method stub
        
    }


    public String getDataAsString()
    {
        // TODO Auto-generated method stub
        return null;
    }


    public String DbAdd(Session sess) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }


    public boolean DbDelete(Session sess) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }


    public boolean DbEdit(Session sess) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }


    public boolean DbGet(Session sess) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }


    public boolean DbHasChildren(Session sess) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }


    public int getAction()
    {
        // TODO Auto-generated method stub
        return 0;
    }


    public String getObjectName()
    {
        // TODO Auto-generated method stub
        return null;
    }


    public String getObjectUniqueId()
    {
        // TODO Auto-generated method stub
        return null;
    }


    public boolean isDebugMode()
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    
}
