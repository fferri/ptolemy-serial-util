package ptolemy.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 *
 * @author Federico Ferri
 */
public class PropertiesPersistent {
	@Retention(RetentionPolicy.RUNTIME)
	@interface Getter {
		String property();
	}

	@Retention(RetentionPolicy.RUNTIME)
	@interface Setter {
		String property();
	}

	private Object fromString(String s, Class<?> c) {
		if(c.equals(String.class))
			return s;
		if(c.equals(Integer.class))
			return Integer.parseInt(s);
		if(c.equals(Double.class))
			return Double.parseDouble(s);
		return null;
	}

	public void loadFromProperties(Object o, Class<?> c, String fileName) throws IOException {
		Properties properties = new Properties();

		properties.load(new FileInputStream(fileName));

		for(Method m : c.getMethods()) {
			Setter anno = m.getAnnotation(Setter.class);
			if(anno == null) continue;

			try {
				String valueStr = properties.getProperty(anno.property());
				if(valueStr == null) continue; // property not found. go on.

				Class<?> paramType[] = m.getParameterTypes();
				if(paramType.length != 1) continue; // not a proper setter. ignore.

				Object value = fromString(valueStr, paramType[0]);
				m.invoke(o, new Object[] {value});
			} catch(IllegalAccessException e) {
			} catch(InvocationTargetException e) {
			}
		}
	}

	public void saveToProperties(Object o, Class<?> c, String fileName) throws FileNotFoundException, IOException {
		Properties properties = new Properties();

		for(Method m : c.getMethods()) {
			Getter anno = m.getAnnotation(Getter.class);
			if(anno == null) continue;

			try {
				Object value = m.invoke(o, new Object[]{});
				properties.setProperty(anno.property(), value.toString());
			} catch(IllegalAccessException e) {
			} catch(InvocationTargetException e) {
			}
		}

		properties.store(new FileOutputStream(fileName), null);
	}
}
