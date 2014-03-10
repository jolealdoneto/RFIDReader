package rfidreader

import grails.transaction.Transactional

@Transactional
class RFIDService {
    def RFIDReader

    def readTags() {
        RFIDReader.allTags
    }

    def readRateTest() {
        RFIDReader.doReadRateTest()
    }
    def successRateTest(String tagId) {
        RFIDReader.doSuccessRateTest(tagId)
    }
}
