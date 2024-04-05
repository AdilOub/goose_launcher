package main;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import fr.flowarg.flowlogger.ILogger;
import fr.flowarg.flowlogger.Logger;
import fr.flowarg.flowupdater.FlowUpdater;
import fr.flowarg.flowupdater.download.IProgressCallback;
import fr.flowarg.flowupdater.download.Step;
import fr.flowarg.flowupdater.download.json.Mod;
import fr.flowarg.flowupdater.utils.UpdaterOptions;
import fr.flowarg.flowupdater.versions.AbstractForgeVersion;
import fr.flowarg.flowupdater.versions.ForgeVersionBuilder;
import fr.flowarg.flowupdater.versions.ForgeVersionBuilder.ForgeVersionType;
import fr.flowarg.flowupdater.versions.VanillaVersion;
import fr.flowarg.flowupdater.versions.VersionType;


public class LauncherUpdate {
	//public static URL modJson = LauncherMain.class.getResource("other/mods.json");
	public static String jsonModAParse = "";

	public static long SizeTotal;
	private static boolean mcHeli = false;
	private static boolean mod = true;
	
	private static String folderName = LauncherMain.folderName;

	public static boolean checkIfFileExist(String FOLDER_PATH){
		File f = new File(FOLDER_PATH);
		if (f.exists() && f.isDirectory()) {
		   return true;
		}
		return false;
	}
	
	public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
	    File destFile = new File(destinationDir, zipEntry.getName());

	    String destDirPath = destinationDir.getCanonicalPath();
	    String destFilePath = destFile.getCanonicalPath();

	    if (!destFilePath.startsWith(destDirPath + File.separator)) {
	        throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
	    }

	    return destFile;
	}
	
	public static void downloadFolder(String FILE_URL, String TEMP_OUTPUT_PATH, String OUTPUT_PATH) {
		System.out.println("download FILE: " + FILE_URL);
		if(checkIfFileExist(OUTPUT_PATH)) {
			System.out.println("existe déjà !");
			return;	
		}
		
		System.out.println("on va download le fichier à l'url: ");
		
		try {
			
			BufferedInputStream in = new BufferedInputStream(new URL(FILE_URL).openStream());
			FileOutputStream fileOutputStream = new FileOutputStream(TEMP_OUTPUT_PATH);

				  
			byte dataBuffer[] = new byte[1024];
		    
			
			int bytesRead;
		    while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
		        fileOutputStream.write(dataBuffer, 0, bytesRead);
		    }
		    fileOutputStream.close();
		    
		    
		    @SuppressWarnings("resource")
			ZipInputStream zis = new ZipInputStream(new FileInputStream(TEMP_OUTPUT_PATH));
		    ZipEntry  zipEntry = zis.getNextEntry();
		    
	        while (zipEntry != null) {
	        	File newFile = newFile(new File(OUTPUT_PATH), zipEntry);
	            if (zipEntry.isDirectory()) {
	                if (!newFile.isDirectory() && !newFile.mkdirs()) {
	                    throw new IOException("Failed to create directory " + newFile);
	                }
	            } else {
	                // fix for Windows-created archives
	                File parent = newFile.getParentFile();
	                if (!parent.isDirectory() && !parent.mkdirs()) {
	                    throw new IOException("Failed to create directory " + parent);
	                }
	                
	                // write file content
	                FileOutputStream fos = new FileOutputStream(newFile);
	                int len;
	                while ((len = zis.read(dataBuffer)) > 0) {
	                    fos.write(dataBuffer, 0, len);
	                }
	                fos.close();
	            }
	         zipEntry = zis.getNextEntry();	         
	         }
	        
	        
	         zis.closeEntry();
	         zis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void updateMinecraftVanilla() throws Exception{
		String workingDirectory;
		String OS = (System.getProperty("os.name")).toUpperCase();
		
	    // MIAM :)
	    if (OS.contains("WIN"))
	    {
	        workingDirectory = System.getenv("AppData");
	    }
	    else
	    {
	        workingDirectory = System.getProperty("user.home");
	        workingDirectory += "/Library/Application Support";
	    }
	    
		try {
			URL website = new URL("http://adandcoinc.xyz/launcher/minecraftscary/servers.dat");
			try (BufferedInputStream bis = new BufferedInputStream(website.openStream());  
					  FileOutputStream fos = new FileOutputStream(workingDirectory + folderName + "/server.dat")) {
					    byte data[] = new byte[1024];
					    int byteContent;
					    while ((byteContent = bis.read(data, 0, 1024)) != -1) {
					        fos.write(data, 0, byteContent);
					    }
					} catch (IOException e) {
					   e.printStackTrace(System.out);
					}
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		
		//TELECHARGE MOD.JSON
		if(mod) {
			try {
				URL website = new URL("http://adandcoinc.xyz/launcher/minecraftscary/mods.json");
				try (BufferedInputStream bis = new BufferedInputStream(website.openStream());  
						  FileOutputStream fos = new FileOutputStream(workingDirectory + folderName + "mods.json")) {
						    byte data[] = new byte[1024];
						    int byteContent;
						    while ((byteContent = bis.read(data, 0, 1024)) != -1) {
						        fos.write(data, 0, byteContent);
						    }
						} catch (IOException e) {
						   e.printStackTrace(System.out);
						}
	
			} catch (MalformedURLException e1) {
				System.out.println("ERROR DOWNLOAD JSON");
				e1.printStackTrace();
			}
		}
		
		//TECLECHARGE MC HELI
		if(mcHeli) {
			File f = new File(workingDirectory+ folderName +"/temp/");
			try{
			    if(f.mkdir()) { 
			        System.out.println("dossier créer");
			    } else {
			        System.out.println("dossier pas là :(");
			    }
			} catch(Exception e){
			    e.printStackTrace();
			} 
			downloadFolder("http://adandcoinc.xyz/launcher/nico/special/mcheli.zip", workingDirectory+ folderName+ "/temp/mcheli.zip", workingDirectory+ folderName + "/mods/mcheli");
		}
		
		 
		
		   final VanillaVersion version = new VanillaVersion.VanillaVersionBuilder()
			        .withName("1.12.2")
			        .withSnapshot(false)
			        .withVersionType(VersionType.FORGE)
			        .build();	 
		

		final List<Mod> mods = new ArrayList<>();
		try {
		//URL modJson = LauncherMain.class.getResource("other/mods.json");
		File modJson =  new File(workingDirectory + folderName + "/mods.json");
		System.out.println("---- IMPORTANT ---- \n\n");
		System.out.println(modJson.getAbsolutePath());
//        URLConnection yc = modJson.openConnection();
//		BufferedReader in = new BufferedReader(new InputStreamReader(
//		yc.getInputStream()));
		String jsonModAParse = "";
//		while ((inputLine = in.readLine()) != null)
//		{
//		jsonModAParse += inputLine;}
//		in.close();
		
		Scanner myReader = new Scanner(modJson);
	      while (myReader.hasNextLine()) {
	        String data = myReader.nextLine();
	        System.out.println(data);
	        jsonModAParse += data;
	      }
	      System.out.println("--- IMPORTANCE ++ ---");
	      System.out.println(jsonModAParse);
	      myReader.close();
	   try {
		   	System.out.println("ON VA AJOUTER LES MODS");
		   	System.out.println(jsonModAParse);
			JSONParser jsonP = new JSONParser();
			JSONObject jsonO = (JSONObject) jsonP.parse(jsonModAParse);
			JSONArray JSONmods = (JSONArray) jsonO.get("mods");
			
			
			for (Object object : JSONmods) {
				JSONObject CurrentMod = (JSONObject) jsonP.parse(object.toString());
				String name = (String) CurrentMod.get("name");
				String sha1 = (String) CurrentMod.get("sha1");
				long size = (long) CurrentMod.get("size");
				String downloadURL = (String) CurrentMod.get("downloadURL");
				mods.add(new Mod(name, sha1, size, downloadURL));
				System.out.println("MOD: " + name + " ajouté ");
				SizeTotal += size;
				}

			System.out.println("Size total: " + SizeTotal);
			
			}catch(Exception e) {
				System.out.println("Erreur en ajoutant les mods! " + e);
			}
	   
		}catch(Exception errorReadJson) {
		System.out.println("GRAVE");
		System.out.println("errorReadJson: " + errorReadJson);
		jsonModAParse = "errored";
		}
	    
	    System.out.println("FIN DE L'AJOUT DES MODS: ");
	    System.out.println(mods);
	    
	   final ILogger logger = new Logger("[launcher log]", Paths.get(workingDirectory + LauncherMain.folderName + "/log.txt"), true);
 
		   
	    final UpdaterOptions options = new UpdaterOptions.UpdaterOptionsBuilder()
	        .withEnableCurseForgePlugin(false)
	        .withEnableOptifineDownloaderPlugin(false)
	        .build();
	    
	    IProgressCallback callbackLauncher = new IProgressCallback() {
	        @SuppressWarnings("unused")
			private final DecimalFormat decimalFormat = new DecimalFormat("#.#");
	        @SuppressWarnings("unused")
			private Step currentStep;
	        @Override
	        public void init(ILogger logger) {}

	        @Override
	        public void step(Step step) {
	          this.currentStep = step;
	          System.out.println("OMG STEP: " + step);
	        }

	        @Override
	        public void update(long downloaded, long max) {
	                double percent = downloaded * 100.0d / max;
	                //logger.debug("OMG: " + downloaded);
	                //System.out.println("OMG STEP: " + currentStep);
	                LauncherFrame.getInstance().setDownloadInfo(String.valueOf( (int) Math.floor(percent)) + "%");
	       }
	    };


	    
	    @SuppressWarnings("deprecation")
		final AbstractForgeVersion forge = new ForgeVersionBuilder(ForgeVersionType.NEW)
	    		.withForgeVersion("1.12.2-14.23.5.2855")
	    		.withVanillaVersion(version)
	            .withProgressCallback(callbackLauncher)
	            .withMods(mods)
	            .withLogger(logger)
	            .build();
	    
		System.out.println("ON LANCE EN 1.12.2 MODDER DANS LAUNCHER UPDATE");

	  
	    @SuppressWarnings("deprecation")
		final FlowUpdater updater = new FlowUpdater.FlowUpdaterBuilder()
	        .withVersion(version)
	        .withLogger(logger)
		    .withUpdaterOptions(options)
	        .withProgressCallback(callbackLauncher)
	        .withForgeVersion(forge)
	        .withUpdaterOptions(options)
		    .build();

	    
	    

	  System.out.println("CALLBACK :");
	  Thread ThreadUpdate = new Thread() {
		  String workingDirectory;
		public void run() {
		  try {
			  if (OS.contains("WIN"))
			    {
			        workingDirectory = System.getenv("AppData");
			    }
			    else
			    {
			        workingDirectory = System.getProperty("user.home");
			        workingDirectory += "/Library/Application Support";
			    }
			updater.update(Paths.get(workingDirectory + folderName));
		} catch (Exception e) {
			e.printStackTrace();
		}
		  }
	  };
	  ThreadUpdate.run();

	}
	
}
