package ggc.core.util;

import java.util.ArrayList;

import com.atech.data.FileDirectoryDto;
import com.atech.data.install_migration.InstallRecipe;
import com.atech.data.install_migration.MigrationRecipe;
import com.atech.data.user_data_dir.v2.UserDataDirectoryContextV2Abstract;
import org.hibernate.tool.hbm2ddl.TableMetadata;

/**
 * Created by andy on 18/12/17.
 */
public class GGCUserDataDirectoryContext extends UserDataDirectoryContextV2Abstract
{


    public GGCUserDataDirectoryContext() {
        super();
    }


    @Override
    protected void initData() {
        this.directoriesToCheck = new ArrayList<FileDirectoryDto>();
        this.directoriesToCheck.add(new FileDirectoryDto("db", true));
        this.directoriesToCheck.add(new FileDirectoryDto("export", true));
        this.directoriesToCheck.add(new FileDirectoryDto("log", true));

        this.filesToCheck = new ArrayList<FileDirectoryDto>();
        this.filesToCheck.add(new FileDirectoryDto("db/ggc_db.mv.db", false, "install/db/h2_db_7.zip", false, true));
        this.filesToCheck.add(new FileDirectoryDto("GGC_Config.properties", false, "/data_template/root.zip", false, true));
        this.filesToCheck.add(new FileDirectoryDto("skinlf_themes/modernthemepack_ggc.zip", false, "../data_template/skinlf_themes.zip", false, false));
        this.filesToCheck.add(new FileDirectoryDto("skinlf_themes/modernthemepack_orig.zip", false, "../data_template/skinlf_themes.zip", false, true));
    }


    @Override
    public String getApplicationName() {
        return "GGC Gluco Control";
    }


    @Override
    public String getApplicationDataDirectoryName() {
        return "GNU_Gluco_Control";
    }


    @Override
    public String getFallbackDirectory() {
        return "./data/";
    }


    @Override
    public int howManyParentsToTraverseForFindingLegacyDataFolder() {
        return 2;
    }


    @Override
    public String getLegacyApplicationDataFolderName() {
        return "data";
    }


    public MigrationRecipe getMigrationRecipe() {
        MigrationRecipe recipe = new MigrationRecipe();

        recipe.addRenameBefore("log/GGC.log", "log/GGC_beforeMigration.log");
        recipe.addCopyContents("log");
        recipe.addCopyDirectory("db");
        recipe.addUnzipArchive("/install/skinlf_themes.zip", null);
        recipe.addCopyDirectoryOrInstall("nutrition", "/install/nutrition.zip");
        recipe.addCopyDirectory("tools");
        recipe.addDeleteFileFromTarget("tools/WebLister.properties");
        recipe.addCopyFile("GGC_Config.properties");
        recipe.addEditFileInTargetReplaceLine("GGC_Config.properties", "DB0_CONN_URL=", "DB0_CONN_URL=jdbc:h2:%USER_DATA_DIR%/db/ggc_db");
        recipe.addRenameSourceAfter();

        return recipe;
    }


    public InstallRecipe getInstallRecipe() {
        InstallRecipe recipe = new InstallRecipe();

        recipe.addUnzipArchive("/install/db_7_v2.zip");
        recipe.addUnzipArchive("/install/data_root.zip");
        recipe.addUnzipArchive("/install/skinlf_themes.zip");
        recipe.addUnzipArchive("/install/nutrition.zip");
        recipe.addMakeDirectory("tools");
        recipe.addMakeDirectory("export");
        recipe.addMakeDirectory("print");

        return recipe;
    }

}
