/**
 * 
 */
package br.com.fredericci;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

/**
 * @author Dennys Fredericci
 * 
 */
public class Main {

	public static void main(String[] args) throws IOException {

		String host = "localhost";
		Integer port = 10001;

		// Configure the client.
		ClientBootstrap bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));

		// Configure the pipeline factory.
		bootstrap.setPipelineFactory(new ClientPipelineFactory());

		// Start the connection attempt.
		ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));

		// Wait until the connection attempt succeeds or fails.
		Channel channel = future.awaitUninterruptibly().getChannel();
		if (!future.isSuccess()) {
			future.getCause().printStackTrace();
			bootstrap.releaseExternalResources();
			return;
		}
		
		// Read commands from the stdin.
		ChannelFuture lastWriteFuture = null;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		for (;;) {
			String line = in.readLine();
			if (line == null) {
				break;
			}

			// Sends the received line to the server.
			lastWriteFuture = channel.write(line + "\r\n");

			// If user typed the 'bye' command, wait until the server closes
			// the connection.
			if (line.toLowerCase().equals("bye")) {
				channel.getCloseFuture().awaitUninterruptibly();
				break;
			}
		}

		// Wait until all messages are flushed before closing the channel.
		if (lastWriteFuture != null) {
			lastWriteFuture.awaitUninterruptibly();
		}

		// Close the connection. Make sure the close operation ends because
		// all I/O operations are asynchronous in Netty.
		channel.close().awaitUninterruptibly();

		// Shut down all thread pools to exit.
		bootstrap.releaseExternalResources();

	}

}
