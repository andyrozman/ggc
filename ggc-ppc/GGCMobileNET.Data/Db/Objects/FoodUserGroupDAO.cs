using GGCMobileNET.Data.Tools;
using System;
using log4net;
using System.Data;

namespace GGCMobileNET.Data.Db.Objects
{

    public class FoodUserGroupDAO : DatabaseAccessObject
    {

        /// <summary>
        /// Table version
        /// </summary>
        private int tableVersion = 1;


        private readonly ILog log = LogManager.GetLogger(typeof(FoodUserGroupDAO));


        public const int CHANGED_ID = 1;
        public const int CHANGED_NAME = 2;
        public const int CHANGED_NAME_I18N = 4;
        public const int CHANGED_DESCRIPTION = 8;
        public const int CHANGED_PARENT_ID = 16;
        public const int CHANGED_CHANGED = 32;

        /** identifier field */
        private long id;

        /** nullable persistent field */
        private String name;

        /** nullable persistent field */
        private String name_i18n;

        /** nullable persistent field */
        private String description;

        /** nullable persistent field */
        private long parent_id;

        /** nullable persistent field */
        private long changed;

        /** full constructor */
        public FoodUserGroupDAO(String name, String name_i18n, String description, long parent_id, long changed)
        {
            this.name = name;
            this.name_i18n = name_i18n;
            this.description = description;
            this.parent_id = parent_id;
            this.changed = changed;
        }

        /** default constructor */
        public FoodUserGroupDAO()
        {
        }


        public long Id
        {
            get
            {
                return this.id;
            }
            set
            {
                this.id = this.SetChangedValueInt64(this.id, value, FoodUserGroupDAO.CHANGED_ID);
            }
        }



        public string Name
        {
            get
            {
                return this.name;
            }
            set
            {
                this.name = this.SetChangedValueString(this.name, value, FoodUserGroupDAO.CHANGED_NAME);
            }
        }


        public string NameI18n
        {
            get
            {
                return this.name_i18n;
            }
            set
            {
                this.name_i18n = this.SetChangedValueString(this.name_i18n, value, FoodUserGroupDAO.CHANGED_NAME_I18N);
            }
        }


        public string Description
        {
            get
            {
                return this.description;
            }
            set
            {
                this.description = this.SetChangedValueString(this.description, value, FoodUserGroupDAO.CHANGED_DESCRIPTION);
            }
        }


        public long ParentId
        {
            get
            {
                return this.parent_id;
            }
            set
            {
                this.parent_id = this.SetChangedValueInt64(this.parent_id, value, FoodUserGroupDAO.CHANGED_PARENT_ID);
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
                this.changed = this.SetChangedValueInt64(this.changed, value, FoodUserGroupDAO.CHANGED_CHANGED);
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



        /**
         * dbAdd - add entry to database
         * 
         * @param conn
         * @return
         * @throws Exception
         */
/*        public bool dbAdd(Connection conn) //throws Exception
        {
            return this.executeDb(conn,
                "insert into nutrition_user_food_group (id, name, name_i18n, description, parent_id, changed) values (" +
                this.getNextId(conn) + ", " +
                this.getStringForDb(this.name) + "," +
                this.getStringForDb(this.name_i18n) + "," +
                this.getStringForDb(this.description) + "," +
                this.parent_id + "," +
                System.currentTimeMillis() +
                ")");
        }


        public bool dbCreate(Connection conn) //throws Exception
        {
            return this.executeDb(conn,
                "CREATE TABLE nutrition_user_food_group (" +
                "id bigint NOT NULL PRIMARY KEY, " +
                "name varchar(255)," +
                "name_i18n varchar(255), " +
                "description varchar(1000)," +
                "parent_id bigint, " +
                "changed bigint" +
                ")");
        }
*/



        /// <summary>
        /// CreateDb - create db table
        /// </summary>
        /// <param name="connection">connection instance</param>
        /// <param name="transaction">transaction instance</param>
        /// <returns></returns>
        public override bool CreateDb(IDbConnection connection, IDbTransaction transaction)
        {
            
            return this.ProcessSqlItem(connection, transaction,
                                  "CREATE TABLE " + this.TableName + " (" +
                                  "id bigint NOT NULL PRIMARY KEY, " +
                                  "name nvarchar(255), " +
                                  "name_i18n nvarchar(255), " +
                                  "description nvarchar(1000), " +
                                  "parent_id int NOT NULL, " +
                                  "changed bigint)",
                                  DatabaseAccessObject.SQL_CREATE,
                                  log
                                  );
        }



        /// <summary>
        /// AddDb - inserts object into database
        /// </summary>
        /// <param name="connection">connection instance</param>
        /// <param name="transaction">transaction instance</param>
        /// <returns></returns>
        public override bool AddDb(System.Data.IDbConnection connection, System.Data.IDbTransaction transaction)
        {
            return base.AddDb(connection, transaction);
        }


        /// <summary>
        /// EditDb - updates object in database
        /// </summary>
        /// <param name="connection">connection instance</param>
        /// <param name="transaction">transaction instance</param>
        /// <returns></returns>
        public override bool EditDb(System.Data.IDbConnection connection, System.Data.IDbTransaction transaction)
        {
            return base.EditDb(connection, transaction);
        }


        /// <summary>
        /// HasChildren - has this object children
        /// </summary>
        /// <param name="connection">connection instance</param>
        /// <param name="transaction">transaction instance</param>
        /// <returns></returns>
        public override bool HasChildren(System.Data.IDbConnection connection, System.Data.IDbTransaction transaction)
        {
            return false;
        }


        /// <summary>
        /// ObjectName - Name of Object
        /// </summary>
        public override string ObjectName
        {
            get
            {
                return "FoodUserGroup";
            }
        }


        /// <summary>
        /// GetDb method, for resolving data received from db
        /// </summary>
        /// <param name="connection">connection instance</param>
        /// <param name="transaction">transaction instance</param>
        public override bool GetDb(int subType, System.Data.DataRow row)
        {
            throw new NotImplementedException();
        }


        /// <summary>
        /// TableName - get name of table
        /// </summary>
        public override string TableName
        {
            get
            {
                return "nutrition_user_food_group";
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




        public override string GetSQL(int index)
        {
            throw new NotImplementedException();
        }
    }
}