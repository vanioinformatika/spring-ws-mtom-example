package hu.vanio.springwsmtom.client;

import org.junit.Test;

public class JaxWsClientIT {

    @Test
    public void test() throws Exception {
        JaxWsClient jaxWsClient = new JaxWsClient();
        jaxWsClient.storeContent();
        jaxWsClient.loadContent();
    }

}
