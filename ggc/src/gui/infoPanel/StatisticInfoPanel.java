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
 *  Filename: StatisticInfoPanel.java
 *  Purpose:  Contains basic statistics for the last week or month.
 *            Something like avgBG last week, Meter readings, ...
 *
 *  Author:   schultd
 */

package gui.infoPanel;


import javax.swing.*;


public class StatisticInfoPanel extends AbstractInfoPanel
{
    public StatisticInfoPanel()
    {
        super("Statistics for last week");
        init();
        refreshInfo();
    }

    private void init()
    {
        add(new JLabel("coming soon... ;-)"));
    }

    public void refreshInfo()
    {
    }
}