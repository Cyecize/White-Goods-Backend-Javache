package com.cyecize.app.api.base64;

import com.cyecize.app.error.ApiException;
import com.cyecize.solet.SoletConfig;
import com.cyecize.solet.SoletConstants;
import com.cyecize.summer.common.annotations.Configuration;
import com.cyecize.summer.common.annotations.Service;
import com.cyecize.summer.utils.PathUtils;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class Base64FileServiceImpl implements Base64FileService {
    private final SoletConfig soletConfig;

    @Configuration("uploaded.files.location")
    private final String uploadedFilesLocation;

    @Override
    public String saveFile(Base64FileBindingModel file) {
        final String fileName = UUID.randomUUID() + file.getFileName();
        final String filePath = PathUtils.appendPath(this.uploadedFilesLocation, fileName);

        final File physicalFile = new File(PathUtils.appendPath(this.getAssetsDir(), filePath));

        try {
            physicalFile.getParentFile().mkdirs();
            physicalFile.createNewFile();
            try (OutputStream stream = new FileOutputStream(physicalFile)) {
                String base64 = file.getBase64();
                if (base64.contains(",")) {
                    base64 = base64.split(",")[1];
                }

                stream.write(Base64.getMimeDecoder().decode(base64));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new ApiException(String.format("Could not save file %s", file.getFileName()));
        }

        return filePath.replaceAll("\\\\", "/");
    }

    @Override
    public void removeFile(String path) {
        final File physicalFile = new File(PathUtils.appendPath(this.getAssetsDir(), path));
        if (physicalFile.exists()) {
            physicalFile.delete();
        }
    }

    private String getAssetsDir() {
        return this.soletConfig.getAttribute(SoletConstants.SOLET_CONFIG_ASSETS_DIR).toString();
    }
}
