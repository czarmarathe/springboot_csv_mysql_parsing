package com.boot.main;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.batch.item.ItemProcessor;

import com.boot.main.model.People;

public class BadDataProcessor implements ItemProcessor<People, People>{

	@Override
	public People process(People people) throws Exception {
		// TODO Auto-generated method stub
		
		System.out.println("Step 2 - Initial Data :"+people.toString());
		
		// Escape string fields with comma (,) ie. Fields with comma are double-quoted
		// Implemented for image url. Same concept can be extended for other fields by passing getter returns to function below
		String esc_url = StringEscapeUtils.escapeCsv(people.getImage());
		people.setImage(esc_url);
		
		if (people.recordIncomplete()) {
			
			return people;
		}
		
		System.out.println("Step 2 Ends :"+ people.toString());
		
		return null;
		
	} // End of process()
	
} // End of Class
