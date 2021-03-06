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
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Interaction;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * Edit-helper advice to suppress creation of features and other contents of
 * {@link Class}es that are not interesting in {@link Interaction}s in the
 * <em>UML Light</em> context.
 */
public class InteractionContentsAdvice extends AbstractNewChildAdvice {

	/**
	 * Initializes me.
	 */
	public InteractionContentsAdvice() {
		super();
	}

	@Override
	protected boolean approveCreateRequest(CreateElementRequest request, EReference containment) {
		EClass owner = containment.getEContainingClass();
		return (owner == UMLPackage.Literals.INTERACTION) // Needed by the diagram editor
				|| (owner == UMLPackage.Literals.ELEMENT); // For owned comment
		// Exclude Behavior as owner: we don't want behavior parameters in UML Light
	}
}
