package com.epiphan.qa.grid;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.exec.ShutdownHookProcessDestroyer;
import org.apache.commons.lang3.StringUtils;


/**
 * This is a class to spawn a given Jar by command line
 * @author Ian Harvey [iharvey@epiphan.com]
 */
public class NodeSpawner {
	
	static class JarFileAttributes{
		
		private File filePath;
		private String jarArgs;
		
		public File getFilePath(){
			return filePath;
		}
		
		public String getJarArgs(){
			return jarArgs;
		}
		
		@Override
		public String toString(){
			return "JarFileAttributes [filePath="+filePath.getAbsolutePath()+", jarArgs="+jarArgs+"]";
		}
		
		public JarFileAttributes(File filePath, String jarArgs){
			this.filePath = filePath;
			this.jarArgs = jarArgs;
		}
	}
	
	public static void main(String[] args) throws ExecuteException, IOException, InterruptedException {
		JarFileAttributes attributes = getJarFile(args);
		Properties properties = new Properties();
		
		properties.load(NodeSpawner.class.getResourceAsStream("spawner.properties"));
		long interval = Long.parseLong((String) properties.get("defaultInterval"));
		while(true){
			continuouslyRestart(attributes, interval);
			Thread.sleep(interval);
			System.out.println("Application exited, respawning...");
		}
	}
	
	public static JarFileAttributes getJarFile(String[] args){
		File file = null;
		boolean wasJarFound = false;
		int jarArgsIndex = 0;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < args.length; i++){
			if (args[i].contains(".jar")){
				file = new File(args[i]);
				wasJarFound = true;
				jarArgsIndex = i+1;
			}
		}
		if(!wasJarFound){
			throw new RuntimeException("Please specify the jar file");
		}
		for (int i = jarArgsIndex; i < args.length; i++){
			sb.append(args[i]).append(" ");
		}
		return new JarFileAttributes(file, sb.toString());
	}
	
	public static void continuouslyRestart(JarFileAttributes attributes, long interval)
			throws ExecuteException, IOException, InterruptedException {
		System.out.println("Spawning the application with "+attributes.toString());
		CommandLine cmdLine = new CommandLine("java");
		cmdLine.addArgument("-jar");
		cmdLine.addArgument(attributes.getFilePath().getCanonicalPath());
		String[] args = StringUtils.split(attributes.getJarArgs(), " ");
		for (String arg : args){
			cmdLine.addArgument(arg);
		}	
		System.out.println("Executing "+cmdLine.toString());
		
		DefaultExecutor executor = new DefaultExecutor();
		executor.setStreamHandler(new PumpStreamHandler(System.out));
		executor.setProcessDestroyer(new ShutdownHookProcessDestroyer());
		DefaultExecuteResultHandler handler = new DefaultExecuteResultHandler();
		executor.execute(cmdLine, handler);
		while(!handler.hasResult()){
			Thread.sleep(interval);
		}
		
		if (handler.hasResult()){
			ExecuteException e = handler.getException();
		}
	}
}
