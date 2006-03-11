package ggc.nutrition;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import ggc.db.datalayer.FoodDescription;
import ggc.db.datalayer.FoodGroup;
import ggc.db.datalayer.GGCDb;
import ggc.util.DataAccess;

/*
import com.atech.inf_sys.zis.data.DataAccess;
import com.atech.inf_sys.zis.datalayer.Diocese;
import com.atech.inf_sys.zis.datalayer.DiocesePerson;
import com.atech.inf_sys.zis.datalayer.InternalPerson;
import com.atech.inf_sys.zis.datalayer.MonasteryType;
import com.atech.inf_sys.zis.datalayer.ParishData;
import com.atech.inf_sys.zis.datalayer.ParishPerson;
import com.atech.inf_sys.zis.datalayer.PersonTypes;
import com.atech.inf_sys.zis.datalayer.ZISDb;
import com.atech.inf_sys.zisdb.db.diocese.DioceseH;
import com.atech.inf_sys.zisdb.db.misc.MonasteryTypeH;
*/

public class GGCTreeRoot 
{

    public static final int TREE_ROOT_NUTRITION = 1;
    public static final int TREE_ROOT_MEALS = 2;

/*
    ArrayList<Diocese> list_dio = new ArrayList<Diocese>();
    ArrayList<DioceseH> list_dio2 = new ArrayList<DioceseH>();
*/
    private int m_type = 1;

/*
    public Hashtable<String,ArrayList<ParishData>> list_by_dio = null;
    public Hashtable<String,ArrayList<ParishPerson>> list_by_par = null;

    public Hashtable<String,ArrayList<DiocesePerson>> list_dio_per = null;

    public ArrayList<PersonTypes> per_types = null;
    public ArrayList<InternalPerson> m_priests = null;
    public ArrayList<InternalPerson> m_catehists = null;
    public ArrayList<InternalPerson> m_coworkers = null;
    public ArrayList<InternalPerson> m_bishops = null;

    public ArrayList<MonasteryType> m_male_cnv = null;
    public ArrayList<MonasteryType> m_female_cnv = null;

    public Hashtable<String, ArrayList<InternalPerson>> m_cnv_persons = null;
*/

    public ArrayList m_foodGroups = null;
    public Hashtable m_foodDescByGroup = null;


    public GGCTreeRoot(int type) 
    {
        m_type = type;

        GGCDb db = DataAccess.getInstance().getDb();

        if (type==1)
        {
	    m_foodGroups = db.getFoodGroups();
	    Iterator it = m_foodGroups.iterator();

	    m_foodDescByGroup = new Hashtable();

	    while (it.hasNext())
	    {
		FoodGroup fg = (FoodGroup)it.next();
		m_foodDescByGroup.put(""+fg.getId(), new ArrayList());
	    }

	    
	    ArrayList list = db.getFoodDescriptions();
	    it = list.iterator();

	    while (it.hasNext())
	    {
		FoodDescription fd = (FoodDescription)it.next();

		ArrayList al = (ArrayList)m_foodDescByGroup.get(""+fd.getFood_group_id());
		al.add(fd);
	    }


	    //m_foodDescByFG = 
/*
	    list_dio = db.getDioceses();
            list_dio2 = db.getDiocesesH();
            list_by_dio = new Hashtable<String,ArrayList<ParishData>>();
            list_dio_per = new Hashtable<String,ArrayList<DiocesePerson>>();


            Iterator it = list_dio2.iterator();

            while (it.hasNext())
            {
                DioceseH di = (DioceseH)it.next();
                list_by_dio.put(""+di.getId(), db.getParishData(di));
                list_dio_per.put(""+di.getId(), db.getDiocesePersons(di));
            }

            db.getParishPersonListByParish(list_by_dio, this);
*/
        }
        else
        {
	    // meals -- Not implemented yet
/*            per_types = db.getPersonTypes();
            System.out.println("Per types: " + per_types.size());

            this.m_priests = db.getInternalPersonsByType(1);
            this.m_catehists = db.getInternalPersonsByType(4);
            this.m_coworkers = db.getInternalPersonsByType(5);
            this.m_bishops = db.getInternalPersonsByType(6);

            //ArrayList<MonasteryType> lst = db.getMonasteryTypes();

            this.m_male_cnv = db.getMonasteryTypes(true);
            this.m_female_cnv = db.getMonasteryTypes(false);

            m_cnv_persons = new Hashtable<String, ArrayList<InternalPerson>>();


            Iterator it = m_male_cnv.iterator();

            while(it.hasNext())
            {
                MonasteryType mnt = (MonasteryType)it.next();
                m_cnv_persons.put("1_" + mnt.getId(), db.getMonasteryPersons((MonasteryTypeH)mnt, true));
            }

            it = m_female_cnv.iterator();

            while(it.hasNext())
            {
                MonasteryType mnt = (MonasteryType)it.next();
                m_cnv_persons.put("2_" + mnt.getId(), db.getMonasteryPersons((MonasteryTypeH)mnt, false));
            }
	    */


        }
        
    }

    public String toString()
    {

	if (m_type==1)
            return DataAccess.getInstance().m_i18n.getMessage("NUTRITION_DATA");
        else
            return DataAccess.getInstance().m_i18n.getMessage("MEALS");

    }


}
