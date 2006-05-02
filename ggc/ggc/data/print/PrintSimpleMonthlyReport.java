package ggc.data.print;

import java.io.File;
import java.io.FileOutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import ggc.data.MonthlyValues;
import ggc.util.DataAccess;
import ggc.util.I18nControl;

public class PrintSimpleMonthlyReport
{

    public MonthlyValues m_mv = null;
    private DataAccess m_da = DataAccess.getInstance();
    private I18nControl ic = I18nControl.getInstance();
    long name = 0L;

    public PrintSimpleMonthlyReport(MonthlyValues mv)
    {
	m_mv = mv;
	name = System.currentTimeMillis();
	createDocument();
//        test();
    }

    public String getName()
    {
	return name + ".pdf";
    }


    public void createDocument()
    {
	System.out.println("document.add(BigTable)");
	// step1

	File fl = new File("../data/temp/" + name + ".pdf");
	Document document = new Document(PageSize.A4, 20, 20, 20, 20);
	try 
	{
		// step2
		PdfWriter writer = PdfWriter.getInstance(document,
				new FileOutputStream(fl.getAbsoluteFile()));
		// step3
		document.open();
		// step4
		String[] bogusData = {"", "0.0", "0.0", "0.0", "0.0",
				"0.0", "0.0", "0.0", "0.0", "Comm."
				 };
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
		//datatable.getDefaultCell().setPadding(3);
		//datatable.getDefaultCell().setBorderWidth(2);
		datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

		PdfPCell pd = new PdfPCell();
		pd.setPhrase(new Phrase(ic.getMessage("DATE")));
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
		p1.setPhrase(new Phrase(ic.getMessage("BREAKFAST")));
		p1.setHorizontalAlignment(Element.ALIGN_CENTER);
		p1.setBorderWidth(1);
		p1.setColspan(3);

		meal_b.addCell(p1);
        meal_b.addCell(ic.getMessage("BG"));
		meal_b.addCell(ic.getMessage("INS_SHORT"));
		meal_b.addCell(ic.getMessage("CH"));

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
		p2.setPhrase(new Phrase(ic.getMessage("LUNCH")));
		p2.setHorizontalAlignment(Element.ALIGN_CENTER);
		p2.setBorderWidth(1);
		p2.setColspan(3);

		meal_l.addCell(p2);
        meal_l.addCell(ic.getMessage("BG"));
		meal_l.addCell(ic.getMessage("INS_SHORT"));
		meal_l.addCell(ic.getMessage("CH"));

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
		p3.setPhrase(new Phrase(ic.getMessage("DINNER")));
		p3.setHorizontalAlignment(Element.ALIGN_CENTER);
		p3.setBorderWidth(1);
		p3.setColspan(3);

		meal_d.addCell(p3);
        meal_d.addCell(ic.getMessage("BG"));
		meal_d.addCell(ic.getMessage("INS_SHORT"));
		meal_d.addCell(ic.getMessage("CH"));

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
		p4.setPhrase(new Phrase(ic.getMessage("NIGHT")));
		p4.setHorizontalAlignment(Element.ALIGN_CENTER);
		p4.setColspan(3);

		meal_n.addCell(p4);
        meal_n.addCell(ic.getMessage("BG"));
		meal_n.addCell(ic.getMessage("INS_SHORT"));
		meal_n.addCell(ic.getMessage("CH"));

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

		    datatable.addCell(i+"." + this.m_mv.getMonth());

		    System.out.println(i);

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



	    } 
	    catch (Exception de) 
	    {
		de.printStackTrace();
	    }
	    // step5

	    document.close();


    }


    public void test()
    {

	System.out.println("document.add(BigTable)");
        // step1

	File fl = new File("../data/temp/" + "HelloWorld2.pdf");
        Document document = new Document(PageSize.A4, 10, 10, 10, 10);
        try 
        {
                // step2
                PdfWriter writer = PdfWriter.getInstance(document,
                                new FileOutputStream(fl.getAbsoluteFile()));
                // step3
                document.open();
                // step4
                String[] bogusData = {"", "0.0", "0.0", "0.0", "0.0",
                                "0.0", "0.0", "0.0", "0.0", "Comm."
                                 };
                int NumColumns = 10;

                PdfPTable datatable = new PdfPTable(NumColumns);
                int headerwidths[] = { 4, 
                                       4, 4, 4, 4, 
                                       4, 4, 4, 4, 
                                       16 }; // percentage
                datatable.setWidths(headerwidths);
                datatable.setWidthPercentage(100); // percentage
                //datatable.getDefaultCell().setPadding(3);
                //datatable.getDefaultCell().setBorderWidth(2);
                datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                
                datatable.addCell("Date");

                //PdfPCell pp = new PdfPCell();

/*

                PdfPCell p1 = new PdfPCell();


                //p1.addElement(

                PdfPCell cel_up[] = new PdfPCell[1];
                cel_up[0] = new PdfPCell(new Phrase("Insulin"));
                cel_up[0].setColspan(4);

                PdfPRow p_up = new PdfPRow(cel_up);
                //p_up.setColspan(4);


                PdfPCell cells[] = new PdfPCell[4];

                cells[0] = new PdfPCell(new Phrase("Z"));
                cells[1] = new PdfPCell(new Phrase("K"));
                cells[2] = new PdfPCell(new Phrase("V"));
                cells[3] = new PdfPCell(new Phrase("N"));


                PdfPRow p_down = new PdfPRow(cells);
                //p_down.addCell("Z");
                //p_down.addCell("K");
                //p_down.addCell("V");
                //p_down.addCell("N");

                p1.addElement(p_up);
                p1.addElement(p_down);

  */
                //p1.

                
                //datatable.addCell(p1);

                //datatable.addCell(p1);

                //datatable.a.addCell(p_up);
                //datatable.addCell(p_down);



                PdfPTable ins = new PdfPTable(4);
                ins.setHorizontalAlignment(Element.ALIGN_CENTER);
                //ins.getRow(1).s
                //ins.setB
                
                //ins.getDefaultCell().setBorderWidth(2);
                ins.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                
                PdfPCell p1 = new PdfPCell();
                p1.setPhrase(new Phrase("Insulin"));
                p1.setHorizontalAlignment(Element.ALIGN_CENTER);
                //pp.setHorizontalAlignment(Element.ALIGN_CENTER);
                p1.setColspan(4);

                ins.addCell(p1);
                ins.addCell("Z");
                ins.addCell("K");
                ins.addCell("V");
                ins.addCell("N");


                PdfPCell m = new PdfPCell(ins);
                m.setColspan(4);
                





                PdfPTable bg = new PdfPTable(4);
                bg.setHorizontalAlignment(Element.ALIGN_CENTER);
                
                PdfPCell p2 = new PdfPCell();
                p2.setPhrase(new Phrase("Blood Glucose"));
                p2.setHorizontalAlignment(Element.ALIGN_CENTER);
                p2.setColspan(4);

                bg.addCell(p2);
                bg.addCell("Z");
                bg.addCell("K");
                bg.addCell("V");
                bg.addCell("N");


                PdfPCell m2 = new PdfPCell(bg);
                //m.
                m2.setColspan(4);




                //pp.s

                //import com.lowagie.text.pdf.PdfPRow
                
                //PdfPCell c = new PdfPCell


                //datatable.addCell("Z");
                //datatable.addCell("K");
                //datatable.addCell("V");
                //datatable.addCell("N");

//                PdfPCell p = new PdfPCell();

                
                
                //p.setColspan(


//                datatable.addCell(ins);
                datatable.addCell(m);
                datatable.addCell(m2);

                //datatable.addCell("Z");
                //datatable.addCell("K");
                //datatable.addCell("V");
                //datatable.addCell("N");
                datatable.addCell("Comment");

                datatable.setHeaderRows(1); // this is the end of the table header

                datatable.getDefaultCell().setBorderWidth(1);
                for (int i = 1; i < 32; i++) 
                {
                    if (i%2==1) 
                    {
                        datatable.getDefaultCell().setGrayFill(0.9f);
                    }
                    
                    datatable.addCell(i+".2");

                    for (int x=1; x < NumColumns; x++) 
                    {
                        datatable.addCell(bogusData[x]);
                    }
                    
                    if (i%2==1) 
                    {
                        datatable.getDefaultCell().setGrayFill(0.0f);
                    }
                }
                
                document.add(datatable);

                    

            } 
            catch (Exception de) 
            {
                de.printStackTrace();
            }
            // step5

            document.close();

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


}




