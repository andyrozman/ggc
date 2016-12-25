using System.IO;
using System;
using System.Text;

namespace GGCMobileNET.Data.Tools
{

    public class RestoreFileInfo
    {
        public String name = "";
        public String class_name = "";
        public int element_count = 0;
        public String db_version = "";
        FileInfo file;

        public FileInfo File
        {
            get { return file; }
            set { file = value; }
        }
        public bool selected = false;


        public RestoreFileInfo(FileInfo file)
        {
            this.file = file;
            processFile();
        }

        public void processFile()
        {
            this.name = this.file.Name;

            try
            {
                StreamReader br = new StreamReader(new FileStream(file.FullName, FileMode.CreateNew), Encoding.UTF8);

                //BufferedReader br = new BufferedReader(new FileReader(this.file));

                String line;
                int data_line = 0;

                while ((line = br.ReadLine()) != null)
                {
                    if (line.StartsWith(";"))
                        processStatusLine(line);
                    else if (line.Trim().Length > 0)
                    {
                        data_line++;
                    }
                }

                this.element_count = data_line;

            }
            catch (Exception ex)
            {
                Console.WriteLine("Exception on processFile: Ex: " + ex);
            }

        }

        private void processStatusLine(String line)
        {
            if (line.Contains("Class:"))
            {
                this.class_name = this.getSearchedValue("Class:", line);
            }

            if (line.Contains("Database version:"))
            {
                this.db_version = this.getSearchedValue("Database version:", line);
            }

        }

        private String getSearchedValue(String keyword, String data)
        {
            int ind = data.IndexOf(keyword) + keyword.Length + 1;
            return data.Substring(ind);
        }

        public String toString()
        {
            return "RestoreFileInfo [filename=" + this.name + ",class_name=" + this.class_name + ",db_ver="
                    + this.db_version + ",data=" + this.element_count + "]";
        }

    }
}