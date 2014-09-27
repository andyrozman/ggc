package ggc.core.print;

import ggc.core.data.MonthlyValues;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPCell;
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
 *  Filename:     PrintSimpleMonthlyReport  
 *  Description:  For printing simple Monthly Report
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class PrintSimpleMonthlyReport extends PrintExtendedMonthlyReport
{

    /**
     * Constructor
     * 
     * @param mv
     */
    public PrintSimpleMonthlyReport(MonthlyValues mv)
    {
        super(mv);
    }

    @Override
    public String getTitleText()
    {
        return "EXTENDED_MONTHLY_REPORT";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillDocumentBody(Document document) throws Exception
    {

        // Font f = text_bold;

        int NumColumns = 13;
        float groupWidth[] = { 30, 40, 30 };

        PdfPTable datatable = new PdfPTable(NumColumns);
        int headerwidths[] = { 8, 6, 8, 6, 6, 8, 6, 6, 8, 6, 6, 8, 6 }; // percentage
        datatable.setWidths(headerwidths);
        datatable.setWidthPercentage(100); // percentage
        datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell pd = new PdfPCell();
        pd.setPhrase(this.createBoldTextPhrase("DATE"));
        pd.setHorizontalAlignment(Element.ALIGN_CENTER);

        pd.setBorderWidth(1);
        datatable.addCell(pd);

        // Breakfast
        datatable.addCell(createMealHeader(groupWidth, "BREAKFAST"));

        // Lunch
        datatable.addCell(createMealHeader(groupWidth, "LUNCH"));

        // Dinner
        datatable.addCell(createMealHeader(groupWidth, "DINNER"));

        // Night
        datatable.addCell(createMealHeader(groupWidth, "NIGHT"));

        datatable.getDefaultCell().setBorderWidth(1);

        for (int i = 1; i <= this.monthlyValues.getDaysInMonth(); i++)
        {
            String[][] dta = this.monthlyValues.getDayValuesSimple(i);

            if (i % 2 == 1)
            {
                datatable.getDefaultCell().setGrayFill(0.9f);
            }
            else
            {
                datatable.getDefaultCell().setBackgroundColor(BaseColor.WHITE); // .setGrayFill(0.0f);
            }

            datatable.addCell(i + "." + this.monthlyValues.getMonth());

            for (int x = 0; x < 4; x++)
            {
                datatable.addCell(dta[x][0]);
                datatable.addCell(dta[x][1]);
                datatable.addCell(dta[x][2]);
            }

            if (i % 2 == 1)
            {
                datatable.getDefaultCell().setGrayFill(0.0f);
            }
        }

        document.add(datatable);

    }

    private PdfPCell createMealHeader(float[] groupWidth, String mealName) throws DocumentException
    {
        PdfPTable meal_b = new PdfPTable(3);
        meal_b.setHorizontalAlignment(Element.ALIGN_CENTER);
        meal_b.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        meal_b.getDefaultCell().setBorderWidth(1);
        meal_b.setWidths(groupWidth);

        PdfPCell p1 = new PdfPCell();
        p1.setPhrase(createBoldTextPhrase(mealName));
        p1.setHorizontalAlignment(Element.ALIGN_CENTER);
        p1.setBorderWidth(1);
        p1.setColspan(3);

        meal_b.addCell(p1);
        meal_b.addCell(createBoldTextPhrase("BG"));
        meal_b.addCell(createBoldTextPhrase("INS_SHORT"));
        meal_b.addCell(createBoldTextPhrase("CH"));

        PdfPCell m = new PdfPCell(meal_b);
        m.setColspan(3);

        return m;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFileNameBase()
    {
        return "ReportSimple";
    }

}
