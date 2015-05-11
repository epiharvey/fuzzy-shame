package com.epiphan.qa.grid;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A simple servlet which brings down a node when invoked
 * 
 * @author Ian Harvey [iharvey@epiphan.com]
 *
 */

public class NodeKillerServlet extends HttpServlet {

	private static final long serialVersionUID = -1;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doPost(req, resp);
		shutDownNode();
	}

	protected void shutDownNode() {
		System.out.println("Terminating node...");
		System.exit(0);
	}

}
