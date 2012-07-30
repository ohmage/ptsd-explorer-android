package gov.va.ptsd.ptsdcoach.questionnaire;

import java.io.IOException;
import java.io.InputStream;

public class SurveyUtil {
	static public boolean isTrue(Object o) {
		if (o == null) return false;
		if (o instanceof Boolean) {
			Boolean b = (Boolean)o;
			return b.booleanValue();
		}
		
		return false;
	}
	
	static public boolean isWhitespace(String s) {
		for (int i=0;i<s.length();i++) {
			if (!Character.isWhitespace(s.charAt(i))) return false;
		}
		
		return true;
	}
	
	static public byte[] readAll(InputStream in) {
		try {
			byte[] buf = new byte[4096];
			int offset = 0;
			while (true) {
				int r = in.read(buf, offset, buf.length - offset);
				if (r == -1) break;
				offset += r;
				if (offset == buf.length) {
					byte[] a = new byte[buf.length*2];
					System.arraycopy(buf, 0, a, 0, buf.length);
					buf = a;
				}
			}

			byte[] a = new byte[offset];
			System.arraycopy(buf, 0, a, 0, offset);
			buf = a;
			in.close();
			return buf;
		} catch (Exception e) {
			return null;
		}
	}
	
	static public String answerToString(Object o) {
		if (o == null) return null;
		if (o instanceof String) return (String)o;
		if (o instanceof String[]) {
			String[] a = (String[])o;
			StringBuilder sb = new StringBuilder();
			for (int i=0;i<a.length;i++) {
				String answer = a[i];
				if (i>0) sb.append(",");
				sb.append(answer);
			}
			return sb.toString();
		}
		
		return null;
	}
}
