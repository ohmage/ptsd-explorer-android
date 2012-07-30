package gov.va.ptsd.ptsdcoach.questionnaire.android;

import gov.va.ptsd.ptsdcoach.questionnaire.Resource;

public class AndroidResource extends Resource {

	private final QuestionnaireDatabase db;
	
	public AndroidResource(QuestionnaireDatabase db, String url, int flags) {
		super(url, flags);
		this.db = db;
	}

	public AndroidResource(QuestionnaireDatabase db, String url, int flags, byte[] content) {
		super(url, flags);
		this.db = db;
		super.setContent(content);
	}

	@Override
	public void setContent(byte[] content) {
		super.setContent(content);
		db.updateResource(this);
	}
}
