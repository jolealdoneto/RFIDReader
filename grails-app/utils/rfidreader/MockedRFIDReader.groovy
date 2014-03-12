package rfidreader;

import java.util.List;

import com.alien.enterpriseRFID.reader.AlienReaderException;
import com.alien.enterpriseRFID.tags.Tag;

public class MockedRFIDReader implements RFIDReader {

	def generator = { String alphabet, int n ->
		new Random().with {
		  (1..n).collect { alphabet[ nextInt( alphabet.length() ) ] }.join()
		}
	  }
	
	def generateRandomTag() {
		def tag = new Tag();
		def alphabet = 
		tag.setTagID(generator(('A'..'Z').join(), 9))
		
		tag
	}
	
	@Override
	public List<Tag> getAllTags() throws AlienReaderException {
		(0..9).collect {
			generateRandomTag()
		}
	}

	@Override
	public Tag doReadRateTest(String tagId) {
        generateRandomTag()
	}

	@Override
	public double doSuccessRateTest(String tagId) {
        new Random().nextDouble()
	}

}
