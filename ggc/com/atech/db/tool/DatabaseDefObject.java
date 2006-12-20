package com.atech.db.tool;

import java.util.Hashtable;

public class DatabaseDefObject
{

    public String name = null;
    public String driver = null;
    public String url = null;
    public String port = null;
    public String dialect = null;
    public String short_dialect = null;

    public DatabaseDefObject(String name, String driver, String url, String port, String dialect, String sh_dialect)
    {
	this.name = name;
	this.driver = driver;
	this.url = url;
	this.port = port;
	this.dialect = dialect;
	this.short_dialect = sh_dialect;
    }

    @Override
    public String toString()
    {
	return name;
    }

}
