package ggc.meter.data;

import ggc.core.db.hibernate.DayValueH;
import ggc.core.db.hibernate.GGCHibernateObject;
import ggc.meter.util.DataAccessMeter;
import ggc.plugin.data.DeviceDataHandler;
import ggc.plugin.data.DeviceValuesEntry;
import ggc.plugin.data.DeviceValuesEntryInterface;
import ggc.plugin.data.DeviceValuesTableModel;

import java.util.ArrayList;

import com.atech.i18n.I18nControlAbstract;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Meter Tool (support for Meter devices)
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
 *  Filename:     MeterValuesTableModel
 *  Description:  MeterValues Table Model
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class MeterValuesExtTableModel extends DeviceValuesTableModel // extends
                                                                     // AbstractTableModel
{

    private I18nControlAbstract m_ic = DataAccessMeter.getInstance().getI18nControlInstance();

    // private String[] column_names = { m_ic.getMessage("DATETIME"),
    // m_ic.getMessage("BG_MMOLL"),
    // m_ic.getMessage("BG_MGDL"), m_ic.getMessage("STATUS"),
    // m_ic.getMessage(""), };

    private static final long serialVersionUID = -660580365600276458L;
    private int column_width[] = { 120, 150, 200, 90, 50 };
    private String column_names[] = { m_ic.getMessage("DATETIME"), m_ic.getMessage("ENTRY_TYPE"),
                                     m_ic.getMessage("VALUE"), m_ic.getMessage("STATUS"), "" };

    /**
     * Constructor
     * 
     * @param ddh DeviceDataHandler instance
     */
    public MeterValuesExtTableModel(DeviceDataHandler ddh, String source)
    {
        super(DataAccessMeter.getInstance(), ddh, source);
    }

    /**
     * Get Column Count
     * 
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount()
    {
        return 5;
    }

    /**
     * Get Checkable Column (one column if checkable, all others are non-editable)
     * 
     * @return
     */
    @Override
    public int getCheckableColumn()
    {
        return 4;
    }

    /**
     * Get Column Width
     * 
     * @param column column index
     * @param width width for column
     * @return calculated size of column
     */
    @Override
    public int getColumnWidth(int column, int width)
    {
        return this.column_width[column];
    }

    /**
     * Get Value At
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int row, int column)
    {
        MeterValuesEntry mve = (MeterValuesEntry) this.displayed_dl_data.get(row);

        switch (column)
        {
            case 0:
                return mve.getDateTimeObject().getDateTimeString();

            case 1:
                return mve.getExtendedTypeDescription();

            case 2:
                return mve.getExtendedTypeValue(true);

            case 3:
                return new Integer(mve.getStatus());

            case 4:
                return new Boolean(mve.getChecked());

            default:
                return "";
        }

    }

    /**
     * Process Device Value Entry
     * 
     * @param mve DeviceValuesEntry instance
     */
    @Override
    @SuppressWarnings("deprecation")
    public void processDeviceValueEntry(DeviceValuesEntryInterface mve)
    {
        // System.out.println("Old data: " + this.deviceDataHandler.getOldData());

        if (this.m_ddh.hasOldData())
        {
            // System.out.println("OLD Data" );
            if (!this.m_ddh.getOldData().containsKey("" + mve.getSpecialId()))
            {

                mve.setStatus(DeviceValuesEntry.STATUS_NEW);
                mve.setObjectStatus(DeviceValuesEntry.OBJECT_STATUS_NEW);
            }
            else
            {
                // System.out.println("MVE: " + mve.getSpecialId());
                // System.out.println("Found" );
                MeterValuesEntry mve2 = (MeterValuesEntry) mve;
                MeterValuesEntry mve_old = (MeterValuesEntry) this.m_ddh.getOldData().get(mve.getSpecialId());

                // DayValueH gvh = (DayValueH)this.deviceDataHandler.getOldData().get("" +
                // dt);
                // int vl =
                // Integer.parseInt(mve2.getBGValue(OutputUtil.BG_MGDL));
                mve_old.prepareEntry_v2();

                // System.out.println("MeterValuesExtTableModel: " +
                // mve_old.getDateTimeObject());
                // System.out.println("old=" + mve_old.getValueFull() + ", new="
                // + mve2.getValueFull());

                if (mve_old.getValueFull().equals(mve2.getValueFull()))
                {
                    mve2.setStatus(DeviceValuesEntry.STATUS_OLD);
                    mve2.object_status = DeviceValuesEntry.OBJECT_STATUS_OLD;
                }
                else
                {
                    mve2.setStatus(DeviceValuesEntry.STATUS_CHANGED);
                    mve2.object_status = DeviceValuesEntry.OBJECT_STATUS_EDIT;
                    mve2.entry_object = mve_old.getHibernateObject();
                }
            }
        }
        else
        {
            // System.out.println("oldData == null");
            mve.setStatus(DeviceValuesEntry.STATUS_NEW);
        }

    }

    /**
     * Get Column Name
     * 
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(int column)
    {
        return column_names[column];
    }

    /**
     * Get Column Class
     * 
     * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
     */
    @Override
    public Class<?> getColumnClass(int c)
    {
        Object o = getValueAt(0, c);
        if (o != null)
            return o.getClass();
        else
            return null;
        // return getValueAt(0,c).getClass();
    }

    /**
     * Is Cell Editable
     * 
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
     */
    @Override
    public boolean isCellEditable(int row, int col)
    {
        if (col == 4)
            return true;
        else
            return false;
    }

    /**
     * Set Value At
     * 
     * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
     */
    @Override
    public void setValueAt(Object aValue, int row, int column)
    {
        Boolean b = (Boolean) aValue;
        this.displayed_dl_data.get(row).setChecked(b.booleanValue());
        // System.out.println("set Value: rw=" + row + ",column=" + column +
        // ",value=" + aValue);
        // dayData.setValueAt(aValue, row, column);
        // fireTableChanged(null);
    }

    /**
     * Add To Array 
     * 
     * @param lst
     * @param source
     */
    @SuppressWarnings("unchecked")
    @Override
    public void addToArray(ArrayList<?> lst, ArrayList<?> source)
    {
        if (source == null || source.size() == 0)
            return;

        ArrayList<DayValueH> lst2 = (ArrayList<DayValueH>) lst;
        ArrayList<DayValueH> src2 = (ArrayList<DayValueH>) source;

        for (int i = 0; i < source.size(); i++)
        {
            lst2.add(src2.get(i));
        }
    }

    /**
     * Get Empty ArrayList
     * 
     * @return
     */
    @Override
    public ArrayList<? extends GGCHibernateObject> getEmptyArrayList()
    {
        return new ArrayList<DayValueH>();
    }

}
