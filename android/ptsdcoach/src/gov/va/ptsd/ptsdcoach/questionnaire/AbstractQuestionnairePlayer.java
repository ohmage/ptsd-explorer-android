package gov.va.ptsd.ptsdcoach.questionnaire;

import com.openmhealth.ohmage.campaigns.va.ptsd_explorer.PclQuestionAnsweredEvent;
import com.openmhealth.ohmage.core.EventLog;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Hashtable;

abstract public class AbstractQuestionnairePlayer {

	public static final int BUTTON_NEXT = 1;
	public static final int BUTTON_DEFER = 2;
	public static final int BUTTON_SKIP = 3;
	public static final int BUTTON_DONE = 4;
	
	Questionnaire[] questionnaires;
	Hashtable[] answersByID;
	Hashtable userData = new Hashtable();
	Node currentNode = null;
	long triggerTime;
    private int currentQuestionnaire;

	public AbstractQuestionnairePlayer(Questionnaire... q) {
		questionnaires = q;
		currentQuestionnaire = 0;
		answersByID = new Hashtable[q.length];
	}
	
	public void setTriggerTime(long triggerTime) {
		this.triggerTime = triggerTime;
	}
	
	public void recordAnswer(String id, Object answer) {
		if (id.startsWith("pcl")) {
			int questionNum = Integer.parseInt(id.substring(3));
	        PclQuestionAnsweredEvent e = new PclQuestionAnsweredEvent();
	        e.pclNumberOfQuestionsAnswered = questionNum;
	        EventLog.log(e);
		}
		getAnswers().put(id, answer);
	}

	public Object fetchAnswer(String id) {
		return getAnswers().get(id);
	}

	public void setUserData(Object key, Object data) {
		userData.put(key, data);
	}

	public Object getUserData(Object key) {
		return userData.get(key);
	}
	
	public Hashtable getAnswers() {
	    if(answersByID[currentQuestionnaire] == null)
	        answersByID[currentQuestionnaire] = new Hashtable();
		return answersByID[currentQuestionnaire];
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
		return questionnaires[currentQuestionnaire];
	}

	public void play() {
	    playNode(getQuestionnaire().getSubnodes()[0]);
	}

	public void playIntro() {
		playNode(getQuestionnaire().getIntro());
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
		    if(currentQuestionnaire < questionnaires.length - 1) {
		        currentQuestionnaire++;
		        play();
		    }
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
