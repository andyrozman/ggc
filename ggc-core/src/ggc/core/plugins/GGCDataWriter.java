/*
 * Created on 12.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */

package ggc.core.plugins; //com.atech.db;

import ggc.core.db.GGCDb;
import ggc.core.db.hibernate.DayValueH;
import ggc.core.db.hibernate.pump.PumpDataExtendedH;
import ggc.core.util.DataAccess;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import com.atech.db.DbDataWriterAbstract;
import com.atech.graphics.components.StatusReporterInterface;

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
 *  Filename:     GGCDataWriter  
 *  Description:  Class for writing data in separate thread
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class GGCDataWriter extends DbDataWriterAbstract
{

    /**
     * Data: None
     */
    public static final int DATA_NONE = 0;

    /**
     * Data: Meter
     */
    public static final int DATA_METER = 1;

    protected int current_status = 0;
    protected StatusReporterInterface stat_rep_int = null;
    protected Hashtable<String, ArrayList<DayValueH>> meter_data;
    protected GGCDb db = null;

    /**
     * Constructor
     * 
     * @param type
     * @param data
     * @param stat_rep_int
     */
    @SuppressWarnings("unchecked")
    public GGCDataWriter(int type, Object data, StatusReporterInterface stat_rep_int)
    {
        super(type, stat_rep_int);
        this.selected_data_type = type;

        this.meter_data = (Hashtable<String, ArrayList<DayValueH>>) data;
        // this.data = (ArrayList<MeterValuesEntry>)data;
        this.stat_rep_int = stat_rep_int;

        this.db = DataAccess.getInstance().getDb();

    }

    boolean running = true;

    /** 
     * Run - method for running thread
     */
    @Override
    public void run()
    {
        try
        {
            Thread.sleep(1000);
        }
        catch (Exception ex)
        {}

        while (running)
        {
            int full_count = 0;
            int current = 0;

            full_count += this.meter_data.get("ADD").size();
            full_count += this.meter_data.get("EDIT").size();

            for (Enumeration<String> en = this.meter_data.keys(); en.hasMoreElements();)
            {
                String key = en.nextElement();
                ArrayList<DayValueH> lst = this.meter_data.get(key);

                for (int i = 0; i < lst.size(); i++)
                {
                    if (key.equals("ADD"))
                    {
                        DayValueH dv = lst.get(i);
                        dv.setPerson_id((int) DataAccess.getInstance().getCurrentUserId());
                        db.addHibernate(dv);

                        PumpDataExtendedH pde = new PumpDataExtendedH();
                        pde.setDt_info(dv.getDt_info() * 100);
                        pde.setType(3); // carefull if this is changed in Pump
                                        // Tool it will need to be changed
                        pde.setValue("" + dv.getBg());
                        pde.setPerson_id(dv.getPerson_id());
                        pde.setComment(dv.getComment());
                        pde.setExtended(dv.getExtended());
                        pde.setChanged(dv.getChanged());
                        db.addHibernate(pde);

                    }
                    else
                    {
                        db.editHibernate(lst.get(i));
                    }

                    current++;

                    float f = current / (full_count * 1.0f);

                    int pr = (int) (100.0f * f);

                    this.stat_rep_int.setStatus(pr);

                } // for i
            } // for enum

            this.stat_rep_int.setStatus(100);
            this.running = false;

        } // while running
    }

}
