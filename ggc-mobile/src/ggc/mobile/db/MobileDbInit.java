/*
 * GGC - GNU Gluco Control
 * 
 * A pure Java application to help you manage your diabetes.
 * 
 * See AUTHORS for copyright information.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Filename: GGCDb 
 *
 * Purpose: This is main datalayer file. It contains all methods
 *      for initialization of Hibernate framework, for adding/updating/deleting data
 *      from database (hibernate). It also contains all methods for mass readings of
 *      data from hibernate.
 * 
 * Author: andyrozman {andy@atech-software.com}
 */

// Methods are added to this class, whenever we make changes to our existing
// database, either add methods for handling data or adding new tables.
// 
// andyrozman

package ggc.mobile.db;

import ggc.mobile.db.objects.DayValueDAO;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;

import com.atech.mobile.db.objects.DatabaseAccessObject;


public class MobileDbInit // implements DbCheckInterface HibernateDb
{

    GGCDbMobile m_db;
    Connection m_conn;
    
    ArrayList<DatabaseAccessObject> db_objects = null;
    
    
    public MobileDbInit()
    {
        this.m_db = new GGCDbMobile();
        this.m_conn = this.m_db.getConnection();
        
        initObjects();
        
    }

    private void initObjects()
    {
        this.db_objects = new ArrayList<DatabaseAccessObject>();
        this.db_objects.add(new DayValueDAO());
        
    }
    
    
    public void initDb()
    {
        
        
        try
        {
//x            this.executeDb(this.m_conn, "CREATE DATABASE '../data/db3'");

            for(int i=0; i<this.db_objects.size(); i++)
            {
                this.db_objects.get(i).dbCreate(m_conn);
            }
        }
        catch(Exception ex)
        {
            System.out.println("Exception on init: " + ex);
        }
        
    }
    
    
    
    public static void main(String[] args)
    {
        MobileDbInit db = new MobileDbInit();
        db.initDb();
        
        //db.initDb();
    }
    
    
    protected boolean executeDb(Connection conn, String sql) throws Exception
    {
        try
        {
            Statement st = conn.createStatement();
            st.execute(sql);
        }
        catch(Exception ex)
        {
            throw ex;
        }
        
        return true;
        
    }
    
    
    
}
