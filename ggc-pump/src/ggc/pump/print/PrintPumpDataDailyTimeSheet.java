package ggc.pump.print;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

import org.apache.commons.collections.MapUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.i18n.I18nControlAbstract;
import com.atech.print.engine.ITextDocumentPrintSettings;
import com.atech.print.engine.PrintAbstractITextWithDataRead;
import com.atech.print.engine.PrintParameters;
import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;
import com.itextpdf.text.*;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import ggc.core.db.GGCDbLoader;
import ggc.core.db.tool.DbToolApplicationGGC;
import ggc.core.util.DataAccess;
import ggc.plugin.data.DeviceValuesDay;
import ggc.plugin.data.DeviceValuesEntry;
import ggc.plugin.data.DeviceValuesRange;
import ggc.plugin.graph.data.CGMSGraphDataHandler;
import ggc.plugin.report.data.cgms.CGMSDayData;
import ggc.plugin.util.DataAccessPlugInBase;
import ggc.pump.data.PumpValuesHour;
import ggc.pump.data.PumpValuesHourProcessor;
import ggc.pump.data.defs.PumpDeviceValueType;
import ggc.pump.data.dto.BasalRatesDayDTO;
import ggc.pump.data.util.PumpBasalManager;
import ggc.pump.db.GGCPumpDb;
import ggc.pump.db.PumpProfile;
import ggc.pump.util.DataAccessPump;

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

public class PrintPumpDataDailyTimeSheet extends PrintAbstractITextWithDataRead
{

    private static final Logger LOG = LoggerFactory.getLogger(PrintPumpDataDailyTimeSheet.class);

    Font smallFont = null;
    protected DeviceValuesRange deviceValuesRange;

    private GregorianCalendar gcFrom;
    private GregorianCalendar gcTill;
    DataAccessPump dataAccessPump = DataAccessPump.getInstance();
    I18nControlAbstract i18nControl = dataAccessPump.getI18nControlInstance();
    PumpValuesHourProcessor pumpValuesHourProcessor;
    List<PumpProfile> profilesRange;
    HashMap<String, Font> fontColors = new HashMap<String, Font>();
    PumpBasalManager pumpBasalManager;
    PrintPumpDataDailyTimeSheetType sheetType;

    CGMSGraphDataHandler cgmsGraphDataHandler = new CGMSGraphDataHandler();
    Map<Long, CGMSDayData> cgmsReadingsDataMap;
    boolean hasCGMSData = false;


    public PrintPumpDataDailyTimeSheet(PrintParameters parameters, PrintPumpDataDailyTimeSheetType sheetType)
    {
        super(DataAccessPump.getInstance(), parameters, false);

        smallFont = new Font(this.baseFontTimes, 6, Font.NORMAL);
        pumpValuesHourProcessor = new PumpValuesHourProcessor();
        pumpBasalManager = new PumpBasalManager(dataAccessPump);
        this.sheetType = sheetType;

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
        p.add(new Paragraph(this.i18nControl.getMessage(getTitleText()) + " [" + this.getDateString(this.gcFrom) + " - "
                + this.getDateString(this.gcTill) + "]", f));
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
        // FIXME read from parameters

        if (this.printParameters.containsKey("RANGE_FROM"))
        {
            gcFrom = (GregorianCalendar) this.printParameters.get("RANGE_FROM");
            gcTill = (GregorianCalendar) this.printParameters.get("RANGE_TO");
        }
        else
        {
            gcFrom = new GregorianCalendar(2015, 3, 1);
            gcTill = new GregorianCalendar(2015, 3, 12);
        }

        GGCPumpDb db = DataAccessPump.getInstance().getDb();

        this.deviceValuesRange = db.getRangePumpValues(gcFrom, gcTill);
        this.profilesRange = db.getProfilesForRange(gcFrom, gcTill);

        if (this.profilesRange.size() > 0)
        {
            this.lastActiveProfile = this.profilesRange.get(0);
        }

        System.out.println("Profiles all: " + this.profilesRange.size());

        // FIXME load Basals and remove profiles

        if ((this.sheetType == PrintPumpDataDailyTimeSheetType.BaseWithCGMS) || //
                (this.sheetType == PrintPumpDataDailyTimeSheetType.BaseWithFoodAndCGMS))
        {
            cgmsReadingsDataMap = cgmsGraphDataHandler.getCGMSReadingsRange(dataAccessPump, gcFrom, gcTill);

            if (MapUtils.isNotEmpty(cgmsReadingsDataMap))
            {
                hasCGMSData = true;
            }

        }

        // FIXME configuration of colors for reports

        // colors
        this.fontColors.put("BG_COLOR", createFont("DarkRed")); // createFont(63));
                                                                // // DarkRed
        this.fontColors.put("BASAL_COLOR", createFont("MidnightBlue")); // createFont(37));
                                                                        // //
                                                                        // MidnightBlue
        this.fontColors.put("BOLUS_COLOR", createFont("DodgerBlue")); // createFont(128));
                                                                      // //
                                                                      // IndianRed
        this.fontColors.put("CH_COLOR", createFont("DarkGreen")); // createFont(186));
                                                                  // //
                                                                  // YellowGreen
        this.fontColors.put("PUMP_ADD_DATA_COLOR", createFont("MediumSeaGreen")); // createFont(213));
        // //
        // PowderBlue
    }


    public Font createFont(String colorName)
    {
        return new Font(FontFamily.HELVETICA, 6, Font.BOLD, WebColors.getRGBColor(colorName));
        // Font f = new Font(this.baseFontTimes, 6, Font.NORMAL);
        // f.setColor(new BaseColor(colorNumber));

        // return f;
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
            else
            {
                createDayEntry(mainTable, null, gc_current);
            }

            gc_current.add(Calendar.DAY_OF_MONTH, 1);

        } while (gc_current.before(gc_end));

        document.add(mainTable);
    }


    private List<PumpProfile> getCorrectProfilesForDay(GregorianCalendar currentGc)
    {
        GregorianCalendar gc = (GregorianCalendar) currentGc.clone();
        gc.set(Calendar.HOUR_OF_DAY, 23);
        gc.set(Calendar.MINUTE, 59);
        gc.set(Calendar.SECOND, 59);

        GregorianCalendar gc2 = (GregorianCalendar) currentGc.clone();
        gc2.set(Calendar.HOUR_OF_DAY, 0);
        gc2.set(Calendar.MINUTE, 0);
        gc2.set(Calendar.SECOND, 0);

        // long ss = ATechDate.getATDateTimeFromGC(gc,
        // ATechDate.FORMAT_DATE_AND_TIME_S);

        System.out.println("Profiles for day: " + ATechDate.getDateString(ATechDate.FORMAT_DATE_AND_TIME_S, currentGc));

        List<PumpProfile> acProfiles = new ArrayList<PumpProfile>();

        long dt1 = ATechDate.getATDateTimeFromGC(gc, ATechDate.FORMAT_DATE_AND_TIME_S);
        long dt2 = ATechDate.getATDateTimeFromGC(gc2, ATechDate.FORMAT_DATE_AND_TIME_S);

        for (PumpProfile pp : this.profilesRange)
        {
            if (((pp.getActive_from() < dt1 && pp.getActive_from() != 0))
                    && (pp.getActive_till() == 0 || pp.getActive_till() > dt2))
            {
                acProfiles.add(pp);
                System.out.println("Profile: " + pp.getActive_from() + " - " + pp.getActive_till());
            }
        }

        return acProfiles;
    }

    /**
     * TODO
     *
     * + main view table (day, comment, space)    100%
     * - multiple BG in comment                   100%
     * - comments                                 100%
     * - bolus swuare or extended                 100%
     * + sums                                     100%
     * - days resolve                              95%
     * - basal                                     95%  some bugs to fix...
     *
     *
     * NOT IMPORTANT FOR NOW:
     * - Extended and Multiwave in table and comm.        0%
     *
     * PROFILES REPORT                             100%
     *
     *   I. (v2)
     * - special pump statues (stopped pump, change
     *   of pump material, TBR)                           0%
     * - Color display                                   40%
     * - Comments types                                  30%
     * - Extended and Multiwave in comm.                  0%
     *
     *
     *   II. (Base + CGMS)
     *
     *
     */

    private PumpProfile lastActiveProfile;


    private void createDayEntry(PdfPTable mainTable, DeviceValuesDay deviceValuesDay, GregorianCalendar gcCurrent)
            throws Exception
    {
        PdfPTable datatable = new PdfPTable(27);
        datatable.setWidths(
            new float[] { 4.0f, 6.0f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f,
                          3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 6.0f }); // 6
                                                                                                     // +
                                                                                                     // 2
                                                                                                     // +
                                                                                                     // 4
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
                datatable.addCell(this.createNormalTextPhrase(dataAccessPump.getDayOfWeekFromGCShorter(gcCurrent, 2)));
                datatable.addCell(this.createBoldTextPhrase("BASAL"));

                writeHourlyValues(PumpDeviceValueType.BASAL, datatable, deviceValuesDay, gcCurrent);
            }
            else if (j == 1)
            {
                PdfPCell cell = new PdfPCell();
                cell.setPhrase(this.createNormalTextPhrase(gcCurrent.get(Calendar.DAY_OF_MONTH) + "."));
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.TOP);
                cell.setBorderWidth(1);
                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);

                datatable.addCell(cell);
                datatable.addCell(this.createBoldTextPhrase("BOLUS"));

                writeHourlyValues(PumpDeviceValueType.BOLUS, datatable, deviceValuesDay, gcCurrent);
            }
            else if (j == 2)
            {
                PdfPCell cell = new PdfPCell();
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                cell.setBorderWidth(1);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPhrase(this.createNormalTextPhrase(
                    dataAccessPump.getMonthsArray()[gcCurrent.get(Calendar.MONTH)].substring(0, 3)));

                datatable.addCell(cell);
                datatable.addCell(this.createBoldTextPhrase("BG"));

                writeHourlyValues(PumpDeviceValueType.BG, datatable, deviceValuesDay, gcCurrent);
            }
            else
            // if (j==3)
            {
                PdfPCell cell = new PdfPCell();
                cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
                cell.setBorderWidth(1);
                cell.setVerticalAlignment(Element.ALIGN_TOP);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPhrase(this.createNormalTextPhrase("" + gcCurrent.get(Calendar.YEAR)));

                datatable.addCell(cell);
                datatable.addCell(this.createBoldTextPhrase("CH"));

                writeHourlyValues(PumpDeviceValueType.CH, datatable, deviceValuesDay, gcCurrent);
            }

        }

        mainTable.addCell(datatable);

        // comment table
        PdfPTable tableComment = new PdfPTable(2);
        tableComment.setWidths(new float[] { 8.0f, 92.0f });
        tableComment.setWidthPercentage(100);
        tableComment.getDefaultCell().setBorderWidth(0);

        if (this.pumpValuesHourProcessor.isAdditionalDataForPumpTypeSet(PumpDeviceValueType.BG))
        {
            tableComment.addCell(this.createBoldTextPhrase(this.i18nControl.getMessage("BG") + ":"));
            tableComment.addCell(this.createNormalTextPhraseSmall(
                this.pumpValuesHourProcessor.getAdditionalDataForPumpTypeSet(PumpDeviceValueType.BG),
                PumpDeviceValueType.BG.getColorKey()));
        }

        if (this.pumpValuesHourProcessor.isAdditionalDataForPumpTypeSet(PumpDeviceValueType.PUMP_ADDITIONAL_DATA))
        {
            tableComment.addCell(this.createBoldTextPhrase(this.i18nControl.getMessage("PUMP") + ":"));
            tableComment.addCell(this.createNormalTextPhraseSmall(
                this.pumpValuesHourProcessor.getAdditionalDataForPumpTypeSet(PumpDeviceValueType.PUMP_ADDITIONAL_DATA),
                PumpDeviceValueType.PUMP_ADDITIONAL_DATA.getColorKey()));
        }

        displayFoodData(tableComment, gcCurrent);

        tableComment.addCell(this.createBoldTextPhrase(this.i18nControl.getMessage("COMMENT") + ":"));
        tableComment.addCell(this.createNormalTextPhrase(
            this.pumpValuesHourProcessor.getAdditionalDataForPumpTypeSet(PumpDeviceValueType.COMMENT)));

        mainTable.addCell(tableComment);

        if (!hasCGMSData)
        {
            mainTable.addCell(createEmptyLine());
        }
        else
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


    private void displayFoodData(PdfPTable tableComment, GregorianCalendar gcCurrent)
    {
        if ((this.sheetType != PrintPumpDataDailyTimeSheetType.BaseWithFood) && //
                (this.sheetType != PrintPumpDataDailyTimeSheetType.BaseWithFoodAndCGMS))
        {
            return;
        }

        // FIXME
        // add adding of food to report
        // tableComment.addCell(this.createBoldTextPhrase(this.i18nControl.getMessage("PUMP")
        // + ":"));
        // tableComment.addCell(this.createNormalTextPhraseSmall(
        // this.pumpValuesHourProcessor.getAdditionalDataForPumpTypeSet(PumpDeviceValueType.PUMP_ADDITIONAL_DATA)
        // ));

    }


    private void displayCGMSData(PdfPTable mainTable, GregorianCalendar currentCalendar)
    {

        try
        {
            long date = ATechDate.getATDateTimeFromGC(currentCalendar, ATechDateType.DateOnly);

            // System.out.println("Querty for CGMS : " + date);

            JFreeChart chart = null;

            if (cgmsReadingsDataMap.containsKey(date))
            {
                //System.out.println("CGMS Data Found in Map");
                chart = cgmsGraphDataHandler.createDailyChartForReport(dataAccessPump, cgmsReadingsDataMap.get(date));
            }

            // FIXME
            // add adding of CGMS to report

            if (chart != null)
            {

                // add image
                int width = 1000;
                int height = 180;
                // chart = getChart(currentCalendar);
                BufferedImage bufferedImage = chart.createBufferedImage(1000, 160);
                Image image = Image.getInstance(pdfWriter, bufferedImage, 1.0f);
                mainTable.addCell(image);

                mainTable.addCell(createEmptyLine());
                mainTable.addCell(createEmptyLine());
            }
            else
            {
                System.out.println("Chart was null. Shouldn't happen.");

            }

        }
        catch (Exception ex)
        {
            LOG.error("Error creating graph instance. {}", ex.toString(), ex);
        }

    }


    public JFreeChart getChart(GregorianCalendar currentCalendar)
    {

        XYSeries series = cgmsGraphDataHandler.getCGMSDailyReadings(dataAccessPump, currentCalendar);

        XYSeriesCollection coll = new XYSeriesCollection();
        coll.addSeries(series);

        // use the ChartFactory to create a pie chart
        JFreeChart chart = ChartFactory.createScatterPlot(null, "X", "Y", coll, PlotOrientation.VERTICAL, false, false,
            false);

        // .createLineChart("Dummy Data", "X", "Y", coll,
        // PlotOrientation.HORIZONTAL, true, true, false);
        // String title, String categoryAxisLabel, String valueAxisLabel,
        // CategoryDataset dataset, PlotOrientation orientation, boolean legend,
        // boolean tooltips, boolean urls

        return chart;
    }


    private void writeHourlyValues(PumpDeviceValueType type, PdfPTable table, DeviceValuesDay deviceValuesDay,
            GregorianCalendar gcCurrent)
    {

        float sum = 0.0f;
        float count = 0;

        // List<PumpProfile> profiles = null;

        BasalRatesDayDTO basalRatesDTO = null;

        if (deviceValuesDay != null)
        {
            deviceValuesDay.prepareHourlyEntries();
        }

        if (type == PumpDeviceValueType.BASAL)
        {
            // FIXME
            // basalRatesDTO =
            // pumpBasalManager.getBasalRatesForDay(deviceValuesDay);
            // basalRatesDTO.addProfilesForDay(this.getCorrectProfilesForDay(gcCurrent));
        }

        for (int hour = 0; hour < 24; hour++)
        {

            // if (type == PumpDeviceValueType.BASAL)
            // {
            // // FIXME profile name
            // PumpProfile profile =
            // pumpValuesHourProcessor.getProfileForHour(profiles, gcCurrent,
            // hour, "5");
            //
            // if (profile == null)
            // {
            // profile = this.lastActiveProfile;
            // }
            // else
            // {
            // this.lastActiveProfile = profile;
            // }
            //
            // float value = -1.0f;
            //
            // if (profile != null)
            // {
            // ProfileSubPattern patternForHour =
            // profile.getPatternForHour(hour);
            //
            // if (patternForHour != null)
            // {
            // value = patternForHour.getAmount();
            // }
            // }
            //
            // if (value < 0.0f)
            // {
            // table.addCell(this.createNormalTextPhraseSmall(""));
            // }
            // else
            // {
            // table.addCell(this.createNormalTextPhraseSmall(dataAccessPump.getFormatedBasalValue(value)));
            // }
            // }

            if (deviceValuesDay == null)
            {
                if (type == PumpDeviceValueType.BASAL)
                {

                    // // FIXME profile name
                    float value = writeBasalProfileValue(gcCurrent, hour, basalRatesDTO, "5", table);

                    if (value > 0.0f)
                    {
                        sum += value;
                    }

                    // // FIXME profile name
                    // PumpProfile profile = getPumpProfile(gcCurrent, profiles,
                    // hour, "5");
                    //
                    // float value = pumpBasalManager.getBasalValueForHour(hour,
                    // profile, basalRatesDTO);
                    //
                    // if (value < 0.0f)
                    // {
                    // table.addCell(this.createNormalTextPhraseSmall(""));
                    // }
                    // else
                    // {
                    // table.addCell(this.createNormalTextPhraseSmall(dataAccessPump.getFormatedBasalValue(value),
                    // type.getColorKey()));
                    // sum += value;
                    // }
                }
                else
                {

                    table.addCell(this.createEmptyTextPhraseSmall());

                }
            }
            else
            {
                List<DeviceValuesEntry> entries = deviceValuesDay.getEntriesForHour(hour);

                // System.out.println("Hour: " + i + "DateTime: "
                // + deviceValuesDay.getDateTimeGC().get(Calendar.DAY_OF_MONTH)
                // + ", entries=" + entries.size());

                if (type == PumpDeviceValueType.BASAL)
                {

                    // // FIXME profile name
                    float value = writeBasalProfileValue(gcCurrent, hour, basalRatesDTO, "5", table);

                    if (value > 0.0f)
                    {
                        sum += value;
                    }

                    // // FIXME profile name
                    // PumpProfile profile =
                    // getPumpProfile(deviceValuesDay.getDateTimeGC(), profiles,
                    // hour, "5");
                    //
                    // float value = pumpBasalManager.getBasalValueForHour(hour,
                    // profile, basalRatesDTO);
                    //
                    // if (value < 0.0f)
                    // {
                    // table.addCell(this.createNormalTextPhraseSmall(""));
                    // }
                    // else
                    // {
                    // table.addCell(this.createNormalTextPhraseSmall(dataAccessPump.getFormatedBasalValue(value),
                    // type.getColorKey()));
                    // }
                }
                else
                {
                    PumpValuesHour pumpValuesHour = pumpValuesHourProcessor.createPumpValuesHour(entries);

                    if (type == PumpDeviceValueType.BOLUS)
                    {
                        if (pumpValuesHour.getBolus() > 0.0f)
                        {
                            sum += pumpValuesHour.getBolus();
                            table.addCell(this.createNormalTextPhraseSmall(
                                dataAccessPump.getFormatedBolusValue(pumpValuesHour.getBolus()), type.getColorKey()));

                            if (pumpValuesHour.hasBolusSpecial())
                            {
                                pumpValuesHourProcessor.addAdditionalData(PumpDeviceValueType.PUMP_ADDITIONAL_DATA,
                                    pumpValuesHour.getBolusSpecial());

                                sum += pumpValuesHour.getBolusSpecialForSum();
                            }
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
                            table.addCell(this.createNormalTextPhraseSmall("*", type.getColorKey()));

                            pumpValuesHourProcessor.addAdditionalData(PumpDeviceValueType.BG,
                                pumpValuesHour.getMultipleBgs());
                        }
                        else if (pumpValuesHour.getBgs().size() == 1)
                        {
                            sum += pumpValuesHour.getBgProcessedValue();
                            count++;

                            table.addCell(this.createNormalTextPhraseSmall(
                                dataAccessPump.getDisplayedBGString(pumpValuesHour.getBgProcessedValue()),
                                type.getColorKey()));
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
                            table.addCell(this.createNormalTextPhraseSmall(
                                DataAccessPlugInBase.Decimal0Format.format(pumpValuesHour.getCH()),
                                type.getColorKey()));
                        }
                        else
                        {
                            table.addCell(this.createEmptyTextPhraseSmall());
                        }
                    }
                }
            }
        }

        if (sum > 0.0f)
        {
            if (type == PumpDeviceValueType.BG)
            {
                float d = sum / (count * (1.0f));

                table.addCell(
                    this.createNormalTextPhraseSmall(dataAccessPump.getDisplayedBGString(d), type.getColorKey()));
            }
            else if (type == PumpDeviceValueType.BASAL)
            {
                table.addCell(
                    this.createNormalTextPhraseSmall(dataAccessPump.getFormatedBasalValue(sum), type.getColorKey()));
            }
            else if (type == PumpDeviceValueType.BOLUS)
            {
                table.addCell(
                    this.createNormalTextPhraseSmall(dataAccessPump.getFormatedBolusValue(sum), type.getColorKey()));
            }
            else
            {
                table.addCell(this.createNormalTextPhraseSmall(DataAccessPlugInBase.Decimal0Format.format(sum),
                    type.getColorKey()));
            }

        }
        else
        {
            table.addCell(this.createEmptyTextPhraseSmall());
        }

    }


    private float writeBasalProfileValue(GregorianCalendar gcCurrent, int hour, BasalRatesDayDTO basalRatesDTO,
            String activeProfileName, PdfPTable table)
    {

        // FIXME

        if (true)
        {
            table.addCell(this.createNormalTextPhraseSmall(""));
            return 0.0f;
        }

        // PumpProfile profile = getPumpProfile(gcCurrent,
        // basalRatesDTO.getProfiles(), hour, "5");

        PumpProfile profile = pumpValuesHourProcessor.getProfileForHour(basalRatesDTO.getProfiles(), gcCurrent, hour,
            activeProfileName);

        if (profile == null)
        {
            profile = this.lastActiveProfile;
        }
        else
        {
            this.lastActiveProfile = profile;
        }

        float value = 0.0f;
        // FIXME
        // pumpBasalManager.getBasalValueForHour(hour, profile, basalRatesDTO);

        if (value < 0.0f)
        {
            table.addCell(this.createNormalTextPhraseSmall(""));
        }
        else
        {
            table.addCell(this.createNormalTextPhraseSmall(dataAccessPump.getFormatedBasalValue(value),
                PumpDeviceValueType.BASAL.getColorKey()));
        }

        return value;

    }


    private Phrase createEmptyTextPhraseSmall()
    {
        return new Phrase("", smallFont);
    }


    private Phrase createNormalTextPhraseSmall(String text)
    {
        return new Phrase(this.i18nControl.getMessage(text), smallFont);
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
        ATechDate atd1 = new ATechDate(dataAccessPump.getDataEntryObject().getDateTimeFormat(),
                deviceValuesRange.getStartGC());
        ATechDate atd2 = new ATechDate(dataAccessPump.getDataEntryObject().getDateTimeFormat(),
                deviceValuesRange.getEndGC());

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

        PrintPumpDataDailyTimeSheet pa = new PrintPumpDataDailyTimeSheet(new PrintParameters(),
                PrintPumpDataDailyTimeSheetType.BaseWithCGMS);
        // pa.init();

        System.out.println("Path: " + pa.getRelativeNameWithPath());

        // displayPDF(pa.getRelativeNameWithPath());

    }

    public enum PrintPumpDataDailyTimeSheetType
    {
        BaseSheet, // Normal sheet (v2)
        BaseWithCGMS, // Normal + CGMS II
        BaseWithFood, // Normal + Food III
        BaseWithFoodAndCGMS // Normal + Food + CGMS IV
    }

}
