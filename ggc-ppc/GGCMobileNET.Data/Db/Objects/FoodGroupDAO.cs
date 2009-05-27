using GGCMobileNET.Data.Tools;
using System;
using log4net;
namespace GGCMobileNET.Data.Db.Objects
{

    public class FoodGroupDAO : DatabaseAccessObject
    {

        /// <summary>
        /// Table version
        /// </summary>
        private int tableVersion = 1;


        private readonly ILog log = LogManager.GetLogger(typeof(FoodGroupDAO));


        public const int CHANGED_ID = 1;
        public const int CHANGED_NAME = 2;
        public const int CHANGED_NAME_I18N = 4;
        public const int CHANGED_DESCRIPTION = 8;

        /*  identifier field */
        private long id;

        /*  nullable persistent field */
        private String name;

        /*  nullable persistent field */
        private String name_i18n;

        /*  nullable persistent field */
        private String description;

        /*  full constructor */
        public FoodGroupDAO(String name, String name_i18n, String description)
        {
            this.name = name;
            this.name_i18n = name_i18n;
            this.description = description;
        }

        /*  default constructor */
        public FoodGroupDAO()
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
                this.id = this.SetChangedValueInt64(this.id, value, FoodGroupDAO.CHANGED_ID);
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
                this.name = this.SetChangedValueString(this.name, value, FoodGroupDAO.CHANGED_NAME);
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
                this.name_i18n = this.SetChangedValueString(this.name_i18n, value, FoodGroupDAO.CHANGED_NAME_I18N);
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
                this.description = this.SetChangedValueString(this.description, value, FoodGroupDAO.CHANGED_DESCRIPTION);
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



/*
        public bool dbAdd(Connection conn) //throws Exception
        {
            return this.executeDb(conn,
                "insert into nutrition_usda_food_group (id, name, name_i18n, description) values (" +
                this.getNextId(conn) + ", " +
                this.getStringForDb(this.name) + "," +
                this.getStringForDb(this.name_i18n) + "," +
                this.getStringForDb(this.description) + ")");
        }


        public bool dbCreate(Connection conn) //throws Exception
        {
            return this.executeDb(conn,
                "CREATE TABLE nutrition_usda_food_group (" +
                "id bigint NOT NULL PRIMARY KEY, " +
                "name varchar(255)," +
                "name_i18n varchar(255), " +
                "description varchar(1000)" +
                ")");
        }



        public void dbGet(ResultSet rs) //throws Exception
        {
            this.id = rs.getLong("id");
            this.name = rs.getString("name");
            this.name_i18n = rs.getString("name_i18n");
            this.description = rs.getString("description");
        }
*/





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
                return "FoodGroupDAO";
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
                return "nutrition_usda_food_group";
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