package keep.call.me.Model;

public class ListagemModel {
	private int id;
	private String regiao;
	private int versao;
	private String uf;
	private String link;

	public ListagemModel(int id, String cidade, int versao, String uf, String link) {
		super();
		this.id = id;
		this.regiao = cidade;
		this.versao = versao;
		this.uf = uf;
		this.link = link;
	}

	public ListagemModel() {
	}

	public String getRegiao() {
		return regiao;
	}

	public void setRegiao(String regiao) {
		this.regiao = regiao;
	}

	public int getVersao() {
		return versao;
	}

	public void setVersao(int versao) {
		this.versao = versao;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}