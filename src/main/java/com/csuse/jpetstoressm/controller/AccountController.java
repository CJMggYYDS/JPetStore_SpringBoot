package com.csuse.jpetstoressm.controller;

import com.csuse.jpetstoressm.domain.*;
import com.csuse.jpetstoressm.service.AccountService;
import com.csuse.jpetstoressm.service.CatalogService;
import com.csuse.jpetstoressm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import org.springframework.util.DigestUtils;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CatalogService catalogService;

    //跳往登录界面
    @GetMapping("/login")
    public String ViewLoginForm() {
        return "account/SignOnForm";
    }

    //登录
    @PostMapping("/SignOn")
    public String SignOn(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("checkcode") String checkcode, HttpSession session, Model model) {
        String message = null;
        String errorcode=null;
        String code = (String) session.getAttribute("code");
        session.removeAttribute("code");

        if(checkcode.equals(code)) {
            password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
            Account account = accountService.getAccount(username, password);
            if(account!=null&&account.getStatus().equals("1")){
                session.setAttribute("account", account);
                model.addAttribute("account", account);
                Cart cart = (Cart)session.getAttribute("cart");
                if(cart==null){
                    cart = new Cart();
                    List<CartItem> cartItems = orderService.getCartShopItems(account);
//                    for (int i=0;i<cartItems.size();i++)
//                    System.out.println(cartItems.get(i).getItem().getProductId());
                    cart.addItem(cartItems);
                    session.setAttribute("cart", cart);
                    model.addAttribute("cart", cart);
                }
                else {
                    Iterator<CartItem> cartItemIterator = cart.getAllCartItems();
                    while(cartItemIterator.hasNext()){
                        CartItem cartItem = cartItemIterator.next();
                        String itemId = cartItem.getItem().getItemId();
                        orderService.insertIntoCartShop(account, cart.getCarItem(itemId));
                    }
                }
                return "/catalog/main";
            }
            else {

                if(username == null&&password!=null)
                {
                    message= "username is empty!";
                }
                if(password == null&&username!=null)
                {
                    message = "password is empty!";
                }
                else {
                    message = "username or password is wrong!Please try again!";
                }
                if (username !=null&&password!=null&&account.getStatus().equals("0"))
                    message = "该账户由于违规被禁用，如有疑问，请申诉！";
                session.setAttribute("message",message);
            }
        }
        else{
            errorcode = "Your checkcode is wrong!Please try again!";
        }session.setAttribute("errorcode",errorcode);
        return "/account/SignOnForm";
    }

    @GetMapping("/SignOut")
    public String SignOut(Model model, HttpSession session) {
        Account account = (Account)session.getAttribute("account");
        account = null;
        model.addAttribute("account", account);

        session.removeAttribute("account");
        Cart cart=(Cart) session.getAttribute("cart");
        cart=null;
        session.setAttribute("cart",cart);
        return "/catalog/main";
    }

    //跳往注册界面
    @GetMapping("/viewRegister")
    public String ViewRegister(HttpSession session) {
        List<Category> categories = catalogService.getCategoryList();
        session.setAttribute("categories", categories);
        return "/account/NewAccountForm";
    }

    //注册
    @PostMapping("/newAccount")
    public String Register(Model model, HttpSession session, HttpServletRequest request) {
        String message1 = null;
        String message2 = null;
        //获得输入的验证码值
        String value1 = request.getParameter("verificationCode");
        /*获取图片的值*/
        String value2 = (String) session.getAttribute("code");
        session.removeAttribute("code");
        Boolean isSame = false;
        /*对比两个值（字母不区分大小写）*/
        if (value2.equalsIgnoreCase(value1)) {
            System.out.println("sss");
            isSame = true;
        }
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String repeatPassword = request.getParameter("repeatPassword");
        String firstName = request.getParameter("firstname");
        String lastName = request.getParameter("lastname");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address1 = request.getParameter("address1");
        String address2 = request.getParameter("address2");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String zip = request.getParameter("zip");
        String country = request.getParameter("country");
        String languagePreference = request.getParameter("languagePreference");
        String favouriteCategoryId = request.getParameter("favouriteCategoryId");
        String listOption = request.getParameter("listOption");
        String bannerOption = request.getParameter("bannerOption");

        System.out.println("1" + languagePreference + favouriteCategoryId + listOption + bannerOption);
        Account account1 = new Account();
        account1.setUsername(username);
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        repeatPassword = password;
        account1.setPassword(password);
        account1.setFirstName(firstName);
        account1.setLastName(lastName);
        account1.setEmail(email);
        account1.setPhone(phone);
        account1.setAddress1(address1);
        account1.setAddress2(address2);
        account1.setCity(city);
        account1.setState(state);
        account1.setZip(zip);
        account1.setCountry(country);
        account1.setLanguagePreference(languagePreference);
        account1.setFavouriteCategoryId(favouriteCategoryId);
        account1.setListOption(Boolean.parseBoolean(listOption));
        account1.setBannerOption(Boolean.parseBoolean(bannerOption));
        System.out.println(username);
        System.out.println(1);
        System.out.println(password);
        if (isSame) {
            Account account = accountService.getAccount(account1.getUsername());
            System.out.println(account);
            if (account == null) {
                if (username.equals("")) {
                    if (password.equals("")) {
                        message2 = null;
                        message1 = "Username and password are empty!";
                        session.setAttribute("message1", message1);
                        session.setAttribute("message2", message2);
                    } else {
                        message2 = null;
                        message1 = "Username is empty!";
                        session.setAttribute("message1", message1);
                        session.setAttribute("message2", message2);
                    }
                } else if (password.equals("")) {
                    message2 = null;
                    message1 = "Password is empty!";
                    session.setAttribute("message1", message1);
                    session.setAttribute("message2", message2);
                }
                else if(!password.equals("")&&password.equals(repeatPassword)){
                    accountService.insertAccount(account1);
                    return "/catalog/main";
                }
                else {
                    message2=null;
                    message1 = "Passwords are not the same!";
                    session.setAttribute("message1",message1);
                    session.setAttribute("message2",message2);
                }
            }
            else {
                message2=null;
                message1 = "The username has been registered!";
                session.setAttribute("message1",message1);
                session.setAttribute("message2",message2);
            }
        }
        else {
            message1=null;
            message2 = "VerificationCode is wrong!";
            session.setAttribute("message1",message1);
            session.setAttribute("message2",message2);
        }
        return "/account/NewAccountForm";
    }

    @GetMapping("/viewEdit")
    public String viewEdit(HttpSession session){
        List<Category> categories = catalogService.getCategoryList();
        session.setAttribute("categories", categories);
        return "/account/EditAccountForm";
    }

    @PostMapping("/confirmEdit")
    public String confirmEdit(HttpServletRequest request, HttpSession session) {
        Account account = (Account) session.getAttribute("account");

        String username = account.getUsername();
        String password = request.getParameter("password");
        String firstName = request.getParameter("firstname");
        String lastName = request.getParameter("lastname");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address1 = request.getParameter("address1");
        String address2 = request.getParameter("address2");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String zip = request.getParameter("zip");
        String country = request.getParameter("country");
        String languagePreference = request.getParameter("languagePreference");
        String favouriteCategoryId = request.getParameter("favouriteCategoryId");
        String listOption = request.getParameter("listOption");
        String bannerOption = request.getParameter("bannerOption");
        System.out.println(favouriteCategoryId);
        account.setUsername(username);
        account.setPassword(password);
        account.setFirstName(firstName);
        account.setLastName(lastName);
        account.setEmail(email);
        account.setPhone(phone);
        account.setAddress1(address1);
        account.setAddress2(address2);
        account.setCity(city);
        account.setState(state);
        account.setZip(zip);
        account.setCountry(country);
        account.setLanguagePreference(languagePreference);
        account.setFavouriteCategoryId(favouriteCategoryId);
        account.setBannerName("<image src=\""+"../images/banner_"+favouriteCategoryId.toLowerCase()+".gif\">");
        account.setListOption(Boolean.parseBoolean(listOption));
        account.setBannerOption(Boolean.parseBoolean(bannerOption));

        accountService.updateAccount(account);
        session.removeAttribute("account");
        session.setAttribute("account", account);

        return "/account/EditAccountForm";
    }

    @GetMapping("/usernameIsExist")
    public void usernameIsExist(@RequestParam("username") String username, HttpServletResponse response) throws IOException  {
        PrintWriter out = response.getWriter();
        response.setContentType("text/plain");
        Account account = accountService.getAccount(username);
        if(account != null) {
            out.print("Exist");
        }
        else{
            out.print("Not Exist");
        }
        out.flush();
        out.close();
    }

    @GetMapping("/verify")
    public void getVerifyCode(HttpServletResponse response, HttpSession session) throws IOException {
        final int width = 180; // 图片宽度
        final int height = 40; // 图片高度
        final String imgType = "jpeg"; // 指定图片格式 (不是指MIME类型)
        final OutputStream output = response.getOutputStream(); // 获得可以向客户端返回图片的输出流
        // (字节流)
        // 创建验证码图片并返回图片上的字符串
        String code = GraphicHelper.create(width, height, imgType, output);
        System.out.println("验证码内容: " + code);

        // 建立 uri 和 相应的 验证码 的关联 ( 存储到当前会话对象的属性中 )
        session.setAttribute("code", code);
    }
}
