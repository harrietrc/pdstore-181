package nz.ac.auckland.se.genoupe.tools;

/**
 * 
 * Class L for lazy!
 * Prevents premature toString calls.
 * 
 * @author gweb017
 *
 */
public class L {
	
	private Object[] strings;
	private StringBuffer sb;
	
	public L(Object[] strings) {
		super();
		this.strings = strings;
	}

	/**
	 * lazy concatenation of strings and objects.
	 * ToString is only called on demand.
	 * 
	 * @param strings
	 * @return
	 */
	public static L s(Object... strings){
		
		L l = new L(strings);
		return l;
		
	}

	@Override
	public String toString() {
		if(sb!=null) return sb.toString();
		sb = new StringBuffer();
		for(Object st:strings){
			sb.append(st.toString());
		}
		return sb.toString();
	}
	
	

}
