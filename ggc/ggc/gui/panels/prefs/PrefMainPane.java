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
 *  Filename: prefGeneralPane.java
 *  Purpose:  General Options.
 *
 *  Author:   schultd
 */

package ggc.gui.panels.prefs;

import ggc.util.I18nControl;

import javax.swing.*;
import java.awt.*;

public class PrefMainPane extends AbstractPrefOptionsPanel 
{
    private JTextField fieldUserName;

    //private I18nControl m_ic = I18nControl.getInstance();

    private JComboBox langBox;

    public PrefMainPane() 
    {
        init();
    }

    private void init() 
    {
        JPanel a = new JPanel(new GridLayout(2, 2));


        a.add(new JLabel(m_ic.getMessage("HAHAHA") + ":"));


        Box c = Box.createHorizontalBox();
        c.add(a);

        add(c);
    }

    @Override
    public void saveProps() 
    {
    }
}
