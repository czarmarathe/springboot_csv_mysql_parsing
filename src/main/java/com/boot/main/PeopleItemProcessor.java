package com.boot.main;

import org.springframework.batch.item.ItemProcessor;

import com.boot.main.model.People;
import com.mysql.fabric.xmlrpc.base.Data;

import javax.xml.bind.DatatypeConverter;

public class PeopleItemProcessor implements ItemProcessor<People, People>{

	@Override
	public People process(People people) throws Exception {
		// TODO Auto-generated method stub
		
		//Track total records
		People.data_received ++;
		
		//Check if record is incomplete
		//Filter out incomplete records before any processing
		if (people.recordIncomplete()) {
			
			People.data_failed ++;
			return null;
		}
		
		
		//Initial CSV data
		System.out.println("Step 1 - Initial Data: "+people.toString());
		
		
		// New instance of class created with same values for processing
		People p_new = new People(people);
		
		//Convert string to boolean
		p_new.setB1_out(Boolean.parseBoolean(people.getB1()));
		
		// Convert Base64 encoded string to byte array for efficient storage in DB
		// Better to store image data as byte array than Base64 encoded
		// Byte array can be easily encoded to Base64 for retrieval
		String image_url = people.getImage();
		String image_base64;
		// Try-catch block essential for ArrayIndexOutofBoundsException in case the input doesn't contain comma, 
		// is blank or incorrect format
		try {
			image_base64 = image_url.split(",")[1];
		} catch (Exception e) {
			/* To not include the header values as incomplete or not : Records considered as bad-data but not written to csv for now
			if (!p_new.getImage().equals("F")) {
				People.data_failed ++;
			}*/
			
			People.data_failed ++;
			
			return null;
		}
		byte[] image = javax.xml.bind.DatatypeConverter.parseBase64Binary(image_base64);
		// Assign byte array to a field using setter method
		p_new.setBlob_image(image);
		p_new.setImage(null);
		
		// Final Processed data
		System.out.println("Final Data...");
		System.out.println("Values : "+p_new.toString());
		
		People.data_successful ++;
		
		// Return Modified instance of People to writer
		return p_new;
	}
	

}
