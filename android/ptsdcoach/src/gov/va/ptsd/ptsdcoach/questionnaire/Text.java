package gov.va.ptsd.ptsdcoach.questionnaire;

import org.xml.sax.SAXException;

public class Text extends Node {
	String text;
	String locale;

	public void appendText(String newText) {
		if (text == null) text = newText;
		else text = text + newText;
	}
	
	public String getLocale() {
		return locale;
	}
	
	public String getText() {
		return text;
	}
	
	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException {
		String s = new String(ch,start,length);
		appendText(s);
	}

}
