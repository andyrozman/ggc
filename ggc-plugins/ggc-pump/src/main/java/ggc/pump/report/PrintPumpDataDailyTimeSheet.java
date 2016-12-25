package main.java.ggc.pump.report;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.jfree.chart.JFreeChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.data.SimpleTimeValueDataDto;
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

import ggc.core.db.GGCDbLoader;
import ggc.core.db.tool.DbToolApplicationGGC;
import ggc.core.util.DataAccess;
import ggc.plugin.data.DeviceValuesDay;
import ggc.plugin.data.DeviceValuesEntry;
import ggc.plugin.data.DeviceValuesRange;
import ggc.plugin.graph.data.CGMSGraphDataHandler;
import ggc.plugin.report.data.cgms.CGMSDayData;
import ggc.plugin.util.DataAccessPlugInBase;
import main.java.ggc.pump.data.PumpValuesEntry;
import main.java.ggc.pump.data.PumpValuesHour;
import main.java.ggc.pump.data.PumpValuesHourProcessor;
import main.java.ggc.pump.data.defs.PumpBaseType;
import main.java.ggc.pump.data.defs.PumpDeviceValueType;
import main.java.ggc.pump.data.dto.BasalRatesDayDTO;
import main.java.ggc.pump.data.util.PumpAdditionalDataHandler;
import main.java.ggc.pump.data.util.PumpBasalManager;
import main.java.ggc.pump.db.GGCPumpDb;
import main.java.ggc.pump.defs.PumpPluginDefinition;
import main.java.ggc.pump.util.DataAccessPump;

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

    DataAccessPump dataAccessPump = DataAccessPump.getInstance();
    I18nControlAbstract i18nControl = dataAccessPump.getI18nControlInstance();

    PumpValuesHourProcessor pumpValuesHourProcessor;
    CGMSGraphDataHandler cgmsGraphDataHandler;
    PumpBasalManager pumpBasalManager;
    PumpAdditionalDataHandler pumpAdditionalDataHandler;
    PrintPumpDataDailyTimeSheetType sheetType;

    Font smallFont = null;
    private GregorianCalendar gcFrom;
    private GregorianCalendar gcTill;
    HashMap<String, Font> fontColors = new HashMap<String, Font>();

    boolean hasCGMSData = false;
    boolean shouldDisplayFood = false;

    protected DeviceValuesRange deviceValuesRange;
    Map<Long, CGMSDayData> cgmsReadingsDataMap;
    Map<String, BasalRatesDayDTO> basalRatesRange;


    // List<PumpProfile> profilesRange;

    public PrintPumpDataDailyTimeSheet(PrintParameters parameters, PrintPumpDataDailyTimeSheetType sheetType)
    {
        super(DataAccessPump.getInstance(), parameters, false);

        smallFont = new Font(this.baseFontTimes, 6, Font.NORMAL);
        pumpValuesHourProcessor = new PumpValuesHourProcessor();
        pumpBasalManager = new PumpBasalManager(dataAccessPump);
        cgmsGraphDataHandler = new CGMSGraphDataHandler();
        pumpAdditionalDataHandler = new PumpAdditionalDataHandler();
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

        GGCPumpDb db = DataAccessPump.getInstance().getDb();

        this.deviceValuesRange = db.getRangePumpValues(gcFrom, gcTill);
        basalRatesRange = pumpBasalManager.getBasalRatesForRange(gcFrom, gcTill);

        if ((this.sheetType == PrintPumpDataDailyTimeSheetType.BaseWithFood) || //
                (this.sheetType == PrintPumpDataDailyTimeSheetType.BaseWithFoodAndCGMS))
        {
            shouldDisplayFood = true;
        }

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
     *   TODO
     *
     *   III. (Base + Food)
     *   - Food data                                      0%
     *
     *   IV. (Base + CGMS + Food)
     *   - Base                                         100%
     *   - CGMS                                         100%
     *   - Food                                           0%
     *
     */

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


    private void createDayEntry(PdfPTable mainTable, DeviceValuesDay deviceValuesDay, GregorianCalendar gcCurrent)
            throws Exception
    {
        PdfPTable datatable = new PdfPTable(27);
        datatable.setWidths(
            new float[] { 4.0f, 6.0f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f,
                          3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 6.0f });

        datatable.setWidthPercentage(100); // percentage

        datatable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER); // ALIGN_CENTER);
        datatable.getDefaultCell().setBorderWidth(1);

        // cell 1
        datatable.addCell(createGreyCenteredCellWithNormalText("DAY"));
        datatable.addCell(createGreyCenteredCellWithNormalText("TIME"));

        for (int i = 0; i < 24; i++)
        {
            datatable.addCell(createGreyCenteredCellWithNormalText("" + i));
        }

        datatable.addCell(createGreyCenteredCellWithNormalText("SUM"));

        pumpValuesHourProcessor.clearComments();

        for (int j = 0; j < 4; j++)
        {
            if (j == 0)
            {
                datatable.addCell(
                    this.createCenteredCellWithNormalText(dataAccessPump.getDayOfWeekFromGCShorter(gcCurrent, 2)));
                datatable.addCell(this.createGreyCenteredCellWithNormalText("BASAL"));

                writeHourlyValues(PumpDeviceValueType.BASAL, datatable, deviceValuesDay, gcCurrent);
            }
            else if (j == 1)
            {
                PdfPCell cell = new PdfPCell();
                cell.setPhrase(this.createNormalTextPhrase(gcCurrent.get(Calendar.DAY_OF_MONTH) + "."));
                // cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT |
                // Rectangle.TOP);
                // cell.setBorderWidth(0);
                // cell.setBorder(Rectangle.BOTTOM);
                // cell.setBorder(Rectangle.LEFT);
                // cell.setBorderColorLeft(BaseColor.BLACK);
                //
                // cell.setBorderColorBottom(BaseColor.WHITE);

                cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);

                datatable.addCell(cell);
                datatable.addCell(this.createGreyCenteredCellWithNormalText("BOLUS"));

                writeHourlyValues(PumpDeviceValueType.BOLUS, datatable, deviceValuesDay, gcCurrent);
            }
            else if (j == 2)
            {
                PdfPCell cell = new PdfPCell();
                // cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                // cell.setBorder(Rectangle.BOTTOM | Rectangle.TOP);
                // cell.setBorderWidth(0);
                // cell.setBorder(Rectangle.LEFT);
                // cell.setBorderColorLeft(BaseColor.BLACK);
                // cell.setBorderWidthBottom(1);
                //
                cell.setBorderColorBottom(BaseColor.WHITE);
                //
                cell.setBorderColorTop(BaseColor.WHITE);
                // cell.setBorderWidthTop(1);

                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPhrase(this.createNormalTextPhrase(
                    dataAccessPump.getMonthsArray()[gcCurrent.get(Calendar.MONTH)].substring(0, 3)));

                datatable.addCell(cell);
                datatable.addCell(this.createGreyCenteredCellWithNormalText("BG"));

                writeHourlyValues(PumpDeviceValueType.BG, datatable, deviceValuesDay, gcCurrent);
            }
            else
            // if (j==3)
            {
                PdfPCell cell = new PdfPCell();
                // cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT |
                // Rectangle.BOTTOM);
                // cell.setBorderWidth(1);

                // cell.setBorderWidthLeft(1);
                // cell.setBorderWidthRight(1);
                //
                cell.setBorderColorTop(BaseColor.WHITE);
                // cell.setBorderWidthTop(1);

                cell.setVerticalAlignment(Element.ALIGN_TOP);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPhrase(this.createNormalTextPhrase("" + gcCurrent.get(Calendar.YEAR)));

                datatable.addCell(cell);
                datatable.addCell(this.createGreyCenteredCellWithNormalText("CH"));

                writeHourlyValues(PumpDeviceValueType.CH, datatable, deviceValuesDay, gcCurrent);
            }

        }

        mainTable.addCell(datatable);

        // comment table
        PdfPTable tableComment = new PdfPTable(2);
        tableComment.setWidths(new float[] { 8.0f, 92.0f });
        tableComment.setWidthPercentage(100);
        tableComment.getDefaultCell().setBorderWidth(0);

        boolean commentFound = false;

        if (this.pumpValuesHourProcessor.isAdditionalDataForPumpTypeSet(PumpDeviceValueType.BG))
        {
            tableComment.addCell(this.createNormalTextPhrase(this.i18nControl.getMessage("BG") + ":"));
            tableComment.addCell(this.createNormalTextPhraseSmall(
                this.pumpValuesHourProcessor.getAdditionalDataForPumpTypeSet(PumpDeviceValueType.BG),
                PumpDeviceValueType.BG.getColorKey()));

            commentFound = true;
        }

        String pumpAdditionalData = getPumpAdditionalData(deviceValuesDay);

        if (StringUtils.isNotBlank(pumpAdditionalData))
        {
            tableComment.addCell(this.createNormalTextPhrase(this.i18nControl.getMessage("PUMP") + ":"));
            tableComment.addCell(this.createNormalTextPhraseSmall(pumpAdditionalData,
                PumpDeviceValueType.PUMP_ADDITIONAL_DATA.getColorKey()));
            commentFound = true;
        }

        displayFoodData(tableComment, gcCurrent);
        // commentFound = true;

        if (this.pumpValuesHourProcessor.isAdditionalDataForPumpTypeSet(PumpDeviceValueType.COMMENT))
        {
            tableComment.addCell(this.createNormalTextPhrase(this.i18nControl.getMessage("COMMENT") + ":"));
            tableComment.addCell(this.createNormalTextPhrase(
                this.pumpValuesHourProcessor.getAdditionalDataForPumpTypeSet(PumpDeviceValueType.COMMENT)));
            commentFound = true;

        }

        if (commentFound)
        {
            mainTable.addCell(tableComment);
        }

        mainTable.addCell(createEmptyLine());

        if (hasCGMSData)
        {
            displayCGMSData(mainTable, gcCurrent);
        }

    }


    private String getPumpAdditionalData(DeviceValuesDay deviceValuesDay)
    {
        List<PumpValuesEntry> tbrList = new ArrayList<PumpValuesEntry>();
        List<PumpValuesEntry> pairedEventsList = new ArrayList<PumpValuesEntry>();

        List<SimpleTimeValueDataDto> pumpExtraDataList = new ArrayList<SimpleTimeValueDataDto>();

        if (deviceValuesDay == null)
            return null;

        List<DeviceValuesEntry> listEntries = deviceValuesDay.getList();

        if (listEntries == null)
            return null;

        for (DeviceValuesEntry entry : deviceValuesDay.getList())
        {
            PumpValuesEntry pve = (PumpValuesEntry) entry;
            SimpleTimeValueDataDto pumpAdditionalDataDto = null;

            if (pve.getBaseType() == PumpBaseType.Bolus)
            {
                pumpAdditionalDataDto = pumpAdditionalDataHandler.processBolusEntry(pve);
            }
            else if (pve.getBaseType() == PumpBaseType.Basal)
            {
                if (pumpAdditionalDataHandler.isTBREntry(pve))
                {
                    tbrList.add(pve);
                }

            }
            else if (pve.getBaseType() == PumpBaseType.Event)
            {
                pumpAdditionalDataDto = pumpAdditionalDataHandler.processEventEntryWithoutPaired(pve);

                if (pumpAdditionalDataDto == null)
                {
                    if (pumpAdditionalDataHandler.isPairedEvent(pve))
                    {
                        pairedEventsList.add(pve);
                    }
                }
            }

            if (pumpAdditionalDataDto != null)
            {
                pumpExtraDataList.add(pumpAdditionalDataDto);
            }
        }

        if (tbrList.size() > 0)
        {
            pumpExtraDataList.addAll(pumpAdditionalDataHandler.processTBRs(tbrList));
        }

        if (pairedEventsList.size() > 0)
        {
            pumpExtraDataList.addAll(pumpAdditionalDataHandler.processPairedEvents(pairedEventsList));
        }

        StringBuilder sb = new StringBuilder();

        Collections.sort(pumpExtraDataList);

        for (SimpleTimeValueDataDto data : pumpExtraDataList)
        {
            DataAccessPump.appendToStringBuilder(sb, data.getValue(), ", ");
        }

        return sb.toString();
    }


    private PdfPCell createEmptyLine()
    {
        PdfPCell cell = new PdfPCell();
        cell.setFixedHeight(12);
        cell.setBorder(0);
        cell.setPhrase(this.createEmptyTextPhrase());
        return cell;
    }


    private boolean displayFoodData(PdfPTable tableComment, GregorianCalendar gcCurrent)
    {
        if ((this.sheetType != PrintPumpDataDailyTimeSheetType.BaseWithFood) && //
                (this.sheetType != PrintPumpDataDailyTimeSheetType.BaseWithFoodAndCGMS))
        {
            return false;
        }

        // FIXME
        // add adding of food to report
        // tableComment.addCell(this.createBoldTextPhrase(this.i18nControl.getMessage("PUMP")
        // + ":"));
        // tableComment.addCell(this.createNormalTextPhraseSmall(
        // this.pumpValuesHourProcessor.getAdditionalDataForPumpTypeSet(PumpDeviceValueType.PUMP_ADDITIONAL_DATA)
        // ));

        return false;

    }


    private void displayCGMSData(PdfPTable mainTable, GregorianCalendar currentCalendar)
    {
        try
        {
            long date = ATechDate.getATDateTimeFromGC(currentCalendar, ATechDateType.DateOnly);

            JFreeChart chart = null;

            if (cgmsReadingsDataMap.containsKey(date))
            {
                chart = cgmsGraphDataHandler.createDailyChartForReport(dataAccessPump, cgmsReadingsDataMap.get(date));
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
            String dateString = ATechDate.getDateString(ATechDateType.DateAndTimeSec, gcCurrent);

            if (this.basalRatesRange.containsKey(dateString))
            {
                basalRatesDTO = this.basalRatesRange.get(dateString);
            }
        }

        for (int hour = 0; hour < 24; hour++)
        {

            if (type == PumpDeviceValueType.BASAL)
            {
                float value = writeBasalProfileValue(gcCurrent, hour, basalRatesDTO, "5", table);

                if (value > 0.0f)
                {
                    sum += value;
                }
            }
            else
            {
                if (deviceValuesDay == null)
                {
                    table.addCell(this.createCellWithEmptySmallText());
                }
                else
                {
                    List<DeviceValuesEntry> entries = deviceValuesDay.getEntriesForHour(hour);

                    PumpValuesHour pumpValuesHour = pumpValuesHourProcessor.createPumpValuesHour(entries);

                    if (type == PumpDeviceValueType.BOLUS)
                    {
                        if (pumpValuesHour.getBolus() > 0.0f)
                        {
                            sum += pumpValuesHour.getBolus();
                            table.addCell(this.createCellWithNormalSmallText(
                                dataAccessPump.getFormatedBolusValue(pumpValuesHour.getBolus()), type.getColorKey()));

                            if (pumpValuesHour.hasBolusSpecial())
                            {
                                // pumpValuesHourProcessor.addAdditionalData(PumpDeviceValueType.PUMP_ADDITIONAL_DATA,
                                // pumpValuesHour.getBolusSpecial());

                                sum += pumpValuesHour.getBolusSpecialForSum();
                            }

                        }
                        else if (pumpValuesHour.hasBolusSpecial())
                        {
                            table.addCell(this.createCellWithNormalSmallText("*", type.getColorKey()));
                            // pumpValuesHourProcessor.addAdditionalData(PumpDeviceValueType.PUMP_ADDITIONAL_DATA,
                            // pumpValuesHour.getBolusSpecial());

                            sum += pumpValuesHour.getBolusSpecialForSum();
                        }
                        else
                        {
                            table.addCell(this.createCellWithEmptySmallText());
                        }

                    }
                    else if (type == PumpDeviceValueType.BG)
                    {
                        if (pumpValuesHour.getBgs().size() > 1)
                        {
                            table.addCell(this.createCellWithNormalSmallText("*", type.getColorKey()));

                            pumpValuesHourProcessor.addAdditionalData(PumpDeviceValueType.BG,
                                pumpValuesHour.getMultipleBgs());
                        }
                        else if (pumpValuesHour.getBgs().size() == 1)
                        {
                            sum += pumpValuesHour.getBgProcessedValue();
                            count++;

                            table.addCell(this.createCellWithNormalSmallText(
                                dataAccessPump.getDisplayedBGString(pumpValuesHour.getBgProcessedValue()),
                                type.getColorKey()));
                        }
                        else
                        {
                            table.addCell(this.createCellWithEmptySmallText());
                        }
                    }
                    else
                    {
                        // CH
                        if (pumpValuesHour.getCH() > 0.0f)
                        {
                            sum += pumpValuesHour.getCH();
                            table.addCell(this.createCellWithNormalSmallText(
                                DataAccessPlugInBase.Decimal0Format.format(pumpValuesHour.getCH()),
                                type.getColorKey()));
                        }
                        else
                        {
                            table.addCell(this.createCellWithEmptySmallText());
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
                    this.createCellWithNormalSmallText(dataAccessPump.getDisplayedBGString(d), type.getColorKey()));
            }
            else if (type == PumpDeviceValueType.BASAL)
            {
                table.addCell(
                    this.createCellWithNormalSmallText(dataAccessPump.getFormatedBasalValue(sum), type.getColorKey()));
            }
            else if (type == PumpDeviceValueType.BOLUS)
            {
                table.addCell(
                    this.createCellWithNormalSmallText(dataAccessPump.getFormatedBolusValue(sum), type.getColorKey()));
            }
            else
            {
                table.addCell(this.createCellWithNormalSmallText(DataAccessPlugInBase.Decimal0Format.format(sum),
                    type.getColorKey()));
            }

        }
        else
        {
            table.addCell(this.createCellWithEmptySmallText());
        }

    }


    private float writeBasalProfileValue(GregorianCalendar gcCurrent, int hour, BasalRatesDayDTO basalRatesDTO,
            String activeProfileName, PdfPTable table)
    {

        if (basalRatesDTO == null)
        {
            table.addCell(this.createCellWithEmptySmallText());
            return 0.0f;
        }
        else
        {
            float basalValue = basalRatesDTO.getBasalForHour(hour);

            if (basalValue < 0.0f)
            {
                table.addCell(this.createCellWithEmptySmallText());
            }
            else
            {
                table.addCell(this.createCellWithNormalSmallText(dataAccessPump.getFormatedBasalValue(basalValue),
                    PumpDeviceValueType.BASAL.getColorKey()));
            }

            return basalValue;
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
        return this.sheetType.getTitle();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getFileNameBase()
    {
        return this.sheetType.getFileNamePrefix();
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


    private static PumpPluginDefinition getPluginDefinition(ATDataAccessLMAbstract da)
    {
        return new PumpPluginDefinition(da.getLanguageManager());
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

        DataAccessPump da = DataAccessPump.createInstance(getPluginDefinition(daCore));

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
        // Normal sheet (v2) - I
        BaseSheet("PUMP_DATA_DAILY_TIMESHEET_1", "Pump_DailyTimeSheet_1"),
        // Normal + CGMS - II
        BaseWithCGMS("PUMP_DATA_DAILY_TIMESHEET_2", "Pump_DailyTimeSheet_2"),
        // Normal + Food - III
        BaseWithFood("PUMP_DATA_DAILY_TIMESHEET_3", "Pump_DailyTimeSheet_3"),
        // Normal + Food + CGMS - IV
        BaseWithFoodAndCGMS("PUMP_DATA_DAILY_TIMESHEET_4", "Pump_DailyTimeSheet_4");

        private String title;
        private String fileNamePrefix;


        PrintPumpDataDailyTimeSheetType(String title, String fileNamePrefix)
        {
            this.title = title;
            this.fileNamePrefix = fileNamePrefix;
        }


        public String getTitle()
        {
            return title;
        }


        public String getFileNamePrefix()
        {
            return fileNamePrefix;
        }
    }

}
