package keep.call.me.Model;

public class TelefoneModel {
	private String cidade;
	private String regiao;
	private String ddd;
	private String uf;
	private int id;

	public TelefoneModel(int id, String cidade, String ddd, String uf, String regiao) {
		super();
		this.id = id;
		this.cidade = cidade;
		this.ddd = ddd;
		this.uf = uf;
		this.regiao = regiao;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getDdd() {
		return ddd;
	}

	public void setDdd(String ddd) {
		this.ddd = ddd;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getRegiao() {
		return regiao;
	}

	public void setRegiao(String regiao) {
		this.regiao = regiao;
	}

}