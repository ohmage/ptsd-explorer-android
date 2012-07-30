package gov.va.ptsd.ptsdcoach.questionnaire;

public class EqualsCondition extends Node {

	String variableName;
	String[] values;
	
	public EqualsCondition(String variable, String values) {
		this.variableName = variable;
		this.values = values.split(",");
	}
	
	public Object evaluate(AbstractQuestionnairePlayer q) {
		Object o = q.fetchAnswer(variableName);
		
		if (o == null) return null;

		if (o instanceof Object[]) {
			Object[] a = (Object[])o;
			for (int i=0;i<values.length;i++) {
				boolean isInThere = false;
				for (int j=0;j<a.length;j++) {
					if (values[i].equals(a[j])) {
						isInThere = true;
						break;
					}
				}
				
				if (!isInThere) return false;
			}
			
			if (values.length == a.length) return true;
			return false;
		}
		
		if (values[0].equals(o)) return true;
		return false;
	}
}
