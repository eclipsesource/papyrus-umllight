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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.uml.diagram.wizards.pages.SelectArchitectureContextPage;
import org.eclipse.papyrus.uml.diagram.wizards.pages.SelectRepresentationKindPage;
import org.eclipse.papyrus.uml.diagram.wizards.providers.INewModelStorageProvider;
import org.eclipse.papyrus.uml.diagram.wizards.wizards.CreateModelWizard;
import org.eclipse.papyrus.uml.diagram.wizards.wizards.InitModelWizard;
import org.eclipse.ui.IWorkbench;

/**
 * A <em>New Model Wizard</em> that optimally creates <em>UML Light</em> models.
 */
public class InitUMLLightModelWizard extends InitModelWizard {

	private final CreateUMLLightModelWizardKernel kernel = new CreateUMLLightModelWizardKernel(this);

	public InitUMLLightModelWizard() {
		super();
	}

	/**
	 * Initialize the New Model wizard.
	 * 
	 * @param workbench the host workbench
	 * @param selection the current workbench selection
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		super.init(workbench, selection);

		kernel.init(workbench, selection);

		if (isCreateFromExistingDomainModel()) {
			// We are not creating a new model but initializing the diagrams for it
			setWindowTitle("Initialize UML Light Diagrams");
		}
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

	/**
	 * Override initialization of the domain model to set the <em>UML Light</em>
	 * architecture context and viewpoint even for initialization of the diagrams
	 * for an existing UML model resource.
	 * 
	 * @param modelSet     the model set to initialize
	 * @param contextId    the architecture context ID (which would be <em>UML
	 *                     Light</em>)
	 * @param viewpointIds the viewpoint (which would be <em>UML Light</em>)
	 * 
	 * @see CreateModelWizard#initDomainModel(ModelSet, String, String[])
	 */
	protected void initDomainModel(ModelSet modelSet, String contextId, String[] viewpointIds) {
		if (isCreateFromExistingDomainModel()) {
			// Do at least set the UML Light architecture context and viewpoint
			kernel.initDomainModel(modelSet, contextId, viewpointIds);
		} else {
			super.initDomainModel(modelSet, contextId, viewpointIds);

			// Standard content for UML Light, e.g. library imports
			kernel.initDomainModel(modelSet);
		}
	}

	/**
	 * Wraps the assigned storage {@code provider} to omit the architecture context
	 * selection page, instead forcing always the <em>UML Light</em> context.
	 */
	@Override
	protected void setSelectedStorageProvider(INewModelStorageProvider provider) {
		INewModelStorageProvider newProvider = provider;

		if (newProvider != null) {
			// But we don't need the architecture context page
			newProvider = new NewModelStorageProviderWrapper(newProvider) {

				public SelectArchitectureContextPage getArchitectureContextPage() {
					// Don't need this.
					return null;
				}

				@Override
				public List<? extends IWizardPage> createPages() {
					List<IWizardPage> result = new ArrayList<>(delegate.createPages());

					// Don't need this.
					result.remove(delegate.getArchitectureContextPage());

					return result;
				}
			};
		}

		super.setSelectedStorageProvider(newProvider);
	}

}
