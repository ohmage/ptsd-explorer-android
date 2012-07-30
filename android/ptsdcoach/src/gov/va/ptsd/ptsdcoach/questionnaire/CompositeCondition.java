package gov.va.ptsd.ptsdcoach.questionnaire;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class CompositeCondition extends Node {

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes, QuestionnaireHandler handler)
			throws SAXException {
		if (localName.equals("or")) {
			Or n = new Or();
			n.setID(attributes.getValue("", "id"));
			handler.pushNode(n);
		} else if (localName.equals("and")) {
			And n = new And();
			n.setID(attributes.getValue("", "id"));
			handler.pushNode(n);
		} else if (localName.equals("contains")) {
			ContainsCondition n = new ContainsCondition(attributes.getValue("", "var"), attributes.getValue("", "value"));
			n.setID(attributes.getValue("", "id"));
			handler.pushNode(n);
		} else if (localName.equals("equals")) {
			EqualsCondition n = new EqualsCondition(attributes.getValue("", "var"), attributes.getValue("", "value"));
			n.setID(attributes.getValue("", "id"));
			handler.pushNode(n);
		} else {
			super.startElement(uri, localName, qName, attributes, handler);
		}
	}
}
