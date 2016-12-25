package ggc.plugin.comm.ports.discovery;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.plugin.comm.ports.DevicePortDto;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.protocol.DeviceConnectionProtocol;

/**
 * Created by andy on 19.04.15.
 */
public class MassStorageDeviceDiscoveryAgent extends PortDiscoveryAgentAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(MassStorageDeviceDiscoveryAgent.class);


    public MassStorageDeviceDiscoveryAgent()
    {
        super(DeviceConnectionProtocol.MassStorageXML);
    }


    public List<DevicePortDto> getAllPossiblePorts() throws PlugInBaseException
    {
        List<String> drives = new ArrayList<String>();

        if (System.getProperty("os.name").contains("Win"))
        {
            File[] fls = File.listRoots();

            for (File fl : fls)
            {
                LOG.debug("Root found: {}", fl.toString());
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
                LOG.debug("Possible Port (root): {}", rt);

                File f = new File(rt);

                if (f.exists())
                {
                    LOG.debug("Possible Port (root) FOUND: {}", rt);
                    File[] f2 = f.listFiles();

                    for (File element : f2)
                    {
                        if (element.isDirectory())
                        {
                            LOG.debug("   DRIVE: {}", element.toString());
                            drives.add(element.toString());
                        }
                    }
                }
            }

        }

        return getListOfDevicePorts(drives);
    }


    public String getSelectProtocolString()
    {
        return "SELECT_MASS_STORAGE_DRIVE";
    }


    public String getPortDeviceName()
    {
        return "MASS_STORAGE_DRIVE";
    }


    public static void main(String[] args)
    {
        MassStorageDeviceDiscoveryAgent msd = new MassStorageDeviceDiscoveryAgent();
        List<DevicePortDto> ports = null;
        try
        {
            ports = msd.getAllPossiblePorts();
        }
        catch (PlugInBaseException e)
        {
            System.out.println("" + e.getMessage());
        }

        for (DevicePortDto p : ports)
        {
            System.out.println("D: " + p);
        }
    }

}
