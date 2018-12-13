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
package org.eclipse.papyrus.umllight.ui.properties.widgets;

import org.eclipse.papyrus.infra.widgets.editors.MultiplicityDialog;
import org.eclipse.papyrus.umllight.ui.internal.widgets.editors.UMLLightMultiplicityEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * This is the <code>UMLLightMultiplicityDialog</code> type. Enjoy.
 *
 */
public class UMLLightMultiplicityDialog extends org.eclipse.papyrus.uml.properties.widgets.MultiplicityDialog {

	/**
	 * Initializes me with my {@code parent} composite and my {@code style}. I
	 * by-pass the preference store because switching editors is not supported.
	 * 
	 * @param parent my parent composite
	 * @param style  my style bits (as supported by the superclass)
	 */
	public UMLLightMultiplicityDialog(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected MultiplicityDialog createMultiplicityDialog(Composite parent, int style) {
		// The superclass requires that the 'editor' field be assigned here
		editor = new UMLLightMultiplicityEditor(parent, style);
		return editor;
	}
}
