package modelo;

public enum OrdenCriterio {
	ASCENDENTE("ASC"), DESCENDENTE("DESC");

	private String displayName;

	OrdenCriterio(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}
