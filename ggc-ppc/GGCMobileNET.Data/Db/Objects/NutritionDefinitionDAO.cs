using GGCMobileNET.Data.Tools;
using System;
using log4net;
namespace GGCMobileNET.Data.Db.Objects
{

    public class NutritionDefinitionDAO : DatabaseAccessObject
    {

        /// <summary>
        /// Table version
        /// </summary>
        private int tableVersion = 1;


        private readonly ILog log = LogManager.GetLogger(typeof(NutritionDefinitionDAO));


        public const int CHANGED_ID = 1;
        public const int CHANGED_WEIGHT_UNIT = 2;
        public const int CHANGED_TAG = 4;
        public const int CHANGED_NAME = 8;
        public const int CHANGED_DECIMAL_PLACES = 16;
        public const int CHANGED_STATIC_ENTRY = 32;


        /*  identifier field */
        private long id;

        /*  nullable persistent field */
        private String weight_unit;

        /*  nullable persistent field */
        private String tag;

        /*  nullable persistent field */
        private String name;

        /*  nullable persistent field */
        private String decimal_places;

        /*  nullable persistent field */
        private int static_entry;



        /*  full constructor */
        public NutritionDefinitionDAO(String weight_unit, String tag, String name, String decimal_places, int static_entry)
        {
            this.weight_unit = weight_unit;
            this.tag = tag;
            this.name = name;
            this.decimal_places = decimal_places;
            this.static_entry = static_entry;
        }

        /*  default constructor */
        public NutritionDefinitionDAO()
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
                this.id = this.SetChangedValueInt64(this.id, value, NutritionDefinitionDAO.CHANGED_ID);
            }
        }

        public String WeightUnit
        {
            get
            {
                return this.weight_unit;
            }
            set
            {
                this.weight_unit = this.SetChangedValueString(this.weight_unit, value, NutritionDefinitionDAO.CHANGED_WEIGHT_UNIT);
            }
        }

        public String Tag
        {
            get
            {
                return this.tag;
            }
            set
            {
                this.tag = this.SetChangedValueString(this.tag, value, NutritionDefinitionDAO.CHANGED_TAG);
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
                this.name = this.SetChangedValueString(this.name, value, NutritionDefinitionDAO.CHANGED_NAME);
            }
        }

        public string DecimalPlaces
        {
            get
            {
                return this.name;
            }
            set
            {
                this.name = this.SetChangedValueString(this.name, value, NutritionDefinitionDAO.CHANGED_NAME);
            }
        }


        public int StaticEntry
        {
            get
            {
                return this.static_entry;
            }
            set
            {
                this.static_entry = this.SetChangedValueInt32(this.static_entry, value, NutritionDefinitionDAO.CHANGED_STATIC_ENTRY);
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
                return "NutritionDefinitionDAO";
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
                return "nutrition_definition";
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