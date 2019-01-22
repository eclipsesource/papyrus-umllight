/*****************************************************************************
 * Copyright (c) 2019 Christian W. Damus and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *		Christian W. Damus - Initial API and implementation
 *****************************************************************************/
package org.eclipse.papyrus.umllight.core.internal.advice;

import static org.eclipse.papyrus.uml.service.types.utils.ElementUtil.isTypeOf;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.core.util.PackageUtil;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.commands.GetEditContextCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.GetEditContextRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;
import org.eclipse.papyrus.uml.service.types.element.UMLElementTypes;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Vertex;

/**
 * An edit-advice helper for the elision of {@link Region}s in the UI context of
 * the editor (<em>Model Explorer</em> tree, diagram, etc.).
 */
final class RegionElisionHelper {

	/**
	 * Initializes me
	 */
	RegionElisionHelper() {
		super();
	}

	/**
	 * Creates advice for an edit-context {@code request}.
	 * 
	 * @param request an edit-context request
	 * @return my advice, or {@code null} if I provide none
	 */
	ICommand getBeforeEditContextCommand(GetEditContextRequest request) {
		GetEditContextCommand result = null;

		Object context = request.getEditContext();
		if (context instanceof StateMachine) {
			StateMachine stateMachine = (StateMachine) context;

			if (isRequestForRegionOwnedElement(request.getEditCommandRequest())
					&& !stateMachine.getRegions().isEmpty()) {

				context = stateMachine.getRegions().get(0);
				result = new GetEditContextCommand(request);
				result.setEditContext(context);
			}
		}

		return result;
	}

	/**
	 * Configures the given create {@code request}, if appropriate.
	 * 
	 * @param request a create request to configure
	 * 
	 * @return {@code true} if I configured it and it probably shouldn't be
	 *         configured again by another helper; {@code false} otherwise
	 */
	boolean configureCreateRequest(CreateElementRequest request) {
		boolean result = (request.getContainmentFeature() == null) && isRequestForRegionOwnedElement(request);

		if (result) {
			// Set the containment reference that we had to leave unset in order to pass
			// the New Child framework's filtering
			request.initializeContainmentFeature(
					PackageUtil.findFeature(UMLPackage.Literals.REGION, request.getElementType().getEClass()));
		}

		return result;
	}

	/**
	 * Query whether a {@code request} is for creation or deletion of a
	 * {@link Region}-owned element in the context of a {@link StateMachine}.
	 * 
	 * @param request a request
	 * @return whether it is a create or destroy request for a region-owned element
	 */
	boolean isRequestForRegionOwnedElement(IEditCommandRequest request) {
		boolean result = false;

		if (request instanceof CreateElementRequest) {
			CreateElementRequest create = (CreateElementRequest) request;
			IElementType type = create.getElementType();
			result = isTypeOf(type, UMLElementTypes.VERTEX) || isTypeOf(type, UMLElementTypes.TRANSITION);
		} else if (request instanceof DestroyElementRequest) {
			DestroyElementRequest destroy = (DestroyElementRequest) request;
			EObject element = destroy.getElementToDestroy();
			result = (element instanceof Vertex) || (element instanceof Transition);
		}

		return result;
	}

}
