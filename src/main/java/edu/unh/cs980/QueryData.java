package edu.unh.cs980;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import edu.unh.cs.treccar_v2.Data;
import edu.unh.cs.treccar_v2.read_data.DeserializeData;

public class QueryData {
	ArrayList<String> pageQueryList;
	ArrayList<String> sectionQueryList;

	public QueryData(String queryFilePath) {
		if (pageQueryList == null || sectionQueryList == null) {
			try {
				cacheAllQueryData(queryFilePath);
			} catch (FileNotFoundException e) {
				System.out.println("Unable to find query data.");
				e.printStackTrace();
			}
		}
	}

	public ArrayList<String> getAllpageQueries() {
		return pageQueryList;
	}

	public ArrayList<String> getAllSectionQueries() {
		return sectionQueryList;
	}

	private void cacheAllQueryData(String file_path) throws FileNotFoundException {
		FileInputStream fis = new FileInputStream((new File(file_path)));

		for (Data.Page page : DeserializeData.iterableAnnotations(fis)) {
			pageQueryList.add(page.getPageName());

			for (List<Data.Section> sectionPath : page.flatSectionPaths()) {
				String queryStr = page.getPageName();
				for (Data.Section section : sectionPath) {
					queryStr += " ";
					queryStr += section.getHeading();
				}
				sectionQueryList.add(queryStr);
			}

		}

	}

}
