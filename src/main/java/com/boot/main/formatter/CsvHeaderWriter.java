package com.boot.main.formatter;

import java.io.IOException;
import java.io.Writer;

import org.springframework.batch.item.file.FlatFileHeaderCallback;

// Class adds assigned header information to the CSV File during write
public class CsvHeaderWriter implements FlatFileHeaderCallback{

	// Var is assigned header field
	private final String csv_header;
			
	public CsvHeaderWriter(String csv_header) {
		super();
		this.csv_header = csv_header;
	}



	@Override
	public void writeHeader(Writer writer) throws IOException {
		// TODO Auto-generated method stub
		
		writer.write(csv_header);
		
	}

}
