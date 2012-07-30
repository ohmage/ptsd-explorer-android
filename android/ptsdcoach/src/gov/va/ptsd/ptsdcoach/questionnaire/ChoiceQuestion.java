package gov.va.ptsd.ptsdcoach.questionnaire;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ChoiceQuestion extends TextContainer {

	private int minSelectable;
	private int maxSelectable;
	
	public ChoiceQuestion() {
		maxSelectable = 1;
		minSelectable = 1;
	}
	
	public void setMinSelectable(int minSelectable) {
		this.minSelectable = minSelectable;
	}
	
	public void setMaxSelectable(int maxSelectable) {
		this.maxSelectable = maxSelectable;
	}
	
	public Choice[] getChoices() {
		int count = 0;
		for (int i=0;i<subnodes.length;i++) {
			if (subnodes[i] instanceof Choice) count++;
		}
		Choice[] c = new Choice[count];
		int j = 0;
		for (int i=0;i<subnodes.length;i++) {
			if (subnodes[i] instanceof Choice) {
				c[j++] = (Choice)subnodes[i];	
			}
		}
		return c;
	}
	
	@Override
	public Object evaluate(AbstractQuestionnairePlayer ctx) {
		ctx.addChoiceQuestion(id, getText(ctx), minSelectable, maxSelectable, getChoices());
		return null;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes, QuestionnaireHandler handler)
			throws SAXException {
		if (localName.equals("choice")) {
			Choice n = new Choice();
			n.setID(attributes.getValue("", "id"));
			n.setValue(attributes.getValue("", "value"));
			handler.pushNode(n);
		} else {
			super.startElement(uri, localName, qName, attributes, handler);
		}
	}
}
