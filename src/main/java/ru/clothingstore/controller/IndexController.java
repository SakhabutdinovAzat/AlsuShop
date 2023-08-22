package ru.clothingstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.clothingstore.service.GoodService;
import ru.clothingstore.service.NewsService;

@Controller
@RequestMapping("/")
public class IndexController {
    private final GoodService goodService;
    private final NewsService newsService;

    @Autowired
    public IndexController(GoodService goodService, NewsService newsService) {
        this.goodService = goodService;
        this.newsService = newsService;
    }

    @GetMapping(value = {"/", "/index"})
    public String indexPage(Model model) {
        model.addAttribute("newGoods", goodService.getGoods(4, true));
        model.addAttribute("catalogGoods", goodService.getAllGoods());
        model.addAttribute("listNews", newsService.getAllNews());

        return "index";
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }
}
