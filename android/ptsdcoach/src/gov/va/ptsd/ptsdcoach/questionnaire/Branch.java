package gov.va.ptsd.ptsdcoach.questionnaire;

public class Branch extends And {

	String destination;
	
	public Object getDestination() {
		return destination;
	}
	
	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Object evaluate(AbstractQuestionnairePlayer ctx) {
		if (SurveyUtil.isTrue(super.evaluate(ctx))) {
			return ctx.getQuestionnaire().getNodeByID(destination);
		}
		
		return next(ctx);
	}
}
