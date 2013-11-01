package utils;

public class Constraint {

	private String name;
	private String type;
	private String constraint;
	
	public Constraint(String name, String type, String constraint) {
		this.name = name;
		this.type = type;
		this.constraint = constraint;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getType() {
		return type;
	}
	
	public String getConstraint() {
		return constraint;
	}
	
	public boolean equals(Constraint c){
		if (this.name.compareTo(c.getName()) == 0 && this.type.compareTo(c.getType()) == 0 && this.constraint.compareTo(c.getConstraint()) == 0)
			return true;
		return false;
	}
}
