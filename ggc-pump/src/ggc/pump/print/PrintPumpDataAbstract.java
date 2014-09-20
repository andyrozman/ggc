package ggc.pump.print;

import ggc.core.data.DailyValuesRow;
import ggc.core.util.DataAccess;
import ggc.plugin.data.DeviceValuesRange;
import ggc.pump.util.DataAccessPump;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.atech.print.engine.ITextDocumentPrintSettings;
import com.atech.print.engine.PrintAbstractIText;
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

    protected DeviceValuesRange deviceValuesRange;
    protected DataAccessPump da_local = null;

    /**
     * {@inheritDoc}
     */
    public PrintPumpDataAbstract(DeviceValuesRange dvr)
    {
        super(DataAccessPump.getInstance().getI18nControlInstance(), false);

        deviceValuesRange = dvr;
        da_local = DataAccessPump.getInstance();

        init();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Paragraph getTitle()
    {
        Paragraph p = new Paragraph();

        Font f = new Font(this.baseFontTimes, 14, Font.BOLD);

        p.setAlignment(Element.ALIGN_CENTER);
        p.add(new Paragraph("", f));
        p.add(new Paragraph(this.i18nControl.getMessage(getTitleText()) + " [" + this.getDateString(this.deviceValuesRange.getStartGC()) + " - "
                + this.getDateString(this.deviceValuesRange.getEndGC()) + "]", f));
        p.add(new Paragraph(this.i18nControl.getMessage("FOR") + " " + DataAccess.getInstance().getSettings().getUserName(), new Font(FontFamily.TIMES_ROMAN,
                10, Font.ITALIC)));
        p.add(new Paragraph("", f));
        p.add(new Paragraph("", f));

        return p;
    }


    protected String getDateString(GregorianCalendar gc)
    {
        return gc.get(Calendar.DAY_OF_MONTH) + "." + (gc.get(Calendar.MONTH) +1 ) + "." + gc.get(Calendar.YEAR);
    }


    /**
     * Get Text Size
     */
    public int getTextSize()
    {
        return 8;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ITextDocumentPrintSettings getCustomDocumentSettings()
    {
    	return new ITextDocumentPrintSettings(30, 30, 10, 30);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void fillDocumentBody(Document document) throws Exception;


    /**
     * {@inheritDoc}
     */
    @Override
    public String getFileNameRange()
    {
        ATechDate atd1 = new ATechDate(da_local.getDataEntryObject().getDateTimeFormat(), deviceValuesRange.getStartGC());
        ATechDate atd2 = new ATechDate(da_local.getDataEntryObject().getDateTimeFormat(), deviceValuesRange.getEndGC());

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
    public void writeEmptyColumnData(PdfPTable table) throws Exception
    {
        for(int i=0; i<this.getTableColumnsCount(); i++)
        {
            table.addCell(this.createEmptyTextPhrase());
        }
    }


}