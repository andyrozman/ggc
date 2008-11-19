
package ggc.pump.device.accuchek;

import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.output.OutputWriter;
import ggc.pump.data.PumpValuesEntry;
import ggc.pump.data.defs.PumpAlarms;
import ggc.pump.data.defs.PumpBasalSubType;
import ggc.pump.data.defs.PumpBolusType;
import ggc.pump.data.defs.PumpDataType;
import ggc.pump.data.defs.PumpErrors;
import ggc.pump.data.defs.PumpEvents;
import ggc.pump.data.defs.PumpReport;
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
    
    private Hashtable<String,Integer> alarm_mappings = null;
    private Hashtable<String,Integer> event_mappings = null;
    private Hashtable<String,Integer> error_mappings = null;
    private Hashtable<String,Integer> bolus_mappings = null;
    private Hashtable<String,Integer> report_mappings = null;
   
    
    
    

    
    public AccuChekSmartPixPump()
    {
        super();
        loadPumpSpecificValues();
    }
    
    public AccuChekSmartPixPump(String drive_letter, OutputWriter writer)
    {
        super(drive_letter, writer); 
        loadPumpSpecificValues();
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
            
            System.out.println("Reading done");
            
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

        System.out.println(" -- Basals --");
        list.addAll(getBasals());
/*        System.out.println(" -- Boluses --");
        list.addAll(getBoluses());
        System.out.println(" -- Events --");
        list.addAll(getEvents());
*/  
        
        // postprocess entries (profile), 
        
    }

    
    @SuppressWarnings("unused")
    private ArrayList<PumpValuesEntry> getBoluses()
    {
        return getSpecificElements("BOLUS", AccuChekSmartPixPump.TAG_BOLUS);
    }
    

    private ArrayList<PumpValuesEntry> getBasals()
    {
        return getSpecificElements("BASAL", AccuChekSmartPixPump.TAG_BASAL);
    }
    
    
    
    @SuppressWarnings("unused")
    private ArrayList<PumpValuesEntry> getEvents()
    {
        return getSpecificElements("EVENT", AccuChekSmartPixPump.TAG_EVENT);
    }

    
    
    public static final int TAG_BASAL = 1;
    public static final int TAG_BOLUS = 2;
    public static final int TAG_EVENT = 3;
    
    
    
    private ArrayList<PumpValuesEntry> getSpecificElements(String element, int type)
    {
        List<Node> lst = getSpecificDataChildren("IMPORT/IPDATA/" + element);
        ArrayList<PumpValuesEntry> lst_out = new ArrayList<PumpValuesEntry>();
        
        for(int i=0; i<lst.size(); i++)
        {
            Element el = (Element)lst.get(i);
            
            PumpValuesEntry pve = new PumpValuesEntry();
            pve.setDt_info(this.getDateTime(el.attributeValue("Dt"), el.attributeValue("Tm")));

            
            
            if (type==AccuChekSmartPixPump.TAG_BASAL)
            {
                this.resolveBasal(pve, el);                
            }
            else if (type==AccuChekSmartPixPump.TAG_EVENT)
            {
                this.resolveEvent(pve, el);
            }
            else if (type==AccuChekSmartPixPump.TAG_BOLUS)
            {
                this.resolveBolus(pve, el);
            }
            
            lst_out.add(pve);
        }
        
        return lst_out;
        
    }
    
    
    private void resolveBasal(PumpValuesEntry pve, Element el)
    {
        String remark = el.attributeValue("remark");
        
        
        
        if (isSet(remark))
        {
            // all that are special should be removed, all other are checked over events
            if (remark.contains("changed"))
            {
                
            }
            else
            {
                if (this.getEventMappings().containsKey(remark))
                {
                    
                }
                else
                {
                    System.out.println("Unknown Basal Event. [remark=" + remark + "]");
                }
            }
            
            //System.out.println("Remark:" + remark);
        }
        else
        {
            int type = 0;

            if (isSet(el.attributeValue("TBRdec")))
                type = 1;
            else if (isSet(el.attributeValue("TBRinc")))
                type = 2;
            
            pve.setProfile(el.attributeValue("profile"));

            if (type==0)
            {
                pve.setSub_type(PumpBasalSubType.PUMP_BASAL_VALUE);
                pve.setValue(el.attributeValue("cbrf"));
            }
            else
            {
                String procs = "";
                
                if (type==1)
                {
                    // TODO Fix
                    //pve.setSubType(PumpBasalSubType.PUMP_BASAL_TEMPORARY_BASAL_DECREMENT);
                    procs = el.attributeValue("TBRdec"); 
                }
                else
                {
                    // TODO Fix
                    //pve.setSubType(PumpBasalSubType.PUMP_BASAL_TEMPORARY_BASAL_INCREMENT);
                    procs = el.attributeValue("TBRinc"); 
                }
            
                // inc or dec is '110 %'
                procs = procs.substring(0, procs.indexOf(" "));
                procs = procs.trim();
                
                pve.setValue("VALUE=" + el.attributeValue("cbrf") + ";INC_DEC=" + procs);
                
            }
            
            
            
//            System.out.println("Undefined Basal [cbrf=" + cbrf + "]");
/*
            cbrf      CDATA #REQUIRED
            TBRdec    CDATA #IMPLIED
            TBRinc    CDATA #IMPLIED
            profile   CDATA #IMPLIED
  */          
            
        }
        
        
        
        /*
        if (isSet(el.attributeValue("TBRdec")))
            type = 1;
        else if (isSet(el.attributeValue("TBRinc")))
            type = 2;
        */
        
        
/*        
        cbrf      CDATA #REQUIRED
        TBRdec    CDATA #IMPLIED
        TBRinc    CDATA #IMPLIED
        profile   CDATA #IMPLIED
        remark    CDATA #IMPLIED
  */      
        
    }
    
    

    private void resolveBolus(PumpValuesEntry pve, Element el)
    {
        String type = el.attributeValue("type");
        String amount = el.attributeValue("amount");
        String remark = el.attributeValue("remark");
    

        if (isSet(type))
        {
            if (this.getBolusMappings().containsKey(type))
            {
                pve.setBase_type(PumpDataType.PUMP_DATA_BOLUS);
                pve.setSub_type(this.getBolusMappings().get(type));
                pve.setValue(amount);
            }
            else
            {
                System.out.println("Unknown Pump Bolus [info=" + type + ",desc=" + amount + ",remark=" + remark + "]");
            }
        }
        else
        {
            if (this.getReportMappings().containsKey(remark))
            {
                pve.setBase_type(PumpDataType.PUMP_DATA_BOLUS);
                pve.setSub_type(this.getReportMappings().get(remark));
                pve.setValue(amount);
            }
            else
            {
                System.out.println("Unknown Pump Bolus [desc=" + amount + ",remark=" + remark + "]");
            }
        }
        
        
        
/*        
        type      CDATA #REQUIRED
        amount    CDATA #REQUIRED
        remark    CDATA #IMPLIED
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
                if (this.getAlarmMappings().containsKey(info))
                {
                    pve.setBase_type(PumpDataType.PUMP_DATA_ALARM);
                    pve.setSub_type(this.getAlarmMappings().get(info).intValue());
                    //System.out.println("info: " + info + ", desc=" + desc);
                }
                else
                {
                    System.out.println("Unknown Pump Alarm [info=" + info + ",desc=" + desc + "]");
                }
            }
            else if (info.startsWith("E"))
            {
                if (this.getErrorMappings().containsKey(info))
                {
                    pve.setBase_type(PumpDataType.PUMP_DATA_ERROR);
                    pve.setSub_type(this.getErrorMappings().get(info).intValue());
                    //System.out.println("info: " + info + ", desc=" + desc);
                }
                else
                {
                    System.out.println("Unknown Pump Error [info=" + info + ",desc=" + desc + "]");
                }
                
            }
            else
            {

                if (this.getEventMappings().containsKey(desc))
                {
                    pve.setBase_type(PumpDataType.PUMP_DATA_EVENT);
                    pve.setSub_type(this.getEventMappings().get(desc));
                    pve.setValue(info);
                }
                else
                {
                    System.out.println("Unknown Pump Event [info=" + info + ",desc=" + desc + "]");
                }
                
            }
                
        }
        else
        {
            if (this.getEventMappings().containsKey(desc))
            {
                pve.setBase_type(PumpDataType.PUMP_DATA_EVENT);
                pve.setSub_type(this.getEventMappings().get(desc));
            }
            else
            {
                System.out.println("Unknown Pump Event [info=" + info + ",desc=" + desc + "]");
            }
        }
        
        //pve.setEntryType(PumpDataType.PUMP_DATA_EVENT);
        
    }
    

    private boolean isSet(String str)
    {
       // System.out.println("isSet: " + str);
        
        if ((str==null) || (str.trim().length()==0))
        {
        //    System.out.println("false");
            return false;
        }
        else
        {
          //  System.out.println("true");
            return true;
        }
        
        
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
        // alarm mappings
        this.alarm_mappings = new Hashtable<String,Integer>();
        this.alarm_mappings.put("A1", new Integer(PumpAlarms.PUMP_ALARM_CARTRIDGE_LOW));
        this.alarm_mappings.put("A2", new Integer(PumpAlarms.PUMP_ALARM_BATTERY_LOW));
        this.alarm_mappings.put("A3", new Integer(PumpAlarms.PUMP_ALARM_REVIEW_DATETIME));
        this.alarm_mappings.put("A4", new Integer(PumpAlarms.PUMP_ALARM_ALARM_CLOCK));
        this.alarm_mappings.put("A5", new Integer(PumpAlarms.PUMP_ALARM_PUMP_TIMER));
        this.alarm_mappings.put("A6", new Integer(PumpAlarms.PUMP_ALARM_TEMPORARY_BASAL_RATE_CANCELED));
        this.alarm_mappings.put("A7", new Integer(PumpAlarms.PUMP_ALARM_TEMPORARY_BASAL_RATE_OVER));
        this.alarm_mappings.put("A8", new Integer(PumpAlarms.PUMP_ALARM_BOLUS_CANCELED));
        
        this.event_mappings = new Hashtable<String,Integer>();
        this.event_mappings.put("prime infusion set", new Integer(PumpEvents.PUMP_EVENT_PRIME_INFUSION_SET));
        this.event_mappings.put("cartridge changed", new Integer(PumpEvents.PUMP_EVENT_CARTRIDGE_CHANGED));
        this.event_mappings.put("Run", new Integer(PumpEvents.PUMP_EVENT_BASAL_RUN));
        this.event_mappings.put("Stop", new Integer(PumpEvents.PUMP_EVENT_BASAL_STOP));
        this.event_mappings.put("power down", new Integer(PumpEvents.PUMP_EVENT_POWER_DOWN));
        this.event_mappings.put("power up", new Integer(PumpEvents.PUMP_EVENT_POWER_UP));
        this.event_mappings.put("time / date set", new Integer(PumpEvents.PUMP_EVENT_DATETIME_SET));
        this.event_mappings.put("time / date corrected", new Integer(PumpEvents.PUMP_EVENT_DATETIME_CORRECT));
        this.event_mappings.put("time / date set (time shift back)", new Integer(PumpEvents.PUMP_EVENT_DATETIME_CORRECT_TIME_SHIFT_BACK));
        
        
        this.error_mappings = new Hashtable<String,Integer>();
        this.error_mappings.put("E1", new Integer(PumpErrors.PUMP_ERROR_CARTRIDGE_EMPTY));
        this.error_mappings.put("E2", new Integer(PumpErrors.PUMP_ERROR_BATTERY_DEPLETED));
        this.error_mappings.put("E3", new Integer(PumpErrors.PUMP_ERROR_AUTOMATIC_OFF));
        this.error_mappings.put("E4", new Integer(PumpErrors.PUMP_ERROR_NO_DELIVERY));
        this.error_mappings.put("E5", new Integer(PumpErrors.PUMP_ERROR_END_OF_OPERATION));
        this.error_mappings.put("E6", new Integer(PumpErrors.PUMP_ERROR_MECHANICAL_ERROR));
        this.error_mappings.put("E7", new Integer(PumpErrors.PUMP_ERROR_ELECTRONIC_ERROR));
        this.error_mappings.put("E8", new Integer(PumpErrors.PUMP_ERROR_POWER_INTERRUPT));
        this.error_mappings.put("E10", new Integer(PumpErrors.PUMP_ERROR_CARTRIDGE_ERROR));
        this.error_mappings.put("E11", new Integer(PumpErrors.PUMP_ERROR_SET_NOT_PRIMED));
        this.error_mappings.put("E12", new Integer(PumpErrors.PUMP_ERROR_DATA_INTERRUPTED));
        this.error_mappings.put("E13", new Integer(PumpErrors.PUMP_ERROR_LANGUAGE_ERROR));
        this.error_mappings.put("E14", new Integer(PumpErrors.PUMP_ERROR_INSULIN_CHANGED));
        
        
        this.bolus_mappings = new Hashtable<String,Integer>();
        this.bolus_mappings.put("Std", new Integer(PumpBolusType.PUMP_BOLUS_STANDARD));
        this.bolus_mappings.put("Scr", new Integer(PumpBolusType.PUMP_BOLUS_SCROLL));
        
        // TODO: Support for other boluses
//        this.bolus_mappings.put("Ext", new Integer(PumpBolusType.PUMP_BOLUS_EXTENDED));
//        this.bolus_mappings.put("Mul", new Integer(PumpBolusType.PUMP_BOLUS_MULTIWAVE));
        
        // report
        this.report_mappings = new Hashtable<String,Integer>();
        this.report_mappings.put("Bolus Total", new Integer(PumpReport.PUMP_REPORT_BOLUS_TOTAL_DAY));
        this.report_mappings.put("Bolus+Basal Total", new Integer(PumpReport.PUMP_REPORT_INSULIN_TOTAL_DAY));
    }
    
    
    /**
     * Map pump specific alarms to PumpTool specific alarm codes
     * @return
     */
    public Hashtable<String,Integer> getAlarmMappings()
    {
        return this.alarm_mappings;
    }
    
    
    /**
     * Map pump specific events to PumpTool specific event codes
     * @return
     */
    public Hashtable<String,Integer> getEventMappings()
    {
        return this.event_mappings;
    }

    
    public Hashtable<String,Integer> getErrorMappings()
    {
        return this.error_mappings;
    }
    

    public Hashtable<String,Integer> getBolusMappings()
    {
        return this.bolus_mappings;
    }
    

    public Hashtable<String,Integer> getReportMappings()
    {
        return this.report_mappings;
    }
    
    
}
