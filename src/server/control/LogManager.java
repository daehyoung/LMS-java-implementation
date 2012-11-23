package server.control;

import java.io.IOException;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LogManager {	
	private static Logger serverLogger;
    private final static String errorLogFileName = "log/server.%g.log";

	//private LogManager() {}
	
	public static Logger getInstance( ) {
		if(serverLogger == null){
	         synchronized(Logger.class){
	                if(serverLogger == null){
	                    serverLogger = getFileLogger(errorLogFileName);
	                }
	            }
	         }
	     return serverLogger;
	}
	
	private static Logger getFileLogger(String fileName) {

        Logger logger = Logger.getLogger("server.control.LogManager");
 
        try {
            boolean append = true;
            int limit = 5024; // 5 KB Per log file, modify as required.

            String pattern = fileName;
            int numLogFiles = 5;

            FileHandler handler = new FileHandler(pattern, limit, numLogFiles, append);
            Formatter formatter = getCustomFormatter();
 
            if (formatter != null)
                handler.setFormatter(formatter);
 
            logger.addHandler(handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        return logger;
    }

	private static Formatter getCustomFormatter() {
        return new Formatter() {
            public String format(LogRecord record) {
                String recordStr = "[Date] " + new Date() + " [Log Level] "
                        + record.getLevel() + ": [Class] "
                        + record.getSourceClassName() + " [Method] "
                        + record.getSourceMethodName() + " [Message] "
                        + record.getMessage() + "\n";
                return recordStr;
            }
        };
    }

}
