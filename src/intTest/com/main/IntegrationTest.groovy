package com.main

import com.main.ApplicationKt
import com.main.template.MongoContainer
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification;

class IntegrationTest extends MongoContainer {


    def "IntegrationTest gfgfg"() {
        given:
        int i=0;

        when:
        int k=0;
        System.out.println("ello")

        then:
        System.out.println("hi")

    }

}
