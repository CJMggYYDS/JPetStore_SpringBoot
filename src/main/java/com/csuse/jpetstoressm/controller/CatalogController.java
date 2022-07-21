package com.csuse.jpetstoressm.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.csuse.jpetstoressm.domain.Cart;
import com.csuse.jpetstoressm.domain.Category;
import com.csuse.jpetstoressm.domain.Item;
import com.csuse.jpetstoressm.domain.Product;
import com.csuse.jpetstoressm.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Controller
@RequestMapping("/catalog")
public class CatalogController {

    @Autowired
    private CatalogService catalogService;


    @GetMapping("/main")
    public String ViewMain() {
        return "catalog/main";
    }

    @GetMapping("/Category")
    public String ViewCategory(String categoryId, Model model) {
        if(categoryId != null) {
            Category category = catalogService.getCategory(categoryId);
            List<Product> productList = catalogService.getProductListByCategory(categoryId);
            model.addAttribute("category", category);
            model.addAttribute("productList", productList);
            return "catalog/category";
        }
        return null;
    }

    @GetMapping("/Product")
    public String ViewProduct(@RequestParam("productId") String productId, HttpSession session, Model model) {
        Product product = catalogService.getProduct(productId);
        List<Item> itemList = catalogService.getItemListByProduct(productId);

        session.setAttribute("product", product);
        model.addAttribute("itemList", itemList);
        model.addAttribute("product", product);
        return "catalog/product";
    }

    @GetMapping("/Item")
    public String ViewItem(@RequestParam("itemId") String itemId, Model model,HttpSession session) {
        Item item = catalogService.getItem(itemId);
        item.setProduct((Product) session.getAttribute("product"));
        model.addAttribute("item", item);
        return "catalog/item";
    }

    @PostMapping("/search")
    public String SearchProducts(@RequestParam("keyword") String keyword, Model model) {
        if(keyword.trim().equals("")){
            return "catalog/main";
        }

        List<Product> productList = catalogService.searchProductList(keyword);
        model.addAttribute("productList", productList);
        return "catalog/searchProducts";
    }

    @GetMapping("/AutoFindProductAjax")
    @ResponseBody
    public void AutoFindProductAjax(@RequestParam("keyword") String keyword, HttpServletResponse response) {
        List<Product> productList = catalogService.searchProductList(keyword);
        response.setContentType("application/json;charset=utf-8");
        response.setCharacterEncoding("utf-8");

        JSONArray searchArray=new JSONArray();
        for (Product product : productList) {
            JSONObject searchObj = new JSONObject();
            searchObj.put("name", product.getName());
            searchArray.add(searchObj);
        }

        String searchStr=searchArray.toJSONString();

        System.out.println(searchStr);
        PrintWriter out= null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.print(searchStr);

        out.flush();
        out.close();
    }

    @GetMapping("/categoryShowJs")
    @ResponseBody
    public void ShowInfo(@RequestParam("categoryId") String categoryId, HttpServletResponse resp) {
        List<Product> productList = catalogService.getProductListByCategory(categoryId);
        String response = "ProductID            Name\n";

        for(int i = 0;i<productList.size();i++)
        {
            Product product = productList.get(i);
            response += product.getProductId()+"        "+product.getName()+"\n";

        }
        System.out.println(response);
        resp.setContentType("text/xml");
        PrintWriter out = null;
        try {
            out = resp.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.write(response);

        out.close();
    }

}
