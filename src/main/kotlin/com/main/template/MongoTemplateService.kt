package com.main.template
import com.main.data.Error
import com.main.data.Member
import org.springframework.stereotype.Service

@Service("MongoTemplateService")
class MongoTemplateService(val mongoTemplateRepository: MongoTemplateRepository,val testMongodbRepo:TestMongodbRepo) {


    fun getMember() {
        //var member = memberRepo.findById(ObjectId("62e3d3efe7bdacfef47a87ba"))
      var members = testMongodbRepo.getMember("prakash")
       // testMongodbRepo.testArrayError()
      //  println(members!!.addFacet.first().percentage)
        // members.forEach{ print(it) }
        // mongoTemplateRepository.getAddCount()
   // testMongodbRepo.getMemberssss("prakash","")
    /*    var error =  Error("1","test error 1","ADD")
        val  error2 = Error("2","test error 2","CHANGE")
        val lists = mutableListOf<Error>()
        lists.add(error)
        lists.add(error2)*/
     //  testMongodbRepo.updateAggregationPipe()

    }
    fun saveMember(){
        //var m=Member(name = "test",age="55", kids = 5)
        //mongoTemplateRepository.saveMember(m)
    }
}