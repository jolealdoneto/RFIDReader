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
        ReadRateTest rrt = new ReadRateTest();
        rrt.performTest();

        //rrt.tagList()
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
            System.out.println(tagTable != null);
            List<Tag> tags = new ArrayList<Tag>(); //new ArrayList<Tag>(Arrays.asList(tagTable.getTagList()));
        }

        public ReadRateTest() {
            tagTable = new TagTable();
        }

        public void performTest() {
            MessageListenerService service = null;
            try {
                tagTable.setTagTableListener(this);

                // Set up the message listener service.
                // It handles streamed data as well as notifications.
                service = new MessageListenerService(4000); // 4000 será a porta
                service.setMessageListener(this);
                service.startService();
                System.out.println("Message Listener has Started");

                // Instantiate a new reader object, and open a connection to it on COM1
                AlienClass1Reader reader = new AlienClass1Reader();

                reader.setConnection(address, port);
                reader.setUsername("alien");
                reader.setPassword("password");

                reader.open();
                System.out.println("Configuring Reader ip: " + "150.164.200.3");

                reader.setNotifyAddress("150.164.200.3", 4000);
                reader.setNotifyFormat(AlienClass1Reader.XML_FORMAT); // Make sure service can decode it.
                reader.setNotifyTrigger("ADD"); // Notify whether there's a tag or not
                reader.setNotifyMode(AlienClass1Reader.ON);

                  // Set up AutoMode
                reader.autoModeReset();
                reader.setAutoStopTimer(1000); // Read for 1 second
                reader.setAutoMode(AlienClass1Reader.ON);

                // Close the connection and spin while messages arrive
                reader.close();
                long runTime = 10000; // milliseconds
                long startTime = System.currentTimeMillis();
                while(service.isRunning() && (System.currentTimeMillis()-startTime) < runTime) {
                    Thread.sleep(1000);
                }
            }
            finally {
                if (service != null) {
                    service.stopService();
                }
                if (reader != null) {
                    reader.open();
                    reader.autoModeReset();
                    reader.setTagStreamMode(AlienClass1Reader.OFF);
                    reader.close();
                }
            }
        }

        public void messageReceived(Message message){
          System.out.println("Mensagem recebida");
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
