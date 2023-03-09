package com.main.template

import com.main.data.Count
import com.main.data.CountFacet
import com.main.data.Member
import com.mongodb.client.MongoClient
import org.bson.Document
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.query
import org.springframework.data.mongodb.core.query.*
import org.springframework.stereotype.Repository

import org.springframework.data.mongodb.core.aggregation.*
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Ceil;
import org.springframework.data.mongodb.core.aggregation.Aggregation.*

import kotlin.math.ceil
import org.springframework.data.mongodb.core.aggregation.Aggregation.count as countAlias

@Repository
class TestMongodbRepo_bkup(val mongoTemplate: MongoTemplate) {

    fun addFacet(transType: String, count1: String, totalMemberId: Int): Array<AggregationOperation> {
       // val match = match(Criteria("name").`is`("prakash"))
        val addmatch = match(Criteria(transType).`is`(true))
        val count = countAlias().`as`(count1)
        val projection =
            project(count1)
                .andExpression("ceil(($count1*100)/[0])",totalMemberId).`as`(count1+"percentage")
        return arrayOf( addmatch, count, projection)
    }

    fun getMember(name: String) {

        val facet1 = addFacet("transactionType.add", "addCount", 6)
        val facet2 = addFacet("transactionType.change", "changeCount", 5)
        val facet3 = addFacet("transactionType.nochange", "nochangeCount", 5)
        val facet4 = addFacet("transactionType.cancel", "cancelCount", 5)

        val match = match(Criteria("name").`is`("prakash"))
        val addmatch = match(Criteria("transactionType.add").`is`(true))
        val addCount = countAlias().`as`("addCount")

        val changematch = match(Criteria("transactionType.change").`is`(true))
        val changeCount = countAlias().`as`("changeCount")

/*        val facetDelete= facet(match,addmatch, countAlias().`as`("addCount")).`as`("facet1").
        and(match,changematch,changeCount).`as`("facet2").and(match,changematch,changeCount).`as`("facet3")*/

        val facetOriginal = facet(*facet1).`as`("addFacet")
            .and(*facet2).`as`("changeFacet")
            .and(*facet3).`as`("nochangeFacet")
            .and(*facet4).`as`("cancelFacet")
       // val facetProject = project("facet1,facet2,facet3,facet4").andExpression("setUnion(facet1,facet,facet3,facet4)") unwind()
        var aggregation = newAggregation(match,facetOriginal)
            //project("facet1,facet2,facet3,facet4").andExpression("setUnion(facet1,facet,facet3,facet4)"))
        //var aggregation = newAggregation(facetOriginal)

        var documents = mongoTemplate.aggregate(aggregation, "testcollection", Document::class.java)
        print(documents.mappedResults.size)
        documents.mappedResults.forEach { println(it) }
        val docs = documents.mappedResults.flatMap { it.values }

        //val query=Query().addCriteria(Member::name exists true and
        //and (Member::name isEqualTo name))
        //.where("name").`is`(name))
    }


    fun saveMember(member: Member) {
        val query = Query().addCriteria(Member::age gt "10")
        //val udate=Update().inc()
        //mongoTemplate.updateMulti(query)
        mongoTemplate.insert(member)
    }

    fun getAddCount() {
        var query = Query(Criteria("name").`is`("prakash").and("transactionType.add").`is`(true))
        //val output=  mongoTemplate.find(query,Document::class.java)
        val output = mongoTemplate.count(query, "testcollection")
        print(output)

    }

}