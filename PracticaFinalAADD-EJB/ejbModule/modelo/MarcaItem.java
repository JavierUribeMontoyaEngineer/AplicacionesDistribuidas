package modelo;

// Extensible a mas categorias de producto
public enum MarcaItem {
	ADIDAS("ADIDAS"), AKKERON("AKKERON"), ARES("ARES"), ASICS("ASICS"), BULL_PADEL("BULL PADEL"), 
	DROP_SHOT("DROP SHOT"), DUNLOP("DUNLOP"), HEAD("HEAD"), MIDDLE_MOON("MIDDLE MOON"), MYSTICA("MYSTICA"), 
	NOX("NOX"), PADDLE_COACH("PADDLE COACH"), ROYAL_PADEL("ROYAL PADEL"), STAR_VIE("STAR VIE"), 
	VIBORA("VIBORA"), VISION("VISION"), WILSON("WILSON");
	
	private String displayName; 
	
	MarcaItem(String displayName) {
		this.displayName = displayName;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	// Funcion que a partir de un String (con espacios) me devuelve un valor valido del enumerado
	public static MarcaItem getEnum(String enumString) {
		for (MarcaItem marcaItem : MarcaItem.values()) {
			if (marcaItem.getDisplayName().equals(enumString)) return marcaItem;
		}
		//Si no existe devuelve null
		return null;
	}
}