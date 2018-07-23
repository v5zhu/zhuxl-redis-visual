//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zhuxl.redis.visual.system.utils;

import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class SecurityCodeServlet extends HttpServlet {
    private static final long serialVersionUID = -770474964506858977L;
    public static final String KEY_SECURITY_CODE = "securityCode";
    public static final String CODE = "23456789abcdefghijkmnpqrstuvwxyzABCDEFGHIJKLMNPQRSTUVWXYZ";
    public static final int CODE_LENGTH = "23456789abcdefghijkmnpqrstuvwxyzABCDEFGHIJKLMNPQRSTUVWXYZ".length();
    private Logger log = null;

    public SecurityCodeServlet() {
        this.log = Logger.getRootLogger();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Constants.DATABASEPATH = request.getSession().getServletContext().getRealPath("/") + "WEB-INF" + File.separator + "classes" + File.separator;
        response.setContentType("image/jpeg");
        this.createImage(request, response);
    }

    private void createImage(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0L);
        int width = 60;
        int height = 30;
        BufferedImage image = new BufferedImage(width, height, 1);
        Graphics g = image.getGraphics();
        Random random = new Random();
        g.setColor(new Color(255, 255, 255));
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Arial", 0, 18));
        g.setColor(this.getRandColor(160, 200));

        int i;
        int code;
        for (i = 0; i < 155; ++i) {
            i = random.nextInt(width);
            code = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(i, code, i + xl, code + yl);
        }

        String sRand = "";

        for (i = 0; i < 4; ++i) {
            code = random.nextInt(CODE_LENGTH);
            String rand = String.valueOf("23456789abcdefghijkmnpqrstuvwxyzABCDEFGHIJKLMNPQRSTUVWXYZ".charAt(code));
            sRand = sRand.concat(rand);
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            g.drawString(rand, 13 * i + 6, 16);
        }

        request.getSession().setAttribute("KAPTCHA_SESSION_KEY", sRand.toLowerCase());
        g.dispose();

        try {
            ImageIO.write(image, "JPEG", response.getOutputStream());
        } catch (IOException var13) {
            this.log.error("SecurityCodeSevlet.createImage() : Failed : " + var13.getMessage());
        }

    }

    private Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }

        if (bc > 255) {
            bc = 255;
        }

        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
}
