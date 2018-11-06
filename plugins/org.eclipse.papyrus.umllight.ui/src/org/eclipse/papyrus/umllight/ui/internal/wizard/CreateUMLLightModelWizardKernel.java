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

import static java.lang.String.format;

import org.eclipse.emf.common.command.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.papyrus.infra.architecture.ArchitectureDescriptionUtils;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.uml.diagram.wizards.pages.SelectRepresentationKindPage;
import org.eclipse.papyrus.uml.diagram.wizards.pages.SelectRepresentationKindPage.ContextProvider;
import org.eclipse.papyrus.uml.diagram.wizards.wizards.CreateModelWizard;
import org.eclipse.papyrus.uml.diagram.wizards.wizards.NewPapyrusProjectWithMultiModelsWizard;
import org.eclipse.papyrus.uml.diagram.wizards.wizards.NewPapyrusProjectWizard;
import org.eclipse.papyrus.umllight.core.internal.UMLLightArchitecture;
import org.eclipse.papyrus.umllight.ui.internal.Activator;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * A shared core behaviour of the model creation wizards for <em>UML Light</em>.
 */
class CreateUMLLightModelWizardKernel {

	private final CreateModelWizard owner;
	private final boolean isProject;

	/**
	 * Initializes me with my owner wizard.
	 * 
	 * @param owner my wizard
	 */
	public CreateUMLLightModelWizardKernel(CreateModelWizard owner) {
		super();

		this.owner = owner;
		this.isProject = (owner instanceof NewPapyrusProjectWizard)
				|| (owner instanceof NewPapyrusProjectWithMultiModelsWizard);
	}

	/**
	 * Initialize the New Model/Project wizard.
	 * 
	 * @param workbench the host workbench
	 * @param selection the current workbench selection
	 * 
	 * @see INewWizard#init(IWorkbench, IStructuredSelection)
	 */
	void init(IWorkbench workbench, IStructuredSelection selection) {
		String title = isProject ? "New UML Light Model Project" : "New UML Light Model";
		String iconName = isProject ? "newprj_wiz" : "newfile_wiz"; //$NON-NLS-1$//$NON-NLS-2$

		owner.setWindowTitle(title);
		owner.setDefaultPageImageDescriptor(
				Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, format("icons/full/wizban/%s.png", iconName))); //$NON-NLS-1$
	}

	/**
	 * Get the selected architecture contexts, which in these wizards is always and
	 * only the <em>UML Light</em> context.
	 * 
	 * @return the <em>UML Light</em> architecture context
	 * 
	 * @see CreateModelWizard#getSelectedContexts()
	 */
	String[] getSelectedContexts() {
		return new String[] { UMLLightArchitecture.CONTEXT_ID };
	}

	/**
	 * Get the selected viewpoints, which in these wizards is always and only the
	 * <em>UML Light</em> viewpoint.
	 * 
	 * @return the <em>UML Light</em> viewpoint
	 * 
	 * @see CreateModelWizard#getSelectedViewpoints()
	 */
	String[] getSelectedViewpoints() {
		return new String[] { UMLLightArchitecture.VIEWPOINT_ID };
	}

	/**
	 * Get the selected viewpoints for the given architecture context, which in
	 * these wizards is always and only the <em>UML Light</em> viewpoint when the
	 * referenced context is the <em>UML Light</em> architecture context.
	 * 
	 * @param contextId the architecture context for which to get selected
	 *                  viewpoints
	 * @return the <em>UML Light</em> viewpoint for its architecture context,
	 *         otherwise an empty array if the referenced context is something else
	 * 
	 * @see CreateModelWizard#getSelectedViewpoints(String)
	 */
	String[] getSelectedViewpoints(String contextId) {
		return UMLLightArchitecture.CONTEXT_ID.equals(contextId) ? new String[] { UMLLightArchitecture.VIEWPOINT_ID }
				: new String[0];
	}

	/**
	 * initialize the domain model to set the <em>UML Light</em> architecture
	 * context and viewpoint even for initialization of the diagrams for an existing
	 * UML model resource.
	 * 
	 * @param modelSet     the model set to initialize
	 * @param contextId    the architecture context ID (which would be <em>UML
	 *                     Light</em>)
	 * @param viewpointIds the viewpoint (which would be <em>UML Light</em>)
	 * 
	 * @see CreateModelWizard#initDomainModel(ModelSet, String, String[])
	 */
	void initDomainModel(ModelSet modelSet, String contextId, String[] viewpointIds) {
		ArchitectureDescriptionUtils util = new ArchitectureDescriptionUtils(modelSet);

		Command switchContext = util.switchArchitectureContextId(contextId);
		Command switchViewpoint = util.switchArchitectureViewpointIds(viewpointIds);

		Command init = switchContext;
		if (switchViewpoint != null) {
			init = (init == null) ? switchViewpoint : init.chain(switchViewpoint);
		}

		modelSet.getTransactionalEditingDomain().getCommandStack().execute(init);
	}

	/**
	 * Create a main wizard page optimized for the <em>UML Light</em> context.
	 * 
	 * @return the main wizard page
	 */
	SelectRepresentationKindPage createSelectRepresentationKindPage() {
		SelectRepresentationKindPage result = new SelectRepresentationKindPage(createContextProvider());

		result.setShowTemplateChooser(false);
		result.setShowProfileChooser(false);

		return result;
	}

	/**
	 * Create a context provider that delegates to me (to the wizard).
	 * 
	 * @return the context provider
	 * 
	 * @see CreateModelWizard#createContextProvider()
	 */
	protected ContextProvider createContextProvider() {
		return new ContextProvider() {

			@Override
			public String[] getCurrentContexts() {
				return getSelectedContexts();
			}

			@Override
			public String[] getCurrentViewpoints() {
				return getSelectedViewpoints();
			}

		};
	}

}
