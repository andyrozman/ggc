/**
 * 
 */
package ggc.core.data;

import com.atech.misc.converter.ATechConverter;

/**
 * @author Andy
 *
 */
public class Converter_mgdL_mmolL extends ATechConverter
{

    private static final float MGDL_TO_MMOL_FACTOR = 0.0555f;

    private static final float MMOL_TO_MGDL_FACTOR = 18.016f;
    
    
    /**
     * 
     */
    public Converter_mgdL_mmolL()
    {
        super(ATechConverter.BASETYPE_INT, 
              ATechConverter.BASETYPE_FLOAT,
              MGDL_TO_MMOL_FACTOR,
              MMOL_TO_MGDL_FACTOR);
        //ATechConverter.   int type1_type, int type2_type, float type1_2_type2, float type2_2_type1)
        // TODO Auto-generated constructor stub
    }

    public static void main(String args[])
    {
        System.out.println("Convert 9.3 mmol/L to 167 mg/dL");
        
        Converter_mgdL_mmolL cnv = new Converter_mgdL_mmolL();
        
        System.out.println("Value: " + cnv.getBGValueByType(2, 1, 9.3f));
        
        
    }
    
    
    
    
    
}
