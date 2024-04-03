package ggc.connect.software.local.diasend.parser;

import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by andy on 14/09/17.
 */
public class DiasendParserInsulinUseAndCarbs extends DiasendParserAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(DiasendParserInsulinUseAndCarbs.class);


    /**
     * {@inheritDoc}
     */
    @Override
    protected void parseExcelSheetViewConfig(Sheet sheet)
    {
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void parseExcelSheetImportData(Sheet sheet)
    {
        // FIXME
        super.parseExcelSheetImportData(sheet);
    }

}
