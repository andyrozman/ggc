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
import ggc.core.data.DayValuesData;

import java.util.Iterator;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;

public class PrintFoodMenuBase extends PrintAbstract
{

    
    
    public PrintFoodMenuBase(DayValuesData mv)
    {
        super(mv);
    }

    
    
    
    public Paragraph getTitle()
    {
        Paragraph p = new Paragraph();

        Font f = new Font(this.base_times , 16, Font.BOLD); 
        
        p.setAlignment(Element.ALIGN_CENTER);
        //p.add(new Paragraph("", f));
//        p.add(new Paragraph(ic.getMessage("EXTENDED_MONTHLY_REPORT") + " - " + m_da.getMonthsArray()[m_mv.getMonth()-1] + " " + m_mv.getYear(), f));
        //p.add(new Paragraph("May 2006"));
        p.add(new Paragraph(ic.getMessage("FOR") + " " + m_da.getSettings().getUserName(), new Font(Font.TIMES_ROMAN, 12, Font.ITALIC)));
        p.add(new Paragraph("", f));
        p.add(new Paragraph("", f));
        //p.add(new Paragraph("", f));

        return p;
    }
    
    
    

    
    
    
    

    



    @Override
    public void fillDocumentBody(Document document)
    {
        // TODO Auto-generated method stub
        
        System.out.println("Jedilnik");
        
        Iterator<DailyValues> it = this.m_data.iterator();
        
        int count = 0;
        
        while (it.hasNext())
        {
            DailyValues dv = it.next();
            System.out.println(dv.getDateAsString());
            count++;
        }
        
        System.out.println("Elements all: " + this.m_data.size() + " in iterator: " + count);
        
        
        
    }




    @Override
    public String getFileNameBase()
    {
        return "foodmenu_base_";
    }




    @Override
    public String getFileNameRange()
    {
        return this.m_data.getRangeBeginObject().getDateString() + "-" + this.m_data.getRangeEndObject().getDateString(); 
    }

}




