package gov.va.ptsd.ptsdcoach.questionnaire;

public class ContainsCondition extends Node {

	String variableName;
	String[] values;
	
	public ContainsCondition(String variable, String values) {
		this.variableName = variable;
		this.values = values.split(",");
	}
	
	@Override
	public Object evaluate(AbstractQuestionnairePlayer q) {
		Object o = q.fetchAnswer(variableName);
		if (o == null) return null;
		if (o instanceof String[]) {
			String[] a = (String[])o;
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
			
			return true;
		} else if (o instanceof String) {
			String s = (String)o;
			for (int i=0;i<values.length;i++) {
				if (!s.contains((CharSequence)values[i])) return false;
			}
			
			return true;
		}
		
		return null;
	}
}
