package ggc.gui.main.panels;

import java.text.DecimalFormat;
import java.util.GregorianCalendar;

import ggc.core.data.HbA1cValues;
import ggc.core.db.GGCDb;
import info.clearthought.layout.TableLayout;

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
 *  Filename:     HbA1cInfoPanel  
 *  Description:  Panel for HbA1c info
 *
 *  Author: schultd, andyrozman
 */
public class HbA1cInfoPanel extends AbstractInfoPanel
{

    private static final long serialVersionUID = -8750479135671337356L;


    /**
     * Constructor
     */
    public HbA1cInfoPanel()
    {
        super("HBA1C");
        init();
        refreshInfo();
    }


    private void init()
    {
        double sizes[][] = { { 10, 0.45, 10, TableLayout.FILL, 10 },
                             { 0.03, 0.17, 0.17, 0.17, 0.17, 0.17, TableLayout.FILL }, };

        initWithTableLayoutAndDisplayPairs(sizes, //
            "YOUR_CURRENT_HBA1C", //
            "VALUATION", //
            "AVG_BG", //
            "READINGS", //
            "READINGS_SLASH_DAY");
    }


    /**
     * Do Refresh - This method can do Refresh
     */
    @Override
    public void doRefresh()
    {
        HbA1cValues hbVal = null;

        GGCDb db = dataAccess.getDb();

        if (db != null && db.isDbStarted())
        {
            hbVal = dataAccess.getHbA1c(new GregorianCalendar());
        }

        DecimalFormat df = new DecimalFormat("#0.00");

        if (hbVal != null)
        {
            if (hbVal.getReadings() == 0)
            {
                setValueOnDisplayLabel("YOUR_CURRENT_HBA1C", i18nControl.getMessage("NO_READINGS"));
            }
            else
            {
                setValueOnDisplayLabel("YOUR_CURRENT_HBA1C", df.format(hbVal.getHbA1c_Method3()) + " %");
            }

            setValueOnDisplayLabel("VALUATION", hbVal.getValuation());
            setValueOnDisplayLabel("AVG_BG", df.format(hbVal.getAvgBG()));
            setValueOnDisplayLabel("READINGS", hbVal.getReadings() + "");
            setValueOnDisplayLabel("READINGS_SLASH_DAY", df.format(hbVal.getReadingsPerDay()));
        }
        else
        {
            setValueOnDisplayLabel("YOUR_CURRENT_HBA1C", "");
            setValueOnDisplayLabel("VALUATION", "");
            setValueOnDisplayLabel("AVG_BG", "");
            setValueOnDisplayLabel("READINGS", "");
            setValueOnDisplayLabel("READINGS_SLASH_DAY", "");
        }

    }


    @Override
    public InfoPanelType getPanelType()
    {
        return InfoPanelType.HbA1c;
    }

}
