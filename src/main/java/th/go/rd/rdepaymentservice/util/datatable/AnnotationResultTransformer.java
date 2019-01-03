package th.go.rd.rdepaymentservice.util.datatable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;

import org.hibernate.transform.ResultTransformer;
import org.springframework.core.convert.ConversionService;

/**
 * 
 * @author Thanadee
 *
 */
public class AnnotationResultTransformer implements ResultTransformer {

	private static final long serialVersionUID = 1L;
	private final Class<?> clazz;
	private static final Map<Class<?>, ResultMapper> assignMap = new HashMap<Class<?>, ResultMapper>();

	private static final class ResultMapper {
		private static interface Assignable {
			public void assign(Object object, Object data) throws Exception;
		}

		private static final class MethodAssignable implements Assignable {
			private final Method method;

			public MethodAssignable(Method method) {
				this.method = method;
			}

			@Override
			public void assign(Object object, Object data) throws Exception {
				Object cData = StaticGlobalObject.getBean(ConversionService.class).convert(data,
						method.getParameterTypes()[0]);
				method.invoke(object, cData);
			}
		}

		private static final class FieldAssignable implements Assignable {
			private final Field field;

			public FieldAssignable(Field field) {
				this.field = field;
				this.field.setAccessible(true);
			}

			@Override
			public void assign(Object object, Object data) throws Exception {
				Object cData = StaticGlobalObject.getBean(ConversionService.class).convert(data, field.getType());
				field.set(object, cData);
			}
		}

		private Map<String, Assignable> resultMap = new HashMap<String, Assignable>();
		private Map<String, String> fieldNameToColName = new HashMap<>();
		private boolean needAllSource;
		private boolean needAllTarget;
		private Class<?> clazz;
		private int targetCount = 0;

		public ResultMapper(Class<?> clazz) {
			this.clazz = clazz;

			for (Class<?> clz = clazz; clz.getSuperclass() != null; clz = clz.getSuperclass()) {
				for (Field field : clz.getDeclaredFields()) {
					boolean isAnnotated = false;
					if (field.isAnnotationPresent(Column.class)) {
						Column col = field.getAnnotation(Column.class);
						String name = col.name();
						if (name.equals("")) {
							name = field.getName();
						}
						resultMap.put(name, new FieldAssignable(field));
						fieldNameToColName.put(field.getName(), name);
						isAnnotated = true;
					}
					if (isAnnotated) {
						targetCount++;
					}
				}
			}
			for (Method method : clazz.getMethods()) {
				boolean isAnnotated = false;
				if (method.isAnnotationPresent(Column.class)) {
					Column col = method.getAnnotation(Column.class);
					String name = col.name();
					if (name.equals("")) {
						name = method.getName();
					}
					resultMap.put(name, new MethodAssignable(getSetterFromGetter(clazz, method)));
					fieldNameToColName.put(getPropertyName(clazz, method), name);
					isAnnotated = true;
				}
				if (isAnnotated) {
					targetCount++;
				}
			}
		}

		public Object assignResult(String[] names, Object[] values) throws Exception {
			Constructor<?> constructor = clazz.getDeclaredConstructor();
			constructor.setAccessible(true);
			Object t = constructor.newInstance();
			int count = 0;
			for (int i = 0; i < names.length; ++i) {
				if (resultMap.containsKey(names[i])) {
					resultMap.get(names[i]).assign(t, values[i]);
					count++;
				} else {
					if (needAllSource) {
						throw new NotAllSourceMapped(names[i]);
					}
				}
			}
			if (needAllTarget && count != targetCount) {
				throw new RuntimeException("Transformer Not All Target Mapped");
			}
			return t;
		}
	}

	private static String getPropertyName(Class<?> clazz, Method method) {
		if (method.getName().indexOf("get") == 0) {
			return method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4);
		}
		return method.getName();
	}

	private static Method getSetterFromGetter(Class<?> clazz, Method method) {
		if (method.getName().indexOf("get") == 0) {
			Class<?> type = method.getReturnType();
			try {
				return clazz.getMethod("set" + method.getName().substring(3), type);
			} catch (NoSuchMethodException e) {
				System.err.println("AnnotationResultTransformer Can't select setter from getter");
				e.printStackTrace();
				throw new RuntimeException(e.getLocalizedMessage());
			} catch (SecurityException e) {
				System.err.println("AnnotationResultTransformer Can't select setter from getter");
				e.printStackTrace();
				throw new RuntimeException(e.getLocalizedMessage());
			}
		} else {
			return method;
		}
	}

	private static class NotAllSourceMapped extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public NotAllSourceMapped(String mes) {
			super(mes);
		}
	}

	private static class ChainedException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public ChainedException(Throwable t) {
			super(t);
		}
	}

	public AnnotationResultTransformer(Class<?> clazz) {
		this.clazz = clazz;
		synchronized (assignMap) {
			if (!assignMap.containsKey(clazz)) {
				assignMap.put(clazz, new ResultMapper(clazz));
			}
		}
	}

	public String getColumnNameFromPropertyName(String propertyName) {
		ResultMapper resultMapper = assignMap.get(clazz);
		return resultMapper.fieldNameToColName.get(propertyName);
	}

	@Override
	public Object transformTuple(Object[] tuple, String[] aliases) {
		ResultMapper resultMapper = assignMap.get(clazz);
		try {
			return resultMapper.assignResult(aliases, tuple);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new ChainedException(e);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List transformList(List collection) {
		return collection;
	}

}