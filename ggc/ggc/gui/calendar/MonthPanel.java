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
 *  Filename: monthPanel.java
 *  Purpose:  ComboBox to select a month.
 *
 *  Author:   schultd
 */

package ggc.gui.calendar;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import ggc.data.calendar.CalendarEvent;
import ggc.data.calendar.CalendarListener;
import ggc.data.calendar.CalendarModel;
import ggc.util.DataAccess;
import ggc.util.I18nControl;


public class MonthPanel extends JPanel implements CalendarListener
{
    private I18nControl m_ic = I18nControl.getInstance();    
    private DataAccess m_da = DataAccess.getInstance();

    /*
    private String[] months = {
            m_ic.getMessage("JANUARY"), 
            m_ic.getMessage("FEBRUARY"), 
            m_ic.getMessage("MARCH"), 
            m_ic.getMessage("APRIL"), 
            m_ic.getMessage("MAY"), 
            m_ic.getMessage("JUNE"), 
            m_ic.getMessage("JULY"), 
            m_ic.getMessage("AUGUST"), 
            m_ic.getMessage("SEPTEMBER"), 
            m_ic.getMessage("OCTOBER"), 
            m_ic.getMessage("NOVEMBER"), 
            m_ic.getMessage("DECEMBER") };
            */

    private JComboBox monthCombo;

    public MonthPanel(final CalendarModel cMod)
    {
        monthCombo = new JComboBox(m_da.getMonthsArray());
        monthCombo.setSelectedIndex(cMod.getMonth());
        add(monthCombo);

        monthCombo.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                cMod.setMonth(monthCombo.getSelectedIndex());
            }
        });
    }

    public void dateHasChanged(CalendarEvent e)
    {
        if (e.getEvent() > CalendarEvent.MONTH_CHANGED)
            monthCombo.setSelectedIndex(e.getNewMonth());
    }
}
