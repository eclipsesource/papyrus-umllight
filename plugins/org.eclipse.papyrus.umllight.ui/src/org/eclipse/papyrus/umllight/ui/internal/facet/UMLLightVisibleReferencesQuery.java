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
package org.eclipse.papyrus.umllight.ui.internal.facet;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.papyrus.emf.facet.efacet.core.IFacetManager;
import org.eclipse.papyrus.emf.facet.efacet.core.exception.DerivedTypedElementException;
import org.eclipse.papyrus.emf.facet.query.java.core.IParameterValueList2;
import org.eclipse.papyrus.uml.modelexplorer.queries.GetVisibleUMLReferencesQuery;
import org.eclipse.uml2.uml.UMLPackage;

import com.google.common.collect.ImmutableSet;

/**
 * Facet query determining the restricted set of references to be followed to
 * construct the content tree in the <em>Model Explorer</em> view.
 */
public class UMLLightVisibleReferencesQuery extends GetVisibleUMLReferencesQuery {

	private static final Set<EReference> EXCLUDED_REFERENCES = ImmutableSet
			.of(UMLPackage.Literals.STATE_MACHINE__REGION, UMLPackage.Literals.STATE__REGION);

	/**
	 * Initializes me.
	 */
	public UMLLightVisibleReferencesQuery() {
		super();
	}

	@Override
	public List<EReference> evaluate(EObject source, IParameterValueList2 parameterValues, IFacetManager facetManager)
			throws DerivedTypedElementException {

		List<EReference> result = new ArrayList<EReference>(super.evaluate(source, parameterValues, facetManager));

		result.removeAll(EXCLUDED_REFERENCES);

		return result;
	}

}
