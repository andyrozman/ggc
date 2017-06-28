package ggc.gui.dialogs.doctor;

import javax.swing.*;

import com.atech.db.hibernate.HibernateObject;
import com.atech.graphics.dialogs.StandardDialogForObject;
import com.atech.utils.ATSwingUtils;

import ggc.core.db.hibernate.doc.DoctorTypeH;
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
 *  Filename:     DoctorTypeDialog
 *  Description:  Add/Edit Doctor Type's
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class DoctorTypeDialog extends StandardDialogForObject
{

    private static final long serialVersionUID = 6424821713180565428L;

    private JTextField tfDoctorType;
    private DoctorTypeH doctorTypeObject;


    public DoctorTypeDialog(JDialog dialog)
    {
        super(dialog, DataAccess.getInstance());
    }


    // public DoctorTypeDialog(JDialog dialog, DoctorTypeH doctorObject, boolean
    // editValue)
    // {
    // super(dialog, DataAccess.getInstance(), doctorObject, false);
    //
    // this.editValue = editValue;
    //
    // init(doctorObject);
    // }

    public DoctorTypeDialog(JDialog dialog, HibernateObject doctorObject, boolean editValue)
    {
        super(dialog, DataAccess.getInstance(), doctorObject, false);

        this.editValue = editValue;

        init(doctorObject);
    }


    @Override
    public void loadData(Object dataObject)
    {
        // editing (changes value only)
        this.doctorTypeObject = (DoctorTypeH) dataObject;
        this.tfDoctorType.setText(this.doctorTypeObject.getName());
    }


    @Override
    public boolean saveData()
    {
        if (doctorTypeObject == null)
        {
            doctorTypeObject = new DoctorTypeH();
        }

        doctorTypeObject.setName(tfDoctorType.getText());
        doctorTypeObject.setPredefined(0);

        if (doctorTypeObject.getId() == 0)
        {
            Long id = dataAccess.getHibernateDb().addHibernate(doctorTypeObject);

            if ((id == null) || (id <= 0))
            {
                System.out.println("id null");
                return false;
            }
            else
            {
                doctorTypeObject.setId(id);
                return true;
            }
        }
        else
        {
            return dataAccess.getHibernateDb().editHibernate(doctorTypeObject);
        }

    }


    public void initGUI()
    {
        int width = 400;
        int height = 340;

        this.setBounds(0, 0, width, height);

        mainPanel = new JPanel();
        mainPanel.setBounds(0, 0, 400, 340);
        mainPanel.setLayout(null);

        getContentPane().setLayout(null);

        this.getContentPane().add(mainPanel);

        String titleMessage = i18nControl.getMessage(this.editValue ? "DOCTOR_TYPE_EDIT" : "DOCTOR_TYPE_ADD");

        ATSwingUtils.getTitleLabel(titleMessage, 0, 25, width, 40, mainPanel, ATSwingUtils.FONT_BIG_BOLD);
        this.setTitle(titleMessage);

        createEntryLabel("DOCTOR_TYPE", 100);
        tfDoctorType = ATSwingUtils.getTextField("", 40, 135, 320, 25, mainPanel, ATSwingUtils.FONT_NORMAL);

        JButton button = new JButton(" " + i18nControl.getMessage("OK"));
        button.setBounds(15, 220, 110, 30);
        button.setIcon(ATSwingUtils.getImageIcon_22x22("ok.png", this, dataAccess));
        button.setActionCommand("ok");
        button.addActionListener(this);
        mainPanel.add(button);

        button = new JButton(" " + i18nControl.getMessage("CANCEL"));
        button.setBounds(135, 220, 110, 30);
        button.setIcon(ATSwingUtils.getImageIcon_22x22("cancel.png", this, dataAccess));
        button.setActionCommand("cancel");
        button.addActionListener(this);
        mainPanel.add(button);

        this.btnHelp = ATSwingUtils.createHelpButtonBySize(110, 30, mainPanel, dataAccess);
        this.btnHelp.setBounds(255, 220, 120, 30);
        mainPanel.add(this.btnHelp);

        this.dataAccess.centerJDialog(this);

    }


    @Override
    public String getHelpId()
    {
        return "Doc_DocTypeDialog";
    }

}
