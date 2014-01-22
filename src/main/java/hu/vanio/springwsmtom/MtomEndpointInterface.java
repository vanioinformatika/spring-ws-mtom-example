package hu.vanio.springwsmtom;

import java.io.IOException;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.server.endpoint.annotation.SoapAction;

import hu.vanio.springwsmtom.wstypes.LoadContentRequest;
import hu.vanio.springwsmtom.wstypes.LoadContentResponse;
import hu.vanio.springwsmtom.wstypes.StoreContentRequest;
import hu.vanio.springwsmtom.wstypes.StoreContentResponse;

/**
 * Endpoint interface
 * 
 * @author Gyula Szalai <gyula.szalai@vanio.hu>
 */
@Endpoint
public interface MtomEndpointInterface {

    /**
     * Handles LoadContentRequests
     * @param loadContentRequest The request
     * @return The LoadContentResponse
     */
    @SoapAction("")
    @ResponsePayload
    public LoadContentResponse loadContent(@RequestPayload LoadContentRequest loadContentRequest);

    /**
     * Handles StoreContentRequests
     * @param storeContentRequest The request
     * @return The StoreContentResponse
     * @throws java.io.IOException If an error occurs during storing the content
     */
    @SoapAction("")
    @ResponsePayload
    public StoreContentResponse storeContent(@RequestPayload StoreContentRequest storeContentRequest) throws IOException;
}
