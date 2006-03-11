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
 *  Filename: HSQLHandler.java
 *  Purpose:  Handler to access a HSQL (Hypersonic) Database.
 *
 *  Author:   andyrozman
 */

package ggc.db.carbs;


import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import javax.swing.*;

import ggc.datamodels.DailyValues;
import ggc.datamodels.DailyValuesRow;
import ggc.datamodels.HbA1cValues;
import ggc.db.datalayer.FoodDescription;
import ggc.db.datalayer.FoodGroup;
import ggc.db.datalayer.FoodHomeWeight;
import ggc.db.datalayer.GGCDb;
import ggc.gui.StatusBar;
import ggc.nutrition.NutritionInfo;
import ggc.util.GGCProperties;



public class NutritionImport 
{

    //private static HSQLHandler singleton = null;
    //private GGCProperties props = GGCProperties.getInstance();

    boolean connected = false;
    Connection con = null;

    String path = "../data/nutrition/";


    GGCDb m_db = null;

    public NutritionImport()
    {

	connected = false;
	m_db = new GGCDb();
	m_db.initDb();
	m_db.createDatabase();
	//DataBaseHandler.connectedToDB = true;

    }




    public void loadModifiedNutritionDatabase()
    {

        this.insertFoodGroups();
        this.insertFoodDescription();
        this.insertNutritionData();
        this.insertHomeWeightData();

    }


    public void insertFoodGroups()
    {

	try 
	{
	    System.out.println("\nLoading Food Groups (FD_GROUP.txt) (1/dot)");

	    BufferedReader br = new BufferedReader(new FileReader(new File(path+"FD_GROUP.txt")));
	    String line = null;

	    while ((line=br.readLine())!=null) 
	    {
		StringTokenizer strtok = new StringTokenizer(line, "^");

		FoodGroup fg = new FoodGroup();
		fg.setId(getInt(strtok.nextToken()));
		fg.setDescription(getString(strtok.nextToken()));

		m_db.add(fg);
		System.out.print(".");
	    }

	} 
	catch (Exception ex) 
	{
	    System.err.println("Error on insertFoodGroups(): " + ex);
	}

    }


    public void insertFoodDescription()
    {

	// 204 = Fat(Lipid) (g)
	// 205 = Carbohydrate by difference (g)
	// 208 = Energy (kcal)
	// 268 = Energy (kJ)
	// 269 = Sugar (total) (g)


	String tmp ="";
	int i=0;

	try 
	{

	    System.out.println("\nInsert Food Description (food_des.txt) (100/dot)");

	    BufferedReader br = new BufferedReader(new FileReader(new File(path+"FOOD_DES.txt")));
	    String line = null;


	    //PreparedStatement ps = con.prepareStatement("INSERT INTO food_des (fd_no, fd_gp, fd_desc, short_desc, refuse) VALUES (?,?,?,?,?)");


	    while ((line=br.readLine())!=null) 
	    {

		line = parseExpressionFull(line, "^^", "^0.0^");

		if (line.charAt(line.length()-1)=='^') 
		    line = line+"0.0";

		//System.out.println(line);

		StringTokenizer strtok = new StringTokenizer(line, "^");
		FoodDescription fd = new FoodDescription();

		fd.setId(getLong(strtok.nextToken()));		 // NDB_No	
		fd.setFood_group_id(getInt(strtok.nextToken()));    // FdGrp_Cd
		fd.setDescription(getString(strtok.nextToken()));  // Long Desc
		fd.setShort_description(getString(strtok.nextToken()));  // Short Desc

		strtok.nextToken();                              // - ComName
		strtok.nextToken();                              // - ManufName
		strtok.nextToken();                              // - Survey
		strtok.nextToken();                              // - Ref Desc

		fd.setRefuse(getFloat(strtok.nextToken()));    // Refuse

		//System.out.println(fd.getId());

		m_db.add(fd);

		if (i%100==0)
		    System.out.print(".");

		i++;

	    }

	} 
	catch (Exception ex) 
	{
	    System.err.println("Error on insertFoodDescription(): " + ex);
	    ex.printStackTrace();
	}

    }



    public void insertNutritionData()
    {

	String tmp ="";
	int i=0;

	try 
	{

	    System.out.println("\nInsert Nutrition Data into food_des (nut_data.txt) (100/dot)");

	    BufferedReader br = new BufferedReader(new FileReader(new File(path+"NUT_DATA.txt")));
	    String line = null;

	    long id = -1;
	    FoodDescription fd = null;

	    while ((line=br.readLine())!=null) 
	    {

		line = parseExpressionFull(line, "^^", "^0.0^");

		if (line.charAt(line.length()-1)=='^') 
		    line = line+"0.0";


		// 204 = Fat(Lipid)
		// 205 = Carbohydrate by difference
		// 208 = Energy (kcal)
		// 268 = Energy (kJ)
		// 269 = Sugar (total)

		StringTokenizer strtok = new StringTokenizer(line, "^");
		long id_2 = getLong(strtok.nextToken());

		if (id_2!=id)
		{
		    if (id!=-1)
		    {
			
			m_db.edit(fd);
		    }

		    id = id_2;
		    fd = new FoodDescription();
		    fd.setId(id);
		    m_db.get(fd);

		    if (i%100==0)
			System.out.print(".");

		    i++;
		}


		int type = getInt(strtok.nextToken());
		float value = getFloat(strtok.nextToken());

		if (type==204)
		{
		    // 204 = Fat(Lipid)
		    fd.setFat_g(value);
		}
		else if (type==205)
		{
		    // 205 = Carbohydrate by difference
		    fd.setCH_g(value);
		}
		else if (type==208)
		{
		    // 208 = Energy (kcal)
		    fd.setEnergy_kcal(value);
		}
		else if (type==268)
		{
		    // 268 = Energy (kJ)
		    fd.setEnergy_kJ(value);
		}
		else if (type==269)
		{
		    // 269 = Sugar (total)
		    fd.setSugar_g(value);
		}

	    }


	} 
	catch (Exception ex) 
	{
	    System.err.println("Error on insertNutritionData(): " + ex);
	    ex.printStackTrace();
	}

    }







    // OLD






    public void insertHomeWeightData()
    {
/*
CREATE TABLE `home_weight` (
  `fd_no` bigint(20) NOT NULL default '0',
  `seq` int(11) NOT NULL default '0',
  `amount` int(11) NOT NULL default '1',
  `msr_desc` varchar(100) NOT NULL default '',
  `weight_g` int(11) NOT NULL default '0'
) ENGINE=MyISAM DEFAULT CHARSET=utf8; 
*/

//        connectDb();

        String tmp ="";

        try 
        {

            System.out.println("\nInsert Home Weight (WEIGHT.txt) (200/dot)");

	    int i=0;

            BufferedReader br = new BufferedReader(new FileReader(new File(path+"WEIGHT.txt")));
            String line = null;
            
            //PreparedStatement ps = con.prepareStatement("INSERT INTO home_weight (fd_no, seq, amount, msr_desc, weight_g) VALUES (?,?,?,?,?)");

            while ((line=br.readLine())!=null) 
            {

                line = parseExpressionFull(line, "^^", "^0.0^");

                if (line.charAt(line.length()-1)=='^') 
                    line = line+"0.0";

                StringTokenizer strtok = new StringTokenizer(line, "^");

		FoodHomeWeight fhw = new FoodHomeWeight();

		fhw.setFood_number(getLong(strtok.nextToken()));
		fhw.setSequence(getInt(strtok.nextToken()));
		fhw.setAmount(getFloat(strtok.nextToken()));
		fhw.setMsr_desc(strtok.nextToken());
		fhw.setWeight_g(getFloat(strtok.nextToken()));

		m_db.add(fhw);

		if (i%200==0)
		    System.out.print(".");

		i++;

            }

        } 
        catch (Exception ex) 
        {
            System.err.println("Error on insertHomeWeightData(): " + ex);
            ex.printStackTrace();
        }


    }









/*
    public void insertHomeWeightData()
    {
/*
CREATE TABLE `home_weight` (
  `fd_no` bigint(20) NOT NULL default '0',
  `seq` int(11) NOT NULL default '0',
  `amount` int(11) NOT NULL default '1',
  `msr_desc` varchar(100) NOT NULL default '',
  `weight_g` int(11) NOT NULL default '0'
) ENGINE=MyISAM DEFAULT CHARSET=utf8; 
*/

//        connectDb();
/*
	String tmp ="";

	try 
	{

	    System.out.println("Insert Home Weight (WEIGHT.txt)");

	    BufferedReader br = new BufferedReader(new FileReader(new File("WEIGHT.txt")));
	    String line = null;

	    PreparedStatement ps = con.prepareStatement("INSERT INTO home_weight (fd_no, seq, amount, msr_desc, weight_g) VALUES (?,?,?,?,?)");


	    while ((line=br.readLine())!=null) 
	    {

		line = parseExpressionFull(line, "^^", "^0.0^");

		if (line.charAt(line.length()-1)=='^') 
		    line = line+"0.0";


		StringTokenizer strtok = new StringTokenizer(line, "^");

		FoodHomeWeight fhw = new FoodHomeWeight();

		fhw.setFood_number(getLong(strtok.nextToken()));
		fhw.setSequence(strtok.nextToken());
		fhw.setAmount(strtok.nextToken());
		fhw.setMsr_desc(strtok.nextToken());
		fhw.setWeight_g(getFloat(strtok.nextToken()));

	    }

	} 
	catch (Exception ex) 
	{
	    System.err.println("Error on insertHomeWeightData(): " + ex);
	    ex.printStackTrace();
	}


    }



    public ArrayList getNutritionTypes()
    {


        connectDb();

        ArrayList list = new ArrayList();

        try 
        {
            //System.out.println(" Loading Food Groups (fd_group.txt)");


            //BufferedReader br = new BufferedReader(new FileReader(new File("fd_group.txt")));
            //String line = null;

            
            PreparedStatement ps = con.prepareStatement("SELECT fd_gp, gp_desc FROM fd_group");

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {

                NutritionInfo n = new NutritionInfo(rs.getInt(1), 1, rs.getString(2));

                list.add(n);

            }


        } 
        catch (Exception ex) 
        {
            System.err.println("Error on getNutritionTypes: " + ex);
        }

        return list;


    }



    public ArrayList getNutritionTypesFoods(int group)
    {


        connectDb();

        ArrayList list = new ArrayList();

        try 
        {
            //System.out.println(" Loading Food Groups (fd_group.txt)");


            //BufferedReader br = new BufferedReader(new FileReader(new File("fd_group.txt")));
            //String line = null;

            
            PreparedStatement ps = con.prepareStatement("SELECT fd_no, fd_desc, short_desc FROM food_des WHERE fd_gp=" + group);

            ResultSet rs = ps.executeQuery();

            while(rs.next())
            {

                NutritionInfo n = new NutritionInfo(rs.getInt(1), 2, rs.getString(2), rs.getString(3));

                list.add(n);

            }


        } 
        catch (Exception ex) 
        {
            System.err.println("Error on getNutritionTypesFoods: " + ex);
        }

        return list;


    }
*/




/*
    public void initDb()
    {
        
	System.out.println("create");

	try 
	{

	    connectDb();
            
	    Statement statement;


            //statement = con.createStatement();
	    //statement.execute("DROP TABLE DayValues");



            statement = con.createStatement();
            String query2 = "CREATE TABLE DayValues( " + 
			    "id bigint NOT NULL IDENTITY PRIMARY KEY," + 
			    "dt_date int NOT NULL, "+
		            "dt_time int NOT NULL, "+
			    "bg decimal(7,2) NOT NULL, "+
			    "ins1 decimal(6,2) NOT NULL, "+
			    "ins2 decimal(6,2) NOT NULL, "+
			    "bu decimal(6,2) NOT NULL, "+
			    "act tinyint NOT NULL, "+
			    "comment varchar(255) NOT NULL)";

            statement.execute(query2);
            //dbName = name;

	    //setStatus();
        } catch (SQLException sqle) {
            System.err.println(sqle);
        }
    }

*/


    public void disconnectDb()
    {
        try 
        {
            if (con!=null) 
                con.close();
        } 
        catch (Exception sqle) 
        {
            System.err.println(sqle);
        } 
        finally {
            connected = false;
            //connectedToDB = false;

	    //dbStatus = "HSQL [" + m_ic.getMessage("NO_CONNECTION") + "]";
	    //StatusBar.getInstance().setStatus();

//	    setStatus();

            //StatusBar.getInstance().setDataSourceText("HSQL [" + m_ic.getMessage("NO_CONNECTION") + "]");
        }
    }


     /**
     * Metoda za delo s Stringi.
     * 
     * @param in         Vhodni string
     * @param expression Kaj iscemo
     * @param replace    S cim bomo zamenjali
     * 
     * @return Spremenjen String.
     */
    private String parseExpression(String in, String expression, String replace)
    {

        StringBuffer buffer;

        int idx=in.indexOf(expression);
        
        if (replace==null)
            replace ="";
        
        if (idx==-1)
            return in;

        buffer = new StringBuffer();
        
        while (idx!=-1)
        {
            buffer.append(in.substring(0,idx));
            buffer.append(replace);

            in = in.substring(idx+expression.length());
            
            idx=in.indexOf(expression);
        }

        buffer.append(in);

        return buffer.toString();

    }



     /**
     * Metoda za delo s Stringi.
     * 
     * @param in         Vhodni string
     * @param expression Kaj iscemo
     * @param replace    S cim bomo zamenjali
     * 
     * @return Spremenjen String.
     */
    private String parseExpressionFull(String in, String expression, String replace)
    {

        String buffer;

        int idx=in.indexOf(expression);
        
        if (replace==null)
            replace ="";
        
        if (idx==-1)
            return in;

        buffer = "";
        
        if (idx!=-1)
        {
            
            buffer = in.substring(0,idx) + replace + in.substring(idx+expression.length());
            
            idx=in.indexOf(expression);

            if (idx!=-1) 
                buffer = parseExpressionFull(buffer,expression,replace);

        }

        return buffer;

    }


    public int getInt(String input)
    {

        if (input.startsWith("~")) 
            input = input.substring(1, input.length()-1);


        if (input.length()==0) 
            return 0;
        else
            return Integer.parseInt(input);

    }


    public short getShort(String input)
    {

        if (input.startsWith("~")) 
            input = input.substring(1, input.length()-1);


        if (input.length()==0) 
            return 0;
        else
            return Short.parseShort(input);

    }



    public long getLong(String input)
    {

        if (input.startsWith("~")) 
            input = input.substring(1, input.length()-1);


        if (input.length()==0) 
            return 0;
        else
            return Long.parseLong(input);

    }


    public float getFloat(String input)
    {

        if (input.startsWith("~")) 
            input = input.substring(1, input.length()-1);


        if (input.length()==0) 
            return 0;
        else
            return Float.parseFloat(input);

    }



    public String getString(String input)
    {

        if (input.startsWith("~")) 
            input = input.substring(1, input.length()-1);

        return input;

    }




    public static void main(String args[])
    {

	NutritionImport ni = new NutritionImport();

        ni.insertFoodGroups();
        ni.insertFoodDescription();
        ni.insertNutritionData();
        ni.insertHomeWeightData();

    }


}
