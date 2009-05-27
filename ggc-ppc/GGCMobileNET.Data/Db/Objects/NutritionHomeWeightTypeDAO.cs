using GGCMobileNET.Data.Tools;
using System;
using log4net;
namespace GGCMobileNET.Data.Db.Objects
{

    public class NutritionHomeWeightTypeDAO : DatabaseAccessObject
    {

        /// <summary>
        /// Table version
        /// </summary>
        private int tableVersion = 1;


        private readonly ILog log = LogManager.GetLogger(typeof(NutritionHomeWeightTypeDAO));


        public const int CHANGED_ID = 1;
        public const int CHANGED_NAME = 2;
        public const int CHANGED_STATIC_ENTRY = 4;


        /*  identifier field */
        private long id;

        /*  nullable persistent field */
        private String name;

        /*  nullable persistent field */
        private int static_entry;

        /*  full constructor */
        public NutritionHomeWeightTypeDAO(String name, int static_entry)
        {
            this.name = name;
            this.static_entry = static_entry;
        }

        /*  default constructor */
        public NutritionHomeWeightTypeDAO()
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
                this.id = this.SetChangedValueInt64(this.id, value, NutritionHomeWeightTypeDAO.CHANGED_ID);
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
                this.name = this.SetChangedValueString(this.name, value, NutritionHomeWeightTypeDAO.CHANGED_NAME);
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
                this.static_entry = this.SetChangedValueInt32(this.static_entry, value, NutritionHomeWeightTypeDAO.CHANGED_STATIC_ENTRY);
            }
        }



        public String toString()
        {
            return "NutritionHomeWeightTypeDAO";
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
                return "NutritionHomeWeightTypeDAO";
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
                return "nutrition_home_weight_type";
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