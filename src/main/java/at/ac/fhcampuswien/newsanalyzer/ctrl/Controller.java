package at.ac.fhcampuswien.newsanalyzer.ctrl;

import at.ac.fhcampuswien.newsapi.NewsApi;
import at.ac.fhcampuswien.newsapi.NewsApiBuilder;
import at.ac.fhcampuswien.newsapi.beans.Article;
import at.ac.fhcampuswien.newsapi.beans.NewsResponse;
import at.ac.fhcampuswien.newsapi.enums.Category;
import at.ac.fhcampuswien.newsapi.enums.Country;
import at.ac.fhcampuswien.newsapi.enums.Endpoint;

import java.io.*;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Controller {

	public static final String APIKEY = "da7b634eb3504b9abbf3e2395b286c83";  //TODO add your api key

	public void process(String query, Category category) {//musste die übergeben  had to import Category from enum
		System.out.println("Start process");

		//TODO implement Error handling


		//TODO load the news based on the parameters

		//TODO implement methods for analysis

		//Schnittstelle anpassen   3a
		NewsApi newsApi;//start

		if (category == null){
			newsApi = new NewsApiBuilder()
					.setApiKey(APIKEY)
					.setQ(query)
					.setEndPoint(Endpoint.TOP_HEADLINES)
					.createNewsApi();
		}
		else{
			newsApi = new NewsApiBuilder()
					.setApiKey(APIKEY)
					.setQ(query)
					.setEndPoint(Endpoint.TOP_HEADLINES).setSourceCountry(Country.at)
					.setSourceCategory(category)
					.createNewsApi();
		}
		NewsResponse newsResponse = null;

		try{
			newsResponse = newsApi.getNews();
		}
		catch (Exception e){
			System.err.println("Error: "+e.getMessage());
		}
		if (newsResponse != null){
			List<Article> articles = newsResponse.getArticles(); //needed to import class List
			System.out.println("Amount of articles: "+articles.size());
			if (articles.isEmpty()){
				System.out.println("No articles to analyze.");
				return;
			}
			//5a											//had to import stream Collectors
			String provider = articles
					.stream()
					.collect(Collectors.groupingBy(article -> article.getSource().getName(),
					Collectors.counting()))
					.entrySet()
					.stream()
					.max(Comparator.comparingInt(t-> t.getValue().intValue()))
					.get()
					.getKey();

			if (provider != null)
				System.out.println("Provider with max articles: "+provider);

			//5b
			String author = articles
					.stream()
					.filter(article -> Objects.nonNull(article.getAuthor()))
					.min(Comparator.comparingInt(article -> article.getAuthor().length()))
					.get()
					.getAuthor();

			if (author != null)
				System.out.println("Author with shortest name: "+author);

			//5d
			List<Article> sortedArticles = articles
					.stream()
					.sorted(Comparator.comparingInt(article -> article.getTitle().length()))
					.sorted(Comparator.comparing(Article::getTitle))
					.collect(Collectors.toList());

			System.out.println("First article of sorted List: "+sortedArticles.get(0));

		}

		System.out.println("End process");

	}


	public Object getData() {

		return null;
	}

}


/*for (Article article : articles){
		try{
		URL url = new URL(article.getUrl()); //had to import URL class
		InputStream inputStream = url.openStream();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(article.getTitle().substring(0,10)+ "html"));
		String line = bufferedReader.readLine();
		while (line != null){
		bufferedWriter.write(line);
		}
		bufferedReader.close();
		bufferedWriter.close();
		}
		catch (Exception e){
		System.err.println("Failed to save webpage: "+e.getMessage());
		}*/
