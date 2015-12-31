package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class Tools {

	public static String httpRequest(String url, Map<String, String> headers) throws IOException{
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		// add request header
		//con.setRequestProperty("Authorization", JimgurConstants.authenticationHeader);
		Set<String> keys = headers.keySet();
		for(String key : keys){
			con.setRequestProperty(key, (String) headers.get(key));
		}

		//int responseCode = con.getResponseCode();

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response.toString();

	}

	public static void printJson(Object json){
		if(json instanceof JSONObject)
			printJsonObject((JSONObject)json);
		if(json instanceof JSONArray)
			printJsonArray((JSONArray)json);
	}

	public static void printJsonObject(JSONObject json){
		for(Object key : json.keySet()){
			System.out.println(key + ": " + json.get(key));
			if(json.get(key) instanceof JSONArray)
				printJsonArray((JSONArray)json.get(key));
		}
	}

	public static void printJsonArray(JSONArray json){
		for(int i = 0; i < json.size(); i++){
			System.out.print("\t");
			printJson(json.get(i));
		}
	}

}