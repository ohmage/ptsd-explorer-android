package gov.va.ptsd.ptsdcoach.questionnaire;

public class And extends CompositeCondition {

	public Object evaluate(AbstractQuestionnairePlayer q) {
		if (subnodes == null) return true;
		for (int i=0;i<subnodes.length;i++) {
			if (!SurveyUtil.isTrue(subnodes[i].evaluate(q))) return false;
		}
		return true;
	}
}
