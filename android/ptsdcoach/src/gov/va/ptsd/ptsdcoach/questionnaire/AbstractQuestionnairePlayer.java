package gov.va.ptsd.ptsdcoach.questionnaire;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

abstract public class AbstractQuestionnairePlayer {

	public static final int BUTTON_NEXT = 1;
	public static final int BUTTON_DEFER = 2;
	public static final int BUTTON_SKIP = 3;
	public static final int BUTTON_DONE = 4;
	
	Questionnaire questionnaire;
	Hashtable answersByID = new Hashtable();
	Hashtable userData = new Hashtable();
	Node currentNode = null;
	long triggerTime;	

	public AbstractQuestionnairePlayer(Questionnaire q) {
		questionnaire = q;
	}
	
	public void setTriggerTime(long triggerTime) {
		this.triggerTime = triggerTime;
	}
	
	public void recordAnswer(String id, Object answer) {
		answersByID.put(id, answer);
	}

	public Object fetchAnswer(String id) {
		return answersByID.get(id);
	}

	public void setUserData(Object key, Object data) {
		userData.put(key, data);
	}

	public Object getUserData(Object key) {
		return userData.get(key);
	}
	
	public Hashtable getAnswers() {
		return answersByID;
	}
	
	public String getGlobalVariable(String key) {
		if (key.equals("triggerTime")) {
			Calendar now = Calendar.getInstance();
			Calendar then = Calendar.getInstance();
			then.setTimeInMillis(triggerTime);
			DateFormat format = null;
			if (now.get(Calendar.DAY_OF_YEAR) == then.get(Calendar.DAY_OF_YEAR)) {
				format = DateFormat.getTimeInstance(DateFormat.LONG);
			} else {
				format = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.LONG);
			}
			return format.format(then.getTime());
		} else {
			return getQuestionnaire().getSettings().getGlobal(this, key);
		}
	}

	public String getLocale() {
		return "en";
	}
	
	public Questionnaire getQuestionnaire() {
		return questionnaire;
	}

	public void play() {
		playNode(questionnaire.getSubnodes()[0]);
	}

	public void playIntro() {
		playNode(questionnaire.getIntro());
	}

	public void playNode(Node n) {
		while (n != null) {
			currentNode = n;
			n = (Node)n.evaluate(this);
		}
	}
	
	public void nextPressed() {
		Node n = currentNode.next(this);
		if (n == null) {
			finish();
		} else {
			playNode(n);
		}
	}
	
	abstract public void beginScreen(String title);
	abstract public void addImage(String url);
	abstract public void addText(String text);
	abstract public void addFreeformQuestion(String id, String question, int lines, boolean mandatory);
	abstract public void addChoiceQuestion(String id, String question, int minChoices, int maxChoices, Choice[] choices);
	abstract public void addButton(int buttonType, String label);
	abstract public void showScreen();
	abstract public void finish();
	
	abstract public void donePressed();
	abstract public void deferPressed();
	abstract public void skipPressed();

}
