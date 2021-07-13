package ggc.core.util;

import java.util.ArrayList;

import com.atech.data.FileDirectoryDto;
import com.atech.data.user_data_dir.UserDataDirectoryContextAbstract;

/**
 * Created by andy on 18/12/17.
 */
public class GGCUserDataDirectoryContext extends UserDataDirectoryContextAbstract
{

    String basePath;



    public GGCUserDataDirectoryContext(String path)
    {
        super();
        // validator per version !!!!!!!!!!!!!!!!!!!!!!!!
    }


    @Override
    protected void initData() {

        this.directoriesToCheck = new ArrayList<FileDirectoryDto>();
        this.directoriesToCheck.add(new FileDirectoryDto("db", true));
        this.directoriesToCheck.add(new FileDirectoryDto("export", true));
        this.directoriesToCheck.add(new FileDirectoryDto("lang", true));
        this.directoriesToCheck.add(new FileDirectoryDto("nutrition", true));

        this.filesToCheck = new ArrayList<FileDirectoryDto>();
        // fileName, isDirectory, template, replacementForced, checkNeeded
        this.filesToCheck.add(new FileDirectoryDto("db/ggc_db.data.db", false, "../data_template/db.zip", false, true));
        this.filesToCheck.add(new FileDirectoryDto("lang/GGC_Languages.properties", false, "../data_template/lang.zip", true, true));
        this.filesToCheck.add(new FileDirectoryDto("tools/WebLister.properties", false, "../data_template/tools.zip", true, true));
        this.filesToCheck.add(new FileDirectoryDto("GGC_Config.properties", false, "../data_template/root.zip", false, true));
        this.filesToCheck.add(new FileDirectoryDto("skinlf_themes/modernthemepack_ggc.zip", false, "../data_template/skinlf_themes.zip", false, true));
        this.filesToCheck.add(new FileDirectoryDto("update/GGC_Update.properties", false, "../data_template/update.zip", true, true));
        this.filesToCheck.add(new FileDirectoryDto("nutrition/NUT_DATA.txt", false, "../data_template/nutrition.zip", false, false));
    }
}
