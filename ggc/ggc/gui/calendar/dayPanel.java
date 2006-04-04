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
 *  Filename: dayPanel.java
 *  Purpose:  The button Panel to select a day in a month.
 *
 *  Author:   schultd
 */

package ggc.gui.calendar;


import ggc.datamodels.calendar.CalendarEvent;
import ggc.datamodels.calendar.CalendarListener;
import ggc.datamodels.calendar.CalendarModel;
import ggc.util.I18nControl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Date;

public class dayPanel extends JPanel implements CalendarListener
{

    private I18nControl m_ic = I18nControl.getInstance();    

    CalendarModel cMod;

    public String m_days[] = null;

    private GridLayout m_grid67 = null;
    private GridLayout m_grid77 = null;


    public dayPanel(CalendarModel cMod)
    {
        this.cMod = cMod;
        //setLayout(new GridLayout(6, 7));

        this.m_grid67 = new GridLayout(6, 7);
        this.m_grid77 = new GridLayout(7, 7);
        setLayout(this.m_grid67);

        String days[] = { 
            m_ic.getMessage("SU"),
            m_ic.getMessage("MO"),
            m_ic.getMessage("TU"),
            m_ic.getMessage("WE"),
            m_ic.getMessage("TH"),
            m_ic.getMessage("FR"),
            m_ic.getMessage("SA")
        };

        this.m_days = days;
        //setLayout(null);
        //setComponentOrientation(
        //        ComponentOrientation.RIGHT_TO_LEFT);
        doLayoutButtons();
        //this.setBackground(Color.blue);
        //this.setBounds(10, 40, 100,200);
    }

    private void doLayoutButtons()
    {
        int firstday = cMod.getFirstDayInMonth() - 1;
        int numdays = firstday + cMod.getNumDaysInMonth();
        int numdays_m = cMod.getNumDaysInMonth();
        int curday = firstday + cMod.getDay();

        removeAll();

//        System.out.println("first: " + firstday + " num: " + numdays);

        if (((numdays_m==31) && (firstday>4)) ||
            ((numdays_m==30) && (firstday>5)))
            setLayout(this.m_grid77); 
        else
            setLayout(this.m_grid67); 

        this.invalidate();

        for (int i=0; i<7; i++) 
        {
            JLabel label = new JLabel(this.m_days[i]);
            label.setHorizontalTextPosition(JLabel.CENTER);
            label.setHorizontalAlignment(JLabel.CENTER);
            this.add(label);
        }



        for (int i = 0; i < firstday; i++) 
        {
            add(new JLabel(""));
        }

        int lbl = 0;
        for (int i = firstday; i < curday - 1; i++) 
        {
            add(createButton(++lbl));
        }

        JButton button2 = new JButton(++lbl + "");
        button2.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        button2.setFont(new Font("Dialog", Font.BOLD, 12));
        add(button2);

        for (int i = curday; i < numdays; i++) {
            add(createButton(++lbl));
        }

        this.validate();
        this.repaint();

    }


    public void doLayoutButtons2(int firstday, int numdays)
    {
        //System.out.println("day" + cMod.getDay());
        //System.out.println("month" + cMod.getMonth());
        //System.out.println("year" + cMod.getYear());

        //int firstday = cMod.getFirstDayInMonth() - 1;
        //int numdays = firstday + cMod.getNumDaysInMonth();
        int curday = 1;

        removeAll();

        System.out.println("first: " + firstday + " num: " + numdays);

        boolean big = false;

        if (numdays==31) 
        {
            if ((firstday==5) || (firstday==6))
            {
                big = true;
            }
        }
        else if (numdays==30) 
        {
            if (firstday==6)
            {
                big = true;
            }
            //setLayout(new GridLayout(6,7));
        }
        else
        {
            //setLayout(new GridLayout(6,7));
        }


        if (big)
            setLayout(new GridLayout(7,7));
        else
            setLayout(new GridLayout(6,7));


/*
        if (((firstday>4) && (numdays==31)) ||
            ((firstday>5) && (numdays==30))) // 5
            setLayout(new GridLayout(7,7));
        else
            setLayout(new GridLayout(6,7));
*/
        this.invalidate();

        System.out.println("firdstday: " + firstday);



/*
        for (int i=0; i<8; i++) 
        {
            add(new JLabel(days[i]));
            //JLabel label = new JLabel(m_ic.getMessage("SU"));
            //label.setBounds(20 * i, 20, 20, 18);
            //this.add(label);
        }
*/


        add(new JLabel(m_ic.getMessage("SU")));
        add(new JLabel(m_ic.getMessage("MO")));
        add(new JLabel(m_ic.getMessage("TU")));
        add(new JLabel(m_ic.getMessage("WE")));
        add(new JLabel(m_ic.getMessage("TH")));
        add(new JLabel(m_ic.getMessage("FR")));
        add(new JLabel(m_ic.getMessage("SA")));
/*
        int firstday = cMod.getFirstDayInMonth() - 1;
        int numdays = firstday + cMod.getNumDaysInMonth();
        int curday = firstday + cMod.getDay();
*/

        for (int i = 0; i < firstday; i++) 
        {
            add(new JLabel(""));
        }

        int lbl = 0;
        for (int i = firstday; i < curday - 1; i++) 
        {
            add(createButton(++lbl));
        }

        JButton button2 = new JButton(++lbl + "");
        button2.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        button2.setFont(new Font("Dialog", Font.BOLD, 12));
        add(button2);

        for (int i = curday; i < numdays; i++) {
            add(createButton(++lbl));
        }

        this.validate();
        this.repaint();

    }



    private JButton createButton(final int j)
    {
        JButton button = new JButton(j + "");
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setPreferredSize(new Dimension(20, 18));
        button.setFont(new Font("Dialog", Font.PLAIN, 12));
        button.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                cMod.setDay(j);
            }
        });
        return button;
    }

    public void dateHasChanged(CalendarEvent e)
    {
        //if(e.getEvent() > CalendarEvent.DAY_CHANGED)
        doLayoutButtons();
    }

    public static void main(String args[])
    {
        JDialog dialog = new JDialog();
        dialog.setBounds(20, 20, 300, 200);
        dialog.setLayout(new BorderLayout());

        CalendarModel cModel = new CalendarModel(new Date(), null);

        dayPanel dPanel = new dayPanel(cModel);
        dialog.add(dPanel, BorderLayout.CENTER);

        dPanel.doLayoutButtons2(Integer.parseInt(args[0]), Integer.parseInt(args[1]));

        dialog.setVisible(true);


    }


}
