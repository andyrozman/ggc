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
 *  Filename: OtherInfoPanel
 *  Purpose:  This Panel is container for Schedule and Stocks
 *
 *  Author:   andyrozman  {andy@atech-software.com}
 */

package ggc.gui.panels.info;

import ggc.core.util.I18nControl;

import java.awt.GridLayout;


public class OtherInfoPanel extends AbstractInfoPanel
{
    public OtherInfoPanel()
    {
        super(I18nControl.getInstance().getMessage("OTHER_INFO"));
        setLayout(new GridLayout(2, 0));
        init();
        //refreshInfo();
    }

    private void init()
    {
        add(new ScheduleInfoPanel());
        add(new StocksInfoPanel());

        //add(new JLabel(m_ic.getMessage("YOUR_NEXT_APPOINTMENT")+":"));
        //add(new JLabel(m_ic.getMessage("WILL_BE_FOUND_HERE")+" NOT YET"));
    }

    @Override
    public void refreshInfo()
    {
    }
}
