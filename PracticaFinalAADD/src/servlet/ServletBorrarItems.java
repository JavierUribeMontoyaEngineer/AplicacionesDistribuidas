package servlet;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controlador.Controlador;
import modelo.Item;

/**
 * Servlet implementation class ServletBorrarItems
 */
@WebServlet("/ServletBorrarItems")
public class ServletBorrarItems extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletBorrarItems() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Item item = Controlador.getUnicaInstancia().buscarItem("Pala Explosion 15");
		Item item2 = Controlador.getUnicaInstancia().buscarItem("Pala Sanyo Graphene XT Alpha Pro");
		Item item3 = Controlador.getUnicaInstancia().buscarItem("Pala Attack Pro P1");
		Item item4 = Controlador.getUnicaInstancia().buscarItem("Pala Graphene XT Delta Elite");
		List<Item> lista = new LinkedList<Item>();
		lista.add(item);
		lista.add(item2);
		lista.add(item3);
		lista.add(item4);
		Controlador.getUnicaInstancia().borrarItems(lista);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
