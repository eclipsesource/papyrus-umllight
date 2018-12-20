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

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.papyrus.infra.properties.ui.creation.EcorePropertyEditorFactory;
import org.eclipse.papyrus.uml.properties.creation.UMLPropertyEditorFactory;
import org.eclipse.papyrus.umllight.core.internal.UMLLightSubset;
import org.eclipse.papyrus.umllight.ui.internal.Activator;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * Custom property editor factory that supports filtering of the metaclasses
 * that may be instantiated for creation of new elements, to the
 * {@linkplain UMLLightSubset subset} recognized by <em>UML Light</em>.
 */
public class UMLLightPropertyEditorFactory extends UMLPropertyEditorFactory {

	private static final ConcurrentHashMap<String, Function<EcorePropertyEditorFactory, ?>> fieldAccessors = new ConcurrentHashMap<>();

	private static final Set<EReference> NON_EDITABLE_REFERENCES = new HashSet<>(
			Arrays.asList(UMLPackage.Literals.MESSAGE__SEND_EVENT, //
					UMLPackage.Literals.MESSAGE__RECEIVE_EVENT, //
					UMLPackage.Literals.MESSAGE_END__MESSAGE, //
					UMLPackage.Literals.EXECUTION_SPECIFICATION__START, //
					UMLPackage.Literals.EXECUTION_SPECIFICATION__FINISH, //
					UMLPackage.Literals.EXECUTION_OCCURRENCE_SPECIFICATION__EXECUTION, //
					UMLPackage.Literals.EXTEND__EXTENSION_LOCATION, //
					UMLPackage.Literals.EXTEND__EXTENDED_CASE, //
					UMLPackage.Literals.INCLUDE__ADDITION //
			));

	/**
	 * Initializes me from the {@code original} to copy.
	 * 
	 * @param original the property editor factory to copy
	 */
	public UMLLightPropertyEditorFactory(EcorePropertyEditorFactory original) {
		super(get(original, "referenceIn")); //$NON-NLS-1$

		this.type = get(original, "type"); //$NON-NLS-1$
		this.eClass = get(original, "eClass"); //$NON-NLS-1$
		this.nsUri = get(original, "nsUri"); //$NON-NLS-1$
		this.className = get(original, "className"); //$NON-NLS-1$
		this.containerContentProvider = get(original, "containerContentProvider"); //$NON-NLS-1$
		this.referenceContentProvider = get(original, "referenceContentProvider"); //$NON-NLS-1$
		this.containerLabelProvider = get(original, "containerLabelProvider"); //$NON-NLS-1$
		this.referenceLabelProvider = get(original, "referenceLabelProvider"); //$NON-NLS-1$
		this.labelProviderService = get(original, "labelProviderService"); //$NON-NLS-1$
	}

	@Override
	protected List<EClass> getAvailableEClasses() {
		List<EClass> result = super.getAvailableEClasses();
		result.removeIf(UMLLightSubset.getInstance().getMetaclassFilter().negate());
		return result;
	}

	@Override
	public boolean canCreateObject() {
		return super.canCreateObject() && !NON_EDITABLE_REFERENCES.contains(referenceIn);
	}

	@Override
	public boolean canEdit() {
		return super.canEdit() && !NON_EDITABLE_REFERENCES.contains(referenceIn);
	}

	/**
	 * Set a specific metaclass to instantiate, or {@code null} to infer from the
	 * reference type or require user selection.
	 * 
	 * @param eClass the metaclass to instantiate, or {@code null}
	 */
	void setEClass(EClass eClass) {
		this.eClass = null;
		this.className = null;
		this.nsUri = null;

		if (eClass != null) {
			setNsUri(eClass.getEPackage().getNsURI());
			setClassName(eClass.getName());
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> T get(EcorePropertyEditorFactory original, String field) {
		return (T) getField(original, field).apply(original);
	}

	private static Function<EcorePropertyEditorFactory, ?> getField(EcorePropertyEditorFactory target, String name) {
		return fieldAccessors.computeIfAbsent(name, UMLLightPropertyEditorFactory::access);
	}

	private static Function<EcorePropertyEditorFactory, ?> access(String fieldName) {
		try {
			Field field = EcorePropertyEditorFactory.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			return owner -> {
				try {
					return field.get(owner);
				} catch (Exception e) {
					Activator.getDefault().log(e);
					// Don't try this again
					fieldAccessors.put(fieldName, __ -> null);
					return null;
				}
			};
		} catch (Exception e) {
			Activator.getDefault().log(e);
			return __ -> null;
		}
	}
}
