package servlet;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controlador.Controlador;
import modelo.Catalogo;
import modelo.Usuario;

/**
 * Servlet implementation class ServletRegistrarCatalogo
 */
@WebServlet("/ServletRegistrarCatalogo")
public class ServletRegistrarCatalogo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletRegistrarCatalogo() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//Usuario usuario = Controlador.getUnicaInstancia().registrarUsuario("Jorge12", "clave", "email", "nombre",
	//			"nif");
		Usuario usuario = Controlador.getUnicaInstancia().buscarUsuario("a");
		Controlador.getUnicaInstancia().registrarCatalogo("C1", new Date(), "PadelMania1",
				"https://padelmania.com/1", usuario);
		Controlador.getUnicaInstancia().registrarCatalogo("C2", new Date(), "PadelMania2",
				"https://padelmania.com/2", usuario);
		Controlador.getUnicaInstancia().registrarCatalogo("C3", new Date(), "PadelMania3",
				"https://padelmania.com/3", usuario);
		Controlador.getUnicaInstancia().registrarCatalogo("C4", new Date(), "PadelMania4",
				"https://padelmania.com/4", usuario);

		System.out.println("Lista catalogos");
		for (Catalogo catalogoIterador : usuario.getCatalogos()) {
			System.out.println("*"+catalogoIterador.toString());
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

}
