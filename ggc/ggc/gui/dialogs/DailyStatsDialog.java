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
 *  Filename: DailyStatsFrame.java
 *  Purpose:  Enter and view all data for one day.
 *
 *  Author:   schultd
 *      Changes by andy 
 *
 */

package ggc.gui.dialogs;

import ggc.core.data.DailyStatsTableModel;
import ggc.core.data.DailyValues;
import ggc.core.data.DailyValuesRow;
import ggc.core.data.calendar.CalendarEvent;
import ggc.core.data.calendar.CalendarListener;
import ggc.core.util.DataAccess;
import ggc.core.util.I18nControl;
import ggc.db.GGCDb;
import ggc.gui.MainFrame;
import ggc.gui.calendar.CalendarPane;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import com.atech.help.HelpCapable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class DailyStatsDialog extends JDialog implements ActionListener, HelpCapable
{


    private static Log log = LogFactory.getLog(DailyStatsDialog.class); 
    

    private I18nControl m_ic = I18nControl.getInstance();    
    private DataAccess m_da = null; //DataAccess.getInstance();

    DailyStatsTableModel model = null;
    JScrollPane resultsPane;
    
    JTable table;

    public boolean save_needed = false;

    CalendarPane calPane;

    JLabel sumIns1, sumIns2, sumIns;
    JLabel avgIns1, avgIns2, avgIns;
    JLabel doseIns1, doseIns2, doseIns;
    JLabel sumBE, avgBE, meals;
    JLabel avgBG, highestBG, lowestBG;
    JLabel readings, stdDev;

    JLabel lblDate;
    JButton saveButton;
    JButton help_button;
    DailyValues dayData;


    GGCDb m_db = null;

    public DailyStatsDialog(DataAccess da)
    {
        super(da.getMainParent(), "DailyStatsDialog", false);
        setTitle(m_ic.getMessage("DAILYSTATSFRAME"));

        this.m_da = da;
        this.m_db = m_da.getDb();
        
    	setSize(640, 500);
    	m_da.centerJDialog(this, m_da.getMainParent());
    	
        init();
    }


    public DailyStatsTableModel getTableModel()
    {
        return model;
    }

    protected void close()
    {
        DataAccess.getInstance().loadDailySettings(new GregorianCalendar(), true);
       	MainFrame mf = DataAccess.getInstance().getParent();
        mf.informationPanel.refreshPanels();
        this.dispose();
    }

    private void init()
    {
        //setBounds(150, 150, 550, 500);

        //Panel for Insulin Stats
        JPanel InsPanel = new JPanel(new GridLayout(3, 6));
        InsPanel.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("INSULIN")+":"));

        InsPanel.add(new JLabel(m_da.getSettings().getIns1Abbr() + ":"));
        InsPanel.add(sumIns1 = new JLabel());
        InsPanel.add(new JLabel(m_ic.getMessage("AVG")+" " + m_da.getSettings().getIns1Abbr() + ":"));
        InsPanel.add(avgIns1 = new JLabel());
        InsPanel.add(new JLabel(m_ic.getMessage("DOSE")+" " + m_da.getSettings().getIns1Abbr() + ":"));
        InsPanel.add(doseIns1 = new JLabel());

        InsPanel.add(new JLabel(m_da.getSettings().getIns2Abbr() + ":"));
        InsPanel.add(sumIns2 = new JLabel());
        InsPanel.add(new JLabel(m_ic.getMessage("AVG")+" " + m_da.getSettings().getIns2Abbr() + ":"));
        InsPanel.add(avgIns2 = new JLabel());
        InsPanel.add(new JLabel(m_ic.getMessage("DOSE")+" " + m_da.getSettings().getIns2Abbr() + ":"));
        InsPanel.add(doseIns2 = new JLabel());

        InsPanel.add(new JLabel(m_ic.getMessage("TOTAL")+":"));
        InsPanel.add(sumIns = new JLabel());
        InsPanel.add(new JLabel(m_ic.getMessage("AVG_INS")+":"));
        InsPanel.add(avgIns = new JLabel());
        InsPanel.add(new JLabel(m_ic.getMessage("DOSE_INS")+":"));
        InsPanel.add(doseIns = new JLabel());

        //Panel for BU Stats
        JPanel BUPanel = new JPanel(new GridLayout(1, 6));
        BUPanel.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("BREAD_UNITS")+":"));

        BUPanel.add(new JLabel(m_ic.getMessage("SUM")+":"));
        BUPanel.add(sumBE = new JLabel());
        BUPanel.add(new JLabel(m_ic.getMessage("AVG")+":"));
        BUPanel.add(avgBE = new JLabel());
        BUPanel.add(new JLabel(m_ic.getMessage("MEALS")+":"));
        BUPanel.add(meals = new JLabel());

        //Panel for BG Stats
        JPanel BGPanel = new JPanel(new GridLayout(0, 6));
        BGPanel.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("BLOOD_GLUCOSE")+":"));

        BGPanel.add(new JLabel(m_ic.getMessage("AVG_BG")+":"));
        BGPanel.add(avgBG = new JLabel());
        BGPanel.add(new JLabel(m_ic.getMessage("HIGHEST")+":"));
        BGPanel.add(highestBG = new JLabel());
        BGPanel.add(new JLabel(m_ic.getMessage("READINGS")+":"));
        BGPanel.add(readings = new JLabel());
        BGPanel.add(new JLabel(m_ic.getMessage("STD_DEV")+":"));
        BGPanel.add(stdDev = new JLabel());
        BGPanel.add(new JLabel(m_ic.getMessage("LOWEST")+":"));
        BGPanel.add(lowestBG = new JLabel());

        Box dayStats = Box.createVerticalBox();
        dayStats.add(InsPanel);
        dayStats.add(BUPanel);
        dayStats.add(BGPanel);

        JPanel dayHeader = new JPanel();
        dayHeader.setLayout(new BorderLayout());

        JPanel dayCalendar = new JPanel();
        dayCalendar.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("DATE")+":"));

        calPane = new CalendarPane();
        calPane.addCalendarListener(new CalendarListener()
        {
            public void dateHasChanged(CalendarEvent e)
            {
                //System.out.println("dateHasChanged");
                dayData = m_da.getDb().getDayStats(e.getNewCalendar());
                
                model.setDailyValues(dayData);
                //setDailyValues(dayData);
                //saveButton.setEnabled(false);
                updateLabels();
                getTableModel().fireTableChanged(null);
//x                dailyGraphWindow.setDailyValues(dayData);

                //DailyGraphFrame.setDailyValues(dayData);
            }
        });
        //calPane.setBounds(10, 10, 300, 200);
        dayCalendar.add(calPane);


        dayHeader.add(dayCalendar, BorderLayout.WEST);
        dayHeader.add(dayStats, BorderLayout.CENTER);

        dayData = DataAccess.getInstance().getDayStats(new GregorianCalendar());

        //dbH.getDayStats(new Grenew Date(System.currentTimeMillis()));
        //dailyGraphWindow.setDailyValues(dayData);
        //DailyGraphFrame.setDailyValues(dayData);

        model = new DailyStatsTableModel(dayData);
        model.addTableModelListener(new TableModelListener()
        {
            public void tableChanged(TableModelEvent e)
            {
                //dailyGraphWindow.repaint();
                //DailyGraphFrame.repaint(); //.redraw();
                updateLabels();
                //saveButton.setEnabled(true);
            }
        });
        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        resultsPane = new JScrollPane(table);


	Dimension dim = new Dimension(95, 25);
        
	JPanel gg = new JPanel();
	gg.setLayout(new BorderLayout());
	//gg.setPreferredSize(dim);


	JPanel EntryBox1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
	

        JButton tButton = new JButton("  " + m_ic.getMessage("GRAPH"));
        tButton.setPreferredSize(dim);
        tButton.setIcon(m_da.getImageIcon_22x22("course.png", this));
	//tButton.setMaximumSize(dim);
	tButton.setActionCommand("show_daily_graph");
	tButton.addActionListener(this);

	EntryBox1.add(tButton);

	
        help_button = m_da.createHelpButtonBySize(95, 25, this); 

        EntryBox1.add(help_button);
	
	gg.add(EntryBox1, BorderLayout.WEST);
	
	
	JPanel EntryBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 1, 2));
        //Dimension dim = new Dimension(120, 20);

        JButton addButton = new JButton("  " + m_ic.getMessage("ADD"));
        addButton.setPreferredSize(dim);
        addButton.setIcon(m_da.getImageIcon_22x22("table_add.png", this)); 
	addButton.setActionCommand("add_row");
	addButton.addActionListener(this);
	EntryBox.add(addButton);

	JButton editButton = new JButton("  " + m_ic.getMessage("EDIT"));
	editButton.setPreferredSize(dim);
        editButton.setIcon(m_da.getImageIcon_22x22("table_edit.png", this)); 
	editButton.setActionCommand("edit_row");
	editButton.addActionListener(this);
        EntryBox.add(editButton);

        JButton delButton = new JButton("  " + m_ic.getMessage("DELETE"));
        delButton.setPreferredSize(dim);
        delButton.setIcon(m_da.getImageIcon_22x22("table_delete.png", this)); 
        delButton.setActionCommand("delete_row");
        delButton.addActionListener(this);
        EntryBox.add(delButton);

        saveButton = new JButton("  " + m_ic.getMessage("CLOSE"));
        saveButton.setPreferredSize(dim);
        saveButton.setIcon(m_da.getImageIcon_22x22("cancel.png", this)); 
	saveButton.setActionCommand("close");
	saveButton.addActionListener(this);
        EntryBox.add(saveButton);

	gg.add(EntryBox, BorderLayout.EAST);

        getContentPane().add(resultsPane, BorderLayout.CENTER);
        getContentPane().add(dayHeader, BorderLayout.NORTH);
        //getContentPane().add(EntryBox, BorderLayout.SOUTH);
	getContentPane().add(gg, BorderLayout.SOUTH);

        updateLabels();
        
        m_da.enableHelp(this);

        setVisible(true);
    }

    
    
    public void updateLabels()
    {
        if (dayData == null)
            return;

        DecimalFormat df = new DecimalFormat("#0.0");
        sumIns1.setText(df.format(dayData.getSumIns1()));
        sumIns2.setText(df.format(dayData.getSumIns2()));
        sumIns.setText(df.format(dayData.getSumIns()));

        avgIns1.setText(df.format(dayData.getAvgIns1()));
        avgIns2.setText(df.format(dayData.getAvgIns2()));
        avgIns.setText(df.format(dayData.getAvgIns()));

        doseIns1.setText(dayData.getIns1Count() + "");
        doseIns2.setText(dayData.getIns2Count() + "");
        doseIns.setText(dayData.getInsCount() + "");

        sumBE.setText(df.format(dayData.getSumCH()));
        avgBE.setText(df.format(dayData.getAvgCH()));
        meals.setText(dayData.getCHCount() + "");

        avgBG.setText(df.format(dayData.getAvgBG()));
        stdDev.setText(df.format(dayData.getStdDev()));
        highestBG.setText(df.format(dayData.getHighestBG()));
        lowestBG.setText(df.format(dayData.getLowestBG()));
        readings.setText(dayData.getBGCount() + "");
    }

/*
    public void processWindowEvent(WindowEvent e)
    {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            this.dispose();
        }
        super.processWindowEvent(e);
    }
      */

    public void actionPerformed(ActionEvent e)
    {

    	String command = e.getActionCommand();
    
    	if (command.equals("add_row"))
    	{
    	    SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy");
    
            DailyRowDialog aRF = new DailyRowDialog(dayData, sf.format(calPane.getSelectedDate()), this);

            if (aRF.actionSuccesful()) 
            {
                m_db.saveDayStats(dayData);
                this.model.fireTableChanged(null);
            }
    	}
    	else if (command.equals("edit_row"))
    	{
    	    //SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy");
    	    //EditRowFrame eRF = EditRowFrame.getInstance(model, dayData, sf.format(calPane.getSelectedDate()));
    	    //aRF.show();
    
            if (table.getSelectedRow()==-1) 
            {
                JOptionPane.showMessageDialog(this, m_ic.getMessage("SELECT_ROW_FIRST"), m_ic.getMessage("ERROR"), JOptionPane.ERROR_MESSAGE);
                return;
            }

            DailyValuesRow dvr = dayData.getRowAt(table.getSelectedRow());

            DailyRowDialog aRF = new DailyRowDialog(dvr, this);

            if (aRF.actionSuccesful()) 
            {
                m_db.saveDayStats(dayData);
                this.model.fireTableChanged(null);
            }
    
    	}
    	else if (command.equals("delete_row"))
    	{
            if (table.getSelectedRow()==-1) 
            {
                JOptionPane.showMessageDialog(this, m_ic.getMessage("SELECT_ROW_FIRST"), m_ic.getMessage("ERROR"), JOptionPane.ERROR_MESSAGE);
                return;
            }

            

            int option_selected = JOptionPane.showOptionDialog(this, 
                                         m_ic.getMessage("ARE_YOU_SURE_DELETE_ROW"),
                                         m_ic.getMessage("QUESTION"),
                                         JOptionPane.YES_NO_OPTION,
                                         JOptionPane.QUESTION_MESSAGE,
                                         null,
                                         m_da.options_yes_no,
                                         JOptionPane.YES_OPTION);


            if (option_selected == JOptionPane.NO_OPTION)
            {
                //System.out.println("Option NO was here!");
                return;
            }
            //System.out.println("Option YES was here!");

    	    try 
    	    {
                dayData.deleteRow(table.getSelectedRow());
                model.fireTableChanged(null);
                m_db.saveDayStats(dayData);
    	    }
    	    catch (Exception ex) 
    	    {
                System.out.println("DailyStatsDialog:Action:Delete Row: " + ex);
                log.error("Action::Delete Row::Exception: " + ex, ex); 
    	    }
    	}
    	else if (command.equals("close"))
    	{
            this.close();
    	}
    	else if (command.equals("show_daily_graph"))
    	{
    	    DailyGraphDialog dgd = new DailyGraphDialog(this, this.dayData);
            System.out.println(dayData.getDate());
    	    dgd.setDailyValues(this.dayData);
    	}
        else
            System.out.println("DailyStatsDialog:Unknown Action: " + command);


    }

    
    

    
    // ****************************************************************
    // ******              HelpCapable Implementation             *****
    // ****************************************************************
    
    /* 
     * getComponent - get component to which to attach help context
     */
    public Component getComponent()
    {
	return this.getRootPane();
    }

    /* 
     * getHelpButton - get Help button
     */
    public JButton getHelpButton()
    {
	return this.help_button;
    }

    /* 
     * getHelpId - get id for Help
     */
    public String getHelpId()
    {
	return "pages.GGC_BG_Daily_View";
    }
    
    
    
    
}