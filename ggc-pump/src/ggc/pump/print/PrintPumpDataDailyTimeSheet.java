package ggc.pump.print;

import ggc.core.util.DataAccess;
import ggc.core.util.GGCLanguageManagerRunner;
import ggc.pump.data.PumpDeviceValueType;
import ggc.pump.util.DataAccessPump;

import com.atech.i18n.mgr.LanguageManager;
import com.atech.print.engine.ITextDocumentPrintSettings;
import com.atech.print.engine.PrintAbstractITextWithDataRead;
import com.atech.print.engine.PrintParameters;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;

/**
 * Application: GGC - GNU Gluco Control
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
 * Filename: PrintFoodMenuBase
 * Description: Print Base Food Menu
 * Author: andyrozman {andy@atech-software.com}
 */

// WARNING: THIS IS WORK IN PROGRESS, PLEASE DON'T EDIT. Andy

public class PrintPumpDataDailyTimeSheet extends PrintAbstractITextWithDataRead
{
    Font smallFont = null;

    public PrintPumpDataDailyTimeSheet(PrintParameters parameters)
    {
        super(DataAccessPump.getInstance(), parameters, false);

        smallFont = new Font(this.baseFontTimes, 6, Font.BOLD);

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

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillDocumentBody(Document document) throws Exception
    {

        PdfPTable datatable = new PdfPTable(27);
        datatable.setWidths(new float[] { 4.0f, 6.0f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f,
                                         3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f, 3.5f,
                                         6.0f }); // 6 + 2 + 4
        datatable.setWidthPercentage(100); // percentage

        datatable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT); // ALIGN_CENTER);
        datatable.getDefaultCell().setBorderWidth(1);

        /*
         * datatable.addCell(this.createBoldTextPhrase("DATE"));
         * datatable.addCell(this.createBoldTextPhrase("TIME"));
         * datatable.addCell(this.createBoldTextPhrase("BASE_TYPE"));
         * datatable.addCell(this.createBoldTextPhrase("SUB_TYPE"));
         * datatable.addCell(this.createBoldTextPhrase("VALUE_SHORT"));
         * datatable.addCell(this.createBoldTextPhrase("OTHER_DATA"));
         */
        // PdfCell c = new PdfCell();
        // c.

        // line 1

        // cell 1
        datatable.addCell(this.createEmptyTextPhrase());
        datatable.addCell(this.createEmptyTextPhrase());

        for (int i = 0; i < 24; i++)
        {
            datatable.addCell(this.createBoldTextPhrase("" + i));
        }

        datatable.addCell(this.createBoldTextPhrase("SUM"));

        for (int j = 0; j < 4; j++)
        {
            if (j == 0)
            {
                datatable.addCell(this.createEmptyTextPhrase());
                datatable.addCell(this.createEmptyTextPhrase());

                writeHourlyValues(PumpDeviceValueType.BASAL, datatable);
            }
            else if (j == 1)
            {
                datatable.addCell(this.createEmptyTextPhrase());
                datatable.addCell(this.createEmptyTextPhrase());

                writeHourlyValues(PumpDeviceValueType.BOLUS, datatable);
            }
            else if (j == 2)
            {
                datatable.addCell(this.createEmptyTextPhrase());
                datatable.addCell(this.createEmptyTextPhrase());

                writeHourlyValues(PumpDeviceValueType.BG, datatable);
            }
            else
            // if (j==3)
            {
                datatable.addCell(this.createEmptyTextPhrase());
                datatable.addCell(this.createEmptyTextPhrase());

                writeHourlyValues(PumpDeviceValueType.CH, datatable);
            }

        }

        document.add(datatable);

    }

    private void writeHourlyValues(PumpDeviceValueType type, PdfPTable table)
    {
        // TODO Auto-generated method stub
        float sum = 0.0f;
        float count = 0;

        for (int i = 0; i < 24; i++)
        {
            if (type == PumpDeviceValueType.BASAL)
            {
                table.addCell(this.createNormalTextPhraseSmall("10.25"));
            }
            else if (type == PumpDeviceValueType.BOLUS)
            {
                table.addCell(this.createNormalTextPhraseSmall("20.50"));
            }
            else if (type == PumpDeviceValueType.BG)
            {
                table.addCell(this.createNormalTextPhraseSmall("10.1"));
            }
            else
            // CH
            {
                table.addCell(this.createNormalTextPhraseSmall("100"));
            }
        }

        if (type == PumpDeviceValueType.BG)
        {
            // format
            float d = sum / (count * (1.0f));
            table.addCell(this.createNormalTextPhrase("" + d));
        }
        else
        {
            // format
            table.addCell(this.createNormalTextPhrase("" + sum));
        }

    }

    private Phrase createNormalTextPhraseSmall(String text)
    {
        return new Phrase(this.i18nControl.getMessage(text), smallFont);
    }

    /*
     * public void fillDocumentBodyCCC(Document document) throws Exception
     * {
     * Font f = this.textFontNormal;
     * PdfPTable datatable = new PdfPTable(getTableColumnsCount());
     * datatable.setWidths(getTableColumnWidths());
     * datatable.setWidthPercentage(100); // percentage
     * datatable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
     * datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT); //
     * ALIGN_CENTER);
     * datatable.getDefaultCell().setBorderWidth(1);
     * datatable.addCell(this.createBoldTextPhrase("DATE"));
     * datatable.addCell(this.createBoldTextPhrase("TIME"));
     * datatable.addCell(this.createBoldTextPhrase("BASE_TYPE"));
     * datatable.addCell(this.createBoldTextPhrase("SUB_TYPE"));
     * datatable.addCell(this.createBoldTextPhrase("VALUE_SHORT"));
     * datatable.addCell(this.createBoldTextPhrase("OTHER_DATA"));
     * // writeAdditionalHeader(datatable);
     * GregorianCalendar gc_end = deviceValuesRange.getEndGC();
     * gc_end.add(Calendar.DAY_OF_MONTH, 1);
     * GregorianCalendar gc_current = deviceValuesRange.getStartGC();
     * do
     * {
     * ATechDate atd = new
     * ATechDate(da_local.getDataEntryObject().getDateTimeFormat(), gc_current);
     * if (deviceValuesRange.isDayEntryAvailable(atd.getATDateTimeAsLong()))
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
     * datatable.addCell(new Phrase(pve.getValue(), f));
     * datatable.addCell(new Phrase(pve
     * .getAdditionalDataPrint(PumpValuesEntry.PRINT_ADDITIONAL_ALL_ENTRIES),
     * f));
     * }
     * }
     * else
     * {
     * datatable.addCell(new Phrase(atd.getDateString(), f));
     * this.writeEmptyColumnData(datatable);
     * }
     * gc_current.add(Calendar.DAY_OF_MONTH, 1);
     * } while (gc_current.before(gc_end));
     * document.add(datatable);
     * // System.out.println("Elements all: " + this.m_data.size() +
     * // " in iterator: " + count);
     * }
     */

    /**
     * {@inheritDoc}
     */

    public int[] getTableColumnWidths()
    {
        int headerwidths[] = { 25, 25, 25, 25 }; // percentage
        return headerwidths;
    }

    /**
     * {@inheritDoc}
     */

    public int getTableColumnsCount()
    {
        return 4;
    }

    /**
     * {@inheritDoc}
     */

    public String getTitleText()
    {
        return "PUMP_DATA_EXT";
    }

    /**
     * {@inheritDoc}
     */

    public void writeColumnData(PdfPTable table, Object /* DailyFoodEntry */mp) throws Exception
    {
        /*
         * table.addCell(new Phrase("", this.text_normal));
         * table.addCell(new Phrase("", this.text_normal));
         * table.addCell(new Phrase(mp.getName(), this.text_normal));
         * float value = 0.0f;
         * if (mp.getAmountType()==DailyFoodEntry.WEIGHT_TYPE_AMOUNT)
         * {
         * table.addCell(new Phrase(ic.getMessage("AMOUNT_LBL"),
         * this.text_normal));
         * //value = mp.getNutrientValue(205);
         * value = mp.getMealCH();
         * }
         * else if (mp.getAmountType()==DailyFoodEntry.WEIGHT_TYPE_WEIGHT)
         * {
         * table.addCell(new Phrase(ic.getMessage("WEIGHT_LBL2"),
         * this.text_normal));
         * //value = mp.getNutrientValue(205);
         * value = mp.getNutrientValue(205) * (mp.getAmount() / 100.0f);
         * }
         * else
         * {
         * table.addCell(new Phrase(mp.getHomeWeightDescription() + " (" +
         * DataAccess.Decimal0Format.format(mp.getHomeWeightMultiplier() * 100)
         * + " g)", this.text_normal));
         * value = mp.getNutrientValue(205) * mp.getHomeWeightMultiplier();
         * }
         * table.addCell(new Phrase(mp.getAmountSingleDecimalString(),
         * this.text_normal));
         * table.addCell(new Phrase(DataAccess.Decimal2Format.format(value),
         * this.text_normal)); // ch
         */
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFileNameBase()
    {
        return "Pump_DailyTimeSheet";
    }

    public static void main(String[] args)
    {
        LanguageManager lm = new LanguageManager(new GGCLanguageManagerRunner());

        DataAccessPump da = DataAccessPump.createInstance(lm);

        PrintPumpDataDailyTimeSheet pa = new PrintPumpDataDailyTimeSheet(new PrintParameters());
        // pa.init();

        System.out.println("Path: " + pa.getRelativeNameWithPath());

        // displayPDF(pa.getRelativeNameWithPath());

    }

    @Override
    public void initData()
    {
        // TODO Auto-generated method stub

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
        // TODO Auto-generated method stub
        return 8;
    }

}
