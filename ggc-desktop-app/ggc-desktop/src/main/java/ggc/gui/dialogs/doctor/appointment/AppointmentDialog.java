package ggc.gui.dialogs.doctor.appointment;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.apache.commons.lang.StringUtils;

import com.atech.db.hibernate.HibernateObject;
import com.atech.graphics.components.DateTimeComponent;
import com.atech.graphics.dialogs.StandardDialogForObject;
import com.atech.graphics.layout.TableLayoutUtil;
import com.atech.utils.ATSwingUtils;

import ggc.core.db.GGCDb;
import ggc.core.db.hibernate.doc.DoctorAppointmentH;
import ggc.core.db.hibernate.doc.DoctorH;
import ggc.core.util.DataAccess;
import ggc.gui.dialogs.selector.GGCSelectorConfiguration;
import ggc.gui.dialogs.selector.GGCSelectorDialog;
import info.clearthought.layout.TableLayout;

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
 *  Filename:     AppointmentDialog
 *  Description:  Dialog for Appointments
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class AppointmentDialog extends StandardDialogForObject
{

    private static final long serialVersionUID = -8705718295028204845L;

    JLabel lblDoctor;
    JTextField tfAppointment;
    JTextArea taComment;
    DateTimeComponent dtAppointment;
    DoctorH doctorH;
    DoctorAppointmentH doctorAppointmentH;
    long currentTime;


    /**
     * Constructor
     * 
     * @param dialog
     */
    public AppointmentDialog(JDialog dialog)
    {
        super(dialog, DataAccess.getInstance());
    }


    public AppointmentDialog(JDialog dialog, HibernateObject doctorAppointment, boolean editValue)
    {
        super(dialog, DataAccess.getInstance(), doctorAppointment, false);

        this.editValue = editValue;

        init(doctorAppointment);
    }


    @Override
    public void initGUI()
    {
        ATSwingUtils.initLibrary();

        // int x = 0;
        // int y = 0;
        int width = 500;
        int height = 450;

        // this.positionXForEntryLabel = 60;

        this.setBounds(0, 0, width, height);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, width, height);
        panel.setLayout(TableLayoutUtil.createVerticalLayout(80, TableLayout.FILL, 10, 30, 20));

        getContentPane().add(panel);

        String titleMessage = i18nControl.getMessage(this.editValue ? "DOCTOR_APP_EDIT_DESC" : "DOCTOR_APP_ADD_DESC");

        this.setTitle(titleMessage);
        panel.add(ATSwingUtils.getTitleLabel(titleMessage, ATSwingUtils.FONT_BIG_BOLD), "0,0");

        JPanel bodyPanel = new JPanel();

        double[][] sizes = { { 40, 0.4, 10, 0.6, 10, 40, 40 }, //
                             { 10, 35, 10, 65, 10, 30, 10, 90, 10 } };

        bodyPanel.setLayout(new TableLayout(sizes));

        createNameLabel("DOCTOR", bodyPanel, "1, 1");
        lblDoctor = ATSwingUtils.getLabel("", bodyPanel, "3, 1", ATSwingUtils.FONT_NORMAL);

        JButton b = ATSwingUtils.getButton(null, bodyPanel, "5, 1", ATSwingUtils.FONT_NORMAL, "select.jpg", dataAccess);
        b.setToolTipText(i18nControl.getMessage("SELECT"));
        b.addActionListener(new ActionListener()
        {

            public void actionPerformed(ActionEvent e)
            {
                GGCSelectorDialog selector = new GGCSelectorDialog(AppointmentDialog.this,
                        GGCSelectorConfiguration.DoctorH);

                if (selector.wasAction())
                {
                    doctorH = (DoctorH) selector.getSelectedItem();
                    displayDoctor();
                }

            }
        });

        createNameLabel("APPOINTMENT_DATE", bodyPanel, "1, 3");
        dtAppointment = new DateTimeComponent(dataAccess, DateTimeComponent.ALIGN_VERTICAL, 10,
                DateTimeComponent.TIME_MAXIMAL_MINUTE);
        bodyPanel.add(dtAppointment, "3, 3");

        dtAppointment.setDateTimeAsCurrent();

        currentTime = dtAppointment.getDateTime();

        createNameLabel("APPOINTMENT_TEXT", bodyPanel, "1, 5");
        tfAppointment = ATSwingUtils.getTextField("", bodyPanel, "3, 5", ATSwingUtils.FONT_NORMAL);

        createNameLabel("COMMENT", bodyPanel, "1, 7");
        taComment = ATSwingUtils.getTextArea("", bodyPanel, "3, 7", ATSwingUtils.FONT_NORMAL);

        panel.add(bodyPanel, "0, 1");

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(TableLayoutUtil.createHorizontalLayout(0.5, 120, 6, 120, 6, 120, 0.5));

        JButton button = new JButton("   " + i18nControl.getMessage("OK"));
        button.setIcon(ATSwingUtils.getImageIcon_22x22("ok.png", this, dataAccess));
        button.setActionCommand("ok");
        button.addActionListener(this);
        buttonsPanel.add(button, "1, 0");

        button = new JButton("   " + i18nControl.getMessage("CANCEL"));
        button.setIcon(ATSwingUtils.getImageIcon_22x22("cancel.png", this, dataAccess));
        button.setActionCommand("cancel");
        button.addActionListener(this);
        buttonsPanel.add(button, "3, 0");

        btnHelp = ATSwingUtils.createHelpButtonBySize(120, 30, buttonsPanel, dataAccess);
        buttonsPanel.add(btnHelp, "5, 0");

        panel.add(buttonsPanel, "0, 3");

        // this.getContentPane().add(buttonsPanel, "0, 3");

        this.dataAccess.centerJDialog(this);

    }


    private void displayDoctor()
    {
        if (doctorH == null)
        {
            this.lblDoctor.setText(i18nControl.getMessage("SELECT_DOCTOR"));
        }
        else
        {
            this.lblDoctor.setText(doctorH.getName());
        }
    }


    private void createNameLabel(String keyword, JPanel panel, String layoutConstraint)
    {
        ATSwingUtils.getLabel(i18nControl.getMessage(keyword) + ":", panel, layoutConstraint,
            ATSwingUtils.FONT_NORMAL_BOLD);
    }


    @Override
    public void loadData(Object dataObject)
    {
        this.doctorAppointmentH = (DoctorAppointmentH) dataObject;

        this.doctorH = this.doctorAppointmentH.getDoctor();
        displayDoctor();

        this.dtAppointment.setDateTime(this.doctorAppointmentH.getAppointmentDateTime());
        this.tfAppointment.setText(this.doctorAppointmentH.getAppointmentText());
        this.taComment.setText(this.doctorAppointmentH.getComment());
    }


    @Override
    public boolean saveData()
    {
        if (this.doctorAppointmentH == null)
        {
            this.doctorAppointmentH = new DoctorAppointmentH();
        }

        if (this.doctorH == null)
        {
            dataAccess.showMessageDialog(this, ATSwingUtils.DialogType.Error, "SELECT_DOCTOR");
            return false;
        }

        if (StringUtils.isBlank(this.tfAppointment.getText()))
        {
            dataAccess.showMessageDialog(this, ATSwingUtils.DialogType.Error, "ENTER_APPOINTMENT_TEXT");
            return false;
        }

        if (this.dtAppointment.getDateTime() == currentTime)
        {
            dataAccess.showMessageDialog(this, ATSwingUtils.DialogType.Error, "ENTER_APPOINTMENT_TIME");
            return false;
        }

        this.doctorAppointmentH.setAppointmentText(this.tfAppointment.getText());
        this.doctorAppointmentH.setDoctor(doctorH);
        this.doctorAppointmentH.setAppointmentDateTime(this.dtAppointment.getDateTime());
        this.doctorAppointmentH.setComment(getText(this.taComment));
        this.doctorAppointmentH.setPersonId((int) this.dataAccess.getCurrentUserId());

        GGCDb db = ((DataAccess) dataAccess).getDb();

        if (this.doctorAppointmentH.getId() > 0)
        {
            db.editHibernate(this.doctorAppointmentH);
        }
        else
        {
            db.addHibernate(this.doctorAppointmentH);
        }

        return true;

    }


    @Override
    public String getHelpId()
    {
        return "Appointment_AppointmentDialog";
    }

}
