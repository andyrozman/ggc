package ggc.cgms.device.dexcom.receivers.g4receiver.util;

import ggc.cgms.device.dexcom.receivers.g4receiver.DexcomG4Api;
import ggc.cgms.device.dexcom.receivers.g4receiver.converter.BytesToDatabasePagesConverter;
import ggc.cgms.device.dexcom.receivers.g4receiver.converter.ConverterType;
import ggc.cgms.device.dexcom.receivers.g4receiver.converter.ElementToPartitionInfoConverter;
import ggc.cgms.device.dexcom.receivers.g4receiver.converter.data.DataPageToEGVDataConverter;
import ggc.cgms.device.dexcom.receivers.g4receiver.converter.data.DataPageToFileConverter;
import ggc.cgms.device.dexcom.receivers.g4receiver.converter.data.DataPageToUserEventDataConverter;
import ggc.cgms.device.dexcom.receivers.g4receiver.converter.data.DataPagesToInsertionTimeConverter;
import ggc.cgms.device.dexcom.receivers.g4receiver.converter.data.DataPagesToMeterConverter;
import ggc.cgms.device.dexcom.receivers.g4receiver.converter.data.DataPagesToXmlRecordConverter;
import ggc.cgms.device.dexcom.receivers.g4receiver.enums.ReceiverRecordType;
import ggc.cgms.device.dexcom.receivers.g4receiver.internal.Partition;
import ggc.cgms.device.dexcom.receivers.g4receiver.internal.PartitionInfo;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.atech.utils.data.ShortUtils;

public class DexcomUtils
{
    private static Log log = LogFactory.getLog(DexcomUtils.class);
    public static int ImmediateMatchMask = 0x80;
    private static DexcomG4Api dexcomG4Api;
    private static PartitionInfo partitionInfo;
    private static SAXBuilder saxBuilder = new SAXBuilder();
    private static final String EPOCH = "01-01-2009";
    private static Integer displayTimeOffset = null;
    static SimpleDateFormat dateTimeFormater = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
    private static ShortUtils shortUtils = new ShortUtils();
    private static HashMap<ConverterType, Object> converters = new HashMap<ConverterType, Object>();

    static
    {
        converters.put(ConverterType.ElementToPartitionInfoConverter, new ElementToPartitionInfoConverter());
        converters.put(ConverterType.BytesToDatabasePagesConverter, new BytesToDatabasePagesConverter());
        converters.put(ConverterType.DataPagesToInsertionTimeConverter, new DataPagesToInsertionTimeConverter());
        converters.put(ConverterType.DataPagesToXmlRecordConverter, new DataPagesToXmlRecordConverter());
        converters.put(ConverterType.DataPageToEGVDataConverter, new DataPageToEGVDataConverter());
        converters.put(ConverterType.DataPageToUserEventDataConverter, new DataPageToUserEventDataConverter());
        converters.put(ConverterType.DataPagesToMeterConverter, new DataPagesToMeterConverter());
        converters.put(ConverterType.DataPageToFileConverter, new DataPageToFileConverter());
        
    }

    public enum BitConversion
    {
        LITTLE_ENDIAN, // 20 0 0 0 = reverse
        BIG_ENDIAN // 0 0 0 20 = normal - java
    }

    // Convert the packet data
    public static int toInt(byte[] b, BitConversion flag)
    {
        switch (flag)
        {
        case BIG_ENDIAN: // BitConverter.FLAG_JAVA:
            return ((b[0] & 0xff) << 24) | ((b[1] & 0xff) << 16) | ((b[2] & 0xff) << 8) | (b[3] & 0xff);
        case LITTLE_ENDIAN:
            return ((b[3] & 0xff) << 24) | ((b[2] & 0xff) << 16) | ((b[1] & 0xff) << 8) | (b[0] & 0xff);
        default:
            throw new IllegalArgumentException("BitConverter: toInt");
        }
    }

    public static int toInt(short[] b, BitConversion flag)
    {
        switch (flag)
        {
        case BIG_ENDIAN: // BitConverter.FLAG_JAVA:
            return ((b[0] & 0xff) << 24) | ((b[1] & 0xff) << 16) | ((b[2] & 0xff) << 8) | (b[3] & 0xff);
        case LITTLE_ENDIAN:
            return ((b[3] & 0xff) << 24) | ((b[2] & 0xff) << 16) | ((b[1] & 0xff) << 8) | (b[0] & 0xff);
        default:
            throw new IllegalArgumentException("BitConverter: toInt");
        }
    }

    public static short toShort(byte[] b, BitConversion flag)
    {
        switch (flag)
        {
        case BIG_ENDIAN: // BitConverter.FLAG_JAVA:
            return (short) (((b[0] & 0xff) << 8) | (b[1] & 0xff));
        case LITTLE_ENDIAN:
            return (short) (((b[1] & 0xff) << 8) | (b[0] & 0xff));
        default:
            throw new IllegalArgumentException("BitConverter: toInt");
        }
    }

    public static short toShort(short[] b, BitConversion flag)
    {
        switch (flag)
        {
        case BIG_ENDIAN: // BitConverter.FLAG_JAVA:
            return (short) (((b[0] & 0xff) << 8) | (b[1] & 0xff));
        case LITTLE_ENDIAN:
            return (short) (((b[1] & 0xff) << 8) | (b[0] & 0xff));
        default:
            throw new IllegalArgumentException("BitConverter: toInt");
        }
    }

    public static int toIntShort(short[] b, BitConversion flag)
    {
        switch (flag)
        {
        case BIG_ENDIAN: // BitConverter.FLAG_JAVA:
            return ((b[0] & 0xff) << 8) | (b[1] & 0xff);
        case LITTLE_ENDIAN:
            return ((b[1] & 0xff) << 8) | (b[0] & 0xff);
        default:
            throw new IllegalArgumentException("BitConverter: toInt");
        }
    }

    public static Date toDate(byte[] b, BitConversion flag)
    {

        int dt = toInt(b, flag);

        Date d = new Date();
        d.setTime(dt);

        return d;
    }

    public static Date toDate(int date)
    {

        Date d = new Date();
        d.setTime(date);

        return d;
    }

    public static byte[] getBytes(int i, int flag)
    {
        byte[] b = new byte[4];
        switch (flag)
        {
        case 0:
            b[0] = (byte) ((i >> 24) & 0xff);
            b[1] = (byte) ((i >> 16) & 0xff);
            b[2] = (byte) ((i >> 8) & 0xff);
            b[3] = (byte) (i & 0xff);
            break;
        case 1:
            b[3] = (byte) ((i >> 24) & 0xff);
            b[2] = (byte) ((i >> 16) & 0xff);
            b[1] = (byte) ((i >> 8) & 0xff);
            b[0] = (byte) (i & 0xff);
            break;
        default:
            break;
        }
        return b;
    }

    // TODO remove possibly see CrcUtil
    // CRC methods
    public static int calculateCRC16(byte[] buff, int start, int end)
    {
        int crc = 0;
        for (int i = start; i < end; i++)
        {
            crc = ((crc >>> 8) | (crc << 8)) & 0xffff;
            crc ^= (buff[i] & 0xff);
            crc ^= ((crc & 0xff) >> 4);
            crc ^= (crc << 12) & 0xffff;
            crc ^= ((crc & 0xFF) << 5) & 0xffff;
        }
        crc &= 0xffff;
        return crc;
    }

    public static int calculateCRC16(short[] buff, int start, int end)
    {
        int crc = 0;
        for (int i = start; i < end; i++)
        {
            crc = ((crc >>> 8) | (crc << 8)) & 0xffff;
            crc ^= (buff[i] & 0xff);
            crc ^= ((crc & 0xff) >> 4);
            crc ^= (crc << 12) & 0xffff;
            crc ^= ((crc & 0xFF) << 5) & 0xffff;
        }
        crc &= 0xffff;
        return crc;
    }

    public static int calculateCRC16(int[] buff, int start, int end)
    {
        long crc = 0;
        for (int i = start; i < end; i++)
        {
            crc = ((crc >>> 8) | (crc << 8)) & 0xffff;
            crc ^= (buff[i] & 0xff);
            crc ^= ((crc & 0xff) >> 4);
            crc ^= (crc << 12) & 0xffff;
            crc ^= ((crc & 0xFF) << 5) & 0xffff;
        }
        crc &= 0xffff;
        return (int) crc;
    }

    public static DexcomG4Api getDexcomG4Api()
    {
        return dexcomG4Api;
    }

    public static void setDexcomG4Api(DexcomG4Api dexcomG4Api)
    {
        DexcomUtils.dexcomG4Api = dexcomG4Api;
    }

    public static PartitionInfo getPartitionInfo() throws DexcomException
    {
    	return dexcomG4Api.readDatabasePartitionInfo();
    	
//        if (partitionInfo == null)
//        {
//            partitionInfo = dexcomG4Api.readDatabasePartitionInfo();
//        }
//
//        return partitionInfo;
    }

    public static Partition getPartition(ReceiverRecordType recordType) throws DexcomException
    {
    	return dexcomG4Api.getPartition(recordType);
//        if (partitionInfo == null)
//        {
//            getPartitionInfo();
//        }
//
//        return partitionInfo.getPartitionByRecordType(recordType);
    }

    public static Element createXmlTree(String xmlData) throws DexcomException
    {
        try
        {
            Document doc = saxBuilder.build(new StringReader(xmlData));

            Element element = doc.getRootElement();
            return element;
        }
        catch (Exception ex)
        {
            throw new DexcomException(DexcomExceptionType.ParsingError, new Object[] { ex.getLocalizedMessage() }, ex);
        }

    }

    public static String getXmlStringFromElement(Element element)
    {
        XMLOutputter outp = new XMLOutputter();
        outp.setFormat(Format.getPrettyFormat());
        return outp.outputString(element);
    }

    public static void debugXmlTree(Element element)
    {
        String s = getXmlStringFromElement(element);

        log.debug(s);
    }

    public static int readDisplayTimeOffset()
    {
        try
        {
            if (displayTimeOffset == null)
            {
                displayTimeOffset = dexcomG4Api.readDisplayTimeOffset();
            }

            return displayTimeOffset;
        }
        catch (DexcomException ex)
        {
            log.warn("Error reading DisplayTimeOffset: " + ex, ex);
            return 0;
        }
    }

    public static Date getDateFromSeconds(int timeSeconds)
    {

        return getDateFromSeconds(timeSeconds, DexcomDateParsing.RawDate);
    }

    public enum DexcomDateParsing
    {
        RawDate, DateWithDifference, DateWithDifferenceWithTimeZoneFix,
    }

    public static Date getDateFromSeconds(int timeSeconds, DexcomDateParsing parsing)
    {
        int dt = timeSeconds;

        if ((parsing == DexcomDateParsing.DateWithDifference) || // 
                (parsing == DexcomDateParsing.DateWithDifferenceWithTimeZoneFix))
        {
            dt += readDisplayTimeOffset();
        }

        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
        Date epoch;

        try
        {
            epoch = f.parse(EPOCH);
        }

        catch (ParseException e)
        {
            DexcomUtils.log.error("Unable to parse date: " + EPOCH + ", using current time", e);
            epoch = new Date();
        }

        // Epoch is PST, but but having epoch have user timezone added, then
        // don't have to add to the // display time 

        long milliseconds = epoch.getTime();
        long timeAdd = milliseconds + (1000L * dt);

        if (parsing == DexcomDateParsing.DateWithDifferenceWithTimeZoneFix)
        {
            TimeZone tz = TimeZone.getDefault();

            if (tz.inDaylightTime(new Date()))
            {
                timeAdd = timeAdd - 3600000L;
            }
        }

        return new Date(timeAdd);
    }

    public static String getDateTimeString(Date dateTime)
    {
        return dateTimeFormater.format(dateTime);
    }

    public static ShortUtils getShortUtils()
    {
        return shortUtils;
    }
    
    
    

    public static Object getConverter(ConverterType converterType)
    {
        return converters.get(converterType);
    }

    
}
