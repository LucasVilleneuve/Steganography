import java.awt.*;

public class FilePicker
{
    /**
     * Opens a GUI window letting the user pick a file.
     * @return Full path of the chosen file, null if the user clicked cancel
     */
    public String pickFile()
    {
        FileDialog chooser = new FileDialog((Frame)null, "Select file to open", FileDialog.LOAD);
        chooser.setFile("*.jpg;*.png");
        chooser.setVisible(true);

        String fileName = chooser.getFile();
        if (fileName == null)
            return null;

        String filePath = chooser.getDirectory() + fileName;

        chooser.dispose();

        return filePath;
    }
}
