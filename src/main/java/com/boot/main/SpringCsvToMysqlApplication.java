package com.boot.main;

import java.util.logging.FileHandler;
import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.boot.main.formatter.CustomLogFormatter;
import com.boot.main.model.People;

@SpringBootApplication
public class SpringCsvToMysqlApplication {
	
	public static Logger logger = Logger.getLogger("Logs");

	public static void main(String[] args) {
		SpringApplication.run(SpringCsvToMysqlApplication.class, args);
		
		// Two techniques used to log results
				// 1. Log results along with all other main messages under resources/app.log
				logger.info(People.data_received + " : Records Received");
				logger.info(People.data_successful + " : Records Successful");
				logger.info(People.data_failed + " : Records Failed");

				// 2. Log only the 3 fields in a separate file under resources/Result.log
				try {  
			        FileHandler fileHandler = new FileHandler("src/main/resources/logs/Result.log");  
			        logger.addHandler(fileHandler);
			        // Invoke a custom formatter for log messages. Managed by CustomLogFormatter class
			        fileHandler.setFormatter(new CustomLogFormatter());
			        
			        logger.info(People.data_received + " : Records Received");
					logger.info(People.data_successful + " : Records Successful");
					logger.info(People.data_failed + " : Records Failed");
			    } catch (Exception e) {  
			        e.printStackTrace();  
			    } // End of try-catch 
	}
}
