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

import static org.eclipse.papyrus.uml.service.types.utils.ElementUtil.isTypeOf;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.papyrus.uml.service.types.element.UMLElementTypes;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * Edit-helper advice for {@link Classifier}s as use-case subjects in the
 * <em>UML Light</em> context.
 */
public class ClassifierAsSubjectAdvice extends AbstractNewChildAdvice {

	/**
	 * Initializes me.
	 */
	public ClassifierAsSubjectAdvice() {
		super();
	}

	@Override
	protected void configureCreateRequest(CreateElementRequest request) {
		if ((request.getContainmentFeature() == null) //
				&& UMLPackage.Literals.CLASSIFIER.isInstance(request.getContainer()) //
				&& isTypeOf(request.getElementType(), UMLElementTypes.USE_CASE)) {

			// Can only create these as owned use cases
			request.setContainmentFeature(UMLPackage.Literals.CLASSIFIER__OWNED_USE_CASE);
		}
	}

	@Override
	protected boolean approveCreateRequest(CreateElementRequest request, EReference containment) {
		boolean result = true;

		if (isTypeOf(request.getElementType(), UMLElementTypes.ACTOR)) {
			// Never create actors as nested classifiers
			result = false;
		} else if (isTypeOf(request.getElementType(), UMLElementTypes.USE_CASE)) {
			// Use cases can only be created in the owned use cases of a class (exactly,
			// not some other kind of class such as a behavior)
			result = (request.getContainer().eClass() == UMLPackage.Literals.CLASS) //
					&& (containment == UMLPackage.Literals.CLASSIFIER__OWNED_USE_CASE);
		}

		return result;
	}

}
