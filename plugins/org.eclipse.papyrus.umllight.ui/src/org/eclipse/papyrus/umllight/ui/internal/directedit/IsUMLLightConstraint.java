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
package org.eclipse.papyrus.umllight.ui.internal.directedit;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.papyrus.infra.emf.utils.EMFHelper;
import org.eclipse.papyrus.infra.gmfdiag.extensionpoints.editors.configuration.IDirectEditorConstraint;
import org.eclipse.papyrus.umllight.core.internal.UMLLightArchitecture;

/**
 * A direct editor constraint that checks whether the contextual element is in a
 * <em>UML Light</em> model.
 */
public class IsUMLLightConstraint implements IDirectEditorConstraint {

	/**
	 * Initializes me.
	 */
	public IsUMLLightConstraint() {
		super();
	}

	@Override
	public String getLabel() {
		return "In UML Light Model";
	}

	@Override
	public boolean appliesTo(Object selection) {
		EObject object = EMFHelper.getEObject(selection);

		return (object != null) && UMLLightArchitecture.isUMLLight(object);
	}

}
