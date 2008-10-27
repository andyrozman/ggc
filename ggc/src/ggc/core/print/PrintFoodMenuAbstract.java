package ggc.core.print;

/*
 * GGC - GNU Gluco Control
 * 
 * A pure java app to help you manage your diabetes.
 * 
 * See AUTHORS for copyright information.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Filename: PrintSimpleonthlyReport.java
 * 
 * Purpose: Creating PDF for Extended Monthly Report (used for printing)
 * 
 * Author: andyrozman {andy@atech-software.com}
 */

import ggc.core.data.DailyValues;
import ggc.core.data.DailyValuesRow;
import ggc.core.data.DayValuesData;
import ggc.core.db.datalayer.DailyFoodEntries;
import ggc.core.db.datalayer.DailyFoodEntry;
import ggc.core.util.DataAccess;

import java.util.Iterator;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPTable;

public abstract class PrintFoodMenuAbstract extends PrintAbstract
{

    public PrintFoodMenuAbstract(DayValuesData mv)
    {
        super(mv, DataAccess.getInstance().getNutriI18nControl());

        // System.out.println("getNutriControl");

        // this.ic = m_da.getNutriI18nControl();
    }

    public Paragraph getTitle()
    {
        Paragraph p = new Paragraph();

        Font f = new Font(this.base_times, 16, Font.BOLD);

        p.setAlignment(Element.ALIGN_CENTER);
        p.add(new Paragraph("", f));
        p.add(new Paragraph(ic.getMessage(getTitleText()) + " [" + this.m_data.getFromAsLocalizedDate() + " - "
                + this.m_data.getToAsLocalizedDate() + "]", f));
        // p.add(new Paragraph("May 2006"));
        p.add(new Paragraph(ic.getMessage("FOR") + " " + m_da.getSettings().getUserName(), new Font(Font.TIMES_ROMAN,
                12, Font.ITALIC)));
        p.add(new Paragraph("", f));
        p.add(new Paragraph("", f));
        // p.add(new Paragraph("", f));

        return p;
    }


    
    @Override
    public void fillDocumentBody(Document document) throws Exception
    {
        // TODO Auto-generated method stub

        // System.out.println("Jedilnik CH,Ins,BG");

        Iterator<DailyValues> it = this.m_data.iterator();

        int count = 0;

        Font f = this.text_normal; // new Font(this.base_helvetica , 12,
                                   // Font.NORMAL); // this.base_times

        PdfPTable datatable = new PdfPTable(getTableColumnsCount());
        //int headerwidths[] = { 13, 7, 35, 17, 8, 8, 6, 6 }; // percentage

        // int headerwidths

        datatable.setWidths(getTableColumnWidths());
        datatable.setWidthPercentage(100); // percentage
        // datatable.getDefaultCell().setPadding(3);
        // datatable.getDefaultCell().setBorderWidth(2);

        datatable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT); // ALIGN_CENTER);
        datatable.getDefaultCell().setBorderWidth(1);

        datatable.addCell(new Phrase(ic.getMessage("DATE"), this.text_bold));
        datatable.addCell(new Phrase(ic.getMessage("TIME"), this.text_bold));
        datatable.addCell(new Phrase(ic.getMessage("PRINT_FOOD_DESC"), this.text_bold));
        datatable.addCell(new Phrase(ic.getMessage("WEIGHT_TYPE"), this.text_bold));
        datatable.addCell(new Phrase(ic.getMessage("AMOUNT_SHORT"), this.text_bold));

        writeAdditionalHeader(datatable);

        /*
        datatable.addCell(new Phrase(ic.getMessage("CH"), this.text_bold));
        datatable.addCell(new Phrase(ic.getMessage("INS"), this.text_bold));
        datatable.addCell(new Phrase(ic.getMessage("BG"), this.text_bold));
        */
        // document.add(datatable);
        while (it.hasNext())
        {
            DailyValues dv = it.next();
            dv.sort();

            datatable.addCell(new Phrase(dv.getDateAsLocalizedString(), f));

            System.out.println("Row count: " + dv.getRowCount());
            System.out.println(dv.getDateAsString());

            int active_day_entry = 0;

/*            if (dv.getRowCount() == 0)
            {
//                this.writeEmptyColumnData(datatable);
            }
            else */
            {

                for (int i = 0; i < dv.getRowCount(); i++)
                {

                    DailyValuesRow rw = (DailyValuesRow) dv.getRow(i);

                    if ((rw.getMealsIds() == null) || (rw.getMealsIds().length() == 0))
                        continue;

                    if (active_day_entry > 0)
                    {
                        datatable.addCell(new Phrase("", f));
                    }

                    active_day_entry++;

                    datatable.addCell(new Phrase(rw.getTimeAsString(), f));

                    DailyFoodEntries mpts = new DailyFoodEntries(rw.getMealsIds(), true);

                    writeTogetherData(datatable, rw);

                    /*
                    datatable.addCell(new Phrase(ic.getMessage("TOGETHER"), this.text_italic));
                    datatable.addCell(new Phrase("", f));
                    datatable.addCell(new Phrase("", f));

                    datatable.addCell(new Phrase(DataAccess.Decimal2Format.format(rw.getCH()), this.text_italic));
                    datatable.addCell(new Phrase(rw.getIns1AsString(), this.text_italic));
                    datatable.addCell(new Phrase(rw.getBGAsString(), this.text_italic));
                    */

                    for (int j = 0; j < mpts.getElementsCount(); j++)
                    {

                        DailyFoodEntry mp = mpts.getElement(j);

                        this.writeColumnData(datatable, mp);

                        /*
                        datatable.addCell(new Phrase("", f));
                        datatable.addCell(new Phrase("", f));
                        
                        datatable.addCell(new Phrase(mp.getName(), f));
                        
                        
                        float value = 0.0f;
                        
                        if (mp.amount_type==DailyFoodEntry.WEIGHT_TYPE_AMOUNT)
                        {
                            datatable.addCell(new Phrase(ic.getMessage("AMOUNT_LBL"), f));
                            //value = mp.getNutrientValue(205);
                            value = mp.getMealCH();
                            
                        }
                        else if (mp.amount_type==DailyFoodEntry.WEIGHT_TYPE_WEIGHT)
                        {
                            datatable.addCell(new Phrase(ic.getMessage("WEIGHT_LBL2"), f));
                            //value = mp.getNutrientValue(205);
                            value = mp.getNutrientValue(205) * (mp.amount / 100.0f);
                        }
                        else
                        {
                            datatable.addCell(new Phrase(mp.getHomeWeightDescription() + " (" + DataAccess.Decimal0Format.format(mp.getHomeWeightMultiplier() * 100) + " g)", f));
                            value = mp.getNutrientValue(205) * mp.getHomeWeightMultiplier();
                        }
                        
                        
                        
                        datatable.addCell(new Phrase(mp.getAmountSingleDecimalString(), f));
                        datatable.addCell(new Phrase(DataAccess.Decimal2Format.format(value), f));  // ch

                        datatable.addCell(new Phrase("", f));
                        datatable.addCell(new Phrase("", f));
                        */

                        // System.out.println("     " + rw.getTimeAsString() +
                        // " " + mp);
                    }

                }

                if (active_day_entry==0)
                {
                    this.writeEmptyColumnData(datatable);
                }
                
                /*            
                  after together code this part is obsolete
                            if (active_day_entry==0)
                            {
                                datatable.addCell(new Phrase("", f));
                                datatable.addCell(new Phrase("", f));
                                datatable.addCell(new Phrase("", f));
                                datatable.addCell(new Phrase("", f));
                                datatable.addCell(new Phrase("", f));
                            } 
                  */

                count++;
            }
        }

        document.add(datatable);

        System.out.println("Elements all: " + this.m_data.size() + " in iterator: " + count);

    }


    @Override
    public String getFileNameRange()
    {
        return this.m_data.getRangeBeginObject().getDateString() + "-"
                + this.m_data.getRangeEndObject().getDateString();
    }

    
    
    
    /**
     * Get text for title
     * 
     * @return title
     */
    public abstract String getTitleText();

    /**
     * Return count of table columns
     * @return
     */
    public abstract int getTableColumnsCount();

    /** 
     * Return columns widths for table
     * @return
     */
    public abstract int[] getTableColumnWidths();

    /**
     * Write additional header to documents
     *  
     * @param table
     * @throws Exception
     */
    public abstract void writeAdditionalHeader(PdfPTable table) throws Exception;

    /**
     * Write together data (all data of certain type summed)
     * 
     * @param table
     * @param rw
     * @throws Exception
     */
    public abstract void writeTogetherData(PdfPTable table, DailyValuesRow rw) throws Exception;

    /**
     * Write data in column
     * 
     * @param table
     * @param mp
     * @throws Exception
     */
    public abstract void writeColumnData(PdfPTable table, DailyFoodEntry mp) throws Exception;

    /**
     * Write empty column data. If there is no data, this is used, to fill empty places.
     * 
     * @param table
     * @throws Exception
     */
    public abstract void writeEmptyColumnData(PdfPTable table) throws Exception;
    
    
}