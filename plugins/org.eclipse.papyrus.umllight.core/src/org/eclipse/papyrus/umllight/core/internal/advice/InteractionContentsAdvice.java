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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.gmf.runtime.emf.core.util.PackageUtil;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Interaction;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * Edit-helper advice to suppress creation of features and other contents of
 * {@link Class}es that are not interesting in {@link Interaction}s in the
 * <em>UML Light</em> context.
 */
public class InteractionContentsAdvice extends AbstractEditHelperAdvice {

	/**
	 * Initializes me.
	 */
	public InteractionContentsAdvice() {
		super();
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
			EClass owner = containment.getEContainingClass();
			result = (owner == UMLPackage.Literals.INTERACTION) // Needed by the diagram editor
					|| (owner == UMLPackage.Literals.ELEMENT); // For owned comment
			// Exclude Behavior as owner: we don't want behavior parameters in UML Light
		}

		return result;
	}
}
