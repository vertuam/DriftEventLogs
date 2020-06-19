package br.uel.icp.merge.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class Util {

	public static boolean save(List<Event> merged, String cabecalho, String logType, String driftType) {
		try {
			// Colocar no nome do log o tipo smal, big, giagnt + o tipo do drift
			File file = new File("/Users/vertuam/Desktop/Artigo/ICPM_2020_Clustering/Experimentacao/logsMergeJava/merged/"+logType.replace(".csv", "")+"_"+driftType+".csv");
	        FileWriter fw = new FileWriter(file);
	        BufferedWriter bw = new BufferedWriter(fw);
	        String[] newCabecalho = cabecalho.split(",");
	        bw.write(newCabecalho[0]+","+newCabecalho[1]+","+newCabecalho[2]+","+newCabecalho[3]+","+newCabecalho[4]+","+"case"+","+newCabecalho[5]+","+newCabecalho[6]);
	        bw.newLine();
	        for(int i=0; i < merged.size(); i++) {
	        	// case_id,event_position,activity_name,timestamp,label,anomaly_type,anomaly_description
	        	bw.write(merged.get(i).getCaseId()+","+merged.get(i).getEventPosition()+","+merged.get(i).getActivityName()+","+merged.get(i).getTimeStamp()+","+merged.get(i).getLabel()+","+merged.get(i).getClasse()+","+merged.get(i).getAnomalyType()+",\""+merged.get(i).getAnomalyDescription()+"\"");
	            bw.newLine();
	        }
	        bw.close();
	        fw.close();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

}