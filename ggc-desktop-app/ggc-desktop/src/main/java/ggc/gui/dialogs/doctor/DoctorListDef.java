package ggc.gui.dialogs.doctor;

import java.awt.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

import org.apache.commons.lang.StringUtils;

import com.atech.graphics.dialogs.guilist.ButtonDef;
import com.atech.graphics.dialogs.guilist.GUIListDefAbstract;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;

import ggc.core.db.GGCDb;
import ggc.core.db.hibernate.doc.DoctorH;
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
 *  Filename:     DoctorListDef
 *  Description:  GuiListDefintion for Doctor's List
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class DoctorListDef extends GUIListDefAbstract
{

    DataAccess m_da = DataAccess.getInstance();
    I18nControlAbstract i18nControl = m_da.getI18nControlInstance();

    private List<DoctorH> fullList = null;
    private List<DoctorH> activeList = null;

    private GGCDb database;


    /**
     * Constructor
     */
    public DoctorListDef()
    {
        database = m_da.getDb();
        init();
    }


    @Override
    public void doTableAction(String action)
    {
        if (action.equals("add_doctor"))
        {
            DoctorDialog d = new DoctorDialog(this.getParentDialog());

            if (d.wasOperationSuccessful())
            {
                this.loadData();
            }
        }
        else if (action.equals("edit_doctor"))
        {
            this.editTableRow();
        }
        else
        {
            System.out.println("DoctorListDef: Unknown action: " + action);
        }

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
                    return 2;
                }


                public int getRowCount()
                {
                    return activeList.size();
                }


                public Object getValueAt(int row, int column)
                {
                    DoctorH doctor = activeList.get(row);

                    switch (column)
                    {
                        case 0:
                            return doctor.getName();

                        case 1:
                            return i18nControl.getMessage(doctor.getDoctorType().getName());

                        default:
                            return null;
                    }
                }
            });

            String[] columns = { i18nControl.getMessage("NAME"), i18nControl.getMessage("TYPE") };
            int[] cwidths = { 100, 100 };
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
        return i18nControl.getMessage("DOCTOR_LIST");
    }


    @Override
    public void init()
    {
        this.ic = DataAccess.getInstance().getI18nControlInstance();
        this.translation_root = "DOCTORS";

        this.filter_type = FILTER_COMBO_AND_TEXT;

        String s1[] = { ic.getMessage("STATUS_USED") + ":", ic.getMessage("DESCRIPTION") + ":" };
        this.filter_texts = s1;

        String s[] = { ic.getMessage("FILTER_ACTIVE"), ic.getMessage("FILTER_INACTIVE"), ic.getMessage("FILTER_ALL") };

        this.filter_options_combo1 = s;

        this.button_defs = new ArrayList<ButtonDef>();

        this.button_defs
                .add(new ButtonDef(this.ic.getMessage("NEW"), "add_doctor", "DOCTOR_ADD_DESC", "table_add.png"));
        this.button_defs
                .add(new ButtonDef(this.ic.getMessage("EDIT"), "edit_doctor", "DOCTOR_EDIT_DESC", "table_edit.png"));

        this.defaultParameters = new String[1];
        this.defaultParameters[0] = ic.getMessage("FILTER_ACTIVE");

        loadData();
    }


    public void loadData()
    {
        this.fullList = database.getDoctors();
        filterData();
    }


    public void filterData()
    {

        List<DoctorH> list = new ArrayList<DoctorH>();

        if (ic.getMessage("FILTER_ACTIVE").equals(filterComboText))
        {
            int today = (int) ATechDate.getATDateTimeFromGC(new GregorianCalendar(), ATechDateType.DateOnly);

            for (DoctorH dh : this.fullList)
            {
                if ((dh.getActiveFrom() <= today) && ((dh.getActiveTill() == 0 || (dh.getActiveTill() >= today))))
                {
                    list.add(dh);
                }
            }
        }
        else if (ic.getMessage("FILTER_INACTIVE").equals(filterComboText))
        {
            int today = (int) ATechDate.getATDateTimeFromGC(new GregorianCalendar(), ATechDateType.DateOnly);

            for (DoctorH dh : this.fullList)
            {
                if ((dh.getActiveTill() != 0) && (dh.getActiveTill() < today))
                {
                    list.add(dh);
                }
            }
        }
        else
        {
            list.addAll(this.fullList);
        }

        if (StringUtils.isBlank(this.filterTextText))
        {
            this.activeList = list;
        }
        else
        {
            List<DoctorH> list2 = new ArrayList<DoctorH>();

            for (DoctorH doc : list)
            {
                if (doc.getName().toUpperCase().contains(this.filterTextText.toUpperCase()))
                {
                    list2.add(doc);
                }
            }

            this.activeList = list2;
        }

        System.out.println("Filter Data. List: " + this.activeList.size());

        if ((table != null) && (table.getModel() != null))
        {
            ((AbstractTableModel) table.getModel()).fireTableDataChanged();
        }

    }


    @Override
    public String getDefName()
    {
        return "DoctorListDef";
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
    }


    @Override
    public void editTableRow()
    {
        int index = this.getParentDialog().getSelectedObjectIndexFromTable();

        if (index > -1)
        {
            DoctorH doctor = this.activeList.get(index);

            DoctorDialog dialog = new DoctorDialog(this.getParentDialog(), doctor, true);

            if (dialog.wasOperationSuccessful())
            {
                loadData();
            }
        }

    }


    @Override
    public String getHelpId()
    {
        return "Doc_DocList";
    }

}
