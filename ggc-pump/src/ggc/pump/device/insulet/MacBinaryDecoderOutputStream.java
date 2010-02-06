// Copyright (C) 1999-2001 by Jason Hunter <jhunter_AT_acm_DOT_org>.
// All rights reserved.  Use of this class is limited.
// Please see the LICENSE for more information.

//package com.test;


package ggc.pump.device.insulet;

import java.io.FileInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A <code>MacBinaryDecoderOutput</code> filters MacBinary files to normal 
 * files on the fly; optimized for speed more than readability.
 * 
 * @author Jason Hunter
 */
public class MacBinaryDecoderOutputStream extends FilterOutputStream 
{
  private int bytesFiltered = 0;
  private int dataForkLength = 0;

  public MacBinaryDecoderOutputStream(OutputStream out) {
    super(out);
  }
  
  
  public int getBytesFiltered()
  {
      return this.bytesFiltered;
  }

  public void write(int b) throws IOException {
    // Bytes 83 through 86 are a long representing the data fork length
    // Check <= 86 first to short circuit early in the common case
    if (bytesFiltered <= 86 && bytesFiltered >= 83) {
      int leftShift = (86 - bytesFiltered) * 8;
      dataForkLength = dataForkLength | (b & 0xff) << leftShift;
    }

    // Bytes 128 up to (128 + dataForkLength - 1) are the data fork
    else if (bytesFiltered < (128 + dataForkLength) && bytesFiltered >= 128) {
      out.write(b);
    }

    bytesFiltered++;
  }

  public void write(byte b[]) throws IOException 
  {
    write(b, 0, b.length);
  }

  public void write(byte b[], int off, int len) throws IOException 
  {
    // If the write is for content past the end of the data fork, ignore
    if (bytesFiltered >= (128 + dataForkLength)) {
      bytesFiltered += len;
    }
    // If the write is entirely within the data fork, write it directly
    else if (bytesFiltered >= 128 && 
             (bytesFiltered + len) <= (128 + dataForkLength)) {
      out.write(b, off, len);
      bytesFiltered += len;
    }
    // Otherwise, do the write a byte at a time to get the logic above
    else {
      for (int i = 0 ; i < len ; i++) {
        write(b[off + i]);
      }
    }
  }
  
  public static void main(String[] args) throws Exception 
  {
      try
      {
        InputStream in = new FileInputStream("../test/20006422-2009-09-17-23-09-07.ibf");
        OutputStream out = System.out;
    
        //BufferedWriter bw = new BufferedWriter(new FileWriter("../test/outt.txt"));
        //OutputWriter ow = new OutputWriter();
        
        MacBinaryDecoderOutputStream pout = new MacBinaryDecoderOutputStream(out);
        
        
        
        
        //pout.write()
        
        
        for (int c = in.read(); c != -1; c = in.read()) 
        {
            System.out.println(".");
            System.out.println("c: " + (char)c);
            
            pout.write(c);
            System.out.println("Read: " + pout.getBytesFiltered());
        }
        //pout.flush();
        
        out.close();
        
        System.out.println("Read: " + pout.getBytesFiltered());
        
      }
      catch(Exception ex)
      {
          ex.printStackTrace();
      }
    
  }
  
  
  
  
  
}