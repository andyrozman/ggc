package ggc.cgms.data;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.atech.utils.data.ATechDate;

import ggc.cgms.util.DataAccessCGMS;
import ggc.plugin.data.DeviceTempValues;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.output.OutputWriterData;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       CGMS Tool (support for CGMS devices)
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
 *  Filename:     CGMSDataWriter
 *  Description:  CGMS Entries data Writer
 *
 *  Author: Andy {andy@atech-software.com}
 */

// Writing of data for CGMS is little different than any other data (we pack
// daily data together in one entry per day), which means that normal writer
// can't be used, at least not yet... This is only temporary solution.

public class CGMSDataWriter extends HashMap<String, DeviceTempValues>
{

    private static final Logger LOG = LoggerFactory.getLogger(CGMSDataWriter.class);
    OutputWriter outputWriter;
    CGMSValuesTableModel valuesModel;
    DataAccessCGMS dataAccess = DataAccessCGMS.getInstance();
    private boolean isIdentificationWriten = false;
    String source = "";
    boolean debug = false;


    public CGMSDataWriter(OutputWriter outputWriter)
    {
        this.outputWriter = outputWriter;
        valuesModel = (CGMSValuesTableModel) dataAccess.getDeviceDataHandler().getDeviceValuesTableModel();
    }


    public CGMSDataWriter()
    {
        valuesModel = (CGMSValuesTableModel) dataAccess.getDeviceDataHandler().getDeviceValuesTableModel();
    }


    public void setOutputWriter(OutputWriter ow)
    {
        this.outputWriter = ow;
    }


    private void addEntry(CGMSValuesSubEntry entry)
    {
        this.valuesModel.addEntry(entry);
        this.valuesModel.fireTableDataChanged();
    }


    /**
     * Write Object
     * @param _type
     * @param _datetime
     * @param _value
     * @return
     */
    public boolean writeObject(String _type, ATechDate _datetime, String _value)
    {
        return this.writeObject(_type, _datetime, _value, StringUtils.isNotBlank(_value));
    }


    /**
     * Write Object
     * @param _type
     * @param _datetime
     * @param _value
     * @return
     */
    public boolean writeObject(String _type, ATechDate _datetime, String _value, boolean isNumericValue)
    {
        if (this.containsKey(_type))
        {
            DeviceTempValues dtv = this.get(_type);

            if (StringUtils.isNotBlank(_value))
            {
                boolean numeric = false;

                if (dtv.getIsNumericValue() != null)
                {
                    numeric = dtv.getIsNumericValue().booleanValue();
                }
                else
                {
                    numeric = isNumericValue;
                }

                if (numeric)
                {
                    _value = _value.replace(',', '.');
                }
            }

            OutputWriterData owd = dtv.getData(_datetime, _value);

            writeObject(owd);

            return true;
        }
        else
        {
            LOG.warn("Unknown key:" + _type);
            return false;
        }
    }


    public boolean writeObject(String _type, ATechDate _datetime, Number _value)
    {
        if (this.containsKey(_type))
        {
            DeviceTempValues dtv = this.get(_type);

            OutputWriterData owd = dtv.getData(_datetime, _value.toString());

            writeObject(owd);

            return true;
        }
        else
        {
            LOG.warn("Unknown key:" + _type);
            return false;
        }
    }


    public void writeObject(OutputWriterData owd)
    {
        if (owd instanceof CGMSValuesSubEntry)
        {

            CGMSValuesSubEntry cvse = (CGMSValuesSubEntry) owd;

            // System.out.println("Value: " + cvse);
            addEntry(cvse);
        }
        else
        {
            throw new NotImplementedException();
        }

    }


    public void addConfiguration(String key, DeviceTempValues deviceTempValues)
    {
        if (debug)
        {
            System.out.println("Config [key=" + key + ", DeviceTempValues=" + deviceTempValues.toString() + "]");
        }

        put(key, deviceTempValues);
    }

    /**
     * Write Object
     *
     * @param _type
     * @param _datetime
     * @param code_type
     * @param _value
     * @return
     */
    // public boolean writeObject(String _type, ATechDate _datetime, int
    // code_type, String _value)
    // {
    // if (this.containsKey(_type))
    // {
    // if (_value != null)
    // {
    // _value = _value.replace(',', '.');
    // }
    // DeviceTempValues dtv = this.get(_type);
    //
    // //if (!this.is_silent_mode)
    // {
    // // output_writer.writeData(dtv.getData(_datetime, code_type, _value));
    // }
    // return true;
    // }
    // else
    // {
    // LOG.warn("Unknown key:" + _type);
    // return false;
    // }
    // }

}
