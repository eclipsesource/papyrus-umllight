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

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.papyrus.infra.gmfdiag.common.databinding.GMFObservableValue;
import org.eclipse.papyrus.infra.properties.ui.creation.EcorePropertyEditorFactory;
import org.eclipse.papyrus.infra.properties.ui.modelelement.DataSource;
import org.eclipse.papyrus.infra.widgets.creation.ReferenceValueFactory;
import org.eclipse.papyrus.infra.widgets.providers.IStaticContentProvider;
import org.eclipse.papyrus.uml.properties.modelelement.UMLModelElement;
import org.eclipse.uml2.uml.Message;
import org.eclipse.uml2.uml.MessageSort;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * A specific properties view model element façade implementation for <em>UML
 * Light</em>.
 */
public class UMLLightModelElement extends UMLModelElement {

	private final DynamicEnumComboHelper<MessageSort> messageSortHelper;

	private final Map<String, ReferenceValueFactory> valueFactories = new HashMap<>();

	/**
	 * Initializes me with my {@code source} element and contextual editing
	 * {@code domain}.
	 * 
	 * @param source the source UML model element
	 * @param domain the contextual editing domain
	 */
	public UMLLightModelElement(EObject source, EditingDomain domain) {
		super(source, domain);

		messageSortHelper = new DynamicEnumComboHelper<>(this, UMLPackage.Literals.MESSAGE__MESSAGE_SORT,
				this::getAllowedMessageSorts);
	}

	@Override
	public IObservable doGetObservable(String propertyPath) {
		EStructuralFeature feature = getFeature(propertyPath);

		if (feature == UMLPackage.Literals.MESSAGE__MESSAGE_SORT) {
			// We don't support all of the message sorts
			return messageSortHelper.getObservableValue();
		} else if (feature == UMLPackage.Literals.EXTEND__EXTENSION_LOCATION) {
			// We disguise the multi reference as an ObservableValue
			return new GMFObservableValue(Realm.getDefault(), getSource(), feature, getDomain());
		} else {
			return super.doGetObservable(propertyPath);
		}
	}

	@Override
	public IStaticContentProvider getContentProvider(String propertyPath) {
		EStructuralFeature feature = getFeature(propertyPath);

		if (feature == UMLPackage.Literals.MESSAGE__MESSAGE_SORT) {
			// We don't support all of the message sorts
			return messageSortHelper.getContentProvider();
		} else {
			return super.getContentProvider(propertyPath);
		}
	}

	@Override
	public ILabelProvider getLabelProvider(String propertyPath) {
		EStructuralFeature feature = getFeature(propertyPath);
		ILabelProvider baseLabelProvider = super.getLabelProvider(propertyPath);
		if (feature == UMLPackage.Literals.EXTEND__EXTENSION_LOCATION) {
			return new MultiAsSingleReferenceLabelProvider(baseLabelProvider);
		}
		return super.getLabelProvider(propertyPath);
	}

	private Set<MessageSort> getAllowedMessageSorts(EObject source) {
		Message message = (Message) source;
		switch (message.getMessageSort()) {
		case SYNCH_CALL_LITERAL:
		case ASYNCH_CALL_LITERAL:
			// Can switch freely between these sorts
			return EnumSet.of(MessageSort.SYNCH_CALL_LITERAL, MessageSort.ASYNCH_CALL_LITERAL);
		default:
			// It doesn't make sense to change the sort of a reply, create, or delete
			return EnumSet.of(message.getMessageSort());
		}
	}

	@Override
	public ReferenceValueFactory getValueFactory(String propertyPath) {
		return valueFactories.computeIfAbsent(propertyPath, path -> {
			ReferenceValueFactory result = super.getValueFactory(path);

			if (result == null || !(result instanceof EcorePropertyEditorFactory)) {
				return result;
			}

			// Copy with filtering of creatable EClasses
			UMLLightPropertyEditorFactory factory = new UMLLightPropertyEditorFactory(
					(EcorePropertyEditorFactory) result);

			EStructuralFeature feature = getFeature(path);
			if (feature == UMLPackage.Literals.MESSAGE__SIGNATURE) {
				// We don't support signals, so there's only one choice
				factory.setEClass(UMLPackage.Literals.OPERATION);
				result = factory;
			}

			result = factory;
			return result;
		});
	}

	@Override
	public boolean getDirectCreation(String propertyPath) {
		EStructuralFeature feature = getFeature(propertyPath);
		return super.getDirectCreation(propertyPath)
				// We don't want to show the '...' button for read-only references
				|| ((feature instanceof EReference) && !getValueFactory(propertyPath).canCreateObject());
	}

	@Override
	public boolean isMandatory(String propertyPath) {
		EStructuralFeature feature = getFeature(propertyPath);
		return super.isMandatory(propertyPath)
				// We don't want to show the 'X' button for read-only references
				|| ((feature instanceof EReference) && !getValueFactory(propertyPath).canEdit());
	}

	@Override
	public void setDataSource(DataSource source) {
		if (dataSource != null) {
			dataSource.removeDataSourceListener(messageSortHelper);
		}

		super.setDataSource(source);

		if (dataSource != null) {
			dataSource.addDataSourceListener(messageSortHelper);
		}
	}

	@Override
	public void dispose() {
		if (dataSource != null) {
			dataSource.removeDataSourceListener(messageSortHelper);
		}

		super.dispose();
	}

}
