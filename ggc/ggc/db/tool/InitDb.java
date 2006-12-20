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
 *  Filename: NutritionImport
 *  Purpose:  Imports nutrition data from USDA Nutrient Database for Standard 
 *      Reference (from Release 18 up)
 *
 *  Author:   andyrozman
 */

package ggc.db.tool;


import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import javax.swing.*;

import ggc.db.datalayer.FoodDescription;
import ggc.db.datalayer.FoodGroup;
import ggc.db.datalayer.FoodHomeWeight;
import ggc.db.GGCDb;
import ggc.db.hibernate.ColorSchemeH;
import ggc.db.hibernate.SettingsMainH;
//import ggc.gui.StatusBar;
//import ggc.util.GGCProperties;
import ggc.util.I18nControl;



public class InitDb 
{

    String path = "../data/nutrition/";

    GGCDb m_db = null;

    public InitDb()
    {
	m_db = new GGCDb();
	m_db.initDb();
	m_db.createDatabase();

        initSettings();
	loadNutritionDatabase();
    }


    public void initSettings()
    {

        SettingsMainH seti = new SettingsMainH(I18nControl.getInstance().getMessage("UNNAMED_USER"), 
                       "Insulin 1", "Ins1", "Insulin 2", "Ins2", 0, "No port available", 
                       2, 60.0f, 200.0f, 80.0f, 120.0f, 
                       3.0f, 20.0f, 4.4f, 14.0f,
                       2, "blueMetalthemepack.zip",
                       0, 0, 0, 0, 0, 0, 0, 
                       "", 1100, 1800, 2100, "0", "Default Scheme");
        seti.setId(1);

        m_db.addHibernate(seti);

        ColorSchemeH colors = new ColorSchemeH(
        "Default Scheme", 0, -65485, -6750208, -163654, -81409, -1184275, -16724788, 
        -6710785, -16776961, -6711040, -16724941);
        colors.setId(1);

        m_db.addHibernate(colors);

    }


    // load nutrition database
    public void loadNutritionDatabase()
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

	int i=0;

	try 
	{

	    System.out.println("\nInsert Food Description (food_des.txt) (100/dot)");

	    BufferedReader br = new BufferedReader(new FileReader(new File(path+"FOOD_DES.txt")));
	    String line = null;

	    while ((line=br.readLine())!=null) 
	    {

		line = parseExpressionFull(line, "^^", "^0.0^");

		if (line.charAt(line.length()-1)=='^') 
		    line = line+"0.0";

		StringTokenizer strtok = new StringTokenizer(line, "^");
		FoodDescription fd = new FoodDescription();

		fd.setId(getLong(strtok.nextToken()));		 // NDB_No	
		fd.setFood_group_id(getInt(strtok.nextToken()));    // FdGrp_Cd
		fd.setName(getString(strtok.nextToken()));  // Long Desc
		fd.setI18n_name(getString(strtok.nextToken()));  // Short Desc

		strtok.nextToken();                              // - ComName
		strtok.nextToken();                              // - ManufName
		strtok.nextToken();                              // - Survey
		strtok.nextToken();                              // - Ref Desc

		fd.setRefuse(getFloat(strtok.nextToken()));    // Refuse

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

        //XXX: probable NullPointer: if id_2==id fd will stay null!!
		if (id_2!=id)
		{
		    if (id!=-1)
		    {
			
			m_db.edit(fd);
		    }

		    id = id_2;
            //IDEA: do the following three lines have to be inside the if?
		    fd = new FoodDescription();
		    fd.setId(id);
		    m_db.get(fd);

		    if (i%100==0)
			System.out.print(".");

		    i++;
		}
        else
        {
            System.err.println(InitDb.class.getName()
                    + ": returning to prevent NullPointerException! Please fix!");
            return;
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


    public void insertHomeWeightData()
    {

        try 
        {

            System.out.println("\nInsert Home Weight (WEIGHT.txt) (200/dot)");

	    int i=0;

            BufferedReader br = new BufferedReader(new FileReader(new File(path+"WEIGHT.txt")));
            String line = null;
            
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


    //XXX: what if input isn't a valid int?
    public int getInt(String input)
    {

        if (input.startsWith("~")) 
            input = input.substring(1, input.length()-1);


        if (input.length()==0) 
            return 0;
        else
            return Integer.parseInt(input);

    }


    //XXX: what if input isn't a valid short?
    public short getShort(String input)
    {

        if (input.startsWith("~")) 
            input = input.substring(1, input.length()-1);


        if (input.length()==0) 
            return 0;
        else
            return Short.parseShort(input);

    }



    //XXX: what if input isn't a valid long?
    public long getLong(String input)
    {

        if (input.startsWith("~")) 
            input = input.substring(1, input.length()-1);


        if (input.length()==0) 
            return 0;
        else
            return Long.parseLong(input);

    }


    //XXX: what if input isn't a valid float?
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
	new InitDb();
    }


}
