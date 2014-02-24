package no.fosstveit.ui;

import java.io.File;

/**
*
* @author Håvar Aambø Fosstveit
*/
public class ImageFileFilter extends javax.swing.filechooser.FileFilter {
    
    @Override
    public boolean accept(File f) {
        
        if (f.isDirectory()) {
            return true;
        }
        
        String extension = getExtension(f);
        
        if ((extension.equals("jpeg")) || (extension.equals("jpg"))) {
            return true;
        } else if (extension.equals("png")) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Again, this is declared in the abstract class
     * The description of this filter
     */
    @Override
    public String getDescription() {
        return "Supported files";
    }
    
    /**
     * Method to get the extension of the file, in lowercase
     */
    private String getExtension(File f) {
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 &&  i < s.length() - 1)
            return s.substring(i+1).toLowerCase();
        return "";
    }
}