package source;

import java.util.Random;
import java.util.random.*;

public class Principal {
	
	//grade
	int tamanho=4;
	public Celula[][] matriz = new Celula[tamanho][tamanho];
	
	//inicializa a matriz com celulas mortas
	public void inicializa() {
		for(int l=0;l<tamanho;l++) {
			for(int c=0;c<tamanho;c++) {
				Celula cel = new Celula();
				cel.estado=false;
				cel.linha=l;
				cel.coluna=c;
				matriz[l][c]=cel;
			}
		}
	}	
	
	//inicializa a matriz com celulas aleatorias
	public void inicializaR() {
		Random r = new Random();
		for(int l=0;l<tamanho;l++) {
			for(int c=0;c<tamanho;c++) {
				Celula cel = new Celula();
				int valor = r.nextInt();
				if (valor%2==0) {
					cel.estado = false;
				}else {
					cel.estado=true;
				}				
				cel.linha=l;
				cel.coluna=c;
				matriz[l][c]=cel;
			}
		}
	}
	//mostra o conteudo de uma matriz (GERACAO)
	public void mostra() {
		for(int l=0;l<tamanho;l++) {
			for(int c=0;c<tamanho;c++) {
				System.out.print((matriz[l][c]).estado+"\t");
			}
			System.out.println();
		}
	}
	

	public static void main(String[] args) {
		Principal p = new Principal();
		p.inicializaR();
		p.mostra();
		p.determinaVizinhos(p.matriz);
		//matriz[1][1].determinaVizinhanca(p.tamanho, p.matriz);
		//p.matriz[1][1].mostraVizinhos();

	}

	private void determinaVizinhos(Celula[][] matriz) {
		int tamanho = matriz.length;
		for(int l=0;l<tamanho;l++) {
			for(int c=0;c<tamanho;c++) {
				matriz[l][c].determinaVizinhanca(tamanho, matriz);
			}
		}		
	}

	public int contaVivos(Celula lista[]) {
		int valor =0;
		for(int x=0;x<lista.length;x++) {
			if(lista[x].isEstado()==true) {
				valor++;
			}
		}
		return valor;
	}
	
	public Celula aplicaRegra(Celula celula) {
		int vizinhosVivos = contaVivos(celula.vizinhos);
		if(celula.isEstado()) {//celula viva
			if(vizinhosVivos<2) {
				celula.setEstado(false);
			}
			if(vizinhosVivos>3) {
				celula.setEstado(false);
			}
		}else {//celula morta
			if(vizinhosVivos==3) {
				celula.setEstado(true);
			}
		}
		return celula;
	}
	
	public Celula[][] novaGeracao(Celula matriz[][]){
		Celula[][] resultante = matriz.clone();
		int tamanho = resultante.length;
		for(int linha = 0;linha<tamanho;linha++) {
			for(int coluna=0;coluna<tamanho;coluna++) {
				resultante[linha][coluna]=
						aplicaRegra(matriz[linha][coluna])
			}
		}
		return resultante;
	}
	
	
	


}
