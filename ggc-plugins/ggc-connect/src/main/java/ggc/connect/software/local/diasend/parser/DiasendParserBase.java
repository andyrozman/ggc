package ggc.connect.software.local.diasend.parser;

import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by andy on 14/09/17.
 */
@Deprecated
public class DiasendParserBase extends DiasendParserAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(DiasendParserBase.class);


    public void parseExcelSheet(Sheet sheet)
    {
        LOG.info("Parsing Sheet [name={}] with {}", sheet.getSheetName(), getClass().getSimpleName());
    }

}
