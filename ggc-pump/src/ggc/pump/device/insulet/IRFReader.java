package ggc.pump.device.insulet;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author andy
 *
 */
public class IRFReader
{

    /**
     * @param filename
     */
    public IRFReader(String filename)
    {

        try
        {
            InputStream in = new FileInputStream(filename);
            // OutputStream out = System.out;

            // BufferedWriter bw = new BufferedWriter(new
            // FileWriter("../test/outt.txt"));
            // OutputWriter ow = new OutputWriter();

            // MacBinaryDecoderOutputStream pout = new
            // MacBinaryDecoderOutputStream(out);

            // pout.write()

            for (int c = in.read(); c != -1; c = in.read())
            {
                if (c > 30)
                {
                    System.out.print((char) c);
                }

                if (c == 131) // 0083
                {
                    System.out.println();
                }

                // System.out.println(".");
                // System.out.println("c: " + (char)c);

                // pout.write(c);
                // System.out.println("Read: " + pout.getBytesFiltered());
            }
            // pout.flush();

            // out.close();

            // System.out.println("Read: " + pout.getBytesFiltered());

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    /*
     * public static void main(String[] args)
     * {
     * new IRFReader("../test/20006422-2009-09-17-23-09-07.ibf");
     * }
     */

}
