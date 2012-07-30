package gov.va.ptsd.ptsdcoach.questionnaire;

import java.util.Hashtable;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class Settings extends Node {

	Hashtable texts = new Hashtable();

	static public final String VAR_NEXT_BUTTON = "nextButton";
	static public final String VAR_DONE_BUTTON = "doneButton";
	static public final String VAR_DEFER_BUTTON = "deferButton";
	static public final String VAR_PROCEED_BUTTON = "proceedButton";
	static public final String VAR_SKIP_BUTTON = "skipButton";
	static public final String VAR_TITLE = "title";
	static public final String VAR_NOTIFICATION = "notification";
	static public final String VAR_REMINDERS = "reminders";
	static public final String VAR_PERCENT_ACTIVATION = "percentToActivate";
	
	public Settings() {
		texts.put(VAR_NEXT_BUTTON, "Next");
		texts.put(VAR_DONE_BUTTON, "Done");
		texts.put(VAR_DEFER_BUTTON, "Ask me later");
		texts.put(VAR_PROCEED_BUTTON, "Ok");
		texts.put(VAR_SKIP_BUTTON, "No Thanks");
		texts.put(VAR_TITLE, "IQ Agent Questionnaire");
		texts.put(VAR_NOTIFICATION, "Please complete this questionnaire.");
		texts.put(VAR_REMINDERS, "1,5,10,15,60,120,180");
		texts.put(VAR_PERCENT_ACTIVATION, "100");
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes, QuestionnaireHandler handler)
			throws SAXException {

		TextContainer t = new TextContainer();
		t.setID(attributes.getValue("", "id"));
		texts.put(localName, t);
		handler.pushNode(t);
	}
	
	public String getGlobal(AbstractQuestionnairePlayer ctx, String varName) {
		Object o = texts.get(varName);
		if (o instanceof String) return (String)o;
		if (o instanceof TextContainer) return ((TextContainer)o).getText(ctx);
		return null;
	}

}
