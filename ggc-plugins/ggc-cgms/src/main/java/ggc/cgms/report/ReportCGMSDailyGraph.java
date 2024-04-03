package ggc.cgms.report;

import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.jfree.chart.JFreeChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.i18n.I18nControlAbstract;
import com.atech.print.engine.ITextDocumentPrintSettings;
import com.atech.print.engine.PrintAbstractITextWithDataRead;
import com.atech.print.engine.PrintParameters;
import com.atech.utils.ATDataAccessLMAbstract;
import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;
import com.itextpdf.text.*;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import ggc.cgms.data.db.GGC_CGMSDb;
import ggc.cgms.defs.CGMSPluginDefinition;
import ggc.cgms.util.DataAccessCGMS;
import ggc.core.db.GGCDbLoader;
import ggc.core.db.tool.DbToolApplicationGGC;
import ggc.core.util.DataAccess;
import ggc.plugin.data.DeviceValuesDay;
import ggc.plugin.data.DeviceValuesRange;
import ggc.plugin.graph.data.CGMSGraphDataHandler;
import ggc.plugin.report.data.cgms.CGMSDayData;

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

public class ReportCGMSDailyGraph extends PrintAbstractITextWithDataRead
{

    private static final Logger LOG = LoggerFactory.getLogger(ReportCGMSDailyGraph.class);

    DataAccessCGMS dataAccessCGMS = DataAccessCGMS.getInstance();
    I18nControlAbstract i18nControl = dataAccessCGMS.getI18nControlInstance();

    CGMSGraphDataHandler cgmsGraphDataHandler;

    Font smallFont = null;
    private GregorianCalendar gcFrom;
    private GregorianCalendar gcTill;
    HashMap<String, Font> fontColors = new HashMap<String, Font>();

    boolean hasCGMSData = false;

    protected DeviceValuesRange deviceValuesRange;
    Map<Long, CGMSDayData> cgmsReadingsDataMap;


    // List<PumpProfile> profilesRange;

    public ReportCGMSDailyGraph(PrintParameters parameters)
    {
        super(DataAccessCGMS.getInstance(), parameters, false);

        smallFont = new Font(this.baseFontTimes, 6, Font.NORMAL);

        cgmsGraphDataHandler = new CGMSGraphDataHandler();

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
        p.add(new Paragraph(this.i18nControl.getMessage(getTitleText()) + "   ["
                + DataAccess.getGregorianCalendarAsDateString(this.gcFrom) + " - "
                + DataAccess.getGregorianCalendarAsDateString(this.gcTill) + "]", f));
        p.add(new Paragraph(
                this.i18nControl.getMessage("FOR") + " "
                        + DataAccess.getInstance().getConfigurationManagerWrapper().getUserName(),
                new Font(FontFamily.TIMES_ROMAN, 10, Font.ITALIC)));
        p.add(new Paragraph("", f));
        p.add(new Paragraph("", f));

        return p;
    }


    @Override
    public void initData()
    {
        if (this.printParameters.containsKey("RANGE_FROM"))
        {
            gcFrom = this.printParameters.getRangeFrom();
            gcTill = this.printParameters.getRangeTo();
        }
        else
        {
            // gcFrom = new GregorianCalendar(2015, 3, 1); // CGMS
            // gcTill = new GregorianCalendar(2015, 3, 12);
            gcFrom = new GregorianCalendar(2012, 11, 17); // TBR, Bolus Ext,
                                                          // Multiwave
            gcTill = new GregorianCalendar(2012, 11, 28);
        }

        GGC_CGMSDb db = DataAccessCGMS.getInstance().getDb();

        cgmsReadingsDataMap = cgmsGraphDataHandler.getCGMSReadingsRange(dataAccessCGMS, gcFrom, gcTill);

        if (MapUtils.isNotEmpty(cgmsReadingsDataMap))
        {
            hasCGMSData = true;
        }

        // FIXME configuration of colors for reports

        // colors
        this.fontColors.put("BG_COLOR", createFont("DarkRed"));
        this.fontColors.put("BASAL_COLOR", createFont("MidnightBlue"));
        this.fontColors.put("BOLUS_COLOR", createFont("DodgerBlue"));
        this.fontColors.put("CH_COLOR", createFont("DarkGreen"));
        this.fontColors.put("PUMP_ADD_DATA_COLOR", createFont("MediumSeaGreen"));
    }


    public Font createFont(String colorName)
    {
        return new Font(FontFamily.HELVETICA, 6, Font.BOLD, WebColors.getRGBColor(colorName));
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
            ATechDate atd = new ATechDate(dataAccessCGMS.getDataEntryObject().getDateTimeFormat(), gc_current);

            if (deviceValuesRange.isDayEntryAvailable(atd.getATDateTimeAsLong()))
            {
                DeviceValuesDay dvd = deviceValuesRange.getDayEntry(atd.getATDateTimeAsLong());
                dvd.prepareHourlyEntries();
                createDayEntry(mainTable, dvd, gc_current);
            }
            else
            {
                createDayEntry(mainTable, null, gc_current);
            }

            gc_current.add(Calendar.DAY_OF_MONTH, 1);

        } while (gc_current.before(gc_end));

        document.add(mainTable);
    }


    private void createDayEntry(PdfPTable mainTable, DeviceValuesDay deviceValuesDay, GregorianCalendar gcCurrent)
            throws Exception
    {

        mainTable.addCell(createEmptyLine());

        if (hasCGMSData)
        {
            displayCGMSData(mainTable, gcCurrent);
        }

    }


    private PdfPCell createEmptyLine()
    {
        PdfPCell cell = new PdfPCell();
        cell.setFixedHeight(12);
        cell.setBorder(0);
        cell.setPhrase(this.createEmptyTextPhrase());
        return cell;
    }


    private void displayCGMSData(PdfPTable mainTable, GregorianCalendar currentCalendar)
    {
        try
        {
            long date = ATechDate.getATDateTimeFromGC(currentCalendar, ATechDateType.DateOnly);

            JFreeChart chart = null;

            if (cgmsReadingsDataMap.containsKey(date))
            {
                chart = cgmsGraphDataHandler.createDailyChartForReport(dataAccessCGMS, cgmsReadingsDataMap.get(date));
            }

            if (chart != null)
            {
                BufferedImage bufferedImage = chart.createBufferedImage(1000, 160);
                Image image = Image.getInstance(pdfWriter, bufferedImage, 1.0f);
                mainTable.addCell(image);

                mainTable.addCell(createEmptyLine());
                mainTable.addCell(createEmptyLine());
            }
        }
        catch (Exception ex)
        {
            LOG.error("Error creating graph instance. {}", ex.toString(), ex);
        }

    }


    private PdfPCell createGreyCenteredCellWithNormalText(String text)
    {
        return this.createCellWithCenterAlignment(this.createNormalTextPhrase(text), BaseColor.LIGHT_GRAY);
    }


    private PdfPCell createCenteredCellWithNormalText(String text)
    {
        return this.createCellWithCenterAlignment(this.createNormalTextPhrase(text), null);
    }


    private Phrase createEmptyTextPhraseSmall()
    {
        return new Phrase("", smallFont);
    }


    private PdfPCell createCellWithEmptySmallText()
    {
        return this.createCellWithCenterAlignment(new Phrase("", smallFont), null);
    }


    private Phrase createNormalTextPhraseSmall(String text)
    {
        return new Phrase(this.i18nControl.getMessage(text), smallFont);
    }


    private PdfPCell createCellWithNormalSmallText(String text, String fontName)
    {
        return this.createCellWithCenterAlignment(new Phrase(this.i18nControl.getMessage(text), getFont(fontName)),
            null);
    }


    private Phrase createNormalTextPhraseSmall(String text, String fontName)
    {
        return new Phrase(this.i18nControl.getMessage(text), getFont(fontName));
    }


    private Font getFont(String fontName)
    {
        if (this.fontColors.containsKey(fontName))
        {
            // System.out.println("fontName: " + fontName);
            return this.fontColors.get(fontName);
        }
        else
            return this.smallFont;
    }


    public String getTitleText()
    {
        return "PUMP_DATA_DAILY_TIMESHEET_1";
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getFileNameBase()
    {
        return "CGMS_DailyGraph";
    }


    @Override
    public ITextDocumentPrintSettings getCustomDocumentSettings()
    {
        return new ITextDocumentPrintSettings(20, 20, 10, 30);
    }


    @Override
    public String getFileNameRange()
    {
        ATechDate atd1 = new ATechDate(dataAccessCGMS.getDataEntryObject().getDateTimeFormat(),
                deviceValuesRange.getStartGC());
        ATechDate atd2 = new ATechDate(dataAccessCGMS.getDataEntryObject().getDateTimeFormat(),
                deviceValuesRange.getEndGC());

        return atd1.getDateFilenameString() + "-" + atd2.getDateFilenameString();
    }


    @Override
    public int getTextSize()
    {
        return 6;
    }


    private static CGMSPluginDefinition getPluginDefinition(ATDataAccessLMAbstract da)
    {
        return new CGMSPluginDefinition(da.getLanguageManager());
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

        DataAccessCGMS da = DataAccessCGMS.createInstance(getPluginDefinition(daCore));

        da.createPlugInDataRetrievalContext();

        da.createDb(daCore.getHibernateDb());

        ReportCGMSDailyGraph pa = new ReportCGMSDailyGraph(new PrintParameters());
        // pa.init();

        System.out.println("Path: " + pa.getRelativeNameWithPath());

        // displayPDF(pa.getRelativeNameWithPath());

    }

}
