package de.idnow.example.core.resource;

import java.util.HashMap;
import java.util.Map;

import de.idnow.example.core.entity.Entity;

public final class ResourceManager<T> {
	private static Map<Class, ResourceManager> map;
	private static Object mutex = new Object();
	private Map<Integer, T> resource = new HashMap<>();

	public Map<Integer, T> getResource() {
		return resource;
	}

	private static Map<Class, ResourceManager> getReourcesMap() {
		Map<Class, ResourceManager> result = map;
		if (result == null) {
			synchronized (mutex) {
				result = map;
				if (result == null) {
					result = map = new HashMap<>();
				}
			}
		}
		return result;
	}

	public static <T extends Entity> ResourceManager<T> get(Class<T> clazz) {
		Map<Class, ResourceManager> reourcesMap = getReourcesMap();
		ResourceManager<T> result = reourcesMap.get(clazz);
		if (result == null) {
			synchronized (mutex) {
				result = reourcesMap.get(clazz);
				if (result == null) {
					result = new ResourceManager<T>();
					reourcesMap.put(clazz, result);
				}
			}
		}
		return result;
	}

}
