package ggc.gui.dialogs.doctor.appointment;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

import com.atech.graphics.dialogs.guilist.ButtonDef;
import com.atech.graphics.dialogs.guilist.GUIListDefAbstract;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;

import ggc.core.db.GGCDb;
import ggc.core.db.hibernate.doc.DoctorAppointmentH;
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
 *  Filename:     HbA1cDialog2  
 *  Description:  Dialog for HbA1c, using new graph framework
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class AppointmentListDef extends GUIListDefAbstract
{

    DataAccess m_da = DataAccess.getInstance();
    I18nControlAbstract i18nControl = m_da.getI18nControlInstance();

    // private ArrayList<DoctorH> activeList= null;

    private List<DoctorAppointmentH> fullList = null;
    private List<DoctorAppointmentH> activeList = null;

    private GGCDb database;


    /**
     * Constructor
     */
    public AppointmentListDef()
    {
        database = m_da.getDb();
        init();
    }


    @Override
    public void doTableAction(String action)
    {
        // if (action.equals("add"))
        // {
        // StockSelectorDialog ssd = new
        // StockSelectorDialog(this.getParentDialog(), dataAccess, 1);
        // ssd.showDialog();
        // }
        // else if (action.equals("add_type"))
        // {
        // //StockTypeDialog std = new StockTypeDialog(this.getParentDialog());
        // //std.showDialog();
        // }
        // else if (action.equals("edit_type"))
        // {
        // StockSubTypeDialog sstd = new
        // StockSubTypeDialog(this.getParentDialog());
        // sstd.setVisible(true);
        // }
        // else

        if (action.equals("add_appointment"))
        {
            AppointmentDialog d = new AppointmentDialog(this.getParentDialog());

            if (d.wasOperationSuccessful())
            {
                this.loadData();
            }
        }
        else if (action.equals("edit_appointment"))
        {
            this.editTableRow();
        }
        else
        {
            System.out.println("DoctorListDef: Unknown action: " + action);
        }

        // if (action.equals("add_stocktaking"))
        // {
        // // StockAmounts sa = new StockAmounts(null, null,
        // // this.getParentDialog());
        // // sa.setVisible(true);
        // System.out.println(this.getDefName() + " has not implemented action "
        // + action);
        // }
        // else if (action.equals("edit_stocktaking"))
        // {
        // GUIListDialog guiListDialog = new
        // GUIListDialog(this.getParentDialog(), new StocktakingListDef(),
        // dataAccess);
        //
        // if (guiListDialog.isDataChanged())
        // {
        // loadData();
        // }
        // }
        // else if (action.equals("manage_types"))
        // {
        // new GUIListDialog(this.getParentDialog(), new StockTypeListDef(),
        // dataAccess);
        // }
        // else
        // {
        // System.out.println(this.getDefName() + " has not implemented action "
        // + action);
        // }
    }


    @Override
    public JTable getJTable()
    {
        if (this.table == null)
        {
            this.table = new JTable(new AbstractTableModel()
            {

                private static final long serialVersionUID = -5352934240965073481L;


                public int getColumnCount()
                {
                    return 3;
                }


                public int getRowCount()
                {
                    return activeList.size();
                }


                public Object getValueAt(int row, int column)
                {
                    DoctorAppointmentH doctorAppointment = activeList.get(row);

                    switch (column)
                    {
                        case 0:
                            return doctorAppointment.getDoctor().getName();

                        case 1:
                            return i18nControl.getMessage(doctorAppointment.getDoctor().getDoctorType().getName());

                        case 2:
                            return ATechDate.getDateTimeString(ATechDateType.DateAndTimeMin,
                                doctorAppointment.getDt_apoint());

                        default:
                            return null;
                    }
                }
            });

            String[] columns = { i18nControl.getMessage("NAME"), //
                                 i18nControl.getMessage("TYPE"), //
                                 i18nControl.getMessage("APPOINTMENT_DATE") };
            int[] cwidths = { 100, 100, 100 };
            int cwidth = 0;

            TableColumnModel cm = table.getColumnModel();

            for (int i = 0; i < columns.length; i++)
            {
                cm.getColumn(i).setHeaderValue(ic.getMessage(columns[i]));

                cwidth = cwidths[i];

                if (cwidth > 0)
                {
                    cm.getColumn(i).setPreferredWidth(cwidth);
                }
            }

            DataAccess.reSkinifyComponent(table);
        }

        return this.table;

    }


    @Override
    public String getTitle()
    {
        return i18nControl.getMessage("APPOINTMENT_LIST");
    }


    @Override
    public void init()
    {
        this.ic = DataAccess.getInstance().getI18nControlInstance();
        this.translation_root = "DOCTORS";

        // this.setCustomDisplayHeader(true);
        this.filter_type = FILTER_COMBO_AND_TEXT;

        // this.filter_enabled = true;

        // this.filter_type = GUIListDefAbstract.FILTER_COMBO_AND_TEXT;
        // this.filter_text = i18nControlAbstract.getMessage("FILTER") + ":";

        String s1[] = { ic.getMessage("STATUS_USED") + ":", ic.getMessage("DESCRIPTION") + ":" };
        this.filter_texts = s1;

        String s[] = { ic.getMessage("FILTER_ACTIVE"), ic.getMessage("FILTER_INACTIVE"), ic.getMessage("FILTER_ALL") };

        this.filter_options_combo1 = s;

        // FIXME
        this.button_defs = new ArrayList<ButtonDef>();
        // this.button_defs.add(new
        // LabelDef(this.i18nControl.getMessage("DOCTOR_MGMT"),
        // LabelDef.FONT_BOLD));
        // this.button_defs.add(
        // new ButtonDef(this.i18nControl.getMessage("MANAGE"), "manage_types",
        // "manage_types desc", "table_sql_check.png"));
        // this.button_defs.add(new
        // ButtonDef(this.i18nControl.getMessage("EDIT"),
        // "edit_type", "STOCKS_TABLE_EDIT_DESC",
        // "table_edit.png"));
        // this.button_defs.add(new
        // ButtonDef(this.i18nControlAbstract.getMessage("VIEW_TYPE"),
        // "view", "STOCKS_TABLE_VIEW_DESC", "table_view.png"));
        // this.button_defs.add(new DividerDef());
        // this.button_defs.add(new
        // LabelDef(this.i18nControl.getMessage("STOCKTAKING"),
        // LabelDef.FONT_BOLD));

        this.button_defs.add(
            new ButtonDef(this.ic.getMessage("NEW"), "add_appointment", "DOCTOR_APP_ADD_DESC", "table_add.png"));
        this.button_defs.add(
            new ButtonDef(this.ic.getMessage("EDIT"), "edit_appointment", "DOCTOR_APP_EDIT_DESC", "table_edit.png"));
        this.button_defs.add(new ButtonDef(this.ic.getMessage("DELETE"), "delete_appointment", "DOCTOR_APP_DELETE_DESC",
                "table_delete.png"));
        // this.button_defs.add(new DividerDef());
        // this.button_defs.add(new
        // ButtonDef(this.i18nControl.getMessage("EDIT_LIST"),
        // "edit_list", "STOCKS_TABLE_VIEW_DESC",
        // "table_view.png"));

        this.defaultParameters = new String[1];
        this.defaultParameters[0] = ic.getMessage("FILTER_ACTIVE");

        loadData();

    }


    public void loadData()
    {
        // System.out.println("Load Data. N/A !!!");
        // StocktakingDTO std = database.getLatestStocktakingDTO();

        // List<DoctorH> doctors = database.getDoctors();

        this.fullList = database.getDoctorAppointments();

        // System.out.println("doctors: " + this.fullList.size());

        // this.activeList = fullList;

        filterData();

        // this.activeList = dataAccess.getDb().getStocks(-1, -1);
    }


    public void filterData()
    {
        this.activeList = this.fullList;

        //
        //
        // List<DoctorH> list = new ArrayList<DoctorH>();
        //
        // if (i18nControl.getMessage("FILTER_ACTIVE").equals(filterComboText))
        // {
        // int today = (int) ATechDate.getATDateTimeFromGC(new
        // GregorianCalendar(), ATechDateType.DateOnly);
        //
        // for (DoctorH dh : this.fullList)
        // {
        // if ((dh.getActiveFrom() <= today) && ((dh.getActiveTill() == 0 ||
        // (dh.getActiveTill() >= today))))
        // {
        // list.add(dh);
        // }
        // }
        // }
        // else if
        // (i18nControl.getMessage("FILTER_INACTIVE").equals(filterComboText))
        // {
        // int today = (int) ATechDate.getATDateTimeFromGC(new
        // GregorianCalendar(), ATechDateType.DateOnly);
        //
        // for (DoctorH dh : this.fullList)
        // {
        // if ((dh.getActiveTill() != 0) && (dh.getActiveTill() < today))
        // {
        // list.add(dh);
        // }
        // }
        // }
        // else
        // {
        // list.addAll(this.fullList);
        // }
        //
        // if (StringUtils.isBlank(this.filterTextText))
        // {
        // this.activeList = list;
        // }
        // else
        // {
        // List<DoctorH> list2 = new ArrayList<DoctorH>();
        //
        // for (DoctorH doc : list)
        // {
        // if
        // (doc.getName().toUpperCase().contains(this.filterTextText.toUpperCase()))
        // {
        // list2.add(doc);
        // }
        // }
        //
        // this.activeList = list2;
        // }
        //
        // System.out.println("Filter Data. List: " + this.activeList.size());
        //
        // if ((table != null) && (table.getModel() != null))
        // {
        // ((AbstractTableModel) table.getModel()).fireTableDataChanged();
        // }

    }


    @Override
    public String getDefName()
    {
        return "DoctorAppointmentListDef";
    }


    @Override
    public Rectangle getTableSize(int pos_y)
    {
        return new Rectangle(40, pos_y, 480, 250);
    }


    @Override
    public Dimension getWindowSize()
    {
        return new Dimension(700, 500);
    }

    String filterComboText = "";
    String filterTextText = "";


    @Override
    public void setFilterCombo(String val)
    {
        this.filterComboText = val;
        filterData();
    }


    @Override
    public void setFilterText(String val)
    {
        this.filterComboText = val;
        filterData();
    }


    @Override
    public void setFilterCombo_2(String val)
    {
    }


    @Override
    public JPanel getCustomDisplayHeader()
    {
        return null;
        // JPanel panel = new JPanel();
        // panel.setLayout(null);
        // panel.setBackground(Color.green);
        // panel.setBounds(0, 0, 300, 55);
        //
        // ATSwingUtils.initLibrary();
        //
        // ATSwingUtils.getLabel("Test1:", 0, 0, 120, 25, panel);
        //
        // JLabel label_datetime = ATSwingUtils.getLabel("ccc", 130, 0, 90, 25,
        // panel);
        //
        // ATSwingUtils.getLabel("Test2:", 0, 30, 120, 25, panel);
        //
        // JLabel label_ddd = ATSwingUtils.getLabel("ccc", 130, 30, 90, 25,
        // panel);
        //
        // return panel;
    }


    @Override
    public void editTableRow()
    {
        System.out.println("editTableRow NA");
        // int index = this.getParentDialog().getSelectedObjectIndexFromTable();
        //
        // if (index > -1)
        // {
        // DoctorH doctor = this.activeList.get(index);
        //
        // DoctorDialog dialog = new DoctorDialog(this.getParentDialog(),
        // doctor, true);
        //
        // if (dialog.wasOperationSuccessful())
        // {
        // loadData();
        // }
        // }

    }


    @Override
    public String getHelpId()
    {
        return null;
    }

}
