package ggc.meter.device.accuchek;

import ggc.meter.data.MeterValuesEntry;
import ggc.meter.data.MeterValuesEntrySpecial;
import ggc.meter.device.MeterInterface;
import ggc.meter.manager.MeterDevicesIds;
import ggc.meter.util.DataAccessMeter;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.impl.accuchek.AccuChekSmartPix;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputUtil;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.ConnectionProtocols;
import ggc.plugin.util.DataAccessPlugInBase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import com.atech.utils.ATDataAccessAbstract;
import com.atech.utils.data.ATechDate;
import com.atech.utils.data.TimeZoneUtil;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Meter Tool (support for Meter devices)
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
 *  Filename:     AccuChekSmartPixMeter
 *  Description:  Super class for all AccuChek Meter devices supported through SmartPix 
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public abstract class AccuChekSmartPixMeter extends AccuChekSmartPix implements MeterInterface // extends
                                                                                               // AbstractXmlMeter
                                                                                               // //mlProtocol
                                                                                               // //implements
                                                                                               // SelectableInterface
{

    // DataAccessMeter dataAccess = DataAccessMeter.getInstance();
    // OutputWriter outputWriter = null;

    private int bg_unit = OutputUtil.BG_MGDL;

    protected TimeZoneUtil tzu = TimeZoneUtil.getInstance();

    /**
     * Constructor
     * 
     * @param cmp
     */
    public AccuChekSmartPixMeter(AbstractDeviceCompany cmp)
    {
        super(cmp, DataAccessMeter.getInstance());
        this.setMeterType(cmp.getName(), getName());
    }

    /**
     * Constructor
     * 
     * @param conn_parameter
     * @param writer
     */
    public AccuChekSmartPixMeter(String conn_parameter, OutputWriter writer)
    {
        this(conn_parameter, writer, DataAccessMeter.getInstance());
    }

    /**
     * Constructor
     * 
     * @param conn_parameter
     * @param writer
     * @param m_da 
     */
    public AccuChekSmartPixMeter(String conn_parameter, OutputWriter writer, DataAccessPlugInBase m_da)
    {
        super(conn_parameter, writer, m_da);
        this.setMeterType("Accu-Chek/Roche", getName());
    }

    /**
     * getMeterId - Get Meter Id, within Meter Company class 
     * 
     * @return id of meter within company
     */
    public int getMeterId()
    {
        return MeterDevicesIds.METER_ROCHE_SMARTPIX_DEVICE;
    }

    /**
     * getCompanyId - Get Company Id 
     * 
     * @return id of company
     */
    public int getCompanyId()
    {
        return MeterDevicesIds.COMPANY_ROCHE;
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
            /* Document doc = */openXmlFile(file);

            getPixDeviceInfo();
            System.out.println();

            getMeterDeviceInfo();
            System.out.println();

            this.outputWriter.writeDeviceIdentification();

            readData();

        }
        catch (Exception ex)
        {
            System.out.println("Exception on testXml: " + ex);
            ex.printStackTrace();

        }
    }

    private void getPixDeviceInfo()
    {
        DeviceIdentification di = this.outputWriter.getDeviceIdentification();

        Node nd = getNode("IMPORT/ACSPIX");
        System.out.println(nd);

        StringBuffer sb = new StringBuffer();

        Element e = (Element) nd;

        String s = "Accu-Chek Smart Pix Device [" + e.attributeValue("Type") + "]";
        sb.append(s + "\n");

        di.company = s;

        StringBuilder sb2 = new StringBuilder();

        sb2.append(i18nControlAbstract.getMessage("VERSION") + " v" + e.attributeValue("Ver"));
        sb2.append(" [S/N=" + e.attributeValue("SN") + "]");

        di.device_selected = sb2.toString();

        sb.append(di.device_selected);

        // System.out.println(sb.toString());
        // List nodes = getNodes("ACSPIX");
        // System.out.println(nodes);

    }

    /**
     * Letter with which report starts (I for insulin pumps, G for glucose meters)
     * 
     * @return
     */
    @Override
    public String getFirstLetterForReport()
    {
        return "G";
    }

    private void getMeterDeviceInfo()
    {
        DeviceIdentification di = this.outputWriter.getDeviceIdentification();

        Element el = getElement("IMPORT/DEVICE");
        // System.out.println(nd);

        StringBuffer sb = new StringBuffer();
        sb.append(i18nControlAbstract.getMessage("DEVICE_DEVICE") + ": Accu-Chek " + el.attributeValue("Name"));
        sb.append("\nS/N=" + el.attributeValue("SN") + ", " + i18nControlAbstract.getMessage("BG_UNIT") + ": ");
        sb.append(el.attributeValue("BGUnit"));
        sb.append(", " + i18nControlAbstract.getMessage("TIME_ON_DEVICE") + ": " + el.attributeValue("Tm") + " "
                + el.attributeValue("Dt"));

        di.device_identified = sb.toString();

        // System.out.println(sb.toString());

        if (el.attributeValue("BGUnit").equals("mmol/L"))
        {
            this.bg_unit = OutputUtil.BG_MMOL;
        }

        // <DEVICE Name="Performa" SN="50003006"
        // Dt="2008-05-13" Tm="10:12" BGUnit="mmol/L"/>

    }

    private void readData()
    {
        // this.outputWriter.

        List<Node> nodes = getNodes("IMPORT/BGDATA/BG");
        ArrayList<MeterValuesEntry> lst = new ArrayList<MeterValuesEntry>();

        for (int i = 0; i < nodes.size(); i++)
        {
            lst.addAll(getDataEntries(nodes.get(i)));
        }

    }

    private ArrayList<MeterValuesEntry> getDataEntries(Node entry)
    {
        Element el = (Element) entry;
        boolean just_ch = false;

        ArrayList<MeterValuesEntry> list = new ArrayList<MeterValuesEntry>();

        MeterValuesEntry mve = getEmptyEntry(el);

        if (el.attributeValue("Val").toUpperCase().startsWith("HI"))
        {
            mve.setBgValue("" + 600);
        }
        else if (el.attributeValue("Val").toUpperCase().startsWith("LO"))
        {
            mve.setBgValue("" + 10);
        }
        else if (el.attributeValue("Val").toUpperCase().startsWith("---"))
        {
            just_ch = true;
        }
        else
        {
            mve.setBgValue(el.attributeValue("Val"));
        }

        if (!just_ch)
        {
            this.outputWriter.writeData(mve);
            list.add(mve);
        }

        // check for presence of ch
        String element_attribute = el.attributeValue("Carb");

        if (element_attribute != null)
        {
            mve = getEmptyEntry(el);
            mve.addSpecialEntry(MeterValuesEntrySpecial.SPECIAL_ENTRY_CH, element_attribute);

            this.outputWriter.writeData(mve);
            list.add(mve);
        }

        return list;

        // dt_info > 201012100000 AND
        // dt_info < 201012102359
        //

    }

    private MeterValuesEntry getEmptyEntry(Element el)
    {
        MeterValuesEntry mve = new MeterValuesEntry();
        mve.setDateTimeObject(tzu.getCorrectedDateTime(new ATechDate(this.getDateTime(el.attributeValue("Dt"),
            el.attributeValue("Tm")))));
        mve.setBgUnit(this.bg_unit);

        return mve;
    }

    @SuppressWarnings("unused")
    private MeterValuesEntry getDataEntry(Node entry)
    {
        Element el = (Element) entry;

        MeterValuesEntry mve = new MeterValuesEntry();
        // ATechDate at = null;
        mve.setDateTimeObject(tzu.getCorrectedDateTime(new ATechDate(this.getDateTime(el.attributeValue("Dt"),
            el.attributeValue("Tm")))));
        mve.setBgUnit(this.bg_unit);

        if (el.attributeValue("Val").toUpperCase().startsWith("HI"))
        {
            mve.setBgValue("" + 600);
        }
        else if (el.attributeValue("Val").toUpperCase().startsWith("LO"))
        {
            mve.setBgValue("" + 10);
        }
        else if (el.attributeValue("Val").toUpperCase().startsWith("---"))
        {

        }
        else
        {
            mve.setBgValue(el.attributeValue("Val"));
        }

        // <BG Val="5.1" Dt="2005-06-07" Tm="18:01" D="1"/>
        /*
         * <BG Val="8.4" Dt="2010-12-10" Tm="16:35" Carb="20" D="1"/>
         * <BG Val="8.3" Dt="2010-12-10" Tm="12:07" Carb="130" D="1"/>
         * <BG Val="10.9" Dt="2010-12-10" Tm="09:34" D="1"/>
         * <BG Val="---" Dt="2010-12-09" Tm="22:57" Carb="45" D="1"/>
         * <BG Val="10.6" Dt="2010-12-09" Tm="20:25" Carb="60" D="1"/>
         * <BG Val="---" Dt="2010-12-09" Tm="17:49" Carb="60" D="1"/>
         * <BG Val="8.9" Dt="2010-12-09" Tm="14:42" Carb="132" D="1"/>
         * <BG Val="---" Dt="2010-12-09" Tm="00:03" Carb="30" D="1"/>
         * <BG Val="---" Dt="2010-12-08" Tm="23:18" Carb="40" D="1"/>
         * <BG Val="---" Dt="2010-12-08" Tm="19:20" Carb="45" D="1"/>
         * <BG Val="---" Dt="2010-12-08" Tm="18:52" Carb="45" D="1"/>
         */

        String element_attribute = el.attributeValue("Carb");

        if (element_attribute != null)
        {
            mve.addSpecialEntry(MeterValuesEntrySpecial.SPECIAL_ENTRY_CH, element_attribute);
        }

        // System.out.println(mve);

        this.outputWriter.writeData(mve);
        // .writeBGData(mve);

        return mve;

    }

    private long getDateTime(String date, String time)
    {
        String o = ATDataAccessAbstract.replaceExpression(date, "-", "");

        if (time == null)
        {
            o += "0000";
        }
        else
        {
            o += ATDataAccessAbstract.replaceExpression(time, ":", "");
        }

        return Long.parseLong(o);

    }

    /** 
     * Get Connection Protocol
     */
    @Override
    public int getConnectionProtocol()
    {
        return ConnectionProtocols.PROTOCOL_MASS_STORAGE_XML;
    }

    /**
     * Get Download Support Type
     * 
     * @return
     */
    @Override
    public int getDownloadSupportType()
    {
        return DownloadSupportType.DOWNLOAD_FROM_DEVICE + DownloadSupportType.DOWNLOAD_FROM_DEVICE_FILE;
    }

    /**
     * getInterfaceTypeForMeter - most meter devices, store just BG data, this use simple interface, but 
     *    there are some device which can store different kind of data (Ketones - Optium Xceed; Food, Insulin
     *    ... - OT Smart, etc), this devices require more extended data display. 
     * @return
     */
    public int getInterfaceTypeForMeter()
    {
        return MeterInterface.METER_INTERFACE_SIMPLE;
    }

    /**
     * Set Meter type
     * 
     * @param group
     * @param device
     */
    public void setMeterType(String group, String device)
    {
        // this.device_name = device;

        DeviceIdentification di = new DeviceIdentification(dataAccess.getI18nControlInstance());
        di.company = group;
        di.device_selected = device;

        if (this.outputWriter != null)
        {
            this.outputWriter.setDeviceIdentification(di);
            // this.outputWriter.
            // this.device_instance =
            // MeterManager.getInstance().getMeterDevice(group, device);
        }

        this.deviceSourceName = group + " " + device;
    }

}
