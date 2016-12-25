package main.java.ggc.pump.data;

import com.atech.misc.statistics.StatisticsCollection;

import ggc.core.data.defs.GlucoseUnitType;
import main.java.ggc.pump.data.defs.PumpBaseType;
import main.java.ggc.pump.util.DataAccessPump;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 *
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 *
 *  Filename:  PumpDailyStatistics
 *  Description: Pump Statistics (excluding Basal, see BasalStatistics object for that)
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class PumpDailyStatistics extends StatisticsCollection
{

    // public int bg_type = -1;
    DataAccessPump da_pump = DataAccessPump.getInstance();


    /**
     * Constructor
     */
    public PumpDailyStatistics()
    {
        super(DataAccessPump.getInstance(), new PumpValuesEntry());
    }


    /**
     * Process Special Statistics
     *
     * @see com.atech.misc.statistics.StatisticsCollection#processSpecialStatistics()
     */
    @Override
    public void processSpecialStatistics()
    {
        if (this.items.size() == 0)
        {
            this.special_processed = true;
            return;
        }

        // System.out.println("SS: " + da_pump.getGlucoseUnitType());
        if (da_pump.getGlucoseUnitType() == GlucoseUnitType.mmol_L)
        {
            // setValue(PumpValuesEntry.BG_AVG,
            // da_pump.getBGValueByType(DataAccessPump.BG_MGDL, output_type,
            // bg_value))
            setBGValue(PumpValuesEntry.BG_AVG);
            setBGValue(PumpValuesEntry.BG_MIN);
            setBGValue(PumpValuesEntry.BG_MAX);
        }
        // int ijk = PumpValuesEntry.CH_AVG;

        setValue(PumpValuesEntry.BG_STD_DEV, getStandardDeviation());

        // ArrayList<PumpValuesEntry> lst = new ArrayList<PumpValuesEntry>();

        // for (int i = 0; i < this.items.size(); i++)
        // {
        // PumpValuesEntry pve = (PumpValuesEntry) this.items.get(i);
        //
        // if (pve.baseType == PumpBaseType.Basal)
        // {
        // lst.add(pve);
        // }
        // }

        // float v = this.stat_objects.get(index-1).sum;

        // // int count = 0;
        // float sum = 0;
        //
        // for (int i = 0; i < lst.size(); i++)
        // {
        // PumpValuesEntry pve = lst.get(i);
        //
        // if (isCurrentlyIgnoredEntry(pve))
        // {
        // continue;
        // }
        //
        // // if ((pve.base_type == PumpBaseType.PUMP_DATA_BASAL) &&
        // // (pve.sub_type == PumpBasalSubType.PUMP_BASAL_PROFILE))
        // // continue;
        //
        // if (i + 1 == lst.size())
        // {
        // int s = 24 - pve.getDateTimeObject().getHourOfDay();
        // float val = dataAccess.getFloatValueFromString(pve.getValue());
        // sum += s * val;
        //
        // // System.out.println("Time diff: " + s + ", val=" + val);
        // }
        // else
        // {
        // PumpValuesEntry pve2 = lst.get(i + 1);
        //
        // if (isCurrentlyIgnoredEntry(pve2))
        // {
        // continue;
        // }
        //
        // // if ((pve2.base_type == PumpBaseType.PUMP_DATA_BASAL) &&
        // // (pve2.sub_type == PumpBasalSubType.PUMP_BASAL_PROFILE))
        // // continue;
        //
        // // System.out.println("Hour: " +
        // // pve2.getDateTimeObject().hour_of_day + ", hour2=" +
        // // pve.getDateTimeObject().hour_of_day);
        //
        // // System.out.println("pve2: " + pve2.getBaseTypeString() +
        // // pve2.getSubTypeString());
        //
        // int s = pve2.getDateTimeObject().getHourOfDay() -
        // pve.getDateTimeObject().getHourOfDay();
        // float val = da_pump.getFloatValueFromString(pve.getValue());
        // sum += s * val;
        //
        // // System.out.println("Time diff: " + s + ", val=" + val);
        // }
        //
        // }
        //
        // this.stat_objects.get(PumpValuesEntry.INS_SUM_BASAL - 1).setSum(sum);
        // this.stat_objects.get(PumpValuesEntry.INS_AVG_BASAL - 1).setSum(sum /
        // 24.0f);
        // this.stat_objects.get(PumpValuesEntry.INS_DOSES_BASAL - 1).setSum(0);

        this.stat_objects.get(PumpValuesEntry.INS_SUM_TOGETHER - 1)
                .setSum(this.getValueInternal(PumpValuesEntry.INS_SUM_BASAL)
                        + this.getValueInternal(PumpValuesEntry.INS_SUM_BOLUS));

        // System.out.println("Avg Basal: " +
        // this.getValueInternal(PumpValuesEntry.INS_AVG_BASAL) + "Avg Bolus: "
        // + this.getValueInternal(PumpValuesEntry.INS_AVG_BOLUS));

        // this.stat_objects.get(PumpValuesEntry.INS_AVG_TOGETHER-1).setSum(this.getValueInternal(PumpValuesEntry.INS_AVG_BASAL)
        // + this.getValueInternal(PumpValuesEntry.INS_AVG_BOLUS));
        this.stat_objects.get(PumpValuesEntry.INS_DOSES_TOGETHER - 1)
                .setSum(this.getValueInternal(PumpValuesEntry.INS_DOSES_BOLUS));

        // this.stat_objects.get(PumpValuesEntry.INS_SUM_BASAL-1).setCount(lst.size());

        // this.stat_objects.get(PumpValuesEntry.INS_AVG_BASAL-1).setSum(sum/24.0f);
        // this.stat_objects.get(PumpValuesEntry.INS_SUM_BASAL-1).setCount(lst.size());

        // public static final int BG_AVG =13;
        // public static final int BG_MAX =14;
        // public static final int BG_MIN =15;
        // public static final int BG_COUNT =16;
        // public static final int BG_STD_DEV =17;

        this.special_processed = true;
    }


    private boolean isCurrentlyIgnoredEntry(PumpValuesEntry pve)
    {
        if (pve.baseType == PumpBaseType.Basal)
        {
            return true; // we ignore whole basals
            // return pve.sub_type != PumpBasalType.Value.getCode();
        }
        else
            return false;
    }


    private float getStandardDeviation()
    {
        float f = this.getValueInternal(PumpValuesEntry.BG_AVG) - this.getValueInternal(PumpValuesEntry.BG_MIN);

        if (f < 0)
        {
            setValue(PumpValuesEntry.BG_MIN, 0.0f);
            return 0.0f;
        }
        else
            return f;
    }


    private void setBGValue(int index)
    {
        float v = this.stat_objects.get(index - 1).sum;
        float new_val = da_pump.getBGValueDifferent(GlucoseUnitType.mg_dL, v);

        setValue(index, new_val);
    }


    private float getValueInternal(int index)
    {
        return this.stat_objects.get(index - 1).getStatistics();
    }


    private void setValue(int index, float val)
    {
        this.stat_objects.get(index - 1).sum = val;
    }

}
