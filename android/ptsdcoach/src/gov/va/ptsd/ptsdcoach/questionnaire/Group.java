package gov.va.ptsd.ptsdcoach.questionnaire;

import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class Group extends Node {
	public void startElement(String uri, String localName, String qName, Attributes attributes, QuestionnaireHandler handler) throws SAXException {
		if (localName.equals("screen")) {
			Screen n = new Screen();
			n.setID(attributes.getValue("", "id"));
			n.setTitle(attributes.getValue("", "title"));
			handler.pushNode(n);
		} else if (localName.equals("randomOrder")) {
			RandomOrderGroup n = new RandomOrderGroup();
			n.setID(attributes.getValue("", "id"));
			handler.pushNode(n);
		} else if (localName.equals("selectOne")) {
			ChooseOneGroup n = new ChooseOneGroup();
			n.setID(attributes.getValue("", "id"));
			handler.pushNode(n);
		} else {
			super.startElement(uri, localName, qName, attributes, handler);
		}
	}

}
