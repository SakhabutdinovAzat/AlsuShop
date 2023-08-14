package ru.clothingstore.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.clothingstore.model.good.Good;
import ru.clothingstore.service.CategoryService;
import ru.clothingstore.service.GoodService;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Controller(value = "adminGoodController")
@RequestMapping("/admin/good")
public class GoodController {
    private final GoodService goodService;
    private final CategoryService categoryService;

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    public GoodController(GoodService goodService, CategoryService categoryService) {
        this.goodService = goodService;
        this.categoryService = categoryService;
    }

    @GetMapping("/index")
    public String index(Model model) {
        List<Good> goods = new ArrayList<>(goodService.getAllGoods());
        goods.sort(Comparator.comparing(Good::getId));
        model.addAttribute("goods", goods);

        return "admin/good/index";
    }

    @GetMapping("/add")
    public String add(Model model) {
        getGoodModel(model, new Good());
        return "admin/good/add";
    }

    @PostMapping("/add")
    public String add(@RequestParam("file") MultipartFile file, @ModelAttribute("good") @Valid Good good,
                      BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes
    ) throws IOException {

        if (bindingResult.hasErrors()){
            getGoodModel(model, good);
            return "admin/good/add";}

        if (file != null && !file.isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFileName));

            good.setSmallImageLink(resultFileName);
        }
;
        goodService.addGood(good);
        redirectAttributes.addFlashAttribute("success", "Good has added: " + good);
        return "redirect:/admin/good/index";
    }

    @GetMapping(value = {"/update/{id}", "/update"})
    public String update(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttrs) {

        if (id == 0) {
            redirectAttrs.addFlashAttribute("error", "Not the selected good");
            return "redirect:/admin/good/index";
        }
        Good good = goodService.getGoodById(id);
        getGoodModel(model, good);
        return "admin/good/update";
    }

    @PostMapping("/update")
    public String update(@RequestParam("file") MultipartFile file, @ModelAttribute("good") @Valid Good good, BindingResult bindingResult, Model model, RedirectAttributes redirectAttrs) throws IOException {

        if (bindingResult.hasErrors()) {
            getGoodModel(model, good);
            return "admin/good/update";
        }

        if (file != null && !file.isEmpty()) {
            System.out.println("hello" + file.getOriginalFilename());
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFileName));

            good.setSmallImageLink(resultFileName);
        }
        goodService.updateGood(good);
        redirectAttrs.addFlashAttribute("success", "Good has updated: " + good);
        return "redirect:/admin/good/index";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id, RedirectAttributes redirectAttrs) {
        Good good = goodService.getGoodById(id);
        goodService.deleteGood(id);
        redirectAttrs.addFlashAttribute("success", "Good has deleted: " + good);
        return "redirect:/admin/good/index";
    }

    private Model getGoodModel(Model model, Good good) {
        model.addAttribute("currentCategory", good.getCategory());
        model.addAttribute("allCategory", categoryService.getAllCategory());
        model.addAttribute("good", good);
        return model;
    }
}
