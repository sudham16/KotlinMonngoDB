package com.main.template

import com.main.data.Count
import com.main.data.CountFacet
import com.mongodb.bulk.BulkWriteResult
import org.bson.Document
import com.main.data.Error
import org.springframework.data.mongodb.core.BulkOperations
import org.springframework.data.mongodb.core.DefaultBulkOperations
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.util.Pair
import spock.lang.Specification

class TestMongoDBRepoSpec extends Specification {

    def mongoTemplate = Mock(MongoTemplate)
    def testMongoDBRepo = new TestMongodbRepo(mongoTemplate)
    def bulkoperations = Mock(DefaultBulkOperations)
    // def document=Mock(Document)


    def "Asert for Aggregation"() {
        given:
        def add = new Count(10, 25)
        def change = new Count(10, 25)

        def NoChange = new Count(10, 25)

        def autoCancel = new Count(10, 25)

        def countFacet = new CountFacet([add], [change], [NoChange], [autoCancel])
        def aggregationReult = new AggregationResults<CountFacet>([countFacet], new Document())
        mongoTemplate.aggregate(_, "testcollection", CountFacet.class) >> aggregationReult
        when:
        def result = testMongoDBRepo.getMember("prakash")

        then:
        System.out.println(result)

    }


    def "Asert for testQuery"() {
        given:
        def query = new Query()
        query.addCriteria(new Criteria("name").is("prakash"))
        def update = Update.update("status", "Complete")
        when:
        testMongoDBRepo.updateQueryTest()

        then:
        1 * mongoTemplate.updateMulti(query, update, "testcollection")

    }

    def "Asert for testQuery111"() {
        given:
        def error = new Error("1", "test error 1", "ADD")
        def error2 = new Error("2", "test error 2", "CHANGE")
        def errors = [error, error2]

        def pairs = new ArrayList<Pair<Query, Update>>()
        def criteria = new Criteria("name").is("prakash").and("kids").is(5)
        def query1 = new Query().addCriteria(
                criteria.and("transactionType.add").is(true))
        def update1 = Update.update("create", true).push("error", error)
        pairs.add(Pair.of(query1, update1))
        def query2 = new Query().addCriteria(
                criteria.and("transactionType.change").is(true))
        def update2 = Update.update("create", true).push("error", error2)
        pairs.add(Pair.of(query2, update2))
        when:
        testMongoDBRepo.updateAggregationPipe()

        then:
        1 * mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED, "testcollection") >> bulkoperations
        1 * bulkoperations.updateMulti(pairs) >> bulkoperations
        1 * bulkoperations.execute() >> Mock(BulkWriteResult)

    }

    def "adder fr tet query122"() {
        given:
        def error1 = new Error("1", "test error 1", "ADD")
        def error2 = new Error("2", "test error 2", "CHANGE")
        def errors = [error1, error2]
        def pairs = new ArrayList<Pair<Query, Update>>()

        for (error in errors) {
            def criteria = new Criteria("name").is("prakash").and("kids").is(5)
            if (error.thresholdType == "ADD") {
                criteria.and("transactionType.add").is(true)

            } else {
                criteria.and("transactionType.change").is(true)
            }

            def query = new Query().addCriteria(criteria)
            def update = Update.update("create", true).push("error", error)
            pairs.add(Pair.of(query, update))
        }
            println("test Cla*****8")
            print(pairs)

            when:
            testMongoDBRepo.updateAggregationPipe(errors)

            then:
            1 * mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED, "testcollection") >> bulkoperations
            1 * bulkoperations.updateMulti(pairs) >> bulkoperations
            1 * bulkoperations.execute() >> Mock(BulkWriteResult)

        }
}