package br.com.redewalker.kaduzin.engine.sistema.economia;

public enum EconomiaType {
	
	CASH;
	
	public static EconomiaType getByNome(String nome) {
		if (nome.equalsIgnoreCase("cash")) return CASH;
		return null;
	}

}
