package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;
import fr.theshark34.openlauncherlib.minecraft.GameFolder;
import fr.theshark34.openlauncherlib.minecraft.GameInfos;
import fr.theshark34.openlauncherlib.minecraft.GameTweak;
import fr.theshark34.openlauncherlib.minecraft.GameType;
import fr.theshark34.openlauncherlib.minecraft.GameVersion;
import fr.theshark34.openlauncherlib.minecraft.MinecraftLauncher;
import fr.theshark34.openlauncherlib.util.ramselector.RamSelector;

public class LauncherMain {
	public static String folderName = "\\.hardminecraft\\";

	public static void main(String[] args){
		System.out.println("Log: Launcher launched");
		


		//ON DEMMARE LA FENETRE
		LauncherFrame Lframe = new LauncherFrame();
		Lframe.MainWindows();
		Lframe.setInstance(Lframe);
				
	}
	
	
	
	public synchronized static void LaunchMinecraft(String pseudo, String token, String uuid) throws LaunchException {
		System.out.println("ON LANCE EN 1.12.2 MODEEDANS MAIN");
		GameInfos infos = new GameInfos("hardminecraft", new GameVersion("1.12.2", GameType.V1_8_HIGHER), new GameTweak[] {GameTweak.FORGE});
		AuthInfos authInfos = new AuthInfos(pseudo, token, uuid);
		
		ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(infos, GameFolder.FLOW_UPDATER, authInfos);
		RamSelector ramSelector = LauncherFrame.getInstance().getRamSelector();
		profile.getVmArgs().addAll(Arrays.asList(ramSelector.getRamArguments()));
		ExternalLauncher launcher = new ExternalLauncher(profile);
		launcher.launch();
    }
	
	
	
	
	public static ArrayList<String> getTokenMojang(String email,  String password) throws Exception {
		JSONParser jsonP = new JSONParser();
		String jsonInputString = "{\r\n"
				+ "    \"agent\": {\r\n"
				+ "        \"name\": \"Minecraft\",\r\n"
				+ "        \"version\": 1                       \r\n"
				+ "                                           \r\n"
				+ "    },\r\n"
				+ "    \"username\": \"" + email+ "\",\r\n"
				+ "    \"password\": \"" + password + "\",\r\n"
				+ "    \"requestUser\": true                     \r\n"
				+ "}";		
		
		
	    byte[] contentBytes = jsonInputString.getBytes("UTF-8");

	    
		URL url = new URL("https://authserver.mojang.com/authenticate");
		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Accept-Charset", "UTF-8");
	    connection.setRequestProperty("Content-Type", "application/json");
	    connection.setRequestProperty("Content-Length", Integer.toString(contentBytes.length));
		
	    OutputStream requestStream = connection.getOutputStream();
	    requestStream.write(contentBytes, 0, contentBytes.length);
	    requestStream.close();

	    String response;
	    
	    BufferedReader responseStream;
	    
	    if (connection.getResponseCode() == 200) {
	        responseStream = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
	    } else {
	        responseStream = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "UTF-8"));
	    }

	    response = responseStream.readLine();
	    responseStream.close();

	    if (connection.getResponseCode() == 200) {
			JSONObject responseJSON = (JSONObject) jsonP.parse(response);	
			JSONObject selectedProfile = (JSONObject) responseJSON.get("selectedProfile");
			
			String id = (String) selectedProfile.get("id");
			String pseudo = (String) selectedProfile.get("name");
			String accessToken = (String) responseJSON.get("accessToken");
			
			ArrayList<String> identifiant = new ArrayList<String>();
			identifiant.add(id);
			identifiant.add(pseudo);
			identifiant.add(accessToken);

			return identifiant;
			
	    } else {
	        // Failed to log in; response will contain data about why
	        
	        JSONObject responseJSON = (JSONObject) jsonP.parse(response);
	        String errorMessage = (String) responseJSON.get("errorMessage");
	        	        
	        System.out.println("ERREUR !" + errorMessage);
	        ArrayList<String> errorMessageArray = new ArrayList<String>();
	        errorMessageArray.add(errorMessage);
	        return errorMessageArray;
	    }
	
	}
	
}
