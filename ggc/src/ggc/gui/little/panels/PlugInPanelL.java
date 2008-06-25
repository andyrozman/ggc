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
 *  Filename: ScheduleInfoPanelL
 *  Purpose:  This Panel contains information on your Schedule. Like your
 *            next check, when you see your doctor again, ...
 *
 *  Author:   andyrozman {andy@atech-software.com}
 */

package ggc.gui.little.panels;

import java.awt.GridLayout;

import javax.swing.JPanel;


public class PlugInPanelL extends JPanel
{
    /**
     * 
     */
    private static final long serialVersionUID = -6635563498789159187L;

    public PlugInPanelL()
    {
        super(); //I18nControl.getInstance().getMessage("SCHEDULE"));
        setLayout(new GridLayout(2, 1));
        init();
        //new GridLayout()
        //refreshInfo();
    }

    private void init()
    {
        add(new PlugInMeterPanelL());
//        	new JLabel(m_ic.getMessage("YOUR_NEXT_APPOINTMENT")+":"));
        add(new PlugInPumpPanelL());
//        add(new JLabel(m_ic.getMessage("WILL_BE_FOUND_HERE")+" NOT YET"));
    }

}
