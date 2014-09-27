/*
 * GGC - GNU Gluco Control
 * 
 * A pure java app to help you manage your diabetes.
 * 
 * See AUTHORS for copyright information.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Filename: NutritionImport Purpose: Imports nutrition data from USDA Nutrient
 * Database for Standard Reference (from Release 18 up)
 * 
 * Author: andyrozman
 */

package ggc.core.db.tool;

import ggc.core.db.GGCDb;
import ggc.core.db.hibernate.ColorSchemeH;
import ggc.core.db.hibernate.DbInfoH;
import ggc.core.db.hibernate.DoctorTypeH;
import ggc.core.db.hibernate.FoodDescriptionH;
import ggc.core.db.hibernate.FoodGroupH;
import ggc.core.db.hibernate.NutritionDefinitionH;
import ggc.core.db.hibernate.NutritionHomeWeightTypeH;
import ggc.core.util.DataAccess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *  Application:   GGC - GNU Gluco Control
 *
 *  See AUTHORS for copyright information.
 * 
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 * 
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 *  Filename:     InitDb  
 *  Description:  Init Db for GGC
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class InitDb
{

    String path = "../data/nutrition/";
    boolean load_nutrition = true;

    GGCDb m_db = null;

    Hashtable<String, NutritionHomeWeightTypeH> home_weight_type_list = null;
    DataAccess m_da = DataAccess.getInstance();

    Hashtable<Long, FoodDescriptionH> lst_food_desc = new Hashtable<Long, FoodDescriptionH>();

    /**
     * Password for Init
     */
    public static String passWord = "I_Really_Want_To_Do_This";

    /**
     * Constructor
     */
    public InitDb()
    {
        this(true);
    }

    /**
     * Constructor
     * 
     * @param load_nutr
     */
    public InitDb(boolean load_nutr)
    {
        long time_start = System.currentTimeMillis();

        m_db = new GGCDb();
        m_db.initDb();
        m_db.createDatabase();
        this.load_nutrition = load_nutr;

        setDbInfo();
        loadSettings();
        insertDoctorTypes();

        if (load_nutrition)
        {
            loadNutritionDatabase();
        }

        // loadMeters();
        m_db.closeDb();
        System.out.println();

        long dif = System.currentTimeMillis() - time_start;

        System.out.println("We needed " + dif / 1000 + " seconds to create and fill database.");

    }

    private void setDbInfo()
    {
        DbInfoH dbi = new DbInfoH();

        dbi.setId(1);
        dbi.setKey("DB_INFO");
        dbi.setValue(m_da.current_db_version);

        m_db.addHibernate(dbi);
    }

    /**
     * Set Load Nutrition
     * 
     * @param load_nutr
     */
    public void setLoadNutrition(boolean load_nutr)
    {
        this.load_nutrition = load_nutr;
    }

    private void loadSettings()
    {

        System.out.println("\n --- Loading Settings --- ");

        System.out.println(" Missing settings...");

        /*
         * SettingsH seti = new
         * SettingsH(m_da.getI18nControlInstance().getMessage("UNNAMED_USER"),
         * "Insulin 1", "Ins1", "Insulin 2", "Ins2", 0, "No port available", 2,
         * 60.0f, 200.0f, 80.0f, 120.0f, 3.0f, 20.0f, 4.4f, 14.0f, 2,
         * "blueMetalthemepack.zip", 0, 0, 0, 0, 0, 0, 0, "", 1100, 1800, 2100,
         * "0", "Default Scheme" );
         * seti.setId(1);
         * m_db.addHibernate(seti);
         */

        ColorSchemeH colors = new ColorSchemeH("Default Scheme", 0, -65485, -6750208, -163654, -81409, -1184275,
                -16724788, -6710785, -16776961, -6711040, -16724941);
        colors.setId(1);

        m_db.addHibernate(colors);

        System.out.println();
    }

    private boolean checkIfNutritionDbFilesExist()
    {

        String files[] = { "FD_GROUP.txt", "FOOD_DES.txt", "NUT_DATA.txt", "NUTR_DEF.txt", "WEIGHT.txt" };

        for (String file : files)
        {
            File f = new File(path + file);
            if (!f.exists())
                return false;
        }

        return true;
    }

    // load nutrition database
    private void loadNutritionDatabase()
    {

        if (!checkIfNutritionDbFilesExist())
        {
            System.out.println("Something seems to be wrong with Nutrition Database files. Download USDBA \n"
                    + "Nutrition database v18 (or greater) and put files into ../data/nutrition/ \n"
                    + "Make sure that at least following files are present: \n"
                    + "FD_GROUP.txt ,FOOD_DES.txt, NUT_DATA.txt, NUTR_DEF.txt, WEIGHT.txt \n\n"
                    + "After all files are present try to init database again.");
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

    private void insertFoodGroups()
    {

        try
        {
            System.out.println("\nLoading Food Groups (FD_GROUP.txt) (1/dot)");

            BufferedReader br = new BufferedReader(new FileReader(new File(path + "FD_GROUP.txt")));
            String line = null;

            while ((line = br.readLine()) != null)
            {
                StringTokenizer strtok = new StringTokenizer(line, "^");

                FoodGroupH fg = new FoodGroupH();
                fg.setId(getInt(strtok.nextToken()));

                String st = getString(strtok.nextToken()); // name

                fg.setName(st);
                fg.setName_i18n(m_da.makeI18nKeyword(st));
                fg.setDescription(st);

                m_db.addHibernate(fg);
                System.out.print(".");
            }

        }
        catch (Exception ex)
        {
            System.err.println("Error on insertFoodGroups(): " + ex);
            ex.printStackTrace();
        }

    }

    private void insertFoodDescription()
    {

        // 204 = Fat(Lipid) (g)
        // 205 = Carbohydrate by difference (g)
        // 208 = Energy (kcal)
        // 268 = Energy (kJ)
        // 269 = Sugar (total) (g)

        int i = 0;

        try
        {

            System.out.println("\nInsert Food Description (food_des.txt) (100/dot)");

            BufferedReader br = new BufferedReader(new FileReader(new File(path + "FOOD_DES.txt")));
            String line = null;

            while ((line = br.readLine()) != null)
            {

                line = m_da.parseExpressionFull(line, "^^", "^0.0^");

                if (line.charAt(line.length() - 1) == '^')
                {
                    line = line + "0.0";
                }

                StringTokenizer strtok = new StringTokenizer(line, "^");
                FoodDescriptionH fd = new FoodDescriptionH();

                fd.setId(getLong(strtok.nextToken())); // NDB_No
                fd.setGroup_id(getInt(strtok.nextToken())); // FdGrp_Cd
                fd.setName(getString(strtok.nextToken())); // Long Desc
                fd.setName_i18n(getString(strtok.nextToken())); // Short Desc

                strtok.nextToken(); // - ComName
                strtok.nextToken(); // - ManufName
                strtok.nextToken(); // - Survey
                strtok.nextToken(); // - Ref Desc

                fd.setRefuse(getFloat(strtok.nextToken())); // Refuse

                m_db.addHibernate(fd);

                this.lst_food_desc.put(fd.getId(), fd);

                // m_db.addHibernate(fd.getHibernateObject());

                if (i % 100 == 0)
                {
                    System.out.print(".");
                }

                i++;

            }

        }
        catch (Exception ex)
        {
            System.err.println("Error on insertFoodDescription(): " + ex);
            ex.printStackTrace();
        }

    }

    private void insertNutritionData()
    {

        int i = 0;

        try
        {

            System.out.println("\nInsert Nutrition Data into food_des (nut_data.txt) (100/dot)");

            BufferedReader br = new BufferedReader(new FileReader(new File(path + "NUT_DATA.txt")));
            String line = null;

            long id = -1;
            FoodDescriptionH fd = null;

            StringBuffer data = new StringBuffer();

            while ((line = br.readLine()) != null)
            {

                line = m_da.parseExpressionFull(line, "^^", "^0.0^");

                if (line.charAt(line.length() - 1) == '^')
                {
                    line = line + "0.0";
                }

                // 204 = Fat(Lipid)
                // 205 = Carbohydrate by difference
                // 208 = Energy (kcal)
                // 268 = Energy (kJ)
                // 269 = Sugar (total)

                StringTokenizer strtok = new StringTokenizer(line, "^");
                long id_2 = getLong(strtok.nextToken());

                if (id_2 != id)
                {
                    if (id != -1)
                    {
                        fd.setNutritions(data.toString());
                        m_db.editHibernate(fd);
                    }

                    id = id_2;
                    fd = this.getFoodDescription(id);
                    // fd.setId(id);
                    // m_db.get(fd);
                    data.delete(0, data.length());

                    if (i % 100 == 0)
                    {
                        System.out.print(".");
                    }

                    i++;
                }

                if (data.length() != 0)
                {
                    data.append(";");
                }

                data.append(getString(strtok.nextToken()));
                data.append("=");
                data.append(strtok.nextToken());

                // int type = getInt(strtok.nextToken());
                // float value = getFloat(strtok.nextToken());

                /*
                 * if (type==204) { // 204 = Fat(Lipid) fd.setFat_g(value); }
                 * else if (type==205) { // 205 = Carbohydrate by difference
                 * fd.setCH_g(value); } else if (type==208) { // 208 = Energy
                 * (kcal) fd.setEnergy_kcal(value); } else if (type==268) { //
                 * 268 = Energy (kJ) fd.setEnergy_kJ(value); } else if
                 * (type==269) { // 269 = Sugar (total) fd.setSugar_g(value); }
                 */
            }

            fd.setNutritions(data.toString());
            m_db.editHibernate(fd);

        }
        catch (Exception ex)
        {
            System.err.println("Error on insertNutritionData(): " + ex);
            ex.printStackTrace();
        }

    }

    /**
     * Get Food Description
     * 
     * @param _id
     * @return
     */
    public FoodDescriptionH getFoodDescription(long _id)
    {
        return this.lst_food_desc.get(_id);
    }

    private void insertNutritionDefintions()
    {

        try
        {

            System.out.println("\nInsert Nutrition Definitons (NUTR_DEF.txt) (10/dot)");

            int i = 0;

            BufferedReader br = new BufferedReader(new FileReader(new File(path + "NUTR_DEF.txt")));
            String line = null;

            while ((line = br.readLine()) != null)
            {
                line = m_da.parseExpressionFull(line, "^^", "^0.0^");

                if (line.charAt(line.length() - 1) == '^')
                {
                    line = line + "0.0";
                }

                StringTokenizer strtok = new StringTokenizer(line, "^");

                NutritionDefinitionH fnd = new NutritionDefinitionH();

                fnd.setId(getLong(strtok.nextToken()));
                fnd.setWeight_unit(getString(strtok.nextToken()));
                fnd.setTag(getString(strtok.nextToken()));
                fnd.setName(getString(strtok.nextToken()));
                fnd.setDecimal_places(getString(strtok.nextToken()));

                m_db.addHibernate(fnd);

                if (i % 10 == 0)
                {
                    System.out.print(".");
                }

                i++;
            }
        }
        catch (Exception ex)
        {
            System.err.println("Error on insertNutritionDefintions(): " + ex);
            ex.printStackTrace();
        }

    }

    private void insertHomeWeightTypes()
    {

        // System.out.println("FIX!!!!!");

        Hashtable<String, NutritionHomeWeightTypeH> list = new Hashtable<String, NutritionHomeWeightTypeH>();

        try
        {

            System.out.println("\nInsert Home Weight Types (WEIGHT.txt) (200/dot)");

            int i = 0;

            BufferedReader br = new BufferedReader(new FileReader(new File(path + "WEIGHT.txt")));
            String line = null;

            while ((line = br.readLine()) != null)
            {
                line = m_da.parseExpressionFull(line, "^^", "^0.0^");

                if (line.charAt(line.length() - 1) == '^')
                {
                    line = line + "0.0";
                }

                StringTokenizer strtok = new StringTokenizer(line, "^");

                // FoodHomeWeight fhw = new FoodHomeWeight();

                strtok.nextToken(); // food number
                strtok.nextToken(); // sequence
                strtok.nextToken(); // amount
                // fhw.setFood_number(getLong(strtok.nextToken()));
                // fhw.setSequence(getInt(strtok.nextToken()));
                // fhw.setAmount(getFloat(strtok.nextToken()));

                String name = strtok.nextToken();
                String full_name = getString(name);
                full_name = full_name.toUpperCase().trim().replaceAll(" ", "_");

                if (list.containsKey(name))
                {
                    continue;
                }

                NutritionHomeWeightTypeH fhwt = new NutritionHomeWeightTypeH();
                fhwt.setName(full_name);

                m_db.addHibernate(fhwt);
                list.put(name, fhwt);

                if (i % 200 == 0)
                {
                    System.out.print(".");
                }

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

    private void insertHomeWeightData()
    {

        int i = 0;

        try
        {

            System.out.println("\nInsert Home Weight data into FoodDescriptions (WEIGHT.txt) (100/dot)");

            BufferedReader br = new BufferedReader(new FileReader(new File(path + "WEIGHT.txt")));
            String line = null;

            long id = -1;
            FoodDescriptionH fd = null;

            StringBuffer data = new StringBuffer();

            while ((line = br.readLine()) != null)
            {

                line = m_da.parseExpressionFull(line, "^^", "^0.0^");

                if (line.charAt(line.length() - 1) == '^')
                {
                    line = line + "0.0";
                }

                StringTokenizer strtok = new StringTokenizer(line, "^");
                long id_2 = getLong(strtok.nextToken());

                if (id_2 != id)
                {
                    if (id != -1)
                    {
                        fd.setHome_weights(data.toString());
                        m_db.editHibernate(fd);
                    }

                    id = id_2;
                    fd = this.getFoodDescription(id);
                    // fd.setId(id);
                    // m_db.get(fd);
                    data.delete(0, data.length());

                    if (i % 100 == 0)
                    {
                        System.out.print(".");
                    }

                    i++;
                }

                if (data.length() != 0)
                {
                    data.append(";");
                }

                strtok.nextToken(); // sequence
                String amount = strtok.nextToken();
                String desc = strtok.nextToken();
                String weight = strtok.nextToken();

                long id_hw = this.home_weight_type_list.get(desc).getId();
                // System.out.println(this.home_weight_type_list.get(desc).getName
                // ());

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
            m_db.editHibernate(fd);

        }
        catch (Exception ex)
        {
            System.err.println("Error on insertNutritionData(): " + ex);
            ex.printStackTrace();
        }

    }

    private void insertDoctorTypes()
    {

        System.out.println("\nLoading Doctor Types (4/dot)");

        // id, name, predefined

        String data[] = { "1", "ADDICTION_MEDICINE", "1", "2", "ADOLESCENT_MEDICINE", "1", "3",
                         "AIDS_HIV_CARE=AIDS/HIV Care", "1", "4", "ANESTHESIOLOGY", "1", "5",
                         "ASTHMA_ALLERGY_IMMUNOLOGY", "1", "6", "BREAST_CANCER_SURGERY", "1", "7", "CARDIAC_SURGERY",
                         "1", "8", "CHILD_CARE", "1", "9", "CARDIOLOGY", "1", "10", "COLORECTAL_SURGERY", "1", "11",
                         "COSMETIC_SURGERY", "1", "12", "CRITICAL_CARE", "1", "13", "DENTAL_SPECIALTIES", "1", "14",
                         "DERMATOLOGY", "1", "15", "DIABETES", "1", "16", "EMERGENCY_MEDICINE", "1", "17",
                         "ENDOCRINOLOGY", "1", "18", "EPILEPSY", "1", "19", "FAMILY_MEDICINE", "1", "20",
                         "GASTROENTEROLOGY", "1", "21", "GENERAL_SURGERY", "1", "22", "GENERAL_INTERNAL_MEDICINE", "1",
                         "23", "GERIATRICS", "1", "24", "GYNECOLOGIC_ONCOLOGY", "1", "25", "GYNECOLOGY", "1", "26",
                         "HAND_SURGERY", "1", "27", "HEAD_AND_NECK_SURGERY", "1", "28", "HEADACHE", "1", "29",
                         "HEMATOLOGY", "1", "30", "HOSPITAL_INTERNAL_MEDICINE", "1", "31", "INFECTIOUS_DISEASES", "1",
                         "32", "INFERTILITY_MEDICINE", "1", "33", "INTERNAL_MEDICINE", "1", "34",
                         "LABORATORY_MEDICINE", "1", "35", "MIDWIFE", "1", "36", "MEDICAL_GENETICS", "1", "37",
                         "METABOLISM", "1", "38", "MOVEMENT_DISORDERS", "1", "39", "NEONATOLOGY", "1", "40",
                         "NEPHROLOGY", "1", "41", "NEUROLOGY", "1", "42", "NEURO_OPTHALMOLOGY", "1", "43",
                         "NEUROSURGERY", "1", "44", "NUCLEAR_MEDICINE", "1", "45", "NUTRITION_MEDICINE", "1", "46",
                         "OBSTETRICS_AND_GYNECOLOGY", "1", "47", "ONCOLOGY_MEDICAL", "1", "48", "ONCOLOGY_RADIATION",
                         "1", "49", "OPTHALMOLOGY", "1", "50", "ORAL_MAXILLOFACIAL_SURGERY", "1", "51",
                         "ORTHOPEDIC_SURGERY", "1", "52", "OTOLARYNGOLOGY", "1", "53", "OTOLOGY", "1", "54",
                         "PAIN_MANAGEMENT", "1", "55", "PATHOLOGY", "1", "56", "PEDIATRIC", "1", "57",
                         "PEDIATRIC_SPECIALISTS", "1", "58", "PERINATOLOGY", "1", "59", "PHYSIATRY", "1", "60",
                         "PHYSICAL_REHABILITATION", "1", "61", "PLASTIC_RECONSTRUCTIVE_SURGERY", "1", "62", "PODIATRY",
                         "1", "63", "PROSTATE_CARE", "1", "64", "PSYCHIATRY", "1", "65", "PSYCHOLOGY", "1", "66",
                         "PULMONARY_MEDICINE", "1", "67", "RADIOLOGY", "1", "68", "RADIATION_ONCOLOGY", "1", "69",
                         "REPRODUCTIVE_ENDOCRINOLOGY_INFERTILITY", "1", "70", "RHEUMATOLOGY", "1", "71",
                         "SLEEP_DISOREDERS", "1", "72", "SPORTS_MEDICINE", "1", "73", "THORACIC_SURGERY", "1", "74",
                         "TRANSPLANT", "1", "75", "UROLOGY", "1", "76", "VASCULAR_SURGERY", "1" };

        int j = 0;

        for (int i = 0; i < data.length; i += 3)
        {
            // new DoctorTypeH()
            DoctorTypeH dth = new DoctorTypeH(data[i + 1], 1);
            dth.setId(Long.parseLong(data[i]));

            m_db.addHibernate(dth);

            j++;

            if (j % 4 == 0)
            {
                System.out.print(".");
            }
        }

    }

    private int getInt(String input)
    {

        if (input.startsWith("~"))
        {
            input = input.substring(1, input.length() - 1);
        }

        if (input.length() == 0)
            return 0;
        else
            return Integer.parseInt(input);

    }

    /*
     * private short getShort(String input)
     * {
     * if (input.startsWith("~"))
     * input = input.substring(1, input.length() - 1);
     * if (input.length() == 0)
     * return 0;
     * else
     * return Short.parseShort(input);
     * }
     */

    private long getLong(String input)
    {
        if (input.startsWith("~"))
        {
            input = input.substring(1, input.length() - 1);
        }

        if (input.length() == 0)
            return 0;
        else
            return Long.parseLong(input);
    }

    private float getFloat(String input)
    {
        if (input.startsWith("~"))
        {
            input = input.substring(1, input.length() - 1);
        }

        if (input.length() == 0)
            return 0;
        else
            return Float.parseFloat(input);
    }

    /**
     * Get String
     * 
     * @param input
     * @return
     */
    public String getString(String input)
    {
        if (input.startsWith("~"))
        {
            input = input.substring(1, input.length() - 1);
        }

        return input;
    }

    /*
     * public String makeI18nKeyword(String input)
     * {
     * String i = input.toUpperCase();
     * i = i.replaceAll(" ", "");
     * i = i.replaceAll(",", "_");
     * return i;
     * }
     */

    /**
     * Usage
     */
    public static void usage()
    {
        System.out.println("InitDb is very dangerous application. It will totally DESTROY your database.\n"
                + "Please make a backup of your database and it's data, before you attempt to do\n"
                + " this. If you are sure you will need password to start this process. \n"
                + "This message is displayed because you didn'y supply password on command line.\n"
                + "Password is (without spaces): " + InitDb.passWord + "\n"
                + "now you can either type the password in after prompt, or you can start\n"
                + "again with right password as parameter. Press CTRL-C to abort.");
    }

    /**
     * Read Password
     * 
     * @return
     */
    public static String readPassWord()
    {
        // System.out.println("Fix this: ");
        String pass;

        Scanner in = new Scanner(System.in);

        // Reads a single line from the console
        // and stores into name variable
        pass = in.nextLine();

        in.close();

        return pass;
    }

    /**
     * Startup Method
     * 
     * @param args
     */
    public static void main(String args[])
    {
        // I_Really_Want_To_Do_This
        if (args.length == 1)
        {
            if (args[0].equals(InitDb.passWord))
            {
                new InitDb();
                return;
            }
        }

        InitDb.usage();

        if (InitDb.passWord.equals(InitDb.readPassWord()))
        {
            new InitDb();
            return;
        }

        System.out.println("Since you failed to provide right password, operation is canceled.");

    }

}
