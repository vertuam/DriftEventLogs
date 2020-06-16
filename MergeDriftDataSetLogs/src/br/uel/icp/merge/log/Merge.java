package br.uel.icp.merge.log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Merge {

	public static void main(String[] args) {

		List<String> logNormal = new ArrayList<String>();
		List<String> logDrifted = new ArrayList<String>();
		
		//String tipoDrift = "sudden";
		//String tipoDrift = "recourrent";
		String tipoDrift = "gradual";
		//String tipoDrift = "incremental";
		
		final File folderNormal = new File("/Users/vertuam/Desktop/Artigo/ICPM_2020_Clustering/Experimentacao/logs/anomalyDetectionInEventLogs/normal");
		final File folderDrifted = new File("/Users/vertuam/Desktop/Artigo/ICPM_2020_Clustering/Experimentacao/logs/anomalyDetectionInEventLogs/drifted");
		
		logNormal = listFilesForFolder(folderNormal);
		logDrifted = listFilesForFolder(folderDrifted);

		// Carrega dois logs files para sudden, recourrent e gradual
		String csvFile_a = "/Users/vertuam/Desktop/Artigo/ICPM_2020_Clustering/Experimentacao/logsMergeJava/original/small-0.1-1.csv";
		String csvFile_b = "/Users/vertuam/Desktop/Artigo/ICPM_2020_Clustering/Experimentacao/logsMergeJava/original/small_2-0.1-1.csv";

		// Apenas para incremental
		String csvFile_c = "/Users/vertuam/Desktop/Artigo/ICPM_2020_Clustering/Experimentacao/logsMergeJava/original/small_3-0.1-1.csv";
		String csvFile_d = "/Users/vertuam/Desktop/Artigo/ICPM_2020_Clustering/Experimentacao/logsMergeJava/original/small_4-0.1-1.csv";
		String csvFile_e = "/Users/vertuam/Desktop/Artigo/ICPM_2020_Clustering/Experimentacao/logsMergeJava/original/small_5-0.1-1.csv";

        /*if (tipoDrift.equals("sudden")) {
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
        } else if (tipoDrift.equals("gradual")) {
        	MergeLogs mergeLogs = new MergeLogs(csvFile_a, csvFile_b, csvFile_c, csvFile_d, csvFile_e, csvFile_a.split("-")[0].split("/")[9], tipoDrift);
        	List<Event> processos = new ArrayList<Event>();
        	processos = mergeLogs.doMergeGradual();
        	Util.save(processos, MergeLogs.cabecalho, csvFile_a.split("-")[0].split("/")[9], tipoDrift);
        }*/

	}
	
	public static List<String> listFilesForFolder(final File folder) {
		List<String> logs = new ArrayList<String>();
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else {
	        	logs.add(fileEntry.getAbsolutePath()+"/"+fileEntry.getName());
	        }
	    }
		return logs;
	}

}
