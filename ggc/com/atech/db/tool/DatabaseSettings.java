package ggc.db.db_tool;

import java.util.Hashtable;

public class DatabaseSettings 
{

    public String name = null;
    public String driver = null;
    public String def_url = null;
    public String def_port = null;
    public String dialect = null;

    public String username = null;
    public String password = null;

    public Hashtable settings = null;

/*
    public String hostname = null;
    public String url = null;
    public String port = null;
    public String database = null;
*/

    public DatabaseSettings(String name, String driver, String url, String port, String dialect)
    {

	this.name = name;
	this.driver = driver;
	this.def_url = url;
	this.def_port = port;
	this.dialect = dialect;

	settings = new Hashtable();

    }



}
