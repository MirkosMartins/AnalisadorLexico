package source;

public class Celula {
	//atributos
	boolean estado=false;//false=morto true=vivo
	//posicao da celula na grade
	int coluna;
	int linha;
	int ln;
	int cn;
	Celula[] vizinhos = new Celula[8];//vizinhanca
	
	public void determinaVizinhanca(int tamanho,Celula[][] matriz) {
		if(this.linha==0) {
			ln = tamanho-1;
		}else {
			ln = linha-1;	
			}
		if(this.coluna==0) {
			cn = tamanho-1;
		}else {
			cn = coluna-1;
		}
		vizinhos[0]=matriz[ln][cn];//primeiro vizinho
		vizinhos[1]=matriz[ln][coluna];//segundo vizinho
		vizinhos[3]=matriz[linha][cn];//quarto vizinho
		
		if(linha+1==tamanho) {
			ln=0;
		}else {
			ln=linha+1;
		}
		vizinhos[5]=matriz[ln][cn];//sexto vizinho *
		if(this.coluna+1==tamanho) {
			cn = 0;
		}else {
			cn = coluna+1;
		}
		vizinhos[2]=matriz[ln][cn];//terceiro vizinho
		vizinhos[4]=matriz[linha][cn];//quinto vizinho
		
		vizinhos[6]=matriz[ln][coluna];//setimo vizinho
		vizinhos[7]=matriz[ln][cn];//oitavo vizinho
	}
	
	public void mostraVizinhos() {
		for(int x=0;x<8;x++) {
			System.out.print(vizinhos[x].estado+"\t");
		}
		System.out.println();
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public int getColuna() {
		return coluna;
	}
	public void setColuna(int coluna) {
		this.coluna = coluna;
	}
	public int getLinha() {
		return linha;
	}
	public void setLinha(int linha) {
		this.linha = linha;
	}
	
	
}
