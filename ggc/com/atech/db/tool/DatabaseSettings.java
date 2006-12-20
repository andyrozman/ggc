package com.atech.db.tool;

import java.io.BufferedWriter;
import java.util.Hashtable;

public class DatabaseSettings 
{
    public int number = 0;

    public String db_name = null;
    public String name = null;
    public String driver = null;
    public String driver_class = null;
    public String url = null;
    public String port = null;
    public String dialect = null;

    public String username = null;
    public String password = null;

    public boolean isDefault = false;

    public Hashtable settings = null;

/*
    public String hostname = null;
    public String url = null;
    public String port = null;
    public String database = null;
*/

    public DatabaseSettings()
    {
        settings = new Hashtable();
    }

    public DatabaseSettings(String name, String driver, String url, String port, String dialect)
    {

	this.name = name;
	this.driver = driver;
	this.url = url;
	this.port = port;
	this.dialect = dialect;

	settings = new Hashtable();

    }

    public void write(BufferedWriter bw) throws java.io.IOException
    {
    	bw.write("\n#\n# Database #" + this.number +" - " + this.name + "\n#\n");
    	bw.write("DB" + this.number + "_CONN_NAME=" + this.name +"\n");
    	bw.write("DB" + this.number + "_DB_NAME=" + this.db_name +"\n");
    	bw.write("DB" + this.number + "_CONN_DRIVER_CLASS=" + this.driver_class +"\n");
    	bw.write("DB" + this.number + "_CONN_URL=" + this.url +"\n");
    	bw.write("DB" + this.number + "_CONN_USERNAME=" + this.username +"\n");
    	bw.write("DB" + this.number + "_CONN_PASSWORD=" + this.password +"\n");
    	bw.write("DB" + this.number + "_HIBERNATE_DIALECT=" + this.dialect +"\n");
    }


    @Override
    public String toString()
    {
	return number + " - " + name;
    }

}
