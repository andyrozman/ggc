package ggc.core.db.tool;

import com.atech.db.hibernate.tool.DbTool;
//import com.atech.db.hibernate.tool.DbToolApplicationInterface;
//import java.util.ArrayList;
//import java.util.Iterator;

public class DbToolGGC 
{
    public DbToolGGC()
    {
        DbToolApplicationGGC appl = new DbToolApplicationGGC();
        /*DbTool tool =*/ new DbTool(appl);
    }

    public static void main(String args[])
    {
        new DbToolGGC();
    }

}
