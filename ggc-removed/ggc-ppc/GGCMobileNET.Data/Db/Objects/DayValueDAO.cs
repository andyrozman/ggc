
using GGCMobileNET.Data.Tools;
using System;
using log4net;
using System.Data;
using System.Text;

namespace GGCMobileNET.Data.Db.Objects
{

    public class DayValueDAO : DatabaseAccessObject
    {

        #region Variables

        /// <summary>
        /// Table version
        /// </summary>
        private int tableVersion = 1;


        private readonly ILog log = LogManager.GetLogger(typeof(DayValueDAO));

        public string[] SQL = {
                                  "id, dt_info, bg, ins1, ins2, ch, meals_ids, extended, person_id, comment, changed"
                              };


        public const int CHANGED_ID = 1;
        public const int CHANGED_DT_INFO = 2;
        public const int CHANGED_BG = 4;
        public const int CHANGED_INS1 = 8;
        public const int CHANGED_INS2 = 16;
        public const int CHANGED_CH = 32;
        public const int CHANGED_MEALS_IDS = 64;
        public const int CHANGED_EXTENDED = 128;
        public const int CHANGED_PERSON_ID = 256;
        public const int CHANGED_COMMENT = 512;
        public const int CHANGED_CHANGED = 1024;


        /*  identifier field */
        private long id;

        /*  persistent field */
        private long dt_info;

        /*  nullable persistent field */
        private int bg;

        /*  nullable persistent field */
        private int ins1;

        /*  nullable persistent field */
        private int ins2;

        /*  nullable persistent field */
        private float ch;

        /*  nullable persistent field */
        private String meals_ids;

        //private string urine;

        //private string activity;

        /*  nullable persistent field */
        private String extended;

        /*  persistent field */
        private int person_id;

        /*  nullable persistent field */
        private String comment;

        /*  nullable persistent field */
        private long changed;

        #endregion


        #region Constructor

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
        #endregion


        #region Accessors

        public long Id
        {
            get
            {
                return this.id;
            }
            set
            {
                this.id = this.SetChangedValueInt64(this.id, value, DayValueDAO.CHANGED_ID);
            }
        }

        public long DtInfo
        {
            get
            {
                return this.dt_info;
            }
            set
            {
                this.dt_info = this.SetChangedValueInt64(this.dt_info, value, DayValueDAO.CHANGED_DT_INFO);
            }
        }

        public int Bg
        {
            get
            {
                return this.bg;
            }
            set
            {
                this.bg = this.SetChangedValueInt32(this.bg, value, DayValueDAO.CHANGED_BG);
            }
        }

        public int Ins1
        {
            get
            {
                return this.ins1;
            }
            set
            {
                this.ins1 = this.SetChangedValueInt32(this.ins1, value, DayValueDAO.CHANGED_INS1);
            }
        }


        public int Ins2
        {
            get
            {
                return this.ins2;
            }
            set
            {
                this.ins2 = this.SetChangedValueInt32(this.ins2, value, DayValueDAO.CHANGED_INS2);
            }
        }



        public float Ch
        {
            get
            {
                return this.ch;
            }

            set
            {
                this.ch = this.SetChangedValueFloat(this.ch, value, DayValueDAO.CHANGED_CH);
            }
        }

        public String MealsIds
        {
            get
            {
                return this.meals_ids;
            }

            set
            {
                this.meals_ids = this.SetChangedValueString(this.meals_ids, value, DayValueDAO.CHANGED_MEALS_IDS);
            }
        }


        public String Extended
        {
            get
            {
                return this.extended;
            }

            set
            {
                this.extended = this.SetChangedValueString(this.extended, value, DayValueDAO.CHANGED_EXTENDED);
            }
        
        }


        public int PersonId
        {
            get
            {
                return this.person_id;
            }
            set
            {
                this.person_id = this.SetChangedValueInt32(this.person_id, value, DayValueDAO.CHANGED_PERSON_ID);
            }
 
        }

        public String Comment
        {

            get
            {
                return this.comment;
            }

            set
            {
                this.comment = this.SetChangedValueString(this.comment, value, DayValueDAO.CHANGED_COMMENT);
            }
        
        }

        /// <summary>
        /// TableVersion - return version of table
        /// </summary>
        public override int TableVersion
        {
            get
            {
                return this.tableVersion;
            }
        }


        public long Changed
        {
            get
            {
                return this.changed;
            }
            set
            {
                this.changed = this.SetChangedValueInt64(this.changed, value, DayValueDAO.CHANGED_CHANGED);
            }

        }

        /*
        public string Urine
        {
            get { return urine; }
            set 
            { 
                urine = value;
                //CreateExtended();
            }
        }


        public string Activity
        {
            get { return activity; }
            set 
            { 
                activity = value;
                //CreateExtended();
            }
        }
        */

        #endregion

        /*
        public void CreateExtended()
        {
            StringBuilder sb = new StringBuilder();

            if ((this.Urine != null) && (this.Urine.Length > 0))
            {
                sb.Append("URINE=" + this.Urine);
            }

            if ((this.Activity != null) && (this.Activity.Length > 0))
            {
                if (sb.Length != 0)
                    sb.Append(";");

                sb.Append("ACTIVITY=" + this.Activity);
            }

            if (sb.Length!=0)
                this.Extended = sb.ToString();

        }
        */







        /*
        public void dbGet(ResultSet rs) //throws Exception
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
        }*/


        /// <summary>
        /// Get SQL variables for retriving data.
        /// </summary>
        /// <param name="index"></param>
        /// <returns></returns>
        public override string GetSQL(int index)
        {
            return SQL[index];
        }


        /// <summary>
        /// CreateDb - create db table
        /// </summary>
        /// <param name="connection">connection instance</param>
        /// <param name="transaction">transaction instance</param>
        /// <returns></returns>
        public override bool CreateDb(IDbConnection connection, IDbTransaction transaction)
        {

            return this.ProcessSqlItem(connection, transaction,
                                  "CREATE TABLE data_dayvalues (" +
                                  "id bigint NOT NULL PRIMARY KEY, " +
                                  "dt_info bigint NOT NULL," +
                                  "bg int, " +
                                  "ins1 int, " +
                                  "ins2 int, " +
                                  "ch float, " +
                                  "meals_ids ntext, " +
                                  "extended ntext, " +
                                  "person_id int NOT NULL, " +
                                  "comment nvarchar(2000), " +
                                  "changed bigint)",
                                  DatabaseAccessObject.SQL_CREATE,
                                  log
                                  );


            //log.Info("Create method for " + this.ObjectName + " not available.");
            //return false;
        }



        /// <summary>
        /// AddDb - inserts object into database
        /// </summary>
        /// <param name="connection">connection instance</param>
        /// <param name="transaction">transaction instance</param>
        /// <returns></returns>
        public override bool AddDb(System.Data.IDbConnection connection, System.Data.IDbTransaction transaction)
        {
            return this.ProcessSqlItem(connection, transaction,
                     "insert into data_dayvalues (id, dt_info, bg, ins1, ins2, ch, meals_ids, extended, person_id, " +
                     "comment, changed) values (" +
                     this.GetNextId(connection, transaction) + ", " +
                     this.dt_info + "," +
                     this.bg + "," +
                     this.ins1 + "," +
                     this.ins2 + "," +
                     this.ch + "," +
                     this.GetStringForDb(this.meals_ids) + "," +
                     this.GetStringForDb(this.extended) + "," +
                     this.person_id + "," +
                     this.GetStringForDb(this.comment) + "," +
                     this.changed + ")", 
                     DatabaseAccessObject.SQL_INSERT,
                     log);
        }


        /// <summary>
        /// EditDb - updates object in database
        /// </summary>
        /// <param name="connection">connection instance</param>
        /// <param name="transaction">transaction instance</param>
        /// <returns></returns>
        public override bool EditDb(System.Data.IDbConnection connection, System.Data.IDbTransaction transaction)
        {

            if (!this.WasChanged)
                return false;

            StringBuilder sb = new StringBuilder("update data_dayvalues set ");

            if (this.HasValueChanged(DayValueDAO.CHANGED_DT_INFO))
                sb.Append("dt_info=" + this.DtInfo + ",");

            if (this.HasValueChanged(DayValueDAO.CHANGED_BG))
                sb.Append("bg=" + this.Bg + ",");

            if (this.HasValueChanged(DayValueDAO.CHANGED_CH))
                sb.Append("ch=" + this.Ch + ",");

            if (this.HasValueChanged(DayValueDAO.CHANGED_INS1))
                sb.Append("ins1=" + this.Ins1 + ",");

            if (this.HasValueChanged(DayValueDAO.CHANGED_INS2))
                sb.Append("ins2=" + this.Ins2 + ",");
            
            if (this.HasValueChanged(DayValueDAO.CHANGED_MEALS_IDS))
                sb.Append("meals_ids=" + GetStringForDb(this.MealsIds) + ",");

            if (this.HasValueChanged(DayValueDAO.CHANGED_PERSON_ID))
                sb.Append("person_id=" + this.PersonId+ ",");

            if (this.HasValueChanged(DayValueDAO.CHANGED_EXTENDED))
                sb.Append("extended=" + this.GetStringForDb(this.Extended) + ",");

            if (this.HasValueChanged(DayValueDAO.CHANGED_COMMENT))
                sb.Append("comment=" + this.GetStringForDb(this.Comment) + ",");

            sb.Append("changed=" + DateTime.Now.ToFileTimeUtc());

            return this.ProcessSqlItem(connection, transaction,
                     sb.ToString(),
                     DatabaseAccessObject.SQL_UPDATE,
                     log);
        }




        public override string ObjectName
        {
            get
            {
                return "DayValue";
            }
        }

        public override bool GetDb(int subType, System.Data.DataRow row)
        {
            // "id, dt_info, bg, ins1, ins2, ch, meals_ids, extended, person_id, comment, changed"

            if (subType == 0)
            {
                object[] items = row.ItemArray;

                this.Id = GetInt64OfDbRow(items[0]);
                this.DtInfo = GetInt64OfDbRow(items[1]);
                this.Bg = GetInt32OfDbRow(items[2]);
                this.Ins1 = GetInt32OfDbRow(items[3]);
                this.Ins2 = GetInt32OfDbRow(items[4]);
                this.Ch = GetFloatOfDbRow(items[5]);
                this.MealsIds = GetStringOfDbRow(items[6]);
                this.Extended = GetStringOfDbRow(items[7]);
                this.PersonId = GetInt32OfDbRow(items[8]);
                this.Comment = GetStringOfDbRow(items[9]);
                this.Changed = GetInt64OfDbRow(items[10]);
            }

            ResetChange();

            return true;

        }


        /// <summary>
        /// TableName - get name of table
        /// </summary>
        public override string TableName
        {
            get 
            {
                return "data_dayvalues";
            }
        }

        /// <summary>
        /// IdColumnName - get name of column containing id
        /// </summary>
        public override string IdColumnName
        {
            get 
            {
                return "id";
            }
        }


        /// <summary>
        /// ObjectUniqueId - get unique id of this object
        /// </summary>
        /// <returns></returns>
        public override string ObjectUniqueId
        {
            get
            {
                return "" + this.Id;
            }
        }
    }
}