import org.springframework.beans.factory.annotation.Autowired;

import rfidreader.MockedRFIDReader;

// Place your Spring DSL code here
beans = {
	RFIDReader(MockedRFIDReader) {}
}
