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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.papyrus.uml.service.types.element.UMLElementTypes;
import org.eclipse.uml2.uml.Actor;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.UseCase;

/**
 * Edit-helper advice for creation of relationships of {@link Classifier}s in
 * the <em>UML Light</em> context.
 */
public class ClassifierRelationshipsAdvice extends AbstractNewRelationshipAdvice {

	/**
	 * Initializes me.
	 */
	public ClassifierRelationshipsAdvice() {
		super();
	}

	@Override
	protected boolean approveCreateRelationshipRequest(EObject source, EObject target, IElementType relationship) {
		boolean result = true;

		if (isTypeOf(relationship, UMLElementTypes.INTERFACE_REALIZATION)) {
			// don't create interface realizations in actors and use cases
			result = !(source instanceof Actor) && !(source instanceof UseCase);
		} else if (isTypeOf(relationship, UMLElementTypes.ASSOCIATION_CLASS)) {
			// don't create association-classes in use case diagrams
			result = !(source instanceof Actor) && !(source instanceof UseCase) //
					&& !(target instanceof Actor) && !(target instanceof UseCase);
		}

		return result;
	}

}
