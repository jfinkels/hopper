package perseus.chunking;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import perseus.document.ChunkSchemes;

public class ChunkSchemeProcessor {
	
	List<ChunkState> states;
	
	// Some chunk schemes only apply to sub-documents. If the current
	// scheme is not applicable to the current position in the document,
	// the scheme is not active.
	boolean active = true;
	
	// The subtext variable is used to determine whether to activate
	// this processor.
	String subtextName = null;
	
	public static List<ChunkSchemeProcessor> getProcessors(ChunkSchemes schemes) {
		List<ChunkSchemeProcessor> processors =
			new ArrayList<ChunkSchemeProcessor>();
		for (String scheme : schemes.getSchemes()) {
			ChunkSchemeProcessor processor = new ChunkSchemeProcessor(scheme);
			processors.add(processor);
		}
		return processors;
	}
	
	/** Constructor for chunk schemes that apply to the whole document */
	public ChunkSchemeProcessor (String scheme) {
		states = new ArrayList<ChunkState>();
		setStates(scheme);
	}
	
	/** Constructor for chunk schemes that only apply to a sub-document */
	public ChunkSchemeProcessor (String scheme, String textName) {
		// subdoc schemes are not, by default, active
		active = false;
		subtextName = textName;
		
		// Create a state for the text element
		states = new ArrayList<ChunkState>();
		states.add(new ChunkState("text"));
		
		setStates(scheme);
	}
	
	private void setStates(String scheme) {
		StringTokenizer tokenizer = new StringTokenizer(scheme, ":");
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			states.add(new ChunkState(token));
		}
	}
	
	public String toString() {
		StringBuffer output = new StringBuffer();
		for (int i=0;i<states.size();i++) {
			ChunkState currentState = (ChunkState) states.get(i);
			output.append (currentState);
			output.append (" ");
		}
		return output.toString();
	}
	
	class ChunkState {
		public String type;
		public String value = null;
		public int depth;
		public int index;
		
		public ChunkState (String t) {
			type = t.replaceAll("[*+]", "");
			index = 0;
		}
		
		public boolean isSet() {
			return value == null;
		}
		
		public void set(String v) {
			value = v;
		}
		
		public void set(String v, int d) {
			value = v;
			depth = d;
		}
		
		public void clear() {
			value = null;
		}
		
		public boolean matches (String t) {
			if (type.equals(t)) {
				return true;
			}
			return false;
		}
		
		public void increment() {
			index++;
		}
		
		public int getIndex() {
			return index;
		}
		
		public String toString() {
			return type + "=" + value;
		}
	}
}
