package beans;

import javax.faces.application.FacesMessage;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import controlador.Controlador;
import modelo.Catalogo;

@ManagedBean(name = "cargarItemsBean")
@SessionScoped
public class CargarItemsBean {
	private Catalogo catalogoActual;
	private UploadedFile file;
	private final String RUTA_UPLOADS = "WEB-INF/files";

	public Catalogo getCatalogo() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String catalogoActualSesion = (String) request.getSession().getAttribute("catalogo_actual");
		catalogoActual = Controlador.getUnicaInstancia().buscarCatalogo(catalogoActualSesion);
		return catalogoActual;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	// Esta funcion guarda el fichero uploadedFile recibido del cliente en ruta
	// pasado por parametro
	private void guardarFichero(UploadedFile uploadedFile, String ruta) throws IOException {
		String filename = FilenameUtils.getName(uploadedFile.getFileName());
		InputStream input = null;
		input = uploadedFile.getInputstream();
		OutputStream output = new FileOutputStream(new File(ruta, filename));
		try {
			IOUtils.copy(input, output);
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
	}

	public void cargar(FileUploadEvent event) {
		if (event.getFile() != null) {
			String nombreFichero = event.getFile().getFileName();
			FacesMessage message = new FacesMessage("Succesful", nombreFichero + " is uploaded.");
			FacesContext.getCurrentInstance().addMessage(null, message);
			ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
					.getContext();
			String urlCarpeta = servletContext.getRealPath(RUTA_UPLOADS);
			urlCarpeta += "\\";
			String urlFichero = urlCarpeta + nombreFichero;
			try {
				// Guardamos el fichero en nuestra ruta de servidor
				guardarFichero(event.getFile(), urlCarpeta);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("**RUTA:" + urlFichero);
			// Se pasa al controlador la ruta completa donde tendra que leer el
			// fichero para cargar los items
			Controlador.getUnicaInstancia().cargarItemsEnCatalogo(urlFichero, getCatalogo());
			System.out.println(",,,,,,,,,,,,,,,,,, " + catalogoActual);
		} else {
			FacesMessage message = new FacesMessage("Error 404 File not Found");
			FacesContext.getCurrentInstance().addMessage(null, message);

		}

	}

}
