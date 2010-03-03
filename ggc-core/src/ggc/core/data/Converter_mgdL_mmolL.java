package ggc.core.data;

import com.atech.misc.converter.ATechConverter;

/**
 * @author Andy
 *
 */
public class Converter_mgdL_mmolL extends ATechConverter
{
    
    /**
     * Unit: mg/dL
     */
    public static final int UNIT_mg_dL = 1;
    
    /**
     * Unit: mmol/L
     */
    public static final int UNIT_mmol_L = 2;
    

    private static final float MGDL_TO_MMOL_FACTOR = 0.0555f;

    private static final float MMOL_TO_MGDL_FACTOR = 18.016f;
    
    
    /**
     * 
     */
    public Converter_mgdL_mmolL()
    {
        super(UNIT_mg_dL,                     // unit #1
              UNIT_mmol_L,                    // unit #2
              ATechConverter.BASETYPE_INT,    // unit #1 type 
              ATechConverter.BASETYPE_FLOAT,  // unit #2 type
              "mmol/L",                       // unit #1 name 
              "mg/dL",                        // unit #2 name 
              MGDL_TO_MMOL_FACTOR,            // factor 1 -> 2 
              MMOL_TO_MGDL_FACTOR,            // factor 2 -> 1
              0,                              // decimal precission (nr. of decimals) unit #1 
              1);                             // decimal precission (nr. of decimals) unit #2  

    }

    /**
     * @param args
     */
    public static void main(String args[])
    {
        System.out.println("Convert 9.3 mmol/L to 167 mg/dL");
        Converter_mgdL_mmolL cnv = new Converter_mgdL_mmolL();
        
        //System.out.println("Value: " + cnv.getBGValueByType(2, 1, 9.3f));
        //System.out.println("Value: " + cnv.getBGValueByType(UNIT_mmol_L, UNIT_mg_dL, 11.6f));
        System.out.println("Value: " + cnv.getValueByType(UNIT_mg_dL, UNIT_mmol_L, 209));
    }
    
    
    
    
    
}
