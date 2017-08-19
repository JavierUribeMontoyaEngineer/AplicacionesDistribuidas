package servlet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controlador.Controlador;

/**
 * Servlet implementation class ServletLogin
 */
@WebServlet({ "/ServletLogin", "/index.html" })
public class ServletLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletLogin() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	private boolean isAdmin(String usuario) {
		return usuario.equals("admin");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		String usuarioLogueado = (String) request.getSession().getAttribute("usuario_actual");

		if (usuarioLogueado != null) {
			if (isAdmin(usuarioLogueado))
				response.sendRedirect("homeAdmin.xhtml");
			else
				response.sendRedirect("home.xhtml");

		} else {
			response.sendRedirect("index.xhtml");

		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest peticion, HttpServletResponse respuesta)
			throws ServletException, IOException {
		String usuario = peticion.getParameter("usuario");
		String clave = peticion.getParameter("clave");
		System.out.println("SERVLET LOGIN POST********");
		HttpSession sesion = peticion.getSession();
		Integer numIntentos;
		numIntentos = (Integer) sesion.getAttribute("numIntentos");

		if (numIntentos != null) {
			numIntentos++;
		} else {
			numIntentos = 1;
		}
		sesion.setAttribute("numIntentos", numIntentos);

		if (numIntentos > 3) {
			respuesta.sendError(500, "Numero de intentos superado");
			return;
		}

		// if(Controlador.getUnicaInstancia().login(usuario, clave)){
		String claveUsuario = getServletConfig().getInitParameter(usuario);

		HashMap<String, Date> usuarios = (HashMap<String, Date>) getServletConfig().getServletContext()
				.getAttribute("usuarios");
		if (usuarios == null) {
			usuarios = new HashMap<String, Date>();
			getServletConfig().getServletContext().setAttribute("usuarios", usuarios);
		}

		// if (usuarios.get(usuario) != null) {
		// respuesta.sendError(500, "USUARIO YA LOGEADO EN OTRA SESION: " +
		// usuarios.get(usuario));
		// return;
		// }

		boolean error = true;

		// if(clave!=null && clave.equals(claveUsuario)){
		// ********************* NUEVO PARA LA FUNCION LOGIN DEL CONTROLADOR
		// ************
		if (Controlador.getUnicaInstancia().login(usuario, clave)) {
			sesion.setAttribute("numIntentos", new Integer(0));
			sesion.setAttribute("usuario_actual", usuario);
			error = false;
		}

		if (!error) {

			usuarios.put(usuario, new Date());

			// RequestDispatcher rd =
			// peticion.getRequestDispatcher("ServletMain");
			getServletConfig().getServletContext().setAttribute("usuario_actual", usuario);
			// String ruta = peticion.getContextPath() + "/home.xhtml";
			// peticion.getRequestDispatcher("homeAdmin.xhtml").forward(peticion,
			// respuesta);

			if (isAdmin(usuario))
				respuesta.sendRedirect("homeAdmin.xhtml");
			else
				respuesta.sendRedirect("home.xhtml");
			// rd.forward(peticion, respuesta);
		} else {
			respuesta.sendRedirect("error.html");
		}
	}

}
