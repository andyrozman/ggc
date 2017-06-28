package ggc.pump.device.insulet;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.BitUtils;

import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.data.progress.ProgressType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.reader.AbstractDeviceReader;
import ggc.pump.device.insulet.data.converter.OmnipodConfigConverter;
import ggc.pump.device.insulet.data.converter.OmnipodDataConverter;
import ggc.pump.device.insulet.data.dto.AbstractRecord;
import ggc.pump.device.insulet.data.dto.config.*;
import ggc.pump.device.insulet.data.dto.data.HistoryRecord;
import ggc.pump.device.insulet.data.dto.data.LogRecord;
import ggc.pump.device.insulet.util.InsuletUtil;

/**
 * Created by andy on 19.05.15.
 */
public class InsuletReader extends AbstractDeviceReader
{

    private static final Logger LOG = LoggerFactory.getLogger(InsuletReader.class);

    List<Integer> dataPage;
    BitUtils bitUtils = new BitUtils();
    OmnipodDataConverter dataConverter;
    OmnipodConfigConverter configConverter;
    long fileLength;


    public InsuletReader(String filename, OutputWriter outputWriter) throws PlugInBaseException
    {
        super(outputWriter, false);
        dataPage = new ArrayList<Integer>();

        InsuletUtil.setOutputWriter(outputWriter);

        dataConverter = new OmnipodDataConverter(outputWriter);
        configConverter = new OmnipodConfigConverter(outputWriter);

        InsuletUtil.translateTypes();

        readFile(filename);
    }

    byte[] result;


    private void readFile(String aInputFileName) throws PlugInBaseException
    {
        // LOG.debug("Reading in binary file named : " + aInputFileName);
        File file = new File(aInputFileName);
        // LOG.debug("File size: " + file.length());
        fileLength = file.length();
        result = new byte[(int) file.length()];
        try
        {
            InputStream input = null;
            try
            {
                int totalBytesRead = 0;
                input = new BufferedInputStream(new FileInputStream(file));
                while (totalBytesRead < result.length)
                {
                    int bytesRemaining = result.length - totalBytesRead;
                    // input.read() returns -1, 0, or more :
                    int bytesRead = input.read(result, totalBytesRead, bytesRemaining);
                    if (bytesRead > 0)
                    {
                        totalBytesRead = totalBytesRead + bytesRead;
                    }
                }
                /*
                 * the above style is a bit tricky: it places bytes into the
                 * 'result' array;
                 * 'result' is an output parameter;
                 * the while loop usually has a single iteration only.
                 */
                // LOG.debug("Num bytes read: " + totalBytesRead);
            }
            finally
            {
                // LOG.debug("Closing input stream.");
                input.close();
            }
        }
        catch (FileNotFoundException ex)
        {
            LOG.debug("File not found.");
            throw new PlugInBaseException(PlugInExceptionType.ImportFileNotFound, new Object[] { aInputFileName });
        }
        catch (IOException ex)
        {
            LOG.debug("IO Exception: " + ex);
            throw new PlugInBaseException(PlugInExceptionType.ImportFileCouldNotBeRead,
                    new Object[] { aInputFileName, ex.getMessage() }, ex);
        }

        dataPage.clear();

        for (byte b : result) // int j = 0; j < dataFromFile.length; j++)
        {
            dataPage.add(bitUtils.convertUnsignedByteToInt(b));
        }

        // return result;
    }

    int offset = 0;


    @Override
    public void readData() throws PlugInBaseException
    {
        configureProgressReporter(ProgressType.Static, 100, 99, 0);
        readConfigurationInternal(false);

        boolean sameType = false;
        int size = 0;
        int[] rec;

        int log = 0, history = 0;

        do
        {
            rec = getRecord(dataPage, offset);

            if (size == 0)
            {
                size = rec.length;
                sameType = true;
            }
            else if (rec.length != size)
            {
                sameType = false;
                size = rec.length;
            }

            if (sameType)
            {
                processRecord(new LogRecord(), rec, true);
                log++;
            }

        } while (sameType);
        addToProgress(ProgressType.Static, 44);

        // this.offset -= rec.length;
        size = 0;

        do
        {
            do
            {
                rec = getRecord(dataPage, offset);

                if (size == 0)
                {
                    size = rec.length;
                    sameType = true;
                }
                else if (rec.length != size)
                {
                    sameType = false;
                    size = rec.length;
                }

                if (sameType)
                {
                    processRecord(new HistoryRecord(), rec, true);
                    history++;
                }

            } while ((sameType) && offset < dataPage.size());

        } while (offset < dataPage.size());

        addToProgress(ProgressType.Static, 44);

        System.out.println("Logs: " + log + ", History: " + history);

        dataConverter.postProcessData();
    }


    @Override
    public void readConfiguration() throws PlugInBaseException
    {
        configureProgressReporter(ProgressType.Static, 100, 11, 0);
        readConfigurationInternal(true);
    }


    @Override
    public void closeDevice() throws PlugInBaseException
    {
        // not used
    }


    private void readConfigurationInternal(boolean convert)
    {
        processRecord(new IbfVersion(), getRecord(dataPage, offset), convert);
        addToProgress(ProgressType.Static, 1);

        processRecord(new PdmVersion(), getRecord(dataPage, offset), convert);
        addToProgress(ProgressType.Static, 1);

        processRecord(new ManufacturingData(), getRecord(dataPage, offset), convert);
        addToProgress(ProgressType.Static, 1);

        processRecord(new BasalProgramNames(), getRecord(dataPage, offset), convert);
        addToProgress(ProgressType.Static, 1);

        processRecord(new EepromSettings(), getRecord(dataPage, offset), convert);
        addToProgress(ProgressType.Static, 1);

        boolean sameType = false;
        int size = 0;
        int[] rec;

        do
        {
            rec = getRecord(dataPage, offset);

            if (size == 0)
            {
                size = rec.length;
                sameType = true;
            }
            else if (rec.length != size)
            {
                sameType = false;
                size = rec.length;
            }

            if (sameType)
                processRecord(new Profile(), rec, convert);

        } while (sameType);
        addToProgress(ProgressType.Static, 3);

        processRecord(new LogDescriptions(), rec, convert);
        addToProgress(ProgressType.Static, 3);

    }


    @Override
    public void customInitAndChecks() throws PlugInBaseException
    {

    }


    @Override
    public void configureProgressReporter()
    {

    }


    private int[] getRecord(List<Integer> data, int offset)
    {
        int size = bitUtils.toInt(data.get(offset), data.get(offset + 1));

        int[] record = bitUtils.getIntSubArray(data, offset, size + 2);

        // crc check

        return record;
    }


    public void processRecord(AbstractRecord record, int[] data, boolean convert)
    {
        record.process(data);
        offset += record.getLength();

        if (convert)
        {
            if (record.getOmnipodDataType().isData())
            {
                dataConverter.convert(record);
            }
            else
            {
                configConverter.convert(record);
            }
        }
    }

}
