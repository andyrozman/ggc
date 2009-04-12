package ggc.pump.print;

import ggc.core.data.DailyValuesRow;
import ggc.core.util.DataAccess;
import ggc.plugin.data.DeviceValuesDay;
import ggc.plugin.data.DeviceValuesRange;
import ggc.pump.data.PumpValuesEntry;

import java.util.GregorianCalendar;

import com.atech.utils.ATechDate;
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
 *  Filename:     PrintFoodMenuBase  
 *  Description:  Print Base Food Menu
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */


public class PrintPumpDataBase extends PrintPumpDataAbstract
{
   
    
    /**
     * Constructor
     *  
     * @param dvr 
     */
    public PrintPumpDataBase(DeviceValuesRange dvr)
    {
        super(dvr);
    }

    
    
    /*
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
    */
    
    

    
    
    /**
     * Create document body.
     * 
     * @param document
     * @throws Exception
     */
    @Override
    public void fillDocumentBody(Document document) throws Exception
    {
        //Iterator<DailyValues> it = this.m_data.iterator();

        //int count = 0;

        Font f = this.text_normal; // new Font(this.base_helvetica , 12,
                                   // Font.NORMAL); // this.base_times

        PdfPTable datatable = new PdfPTable(getTableColumnsCount());
        datatable.setWidths(getTableColumnWidths());
        datatable.setWidthPercentage(100); // percentage

        datatable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT); // ALIGN_CENTER);
        datatable.getDefaultCell().setBorderWidth(1);

        datatable.addCell(new Phrase(ic.getMessage("DATE"), this.text_bold));
        datatable.addCell(new Phrase(ic.getMessage("TIME"), this.text_bold));
        datatable.addCell(new Phrase(ic.getMessage("BASE_TYPE"), this.text_bold));
        datatable.addCell(new Phrase(ic.getMessage("SUB_TYPE"), this.text_bold));
        datatable.addCell(new Phrase(ic.getMessage("VALUE_SHORT"), this.text_bold));
        datatable.addCell(new Phrase(ic.getMessage("OTHER_DATA"), this.text_bold));
        
        //writeAdditionalHeader(datatable);

        GregorianCalendar gc_end = m_dvr.getEndGC();
        gc_end.add(GregorianCalendar.DAY_OF_MONTH, 1);
        
        GregorianCalendar gc_current = m_dvr.getStartGC(); 
        
        
        do 
        {
            
            ATechDate atd = new ATechDate(da_local.getDataEntryObject().getDateTimeFormat(), gc_current);

            if (m_dvr.isDayEntryAvailable(atd.getATDateTimeAsLong()))
            {
                
                DeviceValuesDay dvd = m_dvr.getDayEntry(atd.getATDateTimeAsLong());
                
                // FIXME fix this
                datatable.addCell(new Phrase(atd.getDateString(), f));
                
                
                for(int i=0; i<dvd.getList().size(); i++)
                {

                    PumpValuesEntry pve = (PumpValuesEntry)dvd.getList().get(i);

                    ATechDate atdx = new ATechDate(da_local.getDataEntryObject().getDateTimeFormat(), pve.getDateTime());
      
                    if (i!=0)
                        datatable.addCell(new Phrase("", f));
                    
                    datatable.addCell(new Phrase(atdx.getTimeString(), f));
                    datatable.addCell(new Phrase(pve.getBaseTypeString(), f));
                    datatable.addCell(new Phrase(pve.getSubTypeString(), f));
                    datatable.addCell(new Phrase(pve.getValue(), f));
                    datatable.addCell(new Phrase(pve.getAdditionalDataPrint(), f));
                }
                
            }
            else
            {
                datatable.addCell(new Phrase(atd.getDateString(), f));
                this.writeEmptyColumnData(datatable);
            }
            

            gc_current.add(GregorianCalendar.DAY_OF_MONTH, 1);
            
        } while (gc_current.before(gc_end) );

        document.add(datatable);

        //System.out.println("Elements all: " + this.m_data.size() + " in iterator: " + count);

    }
    
    



    /** 
     * Return columns widths for table
     * @return
     */
    @Override
    public int[] getTableColumnWidths()
    {
        int headerwidths[] = { 9, 7,
                               10, 10, 12, 52 
                                }; // percentage
        return headerwidths;
    }

    
    /**
     * Return count of table columns
     * @return
     */
    @Override
    public int getTableColumnsCount()
    {
        return 6;
    }


    /**
     * Get text for title
     * 
     * @return title
     */
    @Override
    public String getTitleText()
    {
        return "PUMP_DATA_BASE";
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
        //table.addCell(new Phrase(ic.getMessage("CH"), this.text_bold));
    }


    /**
     * Write empty column data. If there is no data, this is used, to fill empty places.
     * 
     * @param table
     * @throws Exception
     */
    //@Override
    public void writeEmptyColumnData(PdfPTable table) throws Exception
    {
        table.addCell(new Phrase("", this.text_normal));
        table.addCell(new Phrase("", this.text_normal));
        table.addCell(new Phrase("", this.text_normal));
        table.addCell(new Phrase("", this.text_normal));
        table.addCell(new Phrase("", this.text_normal));
    }

    /**
     * Write data in column
     * 
     * @param table
     * @param mp
     * @throws Exception
     */
    @Override
    public void writeColumnData(PdfPTable table, Object /*DailyFoodEntry*/ mp) throws Exception
    {
        /*
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
*/
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
    }


    /**
     * Returns base filename for printing job, this is just part of end filename (starting part)
     */
    @Override
    public String getFileNameBase()
    {
        return "PumpDataBase";
    }



    
    
    
    
    
    
}




