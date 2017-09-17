package ggc.core.db.tool.impexp;

import java.io.File;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.data.mng.DataDefinitionEntry;
import com.atech.db.hibernate.HibernateBackupObject;
import com.atech.db.hibernate.HibernateObject;
import com.atech.db.hibernate.tool.data.DatabaseImportStrategy;
import com.atech.db.hibernate.tool.data.dto.DbTableExportInfoDto;
import com.atech.db.hibernate.tool.data.management.common.ImportExportContext;
import com.atech.db.hibernate.tool.data.management.common.ImportExportStatusType;
import com.atech.db.hibernate.tool.data.management.impexp.ImportTool;
import com.atech.db.hibernate.transfer.BackupRestoreWorkGiver;
import com.atech.db.hibernate.transfer.RestoreFileInfo;

import ggc.core.util.DataAccess;

/**
 * Created by andy on 15/12/16.
 */
public class GGCDbImporter extends ImportTool
{

    private static final Logger LOG = LoggerFactory.getLogger(GGCDbImporter.class);

    GGCDbImportConverter converter;


    public GGCDbImporter(Configuration cfg, ImportExportContext context, RestoreFileInfo restoreFileInfo)
    {
        super(cfg, context, restoreFileInfo);
        converter = new GGCDbImportConverter(DataAccess.getInstance());
    }


    /**
     * Constructor
     *
     * @param giver
     */
    public GGCDbImporter(BackupRestoreWorkGiver giver, ImportExportContext importExportContext,
            RestoreFileInfo restoreFileInfo)
    {
        super(DataAccess.getInstance().getDb().getHibernateConfiguration(), importExportContext, restoreFileInfo);
        converter = new GGCDbImportConverter(DataAccess.getInstance());

        this.setStatusReceiver(giver);
        this.setTypeOfStatus(ImportExportStatusType.Special);
    }


    public void importData(Class<? extends HibernateBackupObject> clazz, DataDefinitionEntry definitionEntry,
            DatabaseImportStrategy databaseImportStrategy)
    {

        String line = null;
        boolean append = false;

        try
        {

            if (databaseImportStrategy == DatabaseImportStrategy.Clean)
            {
                if (definitionEntry.hasToBeCleaned())
                    this.clearExistingData(clazz.getSimpleName());
            }
            else if (databaseImportStrategy == DatabaseImportStrategy.Append)
            {
                append = true;
            }

            this.openFileForReading(new File(this.getRootPath() + clazz.getSimpleName() + ".dbe"));

            int dot_mark = 5;
            int count = 0;

            DbTableExportInfoDto exportInfoDto = new DbTableExportInfoDto();
            exportInfoDto.setDatabaseImportStrategy(databaseImportStrategy);

            while ((line = this.bufferedReader.readLine()) != null)
            {
                if (line.trim().startsWith(";") || line.trim().length() == 0)
                {
                    exportInfoDto.addHeaderLine(line);
                    continue;
                }

                HibernateObject object = converter.convert(definitionEntry, line, exportInfoDto);

                this.hibernateUtil.add(object);

                count++;
                this.writeStatus(dot_mark, count);

            }

            this.closeFile();

        }
        catch (Exception ex)
        {
            LOG.error("Error on importData: \nData: " + line + "\nException: " + ex, ex);
        }

    }


    @Override
    public int getActiveSession()
    {
        return 1;
    }
}
