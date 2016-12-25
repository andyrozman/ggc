package main.java.ggc.pump.test.minimed;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.BitUtils;

import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.data.MinimedCommandReply;
import ggc.plugin.device.impl.minimed.data.MinimedDataPage;
import ggc.plugin.device.impl.minimed.enums.MinimedCommandType;
import ggc.plugin.device.impl.minimed.enums.MinimedDeviceType;
import ggc.plugin.device.impl.minimed.enums.MinimedTargetType;
import ggc.plugin.device.impl.minimed.util.MinimedUtil;
import ggc.plugin.output.ConsoleOutputWriter;
import main.java.ggc.pump.device.minimed.data.decoder.MinimedPumpHistoryDecoder;
import main.java.ggc.pump.test.AbstractPumpTest;

/**
 * Created by andy on 27.03.15.
 */

public class DecoderLoadData extends AbstractPumpTest
{

    private static final Logger LOG = LoggerFactory.getLogger(DecoderLoadData.class);
    int[] dataFromFile;
    List<Byte> dataClear = new ArrayList<Byte>();
    byte[] rawData;

    static BitUtils bitUtils = new BitUtils();

    // PumpHistoryRecordsCreator creator;
    MinimedPumpHistoryDecoder decoderPump;

    boolean isPumpMode = true;


    public DecoderLoadData(boolean isPump)
    {
        this.initDb = false;
        prepareContext();

        MinimedUtil.setOutputWriter(new ConsoleOutputWriter());
        MinimedUtil.setDeviceType(MinimedDeviceType.Minimed_512_712);

        isPumpMode = isPump;

        if (isPump)
        {
            decoderPump = new MinimedPumpHistoryDecoder();
        }

        // creator = new PumpHistoryRecordsCreator(new ConsoleOutputWriter());

    }


    public void createEntries()
    {
        // FIXME
        // creator.createRecords(this.dataClear);
    }


    public void testMy512()
    {
        String fileName = "HistoryData_0.bin";

        ObjectInputStream inputStream = null;
        try
        {
            inputStream = new ObjectInputStream(new FileInputStream("/home/andy/Dropbox/GGC/testdata/511/" + fileName));

            // readFile("/home/andy/Dropbox/GGC/testdata/511/" + fileName);

            MinimedCommandReply mcr = new MinimedCommandReply(MinimedCommandType.HistoryData);

            // int number = inputStream.readInt();

            // System.out.println("Number: " + number);number

            byte[] rawData = new byte[1024];

            inputStream.read(rawData); // .readObject();

            System.out.println("RawData: " + rawData.length);

            this.rawData = rawData;

            // createPageAndDecodePump();

            //
            // mcr.setRawData(rawData);
            //
            // MinimedDataPage mdp = new MinimedDataPage(mcr);
            // mdp.setTargetType(MinimedTargetType.Pump);
            //
            // decoderPump.decodePage(mdp);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    public void createPageAndDecode()
    {
        if (isPumpMode)
            createPageAndDecodePump();

    }


    public void createPageAndDecodePump()
    {
        MinimedCommandReply mcr = new MinimedCommandReply(MinimedCommandType.HistoryData);

        mcr.setRawData(rawData);

        MinimedDataPage mdp = new MinimedDataPage(mcr);
        mdp.setTargetType(MinimedTargetType.Pump);

        try
        {
            decoderPump.decodePage(mdp);
        }
        catch (PlugInBaseException ex)
        {
            LOG.error("Exception: {}", ex.getMessage(), ex);
        }
    }


    // private void processData()
    // {
    // int counter = 0;
    // int record = 0;
    //
    // do
    // {
    // int opCode = dataClear.get(counter);
    // boolean special = false;
    //
    // PumpHistoryEntryType entryType = PumpHistoryEntryType.getByCode(opCode);
    //
    // PumpHistoryEntry pe = new PumpHistoryEntry();
    // pe.setEntryType(entryType);
    //
    // counter++;
    //
    // List<Byte> listRawData = new ArrayList<Byte>();
    // listRawData.add((byte) opCode);
    //
    // if (entryType == PumpHistoryEntryType.UnabsorbedInsulinBolus)
    // {
    // for (int k = 0; k < 50; k++)
    // {
    // if (counter == this.dataClear.size())
    // break;
    //
    // if (dataClear.get(counter) != PumpHistoryEntryType.Bolus.getOpCode())
    // {
    // listRawData.add(dataClear.get(counter));
    // counter++;
    // }
    // else
    // {
    // break;
    // }
    // }
    //
    // special = true;
    // }
    // else
    // {
    // for (int j = 0; j < (entryType.getTotalLength() - 1); j++)
    // {
    // listRawData.add(dataClear.get(counter));
    // counter++;
    // }
    // }
    //
    // // counter += (entryType.getTotalLength() - 1);
    //
    // if (entryType == PumpHistoryEntryType.None)
    // {
    // System.out.println("!!! Unknown Entry: 0x" +
    // bitUtils.getCorrectHexValue(opCode) + "[" + opCode + "]");
    // }
    // else
    // {
    // pe.setData(listRawData, special);
    // // System.out.println("Found entry: " + entryType.name() + " ["
    // // + bitUtils.getDebugByteListHex(listRawData) + "] ");
    // System.out.println("#" + record + " " + pe);
    // record++;
    // }
    //
    // // System.out.println("Counter: " + counter);
    //
    // // if (counter > 100)
    // // break;
    //
    // } while (counter < dataClear.size());
    //
    // }

    private void loadData_AndyAll()
    {
        String path = "/mnt/d/mm/";

        // mm_history_cmd_128_page_0.data

        // for (int i = 0; i < 15; i++)
        // {
        // dataFromFile = readFile(path + "mm_history_cmd_128_page_" + i +
        // ".data");
        //
        // for (int j = 0; j < dataFromFile.length; j += 4)
        // {
        // // System.out.print(" " + dataFromFile[j + 3]);
        // dataClear.add(dataFromFile[j + 3]);
        // }
        // }

        // dataFromFile = readFile(path + "mm_history_cmd_128_page_0.data");
        //
        // LOG.info("Output: " + bitUtils.getDebugByteArrayHex(dataFromFile));
        //
        // for (int i = 0; i < dataFromFile.length; i = i + 4)
        // {
        // //System.out.print(" " + dataFromFile[i + 3]);
        // dataClear.add(dataFromFile[i + 3]);
        // }
        // System.out.println(" ");

        // LOG.info("Output: " + bitUtils.getDebugByteListHex(dataClear));

    }


    public void readStandardIntoList()
    {
        for (int j = 0; j < dataFromFile.length; j++)
        {
            dataClear.add((byte) dataFromFile[j]);
        }
    }


    public void loadData_578398_Cgms()
    {
        int page = 6;
        String path = "/home/andy/Dropbox/GGC/others/dcl/cgms/578398/";
        readFile(path + "ReadGlucoseHistory-page-" + page + ".data");
        MinimedUtil.setDeviceType(MinimedDeviceType.Minimed_523_723);
    }


    public void loadData_analysis_342725(int page)
    {
        // 523
        String path = "/home/andy/Dropbox/GGC/others/dcl/pump/342725/logs/";
        readFile(path + "ReadHistoryData-page-" + page + ".data");
        MinimedUtil.setDeviceType(MinimedDeviceType.Minimed_523_723);
    }


    private void readFile(String aInputFileName)
    {
        LOG.debug("Reading in binary file named : " + aInputFileName);
        File file = new File(aInputFileName);
        LOG.debug("File size: " + file.length());
        rawData = new byte[(int) file.length()];
        try
        {
            InputStream input = null;
            try
            {
                int totalBytesRead = 0;
                input = new BufferedInputStream(new FileInputStream(file));
                while (totalBytesRead < rawData.length)
                {
                    int bytesRemaining = rawData.length - totalBytesRead;
                    // input.read() returns -1, 0, or more :
                    int bytesRead = input.read(rawData, totalBytesRead, bytesRemaining);
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
                LOG.debug("Num bytes read: " + totalBytesRead);
            }
            finally
            {
                LOG.debug("Closing input stream.");
                input.close();
            }
        }
        catch (FileNotFoundException ex)
        {
            LOG.debug("File not found.");
        }
        catch (IOException ex)
        {
            LOG.debug(ex.getMessage(), ex);
        }

        dataClear.clear();

        for (byte b : rawData) // int j = 0; j < dataFromFile.length; j++)
        {
            dataClear.add(b); // bitUtils.convertUnsignedByteToInt(b));
        }

        MinimedUtil.setDeviceType(MinimedDeviceType.Minimed_512_712);

        // return result;
    }


    public static void main(String args[])
    {

        // DataAccess daCore = DataAccess.getInstance();
        //
        // LanguageManager lm = new LanguageManager(new
        // GGCLanguageManagerRunner()); // ,
        // // new
        // // GGCCoreICRunner()));
        //
        // DataAccessPump dap = DataAccessPump.createInstance(lm);
        // dap.initSpecial();

        // FIXME
        // DecoderLoadData ld = new DecoderLoadData(true);
        // // ld.loadData();
        // ld.testMy512();
        // // ld.loadData_AndyAll();
        // // ld.loadData_analysis_342725(13); // 14
        // ld.createPageAndDecodePump();

        DecoderLoadData ld = new DecoderLoadData(false);
        ld.loadData_578398_Cgms();
        ld.createPageAndDecode();

        // static part
        // ld.readStandardIntoList();
        // ld.createEntries();

        // ld.processData();
    }

}
