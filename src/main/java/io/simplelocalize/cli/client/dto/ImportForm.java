package io.simplelocalize.cli.client.dto;

import io.micronaut.core.annotation.Introspected;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Set;

@Introspected
public final class ImportForm
{
    private final Set<ImportKey> content;

    public ImportForm(Set<ImportKey> content)
    {
        this.content = content;
    }

    @Override
    public boolean equals(Object o)
    {
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

}
