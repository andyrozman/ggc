
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
 *  Filename: BGInputVerifier
 *  Purpose:  Verifing BG input.      
 *
 *  Author:   stephan
 *  
 *  Created on 16.08.2002
 */


package ggc.util;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;


public class BGInputVerifier extends InputVerifier
{

    private Border border = null;
    private boolean nonNull;

    public BGInputVerifier(boolean nonNull)
    {
        super();

        this.nonNull = nonNull;
    }

    /**
     * @see javax.swing.InputVerifier#verify(JComponent)
     */
    @Override
    public boolean verify(JComponent input)
    {
        if (border == null)
            border = input.getBorder();

        try 
        {
            String bgStr = ((JTextField)input).getText();

            if (!nonNull) 
            {
                if (bgStr == null || bgStr.equals(""))
                    return true;
            }

            //float bg = 
            Float.parseFloat(bgStr);
        } 
        catch (NumberFormatException e) 
        {
            input.setBorder(new LineBorder(Color.red));
            return false;
        }

        input.setBorder(border);
        return true;
    }

}
