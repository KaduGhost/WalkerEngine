package br.com.redewalker.kaduzin.engine.sistema.reports;

import java.util.EnumSet;

public enum ReportMotivoType {
	
	Imune,
	Aimbot,
	AutoArmor,
	ChestFinder,
	ClienteAlternativo,
	Critical,
	FastPlace,
	Fly,
	ForceField,
	NoFall,
	NoSlow,
	Regen,
	Repulsao,
	Speed,
	Wall,
	XRay,
	Outro;
	
	public static ReportMotivoType getByNome(String nome) {
		for (ReportMotivoType tipo : EnumSet.allOf(ReportMotivoType.class)) {
			if (nome.equalsIgnoreCase(tipo.toString())) return tipo;
		}
		return null;
	}
	
}
