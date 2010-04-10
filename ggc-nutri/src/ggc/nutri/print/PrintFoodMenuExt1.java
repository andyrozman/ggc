package ggc.nutri.print;


/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: PrintSimpleonthlyReport.java

 *  Purpose:  Creating PDF for Extended Monthly Report (used for printing)
 *
 *  Author:   andyrozman  {andy@atech-software.com}
 */


import ggc.core.data.DailyValuesRow;
import ggc.core.data.DayValuesData;
import ggc.core.data.ExtendedDailyValue;
import ggc.nutri.db.datalayer.DailyFoodEntry;

import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPTable;


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
 *  Filename:     PrintFoodMenuExt1  
 *  Description:  Class for printing - Foodmenu Extended 1
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */


public class PrintFoodMenuExt1 extends PrintFoodMenuAbstract
{
    
    /**
     * Constructor
     * 
     * @param mv
     */
    public PrintFoodMenuExt1(DayValuesData mv)
    {
        super(mv);
    }

    
    /**
     * Get text for title
     * 
     * @return title
     */
    @Override    
    public String getTitleText()
    {
        return "FOOD_MENU_EXT_I";
    }
    
//    FOOD_MENU_EXT_I=Extended Food Menu I (CH,Ins)

    /** 
     * Return columns widths for table
     * @return
     */
    @Override
    public int[] getTableColumnWidths()
    {
        int headerwidths[] = { 13, 7,
                               41, 17, 8, 8, 6 
                                }; // percentage
        return headerwidths;
    }



    /**
     * Return count of table columns
     * 
     * @return
     */
    @Override
    public int getTableColumnsCount()
    {
        return 7;
    }



    /**
     * Write additional header to documents
     *  
     * @param table
     * @throws Exception
     */
    @Override
    public void writeAdditionalHeader(PdfPTable table) throws Exception
    {
        table.addCell(new Phrase(ic.getMessage("CH"), this.text_bold));
        table.addCell(new Phrase(ic.getMessage("INS"), this.text_bold));
    }


    
    /**
     * Write data in column
     * 
     * @param table
     * @param mp
     * @throws Exception
     */
    @Override
    public void writeColumnData(PdfPTable table, DailyFoodEntry mp) throws Exception
    {
        table.addCell(new Phrase("", this.text_normal));
        table.addCell(new Phrase("", this.text_normal));
        
        table.addCell(new Phrase(mp.getName(), this.text_normal));
        
        
        float value = 0.0f;
        
        if (mp.getAmountType()==DailyFoodEntry.WEIGHT_TYPE_AMOUNT)
        {
            table.addCell(new Phrase(ic.getMessage("AMOUNT_LBL"), this.text_normal));
            //value = mp.getNutrientValue(205);
            value = mp.getMealCH();
            
        }
        else if (mp.getAmountType()==DailyFoodEntry.WEIGHT_TYPE_WEIGHT)
        {
            table.addCell(new Phrase(ic.getMessage("WEIGHT_LBL2"), this.text_normal));
            //value = mp.getNutrientValue(205);
            value = mp.getNutrientValue(205) * (mp.getAmount() / 100.0f);
        }
        else
        {
            table.addCell(new Phrase(mp.getHomeWeightDescription() + " (" + this.getFormatedValue((mp.getHomeWeightMultiplier() * 100), 0) + " g)", this.text_normal));
            value = mp.getNutrientValue(205) * mp.getHomeWeightMultiplier();
        }
        
        table.addCell(new Phrase(mp.getAmountSingleDecimalString(), this.text_normal));
        table.addCell(new Phrase(this.getFormatedValue(value,2), this.text_normal));  // ch
        
        table.addCell(new Phrase("", this.text_normal));
    }


    
    
    

    /**
     * Write empty column data. If there is no data, this is used, to fill empty places.
     * 
     * @param table
     * @throws Exception
     */
    @Override
    public void writeEmptyColumnData(PdfPTable table) throws Exception
    {
        table.addCell(new Phrase("", this.text_normal));
        table.addCell(new Phrase("", this.text_normal));
        table.addCell(new Phrase("", this.text_normal));
        table.addCell(new Phrase("", this.text_normal));
        table.addCell(new Phrase("", this.text_normal));
        table.addCell(new Phrase("", this.text_normal));
    }



    /**
     * Write together data (all data of certain type summed)
     * 
     * @param table
     * @param rw
     * @throws Exception
     */
    @Override
    public void writeTogetherData(PdfPTable table, DailyValuesRow rw) throws Exception
    {
        table.addCell(new Phrase(ic.getMessage("TOGETHER"), this.text_italic));
        table.addCell(new Phrase("", this.text_normal));
        table.addCell(new Phrase("", this.text_normal));

        table.addCell(new Phrase(this.getFormatedValue(rw.getCH(), 2), this.text_italic));
        table.addCell(new Phrase(rw.getIns1AsString(), this.text_italic));
    }


    
    /**
     * Write Food Description Data
     * 
     * @param table
     * @param dvr
     * @throws Exception
     */
    public void writeFoodDescData(PdfPTable table, DailyValuesRow dvr) throws Exception
    {
        table.addCell(new Phrase(dvr.getExtendedValue(ExtendedDailyValue.EXTENDED_FOOD_DESCRIPTION), this.text_normal));
        table.addCell(new Phrase(ic.getMessage("DESCRIPTION"), this.text_normal));
        table.addCell(new Phrase("", this.text_normal));

        table.addCell(new Phrase(dvr.getExtendedValue(ExtendedDailyValue.EXTENDED_FOOD_CH), this.text_italic));
        table.addCell(new Phrase(dvr.getIns1AsString(), this.text_italic));
    }
    
    

    /**
     * Returns base filename for printing job, this is just part of end filename (starting part)
     */
    @Override
    public String getFileNameBase()
    {
        return "FoodMenuExt1";
    }
    

    
}




