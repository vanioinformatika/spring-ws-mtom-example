package hu.vanio.springwsmtom.client;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.activation.DataHandler;
import javax.activation.URLDataSource;
import javax.xml.transform.TransformerException;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMOutputFormat;
import org.apache.axiom.om.OMText;
import org.apache.axiom.soap.SOAPBody;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAPMessage;

import org.springframework.util.Assert;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceMessageExtractor;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.axiom.AxiomSoapMessage;
import org.springframework.ws.soap.axiom.AxiomSoapMessageFactory;

/**
 * Axiom based MTOM enabled WebService client
 *
 * @author Gyula Szalai <gyula.szalai@vanio.hu>
 */
public class AxiomMtomClient extends WebServiceGatewaySupport {

    /** Stopwatch to measure times */
    private final StopWatch stopWatch = new StopWatch(getClass().getSimpleName());

    /** Target namespace */
    private final String TNS = "http://www.springframework.org/spring-ws/samples/mtom";
    
    /**
     * Constructor
     * @param messageFactory The Axiom message factory
     */
    public AxiomMtomClient(AxiomSoapMessageFactory messageFactory) {
        super(messageFactory);
    }

    /**
     * Sends the test content file to the WebService
     */
    public void storeContent() {
        stopWatch.start("storeContent");
        getWebServiceTemplate().sendAndReceive(new WebServiceMessageCallback() {
            @Override
            public void doWithMessage(WebServiceMessage message) throws IOException, TransformerException {
                AxiomSoapMessage soapMessage = (AxiomSoapMessage) message;
                SOAPMessage axiomMessage = soapMessage.getAxiomMessage();
                SOAPFactory factory = (SOAPFactory) axiomMessage.getOMFactory();
                SOAPBody body = axiomMessage.getSOAPEnvelope().getBody();
                // Namespace node
                OMNamespace ns = factory.createOMNamespace(TNS, "tns");
                OMElement storeContentRequestElement = factory.createOMElement("StoreContentRequest", ns);
                body.addChild(storeContentRequestElement);
                OMElement nameElement = factory.createOMElement("Name", ns);
                storeContentRequestElement.addChild(nameElement);
                nameElement.setText(StringUtils.getFilename(ClientUtil.TEST_CONTENT_NAME));
                OMElement contentElement = factory.createOMElement("Content", ns);
                storeContentRequestElement.addChild(contentElement);
                
                DataHandler dataHandler = new DataHandler(new URLDataSource(ClientUtil.TEST_CONTENT_URL));
                OMText text = factory.createOMText(dataHandler, true);
                contentElement.addChild(text);

                OMOutputFormat outputFormat = new OMOutputFormat();
                outputFormat.setSOAP11(true);
                outputFormat.setDoOptimize(true);
                soapMessage.setOutputFormat(outputFormat);
            }
        }, new WebServiceMessageExtractor() {
            @Override
            public Object extractData(WebServiceMessage message) throws IOException, TransformerException {
                return null;
            }
        });
        stopWatch.stop();
        logger.info("Content stored");
        System.out.println(stopWatch.prettyPrint());
    }

    /**
     * Loads the test content from the WebService
     */
    public void loadContent() {
        
        File file = (File) getWebServiceTemplate().sendAndReceive(new WebServiceMessageCallback() {
            @Override
            public void doWithMessage(WebServiceMessage message) throws IOException, TransformerException {
                SOAPMessage axiomMessage = ((AxiomSoapMessage) message).getAxiomMessage();
                SOAPFactory factory = (SOAPFactory) axiomMessage.getOMFactory();
                SOAPBody body = axiomMessage.getSOAPEnvelope().getBody();
                // Namespace node
                OMNamespace ns = factory.createOMNamespace(TNS, "tns");
                OMElement loadContentRequestElement = factory.createOMElement("LoadContentRequest", ns);
                OMElement nameElement = factory.createOMElement("Name", ns);
                loadContentRequestElement.addChild(nameElement);
                nameElement.setText(ClientUtil.TEST_CONTENT_NAME);
                body.addChild(loadContentRequestElement);
                stopWatch.start("loadContent");
            }
        }, new WebServiceMessageExtractor<File>() {
            @Override
            public File extractData(WebServiceMessage message) throws IOException, TransformerException {
                stopWatch.stop();
                SOAPMessage axiomMessage = ((AxiomSoapMessage) message).getAxiomMessage();
                SOAPBody body = axiomMessage.getSOAPEnvelope().getBody();
                OMElement loadContentResponseElement = (OMElement) body.getChildElements().next();
                Assert.isTrue("LoadContentResponse".equals(loadContentResponseElement.getLocalName()));
                Iterator childElements = loadContentResponseElement.getChildElements();
                OMElement nameElement = (OMElement) childElements.next();
                Assert.isTrue("Name".equals(nameElement.getLocalName()));
                OMElement contentElement = (OMElement) childElements.next();
                Assert.isTrue("Content".equals(contentElement.getLocalName()));
                OMText text = (OMText) contentElement.getFirstOMChild();

                DataHandler dataHandler = (DataHandler) text.getDataHandler();
                File outFile = new File(System.getProperty("java.io.tmpdir"), "spring_mtom_jaxws_tmp.bin");
                
                stopWatch.start("loadAttachmentContent");
                long size = ClientUtil.saveContentToFile(dataHandler, outFile);
                System.out.println("Received file size [kB]: " + (size / 1024));
                stopWatch.stop();
                
                return outFile;
            }
        });
        logger.info("Content received");
        System.out.println(stopWatch.prettyPrint());
    }

}
