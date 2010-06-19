package ggc.pump.device.accuchek;

import ggc.plugin.device.DeviceAbstract;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.impl.accuchek.AccuChekSmartPix;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.ConnectionProtocols;
import ggc.plugin.util.DataAccessPlugInBase;
import ggc.pump.data.PumpValuesEntry;
import ggc.pump.data.PumpValuesEntryProfile;
import ggc.pump.data.defs.PumpAlarms;
import ggc.pump.data.defs.PumpBasalSubType;
import ggc.pump.data.defs.PumpBaseType;
import ggc.pump.data.defs.PumpBolusType;
import ggc.pump.data.defs.PumpErrors;
import ggc.pump.data.defs.PumpEvents;
import ggc.pump.data.defs.PumpReport;
import ggc.pump.data.profile.Profile;
import ggc.pump.data.profile.ProfileSubEntry;
import ggc.pump.data.profile.ProfileSubOther;
import ggc.pump.data.profile.ProfileSubPattern;
import ggc.pump.device.PumpInterface;
import ggc.pump.manager.PumpDevicesIds;
import ggc.pump.util.DataAccessPump;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.GregorianCalendar;
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


public abstract class AccuChekSmartPixPump extends AccuChekSmartPix implements PumpInterface //extends AbstractXmlMeter //mlProtocol //implements SelectableInterface
{
    
    private static Log log = LogFactory.getLog(AccuChekSmartPixPump.class);
    
    private Hashtable<String,Integer> alarm_mappings = null;
    private Hashtable<String,Integer> event_mappings = null;
    private Hashtable<String,Integer> error_mappings = null;
    private Hashtable<String,Integer> bolus_mappings = null;
    private Hashtable<String,Integer> report_mappings = null;
    private Hashtable<String,Integer> basal_mappings = null;
   
    
    /**
     * Constructor
     * 
     * @param cmp 
     */
    public AccuChekSmartPixPump(AbstractDeviceCompany cmp)
    {
        super(cmp, DataAccessPump.getInstance());
        this.loadPumpSpecificValues();
        this.setDeviceType(cmp.getName(), getName(), DeviceAbstract.DEVICE_TYPE_PUMP);
    }
    
    
    
    /**
     * Constructor
     * 
     * @param conn_parameter
     * @param writer
     */
    public AccuChekSmartPixPump(String conn_parameter, OutputWriter writer)
    {
        this(conn_parameter, writer, DataAccessPump.getInstance());
    }
    
    
    /**
     * Constructor
     * 
     * @param conn_parameter
     * @param writer
     * @param da 
     */
    public AccuChekSmartPixPump(String conn_parameter, OutputWriter writer, DataAccessPlugInBase da)
    {
        super(conn_parameter, writer, da); 
        loadPumpSpecificValues();
        this.setDeviceType("Accu-Chek/Roche", getName(), DeviceAbstract.DEVICE_TYPE_PUMP);
    }
    
    
    
    
    /**
     * getDeviceId - Get Device Id 
     * 
     * @return id of device
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

            getPixDeviceInfo();
            getPumpDeviceInfo();

            this.output_writer.writeDeviceIdentification();
            
            readPumpData();
        }
        catch(Exception ex)
        {
            System.out.println("Exception on testXml: " + ex);
            ex.printStackTrace();
            
        }
    }
    
    
    
    
 

    
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
        
    }

    
    private void getPumpDeviceInfo()
    {
        DeviceIdentification di = this.output_writer.getDeviceIdentification();

        Element el = getElement("IMPORT/IP");

        StringBuffer sb = new StringBuffer();
        
        sb.append("Pump Device: Accu-Chek " + el.attributeValue("Name"));
        sb.append("\nS/N=" + el.attributeValue("SN")); // + ", BG Unit: ");
        sb.append(", Time on device: " + el.attributeValue("Tm") + " " + el.attributeValue("Dt"));

        di.device_identified = sb.toString();
        
    }

    
    
    
    private void readPumpData()
    {
        ArrayList<PumpValuesEntry> list = new ArrayList<PumpValuesEntry>();

        //log.info(" -- Basals --");
        list.addAll(getBasals());
        //System.out.println("Basals: " + list.size());
        
        //log.info(" -- Boluses --");
        list.addAll(getBoluses());
        //System.out.println("Boluses: " + list.size());

        //log.info(" -- Events --");
        list.addAll(getEvents());
        //System.out.println("Events: " + list.size());

        //log.info(" -- Profiles --");
        //list.addAll(this.getPumpProfiles());
        ArrayList<PumpValuesEntryProfile> list_profiles = this.getPumpProfiles();
      
        /*
        System.out.println(" -- Basals (run 2) --");
        list.addAll(getSpecificElements2("BASAL"));
        System.out.println("Basals2: " + list.size());
        */
        if (first_basal !=null)
            list.add(first_basal);
        
        
        for(int i=0; i<list.size(); i++)
        {
            this.output_writer.writeData(list.get(i));
        }
        
        
        for(int i=0; i<list_profiles.size(); i++)
        {
            this.output_writer.writeData(list_profiles.get(i));
        }
        
    }

    
    /**
     * Main method for reading Profile Pattern/Event entries
     */
    @SuppressWarnings("unchecked")
    private ArrayList<PumpValuesEntryProfile> getPumpProfiles()
    {
        List<Node> nodelist;
        
        //boolean was_broken = true;
        log.info("Profile processing - START");
        
        Element id = (Element)this.getNode("IMPORT/IP");
//        System.out.println("" + id.attributeValue("Dt") + id.attributeValue("Tm"));
        ATechDate dt = this.getDateTime(id.attributeValue("Dt"), id.attributeValue("Tm"));

        
        // 1. read initial
        log.info("STEP 1 - Read inital profiles");
        Hashtable<String,Profile> original_profiles = new Hashtable<String,Profile>();        

        nodelist = getSpecificDataChildren("IMPORT/IP/IPPROFILE");
        
        //System.out.println("Length: "+ nodelist.size());
        
        for(int i=0; i<nodelist.size(); i++)
        {
            Element n = (Element)nodelist.get(i);
            
            Profile profile = new Profile();
            profile.profile_id = n.attributeValue("Name");
           
            List<Node> nlist2 = (List<Node>)n.elements();
            
            for(int j=0; j<nlist2.size(); j++)
            {
                Element n2 = (Element)nlist2.get(j);
                
                ProfileSubPattern psp = new ProfileSubPattern();
                int num = Integer.parseInt(n2.attributeValue("Number"));
                
                psp.time_start = (num-1) * 100;
                psp.time_end = ((num-1) * 100) + 59;
                psp.amount = Float.parseFloat(n2.attributeValue("IU"));
                
                profile.add(psp);
            }
            
            profile.packProfiles();
            profile.isCompleteProfile();
            
            original_profiles.put(profile.profile_id, profile);
        }
        
        
        // 2. read all basal entries (we don't sort them now, we just put them all together, also events)
        log.info("STEP 2 - Reading profile data");
        
        List<Node> lst = getSpecificDataChildren("IMPORT/IPDATA/BASAL" );
        
        Hashtable<Long,Profile> profiles_raw = new Hashtable<Long,Profile>(); 
        
        
        for(int i=0; i<lst.size(); i++)
        {
            Element el = (Element)lst.get(i);
            
            ProfileSubEntry pse = this.resolveBasalProfilePatterns(el);

            if ((pse==null) || (pse.profile_id==null))
                continue;
            
            long dt_current = this.getDateFromDT(this.getDateTime(el.attributeValue("Dt"), el.attributeValue("Tm")).getATDateTimeAsLong()); 
            
            if (!profiles_raw.containsKey(dt_current))
            {
                Profile p = new Profile();
                p.date_at = dt_current;
                p.profile_id = pse.profile_id;
                p.add(pse);
                
                profiles_raw.put(dt_current, p);
            }
            else
                profiles_raw.get(dt_current).add(pse);
        }
        
        
        // 3. sort profile_entries into different profiles and patterns 
        log.info("STEP 3 - Remove incompletes and group profiles by name");
        
        // remove incomplete profiles, and group profiles by name
        
        Hashtable<String,ArrayList<Profile>> profiles_sorted = new Hashtable<String,ArrayList<Profile>>(); 
        ArrayList<ProfileSubOther> profile_changes = new ArrayList<ProfileSubOther>();
        
        for(Enumeration<Long> en = profiles_raw.keys(); en.hasMoreElements();  )
        {
            Long key = en.nextElement();
            Profile profile = profiles_raw.get(key);
            profile.fillEndTimes();
            
            profile_changes.addAll(profile.other_entries);
            
            
            if (profile.isCompleteProfile())
            {
                System.out.println("Profile id: " + profile.profile_id);
                if (profiles_sorted.containsKey(profile.profile_id))
                {
                    profiles_sorted.get(profile.profile_id).add(profile);
                }
                else
                {
                    ArrayList<Profile> lstx = new ArrayList<Profile>();
                    lstx.add(profile);
                    profiles_sorted.put(profile.profile_id, lstx);
                }
            }
        }
        
        
        
        // 4 - Create changes list
        log.info("STEP 4 - Create changes list");

        Hashtable<Long,ArrayList<ProfileSubOther>> profile_changes_v2 = new Hashtable<Long,ArrayList<ProfileSubOther>>();
        
        for(int i=0; i<profile_changes.size(); i++)
        {
            ProfileSubOther pchan = profile_changes.get(i);
            long date = ATechDate.convertATDate(pchan.time_event, ATechDate.FORMAT_DATE_AND_TIME_S, ATechDate.FORMAT_DATE_ONLY); 
            
            if (profile_changes_v2.containsKey(date))
            {
                profile_changes_v2.get(date).add(pchan);
            }
            else
            {
                ArrayList<ProfileSubOther> al = new ArrayList<ProfileSubOther>();
                al.add(pchan);
                profile_changes_v2.put(date, al);
            }
        }
        profile_changes = null;

        Hashtable<Long,ProfileSubOther> profile_changes_v3 = new Hashtable<Long,ProfileSubOther>();
        
        
        for(Enumeration<Long> en = profile_changes_v2.keys(); en.hasMoreElements(); )
        {

            long key = en.nextElement();
            
            if (profile_changes_v2.get(key).size()==1)
            {
                profile_changes_v3.put(key, profile_changes_v2.get(key).get(0));
            }
            else
            {
                ArrayList<ProfileSubOther> al = profile_changes_v2.get(key);
                Collections.sort(al);
                profile_changes_v3.put(key, al.get(0));
            }
        }
        
        profile_changes_v2 = null;
        

        // 5 - Create actual profile list and export it
        log.info("STEP 5 - Create actual active profile list");
        
        
        ArrayList<Profile> active_profiles = new ArrayList<Profile>();
        Hashtable<String,Profile> current_profiles = new Hashtable<String,Profile>();
        
        for(Enumeration<String> en = profiles_sorted.keys(); en.hasMoreElements();  )
        {
            String key = en.nextElement();
            current_profiles.put(key, original_profiles.get(key));
        }
        
        m_da.setSortSetting("Profile", "DESC");
        
        for(Enumeration<String> en = current_profiles.keys(); en.hasMoreElements();  )
        {
            String key = en.nextElement();
            ArrayList<Profile> profiles = profiles_sorted.get(key);
            
            Collections.sort(profiles);
            
            
            for(int i=0; i<profiles.size(); i++)
            {
                Profile p_curr = profiles.get(i);
                
                if (i==0)
                {
                    if (p_curr.date_at==ATechDate.convertATDate(dt.getATDateTimeAsLong(), ATechDate.FORMAT_DATE_AND_TIME_S, ATechDate.FORMAT_DATE_ONLY))
                        continue;
                }
                
                if (current_profiles.get(p_curr.profile_id).equals(p_curr))
                {
                    current_profiles.get(p_curr.profile_id).profile_active_from = (p_curr.date_at * 1000000); 
                }
                else
                {
                    active_profiles.add(current_profiles.get(p_curr.profile_id));
                    current_profiles.remove(p_curr.profile_id);
                    current_profiles.put(p_curr.profile_id, p_curr);
                    current_profiles.get(p_curr.profile_id).profile_active_till = ((p_curr.date_at * 1000000) + 235900); 
                    current_profiles.get(p_curr.profile_id).profile_active_from = (p_curr.date_at * 1000000); 
                }
            }
        }

       
        // 6 - Profile changes
        log.info("STEP 6 - Check profiles (dates) and save (export) data");
        
        
        for(int j=0; j<active_profiles.size(); j++)
        {
            Profile p_curr = active_profiles.get(j);
            
            long date = ATechDate.convertATDate(p_curr.profile_active_till, ATechDate.FORMAT_DATE_AND_TIME_S, ATechDate.FORMAT_DATE_ONLY);
            
            if (profile_changes_v3.containsKey(date))
            {
                ProfileSubOther chan = profile_changes_v3.get(date); 
                p_curr.profile_active_from = chan.time_event;
                
                if (j!=(active_profiles.size()-1))
                {
                    ATechDate aat = new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, chan.time_event);
                    aat.add(GregorianCalendar.MINUTE, -1);
                    int k = j + 1;
                    active_profiles.get(k).profile_active_till = aat.getATDateTimeAsLong();
                    j++;
                }
            }
        }

        // 7 - Profile export
        log.info("STEP 7 - Profile export");
        
        ArrayList<PumpValuesEntryProfile> pvep = new ArrayList<PumpValuesEntryProfile>();

        for(int i=0; i<active_profiles.size(); i++)
        {
            pvep.add(active_profiles.get(i).createDbObject());
        }
       
        log.info("Profile processing - END");

        
        return pvep;
        
    }
    
    
    
    /*
    private void readPumpDataTest()
    {
        ArrayList<PumpValuesEntry> list = new ArrayList<PumpValuesEntry>();
        
        //System.out.println(" -- Basals --");
        //list.addAll(getBasals());
        
        
        list.addAll(getProfileElements());
        
        if (first_basal !=null)
            list.add(first_basal);
        
    }*/
    
    
    
    
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
        //readPumpDataTest();
        getPumpProfiles();
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
    
    
    String[] type_desc = { "", "Basal", "Bolus", "Events" };
    
    private ArrayList<PumpValuesEntry> getSpecificElements(String element, int type)
    {
        List<Node> lst = getSpecificDataChildren("IMPORT/IPDATA/" + element);
        ArrayList<PumpValuesEntry> lst_out = new ArrayList<PumpValuesEntry>();
        boolean add = false;
        
        log.info("Process " + type_desc[type] + " data - START");
        
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
                //this.output_writer.writeData(pve);

                lst_out.add(pve);
            }
        }

        log.info("Process " + type_desc[type] + " data - END");
        
        return lst_out;
        
    }
    

    /*
    private ArrayList<PumpValuesEntry> getSpecificElements2(String element)
    {
        List<Node> lst = getSpecificDataChildren("IMPORT/IPDATA/" + element);
        ArrayList<PumpValuesEntry> lst_out = new ArrayList<PumpValuesEntry>();
        boolean add = false;
        
        
        Hashtable<Long,Profile> lst_spec = new Hashtable<Long,Profile>(); 
        
        
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
        
    }*/
    
    /*
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

//            add = this.resolveBasalProfilePatterns(pve, el);                
            
            if (add)
            {
                // testing only
//                this.output_writer.writeData(pve);

                lst_out.add(pve);
            }
        }
        
        System.out.println("Profiles patterns: " + lst_out.size());
        
        return lst_out;
        
    }*/

    
    
    private boolean resolveBasalBase(PumpValuesEntry pve, Element el)
    {
        String remark = el.attributeValue("remark");
        String tbrdec = el.attributeValue("TBRdec");
        String tbrinc = el.attributeValue("TBRinc");
        String cbrf = el.attributeValue("cbrf");
        String profile = el.attributeValue("profile");
        
        //System.out.println(el);
        
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
    
    
    
    
    private ProfileSubEntry resolveBasalProfilePatterns(/*PumpValuesEntry pve,*/ Element el)
    {
        //pve.setDateTimeObject(this.getDateTime(el.attributeValue("Dt"), el.attributeValue("Tm")));
        
        long dt = this.getDateTime(el.attributeValue("Dt"), el.attributeValue("Tm")).getATDateTimeAsLong();
        
        String remark = el.attributeValue("remark");
        String tbrdec = el.attributeValue("TBRdec");
        String tbrinc = el.attributeValue("TBRinc");
        String cbrf = el.attributeValue("cbrf");
        String profile = el.attributeValue("profile");
        
        /*if ((isSet(tbrdec)) || (isSet(tbrinc)))
        {
            //System.out.println("TBR");
            //System.out.println("" + el);
            return null;
        }
        else*/ 
        
        if (isSet(remark))
        {
            // all that are special should be removed, all other are checked over events
            if (remark.contains("changed"))
            {
                ProfileSubOther pso = new ProfileSubOther();
                
                pso.time_event = dt; 
                pso.event_type = ProfileSubOther.EVENT_PATTERN_CHANGED; 
                pso.profile_id = profile;
                pso.time_start = (int)ATechDate.convertATDate(dt, ATechDate.FORMAT_DATE_AND_TIME_S, ATechDate.FORMAT_TIME_ONLY_MIN);
                
                String chh = remark.substring(remark.indexOf("changed") + "changed".length());
                chh = chh.trim();
                
                pso.profile_id = chh;
                
                //System.out.println("Profile changed : " + chh);
                
                // TODO: Changed profile
                //System.out.println("Basal Changed Unknown [remark=" + remark + "]");
                //System.out.println("Basal Rate Changed [datetime=" + pve.getDateTimeObject() + ",remark=" + remark + ",tbrdec=" + tbrdec + ",tbrinc=" + tbrinc + ",value=" + cbrf + "]");
                
                return pso;
            } 
        }
        else
        {
            
            if ((isSet(tbrdec)) || (isSet(tbrinc)))
            {
                return null;
            }
            else
            {
                ProfileSubPattern psp = new ProfileSubPattern();
                psp.dt_start = dt;
                psp.time_start = (int)ATechDate.convertATDate(dt, ATechDate.FORMAT_DATE_AND_TIME_S, ATechDate.FORMAT_TIME_ONLY_MIN);
                
                //System.out.println(psp.time_start);
                
                
                psp.profile_id = profile;
                psp.amount = Float.parseFloat(cbrf); 

                return psp;
                
            }
            
            
            //System.out.println("non TBR");

            //System.out.println("" + el);
            
            /*
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
*/
            //return null;
        }
        return null;
            
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
    
        //return null;
        
    }
    

    /**
     * Resolve Bolus Data
     * 
     * @param pve
     * @param el
     * @return
     */
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
                }
                else if (pve.getSubType()==PumpBolusType.PUMP_BOLUS_MULTIWAVE)
                {
                    if (remark!=null)
                    {
                        String[] str = new String[4];
                        
                        str[0] = remark.substring(0, remark.indexOf(" / "));
                        str[1] = remark.substring(remark.indexOf(" / ")+3);
                        
                        str[2] = str[1].substring(0, str[1].indexOf("  "));
                        str[3] = str[1].substring(str[1].indexOf("  ")+2);
                        
                        str[3] = str[3].substring(0, str[3].indexOf(" h"));
                        
                        pve.setValue(String.format("AMOUNT=%s;AMOUNT_SQUARE=%s;DURATION=%s", 
                                                   str[0], 
                                                   str[2], 
                                                   str[3]));
                    }
                }
                else
                {
                    log.error("AccuChekSmartPixPump: Unknown Bolus Type [" + type +"]");
                }
                
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
            }
            else
            {
                log.error("Unknown Pump Bolus Info [info=" + type + ",desc=" + amount + ",remark=" + remark + "]");
            }
        }
        
        
        return true;
    }    
    
    
    /**
     * Resolve Events
     * 
     * @param pve
     * @param el
     * @return
     */
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
        if ((str==null) || (str.trim().length()==0))
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    

    private List<Node> getSpecificDataChildren(String child_path)
    {
        return getNodes(child_path);   // /BOLUS
    }
    
    
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

    
    /**
     * Get Date from AtechDate long format
     * @param inp
     * @return
     */
    public long getDateFromDT(long inp)
    {
        return (long)(inp/1000000);
    }
    
    
    
    @SuppressWarnings("unused")
    private long getDate(String date)
    {
        String o = m_da.replaceExpression(date, "-", "");
        ATechDate at = new ATechDate(ATechDate.FORMAT_DATE_ONLY, Long.parseLong(o));
        return at.getATDateTimeAsLong();
    }
    
    
    /**
     * Get Connection Protocol
     */
    public int getConnectionProtocol()
    {
        return ConnectionProtocols.PROTOCOL_MASS_STORAGE_XML;
    }
    
    
    
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
        return DownloadSupportType.DOWNLOAD_FROM_DEVICE + DownloadSupportType.DOWNLOAD_FROM_DEVICE_FILE;
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