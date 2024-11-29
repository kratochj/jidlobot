package eu.kratochvil.jidlobot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * The RealJsoupConnector class is an implementation of the JsoupConnector interface.
 * It provides a method to fetch and return the HTML document from a given URL using Jsoup.
 */
@Component
public class RealJsoupConnector implements JsoupConnector{

    public Document getDocument(String url) throws IOException {
        return Jsoup.connect(url).get();
    }
}
