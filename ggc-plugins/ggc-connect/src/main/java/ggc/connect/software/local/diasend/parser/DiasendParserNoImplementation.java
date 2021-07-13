package ggc.connect.software.local.diasend.parser;

import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.connect.enums.ConnectOperationType;

/**
 * Created by andy on 14/09/17.
 */
public class DiasendParserNoImplementation extends DiasendParserAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(DiasendParserNoImplementation.class);


    /**
     * {@inheritDoc}
     */
    @Override
    protected void parseExcelSheetViewConfig(Sheet sheet)
    {
        LOG.error("Parsing Sheet for ViewConfig context NOT IMPLEMENTED/AVAILABLE [name={}].", sheet.getSheetName());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void parseExcelSheetImportData(Sheet sheet)
    {
        LOG.error("Parsing Sheet for ImportData context NOT IMPLEMENTED/AVAILABLE [name={}].", sheet.getSheetName());
    }


    public boolean isOperationSupported(ConnectOperationType connectOperationType)
    {
        return false;
    }


    public boolean isSupported()
    {
        return false;
    }
}
