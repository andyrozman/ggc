
package ggc.meter.device.accuchek;

import ggc.meter.data.MeterValuesEntry;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputUtil;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.ConnectionProtocols;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import com.atech.utils.ATechDate;

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


public abstract class AccuChekSmartPixMeter extends AccuChekSmartPix //extends AbstractXmlMeter //mlProtocol //implements SelectableInterface
{
    
    //DataAccessMeter m_da = DataAccessMeter.getInstance();
    //OutputWriter output_writer = null;

    
    /**
     * Meter Company: Roche
     */
    public static final int ROCHE_COMPANY             = 2;
    
    /**
     * Meter Device: SmartPix
     */
    public static final int METER_ROCHE_SMARTPIX_DEVICE   = 20001;
    
    /**
     * Meter Device: Accu Chek Active
     */
    public static final int METER_ACCUCHEK_ACTIVE         = 20002;
    
    /**
     * Meter Device: Accu Chek Advantage
     */
    public static final int METER_ACCUCHEK_ADVANTAGE      = 20003;
    
    /**
     * Meter Device: Accu Chek Aviva
     */
    public static final int METER_ACCUCHEK_AVIVA          = 20004;
    
    /**
     * Meter Device: Accu Chek Comfort
     */
    public static final int METER_ACCUCHEK_COMFORT        = 20005;
    
    /**
     * Meter Device: Accu Chek Compact
     */
    public static final int METER_ACCUCHEK_COMPACT        = 20006;
    
    /**
     * Meter Device: Accu Chek Compact Plus
     */
    public static final int METER_ACCUCHEK_COMPACT_PLUS   = 20007;
    
    /**
     * Meter Device: Accu Chek Go
     */
    public static final int METER_ACCUCHEK_GO             = 20008;
    
    /**
     * Meter Device: Accu Chek Integra
     */
    public static final int METER_ACCUCHEK_INTEGRA        = 20009;
    
    /**
     * Meter Device: Accu Chek Performa
     */
    public static final int METER_ACCUCHEK_PERFORMA       = 20010;
    
    /**
     * Meter Device: Accu Chek Sensor
     */
    public static final int METER_ACCUCHEK_SENSOR         = 20011;
    
    
    private int bg_unit = OutputUtil.BG_MGDL;

    
    /**
     * Constructor
     */
    public AccuChekSmartPixMeter()
    {
        super();
    }
    
    
    /**
     * Constructor
     * 
     * @param cmp
     */
    public AccuChekSmartPixMeter(AbstractDeviceCompany cmp)
    {
        this.setDeviceCompany(cmp);
    }
    
    
    /**
     * Constructor
     * 
     * @param drive_letter
     * @param writer
     */
    public AccuChekSmartPixMeter(String drive_letter, OutputWriter writer)
    {
        super(drive_letter, writer); 
    }
    
    
    /** 
     * open
     */
    public boolean open() throws PlugInBaseException
    {
        return true;
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


    /** 
     * Process Xml
     */
    public void processXml(File file)
    {
        try
        {
            /*Document doc =*/ openXmlFile(file);
            
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

    
    private void getMeterDeviceInfo()
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
    
    
    private void readData()
    {
        //this.output_writer.
        
        List<Node> nodes = getNodes("IMPORT/BGDATA/BG");
        ArrayList<MeterValuesEntry> lst = new ArrayList<MeterValuesEntry>();
        
        for(int i=0; i<nodes.size(); i++)
        {
            lst.add(getDataEntry(nodes.get(i)));
        }
        
    }
    
    
    
    private MeterValuesEntry getDataEntry(Node entry)
    {
        Element el = (Element)entry;
        
        MeterValuesEntry mve = new MeterValuesEntry();
        //ATechDate at = null;
        mve.setDateTime(new ATechDate(this.getDateTime(el.attributeValue("Dt"), el.attributeValue("Tm"))));
        mve.setBgUnit(this.bg_unit);
        mve.setBgValue(el.attributeValue("Val"));
        
        // <BG Val="5.1" Dt="2005-06-07" Tm="18:01" D="1"/>

        //System.out.println(mve);
        
        this.output_writer.writeData(mve);
        //.writeBGData(mve);
        
        return mve;
        
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
    
    
    
    /** 
     * Get Connection Protocol
     */
    public int getConnectionProtocol()
    {
        return ConnectionProtocols.PROTOCOL_MASS_STORAGE_XML;
    }
    
    
}
