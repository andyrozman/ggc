package ggc.pump.gui.pdtc;

import java.util.Map;

import javax.swing.*;

import ggc.pump.data.PumpValuesEntry;

/**
 * Created by andy on 12/11/16.
 */
public class PumpDataTypeBolusHandler extends PumpDataTypeComponentHandlerAbstract
{

    public void setUIComponent(Map<String, JComponent> componentMap)
    {
        // JLabel label = (JLabel) componentMap.get("label_1");
        // label.setBounds(0, 20, 150, 25);
        // label.setVisible(true);
        // label.setText(i18nControl.getMessage("BOLUS_TYPE") + ":");
        //
        // // System.out.println("DHHHD" +
        // //
        // DataAccess.this.dataAccess.getBolusSubTypes().getStaticDescriptionArray());
        //
        // this.combo_1.setBounds(150, 20, 180, 25);
        // this.combo_1.setVisible(true);
        // this.combo_1.setActionCommand("bolus");
        //
        // // System.out.println("Bolus: " + PumpBolusType.getDescriptions());
        // addAllItems(this.combo_1, PumpBolusType.getDescriptions());
        // // this.combo_1.setSelectedIndex(1);
        //
        // this.button_1.setBounds(120, 20, 25, 25);
        // this.button_1.setVisible(true);
        //
        // this.label_2.setBounds(0, 55, 150, 25);
        // this.label_2.setVisible(true);
        // this.label_2.setText(i18nControl.getMessage("COMMENT") + ":");
        //
        // this.text_1.setBounds(150, 55, 180, 25);
        // this.text_1.setVisible(true);
        //
        // this.sub_type = -1;
        // this.setBolusSubType(PumpBolusType.Normal.getCode());

    }


    public void getUIHeight()
    {

    }


    public boolean isDataSetValid()
    {
        return false;
    }


    public void loadData(PumpValuesEntry data)
    {

    }


    public void saveData(PumpValuesEntry pumpValuesEntry)
    {

    }
}
