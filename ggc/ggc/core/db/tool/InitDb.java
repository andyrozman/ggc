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

package ggc.core.db.tool;

import ggc.core.db.GGCDb;
import ggc.core.db.datalayer.FoodDescription;
import ggc.core.db.datalayer.FoodGroup;
import ggc.core.db.datalayer.NutritionDefinition;
import ggc.core.db.datalayer.NutritionHomeWeightType;
import ggc.core.db.hibernate.ColorSchemeH;
import ggc.core.db.hibernate.DoctorTypeH;
import ggc.core.util.DataAccess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Hashtable;
import java.util.StringTokenizer;


public class InitDb 
{

    String path = "../data/nutrition/";
    boolean load_nutrition = true;

    GGCDb m_db = null;

    Hashtable<String,NutritionHomeWeightType> home_weight_type_list = null;
    DataAccess m_da = DataAccess.getInstance();



    public InitDb()
    {
        this(true);
    }


    public InitDb(boolean load_nutr)
    {
    	m_db = new GGCDb();
    	m_db.initDb();
    	m_db.createDatabase();
    	this.load_nutrition = load_nutr;

        loadSettings();

    	if (load_nutrition)
    	    loadNutritionDatabase();
    
//    	loadMeters();
    	System.out.println();
    }


    public void setLoadNutrition(boolean load_nutr)
    {
	this.load_nutrition = load_nutr;
    }


    public void loadSettings()
    {

	System.out.println("\n --- Loading Settings --- ");


	System.out.println(" Missing settings...");

        /*SettingsMainH seti = new SettingsMainH(I18nControl.getInstance().getMessage("UNNAMED_USER"), 
                       "Insulin 1", "Ins1", "Insulin 2", "Ins2", 0, "No port available", 
                       2, 60.0f, 200.0f, 80.0f, 120.0f, 
                       3.0f, 20.0f, 4.4f, 14.0f,
                       2, "blueMetalthemepack.zip",
                       0, 0, 0, 0, 0, 0, 0, 
                       "", 1100, 1800, 2100, "0", "Default Scheme");
        seti.setId(1);

        m_db.addHibernate(seti); */

        ColorSchemeH colors = new ColorSchemeH(
        "Default Scheme", 0, -65485, -6750208, -163654, -81409, -1184275, -16724788, 
        -6710785, -16776961, -6711040, -16724941);
        colors.setId(1);

        m_db.addHibernate(colors);

	System.out.println();
    }

/*
    public void loadMeters()
    {
	System.out.println("\n --- Loading Meter Data --- ");

	this.insertMeterCompanies();
	this.insertMeters();
	this.insertMeterInterfaces();
	this.insertMeterImplementations();

	System.out.println();
    }
  */  




    public boolean checkIfNutritionDbFilesExist()
    {

	String files[] = { "FD_GROUP.txt" , "FOOD_DES.txt", "NUT_DATA.txt", "NUTR_DEF.txt", "WEIGHT.txt"
	};

	for (int i=0; i<files.length; i++)
	{
	    File f = new File(path + files[i]);
	    if (!f.exists())
		return false;
	}

	return true;
    }


    // load nutrition database
    public void loadNutritionDatabase()
    {

	if (!checkIfNutritionDbFilesExist())
	{
	    System.out.println("Something seems to be wrong with Nutrition Database files. Download USDBA \n"+
			       "Nutrition database v18 (or greater) and put files into ../data/nutrition/ \n"+
			       "Make sure that at least following files are present: \n"+
			       "FD_GROUP.txt ,FOOD_DES.txt, NUT_DATA.txt, NUTR_DEF.txt, WEIGHT.txt \n\n"+
			       "After all files are present try to init database again.");
	    return;
	}

	System.out.println("\n --- Loading Nutrition Data --- ");
        this.insertFoodGroups();
        this.insertFoodDescription();
        this.insertNutritionData(); 
	this.insertHomeWeightTypes(); 
        this.insertHomeWeightData(); 
	this.insertNutritionDefintions();
	System.out.println();
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

		FoodGroup fg = new FoodGroup(1);
		fg.setId(getInt(strtok.nextToken()));

		String st = getString(strtok.nextToken()); // name
		
		fg.setName(st);
		fg.setName_i18n(this.makeI18nKeyword(st));
		fg.setDescription(st);

		m_db.add(fg);
		System.out.print(".");
	    }

	} 
	catch (Exception ex) 
	{
	    System.err.println("Error on insertFoodGroups(): " + ex);
	    ex.printStackTrace();
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

		line = m_da.parseExpressionFull(line, "^^", "^0.0^");

		if (line.charAt(line.length()-1)=='^') 
		    line = line+"0.0";

		StringTokenizer strtok = new StringTokenizer(line, "^");
		FoodDescription fd = new FoodDescription(1);

		fd.setId(getLong(strtok.nextToken()));		 // NDB_No	
		fd.setGroup_id(getInt(strtok.nextToken()));    // FdGrp_Cd
		fd.setName(getString(strtok.nextToken()));  // Long Desc
		fd.setName_i18n(getString(strtok.nextToken()));  // Short Desc

		strtok.nextToken();                              // - ComName
		strtok.nextToken();                              // - ManufName
		strtok.nextToken();                              // - Survey
		strtok.nextToken();                              // - Ref Desc

		fd.setRefuse(getFloat(strtok.nextToken()));    // Refuse

		m_db.add(fd);
		//m_db.addHibernate(fd.getHibernateObject());

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

	    StringBuffer data = new StringBuffer();

	    while ((line=br.readLine())!=null) 
	    {

		line = m_da.parseExpressionFull(line, "^^", "^0.0^");

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
			fd.setNutritions(data.toString());
			m_db.edit(fd);
		    }

		    id = id_2;
		    fd = new FoodDescription(1);
		    fd.setId(id);
		    m_db.get(fd);
		    data.delete(0, data.length());

		    if (i%100==0)
			System.out.print(".");

		    i++;
		}

		if (data.length()!=0)
		{
		    data.append(";");
		}

		data.append(getString(strtok.nextToken()));
		data.append("=");
		data.append(strtok.nextToken());


//		int type = getInt(strtok.nextToken());
//		float value = getFloat(strtok.nextToken());

		/*
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
		} */
	    }

	    fd.setNutritions(data.toString());
	    m_db.edit(fd);

	} 
	catch (Exception ex) 
	{
	    System.err.println("Error on insertNutritionData(): " + ex);
	    ex.printStackTrace();
	}

    }



    public void insertNutritionDefintions()
    {

        try 
        {

            System.out.println("\nInsert Nutrition Definitons (NUTR_DEF.txt) (10/dot)");

	    int i=0;

            BufferedReader br = new BufferedReader(new FileReader(new File(path+"NUTR_DEF.txt")));
            String line = null;
            
            while ((line=br.readLine())!=null) 
            {
                line = m_da.parseExpressionFull(line, "^^", "^0.0^");

                if (line.charAt(line.length()-1)=='^') 
                    line = line+"0.0";


                StringTokenizer strtok = new StringTokenizer(line, "^");

		NutritionDefinition fnd = new NutritionDefinition();

		fnd.setId(getLong(strtok.nextToken()));
		fnd.setWeight_unit(getString(strtok.nextToken()));
		fnd.setTag(getString(strtok.nextToken()));
		fnd.setName(getString(strtok.nextToken()));
		fnd.setDecimal_places(getString(strtok.nextToken()));

		m_db.add(fnd);

		if (i%10==0)
		    System.out.print(".");

		i++;
            }
        } 
        catch (Exception ex) 
        {
            System.err.println("Error on insertNutritionDefintions(): " + ex);
            ex.printStackTrace();
        }


    }


    public void insertHomeWeightTypes()
    {

	//System.out.println("FIX!!!!!");

	Hashtable<String,NutritionHomeWeightType> list = new Hashtable<String,NutritionHomeWeightType>();

	try 
	{

	    System.out.println("\nInsert Home Weight Types (WEIGHT.txt) (200/dot)");

	    int i=0;

	    BufferedReader br = new BufferedReader(new FileReader(new File(path+"WEIGHT.txt")));
	    String line = null;

	    while ((line=br.readLine())!=null) 
	    {
		line = m_da.parseExpressionFull(line, "^^", "^0.0^");

		if (line.charAt(line.length()-1)=='^') 
		    line = line+"0.0";

		StringTokenizer strtok = new StringTokenizer(line, "^");

		//FoodHomeWeight fhw = new FoodHomeWeight();

		strtok.nextToken(); // food number
		strtok.nextToken(); // sequence
		strtok.nextToken(); // amount
		//fhw.setFood_number(getLong(strtok.nextToken()));
		//fhw.setSequence(getInt(strtok.nextToken()));
		//fhw.setAmount(getFloat(strtok.nextToken()));

		String name = strtok.nextToken();
		String full_name = getString(name);
		full_name = full_name.toUpperCase().trim().replaceAll(" ", "_");

		if (list.containsKey(name))
		    continue;
		
		NutritionHomeWeightType fhwt = new NutritionHomeWeightType();
		fhwt.setName(full_name);

		m_db.add(fhwt);
		list.put(name, fhwt);

		if (i%200==0)
		    System.out.print(".");

		i++;
	    }
	} 
	catch (Exception ex) 
	{
	    System.err.println("Error on insertHomeWeightType(): " + ex);
	    ex.printStackTrace();
	}

	home_weight_type_list = list;

    }



    public void insertHomeWeightData()
    {

	int i=0;

	try 
	{

	    System.out.println("\nInsert Home Weight data into FoodDescriptions (WEIGHT.txt) (100/dot)");

	    BufferedReader br = new BufferedReader(new FileReader(new File(path+"WEIGHT.txt")));
	    String line = null;

	    long id = -1;
	    FoodDescription fd = null;

	    StringBuffer data = new StringBuffer();

	    while ((line=br.readLine())!=null) 
	    {

		line = m_da.parseExpressionFull(line, "^^", "^0.0^");

		if (line.charAt(line.length()-1)=='^') 
		    line = line+"0.0";


		StringTokenizer strtok = new StringTokenizer(line, "^");
		long id_2 = getLong(strtok.nextToken());

		if (id_2!=id)
		{
		    if (id!=-1)
		    {
			fd.setHome_weights(data.toString());
			m_db.edit(fd);
		    }

		    id = id_2;
		    fd = new FoodDescription(1);
		    fd.setId(id);
		    m_db.get(fd);
		    data.delete(0, data.length());

		    if (i%100==0)
			System.out.print(".");

		    i++;
		}

		if (data.length()!=0)
		{
		    data.append(";");
		}


		strtok.nextToken();  // sequence
		String amount = strtok.nextToken();
		String desc = strtok.nextToken();
		String weight = strtok.nextToken();

		long id_hw = this.home_weight_type_list.get(desc).getId();
		//System.out.println(this.home_weight_type_list.get(desc).getName());


		data.append(id_hw);
		data.append("=");


		if (!amount.equals("1"))
		{
		    data.append(amount);
		    data.append("*");
		}

		data.append(weight);

	    }

	    fd.setHome_weights(data.toString());
	    m_db.edit(fd);

	} 
	catch (Exception ex) 
	{
	    System.err.println("Error on insertNutritionData(): " + ex);
	    ex.printStackTrace();
	}

    }


/*
    public void insertMeterCompanies()
    {

	System.out.println("\nLoading Meter Companies (1/dot)");

	// id, name, aka_name, www, description

	String data[] = { 
	    "1", "Dummy Meters", null, null, null,
	    "2", "Unknown Meter Company", null, null, null,
	    "3", "Abbot Diabetes Care", null, "http://www.abbottdiabetescare.com/", null,
	    "4", "Bayer Diagnostics", "Ascensia", "http://www.bayercarediabetes.com/", null,
	    "5", "BD Diabetes", null, "http://www.bddiabetes.com/", null,
	    "6", "Arkray USA", "Hypoguard", "http://www.arkrayusa.com/", null,
	    "7", "Lifescan", null, "http://www.lifescan.com/", null,
	    "8", "Prodigy", null, "http://www.prodigymeter.com/", null,
	    "9", "Roche Diagnostics", "Accu-Chek", "http://www.accu-chek.com/", null,
	};

	for(int i=0; i<data.length; i+=5)
	{
	    MeterCompanyH mc = new MeterCompanyH(data[i+1], data[i+2], data[i+3], data[i+4]);
	    mc.setId(Long.parseLong(data[i]));

	    m_db.addHibernate(mc);
	    System.out.print(".");
	}

    }
*/

    public void insertDoctorTypes()
    {
	
	System.out.println("\nLoading Doctor Types (4/dot)");

	// id, name, predefined
	
	String data[] = { 
	    "1", "ADDICTION_MEDICINE", "1",            
	    "2", "ADOLESCENT_MEDICINE", "1",          
	    "3", "AIDS_HIV_CARE=AIDS/HIV Care", "1",                
	    "4", "ANESTHESIOLOGY", "1",          
	    "5", "ASTHMA_ALLERGY_IMMUNOLOGY", "1", 
	    "6", "BREAST_CANCER_SURGERY", "1",
	    "7", "CARDIAC_SURGERY", "1",
	    "8", "CHILD_CARE", "1",
	    "9", "CARDIOLOGY", "1",
	    "10", "COLORECTAL_SURGERY", "1",   
	    "11", "COSMETIC_SURGERY", "1",
	    "12", "CRITICAL_CARE", "1",                
	    "13", "DENTAL_SPECIALTIES", "1", 
	    "14", "DERMATOLOGY", "1",                   
	    "15", "DIABETES", "1",                     
	    "16", "EMERGENCY_MEDICINE", "1",
	    "17", "ENDOCRINOLOGY", "1",                
	    "18", "EPILEPSY", "1",                     
	    "19", "FAMILY_MEDICINE", "1",              
	    "20", "GASTROENTEROLOGY", "1",                          
	    "21", "GENERAL_SURGERY", "1",
	    "22", "GENERAL_INTERNAL_MEDICINE", "1",
	    "23", "GERIATRICS", "1",                   
	    "24", "GYNECOLOGIC_ONCOLOGY", "1",         
	    "25", "GYNECOLOGY", "1",                   
	    "26", "HAND_SURGERY", "1",                 
	    "27", "HEAD_AND_NECK_SURGERY", "1",        
	    "28", "HEADACHE", "1",                     
	    "29", "HEMATOLOGY", "1",
	    "30", "HOSPITAL_INTERNAL_MEDICINE", "1",
	    "31", "INFECTIOUS_DISEASES", "1",           
	    "32", "INFERTILITY_MEDICINE", "1",                
	    "33", "INTERNAL_MEDICINE", "1",
	    "34", "LABORATORY_MEDICINE", "1",
	    "35", "MIDWIFE", "1",   
	    "36", "MEDICAL_GENETICS", "1",
	    "37", "METABOLISM", "1",
	    "38", "MOVEMENT_DISORDERS", "1",           
	    "39", "NEONATOLOGY", "1",                  
	    "40", "NEPHROLOGY", "1",                   
	    "41", "NEUROLOGY", "1",                    
	    "42", "NEURO_OPTHALMOLOGY", "1",
	    "43", "NEUROSURGERY", "1",                 
	    "44", "NUCLEAR_MEDICINE", "1",
	    "45", "NUTRITION_MEDICINE", "1",
	    "46", "OBSTETRICS_AND_GYNECOLOGY", "1",       
	    "47", "ONCOLOGY_MEDICAL", "1",           
	    "48", "ONCOLOGY_RADIATION", "1",         
	    "49", "OPTHALMOLOGY", "1",                
	    "50", "ORAL_MAXILLOFACIAL_SURGERY", "1",   
	    "51", "ORTHOPEDIC_SURGERY", "1",  
	    "52", "OTOLARYNGOLOGY", "1",              
	    "53", "OTOLOGY", "1",                      
	    "54", "PAIN_MANAGEMENT", "1",              
	    "55", "PATHOLOGY", "1",                   
	    "56", "PEDIATRIC", "1",                   
	    "57", "PEDIATRIC_SPECIALISTS", "1",
	    "58", "PERINATOLOGY", "1",              
	    "59", "PHYSIATRY", "1",                    
	    "60", "PHYSICAL_REHABILITATION", "1",  
	    "61", "PLASTIC_RECONSTRUCTIVE_SURGERY", "1",              
	    "62", "PODIATRY", "1",                     
	    "63", "PROSTATE_CARE", "1",           
	    "64", "PSYCHIATRY", "1",                   
	    "65", "PSYCHOLOGY", "1",                   
	    "66", "PULMONARY_MEDICINE", "1",           
	    "67", "RADIOLOGY", "1",    
	    "68", "RADIATION_ONCOLOGY", "1",
	    "69", "REPRODUCTIVE_ENDOCRINOLOGY_INFERTILITY", "1",
	    "70", "RHEUMATOLOGY", "1",                 
	    "71", "SLEEP_DISOREDERS", "1",                              
	    "72", "SPORTS_MEDICINE", "1",              
	    "73", "THORACIC_SURGERY", "1",  
	    "74", "TRANSPLANT", "1",                   
	    "75", "UROLOGY", "1",
	    "76", "VASCULAR_SURGERY", "1"                     	
	};

	
	int j = 0;
	
	for(int i=0; i<data.length; i+=3)
	{
	    //new DoctorTypeH()
	    DoctorTypeH dth = new DoctorTypeH(data[i+1], 1); 
	    dth.setId(Long.parseLong(data[i]));

	    m_db.addHibernate(dth);
	    
	    j++;
	    
	    if (j%4==0)
		System.out.print(".");
	}
	
    }
    
    
/*    
    public void insertMeters()
    {
	// implementation id=special
	// 0  = Unknown status
	// -1 = No support from company
	// -2 = We got protocol data, but at this time not implemented (implementation in future)
	// -3 =          "          , work is in progress
	// -4 = Implemented, but not tested enough
	// -5 = No reply from company

	System.out.println("\nLoading Meters (1/dot)");
	

	// meter_id, company_id, name, picture, implementation_id, extended
	String data[] = {
	    "250", "1", "Dummy Meter (old interface-Stephan)", null, "250", "MULTISITE=?;SAMPLE_SIZE=?;TEST_TIME=?;MEMORY=?;STRIPS=?",
	    "251", "1", "Dummy Meter (new interface-Andy)", null, "251", "MULTISITE=?;SAMPLE_SIZE=?;TEST_TIME=?;MEMORY=?;STRIPS=?",

	    "1", "3", "Freestyle", null, "3", "MULTISITE=Yes;SAMPLE_SIZE=0.3;TEST_TIME=7;MEMORY=250;STRIPS=Freestyles",
	    "2", "3", "Flash", null, null, "MULTISITE=Yes;SAMPLE_SIZE=0.3;TEST_TIME=7;MEMORY=250;STRIPS=Freestyles",
	    "3", "3", "Freedom", null, null, "MULTISITE=Yes;SAMPLE_SIZE=0.3;TEST_TIME=5;MEMORY=250;STRIPS=Freestyles",
	    "4", "3", "Precision Xtra", null, null, "MULTISITE=Yes;SAMPLE_SIZE=0.6;TEST_TIME=5;MEMORY=450;STRIPS=Precision Xtra",

	    "5", "4", "Ascensia Breeze", null, "6", "MULTISITE=Yes;SAMPLE_SIZE=3.0;TEST_TIME=30;MEMORY=100;STRIPS=Autodisk",
	    "6", "4", "Ascensia Contour", null, "4", "MULTISITE=Yes;SAMPLE_SIZE=0.6;TEST_TIME=15;MEMORY=240;STRIPS=Contour",
	    "7", "4", "Ascensia Contour II.", null, "4", "MULTISITE=Yes;SAMPLE_SIZE=0.6(?);TEST_TIME=5;MEMORY=240(?);STRIPS=Contour",
	    "8", "4", "Ascensia Elite", null, "7", "MULTISITE=Yes;SAMPLE_SIZE=3.0;TEST_TIME=30;MEMORY=120;STRIPS=Elite",
	    "9", "4", "Ascensia Elite XL", null, "7", "MULTISITE=Yes;SAMPLE_SIZE=3.0;TEST_TIME=30;MEMORY=120;STRIPS=Elite",
	    "10", "4", "Ascensia Dex", null, "5", "MULTISITE=Yes(?);SAMPLE_SIZE=3.0(?);TEST_TIME=30(?);MEMORY=120(?);STRIPS=DEX Autodisk",

	    "11", "5", "Logic", null, null, "MULTISITE=No;SAMPLE_SIZE=0.3;TEST_TIME=5;MEMORY=250;STRIPS=BD Test Strips",
	    "12", "5", "The Link", null, null, "MULTISITE=No;SAMPLE_SIZE=0.3;TEST_TIME=5;MEMORY=250;STRIPS=BD Test Strips",

	    "13", "6", "Advance Microdraw", null, "-1", "MULTISITE=No;SAMPLE_SIZE=1.5;TEST_TIME=15;MEMORY=250;STRIPS=Advance Test Strips",
	    "14", "6", "Advance Intuition", null, "-1", "MULTISITE=No;SAMPLE_SIZE=3.0;TEST_TIME=10;MEMORY=10;STRIPS=Advance Intuition",
	    "15", "6", "Assure II", null, "-1", "MULTISITE=--;SAMPLE_SIZE=3.0;TEST_TIME=30;MEMORY=--;STRIPS=Assure II Test Strips",
	    "16", "6", "Assure 3", null, "-1", "MULTISITE=--;SAMPLE_SIZE=3.0;TEST_TIME=10;MEMORY=--;STRIPS=Assure 3 Test Strips",
	    "17", "6", "Assure Pro", null, "-1", "MULTISITE=--;SAMPLE_SIZE=1;TEST_TIME=10;MEMORY=250;STRIPS=Assure Pro Test Strips",
	    "18", "6", "Quicktek", null, "-1", "MULTISITE=No;SAMPLE_SIZE=3.5;TEST_TIME=15;MEMORY=250;STRIPS=Quicktek Test Strips",
	    "19", "6", "Supreme Plus", null, "-1", "MULTISITE=No;SAMPLE_SIZE=?;TEST_TIME=30-60(?);MEMORY=70;STRIPS=Supreme Plus Test Strips",

	    "20", "7", "OneTouch Basic", null, "-1", "MULTISITE=No;SAMPLE_SIZE=10.0;TEST_TIME=45;MEMORY=75;STRIPS=Basics",
	    "21", "7", "OneTouch FastTake", null, "-1", "MULTISITE=No;SAMPLE_SIZE=1.5;TEST_TIME=15;MEMORY=150;STRIPS=FastTakes",
	    "22", "7", "OneTouch SureStep", null, "-1", "MULTISITE=No;SAMPLE_SIZE=10;TEST_TIME=15;MEMORY=150;STRIPS=SureSteps",
	    "23", "7", "OneTouch Ultra", null, "-1", "MULTISITE=Yes;SAMPLE_SIZE=1.0;TEST_TIME=5;MEMORY=150;STRIPS=Ultras",
	    "24", "7", "OneTouch UltraMini", null, "-1", "MULTISITE=Yes;SAMPLE_SIZE=1.0;TEST_TIME=5;MEMORY=50;STRIPS=Ultras",
	    "25", "7", "OneTouch UltraSmart", null, "-1", "MULTISITE=Yes;SAMPLE_SIZE=1.0;TEST_TIME=5;MEMORY=150;STRIPS=Ultras",

	    "26", "8", "Prodigy Advance Meter", null, "-1", "MULTISITE=Yes;SAMPLE_SIZE=0.6;TEST_TIME=6;MEMORY=450;STRIPS=Prodigy Personal Strips",
	    "27", "8", "Prodigy Audio Meter", null, "-1", "MULTISITE=No;SAMPLE_SIZE=0.6;TEST_TIME=6;MEMORY=450;STRIPS=Prodigy Personal Strips",
	    "28", "8", "Prodigy Autocode Meter", null, "-1", "MULTISITE=No;SAMPLE_SIZE=0.6;TEST_TIME=6;MEMORY=450;STRIPS=Autocode Strips",
	    "29", "8", "Prodigy Duo Meter", null, "-1", "MULTISITE=Yes;SAMPLE_SIZE=0.6;TEST_TIME=6;MEMORY=450;STRIPS=Duo Strips",
	    "30", "8", "Prodigy Eject Meter", null, "-1", "MULTISITE=Yes;SAMPLE_SIZE=0.6;TEST_TIME=6;MEMORY=450;STRIPS=Eject Strips",

	    "31", "9", "Accu Check Active", null, "-1", "MULTISITE=Yes;SAMPLE_SIZE=1.0;TEST_TIME=5;MEMORY=200;STRIPS=Active",
	    "32", "9", "Accu Check Advantage", null, "-1", "MULTISITE=No;SAMPLE_SIZE=3.0;TEST_TIME=26;MEMORY=480;STRIPS=Comfort Curve",
	    "33", "9", "Accu Check Aviva", null, "-1", "MULTISITE=Yes;SAMPLE_SIZE=0.6;TEST_TIME=5;MEMORY=500;STRIPS=Aviva",
	    "34", "9", "Accu Check Compact", null, "-1", "MULTISITE=Yes;SAMPLE_SIZE=1.5;TEST_TIME=8;MEMORY=100;STRIPS=Compact Drum",

	    "35", "2", "Gluco Card", null, "1", "MULTISITE=?;SAMPLE_SIZE=?;TEST_TIME=?;MEMORY=?;STRIPS=?",
	    "36", "2", "Euro Flash", null, "2", "MULTISITE=?;SAMPLE_SIZE=?;TEST_TIME=?;MEMORY=?;STRIPS=?",

	};

	// meter_id, company_id, name, picture, implementation_id, extended

	for(int i=0; i<data.length; i+=6)
	{
	    // company_id, name, picture, extended, implementation_id, 

	    long imp_id = 0;

	    if (data[i+4]!=null)
	    {
		imp_id = Long.parseLong(data[i+4]);
	    }

	    MeterH mc = new MeterH(Long.parseLong(data[i+1]), data[i+2], data[i+3], data[i+5], imp_id);
	    mc.setId(Long.parseLong(data[i]));

	    m_db.addHibernate(mc);
	    System.out.print(".");
	}


	    //"MULTISITE=?;SAMPLE_SIZE=?;TEST_TIME=?;MEMORY=?;STRIPS=?"
    }

    public void insertMeterInterfaces()
    {

	System.out.println("\nLoading Meter Interfaces (1/dot)");

	// id, desc, classname
	String data[] = 
	{ 
	    "1", "Meter Interface by Stephan (pre 2003)", "ggc.gui.ReadMeterDialog",
	    "2", "Meter Interface by Andy (2006)", "ggc.gui.dialogs.MeterReadDialog"
	};


	for(int i=0; i<data.length; i+=3)
	{
	    MeterInterfaceH mi = new MeterInterfaceH(data[i+1], data[i+2]);
	    mi.setId(Long.parseLong(data[i]));

	    m_db.addHibernate(mi);
	    System.out.print(".");
	}
    }



    public void insertMeterImplementations()
    {

	System.out.println("\nLoading Meter Implementation (1/dot)");

	// id, desc, classname, interface_id
	String data[] = 
	{ 
	    "250", "Dummy Meter Implementation (Stephan)", "ggc.data.imports.DummyImport", "1", "99",
	    "251", "Dummy Meter Implementation (Andy)", "ggc.data.meter.device.DummyMeter", "1", "99",

	    "1", "Gluco Card Implementation", "ggc.data.imports.GlucoCardImport", "1", "0",
	    "2", "EuroFlash Impl.", "ggc.data.imports.EuroFlashImport", "1", "0",
	    "3", "FreeStyle Impl.", "ggc.data.imports.FreeStyleImport", "1", "0",

	    "4", "Ascensia Couyour", "ggc.data.meter.device.AscensiaContourMeter", "2", "2",
	    "5", "Ascensia DEX", "ggc.data.meter.device.AscensiaDEXMeter", "2", "2",
	    "6", "Ascensia Breeze", "ggc.data.meter.device.AscensiaBreezeMeter", "2", "3",
	    "7", "Ascensia Elite/XL", "ggc.data.meter.device.AscensiaEliteXLMeter", "2", "3",
	};

//<!-- 0 = Unknown/Untested,  1=In testing, 2=In implementation, 3=Planned, 99=Complete  -->

	for(int i=0; i<data.length; i+=5)
	{
	    MeterImplementationH mih = new MeterImplementationH(data[i+1], data[i+2], Long.parseLong(data[i+3]), Integer.parseInt(data[i+4]));
	    mih.setId(Long.parseLong(data[i]));

	    m_db.addHibernate(mih);
	    System.out.print(".");
	}
    }
*/


/*
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
*/





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


    public String makeI18nKeyword(String input)
    {
	String i = input.toUpperCase();
	i = i.replaceAll(" " , "");
	i = i.replaceAll(",", "_");
	
	return i;
	
    }
    


    public static void main(String args[])
    {
	new InitDb();
    }


}
