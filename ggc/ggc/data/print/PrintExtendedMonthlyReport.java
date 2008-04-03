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

 *  Purpose:  Creating PDF for Extended Monthly Report (used for printing)
 *
 *  Author:   andyrozman  {andy@atech-software.com}
 */


import ggc.data.DailyValues;
import ggc.data.DailyValuesRow;
import ggc.data.MonthlyValues;
import ggc.core.util.DataAccess;
import ggc.core.util.I18nControl;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

public class PrintExtendedMonthlyReport extends PdfPageEventHelper
{

    public MonthlyValues m_mv = null;
    private DataAccess m_da = DataAccess.getInstance();
    private I18nControl ic = I18nControl.getInstance();
    long name = 0L;

    BaseFont base_helvetica = null;
    BaseFont base_times = null;
    
    Font normal_text = null; 
    
    
    public PrintExtendedMonthlyReport(MonthlyValues mv)
    {
        m_mv = mv;
        name = System.currentTimeMillis();

        try
        {
            base_helvetica = BaseFont.createFont("Helvetica", BaseFont.CP1250, BaseFont.NOT_EMBEDDED);
            base_times = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1250, BaseFont.NOT_EMBEDDED);
            normal_text = new Font(this.base_helvetica , 12, Font.NORMAL);
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
        //p.add(new Paragraph("", f));
        p.add(new Paragraph(ic.getMessage("EXTENDED_MONTHLY_REPORT") + " - " + m_da.getMonthsArray()[m_mv.getMonth()-1] + " " + m_mv.getYear(), f));
        //p.add(new Paragraph("May 2006"));
        p.add(new Paragraph(ic.getMessage("FOR") + " " + m_da.getSettings().getUserName(), new Font(Font.TIMES_ROMAN, 12, Font.ITALIC)));
        p.add(new Paragraph("", f));
        p.add(new Paragraph("", f));
        //p.add(new Paragraph("", f));

        return p;
    }
    
    
    

    public void createDocument()
    {

	System.out.println("document.add(BigTable)");
        // step1
        //Document document = new Document(PageSize.A4, 10, 10, 10, 10);

        File fl = new File("../data/temp/" + name + ".pdf");
        Document document = new Document(PageSize.A4, 40, 40, 40, 40);
        try 
        {
            Font f = new Font(this.base_helvetica , 12, Font.NORMAL); // this.base_times

            // step2
            PdfWriter writer = PdfWriter.getInstance(document,
                    new FileOutputStream(fl.getAbsoluteFile()));
            
                // step3
            
            
            writer.setPageEvent(this);
            
                document.open();
                document.add(getTitle());

                // step4
                /*String[] bogusData = {"", "00:00", "00.0", "00", "00",
                                "000", "00000", "Comm."
                                 }; */
                int NumColumns = 8;

                PdfPTable datatable = new PdfPTable(NumColumns);
                int headerwidths[] = { 10, 10,
                                       10, 10, 10, 
                                       10, 10,  
                                       30 }; // percentage
                datatable.setWidths(headerwidths);
                datatable.setWidthPercentage(100); // percentage
                //datatable.getDefaultCell().setPadding(3);
                //datatable.getDefaultCell().setBorderWidth(2);
                
                datatable.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
                datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                datatable.getDefaultCell().setBorderWidth(1);
                
                datatable.addCell(new Phrase(ic.getMessage("DATE"), f));
/*
                PdfPTable tb = new PdfPTable(7);
                //tb.setWidths(relativeWidths)
                tb.setWidthPercentage(100);
                tb.addCell(new PdfPCell(new Phrase(ic.getMessage("TIME"), f)));
                tb.addCell(new PdfPCell(new Phrase(ic.getMessage("BG"), f)));
                tb.addCell(new PdfPCell(new Phrase(ic.getMessage("INS1"), f)));
                tb.addCell(new PdfPCell(new Phrase(ic.getMessage("INS2"), f)));
                tb.addCell(new PdfPCell(new Phrase(ic.getMessage("CH"), f)));
                tb.addCell(new PdfPCell(new Phrase(ic.getMessage("URINE"), f)));
                tb.addCell(new PdfPCell(new Phrase(ic.getMessage("COMMENTS"), f)));
                
                
                PdfPCell cl = new PdfPCell();
                cl.setColspan(7);
                cl.setBorder(0);
                
                cl.addElement(tb);
                
                datatable.addCell(cl);
  */              
                
                
                
                datatable.addCell(new Phrase(ic.getMessage("TIME"),f));
                datatable.addCell(new Phrase(ic.getMessage("BG"), f));
                datatable.addCell(new Phrase(ic.getMessage("INS_SHORT") + " 1", f));
                datatable.addCell(new Phrase(ic.getMessage("INS_SHORT") + " 2", f));
                datatable.addCell(new Phrase(ic.getMessage("CH_SHORT"), f));
                datatable.addCell(new Phrase(ic.getMessage("URINE"), f));
                datatable.addCell(new Phrase(ic.getMessage("COMMENTS"), f));
                
                
                datatable.setHeaderRows(1); // this is the end of the table header

                datatable.getDefaultCell().setBorderWidth(1);
                
                int count = 1;
                
                for (int i = 1; i <= this.m_mv.getDaysInMonth(); i++) 
                {
                    
                    DailyValues dv = this.m_mv.getDayValuesExtended(i);
                    
                    if (dv==null)
                    {
                	this.setBackground(count, datatable);
                	addEmptyValues(i, datatable);
                	count++;
                    }
                    else
                    {
                	for(int j=0; j<dv.getRowCount(); j++)
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
                document.add(new Paragraph(ic.getMessage("STOCKS"), this.normal_text));
                
                float[] wdts = { 1, 99 };
                
                PdfPTable p = new PdfPTable(2);
                p.getDefaultCell().setBorderWidth(0.0f);
                p.setWidths(wdts);
                p.setWidthPercentage(100); // percentage
                p.addCell("");
                p.addCell(new Phrase(String.format(ic.getMessage("COMMING_IN_VERSION"), "0.5"), this.normal_text));
                
                document.add(p);
                
                document.add(new Paragraph(" "));
                document.add(new Paragraph(" "));
                
                // Doctor appointments
                document.add(new Paragraph(ic.getMessage("APPOINTMENTS"), this.normal_text));
                document.add(p);
                //document.add(new Paragraph("&nbsp;&nbsp;&nbsp;COMMING IN VERSION 0.4"));

            } 
            catch (Exception de) 
            {
                de.printStackTrace();
            }
            // step5

            document.close();

    }

    
    private void SetComment(String text, PdfPTable table)
    {
	table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	table.addCell(new Phrase(text, normal_text));
	table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
    }

    /*
    private PdfPCell GetPhrasedCell(String text)
    {
	//PdfPHeader h = new P
	/*
        PdfPCell cl = new PdfPCell();
        //cl.setBorder(PdfPCell.BOTTOM|PdfPCell.TOP|PdfPCell.LEFT|PdfPCell.RIGHT);
        cl.setBorderWidth(1);
        cl.setVerticalAlignment(Element.ALIGN_CENTER);
        cl.setHorizontalAlignment(Element.ALIGN_LEFT);
        cl.addElement(new Phrase(text, normal_text));
        */
	
	/*
        return cl;

    } */
    
    
    private void setBackground(int element_cnt, PdfPTable table)
    {
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_BASELINE);

	
        if (element_cnt%2==1) 
            table.getDefaultCell().setGrayFill(0.9f);
        else
            table.getDefaultCell().setBackgroundColor(Color.white);
    	
	
        //datatable.getDefaultCell().setGrayFill(0.9f);
	//datatable.getDefaultCell().setBackgroundColor(Color.white);
	
	
    }
    
    
    private void addEmptyValues(int day, PdfPTable table)
    {
        table.addCell(day + "." + this.m_mv.getMonth());
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
	if (entry==0)
	    table.addCell(day + "." + this.m_mv.getMonth());
	else
	    table.addCell("");
	
	
        table.addCell(dvr.getTimeAsString());
        table.addCell(dvr.getBGAsString());
        table.addCell(dvr.getIns1AsString());
        table.addCell(dvr.getIns2AsString());
        table.addCell(dvr.getCHAsString());
        table.addCell(dvr.getUrine());
        //table.addCell(GetPhrasedCell(dvr.getComment()));

        SetComment(dvr.getComment(), table);
    }
    
    
    
    
    

/*
    public void createBill(ShoppingSession sess, ShopDb db) 
    {

	System.out.println("Hello World");

	// step 1: creation of a document-object
	Document document = new Document();
	try 
	{
	    // step 2:
	    // we create a writer that listens to the document
	    // and directs a PDF-stream to a file
	    
	    // step 2: creation of the writer
            PdfWriter writer = PdfWriter.getInstance(document,
				  new FileOutputStream("HelloWorld.pdf"));

	    // step 3: we open the document
	    document.open();

	    DefaultFontMapper mapper = new DefaultFontMapper();
            FontFactory.registerDirectories();
            mapper.insertDirectory("c:\\windows\\fonts");
            // we create a template and a Graphics2D object that corresponds with it
            int w = 150;
            int h = 150;
            PdfContentByte cb = writer.getDirectContent();
            PdfTemplate tp = cb.createTemplate(w, h);
            Graphics2D g2 = tp.createGraphics(w, h, mapper);
            tp.setWidth(w);
            tp.setHeight(h);
            double ew = w/2;
            double eh = h/2;
            Ellipse2D.Double circle, oval, leaf, stem;
            Area circ, ov, leaf1, leaf2, st1, st2;
            circle = new Ellipse2D.Double();
            oval = new Ellipse2D.Double();
            leaf = new Ellipse2D.Double();
            stem = new Ellipse2D.Double();
            circ = new Area(circle);
            ov = new Area(oval);
            leaf1 = new Area(leaf);
            leaf2 = new Area(leaf);
            st1 = new Area(stem);
            st2 = new Area(stem);
            g2.setColor(Color.green);
            
            // Creates the first leaf by filling the intersection of two Area objects created from an ellipse.
            leaf.setFrame(ew-16, eh-29, 15.0, 15.0);
            leaf1 = new Area(leaf);
            leaf.setFrame(ew-14, eh-47, 30.0, 30.0);
            leaf2 = new Area(leaf);
            leaf1.intersect(leaf2);
            g2.fill(leaf1);
            
            // Creates the second leaf.
            leaf.setFrame(ew+1, eh-29, 15.0, 15.0);
            leaf1 = new Area(leaf);
            leaf2.intersect(leaf1);
            g2.fill(leaf2);
            
            g2.setColor(Color.black);
            
            // Creates the stem by filling the Area resulting from the subtraction of two Area objects created from an ellipse.
            stem.setFrame(ew, eh-42, 40.0, 40.0);
            st1 = new Area(stem);
            stem.setFrame(ew+3, eh-47, 50.0, 50.0);
            st2 = new Area(stem);
            st1.subtract(st2);
            g2.fill(st1);
            
            g2.setColor(Color.yellow);
            
            // Creates the pear itself by filling the Area resulting from the union of two Area objects created by two different ellipses.
            circle.setFrame(ew-25, eh, 50.0, 50.0);
            oval.setFrame(ew-19, eh-20, 40.0, 70.0);
            circ = new Area(circle);
            ov = new Area(oval);
            circ.add(ov);
            g2.fill(circ);
            
            g2.setColor(Color.black);
            java.awt.Font thisFont = new java.awt.Font("Arial", java.awt.Font.PLAIN, 18);
            g2.setFont(thisFont);
            String pear = "Pear";
            FontMetrics metrics = g2.getFontMetrics();
            int width = metrics.stringWidth(pear);
            g2.drawString(pear, (w - width) / 2, 20);
            g2.dispose();
            cb.addTemplate(tp, 50, 400);


/*
                    PdfPTable table = new PdfPTable(4);

		    float[] widths = { 0.15f, 0.45f, 0.15f, 0.25f };
		    
		    table.setWidths(widths);
		    table.setHeaderRows(1);




		    //PdfPCell cell = new PdfPCell(new Paragraph("header with colspan 3"));
		    //cell.setColspan(3);
		    //table.addCell(cell);
		    table.addCell("1.");
		    table.addCell("Name");
		    table.addCell("Kolicina");
		    //table.addCell("Cena");
		    table.addCell("1.2");
		    table.addCell("2.2");
		    table.addCell("3.2");
		    //table.addCell("4.2");
		    PdfPCell cell = new PdfPCell(new Paragraph("cell test1"));
		    cell.setBorderColor(new Color(255, 0, 0));
		    table.addCell(cell);
		    cell = new PdfPCell(new Paragraph("cell test2"));
		    cell.setColspan(2);
		    cell.setBackgroundColor(new Color(0xC0, 0xC0, 0xC0));
		    table.addCell(cell);
		    document.add(table);

*/  
  
/*	    } catch (DocumentException de) {
		    System.err.println(de.getMessage());
	    } catch (IOException ioe) {
		    System.err.println(ioe.getMessage());
	    }

	    // step 5: we close the document
	    document.close();
    }
  */

    
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




