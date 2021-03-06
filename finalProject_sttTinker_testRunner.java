package Assignments.finalProject_WastsonImplementation_SpeechToText;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class finalProject_sttTinker_testRunner {
	public static final Logger LOG = LoggerFactory.getLogger(finalProject_sttTinker_testRunner.class);

	public static void main(String[] args) {
		try {
			sttTinker.getText(finalProject_sstTinker_constants.STT_TEST_FILE); // get transcript from audio file
			//streamingStt_Tinker.getText(); //get transcript from stream
		} catch(Exception e) {
			LOG.info(e.getClass().getName() + e.getMessage());
			sttTinker.printException(e);
		}
	}
}
