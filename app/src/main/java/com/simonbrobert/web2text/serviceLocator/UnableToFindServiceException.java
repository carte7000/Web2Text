package com.simonbrobert.web2text.serviceLocator;

public class UnableToFindServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UnableToFindServiceException(Class<?> service) {
		super("Le service " + service.getCanonicalName() + "n'a pas été enregistré.");
	}

}
