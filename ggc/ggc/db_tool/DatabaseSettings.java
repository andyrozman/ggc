package ggc.db.db_tool;

import java.util.Hashtable;

public class DatabaseSettings 
{

    Hashtable listOfDatabases = null;

    public String databaseSettings[] = 
    {

        // DB2
        "DB2",
        "com.ibm.db2.jcc.DB2Driver",
        "jdbc:db2://%%HOSTNAME%%:%%PORT%%/%%DATABASE%%",
	"",
        "net.sf.hibernate.dialect.DB2Dialect",

        // DB2 AS/400
        "DB2 AS/400",
        "DriverClass",
        "URL",
        "net.sf.hibernate.dialect.DB2400Dialect",

        // DB2 OS390
        "DB2 OS390",
        "DriverClass",
        "URL",
        "net.sf.hibernate.dialect.DB2390Dialect",

        // FrontBase
        "FrontBase",
        "DriverClass",
        "URL",
        "net.sf.hibernate.dialect.FrontbaseDialect",

        // Firebird
        "Firebird",
        "DriverClass",
        "URL",
        "net.sf.hibernate.dialect.FirebirdDialect",

        // HypersonicSQL
        "HypersonicSQL",
        "DriverClass",
        "URL",
        "net.sf.hibernate.dialect.HSQLDialect",

        // Informix
        "Informix",
        "DriverClass",
        "URL",
        "net.sf.hibernate.dialect.InformixDialect",

        // Ingres
        "Ingres",
        "DriverClass",
        "URL",
        "net.sf.hibernate.dialect.IngresDialect",

        // Interbase
        "Interbase",
        "DriverClass",
        "URL",
        "net.sf.hibernate.dialect.InterbaseDialect",

        // Microsoft SQL Server
        "Microsoft SQL Server",
        "DriverClass",
        "URL",
        "net.sf.hibernate.dialect.SQLServerDialect",

        // Mckoi SQL
        "Mckoi SQL",
        "DriverClass",
        "URL",
        "net.sf.hibernate.dialect.MckoiDialect",

        // MySQL
        "MySQL",
        "com.mysql.jdbc.Driver",
        "jdbc:mysql://%%HOSTNAME%%:%%PORT%/%%DATABASE%%?user=%%USER%%&password=%%PASSWORD%%&useUnicode=true&characterEncoding=utf-8",
	"",
        "org.hibernate.dialect.MySQLDialect",

        // Oracle (any version)
        "Oracle (any version)",
        "DriverClass",
        "URL",
	"",
        "net.sf.hibernate.dialect.OracleDialect",

        // Oracle 9/10g
        "Oracle 9/10g",
        "DriverClass",
        "URL",
	"",
        "net.sf.hibernate.dialect.Oracle9Dialect",

        // Pointbase
        "Pointbase",
        "DriverClass",
        "URL",
	"",
        "net.sf.hibernate.dialect.PointbaseDialect",

        // PostgreSQL
        "PostgreSQL",
        "org.postgresql.Driver",
        "jdbc:postgresql://%%HOSTNAME%%:%%PORT%%/%%DATABASE%%?user=%%USER%%postgres&password=%%PASSWORD%%",
	"5432",
        "net.sf.hibernate.dialect.PostgreSQLDialect",

        // Progress
        "Progress",
        "DriverClass",
        "URL",
	"",
        "net.sf.hibernate.dialect.ProgressDialect",

        // SAP DB
        "SAP DB",
        "DriverClass",
        "URL",
	"",
        "net.sf.hibernate.dialect.SAPDBDialect",

        // Sybase
        "Sybase",
        "DriverClass",
        "URL",
	"",
        "net.sf.hibernate.dialect.SybaseDialect",

        // Sybase Anywhere
        "Sybase Anywhere",
        "DriverClass",
        "URL",
	"",
        "net.sf.hibernate.dialect.SybaseAnywhereDialect",

        // Undefined
        "Undefined",
        "Unknown_driver",
        "Unknown_URL",
	"",
        "Dialect",

        // Some Database
        "Name",
        "DriverClass",
        "URL",
	"",
        "Dialect",

        /*
// Derby = DerbyDialect
// org\hibernate\dialect\JDataStoreDialect.class
org\hibernate\dialect\MimerSQLDialect.class
org\hibernate\dialect\MySQLInnoDBDialect.class
org\hibernate\dialect\MySQLMyISAMDialect.class
org\hibernate\dialect\RDMSOS2200Dialect.class
org\hibernate\dialect\RDMSOS2200Dialect.class
org\hibernate\dialect\Sybase11Dialect.class
org\hibernate\dialect\TimesTenDialect.class

*/

    };

    public Hashtable getListOfAvailableDatabases()
    {
        if (listOfDatabases == null) 
        {
            listOfDatabases = new Hashtable();

            for (int i=0; i<this.databaseSettings.length; i+=5) 
            {
                listOfDatabases.put(getDatabaseName(i), ""+i);
            }
        }

        return listOfDatabases;
    }

    public String getDatabaseName(int index)
    {
        return databaseSettings[index];
    }

    public String getJdbcDriver(int index)
    {
        return databaseSettings[index+1];
    }

    public String getJdbcURL(int index)
    {
        return databaseSettings[index+2];
    }

    public String getDatabasePort(int index)
    {
	return databaseSettings[index+3];
    }


    public String getHibernateDialect(int index)
    {
        return databaseSettings[index+4];
    }



}
