package gov.va.ptsd.ptsdcoach.questionnaire;

public class FreeFormQuestion extends TextContainer {
	private int lines;

	public void setLines(int lines) {
		this.lines = lines;
	}
	
	public int getLines() {
		return lines;
	}
	
	@Override
	public Object evaluate(AbstractQuestionnairePlayer ctx) {
		ctx.addFreeformQuestion(id, getText(ctx), lines, true);
		return null;
	}
}
