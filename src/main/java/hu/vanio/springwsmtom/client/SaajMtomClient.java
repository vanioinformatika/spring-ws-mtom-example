package hu.vanio.springwsmtom.client;

import java.io.File;
import java.io.IOException;

import javax.activation.DataHandler;
import javax.activation.URLDataSource;

import org.springframework.util.ClassUtils;
import org.springframework.util.StopWatch;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

import hu.vanio.springwsmtom.wstypes.LoadContentRequest;
import hu.vanio.springwsmtom.wstypes.LoadContentResponse;
import hu.vanio.springwsmtom.wstypes.ObjectFactory;
import hu.vanio.springwsmtom.wstypes.StoreContentRequest;

/**
 * Simple SAAJ based MTOM enabled client
 * 
 * @author Gyula Szalai <gyula.szalai@vanio.hu>
 */
public class SaajMtomClient extends WebServiceGatewaySupport {

    /** JAXB object factory */
    private final ObjectFactory objectFactory = new ObjectFactory();

    /** Stopwatch to measure times */
    private final StopWatch stopWatch = new StopWatch(getClass().getSimpleName());

    /**
     * Constructor
     * @param messageFactory The SAAJ message factory
     */
    public SaajMtomClient(SaajSoapMessageFactory messageFactory) {
        super(messageFactory);
    }

    /**
     * Sends the test content file to the WebService
     */
    public void storeContent() {
        StoreContentRequest storeContentRequest = objectFactory.createStoreContentRequest();
        storeContentRequest.setName(ClientUtil.TEST_CONTENT_NAME);
        storeContentRequest.setContent(new DataHandler(new URLDataSource(ClientUtil.TEST_CONTENT_URL)));
        stopWatch.start("store");
        getWebServiceTemplate().marshalSendAndReceive(storeContentRequest);
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }

    /**
     * Loads the test content from the WebService
     * @throws IOException If an IO error occurs
     */
    public void loadContent() throws IOException {
        LoadContentRequest loadContentRequest = objectFactory.createLoadContentRequest();

        String tmpDir = System.getProperty("java.io.tmpdir");
        File outFile = new File(tmpDir, "spring_mtom_tmp.bin");
        
        long freeBefore = Runtime.getRuntime().freeMemory();
        
        stopWatch.start("load");
        LoadContentResponse loadImageResponse = (LoadContentResponse) getWebServiceTemplate().marshalSendAndReceive(loadContentRequest);
        stopWatch.stop();
        
        DataHandler content = loadImageResponse.getContent();
        
        long freeAfter = Runtime.getRuntime().freeMemory();
        logger.info("Memory usage [kB]: " + ((freeAfter - freeBefore)/1024));
        
        stopWatch.start("loadAttachmentContent");
        long size = ClientUtil.saveContentToFile(content, outFile);
        
        logger.info("Received file size [kB]: " + size);
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }

}
