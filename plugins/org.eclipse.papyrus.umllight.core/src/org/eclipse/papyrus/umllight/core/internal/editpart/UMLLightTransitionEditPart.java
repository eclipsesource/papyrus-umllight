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
package org.eclipse.papyrus.umllight.core.internal.editpart;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.uml.diagram.statemachine.custom.edit.part.CustomTransitionEditPart;
import org.eclipse.papyrus.uml.diagram.statemachine.edit.parts.TransitionNameEditPart;
import org.eclipse.papyrus.umllight.core.internal.Activator;

/**
 * Custom edit-part for <em>UML Light</em> transitions. Customizations include:
 * <ul>
 * <li>direct edit targets the name label, not the guard</li>
 * </ul>
 */
public class UMLLightTransitionEditPart extends CustomTransitionEditPart {

	/**
	 * Initializes me with the view that I control.
	 * 
	 * @param view my notation view
	 */
	public UMLLightTransitionEditPart(View view) {
		super(view);
	}

	protected void performDirectEditRequest(Request request) {
		EditPart editPart = this;
		if (request instanceof DirectEditRequest) {
			Point p = new Point(((DirectEditRequest) request).getLocation());
			getFigure().translateToRelative(p);
			IFigure fig = getFigure().findFigureAt(p);
			editPart = (EditPart) getViewer().getVisualPartMap().get(fig);
		}

		if (editPart == this) {
			try {
				editPart = (EditPart) getEditingDomain().runExclusive(new RunnableWithResult.Impl<EditPart>() {

					@Override
					public void run() {
						// Unlike the superclass, we edit the name, not the guard
						@SuppressWarnings("unchecked")
						List<? extends EditPart> children = getChildren();
						setResult(children.stream().filter(TransitionNameEditPart.class::isInstance).findFirst()
								.orElse(null));
					}
				});
			} catch (InterruptedException e) {
				IStatus failure = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Transaction interrupted", e);
				Activator.getDefault().getLog().log(failure);
			}

			if (editPart != null) {
				editPart.performRequest(request);
			}
		}
	}

}
