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
package org.eclipse.papyrus.umllight.core.internal;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.papyrus.infra.architecture.ArchitectureDescriptionUtils;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.emf.utils.EMFHelper;

/**
 * This is the <code>UMLLightArchitecture</code> type.  Enjoy.
 *
 */
public class UMLLightArchitecture {

	public static final String CONTEXT_ID = "org.eclipse.papyrus.umllight";

	public static final String VIEWPOINT_ID = "org.eclipse.papyrus.umllight.viewpoint.basic";
  
	/**
	 * Not instantiable by clients.
	 */
	private UMLLightArchitecture() {
		super();
	}

	/**
	 * Query whether an {@link object} is in the <em>UML Light</em> architecture
	 * context.
	 * 
	 * @param object an object in some Papyrus editor
	 * @return whether it is in a <em>UML Light</em> model
	 */
	public static boolean isUMLLight(EObject object) {
		boolean result = false;

		// Not in any architecture context if not in a ModelSet, which implements the
		// architecture framework in Papyrus
		ResourceSet rset = EMFHelper.getResourceSet(object);
		if (rset instanceof ModelSet) {
			ArchitectureDescriptionUtils arch = new ArchitectureDescriptionUtils((ModelSet) rset);
			result = CONTEXT_ID.equals(arch.getArchitectureContextId());
		}

		return result;
	}

}
