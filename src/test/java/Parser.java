import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class Parser {
    private static HashMap<String, Integer> properties = new HashMap<>();
    private static HashMap<String, Integer> mapUrls = new HashMap<>();

    //сохраняем конфиг свойства
    private static void saveConfigProperties() {
        Properties properties = new Properties();
        File file = new File("src/main/resources/config.properties");
        properties.setProperty("USER_AGENT", "Chrome/4.0.249.0 Safari/532.5");
        properties.setProperty("url", "https://sharafstore.com/c/telefon/akilli-telefonlar/");
        properties.setProperty("REFERRER", "http://www.google.com");
        properties.setProperty("API", "http://back1.1way.market/board/hs/v1/dashboards");
        properties.setProperty("currency", "949");
        properties.setProperty("beginPage", "1");
        try {
            properties.store(new FileOutputStream(file), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //получаем конфиг свойства
    private static String getConfigProperty(String s) {
        Properties properties = new Properties();
        File file = new File("src/main/resources/config.properties");
        try {
            properties.load(new FileInputStream(file));
            String value = properties.getProperty(s);
            return value;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void addMapUrs() {
        mapUrls.put("https://sharafstore.com/c/telefon/tablet-tr/", 24);
        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/beyaz-esya/buzdolabi/", 3);
        mapUrls.put("https://sharafstore.com/c/televizyon-ses/televizyon/", 2);
        mapUrls.put("https://sharafstore.com/c/bilgisayar-oyun/bilgisayar/", 21);
        mapUrls.put("https://sharafstore.com/c/bilgisayar-oyun/konsol/", 75);
        mapUrls.put("https://sharafstore.com/c/bilgisayar-oyun/bilgisayar-aksesuar/", 73);
        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/beyaz-esya/isitma-sogutma/klima/", 77);
        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/beyaz-esya/makineler/bulasik/", 76);
        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/beyaz-esya/pisirme/firinlar/", 82);
        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/beyaz-esya/pisirme/mikrodalga/", 79);
        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/beyaz-esya/pisirme/ocak/", 80);
        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/beyaz-esya/pisirme/aspirator-davlumbaz/", 81);
        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/beyaz-esya/isitma-sogutma/vantilator/", 78);
        mapUrls.put("https://sharafstore.com/c/guvenlik-kamera/", 74);
        mapUrls.put("https://sharafstore.com/c/televizyon-ses/ev-sinemasi/", 70);
        mapUrls.put("https://sharafstore.com/c/televizyon-ses/ses/hoparlor/", 71);
        mapUrls.put("https://sharafstore.com/c/televizyon-ses/ses/kulaklik/", 72);
        mapUrls.put("https://sharafstore.com/c/televizyon-ses/tv-aksesuar/", 69);
        mapUrls.put("https://sharafstore.com/c/telefon/akilli-telefonlar/", 1);
        mapUrls.put("https://sharafstore.com/c/telefon/akilli-saat-bileklik/", 26);
        mapUrls.put("https://sharafstore.com/c/telefon/aksesuar/", 27);
        mapUrls.put("https://sharafstore.com/c/telefon/sabit-telefon/", 28);
        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/beyaz-esya/makineler/camasir/", 31);
        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/beyaz-esya/makineler/kurutmali-camasir/", 31);
        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/beyaz-esya/makineler/kurutma/", 31);
        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/temizlik/kablosuz-supurge/", 30);
        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/temizlik/robot-supurge-paspas/", 30);
        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/temizlik/kablolu-supurge/", 30);
        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/temizlik/elektrikli-paspas/", 30);
        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/temizlik/el-supurge/", 30);
        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/temizlik/yedek-parca/", 83);
        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/mutfak-aletleri/mutfak-robotlari/", 92);
        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/mutfak-aletleri/tencere-tava/", 93);
        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/mutfak-aletleri/elektrikli-izgaralar/", 86);
        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/mutfak-aletleri/pisirici-makineleri/", 87);
        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/mutfak-aletleri/ekmek-kizartma/", 84);
        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/mutfak-aletleri/su-isitici/", 85);
        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/mutfak-aletleri/su-sebil/", 95);
        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/kahve-makineleri/", 94);
        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/utuler/", 88);
        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/hava-bakimi/", 89);
        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/ev-aletleri/akilli-aydinlatma/", 99);
        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/ev-aletleri/akilli-ev-urunleri/", 96);
        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/mutfak-aletleri/dijital-tarti/", 102);
        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/mutfak-aletleri/gerecler/", 103);
        mapUrls.put("https://sharafstore.com/c/guzellik-saglik/sac-bakim/masalar/", 105);
        mapUrls.put("https://sharafstore.com/c/guzellik-saglik/sac-bakim/sekilendiriciler/", 106);
        mapUrls.put("https://sharafstore.com/c/guzellik-saglik/sac-bakim/sac-kurutma-makineleri/", 107);
        mapUrls.put("https://sharafstore.com/c/guzellik-saglik/sac-bakim/sicak-hava-firca-ve-taraklar/", 108);
        mapUrls.put("https://sharafstore.com/c/guzellik-saglik/sac-bakim/aksesuarlar/", 109);
        mapUrls.put("https://sharafstore.com/c/guzellik-saglik/epilasyon/kadin-tiras-makineleri/", 110);
        mapUrls.put("https://sharafstore.com/c/guzellik-saglik/epilasyon/epilasyon-aletleri/", 111);
        mapUrls.put("https://sharafstore.com/c/guzellik-saglik/epilasyon/ipl-lazer/", 112);
        mapUrls.put("https://sharafstore.com/c/guzellik-saglik/erkek-bakim-aletleri/sakal-ve-biyik-tiras/", 116);
        mapUrls.put("https://sharafstore.com/c/guzellik-saglik/erkek-bakim-aletleri/kulak-ve-burun-temizlik/", 117);
        mapUrls.put("https://sharafstore.com/c/guzellik-saglik/erkek-bakim-aletleri/sac-ve-vucut-tiras-makineleri/", 118);
        mapUrls.put("https://sharafstore.com/c/guzellik-saglik/iyilik-ve-bakim-urunleri/masaj-aletleri/", 120);
        mapUrls.put("https://sharafstore.com/c/guzellik-saglik/iyilik-ve-bakim-urunleri/manikur-ve-pedikur/", 121);
        mapUrls.put("https://sharafstore.com/c/guzellik-saglik/iyilik-ve-bakim-urunleri/difuzorler/", 122);
        mapUrls.put("https://sharafstore.com/c/guzellik-saglik/iyilik-ve-bakim-urunleri/yuz-bakim-cihazlari/", 123);
        mapUrls.put("https://sharafstore.com/c/hobi-fltness/fitness-ve-kondisyon/yurume-bandi-tr//", 133);
        mapUrls.put("https://sharafstore.com/c/hobi-fltness/fitness-ve-kondisyon/yurume-bandi/", 133);
        mapUrls.put("https://sharafstore.com/c/hobi-fltness/akilli-ulasim/scooter-tr/", 135);
        mapUrls.put("https://sharafstore.com/c/hobi-fltness/akilli-ulasim/ninebot-tr/", 136);
        mapUrls.put("https://sharafstore.com/c/hobi-fltness/akilli-ulasim/e-bisiklet/", 137);
        mapUrls.put("https://sharafstore.com/c/hobi-fltness/seyahat/valiz/", 139);
        mapUrls.put("https://sharafstore.com/c/hobi-fltness/seyahat/canta/", 140);
        mapUrls.put("https://sharafstore.com/c/hobi-fltness/seyahat/gps-takipci/", 141);
        mapUrls.put("https://sharafstore.com/c/hobi-fltness/seyahat/akilli-semsiye/", 142);
        mapUrls.put("https://sharafstore.com/c/hobi-fltness/foto-ve-video/drone/", 144);
        mapUrls.put("https://sharafstore.com/c/hobi-fltness/foto-ve-video/kamera/", 145);
        mapUrls.put("https://sharafstore.com/c/hobi-fltness/foto-ve-video/hafiza-kartlar/", 146);
        mapUrls.put("https://sharafstore.com/c/hobi-fltness/foto-ve-video/kamera-aksesuar/", 147);
        mapUrls.put("https://sharafstore.com/c/hobi-fltness/diger/evcil-hayvan/", 148);
        mapUrls.put("https://sharafstore.com/c/hobi-fltness/diger/oto-aksesuarlar/", 149);
        mapUrls.put("https://sharafstore.com/c/cocuk-oyuncaklar/cocuk-oyuncaklar-tr/?filter_age-range=0-12-ay", 153);
        mapUrls.put("https://sharafstore.com/c/cocuk-oyuncaklar/cocuk-oyuncaklar-tr/?filter_age-range=12-24-ay", 154);
        mapUrls.put("https://sharafstore.com/c/cocuk-oyuncaklar/cocuk-oyuncaklar-tr/?filter_age-range=3-6-yas", 155);
        mapUrls.put("https://sharafstore.com/c/cocuk-oyuncaklar/cocuk-oyuncaklar-tr/?filter_age-range=6-12-yas", 156);
        mapUrls.put("https://sharafstore.com/c/cocuk-oyuncaklar/cocuk-oyuncaklar-tr/", 157);
        mapUrls.put("https://sharafstore.com/c/cocuk-oyuncaklar/bebek-bakimi/biberon/", 159);
        mapUrls.put("https://sharafstore.com/c/cocuk-oyuncaklar/bebek-bakimi/emzik/", 160);
        mapUrls.put("https://sharafstore.com/c/cocuk-oyuncaklar/cocuklar/dis-firca/", 161);
    }

    // считаем количество страниц, на которые нужно будет переходить
    private static int pageCount(String url) throws IOException, NumberFormatException {
        int pagesCounter = 1;
        Document document = Jsoup.connect(url).userAgent(getConfigProperty("USER_AGENT"))
                .referrer(getConfigProperty("REFERRER"))
                .timeout(100 * 1000)
                .get();
        Elements pages = document.select("nav[class*=\"-pagination\"] li .page-numbers");
        if (pages.size() > 0) {
            for (Element p : pages) {
                switch (p.text()) {
                    case ("1"), ("16"), ("15"), ("2"), ("3"), ("4"), ("5"), ("6"), ("7"), ("9"), ("8"), ("10"), ("11"), ("12"), ("13"), ("14") -> pagesCounter = Integer.parseInt(p.text());
                    default -> {
                        break;
                    }
                }
            }
        }
        return pagesCounter;
    }

    // добавляем ссылки перехода на страницы
    private static List<String> addUrl(String url) throws IOException {
        List<String> urlsPages = new ArrayList<>();
        for (int i = 1; i <= pageCount(url); i++) {
            String u = url + "page/" + i;
            urlsPages.add(u);
        }
        return urlsPages;
    }

    // получаем ссылки на страницу товара, добавляем в лист
    private static List<String> addUrlsProducts(List<String> urlsPages) throws IOException {
        List<String> urlsProducts = new ArrayList<>();
        for (int i = 0; i < urlsPages.size(); i++) {
            Document document = Jsoup.connect(urlsPages.get(i)).userAgent(getConfigProperty("USER_AGENT"))
                    .referrer(getConfigProperty("REFERRER"))
                    .timeout(100 * 1000)
                    .get();
            Elements links = document.select("h3 > a");// link&title
            for (Element link : links) {
                urlsProducts.add(link.attr("abs:href"));
            }
        }
        return urlsProducts;
    }

    // конвертируем цену в число, убираем лишнее
    private static int getConvertPrice(String price) throws NullPointerException {
        if (!(price == "")) {
            String s = price.contains("₺") ? price.substring(1) : "";
            String n = "";
            if (s.contains(",")) {
                int i = s.indexOf(",");
                int j = s.indexOf(".");
                n = s.substring(0, i) + s.substring(i + 1, j);
            } else {
                int j = s.indexOf(".");
                n = s.substring(0, j);
            }
            return Integer.parseInt(n);
        }
        return 0;
    }

    // отрезаем у ссылки лишний слэш
    private static String getConvertUrl(String link) {
        int x = link.lastIndexOf("/");
        String url = link.substring(0, x);
        return url;
    }

    // проверяем, есть ли объявление на сайте, возвращает код, если существует и 0, если нет
    private static HashMap<String, Integer> checkIDProduct(List<String> url) throws IOException {
        HashMap<String, Integer> idProd = new HashMap<>();
        for (String str : url) {
            str = getConvertUrl(str);
            Connection connection = Jsoup.connect("http://back1.1way.market/board/hs/v1/dashboards2");
            connection.userAgent(getConfigProperty("USER_AGENT"));
            connection.requestBody(str).ignoreHttpErrors(true).ignoreContentType(true);
            connection.header("Content-Type", "application/json");
            connection.header("key", "33894f01-a28b-45cd-898d-eddf98012b14");
            connection.method(Connection.Method.POST);
            Connection.Response response = connection.timeout(100 * 1000).execute();
            int i = response.statusCode();
            if (i == 200) {
                idProd.put(str, 200);
            } else {
                idProd.put(str, 0);
            }
        }
        return idProd;
    }

    // посылаем урл картинки, если картинка была загружена, возвращает ИД картинки
    private static int getIdIfExistImg(String url) throws IOException, IllegalArgumentException {
        int n;
        String str = "";
        Connection connection = Jsoup.connect("http://back1.1way.market/board/hs/v1/img2");
        connection.userAgent(getConfigProperty("USER_AGENT"));
        connection.requestBody(url).ignoreContentType(true).ignoreHttpErrors(true);
        connection.header("Content-Type", "application/json");
        connection.header("key", "33894f01-a28b-45cd-898d-eddf98012b14");
        connection.method(Connection.Method.POST);
        Connection.Response response = connection.timeout(100 * 1000).execute();
        Document s = response.parse();
        n = response.statusCode();
        if (s.text().contains("Error")) {
            str = getPermission(url);
            System.out.printf("  Картинки не существует. Отправлен запрос. Тело ответа  " + str);

        } else {
            System.out.printf("  Картинка существует, id   " + s.text());

        }
        return n == 200 ? Integer.parseInt(s.text()) : str == null ? 0 : Integer.parseInt(str);
    }

    // собираем json картинки (ссылка + имя + формат), отправляем на сервер,получаем разрешение на отправку картинки в байтах
    // или ИД картинки, если она уже существует
    private static String getPermission(String url) throws IOException {
        JSONObject json = new JSONObject();
        HashMap<String, String> permission = new HashMap<>();
        String str = "";
        int x = url.lastIndexOf(".");
        int y = url.lastIndexOf("/");
        String name = url.substring(y + 1, x);
        String format = url.substring(x + 1);
        json.put("url", url);
        json.put("title", name);
        json.put("format", format);
        Connection.Response response = Jsoup.connect("http://back1.1way.market/board/hs/v1/img")
                .userAgent(getConfigProperty("USER_AGENT"))
                .requestBody(json.toString()).ignoreContentType(true).ignoreHttpErrors(true)
                .header("Content-Type", "application/json")
                .header("key", "33894f01-a28b-45cd-898d-eddf98012b14")
                .method(Connection.Method.POST).execute();
        if (response.statusCode() == 200) {
            System.out.println("  Разрешение получено    " + url);
            permission.put(response.body(), url);
            str = sendByteImg(permission);
        }
        return str;
    }

    // посылаем двоичные данные картинки
    private static String sendByteImg(HashMap<String, String> permission) throws IOException {
        String r = "";
        for (Map.Entry<String, String> p : permission.entrySet()) {
            String url = getUrlForByte(p);
            URL str = new URL(p.getValue());
            InputStream source = str.openStream();
            byte[] buf = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((bytesRead = source.read(buf)) != -1) {
                baos.write(buf, 0, bytesRead);
            }
            URL uri = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("key", "33894f01-a28b-45cd-898d-eddf98012b14");
            connection.setRequestProperty("User-Agent", getConfigProperty("USER_AGENT"));
            connection.setConnectTimeout(1000 * 1000);
            connection.setReadTimeout(1000 * 1000);
            try (DataOutputStream writer = new DataOutputStream(connection.getOutputStream())) {
                writer.write(baos.toByteArray());
                writer.flush();

            } finally {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (connection.getInputStream())));
                System.out.println("Output from Server .... \n");
                while ((r = br.readLine()) != null) {
                    System.out.println(r);
                    return r;
                }
                connection.disconnect();
            }
        }
        return r;
    }

    // получаем код картинки, если была загружена, складываем в set
    private static Set<Integer> convertImgForID(Set<String> img) throws IOException, IllegalArgumentException {
        Set<Integer> id = new HashSet<>();
        for (String str : img) {
            int x = getIdIfExistImg(str);
            id.add(x);
        }
        return id;
    }

    // ссылки на картинки складываем в set
    private static Set<String> getImgForProduct(Elements links) {
        Set<String> s = new HashSet<>();
        for (Element link : links) {
            String l = link.attr("abs:href");
            s.add(l);
        }
        return s;
    }

    //добавляем ИД разрешения к ссылке
    private static String getUrlForByte(Map.Entry<String, String> p) {
        String link = "http://back1.1way.market/board/hs/v1/img3/";
        String s = p.getKey();
        link = link + s;
        return link;
    }

    //добавляем коды свойств товаров
    private static void addCodProperties() {
        properties.put("İşletim Sistemi Sürümü", 2); // Версия операцинной системы
        properties.put("İşletim Sistemi", 2);
        properties.put("Yönlendirici Bağlantı Türü", 63); //Тип подключения маршрутизатора
        properties.put("Ethernet Bağlantı Noktası Hızı", 64); //Скорость порта Ethernet
        properties.put("LAN Bağlantı Noktaları", 65); // LAN-порты
        properties.put("Max. LAN Veri Oranı", 66); // Максимум. Скорость передачи данных по локальной сети
        properties.put("Anten Miktarı", 67); // Количество антенн
        properties.put("WiFi Mesafesi", 68); // Расстояние Wi-Fi
        properties.put("Grafik", 69); // Графика
        properties.put("Fan Hızı", 70); // Скорость вентилятора
        properties.put("Görüntü Oranı", 71); // Соотношение сторон
        properties.put("Ekran Eğimi", 72); // Наклон экрана
        properties.put("Yenileme Hızı", 73); // Частота обновления
        properties.put("Tepki Süresi", 74); //Время отклика
        properties.put("Renk Gamı", 75); // Цветовая гамма
        properties.put("Kontrast", 76); // Контраст
        properties.put("Parlaklık", 77); // Яркость
        properties.put("Seri", 78); // Серия
        properties.put("Donanım Arabirimi", 79); // Аппаратный интерфейс
        properties.put("Model Numarası", 80); //  Номер модели
        properties.put("Arayüz Türü", 81); // Тип интерфейса
        properties.put("RAM türü", 82); // Тип оперативной памяти
        properties.put("SSD", 83);
        properties.put("Dizüstü Bilgisayar Türü", 84); // Тип ноутбука
        properties.put("Depolama Boyutu", 85); // Размер хранилища
        properties.put("Tasarım", 38); //дизайн
        properties.put("Kasa Tipi", 39); //тип чехла
        properties.put("Uyumlu Ürünler", 40); //Совместимые продукты
        properties.put("Ürün Modeli", 41); //Модель продукта
        properties.put("Model", 41); //Модель
        properties.put("Model adı", 41); //Модель
        properties.put("Modeli", 41); //Модель
        properties.put("Ürünün Ana Süreci", 42); //Основной процесс продукта
        properties.put("Yüzey İşlemi", 43); //Обработка поверхности
        properties.put("Ana Malzeme", 45); //Основной материал
        properties.put("Ana malzemeler", 45); //Основной материал
        properties.put("Malzeme", 45); //материал
        properties.put("Materyal", 45); //материал
        properties.put("Birincil Malzeme", 45); //материал
        properties.put("Plaka Malzemesi", 45); //материал
        properties.put("Malzeme İçeriği", 45); //Содержание материала
        properties.put("İç malzeme", 45); //Содержание материала
        properties.put("Aşınma Direnci", 46); //Износостойкость
        properties.put("Ürün Boyutu", 34); //Размер продукта
        properties.put("Konektör 1", 47); //Разъем 1
        properties.put("Konektör 2", 47);//Разъем 2
        properties.put("HDMI", 61);
        properties.put("USB Yuvaları", 62);
        properties.put("USB", 62);
        properties.put("Bluetooth", 54);
        properties.put("LOGO Ürün Süreci", 44); //Процесс производства логотипа
        properties.put("Desteklenen çıkış gücü", 48); //Поддерживаемая выходная мощность
        properties.put("Uzunluk", 49); //Длина
        properties.put("Derinlik", 103); //Глубина
        properties.put("Çap", 50); // Диаметр
        properties.put("Maşa Çapı", 50); // Диаметр
        properties.put("Tür", 51); // Тип
        properties.put("Türü", 51);
        properties.put("Tip", 51);
        properties.put("Ekran Türü", 52); //Тип дисплея
        properties.put("Genişlik", 53); // Ширина
        properties.put("Bluetooth Sürümü", 54); //Версия Bluetooth
        properties.put("Pil Ömrü", 55); //Срок службы батареи
        properties.put("Görüntüle", 52); //Дисплей
        properties.put("Piller", 56); //Аккумуляторы
        properties.put("Kablosuz Türü", 57); // Тип беспроводной сети
        properties.put("Akıllı Özellikler", 58); // Интеллектуальные функции
        properties.put("Yükseklik", 32); //Высота
        properties.put("Öğe Yüksekliği", 32); //Высота
        properties.put("Ürün Yükseklik", 32); //Высота
        properties.put("Bağlantı", 59); // Связь
        properties.put("Kablosuz bağlantı", 59); // Связь
        properties.put("Fiziksel Boyutlar", 34); // Физические размеры
        properties.put("G x D x Y (mm)", 34); //размеры
        properties.put("Ölçüler", 34); //размеры
        properties.put("Öğe Boyutlar U x G x Y", 34); //размеры
        properties.put("Ambalaj Boyutu", 34); //размеры
        properties.put("Ürün Boyutları LxWxH", 34); //размеры
        properties.put("Ürün Boyutları U x G x Y", 34); //размеры
        properties.put("Ürün Boyutları", 34); //размеры
        properties.put("Ürün büyüklüğü (L x W x H)", 34); //размеры
        properties.put("Dış Boyutlar", 34); //размеры
        properties.put("Çözünürlük", 29); // Разрешение
        properties.put("Çözüm", 29);
        properties.put("Müzikli GPS modu", 60); // Режим GPS с музыкой
        properties.put("Müzik olmadan GPS modu", 60); // Режим GPS без музыки
        properties.put("Marka", 3); // наименование
        properties.put("Ticari Marka", 3); // наименование
        properties.put("Marka Adı", 3); //наименование
        properties.put("Boyut", 34); //Измерение
        properties.put("Boyutlar", 34); //Габаритные размеры
        properties.put("Mm boyutu", 34);
        properties.put("Bağlantı Teknolojisi", 35); //Технология соединения
        properties.put("Sesli Komut", 36); //Голосовые команды
        properties.put("GPS", 37);
        properties.put("Ekran Boyutu", 17);//Размер экрана
        properties.put("LCD ekran", 17);//Размер экрана
        properties.put("Cpu", 18);
        properties.put("CPU modeli", 18); //Модель процессора
        properties.put("İşlemci", 18); //Процессор
        properties.put("Dahili Bellek", 19); //Внутренняя память
        properties.put("RAM", 20);
        properties.put("Sistem RAM", 20);
        properties.put("Pil Kapasitesi", 22); //Емкость батареи
        properties.put("Batarya", 22); //Емкость батареи
        properties.put("Arka", 23); // задняя камера
        properties.put("Renk", 1); // Цвет
        properties.put("Web Color", 1); // Цвет
        properties.put("Üretici Rengi", 1); // Цвет
        properties.put("Açık Renk", 1); // Цвет
        properties.put("Ana renk", 1); // Цвет
        properties.put("Ön Kamera", 21); //Передняя камера
        properties.put("Suya Dayanıklılık", 26); //Водостойкость
        properties.put("Su Direnci", 26); //Водостойкость
        properties.put("CPU Frekansı", 27); //Частота процессора
        properties.put("Ekran Çözünürlüğü", 29); //Разрешение экрана
        properties.put("Piksel Parmak izi Okuyucu", 30); //Пиксельный считыватель отпечатков пальцев
        properties.put("Ses Çıkışı", 31); //Аудио выход
        properties.put("Boy", 32); //Размер
        properties.put("Ağırlık", 33); //Масса
        properties.put("Ağırlık (kg)", 33); //Масса
        properties.put("Ürün Ağırlığı", 33); //Масса
        properties.put("Ağırlık Ç", 33); //Масса
        properties.put("Ürün", 86); // Продукт
        properties.put("Ürün adı", 86); // Продукт
        properties.put("Ürün tipi", 87); // Тип продукта
        properties.put("Ürün Türü", 87); // Тип продукта
        properties.put("Cihaz Tipi", 87); // Тип продукта
        properties.put("Soğutucu", 88); // Хладагент
        properties.put("Güç kaynağı", 89); // Электропитание
        properties.put("İklimler", 90); // Климат
        properties.put("Soğutma kapasitesi", 91); // Холодопроизводительность
        properties.put("Güç Tüketimi (Soğutma) (W)", 92); // Потребляемая мощность (охлаждение) (Вт)
        properties.put("Güç Tüketimi", 92); // Потребляемая мощность
        properties.put("İç Mekan Gürültü Seviyesi (dB)", 93); // Уровень шума в помещении (дБ)
        properties.put("Gürültü Seviyesi Yıkama", 93);// Максимальный уровень шума
        properties.put("Maksimum gürültü seviyesi", 93);// Максимальный уровень шума
        properties.put("Max. gürültü seviyesi", 93);// Максимальный уровень шума
        properties.put("Gürültü Seviyesi", 93);// Максимальный уровень шума
        properties.put("Dış Ortam Gürültü Seviyesi (dB)", 94); // Уровень наружного шума (дБ)
        properties.put("Gürültü Seviyesi Spin", 94); // Уровень наружного шума (дБ)
        properties.put("İç Ünite Boyutu (G * D * Y) (mm)", 95); // Размер внутреннего блока (Ш * Г * В) (мм)
        properties.put("Dış Ünite Boyutu (G * D * Y) (mm)", 96); // Размер наружного блока (Ш * Г * В) (мм)
        properties.put("Uygulama alanı (m3)", 97); // Площадь применения (м3)
        properties.put("Kapasite", 98); // Вместимость
        properties.put("Kapasite (kg)", 98); // Вместимость
        properties.put("Yükleme kapasitesi", 98); // Вместимость
        properties.put("Tambur Kapasitesi", 98); // Вместимость барабана
        properties.put("Enerji derecelendirmesi", 99); // Класс энергоэффективности
        properties.put("Energy Efficiency Class", 99); // Класс энергоэффективности
        properties.put("Enerji Sınıfı", 99); // Класс энергопотребления
        properties.put("Enerji Verimliliği Sınıfı", 99); // Класс энергопотребления
        properties.put("Programlar", 100); // Программы
        properties.put("Su tüketimi", 101); // Расход воды
        properties.put("Maksimum sıcaklık", 102); // Максимальная температура
        properties.put("En Yüksek Isı (°C)", 102); // Максимальная температура
        properties.put("Mikro güç", 104); // Микромощность
        properties.put("Maksimum dönüş hızı", 105); // Максимальная скорость вращения
        properties.put("Maksimum Sıkma Hızı", 105); // Максимальная скорость вращения
        properties.put("Yükleme türü", 106); // Тип установки
        properties.put("Vücut rengi", 1); // Цвет корпуса
        properties.put("Toz Kutusu Kapasitesi", 107); // Емкость пылесборника
        properties.put("İşlev", 108); // Функция
        properties.put("Fonksiyonlar", 108); // Функция
        properties.put("Torbalı veya Torbasız", 109); // Мешок или без мешка
        properties.put("Emiş gücü", 110); // Мощность всасывания
        properties.put("Su haznesi kapasitesi", 111); // Емкость резервуара для воды
        properties.put("Su deposu kapasitesi", 111); // Емкость резервуара для воды
        properties.put("Su deposu", 111); // резервуар для воды
        properties.put("Aşılabilir Engel", 112); // Препятствующий барьер
        properties.put("Çalışma Modu Sayısı", 113); // Количество рабочих режимов
        properties.put("Çalışma Süresi", 114); // Время работы
        properties.put("Programlanabilir Zamanlayıcı", 115); // Программируемый таймер
        properties.put("Hayır-Damla", 116); // Нет капель
        properties.put("Damla yok", 116); // Нет капель
        properties.put("Su Seviyesi Göstergesi", 117); // Индикатор уровня воды
        properties.put("Dijital Ekran", 119); // Цифровой дисплей
        properties.put("Termostatik Ocak Gözü", 120); // Термостатическая плита
        properties.put("Hızlı Demleme", 121); // Быстрое приготовление
        properties.put("Kalıcı Filtre", 122); // Постоянный фильтр
        properties.put("Kahve Tipi", 123); // Тип кофе
        properties.put("Sürekli buhar", 124); // Непрерывный пар
        properties.put("Güç", 48); // Мощность
        properties.put("Vat miktarı", 48); // Мощность
        properties.put("Watt", 48); // Мощность
        properties.put("Güç (Watt)", 48); // Мощность
        properties.put("Güç/Watt", 48); // Мощность
        properties.put("Güç (watt)", 48); // Мощность
        properties.put("Anma gücü", 48); // Мощность
        properties.put("Buhar çıkışı", 125); // Выход пара
        properties.put("Buhar Çekim", 125); // Выход пара
        properties.put("Buhar Atışı", 125); // Выход пара
        properties.put("Buhar artışı", 125); // Выход пара
        properties.put("Güç kablosu", 126); // Шнур питания
        properties.put("Güç kablosu uzunluğu", 126); // Шнур питания
        properties.put("Kablo uzunluğu", 126); // Шнур питания
        properties.put("Anti Ölçek", 127); // Защита от накипи
        properties.put("Otomatik Kapatma", 128); // Автоотключение
        properties.put("Otomatik Güç Kapatma", 128); // Автоотключение
        properties.put("Otomatik Kapanma", 128); // Автоотключение
        properties.put("Taban malzemesi", 129); // Материал подошвы
        properties.put("Değişken buhar", 130); // Переменная подача пара
        properties.put("Dikey buhar", 131); // Вертикальное отпаривание
        properties.put("Ayarlar", 132); // Настройки
        properties.put("Ayrı Pişirme Tenceresi", 133); // Отдельная кастрюля
        properties.put("Bulaşık Makinesinde Yıkanabilir", 134); // Можно мыть в посудомоечной машине
        properties.put("Bulaşık Makinesi Kasası", 134); // Можно мыть в посудомоечной машине
        properties.put("Cool Touch Kolları", 135); // Прохладные сенсорные ручки
        properties.put("Özellikler", 136); // Особенности
        properties.put("Özellikleri", 136); // Особенности
        properties.put("Özel Özellikler", 136); // Особенности
        properties.put("Karıştırma İşlevi", 137); // Функция смешивания
        properties.put("Tarifler", 138); //Рецепты
        properties.put("Dahil Edilen Tarifler", 138); //Рецепты
        properties.put("Kapak", 139); // Крышка
        properties.put("Hacim", 140); // Объем
        properties.put("Ürün Hacmi", 140); // Объем
        properties.put("Darbe İşlevi", 141); // Импульсная функция
        properties.put("Hazne Boyutu", 142); // Размер емкости
        properties.put("En Büyük Sürahi Çalışma Kapasitesi", 142); // Размер емкости
        properties.put("Buz Kırma İşlevi", 143); // Функция измельчения льда
        properties.put("Sürahi Malzemesi", 144); // Материал кувшина
        properties.put("Sürahi Malzemes", 144); // Материал кувшина
        properties.put("Bıçak malzemesi", 145); // Материал лезвия
        properties.put("Kaymaz Ayaklar", 146); // Нескользящие ножки
        properties.put("Basınç Seviyesi Sayısı", 147); // Количество уровней давления
        properties.put("Yapışmaz", 148); // Антипригарный
        properties.put("Katman sayısı", 149); // Количество слоев
        properties.put("Kapaklar Dahil", 150); // Чехлы в комплекте
        properties.put("Değişken genişlikli yuvalar", 151); // Слоты переменной ширины
        properties.put("Yeniden Isıtma İşlevi", 152); // Функция подогрева
        properties.put("Hi-Lift", 153); //
        properties.put("Çıkarılabilir Kırıntı Tepsisi", 154); // Съемный лоток для крошек
        properties.put("Buz Çözme Ayarı", 155); // Настройка разморозки
        properties.put("İptal Düğmesi", 156); // Кнопка отмены
        properties.put("Browning Control", 157); // Контроль подрумянивания
        properties.put("Ekstra Geniş Yuvalar", 158); // Сверхширокие слоты
        properties.put("Voltaj", 159); // Напряжение
        properties.put("Güç Kaynağı", 160); // Источник питания
        properties.put("Üretici", 161); // Производитель
        properties.put("Değişken  esmerleşme kontrolü", 162); // Переменная регулировка подрумянивания
        properties.put("Isı Ayarı", 163); // Настройка нагрева
        properties.put("Isı Ayarı Sayısı", 164); // Количество температурных режимов
        properties.put("Pille çalışır", 165); // Питание от батареек
        properties.put("2 x AA pil", 166); // 2 батарейки АА
        properties.put("Temizlik fırçası", 167); // Щеточка для чистки
        properties.put("Seyahat başlığı", 168); // Крышка для путешествий
        properties.put("Küçük tıraş başlığı", 169); // Маленькая бритвенная головка
        properties.put("Kırpma Tarağı", 170); // Гребень для обрезки
        properties.put("Kullanım", 171); // Использование
        properties.put("Tıraş Bölgesi", 172); // бритье зона
        properties.put("Garanti Tipi", 173); // Тип гарантии
        properties.put("Şarjlı Kullanım Süresi", 174); // время использования аккумулятора
        properties.put("Paket Boyutları", 175); // Размеры упаковки
        properties.put("Anma gerilimi", 159); // Номинальное напряжение
        properties.put("Anma sıklığı", 176); // Номинальная частота
        properties.put("Nominal giriş akımı", 89); // Электропитание
        properties.put("Menşe için", 161); // Происхождение
        properties.put("Çalışma boyutları", 34); // Рабочие размеры
        properties.put("Katlama boyutları", 177); // Размеры в сложенном виде
        properties.put("Yürüyüş alanı", 178); // Прогулочная зона
        properties.put("IML panel işçiliği", 179); // Изготовление панелей IML
        properties.put("Motor tipi", 180); // Тип двигателя
        properties.put("Motor gücü", 7); // Мощность двигателя
        properties.put("Platform yüksekliği", 32); // Высота платформы
        properties.put("Maksimum Yük Rulman", 181); // Максимальная нагрузка на подшипник
        properties.put("Hız aralığı", 182); // Диапазон скоростей
        properties.put("Yaş", 12); // Возраст
        properties.put("Açıklama", 183); // Описание
        properties.put("Şişe", 184); // Бутылка
        properties.put("Meme ucu", 185); // Соска
        properties.put("Bebek şişesi", 186); // Детская бутылочка
        properties.put("Şişe ve meme başı fırçası", 187); // Щетка для бутылочек
        properties.put("Freeflow emzik", 188); // Пустышка Freeflow
    }

    // кладем в мапу код свойств -> свойства
    private static HashMap<Integer, String> getConvertProp(Elements productDesc) {
        HashMap<Integer, String> prop = new HashMap<>();
        if (productDesc.size() > 0) {
            for (Map.Entry<String, Integer> p : properties.entrySet()) {
                for (Map.Entry<String, String> s : getProp(productDesc).entrySet()) {
                    if (s.getKey().equalsIgnoreCase(p.getKey())) {
                        prop.put(p.getValue(), s.getValue());
                    }
                }
            }
        }
        return prop;
    }

    //получаем свойства товара, разделяем на ключ -> значение, отрезаем лишнее, кладем в мапу
    private static HashMap<String, String> getProp(Elements productDesc) {
        HashMap<String, String> keyValue = new HashMap<>();
        for (Element element : productDesc) {
            String s = element.text();
            if (s.length() > 0) {
                if (s.contains(":")) {
                    int x = s.indexOf(":");
                    String key = s.substring(0, x).trim();
                    String value = s.substring(x + 1).trim();
                    if (s.contains("/")) {
                        int y = key.indexOf("/", key.indexOf(0));
                        int z = value.indexOf("/");
                        String key1 = key.substring(0, y + 1);
                        String key2 = key.substring(y + 1, x);
                        String value1 = value.substring(0, z + 1);
                        String value2 = value.substring(z + 1);
                        int p = value1.lastIndexOf("P");
                        String value3 = value1.substring(0, p + 1);
                        keyValue.put(key1.trim(), value3.trim());
                        keyValue.put(key2.trim(), value2.trim());
                    } else {
                        keyValue.put(key.trim(), value.trim());
                    }
                }
            }
        }
        return keyValue;
    }

    //получаем список Json для для отправки
    private static JSONArray getJsonProp(Elements productDesc) {
        JSONArray result = new JSONArray();
        HashMap<Integer, String> prop = getConvertProp(productDesc);
        for (Map.Entry<Integer, String> p : prop.entrySet()) {
            JSONObject jsonKey = new JSONObject();
            jsonKey.put("ID", p.getKey().toString());
            jsonKey.put("value", p.getValue());
            result.put(jsonKey);
        }
        return result;
    }

    //посылаем полностью собранный json на сервер
    private static void sendJson(JSONObject jsonObject) throws IOException {
        Connection connection = Jsoup.connect(getConfigProperty("API"));
        connection.userAgent(getConfigProperty("USER_AGENT"));
        connection.requestBody(jsonObject.toString()).ignoreHttpErrors(true).ignoreContentType(true);
        connection.header("Content-Type", "application/json");
        connection.header("key", "33894f01-a28b-45cd-898d-eddf98012b14");
        connection.method(Connection.Method.POST);
        Connection.Response response = connection.timeout(1000 * 1000).execute();
        Document s = response.parse();
        if (!(response.statusCode() == 200)) {
            System.out.println("\n fail" + jsonObject + response.statusCode());

        } else {
            System.out.println("  Код объявления  " + s.text());
        }
    }

    public static void main(String[] args) throws IOException, NullPointerException, IllegalArgumentException {
        addCodProperties();
        addMapUrs();
        saveConfigProperties();

        try {
            for (Map.Entry<String, Integer> m : mapUrls.entrySet()) {
                pageCount(m.getKey());
                List<String> urlsProducts = addUrlsProducts(addUrl(m.getKey()));
                HashMap<String, Integer> idProd = checkIDProduct(urlsProducts);
                for (Map.Entry<String, Integer> id : idProd.entrySet()) {
                    if (id.getValue() == 0) {
                        JSONObject jsonObject = new JSONObject();
                        Document document = Jsoup.connect(id.getKey())
                                .userAgent(getConfigProperty("USER_AGENT"))
                                .referrer(getConfigProperty("REFERRER"))
                                .timeout(100 * 1000)
                                .get();
                        String mainImg = document.select(".image-action-none .product-image-wrap a").first().attr("abs:href");
                        Elements title = document.select("h1.product_title.wd-entities-title");
                        String price = document.getElementsByClass("price").first().text();
                        Elements productDesc = document.getElementsByClass("woocommerce-product-details__short-description").select("ul > li");
                        String otherDesc = document.getElementsByClass("wc-tab-inner").text();
                        Elements links = document.select(".image-action-none .product-image-wrap a");
                        HashMap<String, Set<Integer>> imgID = new HashMap<>();
                        HashMap<String, Set<String>> images = new HashMap<>();
                        Set<String> img = getImgForProduct(links);
                        Set<Integer> numb = convertImgForID(img);
                        images.put(id.getKey(), img);
                        imgID.put(id.getKey(), numb);
                        jsonObject.put("body", otherDesc);
                        jsonObject.put("url", id.getKey());
                        jsonObject.put("img", getIdIfExistImg(mainImg));
                        jsonObject.put("categoryID", m.getValue());
                        jsonObject.put("title", title.text());
                        jsonObject.put("imgs", imgID.get(id.getKey()));
                        jsonObject.put("properties", getJsonProp(productDesc));
                        jsonObject.put("price", getConvertPrice(price));
                        jsonObject.put("currency", getConfigProperty("currency"));
                        jsonObject.put("lang", "3");
                        sendJson(jsonObject);
                    }
                }
            }

        } catch (HttpStatusException e) {
            e.printStackTrace();
        }
    }
}






