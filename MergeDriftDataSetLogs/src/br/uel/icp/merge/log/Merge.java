package br.uel.icp.merge.log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Merge {
	
	static List<String> logNormal = new ArrayList<String>();
	static List<String> logDrifted = new ArrayList<String>();

	public static void main(String[] args) {
		
		//String tipoDrift = "";
		
		//String tipoDrift = "sudden";
		//String tipoDrift = "recourrent";
		//String tipoDrift = "gradual";
		String tipoDrift = "incremental";
		
		final File folderNormal = new File("/Users/vertuam/Desktop/Artigo/ICPM_2020_Clustering/Experimentacao/logs/anomalyDetectionInEventLogs/normal");
		final File folderDrifted = new File("/Users/vertuam/Desktop/Artigo/ICPM_2020_Clustering/Experimentacao/logs/anomalyDetectionInEventLogs/drifted");
		
		listFilesForFolderNormal(folderNormal);
		listFilesForFolderDrifted(folderDrifted);

		// Carrega dois logs files para sudden, recourrent e gradual
		String csvFile_a = "/Users/vertuam/Desktop/Artigo/ICPM_2020_Clustering/Experimentacao/logsMergeJava/original/small-0.1-1.csv";
		String csvFile_b = "/Users/vertuam/Desktop/Artigo/ICPM_2020_Clustering/Experimentacao/logsMergeJava/original/small_2-0.0-1.csv";

		// Apenas para incremental
		String csvFile_c = "/Users/vertuam/Desktop/Artigo/ICPM_2020_Clustering/Experimentacao/logsMergeJava/original/small_3-0.0-1.csv";
		String csvFile_d = "/Users/vertuam/Desktop/Artigo/ICPM_2020_Clustering/Experimentacao/logsMergeJava/original/small_4-0.0-1.csv";
		String csvFile_e = "/Users/vertuam/Desktop/Artigo/ICPM_2020_Clustering/Experimentacao/logsMergeJava/original/small_5-0.0-1.csv";

        if (tipoDrift.equals("sudden")) {
        	MergeLogs mergeLogs = new MergeLogs(csvFile_a, csvFile_b, csvFile_a.split("-")[0].split("/")[9], tipoDrift);
        	List<Event> processos = new ArrayList<Event>();
        	processos = mergeLogs.doMergeSudden();
        	Util.save(processos, MergeLogs.cabecalho, csvFile_a.split("-")[0].split("/")[9], tipoDrift);
        } else if (tipoDrift.equals("recourrent")) {
        	MergeLogs mergeLogs = new MergeLogs(csvFile_a, csvFile_b, csvFile_a.split("-")[0].split("/")[9], tipoDrift);
        	List<Event> processos = new ArrayList<Event>();
        	processos = mergeLogs.doMergeRecourrent();
        	Util.save(processos, MergeLogs.cabecalho, csvFile_a.split("-")[0].split("/")[9], tipoDrift);
        } else if (tipoDrift.equals("gradual")) {
        	MergeLogs mergeLogs = new MergeLogs(csvFile_a, csvFile_b, csvFile_a.split("-")[0].split("/")[9], tipoDrift);
        	List<Event> processos = new ArrayList<Event>();
        	processos = mergeLogs.doMergeGradual();
        	Util.save(processos, MergeLogs.cabecalho, csvFile_a.split("-")[0].split("/")[9], tipoDrift);
        } else if (tipoDrift.equals("incremental")) {
        	MergeLogs mergeLogs = new MergeLogs(csvFile_a, csvFile_b, csvFile_c, csvFile_d, csvFile_e, csvFile_a.split("-")[0].split("/")[9], tipoDrift);
        	List<Event> processos = new ArrayList<Event>();
        	processos = mergeLogs.doMergeIncremental();
        	Util.save(processos, MergeLogs.cabecalho, csvFile_a.split("-")[0].split("/")[9], tipoDrift);
        }

	}
	
	public static void listFilesForFolderNormal(final File folder) {
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	listFilesForFolderNormal(fileEntry);
	        } else {
	        	logNormal.add(fileEntry.getAbsolutePath()+"/"+fileEntry.getName());
	        }
	    }
	}
	
	public static void listFilesForFolderDrifted(final File folder) {
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	listFilesForFolderDrifted(fileEntry);
	        } else {
	        	logDrifted.add(fileEntry.getAbsolutePath()+"/"+fileEntry.getName());
	        }
	    }
	}

}
