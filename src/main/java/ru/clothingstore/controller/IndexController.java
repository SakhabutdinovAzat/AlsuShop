package ru.clothingstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.clothingstore.service.GoodService;
import ru.clothingstore.service.NewsService;

@Controller
@RequestMapping("/index")
public class IndexController {
    private final GoodService goodService;
    private final NewsService newsService;

    @Autowired
    public IndexController(GoodService goodService, NewsService newsService) {
        this.goodService = goodService;
        this.newsService = newsService;
    }

    @GetMapping()
    public String indexPage(Model model) {
        model.addAttribute("newGoods", goodService.getLastAddedGoods(4, true));
        model.addAttribute("catalogGoods", goodService.getGoods(8, true));
        model.addAttribute("listNews", newsService.getLastAddedNews(3));

        return "index";
    }
}
