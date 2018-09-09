package com.csdapp.fileManager;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.csdapp.model.Mother;

public class FileManager {
	private String projectPath;
	private String dataFileName;
	private String userFileName;
	private File userFile = null;	
	private File dataFile = null;
	
	public FileManager(String projectPath, String dataFileName, String userFileName){
		this.projectPath = projectPath;
		this.dataFileName = projectPath + dataFileName + "_" + ".txt";
		this.userFileName = userFileName;
		Path p = Paths.get(this.dataFileName);
		System.out.println("Path: " + p.toString());
		this.dataFile = new File(p.toString());
		try {
			File directory = new File(projectPath);
			if (! directory.exists()){
				directory.mkdirs();
			}
			if(! dataFile.exists()){
				dataFile.createNewFile();				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("null")
	public void saveUserFile(String str, int generation){
		String temp = projectPath + userFileName + "_" + generation + ".txt";
		userFile = new File(temp);
		try{
			if(! userFile.exists()){
				userFile.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if( userFile.length() > 0){
			PrintWriter pw = null;
			try {
				pw = new PrintWriter(userFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				pw.close();
			} finally {
				pw.close();
			}
		}
		Writer fw = null;
		BufferedWriter bw = null;
		try {
			if(userFile.canWrite()){
				fw = new FileWriter(userFile, true);
				bw = new BufferedWriter(fw);
				bw.append(str);
				bw.flush();
				fw.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		    try {
		        if(bw != null)
		            bw.close();
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }
		    try {
		        if(fw != null)
		            fw.close();
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }
		}
	}
	
	@SuppressWarnings("null")
	public void saveSimInfoFile (String ... strings) {
		try{
			if(! dataFile.exists()){
				dataFile.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if( dataFile.length() > 0){
			PrintWriter pw = null;
			try {
				pw = new PrintWriter(dataFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				pw.close();
			} finally {
				pw.close();
			}
		}
		Writer fw = null;
		BufferedWriter bw = null;
		try {
			if(dataFile.canWrite()){
				fw = new FileWriter(dataFile, true);
				bw = new BufferedWriter(fw);
				for(String s : strings) {
					bw.append(s);
					bw.newLine();
				}
				bw.flush();
				fw.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		    try {
		        if(bw != null)
		            bw.close();
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }
		    try {
		        if(fw != null)
		            fw.close();
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }
		}
	}
	
	public void saveDataFile(List<Mother> list){
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream(dataFile);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(list);
			oos.flush();
			oos.close();
			fos.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(oos != null){
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Mother> readDataFile(){
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		ObjectInputStream ois = null;
		try{
			fis = new FileInputStream(dataFile);
			bis = new BufferedInputStream(fis);
			ois = new ObjectInputStream(bis);
			ArrayList<Mother> list = new ArrayList<>();
			list = (ArrayList<Mother>) ois.readObject();
			return list;
			
		} catch  (IOException e){
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if(ois != null){
				try{
					ois.close();
				} catch (IOException e){
					e.printStackTrace();
				}
			}
			if(bis != null){
				try{
					bis.close();
				} catch (IOException e){
					e.printStackTrace();
				}
			}
			if(fis != null){
				try{
					fis.close();
				} catch (IOException e){
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

	public String getDataFileName() {
		return dataFileName;
	}

	public void setDataFileName(String dataFileName) {
		this.dataFileName = dataFileName;
	}

	public String getUserFileName() {
		return userFileName;
	}

	public void setUserFileName(String userFileName) {
		this.userFileName = userFileName;
	}

	public File getUserFile() {
		return userFile;
	}

	public void setUserFile(File userFile) {
		this.userFile = userFile;
	}

	public File getDataFile() {
		return dataFile;
	}

	public void setDataFile(File dataFile) {
		this.dataFile = dataFile;
	}
}
