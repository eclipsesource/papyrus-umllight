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

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.papyrus.infra.architecture.ArchitectureDescriptionUtils;
import org.eclipse.papyrus.infra.core.resource.IEMFModel;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.infra.emf.utils.ServiceUtilsForResourceSet;
import org.eclipse.papyrus.infra.services.semantic.service.SemanticService;
import org.eclipse.papyrus.uml.diagram.wizards.pages.SelectRepresentationKindPage;
import org.eclipse.papyrus.uml.diagram.wizards.pages.SelectRepresentationKindPage.ContextProvider;
import org.eclipse.papyrus.uml.diagram.wizards.wizards.CreateModelWizard;
import org.eclipse.papyrus.uml.diagram.wizards.wizards.NewPapyrusProjectWithMultiModelsWizard;
import org.eclipse.papyrus.uml.diagram.wizards.wizards.NewPapyrusProjectWizard;
import org.eclipse.papyrus.umllight.core.internal.UMLLightArchitecture;
import org.eclipse.papyrus.umllight.ui.internal.Activator;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.uml2.common.util.UML2Util;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageImport;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;

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
	 * Initialize the domain model to set the <em>UML Light</em> architecture
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

		execute(modelSet, //
				util.switchArchitectureContextId(contextId), //
				util.switchArchitectureViewpointIds(viewpointIds));
	}

	private void execute(ModelSet modelSet, Command... commands) {
		Stream.of(commands).filter(Objects::nonNull).reduce(Command::chain)
				.ifPresent(modelSet.getTransactionalEditingDomain().getCommandStack()::execute);
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

	/**
	 * Initialize a new domain model that already has the <em>UML Light</em>
	 * architecture context and viewpoint configured.
	 * 
	 * @param modelSet the model set to initialize
	 * 
	 * @see #initDomainModel(ModelSet, String, String[])
	 */
	void initDomainModel(ModelSet modelSet) {
		execute(modelSet, createImportLibrariesCommand(modelSet));
	}

	/**
	 * Obtain a command to import standard libraries commonly required for <em>UML
	 * Light</em> models.
	 * 
	 * @param modelSet the model set in which to import libraries
	 * 
	 * @return the import libraries command
	 */
	Command createImportLibrariesCommand(ModelSet modelSet) {
		Command result = null;

		Package primitiveTypes = UML2Util.load(modelSet, URI.createURI(UMLResource.UML_PRIMITIVE_TYPES_LIBRARY_URI),
				UMLPackage.Literals.PACKAGE);
		if (primitiveTypes != null) {
			PackageImport packageImport = UMLFactory.eINSTANCE.createPackageImport();
			packageImport.setImportedPackage(primitiveTypes);

			try {
				SemanticService semanticService = ServiceUtilsForResourceSet.getInstance()
						.getService(SemanticService.class, modelSet);
				Optional<Package> root = Stream.of(semanticService.getSemanticIModels())
						.filter(IEMFModel.class::isInstance).map(IEMFModel.class::cast).map(IEMFModel::getResource)
						.map(r -> EcoreUtil.getObjectByType(r.getContents(), UMLPackage.Literals.PACKAGE))
						.filter(Objects::nonNull).map(Package.class::cast).findFirst();

				// Create a command to add the import if this hasn't been imported already
				result = root.filter(p -> !p.getImportedPackages().contains(primitiveTypes))
						.map(p -> AddCommand.create(modelSet.getTransactionalEditingDomain(), p,
								UMLPackage.Literals.NAMESPACE__PACKAGE_IMPORT, packageImport))
						.orElse(null);
			} catch (ServiceException e) {
				Activator.getDefault().log(e);
			}
		}

		return result;
	}
}
