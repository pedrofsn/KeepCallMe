package keep.call.me.Model;

import java.util.List;

public class Estado{
   	private List cidades;
   	private String estado;
   	private String sigla;

 	public List getCidades(){
		return this.cidades;
	}
	public void setCidades(List cidades){
		this.cidades = cidades;
	}
 	public String getEstado(){
		return this.estado;
	}
	public void setEstado(String estado){
		this.estado = estado;
	}
 	public String getSigla(){
		return this.sigla;
	}
	public void setSigla(String sigla){
		this.sigla = sigla;
	}
}
