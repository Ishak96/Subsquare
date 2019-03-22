package engine;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TimeSimulator
{	
	private Calendar calendar;
	private SimpleDateFormat formatter;
	
	private int secondCount = 0;
	private int am_pm;
	private String currentMonth;
		
	public TimeSimulator() {
		calendar = Calendar.getInstance();
		this.am_pm = calendar.get(Calendar.AM_PM);
		formatter = new SimpleDateFormat("dd MMMM yyyy",Locale.ENGLISH);
		init();
	}
	
	private void init() {
		calendar.set(Calendar.SECOND,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.HOUR,0);
		currentMonth = getMounth();
	}	
	
	public int getSecond() {
		return calendar.get(Calendar.SECOND);
	}
	
	public int getMinute() {
		return calendar.get(Calendar.MINUTE);
	}
	
	public int getHour() {
		return calendar.get(Calendar.HOUR);
	}
	
	public String getTime() {
	    String ampm = "ampm";
	    if (am_pm == 0)
	        ampm = "AM";
	    else
	        ampm = "PM";
		return getHour()+"h:"+getMinute()+"min"+" "+ampm;
	}
	//am if = 0 pm else
	public boolean AM_PM() {
		return am_pm == 0;
	}
	
	public String getDay() {
		return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH);
	}
	
	public String getMounth() {
		return calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
	}
	
	public String getDate() {
		return formatter.format(calendar.getTime());
	}
	
	public int getSecondCount() {
		return secondCount;
	}
	
	public boolean isWeekEnd() {
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		return day == Calendar.SATURDAY || day == Calendar.SUNDAY;
	}
	
	public boolean isEndOfMonth() {
		if(!getMounth().equals(currentMonth)) {
			currentMonth = getMounth();
			return true;
		}
		else {
			return false;
		}
	}
	
	public void update() {
		calendar.add(Calendar.MINUTE,20);
		am_pm = calendar.get(Calendar.AM_PM);
		secondCount++;
	}
}