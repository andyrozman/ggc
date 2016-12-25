package ggc.core.db.tool.impexp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.db.hibernate.tool.data.dto.DbImportExportFileDto;
import com.atech.db.hibernate.transfer.ImportTool;
import com.atech.utils.ATDataAccessAbstract;

import ggc.core.db.GGCDb;
import ggc.core.db.hibernate.DayValueH;

/**
 * Created by andy on 15/12/16.
 */
public class GGCDbImporter extends ImportTool
{

    private static final Logger LOG = LoggerFactory.getLogger(GGCDbImporter.class);

    // String restoreFilename;
    File restoreFile;
    GGCDb m_db;
    boolean clean_db;


    /**
     * Constructor
     *
     * @param file_name
     * @param identify
     */
    public void ImportDailyValues(String file_name, boolean identify)
    {
        // super();

        m_db = new GGCDb();
        m_db.initDb();
        createHibernateUtil(m_db.getHibernateConfiguration());
        this.restoreFile = new File(file_name);

        if (identify)
        {
            importDailyValues();
        }

        System.out.println();

    }


    public void importDbFile(String fileName)
    {
        importDbFile(fileName, true, false);
    }


    public void importDbFile(String fileName, boolean cleanTable, boolean append)
    {
        String line = null;
        // boolean append = false;

        try
        {

            if (cleanTable)
            {
                this.clearExistingData("ggc.core.db.hibernate.DayValueH");
            }

            // else
            // {
            // append = true;
            // }

            System.out.println("\nLoading DailyValues (5/dot)");

            this.openFileForReading(this.restore_file);

            // BufferedReader br = new BufferedReader(new
            // FileReader(this.restore_file)); //new File(file_name)));

            // int i = 0;

            int dot_mark = 5;
            int count = 0;

            LOG.debug("Loading file: " + fileName);

            List<String> headers = new ArrayList();
            List<String> data = new ArrayList();

            while ((line = this.bufferedReader.readLine()) != null)
            {
                if (StringUtils.isNotBlank(line))
                {
                    if (line.startsWith(";"))
                    {
                        headers.add(line);
                    }
                    else
                    {
                        data.add(line);
                    }
                }
            }

            DbImportExportFileDto settingsDto = processHeaders(headers);

            while ((line = this.bufferedReader.readLine()) != null)
            {
                if (line.startsWith(";"))
                {
                    continue;
                }

                // line = line.replaceAll("||", "| |");
                line = ATDataAccessAbstract.replaceExpression(line, "||", "| |");

                StringTokenizer strtok = new StringTokenizer(line, "|");

                DayValueH dvh = new DayValueH();

                // ; Columns: id,dt_info,bg,ins1,ins2,ch,meals_ids,act,comment

                // 1|200603250730|0|10.0|0.0|0.0|null|null|
                // id; dt_info; bg; ins1; ins2; ch; meals_ids; extended;
                // person_id; comment

                if (!append)
                {
                    long id = this.getLong(strtok.nextToken());

                    if (id != 0)
                    {
                        dvh.setId(id);
                    }
                }
                else
                {
                    strtok.nextToken();
                }

                dvh.setDt_info(getLong(strtok.nextToken()));
                dvh.setBg(getInt(strtok.nextToken()));
                dvh.setIns1((int) getFloat(strtok.nextToken()));
                dvh.setIns2((int) getFloat(strtok.nextToken()));
                dvh.setCh(getFloat(strtok.nextToken()));
                dvh.setMeals_ids(getString(strtok.nextToken()));
                dvh.setExtended(getString(strtok.nextToken()));

                int person_id = this.getInt(strtok.nextToken());

                if (person_id == 0)
                {
                    dvh.setPerson_id(1);
                }
                else
                {
                    dvh.setPerson_id(person_id);
                }

                dvh.setComment(getString(strtok.nextToken()));
                dvh.setChanged(getLong(strtok.nextToken()));

                /*
                 * String act = getString(strtok.nextToken());
                 * if (act != null) { dvh.setExtended("ACTIVITY=" + act); }
                 * String bef = "MTI"; // String bef = null;
                 * if (strtok.hasMoreElements()) { String comm =
                 * getString(strtok.nextToken());
                 * // remove if (bef!=null) comm += ";" + bef;
                 * if (comm!=null) dvh.setComment(comm); } else { if (bef!=null)
                 * dvh.setComment(bef); }
                 */

                this.hibernate_util.addHibernate(dvh);

                count++;
                this.writeStatus(dot_mark, count);

                /*
                 * i++;
                 * if (i % 5 == 0)
                 * System.out.print(".");
                 */
            }

            this.closeFile();

        }
        catch (Exception ex)
        {
            // System.err.println("Error on loadDailyValues: " + ex);
            LOG.error("Error on importDailyValues: \nData: " + line + "\nException: " + ex, ex);
            // ex.printStackTrace();
        }

    }


    private DbImportExportFileDto processHeaders(List<String> headers)
    {

        return null;
    }


    public void importDailyValues()
    {

        String line = null;
        boolean append = false;

        try
        {

            if (clean_db)
            {
                this.clearExistingData("ggc.core.db.hibernate.DayValueH");
            }
            else
            {
                append = true;
            }

            System.out.println("\nLoading DailyValues (5/dot)");

            this.openFileForReading(this.restore_file);

            // BufferedReader br = new BufferedReader(new
            // FileReader(this.restore_file)); //new File(file_name)));

            // int i = 0;

            int dot_mark = 5;
            int count = 0;

            while ((line = this.bufferedReader.readLine()) != null)
            {
                if (line.startsWith(";"))
                {
                    continue;
                }

                // line = line.replaceAll("||", "| |");
                line = ATDataAccessAbstract.replaceExpression(line, "||", "| |");

                StringTokenizer strtok = new StringTokenizer(line, "|");

                DayValueH dvh = new DayValueH();

                // ; Columns: id,dt_info,bg,ins1,ins2,ch,meals_ids,act,comment

                // 1|200603250730|0|10.0|0.0|0.0|null|null|
                // id; dt_info; bg; ins1; ins2; ch; meals_ids; extended;
                // person_id; comment

                if (!append)
                {
                    long id = this.getLong(strtok.nextToken());

                    if (id != 0)
                    {
                        dvh.setId(id);
                    }
                }
                else
                {
                    strtok.nextToken();
                }

                dvh.setDt_info(getLong(strtok.nextToken()));
                dvh.setBg(getInt(strtok.nextToken()));
                dvh.setIns1((int) getFloat(strtok.nextToken()));
                dvh.setIns2((int) getFloat(strtok.nextToken()));
                dvh.setCh(getFloat(strtok.nextToken()));
                dvh.setMeals_ids(getString(strtok.nextToken()));
                dvh.setExtended(getString(strtok.nextToken()));

                int person_id = this.getInt(strtok.nextToken());

                if (person_id == 0)
                {
                    dvh.setPerson_id(1);
                }
                else
                {
                    dvh.setPerson_id(person_id);
                }

                dvh.setComment(getString(strtok.nextToken()));
                dvh.setChanged(getLong(strtok.nextToken()));

                /*
                 * String act = getString(strtok.nextToken());
                 * if (act != null) { dvh.setExtended("ACTIVITY=" + act); }
                 * String bef = "MTI"; // String bef = null;
                 * if (strtok.hasMoreElements()) { String comm =
                 * getString(strtok.nextToken());
                 * // remove if (bef!=null) comm += ";" + bef;
                 * if (comm!=null) dvh.setComment(comm); } else { if (bef!=null)
                 * dvh.setComment(bef); }
                 */

                this.hibernate_util.addHibernate(dvh);

                count++;
                this.writeStatus(dot_mark, count);

                /*
                 * i++;
                 * if (i % 5 == 0)
                 * System.out.print(".");
                 */
            }

            this.closeFile();

        }
        catch (Exception ex)
        {
            // System.err.println("Error on loadDailyValues: " + ex);
            LOG.error("Error on importDailyValues: \nData: " + line + "\nException: " + ex, ex);
            // ex.printStackTrace();
        }

    }


    @Override
    public int getActiveSession()
    {
        return 1;
    }
}
