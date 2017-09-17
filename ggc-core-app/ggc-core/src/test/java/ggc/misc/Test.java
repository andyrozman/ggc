package ggc.misc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.*;

import com.atech.i18n.tool.simple.internal.TranslationToolInternalWizard1_ConfigurationCheck;

import ggc.core.util.DataAccess;

/**
 * Created by andy on 05/10/16.
 */
public class Test
{

    public static void copyFile()
    {
        try
        {
            System.out.println(ExportResource("/GGC_en.properties"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    static public String ExportResource(String resourceName) throws Exception
    {
        InputStream stream = null;
        OutputStream resStreamOut = null;
        String jarFolder;
        try
        {
            stream = Test.class.getResourceAsStream(resourceName);// note that
                                                                  // each / is a
                                                                  // directory
                                                                  // down in the
                                                                  // "jar tree"
                                                                  // been the
                                                                  // jar the
                                                                  // root of the
                                                                  // tree
            if (stream == null)
            {
                throw new Exception("Cannot get resource \"" + resourceName + "\" from Jar file.");
            }

            int readBytes;
            byte[] buffer = new byte[4096];
            jarFolder = new File(Test.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath())
                    .getParentFile().getPath().replace('\\', '/');
            resStreamOut = new FileOutputStream(jarFolder + resourceName);
            while ((readBytes = stream.read(buffer)) > 0)
            {
                resStreamOut.write(buffer, 0, readBytes);
            }
        }
        catch (Exception ex)
        {
            System.out.println("Exception: " + ex.getMessage());
            throw ex;
        }
        finally
        {
            if (stream != null)
                stream.close();

            if (resStreamOut != null)
                resStreamOut.close();
        }

        return jarFolder + resourceName;
    }


    public static void main(String[] args)
    {

        // Test.copyFile();

        Test.translationTool();

        // BigDecimal val1 = new BigDecimal(200);
        // BigDecimal val2 = new BigDecimal(35.7);
        //
        // System.out.println("Val1: " + val1 + ", val2: " + val2 + " result: "
        // + (val1.compareTo(val2) > 0));
        //
        // String errorCode = "AddNetworkService:[AVAC][GPRS_ADD]";
        //
        // Matcher matcher =
        // Pattern.compile("(\\[\\w+\\]\\[\\w+\\])").matcher(errorCode);
        // if (matcher.find())
        // {
        // System.out.println(matcher.group());
        // }

    }


    private static void translationTool()
    {
        JFrame frame = new JFrame();

        DataAccess dataAccess = DataAccess.createInstance(frame);

        new TranslationToolInternalWizard1_ConfigurationCheck(frame, dataAccess);

    }

}
