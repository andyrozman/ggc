package main.java.ggc.pump.gui.pdtc;

import java.util.Map;

import javax.swing.*;

import main.java.ggc.pump.data.PumpValuesEntry;
import main.java.ggc.pump.data.defs.PumpBaseType;

/**
 * Created by andy on 12/11/16.
 */
public interface PumpDataTypeComponentHandler
{

    void setUIComponent(Map<String, JComponent> componentMap);


    void getUIHeight();


    boolean isDataSetValid();


    PumpBaseType getPumpBaseType();


    void setPumpBaseType();


    void loadData(PumpValuesEntry data);


    void saveData(PumpValuesEntry pumpValuesEntry);

}
