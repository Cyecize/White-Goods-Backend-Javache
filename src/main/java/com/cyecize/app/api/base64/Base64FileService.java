package com.cyecize.app.api.base64;

public interface Base64FileService {

    String saveFile(Base64FileBindingModel file);

    void removeFile(String path);
}
