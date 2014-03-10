package rfidreader;

import java.util.List;

import com.alien.enterpriseRFID.reader.AlienReaderException;
import com.alien.enterpriseRFID.tags.Tag;

public interface RFIDReader {
	public List<Tag> getAllTags() throws AlienReaderException;
    public List<Tag> doReadRateTest();
    public double doSuccessRateTest(String tagId);
}
