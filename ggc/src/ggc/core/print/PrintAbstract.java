package ggc.core.print;


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


import ggc.core.data.DayValuesData;
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

public abstract class PrintAbstract extends PdfPageEventHelper
{
    public DayValuesData m_data = null;
    protected DataAccess m_da = DataAccess.getInstance();
    protected I18nControl ic = I18nControl.getInstance();
    String name = "";

    BaseFont base_helvetica = null;
    BaseFont base_times = null;
    Font normal_text = null; 
    
    
    public PrintAbstract(DayValuesData data)
    {
        this.m_data = data;
        //name = System.currentTimeMillis();
        createName();

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

    
    public void createName()
    {
        // TODO: implement this
    }
    
    public abstract Paragraph getTitle();
    
    public abstract void fillDocumentBody(Document document);
    
    public abstract String getFileNameBase();
    
    public abstract String getFileNameRange();

    
    
    public void createDocument()
    {

        // step1
        File fl = new File("../data/temp/" + this.getName());
        Document document = new Document(PageSize.A4, 40, 40, 40, 40);

        try 
        {
            // step2 - asign pdf to file
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fl.getAbsoluteFile()));
            
            // step3 - set page event, open document and put on title
            writer.setPageEvent(this);
            
            document.open();
            document.add(getTitle());

            // step4 - fill body of document
            fillDocumentBody(document);
                

        } 
        catch (Exception de) 
        {
            de.printStackTrace();
        }

        // step5
        document.close();

    }

    
    
    
    
   /* 
    private void SetComment(String text, PdfPTable table)
    {
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(new Phrase(text, normal_text));
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
    }*/

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
    
    
    protected void setBackground(int element_cnt, PdfPTable table)
    {
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_BASELINE);
	
        if (element_cnt%2==1) 
            table.getDefaultCell().setGrayFill(0.9f);
        else
            table.getDefaultCell().setBackgroundColor(Color.white);
    	
    }
    
 /*   
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
   */ 

    /*
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
    */
    
    
    
    


    
    @Override
    public void onEndPage(PdfWriter writer, Document document) 
    {
        try 
        {
            Rectangle page = document.getPageSize();
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




