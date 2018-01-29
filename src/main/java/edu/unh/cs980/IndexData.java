package edu.unh.cs980;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import co.nstant.in.cbor.CborException;
import edu.unh.cs.treccar_v2.Data;
import edu.unh.cs.treccar_v2.read_data.DeserializeData;

public class IndexData {
	private IndexSearcher is = null;
	private IndexWriter indexWriter = null;

	public void indeAllData(String INDEX_DIRECTORY, String file_path) throws CborException, IOException {
		Directory indexdir = FSDirectory.open((new File(INDEX_DIRECTORY)).toPath());
		IndexWriterConfig conf = new IndexWriterConfig(new StandardAnalyzer());
		conf.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
		IndexWriter iw = new IndexWriter(indexdir, conf);
		// for (Data.Page p : DeserializeData.iterAnnotations(new
		// FileInputStream(new File(file_path)))) {
		// // this.indexPara(iw, p);
		// }

		final Iterator<Data.Page> pageIterator = DeserializeData
				.iterAnnotations(new FileInputStream(new File(file_path)));

		iw.close();
	}

	private Document convertToLuceneDoc(Data.Page page) {
		Document doc = new Document();

		return null;
	}
}
