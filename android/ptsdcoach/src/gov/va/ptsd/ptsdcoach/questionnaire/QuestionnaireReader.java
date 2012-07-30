package gov.va.ptsd.ptsdcoach.questionnaire;

import java.io.InputStream;

public interface QuestionnaireReader {

	public Questionnaire read(InputStream in);
	
}
