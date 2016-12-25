package ggc.connect;

import java.io.FileWriter;
import java.io.IOException;

import com.atech.utils.ATDataAccessAbstract;

/**
 * Created by andy on 30/04/16.
 */
public class TestRun
{

    public static void main(String[] args)
    {
        try
        {

            FileWriter fw = new FileWriter("/home/andy/tt.txt");

            for (int i = 1; i < 900; i++)
            {
                fw.write("   " + ATDataAccessAbstract.getLeadingZero(i, 3) + "\n");
            }

            fw.flush();
            fw.close();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
