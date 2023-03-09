package com.main.template

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.main.data.Count
import com.main.data.CountFacet
import com.main.data.Error
import com.main.data.Member
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.bulk.BulkWriteResult
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.bson.Document
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.core.BulkOperations
import org.springframework.data.mongodb.core.DefaultBulkOperations
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.util.Pair
import spock.lang.Specification

class TestMongoDBReplica extends MongoContainer {


 def List<Member> readJson(){
     ObjectMapper objectMapper = new ObjectMapper();
     ClassLoader classLoader = getClass().getClassLoader();
     File file = new File(classLoader.getResource("testcollection.json").getFile());
     return objectMapper.readValue(file, new TypeReference<List<Member>>(){});
      }

     MongoTemplate mongo() {
        ConnectionString connectionString = new ConnectionString(m1.getReplicaSetUrl());
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return new MongoTemplate(MongoClients.create(mongoClientSettings),"testgpt");
    }




    def "Asert for Aggregation"() {
        given:
        int i=0;
        MongoTemplate mongoTemplate=  mongo()
        mongoTemplate.insert(readJson(),"testgpt")

        when:
        Query query = new Query()
        query.addCriteria(Criteria.where("name").is("prakash").and());

        List<Member> memberList = mongoTemplate.findOne(query, Member.class)

        then:
        System.out.println("hi")

    }

}