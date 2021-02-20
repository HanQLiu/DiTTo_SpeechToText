package Assignments.finalProject_WastsonImplementation_SpeechToText;

import java.util.logging.Logger;

public class finalProject_sttTinker_testRunner {
	public static Logger Log = Logger.getLogger(finalProject_sttTinker_testRunner.class.toString());

	public static void main(String[] args) {
		try {
			sttTinker.getText(finalProject_sstTinker_constants.AUDIO_FILE_PATH);
		} catch(Exception e) {
			Log.info(e.getClass().getName() + e.getMessage());
			sttTinker.printException(e);
		}
	}
}
