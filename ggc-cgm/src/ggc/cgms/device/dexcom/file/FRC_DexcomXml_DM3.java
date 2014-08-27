package ggc.cgms.device.dexcom.file;

import ggc.cgms.data.CGMSValuesSubEntry;
import ggc.cgms.data.CGMSValuesTableModel;
import ggc.cgms.device.dexcom.DexcomCGMS;
import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.XmlProtocolFile;
import ggc.plugin.util.DataAccessPlugInBase;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Application: GGC - GNU Gluco Control
 * Plug-in: CGMS Tool (support for Pump devices)
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
 * Filename: Dexcom 7 Plus
 * Description: Dexcom 7 Plus implementation (just settings)
 *
 * Author: Andy {andy@atech-software.com}
 */

public class FRC_DexcomXml_DM3 extends XmlProtocolFile implements GGCPlugInFileReaderContext {

    private static Log log = LogFactory.getLog(FRC_DexcomXml_DM3.class);


    /**
     * Constructor
     *
     * @param da
     * @param ow
     */
    public FRC_DexcomXml_DM3(DataAccessPlugInBase da, OutputWriter ow) {
        super(da, ow);
    }


    public String getFileDescription() {
        return "DM3 Dexcom Software Export";
    }


    public String getFullFileDescription() {
        return "DM3 Dexcom Software Export (XML)";
    }


    /**
     * Get File Download Panel
     *
     * @return
     */
    public JPanel getFileDownloadPanel() {
        return null;
    }


    public String getFileExtension() {
        return ".xml";
    }


    public boolean hasSpecialSelectorDialog() {
        return false;
    }


    public void readFile(String filename) {
        try {
            DeviceIdentification di = this.output_writer.getDeviceIdentification();
            di.is_file_import = true;
            di.fi_file_name = new File(filename).getName();
            di.company = this.m_da.getSelectedDeviceInstance().getDeviceCompany().getName();
            di.device_selected = this.m_da.getSelectedDeviceInstance().getName();

            this.output_writer.setDeviceIdentification(di);
            this.output_writer.writeDeviceIdentification();

            XMLReader xmlReader = XMLReaderFactory.createXMLReader();

            MyContentHandler myCH = new MyContentHandler(this.m_da);
            xmlReader.setProperty("http://xml.org/sax/properties/lexical-handler", myCH);
            xmlReader.setProperty("http://xml.org/sax/properties/declaration-handler", myCH);
            xmlReader.setContentHandler(myCH);
            xmlReader.parse(filename);

            // ArrayList<CGMSValuesSubEntry> d = myCH.getData();
            myCH.finishReading();

            // System.out.println("Readings: " + d.size());
        } catch (Exception ex) {
            log.error("Exception on readFile. Ex: " + ex, ex);
        }

    }


    public String toString() {
        return this.getFullFileDescription();
    }

    private class MyContentHandler implements ContentHandler, DeclHandler, LexicalHandler // DefaultHandler,
    {

        // int i=0;

        // private int reading_element = 0;

        // public static final int READING_NOTHING = 0;
        // public static final int READING_SENSOR = 1;
        // public static final int READING_METER = 2;

        private ArrayList<CGMSValuesSubEntry> list_subs = new ArrayList<CGMSValuesSubEntry>();
        CGMSValuesTableModel cvtm = null;


        // DataAccessPlugInBase m_da;

        public MyContentHandler(DataAccessPlugInBase da) {
            cvtm = (CGMSValuesTableModel)da.getDeviceDataHandler().getDeviceValuesTableModel();
        }


        public void finishReading() {
            this.cvtm.finishReading();
        }


        /*
         * public ArrayList<CGMSValuesSubEntry> getData()
         * {
         * return this.list_subs;
         * }
         */

        public void addEntry(CGMSValuesSubEntry entry) {
            this.list_subs.add(entry);
            this.cvtm.addEntry(entry);
        }


        public void startPrefixMapping(String prefix, String uri) throws SAXException {
        }


        public void attributeDecl(String eName, String aName, String type, String mode, String value)
                throws SAXException {
        }


        public void elementDecl(String name, String model) throws SAXException {
        }


        public void externalEntityDecl(String name, String publicId, String systemId) throws SAXException {
        }


        public void internalEntityDecl(String name, String value) throws SAXException {
        }


        public void comment(char[] ch, int start, int length) throws SAXException {
        }


        public void endCDATA() throws SAXException {
        }


        public void endDTD() throws SAXException {
        }


        public void endEntity(String name) throws SAXException {
        }


        public void startCDATA() throws SAXException {
        }


        public void startDTD(String name, String publicId, String systemId) throws SAXException {
        }


        public void startEntity(String name) throws SAXException {
        }


        public void characters(char[] ch, int start, int length) throws SAXException {
        }


        public void endDocument() throws SAXException {
        }


        public void endElement(String uri, String localName, String qName) throws SAXException {
        }


        public void endPrefixMapping(String prefix) throws SAXException {
        }


        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        }


        public void processingInstruction(String target, String data) throws SAXException {
        }


        public void setDocumentLocator(Locator locator) {
        }


        public void skippedEntity(String name) throws SAXException {
        }


        public void startDocument() throws SAXException {
        }


        public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {

            if (localName.equals("Sensor")) {
                CGMSValuesSubEntry sub = new CGMSValuesSubEntry();
                String tmp_time = atts.getValue(0);
                sub.setDateTime(DexcomCGMS.getDateTimeFromString(tmp_time));
                sub.type = CGMSValuesSubEntry.CGMS_BG_READING;
                sub.value = Integer.parseInt(atts.getValue(2));

                this.addEntry(sub);
            } else if (localName.equals("Meter")) {
                CGMSValuesSubEntry sub = new CGMSValuesSubEntry();
                String tmp_time = atts.getValue(0);
                sub.setDateTime(DexcomCGMS.getDateTimeFromString(tmp_time));
                sub.type = CGMSValuesSubEntry.METER_CALIBRATION_READING;
                sub.value = Integer.parseInt(atts.getValue(2));

                this.addEntry(sub);
            }
            // else
            // this.reading_element = this.READING_NOTHING;

            // i++;

        }

    }


    public FileFilter getFileFilter() {

        return new FileFilter() {

            @Override
            public boolean accept(File f) {
                if (f.isDirectory())
                    return true;

                return (f.getName().toLowerCase().endsWith(getFileExtension()));
            }


            @Override
            public String getDescription() {
                return getFileDescription() + " (*" + getFileExtension() + ")";
            }

        };

    }


    public void goToNextDialog(JDialog currentDialog) {
    }


    public void setOutputWriter(OutputWriter ow) {
        this.output_writer = ow;
    }

}
