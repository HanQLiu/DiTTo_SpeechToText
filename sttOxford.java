package Assignments.finalProject_WastsonImplementation_SpeechToText;

import org.slf4j.LoggerFactory;

import  javax.net.ssl.HttpsURLConnection;
import  java.io.BufferedReader;
import  java.io.IOException;
import  java.io.InputStreamReader;
import  java.net.URL;

public class sttOxford {
    public static final org.slf4j.Logger LOG = LoggerFactory.getLogger(sttOxford.class);


    public static String getDefinition(String word) {
        String word_id = word.toLowerCase();
        String restUrl = finalProject_sstTinker_constants.OXFORD_API_URL +
                finalProject_sstTinker_constants.OXFORD_API_LANGUAGE + "/" + word_id + "?" + "fields=" +
                finalProject_sstTinker_constants.OXFORD_API_FIELDS + "&strictMatch=" +
                finalProject_sstTinker_constants.OXFORD_API_STRICTMATCH;
        StringBuilder definitionStringBuilder = new StringBuilder();

        try {
            URL url = new URL(restUrl);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("app_id", finalProject_sstTinker_constants.OXFOD_API_ID);
            urlConnection.setRequestProperty("app_key", finalProject_sstTinker_constants.OXFORD_API_KEY);

            // read the output from the server
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line = null;
            while ((line = reader.readLine()) != null) {
                definitionStringBuilder.append(line + "\n");
            }
        } catch (IOException ex) {
            LOG.info(ex.getClass().getName() + ex.getMessage());
            sttTinker.printException(ex);
        }
        LOG.info(definitionStringBuilder.toString());
        return definitionStringBuilder.toString();
    }
}
