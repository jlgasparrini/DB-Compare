package utils;

public class AttributeTable {
	String name;
	String type;
	
	public AttributeTable(String name, String type){
		this.name = name;
		this.type = type;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getType(){
		return this.type;
	}
	
	public boolean equals(Object o){
		if(o instanceof AttributeTable){
			AttributeTable tmp = (AttributeTable) o;
			if(tmp.name == this.name && tmp.type == this.type)
				return true;
		}
		return false;
	}
	
	public String toString(){
		return "("+this.name+", "+this.type+")";
	}
}
