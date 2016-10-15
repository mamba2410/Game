package callummcgregor.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import callummcgregor.common.entity.EntityBase;
import callummcgregor.lib.Utilities;

public class SavesHandler {
	
	public static final File saveDirectory = new File("/saves/");
	public static List<File> saves;
	
	public SavesHandler(){
		saves = new ArrayList<File>();
		if(!saveDirectory.exists())
			try {
				saveDirectory.createNewFile();
			} catch (IOException e) {
				Utilities.printf("Couldn't create save directory");
			}
		
		for(int i = 0; i < saveDirectory.listFiles().length; i++){
			saves.add(saveDirectory.listFiles()[i]);
		}
	}
	
	public void createSave(String name){
		saves.add(new File(saveDirectory.getName()+name+".gsv"));
	}
	
	public void addEntity(String savename, EntityBase e){
		try {
			@SuppressWarnings("resource")
			BufferedWriter writer = new BufferedWriter(new FileWriter(saveDirectory));
			writer.append(String.format("E: {posX:%s} {posY:%s} {health:%s} {type:%s};", e.posX, e.posY, e.getHealth(), e.getClass()));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
