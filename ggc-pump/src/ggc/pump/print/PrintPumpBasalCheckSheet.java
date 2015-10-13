package ggc.pump.print;

import java.util.GregorianCalendar;

import com.atech.i18n.mgr.LanguageManager;
import com.atech.print.engine.ITextDocumentPrintSettings;
import com.atech.print.engine.PrintAbstractITextWithDataRead;
import com.atech.print.engine.PrintParameters;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;

import ggc.core.util.DataAccess;
import ggc.core.util.GGCLanguageManagerRunner;
import ggc.pump.db.PumpProfile;
import ggc.pump.util.DataAccessPump;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
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
 *  Filename:     PrintPumpBasalCheckSheet
 *  Description:  Sheet for changing Basals
 *
 *  Author: Andy {andy@atech-software.com}
 */
public class PrintPumpBasalCheckSheet extends PrintAbstractITextWithDataRead
{

    GregorianCalendar startDate = null;
    PumpProfile profileForDate;


    /**
     * Constructor
     *
     * @param parameters
     */
    public PrintPumpBasalCheckSheet(PrintParameters parameters)
    {
        super(DataAccessPump.getInstance(), parameters, true);
    }


    @Override
    public void initData()
    {
        if (printParameters.containsKey("START_DATE"))
            this.startDate = null;
        else
            this.startDate = new GregorianCalendar();

        profileForDate = DataAccessPump.getInstance().getDb().getProfileForDayAndTime(this.startDate);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void fillDocumentBody(Document document) throws Exception
    {
        // int count = 0;

        Font f = this.textFontNormal;

        PdfPTable mainTable = new PdfPTable(7);
        mainTable.setWidths(new int[] { 25, 5, 20, 5, 20, 5, 20 });
        mainTable.getDefaultCell().setBorderWidth(0);

        for (int i = 0; i < 5; i++)
        {

            PdfPTable datatable = new PdfPTable(i == 0 ? 4 : 3);
            datatable.setWidths(getTableColumnWidths(i));
            // datatable.setWidthPercentage(100); // percentage

            datatable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT); // ALIGN_CENTER);
            datatable.getDefaultCell().setBorderWidth(1);
            datatable.getDefaultCell().setFixedHeight(18);

            // datatable.addCell(this.createBoldTextPhrase("DATE"));

            if (i == 0)
            {
                datatable.addCell(this.createBoldTextPhrase("TIME"));
            }
            datatable.addCell(this.createBoldTextPhrase("BASAL"));
            datatable.addCell(this.createBoldTextPhrase("BG"));
            datatable.addCell(this.createBoldTextPhrase("NEW_BASAL"));

            // writeAdditionalHeader(datatable);

            // GregorianCalendar gc_end = this.deviceValuesRange.getEndGC();
            // gc_end.add(Calendar.DAY_OF_MONTH, 1);

            // GregorianCalendar gc_current = deviceValuesRange.getStartGC();

            // do

            int startHour = 21;

            for (int j = 0; j < 28; j++)
            {
                startHour++;

                if (startHour == 24)
                {
                    startHour = 0;
                }

                if (i == 0)
                {
                    writeDataLine(datatable, startHour, null);
                }
                else
                {
                    writeDataLine(datatable, null, null);
                }

                /*
                 * ATechDate atd = new
                 * ATechDate(da_local.getDataEntryObject().getDateTimeFormat(),
                 * gc_current);
                 * if
                 * (deviceValuesRange.isDayEntryAvailable(atd.
                 * getATDateTimeAsLong
                 * ()))
                 * {
                 * DeviceValuesDay dvd =
                 * deviceValuesRange.getDayEntry(atd.getATDateTimeAsLong());
                 * // FIXME fix this
                 * datatable.addCell(new Phrase(atd.getDateString(), f));
                 * for (int i = 0; i < dvd.getList().size(); i++)
                 * {
                 * PumpValuesEntry pve = (PumpValuesEntry) dvd.getList().get(i);
                 * ATechDate atdx = new
                 * ATechDate(da_local.getDataEntryObject().getDateTimeFormat(),
                 * pve.getDateTime());
                 * if (i != 0)
                 * {
                 * datatable.addCell(new Phrase("", f));
                 * }
                 * datatable.addCell(new Phrase(atdx.getTimeString(), f));
                 * datatable.addCell(new Phrase(pve.getBaseTypeString(), f));
                 * datatable.addCell(new Phrase(pve.getSubTypeString(), f));
                 * datatable.addCell(new Phrase(pve.getValuePrint(), f));
                 * datatable.addCell(new Phrase(pve
                 * .getAdditionalDataPrint(PumpValuesEntry.
                 * PRINT_ADDITIONAL_ALL_ENTRIES_WITH_FOOD), f));
                 * }
                 * }
                 * else
                 * {
                 * datatable.addCell(new Phrase(atd.getDateString(), f));
                 * this.writeEmptyColumnData(datatable);
                 * }
                 */

                // gc_current.add(Calendar.DAY_OF_MONTH, 1);

            } // while (gc_current.before(gc_end));

            mainTable.addCell(datatable);

            if (i != 5)
            {
                mainTable.addCell(this.createEmptyTextPhrase());
            }

        }

        document.add(mainTable);

        // System.out.println("Elements all: " + this.m_data.size() +
        // " in iterator: " + count);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ITextDocumentPrintSettings getCustomDocumentSettings()
    {
        return new ITextDocumentPrintSettings(10, 10, 10, 30);
    }


    private void writeDataLine(PdfPTable table, Integer hour, Float basalRate)
    {
        if (hour != null)
        {
            table.addCell(this.createNormalTextPhrase(hour + ":00"));

            if (basalRate != null)
                table.addCell(this.createNormalTextPhrase(basalRate.toString()));
            else
                table.addCell(this.createEmptyTextPhrase());

            table.addCell(this.createEmptyTextPhrase());
            table.addCell(this.createEmptyTextPhrase());
        }
        else
        {
            table.addCell(this.createEmptyTextPhrase());
            table.addCell(this.createEmptyTextPhrase());
            table.addCell(this.createEmptyTextPhrase());
        }
    }


    public int[] getTableColumnWidths(int tableNr)
    {
        if (tableNr == 0)
        {
            int headerwidths[] = { 25, 25, 25, 25 }; // percentage
            return headerwidths;
        }
        else
        {
            int headerwidths[] = { 33, 33, 34 }; // percentage
            return headerwidths;
        }
    }


    public String getTitleText()
    {
        return "PUMP_DATA_BASAL_CHECK";
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getFileNameBase()
    {
        return "PumpBasalCheckSheet";
    }


    public static void main(String[] args)
    {
        LanguageManager lm = new LanguageManager(new GGCLanguageManagerRunner()); // ,
                                                                                  // new
                                                                                  // GGCCoreICRunner();

        DataAccessPump da = DataAccessPump.createInstance(lm);

        // DeviceValuesRange dvr = DataAccessPump.getInstance().getDb()
        // .getRangePumpValues(this.getFromDateObject(),
        // this.getToDateObject());

        PrintPumpBasalCheckSheet pa = new PrintPumpBasalCheckSheet(new PrintParameters());
        // pa.init();

        System.out.println("Path: " + pa.getRelativeNameWithPath());

        // displayPDF(pa.getRelativeNameWithPath());

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
        p.add(new Paragraph(this.i18nControl.getMessage(getTitleText()), f));
        p.add(new Paragraph(
                this.i18nControl.getMessage("FOR") + " "
                        + DataAccess.getInstance().getConfigurationManagerWrapper().getUserName(),
                new Font(FontFamily.TIMES_ROMAN, 10, Font.ITALIC)));
        p.add(new Paragraph("", f));
        p.add(new Paragraph("", f));

        return p;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getFileNameRange()
    {
        /*
         * ATechDate atd1 = new
         * ATechDate(da_local.getDataEntryObject().getDateTimeFormat(),
         * deviceValuesRange.getStartGC());
         * ATechDate atd2 = new
         * ATechDate(da_local.getDataEntryObject().getDateTimeFormat(),
         * deviceValuesRange.getEndGC());
         */
        return ""; // atd1.getDateFilenameString() + "-" +
                   // atd2.getDateFilenameString();
    }


    public int[] getTableColumnWidths()
    {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public int getTextSize()
    {
        // TODO Auto-generated method stub
        return 9;
    }

}
