package ggc.gui.main.panels;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;

import ggc.core.db.hibernate.doc.DoctorAppointmentH;
import ggc.core.enums.GGCFunctionality;
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
 *  Filename:     AppointmentsInfoPanel
 *  Description:  Panel for displaying Schedule info
 *
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class AppointmentsInfoPanel extends AbstractInfoPanel
{

    private static final long serialVersionUID = 3297655715644239482L;


    /**
     * Constructor
     */
    public AppointmentsInfoPanel()
    {
        super("DOCTORS_APPOINTMENTS");
        init();
        // refreshInfo();
    }


    private void init()
    {
        if (!GGCFunctionality.DoctorsAppointments.isEnabled())
        {
            setLayout(new GridLayout(0, 2));
            add(new JLabel(i18nControl.getMessage("YOUR_NEXT_APPOINTMENT") + ":"));
            add(new JLabel(
                    i18nControl.getMessage("APP_WILL_BE_FOUND_HERE") + "..." + i18nControl.getMessage("NOT_YET")));
        }
    }


    /**
     * Do Refresh - This method can do Refresh
     */
    @Override
    public void doRefresh()
    {
        System.out.println("DO REFRESH OF SCHEDULE");
        if (GGCFunctionality.DoctorsAppointments.isEnabled())
        {
            this.removeAll();

            java.util.List<DoctorAppointmentH> activeAppointments = dataAccess.getDb().getActiveAppointments();

            if (activeAppointments.size() == 0)
            {
                setLayout(new GridLayout(0, 1));
                add(new JLabel(i18nControl.getMessage("NO_APPOINTMENTS_DEFINED")));
            }
            else
            {
                setLayout(new GridLayout(0, 1));
                // setLayout(new GridLayout(0, 2));
                // JList list = new JList()

                java.util.List<JPanel> itemList = new ArrayList<JPanel>();

                for (DoctorAppointmentH appointment : activeAppointments)
                {
                    itemList.add(createAppointmentItem(appointment));
                }

                JList list = new JList(itemList.toArray());
                list.setCellRenderer(new PanelRenderer());

                JScrollPane scrollPane = new JScrollPane(list);

                add(scrollPane);
            }
        }
    }

    double sizes[][] = { { 0.04, TableLayout.FILL, 0.04, 125, 0.04 }, //
                         { TableLayout.FILL } };


    private JPanel createAppointmentItem(DoctorAppointmentH appointment)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new TableLayout(sizes));

        String text = appointment.getAppointmentText() + " (" + appointment.getDoctor().getName() + ")";
        JLabel jLabel = new JLabel(text);
        jLabel.setToolTipText(text);

        panel.add(jLabel, "1,0");

        text = ATechDate.getDateTimeString(ATechDateType.DateAndTimeMin, appointment.getAppointmentDateTime());
        jLabel = new JLabel(text);
        jLabel.setToolTipText(text);

        panel.add(jLabel, "3,0");

        return panel;
    }


    @Override
    public InfoPanelType getPanelType()
    {
        return InfoPanelType.Appointments;
    }

    class PanelRenderer implements ListCellRenderer
    {

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                boolean cellHasFocus)
        {
            JPanel renderer = (JPanel) value;
            renderer.setBackground(list.getBackground());
            return renderer;
        }
    }

}
