package perseus.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Stringifier {
    public static String toString(Object obj) {
	return createString(obj).toString();
    }
    
    private static Writer createString(Object obj) {
	StringWriter writer = new StringWriter();
	PrintWriter printer = new PrintWriter(writer);
	
	Class klass = obj.getClass();
	printer.printf("%s <%d> {%n", klass.getName(), obj.hashCode() );
	Field[] fields = klass.getDeclaredFields();
	for (int i = 0; i < fields.length; i++) {
	    String name = fields[i].getName();
	    printer.printf("%16s -> ", name);
	    
	    String type = fields[i].getType().getSimpleName();
	    String value = "???";
	    try {
		value = fields[i].get(obj).toString();
	    } catch (IllegalAccessException iae) {
		// see if there's a getter method
		String methodName = "get"
		    + Character.toUpperCase(name.charAt(0))
		    + name.substring(1);
		Method method;
		try {
		    method = klass.getDeclaredMethod(methodName);
		    value = method.invoke(obj).toString();
		} catch (Exception e) {
		    // oh well
		}
	    }
	    printer.printf("%s <%s>%n", type, value);
	}
	
	printer.printf("}%n");
    	return writer;
    }
}
