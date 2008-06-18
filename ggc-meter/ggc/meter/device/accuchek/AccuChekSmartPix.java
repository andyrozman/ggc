
package ggc.meter.device.accuchek;

import ggc.meter.data.MeterValuesEntry;
import ggc.meter.device.MeterException;
import ggc.meter.output.OutputUtil;
import ggc.meter.protocol.XmlProtocol;
import ggc.meter.util.DataAccess;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.atech.utils.ATechDate;

public class AccuChekSmartPix extends XmlProtocol
{
    
    DataAccess m_da = DataAccess.getInstance();
    
    
    public AccuChekSmartPix()
    {
        
    }
    
    
    
    //************************************************
    //***      Meter Identification Methods        ***
    //************************************************


    /**
     * getName - Get Name of meter. 
     * 
     * @return name of meter
     */
    public String getName()
    {
        return "name";
    }


    /**
     * getIcon - Get Icon of meter
     * Should be implemented by meter class.
     * 
     * @return ImageIcon
     */
    public ImageIcon getIcon()
    {
        return null;
    }

    
    /**
     * getIconName - Get Icon of meter
     * 
     * @return icon name
     */
    public String getIconName()
    {
        return null;
    }
    

    /**
     * getMeterId - Get Meter Id, within Meter Company class 
     * 
     * @return id of meter within company
     */
    public int getMeterId()
    {
        return 1;
    }

    
    /**
     * getGlobalMeterId - Get Global Meter Id, within Meter Company class 
     * 
     * @return global id of meter
     */
    public int getGlobalMeterId()
    {
        return 0;
    }

    
    /**
     * getCompanyId - Get Company Id 
     * 
     * @return id of company
     */
    public int getCompanyId()
    {
        return 0;
    }
    
    
    /**
     * getInstructions - get instructions for device
     * 
     * @return instructions for reading data 
     */
    public String getInstructions()
    {
        return null;
    }
    
    /**
     * getComment - Get Comment for device 
     * 
     * @return comment or null
     */
    public String getComment()
    {
        return null;
    }
    
    
    /**
     * getImplementationStatus - Get Company Id 
     * 
     * @return implementation status as number
     * @see ggc.meter.manager.MeterImplementationStatus
     */
    public int getImplementationStatus() 
    {
        return 0;
    }
    
    
    
    
    
    
    

    /* 
     * canClearData
     */
    public boolean canClearData()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* 
     * canReadConfiguration
     */
    public boolean canReadConfiguration()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* 
     * canReadData
     */
    public boolean canReadData()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* 
     * canReadDeviceInfo
     */
    public boolean canReadDeviceInfo()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* 
     * canReadPartitialData
     */
    public boolean canReadPartitialData()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* 
     * clearDeviceData
     */
    public void clearDeviceData() throws MeterException
    {
        // TODO Auto-generated method stub
        
    }

    /* 
     * getData
     */
    public ArrayList<MeterValuesEntry> getData(int from, int to)
            throws MeterException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* 
     * getDataFull
     */
    public ArrayList<MeterValuesEntry> getDataFull() throws MeterException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* 
     * getDeviceConfiguration
     */
    public ArrayList<String> getDeviceConfiguration() throws MeterException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* 
     * getDeviceInfo
     */
    public ArrayList<String> getDeviceInfo() throws MeterException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* 
     * getInfo
     */
    public String getInfo()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* 
     * getStatus
     */
    public int getStatus()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* 
     * getTimeDifference
     */
    public int getTimeDifference()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* 
     * isStatusOK
     */
    public boolean isStatusOK()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* 
     * readDeviceData
     */
    public void readDeviceData() throws MeterException
    {
        // TODO Auto-generated method stub
        
    }

    /* 
     * setDeviceAllowedActions
     */
    public void setDeviceAllowedActions(boolean can_read_data,
            boolean can_read_partitial_data, boolean can_clear_data,
            boolean can_read_device_info, boolean can_read_device_configuration)
    {
        // TODO Auto-generated method stub
        
    }

    /* 
     * test
     */
    public void test()
    {
        // TODO Auto-generated method stub
        
    }
    
    Document document;
    
    public Document parse(File file) throws DocumentException {
        SAXReader reader = new SAXReader();
        
        
        //Document 
        document = reader.read(file);
        return document;
    }
    
    
    private int bg_unit = OutputUtil.BG_MGDL;
    
    
    public void testXml(File file)
    {
        try
        {
            /*Document doc =*/ parse(file);
            
            getPixDeviceInfo();
            System.out.println();

            getMeterDeviceInfo();
            System.out.println();
            
            readData();
            
        }
        catch(Exception ex)
        {
            System.out.println("Exception on testXml: " + ex);
            ex.printStackTrace();
            
        }
    }

    
    public void getPixDeviceInfo()
    {
        Node nd = getNode("IMPORT/ACSPIX");
        //System.out.println(nd);
        
        StringBuffer sb = new StringBuffer();
        
        Element e = (Element)nd;
        
        sb.append("Accu-Chek Smart Pix Device [" + e.attributeValue("Type") + "]\n");
        
        sb.append("Version v" + e.attributeValue("Ver"));
        sb.append(" [S/N=" + e.attributeValue("SN") + "]");
        
        System.out.println(sb.toString());
        //List nodes = getNodes("ACSPIX");
        //System.out.println(nodes);
    }

    
    public void getMeterDeviceInfo()
    {
        Element el = getElement("IMPORT/DEVICE");
        //System.out.println(nd);

        StringBuffer sb = new StringBuffer();
        sb.append("Meter Device: Accu-Chek " + el.attributeValue("Name"));
        sb.append("\nS/N=" + el.attributeValue("SN") + ", BG Unit: ");
        sb.append(el.attributeValue("BGUnit"));
        sb.append(", Time on device: " + el.attributeValue("Tm") + " " + el.attributeValue("Dt"));
        
        System.out.println(sb.toString());
        
        if (el.attributeValue("BGUnit").equals("mmol/L"))
        {
            this.bg_unit = OutputUtil.BG_MMOL;
        }
        
//        <DEVICE  Name="Performa" SN="50003006" 
        //Dt="2008-05-13" Tm="10:12" BGUnit="mmol/L"/>

        
    }
    
    
    public void readData()
    {
        List<Node> nodes = getNodes("IMPORT/BGDATA/BG");
        ArrayList<MeterValuesEntry> lst = new ArrayList<MeterValuesEntry>();
        
        for(int i=0; i<nodes.size(); i++)
        {
            lst.add(getDataEntry(nodes.get(i)));
        }
        
    }
    
    
    public MeterValuesEntry getDataEntry(Node entry)
    {
        Element el = (Element)entry;
        
        MeterValuesEntry mve = new MeterValuesEntry();
        //ATechDate at = null;
        mve.setDateTime(new ATechDate(this.getDateTime(el.attributeValue("Dt"), el.attributeValue("Tm"))));
        mve.setBgUnit(this.bg_unit);
        mve.setBgValue(el.attributeValue("Val"));
        
        // <BG Val="5.1" Dt="2005-06-07" Tm="18:01" D="1"/>

        System.out.println(mve);
        
        return mve;
        
    }
    
    
    public Node getNode(String tag_path)
    {
        return document.selectSingleNode(tag_path);
    }
    
    public Element getElement(String tag_path)
    {
        return (Element)getNode(tag_path);
    }

    
    
    public List<Node> getNodes(String tag_path)
    {
        List<Node> nodes = document.selectNodes(tag_path);
        return nodes;
    }
    
    private long getDateTime(String date, String time)
    {
        String o = m_da.replaceExpression(date, "-", "");
        
        if (time==null)
        {
            o += "0000";
        }
        else
        {
            o += m_da.replaceExpression(time, ":", "");
        }
        
        return Long.parseLong(o);
        
        
    }
    
    
    
    public static final void main(String[] args)
    {
        AccuChekSmartPix acspd = new AccuChekSmartPix();
        
        acspd.testXml(new File("G0003006.XML"));
        
    }
    
    
}
