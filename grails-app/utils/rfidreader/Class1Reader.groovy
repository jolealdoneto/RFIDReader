package rfidreader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.alien.enterpriseRFID.reader.*;
import com.alien.enterpriseRFID.tags.*;

public class Class1Reader implements RFIDReader {
    private AlienClass1Reader reader = null;

    public Class1Reader(String address, int port) {
        reader = new AlienClass1Reader();

        reader.setConnection(address, port);
        reader.setUsername("alien");
        reader.setPassword("password");
    }


    public List<Tag> getAllTags() throws AlienReaderException {
        reader.open();
        List<Tag> tags = new ArrayList<Tag>(Arrays.asList(reader.getTagList()));
        reader.close();
        
        return tags;
    }


    public void speedTest() {
  tagTable = new TagTable();
  tagTable.setTagTableListener(this);
  
  // Set up the message listener service.
  // It handles streamed data as well as notifications.
  MessageListenerService service = new MessageListenerService(4000);
  service.setMessageListener(this);
  service.startService();
  System.out.println("Message Listener has Started");

  // Instantiate a new reader object, and open a connection to it on COM1
  AlienClass1Reader reader = new AlienClass1Reader("COM1");
  reader.open();
  System.out.println("Configuring Reader");
  
  // Only deal with one antenna - we could theoretically get independent speed
  // data from each antenna and analyze it separately.
  reader.setAntennaSequence("0");
  
  // Very fast reads work best, and it only works with Gen2 tags.
  reader.setTagType(AlienClass1Reader.CLASS1GEN2);
  reader.setAcquireG2Cycles(1);
  reader.setAcquireG2Count(1);

  // Set up TagStream.
  // Use this host's IPAddress, and the port number that the service is listening on.
  // getHostAddress() may find a wrong (wireless) Ethernet interface, so you may
  // need to substitute your computers IP address manually.
  reader.setTagStreamAddress(InetAddress.getLocalHost().getHostAddress(), service.getListenerPort());
  // Need to use custom format to get speed.
  // We need at least the EPC, read time in milliseconds, and the speed
  String customFormatStr = "Tag:${TAGID}, Last:${MSEC2}, Speed:${SPEED}";
  reader.setTagStreamFormat(AlienClass1Reader.CUSTOM_FORMAT);
  reader.setTagStreamCustomFormat(customFormatStr);

  // Tell the static TagUtil class about the custom format, so it can decode the streamed data.
  TagUtil.setCustomFormatString(customFormatStr);

  // Tell the MessageListenerService that the data has a custom format.
  service.setIsCustomTagList(true);
  reader.setTagStreamMode(AlienClass1Reader.ON);

  // Set up AutoMode - use standard settings.
  reader.autoModeReset();
  reader.setAutoMode(AlienClass1Reader.ON);

  // Close the connection and spin while messages arrive
  reader.close();
  long runTime = 10000; // milliseconds
  long startTime = System.currentTimeMillis();
  do {
    Thread.sleep(1000);
  } while(service.isRunning() && (System.currentTimeMillis()-startTime) < runTime);
  
  // Reconnect to the reader and turn off AutoMode and TagStreamMode.
  System.out.println("\nResetting Reader");
  reader.open();
  reader.autoModeReset();
  reader.setTagStreamMode(AlienClass1Reader.OFF);
  reader.close();

    }

}
