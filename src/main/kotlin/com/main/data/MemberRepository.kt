package com.main.data

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface MemberRepository :MongoRepository<Member,String>{
    fun findById(id:ObjectId): Member

    @Query(value = "{'name': ?0}")
    fun findByName(name:String):Member
}