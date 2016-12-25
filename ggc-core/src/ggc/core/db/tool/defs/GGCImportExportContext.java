package ggc.core.db.tool.defs;

import com.atech.db.hibernate.tool.data.DatabaseVersionConfiguration;
import com.atech.db.hibernate.tool.data.management.common.ImportExportContextAbstract;
import com.atech.db.hibernate.tool.data.management.export.DbExportConverter;

import ggc.core.db.tool.data.GGCDatabaseVersionConfiguration;
import ggc.core.db.tool.impexp.GGCDbExportConverter;

/**
 * Created by andy on 16/12/16.
 */
public class GGCImportExportContext extends ImportExportContextAbstract
{

    public GGCImportExportContext(String exportLocation, DbExportConverter dbExportConverter,
            DatabaseVersionConfiguration databaseVersionConfiguration)
    {
        super("../data/export", new GGCDbExportConverter(), GGCDatabaseVersionConfiguration.GGC_DB_V7);
    }

}
