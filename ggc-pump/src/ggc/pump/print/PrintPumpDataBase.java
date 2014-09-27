package ggc.pump.print;

import ggc.core.data.DailyValuesRow;
import ggc.core.util.DataAccess;
import ggc.plugin.data.DeviceValuesDay;
import ggc.plugin.data.DeviceValuesRange;
import ggc.pump.data.PumpValuesEntry;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.atech.utils.data.ATechDate;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
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
 *  Filename:     PrintPumpDataBase  
 *  Description:  Print Pump Data - Base
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */
public class PrintPumpDataBase extends PrintPumpDataAbstract
{

    /**
     * Constructor
     *  
     * @param dvr 
     */
    public PrintPumpDataBase(DeviceValuesRange dvr)
    {
        super(dvr);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillDocumentBody(Document document) throws Exception
    {
        Font f = this.textFontNormal;

        PdfPTable datatable = new PdfPTable(getTableColumnsCount());
        datatable.setWidths(getTableColumnWidths());
        datatable.setWidthPercentage(100); // percentage

        datatable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT); // ALIGN_CENTER);
        datatable.getDefaultCell().setBorderWidth(1);

        datatable.addCell(this.createBoldTextPhrase("DATE"));
        datatable.addCell(this.createBoldTextPhrase("TIME"));
        datatable.addCell(this.createBoldTextPhrase("BASE_TYPE"));
        datatable.addCell(this.createBoldTextPhrase("SUB_TYPE"));
        datatable.addCell(this.createBoldTextPhrase("VALUE_SHORT"));
        datatable.addCell(this.createBoldTextPhrase("OTHER_DATA"));

        // writeAdditionalHeader(datatable);

        GregorianCalendar gc_end = deviceValuesRange.getEndGC();
        gc_end.add(Calendar.DAY_OF_MONTH, 1);

        GregorianCalendar gc_current = deviceValuesRange.getStartGC();

        do
        {

            ATechDate atd = new ATechDate(da_local.getDataEntryObject().getDateTimeFormat(), gc_current);

            if (deviceValuesRange.isDayEntryAvailable(atd.getATDateTimeAsLong()))
            {

                DeviceValuesDay dvd = deviceValuesRange.getDayEntry(atd.getATDateTimeAsLong());

                // FIXME fix this
                datatable.addCell(new Phrase(atd.getDateString(), f));

                for (int i = 0; i < dvd.getList().size(); i++)
                {

                    PumpValuesEntry pve = (PumpValuesEntry) dvd.getList().get(i);

                    ATechDate atdx = new ATechDate(da_local.getDataEntryObject().getDateTimeFormat(), pve.getDateTime());

                    if (i != 0)
                    {
                        datatable.addCell(new Phrase("", f));
                    }

                    datatable.addCell(new Phrase(atdx.getTimeString(), f));
                    datatable.addCell(new Phrase(pve.getBaseTypeString(), f));
                    datatable.addCell(new Phrase(pve.getSubTypeString(), f));
                    datatable.addCell(new Phrase(pve.getValue(), f));
                    datatable.addCell(new Phrase(pve
                            .getAdditionalDataPrint(PumpValuesEntry.PRINT_ADDITIONAL_ALL_ENTRIES), f));
                }

            }
            else
            {
                datatable.addCell(new Phrase(atd.getDateString(), f));
                this.writeEmptyColumnData(datatable);
            }

            gc_current.add(Calendar.DAY_OF_MONTH, 1);

        } while (gc_current.before(gc_end));

        document.add(datatable);

        // System.out.println("Elements all: " + this.m_data.size() +
        // " in iterator: " + count);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int[] getTableColumnWidths()
    {
        int headerwidths[] = { 9, 7, 10, 10, 12, 52 }; // percentage
        return headerwidths;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTableColumnsCount()
    {
        return 6;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitleText()
    {
        return "PUMP_DATA_BASE";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeAdditionalHeader(PdfPTable table) throws Exception
    {
        // table.addCell(new Phrase(ic.getMessage("CH"), this.text_bold));
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
    public void writeTogetherData(PdfPTable table, DailyValuesRow rw) throws Exception
    {
        table.addCell(this.createItalicTextPhrase("TOGETHER"));
        table.addCell(this.createEmptyTextPhrase());
        table.addCell(this.createEmptyTextPhrase());

        table.addCell(new Phrase(DataAccess.Decimal2Format.format(rw.getCH()), this.textFontItalic));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFileNameBase()
    {
        return "PumpDataBase";
    }

}
