package ggc.plugin.device.impl.abbott;

import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.protocol.XmlProtocol;
import ggc.plugin.util.DataAccessPlugInBase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.atech.utils.file.FileReaderContext;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
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
 *  Filename:     ----  
 *  Description:  ----
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public abstract class CoPilot extends XmlProtocol implements FileReaderContext
{

    /**
     * @param da
     */
    public CoPilot(DataAccessPlugInBase da)
    {
        super(da);
    }

    /**
     * @param adc 
     * @param da
     */
    public CoPilot(AbstractDeviceCompany adc, DataAccessPlugInBase da)
    {
        super(adc, da);
        // super(da);
    }

    public String getFileDescription()
    {
        return "DM3 Dexcom Software Export";
    }

    /**
     * Get File Download Panel
     * 
     * @return
     */
    public JPanel getFileDownloadPanel()
    {
        return null;
    }

    public String getFileExtension()
    {
        return ".txt";
    }

    public String getFullFileDescription()
    {
        return "DM3 Dexcom Software Export (TXT)";
    }

    public boolean hasSpecialSelectorDialog()
    {
        return false;
    }

    public void readFile(String filename)
    {
        try
        {
            System.out.println("readFile");

            // processXmlPull(filename);

            // removed for now...
            // String new_name = preprocessFile(filename);

            String new_name = "/home/andy/workspace/GGC Desktop/test/CoPilot_Arch.XML_new.xml";

            XMLReader xmlReader = XMLReaderFactory.createXMLReader();

            MyCoPilotHandler myCH = new MyCoPilotHandler(this.dataAccess);
            xmlReader.setProperty("http://xml.org/sax/properties/lexical-handler", myCH);
            xmlReader.setProperty("http://xml.org/sax/properties/declaration-handler", myCH);
            xmlReader.setContentHandler(myCH);
            /*
             * //xmlReader.setErrorHandler(new ErrorHandler(){
             * public void error(SAXParseException exception) throws
             * SAXException
             * {
             * // TODO Auto-generated method stub
             * }
             * public void fatalError(SAXParseException exception) throws
             * SAXException
             * {
             * // TODO Auto-generated method stub
             * }
             * public void warning(SAXParseException exception) throws
             * SAXException
             * {
             * // TODO Auto-generated method stub
             * }
             * });
             */
            xmlReader.parse(new_name);

            // ArrayList<CGMSValuesSubEntry> d = myCH.getData();
            // myCH.finishReading();

            System.out.println("Readings: " + myCH.getCount());
        }
        catch (Exception ex)
        {

        }

    }

    /**
     * Preproces File
     * 
     * @param filename
     * @return
     */
    public String preprocessFile(String filename)
    {
        // String new_name

        try
        {
            File f = new File(filename);

            System.out.println("parent: " + f.getParent());
            System.out.println("name: " + f.getName());

            String new_filename = f.getParent() + File.separator + f.getName() + "_new.xml";

            System.out.println("new name: " + new_filename);

            BufferedReader br = new BufferedReader(new FileReader(filename));

            BufferedWriter bw = new BufferedWriter(new FileWriter(new_filename));

            String line;

            while ((line = br.readLine()) != null)
            {
                line = line.replaceAll("&brvbar", "");
                bw.write(line);
            }

            br.close();
            bw.close();

            System.out.println("File preprocesed !");

            return new_filename;
        }
        catch (Exception ex)
        {
            System.out.println("Problem pre-processing: " + ex);
            return null;
        }

    }

    int i = 0;
    String tmp_time;

    // private XmlStartTag stag;
    // private XmlEndTag etag;
    // private static final boolean USE_SB = true;
    // private StringBuffer buf = new StringBuffer();
    // int [] holderForStartAndLength = new int[2];

    /*
     * private void processXmlPull(String filename)
     * {
     * try
     * {
     * XmlPullParser xpp;
     * xpp = XmlPullParserFactory.newInstance().newPullParser();
     * xpp.setInput(new FileReader(new File(filename)));
     * int token;
     * LOOP:
     * while (true)
     * {
     * //info(""+xpp.getPositionDescription());
     * token = xpp.next();
     * switch (token)
     * {
     * case XmlPullParser.START_TAG:
     * //stag.resetStartTag(); //automatically done when tag is read
     * System.out.println(".");
     * String uri = xpp.getNamespace();
     * String localName = xpp.getName();
     * String prefix = xpp.getPrefix();
     * int len = xpp.getAttributeCount();
     * for (int i=0; i < len; i++) {
     * String n = xpp.getAttributeName(i);
     * // No way to get type
     * // String t = stag.?
     * String t = xpp.getAttributeType(i);
     * String v = xpp.getAttributeValue(i);
     * }
     * if(USE_SB) buf.setLength(0);
     * //stag.resetStartTag();
     * break;
     * case XmlPullParser.END_TAG:
     * if(USE_SB) {String c = buf.toString(); }
     * //xpp.readEndTag(etag);
     * //etag.resetEndTag(); //automaticaly done when tag is read
     * break;
     * case XmlPullParser.TEXT:
     * if(USE_SB) {
     * char[] ch = xpp.getTextCharacters(holderForStartAndLength);
     * int start = holderForStartAndLength[0];
     * int length = holderForStartAndLength[1];
     * buf.append(ch, start, length);
     * } else {
     * String s = xpp.getText();
     * }
     * break;
     * case XmlPullParser.END_DOCUMENT:
     * break LOOP;
     * } // switch
     * } // while
     * }
     * catch(Exception ex)
     * {
     * System.out.println("Ex: " + ex);
     * }
     * }
     */

    /*
     * private void addEntry(CGMSValuesSubEntry entry)
     * {
     * this.list.add(entry);
     * this.cvtm.addEntry(entry);
     * }
     */

    public FileFilter getFileFilter()
    {

        return new FileFilter()
        {

            @Override
            public boolean accept(File f)
            {
                if (f.isDirectory())
                    return true;

                return f.getName().toLowerCase().endsWith(getFileExtension());
            }

            @Override
            public String getDescription()
            {
                return getFileDescription() + " (" + getFileExtension() + ")";
            }

        };

    }

    public void goToNextDialog(JDialog currentDialog)
    {
    }

    @Override
    public String toString()
    {
        return this.getFullFileDescription();
    }

    private class MyCoPilotHandler implements ContentHandler, DeclHandler, LexicalHandler // DefaultHandler,
    {
        long i = 0;

        // private int reading_element = 0;

        // public static final int READING_NOTHING = 0;
        // public static final int READING_SENSOR = 1;
        // public static final int READING_METER = 2;

        // DataAccessPlugInBase dataAccess;

        public MyCoPilotHandler(DataAccessPlugInBase da)
        {
        }

        public void finishReading()
        {
            // this.cvtm.finishReading();
        }

        public long getCount()
        {
            return i;
        }

        /*
         * public ArrayList<> getData()
         * {
         * //return this.list_subs;
         * }
         * public void addEntry(CGMSValuesSubEntry entry)
         * {
         * this.list_subs.add(entry);
         * this.cvtm.addEntry(entry);
         * }
         */

        public void startPrefixMapping(String prefix, String uri) throws SAXException
        {
        }

        public void attributeDecl(String eName, String aName, String type, String mode, String value)
                throws SAXException
        {
        }

        public void elementDecl(String name, String model) throws SAXException
        {
        }

        public void externalEntityDecl(String name, String publicId, String systemId) throws SAXException
        {
        }

        public void internalEntityDecl(String name, String value) throws SAXException
        {
        }

        public void comment(char[] ch, int start, int length) throws SAXException
        {
        }

        public void endCDATA() throws SAXException
        {
        }

        public void endDTD() throws SAXException
        {
        }

        public void endEntity(String name) throws SAXException
        {
        }

        public void startCDATA() throws SAXException
        {
        }

        public void startDTD(String name, String publicId, String systemId) throws SAXException
        {
        }

        public void startEntity(String name) throws SAXException
        {
        }

        public void characters(char[] ch, int start, int length) throws SAXException
        {
        }

        public void endDocument() throws SAXException
        {
        }

        public void endElement(String uri, String localName, String qName) throws SAXException
        {
        }

        public void endPrefixMapping(String prefix) throws SAXException
        {
        }

        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException
        {
        }

        public void processingInstruction(String target, String data) throws SAXException
        {
        }

        // public void setDocumentLocator(Locator locator) { }
        public void skippedEntity(String name) throws SAXException
        {
        }

        public void startDocument() throws SAXException
        {
        }

        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException
        {
            try
            {
                // System.out.println("atts: " + atts);
                if (localName.equals("ROW"))
                {

                    System.out.println(atts.getValue("DEVICE_MODEL"));
                    // CGMSValuesSubEntry sub = new CGMSValuesSubEntry();
                    // String tmp_time = atts.getValue(0);
                    // sub.setDateTime(DexcomCGMS.getDateTimeFromString(tmp_time));
                    // sub.type = CGMSValuesSubEntry.CGMS_BG_READING;
                    // sub.value = Integer.parseInt(atts.getValue(2));

                    // this.addEntry(sub;
                    i++;
                }

            }
            catch (Exception ex)
            {
                System.out.println("Ex: " + ex);
            }

            i++;

        }

        public void setDocumentLocator(Locator locator)
        {

        }

    }

    public void dispose()
    {
        // TODO Auto-generated method stub

    }

    public String getConnectionPort()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public int getConnectionProtocol()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getDownloadSupportType()
    {
        return 0;
    }

    @Override
    public GGCPlugInFileReaderContext[] getFileDownloadTypes()
    {
        return null;
    }

    @Override
    public boolean hasIndeterminateProgressStatus()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean hasSpecialProgressStatus()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isDeviceCommunicating()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isFileDownloadSupported()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isReadableDevice()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void readConfiguration() throws PlugInBaseException
    {
    }

    public void readDeviceDataFull() throws PlugInBaseException
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void readDeviceDataPartitial() throws PlugInBaseException
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void readInfo() throws PlugInBaseException
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void test()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public long getItemId()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * public FileFilter getFileFilter()
     * {
     * return new FileFilter()
     * {
     * @Override
     * public boolean accept(File f)
     * {
     * if (f.isDirectory())
     * return true;
     * return (f.getName().toLowerCase().endsWith(getFileExtension()));
     * }
     * @Override
     * public String getDescription()
     * {
     * return getFileDescription() + " (*" + getFileExtension() + ")";
     * }
     * };
     * }
     */

}
