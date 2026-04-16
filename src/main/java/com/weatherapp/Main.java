package com.weatherapp;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=================================");
        System.out.println("    APP DE PREVISÃO DO TEMPO");
        System.out.println("=================================");
        System.out.print("Digite o nome da cidade: ");

        String cidade = scanner.nextLine();

        try {
            HttpClient client = HttpClient.newHttpClient();

            String cidadeCodificada = URLEncoder.encode(cidade, StandardCharsets.UTF_8);

            // 1. Buscar latitude e longitude
            String geoUrl = "https://geocoding-api.open-meteo.com/v1/search?name=" + cidadeCodificada + "&count=1";

            HttpRequest geoRequest = HttpRequest.newBuilder()
                    .uri(URI.create(geoUrl))
                    .build();

            HttpResponse<String> geoResponse = client.send(geoRequest, HttpResponse.BodyHandlers.ofString());
            String geoBody = geoResponse.body();

            Double latitude = extractNumber(geoBody, "\"latitude\":\\s*(-?\\d+(?:\\.\\d+)?)");
            Double longitude = extractNumber(geoBody, "\"longitude\":\\s*(-?\\d+(?:\\.\\d+)?)");

            if (latitude == null || longitude == null) {
                System.out.println("\n❌ Cidade não encontrada 😢");
                return;
            }

            // 2. Buscar clima atual + previsão de 4 dias
            String weatherUrl = "https://api.open-meteo.com/v1/forecast?latitude="
                    + latitude
                    + "&longitude="
                    + longitude
                    + "&current_weather=true"
                    + "&daily=temperature_2m_max,temperature_2m_min"
                    + "&forecast_days=4";

            HttpRequest weatherRequest = HttpRequest.newBuilder()
                    .uri(URI.create(weatherUrl))
                    .build();

            HttpResponse<String> weatherResponse = client.send(weatherRequest, HttpResponse.BodyHandlers.ofString());
            String weatherBody = weatherResponse.body();

            Double temperatura = extractNumber(weatherBody, "\"temperature\":\\s*(-?\\d+(?:\\.\\d+)?)");

            if (temperatura == null) {
                System.out.println("\n⚠️ Não foi possível obter a temperatura.");
                return;
            }

            String[] datas = extractStringArray(weatherBody, "\"time\"\\s*:\\s*\\[(.*?)\\]");
            Double[] maximas = extractNumberArray(weatherBody, "\"temperature_2m_max\"\\s*:\\s*\\[(.*?)\\]");
            Double[] minimas = extractNumberArray(weatherBody, "\"temperature_2m_min\"\\s*:\\s*\\[(.*?)\\]");

            // 3. Mostrar resultado
            System.out.println("\n=================================");
            System.out.println("📍 Cidade: " + cidade);
            System.out.println("🌡️ Temperatura atual: " + temperatura + "°C");

            if (datas != null && maximas != null && minimas != null) {
                System.out.println("\n📅 Previsão para os próximos 4 dias:");
                System.out.println("---------------------------------");

                int limite = Math.min(datas.length, Math.min(maximas.length, minimas.length));

                for (int i = 0; i < limite; i++) {
                    System.out.println("🗓️ " + datas[i] + " | Máx: " + maximas[i] + "°C | Mín: " + minimas[i] + "°C");
                }
            }

            System.out.println("=================================");

        } catch (Exception e) {
            System.out.println("\n⚠️ Erro ao buscar dados do clima: " + e.getMessage());
        }
    }

    public static Double extractNumber(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return Double.parseDouble(matcher.group(1));
        }

        return null;
    }

    public static String[] extractStringArray(String text, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);

        if (!matcher.find()) {
            return null;
        }

        String content = matcher.group(1).replace("\"", "").trim();
        String[] parts = content.split(",");

        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }

        return parts;
    }

    public static Double[] extractNumberArray(String text, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);

        if (!matcher.find()) {
            return null;
        }

        String content = matcher.group(1).trim();
        String[] parts = content.split(",");
        Double[] numbers = new Double[parts.length];

        for (int i = 0; i < parts.length; i++) {
            numbers[i] = Double.parseDouble(parts[i].trim());
        }

        return numbers;
    }
}