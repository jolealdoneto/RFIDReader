package rfidreader

import grails.converters.JSON;

import com.alien.enterpriseRFID.reader.*;
import com.alien.enterpriseRFID.tags.*;

class ReadingController {
    def RFIDService

    def index() { }

    def readTags() {
        render RFIDService.readTags() as JSON
    }
}
