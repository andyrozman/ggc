package ggc.misc;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        String errorCode = "AddNetworkService:[AVAC][GPRS_ADD]";

        Matcher matcher = Pattern.compile("(\\[\\w+\\]\\[\\w+\\])").matcher(errorCode);
        if (matcher.find())
        {
            System.out.println(matcher.group());
        }

    }

}
