package ggc.meter.device.ascensia.impl;

import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.data.ATechDate;
import com.atech.utils.data.TimeZoneUtil;

import ggc.core.data.defs.GlucoseUnitType;
import ggc.meter.data.GlucoseMeterMarkerDto;
import ggc.meter.data.MeterValuesEntry;
import ggc.meter.defs.GlucoseMeterMarker;
import ggc.meter.defs.device.MeterDeviceDefinition;
import ggc.meter.util.DataAccessMeter;
import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.output.OutputWriter;

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
 *  Filename:     AscensiaDecoder
 *  Description:  Ascensia Meter Decoder
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class AscensiaDecoder
{

    private static final Logger LOG = LoggerFactory.getLogger(AscensiaDecoder.class);

    private OutputWriter outputWriter;
    private I18nControlAbstract i18nControl;
    protected TimeZoneUtil tzu = TimeZoneUtil.getInstance();
    private MeterDeviceDefinition meterDeviceDefinition;


    public AscensiaDecoder(OutputWriter writer, MeterDeviceDefinition meterDeviceDefinition)
    {
        this.outputWriter = writer;
        i18nControl = DataAccessMeter.getInstance().getI18nControlInstance();
        this.meterDeviceDefinition = meterDeviceDefinition;
    }


    public String decode(String textToDecode) throws PlugInBaseException
    {

        int idx = textToDecode.indexOf("|");

        if (idx == -1)
        {
            LOG.warn("Unidentified Text: " + textToDecode);
        }

        textToDecode = textToDecode.substring(idx - 1);

        String code = textToDecode.substring(0, 1);

        //
        // LOG.info("Code: " + code);

        if (code.equals("P"))
        {
            decodePatientInfo(textToDecode);
        }
        else if (code.equals("L"))
        {
            decodeTermination(textToDecode);
        }
        else if (code.equals("H"))
        {
            decodeHeader(textToDecode);
        }
        else if (code.equals("R"))
        {
            decodeResult(textToDecode);
        }
        else
        {
            LOG.warn("Record type " + code + " is unknown/unsupported.");
        }

        return code;
    }


    private void decodeTermination(String text)
    {
        // ignored for now
        // System.out.println("TERMINATION RECORD");
    }


    private void decodePatientInfo(String text)
    {
        // ignored for now
    }


    private void decodeResult(String input) throws PlugInBaseException
    {
        try
        {
            input = DataAccessMeter.replaceExpression(input, "||", "| |");

            StringTokenizer strtok = new StringTokenizer(input, "|");

            boolean found = false;

            // we search for entry containing Glucose... (in case that data was
            // not received entirely)
            while (!found && strtok.hasMoreElements())
            {
                String s = strtok.nextToken();
                if (s.equals("^^^Glucose"))
                {
                    found = true;
                }
            }

            if (!found)
                return;

            // R|5|^^^Glucose|7.9|mmol/L^P||B||201102241308 (Contour USB)
            // R|261|^^^Glucose|441|mg/dL^P||M0/T1||201507220919

            MeterValuesEntry mve = new MeterValuesEntry();

            String val = strtok.nextToken();

            // System.out.println("val:" + val);

            String unit = strtok.nextToken(); // unit mmol/L^x, mg/dL^x
            // System.out.println("Unit: " + unit);

            strtok.nextToken(); // N/A

            String markers = strtok.nextToken();

            // user marks
            convertMarkers(markers, mve); // User Marks

            strtok.nextToken(); // N/A

            String time = strtok.nextToken(); // datetime

            if (meterDeviceDefinition == MeterDeviceDefinition.AscensiaContourNextOne)
            {
                time = time.substring(0, time.length() - 2);
            }

            setBgData(mve, time, val, unit);

            this.outputWriter.writeData(mve);

        }
        catch (Exception ex)
        {
            LOG.error("Problem decoding result data. Ex.: " + ex, ex);

            throw new PlugInBaseException(PlugInExceptionType.ParsingError, new Object[] { ex.getLocalizedMessage() },
                    ex);
        }

    }


    private void setBgData(MeterValuesEntry mve, String time, String value, String unit)
    {
        ATechDate correctedDate = tzu.getCorrectedDateTime(new ATechDate(Long.parseLong(time)));
        GlucoseUnitType unitType = unit.startsWith("mg/dL") ? GlucoseUnitType.mg_dL : GlucoseUnitType.mmol_L;

        LOG.debug("Data: dt={}, value={}, unit={}", correctedDate, value, unitType.getTranslation());

        mve.setDateTimeObject(correctedDate);
        mve.setBgValue(value, unitType);
    }


    public void decodeResultNonUsbMeters(String input)
    {
        try
        {
            StringTokenizer strtok = new StringTokenizer(input, "|");

            boolean found = false;

            // we search for entry containing Glucose... (in case that data was
            // not received entirely)
            while (!found && strtok.hasMoreElements())
            {
                String s = strtok.nextToken();
                if (s.equals("^^^Glucose"))
                {
                    found = true;
                }
            }

            if (!found)
                return;

            // System.out.println(input);

            MeterValuesEntry mve = new MeterValuesEntry();

            String val = strtok.nextToken();

            // System.out.println("val:" + val);

            // mve.setBgValue(val); // bg_value
            String unit = strtok.nextToken(); // unit mmol/L^x, mg/dL^x

            mve.addParameter("REF_RANGES", strtok.nextToken()); // Reference
            // ranges (Dex
            // Only)
            mve.addParameter("RES_ABNORMAL_FLAGS", strtok.nextToken()); // Result
            // abnormal flags (7)
            mve.addParameter("USER_MARKS", strtok.nextToken()); // User Marks
                                                                // (8)
            mve.addParameter("RES_STATUS_MARKER", strtok.nextToken()); // Result
            // status marker
            strtok.nextToken(); // N/A
            strtok.nextToken(); // OperatorId (N/A)

            String time = strtok.nextToken(); // datetime

            setBgData(mve, time, val, unit);

            this.outputWriter.writeData(mve);

        }
        catch (Exception ex)
        {
            LOG.error("Problem decoding result data. Ex.: " + ex, ex);
        }

    }


    private void convertMarkers(String markersInput, MeterValuesEntry mve)
    {
        if (markersInput.contains("<"))
        {
            mve.addGlucoseMeterMarker(GlucoseMeterMarkerDto.createMarker(GlucoseMeterMarker.LowGlucose));
        }
        else if (markersInput.contains(">"))
        {
            mve.addGlucoseMeterMarker(GlucoseMeterMarkerDto.createMarker(GlucoseMeterMarker.HighGlucose));
        }

        if (markersInput.contains("B"))
        {
            if (!markersInput.contains("ZB"))
                mve.addGlucoseMeterMarker(GlucoseMeterMarkerDto.createMarker(GlucoseMeterMarker.PreMeal));
        }
        else if (markersInput.contains("A"))
        {
            if (!markersInput.contains("ZA"))
                mve.addGlucoseMeterMarker(GlucoseMeterMarkerDto.createMarker(GlucoseMeterMarker.PostMeal));
        }

        if (markersInput.contains("D"))
        {
            mve.addGlucoseMeterMarker(GlucoseMeterMarkerDto.createMarker(GlucoseMeterMarker.DontFeelRight));
        }

        if (markersInput.contains("I"))
        {
            mve.addGlucoseMeterMarker(GlucoseMeterMarkerDto.createMarker(GlucoseMeterMarker.Sick));
        }

        if (markersInput.contains("S"))
        {
            mve.addGlucoseMeterMarker(GlucoseMeterMarkerDto.createMarker(GlucoseMeterMarker.Stress));
        }

        if (markersInput.contains("X"))
        {
            mve.addGlucoseMeterMarker(GlucoseMeterMarkerDto.createMarker(GlucoseMeterMarker.Activity));
        }

        if (markersInput.contains("C"))
        {
            if (!markersInput.contains("ZC"))
                mve.addGlucoseMeterMarker(GlucoseMeterMarkerDto.createMarker(GlucoseMeterMarker.ControlResult));
        }

        if (markersInput.contains("Z"))
        {
            String time = "";

            if (markersInput.contains("Z1"))
            {
                time = "15 min";
            }
            else if (markersInput.contains("Z2"))
            {
                time = "0.5 h";
            }
            else if (markersInput.contains("Z3"))
            {
                time = "45 min";
            }
            else if (markersInput.contains("Z4"))
            {
                time = "1 h";
            }
            else if (markersInput.contains("Z5"))
            {
                time = "1.25 h";
            }
            else if (markersInput.contains("Z6"))
            {
                time = "1.5 h";
            }
            else if (markersInput.contains("Z7"))
            {
                time = "1.75 h";
            }
            else if (markersInput.contains("Z8"))
            {
                time = "2 h";
            }
            else if (markersInput.contains("Z9"))
            {
                time = "2.25 h";
            }
            else if (markersInput.contains("ZA"))
            {
                time = "2.5 h";
            }
            else if (markersInput.contains("ZB"))
            {
                time = "2.75 h";
            }
            else if (markersInput.contains("ZC"))
            {
                time = "3 h";
            }

            mve.addGlucoseMeterMarker(GlucoseMeterMarkerDto.createMarker(GlucoseMeterMarker.AfterFoodWithTime, time));
        }

    }


    private void decodeHeader(String text)
    {
        StringTokenizer stringTokenizer = new StringTokenizer(text, "|");
        stringTokenizer.nextToken();
        stringTokenizer.nextToken();
        stringTokenizer.nextToken();
        // stringTokenizer.nextToken();
        String identifier = stringTokenizer.nextToken();

        decodeHeaderDeviceIdentification(identifier);

        this.outputWriter.writeDeviceIdentification();
    }


    /**
     * This method can be used by older device, where we don't posses whole entries just device identification data
     * 
     * @param text identification entry
     */
    public void decodeHeaderDeviceIdentification(String text)
    {

        DeviceIdentification di = this.outputWriter.getDeviceIdentification();

        if (di == null)
            di = new DeviceIdentification(this.i18nControl);

        StringTokenizer strtok = new StringTokenizer(text, "^");

        String inf = "";

        String id = strtok.nextToken();
        String versions = strtok.nextToken();
        String serial = strtok.nextToken();

        inf += i18nControl.getMessage("PRODUCT_CODE") + ": ";

        String tmp = decodeMeterDevice(id);

        di.device_identified = tmp;

        inf += tmp;
        inf += "\n";

        StringTokenizer strtok2 = new StringTokenizer(versions, "\\");

        di.device_software_version = strtok2.nextToken();
        di.device_hardware_version = strtok2.nextToken();
        di.device_serial_number = serial;

        inf += i18nControl.getMessage("SOFTWARE_VERSION") + ": " + di.device_software_version;
        inf += i18nControl.getMessage("\nEEPROM_VERSION") + ": " + di.device_hardware_version;

        inf += i18nControl.getMessage("\nSERIAL_NUMBER") + ": " + serial;

        this.outputWriter.setDeviceIdentification(di);

        // this.m_info = inf;
        // LOG.info("Device Info: " + inf);
    }


    private String decodeMeterDevice(String idFull)
    {
        String tmp;

        String id = idFull.replace(" ", "");

        if (id.equals("Bayer3883"))
        {
            tmp = "ELITE XL Family (";
        }
        else if (id.equals("Bayer3950"))
        {
            tmp = "DEX Family (";
        }
        else if (id.equals("Bayer6115") || id.equals("Bayer6116"))
        {
            tmp = "Breeze Family (";
        }
        else if (id.equals("Bayer7150"))
        {
            tmp = "CONTOUR Family (";
        }
        else if (id.equals("Bayer7390"))
        {
            tmp = "Contour Usb Family (";
        }
        else if (id.equals("Bayer7350"))
        {
            tmp = "Contour Next Family (";
        }
        else if (id.equals("Bayer7410"))
        {
            tmp = "Contour Next USB Family (";
        }
        else if (id.equals("Bayer6300") || id.equals("Bayer6200"))
        {
            tmp = "Contour Next Link Family (";
        }
        else if (id.equals("Contour7800"))
        {
            tmp = "Contour Next One Family (";
        }
        else
        {
            tmp = "Unknown Ascensia Device (";
        }

        tmp += id;
        tmp += ")";
        return tmp;
    }
}
