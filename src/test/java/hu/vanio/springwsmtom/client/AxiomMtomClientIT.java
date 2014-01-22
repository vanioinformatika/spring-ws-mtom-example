package hu.vanio.springwsmtom.client;

import org.junit.Test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AxiomMtomClientIT {

    @Test
    public void testLoad() throws Exception {
        ApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("/client.xml", AxiomMtomClientIT.class);

        AxiomMtomClient axiomClient = applicationContext.getBean("axiomClient", AxiomMtomClient.class);
        axiomClient.storeContent();
        axiomClient.loadContent();
    }

}
