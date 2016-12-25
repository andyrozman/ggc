package ggc.gui.dialogs.doctor.appointment;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.atech.graphics.components.DateTimeComponent;
import com.atech.graphics.dialogs.StandardDialogForObject;
import com.atech.graphics.layout.TableLayoutUtil;
import com.atech.utils.ATSwingUtils;

import ggc.core.db.hibernate.doc.DoctorAppointmentH;
import ggc.core.db.hibernate.doc.DoctorH;
import ggc.core.util.DataAccess;
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
 *  Filename:     zzz  
 *  Description:  zzz
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

// fix this
@Deprecated
public class AppointmentDialog extends StandardDialogForObject
{

    private static final long serialVersionUID = -3472108488117880600L;

    // private DataAccess dataAccess = DataAccess.getInstance();
    // private I18nControlAbstract i18nControl =
    // dataAccess.getI18nControlInstance();

    // private boolean m_actionDone = false;

    // x private String[] schemes_names = null;

    // GregorianCalendar gc = null;
    // JSpinner sl_year = null, sl_month = null;

    // NEW

    JLabel lblDoctor;
    JTextField tfAppointment;
    JTextArea taComment;
    DateTimeComponent dtAppointment;
    DoctorH doctorH;


    /**
     * Constructor
     * 
     * @param dialog
     */
    public AppointmentDialog(JDialog dialog)
    {
        super(dialog, DataAccess.getInstance());
    }


    public AppointmentDialog(JDialog dialog, DoctorAppointmentH doctorAppointment, boolean editValue)
    {
        super(dialog, DataAccess.getInstance(), doctorAppointment, false);

        this.editValue = editValue;

        init(doctorAppointment);
    }


    @Override
    public void initGUI()
    {
        ATSwingUtils.initLibrary();

        int x = 0;
        int y = 0;
        int width = 500;
        int height = 450;

        this.positionXForEntryLabel = 60;

        this.setBounds(x, y, width, height);

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
                DoctorSelector selector = new DoctorSelector(AppointmentDialog.this);
                selector.setVisible(true);

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


    private JPanel createPanel(Color color)
    {
        JPanel p = new JPanel();
        p.setBackground(color);
        return p;
    }


    @Override
    public void loadData(Object dataObject)
    {

    }


    @Override
    public boolean saveData()
    {
        return false;
    }


    @Override
    public String getHelpId()
    {
        return null;
    }

    /*
     * public boolean actionSuccesful()
     * {
     * return m_actionDone;
     * }
     * public String[] getActionResult()
     * {
     * String[] res = new String[3];
     * if (m_actionDone)
     * res[0] = "1";
     * else
     * res[0] = "0";
     * res[1] = this.tfName.getText();
     * res[2] = this.cb_template.getSelectedItem().toString();
     * return res;
     * }
     */
}
