package com.cyecize.app.api.product;

import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.app.util.SpecificationExecutor;
import com.cyecize.summer.common.annotations.Service;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final SpecificationExecutor specificationExecutor;

    @Override
    @Transactional
    public Set<Tag> findOrCreate(Collection<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) {
            return Set.of();
        }

        tagNames = tagNames.stream().map(String::toUpperCase).collect(Collectors.toSet());

        final Collection<Tag> existingTags = this.specificationExecutor.findAll(
                TagSpecifications.tagNamesContains(tagNames),
                Tag.class,
                null
        );

        tagNames.removeAll(existingTags.stream().map(Tag::getName).collect(Collectors.toSet()));

        for (String tagName : tagNames) {
            final Tag tag = new Tag();
            tag.setName(tagName);
            existingTags.add(this.tagRepository.persist(tag));
        }

        return new HashSet<>(existingTags);
    }
}
