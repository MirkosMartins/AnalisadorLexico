package source;

import java.io.BufferedReader;
import java.io.File;

import com.opencsv.CSVWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Analisador lexico 2023
 * @author mirkos
 *
 */
public class ALex {
	String[] pReservadas = 
		{"auto","else","long","switch",
				"break","enum","register","typedef",
				"case","extern","return","union",
				"char","float","short","unsigned",
				"const","for","signed","void",
				"continue","goto","sizeof","volatile",
				"default","if","static","while",
				"do","int","struct","double"};
		
	String estadoInicial;
	String estadoAtual;
	LinkedList<String> estados = new LinkedList<String>();
	LinkedList<EstadoFinal> estadosFinais = new LinkedList<EstadoFinal>();
	LinkedList<RegraTransicao> regrastransicao = new LinkedList<RegraTransicao>();
	LinkedList<Token> tabelaSimbolos = new LinkedList<Token>();
	List<String> fonte;
	Stack<Token> pilhaP = new Stack<Token>();//Pilha Parentesis
	Stack<Token> pilhaCh = new Stack<Token>();//Pilha Chaves
	Stack<Token> pilhaCo = new Stack<Token>();//Pilha Colchete
	
	public void le_arquivo(String nomeArquivo) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(nomeArquivo));
			String linha = br.readLine();
			int index=0;
			while(linha!=null) {//percorre as linhas
				linha = linha.trim();
				if(index==0) {//primeira linha
					//estou lendo a linha dos nomes dos estados
					String nomesE[] = linha.split(",");
					for(int i=0;i<nomesE.length;i++)
						estados.add(nomesE[i]);//adiciono os nomes no linkedList estados				
				}
				if(index==1) {//segunda linha
					//estou lendo o estado inicial
					estadoInicial = linha;
					estadoAtual = estadoInicial;
				}
				if(index==2) {//terceira linha
					//estados finais
					String ef[] = linha.split(",");
					for(int i=0;i<ef.length;i++) {
						//System.out.println(ef[i]);
						String efinal[] = ef[i].split(":");
						EstadoFinal estadoFinal = 
								new EstadoFinal(efinal[0],efinal[1]);
						estadosFinais.add(estadoFinal);
					}
						
				}
				if(index>=3) {//quarta linha em diante
					//lendo as regras de transicao
					String rt[] = linha.split(":");
					RegraTransicao regra = 
							new RegraTransicao(rt[0],rt[1],rt[2]);
					regrastransicao.add(regra);
				}
				linha = br.readLine();
				index++;
			}
			br.close();//fecha o arquivo de conf.
		} catch (FileNotFoundException e) {
			System.out.println("Arquivo nao encontrado.");
		} catch (IOException e) {
			System.out.println("Nao foi possivel abrir o arquivo.");
		}
	}
	
	
	public String reconheceSimbolo(String termo,int linha) {
		estadoAtual = estadoInicial;
		String bufferToken="";
		String bufferEstado="";
		for (int i=0;i<termo.length();i++) {
			char letra = termo.charAt(i);

			for(int j=0;j<regrastransicao.size();j++) {
				RegraTransicao regraAtual = 
						regrastransicao.get(j);
				if(regraAtual.estadoinicial.
						equals(estadoAtual)
					&& regraAtual.simbolos.
							contains(String.valueOf(letra))) {
					estadoAtual = regraAtual.estadofinal;
					//System.out.println(letra+" :regra atual saiu "+regraAtual.estadoinicial+ " para "+regraAtual.estadofinal);
					bufferToken = bufferToken.concat(String.valueOf(letra));
					bufferEstado = estadoAtual;
					if(i==termo.length()-1) {
						//System.out.println("*Reconhecido: tk "
								//+bufferToken+" estadoAtual "+bufferEstado);
						//TODO CRIAR OBJETO TOKEN
						criaToken(linha, bufferToken, bufferEstado);
						break;
					}else {
						break;
					}
					
				}else {//termo nao reconhecido
					if(j==regrastransicao.size()-1) {
						//System.out.println("Reconhecido: tk "
					//+bufferToken+" estadoAtual "+bufferEstado);
						//System.out.println(letra+" Nao reconhecido");

					estadoAtual = estadoInicial;
					criaToken(linha, bufferToken, bufferEstado);
					bufferToken="";
					bufferEstado="";
					i--;
					break;
					}
				}
			}
		}
		return "";
	}


	private void criaToken(int linha, String bufferToken, String bufferEstado) {
		Token tk = new Token();
		int id = tabelaSimbolos.size()+1;
		tk.setId(id);

		tk.setTokenValue(bufferToken);
		tk.setTokenType(buscaEFinais(bufferEstado));
		for(int i=0;i<pReservadas.length;i++) {
			if(pReservadas[i].equals(bufferToken)) {
				tk.setTokenType(pReservadas[i].toUpperCase());
				break;
			}
		}
		tk.setLine(linha);
		//System.out.println(tk.getProperties());
		if(tk.tokenValue.equals("{")) {
			pilhaCh.push(tk);
		}
		if(tk.tokenValue.equals("}")) {
			if(pilhaCh.empty()) {
				tk.setRef(-1);
			}else {
				tk.setRef(pilhaCh.pop().getId());
			}
			//tabelaSimbolos.add(tk);
		}
		if(tk.tokenValue.equals("[")) {
			pilhaCo.push(tk);
		}
		if(tk.tokenValue.equals("]")) {
			if(pilhaCo.empty()) {
				tk.setRef(-1);
			}else {
				tk.setRef(pilhaCo.pop().getId());
			}
			//tabelaSimbolos.add(tk);
		}
		if(tk.tokenValue.equals("(")) {
			//System.out.println("AP");
			pilhaP.push(tk);
		}
		if(tk.tokenValue.equals(")")) {
			int id_abre;
			if(!pilhaP.empty()) {
				id_abre = pilhaP.pop().id;
			}else {
				id_abre = -1;
		}
			
			tk.setRef(id_abre);
		}//--fim fecha parentesis
		
		tabelaSimbolos.add(tk);
	}


	private String buscaEFinais(String estado) {
		String mensagem="";
		for(int j=0;j<estadosFinais.size();j++) {
			EstadoFinal ef = estadosFinais.get(j);
			//System.out.println(ef.nomeestado+"contains"+estadoAtual);
			if(ef.nomeestado.equals(estado)) {
				mensagem = ef.tipo;
				break;
			}
		}
		return mensagem;
	}
	
	public void le_codigofonte(String nomeArquivo) {
		Path path = Paths.get(nomeArquivo);
		try {
			fonte = Files.readAllLines(path, StandardCharsets.UTF_8);
            //fonte.stream().forEach(System.out::println);
		}
		catch(IOException e) {
			System.out.println
			("Nao foi possivel abrir o arquivo");
		}	
	}
	
	public void reconhece(ALex alex) {
		int []idx= {1};
		fonte.stream().forEach(linha->reconheceSimbolo(linha.replace("\t", ""),idx[0]++));
	}

	public static void main(String[] args) {
		ALex alex = new ALex();
		alex.le_arquivo("configuracao.txt");
		//System.out.println(alex.reconheceSimbolo("-300a5"));
		alex.le_codigofonte("codigofonte.cc");
		alex.reconhece(alex);
		alex.gravaCSV_();
	}

	private void gravaCSV_() {
		try {
			FileWriter fw = new FileWriter(new File("saida2.csv"));
			fw.write(tabelaSimbolos.get(0).getCSV_Title_());
			for(int idx=0;idx<tabelaSimbolos.size();idx++) {
				fw.write("\n"+tabelaSimbolos.get(idx).getProperties());
			}
			System.out.println("Salvou.");
			fw.close();
		} catch (IOException e) {
			System.out.println("Nao foi possivel gravar o arquivo");
		}
	}

	private void gravaCSV() {
		String filename = "saida.csv";
		try {
			//FileWriter file = new FileWriter(filename);
			CSVWriter csv_out = new CSVWriter
					(new FileWriter(filename), CSVWriter.DEFAULT_SEPARATOR,
							CSVWriter.NO_ESCAPE_CHARACTER, 
							CSVWriter.DEFAULT_ESCAPE_CHARACTER, 
							CSVWriter.DEFAULT_LINE_END);
			
			csv_out.writeNext(tabelaSimbolos.get(0).getCSV_Title());
			for(int idx=0;idx<tabelaSimbolos.size();idx++) {
				csv_out.writeNext(tabelaSimbolos.get(idx).getProperties_());			
			}
			csv_out.close();
		} catch (IOException e) {
			System.out.println("Nao gravou");
		}	
		
	}

}
