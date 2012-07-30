package gov.va.ptsd.ptsdcoach.questionnaire;

public class Image extends Node {

	String url = null;
	
	public String getURL() {
		return url;
	}

	public void setURL(String url) {
		this.url = url;
	}
	
	@Override
	public Object evaluate(AbstractQuestionnairePlayer ctx) {
		ctx.addImage(url);
		return null;
	}
	
}
