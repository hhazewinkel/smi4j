/**
 * SmiBrowser.java
 * The top-level window from which all view and gui interactions are provided.
 */
package com.mod_snmp.smitools.smibrowser;

import com.mod_snmp.smiparser.errorhandler.SmiException;
import com.mod_snmp.smiparser.Parser;
import com.mod_snmp.smiparser.ParserInputArgs;
import com.mod_snmp.smitools.smibrowser.common.RootFrame;
import com.mod_snmp.smitools.smibrowser.menubar.MenuBar;
import net.lisanza.cliargs.ArgsException;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;

public class SmiBrowser {
    static public ParserInputArgs input;

    JFrame f;
    MenuBar menubar;
    /**
     * SmiBrowserPane, split pane that divides the window
     * in a tree and tabbed view.
     */
    SmiBrowserPane pane;

    public SmiBrowser(String name) {
	f = new JFrame(name);
        RootFrame.set(f);
        try {
            f.setTitle(name);
            f.setSize(new Dimension(1000, 600));
            setupLocation();
            menubar = new MenuBar();
            f.setJMenuBar(menubar);
            pane = new SmiBrowserPane();
            f.setContentPane(pane);
            f.setVisible(true);
            f.validate();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    protected void setupLocation() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = f.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        f.setLocation((screenSize.width - frameSize.width) / 2,           
                          (screenSize.height - frameSize.height) / 2);
    }
    protected void processWindowEvent(WindowEvent e) {
        processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            System.exit(0);
        }
    }
    protected void update() {
        pane.update();
        f.validate();
    }

    public static void main(String[] args) {
        Parser parser = new Parser();
        SmiBrowser rootWindow = new SmiBrowser("SMI browser");
        try {
            input = new ParserInputArgs();
            input.parse(args);
            parser.parseFiles(input.getFiles());
        } catch (ArgsException e) {
            System.out.println(e.getMessage());
        } catch (SmiException e) {
            e.print();
        }
    }
}
