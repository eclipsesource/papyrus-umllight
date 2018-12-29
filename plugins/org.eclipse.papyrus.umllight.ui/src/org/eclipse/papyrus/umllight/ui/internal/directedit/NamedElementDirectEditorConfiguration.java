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
package org.eclipse.papyrus.umllight.ui.internal.directedit;

import org.eclipse.gmf.runtime.common.ui.services.parser.IParser;
import org.eclipse.papyrus.infra.gmfdiag.extensionpoints.editors.configuration.AbstractBasicDirectEditorConfiguration;
import org.eclipse.papyrus.uml.diagram.common.parser.NamedElementLabelParser;
import org.eclipse.uml2.uml.NamedElement;

/**
 * Simple direct-editor configuration for {@link NamedElement}s that just edits
 * their names.
 */
public class NamedElementDirectEditorConfiguration extends AbstractBasicDirectEditorConfiguration {

	/**
	 * Initializes me.
	 */
	public NamedElementDirectEditorConfiguration() {
		super();
	}

	@Override
	public IParser createDirectEditorParser() {
		return new NamedElementLabelParser();
	}

}
