
package ggc.pump.device.accuchek;

import ggc.pump.data.PumpValuesEntry;
import ggc.pump.data.defs.PumpDataType;
import ggc.pump.device.DeviceIdentification;
import ggc.pump.output.OutputUtil;
import ggc.pump.output.OutputWriter;
import ggc.pump.protocol.ConnectionProtocols;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.atech.utils.ATechDate;

public abstract class AccuChekSmartPixPump extends AccuChekSmartPix //extends AbstractXmlMeter //mlProtocol //implements SelectableInterface
{
    
    //DataAccessMeter m_da = DataAccessMeter.getInstance();
    //OutputWriter output_writer = null;

    
    public static final int ROCHE_COMPANY                 = 2;
    public static final int DISETRONIC_COMPANY            = 3;
    
    public static final int ROCHE_SMARTPIX_DEVICE        = 20001;
    public static final int PUMP_DISETRONIC_D_TRON       = 20002;
    public static final int PUMP_ACCUCHEK_D_TRON         = 20003;
    public static final int PUMP_ACCUCHEK_SPIRIT         = 20004;
    
    
    //private int bg_unit = OutputUtil.BG_MGDL;
    
    
    
    
    

    
    public AccuChekSmartPixPump()
    {
        super();
    }
    
    public AccuChekSmartPixPump(String drive_letter, OutputWriter writer)
    {
        super(drive_letter, writer); 
    }
    
    
    
    
    /**
     * getMeterId - Get Meter Id, within Meter Company class 
     * 
     * @return id of meter within company
     */
    public int getMeterId()
    {
        return AccuChekSmartPixPump.ROCHE_SMARTPIX_DEVICE;
    }

    

    
    /**
     * getCompanyId - Get Company Id 
     * 
     * @return id of company
     */
    public int getCompanyId()
    {
        return AccuChekSmartPixPump.ROCHE_COMPANY;
    }
    
    
    
    
    
    Document document;


    public void processXml(File file)
    {
        try
        {
            parse(file);
/*            
            getPixDeviceInfo();
            System.out.println();

            getPumpDeviceInfo();
            System.out.println();
            
            this.output_writer.writeDeviceIdentification();
  */          
            //readData();
            
            readPumpData();
            
        }
        catch(Exception ex)
        {
            System.out.println("Exception on testXml: " + ex);
            ex.printStackTrace();
            
        }
    }
    
    
    
    
    
    
    
    private Document parse(File file) throws DocumentException {
        SAXReader reader = new SAXReader();
        
        
        //Document 
        document = reader.read(file);
        return document;
    }
    

    
    public void getPixDeviceInfo()
    {
        DeviceIdentification di = this.output_writer.getDeviceIdentification();
        
        Node nd = getNode("IMPORT/ACSPIX");
        //System.out.println(nd);
        
        StringBuffer sb = new StringBuffer();
        
        Element e = (Element)nd;
        
        String s = "Accu-Chek Smart Pix Device [" + e.attributeValue("Type") + "]";
        sb.append(s + "\n");

        di.company = s;
        
        StringBuilder sb2 = new StringBuilder();
        
        sb2.append("Version v" + e.attributeValue("Ver"));
        sb2.append(" [S/N=" + e.attributeValue("SN") + "]");

        
        di.device_selected = sb2.toString();
        
        sb.append(di.device_selected);
        
        
        //System.out.println(sb.toString());
        //List nodes = getNodes("ACSPIX");
        //System.out.println(nodes);
        
    }

    
    public void getPumpDeviceInfo()
    {
        DeviceIdentification di = this.output_writer.getDeviceIdentification();

        Element el = getElement("IMPORT/IP");
        //System.out.println(nd);

        StringBuffer sb = new StringBuffer();
        
// extend        
        
        sb.append("Pump Device: Accu-Chek " + el.attributeValue("Name"));
        sb.append("\nS/N=" + el.attributeValue("SN")); // + ", BG Unit: ");
        //sb.append(el.attributeValue("BGUnit"));
        sb.append(", Time on device: " + el.attributeValue("Tm") + " " + el.attributeValue("Dt"));

        di.device_identified = sb.toString();
        
        //System.out.println(sb.toString());
        /*
        if (el.attributeValue("BGUnit").equals("mmol/L"))
        {
            this.bg_unit = OutputUtil.BG_MMOL;
        }*/
        
//        <DEVICE  Name="Performa" SN="50003006" 
        //Dt="2008-05-13" Tm="10:12" BGUnit="mmol/L"/>
        
    }

    
    public void readPumpProfiles()
    {
        
    }
    
    
    
    
    
    
    public void readPumpData()
    {
        ArrayList<PumpValuesEntry> list = new ArrayList<PumpValuesEntry>();
        
//        list.addAll(getBasals());
//        list.addAll(getBoluses());
//        list.addAll(getEvents());
        
        getEvents();
    }

    
    private ArrayList<PumpValuesEntry> getBoluses()
    {
        getNodes("IMPORT/IPDATA/BASAL");   // /BOLUS        
        
        return null;
        
    }
    

    private ArrayList<PumpValuesEntry> getBasals()
    {
        
        
        return null;
        
    }
    
    
    
    private ArrayList<PumpValuesEntry> getEvents()
    {
        List<Node> lst = getSpecificDataChildren("IMPORT/IPDATA/EVENT");
        ArrayList<PumpValuesEntry> lst_out = new ArrayList<PumpValuesEntry>();
        
        for(int i=0; i<lst.size(); i++)
        {
            Element el = (Element)lst.get(i);
            
            PumpValuesEntry pve = new PumpValuesEntry();
            
            pve.setDateTime(this.getDateTime(el.attributeValue("Dt"), el.attributeValue("Tm")));

            // TODO: set event id, alarm id
            //pve.setEntryType(PumpDataType.PUMP_DATA_EVENT);
            this.resolveEvent(pve, el);
            
            lst_out.add(pve);
        }
        
        return lst_out;
        
/*        
        Dt          CDATA #REQUIRED
        Tm          CDATA #REQUIRED
        shortinfo   CDATA #IMPLIED
        description CDATA #IMPLIED
*/
        
        
    }
    
    
    private void resolveEvent(PumpValuesEntry pve, Element el)
    {
        String info = el.attributeValue("shortinfo");
        String desc = el.attributeValue("description");
        
        if (isSet(info))
        {
            if (info.startsWith("A"))
            {
                System.out.println("info: " + info + ", desc=" + desc);
            }
            else if (info.startsWith("E"))
            {
                
            }
        }
        else
        {
            
        }
        
        pve.setEntryType(PumpDataType.PUMP_DATA_EVENT);
        
    }
    

    private boolean isSet(String str)
    {
        if ((str==null) || (str.trim().length()==0))
        {
            return false;
        }
        else
            return true;
        
        
    }
    
    

    public List<Node> getSpecificDataChildren(String child_path)
    {
        return getNodes(child_path);   // /BOLUS
    }
    
    
    
    public PumpValuesEntry getDataEntry(Node entry)
    {
        
        System.out.println(entry.getName());
        
        
        return null;
    }
    
    
    
    
    
    
    
    /*
    public PumpValuesEntry getDataEntry(Node entry)
    {
        Element el = (Element)entry;
        
        PumpValuesEntry mve = new PumpValuesEntry();
        //ATechDate at = null;
        mve.setDateTime(new ATechDate(this.getDateTime(el.attributeValue("Dt"), el.attributeValue("Tm"))));
        mve.setBgUnit(this.bg_unit);
        mve.setBgValue(el.attributeValue("Val"));
        
        // <BG Val="5.1" Dt="2005-06-07" Tm="18:01" D="1"/>

        //System.out.println(mve);
        
        this.output_writer.writeBGData(mve);
        
        return mve;
        
    }
    */
    
    public Node getNode(String tag_path)
    {
        return document.selectSingleNode(tag_path);
    }
    
    public Element getElement(String tag_path)
    {
        return (Element)getNode(tag_path);
    }

    
    
    @SuppressWarnings("unchecked")
    public List<Node> getNodes(String tag_path)
    {
        List<Node> nodes = document.selectNodes(tag_path);
        return nodes;
    }
    
    /**
     * Pump tool, requires dates to be in seconds, so we need to return value is second, eventhough
     * pix returns it in minutes.
     * 
     * @param date
     * @param time
     * @return
     */
    private long getDateTime(String date, String time)
    {
        String o = m_da.replaceExpression(date, "-", "");
        
        if ((time==null) || (time.length()==0))
        {
            o += "0000";
        }
        else
        {
            o += m_da.replaceExpression(time, ":", "");
        }
        
        o += "00"; // seconds
        
        return Long.parseLong(o);
        
        
    }
    
/*
    public void setMeterCompany(AbstractMeterCompany company)
    {
        this.meter_company = company;
    }
    
    
    public AbstractMeterCompany getMeterCompany()
    {
        return this.meter_company;
    }
*/
    
    
    /*
    public static final void main(String[] args)
    {
        AccuChekSmartPix acspd = new AccuChekSmartPix();
        
        acspd.testXml(new File("G0003006.XML"));
        
    }
    */
    
    public int getConnectionProtocol()
    {
        return ConnectionProtocols.PROTOCOL_MASS_STORAGE_XML;
    }
    
    /*
    public int getMaxMemoryRecords()
    {
        return 1;
    }
    */
    
    
    /**
     * loadPumpSpecificValues - should be called from constructor of any AbstractPump classes and should
     *      create, AlarmMappings and EventMappings and any other pump constants.
     */
    public void loadPumpSpecificValues()
    {
        
        
        
    }
    
    
    /**
     * Map pump specific alarms to PumpTool specific alarm codes
     * @return
     */
    public Hashtable<String,String> getAlarmMappings()
    {
        return null;
    }
    
    
    /**
     * Map pump specific events to PumpTool specific event codes
     * @return
     */
    public Hashtable<String,String> getEventMappings()
    {
        return null;
    }

    
    
    
}
