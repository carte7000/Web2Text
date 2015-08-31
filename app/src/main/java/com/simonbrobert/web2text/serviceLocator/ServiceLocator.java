package com.simonbrobert.web2text.serviceLocator;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class ServiceLocator {

	private static ServiceLocator instance;
	private static final ReentrantLock verrou = new ReentrantLock();

	private HashMap<Class<?>, Object> services;

	public static ServiceLocator getInstance() {
		if (instance == null) {
			verrou.lock();
			try {
				if (instance == null) {
					instance = new ServiceLocator();
				}
			} finally {
				verrou.unlock();
			}
		}
		return instance;
	}

	public void remettreAZero() {
		verrou.lock();
		try {
			instance = null;
		} finally {
			verrou.unlock();
		}
	}

	private ServiceLocator() {
		services = new HashMap<Class<?>, Object>();
	}

	@SuppressWarnings("unchecked")
	public <T> T fetch(Class<T> service) {
		if (!services.containsKey(service)) {
			throw new UnableToFindServiceException(service);
		}

		return (T) services.get(service);

	}

	public <T> void register(Class<T> service, T implementation) {
		if (services.containsKey(service)) {
			throw new ServiceAlreadyRegisteredException(service);
		}
		services.put(service, implementation);
	}
}
