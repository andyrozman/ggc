package ggc.plugin.graph.data;

import ggc.plugin.data.DeviceValuesDay;
import ggc.plugin.data.DeviceValuesEntry;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Hashtable;

public class GraphValuesCollection
{

    // Gregori

    // private ArrayList<DeviceValuesEntry> list = null;

    // Date Type
    private Hashtable<Integer, Hashtable<Integer, ArrayList<GraphValue>>> packed_collection = null;
    private Hashtable<Integer, ArrayList<GraphValue>> typed_collection = null;
    // DataAccessPlugInBase dataAccess;
    GregorianCalendar gc_from;
    GregorianCalendar gc_to;

    // DeviceValuesEntry dve;

    /**
     * Constructor
     * 
     * @param da
     * @param from 
     * @param to 
     */
    public GraphValuesCollection(GregorianCalendar from, GregorianCalendar to)
    {
        // this.dataAccess = da;
        // list = new ArrayList<DeviceValuesEntry>();
        // hash_table = new Hashtable<String,DeviceValuesDay>();
        this.gc_from = from;
        this.gc_to = to;
        // dve = dataAccess.getDataEntryObject();

        packed_collection = new Hashtable<Integer, Hashtable<Integer, ArrayList<GraphValue>>>();
        typed_collection = new Hashtable<Integer, ArrayList<GraphValue>>();

    }

    /**
     * Add Entry
     * 
     * @param pve DeviceValuesEntry instance (or derivate)
     */
    public void addEntry(GraphValue gve)
    {

        // private Hashtable<Long,Hashtable<Integer,ArrayList<GraphValue>>>
        // packed_collection = null;
        // private Hashtable<Integer,ArrayList<GraphValue>> typed_collection =
        // null;

        if (this.packed_collection.containsKey(gve.date))
        {
            if (!this.packed_collection.get(gve.date).containsKey(gve.value_type))
            {
                ArrayList<GraphValue> lst = new ArrayList<GraphValue>();
                this.packed_collection.get(gve.date).put(gve.value_type, lst);
                this.typed_collection.put(gve.value_type, lst);
            }

            this.packed_collection.get(gve.date).get(gve.value_type).add(gve);
            this.typed_collection.get(gve.value_type).add(gve);

        }
        else
        {
            Hashtable<Integer, ArrayList<GraphValue>> ht = new Hashtable<Integer, ArrayList<GraphValue>>();
            ArrayList<GraphValue> lst = new ArrayList<GraphValue>();
            lst.add(gve);

            ht.put(gve.value_type, lst);
            this.typed_collection.put(gve.value_type, lst);

            this.packed_collection.put(gve.date, ht);
        }

        /*
         * ATechDate atd = new ATechDate(pve.getDateTimeFormat(),
         * pve.getDateTime());
         * if (!this.hash_table.containsKey(atd.getDateFilenameString()))
         * {
         * DeviceValuesDay dvd = new DeviceValuesDay(this.dataAccess,
         * atd.getGregorianCalendar());
         * dvd.addEntry(pve);
         * this.hash_table.put(atd.getDateFilenameString(), dvd);
         * }
         * else
         * {
         * this.hash_table.get(atd.getDateFilenameString()).addEntry(pve);
         * }
         */
    }

    /**
     * Add Entry
     * 
     * @param dvd DeviceValuesDay instance (or derivate)
     */
    public void addEntry(DeviceValuesDay dvd)
    {
        /*
         * ATechDate atd = new ATechDate(dve.getDateTimeFormat(),
         * dvd.getDateTimeGC());
         * if (!this.hash_table.containsKey(atd.getDateFilenameString()))
         * {
         * this.hash_table.put(atd.getDateFilenameString(), dvd);
         * }
         * else
         * {
         * //Log.debug(message)
         * System.out.println("addEntry problem (DeviceValuesDay)");
         * }
         */
    }

    public void addCollection(ArrayList<GraphValue> list)
    {
        if (list == null)
            return;

        for (int i = 0; i < list.size(); i++)
        {
            this.addEntry(list.get(i));
        }
    }

    /**
     * Is Day Entry Available
     * 
     * @param dt
     * @return
     */
    public boolean isDayEntryAvailable(long dt)
    {
        /*
         * ATechDate atd = new ATechDate(dve.getDateTimeFormat(), dt);
         * return (this.hash_table.containsKey(atd.getDateFilenameString()));
         */

        return false;

    }

    /**
     * Is Entry Available
     * 
     * @param dt
     * @return
     */
    public boolean isEntryAvailable(long dt)
    {
        /*
         * if (isDayEntryAvailable(dt))
         * {
         * DeviceValuesDay dvd = getDayEntry(dt);
         * return dvd.isEntryAvailable(dt);
         * }
         * else
         * return false;
         */

        return false;
    }

    /**
     * Get Day Entry
     * 
     * @param dt
     * @return
     */
    public DeviceValuesDay getDayEntry(long dt)
    {
        /*
         * ATechDate atd = new ATechDate(dve.getDateTimeFormat(), dt);
         * return this.hash_table.get(atd.getDateFilenameString());
         */
        return null;
    }

    /**
     * Get Start GC
     * 
     * @return
     */
    public GregorianCalendar getStartGC()
    {
        return this.gc_from;
    }

    /**
     * Get End GC
     * 
     * @return
     */
    public GregorianCalendar getEndGC()
    {
        return this.gc_to;
    }

    /**
     * Get All Entries (of type DeviceValuesEntry)
     * @return
     */
    public ArrayList<DeviceValuesEntry> getAllEntries()
    {
        /*
         * ArrayList<DeviceValuesEntry> list = new
         * ArrayList<DeviceValuesEntry>();
         * for(Enumeration<DeviceValuesDay> en = this.hash_table.elements();
         * en.hasMoreElements(); )
         * {
         * DeviceValuesDay dvd = en.nextElement();
         * list.addAll(dvd.getList());
         * }
         * return list;
         */

        return null;

    }

}
