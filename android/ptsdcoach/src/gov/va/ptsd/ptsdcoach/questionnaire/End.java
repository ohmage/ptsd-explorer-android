package gov.va.ptsd.ptsdcoach.questionnaire;

public class End extends Screen {
	
	public void addButtons(AbstractQuestionnairePlayer ctx) {
		ctx.addButton(AbstractQuestionnairePlayer.BUTTON_DONE,ctx.getQuestionnaire().getSettings().getGlobal(ctx, Settings.VAR_DONE_BUTTON));
	}
	
}
