package ggc.core.print;


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

public class PrintFoodMenuBase extends PrintAbstract
{

    
    
    public PrintFoodMenuBase(DayValuesData mv)
    {
        super(mv, DataAccess.getInstance().getNutriI18nControl());
        
       // System.out.println("getNutriControl");
        
        //this.ic = m_da.getNutriI18nControl();
    }

    
    
    
    public Paragraph getTitle()
    {
        Paragraph p = new Paragraph();
        
        Font f = new Font(this.base_times , 16, Font.BOLD); 
        
        p.setAlignment(Element.ALIGN_CENTER);
        p.add(new Paragraph("", f));
        p.add(new Paragraph(ic.getMessage("FOOD_MENU_BASE") + " [" + this.m_data.getFromAsLocalizedDate() + " - " + this.m_data.getToAsLocalizedDate() + "]", f));
        //p.add(new Paragraph("May 2006"));
        p.add(new Paragraph(ic.getMessage("FOR") + " " + m_da.getSettings().getUserName(), new Font(Font.TIMES_ROMAN, 12, Font.ITALIC)));
        p.add(new Paragraph("", f));
        p.add(new Paragraph("", f));
        //p.add(new Paragraph("", f));

        return p;
    }
    
    
    

    
    
    
    

    



    @Override
    public void fillDocumentBody(Document document) throws Exception
    {
        // TODO Auto-generated method stub
        
        System.out.println("Jedilnik");
        
        Iterator<DailyValues> it = this.m_data.iterator();
        
        int count = 0;
        
        Font f = new Font(this.base_helvetica , 12, Font.NORMAL); // this.base_times
        
        PdfPTable datatable = new PdfPTable(6);
        int headerwidths[] = { 17, 8,
                               35, 20, 10, 10 
                                }; // percentage
        datatable.setWidths(headerwidths);
        datatable.setWidthPercentage(100); // percentage
        //datatable.getDefaultCell().setPadding(3);
        //datatable.getDefaultCell().setBorderWidth(2);
        
        datatable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT); //ALIGN_CENTER);
        datatable.getDefaultCell().setBorderWidth(1);
        
        datatable.addCell(new Phrase(ic.getMessage("DATE"), f));
        datatable.addCell(new Phrase(ic.getMessage("TIME"), f));
        datatable.addCell(new Phrase(ic.getMessage("PRINT_FOOD_DESC"), f));
        datatable.addCell(new Phrase(ic.getMessage("WEIGHT_TYPE"), f));
        datatable.addCell(new Phrase(ic.getMessage("AMOUNT_LBL"), f));
        datatable.addCell(new Phrase(ic.getMessage("CH"), f));
        
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
            
            /*
            if (dv.getRowCount()==0)
            {
                datatable.addCell(new Phrase("", f));
                datatable.addCell(new Phrase("", f));
                datatable.addCell(new Phrase("", f));
                continue;
            } */
            
            System.out.println(dv.getDateAsString());
            
            int active_day_entry = 0;
            
            for(int i=0; i<dv.getRowCount(); i++)
            {

                
                
                DailyValuesRow rw = (DailyValuesRow)dv.getRow(i);
                
                if ((rw.getMealsIds()==null) || (rw.getMealsIds().length()==0))
                    continue;

//                datatable.addCell(new Phrase(dv.getDateAsString(), f));
                
                if (active_day_entry>0)
                {
                    datatable.addCell(new Phrase("", f));
                }

                active_day_entry++;
/*                
                PdfPTable data_2 = new PdfPTable(2);
                
                int d2_widths[] = { 
                                   10, 90 
                                    }; // percentage
                 data_2.setWidths(d2_widths);
                 //data_2.setWidths(headerwidths);
                 data_2.setWidthPercentage(90); // percentage
                //datatable.getDefaultCell().setPadding(3);
                //datatable.getDefaultCell().setBorderWidth(2);
                
                data_2.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
                data_2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                data_2.getDefaultCell().setBorderWidth(1);
                
                data_2.addCell(new Phrase(rw.getTimeAsString(), f));
  */              
                
                datatable.addCell(new Phrase(rw.getTimeAsString(), f));
                
                DailyFoodEntries mpts = new DailyFoodEntries(rw.getMealsIds()); 
                //MealParts mpts = new MealParts(rw.getMealsIds());
                
/* v2                
                PdfPTable data_3 = new PdfPTable(4);
                
                int d3_widths[] = { 
                                       40, 20, 10, 10 
                                        }; // percentage
                data_3.setWidths(d3_widths);
                
                //data_1.setWidthPercentage(100); // percentage
                //datatable.getDefaultCell().setPadding(3);
                //datatable.getDefaultCell().setBorderWidth(2);
                
                data_3.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
                data_3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                //data_3.getDefaultCell().setBorderWidth(1);
                data_3.getDefaultCell().setBorderWidth(0);
*/                
                
                for(int j=0; j<mpts.getElementsCount(); j++)
                {
                    DailyFoodEntry mp = mpts.getElement(j);
                    
                    
/*                    
                    data_3.addCell(new Phrase(mp.getName(), f));
                    data_3.addCell(new Phrase(mp.getHomeWeightDescription(), f));
                    data_3.addCell(new Phrase(mp.getValueString(), f));
                    data_3.addCell(new Phrase("CH", f));
*/                    

                    if (j>0)
                    {
//                        datatable.addCell(new Phrase(dv.getDateAsString(), f));
//                        datatable.addCell(new Phrase(rw.getTimeAsString(), f));
                        datatable.addCell(new Phrase("", f));
                        datatable.addCell(new Phrase("", f));
                    }
                    
                    
                    datatable.addCell(new Phrase(mp.getName(), f));
                    
                    
                    float value = 0.0f;
                    
                    if (mp.amount_type==DailyFoodEntry.WEIGHT_TYPE_AMOUNT)
                    {
                        datatable.addCell(new Phrase(ic.getMessage("AMOUNT_LBL"), f));
                        value = mp.getNutrientValue(205);
                    }
                    else if (mp.amount_type==DailyFoodEntry.WEIGHT_TYPE_WEIGHT)
                    {
                        datatable.addCell(new Phrase(ic.getMessage("WEIGHT_LBL2"), f));
                        value = mp.getNutrientValue(205);
                    }
                    else
                    {
                        datatable.addCell(new Phrase(mp.getHomeWeightDescription(), f));
                        value = mp.getNutrientValue(205) * mp.getHomeWeightMultiplier();
                    }
                    
                   
                    
                    datatable.addCell(new Phrase(mp.getAmountString(), f));
                    //datatable.addCell(new Phrase(DataAccess.Decimal2Format.format(mp.getNutrientValue(205) * mp.getHomeWeightMultiplier()), f));  // ch
                    
                    datatable.addCell(new Phrase(DataAccess.Decimal2Format.format(value), f));  // ch
                    
                    // x data_3.completeRow();
                    
                    System.out.println("     " + rw.getTimeAsString() + " " + mp);
                }
                
// v2                data_2.addCell(data_3);
                // x data_2.completeRow();

             // v2                data_1.addCell(data_2);
                // x data_1.completeRow();
                
                //datatable.addCell(data_2);
                
                
// v2                document.add(data_1);

                
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
        
        
        System.out.println("Elements all: " + this.m_data.size() + " in iterator: " + count);
        
        
        
    }




    @Override
    public String getFileNameBase()
    {
        return "" + System.currentTimeMillis();
        //return "foodmenu_base_";
    }




    @Override
    public String getFileNameRange()
    {
        return this.m_data.getRangeBeginObject().getDateString() + "-" + this.m_data.getRangeEndObject().getDateString(); 
    }

}




