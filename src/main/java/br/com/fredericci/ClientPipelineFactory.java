/**
 * 
 */
package br.com.fredericci;

import static org.jboss.netty.channel.Channels.pipeline;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

/**
 * @author Dennys Fredericci
 * 
 */
public class ClientPipelineFactory implements ChannelPipelineFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.netty.channel.ChannelPipelineFactory#getPipeline()
	 */
	@Override
	public ChannelPipeline getPipeline() throws Exception {

		// Create a default pipeline implementation.
		ChannelPipeline pipeline = pipeline();

		// Add the text line codec combination first,
		pipeline.addLast("framer", new LantonixFrameDecoder());
		pipeline.addLast("decoder", new StringDecoder());
		pipeline.addLast("encoder", new StringEncoder());

		// and then business logic.
		pipeline.addLast("handler", new ClientHandler());

		return pipeline;

	}

}
