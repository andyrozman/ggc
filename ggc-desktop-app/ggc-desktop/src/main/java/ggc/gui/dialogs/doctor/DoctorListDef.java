package ggc.gui.dialogs.doctor;

import java.awt.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.atech.db.hibernate.HibernateSelectableObject;
import com.atech.graphics.dialogs.guilist.ButtonDef;
import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;

import ggc.core.db.hibernate.doc.DoctorH;
import ggc.core.util.DataAccess;
import ggc.gui.dialogs.GGCListDef;

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
public class DoctorListDef extends GGCListDef
{

    /**
     * Constructor
     */
    public DoctorListDef()
    {
        super(DataAccess.getInstance(), //
                new DoctorH(), // object listed
                "DOCTOR_LIST", // title
                "DoctorListDef", // defintion name
                "Doc_DocList", // help Id
                new Rectangle(40, 0, 480, 250), // table bounds
                new Dimension(700, 500) // size
        );
    }


    @Override
    public boolean doCustomTableAction(String action)
    {
        return false;
    }


    @Override
    public void init()
    {
        this.i18nControl = DataAccess.getInstance().getI18nControlInstance();
        this.translationRoot = "DOCTORS";

        this.filterType = GuiListFilterType.ComboAndText;

        String s1[] = { i18nControl.getMessage("STATUS_USED") + ":", i18nControl.getMessage("DESCRIPTION") + ":" };
        this.filterDescriptionTexts = s1;

        String s[] = { i18nControl.getMessage("FILTER_ACTIVE"), i18nControl.getMessage("FILTER_INACTIVE"),
                       i18nControl.getMessage("FILTER_ALL") };

        this.filterOptionsCombo1 = s;

        this.buttonDefintions = new ArrayList<ButtonDef>();

        this.buttonDefintions.add(
            new ButtonDef(this.i18nControl.getMessage("NEW"), "add_object", "DOCTOR_ADD_DESC", "table_add.png"));
        this.buttonDefintions.add(
            new ButtonDef(this.i18nControl.getMessage("EDIT"), "edit_object", "DOCTOR_EDIT_DESC", "table_edit.png"));
        this.buttonDefintions.add(new ButtonDef(this.i18nControl.getMessage("DELETE"), "delete_object",
                "DOCTOR_DELETE_DESC", "table_delete.png"));

        this.defaultParameters = new String[1];
        this.defaultParameters[0] = i18nControl.getMessage("FILTER_ACTIVE");
    }


    public void filterData()
    {

        List<DoctorH> list = new ArrayList<DoctorH>();

        if (i18nControl.getMessage("FILTER_ACTIVE").equals(filterComboText))
        {
            int today = (int) ATechDate.getATDateTimeFromGC(new GregorianCalendar(), ATechDateType.DateOnly);

            for (HibernateSelectableObject object : this.fullList)
            {
                DoctorH dh = (DoctorH) object;
                if ((dh.getActiveFrom() <= today) && ((dh.getActiveTill() == 0 || (dh.getActiveTill() >= today))))
                {
                    list.add(dh);
                }
            }
        }
        else if (i18nControl.getMessage("FILTER_INACTIVE").equals(filterComboText))
        {
            int today = (int) ATechDate.getATDateTimeFromGC(new GregorianCalendar(), ATechDateType.DateOnly);

            for (HibernateSelectableObject object : this.fullList)
            {
                DoctorH dh = (DoctorH) object;

                if ((dh.getActiveTill() != 0) && (dh.getActiveTill() < today))
                {
                    list.add(dh);
                }
            }
        }
        else
        {
            for (HibernateSelectableObject object : this.fullList)
            {
                DoctorH app = (DoctorH) object;
                list.add(app);
            }
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

    }

}
