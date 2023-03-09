package com.main.template

import com.main.data.CountFacet
import com.main.data.Error
import com.main.data.Member
import org.bson.Document
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.BulkOperations
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.*
import org.springframework.data.mongodb.core.aggregation.Aggregation.*
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators.Switch.CaseOperator
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.core.query.gt
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import org.springframework.data.mongodb.core.aggregation.Aggregation.count as countAlias
import org.springframework.data.util.Pair as UtilPair


@Repository
class TestMongodbRepo(val mongoTemplate: MongoTemplate) {
    private fun addAddressFacet( totalMemberId: Int, transType: String , geographic:String): Array<AggregationOperation> {


        var transCriteria = Criteria(transType).`is`(true)
       val match = match(transCriteria.andOperator(Criteria("fieldChangeSummary.address.changed").`is`(true)))
       //val unwind= unwind("fieldChangeSummary.address")
        //val match1 = match(Criteria("fieldChangeSummary.address.changed").`is`(true))
        val count = countAlias().`as`("count")
        val projection =
            project("count")
                .andExpression("round(((count*100)/[0]),2)", totalMemberId).`as`("percentage")
        return arrayOf(match, count, projection)
    }
    private fun addGeographicFacet( totalMemberId: Int, transType: String ,vararg geographic:String): Array<AggregationOperation> {
        var geoCriteria:Array<Criteria> = emptyArray()
        var transCriteria = Criteria(transType).`is`(true)
        for (c in geographic){
            geoCriteria += Criteria(c).`is`(true)
        }
        val addmatch = match(transCriteria.orOperator(*geoCriteria))
        return createAggregationOperations(totalMemberId, addmatch)
    }
    private fun addFacet( totalMemberId: Int,vararg transType: String ): Array<AggregationOperation> {
        var criteria:Array<Criteria> = emptyArray()
        for (c in transType){
            criteria += Criteria(c).`is`(true)
        }
        val addmatch = if (criteria.size == 1) {match(criteria.first())} else{
            match(Criteria().orOperator(  *criteria))

        }
        return createAggregationOperations(totalMemberId, addmatch)
    }


    private fun createAggregationOperations(
        totalMemberId: Int,
        match: MatchOperation
    ): Array<AggregationOperation> {
        val count = countAlias().`as`("count")
        val projection =
            project("count")
                .andExpression("round(((count*100)/[0]),2)", totalMemberId).`as`("percentage")
        return arrayOf(match, count, projection)
    }

    fun deleteCollection(queryMap: Map<String,String>) {/*
        val lt  = LocalDate.now()
        queryMap["days"]?.let { lt.minusDays(it.toLong()) }
        val localDate= LocalDate.parse(queryMap["date"])
        val byDate = localeDateToDate(localDate)
        val byDays =localeDateToDate(lt)
        val objectId=ObjectId.getSmallestWithDate(byDays)
        val query=Query().addCriteria(Criteria("_id").lte(objectId))
        mongoTemplate.remove(query,"testcol")*/

    }

    private fun localeDateToDate(localDate: LocalDate): Date? {
        val defaultZoneId = ZoneId.systemDefault()

        return Date.from(localDate.atStartOfDay(defaultZoneId).toInstant())
    }
/*    fun addfacetstats():Array<AggregationOperation>{
        val eunwind= unwind("persons")
        val count = countAlias().`as`("count")
    }*/

    fun testArrayError(){
        val project = project("error")
        val unwind =unwind("error")
        val match=match(Criteria().orOperator(  Criteria("error.errorCategory").`is`("1"),Criteria("error.errorCategory").`is`("2")))
        val count=countAlias().`as`("count")

        val group= group("error.errorCategory").count().`as`("groupCount")
        val project1=project("groupCount").and("error.errorCategory").previousOperation()
        val match1 = match(Criteria.where("name").`is`("prakash").and("age").`is`(55))
        val facetOriginal = facet(*arrayOf(project,unwind,match,count)).`as`("errorFacet").and(*arrayOf(project,unwind,match,group,project1)).`as`("errorFacet1")

        //val facetOriginal1 = facet(*arrayOf(project,unwind,match,group,project1)).`as`("errorFacet1")
        val aggregation = newAggregation(match1,facetOriginal)
        val doc = mongoTemplate.aggregate(aggregation,"testcollection", Document::class.java)
        print(doc.mappedResults)
        /*val aggregation = newAggregation(
          //  match(Criteria.where("name").`is`("prakash").and("age").`is`(55)),
            project("error"),
            unwind("error"),
            match(Criteria().orOperator(  Criteria("error.errorCategory").`is`("1"),Criteria("error.errorCategory").`is`("2"))),
            countAlias().`as`("count")
            *//*project()
                .and(
                    filter("error").`as`("error")
                        .by(valueOf("error.errorCategory").equalToValue("1"))

                ).`as`("error1"),
            group("error1").count().`as`("count")*//*
                   )
        val doc = mongoTemplate.aggregate(aggregation,"testcollection", Document::class.java)
        print(doc.mappedResults)*/
       // val match = match(Criteria("name").`is`("prakash").and("age").`is`(55))
       // val addfield = addFields().addField("error.errorCategory").withValue("2").build()
      //  val match = match(Criteria("error.errorCategory").`is`("1"))
        //val match = match(Criteria("name").`is`("prakash").and("age").`is`(55))
   /*     val project = project().and(filter("error")
            .`as`("error")
            .by(
                valueOf(
                "error.errorCategory")
                .equalToValue(
                    "1")))
            .`as`("count1")*/
        //val project1 = project().andExclude("_id")
       /* val count = countAlias().`as`("count")
        var aggregation = newAggregation(project,count )
        val doc = mongoTemplate.aggregate(aggregation,"testcollection", Document::class.java)
        print(doc.mappedResults)
*/


/*
        val query = Query()
        query.addCriteria(Criteria.where("name").`is`("prakash").and("age").`is`(55))
        query.fields().include("errorCategory")
            .elemMatch("error", Criteria.where("errorCategory").`is`("1"))
        val out= mongoTemplate.find(query, Errors::class.java)
       print(out)*/
    }
    fun getMember(name: String): CountFacet? {

        val facet1 = addFacet(totalMemberId = 6, "transactionType.add")
        val facet2 = addFacet(totalMemberId= 5,"transactionType.change" )
        val facet3 = addFacet( totalMemberId=  5,"transactionType.nochange")
        val facet4 = addFacet(totalMemberId = 5, "transactionType.cancel")
        val facet5 = addFacet(totalMemberId = 5, "transactionType.error", "transactionType.warn")
        val facet6 = addGeographicFacet(5,"transactionType.change","middleName.changed","initialName.changed" )
        val facet7 = addAddressFacet(5,"transactionType.change","")
        val match = match(Criteria("name").`is`("prakash"))

        val facetOriginal = facet(*facet1).`as`("addFacet")
            .and(*facet5).`as`("errorFacet")
            .and(*facet2).`as`("changeFacet")
            .and(*facet3).`as`("nochangeFacet")
            .and(*facet4).`as`("cancelFacet")
            .and(*facet6).`as`("geoFacet")
            .and(*facet7).`as`("addressFacet")

        // val facetProject = project("facet1,facet2,facet3,facet4").andExpression("setUnion(facet1,facet,facet3,facet4)") unwind()
        var aggregation = newAggregation(match, facetOriginal)


        var documents = mongoTemplate.aggregate(aggregation, "testcollection", CountFacet::class.java)
        val countFacet: CountFacet? = documents.mappedResults.first()
        print(countFacet)
        return countFacet
    }

    fun updateQueryTest(){

        val query = Query()
        query.addCriteria(Criteria("name").`is`("prakash"))
        val update = Update.update("status","Complete")
        val result = mongoTemplate.updateMulti(query,update,"testcollection")
    }
    fun updateAggregationPipe(errors:List<Error>){

        var pairs= mutableListOf<UtilPair<Query,Update>>()
       errors.forEach{
            val criteria= Criteria("name").`is`("prakash").and("kids").`is`(5)
            when(it.thresholdType){
                "ADD" ->   criteria.and("transactionType.add").`is`(true)
                "CHANGE" ->  criteria.and("transactionType.change").`is`(true)
            }
            val query = Query().addCriteria(criteria)
            val update=Update.update("create",true).push("error",it)
            pairs.add(UtilPair.of(query, update))
        }
        println("Main Cla*****8")
        print(pairs)
        /* errors.forEach {

         }*/
        var result = mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED,"testcollection").updateMulti(pairs).execute()
        print(result)
    }


    fun updateAggregationPipe(){
        val error =Error("1","test error 1","ADD")
        val error2 = Error("2","test error 2","CHANGE")
        var pairs= mutableListOf<UtilPair<Query,Update>>()
        var criteria= Criteria("name").`is`("prakash").and("kids").`is`(5)
        val query1 = Query().addCriteria(
            criteria.and("transactionType.add").`is`(true))
        val update1=Update.update("create",true).push("error",error)
        pairs.add( UtilPair.of(query1, update1))
        val query2 = Query().addCriteria(
            criteria.and("transactionType.change").`is`(true))
        val update2=Update.update("create",true).push("error",error2)
        pairs.add(UtilPair.of(query2, update2))

        var result = mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED,"testcollection").updateMulti(pairs).execute()
        print(result)
        //group("transactionType.add").accumulate(ScriptOperators.Accumulator.)
    }


    private fun pojoToDoc(): Array<Document> {
        val doc = Document()
        doc["errorCategory"] = "1"
        doc["errorName"] = "tet error"
        return arrayOf(doc)
    }

    fun updateMember(value: String, status: List<String>) {
        var criterias = mutableListOf<Criteria>()
        status.map { criterias.add(Criteria(it).`is`(true) ) }
        print("Size is "+criterias.size)
        val query = Query().addCriteria(
            Criteria("name").`is`(value).orOperator(
                *criterias.toTypedArray()
            ))
        val update = Update.update("status","Complete")
        val result = mongoTemplate.updateMulti(query,update,"testcollection")
        print(result)
    }
    fun updateMemwithAggregation(name: String){
        val query = Query().addCriteria(
            Criteria("name").`is`("prakash").and("kids").`is`(5))


        val aggregationUpdate =AggregationUpdate.update().set("status").toValue("Ready With Warnings")
            .set("error").toValueOf(
                ConditionalOperators.switchCases(
                    CaseOperator.`when`(ComparisonOperators.Eq.valueOf("transactionType.add").equalToValue(true)).then(
                        pojoToDoc()),
                    CaseOperator.`when`(ComparisonOperators.Eq.valueOf("transactionType.change").equalToValue(true)).then( pojoToDoc())

                ))


        // val update = Update.update("status","Ready With Warnings")
        //update.push("error",error1)
        val result = mongoTemplate.updateMulti(query,aggregationUpdate,"testcollection")
        print(result)
    }

    fun saveMember(member: Member) {
        val query = Query().addCriteria(Member::age gt "10")
        //val udate=Update().inc()
        //mongoTemplate.updateMulti(query)
        mongoTemplate.insert(member)
        //mongoTemplate.findOne()
    }
    fun getMemberssss(name:String,value: String):String {
        val query = Query()
        //var query = Query(Criteria("_id").`is`("62e3d3efe7bdacfef47a87ba"))
        query.addCriteria(Criteria("_id").`is`("62e3d3efe7bdacfef47a87ba").and("name").`is`(name)).fields().exclude("_id").include("name")
        val result = mongoTemplate.findOne(query,Document::class.java,"testcollection")
        return result["name"] as String
    }
    fun getAddCount() {
        var query = Query(Criteria("name").`is`("prakash").and("transactionType.add").`is`(true))
        //val output=  mongoTemplate.find(query,Document::class.java)
        val output = mongoTemplate.count(query, "testcollection")
        print(output)
        // mongoTemplate.findOne()
        //mongoTemplate.aggregate()

    }

}