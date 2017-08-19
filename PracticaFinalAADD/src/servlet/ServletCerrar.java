package servlet;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ServletCerrar
 */
@WebServlet("/ServletCerrar")
public class ServletCerrar extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public ServletCerrar() {
        super();
    }

	
	protected void doGet(HttpServletRequest peticion, HttpServletResponse respuesta) throws ServletException, IOException {
		@SuppressWarnings("unchecked")
		HashMap<String,Date> usuarios = 
				(HashMap<String,Date>) getServletConfig().getServletContext().getAttribute("usuarios");
		if(usuarios!=null){
			usuarios.remove(peticion.getSession().getAttribute("usuario_actual"));
		}
		peticion.getSession().invalidate();
		getServletConfig().getServletContext().setAttribute("usuario_actual", null);
		respuesta.sendRedirect("index.html");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
