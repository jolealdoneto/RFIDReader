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
        def readRate = RFIDReader.doReadRateTest(tagId).getRenewCount();
        Thread.sleep(1000);
        def successRate = RFIDReader.doSuccessRateTest(tagId);

        [ readRate: readRate, successRate: successRate ]
    }
}
