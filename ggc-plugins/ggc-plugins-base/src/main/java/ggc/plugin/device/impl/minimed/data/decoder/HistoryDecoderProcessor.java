package ggc.plugin.device.impl.minimed.data.decoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.data.MinimedDataPage;
import ggc.plugin.device.impl.minimed.util.MinimedUtil;
import ggc.plugin.output.OutputWriter;

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
 *  Filename:     HistoryDecoderProcessor
 *  Description:  History Decoder Processor (this is called when processing).
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class HistoryDecoderProcessor
{

    private static final Logger LOG = LoggerFactory.getLogger(HistoryDecoderProcessor.class);


    public HistoryDecoderProcessor()
    {
    }


    public boolean processPage(MinimedDataPage dataPage, OutputWriter outputWriter) throws PlugInBaseException
    {
        MinimedHistoryDecoder decoder = MinimedUtil.getDecoder(dataPage.getTargetType(), dataPage.commandType);

        LOG.debug("Process Page: targetType={}, commandType={}", dataPage.getTargetType(), dataPage.commandType);

        if (decoder != null)
        {
            decoder.setOutputWriter(outputWriter);
            decoder.decodePage(dataPage);
            return true;
        }
        else
        {
            LOG.error("Creator for this target type ({}) and this CommandType ({}), could not be found. ",
                dataPage.getTargetType(), dataPage.commandType);
            return false;
        }

    }

}
