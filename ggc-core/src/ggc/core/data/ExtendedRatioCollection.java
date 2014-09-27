package ggc.core.data;

import ggc.core.util.DataAccess;
import ggc.shared.ratio.RatioEntry;

import java.util.ArrayList;
import java.util.Hashtable;

public class ExtendedRatioCollection extends ArrayList<RatioEntry>
{

    DataAccess m_da = DataAccess.getInstance();

    public ExtendedRatioCollection()
    {
        super();
    }

    public void load()
    {

        Hashtable<String, String> dta = m_da.getDb().getExtendedRationEntries();
        // EXTENDED_RATIO_COUNT=2

        // EXTENDED_RATIO_1=From;To;Ch/Ins;Bg/Ins;Ch/BG;Procent

        /*
         * Hashtable<String,String> dta = new Hashtable<String,String>();
         * dta.put("EXTENDED_RATIO_COUNT", "2");
         * dta.put("EXTENDED_RATIO_1", "0000;1159;6.67f;1.33f;5f;100");
         * dta.put("EXTENDED_RATIO_2", "1200;2259;6.67f;1.33f;5f;100");
         */

        if (dta.size() == 0)
            return;

        int count = 0;

        if (dta.containsKey("EXTENDED_RATIO_COUNT"))
        {
            count = m_da.getIntValueFromString(dta.get("EXTENDED_RATIO_COUNT"), 0);
        }

        if (count == 0)
            return;

        for (int i = 1; i <= count; i++)
        {
            this.add(new RatioEntry(dta.get("EXTENDED_RATIO_" + i)));
        }
    }

    public RatioEntry getRatioEntryByTime(String time)
    {
        long t = Long.parseLong(time.replace(":", ""));

        for (int i = 0; i < this.size(); i++)
        {
            if (t >= this.get(i).from && t <= this.get(i).to)
                return this.get(i);
        }

        return null;
    }

    public boolean save()
    {
        return m_da.getDb().saveExtendedRatioEntries(this);
    }

}
