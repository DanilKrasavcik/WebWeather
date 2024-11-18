package com.example;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * WeatherServlet - это сервлет, который обрабатывает запросы
 * на получение информации о погоде для указанного города.
 */

@WebServlet("/weather")
public class WeatherServlet extends HttpServlet {
    private static final String API_KEY = "86f5fb506cc5a768a1842c6c8870da54";
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=" + API_KEY;

    /**
     * Обрабатывает GET-запросы для получения данных о погоде.
     *
     * @param request объект HttpServletRequest, содержащий данные о запросе
     * @param response объект HttpServletResponse, используемый для отправки ответа
     * @throws ServletException если происходит ошибка в процессе обработки
     * @throws IOException если возникает ошибка ввода-вывода
     */

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String city = request.getParameter("city");
        if (city == null || city.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "City parameter is missing");
            return;
        }

        String url = String.format(API_URL, city);
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();

            if (entity != null) {
                String result = EntityUtils.toString(entity);
                response.setContentType("application/json");
                response.getWriter().write(result);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "City not found");
            }
        }
    }
}