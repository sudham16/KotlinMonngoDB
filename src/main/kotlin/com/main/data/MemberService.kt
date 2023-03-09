package com.main.data

import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service

@Service("MemberService")
class MemberService {

    @Autowired
    lateinit var memberRepo: MemberRepository

    fun getMember():Member{
        //var member = memberRepo.findById(ObjectId("62e3d3efe7bdacfef47a87ba"))
        var member = memberRepo.findByName("prakash")
        return member
    }
    fun saveMember(member: Member):Member{
        //var member = memberRepo.findById(ObjectId("62e3d3efe7bdacfef47a87ba"))
        var member1 = memberRepo.save(member)
        return member1
    }

}