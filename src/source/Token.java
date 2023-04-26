package source;

public class Token {
	int id;
	String tokenValue;
	int line;
	int column;
	String tokenType;
	int ref;//no use
	String annotation;//no use
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTokenValue() {
		return tokenValue;
	}
	public void setTokenValue(String tokenValue) {
		this.tokenValue = tokenValue;
	}
	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}
	public int getColumn() {
		return column;
	}
	public void setColumn(int column) {
		this.column = column;
	}
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	public int getRef() {
		return ref;
	}
	public void setRef(int ref) {
		this.ref = ref;
	}
	public String getAnnotation() {
		return annotation;
	}
	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}
	String[] getCSV_Title() {
		String a[]= {"ID","token value","line",
			"column","token type", "ref", "annotations"};
		return a;
		
	}
	String getCSV_Title_() {
		return "ID,token value,line,column,token type, ref, annotations";
	}
	
	String getProperties() {
		return this.id+","+this.tokenValue+","+this.line+","+this.column+","+this.tokenType+","+this.ref+","+this.annotation;
	}
	
	String[] getProperties_() {
		String r[] = {this.id+","+this.tokenValue+","+this.line+","+this.column+","+this.tokenType+","+this.ref+","+this.annotation};
		System.out.println(r[0]);
		return r;
	}
}
