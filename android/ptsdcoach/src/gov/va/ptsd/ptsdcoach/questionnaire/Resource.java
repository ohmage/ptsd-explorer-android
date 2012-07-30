package gov.va.ptsd.ptsdcoach.questionnaire;

public class Resource {
	private final String url;
	private int flags;
	private byte[] content;
	
	protected Resource(String url, int flags) {
		this.url = url;
		this.flags = flags;
	}
	
	public byte[] getContent() {
		return content;
	}
	
	public int getFlags() {
		return flags;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setContent(byte[] content) {
		this.content = content;
	}
	
}
