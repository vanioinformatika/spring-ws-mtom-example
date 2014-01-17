package hu.vanio.springwsmtom;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.server.endpoint.annotation.SoapAction;

import hu.vanio.springwsmtom.xmltypes.LoadImageRequest;
import hu.vanio.springwsmtom.xmltypes.LoadImageResponse;

/**
 * Endpoint interface
 * 
 * @author Gyula Szalai <gyula.szalai@vanio.hu>
 */
@Endpoint
public interface LoadImageEndpointInterface {

    /**
     * Handles LoadImageRequests
     * @param loadImageRequest The request
     * @return The LoadImageResponse
     */
    @SoapAction("")
    @ResponsePayload
    public LoadImageResponse loadImage(@RequestPayload LoadImageRequest loadImageRequest);
}
