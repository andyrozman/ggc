package ggc.plugin.device.impl.minimed.comm.usb.contournext.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.plugin.comm.Hid4JavaCommunicationHandler;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.comm.usb.MinimedCommunicationContourNext2;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.MedtronicCnlSession;
import ggc.plugin.device.impl.minimed.data.dto.MinimedConnectionParametersDTO;
import ggc.plugin.device.impl.minimed.enums.MinimedCommInterfaceType;

/**
 * Created by andy on 31.05.17.
 */
public class SimpleReadContourNext
{

    private static final Logger LOG = LoggerFactory.getLogger(SimpleReadContourNext.class);

    MedtronicCnlSession session;
    Hid4JavaCommunicationHandler communicationHandler;


    public SimpleReadContourNext()
    {
        session = createSession();
        createConnectionDto();
    }


    public static void createConnectionDto()
    {
        MinimedConnectionParametersDTO connectionParametersDTO = new MinimedConnectionParametersDTO( //
                "1a79:6300" + //
                        "#;#" + //
                        "MINIMED_INTERFACE!=" + MinimedCommInterfaceType.ContourNextLink2.name() + //
                        "#;#" + //
                        "MINIMED_SERIAL!=316551");

    }


    public static MedtronicCnlSession createSession()
    {
        MedtronicCnlSession sess = new MedtronicCnlSession();
        sess.setMinimedCommunicationInterfaceType(MinimedCommInterfaceType.ContourNextLink2);
        sess.setStickSerial("C16325");
        sess.setPumpSerial("316551");

        // MinimedUtil.

        return sess;
    }


    public void initConnectorDevice() throws PlugInBaseException
    {
        LOG.debug("# Opening device");

        communicationHandler = new Hid4JavaCommunicationHandler();
        communicationHandler.setTargetDevice("1a79:6300");
        communicationHandler.setDelayForTimedReading(5000);

        // communicationHandler = new Hid4JavaCommunicationHandler();
        // communicationHandler.setTargetDevice(selectedDevice);
        // communicationHandler.setAllowedDevices(handler.getAllowedDevicesList());
        // communicationHandler.setDelayForTimedReading(100);

        boolean connected = communicationHandler.connectAndInitDevice();

        LOG.debug("Connected to CountourLinkNext: " + connected);

    }


    // public void communicateWithPump()
    // {
    // MedtronicCnl1Reader reader = new
    // MedtronicCnl1Reader(this.communicationHandler, session);
    //
    // try
    // {
    // reader.sendResetToDevice();
    //
    // reader.requestDeviceInfo();
    //
    // reader.enterControlMode();
    // //
    // // try
    // // {
    // reader.enterPassthroughMode();
    // reader.openConnection();
    //
    // long pumpTime = reader.getPumpTime().getTime();
    // // long pumpOffset = pumpTime - System.currentTimeMillis();
    // // LOG.debug("Time offset between pump and device: " + pumpOffset +
    // // "
    // // millis.");
    //
    // // reader.requestReadInfo();
    //
    // }
    // catch (Exception e)
    // {
    // e.printStackTrace();
    // }
    // finally
    // {
    // // try
    // // {
    // // reader.closeConnection();
    // // reader.endPassthroughMode();
    // // reader.endControlMode();
    // // }
    // // catch (Exception e)
    // // {
    // // e.printStackTrace();
    // // }
    // close();
    // }
    //
    // // initConnectorDevice();
    // //
    // // MedtronicCnlReader cnlReader = new
    // // MedtronicCnlReader(this.communicationHandler);
    // //
    // // try
    // // {
    // // LOG.debug("Connecting to the Contour Next Link.");
    // // cnlReader.requestDeviceInfo();
    // //
    // // // // Is the device already configured?
    // // // ContourNextLinkInfo info = realm.where(ContourNextLinkInfo.class)
    // // // .equalTo("serialNumber", cnlReader.getStickSerial()).findFirst();
    // //
    // // // if (info == null)
    // // // {
    // // // // TODO - use realm.createObject()?
    // // // info = new ContourNextLinkInfo();
    // // // info.setSerialNumber(cnlReader.getStickSerial());
    // // //
    // // // }
    // //
    // // cnlReader.getPumpSession().setStickSerial(cnlReader.getStickSerial());
    // //
    // // cnlReader.enterControlMode();
    // //
    // // try
    // // {
    // // cnlReader.enterPassthroughMode();
    // // cnlReader.openConnection();
    // // cnlReader.requestReadInfo();
    // //
    // // // String key = info.getKey();
    // //
    // // // if (key == null)
    // // // {
    // // // cnlReader.requestLinkKey();
    // // //
    // // //
    // //
    // info.setKey(MessageUtils.byteArrayToHexString(cnlReader.getPumpSession().getKey()));
    // // // key = info.getKey();
    // // // }
    // //
    // // cnlReader.requestLinkKey();
    // //
    // // //
    // //
    // cnlReader.getPumpSession().setKey(MessageUtils.hexStringToByteArray(key));
    // //
    // // long pumpMAC = cnlReader.getPumpSession().getPumpMAC();
    // // LOG.info("PumpInfo MAC: " + (pumpMAC & 0xffffff));
    // // // MainActivity.setActivePumpMac(pumpMAC);
    // //
    // // byte radioChannel =
    // // cnlReader.negotiateChannel(activePump.getLastRadioChannel());
    // // if (radioChannel == 0)
    // // {
    // // LOG.info("Could not communicate with the 640g. Are you near the
    // // pump?");
    // // }
    // // else
    // // {
    // // activePump.setLastRadioChannel(radioChannel);
    // // sendStatus(String.format(Locale.getDefault(), "Connected to Contour
    // // Next Link on channel %d.",
    // // (int) radioChannel));
    // // LOG.debug(String.format("Connected to Contour Next Link on channel
    // // %d.", (int) radioChannel));
    // // cnlReader.beginEHSMSession();
    // //
    // // PumpStatusEvent pumpRecord = new PumpStatusEvent(); //
    // // realm.createObject(PumpStatusEvent.class);
    // //
    // // String deviceName = String.format("medtronic-640g://%s",
    // // cnlReader.getStickSerial());
    // // activePump.setDeviceName(deviceName);
    // //
    // // // TODO - this should not be necessary. We should reverse
    // // // lookup the device name from PumpInfo
    // // pumpRecord.setDeviceName(deviceName);
    // //
    // // long pumpTime = cnlReader.getPumpTime().getTime();
    // // long pumpOffset = pumpTime - System.currentTimeMillis();
    // // LOG.debug("Time offset between pump and device: " + pumpOffset + "
    // // millis.");
    // //
    // // // TODO - send ACTION to MainActivity to show offset between
    // // // pump and uploader.
    // // pumpRecord.setPumpTimeOffset(pumpOffset);
    // // pumpRecord.setPumpDate(new Date(pumpTime - pumpOffset));
    // // cnlReader.getPumpStatus(pumpRecord, pumpOffset);
    // // activePump.getPumpHistory().add(pumpRecord);
    // //
    // // cnlReader.endEHSMSession();
    // //
    // // boolean cancelTransaction = true;
    // // if (pumpRecord.getSgv() != 0)
    // // {
    // // // Check that the record doesn't already exist before
    // // // committing
    // // RealmResults<PumpStatusEvent> checkExistingRecords =
    // // activePump.getPumpHistory().where()
    // // .equalTo("eventDate", pumpRecord.getEventDate()).equalTo("sgv",
    // // pumpRecord.getSgv())
    // // .findAll();
    // //
    // // // There should be the 1 record we've already added in
    // // // this transaction.
    // // if (checkExistingRecords.size() <= 1)
    // // {
    // // realm.commitTransaction();
    // // cancelTransaction = false;
    // // }
    // //
    // // }
    // //
    // // }
    // // }
    // // catch (UnexpectedMessageException e)
    // // {
    // // LOG.error("Unexpected Message: {}", e.getMessage(), e);
    // // }
    // // catch (NoSuchAlgorithmException e)
    // // {
    // // LOG.error("Could not determine CNL HMAC: {}", e.getMessage(), e);
    // // }
    // // finally
    // // {
    // // // TODO : 05.11.2016 has the close to be here?
    // // cnlReader.closeConnection();
    // // cnlReader.endPassthroughMode();
    // // cnlReader.endControlMode();
    // // }
    // // }
    // // catch (IOException e)
    // // {
    // // LOG.error("Error connecting to Contour Next Link.", e);
    // // }
    // // catch (ChecksumException e)
    // // {
    // // LOG.error("Checksum error getting message from the Contour Next
    // // Link.", e);
    // // }
    // // catch (EncryptionException e)
    // // {
    // // LOG.error("Error decrypting messages from Contour Next Link.", e);
    // // }
    // // catch (TimeoutException e)
    // // {
    // // LOG.error("Timeout communicating with the Contour Next Link.", e);
    // // }
    // // catch (UnexpectedMessageException e)
    // // {
    // // LOG.error("Could not close connection.", e);
    // // }
    //
    // }

    public void close()
    {
        communicationHandler.disconnectDevice();
    }


    public void communicateWithPumpHandler()
    {
        MinimedCommunicationContourNext2 communicationContourNext = new MinimedCommunicationContourNext2(null, null);

    }


    // public void createPacket()
    // {
    // ContourNextLinkBinaryMessage message = new ContourNextLinkBinaryMessage(
    // ContourNextLinkBinaryMessage.CommandType.OPEN_CONNECTION, this.session,
    // null);
    //
    // }

    public static void main(String[] args)
    {
        SimpleReadContourNext reader = null;
        try
        {
            reader = new SimpleReadContourNext();
            reader.initConnectorDevice();
            // reader.communicateWithPump();

        }
        catch (Exception ex)
        {
            LOG.error("Error: {}", ex.getMessage(), ex);

        }
        finally
        {
            if (reader != null)
                reader.close();
        }

    }

}
