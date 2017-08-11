package fileoperation;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

public class UnMap {
	public static void unmap(final Object buffer) throws Exception {
		AccessController.doPrivileged(new PrivilegedAction<Object>() {
			public Object run() {
				try {
					Method getCleanerMethod = buffer.getClass().getMethod("cleaner", new Class[0]);
					getCleanerMethod.setAccessible(true);
					sun.misc.Cleaner cleaner = (sun.misc.Cleaner) getCleanerMethod.invoke(buffer, new Object[0]);
					cleaner.clean();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		});
	}
}