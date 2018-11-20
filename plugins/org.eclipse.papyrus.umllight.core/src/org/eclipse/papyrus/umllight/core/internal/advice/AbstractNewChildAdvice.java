/*****************************************************************************
 * Copyright (c) 2018 Christian W. Damus and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *		Christian W. Damus - Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.umllight.core.internal.advice;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.gmf.runtime.emf.core.util.PackageUtil;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;

/**
 * Framework for edit-helper advice to suppress creation of things in the
 * <em>New Child</em> menu.
 */
abstract class AbstractNewChildAdvice extends AbstractEditHelperAdvice {

	/**
	 * Initializes me.
	 */
	public AbstractNewChildAdvice() {
		super();
	}

	@Override
	public void configureRequest(IEditCommandRequest request) {
		super.configureRequest(request);

		if (request instanceof CreateElementRequest) {
			configureCreateRequest((CreateElementRequest) request);
		}
	}

	/**
	 * Implemented by subclasses to optionally configure a create {@code request}.
	 * 
	 * @param request the request to configure
	 */
	protected void configureCreateRequest(CreateElementRequest request) {
		// Pass
	}

	@Override
	public boolean approveRequest(IEditCommandRequest request) {
		if (request instanceof CreateElementRequest) {
			return approveCreateRequest((CreateElementRequest) request);
		}
		return super.approveRequest(request);
	}

	/**
	 * Query whether a {@code request} to create an element should be permitted.
	 * 
	 * @param request a create-element request
	 * @return {@code false} if that {@code request} should be denied; {@code true}
	 *         otherwise
	 * 
	 * @see #approveCreateRequest(CreateElementRequest, EReference)
	 */
	protected boolean approveCreateRequest(CreateElementRequest request) {
		boolean result = true;

		EReference containment = request.getContainmentFeature();
		if (containment == null) {
			// We'll be looking for the best-fit containment feature later
			containment = PackageUtil.findFeature(request.getContainer().eClass(),
					request.getElementType().getEClass());
		}

		if (containment != null) {
			result = approveCreateRequest(request, containment);
		}

		return result;
	}

	/**
	 * Query whether a {@code request} to create an element should be permitted.
	 * 
	 * @param request     a create-element request
	 * @param containment the containment reference specified by the {@code request}
	 *                    or else inferred as would be by an eventual creation
	 *                    command. Never {@code null}
	 * @return {@code false} if that {@code request} should be denied; {@code true}
	 *         otherwise
	 */
	protected abstract boolean approveCreateRequest(CreateElementRequest request, EReference containment);

}
