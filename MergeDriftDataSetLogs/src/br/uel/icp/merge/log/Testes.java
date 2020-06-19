package br.uel.icp.merge.log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Testes {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

		String timeStart = simpleDateFormat.format(new Date());
		String timeUpdated = simpleDateFormat.format(new Date());
		
		System.out.println("---------------------------------------------");
		System.out.println("Hora de Inicio: "+timeStart);
		System.out.println("---------------------------------------------");
		
		timeUpdated = returntimeStamp(1, timeStart);
		
		int mudar = 10;
		
		for (int i=0; i<=1000; i++) {
			
			if (i>=mudar) {
				timeUpdated = returntimeStamp(1, timeUpdated);
				
				System.out.println("---------------------------------------------");
				mudar = i+10;
			}
			
			timeUpdated = returntimeStamp(1, timeStart);
			System.out.println("Hora Atualizada: "+timeUpdated);
			
		}
	}
	
	private static String returntimeStamp(int i, String timeProcessStart) {
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
			afterAddingTenMins = new Date(t + randomInterval(TimeIncrement.plus60, TimeIncrement.plus45));
		} else if (i == 1) {
			afterAddingTenMins = new Date(t + randomInterval(TimeIncrement.plus60, TimeIncrement.plus30));
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
