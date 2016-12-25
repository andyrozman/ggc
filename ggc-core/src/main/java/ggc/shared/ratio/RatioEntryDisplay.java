package ggc.shared.ratio;

import com.atech.graphics.components.ATTableData;
import com.atech.i18n.I18nControlAbstract;

/**
 *  Application:   GGC - GNU Gluco Control
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
 *  Filename:     NutritionDataDisplay
 *  Description:  Nutrition Data Display
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class RatioEntryDisplay extends ATTableData
{

    private RatioEntry ratio_entry = null;

    /**
     * Constructor
     * 
     * @param ic
     */
    public RatioEntryDisplay(I18nControlAbstract ic)
    {
        super(ic);
    }

    /**
     * Constructor
     * 
     * @param ic
     * @param full
     */
    public RatioEntryDisplay(I18nControlAbstract ic, String full)
    {
        super(ic);
    }

    /**
     * Constructor
     * 
     * @param ic
     * @param re
     */
    public RatioEntryDisplay(I18nControlAbstract ic, RatioEntry re)
    {
        super(ic);
        this.ratio_entry = re;
    }

    /**
     * Init
     * 
     * @see com.atech.graphics.components.ATTableData#init()
     */
    @Override
    public void init()
    {
        String[] col = { "FROM", "TO", "CH_INS_RATIO", "BG_INS_RATIO", "%" };
        float[] col_size = { 0.2f, 0.2f, 0.2f, 0.2f, 0.2f };

        init(col, col_size);
    }

    /**
     * Get Column Value
     * 
     * @see com.atech.graphics.components.ATTableData#getColumnValue(int)
     */
    @Override
    public String getColumnValue(int column)
    {
        if (this.ratio_entry == null)
            return "";

        switch (column)
        {
            case 1:
                return "" + this.ratio_entry.to;
            case 2:
                return "" + this.ratio_entry.ch_insulin;
            case 3:
                return "" + this.ratio_entry.bg_insulin;

            case 4:
                return "" + this.ratio_entry.procent;

            case 0:
            default:
                return "" + this.ratio_entry.from;

        }
    }

}
