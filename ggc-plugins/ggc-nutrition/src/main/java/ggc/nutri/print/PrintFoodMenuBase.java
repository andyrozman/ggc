package ggc.nutri.print;

import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;

import ggc.core.data.DailyValuesRow;
import ggc.core.data.DayValuesData;
import ggc.core.data.ExtendedDailyValueType;
import ggc.nutri.db.datalayer.DailyFoodEntry;
import ggc.plugin.util.DataAccessPlugInBase;

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
 *  Filename:     PrintFoodMenuBase  
 *  Description:  Print Base Food Menu
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class PrintFoodMenuBase extends PrintFoodMenuAbstract
{

    /**
     * Constructor 
     * 
     * @param mv
     */
    public PrintFoodMenuBase(DayValuesData mv)
    {
        super(mv);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int[] getTableColumnWidths()
    {
        int headerwidths[] = { 13, 7, 40, 20, 10, 10 }; // percentage
        return headerwidths;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int getTableColumnsCount()
    {
        return 6;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitleText()
    {
        return "FOOD_MENU_BASE";
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void writeAdditionalHeader(PdfPTable table) throws Exception
    {
        table.addCell(this.createBoldTextPhrase("CH"));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void writeEmptyColumnData(PdfPTable table) throws Exception
    {
        table.addCell(this.createEmptyTextPhrase());
        table.addCell(this.createEmptyTextPhrase());
        table.addCell(this.createEmptyTextPhrase());
        table.addCell(this.createEmptyTextPhrase());
        table.addCell(this.createEmptyTextPhrase());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void writeColumnData(PdfPTable table, DailyFoodEntry mp) throws Exception
    {
        table.addCell(this.createEmptyTextPhrase());
        table.addCell(this.createEmptyTextPhrase());

        table.addCell(new Phrase(mp.getName(), this.textFontNormal));

        float value = 0.0f;

        if (mp.getAmountType() == DailyFoodEntry.WEIGHT_TYPE_AMOUNT)
        {
            table.addCell(this.createNormalTextPhrase("AMOUNT_LBL"));
            value = mp.getMealCH();

        }
        else if (mp.getAmountType() == DailyFoodEntry.WEIGHT_TYPE_WEIGHT)
        {
            table.addCell(this.createNormalTextPhrase("WEIGHT_LBL2"));
            value = mp.getNutrientValue(205) * (mp.getAmount() / 100.0f);
        }
        else
        {
            table.addCell(new Phrase(
                    mp.getHomeWeightDescription() + " ("
                            + DataAccessPlugInBase.Decimal0Format.format(mp.getHomeWeightMultiplier() * 100) + " g)",
                    this.textFontNormal));
            value = mp.getNutrientValue(205) * mp.getHomeWeightMultiplier();
        }

        table.addCell(new Phrase(mp.getAmountSingleDecimalString(), this.textFontNormal));
        table.addCell(new Phrase(DataAccessPlugInBase.Decimal2Format.format(value), this.textFontNormal)); // ch

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void writeTogetherData(PdfPTable table, DailyValuesRow rw) throws Exception
    {
        table.addCell(this.createItalicTextPhrase("TOGETHER"));
        table.addCell(this.createEmptyTextPhrase());
        table.addCell(this.createEmptyTextPhrase());

        table.addCell(new Phrase(DataAccessPlugInBase.Decimal2Format.format(rw.getCH()), this.textFontItalic));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void writeFoodDescData(PdfPTable table, DailyValuesRow dvr) throws Exception
    {
        table.addCell(new Phrase(dvr.getExtendedValue(ExtendedDailyValueType.FoodDescription), this.textFontNormal));
        table.addCell(this.createNormalTextPhrase("DESCRIPTION"));
        table.addCell(this.createEmptyTextPhrase());

        table.addCell(new Phrase(dvr.getExtendedValue(ExtendedDailyValueType.FoodCarbohydrate), this.textFontItalic));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getFileNameBase()
    {
        return "FoodMenuBase";
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int getTextSize()
    {
        return 12;
    }

}
