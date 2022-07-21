package com.csuse.jpetstoressm.controller;

import com.alipay.api.AlipayApiException;
import com.csuse.jpetstoressm.domain.Account;
import com.csuse.jpetstoressm.domain.Cart;
import com.csuse.jpetstoressm.domain.Order;
import com.csuse.jpetstoressm.service.OrderService;
import com.csuse.jpetstoressm.service.alipay.bean.AlipayBean;
import com.csuse.jpetstoressm.service.alipay.service.AlipayService;
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
import java.io.PrintWriter;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AlipayService alipayService;


    @GetMapping("/newOrderForm")
    public String NewOrder(HttpSession session) {
        Account account = (Account) session.getAttribute("account");
        Cart cart = (Cart) session.getAttribute("cart");
        if(account == null){
            return "account/SignOnForm";
        }
        else if(cart!=null) {
            Order order = new Order();
            order.initOrder(account, cart);
            session.setAttribute("order", order);
            return "order/newOrder";
        }
        else {
            session.setAttribute("message0","An order could not be created because a cart could not be found.");
            return "common/error";
        }
    }

    @PostMapping("/shippingAddress")
    public String viewShipAddressForm(HttpSession session, HttpServletRequest req) {
        String shipToFirstName=req.getParameter("shipToFirstName");
        String shipToLastName=req.getParameter("shipToLastName");
        String shipAddress1=req.getParameter("shipAddress1");
        String shipAddress2=req.getParameter("shipAddress2");
        String shipCity=req.getParameter("shipCity");
        String shipState=req.getParameter("shipState");
        String shipZip=req.getParameter("shipZip");
        String shipCountry=req.getParameter("shipCountry");

        Order order = (Order) session.getAttribute("order");
        order.setShipToFirstName(shipToFirstName);
        order.setShipToLastName(shipToLastName);
        order.setShipAddress1(shipAddress1);
        order.setShipAddress2(shipAddress2);
        order.setShipCity(shipCity);
        order.setShipState(shipState);
        order.setShipZip(shipZip);
        order.setCourier(shipCountry);

        session.setAttribute("order", order);

        return "order/confirmOrder";
    }

    //进行订单确认，并支付
    @GetMapping("/confirmOrder")
    public String confirmOrder(HttpSession session, Model model) {
        Order order = (Order) session.getAttribute("order");
        Cart cart = (Cart) session.getAttribute("cart");
        Account account = (Account) session.getAttribute("account");
        if(order!=null) {
            orderService.insertOrder(order);
            session.setAttribute("order", order);
            cart = null;
            session.setAttribute("cart", cart);
            session.setAttribute("message0","Thank you, your order has been submitted.");
            orderService.deleteAll(account);
            return "redirect:/order/alipay?"+
                    "outTradeNo="+ order.getOrderId() +
                    "&subject="+ order.getUsername() +
                    "&totalAmount="+ order.getTotalPrice() +
                    "&body="+ order.getOrderDate().toString();
        }
        else{
            session.setAttribute("message0","An error occurred processing your order (order was null).");
            return "common/error";
        }
    }

    @GetMapping("/viewOrder")
    public String viewOrder(@RequestParam("orderId") int orderId, HttpSession session) {
        Order order = orderService.getOrder(orderId);
        session.setAttribute("order", order);
        return "order/viewOrder";
    }

    @GetMapping("/viewListOrder")
    public String viewListOrder(@RequestParam("username") String username, HttpSession session) {
        List<Order> orderList = orderService.getOrderByUsername(username);
        session.setAttribute("orderList", orderList);

        return "order/listOrders";
    }


    @RequestMapping("/alipay")
    public String alipay(@RequestParam("outTradeNo") String outTradeNo,
                       @RequestParam("subject") String subject,
                       @RequestParam("totalAmount") String totalAmount,
                       @RequestParam("body") String body,
                       Model model) throws AlipayApiException, IOException {
        AlipayBean alipayBean=new AlipayBean();
        alipayBean.setOut_trade_no(outTradeNo);
        alipayBean.setSubject(subject);
        alipayBean.setTotal_amount(totalAmount);
        alipayBean.setBody(body);
        String pay = alipayService.aliPay(alipayBean);
        model.addAttribute("form", pay);
        return "order/pay";
    }

    @RequestMapping("/paySuccess")
    public String paySuccess(HttpSession session){
        int order_id = ((Order)session.getAttribute("order")).getOrderId();
        return "redirect:/order/viewOrder?orderId="+order_id;
    }

}
