package ggc.pump.print;

import ggc.core.db.GGCDbLoader;
import ggc.core.db.tool.DbToolApplicationGGC;
import ggc.core.util.DataAccess;
import ggc.plugin.data.DeviceValuesRange;
import ggc.pump.data.PumpValuesHourProcessor;
import ggc.pump.data.db.GGCPumpDb;
import ggc.pump.data.profile.ProfileSubPattern;
import ggc.pump.db.PumpProfile;
import ggc.pump.util.DataAccessPump;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.atech.i18n.I18nControlAbstract;
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
 *  Filename:     PrintPumpDataDailyTimeSheet
 *  Description:  Report with daily values in smaller tables (one for each day and
 * all hours displayed)
 *
 *  Author: andyrozman {andy@atech-software.com}
 */

// WARNING: THIS IS WORK IN PROGRESS, PLEASE DON'T EDIT. Andy

public class PrintPumpDataProfiles extends PrintAbstractITextWithDataRead
{
    Font smallFont = null;
    protected DeviceValuesRange deviceValuesRange;

    private GregorianCalendar gcFrom;
    private GregorianCalendar gcTill;
    DataAccessPump dataAccessPump = DataAccessPump.getInstance();
    I18nControlAbstract i18nControl = dataAccessPump.getI18nControlInstance();
    PumpValuesHourProcessor pumpValuesHourProcessor;
    List<PumpProfile> profilesRange;

    public PrintPumpDataProfiles(PrintParameters parameters)
    {
        super(DataAccessPump.getInstance(), parameters, false);

        smallFont = new Font(this.baseFontTimes, 5, Font.NORMAL);
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
        p.add(new Paragraph(this.i18nControl.getMessage(getTitleText()) + " [" + this.getDateString(this.gcFrom)
                + " - " + this.getDateString(this.gcTill) + "]", f));
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

        if (this.printParameters.containsKey("RANGE_FROM"))
        {
            gcFrom = (GregorianCalendar) this.printParameters.get("RANGE_FROM");
            gcTill = (GregorianCalendar) this.printParameters.get("RANGE_TO");
        }
        else
        {
            gcFrom = new GregorianCalendar(2014, 8, 22);
            gcTill = new GregorianCalendar(2014, 9, 12);
        }

        GGCPumpDb db = DataAccessPump.getInstance().getDb();

        this.profilesRange = db.getProfilesForRange(gcFrom, gcTill);

        System.out.println("Profiles all: " + this.profilesRange.size());

    }

    // TODO move this to abstract class for printing
    protected String getDateString(GregorianCalendar gc)
    {
        return gc.get(Calendar.DAY_OF_MONTH) + "." + (gc.get(Calendar.MONTH) + 1) + "." + gc.get(Calendar.YEAR);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillDocumentBody(Document document) throws Exception
    {

        PdfPTable datatable = new PdfPTable(28);
        datatable.setWidths(new float[] { 9.0f, 7.0f, 7.0f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f,
                                         3.0f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f, 3.0f,
                                         3.0f, 5.0f }); // 6 + 2 + 4
        // 1 + 12
        datatable.setWidthPercentage(100); // percentage

        datatable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER); // ALIGN_CENTER);
        datatable.getDefaultCell().setBorderWidth(1);

        // cell 1
        datatable.addCell(this.createBoldTextPhrase("NAME"));
        datatable.addCell(this.createBoldTextPhrase("FROM"));
        datatable.addCell(this.createBoldTextPhrase("TILL"));

        for (int i = 0; i < 24; i++)
        {
            datatable.addCell(this.createBoldTextPhrase("" + i));
        }

        datatable.addCell(this.createBoldTextPhrase("SUM"));

        for (PumpProfile pp : this.profilesRange)
        {
            createProfileEntry(datatable, pp);
        }

        document.add(datatable);
    }

    private void createProfileEntry(PdfPTable table, PumpProfile profile) throws Exception
    {

        table.addCell(this.createNormalTextPhrase(profile.getName()));
        table.addCell(this.createNormalTextPhraseSmall(getDateTime(profile.getActive_from())));
        table.addCell(this.createNormalTextPhraseSmall(getDateTime(profile.getActive_till())));

        // table.addCell(this.createNormalTextPhraseSmall(dataAccessPump.getFormatedBasalValue(value)));

        float sum = 0.0f;

        for (int hour = 0; hour < 24; hour++)
        {
            float value = -1.0f;

            if (profile != null)
            {
                ProfileSubPattern patternForHour = profile.getPatternForHour(hour);

                if (patternForHour != null)
                {
                    value = patternForHour.getAmount();
                    sum += value;
                }
            }

            if (value < 0.0f)
            {
                table.addCell(this.createNormalTextPhraseSmall(""));
            }
            else
            {
                table.addCell(this.createNormalTextPhraseSmall(dataAccessPump.getFormatedBasalValue(value)));
            }

        }

        table.addCell(this.createNormalTextPhrase(dataAccessPump.getFormatedBasalValue(sum)));

    }

    private String getDateTime(long time)
    {
        String dt = ATechDate.getDateString(ATechDate.FORMAT_DATE_AND_TIME_S, time);

        dt += " ";
        dt += ATechDate.getTimeString(ATechDate.FORMAT_DATE_AND_TIME_S, time);

        return dt;

    }

    // private Phrase createEmptyTextPhraseSmall()
    // {
    // return new Phrase("", smallFont);
    // }

    private Phrase createNormalTextPhraseSmall(String text)
    {
        return new Phrase(this.i18nControl.getMessage(text), smallFont);
    }

    public String getTitleText()
    {
        return "PUMP_DATA_PROFILES";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFileNameBase()
    {
        return "Pump_Profiles";
    }

    @Override
    public ITextDocumentPrintSettings getCustomDocumentSettings()
    {
        return new ITextDocumentPrintSettings(20, 20, 10, 30);
    }

    @Override
    public String getFileNameRange()
    {
        ATechDate atd1 = new ATechDate(dataAccessPump.getDataEntryObject().getDateTimeFormat(), this.gcFrom);
        ATechDate atd2 = new ATechDate(dataAccessPump.getDataEntryObject().getDateTimeFormat(), this.gcTill);

        return atd1.getDateFilenameString() + "-" + atd2.getDateFilenameString();
    }

    @Override
    public int getTextSize()
    {
        return 6;
    }

    public static void main(String[] args)
    {
        // LanguageManager lm = new LanguageManager(new
        // GGCLanguageManagerRunner());

        DbToolApplicationGGC m_configFile = new DbToolApplicationGGC();
        m_configFile.loadConfig();

        DataAccess daCore = DataAccess.getInstance();

        GGCDbLoader loader = new GGCDbLoader(daCore);
        loader.run();

        // GGCDbConfig db = new GGCDbConfig(false);

        // GGCDb db = new GGCDb(daCore);
        // db.initDb();

        DataAccessPump da = DataAccessPump.createInstance(daCore.getLanguageManager());

        da.createPlugInDataRetrievalContext();

        da.createDb(daCore.getHibernateDb());

        PrintPumpDataProfiles pa = new PrintPumpDataProfiles(new PrintParameters());
        // pa.init();

        System.out.println("Path: " + pa.getRelativeNameWithPath());

        // displayPDF(pa.getRelativeNameWithPath());

    }
}
