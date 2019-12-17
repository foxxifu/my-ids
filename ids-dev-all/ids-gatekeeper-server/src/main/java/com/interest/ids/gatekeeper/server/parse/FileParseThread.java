package com.interest.ids.gatekeeper.server.parse;

import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interest.ids.common.project.buffer.UnSafeHeapBuffer;

/**
 * 
 * @author lhq
 *
 *
 */
public class FileParseThread implements Runnable {

	private static final Logger log = LoggerFactory
			.getLogger(FileParseThread.class);

	private LinkedBlockingQueue<UnSafeHeapBuffer> tasks = new LinkedBlockingQueue<UnSafeHeapBuffer>();

	public void offerMsg(UnSafeHeapBuffer buffer) {
		tasks.offer(buffer);
	}

	@Override
	public void run() {

		try {
			UnSafeHeapBuffer buffer = tasks.take();
			if (buffer != null) {
				consume(buffer);
			}
		} catch (Exception e) {
			log.error("deal task error", e);
		}

	}

	private void consume(UnSafeHeapBuffer buffer) {

	}

	public static class FileTask {

		private byte cmd;

		private UnSafeHeapBuffer buffer;

		public byte getCmd() {
			return cmd;
		}

		public void setCmd(byte cmd) {
			this.cmd = cmd;
		}

		public UnSafeHeapBuffer getBuffer() {
			return buffer;
		}

		public void setBuffer(UnSafeHeapBuffer buffer) {
			this.buffer = buffer;
		}
	}

}
