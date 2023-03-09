package com.main


import com.main.data.MemberService
import com.main.template.MongoTemplateService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@EntityScan(basePackages = ["com.main"])
@ComponentScan(basePackages = ["com.main"])
class InvokeMember {

    /*@Autowired
    lateinit var memberService: MemberService*/


}
fun main(args: Array<String>){
    //runApplication<InvokeMember>(*args)
  /*  var memService: MemberService = runApplication<InvokeMember>(*args).getBean("MemberService") as MemberService
     var member = memService.getMember()*/
   // member.age="65"
   // print(memService.saveMember(member))
    var memService: MongoTemplateService = runApplication<InvokeMember>(*args).getBean("MongoTemplateService") as MongoTemplateService
    memService.getMember()
   // memService.saveMember()
    //memService.getMember()
}