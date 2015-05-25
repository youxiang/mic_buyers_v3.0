package com.micen.buyers.module.favorite;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.micen.buyers.util.Util;

public class FavoriteTime
{
	public String time;
	public String minutes;
	public String seconds;
	public String hours;
	public String month;
	public String year;
	public String timezoneOffset;
	public String day;
	public String date;

	public String toString()
	{
		if (time != null && !"".equals(time))
		{
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(Long.parseLong(time));
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Util.getLocale());
			return format.format(c.getTime());
		}
		else
		{
			return "";
		}
	}
}
