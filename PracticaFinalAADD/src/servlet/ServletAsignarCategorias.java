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
import modelo.Categoria;
import modelo.Usuario;

/**
 * Servlet implementation class ServletAsignarCategorias
 */
@WebServlet("/ServletAsignarCategorias")
public class ServletAsignarCategorias extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletAsignarCategorias() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Categoria categoria = Controlador.getUnicaInstancia().registrarCategoria("Palas Caras90");
		System.out.println("Categoria registrada!");
		Usuario usuario = Controlador.getUnicaInstancia().registrarUsuario("Javier", "clave", "email", "nombre", "nif");
		System.out.println("Usuario registrado!");
		Catalogo catalogo = Controlador.getUnicaInstancia().registrarCatalogo("Palas Padel99", new Date(), "PadelMania",
				"https://padelmania.com/", usuario);
		System.out.println("Catalogo registrado!");
		
		Categoria cat1 = Controlador.getUnicaInstancia().buscarCategoria("Palas Caras");
		System.out.println("Antes de buscar: " + categoria.toString());
		System.out.println("Despues de buscar: " + cat1.toString());
		Controlador.getUnicaInstancia().asignarCategoria(categoria, catalogo);
		
		System.out.println(categoria.toString());
		System.out.println(catalogo.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
