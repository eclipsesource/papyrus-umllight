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
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * Edit-helper advice to suppress creation of value specifications in
 * {@link Package}s that are not interesting in the <em>UML Light</em> context.
 */
public class PackagedValuesAdvice extends AbstractNewChildAdvice {

	/**
	 * Initializes me.
	 */
	public PackagedValuesAdvice() {
		super();
	}

	@Override
	protected boolean approveCreateRequest(CreateElementRequest request, EReference containment) {
		boolean isValueLike = isTypeOf(request.getElementType(), UMLElementTypes.VALUE_SPECIFICATION)
				|| isTypeOf(request.getElementType(), UMLElementTypes.ENUMERATION_LITERAL);

		return !isValueLike || (containment != UMLPackage.Literals.PACKAGE__PACKAGED_ELEMENT);
	}
}
