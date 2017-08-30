package org.me.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

/**
 * Created by t3tiger on 2017/7/12.
 */
@WebServlet(name = "TestServlet", urlPatterns = {"/pay/baofoo"})
public class TestServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        System.out.println(TestServlet.class.getSimpleName() + " Start::Name="
                + Thread.currentThread().getName() + "::ID="
                + Thread.currentThread().getId());

        String TransID = "PAGATID" + System.currentTimeMillis();//商户订单号（不能重复）
        String TradeDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());//下单日期
        String MemberID = "100000178";//商户号
        String TerminalID = "10000001";//终端号
        String ProductName = "商品名称";//商品名称
        String Amount = "1";//商品数量
        String Username = "用户名称";//支付用户名称
        String AdditionalInfo = "附加信息";//订单附加信息
        String PageUrl = "http://47.94.44.56:9090/notify";//页面跳转地址
        String ReturnUrl = "http://47.94.44.56:9090/notify";//服务器底层通知地址
        String NoticeType = "1";//通知类型
        String Md5key = "abcdefg";//md5密钥（KEY）
        String MARK = "|";

        String PayID = "3001";
        String OrderMoney = "100";


        String md5 = new String(MemberID + MARK + PayID + MARK + TradeDate + MARK + TransID + MARK + OrderMoney + MARK + PageUrl + MARK + ReturnUrl + MARK + NoticeType + MARK + Md5key);//MD5签名格式
        log("请求（MD5）拼接字串：" + md5);//商户在正式环境不要输出此项以免泄漏密钥，只在测试时输出以检查验签失败问题

        String Signature = SecurityUtil.MD5(md5);//计算MD5值



        request.getSession().setAttribute("MemberID", MemberID);
        request.getSession().setAttribute("TerminalID", TerminalID);
        request.getSession().setAttribute("InterfaceVersion", "4.0");
        request.getSession().setAttribute("KeyType", "1");
        request.getSession().setAttribute("PayID", PayID);
        request.getSession().setAttribute("TradeDate", TradeDate);
        request.getSession().setAttribute("TransID", TransID);
        request.getSession().setAttribute("OrderMoney", OrderMoney);
        request.getSession().setAttribute("ProductName", ProductName);
        request.getSession().setAttribute("Amount", Amount);
        request.getSession().setAttribute("Username", Username);
        request.getSession().setAttribute("AdditionalInfo", AdditionalInfo);
        request.getSession().setAttribute("NoticeType", NoticeType);
        request.getSession().setAttribute("PageUrl", PageUrl);
        request.getSession().setAttribute("ReturnUrl", ReturnUrl);
        request.getSession().setAttribute("Signature", Signature);

        System.out.println(request.getProtocol());

        response.setContentType("text/html;charset=utf-8");
//        response.setStatus(302);
//        response.setStatus(303);
        response.setStatus(307);


        String targetUrl = "https://vgw.baofoo.com/payindex?";
        Enumeration<String> attributeNames = request.getSession().getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String key = attributeNames.nextElement();
            Object value = request.getSession().getAttribute(key);

            targetUrl += key + "=" + value + "&";
        }

        targetUrl = targetUrl.substring(0, targetUrl.length() - 1);
        response.setHeader("Location", targetUrl);


        PrintWriter out = response.getWriter();
        long endTime = System.currentTimeMillis();
        out.write("正在跳转...");
        System.out.println(TestServlet.class.getSimpleName() + " Start::Name="
                + Thread.currentThread().getName() + "::ID="
                + Thread.currentThread().getId() + "::Time Taken="
                + (endTime - startTime) + " ms.");
    }

}
