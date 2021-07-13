package ggc.connect.software.local.diasend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import ggc.connect.data.ConnectHandlerParameters;
import ggc.connect.enums.ConnectDataType;
import ggc.connect.enums.ConnectOperationType;
import ggc.connect.software.local.diasend.parser.*;
import ggc.connect.software.local.diasend.util.DiaSendUtil;
import ggc.plugin.data.enums.DeviceHandlerType;
import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.output.ConsoleOutputWriter;
import ggc.plugin.output.OutputWriter;

/**
 * Created by andy on 14/09/17.
 */
public class DiasendReader
{

    static Map<String, DiasendParser> parsers = null;
    private OutputWriter outputWriter;


    public DiasendReader() // throws PlugInBaseException
    {
        createParsers();
    }


    private DiasendParser getParser(String sheetName)
    {
        if (parsers.containsKey(sheetName))
            return parsers.get(sheetName);
        else
            return parsers.get("UNKNOWN");

    }


    private void createParsers()
    {
        parsers = new HashMap<String, DiasendParser>();

        parsers.put("Name and glucose", new DiasendParserNameAndGlucose());
        parsers.put("CGM", new DiasendParserCGM());
        parsers.put("Insulin use and carbs", new DiasendParserInsulinUseAndCarbs());
        parsers.put("Insulin pump settings", new DiasendParserInsulinPumpSettings());
        parsers.put("Alarms and events", new DiasendParserAlarmsAndEvents());

        parsers.put("UNKNOWN", new DiasendParserNoImplementation());
    }


    public Map<ConnectDataType, List<String>> createSummaryInformation(ConnectHandlerParameters parameters)
            throws PlugInBaseException
    {

        Map<ConnectDataType, List<String>> information = new HashMap<ConnectDataType, List<String>>();

        information.put(ConnectDataType.UnsupportedItems, new ArrayList<String>());
        information.put(ConnectDataType.ConfigurationItems, new ArrayList<String>());
        information.put(ConnectDataType.DataItems, new ArrayList<String>());

        int entries = 0;
        Workbook workbook = null;
        // String excelFilePath = fileName;
        FileInputStream inputStream = null;

        try
        {

            inputStream = new FileInputStream(new File(parameters.getFileName()));

            workbook = new HSSFWorkbook(inputStream);
            // Sheet firstSheet = workbook.getSheetAt(0);

            entries = workbook.getNumberOfSheets();

            for (int i = 0; i < workbook.getNumberOfSheets(); i++)
            {
                String sheetName = workbook.getSheetName(i);

                DiasendParser parser = getParser(sheetName);

                ConnectDataType dataType = ConnectDataType.UnsupportedItems;

                if (parser.isSupported())
                {
                    if (parser.isOperationSupported(ConnectOperationType.ImportData))
                    {
                        dataType = ConnectDataType.DataItems;
                    }
                    else if (parser.isOperationSupported(ConnectOperationType.ViewConfiguration))
                    {
                        dataType = ConnectDataType.ConfigurationItems;
                    }
                }

                information.get(dataType).add(sheetName);

            }

        }
        catch (FileNotFoundException e)
        {
            // FIXME
            e.printStackTrace();
            // throw e;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (workbook != null)
            {
                try
                {
                    workbook.close();
                }
                catch (IOException e)
                {

                }
            }

            if (inputStream != null)
            {
                try
                {
                    inputStream.close();
                }
                catch (IOException e)
                {

                }
            }
        }

        return entries == 0 ? null : information;

    }


    public void readData(ConnectHandlerParameters parameters, OutputWriter outputWriter) throws PlugInBaseException
    {
        String fileName;
        ConnectOperationType importContext;

        Workbook workbook = null;

        this.outputWriter = outputWriter;

        DiaSendUtil.setOutputWriter(outputWriter);

        // String excelFilePath = fileName;
        FileInputStream inputStream = null;
        try
        {
            inputStream = new FileInputStream(new File(parameters.getFileName()));

            workbook = new HSSFWorkbook(inputStream);
            // Sheet firstSheet = workbook.getSheetAt(0);

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            throw new PlugInBaseException(PlugInExceptionType.UnexpectedException, e);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new PlugInBaseException(PlugInExceptionType.UnexpectedException, e);
        }

        for (String sheetName : parameters.getSelectedItems())
        {
            DiasendParser parser = getParser(sheetName);

            parser.setOutputWriter(outputWriter);

            parser.parseExcelSheet(workbook.getSheet(sheetName), parameters.getActionType());
        }

        // for (int i = 0; i < workbook.getNumberOfSheets(); i++)
        // {
        // String sheetName = workbook.getSheetName(i);
        //
        // if (parameters.getSelectedItems().contains(sheetName))
        // {
        // DiasendParser parser = getParser(sheetName);
        //
        // if (parameters.getActionType()==ConnectOperationType.ViewConfiguration)
        // parser.parseExcelSheet(workbook.getS);
        //
        // }
        //
        //
        // DiasendParser parser = getParser(workbook.getSheetName(i));
        //
        // // parser.parseExcelSheet(workbook.getSheetAt(i), importContext);
        // }

        try
        {
            workbook.close();

            if (inputStream != null)
                inputStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) throws Exception
    {
        DiaSendUtil.setOutputWriter(new ConsoleOutputWriter());

        ConnectHandlerParameters parameters = new ConnectHandlerParameters();
        parameters.setFileName(
            "/home/andy/Dropbox/workspaces/ggc/ggc-desktop-app/ggc-desktop/src/andy.rozman@gmail.com.xls");

        parameters.setActionType(ConnectOperationType.ViewConfiguration);
        parameters.setDeviceHandlerType(DeviceHandlerType.DiaSendHandler);

        Set<String> selectedItems = new HashSet<String>();
        selectedItems.add("Insulin pump settings");

        parameters.setSelectedItems(selectedItems);

        DiasendReader dr = new DiasendReader();
        dr.readData(parameters, new ConsoleOutputWriter());

        // Iterator<Row> iterator = firstSheet.iterator();
        //
        // while (iterator.hasNext())
        // {
        // Row nextRow = iterator.next();
        // Iterator<Cell> cellIterator = nextRow.cellIterator();
        //
        // while (cellIterator.hasNext())
        // {
        // Cell cell = cellIterator.next();
        //
        // switch (cell.getCellType())
        // {
        // case Cell.CELL_TYPE_STRING:
        // System.out.print(cell.getStringCellValue());
        // break;
        // case Cell.CELL_TYPE_BOOLEAN:
        // System.out.print(cell.getBooleanCellValue());
        // break;
        // case Cell.CELL_TYPE_NUMERIC:
        // System.out.print(cell.getNumericCellValue());
        // break;
        // }
        // System.out.print(" - ");
        // }
        // System.out.println();
        // }

    }


    public OutputWriter getOutputWriter()
    {
        return outputWriter;
    }


    public void setOutputWriter(OutputWriter outputWriter)
    {
        this.outputWriter = outputWriter;
    }
}
