/*****************************************************************************
 * Copyright (c) 2018, 2019 Christian W. Damus and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *		Christian W. Damus - Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.umllight.core.internal.provider;

import org.eclipse.gmf.runtime.diagram.ui.services.editpart.AbstractEditPartProvider;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.uml.diagram.statemachine.edit.parts.RegionEditPart;
import org.eclipse.papyrus.uml.diagram.statemachine.edit.parts.TransitionEditPart;
import org.eclipse.papyrus.umllight.core.internal.UMLLightArchitecture;
import org.eclipse.papyrus.umllight.core.internal.editpart.UMLLightRegionEditPart;
import org.eclipse.papyrus.umllight.core.internal.editpart.UMLLightTransitionEditPart;

/**
 * Custom edit-part provider for <em>UML Light</em> state machine diagrams.
 */
public class StateMachineEditPartProvider extends AbstractEditPartProvider {

	/**
	 * Initializes me.
	 */
	public StateMachineEditPartProvider() {
		super();
	}

	@Override
	protected Class<?> getNodeEditPartClass(View view) {
		if (RegionEditPart.VISUAL_ID.equals(view.getType()) && UMLLightArchitecture.isUMLLight(view)) {
			return UMLLightRegionEditPart.class;
		} else {
			return super.getNodeEditPartClass(view);
		}
	}

	@Override
	protected Class<?> getEdgeEditPartClass(View view) {
		if (TransitionEditPart.VISUAL_ID.equals(view.getType()) && UMLLightArchitecture.isUMLLight(view)) {
			return UMLLightTransitionEditPart.class;
		} else {
			return super.getEdgeEditPartClass(view);
		}
	}

}
