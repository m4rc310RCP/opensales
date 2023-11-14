package foundation.cmo.opensales.graphql.services;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.reactivestreams.Publisher;
import org.springframework.security.core.context.SecurityContextHolder;

import foundation.cmo.opensales.graphql.security.dto.MUser;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

public class MFluxService {

	protected final MMultiRegitry<String, FluxSink<Object>> registry = new MMultiRegitry<>();

	@SuppressWarnings("unchecked")
	public <T> Publisher<T> publish(Class<T> type, Object key, T defaultValue) {
		String skey = makeId(type, key);
		return (Publisher<T>) Flux.create(
				fs -> registry.add(skey, fs.onDispose(() -> registry.remove(skey)).next(defaultValue)),
				FluxSink.OverflowStrategy.BUFFER);
	}

	public boolean inPublish(Class<?> type, Object key) {
		String skey = makeId(type, key);
		return registry.contains(skey);
	}

	public <T> void callPublish(Object key, T value) throws Exception {
		if (value == null) {
			throw new Exception("Value is null");
		}

		Class<?> type = value.getClass();

		if (!inPublish(type, key)) {
			throw new Exception("No published listener!");
		}
		
		String skey = makeId(type, key);
		registry.get(skey).forEach(sub -> sub.next(value));
	}
	
	public <T> void removePublish(Class<T> type, String key) {
		String skey = makeId(type, key);
		registry.remove(skey);
	}

	public <T> void callPublish(Class<T> type, Object key, T value) throws Exception {
		String skey = makeId(type, key);
		registry.get(skey).forEach(sub -> sub.next(value));
	}

	public MUser authenticatedUser() {
		try {
			return (MUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			return null;
		}
	}

	private String makeId(Class<?> type, Object key) {
		return String.format("%s-%s", type.getSimpleName(), key);
	}

	public void cloneAtoB(Object a, Object b) {

		List<Field> fas = listAllFields(a);
		List<Field> fbs = listAllFields(b);

		fas.forEach(fa -> {
			try {
				fa.setAccessible(true);
				fbs.forEach(fb -> {
					fb.setAccessible(true);
					if (fa.getName().equals(fb.getName())) {
						try {
							Object value = fa.get(a);
							if (Objects.nonNull(value)) {
								try {
									if (Objects.isNull(fb.get(b))){
										fb.set(b, value);
									}
								} catch (Exception e) {
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		// Class<?> typeA = listAllFields(a);
		// Class<?> typeB = b.getClass();

//		listAllFields(a).forEach(fieldA -> {
		// try {
//				for(Field fieldA : typeA.getDeclaredFields()) {
//					fieldA.setAccessible(true);
//					
//					for(Field fieldB : typeB.getDeclaredFields()) {
//						
//					}
//					
//					
//					//log.info("{}", field.get(a));
//				}
//				
//				
//				
//				Class<?> typeB = b.getClass();
//				
//				
//				
//				
//				Field fieldB = typeB.getField(fieldA.getName());
//				if (Objects.nonNull(fieldB) && Objects.isNull(fieldB.get(b))) {
//					fieldB.setAccessible(true);
//					fieldB.set(b, fieldA.get(a));
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		});
	}
	
	public Field getFieldFromType (Class<?> type, Class<?> typeField) {
		List<Field> fields = new ArrayList<>();
		fields.addAll(Arrays.asList(type.getFields()));
		fields.addAll(Arrays.asList(type.getDeclaredFields()));
		
		for(Field field: fields) {
			field.setAccessible(true);
			if(field.getType().getName().equals(typeField.getName())) {
				return field;
			}
		};
		
		return null;
	}
	

	private List<Field> listAllFields(Object o) {
		List<Field> fields = new ArrayList<>();
		Class<?> type = o.getClass();

		fields.addAll(Arrays.asList(type.getFields()));
		fields.addAll(Arrays.asList(type.getDeclaredFields()));

		return fields;
	}
}
