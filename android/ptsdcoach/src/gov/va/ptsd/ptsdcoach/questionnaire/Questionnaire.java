package gov.va.ptsd.ptsdcoach.questionnaire;

import java.util.Hashtable;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class Questionnaire extends Group {
	
	Screen intro = null;
	Settings settings = new Settings();
	Hashtable nodesByID = new Hashtable();

	public Questionnaire() {
	}

	public void indexNode(Node node) {
		if (node.getID() != null) nodesByID.put(node.getID(), node);
	}
	
	public Node getNodeByID(String id) {
		return (Node)nodesByID.get(id);
	}

	@Override
	public void addSubnode(Node node) {
		if (node == settings) return;
		if (node == intro) return;
		super.addSubnode(node);
	}
	
	public Settings getSettings() {
		return settings;
	}
	
	public Screen getIntro() {
		return intro;
	}
	
	public void startElement(String uri, String localName, String qName, Attributes attributes, QuestionnaireHandler handler) throws SAXException {
		if (localName.equals("branch")) {
			Branch n = new Branch();
			n.setID(attributes.getValue("", "id"));
			n.setDestination(attributes.getValue("", "destination"));
			handler.pushNode(n);
		} else if (localName.equals("intro")) {
			intro = new IntroScreen();
			intro.setID(attributes.getValue("", "id"));
			handler.pushNode(intro);
		} else if (localName.equals("end")) {
			End n = new End();
			n.setID(attributes.getValue("", "id"));
			handler.pushNode(n);
		} else if (localName.equals("globals")) {
			settings.setID(attributes.getValue("", "id"));
			handler.pushNode(settings);
		} else {
			super.startElement(uri, localName, qName, attributes, handler);
		}
	}

}
