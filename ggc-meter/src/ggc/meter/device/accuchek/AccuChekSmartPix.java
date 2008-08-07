
package ggc.meter.device.accuchek;

import ggc.meter.data.MeterValuesEntry;
import ggc.meter.device.AbstractXmlMeter;
import ggc.meter.device.DeviceIdentification;
import ggc.meter.device.MeterException;
import ggc.meter.manager.MeterImplementationStatus;
import ggc.meter.output.OutputUtil;
import ggc.meter.output.OutputWriter;
import ggc.meter.protocol.ConnectionProtocols;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.atech.utils.ATechDate;

public abstract class AccuChekSmartPix extends AbstractXmlMeter //mlProtocol //implements SelectableInterface
{
    
    //DataAccessMeter m_da = DataAccessMeter.getInstance();
    
    //AbstractMeterCompany meter_company = null;
    //String drive_letter;
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
    
    
    
    public AccuChekSmartPix()
    {
    }

    
    public AccuChekSmartPix(String drive_letter, OutputWriter writer)
    {
        this.setConnectionPort(drive_letter);
        this.output_writer = writer; 
        this.output_writer.getOutputUtil().setMaxMemoryRecords(this.getMaxMemoryRecords());
        
        this.setMeterType("Roche", this.getName());
        
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
        return "SmartPix";
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
        return AccuChekSmartPix.METER_ROCHE_SMARTPIX_DEVICE;
    }

    

    
    /**
     * getCompanyId - Get Company Id 
     * 
     * @return id of company
     */
    public int getCompanyId()
    {
        return AccuChekSmartPix.ROCHE_COMPANY;
    }
    
    
    /**
     * getInstructions - get instructions for device
     * 
     * @return instructions for reading data 
     */
/*    public String getInstructions()
    {
        return "INSTRUCTIONS_ACCUCHEK_SMART_PIX";
    }
  */
    
    
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
     * getImplementationStatus - Get Implementation Status 
     * 
     * @return implementation status as number
     * @see ggc.meter.manager.MeterImplementationStatus
     */
    public int getImplementationStatus() 
    {
        return MeterImplementationStatus.IMPLEMENTATION_TESTING;
    }
    
    
    /**
     * getDeviceClassName - Get Class name of device implementation, used by Reflection at later time
     * 
     * @return class name as string
     */
    public String getDeviceClassName()
    {
        return "ggc.meter.device.accuchek.AccuChekSmartPix";
    }
    
    
    /**
     * getDeviceSpecialComment - special comment for device (this is needed in case that we need to display
     *    special comment about device (for example pix device, doesn't display anything till the end, which
     *    would be nice if user knew. 
     */
    public String getDeviceSpecialComment()
    {
        return "DEVICE_PIX_SPECIAL_COMMENT";
    }
    
    
    
    
    
    









    /* 
     * readDeviceDataFull
     */
    public void readDeviceDataFull() throws MeterException
    {
        // write preliminary device identification, based on class
        DeviceIdentification di = this.output_writer.getDeviceIdentification();
        
        di.company = "Accu-Chek";
        di.device_selected = "SmartPix Device Reader";
        
        di.device_identified = "Accu-Chek " + this.getName() + " [not identified]";

        this.output_writer.writeDeviceIdentification();
        
        // start working
        String drv = this.getConnectionPort();
        String cmd = drv + "\\TRG\\";

        this.writeStatus("PIX_ABORT_AUTOSCAN");
        
        //System.out.println("Abort auto scan");
        
        this.output_writer.setSpecialProgress(5);
        
        // abort auto scan
        File f = new File(cmd + "TRG09.PNG");
        f.setLastModified(System.currentTimeMillis());
        
        f = new File(cmd + "TRG03.PNG");
        f.setLastModified(System.currentTimeMillis());
        

        //this.writeStatus("PIX_READING");
        this.output_writer.setSpecialProgress(10);
        
        // read device  
        f = new File(cmd + "TRG09.PNG");
        f.setLastModified(System.currentTimeMillis());
        
        f = new File(cmd + "TRG00.PNG");
        f.setLastModified(System.currentTimeMillis());
        
        boolean found = false;
        sleep(2000);
        
        int count_el = 0;
        
        
        do
        {

            if (this.isDeviceStopped())
            {
                this.setDeviceStopped();
                found = true;
            }
            
            int st = readStatusFromConfig(drv);
            
            if (st==1)
            {
                this.writeStatus("PIX_UNRECOVERABLE_ERROR");
                this.output_writer.setSpecialProgress(100);

                //System.out.println("Unrecoverable error - Aborting");
                return;
            }
            else if (st==2)
            {
                
                this.writeStatus("PIX_FINISHED_READING");
                this.output_writer.setSpecialProgress(90);

                //System.out.println("Finished reading");
                return;
            }
            else if (st==4)
            {
                count_el += this.getNrOfElementsFor1s();
                //System.out.println("Reading elements: " + count_el);
                
                
                float procs_x = (count_el*(1.0f))/this.getMaxMemoryRecords();
                
                //int procs = (int)(procs_x * 100.0f);
                
                //System.out.println("Procents full: " + procs);
                //float procs_calc = 0.007f * procs;
                
                int pro_calc = (int)((0.2f + (0.007f * (procs_x * 100.0f)))*100.0f);
                
                //System.out.println("Procents: " + pro_calc);
                
                //this.writeStatus(String.format("PIX_READING_ELEMENT", pro_calc + " %"));
                this.writeStatus("PIX_READING_ELEMENT"); //, pro_calc + " %"));
                this.output_writer.setSpecialProgress(pro_calc);

            }
            else if (st==20)
            {
                this.writeStatus("PIX_DEVICE_NOT_FOUND");
                this.output_writer.setSpecialProgress(100);

                //System.out.println("Unrecoverable error - Aborting");
                return;
            }
            else if (st>99)
            {
                if (st==101)
                {
                    this.writeStatus("PIX_FINISHED_REPORT_READY");
                    //System.out.println("Finished reading. Report ready." );
                    this.output_writer.setSpecialProgress(95);

                    
                    File f1 = new File(drv + "\\REPORT\\XML");
                    
                    File[] fls = f1.listFiles(new FileFilter()
                    {

                        public boolean accept(File file)
                        {
                            return ((file.getName().toUpperCase().contains(".XML")) &&
                                    (file.getName().startsWith("G")));
                        }}
                    );
                    
                    
                    processXml(fls[0]);

                    this.output_writer.setSpecialProgress(100);
                    this.output_writer.setSubStatus(null);
                    
                    return;
                    
                }
                else
                {
                    this.writeStatus("PIX_FINISHED_REPORT_READY");
                    this.output_writer.setSpecialProgress(95);

                    return;
                }
            }
                
            
            sleep(1000);
            
            
        } while(found!=true);
        
        this.setDeviceStopped();
        //this.output_writer.setSubStatus(null);
        
        //System.out.println("We got out !!!!");
    }
    
    
    
    
/*
    public String getConnectionPort()
    {
        return "XML";
    }
  */  
    

    /* 
     * test
     */
    public void test()
    {
        // TODO Auto-generated method stub
        
    }
    
    
    
    
    /**
     * This is method for reading partitial data from device. All reading from actual device should be done from 
     * here. Reading can be done directly here, or event can be used to read data.
     */
    public void readDeviceDataPartitial() throws MeterException
    {
    }


    /** 
     * This is method for reading configuration
     * 
     * @throws MeterExceptions
     */
    public void readConfiguration() throws MeterException
    {
    }
    

    /**
     * This is for reading device information. This should be used only if normal dump doesn't retrieve this
     * information (most dumps do). 
     * @throws MeterExceptions
     */
    public void readInfo() throws MeterException
    {
    }
    
    
    
    
    
    private boolean isDeviceStopped()
    {
        if (this.output_writer.isReadingStopped())
            return true;
        
        return false;
        
    }
    
    
    private void setDeviceStopped()
    {
        this.output_writer.setSubStatus(null);
        this.output_writer.setSpecialProgress(100);
        this.output_writer.endOutput();
    }
    
    
    private void writeStatus(String text_i18n)
    {
        writeStatus(text_i18n, true);
    }
    
    
    private void writeStatus(String text_i18n, boolean process)
    {
        String tx = "";
        
        if (process)
            tx = ic.getMessage(text_i18n);
        else
            tx = text_i18n;
        
        this.output_writer.setSubStatus(tx);
//x        System.out.println(tx);
        // write log
        
    }
    

    /**
     * getNrOfElementsFor1s - How many elements are read in 1s (which is our refresh time)
     * @return number of elements
     */
    public abstract int getNrOfElementsFor1s();

    
    
    
    private void sleep(int ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch(Exception ex)
        {
            
        }
        
    }

    private boolean device_found = false;
    
    
    // 0 = no status
    // 1 = error_found
    // 2 = finished
    private int readStatusFromConfig(String drive)
    {
        try
        {
            //boolean error_found = false;
            //boolean image_found = false;
            
            
            BufferedReader br = new BufferedReader(new FileReader(new File(drive + "\\REPORT\\SCAN.HTM")));
            
            String line = "";
            
            boolean reports[] = { false, false, false };
            int rep_count = 0;
            
            
            while ((line = br.readLine())!= null)
            {
                
                if (line.contains("Error.htm"))
                {
                    return 1;
                }
                else if (line.contains("img/"))
                {
                    //System.out.println("Image: " + line);
                    if (line.contains("Scanning.gif"))
                    {
                        this.writeStatus("PIX_SCANNING");
                        this.output_writer.setSpecialProgress(15);
                        //System.out.println("Scanning for device");
                        return 0;
                    }
                    else if (line.contains("CrReport.png"))
                    {
                        this.writeStatus("PIX_CREATING_REPORT");
                        this.output_writer.setSpecialProgress(90);

                        //System.out.println("Finished reading - Creating report");
                    }
                    else if (line.contains("rd_"))
                    {
                        device_found = true;
                        //System.out.println("Reading from meter.");
                        return 4;
                    }
                    else
                    {
                        System.out.println("Unknown image: " + line);
                    }
                    
                    
                    return 0;
                }
                else if (line.contains("ReportPresent "))
                {
                    //System.out.println("L: " + line);
                    
                    if (line.contains("parent.BgReportPresent = "))
                    {
                        reports[0] = getBooleanStatus(line);
                        rep_count++;
                    }
                    else if (line.contains("parent.IpReportPresent = "))
                    {
                        reports[1] = getBooleanStatus(line);
                        rep_count++;
                    }
                    else if (line.contains("parent.MgReportPresent = "))
                    {
                        reports[2] = getBooleanStatus(line);
                        rep_count++;
                    }
                    
                    if (rep_count==3)
                    {
                        int rs = 0;
                        
                        if (reports[0])
                            rs += 1;
                        else if (reports[1])
                            rs += 2;
                        else if (reports[2])
                            rs += 4;

                        //System.out.println("Rs: " + rs);
                        
                        if (this.device_found)
                        {
                            return (100 + rs);
                        }
                        else
                            return 20;
                    }
                    
                }
                
            }
            
            //return 2;
            
            return 0;    
        }
        catch(Exception ex)
        {
            System.out.println("Exception: " + ex);
            return 1;
        }
       
        
        
        
    }
    
    
    private boolean getBooleanStatus(String text)
    {
        String val = text.substring(text.indexOf("=")+2, text.indexOf(";"));
        
        try
        {
            //System.out.println("val: '" + val + "'");
            boolean b = Boolean.parseBoolean(val);
            //System.out.println("b: " + b);
            return b;
        }
        catch(Exception ex)
        {
            System.out.println("Error with status.\n" + text);
            return false;
        }
        
        
        
    }
    
    
    
    
    
    
    
    
    
/*    
    public int getNrOfElementsFor1s()
    {
        return 0;
    }
  */  
    
    
    
    
    /**
     * hasSpecialProgressStatus - in most cases we read data directly from device, in this case we have 
     *    normal progress status, but with some special devices we calculate progress through other means.
     * @return true is progress status is special
     */
    public boolean hasSpecialProgressStatus()
    {
        return true;
    }
    
    
    
    
    
    
    
    Document document;
    
    public Document parse(File file) throws DocumentException {
        SAXReader reader = new SAXReader();
        
        
        //Document 
        document = reader.read(file);
        return document;
    }
    
    
    private int bg_unit = OutputUtil.BG_MGDL;
    
    
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
