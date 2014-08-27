package ggc.cgms.device.dexcom.receivers.data.output;

import ggc.cgms.device.dexcom.receivers.data.ReceiverDownloadData;

public interface DataOutputParserInterface
{

    void parse(DataOutputParserType parserType, ReceiverDownloadData data);

}
