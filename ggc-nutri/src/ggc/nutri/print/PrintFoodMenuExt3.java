package ggc.nutri.print;

import ggc.core.data.DailyValuesRow;
import ggc.core.data.DayValuesData;
import ggc.nutri.db.datalayer.DailyFoodEntry;

import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;

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
 *  Filename:     PrintFoodMenuExt3  
 *  Description:  Class for printing - Foodmenu Extended 3
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class PrintFoodMenuExt3 extends PrintFoodMenuAbstract
{

    /**
     * Constructor
     * 
     * @param mv
     */
    public PrintFoodMenuExt3(DayValuesData mv)
    {
        super(mv);
    }

    /*
     * public void fillDocumentBody(Document document) throws Exception
     * {
     * //System.out.println("Jedilnik");
     * Iterator<DailyValues> it = this.m_data.iterator();
     * int count = 0;
     * Font f = this.text_normal; //new Font(this.base_helvetica , 12,
     * Font.NORMAL); // this.base_times
     * PdfPTable datatable = new PdfPTable(6);
     * int headerwidths[] = { 13, 7,
     * 40, 20, 10, 10
     * }; // percentage
     * datatable.setWidths(headerwidths);
     * datatable.setWidthPercentage(100); // percentage
     * //datatable.getDefaultCell().setPadding(3);
     * //datatable.getDefaultCell().setBorderWidth(2);
     * datatable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
     * datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
     * //ALIGN_CENTER);
     * datatable.getDefaultCell().setBorderWidth(1);
     * datatable.addCell(new Phrase(i18nControlAbstract.getMessage("DATE"), this.text_bold));
     * datatable.addCell(new Phrase(i18nControlAbstract.getMessage("TIME"), this.text_bold));
     * datatable.addCell(new Phrase(i18nControlAbstract.getMessage("PRINT_FOOD_DESC"),
     * this.text_bold));
     * datatable.addCell(new Phrase(i18nControlAbstract.getMessage("WEIGHT_TYPE"),
     * this.text_bold));
     * datatable.addCell(new Phrase(i18nControlAbstract.getMessage("AMOUNT_LBL"),
     * this.text_bold));
     * datatable.addCell(new Phrase(i18nControlAbstract.getMessage("CH"), this.text_bold));
     * while (it.hasNext())
     * {
     * DailyValues dv = it.next();
     * dv.sort();
     * datatable.addCell(new Phrase(dv.getDateAsString(), f));
     * System.out.println("Row count: " + dv.getRowCount());
     * System.out.println(dv.getDateAsString());
     * int active_day_entry = 0;
     * for(int i=0; i<dv.getRowCount(); i++)
     * {
     * DailyValuesRow rw = (DailyValuesRow)dv.getRow(i);
     * if ((rw.getMealsIds()==null) || (rw.getMealsIds().length()==0))
     * continue;
     * if (active_day_entry>0)
     * {
     * datatable.addCell(new Phrase("", f));
     * }
     * active_day_entry++;
     * datatable.addCell(new Phrase(rw.getTimeAsString(), f));
     * DailyFoodEntries mpts = new DailyFoodEntries(rw.getMealsIds(), true);
     * datatable.addCell(new Phrase(i18nControlAbstract.getMessage("TOGETHER"),
     * this.text_italic));
     * datatable.addCell(new Phrase("", f));
     * datatable.addCell(new Phrase("", f));
     * datatable.addCell(new Phrase(this.getFormatedValue(rw.getCH(), 2),
     * this.text_italic));
     * for(int j=0; j<mpts.getElementsCount(); j++)
     * {
     * DailyFoodEntry mp = mpts.getElement(j);
     * // if (j>0)
     * {
     * datatable.addCell(new Phrase("", f));
     * datatable.addCell(new Phrase("", f));
     * }
     * datatable.addCell(new Phrase(mp.getName(), f));
     * float value = 0.0f;
     * if (mp.getAmountType()==DailyFoodEntry.WEIGHT_TYPE_AMOUNT)
     * {
     * datatable.addCell(new Phrase(i18nControlAbstract.getMessage("AMOUNT_LBL"), f));
     * // System.out.println("nutr val 205" + mp.getNutrientValue(205) +
     * // "\namount: " + mp.amount +
     * // "\nhw multiplier: " + mp.getHomeWeightMultiplier()
     * // );
     * value = mp.getMealCH();
     * }
     * else if (mp.getAmountType()==DailyFoodEntry.WEIGHT_TYPE_WEIGHT)
     * {
     * datatable.addCell(new Phrase(i18nControlAbstract.getMessage("WEIGHT_LBL2"), f));
     * //value = mp.getNutrientValue(205);
     * value = mp.getNutrientValue(205) * (mp.getAmount() / 100.0f);
     * }
     * else
     * {
     * datatable.addCell(new Phrase(mp.getHomeWeightDescription() + " (" +
     * this.getFormatedValue(mp.getHomeWeightMultiplier() * 100, 0) + " g)",
     * f));
     * value = mp.getNutrientValue(205) * mp.getHomeWeightMultiplier();
     * }
     * datatable.addCell(new Phrase(mp.getAmountSingleDecimalString(), f));
     * datatable.addCell(new Phrase(this.getFormatedValue(value, 2), f)); // ch
     * System.out.println("     " + rw.getTimeAsString() + " " + mp);
     * }
     * }
     * if (active_day_entry==0)
     * {
     * datatable.addCell(new Phrase("", f));
     * datatable.addCell(new Phrase("", f));
     * datatable.addCell(new Phrase("", f));
     * datatable.addCell(new Phrase("", f));
     * datatable.addCell(new Phrase("", f));
     * }
     * count++;
     * }
     * document.add(datatable);
     * //System.out.println("Elements all: " + this.m_data.size() +
     * " in iterator: " + count);
     * }
     */

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitleText()
    {
        return "FOOD_MENU_EXT_III";
    }

    // FOOD_MENU_EXT_I=Extended Food Menu I (CH,Ins)

    /**
     * {@inheritDoc}
     */
    @Override
    public int[] getTableColumnWidths()
    {
        int headerwidths[] = { 13, 7, 31, 15, 8, 8, 6, 6, 6 }; // percentage
        return headerwidths;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTableColumnsCount()
    {
        return 9;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeAdditionalHeader(PdfPTable table) throws Exception
    {
        table.addCell(this.createBoldTextPhrase("CH"));
        table.addCell(this.createBoldTextPhrase("INS"));
        table.addCell(this.createBoldTextPhrase("BG"));
        table.addCell(this.createBoldTextPhrase("ENERGY"));
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
            table.addCell(new Phrase(mp.getHomeWeightDescription() + " ("
                    + this.getFormatedValue(mp.getHomeWeightMultiplier() * 100, 0) + " g)", this.textFontNormal));
            value = mp.getNutrientValue(205) * mp.getHomeWeightMultiplier();
        }

        table.addCell(new Phrase(mp.getAmountSingleDecimalString(), this.textFontNormal));
        table.addCell(new Phrase(this.getFormatedValue(value, 2), this.textFontNormal)); // ch

        table.addCell(this.createEmptyTextPhrase()); // ins
        table.addCell(this.createEmptyTextPhrase()); // bg

        // TODO: do this
        table.addCell(this.createEmptyTextPhrase()); // energy

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
        table.addCell(this.createEmptyTextPhrase());
        table.addCell(this.createEmptyTextPhrase());
        table.addCell(this.createEmptyTextPhrase());
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

        table.addCell(new Phrase(this.getFormatedValue(rw.getCH(), 2), this.textFontItalic));
        table.addCell(new Phrase(rw.getIns1AsString(), this.textFontItalic));
        table.addCell(new Phrase(rw.getBGAsString(), this.textFontItalic));

        // TODO energy
        table.addCell(this.createEmptyTextPhrase());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeFoodDescData(PdfPTable table, DailyValuesRow mp) throws Exception
    {
        writeEmptyColumnData(table);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFileNameBase()
    {
        return "FoodMenuExt3";
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
