package br.uel.icp.merge.log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Merge {
	
	static List<String> logNormal = new ArrayList<String>();
	static List<String> logDrifted = new ArrayList<String>();

	public static void main(String[] args) {
		
		/*String tipoDrift = args[0];
		
		for (String string : args) {
			System.out.println(":: "+string+" ::");
		}*/
		
		//String tipoDrift = "sudden";
		//String tipoDrift = "recourrent";
		//String tipoDrift = "gradual";
		String tipoDrift = "incremental";
		
		final File folderNormal = new File("/Users/vertuam/Desktop/Artigo/ICPM_2020_Clustering/Experimentacao/logs/anomalyDetectionInEventLogs/normal");
		final File folderDrifted = new File("/Users/vertuam/Desktop/Artigo/ICPM_2020_Clustering/Experimentacao/logs/anomalyDetectionInEventLogs/drifted");
		
		listFilesForFolderNormal(folderNormal);
		listFilesForFolderDrifted(folderDrifted);
		
		// Carrega dois logs files para sudden, recourrent e gradual para incremental carrega os dois primeiros 
		//String csvFile_a = "/Users/vertuam/Desktop/Artigo/ICPM_2020_Clustering/Experimentacao/logsMergeJava/original/small-0.1-1.csv";
		//String csvFile_b = "/Users/vertuam/Desktop/Artigo/ICPM_2020_Clustering/Experimentacao/logsMergeJava/original/small_2-0.0-1.csv";
	
		// Apenas para incremental carregar os 3 ultimos de um total de 5 logos para construir o incremental
		//String csvFile_c = "/Users/vertuam/Desktop/Artigo/ICPM_2020_Clustering/Experimentacao/logsMergeJava/original/small_3-0.0-1.csv";
		//String csvFile_d = "/Users/vertuam/Desktop/Artigo/ICPM_2020_Clustering/Experimentacao/logsMergeJava/original/small_4-0.0-1.csv";
		// csvFile_e = "/Users/vertuam/Desktop/Artigo/ICPM_2020_Clustering/Experimentacao/logsMergeJava/original/small_5-0.0-1.csv";
		
		for (String csvFile_a : logNormal) {
			if (!csvFile_a.contains(".DS_Store")) {
				System.out.println("");
				System.out.println("");
				System.out.println("");
				System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------");
				System.out.println("Normal: "+csvFile_a);
				System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------");
				if (tipoDrift.equals("sudden")) {
					for (String csvFile_b : logDrifted) {
						if (!csvFile_b.contains(".DS_Store") && csvFile_a.split("/")[csvFile_a.split("/").length-1].equals(csvFile_b.split("/")[csvFile_b.split("/").length-1].replace("_2", ""))) {
							System.out.println("Normal: "+csvFile_a);
							System.out.println("Drift: "+csvFile_b);
							System.out.println("Will merge!");
							MergeLogs mergeLogs = new MergeLogs(csvFile_a, csvFile_b, csvFile_a.split("-")[0].split("/")[9], tipoDrift);
				        	List<Event> processos = new ArrayList<Event>();
				        	processos = mergeLogs.doMergeSudden();
				        	Util.save(processos, MergeLogs.cabecalho, csvFile_a.split("/")[9], tipoDrift, csvFile_a.split("/")[csvFile_a.split("/").length-1], csvFile_a.split("/")[10]);
				        	System.out.println("Merged!");
				        	break;
						}
					}
		        } else if (tipoDrift.equals("recourrent")) {
		        	for (String csvFile_b : logDrifted) {
						if (!csvFile_b.contains(".DS_Store") && csvFile_a.split("/")[csvFile_a.split("/").length-1].equals(csvFile_b.split("/")[csvFile_b.split("/").length-1].replace("_2", ""))) {
							System.out.println("Normal: "+csvFile_a);
							System.out.println("Drift: "+csvFile_b);
							System.out.println("Will merge!");
							MergeLogs mergeLogs = new MergeLogs(csvFile_a, csvFile_b, csvFile_a.split("-")[0].split("/")[9], tipoDrift);
				        	List<Event> processos = new ArrayList<Event>();
				        	processos = mergeLogs.doMergeRecourrent();
				        	Util.save(processos, MergeLogs.cabecalho, csvFile_a.split("/")[9], tipoDrift, csvFile_a.split("/")[csvFile_a.split("/").length-1], csvFile_a.split("/")[10]);
				        	System.out.println("Merged!");
				        	break;
						}
					}
		        } else if (tipoDrift.equals("gradual")) {
		        	for (String csvFile_b : logDrifted) {
						if (!csvFile_b.contains(".DS_Store") && csvFile_a.split("/")[csvFile_a.split("/").length-1].equals(csvFile_b.split("/")[csvFile_b.split("/").length-1].replace("_2", ""))) {
							System.out.println("Normal: "+csvFile_a);
							System.out.println("Drift: "+csvFile_b);
							System.out.println("Will merge!");
							MergeLogs mergeLogs = new MergeLogs(csvFile_a, csvFile_b, csvFile_a.split("-")[0].split("/")[9], tipoDrift);
				        	List<Event> processos = new ArrayList<Event>();
				        	processos = mergeLogs.doMergeGradual();
				        	Util.save(processos, MergeLogs.cabecalho, csvFile_a.split("/")[9], tipoDrift, csvFile_a.split("/")[csvFile_a.split("/").length-1], csvFile_a.split("/")[10]);
				        	System.out.println("Merged!");
				        	break;
						}
					}
		        } else if (tipoDrift.equals("incremental")) {
		        	for (String csvFile_b : logDrifted) {
						if (!csvFile_b.contains(".DS_Store") && csvFile_a.split("/")[csvFile_a.split("/").length-1].equals(csvFile_b.split("/")[csvFile_b.split("/").length-1].replace("_2", ""))) {
							System.out.println("Normal: "+csvFile_a);
							System.out.println("Drift: "+csvFile_b);
							System.out.println("Will merge!");
							if (csvFile_b.contains("small")) {
								MergeLogs mergeLogs = new MergeLogs(csvFile_a, csvFile_b, csvFile_b.replace("_2-", "_3-"), csvFile_b.replace("_2-", "_4-"), csvFile_b.replace("_2-", "_5-"), csvFile_a.split("-")[0].split("/")[9], tipoDrift);
					        	List<Event> processos = new ArrayList<Event>();
					        	processos = mergeLogs.doMergeIncremental();
					        	Util.save(processos, MergeLogs.cabecalho, csvFile_a.split("/")[9], tipoDrift, csvFile_a.split("/")[csvFile_a.split("/").length-1], csvFile_a.split("/")[10]);
					        	System.out.println("Merged!");
					        	break;	
							}
						}
					}
		        }
				System.out.println("");
				System.out.println("");
				System.out.println("");
			}
		}

        /*if (tipoDrift.equals("sudden")) {
        	MergeLogs mergeLogs = new MergeLogs(csvFile_a, csvFile_b, csvFile_a.split("-")[0].split("/")[9], tipoDrift);
        	List<Event> processos = new ArrayList<Event>();
        	processos = mergeLogs.doMergeSudden();
        	Util.save(processos, MergeLogs.cabecalho, csvFile_a.split("/")[9], tipoDrift);
        } else if (tipoDrift.equals("recourrent")) {
        	MergeLogs mergeLogs = new MergeLogs(csvFile_a, csvFile_b, csvFile_a.split("-")[0].split("/")[9], tipoDrift);
        	List<Event> processos = new ArrayList<Event>();
        	processos = mergeLogs.doMergeRecourrent();
        	Util.save(processos, MergeLogs.cabecalho, csvFile_a.split("/")[9], tipoDrift);
        } else if (tipoDrift.equals("gradual")) {
        	MergeLogs mergeLogs = new MergeLogs(csvFile_a, csvFile_b, csvFile_a.split("-")[0].split("/")[9], tipoDrift);
        	List<Event> processos = new ArrayList<Event>();
        	processos = mergeLogs.doMergeGradual();
        	Util.save(processos, MergeLogs.cabecalho, csvFile_a.split("/")[9], tipoDrift);
        } else if (tipoDrift.equals("incremental")) {
        	MergeLogs mergeLogs = new MergeLogs(csvFile_a, csvFile_b, csvFile_c, csvFile_d, csvFile_e, csvFile_a.split("-")[0].split("/")[9], tipoDrift);
        	List<Event> processos = new ArrayList<Event>();
        	processos = mergeLogs.doMergeIncremental();
        	Util.save(processos, MergeLogs.cabecalho, csvFile_a.split("/")[9], tipoDrift);
        } else if (tipoDrift.equals("--help")) {
        	System.out.println("");
        	System.out.println("PARAMETROS");
        	System.out.println("-----------------------------------------");
        	System.out.println("");
        	System.out.println("tipoDrift diretorio_log_normal diretorio_log_driftado");
        	System.out.println("");
        	System.out.println("+---------------------------------------+");
        	System.out.println("|       TIPOS DE DRIFT SUPORTADOS       |");
        	System.out.println("+---------------------------------------+");
        	System.out.println("| SUDDEN RECOURRENT GRADUAL OMCRMENTAL  |");
        	System.out.println("+---------------------------------------+");
        } else {
        	System.out.println("--help para ajuda");
        }*/

	}
	
	public static void doSudden() {
		System.out.println("Fazer merde sudden!");
	}
	
	public static void listFilesForFolderNormal(final File folder) {
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	listFilesForFolderNormal(fileEntry);
	        } else {
	        	logNormal.add(fileEntry.getAbsolutePath());
	        }
	    }
	}
	
	public static void listFilesForFolderDrifted(final File folder) {
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	listFilesForFolderDrifted(fileEntry);
	        } else {
	        	logDrifted.add(fileEntry.getAbsolutePath());
	        }
	    }
	}

}
