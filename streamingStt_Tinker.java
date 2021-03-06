package Assignments.finalProject_WastsonImplementation_SpeechToText;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import com.ibm.cloud.sdk.core.http.HttpMediaType;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.speech_to_text.v1.SpeechToText;
import com.ibm.watson.speech_to_text.v1.model.RecognizeWithWebsocketsOptions;
import com.ibm.watson.speech_to_text.v1.model.SpeechRecognitionResult;
import com.ibm.watson.speech_to_text.v1.model.SpeechRecognitionResults;
import com.ibm.watson.speech_to_text.v1.websocket.BaseRecognizeCallback;

public class streamingStt_Tinker {
	public static Logger Log = Logger.getLogger(streamingStt_Tinker.class.toString());

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
		int sampleRate = 16000;
		AudioFormat format = new AudioFormat(sampleRate, 16, 1, true, false);
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);



		try {
			// Setup STT Service
			sttAuthenticator = new IamAuthenticator(finalProject_sstTinker_constants.SPEECH_TO_TEXT_PASSWORD);
			sttService = new SpeechToText(sttAuthenticator);
			sttService.setServiceUrl(finalProject_sstTinker_constants.SPEECH_TO_TEXT_URL);

			// setup Audio System connection
			TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info); //@TODO determine how to identify a URL connection as the source rather than the internal audio system
			if (!AudioSystem.isLineSupported(info)) throw new Exception();
			line.open(format);
			line.start();

			AudioInputStream audio = new AudioInputStream(line);

			// Set recognize options
			RecognizeWithWebsocketsOptions recognizeOptions = new RecognizeWithWebsocketsOptions.Builder()
					.interimResults(true)
					.timestamps(true)
					.wordConfidence(true)
					.contentType(HttpMediaType.AUDIO_RAW + "; rate=" + sampleRate)
					.wordAlternativesThreshold((float) 0.9)
					.build();

			// get results
			Log.info("Begin gathering results.");
			sttService.recognizeUsingWebSocket(recognizeOptions,
					new BaseRecognizeCallback() {
						@Override
						public void onTranscription(SpeechRecognitionResults speechResults) {
							super.onTranscription(speechResults);
							for (SpeechRecognitionResult transcript : speechResults.getResults()) {
								transcriptList.add(transcript.toString());
							}
						}
					}
			);

			// delay
			Thread.sleep(30 * 1000); //@TODO Inquiry: should utilize a method get length of video?

			// close connection
			line.stop();
			line.close();

			// print transcript
			Log.info("Begin transcription");
			getTranscript(transcriptList);

		} catch (Exception ex) {
			Log.info(ex.getClass().getName() + ex.getMessage());
			printException(ex);
		}
	}
}
