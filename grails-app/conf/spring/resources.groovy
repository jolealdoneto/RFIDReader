import org.springframework.beans.factory.annotation.Autowired;

import rfidreader.MockedRFIDReader;
import rfidreader.Class1Reader;

// Place your Spring DSL code here
beans = {
	//RFIDReader(MockedRFIDReader) {}
	RFIDReader(Class1Reader, '150.164.9.34', 23) { bean ->
    }
}
