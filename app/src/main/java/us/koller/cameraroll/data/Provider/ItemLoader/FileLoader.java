package us.koller.cameraroll.data.Provider.ItemLoader;

import android.app.Activity;
import android.os.Environment;

import java.io.File;

import us.koller.cameraroll.data.File_POJO;
import us.koller.cameraroll.util.MediaType;

public class FileLoader extends ItemLoader {

    private static File_POJO allFiles;

    private File_POJO dir_pojo;

    FileLoader() {
        if (allFiles == null) {
            allFiles = new File_POJO(Environment.getExternalStorageDirectory().getPath(), false);
        }
    }

    @Override
    public void onNewDir(Activity context, File dir) {
        dir_pojo = new File_POJO(dir.getPath(),
                MediaType.isMedia(dir.getPath()));
    }

    @Override
    public void onFile(Activity context, File file) {
        File_POJO file_pojo = new File_POJO(file.getPath(),
                MediaType.isMedia(file.getPath()));
        dir_pojo.addChild(file_pojo);
    }

    @Override
    public void onDirDone(Activity context) {
        addFiles(allFiles, dir_pojo);
    }

    @Override
    public Result getResult() {
        Result result = new Result();
        result.files = dir_pojo;
        return result;
    }

    private static void addFiles(File_POJO files, File_POJO filesToAdd) {
        if (files.getPath().equals(filesToAdd.getPath())) {
            files.getChildren().addAll(filesToAdd.getChildren());
        } else if (files.getPath().equals(filesToAdd.getPath()
                .replace("/" + filesToAdd.getName(), ""))) {
            files.addChild(filesToAdd);
        } else {
            File_POJO currentFiles = files;

            String[] filesToAddPath = filesToAdd.getPath().split("/");
            for (int i = 0; i < filesToAddPath.length; i++) {
                boolean found = false;
                for (int k = 0; k < currentFiles.getChildren().size(); k++) {
                    if (filesToAddPath[i].equals(
                            currentFiles.getChildren().get(k).getName())) {
                        found = true;
                        currentFiles = currentFiles.getChildren().get(k);
                    }
                }

                if (found) {
                    currentFiles.addChild(filesToAdd);
                    break;
                }
            }
        }

    }
}
