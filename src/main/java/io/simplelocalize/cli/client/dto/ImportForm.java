package io.simplelocalize.cli.client.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Set;

public class ImportForm
{
    private Set<ImportKey> content;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ImportForm that = (ImportForm) o;

        return new EqualsBuilder()
                .append(content, that.content)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(content)
                .toHashCode();
    }

    public Set<ImportKey> getContent() {
        return content;
    }

    public ImportForm setContent(Set<ImportKey> content) {
        this.content = content;
        return this;
    }
}
