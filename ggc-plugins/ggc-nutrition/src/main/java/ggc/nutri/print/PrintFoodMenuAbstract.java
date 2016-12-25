package ggc.nutri.print;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.print.engine.ITextDocumentPrintSettings;
import com.atech.print.engine.PrintAbstractIText;
import com.itextpdf.text.*;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfPTable;

import ggc.core.data.DailyValues;
import ggc.core.data.DailyValuesRow;
import ggc.core.data.DayValuesData;
import ggc.core.data.ExtendedDailyValueType;
import ggc.core.util.DataAccess;
import ggc.nutri.db.datalayer.DailyFoodEntries;
import ggc.nutri.db.datalayer.DailyFoodEntry;
import ggc.nutri.util.DataAccessNutri;

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

public abstract class PrintFoodMenuAbstract extends PrintAbstractIText
{

    private static final Logger LOG = LoggerFactory.getLogger(PrintFoodMenuAbstract.class);

    DayValuesData dayValuesData;
    DataAccess dataAccessCore;


    /**
     * Constructor
     *
     * @param mv
     */
    public PrintFoodMenuAbstract(DayValuesData mv)
    {
        super(DataAccessNutri.getInstance(), false);

        this.dayValuesData = mv;
        dataAccessCore = DataAccess.getInstance();

        this.init();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Paragraph getTitle()
    {
        Paragraph p = new Paragraph();

        Font f = new Font(this.baseFontTimes, 16, Font.BOLD);

        p.setAlignment(Element.ALIGN_CENTER);
        p.add(new Paragraph("", f));
        p.add(new Paragraph(this.i18nControl.getMessage(getTitleText()) + " ["
                + this.dayValuesData.getFromAsLocalizedDate() + " - " + this.dayValuesData.getToAsLocalizedDate() + "]",
                f));

        p.add(new Paragraph(
                this.i18nControl.getMessage("FOR") + " "
                        + this.dataAccessCore.getConfigurationManagerWrapper().getUserName(),
                new Font(FontFamily.TIMES_ROMAN, 12, Font.ITALIC)));
        p.add(new Paragraph("", f));
        p.add(new Paragraph("", f));

        return p;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void fillDocumentBody(Document document) throws Exception
    {
        Iterator<DailyValues> it = this.dayValuesData.iterator();

        int count = 0;

        Font f = this.textFontNormal;

        PdfPTable datatable = new PdfPTable(getTableColumnsCount());
        datatable.setWidths(getTableColumnWidths());
        datatable.setWidthPercentage(100); // percentage

        datatable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT); // ALIGN_CENTER);
        datatable.getDefaultCell().setBorderWidth(1);

        datatable.addCell(this.createBoldTextPhrase("DATE"));
        datatable.addCell(this.createBoldTextPhrase("TIME"));
        datatable.addCell(this.createBoldTextPhrase("PRINT_FOOD_DESC"));
        datatable.addCell(this.createBoldTextPhrase("WEIGHT_TYPE"));
        datatable.addCell(this.createBoldTextPhrase("AMOUNT_SHORT"));

        writeAdditionalHeader(datatable);

        while (it.hasNext())
        {
            DailyValues dv = it.next();
            dv.sort();

            datatable.addCell(new Phrase(dv.getDateAsLocalizedString(), f));

            int active_day_entry = 0;

            for (int i = 0; i < dv.getRowCount(); i++)
            {

                DailyValuesRow rw = dv.getRow(i);

                if (!this.dataAccessCore.isValueSet(rw.getMealsIds())
                        && !this.dataAccessCore.isValueSet(rw.getExtendedValue(ExtendedDailyValueType.FoodDescription)))
                {
                    continue;
                }

                if (active_day_entry > 0)
                {
                    datatable.addCell(new Phrase("", f));
                }

                active_day_entry++;

                datatable.addCell(new Phrase(rw.getTimeAsString(), f));

                if (this.dataAccessCore.isValueSet(rw.getMealsIds()))
                {

                    DailyFoodEntries mpts = new DailyFoodEntries(rw.getMealsIds(), true);
                    writeTogetherData(datatable, rw);

                    for (int j = 0; j < mpts.getElementsCount(); j++)
                    {
                        DailyFoodEntry mp = mpts.getElement(j);
                        this.writeColumnData(datatable, mp);
                    }
                }

                if (this.dataAccessCore.isValueSet(rw.getExtendedValue(ExtendedDailyValueType.FoodDescription)))
                {
                    writeFoodDescData(datatable, rw);
                }

            } // for

            if (active_day_entry == 0)
            {
                this.writeEmptyColumnData(datatable);
            }

            count++;

        }

        document.add(datatable);

        LOG.debug("Elements all: " + this.dayValuesData.size() + " in iterator: " + count);

    }


    /**
     * Get Formated Value (String correctly formated from float value)
     *
     * @param value
     * @param dec_places
     * @return
     */
    public String getFormatedValue(float value, int dec_places)
    {
        return this.dataAccessCore.getDecimalHandler().getDecimalAsString(value, dec_places); // ch

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
    public String getFileNameRange()
    {
        return this.dayValuesData.getRangeBeginObject().getDateFilenameString() + "-"
                + this.dayValuesData.getRangeEndObject().getDateFilenameString();
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
     * Write Food Description Data
     *
     * @param table
     * @param mp
     * @throws Exception
     */
    public abstract void writeFoodDescData(PdfPTable table, DailyValuesRow mp) throws Exception;


    /**
     * Write empty column data. If there is no data, this is used, to fill empty places.
     *
     * @param table
     * @throws Exception
     */
    public abstract void writeEmptyColumnData(PdfPTable table) throws Exception;

}
