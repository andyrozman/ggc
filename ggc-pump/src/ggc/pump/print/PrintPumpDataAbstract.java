package ggc.pump.print;

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

import ggc.core.data.DailyValuesRow;
import ggc.core.util.DataAccess;
import ggc.plugin.data.DeviceValuesRange;
import ggc.pump.util.DataAccessPump;

import java.util.GregorianCalendar;

import com.atech.print.PrintAbstractIText;
import com.atech.utils.data.ATechDate;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
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
 *  Filename:     PrintFoodMenuAbstract
 *  Description:  Abstract class for printing Food Menu's
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */


public abstract class PrintPumpDataAbstract extends PrintAbstractIText
{

    protected DeviceValuesRange m_dvr;
    protected DataAccessPump da_local = null;
    
    /**
     * Constructor
     * 
     * @param dvr 
     */
    public PrintPumpDataAbstract(DeviceValuesRange dvr)
    {
        super(DataAccessPump.getInstance().getI18nControlInstance(), false);

        m_dvr = dvr;
        da_local = DataAccessPump.getInstance(); 

        init();
    }

    /**
     * Get Title
     * 
     * @see ggc.core.print.PrintAbstract#getTitle()
     */
    public Paragraph getTitle()
    {
        Paragraph p = new Paragraph();

        Font f = new Font(this.base_times, 14, Font.BOLD);

        p.setAlignment(Element.ALIGN_CENTER);
        p.add(new Paragraph("", f));
        p.add(new Paragraph(ic.getMessage(getTitleText()) + " [" + this.getDateString(this.m_dvr.getStartGC()) + " - "
                + this.getDateString(this.m_dvr.getEndGC()) + "]", f));
        p.add(new Paragraph(ic.getMessage("FOR") + " " + DataAccess.getInstance().getSettings().getUserName(), new Font(FontFamily.TIMES_ROMAN,
                10, Font.ITALIC)));
        p.add(new Paragraph("", f));
        p.add(new Paragraph("", f));

        return p;
    }


    protected String getDateString(GregorianCalendar gc)
    {
        return gc.get(GregorianCalendar.DAY_OF_MONTH) + "." + (gc.get(GregorianCalendar.MONTH) +1 ) + "." + gc.get(GregorianCalendar.YEAR); 
    }
    
    
    /** 
     * Get Text Size
     */
    public int getTextSize()
    {
        return 8;
    }
    
    
    /**
     * Create document body.
     * 
     * @param document
     * @throws Exception
     */
    @Override
    public abstract void fillDocumentBody(Document document) throws Exception;
    
    /*
    {
        Iterator<DailyValues> it = this.m_data.iterator();

        int count = 0;

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
        datatable.addCell(new Phrase(ic.getMessage("PRINT_FOOD_DESC"), this.text_bold));
        datatable.addCell(new Phrase(ic.getMessage("WEIGHT_TYPE"), this.text_bold));
        datatable.addCell(new Phrase(ic.getMessage("AMOUNT_SHORT"), this.text_bold));

        writeAdditionalHeader(datatable);

        while (it.hasNext())
        {
            DailyValues dv = it.next();
            dv.sort();

            datatable.addCell(new Phrase(dv.getDateAsLocalizedString(), f));

//            System.out.println("Row count: " + dv.getRowCount());
//            System.out.println(dv.getDateAsString());

            int active_day_entry = 0;

/*            if (dv.getRowCount() == 0)
            {
//                this.writeEmptyColumnData(datatable);
            }
            else */
     /*       {

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

                    
/*                    
                    DailyFoodEntries mpts = new DailyFoodEntries(rw.getMealsIds(), true);

                    writeTogetherData(datatable, rw);


                    for (int j = 0; j < mpts.getElementsCount(); j++)
                    {

                        DailyFoodEntry mp = mpts.getElement(j);

                        this.writeColumnData(datatable, mp);


                    }
*/
    /*
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
/*
                count++;
            }
        }

        document.add(datatable);

        System.out.println("Elements all: " + this.m_data.size() + " in iterator: " + count);

    }
*/
    
    /**
     * Returns data part of filename for printing job, showing which data is being printed
     * 
     * @return 
     */
    @Override
    public String getFileNameRange()
    {
        ATechDate atd1 = new ATechDate(da_local.getDataEntryObject().getDateTimeFormat(), m_dvr.getStartGC());
        ATechDate atd2 = new ATechDate(da_local.getDataEntryObject().getDateTimeFormat(), m_dvr.getEndGC());
        
        return atd1.getDateFilenameString() + "-" + atd2.getDateFilenameString();
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
    public abstract void writeColumnData(PdfPTable table, Object /*DailyFoodEntry*/ mp) throws Exception;

    /**
     * Write empty column data. If there is no data, this is used, to fill empty places.
     * 
     * @param table
     * @throws Exception
     */
    public abstract void writeEmptyColumnData(PdfPTable table) throws Exception;
    
    
}