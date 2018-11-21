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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.papyrus.uml.service.types.element.UMLElementTypes;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * Edit-helper advice to suppress creation of unwanted contents of
 * {@link Behavior}s that are not interesting in the <em>UML Light</em> context
 * (for example, stuff inherited from {@link Class}).
 */
public class BehaviorContentsAdvice extends AbstractNewChildAdvice {

	/**
	 * Initializes me.
	 */
	public BehaviorContentsAdvice() {
		super();
	}

	@Override
	protected boolean approveCreateRequest(CreateElementRequest request, EReference containment) {
		boolean result;

		EClass owner = containment.getEContainingClass();

		// We don't want to nest behaviors in behaviors
		if (isTypeOf(request.getElementType(), UMLElementTypes.BEHAVIOR)) {
			result = false;
		} else if (UMLPackage.Literals.BEHAVIOR.isSuperTypeOf(owner)) {
			// Exclude Behavior as owner: we don't want behavior parameters in UML Light,
			// but the diagram needs access to features of the specific behavior type
			result = owner != UMLPackage.Literals.BEHAVIOR;
		} else {
			// Don't want features/nested classifiers/etc. as in classes
			result = !owner.isSuperTypeOf(UMLPackage.Literals.CLASS)
					// Unless it's from Element (owned comment)
					|| (owner == UMLPackage.Literals.ELEMENT);
		}

		return result;
	}
}
