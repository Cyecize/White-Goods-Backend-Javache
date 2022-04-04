package com.cyecize.app.api.product;

import java.util.List;

public interface TagService {
    List<Tag> findOrCreate(List<String> tagNames);
}
