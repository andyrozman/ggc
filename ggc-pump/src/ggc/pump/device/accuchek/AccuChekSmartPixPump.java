package ggc.pump.device.accuchek;

import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.ConnectionProtocols;
import ggc.pump.data.PumpValuesEntry;
import ggc.pump.data.defs.PumpAlarms;
import ggc.pump.data.defs.PumpBasalSubType;
import ggc.pump.data.defs.PumpBaseType;
import ggc.pump.data.defs.PumpBolusType;
import ggc.pump.data.defs.PumpErrors;
import ggc.pump.data.defs.PumpEvents;
import ggc.pump.data.defs.PumpReport;
import ggc.pump.manager.PumpDevicesIds;
import ggc.pump.util.DataAccessPump;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import com.atech.utils.ATechDate;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
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
 *  Filename:      AccuChekSmartPixPump  
 *  Description:   Accu-Chek SmartPix Processor for Pumps 
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public abstract class AccuChekSmartPixPump extends AccuChekSmartPix //extends AbstractXmlMeter //mlProtocol //implements SelectableInterface
{
    
    //DataAccessMeter m_da = DataAccessMeter.getInstance();
    //OutputWriter output_writer = null;
    private static Log log = LogFactory.getLog(AccuChekSmartPixPump.class);

    
    

    
    //private int bg_unit = OutputUtil.BG_MGDL;
    
    private Hashtable<String,Integer> alarm_mappings = null;
    private Hashtable<String,Integer> event_mappings = null;
    private Hashtable<String,Integer> error_mappings = null;
    private Hashtable<String,Integer> bolus_mappings = null;
    private Hashtable<String,Integer> report_mappings = null;
    private Hashtable<String,Integer> basal_mappings = null;
   
    
    
    

    
    /**
     * Constructor
     */
    public AccuChekSmartPixPump()
    {
        super();
        loadPumpSpecificValues();
    }

    
    /**
     * Constructor
     * 
     * @param cmp 
     */
    public AccuChekSmartPixPump(AbstractDeviceCompany cmp)
    {
        super(cmp);
    }
    
    
    /**
     * Constructor
     * 
     * @param drive_letter
     * @param writer
     */
    public AccuChekSmartPixPump(String drive_letter, OutputWriter writer)
    {
        super(drive_letter, writer); 
        loadPumpSpecificValues();
        m_da = DataAccessPump.getInstance();
        this.setPumpType("Accu-Chek/Roche", this.getName());
    }
    
    
    
    
    /**
     * getDeviceId - Get Meter Id, within Meter Company class 
     * 
     * @return id of device within company
     */
    public int getDeviceId()
    {
        return PumpDevicesIds.ROCHE_SMARTPIX_DEVICE;
    }

    

    
    /**
     * getCompanyId - Get Company Id 
     * 
     * @return id of company
     */
    public int getCompanyId()
    {
        return PumpDevicesIds.COMPANY_ROCHE;
    }
    
    
    
    
    
    Document document;


    /**
     * Process Xml
     */
    public void processXml(File file)
    {
        try
        {
            this.openXmlFile(file);
            //openXmlFile()
            //parse(file);
/*            
            getPixDeviceInfo();
            System.out.println();

            getPumpDeviceInfo();
            System.out.println();
            
            this.output_writer.writeDeviceIdentification();
  */          
            //readData();

            getPixDeviceInfo();
            getPumpDeviceInfo();

            this.output_writer.writeDeviceIdentification();
            
            readPumpData();
            //this.readPumpDataTest();
            
            System.out.println("Reading done");
            
        }
        catch(Exception ex)
        {
            System.out.println("Exception on testXml: " + ex);
            ex.printStackTrace();
            
        }
    }
    
    
    
    
    
    
    /*
    private Document parse(File file) throws DocumentException {
        SAXReader reader = new SAXReader();
        
        
        //Document 
        document = reader.read(file);
        return document;
    }
    */

    
    /**
     * Letter with which report starts (I for insulin pumps, G for glucose meters)
     * 
     * @return
     */
    public String getFirstLetterForReport()
    {
        return "I";
    }
    
    
    
    /**
     * Get Pix Device Info 
     */
    private void getPixDeviceInfo()
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

    
    private void getPumpDeviceInfo()
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

    
    @SuppressWarnings("unused")
    private void readPumpProfiles()
    {
        // TODO    
    }
    
    
    private void readPumpData()
    {
        ArrayList<PumpValuesEntry> list = new ArrayList<PumpValuesEntry>();

        System.out.println(" -- Basals --");
        list.addAll(getBasals());
        System.out.println("Basals: " + list.size());
        
        System.out.println(" -- Boluses --");
        list.addAll(getBoluses());
        System.out.println("Boluses: " + list.size());

        System.out.println(" -- Events --");
        list.addAll(getEvents());
        System.out.println("Events: " + list.size());

        System.out.println(" -- Basals (run 2) --");
        list.addAll(getSpecificElements2("BASAL"));
        System.out.println("Basals2: " + list.size());
        
        if (first_basal !=null)
            list.add(first_basal);
        
        /*
        for(int i=0; i<list.size(); i++)
        {
            this.output_writer.writeData(list.get(i));
        }
        */

        
        
        // postprocess entries (profile), 
        
    }

    
    @SuppressWarnings("unused")
    private void readPumpDataTest()
    {
        ArrayList<PumpValuesEntry> list = new ArrayList<PumpValuesEntry>();
        
        //System.out.println(" -- Basals --");
        //list.addAll(getBasals());
        
        
        list.addAll(getProfileElements());
        
        if (first_basal !=null)
            list.add(first_basal);
        
    }
    
    
    
    
    private ArrayList<PumpValuesEntry> getBoluses()
    {
        return getSpecificElements("BOLUS", AccuChekSmartPixPump.TAG_BOLUS);
    }
    

    private ArrayList<PumpValuesEntry> getBasals()
    {
        return getSpecificElements("BASAL", AccuChekSmartPixPump.TAG_BASAL);
    }
    
    
    private ArrayList<PumpValuesEntry> getEvents()
    {
        return getSpecificElements("EVENT", AccuChekSmartPixPump.TAG_EVENT);
    }

    
    /** 
     * test
     */
    public void test()
    {
        readPumpDataTest();
    }
    
    
    /**
     * Tag: Basal
     */
    public static final int TAG_BASAL = 1;
    
    /**
     * Tag: Bolus
     */
    public static final int TAG_BOLUS = 2;
    
    /**
     * Tag: Event
     */
    public static final int TAG_EVENT = 3;
    
    
    
    private ArrayList<PumpValuesEntry> getSpecificElements(String element, int type)
    {
        List<Node> lst = getSpecificDataChildren("IMPORT/IPDATA/" + element);
        ArrayList<PumpValuesEntry> lst_out = new ArrayList<PumpValuesEntry>();
        boolean add = false;
        
        for(int i=0; i<lst.size(); i++)
        {
            Element el = (Element)lst.get(i);
            
            PumpValuesEntry pve = new PumpValuesEntry(this.getDeviceSourceName());
            pve.setDateTimeObject(this.getDateTime(el.attributeValue("Dt"), el.attributeValue("Tm")));

            add = false;
            
            if (type==AccuChekSmartPixPump.TAG_BASAL)
            {
                add = this.resolveBasalBase(pve, el);                
            }
            else if (type==AccuChekSmartPixPump.TAG_EVENT)
            {
                add = this.resolveEvent(pve, el);
            }
            else if (type==AccuChekSmartPixPump.TAG_BOLUS)
            {
                add = this.resolveBolus(pve, el);
            }
        
            
            if (add)
            {
                // testing only
                this.output_writer.writeData(pve);

                lst_out.add(pve);
            }
        }
        
        return lst_out;
        
    }
    

    
    private ArrayList<PumpValuesEntry> getSpecificElements2(String element)
    {
        List<Node> lst = getSpecificDataChildren("IMPORT/IPDATA/" + element);
        ArrayList<PumpValuesEntry> lst_out = new ArrayList<PumpValuesEntry>();
        boolean add = false;
        
        for(int i=0; i<lst.size(); i++)
        {
            Element el = (Element)lst.get(i);
            
            PumpValuesEntry pve = new PumpValuesEntry(this.getDeviceSourceName());
            pve.setDateTimeObject(this.getDateTime(el.attributeValue("Dt"), el.attributeValue("Tm")));

            add = this.resolveBasalProfile(pve, el);                
            
            if (add)
            {
                // testing only
                this.output_writer.writeData(pve);

                lst_out.add(pve);
            }
        }
        
        System.out.println("Profiles: " + lst_out.size());
        
        return lst_out;
        
    }
    
    
    private ArrayList<PumpValuesEntry> getProfileElements()
    {
        List<Node> lst = getSpecificDataChildren("IMPORT/IPDATA/BASAL"); // + element);
        ArrayList<PumpValuesEntry> lst_out = new ArrayList<PumpValuesEntry>();
        boolean add = false;
        
        for(int i=0; i<lst.size(); i++)
        {
            Element el = (Element)lst.get(i);
            
            PumpValuesEntry pve = new PumpValuesEntry(this.getDeviceSourceName());
            pve.setDateTimeObject(this.getDateTime(el.attributeValue("Dt"), el.attributeValue("Tm")));

            add = this.resolveBasalProfilePatterns(pve, el);                
            
            if (add)
            {
                // testing only
//                this.output_writer.writeData(pve);

                lst_out.add(pve);
            }
        }
        
        System.out.println("Profiles patterns: " + lst_out.size());
        
        return lst_out;
        
    }

    
    
    private boolean resolveBasalBase(PumpValuesEntry pve, Element el)
    {
        String remark = el.attributeValue("remark");
        String tbrdec = el.attributeValue("TBRdec");
        String tbrinc = el.attributeValue("TBRinc");
        String cbrf = el.attributeValue("cbrf");
        String profile = el.attributeValue("profile");
        
        System.out.println(el);
        
        if ((isSet(tbrdec)) || (isSet(tbrinc)))
        {
            if ((remark==null) || (remark.trim().length()==0))
                return false;
                
            pve.setBaseType(PumpBaseType.PUMP_DATA_BASAL);
            pve.setSubType(PumpBasalSubType.PUMP_BASAL_TEMPORARY_BASAL_RATE);
            
            String v = "";
            
            if (isSet(tbrdec))
            {
                v = tbrdec.trim();
            }
            else
            {
                v = tbrinc.trim();
            }
            
            // dur 00:01 h
            
            String[] dr = remark.split(" ");
            
            pve.setValue(String.format("DURATION=%s;VALUE=%s",
                         dr[1],
                         v));
            
            //System.out.println("Unknown TBR Event. [remark=" + remark + ",tbrdec=" + tbrdec + ",tbrinc=" + tbrinc + ",value=" + cbrf + "]");
            
            return true;
        }
        else if (isSet(remark))
        {
            // all that are special should be removed, all other are checked over events
            if (remark.contains("changed"))
            {
                //System.out.println("Basal Changed Unknown [remark=" + remark + "]");
                return false;
            }
            else
            {
                if (this.getEventMappings().containsKey(remark))
                {
                    pve.setBaseType(PumpBaseType.PUMP_DATA_EVENT);
                    pve.setSubType(this.getEventMappings().get(remark));
                    //System.out.println("Basal Event Unknown [remark=" + remark + "]");
                    return true;
                }
                else if (this.getBasalMappings().containsKey(remark))
                {
                    pve.setBaseType(PumpBaseType.PUMP_DATA_BASAL);
                    pve.setSubType(this.getBasalMappings().get(remark));
                    //System.out.println("Basal Event Unknown [remark=" + remark + "]");
                    return true;
                    
                }
                else
                {
                    if (!remark.contains(" - "))
                        System.out.println("Unknown Basal Event. [remark=" + remark + ",tbrdec=" + tbrdec + ",tbrinc=" + tbrinc + ",value=" + cbrf + "]");

                    return false;
                }
            }
            
            //System.out.println("Remark:" + remark);
        }
        else
        {
            if (!isSet(profile))
            {
                if ((!isSet(tbrdec)) && (!isSet(tbrinc)) && (cbrf.equals("0.00")))
                {
                }
                else
                    log.error("Basal Unknown [tbrdec=" + tbrdec + ",tbrinc=" + tbrinc + ",value=" + cbrf + "]");
            }
            return false;
            
        }
        
    }
    

    PumpValuesEntry first_basal = null;
    
    private boolean resolveBasalProfile(PumpValuesEntry pve, Element el)
    {
        String remark = el.attributeValue("remark");
        String tbrdec = el.attributeValue("TBRdec");
        String tbrinc = el.attributeValue("TBRinc");
        String cbrf = el.attributeValue("cbrf");
        String profile = el.attributeValue("profile");
        
        if ((isSet(tbrdec)) || (isSet(tbrinc)))
        {
            return false;
        }
        else if (isSet(remark))
        {
            // all that are special should be removed, all other are checked over events
            if (remark.contains("changed"))
            {
                // TODO: Changed profile
//                System.out.println("Basal Changed Unknown [remark=" + remark + "]");
                //System.out.println("Basal Rate Changed [datetime=" + pve.getDateTimeObject() + ",remark=" + remark + ",tbrdec=" + tbrdec + ",tbrinc=" + tbrinc + ",value=" + cbrf + "]");
                
                return false;
            }
            else
            {
                if ((this.getEventMappings().containsKey(remark)) ||
                    (this.getBasalMappings().containsKey(remark)))
                {
                    return false;
                }
                else
                {
                    if (remark.contains(" - "))
                    {
                        pve.setBaseType(PumpBaseType.PUMP_DATA_BASAL);
                        pve.setSubType(PumpBasalSubType.PUMP_BASAL_PROFILE);
                        pve.setValue(remark.substring(remark.indexOf(" - ")+ 3));
//                        System.out.println("Profile changed: " + remark.substring(remark.indexOf(" - ")+ 3));
//                        System.out.println("Unknown Profile Event. [remark=" + remark + ",tbrdec=" + tbrdec + ",tbrinc=" + tbrinc + ",value=" + cbrf + "]");
                        return true;
                    }
                    else
                        return false;
                }
            }
            
        }
        else
        {
            if (!isSet(profile))
            {
                //System.out.println("tbrdec=" + isSet(tbrdec) + "tbrinc=" + isSet(tbrinc));

                if ((!isSet(tbrdec)) && (!isSet(tbrinc)) && (cbrf.equals("0.00")))
                {
                    //System.out.println("tbrdec=" + isSet(tbrdec) + "tbrinc=" + isSet(tbrinc));
                    return false;
                }
                else
                {
                    log.error("Basal Unknown [tbrdec=" + tbrdec + ",tbrinc=" + tbrinc + ",value=" + cbrf + "]");
                    return false;
                }
            }
            else
            {
                // profile used
                pve.setBaseType(PumpBaseType.PUMP_DATA_BASAL);
                pve.setSubType(PumpBasalSubType.PUMP_BASAL_PROFILE);
                pve.setValue(profile);
                
                first_basal = pve;

                //log.error("Basal Unknown [tbrdec=" + tbrdec + ",tbrinc=" + tbrinc + ",value=" + cbrf + "]");
                
                if (pve.getDateTimeObject().getTimeString().equals("00:00:00"))
                {
                    //System.out.println("Profile used: " + pve.getValue());
                    first_basal = null;
                    return true;
                }
                else
                    return false;
            }
        }        
    }
    
    
    private boolean resolveBasalProfilePatterns(PumpValuesEntry pve, Element el)
    {
        // TODO
        String remark = el.attributeValue("remark");
        String tbrdec = el.attributeValue("TBRdec");
        String tbrinc = el.attributeValue("TBRinc");
        String cbrf = el.attributeValue("cbrf");
        String profile = el.attributeValue("profile");
        
        if ((isSet(tbrdec)) || (isSet(tbrinc)))
        {
            return false;
        }
        else //if (isSet(remark))
        {
            if (isSet(remark))
            {
                if ((!remark.contains("TBR")) && (!remark.contains("Run")) && (!remark.contains("Stop")) && (!remark.contains("power"))   
                        )
                {
                    System.out.println(pve.getDateTimeObject().toString() + ",value=" + cbrf + ", profile=" + profile+",remark=" + remark);
                }
            }
            else
                System.out.println(pve.getDateTimeObject().toString() + ",value=" + cbrf + ", profile=" + profile);

            return true;
        }
            
/*            
            // all that are special should be removed, all other are checked over events
            if (remark.contains("changed"))
            {
                // TODO: Changed profile
//                System.out.println("Basal Changed Unknown [remark=" + remark + "]");
                //System.out.println("Basal Rate Changed [datetime=" + pve.getDateTimeObject() + ",remark=" + remark + ",tbrdec=" + tbrdec + ",tbrinc=" + tbrinc + ",value=" + cbrf + "]");
                
                return false;
            }
            else
            {
                if ((this.getEventMappings().containsKey(remark)) ||
                    (this.getBasalMappings().containsKey(remark)))
                {
                    return false;
                }
                else
                {
                    if (remark.contains(" - "))
                    {
                        pve.setBaseType(PumpBaseType.PUMP_DATA_BASAL);
                        pve.setSubType(PumpBasalSubType.PUMP_BASAL_PROFILE);
                        pve.setValue(remark.substring(remark.indexOf(" - ")+ 3));
//                        System.out.println("Profile changed: " + remark.substring(remark.indexOf(" - ")+ 3));
//                        System.out.println("Unknown Profile Event. [remark=" + remark + ",tbrdec=" + tbrdec + ",tbrinc=" + tbrinc + ",value=" + cbrf + "]");
                        return true;
                    }
                    else
                        return false;
                }
            }
            
        }
        else
        {
            if (!isSet(profile))
            {
                //System.out.println("tbrdec=" + isSet(tbrdec) + "tbrinc=" + isSet(tbrinc));

                if ((!isSet(tbrdec)) && (!isSet(tbrinc)) && (cbrf.equals("0.00")))
                {
                    //System.out.println("tbrdec=" + isSet(tbrdec) + "tbrinc=" + isSet(tbrinc));
                    return false;
                }
                else
                {
                    log.error("Basal Unknown [tbrdec=" + tbrdec + ",tbrinc=" + tbrinc + ",value=" + cbrf + "]");
                    return false;
                }
            }
            else
            {
                // profile used
                pve.setBaseType(PumpBaseType.PUMP_DATA_BASAL);
                pve.setSubType(PumpBasalSubType.PUMP_BASAL_PROFILE);
                pve.setValue(profile);
                
                first_basal = pve;

                //log.error("Basal Unknown [tbrdec=" + tbrdec + ",tbrinc=" + tbrinc + ",value=" + cbrf + "]");
                
                if (pve.getDateTimeObject().getTimeString().equals("00:00:00"))
                {
                    //System.out.println("Profile used: " + pve.getValue());
                    first_basal = null;
                    return true;
                }
                else
                    return false;
            }
        } */
            
    }
    

    private boolean resolveBolus(PumpValuesEntry pve, Element el)
    {
        String type = el.attributeValue("type");
        String amount = el.attributeValue("amount");
        String remark = el.attributeValue("remark");
        
        if (remark!=null)
            remark = remark.trim();
    

        if (isSet(type))
        {
            if (this.getBolusMappings().containsKey(type))
            {
                pve.setBaseType(PumpBaseType.PUMP_DATA_BOLUS);
                pve.setSubType(this.getBolusMappings().get(type));
                
                if ((pve.getSubType()==PumpBolusType.PUMP_BOLUS_STANDARD) ||
                    (pve.getSubType()==PumpBolusType.PUMP_BOLUS_AUDIO_SCROLL))
                {
                    pve.setSubType(PumpBolusType.PUMP_BOLUS_STANDARD);
                    pve.setValue(amount);
                }
                else if (pve.getSubType()==PumpBolusType.PUMP_BOLUS_SQUARE)
                {
                    String e = remark.substring(0, remark.indexOf(" h"));
                    pve.setValue("AMOUNT_SQUARE=" + amount + ";DURATION=" + e);
                    // PumpBolusType.PUMP_BOLUS_STANDARD
                    //0:13 h
                }
                else if (pve.getSubType()==PumpBolusType.PUMP_BOLUS_MULTIWAVE)
                {
                    if (remark!=null)
                    {
                        
                        //System.out.println("Remark: " + remark);

                        String[] str = new String[4];
                        
                        str[0] = remark.substring(0, remark.indexOf(" / "));
                        str[1] = remark.substring(remark.indexOf(" / ")+3);
                        
                        str[2] = str[1].substring(0, str[1].indexOf("  "));
                        str[3] = str[1].substring(str[1].indexOf("  ")+2);
                        
                        str[3] = str[3].substring(0, str[3].indexOf(" h"));
                        
                        //System.out.println("Remark: 1=" + str[0] + ",2=" + str[2] + ",3=" + str[3]);
                        
                        pve.setValue(String.format("AMOUNT=%s;AMOUNT_SQUARE=%s;DURATION=%s", 
                                                   str[0], 
                                                   str[2], 
                                                   str[3]));
                    }
                }
                else
                {
                    //System.out.println("Undefined Bolus type");
                    log.error("AccuChekSmartPixPump: Unknown Bolus Type [" + type +"]");
                }
                
                //System.out.println("Bolus [subtype=" + pve.getSubTypeString() + ",amount=" + pve.getValue() + ",remark=" + remark);
            }
            else
            {
                log.error("Unknown Pump Bolus [info=" + type + ",desc=" + amount + ",remark=" + remark + "]");
            }
        }
        else
        {
            if (this.getReportMappings().containsKey(remark))
            {
                pve.setBaseType(PumpBaseType.PUMP_DATA_REPORT);
                pve.setSubType(this.getReportMappings().get(remark));
                pve.setValue(amount);
                //System.out.println("Report mapping [type=" + type + ",subtype=" + pve.getSubTypeString() + ",amount=" + amount + ",remark=" + remark);
            }
            else
            {
                log.error("Unknown Pump Bolus Info [info=" + type + ",desc=" + amount + ",remark=" + remark + "]");
            }
        }
        
        
        return true;
    }    
    
    
    
    private boolean resolveEvent(PumpValuesEntry pve, Element el)
    {
        String info = el.attributeValue("shortinfo");
        String desc = el.attributeValue("description");
        
        if (isSet(info))
        {
            if (info.startsWith("A"))
            {
                if (this.getAlarmMappings().containsKey(info))
                {
                    pve.setBaseType(PumpBaseType.PUMP_DATA_ALARM);
                    pve.setSubType(this.getAlarmMappings().get(info).intValue());
                    //System.out.println("info: " + info + ", desc=" + desc);
                }
                else
                {
                    log.error("Unknown Pump Alarm [info=" + info + ",desc=" + desc + "]");
                }
            }
            else if (info.startsWith("E"))
            {
                if (this.getErrorMappings().containsKey(info))
                {
                    pve.setBaseType(PumpBaseType.PUMP_DATA_ERROR);
                    pve.setSubType(this.getErrorMappings().get(info).intValue());
                    //System.out.println("info: " + info + ", desc=" + desc);
                }
                else
                {
                    log.error("Unknown Pump Error [info=" + info + ",desc=" + desc + "]");
                }
                
            }
            else
            {

                if (this.getEventMappings().containsKey(desc))
                {
                    pve.setBaseType(PumpBaseType.PUMP_DATA_EVENT);
                    pve.setSubType(this.getEventMappings().get(desc));
                    pve.setValue(info);
                }
                else
                {
                    log.error("Unknown Pump Event [info=" + info + ",desc=" + desc + "]");
                }
            }
                
        }
        else
        {
            if (this.getEventMappings().containsKey(desc))
            {
                pve.setBaseType(PumpBaseType.PUMP_DATA_EVENT);
                pve.setSubType(this.getEventMappings().get(desc));
            }
            else
            {
                log.error("Unknown Pump Event [info=" + info + ",desc=" + desc + "]");
            }
        }
        
        //pve.setEntryType(PumpDataType.PUMP_DATA_EVENT);
        
        
        return true;
        
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
    
    

    private List<Node> getSpecificDataChildren(String child_path)
    {
        return getNodes(child_path);   // /BOLUS
    }
    
    
    
    @SuppressWarnings("unused")
    private PumpValuesEntry getDataEntry(Node entry)
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
   
    /*
    public Node getNode(String tag_path)
    {
        return document.selectSingleNode(tag_path);
    }
    
    public Element getElement(String tag_path)
    {
        return (Element)getNode(tag_path);
    }
*/
    
    /*
    @SuppressWarnings("unchecked")
    public List<Node> getNodes(String tag_path)
    {
        List<Node> nodes = document.selectNodes(tag_path);
        return nodes;
    }*/
    
    /**
     * Pump tool, requires dates to be in seconds, so we need to return value is second, eventhough
     * pix returns it in minutes.
     * 
     * @param date
     * @param time
     * @return
     */
    private ATechDate getDateTime(String date, String time)
    {
        //System.out.println("m_da: " + m_da);
        
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
        
        
        return new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, Long.parseLong(o));
        
        
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
    
    /**
     * Get Connection Protocol
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
        //m_da = DataAccessPump.getInstance();
        
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
        this.event_mappings.put("time / date corrected", new Integer(PumpEvents.PUMP_EVENT_DATETIME_CORRECTED));
        this.event_mappings.put("time / date set (time shift back)", PumpEvents.PUMP_EVENT_DATETIME_CORRECTED);
        
        
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
        this.bolus_mappings.put("Scr", new Integer(PumpBolusType.PUMP_BOLUS_AUDIO_SCROLL));
        this.bolus_mappings.put("Ext", new Integer(PumpBolusType.PUMP_BOLUS_SQUARE));
        this.bolus_mappings.put("Mul", new Integer(PumpBolusType.PUMP_BOLUS_MULTIWAVE));
        
        // report
        this.report_mappings = new Hashtable<String,Integer>();
        this.report_mappings.put("Bolus Total", new Integer(PumpReport.PUMP_REPORT_BOLUS_TOTAL_DAY));
        this.report_mappings.put("Bolus+Basal Total", new Integer(PumpReport.PUMP_REPORT_INSULIN_TOTAL_DAY));
        
        
        this.basal_mappings = new Hashtable<String,Integer>();
        this.basal_mappings.put("TBR End (cancelled)", PumpBasalSubType.PUMP_BASAL_TEMPORARY_BASAL_RATE_CANCELED);
        this.basal_mappings.put("TBR End", PumpBasalSubType.PUMP_BASAL_TEMPORARY_BASAL_RATE_ENDED);

        
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

    
    /**
     * Get Error Mappings - Map pump specific errors to Pump Tool specific 
     *     event codes
     * @return
     */
    public Hashtable<String,Integer> getErrorMappings()
    {
        return this.error_mappings;
    }
    

    /**
     * Get Bolus Mappings - Map pump specific bolus to Pump Tool specific 
     *     event codes
     * @return
     */
    public Hashtable<String,Integer> getBolusMappings()
    {
        return this.bolus_mappings;
    }
    

   /**
     * Get Basal Mappings - Map pump specific basal to Pump Tool specific 
     *     event codes
     * @return
     */
    public Hashtable<String,Integer> getBasalMappings()
    {
        return this.basal_mappings;
    }
    
    
    /**
     * Get Report Mappings - Map pump specific reports to Pump Tool specific 
     *     event codes
     * @return
     */
    public Hashtable<String,Integer> getReportMappings()
    {
        return this.report_mappings;
    }
    
 
    
    /**
     * Get Download Support Type
     * 
     * @return
     */
    public int getDownloadSupportType()
    {
        return DownloadSupportType.DOWNLOAD_YES;
    }

    
    /**
     * Are Pump Settings Set
     * 
     * @return
     */
    public boolean arePumpSettingsSet()
    {
        return false;
    }
    
    
    /**
     * How Many Months Of Data Stored
     * 
     * @return
     */
    public int howManyMonthsOfDataStored()
    {
        return 6;
    }
    
    
}