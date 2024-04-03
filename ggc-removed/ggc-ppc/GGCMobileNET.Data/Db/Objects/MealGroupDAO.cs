using System;
using GGCMobileNET.Data.Tools;
using log4net;
namespace GGCMobileNET.Data.Db.Objects
{

    public class MealGroupDAO : DatabaseAccessObject
    {

        /// <summary>
        /// Table version
        /// </summary>
        private int tableVersion = 1;


        private readonly ILog log = LogManager.GetLogger(typeof(MealGroupDAO));



        public const int CHANGED_ID = 1;
        public const int CHANGED_NAME = 2;
        public const int CHANGED_NAME_I18N = 4;
        public const int CHANGED_DESCRIPTION = 8;
        public const int CHANGED_PARENT_ID = 16;
        public const int CHANGED_CHANGED = 32;


        /*  identifier field */
        private long id;

        /*  nullable persistent field */
        private String name;

        /*  nullable persistent field */
        private String name_i18n;

        /*  nullable persistent field */
        private String description;

        /*  nullable persistent field */
        private long parent_id;

        /*  nullable persistent field */
        private long changed;

        /*  full constructor */
        public MealGroupDAO(String name, String name_i18n, String description, long parent_id, long changed)
        {
            this.name = name;
            this.name_i18n = name_i18n;
            this.description = description;
            this.parent_id = parent_id;
            this.changed = changed;
        }

        /*  default constructor */
        public MealGroupDAO()
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
                this.id = this.SetChangedValueInt64(this.id, value, MealGroupDAO.CHANGED_ID);
            }
        }


        public String Name
        {
            get
            {
                return this.name;
            }
            set
            {
                this.name = this.SetChangedValueString(this.name, value, MealGroupDAO.CHANGED_NAME);
            }
        }


        public String NameI18n
        {
            get
            {
                return this.name_i18n;
            }
            set
            {
                this.name_i18n = this.SetChangedValueString(this.name_i18n, value, MealGroupDAO.CHANGED_NAME_I18N);
            }
        }


        public String Description
        {
            get
            {
                return this.description;
            }
            set
            {
                this.description = this.SetChangedValueString(this.description, value, MealGroupDAO.CHANGED_DESCRIPTION);
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
                this.parent_id = this.SetChangedValueInt64(this.parent_id, value, MealGroupDAO.CHANGED_PARENT_ID);
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
                this.changed = value;
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
                return "MealGroup";
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
                return "meal_group";
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


        /// <summary>
        /// GetSQL - get SQL for reading data
        /// </summary>
        /// <param name="index"></param>
        /// <returns></returns>
        public override string GetSQL(int index)
        {
            throw new NotImplementedException();
        }



    }
}