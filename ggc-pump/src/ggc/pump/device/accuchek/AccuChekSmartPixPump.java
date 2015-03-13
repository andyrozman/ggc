package ggc.pump.device.accuchek;

import ggc.plugin.device.DeviceAbstract;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.impl.accuchek.AccuChekSmartPix;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import com.atech.utils.ATDataAccessAbstract;
import com.atech.utils.data.ATechDate;

/**
 * Application: GGC - GNU Gluco Control Plug-in: Pump Tool (support for Pump
 * devices)
 *
 * See AUTHORS for copyright information.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Filename: AccuChekSmartPixPump Description: Accu-Chek SmartPix Processor for
 * Pumps
 * 
 * Author: Andy {andy@atech-software.com}
 */

public abstract class AccuChekSmartPixPump extends AccuChekSmartPix implements PumpInterface // extends
// AbstractXmlMeter
// //mlProtocol
// //implements
// SelectableInterface
{

    private static Log log = LogFactory.getLog(AccuChekSmartPixPump.class);

    private Hashtable<String, PumpAlarms> alarm_mappings = null;
    private Hashtable<String, PumpEvents> event_mappings = null;
    private Hashtable<String, PumpErrors> error_mappings = null;
    private Hashtable<String, PumpBolusType> bolus_mappings = null;
    private Hashtable<String, PumpReport> report_mappings = null;
    private Hashtable<String, PumpBasalSubType> basal_mappings = null;

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
    @Override
    public void processXml(File file)
    {
        try
        {
            this.openXmlFile(file);

            getPixDeviceInfo();
            getPumpDeviceInfo();

            this.outputWriter.writeDeviceIdentification();

            readPumpData();
        }
        catch (Exception ex)
        {
            System.out.println("Exception on testXml: " + ex);
            ex.printStackTrace();

        }
    }

    /**
     * Letter with which report starts (I for insulin pumps, G for glucose
     * meters)
     * 
     * @return
     */
    @Override
    public String getFirstLetterForReport()
    {
        return "I";
    }

    /**
     * Get Pix Device Info
     */
    private void getPixDeviceInfo()
    {
        DeviceIdentification di = this.outputWriter.getDeviceIdentification();

        Node nd = getNode("IMPORT/ACSPIX");

        StringBuffer sb = new StringBuffer();

        Element e = (Element) nd;

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
        DeviceIdentification di = this.outputWriter.getDeviceIdentification();

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

        // log.info(" -- Basals --");
        list.addAll(getBasals());
        // System.out.println("Basals: " + list.size());

        // log.info(" -- Boluses --");
        list.addAll(getBoluses());
        // System.out.println("Boluses: " + list.size());

        // log.info(" -- Events --");
        list.addAll(getEvents());
        // System.out.println("Events: " + list.size());

        // log.info(" -- Profiles --");
        // list.addAll(this.getPumpProfiles());
        ArrayList<PumpValuesEntryProfile> list_profiles = this.getPumpProfiles();

        /*
         * System.out.println(" -- Basals (run 2) --");
         * list.addAll(getSpecificElements2("BASAL"));
         * System.out.println("Basals2: " + list.size());
         */
        if (first_basal != null)
        {
            list.add(first_basal);
        }

        for (int i = 0; i < list.size(); i++)
        {
            this.outputWriter.writeData(list.get(i));
        }

        for (int i = 0; i < list_profiles.size(); i++)
        {
            this.outputWriter.writeData(list_profiles.get(i));
        }

    }

    /**
     * Main method for reading Profile Pattern/Event entries
     */
    @SuppressWarnings("unchecked")
    private ArrayList<PumpValuesEntryProfile> getPumpProfiles()
    {
        List<Node> nodelist;

        // boolean was_broken = true;
        log.info("Profile processing - START");

        Element id = (Element) this.getNode("IMPORT/IP");
        // System.out.println("" + id.attributeValue("Dt") +
        // id.attributeValue("Tm"));
        ATechDate dt = this.getDateTime(id.attributeValue("Dt"), id.attributeValue("Tm"));

        // 1. read initial
        log.info("STEP 1 - Read inital profiles");
        Hashtable<String, Profile> original_profiles = new Hashtable<String, Profile>();

        nodelist = getSpecificDataChildren("IMPORT/IP/IPPROFILE");

        // System.out.println("Length: "+ nodelist.size());

        for (int i = 0; i < nodelist.size(); i++)
        {
            Element n = (Element) nodelist.get(i);

            Profile profile = new Profile();
            profile.profile_id = n.attributeValue("Name");

            List<Node> nlist2 = n.elements();

            for (int j = 0; j < nlist2.size(); j++)
            {
                Element n2 = (Element) nlist2.get(j);

                ProfileSubPattern psp = new ProfileSubPattern();
                int num = Integer.parseInt(n2.attributeValue("Number"));

                psp.timeStart = (num - 1) * 100;
                psp.timeEnd = ((num - 1) * 100) + 59;
                psp.amount = Float.parseFloat(n2.attributeValue("IU"));

                profile.add(psp);
            }

            profile.packProfiles();
            profile.isCompleteProfile();

            original_profiles.put(profile.profile_id, profile);
        }

        // 2. read all basal entries (we don't sort them now, we just put them
        // all together, also events)
        log.info("STEP 2 - Reading profile data");

        List<Node> lst = getSpecificDataChildren("IMPORT/IPDATA/BASAL");

        Hashtable<Long, Profile> profiles_raw = new Hashtable<Long, Profile>();

        for (int i = 0; i < lst.size(); i++)
        {
            Element el = (Element) lst.get(i);

            ProfileSubEntry pse = this.resolveBasalProfilePatterns(el);

            if ((pse == null) || (pse.profileId == null))
            {
                continue;
            }

            long dt_current = this.getDateFromDT(this.getDateTime(el.attributeValue("Dt"), el.attributeValue("Tm"))
                    .getATDateTimeAsLong());

            if (!profiles_raw.containsKey(dt_current))
            {
                Profile p = new Profile();
                p.date_at = dt_current;
                p.profile_id = pse.profileId;
                p.add(pse);

                profiles_raw.put(dt_current, p);
            }
            else
            {
                profiles_raw.get(dt_current).add(pse);
            }
        }

        // 3. sort profile_entries into different profiles and patterns
        log.info("STEP 3 - Remove incompletes and group profiles by name");

        // remove incomplete profiles, and group profiles by name

        Hashtable<String, ArrayList<Profile>> profiles_sorted = new Hashtable<String, ArrayList<Profile>>();
        ArrayList<ProfileSubOther> profile_changes = new ArrayList<ProfileSubOther>();

        for (Enumeration<Long> en = profiles_raw.keys(); en.hasMoreElements();)
        {
            Long key = en.nextElement();
            Profile profile = profiles_raw.get(key);
            profile.fillEndTimes();

            profile_changes.addAll(profile.other_entries);

            if (profile.isCompleteProfile())
            {
                // System.out.println("Profile id: " + profile.profile_id);
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

        Hashtable<Long, ArrayList<ProfileSubOther>> profile_changes_v2 = new Hashtable<Long, ArrayList<ProfileSubOther>>();

        for (int i = 0; i < profile_changes.size(); i++)
        {
            ProfileSubOther pchan = profile_changes.get(i);
            long date = ATechDate.convertATDate(pchan.time_event, ATechDate.FORMAT_DATE_AND_TIME_S,
                ATechDate.FORMAT_DATE_ONLY);

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

        Hashtable<Long, ProfileSubOther> profile_changes_v3 = new Hashtable<Long, ProfileSubOther>();

        for (Enumeration<Long> en = profile_changes_v2.keys(); en.hasMoreElements();)
        {

            long key = en.nextElement();

            if (profile_changes_v2.get(key).size() == 1)
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
        Hashtable<String, Profile> current_profiles = new Hashtable<String, Profile>();

        for (Enumeration<String> en = profiles_sorted.keys(); en.hasMoreElements();)
        {
            String key = en.nextElement();
            current_profiles.put(key, original_profiles.get(key));
        }

        dataAccess.setSortSetting("Profile", "DESC");

        for (Enumeration<String> en = current_profiles.keys(); en.hasMoreElements();)
        {
            String key = en.nextElement();
            ArrayList<Profile> profiles = profiles_sorted.get(key);

            Collections.sort(profiles);

            for (int i = 0; i < profiles.size(); i++)
            {
                Profile p_curr = profiles.get(i);

                if (i == 0)
                {
                    if (p_curr.date_at == ATechDate.convertATDate(dt.getATDateTimeAsLong(),
                        ATechDate.FORMAT_DATE_AND_TIME_S, ATechDate.FORMAT_DATE_ONLY))
                    {
                        continue;
                    }
                }

                if (current_profiles.get(p_curr.profile_id).equals(p_curr))
                {
                    current_profiles.get(p_curr.profile_id).profile_active_from = p_curr.date_at * 1000000;
                }
                else
                {
                    active_profiles.add(current_profiles.get(p_curr.profile_id));
                    current_profiles.remove(p_curr.profile_id);
                    current_profiles.put(p_curr.profile_id, p_curr);
                    current_profiles.get(p_curr.profile_id).profile_active_till = (p_curr.date_at * 1000000) + 235900;
                    current_profiles.get(p_curr.profile_id).profile_active_from = p_curr.date_at * 1000000;
                }
            }
        }

        // 6 - Profile changes
        log.info("STEP 6 - Check profiles (dates) and save (export) data");

        for (int j = 0; j < active_profiles.size(); j++)
        {
            Profile p_curr = active_profiles.get(j);

            long date = ATechDate.convertATDate(p_curr.profile_active_till, ATechDate.FORMAT_DATE_AND_TIME_S,
                ATechDate.FORMAT_DATE_ONLY);

            if (profile_changes_v3.containsKey(date))
            {
                ProfileSubOther chan = profile_changes_v3.get(date);
                p_curr.profile_active_from = chan.time_event;

                if (j != (active_profiles.size() - 1))
                {
                    ATechDate aat = new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, chan.time_event);
                    aat.add(Calendar.MINUTE, -1);
                    int k = j + 1;
                    active_profiles.get(k).profile_active_till = aat.getATDateTimeAsLong();
                    j++;
                }
            }
        }

        // 7 - Profile export
        log.info("STEP 7 - Profile export");

        ArrayList<PumpValuesEntryProfile> pvep = new ArrayList<PumpValuesEntryProfile>();

        for (int i = 0; i < active_profiles.size(); i++)
        {
            Profile p = active_profiles.get(i);

            // p.profile_active_from =
            // ATechDate.convertATDate(p.profile_active_from,
            // ATechDate.FORMAT_DATE_AND_TIME_S,
            // ATechDate.FORMAT_DATE_AND_TIME_MIN);
            // p.profile_active_till =
            // ATechDate.convertATDate(p.profile_active_till,
            // ATechDate.FORMAT_DATE_AND_TIME_S,
            // ATechDate.FORMAT_DATE_AND_TIME_MIN);

            pvep.add(p.createDbObject());
        }

        log.info("Profile processing - END");

        return pvep;

    }

    /*
     * private void readPumpDataTest() { ArrayList<PumpValuesEntry> list = new
     * ArrayList<PumpValuesEntry>(); //System.out.println(" -- Basals --");
     * //list.addAll(getBasals()); list.addAll(getProfileElements()); if
     * (first_basal !=null) list.add(first_basal); }
     */

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
    @Override
    public void test()
    {
        // readPumpDataTest();
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

        for (int i = 0; i < lst.size(); i++)
        {
            Element el = (Element) lst.get(i);

            PumpValuesEntry pve = new PumpValuesEntry(this.getDeviceSourceName());
            pve.setDateTimeObject(this.getDateTime(el.attributeValue("Dt"), el.attributeValue("Tm")));

            add = false;

            if (type == AccuChekSmartPixPump.TAG_BASAL)
            {
                add = this.resolveBasalBase(pve, el);
            }
            else if (type == AccuChekSmartPixPump.TAG_EVENT)
            {
                add = this.resolveEvent(pve, el);
            }
            else if (type == AccuChekSmartPixPump.TAG_BOLUS)
            {
                add = this.resolveBolus(pve, el);
            }

            if (add)
            {
                // testing only
                // this.outputWriter.writeData(pve);

                lst_out.add(pve);
            }
        }

        log.info("Process " + type_desc[type] + " data - END");

        return lst_out;

    }

    /*
     * private ArrayList<PumpValuesEntry> getSpecificElements2(String element) {
     * List<Node> lst = getSpecificDataChildren("IMPORT/IPDATA/" + element);
     * ArrayList<PumpValuesEntry> lst_out = new ArrayList<PumpValuesEntry>();
     * boolean add = false; Hashtable<Long,Profile> lst_spec = new
     * Hashtable<Long,Profile>(); for(int i=0; i<lst.size(); i++) { Element el =
     * (Element)lst.get(i); PumpValuesEntry pve = new
     * PumpValuesEntry(this.getDeviceSourceName());
     * pve.setDateTimeObject(this.getDateTime(el.attributeValue("Dt"),
     * el.attributeValue("Tm"))); add = this.resolveBasalProfile(pve, el); if
     * (add) { // testing only this.outputWriter.writeData(pve);
     * lst_out.add(pve); } } System.out.println("Profiles: " + lst_out.size());
     * return lst_out; }
     */

    /*
     * private ArrayList<PumpValuesEntry> getProfileElements() { List<Node> lst
     * = getSpecificDataChildren("IMPORT/IPDATA/BASAL"); // + element);
     * ArrayList<PumpValuesEntry> lst_out = new ArrayList<PumpValuesEntry>();
     * boolean add = false; for(int i=0; i<lst.size(); i++) { Element el =
     * (Element)lst.get(i); PumpValuesEntry pve = new
     * PumpValuesEntry(this.getDeviceSourceName());
     * pve.setDateTimeObject(this.getDateTime(el.attributeValue("Dt"),
     * el.attributeValue("Tm"))); // add = this.resolveBasalProfilePatterns(pve,
     * el); if (add) { // testing only // this.outputWriter.writeData(pve);
     * lst_out.add(pve); } } System.out.println("Profiles patterns: " +
     * lst_out.size()); return lst_out; }
     */

    private static String PUMP_STOP = "Stop";

    private boolean resolveBasalBase(PumpValuesEntry pve, Element el)
    {
        String remark = el.attributeValue("remark");
        String tbrdec = el.attributeValue("TBRdec");
        String tbrinc = el.attributeValue("TBRinc");
        String cbrf = el.attributeValue("cbrf");
        String profile = el.attributeValue("profile");

        // System.out.println(el);

        if (isSet(tbrdec) || isSet(tbrinc))
        {
            if ((remark == null) || (remark.trim().length() == 0))
            {
                return false;
            }

            // System.out.println(el);

            pve.setBaseType(PumpBaseType.Basal);
            pve.setSubType(PumpBasalSubType.TemporaryBasalRate.getCode());

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

            if (PUMP_STOP.equals(remark))
            {
                pve.setSubType(PumpBasalSubType.TemporaryBasalRateCanceled.getCode());
            }
            else
            {
                String[] dr = remark.split(" ");

                pve.setValue(String.format("DURATION=%s;VALUE=%s", //
                    dr[1], //
                    v));
            }

            // System.out.println("Unknown TBR Event. [remark=" + remark +
            // ",tbrdec=" + tbrdec + ",tbrinc=" + tbrinc + ",value=" + cbrf +
            // "]");

            return true;
        }
        else if (isSet(remark))
        {
            // all that are special should be removed, all other are checked
            // over events
            if (remark.contains("changed"))
            {
                // System.out.println("Basal Changed Unknown [remark=" + remark
                // + "]");
                return false;
            }
            else
            {
                if (this.getEventMappings().containsKey(remark))
                {
                    pve.setBaseType(PumpBaseType.Event.getCode());
                    pve.setSubType(this.getEventMappings().get(remark).getCode());
                    // System.out.println("Basal Event Unknown [remark=" +
                    // remark + "]");
                    return true;
                }
                else if (this.getBasalMappings().containsKey(remark))
                {
                    pve.setBaseType(PumpBaseType.Basal.getCode());
                    pve.setSubType(this.getBasalMappings().get(remark).getCode());
                    // System.out.println("Basal Event Unknown [remark=" +
                    // remark + "]");
                    return true;

                }
                else
                {
                    if (!remark.contains(" - "))
                    {
                        System.out.println("Unknown Basal Event. [remark=" + remark + ",tbrdec=" + tbrdec + ",tbrinc="
                                + tbrinc + ",value=" + cbrf + "]");
                    }

                    return false;
                }
            }

            // System.out.println("Remark:" + remark);
        }
        else
        {
            if (!isSet(profile))
            {
                if (!isSet(tbrdec) && !isSet(tbrinc) && cbrf.equals("0.00"))
                {
                }
                else
                {
                    log.error("Basal Unknown [tbrdec=" + tbrdec + ",tbrinc=" + tbrinc + ",value=" + cbrf + "]");
                }
            }
            return false;

        }

    }

    PumpValuesEntry first_basal = null;

    private ProfileSubEntry resolveBasalProfilePatterns(/* PumpValuesEntry pve, */Element el)
    {
        // pve.setDateTimeObject(this.getDateTime(el.attributeValue("Dt"),
        // el.attributeValue("Tm")));

        long dt = this.getDateTime(el.attributeValue("Dt"), el.attributeValue("Tm")).getATDateTimeAsLong();

        String remark = el.attributeValue("remark");
        String tbrdec = el.attributeValue("TBRdec");
        String tbrinc = el.attributeValue("TBRinc");
        String cbrf = el.attributeValue("cbrf");
        String profile = el.attributeValue("profile");

        /*
         * if ((isSet(tbrdec)) || (isSet(tbrinc))) {
         * //System.out.println("TBR"); //System.out.println("" + el); return
         * null; } else
         */

        if (isSet(remark))
        {
            // all that are special should be removed, all other are checked
            // over events
            if (remark.contains("changed"))
            {
                ProfileSubOther pso = new ProfileSubOther();

                pso.time_event = dt;
                pso.event_type = ProfileSubOther.EVENT_PATTERN_CHANGED;
                pso.profileId = profile;
                pso.timeStart = (int) ATechDate.convertATDate(dt, ATechDate.FORMAT_DATE_AND_TIME_S,
                    ATechDate.FORMAT_TIME_ONLY_MIN);

                String chh = remark.substring(remark.indexOf("changed") + "changed".length());
                chh = chh.trim();

                pso.profileId = chh;

                // System.out.println("Profile changed : " + chh);

                // TODO: Changed profile
                // System.out.println("Basal Changed Unknown [remark=" + remark
                // + "]");
                // System.out.println("Basal Rate Changed [datetime=" +
                // pve.getDateTimeObject() + ",remark=" + remark + ",tbrdec=" +
                // tbrdec + ",tbrinc=" + tbrinc + ",value=" + cbrf + "]");

                return pso;
            }
        }
        else
        {

            if (isSet(tbrdec) || isSet(tbrinc))
            {
                return null;
            }
            else
            {
                ProfileSubPattern psp = new ProfileSubPattern();
                psp.dt_start = dt;
                psp.timeStart = (int) ATechDate.convertATDate(dt, ATechDate.FORMAT_DATE_AND_TIME_S,
                    ATechDate.FORMAT_TIME_ONLY_MIN);

                // System.out.println(psp.time_start);

                psp.profileId = profile;
                psp.amount = Float.parseFloat(cbrf);

                return psp;

            }

            // System.out.println("non TBR");

            // System.out.println("" + el);

            /*
             * if (isSet(remark)) { if ((!remark.contains("TBR")) &&
             * (!remark.contains("Run")) && (!remark.contains("Stop")) &&
             * (!remark.contains("power")) ) {
             * System.out.println(pve.getDateTimeObject().toString() + ",value="
             * + cbrf + ", profile=" + profile+",remark=" + remark); } } else
             * System.out.println(pve.getDateTimeObject().toString() + ",value="
             * + cbrf + ", profile=" + profile);
             */
            // return null;
        }
        return null;

        /*
         * // all that are special should be removed, all other are checked over
         * events if (remark.contains("changed")) { // TODO: Changed profile //
         * System.out.println("Basal Changed Unknown [remark=" + remark + "]");
         * //System.out.println("Basal Rate Changed [datetime=" +
         * pve.getDateTimeObject() + ",remark=" + remark + ",tbrdec=" + tbrdec +
         * ",tbrinc=" + tbrinc + ",value=" + cbrf + "]"); return false; } else {
         * if ((this.getEventMappings().containsKey(remark)) ||
         * (this.getBasalMappings().containsKey(remark))) { return false; } else
         * { if (remark.contains(" - ")) {
         * pve.setBaseType(PumpBaseType.PUMP_DATA_BASAL);
         * pve.setSubType(PumpBasalSubType.PUMP_BASAL_PROFILE);
         * pve.setValue(remark.substring(remark.indexOf(" - ")+ 3)); //
         * System.out.println("Profile changed: " +
         * remark.substring(remark.indexOf(" - ")+ 3)); //
         * System.out.println("Unknown Profile Event. [remark=" + remark +
         * ",tbrdec=" + tbrdec + ",tbrinc=" + tbrinc + ",value=" + cbrf + "]");
         * return true; } else return false; } } } else { if (!isSet(profile)) {
         * //System.out.println("tbrdec=" + isSet(tbrdec) + "tbrinc=" +
         * isSet(tbrinc)); if ((!isSet(tbrdec)) && (!isSet(tbrinc)) &&
         * (cbrf.equals("0.00"))) { //System.out.println("tbrdec=" +
         * isSet(tbrdec) + "tbrinc=" + isSet(tbrinc)); return false; } else {
         * log.error("Basal Unknown [tbrdec=" + tbrdec + ",tbrinc=" + tbrinc +
         * ",value=" + cbrf + "]"); return false; } } else { // profile used
         * pve.setBaseType(PumpBaseType.PUMP_DATA_BASAL);
         * pve.setSubType(PumpBasalSubType.PUMP_BASAL_PROFILE);
         * pve.setValue(profile); first_basal = pve;
         * //log.error("Basal Unknown [tbrdec=" + tbrdec + ",tbrinc=" + tbrinc +
         * ",value=" + cbrf + "]"); if
         * (pve.getDateTimeObject().getTimeString().equals("00:00:00")) {
         * //System.out.println("Profile used: " + pve.getValue()); first_basal
         * = null; return true; } else return false; } }
         */

        // return null;

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

        if (remark != null)
        {
            remark = remark.trim();
        }

        if (isSet(type))
        {
            if (this.getBolusMappings().containsKey(type))
            {
                pve.setBaseType(PumpBaseType.Bolus);

                PumpBolusType bolusType = this.getBolusMappings().get(type);

                pve.setSubType(bolusType.getCode());

                if ((bolusType == PumpBolusType.Normal)
                        || (bolusType == PumpBolusType.Audio))
                {
                    pve.setValue(amount);
                }
                else if (bolusType == PumpBolusType.Extended)
                {
                    String e = remark.substring(0, remark.indexOf(" h"));
                    pve.setValue("AMOUNT_SQUARE=" + amount + ";DURATION=" + e);
                }
                else if (bolusType == PumpBolusType.Multiwave)
                {
                    if (remark != null)
                    {
                        String[] str = new String[4];

                        str[0] = remark.substring(0, remark.indexOf(" / "));
                        str[1] = remark.substring(remark.indexOf(" / ") + 3);

                        str[2] = str[1].substring(0, str[1].indexOf("  "));
                        str[3] = str[1].substring(str[1].indexOf("  ") + 2);

                        str[3] = str[3].substring(0, str[3].indexOf(" h"));

                        pve.setValue(String.format("AMOUNT=%s;AMOUNT_SQUARE=%s;DURATION=%s", str[0], str[2], str[3]));
                    }
                }
                else
                {
                    log.error("AccuChekSmartPixPump: Unknown Bolus Type [" + type + "]");
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
                pve.setBaseType(PumpBaseType.Report.getCode());
                pve.setSubType(this.getReportMappings().get(remark).getCode());
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
                    pve.setBaseType(PumpBaseType.Alarm);
                    pve.setSubType(this.getAlarmMappings().get(info).getCode());
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
                    pve.setBaseType(PumpBaseType.Error.getCode());
                    pve.setSubType(this.getErrorMappings().get(info).getCode());
                }
                else
                {
                    log.error("Unknown Pump Error [info=" + info + ",desc=" + desc + "]");
                }

            }
            else if (info.startsWith("W"))
            {
                if (this.getEventMappings().containsKey(info))
                {
                    pve.setBaseType(PumpBaseType.Event.getCode());
                    pve.setSubType(this.getEventMappings().get(info).getCode());
                    pve.setValue(info);
                }
                else
                {
                    log.error("Unknown Pump Warning [info=" + info + ",desc=" + desc + "]");
                }
            }
            else
            {

                if (this.getEventMappings().containsKey(desc))
                {
                    pve.setBaseType(PumpBaseType.Event.getCode());
                    pve.setSubType(this.getEventMappings().get(desc).getCode());
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
                pve.setBaseType(PumpBaseType.Event.getCode());
                pve.setSubType(this.getEventMappings().get(desc).getCode());
            }
            else
            {
                log.error("Unknown Pump Event [info=" + info + ",desc=" + desc + "]");
            }
        }

        // pve.setEntryType(PumpDataType.PUMP_DATA_EVENT);

        return true;

    }

    private boolean isSet(String str)
    {
        if ((str == null) || (str.trim().length() == 0))
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
        return getNodes(child_path); // /BOLUS
    }

    /**
     * Pump tool, requires dates to be in seconds, so we need to return value is
     * second, eventhough pix returns it in minutes.
     * 
     * @param date
     * @param time
     * @return
     */
    private ATechDate getDateTime(String date, String time)
    {
        String o = ATDataAccessAbstract.replaceExpression(date, "-", "");

        if ((time == null) || (time.length() == 0))
        {
            o += "0000";
        }
        else
        {
            o += ATDataAccessAbstract.replaceExpression(time, ":", "");
        }

        o += "00"; // seconds

        return new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, Long.parseLong(o));
    }

    /**
     * Get Date from AtechDate long format
     * 
     * @param inp
     * @return
     */
    public long getDateFromDT(long inp)
    {
        return inp / 1000000;
    }

    @SuppressWarnings("unused")
    private long getDate(String date)
    {
        String o = ATDataAccessAbstract.replaceExpression(date, "-", "");
        ATechDate at = new ATechDate(ATechDate.FORMAT_DATE_ONLY, Long.parseLong(o));
        return at.getATDateTimeAsLong();
    }



    /**
     * loadPumpSpecificValues - should be called from constructor of any
     * AbstractPump classes and should create, AlarmMappings and EventMappings
     * and any other pump constants.
     */
    public void loadPumpSpecificValues()
    {
        // dataAccess = DataAccessPump.getInstance();

        // alarm mappings
        this.alarm_mappings = new Hashtable<String, PumpAlarms>();
        this.alarm_mappings.put("A1", PumpAlarms.CartridgeLow);
        this.alarm_mappings.put("A2", PumpAlarms.BatteryLow);
        this.alarm_mappings.put("A3", PumpAlarms.ReviewDatetime);
        this.alarm_mappings.put("A4", PumpAlarms.AlarmClock);
        this.alarm_mappings.put("A5", PumpAlarms.PumpTimer);
        this.alarm_mappings.put("A6", PumpAlarms.TemporaryBasalRateCanceled);
        this.alarm_mappings.put("A7", PumpAlarms.TemporaryBasalRateOver);
        this.alarm_mappings.put("A8", PumpAlarms.BolusCanceled);

        this.event_mappings = new Hashtable<String, PumpEvents>();
        this.event_mappings.put("prime infusion set", PumpEvents.PrimeInfusionSet);
        this.event_mappings.put("cartridge changed", PumpEvents.CartridgeChange);
        this.event_mappings.put("Run", PumpEvents.BasalRun);
        this.event_mappings.put("Stop", PumpEvents.BasalStop);
        this.event_mappings.put("power down", PumpEvents.PowerDown);
        this.event_mappings.put("power up", PumpEvents.PowerUp);
        this.event_mappings.put("time / date set", PumpEvents.DateTimeSet);
        this.event_mappings.put("time / date corrected", PumpEvents.DateTimeCorrect);
        this.event_mappings.put("time / date set (time shift back)", PumpEvents.DateTimeCorrect);
        this.event_mappings.put("W1", PumpEvents.ReservoirLow);
        this.event_mappings.put("W8", PumpEvents.BolusCancelled);
        this.event_mappings.put("W2", PumpEvents.BatteryLow);

        this.error_mappings = new Hashtable<String, PumpErrors>();
        this.error_mappings.put("E1", PumpErrors.CartridgeEmpty);
        this.error_mappings.put("E2", PumpErrors.BatteryDepleted);
        this.error_mappings.put("E3", PumpErrors.AutomaticOff);
        this.error_mappings.put("E4", PumpErrors.NoDeliveryOcclusion);
        this.error_mappings.put("E5", PumpErrors.EndOfOperation);
        this.error_mappings.put("E6", PumpErrors.MechanicalError);
        this.error_mappings.put("E7", PumpErrors.ElectronicError);
        this.error_mappings.put("E8", PumpErrors.PowerInterrupt);
        this.error_mappings.put("E10", PumpErrors.CartridgeError);
        this.error_mappings.put("E11", PumpErrors.SetNotPrimed);
        this.error_mappings.put("E12", PumpErrors.DataInterrupted);
        this.error_mappings.put("E13", PumpErrors.LanguageError);
        this.error_mappings.put("E14", PumpErrors.InsulinChanged);

        this.bolus_mappings = new Hashtable<String, PumpBolusType>();
        this.bolus_mappings.put("Std", PumpBolusType.Normal);
        this.bolus_mappings.put("Scr", PumpBolusType.Audio);
        this.bolus_mappings.put("Ext", PumpBolusType.Extended);
        this.bolus_mappings.put("Mul", PumpBolusType.Multiwave);

        // report
        this.report_mappings = new Hashtable<String, PumpReport>();
        this.report_mappings.put("Bolus Total", PumpReport.BolusTotalDay);
        this.report_mappings.put("Bolus+Basal Total", PumpReport.InsulinTotalDay);

        this.basal_mappings = new Hashtable<String, PumpBasalSubType>();
        this.basal_mappings.put("TBR End (cancelled)", PumpBasalSubType.TemporaryBasalRateCanceled);
        this.basal_mappings.put("TBR End", PumpBasalSubType.TemporaryBasalRateEnded);

    }


    /**
     * getImplementationStatus - Get Implementation Status
     *
     * @return implementation status as number
     * @see ggc.plugin.manager.DeviceImplementationStatus
     */
    public DeviceImplementationStatus getImplementationStatus()
    {
        return DeviceImplementationStatus.Done;
    }


    /**
     * Map pump specific alarms to PumpTool specific alarm codes
     * 
     * @return
     */
    public Hashtable<String, PumpAlarms> getAlarmMappings()
    {
        return this.alarm_mappings;
    }

    /**
     * Map pump specific events to PumpTool specific event codes
     * 
     * @return
     */
    public Hashtable<String, PumpEvents> getEventMappings()
    {
        return this.event_mappings;
    }

    /**
     * Get Error Mappings - Map pump specific errors to Pump Tool specific event
     * codes
     * 
     * @return
     */
    public Hashtable<String, PumpErrors> getErrorMappings()
    {
        return this.error_mappings;
    }

    /**
     * Get Bolus Mappings - Map pump specific bolus to Pump Tool specific event
     * codes
     * 
     * @return
     */
    public Hashtable<String, PumpBolusType> getBolusMappings()
    {
        return this.bolus_mappings;
    }

    /**
     * Get Basal Mappings - Map pump specific basal to Pump Tool specific event
     * codes
     * 
     * @return
     */
    public Hashtable<String, PumpBasalSubType> getBasalMappings()
    {
        return this.basal_mappings;
    }

    /**
     * Get Report Mappings - Map pump specific reports to Pump Tool specific
     * event codes
     * 
     * @return
     */
    public Hashtable<String, PumpReport> getReportMappings()
    {
        return this.report_mappings;
    }

    /**
     * Get Download Support Type
     * 
     * @return
     */
    @Override
    public DownloadSupportType getDownloadSupportType()
    {
        return DownloadSupportType.Download_Data_DataFile;
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
