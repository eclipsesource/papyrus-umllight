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
package org.eclipse.papyrus.umllight.ui.internal.wizard;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.uml.diagram.wizards.pages.SelectRepresentationKindPage;
import org.eclipse.papyrus.uml.diagram.wizards.wizards.NewPapyrusProjectWizard;
import org.eclipse.ui.IWorkbench;

/**
 * A <em>New Project Wizard</em> that optimally creates <em>UML Light</em> model
 * projects.
 */
public class NewUMLLightProjectWizard extends NewPapyrusProjectWizard {

	private final CreateUMLLightModelWizardKernel kernel = new CreateUMLLightModelWizardKernel(this);

	/**
	 * Initializes me
	 */
	public NewUMLLightProjectWizard() {
		super();
	}

	/**
	 * Initialize the New Project wizard.
	 * 
	 * @param workbench the host workbench
	 * @param selection the current workbench selection
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		super.init(workbench, selection);

		kernel.init(workbench, selection);
	}

	@Override
	protected String[] getSelectedContexts() {
		return kernel.getSelectedContexts();
	}

	@Override
	protected String[] getSelectedViewpoints() {
		return kernel.getSelectedViewpoints();
	}

	@Override
	protected String[] getSelectedViewpoints(String contextId) {
		return kernel.getSelectedViewpoints(contextId);
	}

	@Override
	protected SelectRepresentationKindPage doCreateSelectRepresentationKindPage() {
		return kernel.createSelectRepresentationKindPage();
	}

	@Override
	protected void initDomainModel(ModelSet modelSet, String contextId, String[] viewpointIds) {
		super.initDomainModel(modelSet, contextId, viewpointIds);

		// Standard content for UML Light, e.g. library imports
		kernel.initDomainModel(modelSet);
	}
}
