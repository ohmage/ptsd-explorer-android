package gov.va.ptsd.ptsdcoach.questionnaire;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class Screen extends Node {

	String title = null;
	And showOnCondition = null;
	
	@Override
	public Object evaluate(AbstractQuestionnairePlayer ctx) {
		if ((showOnCondition == null) || SurveyUtil.isTrue(showOnCondition.evaluate(ctx))) {
			ctx.beginScreen(title);
			for (int i=0;i<subnodes.length;i++) {
				subnodes[i].evaluate(ctx);
			}
			addButtons(ctx);
			ctx.showScreen();
			return null;
		} else {
			return next(ctx);
		}
	}

	public void addButtons(AbstractQuestionnairePlayer ctx) {
		ctx.addButton(AbstractQuestionnairePlayer.BUTTON_NEXT, ctx.getQuestionnaire().getSettings().getGlobal(ctx, Settings.VAR_NEXT_BUTTON));
	}
	
	@Override
	public boolean shouldEvaluate(AbstractQuestionnairePlayer ctx) {
		return ((showOnCondition == null) || SurveyUtil.isTrue(showOnCondition.evaluate(ctx)));
	}
	
	@Override
	public void addSubnode(Node node) {
		if (node == showOnCondition) return;
		super.addSubnode(node);
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes, QuestionnaireHandler handler) throws SAXException {
		if (localName.equals("info")) {
			Info n = new Info();
			n.setID(attributes.getValue("", "id"));
			handler.pushNode(n);
		} else if (localName.equals("image")) {
			Image n = new Image();
			n.setID(attributes.getValue("", "id"));
			n.setURL(attributes.getValue("", "url"));
			handler.pushNode(n);
		} else if (localName.equals("multi")) {
			String maxChoices = attributes.getValue("", "max");
			String minChoices = attributes.getValue("", "min");

			int max;
			if ("none".equals(maxChoices)) max = Integer.MAX_VALUE;
			else if (maxChoices == null) max = 1;
			else max = Integer.parseInt(maxChoices);

			int min;
			if (minChoices == null) min = 1;
			else min = Integer.parseInt(minChoices);

			ChoiceQuestion n = new ChoiceQuestion();
			n.setID(attributes.getValue("", "id"));
			n.setMaxSelectable(max);
			n.setMinSelectable(min);
			handler.pushNode(n);
		} else if (localName.equals("freeform")) {
			FreeFormQuestion n = new FreeFormQuestion();
			n.setID(attributes.getValue("", "id"));
			String linesStr = attributes.getValue("", "lines");
			int lines = 1;
			if (linesStr != null) lines = Integer.parseInt(linesStr);
			n.setLines(lines);
			handler.pushNode(n);
		} else if (localName.equals("condition")) {
			showOnCondition = new And();
			showOnCondition.setID(attributes.getValue("", "id"));
			handler.pushNode(showOnCondition);
		} else {
			super.startElement(uri, localName, qName, attributes, handler);
		}
	}

	public void setTitle(String value) {
		title = value;
	}
}
