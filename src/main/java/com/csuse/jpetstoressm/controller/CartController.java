package com.csuse.jpetstoressm.controller;

import com.csuse.jpetstoressm.domain.Account;
import com.csuse.jpetstoressm.domain.Cart;
import com.csuse.jpetstoressm.domain.CartItem;
import com.csuse.jpetstoressm.domain.Item;
import com.csuse.jpetstoressm.service.CatalogService;
import com.csuse.jpetstoressm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    private CartItem cartItem;

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/viewCart")
    public String viewCart(HttpSession session)
    {
        Cart cart = (Cart) session.getAttribute("cart");
        Account account = (Account) session.getAttribute("account");
        if(account==null){
            return "/account/SignOnForm";
        }
        if(cart==null){
            cart = new Cart();
            session.setAttribute("cart",cart);
        }
        return "/cart/cart";
    }

    @GetMapping("/addItemToCart")
    public String addItemToCart(@RequestParam("workingItemId")String workingItemId, HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        Account account = (Account) session.getAttribute("account");
        if(account==null)
            return "/account/SignOnForm";
        if(cart==null){
            cart = new Cart();
        }
        if(cart.containsItemId(workingItemId)) {
            cart.incrementQuantityByItemId(workingItemId);
            orderService.updateItemQuantity(account, cart.getCarItem(workingItemId));
        }
        else {
            boolean isInStock = catalogService.isItemInStock(workingItemId);
            Item item = catalogService.getItem(workingItemId);
            cart.addItem(item, isInStock);
            orderService.insertIntoCartShop(account, cart.getCarItem(workingItemId));
            List<CartItem> cartItems = orderService.getCartShopItems(account);
            cart.itemClear();
            cart.addItem(cartItems);
            session.setAttribute("cart",cart);
        }
        return "cart/cart";
    }

    @GetMapping("/removeItemFromCart")
    public String removeItemFormCart(@RequestParam("workingItemId")String workingItemId, HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        Account account = (Account) session.getAttribute("account");
        CartItem item = cart.removeItemById(workingItemId);
        if(item==null) {
            session.setAttribute("message0","Attempted to remove null CartItem from Cart.");
            return "/common/error";
        }
        else {
            orderService.deleteItemFromCartShop(account, item);
            List<CartItem> cartItems = orderService.getCartShopItems(account);
            cart.itemClear();
            cart.addItem(cartItems);
            session.setAttribute("cart",cart);
            return "cart/cart";
        }
    }

    @GetMapping("/updateQuantity")
    @ResponseBody
    public void updateQuantity(@RequestParam("quantity")int quantity, @RequestParam("itemId")String itemId, @RequestParam("flag")String flag, HttpServletResponse resp, HttpSession session) throws IOException {
        resp.setContentType("text/xml");
        PrintWriter out = resp.getWriter();
        itemId = itemId.trim();

        Cart cart = (Cart) session.getAttribute("cart");
        Account account = (Account) session.getAttribute("account");
        Iterator<CartItem> cartItemIterator = cart.getAllCartItems();

        while(cartItemIterator.hasNext()) {
            cartItem = cartItemIterator.next();
            String Id = cartItem.getItem().getItemId();
            try {
                if(itemId.equals(Id))
                {
//                    System.out.println(0000);
                    if(flag.equals("sub")) {
                        quantity--;
                    }
                    if(flag.equals("add"))
                        quantity++;
                    if(quantity<1)
                    {
                        cartItemIterator.remove();
                        if(account!=null)
                            orderService.deleteItemFromCartShop(account,cartItem);

                        out.write("d"+"/"+cart.getSubTotal());
                        return;
                    }
                    else {
                        cartItem.setQuantity(quantity);
                        if(account!=null)
                            orderService.updateItemQuantity(account,cartItem);
                        System.out.println(cartItem.getItem().getItemId()+"  "+cartItem.getQuantity());
                    }
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        session.setAttribute("cart", cart);

        Cart cart2 = (Cart)session.getAttribute("cart");
        String quantityAll = "";
        quantityAll += quantity + "/" + cartItem.getTotal() + "/" + cart2.getSubTotal();
        System.out.println(quantityAll+"quantityAll");

        out.write(quantityAll);

    }
}
