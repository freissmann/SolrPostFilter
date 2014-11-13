package de.qaware.blog.solr;

import java.io.IOException;
import org.apache.lucene.index.DocValues;
import org.apache.lucene.search.IndexSearcher;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.search.DelegatingCollector;
import org.apache.solr.search.ExtendedQueryBase;
import org.apache.solr.search.PostFilter;

public class ModuloQuery extends ExtendedQueryBase implements PostFilter {

    private final int moduloX;

    public ModuloQuery(SolrParams localParams) {
        // We try to get the modulo pair
        // if there is none we will still be using 42
        moduloX = localParams.getInt("modulo", 42);
    }

    @Override
    public int getCost() {
        // We make sure that the cost is at least 100 to be a post filter
        return Math.max(super.getCost(), 100);
    }

    @Override
    public boolean getCache() {
        return false;
    }

    @Override
    public DelegatingCollector getFilterCollector(IndexSearcher idxS) {

        return new DelegatingCollector() {

            @Override
            public void collect(int docNumber) throws IOException {
                // gets the id field from the document with the docNumber
                long currentDocId = DocValues.getNumeric(context.reader(), "id")
                        .get(docNumber);

                if (currentDocId % moduloX == 0) {
                    super.collect(docNumber);
                }
            }
        };
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + this.moduloX;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ModuloQuery other = (ModuloQuery) obj;
        if (this.moduloX != other.moduloX) {
            return false;
        }
        return true;
    }
}
