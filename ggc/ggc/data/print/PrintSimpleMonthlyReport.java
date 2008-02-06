package ggc.data.print;

/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: PrintSimpleonthlyReport.java

 *  Purpose:  Creating PDF for Simple Monthly Report (used for printing)
 *
 *  Author:   andyrozman  {andy@atech-software.com}
 */


import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

import com.lowagie.text.pdf.BaseFont;

import ggc.data.MonthlyValues;
import ggc.util.DataAccess;
import ggc.util.I18nControl;

public class PrintSimpleMonthlyReport extends PdfPageEventHelper 
{

    public MonthlyValues m_mv = null;
    private DataAccess m_da = DataAccess.getInstance();
    private I18nControl ic = I18nControl.getInstance();
    long name = 0L;

    BaseFont base_helvetica = null;
    BaseFont base_times = null;
    
    public PrintSimpleMonthlyReport(MonthlyValues mv)
    {
        m_mv = mv;
        name = System.currentTimeMillis();

        try
        {
            base_helvetica = BaseFont.createFont("Helvetica", BaseFont.CP1250, BaseFont.NOT_EMBEDDED);
            base_times = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1250, BaseFont.NOT_EMBEDDED);
        }
        catch(Exception ex)
        {
            System.out.println("Exception on font create: " + ex);
        }
        
        createDocument();
    }

    public String getName()
    {
        return name + ".pdf";
    }


    public Paragraph getTitle()
    {
        Paragraph p = new Paragraph();

        Font f = new Font(this.base_times , 16, Font.BOLD); 
        
        p.setAlignment(Element.ALIGN_CENTER);
        p.add(new Paragraph("", f));
        p.add(new Paragraph(ic.getMessage("SIMPLE_MONTHLY_REPORT") + " - " + m_da.getMonthsArray()[m_mv.getMonth()-1] + " " + m_mv.getYear(), f));
        //p.add(new Paragraph("May 2006"));
        p.add(new Paragraph(ic.getMessage("FOR") + " " + m_da.getSettings().getUserName(), new Font(Font.TIMES_ROMAN, 12, Font.ITALIC)));
        p.add(new Paragraph("", f));
        p.add(new Paragraph("", f));
        p.add(new Paragraph("", f));

        return p;
    }


    public void createDocument()
    {
        // step1

        File fl = new File("../data/temp/" + name + ".pdf");
        Document document = new Document(PageSize.A4, 40, 20, 20, 40);
        
	
        try 
        {
            Font f = new Font(this.base_helvetica , 12, Font.NORMAL); // this.base_times

            
            // step2
            PdfWriter writer = PdfWriter.getInstance(document,
                                   new FileOutputStream(fl.getAbsoluteFile()));

            writer.setPageEvent(this);

            // step3
            document.open();
            document.add(getTitle());

            // step4
            int NumColumns = 13;
            float groupWidth[] = { 30, 40, 30 };

            PdfPTable datatable = new PdfPTable(NumColumns);
            int headerwidths[] = { 8, 
				               6, 8, 6,  
				               6, 8, 6, 
                               6, 8, 6, 
                               6, 8, 6 
				       }; // percentage
            datatable.setWidths(headerwidths);
            datatable.setWidthPercentage(100); // percentage
            datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell pd = new PdfPCell();
            pd.setPhrase(new Phrase(ic.getMessage("DATE"), f));
            pd.setHorizontalAlignment(Element.ALIGN_CENTER);
        
            pd.setBorderWidth(1);
            datatable.addCell(pd);


            // Breakfast
            PdfPTable meal_b = new PdfPTable(3);
            meal_b.setHorizontalAlignment(Element.ALIGN_CENTER);
            meal_b.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            meal_b.getDefaultCell().setBorderWidth(1);
            meal_b.setWidths(groupWidth);

            PdfPCell p1 = new PdfPCell();
            p1.setPhrase(new Phrase(ic.getMessage("BREAKFAST"), f));
            p1.setHorizontalAlignment(Element.ALIGN_CENTER);
            p1.setBorderWidth(1);
            p1.setColspan(3);

            meal_b.addCell(p1);
            meal_b.addCell(new Phrase(ic.getMessage("BG"), f));
            meal_b.addCell(new Phrase(ic.getMessage("INS_SHORT"), f));
            meal_b.addCell(new Phrase(ic.getMessage("CH"), f));

            PdfPCell m = new PdfPCell(meal_b);
            m.setColspan(3);
            datatable.addCell(m);

            // Lunch
            PdfPTable meal_l = new PdfPTable(3);
            meal_l.setHorizontalAlignment(Element.ALIGN_CENTER);
            meal_l.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            meal_l.getDefaultCell().setBorderWidth(1);
            meal_l.setWidths(groupWidth);

            PdfPCell p2 = new PdfPCell();
            p2.setPhrase(new Phrase(ic.getMessage("LUNCH"), f));
            p2.setHorizontalAlignment(Element.ALIGN_CENTER);
            p2.setBorderWidth(1);
            p2.setColspan(3);

            meal_l.addCell(p2);
            meal_l.addCell(new Phrase(ic.getMessage("BG"),f));
            meal_l.addCell(new Phrase(ic.getMessage("INS_SHORT"),f));
            meal_l.addCell(new Phrase(ic.getMessage("CH"),f));

            PdfPCell m2 = new PdfPCell(meal_l);
            m2.setColspan(3);
            datatable.addCell(m2);


            // Dinner
            PdfPTable meal_d = new PdfPTable(3);
            meal_d.setHorizontalAlignment(Element.ALIGN_CENTER);
            meal_d.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            meal_d.getDefaultCell().setBorderWidth(1);
            meal_d.setWidths(groupWidth);


            PdfPCell p3 = new PdfPCell();
            p3.setPhrase(new Phrase(ic.getMessage("DINNER"), f));
            p3.setHorizontalAlignment(Element.ALIGN_CENTER);
            p3.setBorderWidth(1);
            p3.setColspan(3);

            meal_d.addCell(p3);
            meal_d.addCell(new Phrase(ic.getMessage("BG"),f));
            meal_d.addCell(new Phrase(ic.getMessage("INS_SHORT"),f));
            meal_d.addCell(new Phrase(ic.getMessage("CH"),f));

            PdfPCell m3 = new PdfPCell(meal_d);
            m3.setColspan(3);
            datatable.addCell(m3);

            // Night
            PdfPTable meal_n = new PdfPTable(3);
            meal_n.setHorizontalAlignment(Element.ALIGN_CENTER);
            meal_n.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            meal_n.getDefaultCell().setBorderWidth(1);
            meal_n.setWidths(groupWidth);

            PdfPCell p4 = new PdfPCell();
            p4.setPhrase(new Phrase(ic.getMessage("NIGHT"), f));
            p4.setHorizontalAlignment(Element.ALIGN_CENTER);
            p4.setColspan(3);

            meal_n.addCell(p4);
            meal_n.addCell(new Phrase(ic.getMessage("BG"),f));
            meal_n.addCell(new Phrase(ic.getMessage("INS_SHORT"),f));
            meal_n.addCell(new Phrase(ic.getMessage("CH"),f));

            PdfPCell m4 = new PdfPCell(meal_n);
            m4.setColspan(3);
            m4.setBorderWidth(1);
            datatable.addCell(m4);
            datatable.getDefaultCell().setBorderWidth(1);


            for (int i = 1; i <= m_mv.getDaysInMonth(); i++) 
            {
                String[][] dta = m_mv.getDayValuesSimple(i);

                if (i%2==1) 
                {
                    datatable.getDefaultCell().setGrayFill(0.9f);
                }
                else
                {
                    datatable.getDefaultCell().setBackgroundColor(Color.white); //.setGrayFill(0.0f);
                    //datatable.getDefaultCell().se
                }

                datatable.addCell(i+"." + this.m_mv.getMonth());

                //System.out.println(i);

                for (int x=0; x < 4; x++) 
                {
                    datatable.addCell(dta[x][0]);
                    datatable.addCell(dta[x][1]);
                    datatable.addCell(dta[x][2]);
                }

                if (i%2==1) 
                {
                    datatable.getDefaultCell().setGrayFill(0.0f);
                }
            }

            document.add(datatable);

            // step5
            document.close();

	    } 
	    catch (Exception de) 
	    {
            de.printStackTrace();
	    }

    }


    @Override
    public void onEndPage(PdfWriter writer, Document document) 
    {
        try 
	{
            Rectangle page = document.getPageSize();
	    /*
            PdfPTable head = new PdfPTable(3);
            for (int k = 1; k <= 6; ++k)
                head.addCell("head " + k);
            head.setTotalWidth(page.width() - document.leftMargin() - document.rightMargin());
            head.writeSelectedRows(0, -1, document.leftMargin(), page.height() - document.topMargin() + head.getTotalHeight(),
                writer.getDirectContent()); */
            PdfPTable foot = new PdfPTable(1);

            PdfPCell pc = new PdfPCell();
            pc.setBorderColor(Color.white);

            Font f = new Font(this.base_times , 10, Font.ITALIC|Font.BOLD); // this.base_times

            pc.setPhrase(new Phrase(new Chunk(ic.getMessage("REPORT_FOOTER"), f)));
            pc.setHorizontalAlignment(Element.ALIGN_CENTER);
            foot.addCell(pc);
            foot.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
            foot.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin(),
            writer.getDirectContent());
            
            
        }
        catch (Exception e) 
        {
            throw new ExceptionConverter(e);
        }
    }


}

