package eu.kratochvil.jidlobot;

import org.jsoup.nodes.Document;

import java.io.IOException;

public interface JsoupConnector {
    Document getDocument(String url) throws IOException;
}