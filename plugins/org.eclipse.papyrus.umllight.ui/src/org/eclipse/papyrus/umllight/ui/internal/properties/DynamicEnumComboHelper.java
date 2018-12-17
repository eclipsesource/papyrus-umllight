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

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.papyrus.infra.gmfdiag.common.databinding.GMFObservableValue;
import org.eclipse.papyrus.infra.properties.ui.modelelement.DataSourceChangedEvent;
import org.eclipse.papyrus.infra.properties.ui.modelelement.EMFModelElement;
import org.eclipse.papyrus.infra.properties.ui.modelelement.IDataSourceListener;
import org.eclipse.papyrus.infra.ui.emf.providers.EMFEnumeratorContentProvider;
import org.eclipse.papyrus.infra.widgets.providers.IStaticContentProvider;
import org.eclipse.papyrus.umllight.core.internal.UMLLightSubset;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Control;

/**
 * The {@code EnumCombo} property editor does not support different elements
 * having different subsets of the enum values presented in the choice of
 * values. A naïve attempt in the {@link EMFModelElement} simply to return
 * different values for different source elements results in refresh on
 * selection change unsetting the property on an inconsistency between the
 * values allowed for the initial selection as compared to the new selection.
 * This class works around that by direct manipulation of the {@link CCombo}
 * underlying the editor's viewer.
 */
class DynamicEnumComboHelper<E extends Enum<E> & Enumerator> implements IDataSourceListener {

	private final EMFModelElement element;
	private final EStructuralFeature feature;
	private final Class<E> enumType;
	private final Function<? super EObject, ? extends Set<E>> allowedValuesFunction;

	private IStaticContentProvider contentProvider;
	private ComboViewer comboViewer;

	/**
	 * Initializes me with the {@code element} that I help.
	 * 
	 * @param feature               the structural feature that I help
	 * @param allowedValuesFunction the dynamic computation of the values allowed
	 *                              for the model element
	 */
	@SuppressWarnings("unchecked")
	DynamicEnumComboHelper(EMFModelElement element, EStructuralFeature feature,
			Function<? super EObject, ? extends Set<E>> allowedValuesFunction) {
		super();

		this.element = element;
		this.feature = feature;
		this.enumType = (Class<E>) feature.getEType().getInstanceClass().asSubclass(Enum.class);
		this.allowedValuesFunction = allowedValuesFunction;
	}

	@SuppressWarnings("unchecked")
	IObservableValue<E> getObservableValue() {
		return new GMFObservableValue(Realm.getDefault(), element.getSource(), feature, element.getDomain()) {
			@Override
			protected void doSetValue(Object value) {
				// We will be setting the selected value in the viewer to the current
				// value of the new source when the workbench selection changes. This
				// triggers the data binding but we don't want to execute a no-op command
				// for that as the superclass would do
				if (!Objects.equals(value, doGetValue())) {
					super.doSetValue(value);
				}
			}
		};
	}

	IStaticContentProvider getContentProvider() {
		if (contentProvider == null) {
			contentProvider = new EMFEnumeratorContentProvider(feature) {

				private final List<?> values = UMLLightSubset.getInstance().getValues(enumType).stream()
						.map(Enumerator.class::cast).sorted(Comparator.comparing(Enumerator::getLiteral))
						.collect(Collectors.toList());

				@Override
				public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
					super.inputChanged(viewer, oldInput, newInput);

					comboViewer = (ComboViewer) viewer;

					if (comboViewer != null) {
						postFilters();
					}
				}

				@Override
				public Object[] getElements() {
					return values.toArray();
				}

			};
		}

		return contentProvider;
	}

	@Override
	public void dataSourceChanged(DataSourceChangedEvent event) {
		if (comboViewer != null) {
			// Clear any filters to prepare for selection change
			comboViewer.setFilters();
			// Prime the viewer's selection with the new source's value of the feature
			comboViewer.setSelection(new StructuredSelection(element.getSource().eGet(feature)));

			postFilters();
		}
	}

	private void postFilters() {
		Control combo = comboViewer.getControl();
		EObject source = element.getSource();
		Set<E> realValues = allowedValuesFunction.apply(source);

		combo.getDisplay().asyncExec(() -> {
			if (!combo.isDisposed()) {
				// Set a viewer filter to hide disallowed values
				comboViewer.setFilters(new ViewerFilter() {
					public boolean select(Viewer viewer, Object parentElement, Object element) {
						return realValues.contains(element);
					}
				});
			}
		});
	}
}
