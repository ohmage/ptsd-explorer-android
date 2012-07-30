package gov.va.ptsd.ptsdcoach.questionnaire;

public class IntroScreen extends Screen {

	public void addButtons(AbstractQuestionnairePlayer ctx) {
		// Allow user to skip or defer the questionaire if it's not always asked.
		Settings theseSettings = ctx.getQuestionnaire().getSettings();
		float percentActivation = Float.parseFloat(theseSettings.getGlobal(ctx, Settings.VAR_PERCENT_ACTIVATION));
		if (percentActivation < 100) {
			ctx.addButton(AbstractQuestionnairePlayer.BUTTON_DEFER, theseSettings.getGlobal(ctx, Settings.VAR_DEFER_BUTTON));
			ctx.addButton(AbstractQuestionnairePlayer.BUTTON_SKIP, theseSettings.getGlobal(ctx, Settings.VAR_SKIP_BUTTON));
		}
		ctx.addButton(AbstractQuestionnairePlayer.BUTTON_DONE,theseSettings.getGlobal(ctx, Settings.VAR_PROCEED_BUTTON));
	}

}
