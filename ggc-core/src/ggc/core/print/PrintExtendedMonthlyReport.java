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


import ggc.core.data.DailyValues;
import ggc.core.data.DailyValuesRow;
import ggc.core.data.MonthlyValues;
import ggc.core.util.DataAccess;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPTable;

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


public class PrintExtendedMonthlyReport extends PrintAbstract // extends PdfPageEventHelper
{
    
    /**
     * Constructor
     * 
     * @param mv
     */
    public PrintExtendedMonthlyReport(MonthlyValues mv)
    {
        super(mv, DataAccess.getInstance().getI18nControlInstance());
        
        /*
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
        
        createDocument(); */
    }

    
    /**
     * Get Title
     * 
     * @see ggc.core.print.PrintAbstract#getTitle()
     */
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
    
    
    
/*
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
*/
    
    private void SetComment(String text, PdfPTable table)
    {
    	table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
    	table.addCell(new Phrase(text, this.text_normal));
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
    
    /*
    private void setBackground(int element_cnt, PdfPTable table)
    {
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_BASELINE);

	
        if (element_cnt%2==1) 
            table.getDefaultCell().setGrayFill(0.9f);
        else
            table.getDefaultCell().setBackgroundColor(Color.white);
    	
	
        //datatable.getDefaultCell().setGrayFill(0.9f);
	//datatable.getDefaultCell().setBackgroundColor(Color.white);
	
	
    }*/
    
    
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
        
        if (dvr.getBG()>0.0)
            table.addCell(dvr.getBGAsString());
        else
            table.addCell("");
            
        table.addCell(dvr.getIns1AsString());
        table.addCell(dvr.getIns2AsString());
        table.addCell(dvr.getCHAsString());
        table.addCell(dvr.getUrine());
        //table.addCell(GetPhrasedCell(dvr.getComment()));

        SetComment(dvr.getComment(), table);
    }
    
    
    
    
    

    
/*    
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
*/

    
    /**
     * Create document body.
     * 
     * @param document
     * @throws Exception
     */
    @Override
    public void fillDocumentBody(Document document) throws Exception
    {

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
        
        datatable.addCell(new Phrase(ic.getMessage("DATE"), this.text_bold));
        datatable.addCell(new Phrase(ic.getMessage("TIME"),this.text_bold));
        datatable.addCell(new Phrase(ic.getMessage("BG"), this.text_bold));
        datatable.addCell(new Phrase(ic.getMessage("INS_SHORT") + " 1", this.text_bold));
        datatable.addCell(new Phrase(ic.getMessage("INS_SHORT") + " 2", this.text_bold));
        datatable.addCell(new Phrase(ic.getMessage("CH_SHORT"), this.text_bold));
        datatable.addCell(new Phrase(ic.getMessage("URINE"), this.text_bold));
        datatable.addCell(new Phrase(ic.getMessage("COMMENTS"), this.text_bold));
        
        
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
        document.add(new Paragraph(ic.getMessage("STOCKS"), this.text_normal));
        
        float[] wdts = { 1, 99 };
        
        PdfPTable p = new PdfPTable(2);
        p.getDefaultCell().setBorderWidth(0.0f);
        p.setWidths(wdts);
        p.setWidthPercentage(100); // percentage
        p.addCell("");
        p.addCell(new Phrase(String.format(ic.getMessage("COMMING_IN_VERSION"), "0.5"), this.text_normal));
        
        document.add(p);
        
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        
        // Doctor appointments
        document.add(new Paragraph(ic.getMessage("APPOINTMENTS"), this.text_normal));
        document.add(p);
        //document.add(new Paragraph("&nbsp;&nbsp;&nbsp;COMMING IN VERSION 0.4"));
        
    }


    /**
     * Returns base filename for printing job, this is just part of end filename (starting part)
     * 
     * @return 
     */
    @Override
    public String getFileNameBase()
    {
        return "ReportExt";
    }


    /**
     * Returns data part of filename for printing job, showing which data is being printed
     * 
     * @return 
     */
    @Override
    public String getFileNameRange()
    {
        return "" + m_mv.getYear() + "_" + m_da.getLeadingZero(m_mv.getMonth(), 2); 
    }
    

}