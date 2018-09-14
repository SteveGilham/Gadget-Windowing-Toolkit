/****************************************************************
 **
 **  $Id: ShowUserGui.java,v 1.27 1997/11/20 19:41:28 kadel Exp $
 **
 **  $Source: /cvs/classes/dtai/gui/ShowUserGui.java,v $
 **
 ****************************************************************
 **
 **  Gadget Windowing Toolkit (GWT) Java Class Library
 **  Copyright (C) 1997  DTAI, Incorporated (http://www.dtai.com)
 **
 **  This library is free software; you can redistribute it and/or
 **  modify it under the terms of the GNU Library General Public
 **  License as published by the Free Software Foundation; either
 **  version 2 of the License, or (at your option) any later version.
 **
 **  This library is distributed in the hope that it will be useful,
 **  but WITHOUT ANY WARRANTY; without even the implied warranty of
 **  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 **  Library General Public License for more details.
 **
 **  You should have received a copy of the GNU Library General Public
 **  License along with this library (file "COPYING.LIB"); if not,
 **  write to the Free Software Foundation, Inc.,
 **  59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 **
 ****************************************************************/

package dtai.gui;

import dtai.gwt.GadgetDialog;
import dtai.util.ShowUserHandler;
import java.applet.Applet;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

/**
 * ShowUserGui - this shows the user things like error and warning messages, etc.
 *
 * @version	1.0b1
 * @author	DTAI, Incorporated
 *
 * $Id: ShowUserGui.java,v 1.27 1997/11/20 19:41:28 kadel Exp $
 *
 * $Source: /cvs/classes/dtai/gui/ShowUserGui.java,v $
 */

public class ShowUserGui extends Thread implements ShowUserHandler {

	private static final int ERROR = 0;
	private static final int WARNING = 1;
	private static final int INFO = 2;
	private static final int ABOUT = 3;
	private static final int SUSPEND = 4;

    private Frame outerFrame;
    private Applet applet;
    private ErrorDialog errorDialog;
    private WarningDialog warningDialog;
    private InfoDialog infoDialog;
    private AboutDialog aboutDialog;
    private int waiting = 0;
    private int suspended = 0;

	private DataInputStream pop;
	private DataOutputStream push;

	private boolean threaded = true; // MIGHT HAVE PROBLEMS WITH INTERACTIVE ORG CHART AND IE!!!

    /**
     * ShowUserGui default constructor
     */
    public ShowUserGui() {
		super("dtai.gui.ShowUserGui");

		/* There's a nasty bug in Java on NT (I think NT's the only platform).  If you
		 * create a dialog in a thread that then dies, the Graphics object won't work
		 * anymore.  Whatever resource holds "Graphics" objects appears to be stored
		 * on a per-thread level.  Therefore, I create all of these dialogs in the
		 * ShowUserGui thread now.
		 */

		if (threaded) {
    		PipedOutputStream pipe = new PipedOutputStream();
    		push = new DataOutputStream(pipe);
    		try {
    			pop = new DataInputStream( new PipedInputStream(pipe) );
    		}
    		catch (IOException ioe) {
    			ioe.printStackTrace();
    		}
    		setDaemon(true);
    		start();
    	}
    }

    /**
     * suspends showing error and warning (etc.) dialogs
     */
    public synchronized void doSuspend() {
        /**** THERE ARE MAJOR PROBLEMS WITH THIS...
        suspended++;
		if (threaded) {
			postDialog(SUSPEND);
		} else {
			processPosting(SUSPEND);
		}
		*/
    }

    /**
     * resumes showing error and warning (etc.) dialogs
     */
    public synchronized void doResume() {
        /**** THERE ARE MAJOR PROBLEMS WITH THIS...
        suspended--;
        System.err.println("notifying all");
        notifyAll();
        */
    }

	/**
	 * run
	 */
	public void run() {
	    if (dtai.util.Debug.getLevel() == 90) {
		    System.err.println("Started ShowUserGui thread.  Waiting for first message...");
		}
    	while (true) {
    		try {
    			int type = pop.readInt();
    			String message = null;
    			String extra = null;
    			if (type != SUSPEND) {
        			message = pop.readUTF();
        			if (type == ERROR || type == ABOUT) {
        			    extra = pop.readUTF();
        			}
        		}
    		    processPosting(type,message,extra);
    		}
    		catch (IOException ioe) {
    			// ignore broken pipe
    		}
		}
	}

    public void processPosting(int type) {
        processPosting(type,null,null);
	}

    public void processPosting(int type, String message) {
        processPosting(type,message,null);
	}

    public void processPosting(int type, String message, String extra) {
	    if (dtai.util.Debug.getLevel() == 90) {
		    System.err.println("popping message type "+type+": "+message);
		}
		switch(type) {
		case ERROR:
			reallyShowError(message,extra);
			break;
		case WARNING:
			reallyShowWarning(message);
			break;
		case INFO:
			reallyShowInfo(message);
			break;
		case ABOUT:
			reallyShowAbout(extra,message);
			break;
		case SUSPEND:
		    synchronized(this) {
                while (suspended > 0) {
                    try {
                        wait();
                    } catch (InterruptedException ie) {
                    }
                }
            }
            break;
		}
    }

    /**
     * shows user the applet
     * @param applet  the applet to be shown
     */
    public void setShowUserApplet(Applet applet) {
        this.applet = applet;
        if (applet == null) {
            outerFrame = null;
        } else {
            outerFrame = getOuterFrame();
        }
    }

    /**
     * show user the outer frame
     * @param outerFrame  the outer frame to be shown
     */
    public void setShowUserOuterFrame(Frame outerFrame) {
        this.outerFrame = outerFrame;
    }

    private Frame getOuterFrame() {
        if ( outerFrame != null ) {
            return outerFrame;
        }
        else {
		    if (dtai.util.Debug.getLevel() == 90) {
    		    System.err.println("finding outer frame...");
    		}
            if ( applet == null ) {
                return null;
            }
		    if (dtai.util.Debug.getLevel() == 90) {
    		    System.err.println("have applet...");
    		}
            Container last_parent = null;
            Container parent = applet.getParent();
            while ( parent != null ) {
                last_parent = parent;
                parent = parent.getParent();
            }
		    if (dtai.util.Debug.getLevel() == 90) {
    		    System.err.println("parent is now null, last_parent class is "+last_parent.getClass().getName());
    		}
            return outerFrame = ((Frame)last_parent);
        }
    }

    /**
     * shows a dialog box
     * @param dialog   the dialog box to be shown
     * @param message   the message in the dialog box
     */
    public void showDialog( SimpleDialog dialog, String message ) {
        dialog.setMessage( message );
        dialog.pack();
        dialog.show();
    }

	private void postDialog(int type) {
	    postDialog(type,null,null);
	}

	private void postDialog(int type,String message) {
	    postDialog(type,message,null);
	}

	private void postDialog(int type,String message,String extra) {
		try {
		    if (dtai.util.Debug.getLevel() == 90) {
    		    System.err.println("posting message type "+type+": "+message);
    		}
			push.writeInt(type);
			if (message != null) {
    			push.writeUTF(message);
    			if (extra != null) {
        			push.writeUTF(extra);
        		}
    		}
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

    /**
     * shows the user an error dialog box
     * @param t  the error
     * @param    the message
     * @param    the help topic
     */
    public void showError( Throwable t, String message, String helpTopic ) {
        System.err.println("Error: "+message);
        if ( t != null ) {
			t.printStackTrace();
        }
		else {
			Thread.currentThread().dumpStack();
		}

		if (threaded) {
    		postDialog(ERROR,message,helpTopic);
    	} else {
			processPosting(ERROR,message,helpTopic);
		}
    }

    private void reallyShowError( String message, String helpTopic ) {

        if ( outerFrame == null ) {
            getOuterFrame();
        }

        if ( outerFrame != null ) {
            if ( errorDialog == null ) {
                errorDialog = new ErrorDialog( outerFrame, helpTopic );
            } else {
                errorDialog.setFrame(outerFrame);
            }
            showDialog( errorDialog, message );
        }
    }

    /**
     * shows a warning message
     * @param   the warning message
     */
    public void showWarning( String message ) {
		if (threaded) {
    		postDialog(WARNING,message);
    	} else {
			processPosting(WARNING,message);
		}
	}

    private void reallyShowWarning( String message ) {

        if ( outerFrame == null ) {
            getOuterFrame();
        }

        if ( outerFrame == null ) {
            System.err.println( "Warning: "+message );
        }
        else {
            if ( warningDialog == null ) {
                warningDialog = new WarningDialog( outerFrame );
            } else {
                warningDialog.setFrame(outerFrame);
            }
            showDialog( warningDialog, message );
        }
    }

    /**
     * shows info
     * @param  the info mesage
     */
    public void showInfo( String message ) {
		if (threaded) {
    		postDialog(INFO,message);
    	} else {
			processPosting(INFO,message);
		}
	}

    private void reallyShowInfo( String message ) {

        if ( outerFrame == null ) {
            getOuterFrame();
        }
        if ( outerFrame == null ) {
            System.out.println( "Info: "+message );
        }
        else {
            if ( infoDialog == null ) {
                infoDialog = new InfoDialog( outerFrame );
            } else {
                infoDialog.setFrame(outerFrame);
            }
            showDialog( infoDialog, message );
        }
    }

    /**
     * showAbout
     * @param what  TBD
     * @param message  TBD
     */
    public void showAbout( String what, String message ) {
		if (threaded) {
    		postDialog(ABOUT,message,what);
    	} else {
			processPosting(ABOUT,message,what);
		}
	}

    private void reallyShowAbout( String what, String message ) {

        if ( outerFrame == null ) {
            getOuterFrame();
        }

        if ( outerFrame == null ) {
            System.out.println( "About "+what+": "+message );
        }
        else {
            if ( aboutDialog == null ) {
                aboutDialog = new AboutDialog( outerFrame, what );
            } else {
                aboutDialog.setFrame(outerFrame);
                aboutDialog.setTitle( what );
            }
            showDialog( aboutDialog, message );
        }
    }

	/**
	 * @param message		the String to put in the showStatus area
	 */
    public void showStatus( String message ) {
        if ( applet != null ) {
            applet.showStatus( message );
        }
        else {
            System.out.println( message );
        }
    }

    /**
     * shows the user a please wait message, followed by a wait cursor
     */
    public void showPleaseWait() {
        if ( outerFrame == null ) {
            getOuterFrame();
        }

        waiting++;
        if ( waiting == 1 ) {
            if ( outerFrame == null ) {
                System.out.print( "Please wait..." );
                System.out.flush();
            }
            else {
                outerFrame.setCursor( Frame.WAIT_CURSOR );
            }
        }
    }

    /**
     * shows the user when finished waiting
     */
    public void showFinishedWait() {
        if ( outerFrame == null ) {
            getOuterFrame();
        }

        if ( waiting > 0 ) {
            waiting--;
            if ( waiting == 0 ) {
                if ( outerFrame == null ) {
                    System.out.println( "done" );
                }
                else {
                    outerFrame.setCursor( Frame.DEFAULT_CURSOR );
                }
            }
        }
    }

    /**
     * hides error dialog box
     */
    public void doHideError() {
        if ( errorDialog != null ) {
            errorDialog.hide();
        }
    }

    /**
     * hides warning dialog box
     */
    public void doHideWarning() {
        if ( warningDialog != null ) {
            warningDialog.hide();
        }
    }

    /**
     * hides info dialog box
     */
    public void doHideInfo() {
        if ( infoDialog != null ) {
            infoDialog.hide();
        }
    }

    /**
     * hides about dialog box
     */
    public void doHideAbout() {
        if ( aboutDialog != null ) {
            aboutDialog.hide();
        }
    }
}
