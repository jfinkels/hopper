package perseus.ie.entity;

import java.util.Map;
import java.util.HashMap;

public abstract class AbstractDateParser implements DateParser {
    private Map<String,Object> parameters = new HashMap<String,Object>();

    public abstract Date parse(String dateString);
    public abstract DateRange parseRange(String rangeString);

    public Object getParameter(String key) {
	return parameters.get(key);
    }

    public void setParameter(String key, Object value) {
	parameters.put(key, value);
    }

    public boolean hasParameter(String key) {
	return parameters.containsKey(key);
    }
}
