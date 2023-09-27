package ru.clothingstore.service.Impl;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clothingstore.model.news.News;
import ru.clothingstore.repository.NewsRepository;
import ru.clothingstore.service.NewsService;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(NewsService.class);

    @Autowired
    public NewsServiceImpl(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }


    @Override
    public Set<News> getAllNews() {
        List<News> newsList = newsRepository.findAll();
        return new HashSet<>(newsList);
    }

    //TODO
    @Override
    public Set<News> getNews(int count) {
        List<News> newsList = newsRepository.findAll();
        return new HashSet<>(newsList);
    }

    @Override
    public News getNewsById(int id) {
        return newsRepository.findById(id).orElse(null);
    }

    @Override
    public News getNewsByDate(Date date) {
        return newsRepository.findByDate(date).orElse(null);
    }

    //TODO
    @Override
    public List<News> getLastAddedNews(int count) {
        return null;
    }

    @Override
    @Transactional
    public void addNews(News news) {
        news.setDate(new Date());
        newsRepository.save(news);
        LOGGER.info("News has added successfully ", news);
    }

    @Override
    @Transactional
    public void updateNews(News news) {
        News newsToBeUpdate = newsRepository.findById(news.getId()).orElse(null);

        if (newsToBeUpdate == null){
            LOGGER.info("News has not updated ", news);
        }

        news.setDate(newsToBeUpdate.getDate());
        // Todo При добавлении возможности обновить картинку, убрать
        news.setNewsImageLink(newsToBeUpdate.getNewsImageLink());
        newsRepository.save(news);
        LOGGER.info("News has updated successfully ", news);
    }

    @Override
    @Transactional
    public void deleteNews(int id) {
        newsRepository.deleteById(id);
        LOGGER.info("News successfully remove -", id);
    }
}
