
package ggc.meter.device.accuchek;

import ggc.meter.data.MeterValuesEntry;
import ggc.meter.device.DeviceIdentification;
import ggc.meter.output.OutputUtil;
import ggc.meter.output.OutputWriter;
import ggc.meter.protocol.ConnectionProtocols;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.atech.utils.ATechDate;

public abstract class AccuChekSmartPixMeter extends AccuChekSmartPix //extends AbstractXmlMeter //mlProtocol //implements SelectableInterface
{
    
    //DataAccessMeter m_da = DataAccessMeter.getInstance();
    //OutputWriter output_writer = null;

    
    public static final int ROCHE_COMPANY             = 2;
    
    public static final int METER_ROCHE_SMARTPIX_DEVICE   = 20001;
    public static final int METER_ACCUCHEK_ACTIVE         = 20002;
    public static final int METER_ACCUCHEK_ADVANTAGE      = 20003;
    public static final int METER_ACCUCHEK_AVIVA          = 20004;
    public static final int METER_ACCUCHEK_COMFORT        = 20005;
    public static final int METER_ACCUCHEK_COMPACT        = 20006;
    public static final int METER_ACCUCHEK_COMPACT_PLUS   = 20007;
    public static final int METER_ACCUCHEK_GO             = 20008;
    public static final int METER_ACCUCHEK_INTEGRA        = 20009;
    public static final int METER_ACCUCHEK_PERFORMA       = 20010;
    public static final int METER_ACCUCHEK_SENSOR         = 20011;
    
    
    private int bg_unit = OutputUtil.BG_MGDL;

    
    public AccuChekSmartPixMeter()
    {
        super();
    }
    
    public AccuChekSmartPixMeter(String drive_letter, OutputWriter writer)
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
        return AccuChekSmartPixMeter.METER_ROCHE_SMARTPIX_DEVICE;
    }

    

    
    /**
     * getCompanyId - Get Company Id 
     * 
     * @return id of company
     */
    public int getCompanyId()
    {
        return AccuChekSmartPixMeter.ROCHE_COMPANY;
    }
    
    
    
    
    
    Document document;


    public void processXml(File file)
    {
        try
        {
            /*Document doc =*/ parse(file);
            
            getPixDeviceInfo();
            System.out.println();

            getMeterDeviceInfo();
            System.out.println();
            
            this.output_writer.writeDeviceIdentification();
            
            readData();
            
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

    
    public void getMeterDeviceInfo()
    {
        DeviceIdentification di = this.output_writer.getDeviceIdentification();

        Element el = getElement("IMPORT/DEVICE");
        //System.out.println(nd);

        StringBuffer sb = new StringBuffer();
        sb.append("Meter Device: Accu-Chek " + el.attributeValue("Name"));
        sb.append("\nS/N=" + el.attributeValue("SN") + ", BG Unit: ");
        sb.append(el.attributeValue("BGUnit"));
        sb.append(", Time on device: " + el.attributeValue("Tm") + " " + el.attributeValue("Dt"));

        di.device_identified = sb.toString();
        
        //System.out.println(sb.toString());
        
        if (el.attributeValue("BGUnit").equals("mmol/L"))
        {
            this.bg_unit = OutputUtil.BG_MMOL;
        }
        
//        <DEVICE  Name="Performa" SN="50003006" 
        //Dt="2008-05-13" Tm="10:12" BGUnit="mmol/L"/>

        
    }
    
    
    public void readData()
    {
        //this.output_writer.
        
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

        //System.out.println(mve);
        
        this.output_writer.writeBGData(mve);
        
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

    
    
    @SuppressWarnings("unchecked")
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
    
    
}
