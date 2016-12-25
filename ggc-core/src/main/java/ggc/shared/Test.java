package ggc.shared;

import java.math.BigDecimal;

/**
 * Created by andy on 05/10/16.
 */
public class Test
{

    public static void main(String[] args)
    {
        BigDecimal val1 = new BigDecimal(200);
        BigDecimal val2 = new BigDecimal(35.7);

        System.out.println("Val1: " + val1 + ", val2: " + val2 + " result: " + (val1.compareTo(val2) > 0));

    }

}
