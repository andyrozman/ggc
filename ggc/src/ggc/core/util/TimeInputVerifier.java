
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
 *  Filename: TimeInputVerifier
 *  Purpose:  Time Input verifier.      
 *
 *  Author:   stephen
 *  
 *  Created on 16.08.2002
 */


package ggc.core.util;


import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class TimeInputVerifier extends InputVerifier
{

    Border border = null;

    /**
     * @see javax.swing.InputVerifier#verify(JComponent)
     */
    @Override
    public boolean verify(JComponent input)
    {
        if (border == null)
            border = input.getBorder();

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        try 
	{
            String dateStr = ((JTextField)input).getText();
            /*Date date =*/ 
            df.parse(dateStr);
        } 
	catch (ParseException e) 
	{
            //System.out.println("verify failed");
            input.setBorder(new LineBorder(Color.red));
            return false;
        }
        //System.out.println("verify ok");
        input.setBorder(border);
        return true;
    }

}
