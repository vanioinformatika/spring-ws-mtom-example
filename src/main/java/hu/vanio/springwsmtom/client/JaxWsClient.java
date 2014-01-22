package hu.vanio.springwsmtom.client;

import java.io.File;
import java.io.IOException;

import javax.activation.DataHandler;
import javax.activation.URLDataSource;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.SOAPBinding;

import org.springframework.util.StopWatch;

import hu.vanio.springwsmtom.wstypes.ContentStoreHttpPort;
import hu.vanio.springwsmtom.wstypes.ContentStoreHttpPortService;
import hu.vanio.springwsmtom.wstypes.LoadContentRequest;
import hu.vanio.springwsmtom.wstypes.LoadContentResponse;
import hu.vanio.springwsmtom.wstypes.ObjectFactory;
import hu.vanio.springwsmtom.wstypes.StoreContentRequest;
import hu.vanio.springwsmtom.wstypes.StoreContentResponse;


/**
 * Simple JAX-WS MTOM enabled client
 *
 * @author Gyula Szalai <gyula.szalai@vanio.hu>
 */
public class JaxWsClient {

    /** JAXB object factory */
    private final ObjectFactory objectFactory = new ObjectFactory();
    
    /**
     * Sends the test content file to the WebService
     */
    public void storeContent() throws Exception {
        ContentStoreHttpPortService contentStoreService = new ContentStoreHttpPortService();
        ContentStoreHttpPort contentStorePort = contentStoreService.getContentStoreHttpPortSoap11();
        SOAPBinding binding = (SOAPBinding) ((BindingProvider) contentStorePort).getBinding();
        binding.setMTOMEnabled(true);

        StoreContentRequest request = objectFactory.createStoreContentRequest();
        request.setName(ClientUtil.TEST_CONTENT_NAME);
        
        DataHandler dataHandler = new DataHandler(new URLDataSource(ClientUtil.TEST_CONTENT_URL));
        request.setContent(dataHandler);
        
        StopWatch stopWatch = new StopWatch(this.getClass().getSimpleName());

        stopWatch.start("store");
        StoreContentResponse response = contentStorePort.storeContent(request);
        stopWatch.stop();
        
        System.out.println(stopWatch.prettyPrint());
    }
    
    /**
     * Loads the test content from the WebService
     * @throws IOException If an IO error occurs
     */
    public void loadContent() throws IOException {
        ContentStoreHttpPortService service = new ContentStoreHttpPortService();
        ContentStoreHttpPort loadContentPort = service.getContentStoreHttpPortSoap11();
        SOAPBinding binding = (SOAPBinding) ((BindingProvider) loadContentPort).getBinding();
        binding.setMTOMEnabled(true);

        LoadContentRequest request = objectFactory.createLoadContentRequest();
        request.setName(ClientUtil.TEST_CONTENT_NAME);
        StopWatch stopWatch = new StopWatch();

        stopWatch.start("load");
        LoadContentResponse response = loadContentPort.loadContent(request);
        stopWatch.stop();

        DataHandler content = response.getContent();
        File outFile = new File(System.getProperty("java.io.tmpdir"), "spring_mtom_jaxws_tmp.bin");

        stopWatch.start("loadAttachmentContent");
        long size = ClientUtil.saveContentToFile(content, outFile);
        
        System.out.println("Received file size [kB]: " + (size / 1024));
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());

    }

}
