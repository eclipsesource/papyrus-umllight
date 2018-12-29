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

import org.eclipse.papyrus.infra.gmfdiag.extensionpoints.editors.configuration.IDirectEditorConstraint;

/**
 * A direct editor constraint that checks whether the contextual element is
 * <strong>not</strong> in a <em>UML Light</em> model.
 */
public class IsNotUMLLightConstraint implements IDirectEditorConstraint {

	private final IsUMLLightConstraint isUMLLight = new IsUMLLightConstraint();

	/**
	 * Initializes me.
	 */
	public IsNotUMLLightConstraint() {
		super();
	}

	@Override
	public String getLabel() {
		return "In UML Model";
	}

	@Override
	public boolean appliesTo(Object selection) {
		return !isUMLLight.appliesTo(selection);
	}

}
