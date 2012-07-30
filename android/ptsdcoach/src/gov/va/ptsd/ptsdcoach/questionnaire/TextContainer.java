package gov.va.ptsd.ptsdcoach.questionnaire;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class TextContainer extends Node {
	
	static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{([a-zA-Z0-9_]*)\\}");
	
	public String getText(AbstractQuestionnairePlayer ctx) {
		if (subnodes == null) return null;

		String text = null;
		
		// First look for something with our specific locale.
		String locale = (ctx == null) ? null : ctx.getLocale();
		if (locale != null) {
			for (int i=0;i<subnodes.length;i++) {
				Node n = subnodes[i];
				if (n instanceof Text) {
					Text t = (Text)n;
					if (locale.equals(t.getLocale())) {
						text = t.getText();
					}
				}
			}
		}
		
		if (text == null) {
			// If that fails, look for something with no locale set
			for (int i=0;i<subnodes.length;i++) {
				Node n = subnodes[i];
				if (n instanceof Text) {
					Text t = (Text)n;
					if (t.getLocale() == null) {
						text = t.getText();
					}
				}
			}
		}

		if (text == null) {
			// If that fails, look for any text we can find
			for (int i=0;i<subnodes.length;i++) {
				Node n = subnodes[i];
				if (n instanceof Text) {
					Text t = (Text)n;
					text = t.getText();
				}
			}
		}

		if (text == null) return null;
		
		if (ctx != null) {
			// Substitute variables
			Matcher matcher = VARIABLE_PATTERN.matcher(text);
			while (matcher.find()) {
				String var = matcher.group(1);
				String value = ctx.getGlobalVariable(var);
				if (value == null) value = "(value of '"+var+"')";
				int start = matcher.start();
				int end = matcher.end();
				text = text.substring(0, start) + value + text.substring(end);
			}
		}
		
		return text;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes, QuestionnaireHandler handler)
			throws SAXException {
		if (localName.equals("text")) {
			Text n = new Text();
			n.setID(attributes.getValue("", "id"));
			n.setLocale(attributes.getValue("", "locale"));
			handler.pushNode(n);
		} else {
			super.startElement(uri, localName, qName, attributes, handler);
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		String s = new String(ch,start,length);
		if (getText(null) == null) {
			// No text has been placed in this container yet...
			// For convenience, create a new text node to hold this text
			// There will be no locale set for this created text node
			if (!SurveyUtil.isWhitespace(s)) {
				Text n = new Text();
				n.setText(s);
				addSubnode(n);
			}
		} else if (!SurveyUtil.isWhitespace(s)) {
			throw new SAXException("Text container already has a text node, but also contains text itself!");
		}
	}
}
