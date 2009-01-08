package ggc.core.print;

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


    /**
     * Fill Doucment Body
     * @see ggc.core.print.PrintFoodMenuAbstract#fillDocumentBody(com.lowagie.text.Document)
     */
    @Override
    public void fillDocumentBody(Document document) throws Exception
    {
        //System.out.println("Jedilnik");
        
        Iterator<DailyValues> it = this.m_data.iterator();
        
        int count = 0;
        
        Font f =  this.text_normal;  //new Font(this.base_helvetica , 12, Font.NORMAL); // this.base_times
        
        PdfPTable datatable = new PdfPTable(6);
        int headerwidths[] = { 13, 7,
                               40, 20, 10, 10 
                                }; // percentage
        datatable.setWidths(headerwidths);
        datatable.setWidthPercentage(100); // percentage
        //datatable.getDefaultCell().setPadding(3);
        //datatable.getDefaultCell().setBorderWidth(2);
        
        datatable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT); //ALIGN_CENTER);
        datatable.getDefaultCell().setBorderWidth(1);
        
        datatable.addCell(new Phrase(ic.getMessage("DATE"), this.text_bold));
        datatable.addCell(new Phrase(ic.getMessage("TIME"), this.text_bold));
        datatable.addCell(new Phrase(ic.getMessage("PRINT_FOOD_DESC"), this.text_bold));
        datatable.addCell(new Phrase(ic.getMessage("WEIGHT_TYPE"), this.text_bold));
        datatable.addCell(new Phrase(ic.getMessage("AMOUNT_LBL"), this.text_bold));
        datatable.addCell(new Phrase(ic.getMessage("CH"), this.text_bold));
        
//        document.add(datatable);
        
        while (it.hasNext())
        {
            DailyValues dv = it.next();
            dv.sort();
/* v2            
            PdfPTable data_1 = new PdfPTable(2);
            
            int d1_widths[] = { 10, 90 //,
                                   //40, 20, 10, 10 
                                    }; // percentage
            data_1.setWidths(d1_widths);
            data_1.setWidthPercentage(100); // percentage
            
            
            //datatable.setWidths(headerwidths);
            //datatable.setWidthPercentage(100); // percentage
            //datatable.getDefaultCell().setPadding(3);
            //datatable.getDefaultCell().setBorderWidth(2);
            
            data_1.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            data_1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            data_1.getDefaultCell().setBorderWidth(1);
            
*/            
            
/*            
            data_1.addCell(new Phrase(ic.getMessage("DATE"), f));
            data_1.addCell(new Phrase(ic.getMessage("TIME"), f));
            data_1.addCell(new Phrase(ic.getMessage("PRINT_FOOD_DESC"), f));
            data_1.addCell(new Phrase(ic.getMessage("PRINT_AMOUNT_TYPE"), f));
            datatable.addCell(new Phrase(ic.getMessage("PRINT_AMOUNT"), f));
            datatable.addCell(new Phrase(ic.getMessage("CH"), f));
*/            

            
            
// v2            data_1.addCell(new Phrase(dv.getDateAsString(), f));
     
// v2            datatable.addCell(new Phrase(dv.getDateAsString(), f));

            
            datatable.addCell(new Phrase(dv.getDateAsString(), f));
            
            System.out.println("Row count: " + dv.getRowCount());
            System.out.println(dv.getDateAsString());
            
            int active_day_entry = 0;
            
            for(int i=0; i<dv.getRowCount(); i++)
            {
                
                DailyValuesRow rw = (DailyValuesRow)dv.getRow(i);
                
                if ((rw.getMealsIds()==null) || (rw.getMealsIds().length()==0))
                    continue;

                
                if (active_day_entry>0)
                {
                    datatable.addCell(new Phrase("", f));
                }

                active_day_entry++;
                
                datatable.addCell(new Phrase(rw.getTimeAsString(), f));
                
                DailyFoodEntries mpts = new DailyFoodEntries(rw.getMealsIds(), true); 

                
                datatable.addCell(new Phrase(ic.getMessage("TOGETHER"), this.text_italic));
                datatable.addCell(new Phrase("", f));
                datatable.addCell(new Phrase("", f));
                datatable.addCell(new Phrase(DataAccess.Decimal2Format.format(rw.getCH()), this.text_italic));
                
                
                
                
                for(int j=0; j<mpts.getElementsCount(); j++)
                {
                    DailyFoodEntry mp = mpts.getElement(j);
                    
//                    if (j>0)
                    {
                        datatable.addCell(new Phrase("", f));
                        datatable.addCell(new Phrase("", f));
                    }
                    
                    
                    datatable.addCell(new Phrase(mp.getName(), f));
                    
                    
                    float value = 0.0f;
                    
                    if (mp.getAmountType()==DailyFoodEntry.WEIGHT_TYPE_AMOUNT)
                    {
                        datatable.addCell(new Phrase(ic.getMessage("AMOUNT_LBL"), f));
                        //value = mp.getNutrientValue(205);
                        /*
                        System.out.println("nutr val 205" + mp.getNutrientValue(205) + 
                            "\namount: " + mp.amount + 
                            "\nhw multiplier: " + mp.getHomeWeightMultiplier() 
                            ); */
                        value = mp.getMealCH();
                        
                    }
                    else if (mp.getAmountType()==DailyFoodEntry.WEIGHT_TYPE_WEIGHT)
                    {
                        datatable.addCell(new Phrase(ic.getMessage("WEIGHT_LBL2"), f));
                        //value = mp.getNutrientValue(205);
                        value = mp.getNutrientValue(205) * (mp.getAmount() / 100.0f);
                    }
                    else
                    {
                        datatable.addCell(new Phrase(mp.getHomeWeightDescription() + " (" + DataAccess.Decimal0Format.format(mp.getHomeWeightMultiplier() * 100) + " g)", f));
                        value = mp.getNutrientValue(205) * mp.getHomeWeightMultiplier();
                    }
                    
                   
                    
                    datatable.addCell(new Phrase(mp.getAmountSingleDecimalString(), f));
                    datatable.addCell(new Phrase(DataAccess.Decimal2Format.format(value), f));  // ch
                    
                    System.out.println("     " + rw.getTimeAsString() + " " + mp);
                }
                
            }

            
            if (active_day_entry==0)
            {
                datatable.addCell(new Phrase("", f));
                datatable.addCell(new Phrase("", f));
                datatable.addCell(new Phrase("", f));
                datatable.addCell(new Phrase("", f));
                datatable.addCell(new Phrase("", f));
            } 
            
            
            
            count++;
        }

        
        document.add(datatable);
        
        
        //System.out.println("Elements all: " + this.m_data.size() + " in iterator: " + count);
        
    }

    
    
    /**
     * Get text for title
     * 
     * @return title
     */
    @Override    
    public String getTitleText()
    {
        return "FOOD_MENU_EXT_III";
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
                               31, 15, 8, 8, 6, 6, 6 
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
        return 9;
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
        table.addCell(new Phrase(ic.getMessage("BG"), this.text_bold));
        table.addCell(new Phrase(ic.getMessage("ENERGY"), this.text_bold));
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
            table.addCell(new Phrase(mp.getHomeWeightDescription() + " (" + DataAccess.Decimal0Format.format(mp.getHomeWeightMultiplier() * 100) + " g)", this.text_normal));
            value = mp.getNutrientValue(205) * mp.getHomeWeightMultiplier();
        }
        
        table.addCell(new Phrase(mp.getAmountSingleDecimalString(), this.text_normal));
        table.addCell(new Phrase(DataAccess.Decimal2Format.format(value), this.text_normal));  // ch
        
        table.addCell(new Phrase("", this.text_normal)); // ins
        table.addCell(new Phrase("", this.text_normal)); // bg
        
        // TODO: do this
        table.addCell(new Phrase("", this.text_normal)); // energy  

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

        table.addCell(new Phrase(DataAccess.Decimal2Format.format(rw.getCH()), this.text_italic));
        table.addCell(new Phrase(rw.getIns1AsString(), this.text_italic));
        table.addCell(new Phrase(rw.getBGAsString(), this.text_italic));

        // TODO energy
        table.addCell(new Phrase("", this.text_normal));
        
    
    }



    /**
     * Returns base filename for printing job, this is just part of end filename (starting part)
     */
    @Override
    public String getFileNameBase()
    {
        return "" + System.currentTimeMillis();
    }
    
    
}




