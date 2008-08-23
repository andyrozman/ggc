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
