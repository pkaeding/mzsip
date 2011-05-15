package com.meetingzip.mzsip;

import java.applet.Applet;

import net.sourceforge.peers.Logger;
import net.sourceforge.peers.gui.EventManager;
import net.sourceforge.peers.sip.core.useragent.UserAgent;

import com.meetingzip.mzsip.ui.MainFrame;

public class MZSip extends Applet {
	private static final long serialVersionUID = 1L;

	String extension = "70009";
	String server = "local-bbb";


	private UserAgent userAgent;
	private EventManager evtMgr;
	
	Logger logger = new Logger(null);

	private MainFrame main;
	
	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void init() {
		super.init();
		main = new MainFrame(extension, server, new String[]{"/Users/pkaeding/meetingzip/peers"});
		main.actionPerformed(null);
	}
	
	public UserAgent getUserAgent() {
		return userAgent;
	}

	public String getExtension() {
		return extension;
	}

	public String getServer() {
		return server;
	}

	/**
	 * Initiate a call to the SIP endpoint
	 */
	public void connect() {
		evtMgr.callClicked(getExtension() + "@" + getServer());
	}
	
	/**
	 * Set whether the output (sound) should be muted or not
	 * 
	 * @param mute true if you want to mute the sound, false if you want to hear it
	 */
	public void muteOutput(boolean mute) {
		
	}
	
	/**
	 * Set whether the input (microphone) should be muted or not
	 * 
	 * @param mute true if you want to mute the mic, false if you want to send 
	 * input from the mic
	 */
	public void muteInput(boolean mute) {
		
	}

}
