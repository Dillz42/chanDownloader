package com.company;

//http://a.4cdn.org/b/1.json

import org.json.simple.*;
import org.json.simple.parser.*;

import java.io.*;
import java.net.URL;
import java.util.HashMap;

public class Main {

	public static void main(String[] args) throws IOException, ParseException {
		JSONParser parser = new JSONParser();
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		int threadNum = Integer.parseInt(in.readLine());
		String jsonResponse = Tools.httpRequest("https://a.4cdn.org/b/thread/" + threadNum + ".json", new HashMap<String, String>());
		JSONObject thread = (JSONObject)parser.parse(jsonResponse);
		printJson(thread);
	}

	public static void printJson(Object json) throws IOException {
		if(json instanceof JSONObject)
			printJsonObject((JSONObject)json);
		if(json instanceof JSONArray)
			printJsonArray((JSONArray)json);
	}

	public static void printJsonObject(JSONObject json) throws IOException {
		for(Object key : json.keySet()){
			//System.out.println(key + ": " + json.get(key));
			if(json.get(key) instanceof JSONArray)
				printJsonArray((JSONArray)json.get(key));
			if(key.equals("tim")) {
				System.out.println("" + json.get(key) + json.get("ext"));
				System.out.println("" + json.get("filename") + json.get("ext"));

				URL imageURL = new URL("https://i.4cdn.org/b/" + json.get("tim") + json.get("ext"));
				BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
				InputStream remoteImage = new BufferedInputStream(imageURL.openStream());
				OutputStream localImage = new BufferedOutputStream(new FileOutputStream("D:\\Downloads\\asdf\\chan\\thread\\" + json.get("filename") + json.get("ext")));
				for(int i; (i = remoteImage.read()) != -1;)
					localImage.write(i);
				remoteImage.close();
				localImage.close();
			}
		}
	}

	public static void printJsonArray(JSONArray json) throws IOException {
		for(int i = 0; i < json.size(); i++){
			//System.out.print("\t");
			printJson(json.get(i));
		}
	}

	void printFirstPageJSON() throws IOException, ParseException{
		JSONParser parser = new JSONParser();
		for(int page = 1; page < 2; page++){
			System.out.println("Page " + page + ":");
			String indexB = Tools.httpRequest("http://a.4cdn.org/b/" + page + ".json", new HashMap<String, String>());
			JSONObject indexBJson = (JSONObject)parser.parse(indexB);

			for(Object key: indexBJson.keySet())
			{
				System.out.println(key + ": ");
				if(indexBJson.get(key) instanceof JSONArray)
				{
					for(int i = 0; i < ((JSONArray) indexBJson.get(key)).size(); i++)
					{
						System.out.println((i + ((page-1)*15)));// + ":\t" + ((JSONArray) indexBJson.get(key)).get(i));
						JSONObject jsonObject = (JSONObject)((JSONArray) indexBJson.get(key)).get(i);

						System.out.println("\t" + 0 + "\t" +
								((JSONObject)((JSONArray)jsonObject.get("posts")).get(0)).get("no") + "\t" +
								((JSONObject)((JSONArray)jsonObject.get("posts")).get(0)).get("images") + " images\t" +
								((JSONObject)((JSONArray)jsonObject.get("posts")).get(0)).get("replies") + " replies\n\t\t\t\t\t" +
								((JSONObject)((JSONArray)jsonObject.get("posts")).get(0)).get("com")
								//);
								+ "\n\t\t" + ((JSONArray)jsonObject.get("posts")).get(0));

						for(int j = 1; j < ((JSONArray)jsonObject.get("posts")).size(); j++)
						{
							System.out.println("\t" + j + "\t" +
									((JSONObject)((JSONArray)jsonObject.get("posts")).get(j)).get("no") + "\t" +
									((JSONObject)((JSONArray)jsonObject.get("posts")).get(j)).get("com"));
						}
					}
				}
				else
				{
					System.out.println("\t" + indexBJson.get(key));
				}
			}
		}
	}

}
