package edu.unh.cs980;

public class LuceneQuery {

	static final private String INDEX_DIRECTORY = "index";
	static final private String OUTPUT_DIR = "output";
	static IndexData indexer = new IndexData();

	public static void main(String[] args) {
		System.setProperty("file.encoding", "UTF-8");

		// 1. index dataset
		// 2. Create Lucene search engine
		// 3. Get all queries and retrieve search result
		// 4. Create run file.

		// String queryPath = args[0];
		// String dataPath = args[1];

		// Local testing
		String queryPath = "DataSet/";
		String dataPath = "DataSet/paragraphCorpus/dedup.articles-paragraphs.cbor";
		try {
			indexer.indeAllData(INDEX_DIRECTORY, dataPath);
		} catch (Throwable e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

	}

}
