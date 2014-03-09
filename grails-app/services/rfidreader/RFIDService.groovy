package rfidreader

import grails.transaction.Transactional

@Transactional
class RFIDService {
    def RFIDReader

    def readTags() {
        RFIDReader.allTags
    }
}
