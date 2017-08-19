package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ServletTestJDBC
 */
@WebServlet("/ServletTestJDBC")
public class ServletTestJDBC extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletTestJDBC() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		try {
			// Se hace el Class.forName para cargar en memoria la clase
			// com.mysql.jdbc.Driver y que asi
			// el driverManager la detecte de memoria. Equivale a importar la
			// libreria.
			Class.forName("com.mysql.jdbc.Driver");

			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/aadd", "root", "admin");

			Statement stmt = con.createStatement();
			// El ResultSet es como un proxy perezoso bajo demanda, se va
			// trayendo los
			// resultados poco a poco. Al principio solo el primero y conforme
			// hago
			// next va trayendo los demas.
			ResultSet rs = stmt.executeQuery("SELECT id,name FROM test");
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				out.println(id + " , " + name + "<br>");
			}
			rs.close();
			stmt.close();
			con.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
