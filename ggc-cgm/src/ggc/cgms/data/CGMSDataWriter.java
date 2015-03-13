package ggc.cgms.data;

import com.atech.utils.data.ATechDate;
import ggc.cgms.device.dexcom.receivers.DexcomDevice;
import ggc.cgms.util.DataAccessCGMS;
import ggc.plugin.data.DeviceTempValues;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.output.OutputWriterData;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashMap;

/**
 * Created by andy on 04.03.15.
 */


// Writing of data for CGMS is little different than any other data (we pack daily data together in one entry),
// which means that normal writer can't be used, at least not yet...  This is only temporary sollution.

public class CGMSDataWriter extends HashMap<String, DeviceTempValues>
{
    private static Log log = LogFactory.getLog(CGMSDataWriter.class);
    OutputWriter outputWriter;
    CGMSValuesTableModel valuesModel;
    DataAccessCGMS dataAccess = DataAccessCGMS.getInstance();
    private boolean isIdentificationWriten = false;
    String source = "";


    public CGMSDataWriter(OutputWriter outputWriter)
    {
        this.outputWriter = outputWriter;
        valuesModel = (CGMSValuesTableModel) dataAccess.getDeviceDataHandler().getDeviceValuesTableModel();
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
            log.warn("Unknown key:" + _type);
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
            log.warn("Unknown key:" + _type);
            return false;
        }
    }


    public void writeObject(OutputWriterData owd)
    {
        if (owd instanceof CGMSValuesSubEntry)
        {

            CGMSValuesSubEntry cvse = (CGMSValuesSubEntry)owd;

            System.out.println("Value: " + cvse);
            addEntry(cvse);
        }
        else
        {
            throw new NotImplementedException();
        }

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
//    public boolean writeObject(String _type, ATechDate _datetime, int code_type, String _value)
//    {
//        if (this.containsKey(_type))
//        {
//            if (_value != null)
//            {
//                _value = _value.replace(',', '.');
//            }
//            DeviceTempValues dtv = this.get(_type);
//
//            //if (!this.is_silent_mode)
//            {
//     //           output_writer.writeData(dtv.getData(_datetime, code_type, _value));
//            }
//            return true;
//        }
//        else
//        {
//            log.warn("Unknown key:" + _type);
//            return false;
//        }
//    }


}
