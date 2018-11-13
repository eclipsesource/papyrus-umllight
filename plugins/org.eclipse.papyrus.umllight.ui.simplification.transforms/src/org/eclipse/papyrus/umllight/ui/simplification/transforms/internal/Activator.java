/*****************************************************************************
 * Copyright (c) 2018 Christian W. Damus and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *		Christian W. Damus - Initial API and implementation
 *****************************************************************************/

package org.eclipse.papyrus.umllight.ui.simplification.transforms.internal;

import java.net.URL;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 * The activator class controls the plug-in life cycle.
 */
public class Activator implements BundleActivator {

	private ServiceRegistration<URL> transformURLReg;

	/**
	 * Initializes me.
	 */
	public Activator() {
		super();
	}

	public void start(BundleContext context) throws Exception {
		Hashtable<String, Object> properties = new Hashtable<>();
		properties.put("equinox.transformerType", "xslt"); //$NON-NLS-1$ //$NON-NLS-2$
		transformURLReg = context.registerService(URL.class, context.getBundle().getEntry("/transforms/transforms.csv"), //$NON-NLS-1$
				properties);
	}

	public void stop(BundleContext context) throws Exception {
		if (transformURLReg != null) {
			transformURLReg.unregister();
			transformURLReg = null;
		}
	}

}
