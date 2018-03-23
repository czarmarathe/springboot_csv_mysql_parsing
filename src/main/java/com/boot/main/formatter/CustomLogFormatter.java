package com.boot.main.formatter;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class CustomLogFormatter extends Formatter{

	@Override
	public String format(LogRecord record) {
		// TODO Auto-generated method stub
		
		// Fetch only the message part from the logs
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(record.getMessage()).append(System.getProperty("line.separator"));
		
		// Return individual log message for display
		return stringBuilder.toString();
	
	} // End of format()

	
} // End of Class
