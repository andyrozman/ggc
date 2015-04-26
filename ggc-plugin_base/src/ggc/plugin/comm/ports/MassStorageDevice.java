package ggc.plugin.comm.ports;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by andy on 19.04.15.
 */
public class MassStorageDevice implements CommunicationPortDiscovery
{

    public String getKey()
    {
        return null;
    }


    public List<String> getAllPossiblePorts()
    {

        List<String> drives = new ArrayList<String>();

        if (System.getProperty("os.name").contains("Win"))
        {
            File[] fls = File.listRoots();

            for (File fl : fls)
            {
                drives.add(fl.toString());
            }
        }
        else
        {
            // non windows system, will load data from /mnt and /media and
            // /Volumes. Some Linux machines also have additional mount
            // point in latest releases (/media/<username>), which is
            // also checked.

            List<String> rts = new ArrayList<String>();
            rts.add("/mnt");
            rts.add("/Volumes");

            if (System.getProperty("os.name").equals("Linux"))
            {
                String fName = "/media/" + System.getProperty("user.name");
                File f = new File(fName);

                if (f.exists())
                {
                    rts.add(fName);
                }
                else
                {
                    rts.add("/media");
                }
            }

            for (String rt : rts)
            {
                System.out.println("Possible Port (root): " + rt);

                File f = new File(rt);

                if (f.exists())
                {
                    System.out.println("Possible Port (root) FOUND: " + rt);
                    File[] f2 = f.listFiles();

                    for (File element : f2)
                    {
                        if (element.isDirectory())
                        {
                            System.out.println("   DRIVE: " + element.toString());
                            drives.add(element.toString());
                        }
                    }
                }
            }

        }

        return drives;

    }


    public static void main(String[] args)
    {
        MassStorageDevice msd = new MassStorageDevice();
        List<String> ports = msd.getAllPossiblePorts();

        for (String p : ports)
        {
            System.out.println("D: " + p);
        }
    }

}
