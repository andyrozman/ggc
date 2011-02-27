package ggc.meter.data;

import ggc.core.db.hibernate.DayValueH;
import ggc.core.db.hibernate.pump.PumpDataExtendedH;
import ggc.meter.data.db.GGCMeterDb;
import ggc.meter.device.MeterInterface;
import ggc.meter.util.DataAccessMeter;
import ggc.plugin.data.DeviceDataHandler;
import ggc.plugin.data.DeviceValuesEntry;
import ggc.plugin.data.DeviceValuesTableModel;
import ggc.plugin.util.DataAccessPlugInBase;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;


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
 *  Filename:     MeterDataHandler
 *  Description:  Data Handler for Meter Tool
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class MeterDataHandler extends DeviceDataHandler
{

    MeterValuesExtTableModel m_model2;
    
    /**
     * Constructor
     * 
     * @param da
     */
    public MeterDataHandler(DataAccessPlugInBase da)
    {
        super(da);
    }
    

    
    
    /**
     * Execute export Db
     * 
     * @see ggc.plugin.data.DeviceDataHandler#executeExportDb()
     */
    public void executeExportDb()
    {
//        System.out.println("executeExportDb");
        
//        System.out.println("  -- Start");
        
        // Andy [8.3.2010] - in V1 we used setReturnData to write data into database, now each
        //      plugin has it's own access to database. 
        //      With glucose meters, that can measure also other stuff (like urine) we also needed
        //      to rewrite this writing code a lot. Since this code has become very complicated,
        //      there will be a lot of comments here.

        //this.m_server.setReturnData(this.getDeviceValuesTableModel().getCheckedEntries(), this);
        
        GGCMeterDb db = ((DataAccessMeter)m_da).getDb();
            
        Hashtable<String, ArrayList<DeviceValuesEntry>> ll = getDeviceValuesTableModel().getCheckedDVE(); 
        //    this.m_model.getCheckedDVE();
            
//        System.out.println("Size [add]: " + ll.get("ADD").size());
//        System.out.println("Size [edit]: " + ll.get("EDIT").size());
        
        
        //float full_count = 0.0f;
        
        Hashtable<Long,MeterValuesEntry> ht_add = new Hashtable<Long,MeterValuesEntry>(); 
        Hashtable<Long,MeterValuesEntry> ht_edit = new Hashtable<Long,MeterValuesEntry>(); 
        Hashtable<Long,ArrayList<MeterValuesEntry>> ht_add_edit = new Hashtable<Long,ArrayList<MeterValuesEntry>>();
        
        // this are time_marks for pump data retrieval
        Hashtable<Long,String> time_marks = new Hashtable<Long,String>();
        
        // time marks for meter data retrieval
        Hashtable<Long,String> time_marks_meter = new Hashtable<Long,String>();
        
        // we create separate hashtables, if we find same item we create entry into special hashtable
        // we leave base entry inside, so that we can easily find if we have special cases
        for ( DeviceValuesEntry doh : ll.get("ADD")  )
        {
            MeterValuesEntry mve = (MeterValuesEntry)doh;
            
            long d = mve.getDateTime();
            d *= 100;
            
            if (!time_marks.containsKey(d))
                time_marks.put(d, "");
            
            if (ht_add.containsKey(mve.getDateTime()))
            {
                addToSpecialHTable(ht_add_edit, mve);
            }
            else
            {
                ht_add.put(mve.getDateTime(), mve);
            }
        }
        
        
        for ( DeviceValuesEntry doh : ll.get("EDIT")  )
        {
            MeterValuesEntry mve = (MeterValuesEntry)doh;

            long d = mve.getDateTime();
            d *= 100;
            
            if (!time_marks.containsKey(d))
                time_marks.put(d, "");
            
            if (ht_add.containsKey(mve.getDateTime()))
            {
                addToSpecialHTable(ht_add_edit, mve);
            }
            else if (ht_edit.containsKey(mve.getDateTime()))
            {
                addToSpecialHTable(ht_add_edit, mve);
            }
            else
            {
                ht_edit.put(mve.getDateTime(), mve);
            }
        }
        
        // now we remove duplicates (this are entries that happened on same time, but different 
        // event), and we also create time marks for comparing MVEs
        for(Enumeration<Long> en = ht_add_edit.keys(); en.hasMoreElements(); )
        {
            long key = en.nextElement();
            
//            System.out.println("" + key);
            
            if (!time_marks_meter.containsKey(key))
                time_marks_meter.put(key, "");

            
            
            if (ht_add.containsKey(key))
            {
                ht_add_edit.get(key).add(ht_add.get(key));
                ht_add.remove(key);
            }
            
            if (ht_edit.containsKey(key))
            {
                ht_add_edit.get(key).add(ht_edit.get(key));
                ht_edit.remove(key);
            }
        }
        
        this.setCustomStatus(1, 5);

        
        
        // get list
        this.element_count_all = 0;
        this.element_count_all += ht_add.size();
        this.element_count_all += ht_edit.size();
        this.element_count_all += ht_add_edit.size();
        
        // get pump entries
        this.element_count_all += ll.get("ADD").size();
        this.element_count_all += ll.get("EDIT").size();
        

//        System.out.println("  -- Element count: " + this.element_count_all);
        
        
        // we retrieve list of all relevant pump reading
        Hashtable<String, PumpDataExtendedH> pump_data = db.getPumpData(time_marks);
        this.setCustomStatus(1, 6);
        
        
        // we retrieve list of all relevant meter readings
        Hashtable<Long, MeterValuesEntry> meter_compare_data = db.getMeterData(time_marks_meter);
        this.setCustomStatus(1, 10);
        
        
        
        ArrayList<MeterValuesEntrySpecial> list_for_pump = new ArrayList<MeterValuesEntrySpecial>(); 
        
        //for(int i=0; i<2; i++)
        for(int i=0; i<2; i++)
        {
            Hashtable<Long,MeterValuesEntry> ht = null;
            
            boolean add = false;
            
            if (i==0)
            {
                ht = ht_add;
                add = true;
            }
            else
                ht = ht_edit;
            
            
            // write elements to add/edit
            for(Enumeration<Long> en = ht.keys(); en.hasMoreElements(); )
            {
                long key = en.nextElement();
                MeterValuesEntry mve = ht.get(key);
                
                // process pump data, since we now support more than one data type, we have a 
                // little more to do
                list_for_pump.addAll(mve.getDataEntriesAsMVESpecial());
                
                if (add)
                    db.add(mve);
                else
                    db.edit(mve);
                    
                setCustomStatus(2, 0);
                
            } // for
        }
        
        
        // same records add_edit (first we go through all for same day and add them as pump entries,
        //   then we merge them and write them to Pen/Injection mode 
        
        for(Enumeration<Long> en = ht_add_edit.keys(); en.hasMoreElements(); )
        {
            long key = en.nextElement();
            
            ArrayList<MeterValuesEntry> list = ht_add_edit.get(key);
            
            // process pump data, since we now support more than one data type, we have a 
            // little more to do
            
            MeterValuesEntry mve_main = null;
            
            for(int i=0; i<list.size(); i++)
            {
                if (i==0)
                {
                    mve_main = list.get(0);
                }
                else
                {
                    mve_main.addSpecialEntryAll(list.get(i).getDataEntriesAsMVESpecial());
                }
            }
                
            // check if we do add or edit
            
            if (!meter_compare_data.containsKey(mve_main.getDateTime()))
            {
                db.add(mve_main);
            }
            else
            {
                // if data not same edit, else ignore
                if (!meter_compare_data.get(mve_main.getDateTime()).getValueFull().equals(mve_main.getValueFull()))
                {
                    MeterValuesEntry mve2 = meter_compare_data.get(mve_main.getDateTime());
                    
                    mve_main.setId(mve2.getId());
                    mve_main.addSpecialEntryAll(mve2.getDataEntriesAsMVESpecial());
                    db.edit(mve_main);
                }
            }
            
            list_for_pump.addAll(mve_main.getDataEntriesAsMVESpecial());
            
            setCustomStatus(2, 0);
                
        }
        
        // process pump entries
        PumpDataExtendedH pdeh = null;
        
        for(int i=0; i<list_for_pump.size(); i++)
        {
            
            if (!pump_data.containsKey(list_for_pump.get(i).getPumpCompareId()))
            {
                MeterValuesEntrySpecial mves = list_for_pump.get(i);
                
                // add
                pdeh = new PumpDataExtendedH();
                
                pdeh = new PumpDataExtendedH();
                pdeh.setDt_info((mves.datetime_tag * 100));
                pdeh.setType(mves.getPumpMappedType()); 
                pdeh.setValue(mves.getPackedValue());
                pdeh.setPerson_id((int)m_da.getCurrentUserId());
                pdeh.setComment("");
                pdeh.setExtended("SOURCE=" + m_da.getSourceDevice());
                pdeh.setChanged(System.currentTimeMillis());

                db.addHibernate(pdeh);
            }
            else
            {
                // edit
                if (!pump_data.get(list_for_pump.get(i).getPumpCompareId()).getValue().equals(list_for_pump.get(i).getPackedValue()))
                {
                    pdeh = pump_data.get(list_for_pump.get(i).getPumpCompareId());
                    pdeh.setValue(list_for_pump.get(i).getPackedValue());
                    pdeh.setExtended("SOURCE=" + m_da.getSourceDevice());
                    pdeh.setChanged(System.currentTimeMillis());

                    db.editHibernate(pdeh);
                }
            }

            setCustomStatus(2, 0);
        }
        
        
        setCustomStatus(3, 0);

//        System.out.println("  -- End");
        
    }
    
    
    
    
    /**
     * Execute export Db
     * 
     * @see ggc.plugin.data.DeviceDataHandler#executeExportDb()
     */
    public void executeExportDb_Old()
    {
/*        
        
//        System.out.println("executeExportDb");
//        System.out.println("  -- Start");
        
        // Andy [8.3.2010] - in V1 we used setReturnData to write data into database, now each
        //      plugin has it's own access to database. 
        //      With glucose meters, that can measure also other stuff (like urine) we also needed
        //      to rewrite this writing code a lot. Since this code has become very complicated,
        //      there will be a lot of comments here.

        //this.m_server.setReturnData(this.getDeviceValuesTableModel().getCheckedEntries(), this);
        
        GGCMeterDb db = ((DataAccessMeter)m_da).getDb();
            
        Hashtable<String, ArrayList<DeviceValuesEntry>> ll = getDeviceValuesTableModel().getCheckedDVE(); 
        //    this.m_model.getCheckedDVE();
            
//        System.out.println("Size [add]: " + ll.get("ADD").size());
//        System.out.println("Size [edit]: " + ll.get("EDIT").size());
        
        Hashtable<Long,MeterValuesEntry> ht_add = new Hashtable<Long,MeterValuesEntry>(); 
        Hashtable<Long,MeterValuesEntry> ht_edit = new Hashtable<Long,MeterValuesEntry>(); 
        Hashtable<Long,ArrayList<MeterValuesEntry>> ht_add_edit = new Hashtable<Long,ArrayList<MeterValuesEntry>>();
        
        // this are time_marks for pump data retrieval
        Hashtable<Long,String> time_marks = new Hashtable<Long,String>(); 
        
        // we create separate hashtables, if we find same item we create entry into special hashtable
        // we leave base entry inside, so that we can easily find if we have special cases
        for ( DeviceValuesEntry doh : ll.get("ADD")  )
        {
            MeterValuesEntry mve = (MeterValuesEntry)doh;
            
            long d = mve.getDateTime();
            d *= 100;
            
            if (!time_marks.containsKey(d))
                time_marks.put(d, "");
            
            if (ht_add.containsKey(mve.getDateTime()))
            {
                addToSpecialHTable(ht_add_edit, mve);
            }
            else
            {
                ht_add.put(mve.getDateTime(), mve);
            }
        }
        
        
        for ( DeviceValuesEntry doh : ll.get("EDIT")  )
        {
            MeterValuesEntry mve = (MeterValuesEntry)doh;

            long d = mve.getDateTime();
            d *= 100;
            
            if (!time_marks.containsKey(d))
                time_marks.put(d, "");
            
            if (ht_add.containsKey(mve.getDateTime()))
            {
                addToSpecialHTable(ht_add_edit, mve);
            }
            else if (ht_edit.containsKey(mve.getDateTime()))
            {
                addToSpecialHTable(ht_add_edit, mve);
            }
            else
            {
                ht_edit.put(mve.getDateTime(), mve);
            }
        }
        
        // now we remove duplicates (this are entries that happened on same time, but different event)
        for(Enumeration<Long> en = ht_add_edit.keys(); en.hasMoreElements(); )
        {
            long key = en.nextElement();
            
            System.out.println("" + key);
            
            if (ht_add.containsKey(key))
            {
                ht_add_edit.get(key).add(ht_add.get(key));
                ht_add.remove(key);
            }
            
            if (ht_edit.containsKey(key))
            {
                ht_add_edit.get(key).add(ht_edit.get(key));
                ht_edit.remove(key);
            }
        }
        
        this.setCustomStatus(1, 5);

        
        
        // get list
        this.element_count_all = 0;
        this.element_count_all += ht_add.size();
        this.element_count_all += ht_edit.size();
        this.element_count_all += ht_add_edit.size();

//        System.out.println("  -- Element count: " + this.element_count_all);
        
        
        // we retrieve list of all pump reading
        Hashtable<String, PumpDataExtendedH> pump_data = db.getPumpData(time_marks);
        this.setCustomStatus(1, 10);
        
        
        
        //for(int i=0; i<2; i++)
        for(int i=0; i<2; i++)
        {
            Hashtable<Long,MeterValuesEntry> ht = null;
            
            boolean add = false;
            
            if (i==0)
            {
                ht = ht_add;
                add = true;
            }
            else
                ht = ht_edit;
            
            
            // write elements to add/edit
            for(Enumeration<Long> en = ht.keys(); en.hasMoreElements(); )
            {
                long key = en.nextElement();
                MeterValuesEntry mve = ht.get(key);
                
                // process pump data, since we now support more than one data type, we have a 
                // little more to do
                
                
                
                
                if (mve.getPumpMappedType()!=-1)
                {
                    PumpDataExtendedH pdeh = null;
                    
                    if (pump_data.containsKey(mve.getDateTime() + "_" + mve.getPumpMappedType()))
                    {
                        // edit
                        pdeh = pump_data.get(mve.getDateTime() + "_" + mve.getPumpMappedType());
                        
                        
                        if (mve.isSpecialEntry())
                        {
                            if (mve.doWeTransferUnitsForSpecialEntry())
                            {
                                if (!pdeh.getValue().equals(mve.getSpecialEntryValue()))
                                {
                                    pdeh.setValue(mve.getSpecialEntryValue());
                                    db.editHibernate(pdeh);
                                }
                            }
                            else
                            {
                                if (!pdeh.getValue().equals(mve.getSpecialEntryValueWithoutUnit()))
                                {
                                    pdeh.setValue(mve.getSpecialEntryValueWithoutUnit());
                                    db.editHibernate(pdeh);
                                }
                            }
                            
                        }
                        else
                        {
                            if (!pdeh.getValue().equals(mve.getValue()))
                            {
                                pdeh.setValue(mve.getValue());
                                db.editHibernate(pdeh);
                            }
                        }
                        
                    }
                    else
                    {
                        // add
                        pdeh = new PumpDataExtendedH();
                        pdeh.setDt_info((mve.getDateTime() * 100));
                        pdeh.setType(mve.getPumpMappedType()); 

                        if (mve.isSpecialEntry())
                        {
                            if (mve.doWeTransferUnitsForSpecialEntry())
                                pdeh.setValue(mve.getSpecialEntryValue());
                            else
                                pdeh.setValue(mve.getSpecialEntryValueWithoutUnit());
                        }
                        else
                        {
                            pdeh.setValue(mve.getValue());
                        }
    
                        pdeh.setPerson_id((int)m_da.getCurrentUserId());
                        pdeh.setComment("");
                        pdeh.setExtended("SOURCE=" + m_da.getSourceDevice());
                        pdeh.setChanged(System.currentTimeMillis());

                        db.addHibernate(pdeh);
                    }
                    
                    // set value
                    
                    
                }
                
                
                if (add)
                    db.add(mve);
                else
                    db.edit(mve);
                    
                setCustomStatus(2, 0);
                
            } // for
        }
        
        
        // same records add_edit (first we go through all for same day and add them as pump entries,
        //   then we merge them and write them to Pen/Injection mode 
        
        for(Enumeration<Long> en = ht_add_edit.keys(); en.hasMoreElements(); )
        {
            long key = en.nextElement();
            
            ArrayList<MeterValuesEntry> list = ht_add_edit.get(key);
            
            //MeterValuesEntry mve = ht_add_edit.get(key);
            
            // process pump data, since we now support more than one data type, we have a 
            // little more to do
            
            
            for(int i=0; i<list.size(); i++)
            {
            
            
            if (mve.getPumpMappedType()!=-1)
            {
                PumpDataExtendedH pdeh = null;
                
                if (pump_data.containsKey(mve.getDateTime() + "_" + mve.getPumpMappedType()))
                {
                    // edit
                    pdeh = pump_data.get(mve.getDateTime() + "_" + mve.getPumpMappedType());
                    
                    
                    if (mve.isSpecialEntry())
                    {
                        if (mve.doWeTransferUnitsForSpecialEntry())
                        {
                            if (!pdeh.getValue().equals(mve.getSpecialEntryValue()))
                            {
                                pdeh.setValue(mve.getSpecialEntryValue());
                                db.editHibernate(pdeh);
                            }
                        }
                        else
                        {
                            if (!pdeh.getValue().equals(mve.getSpecialEntryValueWithoutUnit()))
                            {
                                pdeh.setValue(mve.getSpecialEntryValueWithoutUnit());
                                db.editHibernate(pdeh);
                            }
                        }
                        
                    }
                    else
                    {
                        if (!pdeh.getValue().equals(mve.getValue()))
                        {
                            pdeh.setValue(mve.getValue());
                            db.editHibernate(pdeh);
                        }
                    }
                    
                }
                else
                {
                    // add
                    pdeh = new PumpDataExtendedH();
                    pdeh.setDt_info((mve.getDateTime() * 100));
                    pdeh.setType(mve.getPumpMappedType()); 

                    if (mve.isSpecialEntry())
                    {
                        if (mve.doWeTransferUnitsForSpecialEntry())
                            pdeh.setValue(mve.getSpecialEntryValue());
                        else
                            pdeh.setValue(mve.getSpecialEntryValueWithoutUnit());
                    }
                    else
                    {
                        pdeh.setValue(mve.getValue());
                    }

                    pdeh.setPerson_id((int)m_da.getCurrentUserId());
                    pdeh.setComment("");
                    pdeh.setExtended("SOURCE=" + m_da.getSourceDevice());
                    pdeh.setChanged(System.currentTimeMillis());

                    db.addHibernate(pdeh);
                }
                
                // set value
                
                
            }
            
            
            if (add)
                db.add(mve);
            else
                db.edit(mve);
                
            }
            
            setCustomStatus(2, 0);
                
        }
        
        
        

        // merge special types and send them to hibernate with addedit option (for meters) and 
        // each component for pump
        setCustomStatus(3, 0);

//        System.out.println("  -- End");
*/        
    }
    
    
    int current_static_stat = 0;
    int element_current = 0;
    int element_count_all = 0;
    
    
    
    private void setCustomStatus(int type, int number)
    {
        
        // type: 1 - just add, 2 - calculate *must be less than 100, 3 - finish
        if (type==1)
        {
            this.current_static_stat = number;
            this.setStatus(current_static_stat);
        }
        else if (type==2)
        {
            // FIXME
            
            element_current++;
            
            float f = ((element_current*(1.0f)) / element_count_all) * 90.0f;
            f += current_static_stat;
            
            int proc = (int)f;
            
            if (proc==100)
                proc = 99;
            
            this.setStatus(proc);
        }
        else if (type==3)
        {
            this.setStatus(100);
            this.export_dialog.setReadingFinished();
        }
        
        
    }
    
    
    
    
    private void addToSpecialHTable(Hashtable<Long,ArrayList<MeterValuesEntry>> ht_add_edit, MeterValuesEntry dvh)
    {
        if (ht_add_edit.containsKey(dvh.getDateTime()))
        {
            ht_add_edit.get(dvh.getDateTime()).add(dvh);
        }
        else
        {
            ArrayList<MeterValuesEntry> al = new ArrayList<MeterValuesEntry>();
            al.add(dvh);
            ht_add_edit.put(dvh.getDateTime(), al);
        }
    }
    
    
    
    /**
     * Execute Export Other (not supported for now)
     * 
     * @see ggc.plugin.data.DeviceDataHandler#executeExportOther()
     */
    public void executeExportOther()
    {
    }
    
    
    /**
     * Create Device Values Table Model
     */
    public void createDeviceValuesTableModel()
    {
        this.m_model = new MeterValuesTableModel(this, m_da.getSourceDevice());
        this.m_model2 = new MeterValuesExtTableModel(this, m_da.getSourceDevice());
    }


    
    /**
     * Get Device Values Table Model (for this tool we override main method)
     * 
     * @return DeviceValuesTableModel instance (or derivate thereof)
     */
    public DeviceValuesTableModel getDeviceValuesTableModel()
    {
        if (m_model==null)
            createDeviceValuesTableModel();
        
        MeterInterface mi = (MeterInterface)m_da.getSelectedDeviceInstance();
        
//        System.out.println("getDeviceValuesTableModel(): " + mi.getInterfaceTypeForMeter() );
        
        if (mi.getInterfaceTypeForMeter()==MeterInterface.METER_INTERFACE_SIMPLE)
            return m_model;
        else
            return m_model2;
    }
    
    
    
    

    /**
     * Set Device Data
     * 
     * @param data data as Hashtable<String,?> data
     */
    @SuppressWarnings("unchecked")
    @Override
    public void setDeviceData(Hashtable<String, ?> data)
    {
        if ((data==null) || (data.size()==0))
        {
            //System.out.println("NO Old data: " + old_data);
            old_data = new Hashtable<String,DayValueH>();
        }
        else
        {
            old_data = (Hashtable<String,DayValueH>)data;
            //System.out.println("Old data: " + old_data);
        }
    }




    /** 
     * Set Reading Finished
     */
    public void setReadingFinished()
    {
        // TODO Auto-generated method stub
        
    }

    
    
    
    
    
}	