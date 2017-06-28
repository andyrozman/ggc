package ggc.gui.dialogs.doctor.worktime;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import com.atech.graphics.components.jlist.JListControler;
import com.atech.graphics.dialogs.StandardDialogForObject;
import com.atech.utils.ATSwingUtils;

import ggc.core.data.defs.WeekDay;
import ggc.core.db.dto.WorkingTimeDTO;
import ggc.core.db.dto.WorkingTimeTimeEntry;
import ggc.core.util.DataAccess;

/**
 *  Application:   GGC - GNU Gluco Control
 *
 *  See AUTHORS for copyright information.
 * 
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 * 
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 *  Filename:     WorkingTimeDialog
 *  Description:  Add/Edit Working Time
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class WorkingTimeDialog extends StandardDialogForObject implements ActionListener
{

    private static final long serialVersionUID = 1871251398440542542L;

    private WorkingTimeDTO workingTimeDTO;
    JPanel mainPanel;
    Map<WeekDay, JCheckBox> cbDaysMap;
    JListControler jListWithControlButtons;
    ListControlerActionsSettings4TimeEntries listControlerActionsSettings4TimeEntries;


    public WorkingTimeDialog(JDialog dialog)
    {
        super(dialog, DataAccess.getInstance());
    }


    public WorkingTimeDialog(JDialog dialog, WorkingTimeDTO workingTimeDTO, boolean editValue)
    {
        super(dialog, DataAccess.getInstance(), workingTimeDTO, false);

        this.editValue = editValue;

        init(workingTimeDTO);
    }


    @Override
    public void loadData(Object dataObject)
    {
        // editing (changes value only)
        this.workingTimeDTO = (WorkingTimeDTO) dataObject;

        for (WeekDay day : this.workingTimeDTO.getWeekDays())
        {
            cbDaysMap.get(day).setSelected(true);
        }

        this.jListWithControlButtons.loadData(this.workingTimeDTO.getTimeEntries());
    }


    @Override
    public boolean saveData()
    {
        if (workingTimeDTO == null)
        {
            workingTimeDTO = new WorkingTimeDTO();
        }

        List<WeekDay> selectedDays = new ArrayList<WeekDay>();

        for (WeekDay weekDay : WeekDay.values())
        {
            if (cbDaysMap.get(weekDay).isSelected())
            {
                selectedDays.add(weekDay);
            }
        }

        if (selectedDays.size() == 0)
        {
            dataAccess.showMessageDialog(this, ATSwingUtils.DialogType.Error, "SELECT_DAYS");
            return false;
        }

        List<WorkingTimeTimeEntry> timeEntries = (List<WorkingTimeTimeEntry>) jListWithControlButtons.getData();

        if (timeEntries.size() == 0)
        {
            dataAccess.showMessageDialog(this, ATSwingUtils.DialogType.Error, "ADD_TIMES");
            return false;
        }

        workingTimeDTO.setWeekDays(selectedDays);
        workingTimeDTO.setTimeEntries(timeEntries);

        return true;
    }


    public void initGUI()
    {
        int width = 400;
        int height = 450;

        this.setBounds(0, 0, width, height);

        mainPanel = new JPanel();
        mainPanel.setBounds(0, 0, width, height);
        mainPanel.setLayout(null);

        this.getContentPane().setLayout(null);
        this.getContentPane().add(mainPanel);

        listControlerActionsSettings4TimeEntries = new ListControlerActionsSettings4TimeEntries();
        listControlerActionsSettings4TimeEntries.setParentDialog(this);

        String titleMessage = i18nControl.getMessage(this.editValue ? "WORKING_TIME_EDIT" : "WORKING_TIME_ADD");

        ATSwingUtils.getTitleLabel(titleMessage, 0, 25, width, 35, mainPanel, ATSwingUtils.FONT_BIG_BOLD);
        this.setTitle(titleMessage);

        ATSwingUtils.getLabel(i18nControl.getMessage("DAYS") + ":", 40, 80, 91, 14, mainPanel,
            ATSwingUtils.FONT_NORMAL_BOLD);

        int i = 1;
        int x = 0, y = 0;

        cbDaysMap = new HashMap<WeekDay, JCheckBox>();

        for (WeekDay weekDay : WeekDay.values())
        {
            if (i < 4)
            {
                y = 0;

            }
            else if ((i > 3) && (i < 7))
            {
                y = 1;
            }
            else
            {
                y = 2;
            }

            if ((i == 1) || (i == 4) || (i == 7))
            {
                x = 0;
            }
            else if ((i == 2) || (i == 5))
            {
                x = 1;
            }
            else
            {
                x = 2;
            }

            JCheckBox cb = ATSwingUtils.getCheckBox("  " + i18nControl.getMessage(weekDay.getDayI18nKey()),
                40 + (x * 100), 100 + (y * 21), 100, 25, mainPanel, ATSwingUtils.FONT_NORMAL);

            cbDaysMap.put(weekDay, cb);

            i++;
        }

        ATSwingUtils.getLabel(i18nControl.getMessage("TIMES") + ":", 40, 190, 120, 25, mainPanel,
            ATSwingUtils.FONT_NORMAL_BOLD);

        jListWithControlButtons = new JListControler(dataAccess, listControlerActionsSettings4TimeEntries,
                listControlerActionsSettings4TimeEntries);
        jListWithControlButtons.setBounds(0, 215, width, 130);
        this.mainPanel.add(jListWithControlButtons);

        JButton button = new JButton(" " + i18nControl.getMessage("OK"));
        button.setBounds(15, 370, 110, 30);
        button.setIcon(ATSwingUtils.getImageIcon_22x22("ok.png", this, dataAccess));
        button.setActionCommand("ok");
        button.addActionListener(this);
        this.mainPanel.add(button);

        button = new JButton(" " + i18nControl.getMessage("CANCEL"));
        button.setBounds(135, 370, 110, 30);
        button.setIcon(ATSwingUtils.getImageIcon_22x22("cancel.png", this, dataAccess));
        button.setActionCommand("cancel");
        button.addActionListener(this);
        this.mainPanel.add(button);

        this.btnHelp = ATSwingUtils.createHelpButtonBySize(110, 30, this.mainPanel, dataAccess);
        this.btnHelp.setBounds(255, 370, 120, 30);
        this.mainPanel.add(this.btnHelp);

        this.dataAccess.centerJDialog(this);

        // System.out.println("CC: " + this.jListWithControlButtons.toString());

    }


    // /**
    // * Invoked when an action occurs.
    // */
    // public void actionPerformed(ActionEvent e)
    // {
    // String action = e.getActionCommand();
    //
    // if (action.equals("item_add"))
    // {
    // PumpDataAdditionalWizardOne pdawo = new
    // PumpDataAdditionalWizardOne(this.ht_data, this); // ,
    // // this.m_pump_add);
    // pdawo.setVisible(true);
    //
    // processAdditionalData(pdawo);
    // }
    // else if (action.equals("item_edit"))
    // {
    // if (this.listTimes.isSelectionEmpty())
    // {
    // JOptionPane.showConfirmDialog(this, i18nControl.getMessage("SELECT_ITEM_FIRST"),
    // i18nControl.getMessage("ERROR"),
    // JOptionPane.CLOSED_OPTION);
    // return;
    // }
    //
    // itemEdit();
    // }
    // else if (action.equals("item_delete"))
    // {
    // if (this.m_list_data.isSelectionEmpty())
    // {
    // JOptionPane.showConfirmDialog(this, i18nControl.getMessage("SELECT_ITEM_FIRST"),
    // i18nControl.getMessage("ERROR"),
    // JOptionPane.CLOSED_OPTION);
    // return;
    // }
    //
    // int ii = JOptionPane.showConfirmDialog(this,
    // i18nControl.getMessage("ARE_YOU_SURE_DELETE"),
    // i18nControl.getMessage("ERROR"), JOptionPane.YES_NO_OPTION);
    //
    // if (ii == JOptionPane.YES_OPTION)
    // {
    // PumpValuesEntryExt pc =
    // this.list_data.get(this.m_list_data.getSelectedIndex());
    //
    // this.deleteAddItem(pc);
    //
    // populateJListExtended(this.timeEntries);
    // }
    // else
    // return;
    //
    // }
    // else
    // {
    // super.actionPerformed(e);
    // }
    //
    // }

    @Override
    public String getHelpId()
    {
        return "Doc_WorkingTimeDialog";
    }


    public Object getObject()
    {
        return this.workingTimeDTO;
    }
}
