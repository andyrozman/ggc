package ggc.pump.device.minimed.data;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class DecodeHistoryData
{

    public void decode(String data_file)
    {

        try
        {

            FileInputStream f_in = new FileInputStream(data_file);

            // Read object using ObjectInputStream
            ObjectInputStream obj_in = new ObjectInputStream(f_in);

            // Read an object
            Object obj = obj_in.readObject();

            if (obj instanceof CommandHistoryData)
            {
                // Cast object to a Vector
                CommandHistoryData chd = (CommandHistoryData) obj;

                // Do something with vector....
            }

        }
        catch (Exception ex)
        {
            System.out.println("Error decodiong data: " + ex);
        }

    }

    public static void main(String args[])
    {

        DecodeHistoryData dhd = new DecodeHistoryData();

        dhd.decode("test/mm_history_page_0.data");

    }

}
