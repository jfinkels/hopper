package perseus.util;

public class Range<T extends Comparable<? super T>> {
    private T start = null;
    private T end = null;
    
    public static final Range<Integer> NONE = new Range<Integer>(null);
    
    public static Range<Integer> range(int start, int end) {
	return new Range<Integer>(start, end);
    }
    
    public Range(T start) {
	this(start, null);
    }
    
    public Range(T start, T end) {
	this.start = start;
	this.end = end;
    }
    
    public T getStart() {
	return start;
    }
    
    public T getEnd() {
	return end;
    }
    
    public boolean hasStart() {
	return (start != null);
    }
    
    public boolean hasEnd() {
	return (end != null);
    }
    
    public boolean includes(T value) {
	return (value.compareTo(start) >= 0 && value.compareTo(end) <= 0);
    }
}
