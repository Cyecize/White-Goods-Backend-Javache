package com.cyecize.app.api.product;

import java.util.Collection;
import java.util.Set;

public interface TagService {

    Set<Tag> findOrCreate(Collection<String> tagNames);
}
