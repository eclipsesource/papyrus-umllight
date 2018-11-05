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

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.papyrus.uml.diagram.wizards.pages.SelectArchitectureContextPage;
import org.eclipse.papyrus.uml.diagram.wizards.providers.INewModelStorageProvider;
import org.eclipse.papyrus.uml.diagram.wizards.wizards.CreateModelWizard;
import org.eclipse.ui.IEditorInput;

/**
 * Wrapper for new-model storage providers.
 */
public class NewModelStorageProviderWrapper implements INewModelStorageProvider {

	protected final INewModelStorageProvider delegate;

	/**
	 * Initializes me with my wrapped provider.
	 * 
	 * @param delegate the wrapped provider
	 */
	public NewModelStorageProviderWrapper(INewModelStorageProvider delegate) {
		super();

		this.delegate = delegate;
	}

	@Override
	public boolean canHandle(IStructuredSelection initialSelection) {
		return delegate.canHandle(initialSelection);
	}

	@Override
	public void init(CreateModelWizard wizard, IStructuredSelection selection) {
		delegate.init(wizard, selection);
	}

	@Override
	public List<? extends IWizardPage> createPages() {
		return delegate.createPages();
	}

	@Override
	public IStatus validateArchitectureContexts(String... newContexts) {
		return delegate.validateArchitectureContexts(newContexts);
	}

	@Override
	public URI createNewModelURI(String diagramCategoryID) {
		return delegate.createNewModelURI(diagramCategoryID);
	}

	@Override
	public IEditorInput createEditorInput(URI uri) {
		return delegate.createEditorInput(uri);
	}

	@Override
	public ISelectProviderPart createSelectProviderPart() {
		return delegate.createSelectProviderPart();
	}

	@Override
	public SelectArchitectureContextPage getArchitectureContextPage() {
		return delegate.getArchitectureContextPage();
	}

}
