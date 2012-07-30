package gov.va.ptsd.ptsdcoach.questionnaire;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

abstract public class UrlScanningHandler extends DefaultHandler {

	public UrlScanningHandler() {
	}
	
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		String url = attributes.getValue("", "url");
		if (url != null) {
			registerResource(url);
		}
	}
	
	abstract public void registerResource(String url);		
	
}
