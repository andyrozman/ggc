package ggc.plugin.protocol;


import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;

import java.io.File;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
 *
 *  See AUTHORS for copyright information.
 * 
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 * 
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 *  Filename:  ###---###  
 *  Description:
 * 
 *  Author: Andy {andy@atech-software.com}
 */


//STUB ONLY. Not implemented
//Will be used by Roche (Pump/Meter) in future


public abstract class XmlProtocol 
{

    protected DataAccessPlugInBase m_da = null; 
    protected OutputWriter m_output_writer = null;


    public XmlProtocol()
    {
    }

    public XmlProtocol(DataAccessPlugInBase da, OutputWriter ow)
    {
        this.m_da = da;
        this.m_output_writer = ow;
    }

    
    protected Document document;


    public Document openXmlFile(File file) throws DocumentException 
    {
        SAXReader reader = new SAXReader();
        
        document = reader.read(file);
        return document;
    }
    
    
    public Node getNode(String tag_path)
    {
        return document.selectSingleNode(tag_path);
    }
    
    public Element getElement(String tag_path)
    {
        return (Element)getNode(tag_path);
    }

    
    
    @SuppressWarnings("unchecked")
    public List<Node> getNodes(String tag_path)
    {
        List<Node> nodes = document.selectNodes(tag_path);
        return nodes;
    }
    
    
/*
    public int getConnectionProtocol()
    {
        return ConnectionProtocols.PROTOCOL_MASS_STORAGE_XML;
    }
  */  


}
