package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controlador.Controlador;
import modelo.Catalogo;
import modelo.Item;
import modelo.OrdenCriterio;
import modelo.TipoCriterio;

/**
 * Servlet implementation class BuscarConCriterio
 */
@WebServlet("/ServletBuscarConCriterio")
public class ServletBuscarConCriterio extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletBuscarConCriterio() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Catalogo c1 = Controlador.getUnicaInstancia().buscarCatalogo("C1");
		List<Item> resultados = Controlador.getUnicaInstancia().buscarItemPorCriterio(TipoCriterio.MARCAITEM, OrdenCriterio.ASCENDENTE,TipoCriterio.PRECIOREBAJADO, c1, "NOX");
		if (!resultados.isEmpty()){
		for (Item item : resultados) {
	        System.out.println(item.toString());
	      }
		} else
			System.out.println("Resultados vacios");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
