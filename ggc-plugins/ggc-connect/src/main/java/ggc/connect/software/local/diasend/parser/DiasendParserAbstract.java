package ggc.connect.software.local.diasend.parser;

import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;

import ggc.connect.enums.ConnectOperationType;
import ggc.plugin.output.OutputWriter;

public abstract class DiasendParserAbstract implements DiasendParser
{

    private static final Logger LOG = LoggerFactory.getLogger(DiasendParserAbstract.class);

    protected OutputWriter outputWriter;


    public void parseExcelSheet(Sheet sheet, ConnectOperationType importContext)
    {
        if (importContext == ConnectOperationType.ImportData)
            parseExcelSheetImportData(sheet);
        else
            parseExcelSheetViewConfig(sheet);
    }


    protected void parseExcelSheetViewConfig(Sheet sheet)
    {
        if (isOperationSupported(ConnectOperationType.ViewConfiguration))
        {
            LOG.warn("Parsing Sheet for ViewConfig context [name={},parser={}] not implemented.", sheet.getSheetName(),
                getClass().getSimpleName());
        }
    }


    protected void parseExcelSheetImportData(Sheet sheet)
    {
        if (isOperationSupported(ConnectOperationType.ImportData))
        {
            LOG.warn("Parsing Sheet for ImportData context [name={},parser={}] not implemented.", sheet.getSheetName(),
                getClass().getSimpleName());
        }
    }


    public boolean isOperationSupported(ConnectOperationType connectOperationType)
    {
        return false;
    }


    public boolean isSupported()
    {
        return false;
    }


    public void setOutputWriter(OutputWriter outputWriter)
    {
        this.outputWriter = outputWriter;
    }


    protected ATechDate getATechDate(String value, ATechDateType type)
    {
        if (type.isTimeOnly())
        {
            String time[] = value.split(":");

            return new ATechDate(0, 0, 0, //
                    Integer.valueOf(time[0]), Integer.valueOf(time[1]), time.length == 3 ? Integer.valueOf(time[2]) : 0, //
                    type);
        }
        else
        {
            // 15/12/2014 07:31
            String[] valueSplit = value.split(" ");

            String date[] = valueSplit[0].split("/");

            String time[] = valueSplit[1].split(":");

            return new ATechDate(Integer.valueOf(date[0]), Integer.valueOf(date[1]), Integer.valueOf(date[2]), //
                    Integer.valueOf(time[0]), Integer.valueOf(time[1]), time.length == 3 ? Integer.valueOf(time[2]) : 0, //
                    type);
        }

    }

}
