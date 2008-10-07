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
 * Filename: ImportDailyValues Purpose: Imports daily values (from export from
 * Meter Tool Import, or some export)
 * 
 * Author: andyrozman {andy@atech-software.com}
 */

package ggc.core.test;

import ggc.core.db.hibernate.FoodUserDescriptionH;
import ggc.core.util.DataAccess;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.atech.db.hibernate.transfer.ImportTool;

public class ImportDacioDb extends ImportTool
{

    // GGCDb m_db = null;
//    public String file_name;
    private static Log log = LogFactory.getLog(ImportDacioDb.class);

    DataAccess m_da = DataAccess.getInstance();
    

    public ImportDacioDb(String file_name)
    {
        super(DataAccess.getInstance().getDb().getHibernateConfiguration());

//        super(new GGCDbConfig(true));
        this.restore_file = new File(file_name);
    }

    public ImportDacioDb(String file_name, boolean ff)
    {
        super(DataAccess.getInstance().getDb().getHibernateConfiguration());
        this.restore_file = new File(file_name);
    }
    
    
    @Override
    public int getActiveSession()
    {
        return 2;
    }
    
    
    public void convertFoods()
    {

        String line = null;

        try
        {
            

            
            System.out.println("\nLoading Settings (5/dot)");

            this.openFileForReading(this.restore_file);
            
//            this.openFile("dacio_export.txt");

//            int dot_mark = 5;
            int count = 0;
            
            count = 433;

            
            Hashtable<String,String> grps_co = new Hashtable<String,String>();
            
            grps_co.put("1", "36");
            grps_co.put("2", "37");
            grps_co.put("3", "38");
            grps_co.put("4", "39");
            grps_co.put("5", "40");
            grps_co.put("6", "41");
            grps_co.put("7", "42");
            grps_co.put("8", "43");
            grps_co.put("9", "44");
            grps_co.put("10", "45");
            grps_co.put("11", "46");
            grps_co.put("12", "47");
            grps_co.put("13", "48");
            grps_co.put("14", "49");
            grps_co.put("15", "50");
            grps_co.put("16", "51");
            grps_co.put("17", "52");
            
            boolean first = true;
            
            Session sess = getSession();
            
            while ((line = this.br_file.readLine()) != null)
            {
                if (line.startsWith(";"))
                    continue;

                if (first)
                {
                    first = false;
                    continue;
                }
                
                // line = line.replaceAll("||", "| |");
                line = m_da.replaceExpression(line, "||", "| |");

                //System.out.println(line);

                 StringTokenizer strtok = new StringTokenizer(line, ";");

                // "id";"zivilo";"id_kat";"kJ";"voda";"B";"M";"OH";
                // "balastne_snovi"
                // ;"pepel";"kalcij";"fosfor";"zelezo";"natrij";"kalij"
                // ;"A_IE";"B1";"B2";"nikotinska_kislina";"C"

                strtok.nextToken(); // id
                String name = getElementString(strtok.nextToken()); // name
//x                String group = getElementString(strtok.nextToken());     // group

                //StringBuffer sb = new StringBuffer();
                ArrayList<DacioDbData> lst = new ArrayList<DacioDbData>();     

                String kj = getElementString(strtok.nextToken());
                
                
                
                addParameters(lst, 268, kj); // "kJ";
                addParameters(lst, 208, getKcal(kj)); // "kcal";
                
                addParameters(lst, 255, getElementString(strtok.nextToken())); // "voda";
                
                addParameters(lst, 203, getElementString(strtok.nextToken())); // "B";
                addParameters(lst, 204, getElementString(strtok.nextToken())); // "M";
                addParameters(lst, 205, getElementString(strtok.nextToken())); // "OH";
                addParameters(lst, 291, getElementString(strtok.nextToken())); // "balastne_snovi";

                
                addParameters(lst, 207, getElementString(strtok.nextToken())); // "pepel";
                addParameters(lst, 301, getElementString(strtok.nextToken())); // "kalcij";
                addParameters(lst, 305, getElementString(strtok.nextToken())); // "fosfor";
                
                addParameters(lst, 303, getElementString(strtok.nextToken())); // "zelezo";
                addParameters(lst, 307, getElementString(strtok.nextToken())); // "natrij";
                addParameters(lst, 306, getElementString(strtok.nextToken())); // "kalij";

                addParameters(lst, 318, getElementString(strtok.nextToken())); // "A_IE";
                addParameters(lst, 404, getElementString(strtok.nextToken())); // "B1";
                addParameters(lst, 405, getElementString(strtok.nextToken())); // "B2";
                addParameters(lst, 406, getElementString(strtok.nextToken())); // "nikotinska kislina";
                addParameters(lst, 401, getElementString(strtok.nextToken())); // "C";

                Collections.sort(lst);
                
                //; Columns: id; name; name_i18n; group_id; refuse; description; home_weights; nutritions; changed                 
                
                //System.out.println("C: " + this.getCollectionString(lst));
                //String s = count + "|" + name + "|" + m_da.makeI18nKeyword(name) + "|" + grps_co.get(group) + "|0.0|null|null|" + this.getCollectionString(lst) + "|" + System.currentTimeMillis();

                //this.writeToFile(s + "\n");
                //System.out.print(".");

                
                FoodUserDescriptionH fud = (FoodUserDescriptionH) sess.get(FoodUserDescriptionH.class, new Long(count));
                
                if (!fud.getName().equals(name))
                    System.out.println("db=" + fud.getName() + ",name=" + name);
                
                fud.setNutritions(this.getCollectionString(lst));
                
                Transaction tx = sess.beginTransaction();

                sess.update(fud);

                tx.commit();
                
                
                //System.out.print("id=" + count + "kj=" + kj + "\n");
                
                
                count++;
                    
                
            }

            this.closeFile();

        }
        catch (Exception ex)
        {
            // System.err.println("Error on loadDailyValues: " + ex);
            log.error("Error on importSettings: \nData: " + line + "\nException: " + ex, ex);
            // ex.printStackTrace();
        }

    }

    
    private String getKcal(String kj)
    {
        //1 kilojoule = 0.239005736 kilocalories
        //1 kilojoule = 0.238845897 kilocalorie (IT)
        float f = 0.0f;
        
        try
        {
            f = Float.parseFloat(kj);
        }
        catch(Exception ex) {}
        
        return DataAccess.MmolDecimalFormat.format( (f * 0.238845897f ));
    }
    
    public void addParameters(ArrayList<DacioDbData> lst, int id, String value)
    {
        if ((value.equals("0")) || (value.equals("-")))
            return;
        
        lst.add(new DacioDbData(id, value));        
        
    }
    
    public String getCollectionString(ArrayList<DacioDbData> lst)
    {
        if (lst.size()==0)
            return "";
        
        StringBuffer sb = new StringBuffer();
        
        int size = lst.size()-1;
        
        for(int i=0; i<=size; i++)
        {
            if (i!=0)
                sb.append(";");
            
            sb.append(lst.get(i));
        }
        
        return sb.toString();
    }
    
    
    public String getElementString(String s)
    {
        String s1 = s;

        if (s1.startsWith("\""))
            s1 = s1.substring(1);
        
        if (s1.endsWith("\""))
            s1 = s1.substring(0, s1.length() - 1);
        
        return s1;
    }
    
    public void run()
    {
        this.convertFoods(); // .importSettings();
    }    
    
    
    private class DacioDbData implements Comparable<DacioDbData>
    {
        public DacioDbData(int id, String value)
        {
            this.id = id;
            this.value = value;
        }
        
        
        public int id;
        public String value;

        public int compare(DacioDbData ddd1, DacioDbData ddd2)
        {
            if (ddd1.id < ddd2.id)
                return -1;
            else if (ddd1.id == ddd2.id)
                return 0;
            else
                return 1;
        }

        public int compareTo(DacioDbData din)
        {

            if (this.id < din.id)
                return -1;
            else if (this.id == din.id)
                return 0;
            else
                return 1;
            
        }
        
        public String toString()
        {
            return this.id + "=" + this.value.replace(',', '.');
        }
        
    }
    
    
    public static void main(String args[])
    {
        /*
         * if (args.length == 0) {
         * System.out.println("You need to specify import file !"); return; }
         */

        // GGCDb db = new GGCDb();

        ImportDacioDb idb = new ImportDacioDb("../data/temp/zivila.csv"); //args[
                                                                          // 0
                                                                          // ]);
        idb.convertFoods();
    }

}
