package ggc.plugin.protocol;

import java.io.File;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;

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
 *  Filename:     XmlProtocol  
 *  Description:  This is implementation for Xml protocol. It contains methods for looking
 *                through Xml files
 *                
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class XmlProtocolFile
{

    protected DataAccessPlugInBase m_da = null;
    protected OutputWriter output_writer = null;


    // protected I18nControlAbstract i18nControlAbstract = null;

    /**
     * Constructor
     * 
     * @param da 
     * @param ow 
     */
    public XmlProtocolFile(DataAccessPlugInBase da, OutputWriter ow)
    {
        this.m_da = da;
        this.output_writer = ow;
    }


    /**
     * Constructor
     *
     * @param da
     */
    public XmlProtocolFile(DataAccessPlugInBase da)
    {
        this.m_da = da;
    }

    protected Document document;


    /**
     * Open Xml File
     * 
     * @param file
     * @return
     * @throws DocumentException
     */
    public Document openXmlFile(File file) throws DocumentException
    {
        SAXReader reader = new SAXReader();

        document = reader.read(file);
        return document;
    }


    /**
     * Get Node
     * 
     * @param tag_path tag path
     * @return Node object
     */
    public Node getNode(String tag_path)
    {
        return document.selectSingleNode(tag_path);
    }


    /**
     * Get Element
     * 
     * @param tag_path tag path
     * @return Element object
     */
    public Element getElement(String tag_path)
    {
        return (Element) getNode(tag_path);
    }


    /**
     * Return List of nodes from path
     * 
     * @param tag_path tag path
     * @return List<Nodes> instance with nodes
     */
    @SuppressWarnings("unchecked")
    public List<Node> getNodes(String tag_path)
    {
        List<Node> nodes = document.selectNodes(tag_path);
        return nodes;
    }

}
