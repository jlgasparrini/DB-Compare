package utils;

/**
 * Objeto: tuplas de tama�o N de tipo String
 * El rango de las tuplas va de 0 ... N-1
 */
public class TuplesOfStrings {
	
	private String[] tuple;
	/**
	 * Constructor de la clase
	 * @param size "determina la cantidad de elementos 
	 * que va a poder almacenar la tupla"
	 */
	public TuplesOfStrings(int size){
		this.tuple = new String[size];
	}

	/**
	 * retorna el valor almacenado en la tupla en el 
	 * @param index "rango de 0 ... N-1"
	 * @return
	 */
	public String getIndex(int index){
		if(0 <= index && index < this.tuple.length)
			return tuple[index];
		return null;
	}
	
	/**
	 * Almacena el Valor en la posicion index, en caso de rango 
	 * invalido devuelve false
	 * @param index "rango valido de 0 .. N-1"
	 * @param value "String"
	 * @return
	 */
	public boolean setIndex(int index, String value){
		if(0 <= index && index < this.tuple.length){
			tuple[index] = value;
			return true;
		}
		return false;
	}
	
	/**
	 * retorna la cantidad de elementos de la tupla
	 * @return "int de 0 .. N-1"
	 */
	public int length(){
		return this.tuple.length;
	}
	
	/**
	 * retorna true si el elemento "o" es igual
	 */
	public boolean equals(Object o){
		if(o instanceof TuplesOfStrings){
			TuplesOfStrings tmp = (TuplesOfStrings) o;
			if(tuple.length == tmp.length()){
				for (int i = 0; i < tuple.length; i++) {
					if(tuple[i] != tmp.getIndex(i))
						return false;
				}
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		String aux= "(";
		for (int i = 0; i < this.tuple.length; i++)
			aux+= this.getIndex(i)+", ";
		aux += ")";
		return aux;
	}
}
