package ggc.gui.dialogs.doctor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.graphics.components.DateComponent;
import com.atech.graphics.components.jlist.JListControler;
import com.atech.graphics.dialogs.StandardDialogForObject;
import com.atech.graphics.layout.TableLayoutUtil;
import com.atech.utils.ATSwingUtils;
import com.atech.utils.xml.XStreamUtil;

import ggc.core.db.GGCDb;
import ggc.core.db.dto.WorkingTimeDTO;
import ggc.core.db.hibernate.doc.DoctorH;
import ggc.core.db.hibernate.doc.DoctorTypeH;
import ggc.core.util.DataAccess;
import ggc.gui.dialogs.doctor.worktime.ListControlerActionsSettings4WorkTime;
import info.clearthought.layout.TableLayout;

/**
 * Application:   GGC - GNU Gluco Control
 * 
 * See AUTHORS for copyright information.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Filename:     DoctorDialog
 * Description:  Add/Edit Dialog for Doctors
 * 
 * Author: andyrozman {andy@atech-software.com}
 */

public class DoctorDialog extends StandardDialogForObject
{

    private static final long serialVersionUID = 7871868139736851282L;

    private static final Logger LOG = LoggerFactory.getLogger(DoctorDialog.class);

    private JTextField tfName, tfPhone, tfPhoneGsm, tfEmail;
    private JTextArea taComment, taAddress;
    private JLabel lblDocType;

    private DateComponent dcFrom, dcTill;
    private DoctorTypeH doctorType;
    JCheckBox activeTillEnabled;

    JListControler jListWithControlButtons;

    JTabbedPane tabbedPane;
    DoctorH doctorH;
    long lastAction = 0;


    // // REMOVE
    // public DoctorDialog(JFrame frame)
    // {
    // super(frame, DataAccess.getInstance(), true);
    // }

    public DoctorDialog(JDialog dialog)
    {
        super(dialog, DataAccess.getInstance());
    }


    public DoctorDialog(JDialog dialog, DoctorH doctor, boolean editValue)
    {
        super(dialog, DataAccess.getInstance(), doctor, false);

        this.editValue = editValue;

        init(doctor);
    }


    @Override
    public void loadData(Object dataObject)
    {
        this.doctorH = (DoctorH) dataObject;

        this.doctorType = this.doctorH.getDoctorType();
        displayDoctorType();

        // basic
        this.setText(this.tfName, this.doctorH.getName());
        this.setText(this.taAddress, this.doctorH.getAddress());

        this.setText(this.tfPhone, this.doctorH.getPhone());
        this.setText(this.tfPhoneGsm, this.doctorH.getPhoneGsm());
        this.setText(this.tfEmail, this.doctorH.getEmail());

        // working time
        List<WorkingTimeDTO> list = (List<WorkingTimeDTO>) XStreamUtil
                .getObjectFromString(this.doctorH.getWorkingTime());
        this.jListWithControlButtons.loadData(list);

        // other
        this.dcFrom.setDate((int) this.doctorH.getActiveFrom());

        int dd = (int) this.doctorH.getActiveTill();

        this.activeTillEnabled.setSelected(dd > 0);
        if (dd > 0)
        {
            this.dcTill.setDate(dd);
        }

        this.setText(this.taComment, this.doctorH.getComment());
    }


    @Override
    public boolean saveData()
    {
        if (this.doctorH == null)
        {
            this.doctorH = new DoctorH();
        }

        if (this.doctorType == null)
        {
            dataAccess.showMessageDialog(this, ATSwingUtils.DialogType.Error, "SELECT_DOCTOR_TYPE");
            return false;
        }

        // basic
        this.doctorH.setName(getText(tfName));
        this.doctorH.setAddress(getText(taAddress));
        this.doctorH.setDoctorType(this.doctorType);
        this.doctorH.setPhone(getText(this.tfPhone));
        this.doctorH.setPhoneGsm(getText(this.tfPhoneGsm));
        this.doctorH.setEmail(getText(this.tfEmail));

        // working time
        this.doctorH.setWorkingTime(XStreamUtil.getStringFromObject(this.jListWithControlButtons.getData()));

        // other
        this.doctorH.setActiveFrom(this.dcFrom.getDate());
        if (this.activeTillEnabled.isSelected())
        {
            this.doctorH.setActiveTill(this.dcTill.getDate());
        }
        this.doctorH.setComment(getText(this.taComment));
        this.doctorH.setPersonId((int) this.dataAccess.getCurrentUserId());

        GGCDb db = ((DataAccess) dataAccess).getDb();

        if (this.doctorH.getId() > 0)
        {
            db.editHibernate(this.doctorH);
        }
        else
        {
            db.addHibernate(this.doctorH);
        }

        return true;
    }


    public void initGUI()
    {

        int x = 0;
        int y = 0;
        int width = 500;
        int height = 500;

        this.positionXForEntryLabel = 60;

        // System.out.println("Doctor v2.2");

        this.setBounds(x, y, width, height);

        getContentPane().setLayout(TableLayoutUtil.createVerticalLayout(60, TableLayout.FILL, 10, 30, 10));

        String titleMessage = i18nControl.getMessage(this.editValue ? "DOCTOR_EDIT" : "DOCTOR_ADD");

        getContentPane().add(ATSwingUtils.getTitleLabel(titleMessage, ATSwingUtils.FONT_BIG_BOLD), "0,0");
        this.setTitle(titleMessage);

        tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(5, 70, width - 20, height - 150);

        tabbedPane.addTab("Base", createBasePanel());
        tabbedPane.addTab("Working Time", createWorkTimePanel());
        tabbedPane.addTab("Other", createOtherPanel());

        // this.mainPanel.add(tabbedPane);
        this.getContentPane().add(tabbedPane, "0, 1");

        int buttonPosY = 10; // tabbedPane.getHeight() + tabbedPane.getY() + 10;

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(TableLayoutUtil.createHorizontalLayout(TableLayout.FILL, 120, 6, 120, 6, 120, 10));

        JButton button = new JButton("   " + i18nControl.getMessage("OK"));
        button.setIcon(ATSwingUtils.getImageIcon_22x22("ok.png", this, dataAccess));
        button.setActionCommand("ok");
        button.setBounds(105, buttonPosY, 120, 30);
        button.addActionListener(this);
        buttonsPanel.add(button, "1, 0");

        button = new JButton("   " + i18nControl.getMessage("CANCEL"));
        button.setIcon(ATSwingUtils.getImageIcon_22x22("cancel.png", this, dataAccess));
        button.setActionCommand("cancel");
        button.setBounds(235, buttonPosY, 120, 30);
        button.addActionListener(this);
        buttonsPanel.add(button, "3, 0");

        btnHelp = ATSwingUtils.createHelpButtonBySize(120, 30, buttonsPanel, dataAccess);
        btnHelp.setBounds(360, buttonPosY, 120, 30);
        buttonsPanel.add(btnHelp, "5, 0");
        this.getContentPane().add(buttonsPanel, "0, 3");

        this.dataAccess.centerJDialog(this);

    }


    private JPanel createBasePanel()
    {
        JPanel panel = new JPanel();

        double sizes[][] = { { 0.05, 0.25, 0.05, TableLayout.FILL, 0.05, 35, 0.05 },
                             { 20, 26, 12, 35, 12, 75, 12, 26, 12, 26, 12, 26, TableLayout.FILL } };

        panel.setLayout(new TableLayout(sizes));

        createNameLabel("NAME", panel, "1, 1");

        ATSwingUtils.getLabel(i18nControl.getMessage("NAME"), panel, "1, 1", ATSwingUtils.FONT_NORMAL_BOLD);

        tfName = ATSwingUtils.getTextField("", ATSwingUtils.FONT_NORMAL);
        panel.add(tfName, "3, 1");

        createNameLabel("TYPE", panel, "1, 3");
        lblDocType = ATSwingUtils.getLabel("", panel, "3, 3", ATSwingUtils.FONT_NORMAL);

        JButton b = ATSwingUtils.getButton(null, panel, "5, 3", ATSwingUtils.FONT_NORMAL, "select.jpg", dataAccess);
        b.setToolTipText(i18nControl.getMessage("SELECT"));
        b.addActionListener(new ActionListener()
        {

            public void actionPerformed(ActionEvent e)
            {
                DoctorTypeSelector selector = new DoctorTypeSelector(DoctorDialog.this);
                selector.setVisible(true);

                if (selector.wasAction())
                {
                    doctorType = (DoctorTypeH) selector.getSelectedItem();
                    displayDoctorType();
                }
            }
        });

        createNameLabel("ADDRESS", panel, "1, 5");
        taAddress = ATSwingUtils.getTextArea("", panel, "3, 5", ATSwingUtils.FONT_NORMAL);

        createNameLabel("PHONE", panel, "1, 7");
        tfPhone = ATSwingUtils.getTextField("", panel, "3, 7", ATSwingUtils.FONT_NORMAL);

        createNameLabel("PHONE_GSM", panel, "1, 9");
        tfPhoneGsm = ATSwingUtils.getTextField("", panel, "3, 9", ATSwingUtils.FONT_NORMAL);

        createNameLabel("EMAIL", panel, "1, 11");
        tfEmail = ATSwingUtils.getTextField("", panel, "3, 11", ATSwingUtils.FONT_NORMAL);

        displayDoctorType();

        return panel;
    }


    private void createNameLabel(String keyword, JPanel panel, String layoutConstraint)
    {
        ATSwingUtils.getLabel(i18nControl.getMessage(keyword) + ":", panel, layoutConstraint,
            ATSwingUtils.FONT_NORMAL_BOLD);
    }


    private JPanel createWorkTimePanel()
    {
        JPanel panel = new JPanel();
        panel.setLayout(TableLayoutUtil.createVerticalLayout(0.05, 0.1, 0.05, TableLayout.FILL, 0.2));

        JPanel p1 = new JPanel();
        p1.setLayout(TableLayoutUtil.createHorizontalLayout(0.1, TableLayout.FILL));

        ATSwingUtils.getLabel(i18nControl.getMessage("WORKING_TIME") + ":", p1, "1, 0", ATSwingUtils.FONT_NORMAL_BOLD);

        ListControlerActionsSettings4WorkTime listControlerActionsSettings4WorkTime = new ListControlerActionsSettings4WorkTime();
        listControlerActionsSettings4WorkTime.setParentDialog(this);

        jListWithControlButtons = new JListControler(dataAccess, listControlerActionsSettings4WorkTime,
                listControlerActionsSettings4WorkTime);

        panel.add(jListWithControlButtons, "0, 3");

        return panel;
    }


    private JPanel createOtherPanel()
    {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        int startY = 30;
        int entryX = 150;

        createEntryLabel("ACTIVE_FROM", startY, panel);

        dcFrom = new DateComponent(2000, dataAccess);
        dcFrom.setBounds(entryX, startY, 0, 0);
        panel.add(dcFrom);

        createEntryLabel("ACTIVE_TILL", startY + 50, panel);

        dcTill = new DateComponent(2000, dataAccess);
        dcTill.setBounds(entryX + 40, startY + 50, 0, 0);
        dcTill.setEnabled(false);
        panel.add(dcTill);

        activeTillEnabled = ATSwingUtils.getCheckBox("", entryX, startY + 50, 30, 25, panel, ATSwingUtils.FONT_NORMAL);
        activeTillEnabled.setSelected(false);
        activeTillEnabled.addChangeListener(new ChangeListener()
        {

            public void stateChanged(ChangeEvent e)
            {
                if (System.currentTimeMillis() - lastAction > 1000)
                {
                    lastAction = System.currentTimeMillis();
                    // System.out.println("State changed: " +
                    // activeTillEnabled.isSelected());
                    dcTill.setEnabled(activeTillEnabled.isSelected());
                }
            }
        });

        createEntryLabel("COMMENT", startY + 100, panel);

        taComment = ATSwingUtils.getTextArea("", 20, startY + 130, 340, 100, panel, ATSwingUtils.FONT_NORMAL);

        return panel;
    }


    protected void createEntryLabel(String message, int positionY, JPanel panel)
    {
        ATSwingUtils.getLabel(i18nControl.getMessage(message) + ":", 20, positionY, 120, 25, panel,
            ATSwingUtils.FONT_NORMAL_BOLD);
    }


    private void displayDoctorType()
    {
        if (doctorType == null)
        {
            this.lblDocType.setText(i18nControl.getMessage("SELECT_DOCTOR_TYPE"));
        }
        else
        {
            doctorType.setNameTranslated();
            this.lblDocType.setText(doctorType.getNameTranslated());
        }
    }


    @Override
    public String getHelpId()
    {
        return "Doc_DoctorDialog";
    }

}
