package hu.vanio.springwsmtom.client;

import org.junit.Test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SaajMtomClientIT {

    @Test
    public void testLoad() throws Exception {
        ApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("/client.xml", SaajMtomClientIT.class);

        SaajMtomClient saajClient = applicationContext.getBean("saajClient", SaajMtomClient.class);
        saajClient.storeContent();
        saajClient.loadContent();
    }

}
