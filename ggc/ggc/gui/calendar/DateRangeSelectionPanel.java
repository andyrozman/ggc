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
 *  Filename: DateRangeSelectionPanel.java
 *  Purpose:  Panel for selecting a date range.
 *
 *  Author:   schutld
 */

package ggc.gui.calendar;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ggc.util.DataAccess;
import ggc.util.I18nControl;


public class DateRangeSelectionPanel extends JPanel implements ChangeListener
{
    //private JTextField fieldStartDate;
    //private JTextField fieldEndDate;
    //private Date endDate;
    //private Date startDate;

    private I18nControl m_ic = I18nControl.getInstance();    

    private JSpinner spinnerEnd;
    private JSpinner spinnerStart;

    private GregorianCalendar gc_end = null;
    private GregorianCalendar gc_start = null;


    private SpinnerDateModel endSpinnerDateModel = null;
    private SpinnerDateModel startSpinnerDateModel = null;

    private int iRadioGroupState = 0;

    public static final int ONE_WEEK = 0;
    public static final int ONE_MONTH = 1;
    public static final int THREE_MONTHS = 2;
    public static final int CUSTOM = 3;

    //private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    public DateRangeSelectionPanel()
    {
        this(new GregorianCalendar(), null);
        iRadioGroupState = ONE_MONTH;
        calcStartDate();
        init();
    }

    public DateRangeSelectionPanel(int flag)
    {
        this(new GregorianCalendar(), null);
        iRadioGroupState = flag;
        calcStartDate();
        init();
    }

    public DateRangeSelectionPanel(GregorianCalendar endDate)
    {
        this(new GregorianCalendar(), null);
        iRadioGroupState = ONE_MONTH;
        calcStartDate();
        init();
    }

    public DateRangeSelectionPanel(GregorianCalendar endDate, int flag)
    {
        this(endDate, null);
        //endSpinnerDateModel.setValue(endDate);
        iRadioGroupState = flag;
        calcStartDate();
        init();
    }

/*
    public DateRangeSelectionPanel(Date endDate, Date startDate)
    {
        endSpinnerDateModel.setValue(endDate);
        startSpinnerDateModel.setValue(startDate);
        iRadioGroupState = CUSTOM;
    }
    */


    public DateRangeSelectionPanel(GregorianCalendar endDate, GregorianCalendar startDate)
    {

	endSpinnerDateModel = new SpinnerDateModel();
	startSpinnerDateModel = new SpinnerDateModel();

        if (endDate!=null) 
        {
            endSpinnerDateModel.setValue(endDate.getTime());
            this.gc_end = endDate;
        }

        if (startDate!=null) 
        {
            startSpinnerDateModel.setValue(startDate.getTime());
            this.gc_start = startDate;
        }
        iRadioGroupState = CUSTOM;
    }



    private void init()
    {
	JPanel a = new JPanel();
	a.setLayout(new GridLayout(0,1));

	//Box a = Box.createVerticalBox();
	a.add(new JLabel(m_ic.getMessage("ENDING_DATE")+":"));
	a.add(spinnerEnd = new JSpinner(endSpinnerDateModel));
	((JSpinner.DateEditor)spinnerEnd.getEditor()).getFormat().applyPattern("dd.MM.yyyy");

	a.add(new JLabel(m_ic.getMessage("STARTING_DATE")+":"));
	a.add(spinnerStart = new JSpinner(startSpinnerDateModel));
	((JSpinner.DateEditor)spinnerStart.getEditor()).getFormat().applyPattern("dd.MM.yyyy");

	spinnerEnd.addChangeListener(this);
	spinnerStart.addChangeListener(this); 


	/*
        Box a = Box.createVerticalBox();
        a.add(new JLabel(m_ic.getMessage("ENDING_DATE")+":"));
        a.add(spinnerEnd = new JSpinner(endSpinnerDateModel));
        ((JSpinner.DateEditor)spinnerEnd.getEditor()).getFormat().applyPattern("dd.MM.yyyy");

        a.add(new JLabel(m_ic.getMessage("STARTING_DATE")+":"));
        a.add(spinnerStart = new JSpinner(startSpinnerDateModel));
        ((JSpinner.DateEditor)spinnerStart.getEditor()).getFormat().applyPattern("dd.MM.yyyy");

        spinnerEnd.addChangeListener(this);
	spinnerStart.addChangeListener(this); 
	*/

	/*new ChangeListener()
        {
            public void stateChanged(ChangeEvent e)
            {
                calcDateAndUpdateFields();
            }
        }); */

	/*new ChangeListener()
        {
            public void stateChanged(ChangeEvent e)
            {
                calcDateAndUpdateFields();
            }
        }); */


        JRadioButton rbOneWeek = new JRadioButton("  " + m_ic.getMessage("1_WEEK"), iRadioGroupState == ONE_WEEK);
	rbOneWeek.setIconTextGap(8);
	//rbOneWeek.se

        JRadioButton rbOneMonth = new JRadioButton("  " + m_ic.getMessage("1_MONTH"), iRadioGroupState == ONE_MONTH);
        JRadioButton rbThreeMonths = new JRadioButton("  " + m_ic.getMessage("3_MONTHS"), iRadioGroupState == THREE_MONTHS);
        JRadioButton rbCustom = new JRadioButton("  " + m_ic.getMessage("CUSTOM"), iRadioGroupState == CUSTOM);

        if (iRadioGroupState != CUSTOM)
            spinnerStart.setEnabled(false);

        ButtonGroup group = new ButtonGroup();
        group.add(rbOneWeek);
        group.add(rbOneMonth);
        group.add(rbThreeMonths);
        group.add(rbCustom);

        rbOneWeek.addActionListener(new RadioListener(ONE_WEEK));
        rbOneMonth.addActionListener(new RadioListener(ONE_MONTH));
        rbThreeMonths.addActionListener(new RadioListener(THREE_MONTHS));
        rbCustom.addActionListener(new RadioListener(CUSTOM));

        Box b = Box.createVerticalBox();
        b.add(rbOneWeek);
        b.add(rbOneMonth);
        b.add(rbThreeMonths);
        b.add(rbCustom);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("DATE_RANGE_SELECTOR")));

	JPanel c = new JPanel();
	c.setLayout(new GridLayout());
	c.add(new JLabel("  "));

        add(a, BorderLayout.WEST);
	add(c, BorderLayout.CENTER);
        add(b, BorderLayout.EAST);
    }

    private void calcDateAndUpdateFields()
    {
        calcStartDate();
    }

    private void calcStartDate()
    {
        if (iRadioGroupState == 3)
            return;

        //if (gc_start==null) 
	gc_start = null;
        gc_start = (GregorianCalendar)gc_end.clone();

        switch (iRadioGroupState) 
	{
            case ONE_WEEK:
                gc_start.add(Calendar.WEEK_OF_YEAR, -1);
                break;
            case THREE_MONTHS:
                gc_start.add(Calendar.MONTH, -3);
		break;
	    default:
	    case ONE_MONTH:
		gc_start.add(Calendar.MONTH, -1);
		break;

	}

	//System.out.println(gc_start);
        startSpinnerDateModel.setValue(gc_start.getTime());
    }

    private class RadioListener extends AbstractAction
    {
        private int stat = 1;

        public RadioListener(int flag)
        {
            this.stat = flag;
        }

        public void actionPerformed(ActionEvent e)
        {
            iRadioGroupState = stat;

            if (stat == CUSTOM)
                spinnerStart.setEnabled(true);
            else
                spinnerStart.setEnabled(false);

            calcStartDate();
        }
    }

    public Date getEndDate()
    {
        return endSpinnerDateModel.getDate();
    }

    public Date getStartDate()
    {
        return startSpinnerDateModel.getDate();
    }

    public GregorianCalendar getEndCalendar()
    {
        return DataAccess.getInstance().getGregorianCalendar(endSpinnerDateModel.getDate());
    }

    public GregorianCalendar getStartCalendar()
    {
        return DataAccess.getInstance().getGregorianCalendar(startSpinnerDateModel.getDate());
    }


    // ChangeListener
    public void stateChanged(ChangeEvent e)
    {
	calcDateAndUpdateFields();
    }


}
