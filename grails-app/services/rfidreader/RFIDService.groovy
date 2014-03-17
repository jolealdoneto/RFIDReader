package rfidreader

import grails.transaction.Transactional

@Transactional
class RFIDService {
    def RFIDReader

    def readTags() {
        RFIDReader.allTags
    }

    def readRateTest(String tagId) {
        RFIDReader.doReadRateTest(tagId)
    }
    def successRateTest(String tagId) {
        RFIDReader.doSuccessRateTest(tagId)
    }

    def doCompleteTest(String tagId) {
        def readRateTest = RFIDReader.doReadRateTest(tagId);
        def readRate = 0
        if (readRateTest) {
            readRate = readRateTest.getRenewCount()
        }
        Thread.sleep(1000);
        def successRate = RFIDReader.doSuccessRateTest(tagId);

        [ readRate: readRate, successRate: successRate ]
    }
}
