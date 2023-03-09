package com.main.data

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("testcollection")
data class Member (
    @Id
    val id  : ObjectId = ObjectId.get(),
    val name: String,
    var age:String,
    val kids:Int?,
    val create:Boolean?,
    var address: Address?

)