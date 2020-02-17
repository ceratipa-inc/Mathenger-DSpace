package com.example.mathengerapi.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class MathService {
    @Value("${path.bash}")
    private String bashPath = "bash";
    @Value("${path.latex}")
    private String latexPath = "pdflatex";
    @Value("${path.imageMagick}")
    private String imageMagickPath = "magick";
    @Value("${app.storageDir}")
    private String storageDir;

    public File texToPng(String tex) throws IOException, InterruptedException {
        var storage = new File(storageDir);
        createDirIfNotExists(storage);
        var localDir = new File(storage, "formula-".concat(UUID.randomUUID().toString()));
        createDirIfNotExists(localDir);
        var texFile = new File(localDir, "formula.tex");
        createFileIfNotExists(texFile);
        var writer = new FileWriter(texFile);
        writer.write(tex);
        writer.flush();
        writer.close();
        var filename = texFile.getName().replace(".tex", "");
        boolean isWindows = System.getProperty("os.name")
                .toLowerCase().startsWith("windows");
        var process = isWindows ? Runtime.getRuntime()
                .exec(String.format("%s -c \"cd %s && %s %s.tex && %s -density 500 %s.pdf -quality 90 %s.png\"",
                        bashPath, localDir.getAbsolutePath().replaceAll("\\\\", "/"),
                        latexPath, filename, imageMagickPath, filename, filename))
                : Runtime.getRuntime()
                .exec(new String[]{
                        bashPath, "-c",
                        String.format("cd %s; %s %s.tex; %s -density 500 %s.pdf -quality 90 %s.png",
                                localDir.getAbsolutePath(), latexPath, filename, imageMagickPath,
                                filename, filename)
                });
        if (!process.waitFor(5, TimeUnit.SECONDS)) {
            process.getInputStream().close();
            process.getErrorStream().close();
            process.descendants().forEach(ProcessHandle::destroy);
            process.children().forEach(ProcessHandle::destroy);
            process.destroy();
            if (process.isAlive()) {
                process.destroyForcibly();
            }
            FileSystemUtils.deleteRecursively(localDir);
            throw new RuntimeException("failed to create an image!");
        }
        return new File(localDir, filename.concat(".png"));
    }

    private void createFileIfNotExists(File texFile) throws IOException {
        if (!texFile.exists() && !texFile.createNewFile()) {
            throw new FileNotFoundException("Can not create file!");
        }
    }

    private void createDirIfNotExists(File dir) throws FileNotFoundException {
        if (!dir.exists() && !dir.mkdir()) {
            throw new FileNotFoundException("Can not create directory!");
        }
    }
}
