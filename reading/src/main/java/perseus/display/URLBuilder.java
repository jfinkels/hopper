/**
 * Utility class for constructing webpage links with numerous parameters. An
 * instance of this class keeps track of a URL and a set of parameters with
 * their values, and can produce a link on command. It also includes a method
 * to make life easier for callers that need several different links that each
 * differ in a single parameters, the withParameter() method.
 */
package perseus.display;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import perseus.util.StringUtil;

public class URLBuilder {
    private String url;
    private Map<String,Object> parameters;
	

    /**
     * Creates a new URLBuilder with no parameters, except those that may
     * already be part of the URL.
     *
     * @param url the target URL
     */
    public URLBuilder(String url) {
	this(url, Collections.emptyMap());
    }

    /**
     * Creates a new URLBuilder with the given URL and a map of parameters
     * and their values. The parameter values are not limited to Strings--they
     * need only have toString() methods that produce useful output--although
     * the getParameter() method will return a String representation of
     * the values.
     *
     * @param url the target URL
     * @param params a map (String, Object) of parameters
     */
    public URLBuilder(String url, Map params) {
	parameters = new HashMap<String,Object>(params);

	// Check for any parameters in the URL
	String[] urlTokens = url.split("\\?");
	if (urlTokens.length == 1) {
	    this.url = url;
	} else {
	    this.url = urlTokens[0];
	    String[] parameterTokens = urlTokens[1].split("&(amp;)?");
	    for (int i = 0; i < parameterTokens.length; i++) {
		String[] typeValue = parameterTokens[i].split("=");
		setParameter(typeValue[0], typeValue[1]);
	    }
	}
    }

    /**
     * Convenience method for constructing URLBuilders; given a request object
     * and an array of parameters, returns a URLBuilder consisting of the given
     * URI and all the given parameters, set to their values in the request
     * object.
     * 
     * @param request the request object to use
     * @param paramNames the parameters to include in the URLBuilder, or null
     * to include all parameters
     * @return a properly-initialized URLBuilder
     */
    public static URLBuilder fromRequest(HttpServletRequest request, String[] paramNames) {
	URLBuilder builder = new URLBuilder(request.getRequestURI());
	
	if (paramNames != null) {
		for (String param : paramNames) {
			builder.setParameter(param, request.getParameter(param));
		}
		return builder;
	}

		return new URLBuilder(request.getRequestURI(), request.getParameterMap());
    }
    
   /**
     * Sets the given parameter to the specified value. Returns a copy of
     * the URLBuilder to facilitate code like
     * builder.setParameter("a", "b").setParameter("a", "b").
     *
     * @param param the parameter to set
     * @param value the parameter's desired value
     * @return this URLBuilder, with the parameter set
     */
    public URLBuilder setParameter(String param, String value) {
	parameters.put(param, value);
	return this;
    }

    /**
     * Sets the given parameter to the specified integer value.
     * This is a convenience method that calls setParameter(String, String).
     */
    public URLBuilder setParameter(String param, int value) {
	return setParameter(param, Integer.toString(value));
    }

    /**
     * Sets the given parameter to the specified double value.
     * This is a convenience method that calls setParameter(String, String).
     */
    public URLBuilder setParameter(String param, double value) {
	return setParameter(param, Double.toString(value));
    }

    /**
     * Sets the given parameter to the specified list of string values.
     */
    public URLBuilder setParameter(String param, List<? extends Object> values) {
		parameters.put(param, values);
		return this;
    }

    /**
     * Sets the given parameter to the specified array of string values.
     */
    public URLBuilder setParameter(String param, String[] values) {
		parameters.put(param, Arrays.<String>asList(values));
		return this;
    }
    /**
     * Returns true if this URLBuilder has a value for the given parameter.
     *
     * @param param the parameter to check for
     * @return true if this URLBuilder has param, otherwise false
     */
    public boolean hasParameter(String param) {
	return parameters.containsKey(param);
    }

    /**
     * Returns the value of the given parameter as a String.
     *
     * @param param the parameter whose value should be returned
     * @return the parameter's value, or null if we don't have the parameter
     */
    public String getParameter(String param) {
	if (parameters.containsKey(param)) {
	    return parameters.get(param).toString();
	}
	return null;
    }

	/**
	 * getParameterList
	 *
	 * @param param the parameter whose value should be returned
	 * @return the parameter's value as a list
	 * @throws IllegalArgumentException if param's value is not a list
	 */
	public List<String> getParameterList(String param) {
		Object value = parameters.containsKey(param);
		if (!List.class.isAssignableFrom(value.getClass())) {
			throw new IllegalArgumentException(
				"parameter " + param + " is not stored as a list");
		}
		
		return (List<String>) value;
	}

	

    /**
     * Removes the given parameter.
     *
     * @param the parameter to remove
     * @return the parameter's value
     */
    public String removeParameter(String param) {
	if (parameters.containsKey(param)) {
	    return parameters.remove(param).toString();
	}
	return null;
    }

    /**
     * Returns the URL generated by the builder.
     */
    public String toString() {
	return toString(true);
    }

    /**
     * Returns the URL generated by the builder.
     *
     * @param escapeAmpersands if true, outputs HTML ampersand entities;
     *	if false, outputs plain old ampersands
     */
    public String toString(boolean escapeAmpersands) {

	StringBuilder buffer = new StringBuilder();
	buffer.append(url);
	if (parameters.isEmpty()) return buffer.toString();

	Set<String> valueSet = new HashSet<String>();

	buffer.append("?");
	for (Iterator it = parameters.keySet().iterator(); it.hasNext(); ) {
	    String parameter = (String) it.next();
	    Object value = (Object) parameters.get(parameter);

	    // [Http]ServletRequest's getParameterMap() method returns a
	    // Map with String arrays as its values, which don't print out
	    // very nicely when invoked with toString(). Check to see whether
	    // we're dealing with arrays; if we are, print them properly.
	    if (value instanceof Object[]) {
		for (Object valuePart : (Object[]) value) {
		    valueSet.add(formatValue(parameter, valuePart));
		}
	    } else if (value != null) {
		valueSet.add(formatValue(parameter, value));
	    }
	}

	String ampersand = (escapeAmpersands ? "&amp;" : "&");
	return String.format("%s?%s", url, StringUtil.join(valueSet, ampersand));
    }

    private String formatValue(String param, Object value) {
	return String.format("%s=%s", param, value);	
    }

    /**
     * Returns a copy of this URLBuilder with the specified parameter set to
     * the specified value. This method, like the other withParameter()
     * methods, are useful for producing a large number of links that differ in
     * only one parameter (in, for example, a pager) with minimal hassle.
     *
     * @param param the parameter whose value should be modified
     * @param value the new value for the modified parameter
     */
    public URLBuilder withParameter(String param, String value) {
	Map<String,Object> newParameters = new HashMap<String,Object>(parameters);
	newParameters.put(param, value);

	return new URLBuilder(url, newParameters);
    }

    /**
     * Returns a copy of this URLBuilder with the specified parameter set to
     * the value of the specified integer. A helper method for
     * withParamter(String, String).
     */
    public URLBuilder withParameter(String param, int value) {
	return withParameter(param, Integer.toString(value));
    }

    /**
     * Returns a copy of this URLBuilder with the specified parameter set to
     * the value of the specified double. A helper method for
     * withParamter(String, String).
     */
    public URLBuilder withParameter(String param, double value) {
	return withParameter(param, Double.toString(value));
    }
}
