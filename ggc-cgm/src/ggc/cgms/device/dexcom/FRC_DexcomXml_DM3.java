package ggc.cgms.device.dexcom;

import ggc.cgms.data.CGMSValuesSubEntry;
import ggc.cgms.data.CGMSValuesTableModel;
import ggc.plugin.protocol.XmlProtocol;
import ggc.plugin.util.DataAccessPlugInBase;

import java.util.ArrayList;

import javax.swing.JPanel;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.atech.utils.file.FileReaderContext;

public class FRC_DexcomXml_DM3 extends XmlProtocol implements FileReaderContext
{

    //CGMSValuesTableModel cvtm = null;
    
    public FRC_DexcomXml_DM3(DataAccessPlugInBase da)
    {
        super(da);
    }

    public String getFileDescription() 
    {
        // TODO Auto-generated method stub
        return null;
    }

    public JPanel getFileDownloadPanel()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getFileExtension()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean hasSpecialSelectorDialog()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public void readFile(String filename)
    {
        try
        {
            System.out.println("readFile");
            
            XMLReader xmlReader = XMLReaderFactory.createXMLReader();

            MyContentHandler myCH = new MyContentHandler(this.m_da);
            xmlReader.setProperty("http://xml.org/sax/properties/lexical-handler",myCH);
            xmlReader.setProperty("http://xml.org/sax/properties/declaration-handler",myCH);
            xmlReader.setContentHandler(myCH);
            xmlReader.parse(filename);            
            
            ArrayList<CGMSValuesSubEntry> d = myCH.getData();
            myCH.finishReading();
             
            System.out.println("Readings: " + d.size());
        }
        catch(Exception ex)
        {
            
        }

    }
    
    private class MyContentHandler implements  ContentHandler, DeclHandler, LexicalHandler  //DefaultHandler,
    {
        int i=0;
        
        //private int reading_element = 0;
        
        
        //public static final int READING_NOTHING = 0;
        //public static final int READING_SENSOR = 1;
        //public static final int READING_METER = 2;
        
        private ArrayList<CGMSValuesSubEntry> list_subs = new ArrayList<CGMSValuesSubEntry>(); 
        CGMSValuesTableModel cvtm = null;
        //DataAccessPlugInBase m_da;
        
        public MyContentHandler(DataAccessPlugInBase da)
        {
            cvtm = (CGMSValuesTableModel)da.getDeviceDataHandler().getDeviceValuesTableModel();
        }

        
        public void finishReading()
        {
            this.cvtm.finishReading();
        }
        
        
        
        public ArrayList<CGMSValuesSubEntry> getData()
        {
            return this.list_subs;
        }
        
        
        public void addEntry(CGMSValuesSubEntry entry)
        {
            this.list_subs.add(entry);
            this.cvtm.addEntry(entry); 
        }
        
        
        public void startPrefixMapping(String prefix, String uri) throws SAXException { }
        public void attributeDecl(String eName, String aName, String type, String mode, String value) throws SAXException { } 
        public void elementDecl(String name, String model) throws SAXException { }
        public void externalEntityDecl(String name, String publicId, String systemId) throws SAXException { }
        public void internalEntityDecl(String name, String value) throws SAXException { }
        public void comment(char[] ch, int start, int length) throws SAXException { }
        public void endCDATA() throws SAXException { }
        public void endDTD() throws SAXException { }
        public void endEntity(String name) throws SAXException { }
        public void startCDATA() throws SAXException  { }
        public void startDTD(String name, String publicId, String systemId) throws SAXException { }
        public void startEntity(String name) throws SAXException { }
        public void characters(char[] ch, int start, int length) throws SAXException { }
        public void endDocument() throws SAXException { }
        public void endElement(String uri, String localName, String qName) throws SAXException { }
        public void endPrefixMapping(String prefix) throws SAXException { }        
        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException { }
        public void processingInstruction(String target, String data) throws SAXException { }
        public void setDocumentLocator(Locator locator) { }
        public void skippedEntity(String name) throws SAXException { }
        public void startDocument() throws SAXException { }

        
        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException
        {
            
            if (localName.equals("Sensor"))
            {
                CGMSValuesSubEntry sub = new CGMSValuesSubEntry();
                String tmp_time = atts.getValue(0);
                sub.setDateTime(DexcomCGMS.getDateFromString(tmp_time));
                sub.type = CGMSValuesSubEntry.CGMS_BG_READING;
                sub.value = Integer.parseInt(atts.getValue(2));
  
                this.addEntry(sub);
            }
            else if (localName.equals("Meter"))
            {
                CGMSValuesSubEntry sub = new CGMSValuesSubEntry();
                String tmp_time = atts.getValue(0);
                sub.setDateTime(DexcomCGMS.getDateFromString(tmp_time));
                sub.type = CGMSValuesSubEntry.METER_CALIBRATION_READING;
                sub.value = Integer.parseInt(atts.getValue(2));

                this.addEntry(sub);
            }
            //else
            //    this.reading_element = this.READING_NOTHING;
            
            
            i++;
            
        }

    }
    

}