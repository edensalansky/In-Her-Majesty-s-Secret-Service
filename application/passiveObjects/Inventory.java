package bgu.spl.mics.application.passiveObjects;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



/**
 *  That's where Q holds his gadget (e.g. an explosive pen was used in GoldenEye, a geiger counter in Dr. No, etc).
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Inventory {
	private static class SingletonHolder {
		private static Inventory instance = new Inventory();
	}
	private List<String> gadgets = new LinkedList<String>(){
	};
	/**
     * Retrieves the single instance of this class.
     */
	public static Inventory getInstance() {
		return SingletonHolder.instance;
	}

	/**
     * Initializes the inventory. This method adds all the items given to the gadget
     * inventory.
     * <p>
     * @param inventory 	Data structure containing all data necessary for initialization
     * 						of the inventory.
     */
	public void load (String[] inventory) {
		for(int i=0; i<inventory.length; i++){
			gadgets.add(inventory[i]);
		}
	}
	
	/**
     * acquires a gadget and returns 'true' if it exists.
     * <p>
     * @param gadget 		Name of the gadget to check if available
     * @return 	‘false’ if the gadget is missing, and ‘true’ otherwise
     */
	public boolean getItem(String gadget){
		boolean item;
		synchronized (gadgets){
			item =  gadgets.contains(gadget);
			gadgets.remove(gadget);
			}
			return item;
		}

	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<String> which is a
	 * list of all the of the gadgeds.
	 * This method is called by the main method in order to generate the output.
	 */
	public void printToFile(String filename)  {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try {String output=gson.toJson(this);
			Files.write(Paths.get(filename),output.getBytes());
		}
		catch (IOException e){e.printStackTrace();}

	}
}
