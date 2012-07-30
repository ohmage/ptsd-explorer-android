package gov.va.ptsd.ptsdcoach.questionnaire;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class QuestionnaireHandler extends DefaultHandler {

	Questionnaire questionnaire;
	Node[] stack = new Node[32];
	int stackDepth = 0;
	
	private static final int ELM_NONE = 0;
	private static final int ELM_TEXT = 1;
	private static final int ELM_CHOICE = 2;
	
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
	}
	
	public void pushNode(Node o) {
		stack[stackDepth++] = o;
	}

	public Node popNode() {
		return stack[--stackDepth];
	}

	public Node topNode() {
		if (stackDepth == 0) return null;
		return stack[stackDepth-1];
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if (stackDepth == 0) {
			if (!localName.equals("questionnaire")) throw new SAXException("Top level element must be 'questionnaire'");
			questionnaire = new Questionnaire();
			questionnaire.setID(attributes.getValue("", "id"));
			pushNode(questionnaire);
		} else {
			topNode().startElement(uri, localName, qName, attributes, this);
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		super.characters(ch, start, length);
		topNode().characters(ch, start, length);
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		Node n = popNode();
		if (n != questionnaire) {
			questionnaire.indexNode(n);
			topNode().addSubnode(n);
		}
		super.endElement(uri, localName, qName);
	}

	public Questionnaire getQuestionaire() {
		return questionnaire;
	}
}
