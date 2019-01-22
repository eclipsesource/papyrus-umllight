/*****************************************************************************
 * Copyright (c) 2018, 2019 Christian W. Damus and others.
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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.IElementMatcher;
import org.eclipse.gmf.runtime.emf.type.core.IHintedType;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.GetEditContextRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.IEditCommandRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.SetRequest;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * Edit-helper advice for configuration and editing of {@link Pseudostate}s.
 */
public class PseudostateAdvice extends AbstractEditHelperAdvice {

	private final RegionElisionHelper region = new RegionElisionHelper();

	/**
	 * Initializes me.
	 */
	public PseudostateAdvice() {
		super();
	}

	@Override
	protected ICommand getBeforeEditContextCommand(GetEditContextRequest request) {
		return region.getBeforeEditContextCommand(request);
	}

	public void configureRequest(IEditCommandRequest request) {
		super.configureRequest(request);

		if (request instanceof CreateElementRequest) {
			configureCreateRequest((CreateElementRequest) request);
		}
	}

	protected void configureCreateRequest(CreateElementRequest request) {
		region.configureCreateRequest(request);
	}

	@Override
	protected ICommand getAfterConfigureCommand(ConfigureRequest request) {
		IHintedType type = (IHintedType) request.getTypeToConfigure();
		PseudostateKind kind = PseudostateKind.get(type.getSemanticHint());
		SetRequest set = new SetRequest(request.getEditingDomain(), request.getElementToConfigure(),
				UMLPackage.Literals.PSEUDOSTATE__KIND, kind);
		return type.getEditCommand(set);
	}

	//
	// Nested types
	//

	/**
	 * Common element-type matcher framework for pseudostates.
	 */
	public static abstract class PseudostateMatcher implements IElementMatcher {
		private final PseudostateKind kind;

		PseudostateMatcher(PseudostateKind kind) {
			super();

			this.kind = kind;
		}

		@Override
		public boolean matches(EObject eObject) {
			return (eObject instanceof Pseudostate) && (((Pseudostate) eObject).getKind() == kind);
		}

	}

	/**
	 * Initial pseudostate element-type matcher.
	 */
	public static final class Initial extends PseudostateMatcher {
		public Initial() {
			super(PseudostateKind.INITIAL_LITERAL);
		}
	}

	/**
	 * Choice pseudostate element-type matcher.
	 */
	public static final class Choice extends PseudostateMatcher {
		public Choice() {
			super(PseudostateKind.CHOICE_LITERAL);
		}
	}

	/**
	 * Junction pseudostate element-type matcher.
	 */
	public static final class Junction extends PseudostateMatcher {
		public Junction() {
			super(PseudostateKind.JUNCTION_LITERAL);
		}
	}

}
