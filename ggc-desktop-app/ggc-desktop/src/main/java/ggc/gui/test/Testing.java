package ggc.gui.test;

import java.math.BigDecimal;

import ggc.core.util.DataAccess;

// TODO: Auto-generated Javadoc
/**
 * The Class Testing.
 */
public class Testing
{

    private String ins_pen = "1";
    private String ins_pump = "0.01";

    DataAccess dataAccess = DataAccess.createInstance(null);

    /**
     * The Constant INSULIN_PEN_INJECTION.
     */
    public static final int INSULIN_PEN_INJECTION = 1;

    /**
     * The Constant INSULIN_PUMP.
     */
    public static final int INSULIN_PUMP = 2;


    /**
     * Gets the correct decimal value for insulin float.
     * 
     * @param mode the mode
     * @param value the value
     * 
     * @return the correct decimal value for insulin float
     */
    public float getCorrectDecimalValueForInsulinFloat(int mode, float value)
    {
        // String tt = "1";
        int dec = getDecimalCount(mode);

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(dec, BigDecimal.ROUND_HALF_DOWN);
        // RoundingMOde.

        return bd.floatValue();

        // 1, 0.5, 0.1
        // 1, 0.5, 0.1, 0.05, 0.01, 0.005, 0.001
        // return value;
    }


    /**
     * Gets the correct decimal value for insulin string.
     * 
     * @param mode the mode
     * @param value the value
     * 
     * @return the correct decimal value for insulin string
     */
    public String getCorrectDecimalValueForInsulinString(int mode, float value)
    {
        int dec = getDecimalCount(mode);

        return dataAccess.getFloatAsString(value, dec);

    }


    /**
     * Gets the decimal count.
     * 
     * @param type the type
     * 
     * @return the decimal count
     */
    public int getDecimalCount(int type)
    {
        if (type == INSULIN_PEN_INJECTION)
            return getDecimalCount(ins_pen);
        else
            return getDecimalCount(ins_pump);
    }


    /**
     * Gets the decimal count.
     * 
     * @param val the val
     * 
     * @return the decimal count
     */
    public int getDecimalCount(String val)
    {
        if (!val.contains("."))
            return 0;
        else
            return val.substring(val.indexOf(".") + 1).length();
    }


    /**
     * The main method.
     * 
     * @param args the arguments
     */
    public static final void main(String[] args)
    {
        Testing t = new Testing();

        // 1, 0.5, 0.1, 0.05, 0.01, 0.005, 0.001

        System.out.println("getDecimalCount: " + t.getDecimalCount("0.005"));

        System.out.println("getDecimal [2.16]: " + t.getCorrectDecimalValueForInsulinFloat(INSULIN_PUMP, 2.16f));

    }

}
