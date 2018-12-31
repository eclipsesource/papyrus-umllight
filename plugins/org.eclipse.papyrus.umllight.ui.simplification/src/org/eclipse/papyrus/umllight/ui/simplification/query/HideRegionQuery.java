package org.eclipse.papyrus.umllight.ui.simplification.query;

import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Region;
import org.eclipse.papyrus.emf.facet.efacet.core.IFacetManager;
import org.eclipse.papyrus.emf.facet.efacet.core.exception.DerivedTypedElementException;
import org.eclipse.papyrus.emf.facet.query.java.core.IJavaQuery2;
import org.eclipse.papyrus.emf.facet.query.java.core.IParameterValueList2;

public class HideRegionQuery implements IJavaQuery2<Element, Boolean> {
	public Boolean evaluate(final Element context, final IParameterValueList2 parameterValues,
			final IFacetManager facetManager) throws DerivedTypedElementException {

		return !(context instanceof Region);
	}
}
