package modelo;



public enum TipoCriterio {
	CODIGO("codigo"), URLPRODUCTO("urlProducto"), NOMBRE("nombre"), URLIMAGEN("urlImagen"), 
	NOMBRECOMPLETO("nombreCompleto"), MARCAITEM("marcaItem"), PRECIOREBAJADO("precioRebajado"),
	PRECIOORIGINAL("precioOriginal");
	
	private String displayName; 
	
	TipoCriterio(String displayName) {
		this.displayName = displayName;
	}
	
	public String getDisplayName() {
		return displayName;
	}
}
