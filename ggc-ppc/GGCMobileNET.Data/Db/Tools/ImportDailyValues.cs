/*
 * GGC - GNU Gluco Control
 * 
 * A pure java app to help you manage your diabetes.
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
 * Filename: ImportDailyValues Purpose: Imports daily values (from export from
 * Meter Tool Import, or some export)
 * 
 * Author: andyrozman {andy@atech-software.com}
 */

using GGCMobileNET.Data.Tools;
using System;
using GGCMobileNET.Data.Utils;
using log4net;
using System.Data;
using GGCMobileNET.Data.Db.Objects;
using System.IO;
using System.Windows.Forms;
namespace GGCMobileNET.Data.Db.Tools
{

    public class ImportDailyValues : ImportTool //implements Runnable
    {

        GGCDbMobile m_db = null;
        //public String file_name;
//        private static SimpleLogger log = new SimpleLogger(); //LogFactory.getLog(ImportDailyValues.class);
        private readonly ILog log = LogManager.GetLogger(typeof(ImportDailyValues));



        DataAccessMobile m_da = DataAccessMobile.Instance;
        bool clean_db = false;


        public ImportDailyValues(BackupRestoreWorkGiver giver) : base(DataAccessMobile.Instance.Db.Connection)
        {
//            super(DataAccessMobile.Instance.getDb().getConnection());
            this.setStatusReceiver(giver);
            this.setTypeOfStatus(ImportTool.STATUS_SPECIAL);
        }


        public ImportDailyValues(BackupRestoreWorkGiver giver, RestoreFileInfo res) : base(DataAccessMobile.Instance.Db.Connection, res)
        {
//            super(DataAccessMobile.getInstance().getDb().getConnection(), res);
            this.setStatusReceiver(giver);
            this.setTypeOfStatus(ImportTool.STATUS_SPECIAL);
        }


        public ImportDailyValues(String file_name) : this(file_name, true)
        {
//            this(file_name, true);
        }


        public ImportDailyValues(String file_name, bool identify) : this(file_name, identify, false)
        { 
        }



        public ImportDailyValues(String file_name, bool identify, bool clean) : base()
        {
//            super();

            m_db = new GGCDbMobile();
            m_db.initDb();
            this.setConnection(m_db.Connection);
            this.restore_file = new FileInfo(file_name);
            this.clean_db = clean;

            if (identify)
                importDailyValues();

            Console.WriteLine();

        }

        public ImportDailyValues(IDbConnection conn, String file_name) : base(conn)
        {
//            super(conn);
            // m_db = new GGCDb();
            // m_db.initDb();
            this.restore_file = new FileInfo(file_name);

            importDailyValues();

            Console.WriteLine();
        }


        public void setImportClean(bool clean)
        {
            this.clean_db = clean;
        }


        public int getActiveSession()
        {
            return 2;
        }



        public void importDailyValues()
        {

            String line = null;
            bool append = false;

            // TODO
            //ObjectContainer db = this.m_db.getDb();


            try
            {
                DayValueDAO dvh;

                if (clean_db)
                {
                    dvh = new DayValueDAO();
                    this.clearExistingData(dvh.TableName);
                }
                else
                    append = true;

                Console.WriteLine("\nLoading DailiyValues (5/dot)");

                this.openFileForReading(this.restore_file);

                //BufferedReader br = new BufferedReader(new FileReader(this.restore_file)); //new File(file_name)));

                // int i = 0;

                int dot_mark = 5;
                int count = 0;




                while ((line = this.br_file.ReadLine()) != null)
                {
                    if (line.StartsWith(";"))
                        continue;

                    // line = line.replaceAll("||", "| |");
                    line = m_da.replaceExpression(line, "||", "| |");

                    //StringTokenizer strtok = new StringTokenizer(line, "|");

                    String[] toks = line.Split("|".ToCharArray());


                    dvh = new DayValueDAO();

                    // ; Columns: id,dt_info,bg,ins1,ins2,ch,meals_ids,act,comment

                    // 1|200603250730|0|10.0|0.0|0.0|null|null|
                    // id; dt_info; bg; ins1; ins2; ch; meals_ids; extended;
                    // person_id; comment

                    if (!append)
                    {
                        long id = this.getLong(toks[0]); //strtok.nextToken());

                        if (id != 0)
                            dvh.Id = id;
                    }
                    /*else
                    {
                        strtok.nextToken();
                    }*/

                    dvh.DtInfo = getLong(toks[1]); //strtok.nextToken()));
                    dvh.Bg = getInt(toks[2]);
                    dvh.Ins1 = (int)getFloat(toks[3]);
                    dvh.Ins2 = (int)getFloat(toks[4]);
                    dvh.Ch = getFloat(toks[5]);
                    dvh.MealsIds = getString(toks[6]);
                    dvh.Extended = getString(toks[7]);

                    int person_id = this.getInt(toks[8]);

                    if (person_id == 0)
                        dvh.PersonId = 1;
                    else
                        dvh.PersonId = person_id;

                    dvh.Comment = getString(toks[9]);
                    dvh.Changed = getLong(toks[10]);

                    dvh.AddDb(this.connection, null);

                    count++;
                    this.writeStatus(dot_mark, count);
                }

                MessageBox.Show("Importing done [" + count + "] !");

                this.closeFile();

            }
            catch (Exception ex)
            {
                MessageBox.Show("Exception: " + ex);
                log.Error("Error on importDailyValues: \nData: " + line + "\nException: " + ex, ex);
            }

        }


        public void run()
        {
            this.importDailyValues();
        }


        public static void main(String[] args)
        {
            if (args.Length == 0)
            {
                Console.WriteLine("You need to specify import file !");
                return;
            }

            // GGCDb db = new GGCDb();

            new ImportDailyValues(args[0]);
        }

    }
}