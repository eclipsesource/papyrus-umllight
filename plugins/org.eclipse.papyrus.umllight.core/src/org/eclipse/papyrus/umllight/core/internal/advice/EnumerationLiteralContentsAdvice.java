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
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * Edit-helper advice to suppress creation of value specifications in
 * {@link EnumerationLiteral}s.
 */
public class EnumerationLiteralContentsAdvice extends AbstractNewChildAdvice {

	/**
	 * Initializes me.
	 */
	public EnumerationLiteralContentsAdvice() {
		super();
	}

	@Override
	protected boolean approveCreateRequest(CreateElementRequest request, EReference containment) {
		return !isTypeOf(request.getElementType(), UMLElementTypes.VALUE_SPECIFICATION)
				|| (containment != UMLPackage.Literals.INSTANCE_SPECIFICATION__SPECIFICATION);
	}
}
