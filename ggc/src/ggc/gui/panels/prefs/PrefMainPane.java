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
 *  Filename: PrefMainPane
 *  Purpose:  General Options.
 *
 *  Author:   schultd
 */

package ggc.gui.panels.prefs;

import ggc.gui.dialogs.PropertiesDialog;
import ggc.core.util.I18nControl;

import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PrefMainPane extends AbstractPrefOptionsPanel 
{
//x    private JTextField fieldUserName;

    /**
     * 
     */
    private static final long serialVersionUID = -7014183829896704623L;
    private I18nControl m_ic = I18nControl.getInstance();

//x    private JComboBox langBox;

    public PrefMainPane(PropertiesDialog dialog) 
    {
	super(dialog);
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