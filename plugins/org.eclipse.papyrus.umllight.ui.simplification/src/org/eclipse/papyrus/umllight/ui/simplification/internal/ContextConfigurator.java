/**
 * Copyright (c) 2016,2018 EclipseSource Services GmbH and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Martin Fleck (EclipseSource) - Initial API and implementation
 * Tobias Ortmayr - adaptation for UML Light
 */
package org.eclipse.papyrus.umllight.ui.simplification.internal;

import org.eclipse.papyrus.views.properties.runtime.ConfigurationManager;

public final class ContextConfigurator {

	/** Configuration manager instance */
	protected static ConfigurationManager configurationManager = ConfigurationManager.getInstance();

	private ContextConfigurator() {
	}

	/**
	 * Disables the context with the given name if the context can be found
	 * 
	 * @param name 
	 * 			context name
	 * @see Context
	 */
	public static void disableContext(String name) {
		org.eclipse.papyrus.infra.properties.contexts.Context context = configurationManager.getContext(name);
		if (context != null) {
			try {
				configurationManager.disableContext(context, true);
			} catch (IllegalStateException e) {
				// nothing we can really do
			}
		}
	}

	/**
	 * Enables the context with the given name if the context can be found
	 * 
	 * @param name 
	 * 			context name
	 * @see Context
	 */
	public static void enableContext(String name) {
		org.eclipse.papyrus.infra.properties.contexts.Context context = configurationManager.getContext(name);
		if (context != null) {
			try {
				configurationManager.enableContext(context, true);
			} catch (IllegalStateException e) {
				// nothing we can really do
			}
		}
	}

	/**
	 * Queries whether the context with the given name is enabled
	 * 
	 * @param name 
	 * 			context name
	 * @return whether the context with the given name is enabled. False if no matching context can be found
	 * @see Context
	 */
	public static boolean isEnabled(String name) {
		org.eclipse.papyrus.infra.properties.contexts.Context context = configurationManager.getContext(name);
		if (context != null) {
			try {
				return configurationManager.isEnabled(context);
			} catch (IllegalStateException e) {
				// nothing we can really do
			}
		}
		return false;
	}

	public interface Context {
		/** AdvanceStyle */
		String ADVANCE_STYLE = "AdvanceStyle";

		/** CSS in Diagrams */
		String CSS = "CSS";

		/** Customization Models */
		String CUSTOMIZATION = "Customization";

		/** Diagram Notation */
		String NOTATION = "notation";

		/** Diagram Styles */
		String STYLE = "style";

		/** Diagram Synchronization */
		String SYNCHRONIZATION = "synchronization";

		/** UML Light */
		String UML_LIGHT = "UML Light";

		/** UML Diagram Symbols */
		String SYMBOLS = "Symbols";

		/** UML Graphical Notation */
		String UML_NOTATION = "UMLNotation";

		/** UML Metamodel */
		String UML = "UML";

		/** UML Profile Externalization */
		String UML_STEREOTYPE_APPLICATION_EXTERNAL_RESOURCE = "UMLStereotypeApplicationExternalResource";

		/** UML Sequence Diagram Notation */
		String SEQUENCE_NOTATION = "SequenceNotation";
	}
}