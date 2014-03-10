package rfidreader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.alien.enterpriseRFID.notify.Message;
import com.alien.enterpriseRFID.notify.MessageListener;
import com.alien.enterpriseRFID.notify.MessageListenerService;
import com.alien.enterpriseRFID.reader.*;
import com.alien.enterpriseRFID.tags.*;

public class Class1Reader implements RFIDReader {
    private AlienClass1Reader reader = null;
    private String address;
    private int port;

    public Class1Reader(String address, int port) {
        reader = new AlienClass1Reader();
        this.address = address;
        this.port = port;

        Class1Reader.configureReader(reader, address, port);
    }

    public static void configureReader(AlienClass1Reader reader, String address, int port) {
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

    public List<Tag> doReadRateTest() {
        Class1Reader.ReadRateTest rrt = new Class1Reader.ReadRateTest();
        rrt.performTest();

        rrt.tagList()
    }

    public double doSuccessRateTest(String tagId) {
        // the idea is to simply get a specific tag for now and stick with it.
        // reading as many times for like 10s

        int runCounter = 0;
        int tagReadCounter = 0;
        long runTime = 10000; // milliseconds
        long startTime = System.currentTimeMillis();
        while(service.isRunning() && (System.currentTimeMillis()-startTime) < runTime) {
            runCounter++;
            if (this.getAllTags().find { it.tagId == tagId } != null) {
                tagReadCounter++;
            }
        }

        tagReadCounter / runCounter
    }

    class ReadRateTest implements MessageListener, TagTableListener {
        private TagTable tagTable;

        public List<Tag> getTagList() {
            List<Tag> tags = new ArrayList<Tag>(Arrays.asList(tagTable.getTagList()));
        }

        public ReadRateTest() {
            tagTable = new TagTable();
        }

        public void performTest() {
            tagTable.setTagTableListener(this);

            // Set up the message listener service.
            // It handles streamed data as well as notifications.
            MessageListenerService service = new MessageListenerService(4000); // 4000 será a porta
            service.setMessageListener(this);
            service.startService();
            System.out.println("Message Listener has Started");

            // Instantiate a new reader object, and open a connection to it on COM1
            AlienClass1Reader reader = new AlienClass1Reader();
            Class1Reader.configureReader(reader, this.Class1Reader.address, this.Class1Reader.port);
            reader.open();
            System.out.println("Configuring Reader");

            // Only deal with one antenna - we could theoretically get independent speed
            // data from each antenna and analyze it separately.
            // TODO: Ver se isso aqui é útil
            //reader.setAntennaSequence("0");

            // Very fast reads work best, and it only works with Gen2 tags.
            reader.setTagType(AlienClass1Reader.CLASS1GEN2);
            reader.setAcquireG2Cycles(1);
            reader.setAcquireG2Count(1);

            // Set up TagStream.
            // Use this host's IPAddress, and the port number that the service is listening on.
            // getHostAddress() may find a wrong (wireless) Ethernet interface, so you may
            // need to substitute your computers IP address manually.
            // TODO: Ver se endereço está sendo settado corretamente
            reader.setTagStreamAddress(InetAddress.getLocalHost().getHostAddress(), service.getListenerPort());
            System.out.println("IP Address: " + InetAddress.getLocalHost().getHostAddress());
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
            while(service.isRunning() && (System.currentTimeMillis()-startTime) < runTime) {
                Thread.sleep(1000);
            }

            // Reconnect to the reader and turn off AutoMode and TagStreamMode.
            System.out.println("\nResetting Reader");
            reader.open();
            reader.autoModeReset();
            reader.setTagStreamMode(AlienClass1Reader.OFF);
            reader.close();
        }

        public void messageReceived(Message message){
          for (int i=0; i < message.getTagCount(); i++) {
            Tag tag = message.getTag(i);

            // TagTable will automatically merge new information about an existing
            // tag, including calculating a smoothed speed and distance update.
            tagTable.addTag(tag);
            
            // After this merge is done, the TagTable will notify us with the final
            // data via the TagTableListener interface (tagAdded, tagRenewed, etc.).
          }
        }


        /**
         * Implements the TagTableListener interface.
         * 
         * When a TagTable is updated, it tells its TagTableListener via these methods.
         * We let the TagTable tell us about new tag reads, so that we get access to the
         * smoothed speed and distance values without having to manually look up in the
         * TagTable after   
         */
        public void tagAdded(Tag tag) {
          System.out.println("New Tag: " + tag.getTagID() + ", v0=" + DF2.format(tag.getSpeed()) + ", d0=" + DF2.format(tag.getSmoothPosition()));
        }
        public void tagRenewed(Tag tag) {
          System.out.println(tag.getTagID() + ", v=" + DF2.format(tag.getSmoothSpeed()) + ", d=" + DF2.format(tag.getSmoothPosition()));
        }
        public void tagRemoved(Tag tag) {}
    }

}
