package ggc.cgms.device.dexcom.file;

import ggc.cgms.data.CGMSValuesSubEntry;
import ggc.cgms.data.CGMSValuesTableModel;
import ggc.cgms.device.dexcom.DexcomCGMS;
import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.XmlProtocolFile;
import ggc.plugin.util.DataAccessPlugInBase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

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

public class FRC_DexcomTxt_DM3 extends XmlProtocolFile implements GGCPlugInFileReaderContext {

    ArrayList<CGMSValuesSubEntry> list = new ArrayList<CGMSValuesSubEntry>();
    CGMSValuesTableModel cvtm = null;


    /**
     * Constructor
     *
     * @param da
     * @param ow
     */
    public FRC_DexcomTxt_DM3(DataAccessPlugInBase da, OutputWriter ow) {
        super(da, ow);
    }


    public String getFileDescription() {
        return "DM3 Dexcom Software Export";
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
        return ".txt";
    }


    public String getFullFileDescription() {
        return "DM3 Dexcom Software Export (TXT)";
    }


    public boolean hasSpecialSelectorDialog() {
        return false;
    }


    public void readFile(String filename) {
        BufferedReader br = null;
        try {

            DeviceIdentification di = this.output_writer.getDeviceIdentification();
            di.is_file_import = true;
            di.fi_file_name = new File(filename).getName();
            di.company = this.m_da.getSelectedDeviceInstance().getDeviceCompany().getName();
            di.device_selected = this.m_da.getSelectedDeviceInstance().getName();

            this.output_writer.setDeviceIdentification(di);
            this.output_writer.writeDeviceIdentification();

            cvtm = (CGMSValuesTableModel)m_da.getDeviceDataHandler().getDeviceValuesTableModel();

            br = new BufferedReader(new FileReader(filename));

            String line = null;

            while ((line = br.readLine()) != null) {
                processLine(line);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }
        // System.out.println("i = " + i + " lisr: " + list.size());

    }

    int i = 0;
    String tmp_time;


    private void addEntry(CGMSValuesSubEntry entry) {
        this.list.add(entry);
        this.cvtm.addEntry(entry);
    }


    private void processLine(String line) {

        // if (i>20)
        // return;

        StringTokenizer strtok = new StringTokenizer(line, "\t");

        int count = strtok.countTokens();

        int read_type = 0;

        if (count == 6) {
            read_type = 2;
        } else if (count == 12) {
            read_type = 0;
        } else if (count == 8) {
            strtok.nextToken();
            strtok.nextToken();
            read_type = 2;
        } else if (count == 7) {
            strtok.nextToken();
            read_type = 2;
        } else if (count == 3) {
            read_type = 1;
        } else {
            System.out.println("Tokens: " + count);
        }

        // if (read_type>0)
        // if (read_type == 20)
        if (read_type > 0) {
            CGMSValuesSubEntry sub = new CGMSValuesSubEntry();
            tmp_time = strtok.nextToken();
            // sub.setDate(DexcomCGMS.getDateFromString(tmp_time));
            // sub.datetime = DexcomCGMS.getDateFromString(tmp_time);
            // sub.time = DexcomCGMS.getTimeFromString(tmp_time);
            sub.setDateTime(DexcomCGMS.getDateTimeFromString(tmp_time));
            sub.setType(CGMSValuesSubEntry.CGMS_BG_READING);
            strtok.nextToken();
            sub.value = Integer.parseInt(strtok.nextToken());
            addEntry(sub);

            if (read_type == 2) {
                sub = new CGMSValuesSubEntry();
                tmp_time = strtok.nextToken();
                sub.setDateTime(DexcomCGMS.getDateTimeFromString(tmp_time));
                // sub.datetime = DexcomCGMS.getDateFromString(tmp_time);
                // sub.time = DexcomCGMS.getTimeFromString(tmp_time);
                sub.setType(CGMSValuesSubEntry.METER_CALIBRATION_READING);
                strtok.nextToken();
                sub.value = Integer.parseInt(strtok.nextToken());
                addEntry(sub);

                // System.out.println("" + sub);
            }

        }

        i++;

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
                return getFileDescription() + " (" + getFileExtension() + ")";
            }

        };

    }


    public void goToNextDialog(JDialog currentDialog) {
    }


    public String toString() {
        return this.getFullFileDescription();
    }


    public void setOutputWriter(OutputWriter ow) {
        this.output_writer = ow;
    }

}
