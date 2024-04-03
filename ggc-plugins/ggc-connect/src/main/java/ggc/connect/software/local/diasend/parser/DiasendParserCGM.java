package ggc.connect.software.local.diasend.parser;

import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;

import ggc.cgms.data.writer.CGMSValuesWriter;
import ggc.connect.enums.ConnectOperationType;
import ggc.core.data.defs.GlucoseUnitType;
import ggc.core.util.DataAccess;

/**
 * Created by andy on 14/09/17.
 */
public class DiasendParserCGM extends DiasendParserAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(DiasendParserCGM.class);


    public boolean isOperationSupported(ConnectOperationType connectOperationType)
    {
        return (connectOperationType == ConnectOperationType.ImportData);
    }


    public boolean isSupported()
    {
        return true;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void parseExcelSheetImportData(Sheet sheet)
    {
        // Time mmol/L

        Iterator<Row> iterator = sheet.iterator();

        iterator.next(); // Date

        // Time row
        Row timeRow = iterator.next();
        Cell unitCell = timeRow.getCell(1);

        GlucoseUnitType unitType = unitCell.getStringCellValue().equals("mmol/L") ? GlucoseUnitType.mmol_L
                : GlucoseUnitType.mg_dL;

        CGMSValuesWriter cgmsValuesWriter = CGMSValuesWriter.getInstance(this.outputWriter);

        while (iterator.hasNext())
        {
            Row nextRow = iterator.next();

            // 15/12/2014 07:31

            ATechDate dateTime = getATechDate(nextRow.getCell(0).getStringCellValue(), ATechDateType.DateAndTimeSec);
            double bgValueDouble = nextRow.getCell(1).getNumericCellValue();

            // System.out.println("Cell count: " + nextRow.getLastCellNum() + " " + dateTime + " " +
            // bgValueDouble);

            String comment = (nextRow.getLastCellNum() == 3) ? nextRow.getCell(2).getStringCellValue() : "";

            int bgValue = DataAccess.getInstance().getBGValueByType(unitType, GlucoseUnitType.mg_dL, bgValueDouble)
                    .intValue();

            String entryType = comment.contains("CGM calibration") ? "Event_SensorCalibrationWithMeter"
                    : "SensorReading";

            cgmsValuesWriter.writeObject(entryType, dateTime, bgValue);

            System.out.println(entryType + //
                    " Dt: " + dateTime + " Value: " + bgValueDouble);

        }

    }

}
