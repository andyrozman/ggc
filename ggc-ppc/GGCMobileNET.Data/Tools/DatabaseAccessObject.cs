using System;
using System.Data;
using log4net;

using GGCMobileNET.Data.Db;
using System.Data.SqlServerCe;
namespace GGCMobileNET.Data.Tools
{

    public abstract class DatabaseAccessObject
    {

        #region Variables
        private readonly ILog log = LogManager.GetLogger(typeof(DatabaseAccessObject));

        int changed_what = 0;
        int changed_object = 0; 

        public abstract string ObjectName { get; }

        public const int OBJECT_NO_ACTION = 0;
        public const int OBJECT_ADD = 1;
        public const int OBJECT_EDIT = 2;
        public const int OBJECT_DELETE = 4;
        public const int OBJECT_CREATE = 8;

        /// <summary>
        /// Nothing changed.
        /// </summary>
        public const Int32 CHANGED_NOTHING = 0;

        #endregion


        #region Accessors

        /// <summary>
        /// What changed
        /// </summary>
        public int WhatChanged
        {
            get { return this.changed_what; }
            set { this.changed_what = value; }
        }

        /// <summary>
        /// Changed. It shouws that something changed
        /// </summary>
        public int ChangedInternal
        {
            get { return this.changed_object; }
            set { this.changed_object = value; }
        }
        #endregion


        #region Abstract database methods

        /// <summary>
        /// Get SQL variables for retriving data.
        /// </summary>
        /// <param name="index"></param>
        /// <returns></returns>
        public abstract string GetSQL(int index);

        /// <summary>
        /// CreateDb - create db table
        /// </summary>
        /// <param name="connection">connection instance</param>
        /// <param name="transaction">transaction instance</param>
        /// <returns></returns>
        public virtual bool CreateDb(IDbConnection connection, IDbTransaction transaction)
        {
            log.Warn("Create method for " + this.ObjectName + " not available.");
            return false;
        }



        /// <summary>
        /// AddDb - inserts object into database
        /// </summary>
        /// <param name="connection">connection instance</param>
        /// <param name="transaction">transaction instance</param>
        /// <returns></returns>
        public virtual bool AddDb(IDbConnection connection, IDbTransaction transaction)
        {
            log.Warn("Insert method for " + this.ObjectName + " not available.");
            return false;
        }


        /// <summary>
        /// EditDb - updates object in database
        /// </summary>
        /// <param name="connection">connection instance</param>
        /// <param name="transaction">transaction instance</param>
        /// <returns></returns>
        public virtual bool EditDb(IDbConnection connection, IDbTransaction transaction)
        {
            log.Warn("Update method for " + this.ObjectName + " not available.");
            return false;
        }

        /// <summary>
        /// Delete - delete object in database
        /// </summary>
        /// <param name="connection">connection instance</param>
        /// <param name="transaction">transaction instance</param>
        /// <returns></returns>
        public virtual bool DeleteDb(IDbConnection connection, IDbTransaction transaction)
        {
            if (!this.HasChildren(connection, transaction))
            {
                return this.ProcessSqlItem(connection, transaction,
                               "delete from " + this.TableName + " where " + this.IdColumnName +
                               "=" + this.ObjectUniqueId,
                               DatabaseAccessObject.OBJECT_DELETE,
                               log);
            }
            else
                return false;
        }

        /// <summary>
        /// HasChildren - has this object children
        /// </summary>
        /// <param name="connection">connection instance</param>
        /// <param name="transaction">transaction instance</param>
        /// <returns></returns>
        public virtual bool HasChildren(IDbConnection connection, IDbTransaction transaction)
        {
            log.Warn("Has Children " + this.ObjectName + " not available.");
            return false;
        }



        /// <summary>
        /// Base GetDb method, for resolving data received from db
        /// </summary>
        /// <param name="row">row instance</param>
        /// <returns></returns>
        public bool GetDb(DataRow row)
        {
            return GetDb(0, row);
        }

        /// <summary>
        /// GetDb method, for resolving data received from db
        /// </summary>
        /// <param name="connection"></param>
        /// <param name="transaction"></param>
        public void GetDb(IDbConnection connection, IDbTransaction transaction) //throws Exception
        {
            string sql;
            try
            {
                sql = "select " + GetSQL(0) + " from " + this.TableName + " where " + this.IdColumnName + "=" + this.ObjectUniqueId;

                DataTable dt = GetDataTable(sql, this.TableName, log);

                if ((dt == null) || (dt.Rows == null))
                    return;

                for (int i = 0; i < dt.Rows.Count; i++)
                {
                    DataRow dr = dt.Rows[i];
                    GetDb(0, dr);
                }
            }
            catch (Exception ex)
            {
                log.Error("Exception on Get: " + ex);
                throw ex;
            }
        }



        /// <summary>
        /// Abstract GetDb method, for resolving data received from db
        /// </summary>
        /// <param name="row">row instance</param>
        /// <returns></returns>
        public abstract bool GetDb(int subType, DataRow row);



        /// <summary>
        /// TableName - get name of table
        /// </summary>
        public abstract String TableName
        {
            get;
        }



        /// <summary>
        /// IdColumnName - get name of column containing id
        /// </summary>
        public abstract String IdColumnName
        {
            get;
        }



        /// <summary>
        /// ObjectUniqueId - get unique id of this object
        /// </summary>
        /// <returns></returns>
        public abstract String ObjectUniqueId
        {
            get;
        }



        #endregion



        #region Changes Methods

        /// <summary>
        /// Has Changed
        /// </summary>
        public bool HasChanged
        {
            get
            {
                return this.WasChanged;
            }
        }

        /// <summary>
        /// Set Deleted
        /// </summary>
        /// <param name="value">true if we want object to be deleted, false if not</param>
        public void SetDeleted(bool value)
        {
            if (value)
            {
                this.ChangedInternal |= DatabaseAccessObject.OBJECT_DELETE;
            }
            else
            {
                if ((this.ChangedInternal & DatabaseAccessObject.OBJECT_DELETE) == DatabaseAccessObject.OBJECT_DELETE)
                {
                    this.ChangedInternal -= DatabaseAccessObject.OBJECT_DELETE;
                }
            }
        }


        /// <summary>
        /// Commit object
        /// </summary>
        /// <param name="connection"></param>
        /// <param name="transaction"></param>
        /// <returns></returns>
        public bool Commit(IDbConnection connection, IDbTransaction transaction)
        {

            if (!this.WasChanged)
                return false;

            if (this.WasDeleted)
            {
                return this.DeleteDb(connection, transaction);
            }
            else
            {
                if (this.WasAdded)
                {
                    return this.AddDb(connection, transaction);
                }
                else
                {
                    return this.EditDb(connection, transaction);
                }
            }
        }


        /// <summary>
        /// Object was added.
        /// </summary>
        public bool WasAdded
        {
            get
            {
                return ((this.ChangedInternal & DatabaseAccessObject.OBJECT_ADD) == DatabaseAccessObject.OBJECT_ADD);
            }
        }

        /// <summary>
        /// Object was deleted.
        /// </summary>
        public bool WasDeleted
        {
            get { return ((this.ChangedInternal & DatabaseAccessObject.OBJECT_DELETE) == DatabaseAccessObject.OBJECT_DELETE); }
        }


        /// <summary>
        /// Object was updated.
        /// </summary>
        public bool WasUpdated
        {
            get
            {
                if (this.WhatChanged != 0)
                    return true;
                else
                    return ((this.ChangedInternal & DatabaseAccessObject.OBJECT_EDIT) == DatabaseAccessObject.OBJECT_EDIT);
            }
        }

        /// <summary>
        /// Object was changed
        /// </summary>
        public bool WasChanged
        {
            get { return (this.ChangedInternal != 0) || (this.WhatChanged != 0); }
        }


        /// <summary>
        /// Sets tags if variable changed
        /// </summary>
        /// <param name="changeValue"></param>
        public void SetChangedTag(int changeValue)
        {
            this.ChangedInternal |= DatabaseAccessObject.OBJECT_EDIT;
            this.WhatChanged |= changeValue;
        }


        /// <summary>
        /// Check if value changed, sets tags and return value (either old or new if new value is 
        /// different. (For Int32 types)
        /// </summary>
        /// <param name="current"></param>
        /// <param name="newone"></param>
        /// <param name="changeValue"></param>
        /// <returns></returns>
        public Int32 SetChangedValueInt32(Int32 current, Int32 newone, int changeValue)
        {
            if (current != newone)
            {
                //current = newone;
                this.SetChangedTag(changeValue);
                return newone;
            }
            else
                return current;
        }

        /// <summary>
        /// Check if value changed, sets tags and return value (either old or new if new value is 
        /// different. (For Int32 types)
        /// </summary>
        /// <param name="current"></param>
        /// <param name="newone"></param>
        /// <param name="changeValue"></param>
        /// <returns></returns>
        public DateTime SetChangedValueDateTime(DateTime current, DateTime newone, int changeValue)
        {
            if (current != newone)
            {
                //current = newone;
                this.SetChangedTag(changeValue);
                return newone;
            }
            else
                return current;
        }



        /// <summary>
        /// Check if value changed, sets tags and return value (either old or new if new value is 
        /// different. (For Int64 types)
        /// </summary>
        /// <param name="current"></param>
        /// <param name="newone"></param>
        /// <param name="changeValue"></param>
        /// <returns></returns>
        public Int64 SetChangedValueInt64(Int64 current, Int64 newone, int changeValue)
        {
            if (current != newone)
            {
                //current = newone;
                this.SetChangedTag(changeValue);
                return newone;
            }
            else
                return current;
        }


        /// <summary>
        /// Check if value changed, sets tags and return value (either old or new if new value is 
        /// different. (For string types)
        /// </summary>
        /// <param name="current"></param>
        /// <param name="newone"></param>
        /// <param name="changeValue"></param>
        /// <returns></returns>
        public string SetChangedValueString(string current, string newone, int changeValue)
        {
            if (current != newone)
            {
                //current = newone;
                this.SetChangedTag(changeValue);
                return newone;
            }
            else
                return current;
        }


        /// <summary>
        /// Check if value changed, sets tags and return value (either old or new if new value is 
        /// different. (For string types)
        /// </summary>
        /// <param name="current"></param>
        /// <param name="newone"></param>
        /// <param name="changeValue"></param>
        /// <returns></returns>
        public bool SetChangedValueBool(bool current, bool newone, int changeValue)
        {
            if (current != newone)
            {
                //current = newone;
                this.SetChangedTag(changeValue);
                return newone;
            }
            else
                return current;
        }



        /// <summary>
        /// Check if value changed, sets tags and return value (either old or new if new value is 
        /// different. (For Float/Single types)
        /// </summary>
        /// <param name="current"></param>
        /// <param name="newone"></param>
        /// <param name="changeValue"></param>
        /// <returns></returns>
        public float SetChangedValueFloat(float current, float newone, int changeValue)
        {
            if (current != newone)
            {
                this.SetChangedTag(changeValue);
                return newone;
            }
            else
                return current;
        }



        /// <summary>
        /// Returns true if value for specified variable changed.
        /// </summary>
        /// <param name="changeValue"></param>
        /// <returns></returns>
        public bool HasValueChanged(int changeValue)
        {
            if ((this.WhatChanged & changeValue) == changeValue)
                return true;
            else
                return false;

        }

        public void ResetId()
        {
            RemoveId();
            ResetChange();

            this.ChangedInternal += DatabaseAccessObject.OBJECT_ADD;
        }

        public virtual void RemoveId()
        {
        }



        #endregion


        #region Utils for reading from Db


        /// <summary>
        /// Log SQL Error
        /// </summary>
        /// <param name="action"></param>
        /// <param name="ex"></param>
        /// <param name="sql"></param>
        /// <param name="log_in"></param>
        public void LogSQLError(string action, Exception ex, string sql, ILog log_in)
        {
            LogSQLError(action, ex, sql, null, log_in);
        }

        /// <summary>
        /// Log SQL Error
        /// </summary>
        /// <param name="action"></param>
        /// <param name="ex"></param>
        /// <param name="sql"></param>
        /// <param name="add_param"></param>
        /// <param name="log_in"></param>
        public void LogSQLError(string action, Exception ex, string sql, string add_param, ILog log_in)
        {
            log_in.Error("Sql: " + sql);

            if (add_param != null)
            {
                log_in.Error(this.ObjectName + "(" + add_param + ")::" + action + " exception: " + ex, ex);
                log_in.Debug(action + " " + this.ObjectName + " (" + add_param + ") -- FAILED");
            }
            else
            {
                log_in.Error(this.ObjectName + "::Insert exception: " + ex, ex);
                log_in.Debug(action + " " + this.ObjectName + "  -- FAILED");
            }

        }




        /// <summary>
        /// Is Value received from database null
        /// </summary>
        /// <param name="obj"></param>
        /// <returns></returns>
        public bool IsDbNull(object obj)
        {
            if (obj == null)
                return true;
            else
            {
                string sobj = Convert.ToString(obj);
                if (sobj == "")
                    return true;
                else
                    return false;
            }
        }


        /// <summary>
        /// Returns bool from Db data.
        /// </summary>
        /// <param name="obj"></param>
        /// <param name="default_value"></param>
        /// <returns></returns>
        public bool GetBoolOfDbRow(object obj, bool default_value)
        {
            if (!IsDbNull(obj))
            {
                Int16 num = Convert.ToInt16(obj);
                if (num == 0)
                    return false;
                else
                    return true;
            }
            else
                return default_value;
        }


        /// <summary>
        /// Returns Int16 from Db data.
        /// </summary>
        /// <param name="obj"></param>
        /// <returns></returns>
        public Int16 GetInt16OfDbRow(object obj)
        {
            if (!IsDbNull(obj))
                return Convert.ToInt16(obj);
            else
                return -1;
        }

        /// <summary>
        /// Returns Int32 from Db data.
        /// </summary>
        /// <param name="obj"></param>
        /// <returns></returns>
        public Int32 GetInt32OfDbRow(object obj)
        {
            return GetInt32OfDbRow(obj, -1);
        }


        /// <summary>
        /// Returns Int32 from Db data, with default value.
        /// </summary>
        /// <param name="obj"></param>
        /// <param name="def_value"></param>
        /// <returns></returns>
        public Int32 GetInt32OfDbRow(object obj, Int32 def_value)
        {
            if (!IsDbNull(obj))
                return Convert.ToInt32(obj);
            else
                return def_value;
        }


        /// <summary>
        /// Returns Int64 from Db data.
        /// </summary>
        /// <param name="obj"></param>
        /// <returns></returns>
        public Int64 GetInt64OfDbRow(object obj)
        {
            if (!IsDbNull(obj))
                return Convert.ToInt64(obj);
            else
                return -1;
        }


        /// <summary>
        /// Returns DateTime from Db data.
        /// </summary>
        /// <param name="obj"></param>
        /// <returns></returns>
        public DateTime GetDateTimeOfDbRow(object obj)
        {
            if (!IsDbNull(obj))
                return Convert.ToDateTime(obj);
            else
                return DateTime.MinValue;
        }


        /// <summary>
        /// Returns Blob (byte[]) from Db data.
        /// </summary>
        /// <param name="obj"></param>
        /// <returns></returns>
        public byte[] GetBlobOfDbRow(object obj)
        {
            if (!IsDbNull(obj))
                return (byte[])obj;
            else
                return null;
        }


        /// <summary>
        /// Returns String from Db data.
        /// </summary>
        /// <param name="obj"></param>
        /// <returns></returns>
        public string GetStringOfDbRow(object obj)
        {
            if (!IsDbNull(obj))
                return Convert.ToString(obj);
            else
                return null;
        }


        /// <summary>
        /// Returns Float from Db data.
        /// </summary>
        /// <param name="obj"></param>
        /// <returns></returns>
        public Single GetFloatOfDbRow(object obj)
        {
            if (!IsDbNull(obj))
                return Convert.ToSingle(obj);
            else
                return 0.0f;
        }



        /// <summary>
        /// Returns Int32 value from object.
        /// </summary>
        /// <param name="val"></param>
        /// <returns></returns>
        public Int32 GetIntValue(object val)
        {
            if (val == null)
                return 0;
            else
            {
                try
                {
                    return Convert.ToInt32(val);
                }
                catch
                {
                    return 0;
                }
            }

        }


        /// <summary>
        /// Create Command
        /// </summary>
        /// <param name="sql_command"></param>
        /// <param name="connection"></param>
        /// <param name="transaction"></param>
        /// <returns></returns>
        public IDbCommand CreateCommand(string sql_command, IDbConnection connection, IDbTransaction transaction)
        {
            if (transaction == null)
                return new SqlCeCommand(sql_command, (SqlCeConnection)connection);
            else
                return new SqlCeCommand(sql_command, (SqlCeConnection)connection, (SqlCeTransaction)transaction);
        }



        /// <summary>
        /// Reset Change flag of object.
        /// </summary>
        public void ResetChange()
        {
            this.ChangedInternal = DatabaseAccessObject.OBJECT_NO_ACTION;
            this.WhatChanged = DatabaseAccessObject.CHANGED_NOTHING;
        }


        /// <summary>
        /// Returns string for database enclosed in '' if text is null, null is returned...
        /// </summary>
        /// <param name="text"></param>
        /// <returns></returns>
        public string GetStringForDb(string text)
        {
            if (text == null)
                return "null";
            else
                return "'" + text + "'";
        }


        /// <summary>
        /// Returns string for database enclosed in '' if text is null, null is returned...
        /// </summary>
        /// <param name="text"></param>
        /// <returns></returns>
        public bool GetBoolValueFromInt16(object val)
        {
            if (val == null)
                return false;
            else
            {
                try
                {
                    Int16 gg = Convert.ToInt16(val);

                    if (gg == 0)
                        return false;
                    else
                        return true;
                }
                catch
                {
                    return false;
                }
            }

        }


        /// <summary>
        /// Returns string for database enclosed in '' if text is null, null is returned...
        /// </summary>
        /// <param name="text"></param>
        /// <returns></returns>
        public Int16 GetBoolForDb(bool val)
        {
            if (val == true)
                return 1;
            else
                return 0;
        }



        /// <summary>
        /// Get Date Time string correctly formated for selected database.
        /// </summary>
        /// <param name="dt"></param>
        /// <returns></returns>
        public string GetDateTimeForDbString(DateTime dt, bool canBeNull)
        {
            if (canBeNull)
            {
                if ((dt == null) || (dt == DateTime.MinValue))
                {
                    return " null ";
                }
                else
                {
                    return " convert(datetime, '" + dt.ToString("dd/MM/yyyy HH:mm:ss") + "', 103) ";
                }
            }
            else
            {
                return " convert(datetime, '" + dt.ToString("dd/MM/yyyy HH:mm:ss") + "', 103) ";
            }

        }



        /// <summary>
        /// Returns string for database enclosed in '' if text is null, or 'is null'...
        /// </summary>
        /// <param name="text"></param>
        /// <returns></returns>
        public string GetStringForDbNull(string text)
        {
            if (text == null)
                return " is null";
            else
                return "='" + text + "'";
        }


        /// <summary>
        /// GetDataTable - Returns DataTable for selected sql statement and table name
        /// </summary>
        /// <param name="sqlstatement"></param>
        /// <param name="tableName"></param>
        /// <returns>DataTable</returns>
        public DataTable GetDataTable(string sqlstatement, string tableName, ILog log)
        {
            try
            {
                DataSet ds = new DataSet();
                SqlCeDataAdapter da = new SqlCeDataAdapter(sqlstatement, GGCDbMobile.ConnectionString);
                da.FillSchema(ds, SchemaType.Mapped, "DSReturn");
                da.Fill(ds, tableName);
                return ds.Tables[tableName];
            }
            catch (Exception ex)
            {
                log.Error("GetDataTable()\n[sql=" + sqlstatement + "]\n", ex);
                return new DataTable();
            }
        }


        /// <summary>
        /// Display error and write it to log
        /// </summary>
        /// <param name="source"></param>
        /// <param name="ex"></param>
        public void DisplayError(String source, Exception ex, ILog log)
        {
            Console.WriteLine("Exception [" + source + "]: " + ex);

            if (log != null)
                log.Error("SQLException [" + source + "]: " + ex.Message, ex);
        }


        /// <summary>
        /// Display error and write it to log
        /// </summary>
        /// <param name="source"></param>
        /// <param name="sql"></param>
        /// <param name="ex"></param>
        public void DisplayError(String source, String sql, Exception ex, ILog log)
        {
            Console.WriteLine("Exception [" + source + "]: " + ex);
            if (log != null)
                log.Debug(sql);
            Console.WriteLine(sql);
            if (log != null)
                log.Error("SQLException [" + source + "]: " + ex.Message, ex);
        }



        public const int SQL_INSERT = 0;
        public const int SQL_UPDATE = 1;
        public const int SQL_DELETE = 2;
        public const int SQL_CREATE = 3;
        public const int SQL_DELETE_ALL = 4;

        public string[] ActionString = { "Insert", "Update", "Delete", "Create", "Delete All" };


        /// <summary>
        /// Main method (sql) for processing data from db
        /// </summary>
        /// <param name="connection">conection instance</param>
        /// <param name="transaction">transaction instance (or null)</param>
        /// <param name="sql">sql string</param>
        /// <param name="action">type of action</param>
        /// <param name="log">instance of logger</param>
        /// <returns></returns>
        public bool ProcessSqlItem(IDbConnection connection, IDbTransaction transaction, string sql, int action, ILog log)
        {
            try
            {
                log.Debug(this.ActionString[action] + " " + this.ObjectName + " -- Start");

                IDbCommand cmd = null;

                log.Debug(sql);

                cmd = CreateCommand(sql, connection, transaction);
                cmd.ExecuteNonQuery();
                cmd.Dispose();

                log.Debug(this.ActionString[action] + " " + this.ObjectName + " -- End");
                return true;
            }
            catch (Exception ex)
            {
                LogSQLError(this.ObjectName, ex, sql, log);
                throw ex;
                //return false;
            }
        }


        /// <summary>
        /// Returns Id in Int64 (long)
        /// </summary>
        /// <param name="connection">conection instance</param>
        /// <param name="transaction">transaction instance (or null)</param>
        /// <param name="sql">sql string</param>
        /// <param name="log">instance of logger</param>
        /// <returns></returns>
        public Int64 GetLongId(IDbConnection connection, IDbTransaction transaction, string sql, ILog log)
        {
            try
            {
                log.Debug(" GetId  " + this.ObjectName + " -- Start");

                IDbCommand cmd = null;

                log.Debug(sql);

                cmd = CreateCommand(sql, connection, transaction);
                object obj = cmd.ExecuteScalar();
                Int64 id = (Int64)obj;
                cmd.Dispose();

                log.Debug(" GetId  " + this.ObjectName + " -- End");
                return id;
            }
            catch (Exception ex)
            {
                LogSQLError(this.ObjectName, ex, sql, log);
                log.Debug(" GetId  " + this.ObjectName + " -- End");
                return -1;
            }
        }


        /// <summary>
        /// Returns Id in Int32 (int)
        /// </summary>
        /// <param name="connection">conection instance</param>
        /// <param name="transaction">transaction instance (or null)</param>
        /// <param name="sql">sql string</param>
        /// <param name="log">instance of logger</param>
        /// <returns></returns>
        public Int32 GetInt32Id(IDbConnection connection, IDbTransaction transaction, string sql, ILog log)
        {
            try
            {
                log.Debug(" GetId  " + this.ObjectName + " -- Start");

                IDbCommand cmd = null;

                log.Debug(sql);

                cmd = CreateCommand(sql, connection, transaction);
                object obj = cmd.ExecuteScalar();
                Int32 id = (Int32)obj;
                cmd.Dispose();

                log.Debug(" GetId  " + this.ObjectName + " -- End");
                return id;
            }
            catch (Exception ex)
            {
                LogSQLError(this.ObjectName, ex, sql, log);
                log.Debug(" GetId  " + this.ObjectName + " -- End");
                return -1;
            }
        }


        public String GetNextId(IDbConnection connection, IDbTransaction transaction)
        {
            if ((this.ObjectUniqueId == null) ||
                (this.ObjectUniqueId.Length == 0)
             
                )
            {
                return "" + GetNextIdInternal(connection, transaction);
            }
            else
            {
                long id;
                try
                {
                    id = Convert.ToInt64(this.ObjectUniqueId);

                    if (id <= 0)
                    {
                        return "" + GetNextIdInternal(connection, transaction);
                    }

                    return this.ObjectUniqueId;
                }
                catch (Exception ex)
                {
                }

                return "" + GetNextIdInternal(connection, transaction);
            }

        }


        public long GetNextIdInternal(IDbConnection connection, IDbTransaction transaction)
        {
            
            try
            {
                DummyDAO dd = new DummyDAO();

                long id = dd.GetLongId(connection, 
                    transaction,
                    "select max(" + this.IdColumnName + ") from " + this.TableName,
                    log);

                id++;

                return id;

                /*
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("select max(" + this.IdColumnName + ") from " + this.TableName);

                if (rs.next())
                {
                    long id = rs.getLong(1);
                    id++;

                    return id;
                }
                else
                    return 1; */

            }
            catch (Exception ex)
            {
                return 1;
            }


        }





        public override string ToString()
        {
            return this.ObjectName + " [id=" + this.ObjectUniqueId + "]";
        }

        #endregion

        public virtual int TableVersion
        {
            get
            {
                return 1;
            }
        }

        public virtual void ImportData(string text, int table_version, Object[] parameters)
        {
            log.Warn("ImportData for " + this.ObjectName + " not implemented.");
        }

        public virtual string ExportData(int db_version, Object[] parameters)
        {
            log.Warn("ExportData for " + this.ObjectName + " not implemented.");
            return null;
        }



    }
}