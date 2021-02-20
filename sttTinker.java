package Assignments.finalProject_WastsonImplementation_SpeechToText;


import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.json.JSONObject;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.speech_to_text.v1.SpeechToText;
import com.ibm.watson.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.speech_to_text.v1.model.SpeechRecognitionAlternative;
import com.ibm.watson.speech_to_text.v1.model.SpeechRecognitionResult;
import com.ibm.watson.speech_to_text.v1.model.SpeechRecognitionResults;

public class sttTinker {
	public static Logger Log = Logger.getLogger(sttTinker.class.toString());

	public static Authenticator sttAuthenticator;

	public static SpeechToText sttService;




	// Exception Handler
	public static void printException(Exception e) {
		System.err.println("Message: " + e.getMessage());
		e.printStackTrace(System.err);
		Throwable t = e.getCause();
		while (t != null) {
			System.out.println("Cause: " + t);
			t = t.getCause();
		}
	}

	public static void getTranscript(List<String> transcriptList) {
		for(String transcript : transcriptList) {
			if (transcript != null) System.out.println(transcript);
		}
	}

	public static void getText( String audioFile) throws Exception {
		List<String> transcriptList = new ArrayList<>();
		try {
			// Setup STT Service
			sttAuthenticator = new IamAuthenticator(finalProject_sstTinker_constants.SPEECH_TO_TEXT_PASSWORD);
			sttService = new SpeechToText(sttAuthenticator);
			sttService.setServiceUrl(finalProject_sstTinker_constants.SPEECH_TO_TEXT_URL);

			// Set recognize options
			RecognizeOptions recognizeOptions = new RecognizeOptions.Builder()
					.audio(new FileInputStream(audioFile))
					.contentType("audio/wav")
					.wordAlternativesThreshold((float) 0.9)
					.build();

			// Execute transcription
			SpeechRecognitionResults speechRecognitionResults = sttService.recognize(recognizeOptions).execute().getResult();

			// Store and Print transcript
			for (SpeechRecognitionResult result : speechRecognitionResults.getResults()) {
				if (result.isXFinal()) {
					for (SpeechRecognitionAlternative alternativeList : result.getAlternatives()) {
						transcriptList.add(alternativeList.getTranscript());
					}
				}

			}

			getTranscript(transcriptList);

		} catch(Exception ex) {
			Log.info(ex.getClass().getName() + ex.getMessage());
			printException(ex);
		}

	}

}
