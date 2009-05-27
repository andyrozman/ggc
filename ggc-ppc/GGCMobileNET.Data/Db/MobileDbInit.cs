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

using GGCMobileNET.Data.Tools;
using System.Collections.Generic;
using System;
using System.Data;
using log4net;
using System.Data.SqlServerCe;
using System.Windows.Forms;
using GGCMobileNET.Data.Db.Objects;
using System.IO;
namespace GGCMobileNET.Data.Db
{

    public class MobileDbInit // implements DbCheckInterface HibernateDb
    {

        private readonly ILog log = LogManager.GetLogger(typeof(MobileDbInit));
        GGCDbMobile m_db;
        IDbConnection m_conn;

        List<DatabaseAccessObject> db_objects = null;


        public MobileDbInit()
        {
            this.m_db = new GGCDbMobile();
            CreateDatabase();

            this.m_conn = this.m_db.Connection;
            InitObjects();
        }


        private void CreateDatabase()
        {
            FileInfo fi = new FileInfo("GGCMobileDb.sdf");

            if (!fi.Exists)
            {

                SqlCeEngine engine = new SqlCeEngine(GGCDbMobile.ConnectionStringInit);
                engine.CreateDatabase();
                engine.Dispose();
                MessageBox.Show("Database Successfully created");
            }
        }


        private void InitObjects()
        {
            this.db_objects = new List<DatabaseAccessObject>();
            this.db_objects.Add(new DayValueDAO());

        }


        public void InitDb()
        {


            try
            {
                //x            this.executeDb(this.m_conn, "CREATE DATABASE '../data/db3'");
                Console.WriteLine("Create database tables");

                for (int i = 0; i < this.db_objects.Count; i++)
                {
                    this.db_objects[i].CreateDb(m_conn, null);
                }

                MessageBox.Show("Tables created !");

                //this.executeDb(m_conn, "SHUTDOWN");
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error on creating tables: " + ex);
                //Console.WriteLine("Exception on init: " + ex);
                //ex.printStackTrace();
            }

        }



        public static void main(String[] args)
        {
            MobileDbInit db = new MobileDbInit();
            db.InitDb();

            //db.initDb();
        }


        protected bool executeDb(IDbConnection conn, String sql) //throws Exception
        {
            try
            {
                DummyDAO dd = new DummyDAO();

                dd.ProcessSqlItem(conn, null, sql, DatabaseAccessObject.OBJECT_ADD, log);

                //Statement st = conn.createStatement();
                //st.execute(sql);
            }
            catch (Exception ex)
            {
                throw ex;
            }

            return true;

        }



    }
}