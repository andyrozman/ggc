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
 *  Filename: ScheduleInfoPanel.java
 *  Purpose:  This Panel contains information on your Schedule. Like your
 *            next check, when you see your doctor again, ...
 *
 *  Author:   schultd
 */

package ggc.gui.panels.info;

import ggc.util.I18nControl;

import javax.swing.*;

import java.awt.*;


public class StocksInfoPanel extends AbstractInfoPanel
{
    public StocksInfoPanel()
    {
        super(I18nControl.getInstance().getMessage("STOCK"));
        //setLayout(new GridLayout(5, 3));
        setLayout(new GridLayout(0,2));
        init();
        refreshInfo();
    }

    private void init()
    {
        //JLabel left_margin = new JLabel

        add(new JLabel(m_ic.getMessage("STOCK_DATA_HERE")+":"));
        add(new JLabel(m_ic.getMessage("STO_WILL_BE_FOUND_HERE")+ "..." + m_ic.getMessage("NOT_YET")));
/*        add(new JLabel("label"));
        add(new JLabel("label"));
        add(new JLabel("label"));
        add(new JLabel("label"));
        add(new JLabel("label")); */
    }

    @Override
    public void refreshInfo()
    {
    }
}
