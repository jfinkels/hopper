package perseus.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Filterer<T> {
    public List<T> filter(List<T> input) {
	List<T> output = new ArrayList<T>();
	for (T item : input) {
	    if (include(item)) output.add(item);
	}
	
	return output;
    }
    
    public Set<T> filter(Set<T> input) {
	Set<T> output = new HashSet<T>();
	for (T item : input) {
	    if (include(item)) output.add(item);
	}
	
	return output;
    }

    public boolean include(T item) {
	return (item != null);   
    }
}
