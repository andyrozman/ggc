/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: ImportDailyValues
 *  Purpose:  Imports daily values (from export from Meter Tool Import, or some export) 
 *
 *  Author:   andyrozman  {andy@atech-software.com}
 */

package ggc.core.db.tool.transfer;

import ggc.core.db.GGCDb;
import ggc.core.db.hibernate.DayValueH;
import ggc.core.util.DataAccess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.StringTokenizer;

import com.atech.db.hibernate.transfer.ImportTool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cfg.Configuration;


public class ImportDailyValues extends ImportTool
{

    GGCDb m_db = null;
    public String file_name;
    private static Log log = LogFactory.getLog(ImportDailyValues.class); 

    DataAccess m_da = DataAccess.getInstance();

    
    public ImportDailyValues(String file_name)
    {
	this(file_name, true);
    }
    
    
    public ImportDailyValues(String file_name, boolean identify)
    {
	super(false);

    	m_db = new GGCDb();
    	m_db.initDb();
    	this.file_name = file_name;

    	if (identify)
    	    importDailyValues();
    
    	System.out.println();
	
    }

    public ImportDailyValues(Configuration cfg, String file_name)
    {
	super(cfg);
    	//m_db = new GGCDb();
    	//m_db.initDb();
    	this.file_name = file_name;

    	importDailyValues();
    
    	System.out.println();
    }


    public void importDailyValues()
    {
	
	String line = null;

	try 
	{
	    System.out.println("\nLoading DailiyValues (5/dot)");

	    BufferedReader br = new BufferedReader(new FileReader(new File(file_name)));
	    
	    int i=0;

	    
	    while ((line=br.readLine())!=null) 
	    {
		if (line.startsWith(";"))
		    continue;
	
		
		//line = line.replaceAll("||", "| |");
		line = m_da.replaceExpression(line, "||","| |");
		
		StringTokenizer strtok = new StringTokenizer(line, "|");

		
		
		DayValueH dvh = new DayValueH();
		
		//; Columns: id,dt_info,bg,ins1,ins2,ch,meals_ids,act,comment
		
		//1|200603250730|0|10.0|0.0|0.0|null|null|
// id; dt_info; bg; ins1; ins2; ch; meals_ids; extended; person_id; comment 

		long id = this.getLong(strtok.nextToken());
		
		if (id!=0)
		    dvh.setId(id);
		    
		dvh.setDt_info(getLong(strtok.nextToken()));
		dvh.setBg(getInt(strtok.nextToken()));
		dvh.setIns1((int)getFloat(strtok.nextToken()));
		dvh.setIns2((int)getFloat(strtok.nextToken()));
		dvh.setCh(getFloat(strtok.nextToken()));
		dvh.setMeals_ids(getString(strtok.nextToken()));
		dvh.setExtended(getString(strtok.nextToken()));
		
		int person_id = this.getInt(strtok.nextToken());
		
		if (person_id==0)
		    dvh.setPerson_id(1);
		else
		    dvh.setPerson_id(person_id);
		
		dvh.setComment(getString(strtok.nextToken()));
		dvh.setChanged(getLong(strtok.nextToken()));
		
		/*
		String act = getString(strtok.nextToken());
		
		if (act != null)
		{
		    dvh.setExtended("ACTIVITY=" + act);
		}
		
		String bef = "MTI";
//		String bef = null;
		
		if (strtok.hasMoreElements())
		{
        	    String comm = getString(strtok.nextToken());
        	    
        	    // remove
        	    if (bef!=null)
        		comm += ";" + bef;
        		
        	    if (comm!=null)
        		dvh.setComment(comm);
		}
		else
		{
        	    if (bef!=null)
        		dvh.setComment(bef);
		}
		
		*/
		
		m_db.addHibernate(dvh);
		
		i++;
		
		if (i%5==0)
		    System.out.print(".");
	    }

	} 
	catch (Exception ex) 
	{
	    //System.err.println("Error on loadDailyValues: " + ex);
	    log.error("Error on importDailyValues: \nData: " + line +"\nException: " + ex, ex);
	    //ex.printStackTrace();
	}
	
    }
    

    public static void main(String args[])
    {
	if (args.length == 0)
	{
	    System.out.println("You need to specify import file !");
	    return;
	}
	
	//GGCDb db = new GGCDb();
	
	new ImportDailyValues(args[0]);
    }


}
