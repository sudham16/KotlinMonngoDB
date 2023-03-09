package com.main.controller

import com.main.template.TestMongodbRepo
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class MongoCollectionController(val testMongodbRepo: TestMongodbRepo) {

    @DeleteMapping("/collection")
    fun deleteEmployeesById(@RequestParam queryMap: Map<String,String>){
        println(queryMap["date"])
        print(queryMap["days"])
        testMongodbRepo.deleteCollection(queryMap);
    }


}