package gov.va.ptsd.ptsdcoach.questionnaire;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class Node {

	protected String id;
	protected Node parent;
	protected Node[] subnodes = null;
	
	public void setID(String id) {
		this.id = id;
	}
	
	public String getID() {
		return id;
	}

	public Node getParent() {
		return parent;
	}
	
	public void addSubnode(Node node) {
		if (subnodes == null) {
			subnodes = new Node[1];
			subnodes[0] = node;
		} else {
			Node[] a = new Node[subnodes.length+1];
			System.arraycopy(subnodes,0,a,0,subnodes.length);
			subnodes = a;
			subnodes[subnodes.length-1] = node;
		}
		
		node.parent = this;
	}
	
	public Node[] getSubnodes() {
		return subnodes;
	}
	
	public Node next(AbstractQuestionnairePlayer ctx) {
		if (parent == null) return null;
		return parent.getSubnodeAfter(ctx,this);
	}

	public Node getSubnodeAfter(AbstractQuestionnairePlayer ctx, Node n) {
		for (int i=0;i<subnodes.length-1;i++) {
			if (n == subnodes[i]) {
				return subnodes[i+1];
			}
		}
		
		return next(ctx);
	}

	public boolean shouldEvaluate(AbstractQuestionnairePlayer ctx) {
		return true;
	}
	
	public Object evaluate(AbstractQuestionnairePlayer ctx) {
		return next(ctx);
	}
	
	public void startElement(String uri, String localName, String qName, Attributes attributes, QuestionnaireHandler handler) throws SAXException {
		throw new SAXException("Invalid element type '"+localName+"' under "+this.toString());
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		String s = new String(ch,start,length);
		if (!SurveyUtil.isWhitespace(s)) {
			throw new SAXException("Invalid text present ('"+s+"') under "+this.toString());
		}
	}

}
