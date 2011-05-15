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

import java.awt.event.ActionListener;

import net.sourceforge.peers.Config;
import net.sourceforge.peers.Logger;
import net.sourceforge.peers.sip.RFC3261;
import net.sourceforge.peers.sip.core.useragent.UserAgent;
import net.sourceforge.peers.sip.syntaxencoding.SipURI;
import net.sourceforge.peers.sip.syntaxencoding.SipUriSyntaxException;
import net.sourceforge.peers.sip.transport.SipRequest;
import net.sourceforge.peers.sip.transport.SipResponse;

/**
 * AccountFrame, edited with NetBeans IDE.
 * 
 * @author yohann
 */
public class AccountFrame {

    private static final long serialVersionUID = 1L;

    private Logger logger;

    /** Creates new form AccountFrame */
    public AccountFrame(ActionListener actionListener, UserAgent userAgent,
            Logger logger) {
        this.userAgent = userAgent;
        this.logger = logger;
        unregistering = false;
        registration = new Registration(logger);
    }

                       

    private void applyNewConfig() {
        Config config = userAgent.getConfig();
        String userpart = ""; // TODO jTextField1.getText();
        if (userpart != null) {
            config.setUserPart(userpart);
        }
        String domain = ""; // TODO jTextField2.getText();
        if (domain != null) {
            config.setDomain(domain);
        }
        String password = ""; // TODO jPasswordField1.getPassword();
        if (password != null) {
            config.setPassword(password);
        }
        String outboundProxy = ""; // TODO jTextField4.getText();
        if (outboundProxy != null) {
            SipURI sipURI;
            try {
                if ("".equals(outboundProxy.trim())) {
                    config.setOutboundProxy(null);
                } else {
                    if (!outboundProxy.startsWith(RFC3261.SIP_SCHEME)) {
                        outboundProxy = RFC3261.SIP_SCHEME
                            + RFC3261.SCHEME_SEPARATOR + outboundProxy;
                    }
                    sipURI = new SipURI(outboundProxy);
                    config.setOutboundProxy(sipURI);
                }
            } catch (SipUriSyntaxException e) {
                logger.error("sip uri syntax issue", e);
                return;
            }
        }
        config.save();
        unregistering = false;
        if (password != null) {
            Runnable runnable = new Runnable() {
                public void run() {
                    try {
                        userAgent.getUac().register();
                    } catch (SipUriSyntaxException e) {
                        logger.error("sip uri syntax issue", e);
                    }
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }
    }

    public void registering(SipRequest sipRequest) {
        registration.registerSent();
    }

    public synchronized void registerSuccess(SipResponse sipResponse) {
        if (unregistering) {
        	userAgent.close();
            applyNewConfig();
        } else {
            registration.registerSuccessful();
        }
    }

    public void registerFailed(SipResponse sipResponse) {
        if (unregistering) {
        	userAgent.close();
            applyNewConfig();
        } else {
            registration.registerFailed();
        }
    }

//    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
//        Runnable runnable;
//        if (userAgent.isRegistered()) {
//            synchronized (this) {
//                unregistering = true;
//            }
//            runnable = new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        userAgent.getUac().unregister();
//                    } catch (SipUriSyntaxException e) {
//                        logger.error("syntax error", e);
//                    }
//                }
//            };
//        } else {
//            runnable = new Runnable() {
//                @Override
//                public void run() {
//                    userAgent.close();
//                    applyNewConfig();
//                }
//            };
//        }
//        SwingUtilities.invokeLater(runnable);
//    }

    private boolean unregistering;
    private UserAgent userAgent;
    private Registration registration;

}

