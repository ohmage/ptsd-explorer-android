package gov.va.ptsd.ptsdcoach.questionnaire;

import java.util.Vector;

public class RandomOrderGroup extends Group {

	public Node getSubnodeAfter(AbstractQuestionnairePlayer ctx, Node n) {
		Node[] children = (Node[])ctx.getUserData(this);
		for (int i=0;i<children.length-1;i++) {
			if (n == children[i]) {
				return children[i+1];
			}
		}
		
		return next(ctx);
	}

	public Object evaluate(AbstractQuestionnairePlayer ctx) {
		Vector v = new Vector();
		for (int i=0;i<subnodes.length;i++) {
			if (subnodes[i].shouldEvaluate(ctx)) v.add(subnodes[i]);
		}
		Node[] children = new Node[v.size()];
		for (int i=0;i<children.length;i++) {
			int j = (int)(Math.random() * v.size());
			children[i] = (Node)v.remove(j);
		}
		ctx.setUserData(this, children);
		
		return children[0];
	}

}
