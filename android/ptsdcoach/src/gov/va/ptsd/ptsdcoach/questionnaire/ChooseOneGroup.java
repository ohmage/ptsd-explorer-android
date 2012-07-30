package gov.va.ptsd.ptsdcoach.questionnaire;

import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ChooseOneGroup extends Group {

	@Override
	public Node getSubnodeAfter(AbstractQuestionnairePlayer ctx, Node n) {
		return parent.getSubnodeAfter(ctx, this);
	}
	
	public Object evaluate(AbstractQuestionnairePlayer ctx) {
		Vector v = new Vector();
		for (int i=0;i<subnodes.length;i++) {
			if (subnodes[i].shouldEvaluate(ctx)) v.add(subnodes[i]);
		}
		int j = (int)(Math.random() * v.size());
		return v.get(j);
	}

}
