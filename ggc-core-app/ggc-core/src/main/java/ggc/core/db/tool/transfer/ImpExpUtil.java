package ggc.core.db.tool.transfer;

import com.atech.data.user_data_dir.UserDataDirectory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * Created by andy on 09.04.2024.
 */
@Slf4j
public class ImpExpUtil {

    private final static UserDataDirectory userDataDirectory = UserDataDirectory.getInstance();

    private static String exportPath;
    private static String exportPathTemp;
    private static String importPath;

    private static String userDataDirectoryPath;

    public static String getImportPath() {
        if (importPath!=null)
            return importPath;

        createImportPath();

        return importPath;
    }

    public static String getExportPath() {
        if (exportPath!=null)
            return exportPath;

        createExportPath();

        return exportPath;
    }


    public static String getExportPathTemp() {
        if (exportPathTemp!=null)
            return exportPathTemp;

        createExportPath();

        return exportPathTemp;
    }


    private static void createExportPath() {

        if (userDataDirectoryPath==null) {
            userDataDirectoryPath = userDataDirectory.getUserDataDirectory();
        }

        try {
            File f = new File(userDataDirectoryPath, "export");
            if (!f.exists()) {
                FileUtils.forceMkdir(f);
            }

            f = new File(f, "tmp");
            if (!f.exists()) {
                FileUtils.forceMkdir(f);
            }
        } catch(Exception ex) {
            log.error("Problem creating export path: {}", ex.getMessage(), ex);
        }

        exportPath = userDataDirectoryPath + "/export/";
        exportPathTemp = exportPath + "tmp/";
    }


    private static void createImportPath() {

        if (userDataDirectoryPath==null) {
            userDataDirectoryPath = userDataDirectory.getUserDataDirectory();
        }

        try {
            File f = new File(userDataDirectoryPath, "import");
            if (!f.exists()) {
                FileUtils.forceMkdir(f);
            }
        } catch(Exception ex) {
            log.error("Problem creating import path: {}", ex.getMessage(), ex);
        }

        importPath = userDataDirectoryPath + "/import";
    }


}
