package hu.vanio.springwsmtom;

import java.net.URL;

import javax.activation.URLDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import hu.vanio.springwsmtom.xmltypes.LoadImageRequest;
import hu.vanio.springwsmtom.xmltypes.LoadImageResponse;

/**
 * Endpoint implementation
 *
 * @author Gyula Szalai <gyula.szalai@vanio.hu>
 */
@Endpoint
public class LoadImageEndpoint implements LoadImageEndpointInterface {
    
    final static Logger logger = LoggerFactory.getLogger(LoadImageEndpoint.class);
    
    private static final String TNS = "http://www.springframework.org/spring-ws/samples/mtom";
    
    @PayloadRoot(localPart = "LoadImageRequest", namespace = TNS)
    @ResponsePayload
    @Override
    public LoadImageResponse loadImage(@RequestPayload LoadImageRequest loadImageRequest) {
        logger.info("LoadImageEndpoint start");
        LoadImageResponse resp = new LoadImageResponse();
        URL imageUrl = Thread.currentThread().getContextClassLoader().getResource("spring-ws-logo.png");
        javax.activation.DataHandler dataHandler = new javax.activation.DataHandler(new URLDataSource(imageUrl));
        resp.setImage(dataHandler);
        return resp;
    }
}
