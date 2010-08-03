package pl.softwaremill.common.uitest.jboss;

import pl.softwaremill.common.uitest.jboss.Deployment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Pawel Wrzeszcz (pawel . wrzeszcz [at] gmail . com)
 */
public class StandardDeployment implements Deployment {

	private String fromPath;
	private String waitForMessage;

	public StandardDeployment(String fromPath) {
		this(fromPath, null);
	}

	public StandardDeployment(String fromPath, String waitForMessage) {
		this.fromPath = fromPath;
		this.waitForMessage = waitForMessage;
	}

	@Override
	public void deploy(String deployDir) throws Exception {
		final File fromFile = new File(fromPath);
		final File toFile = new File(deployDir + "/" + fromFile.getName());
		deployFile(fromFile, toFile);
	}

	@Override
	public void undeploy(String deployDir) throws Exception {
		final File fromFile = new File(fromPath);
		final File toFile = new File(deployDir + "/" + fromFile.getName());
		undeploy(toFile);
	}

	@Override
	public String getWaitForMessage() {
		return waitForMessage;
	}

	protected void deployFile(File from, File to) {
		deployFile(from, to, null);
	}

	public static void undeploy(File file, String name) throws Exception {
		if (file.exists()) {
			if (file.delete()) {
				System.out.println("--- " + name + " deleted");
			} else {
				throw new Exception("Couldn't delete " + name + " !");
			}
		} else {
			System.out.println("--- " + name + " was not present");
		}
	}

	protected void undeploy(File... files) throws Exception {
		for (File file : files) {
			undeploy(file, file.getName());
		}
	}

	public static void deployFile(File from, File to, String message) {
		System.out.println("--- Deploy file " + from + " to " + to);
		try {
			InputStream in = new FileInputStream(from);

			//For Overwrite the file.
			OutputStream out = new FileOutputStream(to);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			if (message != null) {
				System.out.println(message);
			}
		}
		catch (FileNotFoundException ex) {
			System.out.println(ex.getMessage() + " in the specified directory.");
			System.exit(0);
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public String toString() {
		return fromPath;
	}
}