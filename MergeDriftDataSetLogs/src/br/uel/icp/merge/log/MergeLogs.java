package br.uel.icp.merge.log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MergeLogs {

	private String csvFile_a;
	private String csvFile_b;
	private String csvFile_c;
	private String csvFile_d;
	private String csvFile_e;
	private String logType;
	@SuppressWarnings("unused")
	private String driftType;

	static String cabecalho = "";

	private String timeProcessStart = "";
	
	private int mantemHoraInicio = 60;
	
	public MergeLogs(String csvFile_a, String csvFile_b, String logType, String tipoDrift) {
		this.csvFile_a = csvFile_a;
		this.csvFile_b = csvFile_b;
		this.logType = logType;
		this.driftType = tipoDrift;
	}
	
	public MergeLogs(String csvFile_a, String csvFile_b, String csvFile_c, String csvFile_d, String csvFile_e, String logType, String tipoDrift) {
		this.csvFile_a = csvFile_a;
		this.csvFile_b = csvFile_b;
		this.csvFile_c = csvFile_c;
		this.csvFile_d = csvFile_d;
		this.csvFile_e = csvFile_e;
		this.logType = logType;
		this.driftType = tipoDrift;
	}

	@SuppressWarnings("resource")
	public List<Event> doMergeSudden() {
		String line = "";
		List<Event> events = new ArrayList<Event>();
		BufferedReader br = null;
		
		int sequence = 0;
		
		int casoAtual = 0;
		int contaCaso = 0;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		String timeStart = simpleDateFormat.format(new Date());
		String timeUpdated = simpleDateFormat.format(new Date());
		timeUpdated = returntimeStamp(0, timeStart, 0);
		
		try {
			br = new BufferedReader(new FileReader(csvFile_a));
			while ((line = br.readLine()) != null) {
				String opa = line;
				if (opa.contains("activity_name")) {
					cabecalho = line;
					System.out.println("Ignorando cabeçalho primeiro log.");
				} else {
					Event event = new Event();
					event.setCaseId(Integer.valueOf(line.split(",")[0]));
					event.setEventPosition(Integer.valueOf(line.split(",")[1]));
					event.setActivityName(String.valueOf(line.split(",")[2]));
					if (String.valueOf(line.split(",")[2]).equals("▶")
							|| String.valueOf(line.split(",")[2]).equals("■")) {
						event.setTimeStamp("-");
						event.setLabel("-");
						event.setAnomalyType("-");
						event.setAnomalyDescription("-");
						event.setClasse("-");
					} else {
						timeUpdated = returntimeStamp(event.getEventPosition(), timeUpdated, casoAtual);
						event.setTimeStamp(timeUpdated);
						event.setLabel(String.valueOf(line.split(",")[4]));
						event.setAnomalyType(String.valueOf(line.split(",")[5]));
						event.setAnomalyDescription(line.split("\"").length > 1 ? line.split("\"")[1] : "-");
						event.setClasse(logType);
					}
					events.add(event);
					sequence = Integer.valueOf(line.split(",")[0]);
					if (casoAtual!=Integer.valueOf(line.split(",")[0])) {
						if (contaCaso>=60) {
							timeUpdated = returntimeStamp(999, timeStart, 0);
							timeStart = timeUpdated;
							contaCaso=0;
						} else {
							timeUpdated = returntimeStamp(0, timeStart, 0);
						}
						contaCaso++;
					}
					casoAtual = Integer.valueOf(line.split(",")[0]);
				}
			}
			int caseId = 0;
			br = new BufferedReader(new FileReader(csvFile_b));
			while ((line = br.readLine()) != null) {
				String opa = line;
				if (opa.contains("activity_name")) {
					System.out.println("Ignorando cabeçalho segundo log.");
				} else {
					if (caseId != Integer.valueOf(line.split(",")[0])) {
						caseId = Integer.valueOf(line.split(",")[0]);
						sequence++;
					}
					Event event = new Event();
					event.setCaseId(sequence);
					event.setEventPosition(Integer.valueOf(line.split(",")[1]));
					event.setActivityName(String.valueOf(line.split(",")[2]));
					if (String.valueOf(line.split(",")[2]).contains("▶")
							|| String.valueOf(line.split(",")[2]).contains("■")) {
						event.setTimeStamp("-");
						event.setLabel("-");
						event.setAnomalyType("-");
						event.setAnomalyDescription("-");
						event.setClasse("-");
					} else {
						timeUpdated = returntimeStamp(event.getEventPosition(), timeUpdated, casoAtual);
						event.setTimeStamp(timeUpdated);
						event.setLabel(String.valueOf(line.split(",")[4]));
						event.setAnomalyType(String.valueOf(line.split(",")[5]));
						event.setAnomalyDescription(line.split("\"").length > 1 ? line.split("\"")[1] : "-");
						event.setClasse(logType + "_drifted");
					}
					events.add(event);
					if (casoAtual!=Integer.valueOf(line.split(",")[0])) {
						timeUpdated = returntimeStamp(0, timeStart, 0);
						contaCaso++;
					}
					casoAtual = Integer.valueOf(line.split(",")[0]);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return events;
	}

	public List<Event> doMergeRecourrent() {
		int casoAtual = 0;
		int contaCaso = 0;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		String timeStart = simpleDateFormat.format(new Date());
		String timeUpdated = simpleDateFormat.format(new Date());
		timeUpdated = returntimeStamp(0, timeStart, 0);
		BufferedReader br = null;
		String line = "";
		List<Event> events = new ArrayList<Event>();
		Map<Integer, List<Event>> casos_normais = new HashMap<>();
		Map<Integer, List<Event>> casos_drifitados = new HashMap<>();
		try {
			br = new BufferedReader(new FileReader(csvFile_a));
			casoAtual = 0;
			int linha = 0;
			while ((line = br.readLine()) != null) {
				if (line.contains("activity_name")) {
					cabecalho = line;
					System.out.println("Ignorando cabeçalho primeiro log.");
				} else {
					linha = Integer.valueOf(line.split(",")[0]);
					if (casoAtual != linha) {
						casoAtual = linha;
						events = new ArrayList<Event>();
					}
					Event event = new Event();
					event.setCaseId(Integer.valueOf(line.split(",")[0]));
					event.setEventPosition(Integer.valueOf(line.split(",")[1]));
					event.setActivityName(String.valueOf(line.split(",")[2]));
					if (String.valueOf(line.split(",")[2]).contains("▶")
							|| String.valueOf(line.split(",")[2]).contains("■")) {
						event.setTimeStamp("");
						event.setLabel("");
						event.setAnomalyType("");
						event.setAnomalyDescription("");
						event.setClasse("");
					} else {
						event.setTimeStamp("");
						event.setLabel(String.valueOf(line.split(",")[4]));
						event.setAnomalyType(String.valueOf(line.split(",")[5]));
						event.setAnomalyDescription(line.split("\"").length > 1 ? line.split("\"")[1] : "-");
						event.setClasse(logType);
					}
					events.add(event);
					casos_normais.put(casoAtual, events);
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			br = new BufferedReader(new FileReader(csvFile_a));
			casoAtual = 0;
			int linha = 0;
			while ((line = br.readLine()) != null) {
				if (line.contains("activity_name")) {
					System.out.println("Ignorando cabeçalho segundo log.");
				} else {
					linha = Integer.valueOf(line.split(",")[0]);
					if (casoAtual != linha) {
						casoAtual = linha;
						events = new ArrayList<Event>();
					}
					Event event = new Event();
					event.setCaseId(Integer.valueOf(line.split(",")[0]));
					event.setEventPosition(Integer.valueOf(line.split(",")[1]));
					event.setActivityName(String.valueOf(line.split(",")[2]));
					if (String.valueOf(line.split(",")[2]).contains("▶")
							|| String.valueOf(line.split(",")[2]).contains("■")) {
						event.setTimeStamp("");
						event.setLabel("");
						event.setAnomalyType("");
						event.setAnomalyDescription("");
						event.setClasse("");
					} else {
						event.setTimeStamp("");
						event.setLabel(String.valueOf(line.split(",")[4]));
						event.setAnomalyType(String.valueOf(line.split(",")[5]));
						event.setAnomalyDescription(line.split("\"").length > 1 ? line.split("\"")[1] : "-");
						event.setClasse(logType + "_drifted");
					}
					events.add(event);
					casos_drifitados.put(casoAtual, events);
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		events = new ArrayList<Event>();

		// Pegar o numeros de casos
		int caseNumbersN = casos_normais.size();
		System.out.println("Numero total de casos LOG Normal: " + caseNumbersN);

		// Divir por 4 para ter 4 mudancas
		int totalDrifts = caseNumbersN / 4;

		int drift_1 = totalDrifts;
		int normal_2 = totalDrifts * 2;
		int drift_2 = totalDrifts * 3;

		for (Entry<Integer, List<Event>> entry : casos_normais.entrySet()) {
			if (entry.getKey() <= drift_1) {
				List<Event> ls = entry.getValue();
				events.addAll(ls);
			}
		}
		
		for (Entry<Integer, List<Event>> entry : casos_drifitados.entrySet()) {
			if (entry.getKey() >= drift_1 + 1 && entry.getKey() <= normal_2) {
				List<Event> ls = entry.getValue();
				events.addAll(ls);
			}
		}
		
		for (Entry<Integer, List<Event>> entry : casos_normais.entrySet()) {
			if (entry.getKey() >= normal_2 + 1 && entry.getKey() <= drift_2) {
				List<Event> ls = entry.getValue();
				events.addAll(ls);
			}
		}
		
		for (Entry<Integer, List<Event>> entry : casos_drifitados.entrySet()) {
			if (entry.getKey() >= drift_2 + 1 && entry.getKey() <= caseNumbersN) {
				List<Event> ls = entry.getValue();
				events.addAll(ls);
			}
		}
		
		casoAtual = 0;

		for (Event event : events) {
			if (event.getActivityName().equals("▶") || event.getActivityName().equals("■")) {
				event.setTimeStamp("");
			} else {
				timeUpdated = returntimeStamp(event.getEventPosition(), timeUpdated, casoAtual);
				event.setTimeStamp(timeUpdated);
			}
			if (casoAtual!=event.getCaseId()) {
				if (contaCaso>=60) {
					timeUpdated = returntimeStamp(999, timeStart, 0);
					timeStart = timeUpdated;
					contaCaso=0;
				} else {
					timeUpdated = returntimeStamp(0, timeStart, 0);
				}
				contaCaso++;
			}
			casoAtual = event.getCaseId();
		}

		return events;

	}

	public List<Event> doMergeGradual() {
		int casoAtual = 0;
		int contaCaso = 0;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		String timeStart = simpleDateFormat.format(new Date());
		String timeUpdated = simpleDateFormat.format(new Date());
		timeUpdated = returntimeStamp(0, timeStart, 0);
		BufferedReader br = null;
		String line = "";
		List<Event> events = new ArrayList<Event>();
		Map<Integer, List<Event>> casos_normais = new HashMap<>();
		Map<Integer, List<Event>> casos_drifitados = new HashMap<>();
		try {
			br = new BufferedReader(new FileReader(csvFile_a));
			casoAtual = 0;
			int linha = 0;
			while ((line = br.readLine()) != null) {
				if (line.contains("activity_name")) {
					cabecalho = line;
					System.out.println("Ignorando cabeçalho primeiro log.");
				} else {
					linha = Integer.valueOf(line.split(",")[0]);
					if (casoAtual != linha) {
						casoAtual = linha;
						events = new ArrayList<Event>();
					}
					Event event = new Event();
					event.setCaseId(Integer.valueOf(line.split(",")[0]));
					event.setEventPosition(Integer.valueOf(line.split(",")[1]));
					event.setActivityName(String.valueOf(line.split(",")[2]));
					if (String.valueOf(line.split(",")[2]).contains("▶")
							|| String.valueOf(line.split(",")[2]).contains("■")) {
						event.setTimeStamp("");
						event.setLabel("");
						event.setAnomalyType("");
						event.setAnomalyDescription("");
						event.setClasse("");
					} else {
						event.setTimeStamp("");
						event.setLabel(String.valueOf(line.split(",")[4]));
						event.setAnomalyType(String.valueOf(line.split(",")[5]));
						event.setAnomalyDescription(line.split("\"").length > 1 ? line.split("\"")[1] : "-");
						event.setClasse(logType);
					}
					events.add(event);
					casos_normais.put(casoAtual, events);
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			br = new BufferedReader(new FileReader(csvFile_a));
			casoAtual = 0;
			int linha = 0;
			while ((line = br.readLine()) != null) {
				if (line.contains("activity_name")) {
					System.out.println("Ignorando cabeçalho segundo log.");
				} else {
					linha = Integer.valueOf(line.split(",")[0]);
					if (casoAtual != linha) {
						casoAtual = linha;
						events = new ArrayList<Event>();
					}
					Event event = new Event();
					event.setCaseId(Integer.valueOf(line.split(",")[0]));
					event.setEventPosition(Integer.valueOf(line.split(",")[1]));
					event.setActivityName(String.valueOf(line.split(",")[2]));
					if (String.valueOf(line.split(",")[2]).contains("▶")
							|| String.valueOf(line.split(",")[2]).contains("■")) {
						event.setTimeStamp("");
						event.setLabel("");
						event.setAnomalyType("");
						event.setAnomalyDescription("");
						event.setClasse("");
					} else {
						event.setTimeStamp("");
						event.setLabel(String.valueOf(line.split(",")[4]));
						event.setAnomalyType(String.valueOf(line.split(",")[5]));
						event.setAnomalyDescription(line.split("\"").length > 1 ? line.split("\"")[1] : "-");
						event.setClasse(logType + "_drifted");
					}
					events.add(event);
					casos_drifitados.put(casoAtual, events);
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		events = new ArrayList<Event>();

		// Pegar o numeros de casos
		int caseNumbersN = casos_normais.size();
		
		int normal_um = caseNumbersN * 39 / 100;
		int drift_um = caseNumbersN * 1 / 100;
		
		int normal_dois = caseNumbersN * 5 / 100;
		int drift_dois = caseNumbersN * 2 / 100;
		
		int normal_tres = caseNumbersN * 3 / 100;
		int drift_tres = caseNumbersN * 3 / 100;
		
		int normal_quatro = caseNumbersN * 2 / 100;
		int drift_quatro = caseNumbersN * 5 / 100;

		int normal_cinco = caseNumbersN * 1 / 100;
		int drift_cinco = caseNumbersN * 39 / 100;
		
		// Not drifted LOG - 1
		for (Entry<Integer, List<Event>> entry : casos_normais.entrySet()) {
			if (entry.getKey() <= normal_um) {
				List<Event> ls = entry.getValue();
				events.addAll(ls);
			}
		}
		// Drifted LOG - 1
		for (Entry<Integer, List<Event>> entry : casos_drifitados.entrySet()) {
			if (entry.getKey() >= normal_um+1 && entry.getKey() <= normal_um+drift_um) {
				List<Event> ls = entry.getValue();
				events.addAll(ls);
			}
		}
		
		// Not drifted LOG - 2
		for (Entry<Integer, List<Event>> entry : casos_normais.entrySet()) {
			if (entry.getKey() >= normal_um+drift_um+1 && entry.getKey() <= normal_um+normal_dois+drift_um) {
				List<Event> ls = entry.getValue();
				events.addAll(ls);
			}
		}
		// Drifted LOG - 2
		for (Entry<Integer, List<Event>> entry : casos_drifitados.entrySet()) {
			if (entry.getKey() >= normal_um+normal_dois+drift_um+1 && entry.getKey() <= normal_um+normal_dois+drift_um+drift_dois) {
				List<Event> ls = entry.getValue();
				events.addAll(ls);
			}
		}
		
		// Not drifted LOG - 3
		for (Entry<Integer, List<Event>> entry : casos_normais.entrySet()) {
			if (entry.getKey() >= normal_um+normal_dois+drift_um+drift_dois+1 && entry.getKey() <= normal_um+normal_dois+normal_tres+drift_um+drift_dois) {
				List<Event> ls = entry.getValue();
				events.addAll(ls);
			}
		}
		// Drifted LOG - 3
		for (Entry<Integer, List<Event>> entry : casos_drifitados.entrySet()) {
			if (entry.getKey() >= normal_um+normal_dois+normal_tres+drift_um+drift_dois+1 && entry.getKey() <= normal_um+normal_dois+normal_tres+drift_um+drift_dois+drift_tres) {
				List<Event> ls = entry.getValue();
				events.addAll(ls);
			}
		}
		
		// Not drifted LOG - 4
		for (Entry<Integer, List<Event>> entry : casos_normais.entrySet()) {
			if (entry.getKey() >= normal_um+normal_dois+normal_tres+drift_um+drift_dois+drift_tres+1 && entry.getKey() <= normal_um+normal_dois+normal_tres+normal_quatro+drift_um+drift_dois+drift_tres) {
				List<Event> ls = entry.getValue();
				events.addAll(ls);
			}
		}
		// Drifted LOG - 4
		for (Entry<Integer, List<Event>> entry : casos_drifitados.entrySet()) {
			if (entry.getKey() >= normal_um+normal_dois+normal_tres+normal_quatro+drift_um+drift_dois+drift_tres+1 && entry.getKey() <= normal_um+normal_dois+normal_tres+normal_quatro+drift_um+drift_dois+drift_tres+drift_quatro) {
				List<Event> ls = entry.getValue();
				events.addAll(ls);
			}
		}
		
		// Not drifted LOG - 5
		for (Entry<Integer, List<Event>> entry : casos_normais.entrySet()) {
			if (entry.getKey() >= normal_um+normal_dois+normal_tres+normal_quatro+drift_um+drift_dois+drift_tres+drift_quatro+1 && entry.getKey() <= normal_um+normal_dois+normal_tres+normal_quatro+normal_cinco+drift_um+drift_dois+drift_tres+drift_quatro) {
				List<Event> ls = entry.getValue();
				events.addAll(ls);
			}
		}
		// Drifted LOG - 5
		for (Entry<Integer, List<Event>> entry : casos_drifitados.entrySet()) {
			if (entry.getKey() >= normal_um+normal_dois+normal_tres+normal_quatro+normal_cinco+drift_um+drift_dois+drift_tres+drift_quatro+1 && entry.getKey() <= normal_um+normal_dois+normal_tres+normal_quatro+normal_cinco+drift_um+drift_dois+drift_tres+drift_quatro+drift_cinco) {
				List<Event> ls = entry.getValue();
				events.addAll(ls);
			}
		}
		
		for (Event event : events) {
			if (event.getActivityName().equals("▶") || event.getActivityName().equals("■")) {
				event.setTimeStamp("");
			} else {
				timeUpdated = returntimeStamp(event.getEventPosition(), timeUpdated, casoAtual);
				event.setTimeStamp(timeUpdated);
			}
			if (casoAtual!=event.getCaseId()) {
				if (contaCaso>=60) {
					timeUpdated = returntimeStamp(999, timeStart, 0);
					timeStart = timeUpdated;
					contaCaso=0;
				} else {
					timeUpdated = returntimeStamp(0, timeStart, 0);
				}
				contaCaso++;
			}
			casoAtual = event.getCaseId();
		}

		return events;
		
	}

	public List<Event> doMergeIncremental() {
		int casoAtual = 0;
		int contaCaso = 0;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		String timeStart = simpleDateFormat.format(new Date());
		String timeUpdated = simpleDateFormat.format(new Date());
		timeUpdated = returntimeStamp(0, timeStart, 0);
		BufferedReader br = null;
		String line = "";
		List<Event> events = new ArrayList<Event>();
		Map<Integer, List<Event>> casos_normais = new HashMap<>();
		Map<Integer, List<Event>> casos_drifitados1 = new HashMap<>();
		Map<Integer, List<Event>> casos_drifitados2 = new HashMap<>();
		Map<Integer, List<Event>> casos_drifitados3 = new HashMap<>();
		Map<Integer, List<Event>> casos_drifitados4 = new HashMap<>();
		try {
			br = new BufferedReader(new FileReader(csvFile_a));
			casoAtual = 0;
			int linha = 0;
			while ((line = br.readLine()) != null) {
				if (line.contains("activity_name")) {
					cabecalho = line;
					System.out.println("Ignorando cabeçalho primeiro log.");
				} else {
					linha = Integer.valueOf(line.split(",")[0]);
					if (casoAtual != linha) {
						casoAtual = linha;
						events = new ArrayList<Event>();
					}
					Event event = new Event();
					event.setCaseId(Integer.valueOf(line.split(",")[0]));
					event.setEventPosition(Integer.valueOf(line.split(",")[1]));
					event.setActivityName(String.valueOf(line.split(",")[2]));
					if (String.valueOf(line.split(",")[2]).contains("▶")
							|| String.valueOf(line.split(",")[2]).contains("■")) {
						event.setTimeStamp("");
						event.setLabel("");
						event.setAnomalyType("");
						event.setAnomalyDescription("");
						event.setClasse("");
					} else {
						event.setTimeStamp("");
						event.setLabel(String.valueOf(line.split(",")[4]));
						event.setAnomalyType(String.valueOf(line.split(",")[5]));
						event.setAnomalyDescription(line.split("\"").length > 1 ? line.split("\"")[1] : "-");
						event.setClasse(logType);
					}
					events.add(event);
					casos_normais.put(casoAtual, events);
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			br = new BufferedReader(new FileReader(csvFile_a));
			casoAtual = 0;
			int linha = 0;
			while ((line = br.readLine()) != null) {
				if (line.contains("activity_name")) {
					System.out.println("Ignorando cabeçalho segundo log.");
				} else {
					linha = Integer.valueOf(line.split(",")[0]);
					if (casoAtual != linha) {
						casoAtual = linha;
						events = new ArrayList<Event>();
					}
					Event event = new Event();
					event.setCaseId(Integer.valueOf(line.split(",")[0]));
					event.setEventPosition(Integer.valueOf(line.split(",")[1]));
					event.setActivityName(String.valueOf(line.split(",")[2]));
					if (String.valueOf(line.split(",")[2]).contains("▶")
							|| String.valueOf(line.split(",")[2]).contains("■")) {
						event.setTimeStamp("");
						event.setLabel("");
						event.setAnomalyType("");
						event.setAnomalyDescription("");
						event.setClasse("");
					} else {
						event.setTimeStamp("");
						event.setLabel(String.valueOf(line.split(",")[4]));
						event.setAnomalyType(String.valueOf(line.split(",")[5]));
						event.setAnomalyDescription(line.split("\"").length > 1 ? line.split("\"")[1] : "-");
						event.setClasse(logType + "_drifted");
					}
					events.add(event);
					casos_drifitados1.put(casoAtual, events);
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			br = new BufferedReader(new FileReader(csvFile_c));
			casoAtual = 0;
			int linha = 0;
			while ((line = br.readLine()) != null) {
				if (line.contains("activity_name")) {
					System.out.println("Ignorando cabeçalho segundo log.");
				} else {
					linha = Integer.valueOf(line.split(",")[0]);
					if (casoAtual != linha) {
						casoAtual = linha;
						events = new ArrayList<Event>();
					}
					Event event = new Event();
					event.setCaseId(Integer.valueOf(line.split(",")[0]));
					event.setEventPosition(Integer.valueOf(line.split(",")[1]));
					event.setActivityName(String.valueOf(line.split(",")[2]));
					if (String.valueOf(line.split(",")[2]).contains("▶")
							|| String.valueOf(line.split(",")[2]).contains("■")) {
						event.setTimeStamp("");
						event.setLabel("");
						event.setAnomalyType("");
						event.setAnomalyDescription("");
						event.setClasse("");
					} else {
						event.setTimeStamp("");
						event.setLabel(String.valueOf(line.split(",")[4]));
						event.setAnomalyType(String.valueOf(line.split(",")[5]));
						event.setAnomalyDescription(line.split("\"").length > 1 ? line.split("\"")[1] : "-");
						event.setClasse(logType + "_drifted");
					}
					events.add(event);
					casos_drifitados2.put(casoAtual, events);
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			br = new BufferedReader(new FileReader(csvFile_c));
			casoAtual = 0;
			int linha = 0;
			while ((line = br.readLine()) != null) {
				if (line.contains("activity_name")) {
					System.out.println("Ignorando cabeçalho segundo log.");
				} else {
					linha = Integer.valueOf(line.split(",")[0]);
					if (casoAtual != linha) {
						casoAtual = linha;
						events = new ArrayList<Event>();
					}
					Event event = new Event();
					event.setCaseId(Integer.valueOf(line.split(",")[0]));
					event.setEventPosition(Integer.valueOf(line.split(",")[1]));
					event.setActivityName(String.valueOf(line.split(",")[2]));
					if (String.valueOf(line.split(",")[2]).contains("▶")
							|| String.valueOf(line.split(",")[2]).contains("■")) {
						event.setTimeStamp("");
						event.setLabel("");
						event.setAnomalyType("");
						event.setAnomalyDescription("");
						event.setClasse("");
					} else {
						event.setTimeStamp("");
						event.setLabel(String.valueOf(line.split(",")[4]));
						event.setAnomalyType(String.valueOf(line.split(",")[5]));
						event.setAnomalyDescription(line.split("\"").length > 1 ? line.split("\"")[1] : "-");
						event.setClasse(logType + "_drifted");
					}
					events.add(event);
					casos_drifitados3.put(casoAtual, events);
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			br = new BufferedReader(new FileReader(csvFile_c));
			casoAtual = 0;
			int linha = 0;
			while ((line = br.readLine()) != null) {
				if (line.contains("activity_name")) {
					System.out.println("Ignorando cabeçalho segundo log.");
				} else {
					linha = Integer.valueOf(line.split(",")[0]);
					if (casoAtual != linha) {
						casoAtual = linha;
						events = new ArrayList<Event>();
					}
					Event event = new Event();
					event.setCaseId(Integer.valueOf(line.split(",")[0]));
					event.setEventPosition(Integer.valueOf(line.split(",")[1]));
					event.setActivityName(String.valueOf(line.split(",")[2]));
					if (String.valueOf(line.split(",")[2]).contains("▶")
							|| String.valueOf(line.split(",")[2]).contains("■")) {
						event.setTimeStamp("");
						event.setLabel("");
						event.setAnomalyType("");
						event.setAnomalyDescription("");
						event.setClasse("");
					} else {
						event.setTimeStamp("");
						event.setLabel(String.valueOf(line.split(",")[4]));
						event.setAnomalyType(String.valueOf(line.split(",")[5]));
						event.setAnomalyDescription(line.split("\"").length > 1 ? line.split("\"")[1] : "-");
						event.setClasse(logType + "_drifted");
					}
					events.add(event);
					casos_drifitados4.put(casoAtual, events);
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		events = new ArrayList<Event>();
		
		// TODO - Mergear os logs
		
		// 100 cases, daria pra ter algo do tipo. 40 cases CN - 3 cases CD1 - 3 cases CD2 - 3 cases CD3 - 41 cases CD Final
		
		// Not drifted LOG - 1 (40 Casos Normais)
		
		// Drifted LOG - 2 (3 Cases Drifitados)
		
		// Drifted LOG - 3 (3 Casos Drifitados)
		
		// Drifted LOG - 4 (3 Casos Drifitados)
		
		// Drifted LOG - 5 (41 Casos Drifitados)
		
		for (Event event : events) {
			if (event.getActivityName().equals("▶") || event.getActivityName().equals("■")) {
				event.setTimeStamp("");
			} else {
				timeUpdated = returntimeStamp(event.getEventPosition(), timeUpdated, casoAtual);
				event.setTimeStamp(timeUpdated);
			}
			if (casoAtual!=event.getCaseId()) {
				if (contaCaso>=60) {
					timeUpdated = returntimeStamp(999, timeStart, 0);
					timeStart = timeUpdated;
					contaCaso=0;
				} else {
					timeUpdated = returntimeStamp(0, timeStart, 0);
				}
				contaCaso++;
			}
			casoAtual = event.getCaseId();
		}
		
		return events;
		
	}

	private String returntimeStamp(int i, String timeProcessStart, int casoAtual) {
		Calendar date = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.ENGLISH);
		
		try {
			date.setTime(sdf.parse(timeProcessStart));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		long t = date.getTimeInMillis();
		Date afterAddingTenMins = null;
		
		if (i == 0) {
			afterAddingTenMins = new Date(t);
		} else if (i == 1) {
			afterAddingTenMins = new Date(t + casoAtual*10000);
		} else if (i == 2) {
			afterAddingTenMins = new Date(t + randomInterval(TimeIncrement.plus60, TimeIncrement.plus15));
		} else if (i == 3) {
			afterAddingTenMins = new Date(t + randomInterval(TimeIncrement.plus60, TimeIncrement.plus45));
		} else if (i == 4) {
			afterAddingTenMins = new Date(t + randomInterval(TimeIncrement.plus60, TimeIncrement.plus30));
		} else if (i == 5) {
			afterAddingTenMins = new Date(t + randomInterval(TimeIncrement.plus60, TimeIncrement.plus15));
		} else if (i == 6) {
			afterAddingTenMins = new Date(t + randomInterval(TimeIncrement.plus60, TimeIncrement.plus45));
		} else if (i == 7) {
			afterAddingTenMins = new Date(t + randomInterval(TimeIncrement.plus60, TimeIncrement.plus30));
		} else if (i == 8) {
			afterAddingTenMins = new Date(t + randomInterval(TimeIncrement.plus60, TimeIncrement.plus15));
		} else if (i == 9) {
			afterAddingTenMins = new Date(t + randomInterval(TimeIncrement.plus60, TimeIncrement.plus45));
		} else if (i == 10) {
			afterAddingTenMins = new Date(t + randomInterval(TimeIncrement.plus60, TimeIncrement.plus30));
		} else if (i == 999) {
			afterAddingTenMins = new Date(t + TimeIncrement.plus30);
		} else {
			afterAddingTenMins = new Date(t + randomInterval(TimeIncrement.plus60, TimeIncrement.plus15));
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		String dateTime = simpleDateFormat.format(afterAddingTenMins);

		return dateTime;
	}
	
	public static int randomInterval(int s, int i) {
        Random rd = new Random();
        return rd.nextInt(s - i + 1) + i;
    }

}
