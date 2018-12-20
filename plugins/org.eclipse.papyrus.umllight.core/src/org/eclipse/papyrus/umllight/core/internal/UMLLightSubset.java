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

import static java.util.Collections.unmodifiableSet;
import static java.util.EnumSet.complementOf;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.Predicate;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.uml2.uml.MessageSort;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * A provider of subsetting functions for the <em>UML</em> metamodel to
 * determine what is included in the <em>UML Light</em> dialect.
 */
public class UMLLightSubset {

	private static final UMLLightSubset INSTANCE = new UMLLightSubset();

	private final Set<MessageSort> messageSorts = unmodifiableSet(
			complementOf(EnumSet.of(MessageSort.ASYNCH_SIGNAL_LITERAL)));

	private final Set<PseudostateKind> pseudostateKinds = unmodifiableSet(EnumSet.of(PseudostateKind.INITIAL_LITERAL,
			PseudostateKind.CHOICE_LITERAL, PseudostateKind.JUNCTION_LITERAL));

	/**
	 * Initializes me.
	 */
	public UMLLightSubset() {
		super();
	}

	public static UMLLightSubset getInstance() {
		return INSTANCE;
	}

	/**
	 * Query the subset of an enumeration that are the values recognized in <em>UML
	 * Light</em>.
	 * 
	 * @param enumType an enumeration
	 * @return its <em>UML Light</em> values
	 */
	@SuppressWarnings("unchecked") // We actually do check
	public <E extends Enum<E>> Set<E> getValues(Class<E> enumType) {
		if (enumType == PseudostateKind.class) {
			return (Set<E>) pseudostateKinds;
		} else if (enumType == MessageSort.class) {
			return (Set<E>) messageSorts;
		} else {
			return unmodifiableSet(EnumSet.allOf(enumType));
		}
	}

	/**
	 * Obtain a predicate that selects the valid metaclasses (as represented by
	 * {@code EClass}) in the <em>UML Light</em> dialect.
	 * 
	 * @return the metaclass filter
	 */
	public Predicate<EClass> getMetaclassFilter() {
		return this::isUMLLight;
	}

	/**
	 * Query whether a metaclass (as represented by an EMF {@code eclass}) supported
	 * in the <em>UML Light</em> dialect.
	 * 
	 * @param eclass a metaclass
	 * @return whether it is a <em>UML Light</em> metaclass
	 */
	public boolean isUMLLight(EClass eclass) {
		if (eclass.getEPackage() != UMLPackage.eINSTANCE) {
			return false;
		}

		switch (eclass.getClassifierID()) {
		// Package Diagram concepts
		case UMLPackage.MODEL:
		case UMLPackage.PACKAGE:
		case UMLPackage.PACKAGE_IMPORT:
		case UMLPackage.DEPENDENCY:
			return true;
		// Class Diagram concepts
		case UMLPackage.CLASS:
		case UMLPackage.DATA_TYPE:
		case UMLPackage.ENUMERATION:
		case UMLPackage.PRIMITIVE_TYPE:
		case UMLPackage.ENUMERATION_LITERAL:
		case UMLPackage.PROPERTY:
		case UMLPackage.ASSOCIATION:
		case UMLPackage.ASSOCIATION_CLASS:
		case UMLPackage.OPERATION:
		case UMLPackage.PARAMETER:
		case UMLPackage.GENERALIZATION:
		case UMLPackage.INTERFACE_REALIZATION:
		case UMLPackage.REALIZATION:
			return true;
		// Use Case Diagram concepts
		case UMLPackage.ACTOR:
		case UMLPackage.USE_CASE:
		case UMLPackage.INCLUDE:
		case UMLPackage.EXTEND:
		case UMLPackage.EXTENSION_POINT:
			return true;
		// State Machine Diagram concepts
		case UMLPackage.STATE_MACHINE:
		case UMLPackage.STATE:
		case UMLPackage.FINAL_STATE:
		case UMLPackage.PSEUDOSTATE:
		case UMLPackage.TRANSITION:
			return true;
		// Activity Diagram concepts
		case UMLPackage.ACTIVITY:
		case UMLPackage.INITIAL_NODE:
		case UMLPackage.FLOW_FINAL_NODE:
		case UMLPackage.ACTIVITY_FINAL_NODE:
		case UMLPackage.DECISION_NODE:
		case UMLPackage.MERGE_NODE:
		case UMLPackage.FORK_NODE:
		case UMLPackage.JOIN_NODE:
		case UMLPackage.OPAQUE_ACTION:
		case UMLPackage.CALL_BEHAVIOR_ACTION:
		case UMLPackage.CALL_OPERATION_ACTION:
		case UMLPackage.ACCEPT_EVENT_ACTION:
		case UMLPackage.INPUT_PIN:
		case UMLPackage.OUTPUT_PIN:
		case UMLPackage.VALUE_PIN:
		case UMLPackage.CONTROL_FLOW:
			return true;
		// Sequence Diagram concepts:
		case UMLPackage.INTERACTION:
		case UMLPackage.LIFELINE:
		case UMLPackage.MESSAGE:
		case UMLPackage.MESSAGE_OCCURRENCE_SPECIFICATION:
		case UMLPackage.EXECUTION_OCCURRENCE_SPECIFICATION:
		case UMLPackage.ACTION_EXECUTION_SPECIFICATION:
		case UMLPackage.BEHAVIOR_EXECUTION_SPECIFICATION:
		case UMLPackage.INTERACTION_USE:
		case UMLPackage.OPAQUE_BEHAVIOR:
			return true;
		// Common concepts:
		case UMLPackage.LITERAL_BOOLEAN:
		case UMLPackage.LITERAL_STRING:
		case UMLPackage.LITERAL_INTEGER:
		case UMLPackage.LITERAL_UNLIMITED_NATURAL:
		case UMLPackage.LITERAL_REAL:
		case UMLPackage.LITERAL_NULL:
		case UMLPackage.INSTANCE_VALUE:
		case UMLPackage.COMMENT:
		case UMLPackage.CONSTRAINT:
		case UMLPackage.OPAQUE_EXPRESSION:
		case UMLPackage.EXPRESSION:
			return true;
		default:
			return false;
		}
	}
}
