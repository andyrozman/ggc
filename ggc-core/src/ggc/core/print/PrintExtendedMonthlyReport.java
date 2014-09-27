package ggc.core.print;

import ggc.core.data.DailyValues;
import ggc.core.data.DailyValuesRow;
import ggc.core.data.MonthlyValues;
import ggc.core.util.DataAccess;

import com.atech.print.engine.ITextDocumentPrintSettings;
import com.atech.print.engine.PrintAbstractIText;
import com.atech.utils.ATDataAccessAbstract;
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
 *  Filename:     PrintExtendedMonthlyReport
 *  Description:  For printing Extended Monthly Report
 *
 *  Author: andyrozman {andy@atech-software.com}
 */

public class PrintExtendedMonthlyReport extends PrintAbstractIText
{

    MonthlyValues monthlyValues;
    DataAccess dataAccessLocal;

    /**
     * Constructor
     *
     * @param mv
     */
    public PrintExtendedMonthlyReport(MonthlyValues mv)
    {
        super(DataAccess.getInstance().getI18nControlInstance(), false);

        dataAccessLocal = (DataAccess) this.dataAccess;
        this.monthlyValues = mv;

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
        p.add(new Paragraph(this.i18nControl.getMessage(getTitleText()) + " - "
                + this.dataAccessLocal.getMonthsArray()[monthlyValues.getMonth() - 1] + " " + monthlyValues.getYear(),
                f));
        p.add(new Paragraph(
                this.i18nControl.getMessage("FOR") + " " + this.dataAccessLocal.getSettings().getUserName(), new Font(
                        FontFamily.TIMES_ROMAN, 12, Font.ITALIC)));
        p.add(new Paragraph("", f));
        p.add(new Paragraph("", f));

        return p;
    }

    public String getTitleText()
    {
        return "EXTENDED_MONTHLY_REPORT";
    }

    private void setComment(String text, PdfPTable table)
    {
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(new Phrase(text, this.textFontNormal));
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
    }

    private void addEmptyValues(int day, PdfPTable table)
    {
        table.addCell(day + "." + this.monthlyValues.getMonth());
        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("");
    }

    private void addValues(int entry, int day, DailyValuesRow dvr, PdfPTable table)
    {
        if (entry == 0)
        {
            table.addCell(day + "." + this.monthlyValues.getMonth());
        }
        else
        {
            table.addCell("");
        }

        table.addCell(dvr.getTimeAsString());

        if (dvr.getBG() > 0.0)
        {
            table.addCell(dvr.getBGAsString());
        }
        else
        {
            table.addCell("");
        }

        table.addCell(dvr.getIns1AsString());
        table.addCell(dvr.getIns2AsString());
        table.addCell(dvr.getCHAsString());
        table.addCell(dvr.getUrine());
        // table.addCell(GetPhrasedCell(dvr.getComment()));

        setComment(dvr.getComment(), table);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillDocumentBody(Document document) throws Exception
    {
        int numColumns = 8;

        PdfPTable datatable = new PdfPTable(numColumns);
        int headerwidths[] = { 10, 10, 10, 10, 10, 10, 10, 30 }; // percentage
        datatable.setWidths(headerwidths);
        datatable.setWidthPercentage(100); // percentage

        datatable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        datatable.getDefaultCell().setBorderWidth(1);

        datatable.addCell(createBoldTextPhrase("DATE"));
        datatable.addCell(createBoldTextPhrase("TIME"));
        datatable.addCell(createBoldTextPhrase("BG"));
        datatable.addCell(createBoldTextPhrase("INS_SHORT"));
        datatable.addCell(createBoldTextPhrase("INS_SHORT"));
        datatable.addCell(createBoldTextPhrase("CH_SHORT"));
        datatable.addCell(createBoldTextPhrase("URINE"));
        datatable.addCell(createBoldTextPhrase("COMMENTS"));

        datatable.setHeaderRows(1); // this is the end of the table header

        datatable.getDefaultCell().setBorderWidth(1);

        int count = 1;

        for (int i = 1; i <= this.monthlyValues.getDaysInMonth(); i++)
        {

            DailyValues dv = this.monthlyValues.getDayValuesExtended(i);

            if (dv == null)
            {
                this.setBackground(count, datatable);
                addEmptyValues(i, datatable);
                count++;
            }
            else
            {
                for (int j = 0; j < dv.getRowCount(); j++)
                {
                    this.setBackground(count, datatable);
                    addValues(j, i, dv.getRow(j), datatable);
                    count++;
                }
            }

        }

        document.add(datatable);

        // TO-DO

        // Stocks
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph(this.i18nControl.getMessage("STOCKS"), this.textFontNormal));

        float[] wdts = { 1, 99 };

        PdfPTable p = new PdfPTable(2);
        p.getDefaultCell().setBorderWidth(0.0f);
        p.setWidths(wdts);
        p.setWidthPercentage(100); // percentage
        p.addCell("");
        p.addCell(new Phrase(String.format(this.i18nControl.getMessage("COMMING_IN_VERSION"), "0.7"),
                this.textFontNormal));

        document.add(p);

        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));

        // Doctor appointments
        document.add(new Paragraph(this.i18nControl.getMessage("APPOINTMENTS"), this.textFontNormal));
        document.add(p);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFileNameBase()
    {
        return "ReportExt";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFileNameRange()
    {
        return "" + monthlyValues.getYear() + "_" + ATDataAccessAbstract.getLeadingZero(monthlyValues.getMonth(), 2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTextSize()
    {
        return 12;
    }

    @Override
    public ITextDocumentPrintSettings getCustomDocumentSettings()
    {
        return new ITextDocumentPrintSettings(30, 30, 10, 30);
    }

}
