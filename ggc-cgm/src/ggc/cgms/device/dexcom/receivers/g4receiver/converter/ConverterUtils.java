package ggc.cgms.device.dexcom.receivers.g4receiver.converter;

import ggc.cgms.device.dexcom.receivers.g4receiver.converter.data.DataPageToEGVDataConverter;
import ggc.cgms.device.dexcom.receivers.g4receiver.converter.data.DataPageToUserEventDataConverter;
import ggc.cgms.device.dexcom.receivers.g4receiver.converter.data.DataPagesToInsertionTimeConverter;
import ggc.cgms.device.dexcom.receivers.g4receiver.converter.data.DataPagesToMeterConverter;
import ggc.cgms.device.dexcom.receivers.g4receiver.converter.data.DataPagesToXmlRecordConverter;

import java.util.HashMap;

public class ConverterUtils
{
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

        // parsers.put(ParserType.IntegerParser, new IntegerParser());
        // parsers.put(ParserType.StringUTF8Parser, new StringUTF8Parser());
        // parsers.put(ParserType.XmlParser, new XmlParser());
    }

    private ConverterUtils()
    {
    }

    public static Object getConverter(ConverterType converterType)
    {
        return converters.get(converterType);
    }

}
