package com.main.template

import com.main.data.Count
import com.main.data.CountFacet
import com.main.data.Member
import com.mongodb.client.MongoClient
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.GroupOperation
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.query
import org.springframework.data.mongodb.core.query.*
import org.springframework.stereotype.Repository
import com.mongodb.client.model.Aggregates.group
import com.mongodb.client.model.Aggregates.count
import com.mongodb.client.model.Facet
import org.bson.Document

import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.Aggregation.*
import org.springframework.data.mongodb.core.aggregation.Aggregation.count as countAlias
import org.springframework.data.mongodb.core.aggregation.FacetOperation
import org.springframework.data.mongodb.core.aggregation.MatchOperation

@Repository
 class MongoTemplateRepository(val mongoTemplate: MongoTemplate ){


    fun getMember(name:String){

        var facet= FacetOperation()


        val match = match(Criteria("name").`is`("prakash"))
        val addmatch=match(Criteria("transactionType.add").`is`(true))

        // val project=   Aggregation.project("addCount")

        val changematch = match(Criteria("transactionType.change").`is`(true))
        val changeCount=countAlias().`as`("changeCount")

        val facet1= facet(match,addmatch, countAlias().`as`("addCount")).`as`("facet1").
        and(match,changematch,changeCount).`as`("facet2").and(match,changematch,changeCount).`as`("facet3")

        var aggregation = newAggregation(facet1)
        var documents = mongoTemplate.aggregate(aggregation,"testcollection", Document::class.java)
        print(documents.mappedResults.size)
        print(documents.mappedResults)

        //val query=Query().addCriteria(Member::name exists true and
        //and (Member::name isEqualTo name))
        //.where("name").`is`(name))
    }
    fun saveMember(member: Member){
        val query=Query().addCriteria(Member::age gt  "10")
        //val udate=Update().inc()
        //mongoTemplate.updateMulti(query)
        mongoTemplate.insert(member)
    }

}