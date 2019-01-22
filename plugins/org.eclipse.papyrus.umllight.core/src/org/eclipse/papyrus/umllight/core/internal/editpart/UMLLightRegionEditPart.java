/*****************************************************************************
 * Copyright (c) 2019 Christian W. Damus and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *		Christian W. Damus - Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.umllight.core.internal.editpart;

import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.uml.diagram.statemachine.custom.edit.part.CustomRegionEditPart;
import org.eclipse.papyrus.uml.diagram.statemachine.custom.policies.CustomRegionDragTracker;
import org.eclipse.uml2.uml.Region;

/**
 * A customization of the {@link Region} view controller for <em>UML Light</em>.
 */
public class UMLLightRegionEditPart extends CustomRegionEditPart {

	/**
	 * Initializes me with my notation view.
	 * 
	 * @param view my region view
	 */
	public UMLLightRegionEditPart(View view) {
		super(view);
	}

	/**
	 * Forward selection from the region to the state machine, as in the <em>UML
	 * Light</em> context we aren't interested in the region as an element.
	 */
	@Override
	public DragTracker getDragTracker(Request request) {
		return new CustomRegionDragTracker(this) {

			@Override
			protected void setSourceEditPart(EditPart part) {
				if (part == UMLLightRegionEditPart.this) {
					// Select the state machine, itself
					part = part.getParent().getParent();
				}
				super.setSourceEditPart(part);
			}

		};
	}

}
