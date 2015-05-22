package com.epiphan.qa.grid;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.grid.common.RegistrationRequest;
import org.openqa.grid.internal.Registry;
import org.openqa.grid.internal.TestSession;
import org.openqa.grid.selenium.proxy.DefaultRemoteProxy;

/**
 * This proxy, when injected into the grid, counts the number of tests sent to
 * each node. Once a node has exceeded it's limit, the proxy invokes that nodes
 * NodeKillerServlet, terminating it.
 * 
 * @author Ian Harvey [iharvey@epiphan.com]
 *
 */
public class GridProxy extends DefaultRemoteProxy {

	private volatile int counter;

	private volatile long latestNewSession = 0;
	
	private long timeout;
	
	private NodePoller pollingThread = null;
	
	private Properties properties;
	
	

	public GridProxy(RegistrationRequest request, Registry registry)
			throws IOException {
		super(request, registry);

		System.out.println("New proxy instatiated for node at : "
				+ getRemoteHost().getHost());
		properties = new Properties();
		FileInputStream file = new FileInputStream("./grid.properties");
		properties.load(file);
		file.close();
		counter = Integer.parseInt(properties.get("uniqueSessionCount").toString());
		timeout = Integer.parseInt(properties.get("defaultSessionTimeout").toString());
	}

	@Override
	public void startPolling() {
		super.startPolling();

		pollingThread = new NodePoller(this);
		pollingThread.start();
	}

	@Override
	public void stopPolling() {
		super.stopPolling();

		pollingThread.interrupt();
	}

	/**
	 * Decrement the counter until zero
	 * 
	 * @return - <code>true</code> if decrementing didn't result in zero
	 */
	private synchronized boolean decrementCounter() {
		if (this.counter == 0) {
			return false;
		}
		this.counter = this.counter - 1;
		return true;
	}

	/**
	 * Invoke to decide if a node has reached it's limit.
	 * 
	 * @return - <code>true</code> if node can be let go
	 */
	public synchronized boolean shouldReleaseNode() {
		if (this.counter == 0) {
			System.out.println("Safe to release node : "
					+ getRemoteHost().getHost());
			return true;
		}
		return false;
	}
/*
	@Override
	public void beforeSession(TestSession session) {

		String ip = getRemoteHost().getHost();
		if (decrementCounter()) {
			super.beforeSession(session);
		} else {
			System.out.println("This proxy has no free slots " + ip);
			System.exit(counter);
			return;
		}
	}
*/
	
	@Override
	public TestSession getNewSession(Map requestedCapability){
		boolean slotsFree = false;
		if (shouldReleaseNode()) {
			return null;
		}
		
		TestSession session = super.getNewSession(requestedCapability);
		
		if (session != null) {
			slotsFree = decrementCounter();
		}
		
		if (!slotsFree) {
			System.out.println("Node full until refresh");
		}
		latestNewSession = System.currentTimeMillis();
		return session;
	}
	
	public boolean timedOut() {
		if (System.currentTimeMillis() - latestNewSession >= timeout) {
			return true;
		}
		return false;
	}
	/**
	 * This class is used to poll whether nodes are ready to be let go.
	 * 
	 */
	static class NodePoller extends Thread {
		private GridProxy proxy = null;

		public NodePoller(GridProxy proxy) {
			this.proxy = proxy;
		}

		@Override
		public void run() {
			while (true) {
				boolean isBusy = proxy.isBusy();
				boolean canRelease = proxy.shouldReleaseNode();
				boolean timedOut = proxy.timedOut();

				if (!isBusy && canRelease) {
					proxy.getRegistry().removeIfPresent(proxy);
					System.out.println(proxy.getRemoteHost().getHost()
							+ " has been released from the grid");
					shutDownNode();
					return;
				} else if (isBusy && timedOut) {
					System.out.println ("Proxy reads as busy, but is timed out");
					shutDownNode();
					return;
				}
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					return;
				}
			}

		}

		private void shutDownNode() {
			HttpClient client = HttpClientBuilder.create().build();
			StringBuilder url = new StringBuilder();
			url.append("http://");
			url.append(proxy.getRemoteHost().getHost() + ":"
					+ proxy.getRemoteHost().getPort());
			url.append("/extra/");
			url.append(NodeKillerServlet.class.getSimpleName());
			HttpPost post = new HttpPost(url.toString());
			try {
				client.execute(post);
			} catch (ClientProtocolException e) {
			} catch (IOException e) {
			}
			System.out.println("Node " + proxy.getRemoteHost().getHost()
					+ " shut down successfully");
		}
	}

}
