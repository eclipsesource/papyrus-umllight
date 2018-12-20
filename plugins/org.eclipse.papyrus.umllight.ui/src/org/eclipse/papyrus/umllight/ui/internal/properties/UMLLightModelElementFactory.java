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
package org.eclipse.papyrus.umllight.ui.internal.properties;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.papyrus.infra.emf.utils.EMFHelper;
import org.eclipse.papyrus.infra.properties.contexts.DataContextElement;
import org.eclipse.papyrus.uml.properties.modelelement.UMLModelElementFactory;
import org.eclipse.papyrus.uml.tools.utils.UMLUtil;
import org.eclipse.papyrus.umllight.ui.internal.Activator;
import org.eclipse.uml2.uml.Element;

/**
 * Factory for the specific <em>UML Light</em> implementation of the properties
 * view model element façade.
 */
public class UMLLightModelElementFactory extends UMLModelElementFactory {

	/**
	 * Initializes me.
	 */
	public UMLLightModelElementFactory() {
		super();
	}

	@Override
	protected UMLLightModelElement doCreateFromSource(Object source, DataContextElement context) {
		Element umlSource = UMLUtil.resolveUMLElement(source);
		if (umlSource == null) {
			Activator.getDefault().warn("Cannot resolve the selection to a UML Element"); //$NON-NLS-1$
			return null;
		}

		EditingDomain domain = EMFHelper.resolveEditingDomain(umlSource);
		return new UMLLightModelElement(umlSource, domain);
	}

}
