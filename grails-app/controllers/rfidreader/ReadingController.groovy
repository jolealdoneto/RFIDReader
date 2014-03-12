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

    def readRate() {
        def tagId = params.tagId

        println('controller: ' +  params)
        render RFIDService.readRateTest(params.tagId) as JSON
    }

    def successRate() {
        def tagId = params?.tagId

        render ([ result: RFIDService.successRateTest(tagId) ] as JSON)
    }

    def completeTest() {
        def tagId = params?.tagId

        render ([ result: RFIDService.doCompleteTest(tagId) ] as JSON)
    }
}
