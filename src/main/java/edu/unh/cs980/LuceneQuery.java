package edu.unh.cs980;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.FSDirectory;

public class LuceneQuery {

	static final private String INDEX_DIRECTORY = "index";
	static final private String OUTPUT_DIR = "output";
	static final private int Max_Results = 100;

	static IndexData indexer = new IndexData();

	public static void main(String[] args) {
		System.setProperty("file.encoding", "UTF-8");

		// 1. index dataset
		// 2. Create Lucene search engine
		// 3. Get all queries and retrieve search result
		// 4. Create run file.

		String queryPath = args[0];
		// String dataPath = args[1];

		// Local testing args
		// String queryPath = "DataSet/benchmarkY1-train/train.pages.cbor";
		String dataPath = "DataSet/paragraphCorpus/dedup.articles-paragraphs.cbor";
		try {

			// 1. index DataSet
			// indexer.indexAllData(INDEX_DIRECTORY, dataPath);

			// 2. Cache all queries strings.
			QueryData queryData = new QueryData(queryPath);
			ArrayList<String> pageList = queryData.getAllpageQueries();
			ArrayList<String> sectionList = queryData.getAllSectionQueries();
			System.out.println("Got " + pageList.size() + " pages and " + sectionList.size() + " sections.");

			// 3. Lucene Search

			// Create run file for pages.
			System.out.println("Search Results for " + pageList.size() + " pages...");
			ArrayList<String> pageResults = getSearchResult(pageList, Max_Results);
			String pageRunFileName = "pages-bm25.run";
			System.out.println("Got " + pageResults.size() + " results for pages. Write results to " + OUTPUT_DIR + "/"
					+ pageRunFileName);
			writeToFile(pageRunFileName, pageResults);
			System.out.println("Done for pages.");

			// Create run file for sections.
			System.out.println("Search Results for " + sectionList.size() + " sections...");
			ArrayList<String> sectionResults = getSearchResult(sectionList, Max_Results);
			String sectionRunFileName = "section-bm25.run";
			System.out.println("Got " + sectionResults.size() + " results for sections. Write results to " + OUTPUT_DIR
					+ "/" + sectionRunFileName);
			writeToFile(sectionRunFileName, sectionResults);
			System.out.println("Done for Sections.");

			System.out.println("All Done!");

		} catch (Throwable e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

	}

	private static ArrayList<String> getSearchResult(ArrayList<String> queriesStr, int max_result)
			throws IOException, ParseException {
		ArrayList<String> runFileStr = new ArrayList<String>();

		IndexSearcher searcher = new IndexSearcher(
				DirectoryReader.open(FSDirectory.open((new File(INDEX_DIRECTORY).toPath()))));
		searcher.setSimilarity(new BM25Similarity());

		QueryParser parser = new QueryParser("content", new StandardAnalyzer());

		for (String queryStr : queriesStr) {
			Query q = parser.parse(QueryParser.escape(queryStr));
			// Query q = parser.parse(queryStr);

			TopDocs tops = searcher.search(q, max_result);
			ScoreDoc[] scoreDoc = tops.scoreDocs;
			for (int i = 0; i < scoreDoc.length; i++) {
				ScoreDoc score = scoreDoc[i];
				Document doc = searcher.doc(score.doc);
				String paraId = doc.getField("paraid").stringValue();
				float rankScore = score.score;
				int rank = i + 1;

				String runStr = queryStr + " Q0 " + paraId + " " + rank + " " + rankScore + " BM25";
				runFileStr.add(runStr);
			}
		}

		return runFileStr;
	}

	private static void writeToFile(String filename, ArrayList<String> runfileStrings) {
		String fullpath = OUTPUT_DIR + "/" + filename;
		try (FileWriter runfile = new FileWriter(new File(fullpath))) {
			for (String line : runfileStrings) {
				runfile.write(line + "\n");
			}

			runfile.close();
		} catch (IOException e) {
			System.out.println("Could not open " + fullpath);
		}
	}

}
