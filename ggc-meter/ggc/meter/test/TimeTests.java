package ggc.meter.test;

import ggc.meter.util.DataAccessMeter;

import java.util.GregorianCalendar;

import com.atech.utils.TimeZoneUtil;

public class TimeTests
{
	
	DataAccessMeter m_da = DataAccessMeter.getInstance();
	//TimeZone tzi;
	TimeZoneUtil tzu = TimeZoneUtil.getInstance();
	
	public TimeTests()
	{
		
		
		//tzi = TimeZone.getTimeZone("Europe/Prague");
		
		tzu.setTimeZone("Europe/Prague");
		tzu.setWinterTimeChange(0);
		tzu.setSummerTimeChange(-1);
		
		
		GregorianCalendar gc1 = new GregorianCalendar();
		gc1.set(GregorianCalendar.DAY_OF_MONTH, 1);
		gc1.set(GregorianCalendar.MONTH, 0);
		gc1.set(GregorianCalendar.YEAR, 2007);
		

		GregorianCalendar gc2 = new GregorianCalendar();
		gc2.set(GregorianCalendar.DAY_OF_MONTH, 1);
		gc2.set(GregorianCalendar.MONTH, 7);
		gc2.set(GregorianCalendar.YEAR, 2007);
		
		
		System.out.println("1.1 DST: "); // + tzi.inDaylightTime(gc1.getTime()));
		System.out.println("  Winter time:" + tzu.IsWinterTime(gc1));
		System.out.println("  Summer time:" + tzu.IsSummerTime(gc1));
		
		
		System.out.println("1.8 DST: "); // + tzi.inDaylightTime(gc2.getTime()));
		System.out.println("  Winter time:" + tzu.IsWinterTime(gc2));
		System.out.println("  Summer time:" + tzu.IsSummerTime(gc2));
		
		
		//tzi.
/*
		Vector<String> tzs = getTimeZoneDescs();
		
		for(int i=0; i<tzs.size(); i++ )
		{
			System.out.println("" + tzs.get(i));
			
		} */
//		Key: (GMT+01:00) Belgrade, Bratislava, Budapest, Ljubljana, Prague Value: Europe/Prague
		// ,Europe/Belgrade
		
	}

	
	/*
	public boolean IsWinterTime(GregorianCalendar gc)
	{
		return (!tzi.inDaylightTime(gc.getTime()));
	}
	
	public boolean IsSummerTime(GregorianCalendar gc)
	{
		return (tzi.inDaylightTime(gc.getTime()));
		
	}
	
	
    public Vector<String> getTimeZoneDescs()
    {
        Vector<String> vec = new Vector<String>();

        for(Enumeration<String> en = m_da.timeZones.keys(); en.hasMoreElements(); )
        {
        	String key = en.nextElement();
            System.out.println("Key: " + key + " Value: " + m_da.timeZones.get(key));
            vec.add(key);
        }

        Collections.sort(vec, new TimeZoneComparator());
        

        return vec;
    }
	
	
	
    public String getTimeZoneKeyFromValue(String value)
    {
        for(Enumeration<String> en = m_da.timeZones.keys(); en.hasMoreElements(); )
        {
            String key = en.nextElement();

            System.out.println(m_da.timeZones.get(key) + " = " + value);

            if (m_da.timeZones.get(key).contains(value))
            {
                return key;
            }
        }

        return "(GMT) Greenwich Mean Time : Dublin, Edinburgh, Lisbon, London";
    }
*/


/*	
	private class TimeZoneComparator implements Comparator<String>
    {
     /**
       * Compare two TimeZones. 
       *
       *  GMT- (12->1) < GMT < GMT+ (1->12)
       *
       * @return +1 if less, 0 if same, -1 if greater
       */
/*       public final int compare ( String pFirst, String pSecond )
       {

           //System.out.println(pFirst + " " + pSecond);

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
               else if ((pFirst.startsWith("(GMT)"))) // && (second.contains("(GMT)")))
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
           if (((first.startsWith("(GMT+")) && (second.startsWith("(GMT+"))) ||
               ((first.startsWith("(GMT)")) && (second.startsWith("(GMT)"))) ||
               ((first.startsWith("(GMT-")) && (second.startsWith("(GMT-"))))
           {
               return true;
           }
           else
               return false;

       }


    }
*/
    
    
    
    
	public static void main(String args[])
	{
		new TimeTests();
		
	}


}
