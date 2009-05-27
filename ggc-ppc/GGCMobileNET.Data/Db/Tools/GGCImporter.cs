using System;
using GGCMobileNET.Data.Utils;
using System.IO;
using System.Text;
namespace GGCMobileNET.Data.Db.Tools
{


    public class GGCImporter
    {

        GGCDbMobile m_db = null;
        public String file_name;
        //    private static Log log = LogFactory.getLog(GGCImporter.class); 
        String selected_class = null;
        DataAccessMobile m_da = DataAccessMobile.Instance; //.getInstance();



        public GGCImporter(String file_name)
        {
            this.file_name = file_name;
            this.identifyAndImport();
        }



        public void identifyAndImport()
        {

            this.checkFileTarget();

            Console.WriteLine("Importing file '" + this.file_name + "'.");

            if (this.selected_class == "None")
            {
                Console.WriteLine("Class type for export class was unidentified. Exiting !");
                //log.debug("File was not identified as valid import file !!!");
            }
            /*    	else if (this.selected_class.equals("ggc.core.db.hibernate.FoodUserDescriptionH"))
                    {
                        log.debug("File was identified as 'ggc.core.db.hibernate.FoodUserDescriptionH'.");
                        ImportNutrition in = new ImportNutrition(this.file_name, false);
                        in.importUserFood();
                    }
                    else if (this.selected_class.equals("ggc.core.db.hibernate.FoodUserGroupH"))
                    {
                        log.debug("File was identified as 'ggc.core.db.hibernate.FoodUserGroupH'.");
                        ImportNutrition in = new ImportNutrition(this.file_name, false);
                        in.importUserGroups();
                    }
                    else if (this.selected_class.equals("ggc.core.db.hibernate.MealH"))
                    {
                        log.debug("File was identified as 'ggc.core.db.hibernate.MealH'.");
                        ImportNutrition in = new ImportNutrition(this.file_name, false);
                        in.importMeals();
                    }
                    else if (this.selected_class.equals("ggc.core.db.hibernate.MealGroupH"))
                    {
                        log.debug("File was identified as 'ggc.core.db.hibernate.MealGroupH'.");
                        ImportNutrition in = new ImportNutrition(this.file_name, false);
                        in.importMealGroups();
                    } */
            else if (this.selected_class == "ggc.core.db.hibernate.DayValueH")
            {
                Console.WriteLine("File was identified as 'ggc.core.db.hibernate.DayValueH'.");
                ImportDailyValues idv = new ImportDailyValues(this.file_name, false);
                idv.importDailyValues();
            }

            Console.WriteLine();

        }




        public void checkFileTarget()
        {
            selected_class = "None";

            StreamReader br = null;
            try
            {
                br = new StreamReader(new FileStream(file_name, FileMode.Open), Encoding.UTF8);

                //BufferedReader br = new BufferedReader(new FileReader(new File(file_name)));

                String line;

                while ((line = br.ReadLine()) != null)
                {
                    //; Class: ggc.core.db.hibernate.FoodUserDescriptionH

                    if (line.Contains("Class:"))
                    {
                        String[] cls = m_da.splitString(line, " ");
                        this.selected_class = cls[2];
                    }

                }


            }
            catch (Exception ex)
            {

            }
            finally
            {
                if (br!=null)
                    br.Close();
            }
        }



        public static void main(String[] args)
        {
            if (args.Length == 0)
            {
                Console.WriteLine("You need to specify import file !");
                return;
            }

            new GGCImporter(args[0]);
        }




    }
}