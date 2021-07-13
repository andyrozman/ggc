package ggc.connect.software.local.diasend.parser;

import org.apache.poi.ss.usermodel.Sheet;

import ggc.connect.enums.ConnectOperationType;
import ggc.plugin.output.OutputWriter;

/**
 * Created by andy on 14/09/17.
 */
public interface DiasendParser
{

    boolean isOperationSupported(ConnectOperationType connectOperationType);


    void parseExcelSheet(Sheet sheet, ConnectOperationType connectOperationType);


    boolean isSupported();


    void setOutputWriter(OutputWriter outputWriter);
}
