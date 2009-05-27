using GGCMobileNET.Data.Tools;
using System;
using log4net;
namespace GGCMobileNET.Data.Db.Objects
{

    public class FoodUserDescriptionDAO : DatabaseAccessObject
    {

        /// <summary>
        /// Table version
        /// </summary>
        private int tableVersion = 1;


        private readonly ILog log = LogManager.GetLogger(typeof(FoodUserDescriptionDAO));


        public const int CHANGED_ID = 1;
        public const int CHANGED_GROUP_ID = 2;
        public const int CHANGED_NAME = 4;
        public const int CHANGED_NAME_I18N = 8;
        public const int CHANGED_REFUSE = 16;
        public const int CHANGED_NUTRITIONS = 32;
        public const int CHANGED_HOME_WEIGHTS = 64;
        public const int CHANGED_DESCRIPTION = 128;


        /*  identifier field */
        private long id;

        /*  nullable persistent field */
        private long group_id;

        /*  nullable persistent field */
        private String name;

        /*  nullable persistent field */
        private String name_i18n;

        /*  nullable persistent field */
        private String description;

        /*  nullable persistent field */
        private float refuse;

        /*  nullable persistent field */
        private String nutritions;

        /*  nullable persistent field */
        private String home_weights;

        /*  nullable persistent field */
        private long changed;

        /*  full constructor */
        public FoodUserDescriptionDAO(long group_id, String name, String name_i18n, String description, float refuse,
                String nutritions, String home_weights, long changed)
        {
            this.group_id = group_id;
            this.name = name;
            this.name_i18n = name_i18n;
            this.description = description;
            this.refuse = refuse;
            this.nutritions = nutritions;
            this.home_weights = home_weights;
            this.changed = changed;
        }

        /*  default constructor */
        public FoodUserDescriptionDAO()
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
                this.id = this.SetChangedValueInt64(this.id, value, FoodUserDescriptionDAO.CHANGED_ID);
            }
        }


        public long GroupId
        {
            get
            {
                return this.group_id;
            }
            set
            {
                this.group_id = this.SetChangedValueInt64(this.group_id, value, FoodUserDescriptionDAO.CHANGED_ID);
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
                this.name = this.SetChangedValueString(this.name, value, FoodUserDescriptionDAO.CHANGED_NAME);
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
                this.name_i18n = this.SetChangedValueString(this.name_i18n, value, FoodUserDescriptionDAO.CHANGED_NAME_I18N);
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
                this.description = this.SetChangedValueString(this.description, value, FoodUserDescriptionDAO.CHANGED_DESCRIPTION);
            }
        }



        public float Refuse
        {
            get
            {
                return this.refuse;
            }
            set
            {
                this.refuse = this.SetChangedValueFloat(this.refuse, value, FoodUserDescriptionDAO.CHANGED_REFUSE);
            }
        }


        public string Nutritions
        {
            get
            {
                return this.nutritions;
            }
            set
            {
                this.nutritions = this.SetChangedValueString(this.nutritions, value, FoodUserDescriptionDAO.CHANGED_NUTRITIONS);
            }
        }


        public string HomeWeights
        {
            get
            {
                return this.home_weights;
            }
            set
            {
                this.home_weights = this.SetChangedValueString(this.home_weights, value, FoodUserDescriptionDAO.CHANGED_HOME_WEIGHTS);
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



        /**
         * getObjectUniqueId - get unique id of this object
         * 
         * @return id as String
         */
        public String getObjectUniqueId()
        {
            return "" + this.Id;
        }

        /**
         * getTableName - get name of table
         * 
         * @return get name of table
         */
        public String getTableName()
        {
            // TODO
            return "nutrition_user_food_group";
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
                return "FoodUserDesciption";
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
                return "";
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