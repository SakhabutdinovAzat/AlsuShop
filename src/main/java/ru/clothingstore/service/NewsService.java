package ru.clothingstore.service;

import ru.clothingstore.model.news.News;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface NewsService {

    Set<News> getAllNews();

    Set<News> getNews(int count);

    News getNewsById(int id);

    News getNewsByDate(Date date);

    List<News> getLastAddedNews(int count);

    void addNews(News news);

    void updateNews(News news);

    void deleteNews(int id);
}
