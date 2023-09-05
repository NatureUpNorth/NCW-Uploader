import javax.imageio.ImageIO;
import java.io.*;
import java.util.Set;
import java.util.TreeSet;

public class FileProcessor {
    private File filepath;
    private String path;
    private Set<String> allFiles;
    private FileSelectPanel parent;
    private int localCount;

    public FileProcessor(FileSelectPanel parent) {
        setFilepath(null);
        allFiles = new TreeSet<String>();
        Uploader.getInstance().getResponses().replaceKeyValue(parent.getKey(), allFiles);
        localCount = 0;
        this.parent = parent;
        Uploader.getInstance().getResponses().addKey("busy", false);
    }

    private void setBusy(boolean busy) {
        Uploader.getInstance().getResponses().replaceKeyValue("busy", busy);
    }

    public void setFilepath(String path) {
        this.path = path;
        if (path == null || path.length() == 0) {
            filepath = null;
        } else {
            filepath = new File(path);
        }
    }
    public boolean allFiles() {
        if (filepath == null) {
            return false;
        }
        setBusy(true);
        localCount = 0;
        filepath = new File(path);
        allFiles = new TreeSet<>();
        parent.setFileCount(0);
        allFiles.addAll(allFilesRecursive(filepath));

        Uploader.getInstance().getResponses().replaceKeyValue(parent.getKey(), allFiles);

        return true;
    }

    private Set<String> allFilesRecursive(File path) {
        Set<String> allFiles = new TreeSet<String>();
        File[] currentFiles = path.listFiles();
        if (currentFiles != null) {
            for (File file : currentFiles) {
                if (file.isFile() && isImage(file.getAbsolutePath())) {
                    allFiles.add(file.getAbsolutePath());
                    localCount++;
                    parent.setFileCount(localCount);
                }
                if (file.isDirectory()) {
                    Set<String> subFiles = allFilesRecursive(file);
                    allFiles.addAll(subFiles);
                }
            }
        }
        return allFiles;
    }

    private boolean isImage(String pathToFile) {
        try {
            InputStream fileInputStream = new FileInputStream(pathToFile);
            return ImageIO.read(fileInputStream) != null;
        } catch (IOException e) {
            return false;
    }

    }
    public void start() {
        (new Thread(new Runnable() {
            @Override
            public void run() {
                allFiles();
                parent.updatePreviewImages();
                parent.setFileCount(allFiles.size());
                parent.setProgresVisible(false);
                parent.setBrowseButtonEnable(true);
                setBusy(false);
            }
        })).start();
    }

    public Set<String> getFiles() {
        return allFiles;
    }
}
