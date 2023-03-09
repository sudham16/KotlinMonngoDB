package integration

import com.main.ApplicationKt
import com.main.template.MongoContainer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import spock.lang.Specification;

@SpringBootTest()
class IntegrationTest extends Specification {
  // static MongoContainer mongoContainer = new MongoContainer();
    @Autowired
    private MongoTemplate   mongoTemplate;

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
