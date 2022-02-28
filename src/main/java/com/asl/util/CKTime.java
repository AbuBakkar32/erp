package com.asl.util;

import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Zubayer Ahamed
 * @since 09-May-2020
 *
 */
public class CKTime implements Comparable<Object>, Serializable {

	private static final long serialVersionUID = -3104686262221211745L;

	private int hour = 0;
	private int minute = 0;
	private int second = 0;

	public CKTime() {
		super();
	}

	public CKTime(CKTime ob) {
		this(ob.getHour(), ob.getMinute(), ob.getSecond());
	}

	/**
	 * Construct a CKTime instance from <code>HH:mm:ss</code> string.
	 * 
	 * @param time Time value as HH:mm:ss
	 */
	public CKTime(String time) {
		setTime(time);
	}

	/**
	 * Construct a CKTime instance from {@link Calendar}.
	 * 
	 * @param ob {@link Calendar}
	 */
	public CKTime(Calendar ob) {
		this(ob.get(HOUR_OF_DAY), ob.get(MINUTE), ob.get(SECOND));
	}

	/**
	 * Construct a CKTime instance from {@link Date}.
	 * 
	 * @param ob {@link Date}
	 */
	@SuppressWarnings("deprecation")
	public CKTime(Date ob) {
		this(ob.getHours(), ob.getMinutes(), ob.getSeconds());
	}

	public CKTime(int h, int m) {
		this(h, m, 0);
	}

	public CKTime(int h, int m, int s) {
		this.hour = h;
		this.minute = m;
		this.second = s;
	}

	public static CKTime getInstance(Date ob) {
		if (ob == null) ob = Calendar.getInstance().getTime();
		return new CKTime(ob);
	}

	public static CKTime now() {
		return new CKTime(Calendar.getInstance());
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) throws TimeFormatException {
		if (hour <= 23 && hour >= 0) this.hour = hour;
		else throw new TimeFormatException("Invalid value for hour");
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) throws TimeFormatException {
		if (minute <= 59 && minute >= 0) this.minute = minute;
		else throw new TimeFormatException("Invalid value for minute");
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) throws TimeFormatException {
		if (second <= 59 && second >= 0) this.second = second;
		else throw new TimeFormatException("Invalid value for second");
	}

	public String getTime() {
		if (isTimeZero()) return "";
		NumberFormat formatter = new DecimalFormat("#00");
		String time = formatter.format(this.hour) + ":" + formatter.format(this.minute);
		if (this.second > 0) time += ":" + formatter.format(this.second);
		return time;
	}

	public String getT5Time() {
		if (isTimeZero()) return "";
		NumberFormat formatter = new DecimalFormat("#00");
		return formatter.format(this.hour) + ":" + formatter.format(this.minute);
	}

	public void setTime(String time) {
		if (time == null) return;
		if (time.matches("^([0-2][0-9]):([0-5][0-9])(:[0-5][0-9])?$")) {
			int hh = 0, mm = 0, sec = 0;
			String parts[] = time.split(":");
			if (parts.length == 2) {
				hh = Integer.parseInt(parts[0]);
				mm = Integer.parseInt(parts[1]);
			} else {
				hh = Integer.parseInt(parts[0]);
				mm = Integer.parseInt(parts[1]);
				sec = Integer.parseInt(parts[2]);
			}
			if ((hh <= 23) && (mm <= 59) && (sec <= 59)) {
				this.hour = hh;
				this.minute = mm;
				this.second = sec;
			}
		}
	}

	private boolean isTimeZero() {
		boolean isZero = false;
		if (this.hour == 0 && this.minute == 0 && this.second == 0) isZero = true;
		return isZero;
	}

	public long getTimeInSecond() {
		return this.hour * 60 * 60 + this.minute * 30 + this.second;
	}

	public boolean after(CKTime anotherTime) {
		return (compareTo(anotherTime) > 0);
	}

	public boolean before(CKTime anotherTime) {
		return (compareTo(anotherTime) < 0);
	}

	public boolean equals(CKTime anotherTime) {
		return (compareTo(anotherTime) == 0);
	}

	@Override
	public int compareTo(Object o) {
		if (!(o instanceof CKTime)) throw new ClassCastException("Invalid object");
		CKTime tm = (CKTime) o;
		return (int) (this.getTimeInSecond() - tm.getTimeInSecond());
	}

	public CKTime withHour(int hour) {
		try { setHour(hour); } catch (TimeFormatException e) {}
		return this;
	}

	public CKTime withMinute(int minute) {
		try { setMinute(minute); } catch (TimeFormatException e) {}
		return this;
	}

	/**
	 * Returns a copy of {@link CKTime} instance with the specified number of hours added.
	 * 
	 * @param hours Hours to add, may be negative
	 * @return {@link CKTime} copy based on provided instance, not null
	 */
	public CKTime addHour(int hours) {
		CKTime copy = new CKTime(this);
		if (hours == 0) return copy;
		try { copy.setHour(copy.getHour() + hours); } catch (TimeFormatException e) {}
		return copy;
	}

	/**
	 * Returns a copy of {@link CKTime} instance with the specified number of minutes added.
	 *  
	 * @param minutes Minutes to add, may be negative
	 * @return {@link CKTime} copy based on provided instance, not null
	 */
	public CKTime addMinute(int minutes) {
		CKTime copy = new CKTime(this);
		if (minutes == 0) return copy;
		try { copy.setMinute(copy.getMinute() + minutes); } catch (TimeFormatException e) {}
		return copy;
	}

	@Override
	public String toString() {
		return hour + ":" + minute + ":" + second;
	}

	/**
	 * Converts this object into {@link LocalTime}.
	 * 
	 * @return {@link LocalTime} instance representing same time value
	 * @see #getLocalTime(CKTime)
	 */
	public LocalTime toLocalTime() {
		return getLocalTime(this);
	}

	/**
	 * Converts {@link CKTime} object instance into {@link LocalTime}.
	 * 
	 * @param time {@link CKTime} instance
	 * @return {@link LocalTime} instance representing same time value
	 */
	public static LocalTime getLocalTime(CKTime time) {
		return time == null ? null : LocalTime.of(time.getHour(), time.getMinute(), time.getSecond());
	}

}
