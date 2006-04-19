/*
 * Created on 16.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */

package ggc.util;


import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author stephan
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public class DateInputVerifier extends InputVerifier
{

    Border border = null;

    /**
     * @see javax.swing.InputVerifier#verify(JComponent)
     */
    public boolean verify(JComponent input)
    {
        if (border == null)
            border = input.getBorder();

        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        try 
	{
            String dateStr = ((JTextField)input).getText();
            Date date = df.parse(dateStr);
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
