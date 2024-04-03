package ggc.core.db.tool.defs;

import com.atech.db.hibernate.tool.data.management.common.ImportExportContextAbstract;

import ggc.core.db.tool.data.GGCDatabaseVersionConfiguration;
import ggc.core.db.tool.impexp.GGCDbExportConverter;
import ggc.core.db.tool.impexp.GGCDbImportConverter;
import ggc.core.util.DataAccess;

/**
 * Created by andy on 16/12/16.
 */
public class GGCImportExportContext extends ImportExportContextAbstract
{

    public GGCImportExportContext()
    {
        super("../data/export/", //
                new GGCDbExportConverter(), //
                new GGCDbImportConverter(DataAccess.getInstance()), //
                GGCDatabaseVersionConfiguration.GGC_DB_V7);
    }

}
