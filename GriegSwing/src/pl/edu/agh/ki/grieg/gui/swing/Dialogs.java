package pl.edu.agh.ki.grieg.gui.swing;

import java.awt.Component;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JOptionPane;

/**
 * Helper class for displaying dialog windows.
 * 
 * @author los
 */
public final class Dialogs {

    private Dialogs() {
        // non-instantiable
    }

    /**
     * Displays an error message constructed from the exception {@code e} in the
     * dialog window being child of specified component.
     * 
     * @param parent
     *            Component in which to display the dialog
     * @param e
     *            Exception to present
     */
    public static void showError(Component parent, Throwable e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        writer.println("Application encountered an error.\n");
        writer.println("Reason:");
        writer.println(e);
        JOptionPane.showMessageDialog(parent, stringWriter, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Displays an error message constructed from the exception {@code e} in the
     * dialog window.
     * 
     * @param e
     *            Exception to present
     */
    public static void showError(Throwable e) {
        showError(null, e);
    }

}
