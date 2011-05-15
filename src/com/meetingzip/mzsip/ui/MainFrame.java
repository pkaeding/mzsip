/*
    This file is part of Peers, a java SIP softphone.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
    Copyright 2010 Yohann Martineau 
 */

package com.meetingzip.mzsip.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.SwingUtilities;

import net.sourceforge.peers.Logger;
import net.sourceforge.peers.sip.Utils;
import net.sourceforge.peers.sip.syntaxencoding.SipUriSyntaxException;
import net.sourceforge.peers.sip.transport.SipRequest;
import net.sourceforge.peers.sip.transport.SipResponse;

public class MainFrame implements WindowListener, ActionListener {

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(args);
            }
        });
    }

    private static void createAndShowGUI(String[] args) {
        new MainFrame(args);
    }

    private EventManager eventManager;
    private Registration registration;
    private Logger logger;
	private String extension;
	private String server;

    public MainFrame(String extension, String server) {
    	this(new String[0]);
    	this.extension = extension;
    	this.server = server;
    }
    public MainFrame(String extension, String server, String[] args) {
    	this(args);
    	this.extension = extension;
    	this.server = server;
    }
    public MainFrame(final String[] args) {
        String peersHome = Utils.DEFAULT_PEERS_HOME;
        if (args.length > 0) {
            peersHome = args[0];
        }
        logger = new Logger(peersHome);

        registration = new Registration(logger);

        Thread thread = new Thread(new Runnable() {
            public void run() {
                String peersHome = Utils.DEFAULT_PEERS_HOME;
                if (args.length > 0) {
                    peersHome = args[0];
                }
                eventManager = new EventManager(MainFrame.this,
                        peersHome, logger);
                try {
                    eventManager.register();
                } catch (SipUriSyntaxException e) {
                    logger.error("", e);
                }
            }
        });
        thread.start();

        try {
            while (eventManager == null) {
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            return;
        }
    }

    // window events

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
        eventManager.windowClosed();
    }

    @Override
    public void windowClosing(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    // action event

    @Override
    public void actionPerformed(ActionEvent e) {
        eventManager.callClicked("sip:" + extension + "@" + server);
    }

    public void registerFailed(SipResponse sipResponse) {
        registration.registerFailed();
    }

    public void registerSuccessful(SipResponse sipResponse) {
        registration.registerSuccessful();
    }

    public void registering(SipRequest sipRequest) {
        registration.registerSent();
    }

    public void socketExceptionOnStartup() {
        System.exit(1);
    }

}
