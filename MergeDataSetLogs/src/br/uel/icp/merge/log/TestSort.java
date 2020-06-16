package br.uel.icp.merge.log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class TestSort {

	public static void main(String[] args) {
		// initialize a Random object somewhere; you should only need one
		Random random = new Random();

		// generate a random integer from 0 to 899, then add 100
		int x = random.nextInt(1800000) + 600000;
		
		System.out.println(x);
		
		Calendar date = Calendar.getInstance();
		long t = date.getTimeInMillis();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		String timeProcessStart = simpleDateFormat.format(t);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.ENGLISH);
		
		try {
			date.setTime(sdf.parse(timeProcessStart));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Date afterAddingTenMins = null;
		
		afterAddingTenMins = new Date(t + (x));
		String dateTime = simpleDateFormat.format(afterAddingTenMins);

		System.out.println(dateTime);
		
	}

}