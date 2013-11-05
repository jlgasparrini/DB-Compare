package utils;

public class TuplesOfStrings {
	
	private String[] tuple;
	private int size;
	
	/**
	 * Constructor de la clase
	 * @param size "determina el tamaño de la tupla"
	 */
	public TuplesOfStrings(int size){
		this.tuple = new String[size];
		this.size = size;
	}

	/**
	 * retorna el valor almacenado en la tupla en el 
	 * @param index
	 * @return
	 */
	public String getIndex(int index){
		if(0 < index && index <= this.size)
			return tuple[index];
		return null;
	}
	
	public boolean setIndex(int index, String value){
		if(0 < index && index <= this.size){
			tuple[index] = value;
			return true;
		}
		return false;
	}
}
