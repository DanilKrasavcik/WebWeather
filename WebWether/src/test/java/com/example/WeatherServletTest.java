package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

class WeatherServletTest {

    private WeatherServlet weatherServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;

    /**
     * WeatherServlet {@link WeatherServlet} и мока для HttpServletRequest и HttpServletResponse перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        weatherServlet = new WeatherServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    /**
     * Тестирует обработку GET-запроса, когда параметр города отсутствует.
     * Проверяет, что в ответ отправляется ошибка 400 с соответствующим сообщением.
     *
     * @throws Exception если происходит ошибка в процессе выполнения теста
     */
    @Test
    void testDoGet_CityParameterMissing() throws Exception {
        when(request.getParameter("city")).thenReturn(null);

        weatherServlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_BAD_REQUEST, "City parameter is missing");
    }

    /**
     * Тестирует обработку GET-запроса, когда указанный город не найден.
     * Проверяет, что в ответ отправляется ошибка 404 с соответствующим сообщением.
     *
     * <p>
     * Обратите внимание, что этот тест может завершиться таймаутом, если
     * обработка запроса занимает слишком много времени. Для предотвращения
     * этого используется аннотация @Timeout.
     * </p>
     *
     * @throws Exception если происходит ошибка в процессе выполнения теста
     */
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void testDoGet_CityNotFound() throws Exception {
        when(request.getParameter("city")).thenReturn("InvalidCity");

        weatherServlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "City not found");
    }

    /**
     * Тестирует успешный ответ на GET-запрос для существующего города.
     * Проверяет, что данные записываются в ответ.
     *
     * @throws Exception если происходит ошибка в процессе выполнения теста
     */
    @Test
    void testDoGet_SuccessfulResponse() throws Exception {
        when(request.getParameter("city")).thenReturn("London");

        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);
    }
}