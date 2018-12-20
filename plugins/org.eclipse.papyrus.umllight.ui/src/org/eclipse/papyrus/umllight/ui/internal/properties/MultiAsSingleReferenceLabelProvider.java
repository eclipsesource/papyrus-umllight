/*****************************************************************************
 * Copyright (c) 2018 EclipseSource Services GmbH. and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *		Tobias Ortmayr - Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.umllight.ui.internal.properties;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * Specialized labelprovider for disguised multi references. Disguised multi
 * references in this context means multi references which can contain (at all
 * time) at most on element. Such references can be treaded as single reference
 * to use the simpler @{code ReferenceDialog} editor widget.
 * 
 *
 */
class MultiAsSingleReferenceLabelProvider extends LabelProvider {
	private ILabelProvider baseLabelProvider;

	public MultiAsSingleReferenceLabelProvider(ILabelProvider baseLabelProvider) {
		this.baseLabelProvider = baseLabelProvider;
	}

	@Override
	public Image getImage(Object element) {
		return baseLabelProvider.getImage(undisguise(element));
	}

	@Override
	public String getText(Object element) {
		return baseLabelProvider.getText(undisguise(element));
	}

	static Object undisguise(Object element) {
		if (element instanceof EList<?> && !((EList<?>) element).isEmpty()) {
			Object first = ((EList<?>) element).get(0);
			if (first instanceof EObject) {
				return first;
			}
		}
		return element;
	}
}