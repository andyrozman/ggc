package ggc.pump.gui.pdtc;

import java.util.Map;

import javax.swing.*;

import ggc.pump.data.PumpValuesEntry;
import ggc.pump.data.defs.PumpBaseType;

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
