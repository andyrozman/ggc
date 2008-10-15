package ggc.mobile.db.objects;

import java.sql.Connection;
import java.sql.ResultSet;

import com.atech.mobile.db.objects.DatabaseAccessObject;

public class DayValueDAO extends DatabaseAccessObject
{
    
    public static final int CHANGED_ID = 1;
    public static final int CHANGED_DT_INFO = 2;
    public static final int CHANGED_BG = 4;
    public static final int CHANGED_INS1 = 8;
    public static final int CHANGED_INS2 = 16;
    public static final int CHANGED_CH = 32;
    public static final int CHANGED_MEALS_IDS = 64;
    public static final int CHANGED_EXTENDED = 128;
    public static final int CHANGED_PERSON_ID = 256;
    public static final int CHANGED_COMMENT = 512;
    public static final int CHANGED_CHANGED = 1024;
    
    
    /*  identifier field */
    protected long id;

    /*  persistent field */
    protected long dt_info;

    /*  nullable persistent field */
    protected int bg;

    /*  nullable persistent field */
    protected int ins1;

    /*  nullable persistent field */
    protected int ins2;

    /*  nullable persistent field */
    protected float ch;

    /*  nullable persistent field */
    protected String meals_ids;

    /*  nullable persistent field */
    protected String extended;

    /*  persistent field */
    protected int person_id;

    /*  nullable persistent field */
    protected String comment;

    /*  nullable persistent field */
    protected long changed;

    /*  full constructor */
    public DayValueDAO(long dt_info, int bg, int ins1, int ins2, float ch, String meals_ids, String extended,
            int person_id, String comment, long changed)
    {
        this.dt_info = dt_info;
        this.bg = bg;
        this.ins1 = ins1;
        this.ins2 = ins2;
        this.ch = ch;
        this.meals_ids = meals_ids;
        this.extended = extended;
        this.person_id = person_id;
        this.comment = comment;
        this.changed = changed;
    }

    /*  default constructor */
    public DayValueDAO()
    {
    }

    public long getId()
    {
        return this.id;
    }

    public void setId(long id)
    {
        this.id = this.setChangedValueLong(this.id, id, DayValueDAO.CHANGED_ID);
    }

    public long getDt_info()
    {
        return this.dt_info;
    }

    public void setDt_info(long dt_info)
    {
        this.dt_info = this.setChangedValueLong(this.dt_info, dt_info, DayValueDAO.CHANGED_DT_INFO);
    }

    public int getBg()
    {
        return this.bg;
    }

    public void setBg(int bg)
    {
        this.bg = this.setChangedValueInt(this.bg, bg, DayValueDAO.CHANGED_BG);
    }

    public int getIns1()
    {
        return this.ins1;
    }

    public void setIns1(int ins1)
    {
        this.ins1 = this.setChangedValueInt(this.ins1, ins1, DayValueDAO.CHANGED_INS1);
    }

    public int getIns2()
    {
        return this.ins2;
    }

    public void setIns2(int ins2)
    {
        this.ins2 = ins2;
    }

    public float getCh()
    {
        return this.ch;
    }

    public void setCh(float ch)
    {
        this.ch = ch;
    }

    public String getMeals_ids()
    {
        return this.meals_ids;
    }

    public void setMeals_ids(String meals_ids)
    {
        this.meals_ids = meals_ids;
    }

    public String getExtended()
    {
        return this.extended;
    }

    public void setExtended(String extended)
    {
        this.extended = extended;
    }

    public int getPerson_id()
    {
        return this.person_id;
    }

    public void setPerson_id(int person_id)
    {
        this.person_id = person_id;
    }

    public String getComment()
    {
        return this.comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public long getChanged()
    {
        return this.changed;
    }

    public void setChanged(long changed)
    {
        this.changed = changed;
    }

    public String toString()
    {
        return "DayValueDAO";
    }


    @Override
    public boolean dbCreate(Connection conn) throws Exception
    {
        return this.executeDb(conn, 
                              "CREATE TABLE data_dayvalues (" +
                              "id bigint NOT NULL PRIMARY KEY, " +
                              "dt_info bigint NOT NULL," +
                              "bg int, " +
                              "ins1 int, " +
                              "ins2 int, " +
                              "ch float, " +
                              "meals_ids longvarchar, " +
                              "extended longvarchar, " +
                              "person_id int NOT NULL, " +
                              "\"comment\" varchar(2000), " +
                              "changed bigint)");
    }

    
    public boolean dbHasChildren(Connection conn) throws Exception
    {
        return false;
    }
    
    
    
    public String getObjectName()
    {
        return "DayValue";
    }
    
    
    
    public String getTableName()
    {
        return "data_dayvalues";
    }
    
    

    @Override
    public String getObjectUniqueId()
    {
        return "" + this.getId();
    }

    
    public String getIdColumnName()
    {
        return "id";
    }

    @Override
    public boolean dbAdd(Connection conn) throws Exception
    {
        return this.executeDb(conn, 
                 "insert into data_dayvalues (id, dt_info, bg, ins1, ins2, ch, meals_ids, extended, person_id, " +
                 "comment, changed) values (" +
                 this.getNextId(conn) + ", " +
                 this.dt_info + "," +
                 this.bg + "," +
                 this.ins1 + "," +
                 this.ins2 + "," +
                 this.ch + "," +
                 this.getStringForDb(this.meals_ids) + "," +    
                 this.getStringForDb(this.extended) + "," +    
                 this.person_id + "," +
                 this.getStringForDb(this.comment) + "," +    
                 this.changed + ")");
    }

    @Override
    public boolean dbEdit(Connection conn) throws Exception
    {
        new Exception("Not implemented");
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void dbGet(ResultSet rs) throws Exception
    {
        this.id = rs.getLong("id");
        this.dt_info = rs.getLong("dt_info");
        this.bg = rs.getInt("bg");
        this.ins1 = rs.getInt("ins1");
        this.ins2 = rs.getInt("ins2");
        this.ch = rs.getFloat("ch");
        this.meals_ids = rs.getString("meals_ids");
        this.extended = rs.getString("extended");
        this.person_id = rs.getInt("person_id");
        this.comment = rs.getString("comment");
        this.changed = rs.getLong("changed");
    }
    
}
