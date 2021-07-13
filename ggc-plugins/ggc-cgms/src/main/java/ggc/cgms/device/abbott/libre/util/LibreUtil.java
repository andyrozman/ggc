package ggc.cgms.device.abbott.libre.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.BitUtils;

import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.output.OutputWriter;

/**
 * Created by andy on 28/08/17.
 */
// TODO is this real class
public class LibreUtil
{

    private static final Logger LOG = LoggerFactory.getLogger(LibreUtil.class);

    static BitUtils bitUtils = new BitUtils();

    int[] CRC32_TABLE = { 0x00000000, 0x04c11db7, 0x09823b6e, 0x0d4326d9, 0x130476dc, 0x17c56b6b, 0x1a864db2,
                          0x1e475005, 0x2608edb8, 0x22c9f00f, 0x2f8ad6d6, 0x2b4bcb61, 0x350c9b64, 0x31cd86d3,
                          0x3c8ea00a, 0x384fbdbd, };

    String DEVICE_MODEL_NAME = "FreeStyle Libre";

    Float KETONE_VALUE_FACTOR = 18.0f; // according to specs
    Float KETONE_HI = 8.0f;
    Float KETONE_LO = null; // ketone value cannot be low

    Integer GLUCOSE_HI = 500;
    Integer GLUCOSE_LO = 40;
    private static OutputWriter outputWriter;

    static boolean debug = true;
    static boolean currentDebug = false;


    // Map<>
    //
    // export const DB_WRAP_RECORDS=
    // {};DB_WRAP_RECORDS[DB_RECORD_TYPE.RESULT_RECORD_WRAP]=DB_TABLE_ID.GLUCOSE_RESULT; //
    // DB_WRAP_RECORDS[DB_RECORD_TYPE.INSULIN_WRAP]=DB_TABLE_ID.RAPID_ACTING_INSULIN; //
    // DB_WRAP_RECORDS[DB_RECORD_TYPE.HISTORICAL_WRAP]=DB_TABLE_ID.HISTORICAL_DATA; //
    // DB_WRAP_RECORDS[DB_RECORD_TYPE.EVENT_DATABASE_RECORD_NUMBER_WRAP]=DB_TABLE_ID.EVENT;
    //

    public static void setUp()
    {
        // FIXME DB_WRAP_RECORDS
    }


    public static BitUtils getBitUtils()
    {
        return bitUtils;
    }


    public static void setOutputWriter(OutputWriter outputWriter)
    {
        LibreUtil.outputWriter = outputWriter;
    }


    public static OutputWriter getOutputWriter()
    {
        return outputWriter;
    }


    // FIXME
    public static String validateTextResponse(String outText, boolean singleResponse, boolean ignoreCRC)
            throws PlugInBaseException
    {
        String splitedString[] = outText.split("CMD ");
        // LOG.error("extractText not processing full data....\n");

        // LOG.debug("F: {}", splitedString[0]);
        // LOG.debug("S: {}", splitedString[1]);

        if (!splitedString[1].contains("OK\r\n"))
        {
            String responseResult = removeEndingCRLF(splitedString[1]).trim();

            LOG.error("Invalid response: {}. Whole message:\n{}", responseResult, outText);
            throw new PlugInBaseException(PlugInExceptionType.DeviceInvalidResponse, //
                    new Object[] { "OK", removeEndingCRLF(splitedString[1]).trim() });
        }

        splitedString = splitedString[0].split("CKSM:");

        try
        {
            validateChecksum(splitedString[0], splitedString[1]);
        }
        catch (PlugInBaseException ex)
        {
            LOG.error("{}. Whole message:\n{}", ex.getMessage(), outText);

            if (!ignoreCRC)
                throw ex;
        }

        if (singleResponse)
        {
            outText = removeEndingCRLF(splitedString[0]);
        }
        else
        {
            outText = splitedString[0];
        }

        return outText;
    }


    private static String removeEndingCRLF(String text)
    {
        return text.split("\r\n")[0];
    }


    public static boolean validateChecksum(String text, String expectedChecksum) throws PlugInBaseException
    {
        int sum = 0;
        for (byte c : text.getBytes())
        {
            sum += c;
        }

        expectedChecksum = removeEndingCRLF(expectedChecksum);

        long targetChecksum = Long.parseLong(expectedChecksum, 16);

        // LOG.debug("expected Checksum: {}, targetChecksum {}.", expectedChecksum, targetChecksum);

        if (targetChecksum != sum)
        {
            throw new PlugInBaseException(PlugInExceptionType.FailedCRCCheck, //
                    new Object[] { targetChecksum, sum });
        }

        return true;

    }


    public static void setCurrentDebug(boolean debugIn)
    {
        currentDebug = debugIn;
    }


    public static boolean getCurrentDebug()
    {
        return currentDebug;
    }


    public static void resetDebug()
    {
        currentDebug = debug;
    }

}
