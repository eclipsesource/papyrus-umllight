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
package org.eclipse.papyrus.umllight.ui.internal.widgets.editors;

import org.eclipse.papyrus.infra.widgets.editors.MultiplicityDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * A customization of the Papyrus editor composite for multiplicity that
 * restricts the editor to only the combo-box presentation (omitting the switch
 * to the advanced mode).
 */
public class UMLLightMultiplicityEditor extends MultiplicityDialog {

	/**
	 * Initializes me with my {@code parent} composite and my {@code style}. I
	 * by-pass the preference store because switching editors is not supported.
	 * 
	 * @param parent my parent composite
	 * @param style  my style bits (as supported by the superclass)
	 */
	public UMLLightMultiplicityEditor(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected void createButtons() {
		// Don't create the switcher button

		// But do fix the margins
		if (stringComboEditor.getLayout() instanceof GridLayout) {
			GridLayout layout = (GridLayout) stringComboEditor.getLayout();
			layout.marginHeight = 0;
			layout.marginWidth = 0;
		}
	}

	@Override
	protected GridData getLabelLayoutData() {
		// Fix the alignment of the label relative to the combo
		GridData result = super.getLabelLayoutData();
		result.verticalAlignment = SWT.TOP;
		return result;
	}
}
