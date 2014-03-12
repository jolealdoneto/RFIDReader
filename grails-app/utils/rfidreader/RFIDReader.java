package rfidreader;

import java.util.List;

import com.alien.enterpriseRFID.reader.AlienReaderException;
import com.alien.enterpriseRFID.tags.Tag;

public interface RFIDReader {
	public List<Tag> getAllTags() throws AlienReaderException;
    public Tag doReadRateTest(String tagId);
    public double doSuccessRateTest(String tagId);
}
