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
import modelo.Item;
import modelo.Usuario;

/**
 * Servlet implementation class ServletRegistrarCatalogo
 */
@WebServlet("/ServletCargarItems")
public class ServletCargarItems extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletCargarItems() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String urlFichero = getServletContext().getRealPath("WEB-INF/files/palasPadel.csv");
		System.out.println(urlFichero);
		//Usuario u = Controlador.getUnicaInstancia().registrarUsuario("Javier", "clave", "email", "nombre", "nif");
		
		//Se registra un catalogo vacio y se carga los items del csv en el
		//Catalogo catalogo = Controlador.getUnicaInstancia().registrarCatalogo("Palas Padel9", new Date(), "PadelMania", "https://padelmania.com/", u);
		
		Catalogo cat = Controlador.getUnicaInstancia().buscarCatalogo("C1");
		
		Controlador.getUnicaInstancia().cargarItemsEnCatalogo(urlFichero, cat);
		
		
		
		System.out.println("Lista items");
		for (Item items : cat.getItems()) {
			System.out.println(items.toString());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);

	}

}
