package hu.vanio.springwsmtom;

import java.io.File;
import java.io.IOException;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import hu.vanio.springwsmtom.services.ContentRepository;
import hu.vanio.springwsmtom.wstypes.LoadContentRequest;
import hu.vanio.springwsmtom.wstypes.LoadContentResponse;
import hu.vanio.springwsmtom.wstypes.ObjectFactory;
import hu.vanio.springwsmtom.wstypes.StoreContentRequest;
import hu.vanio.springwsmtom.wstypes.StoreContentResponse;

/**
 * Endpoint implementation
 *
 * @author Gyula Szalai <gyula.szalai@vanio.hu>
 */
@Endpoint
public class MtomEndpoint implements MtomEndpointInterface {
    
    /** Logger */
    final static Logger logger = LoggerFactory.getLogger(MtomEndpoint.class);
    
    /** Target namespace */
    private static final String TNS = "http://www.springframework.org/spring-ws/samples/mtom";
    
    /** JAXB object factory */
    private final ObjectFactory objectFactory = new ObjectFactory();
    
    @Autowired
    private ContentRepository contentRepository;
    
    @PayloadRoot(localPart = "LoadContentRequest", namespace = TNS)
    @ResponsePayload
    @Override
    public LoadContentResponse loadContent(@RequestPayload LoadContentRequest loadContentRequest) {
        logger.info("loadContent start");
        LoadContentResponse resp = this.objectFactory.createLoadContentResponse();
        
        File contentFile = this.contentRepository.loadContent(loadContentRequest.getName());
        DataHandler dataHandler = new DataHandler(new FileDataSource(contentFile));
        resp.setName(loadContentRequest.getName());
        resp.setContent(dataHandler);
        return resp;
    }
    
    @PayloadRoot(localPart = "StoreContentRequest", namespace = TNS)
    @ResponsePayload
    @Override
    public StoreContentResponse storeContent(@RequestPayload StoreContentRequest storeContentRequest) throws IOException {
        logger.info("storeContent start");
        this.contentRepository.storeContent(storeContentRequest.getName(), storeContentRequest.getContent());
        StoreContentResponse resp = this.objectFactory.createStoreContentResponse();
        resp.setMessage("Success");
        return resp;
    }
    
}
