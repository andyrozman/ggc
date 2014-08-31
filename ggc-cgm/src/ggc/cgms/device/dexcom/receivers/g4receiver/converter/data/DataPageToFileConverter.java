package ggc.cgms.device.dexcom.receivers.g4receiver.converter.data;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.utils.file.BytesFileUtil;

import ggc.cgms.device.dexcom.receivers.g4receiver.enums.ReceiverRecordType;
import ggc.cgms.device.dexcom.receivers.g4receiver.internal.DatabasePage;

public class DataPageToFileConverter
{
    Log log = LogFactory.getLog(DataPageToFileConverter.class);
    
    public void convert(DatabasePage page, int pageNumber, ReceiverRecordType recordType)
    {
        try
        {
            File f = new File(recordType.name() + "_page_" + pageNumber + "_header.bin");
            BytesFileUtil.writeBytesToFile(f, page.pageHeaderRaw);
            
            f = new File(recordType.name() + "_page_" + pageNumber + "_page.bin");
            BytesFileUtil.writeBytesToFile(f, page.pageData);
            
        }
        catch (IOException ex)
        {
            log.error("Error writing to file. Ex.:" + ex, ex);
        }
    }
}
