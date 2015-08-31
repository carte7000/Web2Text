package com.simonbrobert.web2text.serviceLocator;

public class ServiceAlreadyRegisteredException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ServiceAlreadyRegisteredException(Class<?> service) {
		super("Le service " + service.getCanonicalName() + " a déjà été enregistré.");
	}

}
