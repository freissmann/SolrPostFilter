package de.qaware.blog.solr;

import org.apache.lucene.search.Query;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.QParser;
import org.apache.solr.search.QParserPlugin;
import org.apache.solr.search.SyntaxError;

public class ModuloQueryParserPlugin extends QParserPlugin {

    @Override
    public QParser createParser(String qstr, SolrParams localParams, SolrParams params, SolrQueryRequest req) {

        return new QParser(qstr, localParams, params, req) {

            @Override
            public Query parse() throws SyntaxError {
                // The ModuloQuery knows the function parameters
                return new ModuloQuery(localParams);
            }
        };
    }

    @Override
    public void init(NamedList args) {
        // Emtpy on purpose
    }
}
