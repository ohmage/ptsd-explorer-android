package gov.va.ptsd.ptsdcoach.questionnaire;

public class Or extends CompositeCondition {

	public Object evaluate(AbstractQuestionnairePlayer q) {
		if (subnodes == null) return false;
		for (int i=0;i<subnodes.length;i++) {
			if (SurveyUtil.isTrue(subnodes[i].evaluate(q))) return true;
		}
		return false;
	}
}
