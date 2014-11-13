package de.qaware.blog.solr;

import java.io.IOException;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.solr.search.DelegatingCollector;
import org.apache.solr.search.ExtendedQueryBase;
import org.apache.solr.search.PostFilter;

public class ModuloQuery extends ExtendedQueryBase implements PostFilter {

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
                // To be able to get documents, we need the reader
                AtomicReader reader = context.reader();

                // From the reader we get the current document by the docNumber
                Document currentDoc = reader.document(docNumber);

                // We get the id field from our document
                Number currentDocId = currentDoc.getField("id").numericValue();

                // Filter magic
                if (currentDocId.intValue() % 42 == 0) {
                    super.collect(docNumber);
                }
            }
        };
    }
}
