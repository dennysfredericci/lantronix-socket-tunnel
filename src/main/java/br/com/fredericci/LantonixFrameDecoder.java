/**
 * 
 */
package br.com.fredericci;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

/**
 * @author Dennys Fredericci
 * 
 */
public class LantonixFrameDecoder extends FrameDecoder {

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {

		String message = new String(buffer.array());

		buffer.readSlice(buffer.array().length);

		System.out.println(message);

		return message;
	}

}
