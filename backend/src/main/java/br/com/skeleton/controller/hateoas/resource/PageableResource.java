package br.com.skeleton.controller.hateoas.resource;

import java.io.Serializable;
import java.util.List;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
public final class PageableResource implements Serializable {

    private final List records;
    private final MetadataPageable _metadata;

    public PageableResource(final List records, final MetadataPageable _metadata) {
        this.records = records;
        this._metadata = _metadata;
    }

    public List getRecords() {
        return records;
    }

    public MetadataPageable get_metadata() {
        return _metadata;
    }
}
