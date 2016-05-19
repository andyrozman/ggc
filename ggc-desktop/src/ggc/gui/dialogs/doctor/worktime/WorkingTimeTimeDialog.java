package ggc.gui.dialogs.doctor.worktime;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.atech.graphics.components.TimeComponent;
import com.atech.graphics.dialogs.StandardDialogForObject;
import com.atech.utils.ATSwingUtils;

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
 *  Filename:     WorkingTimeTimeDialog
 *  Description:  Dialog for setting working time (from and till)
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class WorkingTimeTimeDialog extends StandardDialogForObject
{

    private static final long serialVersionUID = 7242367041788589271L;

    TimeComponent timeComponentFrom, timeComponentTo;
    WorkingTimeTimeEntry workingTimeTimeEntry;


    public WorkingTimeTimeDialog(JDialog dialog)
    {
        super(dialog, DataAccess.getInstance());
    }


    public WorkingTimeTimeDialog(JDialog dialog, WorkingTimeTimeEntry workingTimeTimeEntry, boolean editValue)
    {
        super(dialog, DataAccess.getInstance(), workingTimeTimeEntry, false);

        this.editValue = editValue;

        init(workingTimeTimeEntry);
    }


    @Override
    public void loadData(Object dataObject)
    {
        this.workingTimeTimeEntry = (WorkingTimeTimeEntry) dataObject;

        this.timeComponentFrom.setTime(this.workingTimeTimeEntry.getTimeFrom());
        this.timeComponentTo.setTime(this.workingTimeTimeEntry.getTimeTill());
    }


    @Override
    public boolean saveData()
    {
        if (workingTimeTimeEntry == null)
        {
            workingTimeTimeEntry = new WorkingTimeTimeEntry();
        }

        this.workingTimeTimeEntry.setTimeFrom(this.timeComponentFrom.getTime());
        this.workingTimeTimeEntry.setTimeTill(this.timeComponentTo.getTime());

        return true;
    }


    public void initGUI()
    {
        int width = 400;
        int height = 280;

        this.setSize(width, height);

        mainPanel = new JPanel();
        mainPanel.setBounds(0, 0, width, height);
        mainPanel.setLayout(null);

        getContentPane().setLayout(null);

        this.getContentPane().add(mainPanel);

        String titleMessage = i18nControl
                .getMessage(this.editValue ? "WORKING_TIME_TIME_EDIT" : "WORKING_TIME_TIME_ADD");

        ATSwingUtils.getTitleLabel(titleMessage, 0, 20, width, 40, mainPanel, ATSwingUtils.FONT_BIG_BOLD);
        this.setTitle(titleMessage);

        createEntryLabel("TIME_FROM", 90);

        timeComponentFrom = new TimeComponent();
        timeComponentFrom.setBounds(160, 90, 120, 25);
        timeComponentFrom.addActionListener(new ActionListener()
        {

            public void actionPerformed(ActionEvent e)
            {
                if (timeComponentFrom.getTime() > timeComponentTo.getTime())
                {
                    timeComponentTo.setTime(timeComponentFrom.getTime());
                }
            }
        });
        mainPanel.add(timeComponentFrom);

        createEntryLabel("TIME_TILL", 140);

        timeComponentTo = new TimeComponent();
        timeComponentTo.setBounds(160, 140, 120, 25);
        timeComponentTo.addActionListener(new ActionListener()
        {

            public void actionPerformed(ActionEvent e)
            {
                if (timeComponentTo.getTime() < timeComponentFrom.getTime())
                {
                    timeComponentFrom.setTime(timeComponentTo.getTime());
                }
            }
        });
        mainPanel.add(timeComponentTo);

        JButton button = new JButton(" " + i18nControl.getMessage("OK"));
        button.setBounds(15, 200, 110, 30);
        button.setIcon(ATSwingUtils.getImageIcon_22x22("ok.png", this, dataAccess));
        button.setActionCommand("ok");
        button.addActionListener(this);
        mainPanel.add(button);

        button = new JButton(" " + i18nControl.getMessage("CANCEL"));
        button.setBounds(135, 200, 110, 30);
        button.setIcon(ATSwingUtils.getImageIcon_22x22("cancel.png", this, dataAccess));
        button.setActionCommand("cancel");
        button.addActionListener(this);
        mainPanel.add(button);

        this.btnHelp = ATSwingUtils.createHelpButtonBySize(110, 30, mainPanel, dataAccess);
        this.btnHelp.setBounds(255, 200, 120, 30);
        mainPanel.add(this.btnHelp);

        this.dataAccess.centerJDialog(this);

    }


    public WorkingTimeTimeEntry getObject()
    {
        return this.workingTimeTimeEntry;
    }


    @Override
    public String getHelpId()
    {
        return "Doc_WorkingTimeTimeDialog";
    }

}
