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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;
import org.eclipse.uml2.uml.Association;

/**
 * Framework for edit-helper advice to suppress creation of things in the
 * <em>New Relationship</em> menu.
 */
abstract class AbstractNewRelationshipAdvice extends AbstractEditHelperAdvice {

	/**
	 * Initializes me.
	 */
	public AbstractNewRelationshipAdvice() {
		super();
	}

	@Override
	public void configureRequest(IEditCommandRequest request) {
		super.configureRequest(request);

		if (request instanceof CreateRelationshipRequest) {
			configureCreateRelationshipRequest((CreateRelationshipRequest) request);
		}
	}

	/**
	 * Implemented by subclasses to optionally configure a "create relationship"
	 * {@code request}.
	 * 
	 * @param request the request to configure
	 */
	protected void configureCreateRelationshipRequest(CreateRelationshipRequest request) {
		// Pass
	}

	@Override
	public boolean approveRequest(IEditCommandRequest request) {
		if (request instanceof CreateRelationshipRequest) {
			return approveCreateRelationshipRequest((CreateRelationshipRequest) request);
		}
		return super.approveRequest(request);
	}

	/**
	 * Query whether a {@code request} to create a relationship should be permitted.
	 * 
	 * @param request a create-relationship request
	 * @return {@code false} if that {@code request} should be denied; {@code true}
	 *         otherwise
	 * 
	 * @see #approveCreateRelationshipRequest(EObject, EObject, IElementType)
	 */
	protected boolean approveCreateRelationshipRequest(CreateRelationshipRequest request) {
		boolean result = true;

		EObject source = request.getSource();
		EObject target = request.getTarget();
		IElementType relationship = request.getElementType();
		if ((source != null) && (target != null) && (relationship != null)) {
			result = approveCreateRelationshipRequest(source, target, relationship);
		}

		return result;
	}

	/**
	 * Query whether a {@code relationship} should be permitted to be created from a
	 * {@code source} to a {@code target}. Note that in the case of non-directed
	 * relationships (e.g., {@link Association}s), there is nonetheless an element
	 * that the user chose from which to draw it to another element.
	 * 
	 * @param source       the source element of the {@code relationship}
	 * @param target       the target element of the {@code relationship}
	 * @param relationship the type of relationship to be created
	 * 
	 * @return {@code false} if that request should be denied; {@code true}
	 *         otherwise
	 */
	protected abstract boolean approveCreateRelationshipRequest(EObject source, EObject target,
			IElementType relationship);

}
