package de.kuei.metafora.server;

import javax.servlet.http.HttpServlet;

public class StartupServlet extends HttpServlet {

	private static final boolean test = true;
	// this flag also change the database server
	public static final boolean productive = false;

	public void init() {

	}

}
