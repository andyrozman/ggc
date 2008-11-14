package ggc.plugin.util;

import java.util.Comparator;


public class TimeZoneComparator implements Comparator<String>
{
 
    
    /*
     * Compare two TimeZones.
     * 
     * GMT- (12->1) < GMT < GMT+ (1->12)
     * 
     * @return +1 if less, 0 if same, -1 if greater
     */
    public final int compare(String pFirst, String pSecond)
    {

        // System.out.println(pFirst + " " + pSecond);

        if (areSameType(pFirst, pSecond))
        {
            return ((pFirst.compareTo(pSecond)) * typeChanger(pFirst, pSecond));
        }
        else
        {
            if ((pFirst.startsWith("(GMT-"))) // && (second.contains("(GMT)")))
            {
                return -1;
            }
            else if ((pFirst.startsWith("(GMT)"))) // &&
                                                   // (second.contains("(GMT)"
                                                   // )))
            {
                if (pSecond.startsWith("(GMT-"))
                    return 1;
                else
                    return -1;
            }
            else
            {
                return 1;
            }

        }
    } // end compare

    public int typeChanger(String first, String second)
    {
        if ((first.startsWith("(GMT-")) && (second.startsWith("(GMT-")))
            return -1;
        else
            return 1;
    }

    public boolean areSameType(String first, String second)
    {
        if (((first.startsWith("(GMT+")) && (second.startsWith("(GMT+")))
                || ((first.startsWith("(GMT)")) && (second.startsWith("(GMT)")))
                || ((first.startsWith("(GMT-")) && (second.startsWith("(GMT-"))))
        {
            return true;
        }
        else
            return false;

    }

}
