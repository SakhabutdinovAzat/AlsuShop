package ru.clothingstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.clothingstore.model.good.Good;
import ru.clothingstore.service.GoodService;
import ru.clothingstore.service.NewsService;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    public String category(Model model,
                           @RequestParam(value = "offset", defaultValue = "0", required = false) @Min(0) Integer offset,
                           @RequestParam(value = "limit", defaultValue = "3", required = false) @Min(1) @Max(20) Integer limit,
                           @RequestParam(value = "sort", defaultValue = "title", required = false) String sort) {

        Page<Good> goodsPage = goodService.getAllGoods(true, offset, limit, sort);
        model.addAttribute("newGoods", goodService.getAllGoods(true, offset, 4, "added"));
        model.addAttribute("goodsPage", goodsPage);
        model.addAttribute("url", "index");
        model.addAttribute("listNews", newsService.getAllNews());

        int totalPages = goodsPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "index";
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }
}
