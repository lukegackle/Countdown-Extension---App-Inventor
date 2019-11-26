/*------------------------------------------------
 ----------- Countdown Extension -----------

This is a Java extension for App Inventor based platforms.
This extension provides a range of functions to get the duration between two specified dates, a startDate, and an endDate.

Created by: Luke Gackle

NOTE: I just ask that contributors to this extension post commits to this repository for consistency and retain the same naming conventions, contributors may add thier name or username below along with the version number they have created. Contributors may send any documentation they would like me to upload onto the extension page to me directly via https://thunkableblocks.blogspot.com/p/extension-update.html

Other Contributors:
Nill

Contribute to this extension on Github at https://github.com/lukegackle/

Website: https://thunkableblocks.blogspot.com

Find this extension at: 
https://thunkableblocks.blogspot.com/2017/05/countdown-extension.html

------------------------------------------------*/

package com.LukeGackle;

import android.content.Context;
import android.util.Log;
import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.collect.Sets;
import com.google.appinventor.components.runtime.util.BoundingBox;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.FileUtil;
import com.google.appinventor.components.runtime.util.MediaUtil;
import com.google.appinventor.components.runtime.util.PaintUtil;
import com.google.appinventor.components.runtime.util.Dates;
import com.google.appinventor.components.runtime.util.YailList;
import com.google.appinventor.components.common.ComponentConstants;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.common.YaVersion;
import com.google.appinventor.components.runtime.errors.YailRuntimeError;

import java.util.Calendar;
import java.lang.Math; //For calculating the Math.Floor for years and months
import java.util.ArrayList; //This is for the countdown function to return a list
import java.util.List;
import android.os.CountDownTimer;


@DesignerComponent(version = countdown.VERSION,
description = "This extension provides methods to more easily calculate the duration between two dates, the methods GetCountdownFromStrings, and GetCountdownFromInstants return a countdown as a YailList and can be easily called to retrieve the number of days, hours, minutes, and seconds remaining.",
category = ComponentCategory.EXTENSION,
nonVisible = true,
iconName = "https://1.bp.blogspot.com/-d-xyqbKFyAY/WSDvpMEG-tI/AAAAAAABYTk/I9gjYEgABZYxjwi2pzmlqbvQg6eMJhSeQCLcB/s1600/ExtensionsIcons.png")
@SimpleObject(external = true)

public class countdown extends AndroidNonvisibleComponent implements Component{
public static final int VERSION = 2;
private ComponentContainer container;
private Context context;
private static final String LOG_TAG = "countdown";

private String argumentError = "Arguments must be formatted correctly like MM/DD/YYYY hh:mm:ss, or MM/DD/YYYY or hh:mm";
private static final String DAYS = "DAYS";
private static final String WEEKS = "WEEKS";
private static final String MONTHS = "MONTHS";
private static final String YEARS = "YEARS";
private static final String HOURS = "HOURS";
private static final String MINUTES = "MINUTES";
private static final String SECONDS = "SECONDS";

private boolean TimerInProgress = false;
private CountDownTimer timer;
//Experiment with this??
//private Calendar startDate;
//private Calendar endDate;

public countdown(ComponentContainer container){
super(container.$form());
this.container = container;
context = (Context) container.$context();
Log.d(LOG_TAG, "countdown Created");
}

	@SimpleFunction(description = "Returns the number of days between given start date and end date. Arguments must be formatted correctly like MM/DD/YYYY hh:mm:ss, or MM/DD/YYYY or hh:mm")
	public long GetDurationInDays(String startDate, String endDate){
		return calculateDuration(DAYS, startDate, endDate);
	}
  
	@SimpleFunction(description = "Returns the number of weeks between given start date and end date. Arguments must be formatted correctly like MM/DD/YYYY hh:mm:ss, or MM/DD/YYYY or hh:mm")
	public long GetDurationInWeeks(String startDate, String endDate){
		return calculateDuration(WEEKS, startDate, endDate);
	}
  
    @SimpleFunction(description = "Returns the number of months between given start date and end date. Arguments must be formatted correctly like MM/DD/YYYY hh:mm:ss, or MM/DD/YYYY or hh:mm")
public long GetDurationInMonths(String startDate, String endDate){
	return calculateDuration(MONTHS, startDate, endDate);
  }
  
    @SimpleFunction(description = "Returns the number of years between given start date and end date. Arguments must be formatted correctly like MM/DD/YYYY hh:mm:ss, or MM/DD/YYYY or hh:mm")
public long GetDurationInYears(String startDate, String endDate){
	return calculateDuration(YEARS, startDate, endDate);
  }
  
    @SimpleFunction(description = "Returns the number of hours between given start date and end date. Arguments must be formatted correctly like MM/DD/YYYY hh:mm:ss, or MM/DD/YYYY or hh:mm")
public long GetDurationInHours(String startDate, String endDate){
	return calculateDuration(HOURS, startDate, endDate);
  }
  
    @SimpleFunction(description = "Returns the number of minutes between given start date and end date. Arguments must be formatted correctly like MM/DD/YYYY hh:mm:ss, or MM/DD/YYYY or hh:mm")
public long GetDurationInMinutes(String startDate, String endDate){
	return calculateDuration(MINUTES, startDate, endDate);
  }
  
    @SimpleFunction(description = "Returns the number of seconds between given start date and end date. Arguments must be formatted correctly like MM/DD/YYYY hh:mm:ss, or MM/DD/YYYY or hh:mm")
public long GetDurationInSeconds(String startDate, String endDate){
	return calculateDuration(SECONDS, startDate, endDate);
  }
  @SimpleFunction(description = "Returns the current time as a suitable String to be used with the GetCountdownFromStrings function. The returned string is in the format MM/DD/YYYY HH:mm:ss")
  public String GetCurrentTimeAsString(){
	 Calendar now = Dates.Now();
	 return Dates.FormatDateTime(now, "MM/dd/yyyy hh:mm:ss");
  }
  
  @SimpleFunction(description = "Returns true if date1 is before date2, else returns false. Argument strings must be in the format MM/DD/YYYY HH:mm:ss")
  public boolean IsDateBefore(String date1, String date2){
	  Calendar Date1 = Dates.DateValue(date1);
	  Calendar Date2 = Dates.DateValue(date2);
	  
	  return Date1.before(Date2);
  }
  
    @SimpleFunction(description = "Returns true if date1 is after date2, else returns false. Argument strings must be in the format MM/DD/YYYY HH:mm:ss")
  public boolean IsDateAfter(String date1, String date2){
	  Calendar Date1 = Dates.DateValue(date1);
	  Calendar Date2 = Dates.DateValue(date2);
	  
	  return Date1.after(Date2);
  }
  
     @SimpleFunction(description = "Returns true if date1 is equal to date2, else returns false. Argument strings must be in the format MM/DD/YYYY HH:mm:ss")
  public boolean IsDateEqual(String date1, String date2){
	  Calendar Date1 = Dates.DateValue(date1);
	  Calendar Date2 = Dates.DateValue(date2);
	  
	  return Date1.equals(Date2);
  }
  
  
	@SimpleFunction(description = "Returns a list with the days, hours, minutes, and seconds between two dates. Arguments must be formatted correctly like MM/DD/YYYY hh:mm:ss, or MM/DD/YYYY or hh:mm")
	public YailList GetCountdownFromStrings(String startDate, String endDate){
		Calendar start;
		Calendar end;
		
		try {
			start = Dates.DateValue(startDate);
			end = Dates.DateValue(endDate);
			}
		catch (IllegalArgumentException e) {
			throw new YailRuntimeError(argumentError,"");
		}
		List<Object> listItems = new ArrayList<Object>();
		//long[] list = new long[4];
		long duration;
		
		duration = Dates.ConvertDuration(end.getTimeInMillis() - start.getTimeInMillis(), Calendar.DATE); //Get Days
		if(duration < 0){
			duration = 0;
		}
		listItems.add(duration); //add to the output arraylist the days
		Dates.DateAdd(start, Calendar.DATE, (int) duration); //Remove(Add) days to the start ready for the next unit
		
		duration = Dates.ConvertDuration(end.getTimeInMillis() - start.getTimeInMillis(), Calendar.HOUR_OF_DAY);
		if(duration < 0){
			duration = 0;
		}
		listItems.add(duration);
		Dates.DateAdd(start, Calendar.HOUR_OF_DAY, (int)duration);
		
		duration = Dates.ConvertDuration(end.getTimeInMillis() - start.getTimeInMillis(), Calendar.MINUTE);
		if(duration < 0){
			duration = 0;
		}
		listItems.add(duration);
		Dates.DateAdd(start, Calendar.MINUTE, (int) duration);
		
		duration = Dates.ConvertDuration(end.getTimeInMillis() - start.getTimeInMillis(), Calendar.SECOND);
		if(duration < 0){
			duration = 0;
		}
		listItems.add(duration);
		Dates.DateAdd(start, Calendar.SECOND, (int) duration);
		
		YailList list2 = YailList.makeList(listItems);
		return list2;
	}
	
	@SimpleFunction(description = "Returns a list with the days, hours, minutes, and seconds between two dates. Arguments must be instants.")
	public YailList GetCountdownFromInstants(Calendar startDate, Calendar endDate){
		
		List<Object> listItems = new ArrayList<Object>();
		//long[] list = new long[4];
		long duration;
		
		duration = Dates.ConvertDuration(endDate.getTimeInMillis() - startDate.getTimeInMillis(), Calendar.DATE); //Get Days
		if(duration < 0){
			duration = 0;
		}
		listItems.add(duration); //add to the output arraylist the days
		Dates.DateAdd(startDate, Calendar.DATE, (int) duration); //Remove(Add) days to the start ready for the next unit
		
		duration = Dates.ConvertDuration(endDate.getTimeInMillis() - startDate.getTimeInMillis(), Calendar.HOUR_OF_DAY);
		if(duration < 0){
			duration = 0;
		}
		listItems.add(duration);
		Dates.DateAdd(startDate, Calendar.HOUR_OF_DAY, (int)duration);
		
		duration = Dates.ConvertDuration(endDate.getTimeInMillis() - startDate.getTimeInMillis(), Calendar.MINUTE);
		if(duration < 0){
			duration = 0;
		}
		listItems.add(duration);
		Dates.DateAdd(startDate, Calendar.MINUTE, (int) duration);
		
		duration = Dates.ConvertDuration(endDate.getTimeInMillis() - startDate.getTimeInMillis(), Calendar.SECOND);
		if(duration < 0){
			duration = 0;
		}
		listItems.add(duration);
		Dates.DateAdd(startDate, Calendar.SECOND, (int) duration);
		
		YailList list2 = YailList.makeList(listItems);
		return list2;
	}
  
  private long calculateDuration(String format, String startDate, String endDate){
	Calendar start;
	Calendar end;
	
	try {
		start = Dates.DateValue(startDate);
		end = Dates.DateValue(endDate);
		}
	catch (IllegalArgumentException e) {
		throw new YailRuntimeError(argumentError,"");
    }
	long duration = 0;

	if(format == DAYS){
		duration = Dates.ConvertDuration(end.getTimeInMillis() - start.getTimeInMillis(), Calendar.DATE);
	}
	else if(format == WEEKS){
		duration = Dates.ConvertDuration(end.getTimeInMillis() - start.getTimeInMillis(), Calendar.WEEK_OF_YEAR);
	}
	else if(format == HOURS){
		duration = Dates.ConvertDuration(end.getTimeInMillis() - start.getTimeInMillis(), Calendar.HOUR_OF_DAY);
	}
	else if(format == MINUTES){
		duration = Dates.ConvertDuration(end.getTimeInMillis() - start.getTimeInMillis(), Calendar.MINUTE);
	}
	else if(format == SECONDS){
		duration = Dates.ConvertDuration(end.getTimeInMillis() - start.getTimeInMillis(), Calendar.SECOND);
	}	
	else if(format == YEARS){ //Note no Dates.ConvertDuration option for this
		duration = (long)Math.floor(0.01916536482 * Dates.ConvertDuration(end.getTimeInMillis() - start.getTimeInMillis(), Calendar.WEEK_OF_YEAR));
	}
	else if(format == MONTHS){ //Note no Dates.ConvertDuration option for this
		duration = (long)Math.floor(0.229984378 * Dates.ConvertDuration(end.getTimeInMillis() - start.getTimeInMillis(), Calendar.WEEK_OF_YEAR));
	}
	
	if(duration < 0){
		duration = 0;
	}
	
	return duration;
  }

  @SimpleEvent(description="OnTick")
  public void OnTick(long millisUntilFinished){
	  EventDispatcher.dispatchEvent(this, "OnTick", millisUntilFinished);
  }
  
    @SimpleEvent(description="OnFinish")
  public void OnFinish(){
	  TimerInProgress = false;
	  EventDispatcher.dispatchEvent(this, "OnFinish");
  }
  
  @SimpleFunction(description = "Starts a countdown timer to the specified number of milliseconds.")
  public void StartTimer(long millisDuration, long millisInterval){
	if(!TimerInProgress){  
		timer = new CountDownTimer(millisDuration, millisInterval) {

			public void onTick(long millisUntilFinished) {
				OnTick( millisUntilFinished );
			   //here you can have your logic to set text to edittext
			}

			public void onFinish() {
				OnFinish();
			}
		}.start();
		
		TimerInProgress = true;
	}
	else{
		throw new YailRuntimeError("Timer already running.","");
	}
  }
  
  @SimpleFunction(description = "Cancel the currently running timer.")
  public void CancelTimer(){
	  timer.cancel();
	  TimerInProgress = false;
  }
}
