package ggc.pump.print;

import ggc.core.db.GGCDbLoader;
import ggc.core.db.tool.DbToolApplicationGGC;
import ggc.core.util.DataAccess;
import ggc.core.util.GGCLanguageManagerRunner;
import ggc.plugin.data.DeviceValuesDay;
import ggc.plugin.data.DeviceValuesEntry;
import ggc.plugin.data.DeviceValuesRange;
import ggc.plugin.util.DataAccessPlugInBase;
import ggc.pump.data.PumpDeviceValueType;
import ggc.pump.data.PumpValuesHour;
import ggc.pump.data.PumpValuesHourProcessor;
import ggc.pump.util.DataAccessPump;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.atech.i18n.I18nControlAbstract;
import com.atech.i18n.mgr.LanguageManager;
import com.atech.print.engine.ITextDocumentPrintSettings;
import com.atech.print.engine.PrintAbstractITextWithDataRead;
import com.atech.print.engine.PrintParameters;
import com.atech.utils.data.ATechDate;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

/**
 * Application: GGC - GNU Gluco Control
 * Plug-in: Pump Tool (support for Pump devices)
 * See AUTHORS for copyright information.
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * Filename: PrintPumpDataDailyTimeSheet
 * Description: Report with daily values in smaller tables (one for each day and
 * all hours displayed)
 * Author: Andy {andy@atech-software.com}
 */

// WARNING: THIS IS WORK IN PROGRESS, PLEASE DON'T EDIT. Andy

public class PrintPumpDataDailyTimeSheet extends PrintAbstractITextWithDataRead
{
    Font smallFont = null;
    protected DeviceValuesRange deviceValuesRange;

    private GregorianCalendar gc_from;
    private GregorianCalendar gc_to;
    DataAccessPump dataAccessPump = DataAccessPump.getInstance();
    I18nControlAbstract i18nControl = dataAccessPump.getI18nControlInstance();
    PumpValuesHourProcessor pumpValuesHourProcessor;

    public PrintPumpDataDailyTimeSheet(PrintParameters parameters)
    {
        super(DataAccessPump.getInstance(), parameters, false);

        smallFont = new Font(this.baseFontTimes, 6, Font.NORMAL);
        pumpValuesHourProcessor = new PumpValuesHourProcessor();

        this.initData();
        this.init();

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
        p.add(new Paragraph(this.i18nControl.getMessage("FOR") + " "
                + DataAccess.getInstance().getSettings().getUserName(), new Font(FontFamily.TIMES_ROMAN, 10,
                Font.ITALIC)));
        p.add(new Paragraph("", f));
        p.add(new Paragraph("", f));

        return p;
    }

    @Override
    public void initData()
    {

        // FIXME read from parameters

        gc_from = new GregorianCalendar(2014, 8, 20);
        gc_to = new GregorianCalendar(2014, 8, 28);

        deviceValuesRange = DataAccessPump.getInstance().getDb().getRangePumpValues(gc_from, gc_to);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillDocumentBody(Document document) throws Exception
    {
        GregorianCalendar gc_current = deviceValuesRange.getStartGC();

        GregorianCalendar gc_end = deviceValuesRange.getEndGC();
        gc_end.add(Calendar.DAY_OF_MONTH, 1);

        PdfPTable mainTable = new PdfPTable(1);
        mainTable.setWidthPercentage(100);
        mainTable.getDefaultCell().setBorderWidth(0);

        do
        {

            ATechDate atd = new ATechDate(dataAccessPump.getDataEntryObject().getDateTimeFormat(), gc_current);

            if (deviceValuesRange.isDayEntryAvailable(atd.getATDateTimeAsLong()))
            {
                DeviceValuesDay dvd = deviceValuesRange.getDayEntry(atd.getATDateTimeAsLong());
                dvd.prepareHourlyEntries();
                createDayEntry(mainTable, dvd, gc_current);
            }

            gc_current.add(Calendar.DAY_OF_MONTH, 1);

        } while (gc_current.before(gc_end));

        document.add(mainTable);
    }

    /**
     * TODO
     *
     * + main view table (day, comment, space)    100%
     * - multiple BG in comment                   100%
     * - comments                                   0%
     * - bolus swuare or extended                   0%
     * + sums                                     100%
     * - days resolve                               0%
     * - basal                                      0%
     *
     * NOT IMPORTANT FOR NOW:
     * - special pump statues (stopped pump, change of pump material, TBR)
     * - Hipo, Hyper ?
     *
     *
     * PROFILES REPORT                              0%
     *
     */

    private void createDayEntry(PdfPTable mainTable, DeviceValuesDay deviceValuesDay, GregorianCalendar gcCurrent)
            throws Exception
    {
        PdfPTable datatable = new PdfPTable(27);
        datatable.setWidths(new float[] { 4.0f, 6.0f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f,
                                         3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f,
                                         6.0f }); // 6 + 2 + 4
        datatable.setWidthPercentage(100); // percentage

        datatable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER); // ALIGN_CENTER);
        datatable.getDefaultCell().setBorderWidth(1);

        // cell 1
        datatable.addCell(this.createBoldTextPhrase("DAY"));
        datatable.addCell(this.createBoldTextPhrase("TIME"));

        for (int i = 0; i < 24; i++)
        {
            datatable.addCell(this.createBoldTextPhrase("" + i));
        }

        datatable.addCell(this.createBoldTextPhrase("SUM"));

        pumpValuesHourProcessor.clearComments();

        for (int j = 0; j < 4; j++)
        {
            if (j == 0)
            {
                // SU=Su
                // MO=Mo
                // TU=Tu
                // WE=We
                // TH=Th
                // FR=Fr
                // SA=Sa

                datatable.addCell(this.createNormalTextPhrase("???"));
                datatable.addCell(this.createBoldTextPhrase("BASAL"));

                writeHourlyValues(PumpDeviceValueType.BASAL, datatable, deviceValuesDay);
            }
            else if (j == 1)
            {
                PdfPCell cell = new PdfPCell();
                cell.setPhrase(this.createBoldTextPhrase(gcCurrent.get(Calendar.DAY_OF_MONTH) + "."));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP);
                cell.setBorderWidth(1);
                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);

                datatable.addCell(cell);
                datatable.addCell(this.createBoldTextPhrase("BOLUS"));

                writeHourlyValues(PumpDeviceValueType.BOLUS, datatable, deviceValuesDay);
            }
            else if (j == 2)
            {
                PdfPCell cell = new PdfPCell();
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPhrase(this.createBoldTextPhrase(dataAccessPump.getMonthsArray()[gcCurrent.get(Calendar.MONTH)]
                        .substring(0, 3)));

                datatable.addCell(cell);
                datatable.addCell(this.createBoldTextPhrase("BG"));

                writeHourlyValues(PumpDeviceValueType.BG, datatable, deviceValuesDay);
            }
            else
            // if (j==3)
            {
                PdfPCell cell = new PdfPCell();
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
                cell.setBorderWidth(1);
                cell.setVerticalAlignment(Element.ALIGN_TOP);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPhrase(this.createBoldTextPhrase("" + gcCurrent.get(Calendar.YEAR)));

                datatable.addCell(cell);
                datatable.addCell(this.createBoldTextPhrase("CH"));

                writeHourlyValues(PumpDeviceValueType.CH, datatable, deviceValuesDay);
            }

        }

        mainTable.addCell(datatable);

        // comment table
        PdfPTable tableComment = new PdfPTable(2);
        tableComment.setWidths(new float[] { 8.0f, 92.0f });
        tableComment.setWidthPercentage(100);
        tableComment.getDefaultCell().setBorderWidth(0);
        tableComment.addCell(this.createBoldTextPhrase(this.i18nControl.getMessage("COMMENT") + ":"));
        tableComment.addCell(this.createNormalTextPhrase(this.pumpValuesHourProcessor.getFullComment()));

        mainTable.addCell(tableComment);

        // empty line
        PdfPCell cell = new PdfPCell();
        cell.setFixedHeight(12);
        cell.setBorder(0);
        cell.setPhrase(this.createEmptyTextPhrase());

        mainTable.addCell(cell);

    }

    private void writeHourlyValues(PumpDeviceValueType type, PdfPTable table, DeviceValuesDay deviceValuesDay)
    {

        float sum = 0.0f;
        float count = 0;

        if (deviceValuesDay != null)
        {
            deviceValuesDay.prepareHourlyEntries();
        }

        for (int i = 0; i < 24; i++)
        {
            if (deviceValuesDay == null)
            {
                table.addCell(this.createEmptyTextPhraseSmall());
            }
            else
            {
                List<DeviceValuesEntry> entries = deviceValuesDay.getEntriesForHour(i);

                if (type == PumpDeviceValueType.BASAL)
                {
                    table.addCell(this.createNormalTextPhraseSmall("xx.xx"));

                    // sum += pumpValuesHour.getBolus();
                    // table.addCell(this.createNormalTextPhraseSmall(dataAccessPump
                    // .getFormatedBolusValue(pumpValuesHour.getBolus())));

                }
                else
                {
                    PumpValuesHour pumpValuesHour = pumpValuesHourProcessor.createPumpValuesHour(entries);

                    float value = pumpValuesHourProcessor.getValueForType(entries, type);
                    sum += value;

                    if (type == PumpDeviceValueType.BOLUS)
                    {
                        if (pumpValuesHour.getBolus() > 0.0f)
                        {
                            sum += pumpValuesHour.getBolus();
                            table.addCell(this.createNormalTextPhraseSmall(dataAccessPump
                                    .getFormatedBolusValue(pumpValuesHour.getBolus())));

                            // FIXME special boluses
                            // pumpValuesHourProcessor.addComments(PumpDeviceValueType.BG,
                            // pumpValuesHour.getMultipleBgs());

                        }
                        else
                        {
                            table.addCell(this.createEmptyTextPhraseSmall());
                        }

                    }
                    else if (type == PumpDeviceValueType.BG)
                    {
                        if (pumpValuesHour.getBgs().size() > 1)
                        {
                            table.addCell(this.createNormalTextPhraseSmall("*"));

                            pumpValuesHourProcessor
                                    .addComments(PumpDeviceValueType.BG, pumpValuesHour.getMultipleBgs());
                        }
                        else if (pumpValuesHour.getBgs().size() == 1)
                        {
                            sum += dataAccessPump.getDisplayedBG(pumpValuesHour.getBgProcessedValue());
                            count++;

                            table.addCell(this.createNormalTextPhraseSmall(dataAccessPump
                                    .getDisplayedBGString(pumpValuesHour.getBgProcessedValue())));
                        }
                        else
                        {
                            table.addCell(this.createEmptyTextPhraseSmall());
                        }
                    }
                    else
                    {
                        // CH
                        if (pumpValuesHour.getCH() > 0.0f)
                        {
                            sum += pumpValuesHour.getCH();
                            table.addCell(this.createNormalTextPhraseSmall(DataAccessPlugInBase.Decimal0Format
                                    .format(pumpValuesHour.getCH())));
                        }
                        else
                        {
                            table.addCell(this.createEmptyTextPhraseSmall());
                        }
                    }

                    // FIXME add comments

                }
            }
        }

        if (sum > 0.0f)
        {
            if (type == PumpDeviceValueType.BG)
            {
                float d = sum / (count * (1.0f));

                table.addCell(this.createNormalTextPhrase(dataAccessPump.getDisplayedBGString(d)));
            }
            else if (type == PumpDeviceValueType.BASAL)
            {
                table.addCell(this.createNormalTextPhrase(dataAccessPump.getFormatedBasalValue(sum)));
            }
            else if (type == PumpDeviceValueType.BOLUS)
            {
                table.addCell(this.createNormalTextPhrase(dataAccessPump.getFormatedBolusValue(sum)));
            }
            else
            {
                table.addCell(this.createNormalTextPhrase(DataAccessPlugInBase.Decimal0Format.format(sum)));
            }

        }
        else
        {
            table.addCell(this.createEmptyTextPhraseSmall());
        }

    }

    private Phrase createEmptyTextPhraseSmall()
    {
        return new Phrase("", smallFont);
    }

    private Phrase createNormalTextPhraseSmall(String text)
    {
        return new Phrase(this.i18nControl.getMessage(text), smallFont);
    }

    /**
     * {@inheritDoc}
     */
    public String getTitleText()
    {
        return "PUMP_DATA_DAILY_TIMESHEET";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFileNameBase()
    {
        return "Pump_DailyTimeSheet";
    }

    @Override
    public ITextDocumentPrintSettings getCustomDocumentSettings()
    {
        return new ITextDocumentPrintSettings(20, 20, 10, 30);
    }

    @Override
    public String getFileNameRange()
    {
        // TODO Auto-generated method stub
        return "";
    }

    @Override
    public int getTextSize()
    {
        return 6;
    }

    public static void main(String[] args)
    {
        LanguageManager lm = new LanguageManager(new GGCLanguageManagerRunner());

        DbToolApplicationGGC m_configFile = new DbToolApplicationGGC();
        m_configFile.loadConfig();

        DataAccess daCore = DataAccess.getInstance();
        GGCDbLoader loader = new GGCDbLoader(daCore);
        loader.run();

        // GGCDbConfig db = new GGCDbConfig(false);

        // GGCDb db = new GGCDb(daCore);
        // db.initDb();

        DataAccessPump da = DataAccessPump.createInstance(lm);

        da.createDb(daCore.getHibernateDb());

        PrintPumpDataDailyTimeSheet pa = new PrintPumpDataDailyTimeSheet(new PrintParameters());
        // pa.init();

        System.out.println("Path: " + pa.getRelativeNameWithPath());

        // displayPDF(pa.getRelativeNameWithPath());

    }
}
