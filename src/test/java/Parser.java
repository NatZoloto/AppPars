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
    private static final HashMap<String, Integer> properties = new HashMap<>();
    private static final HashMap<String, Integer> mapUrls = new HashMap<>();

    //сохраняем конфиг свойства
    private static void saveConfigProperties() {
        Properties properties = new Properties();
        File file = new File("src/main/resources/config.properties");
        properties.setProperty("USER_AGENT", "Chrome/4.0.249.0 Safari/532.5");
        properties.setProperty("REFERRER", "http://www.google.com");
        properties.setProperty("API", "https://back1.1way.market/board/hs/v1/dashboards");
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

            return properties.getProperty(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void addMapUrs() {
         mapUrls.put("https://sharafstore.com/shop/category/mobile-tablet-939/", 24);
 //       mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/beyaz-esya/buzdolabi/", 3);
 //       mapUrls.put("https://sharafstore.com/c/televizyon-ses/televizyon/", 2);
 //           mapUrls.put("https://sharafstore.com/c/bilgisayar-oyun/bilgisayar/", 21);
 //       mapUrls.put("https://sharafstore.com/c/bilgisayar-oyun/konsol/", 75);
 //      mapUrls.put("https://sharafstore.com/c/bilgisayar-oyun/oyunlar/", 164);
 //      mapUrls.put("https://sharafstore.com/c/bilgisayar-oyun/bilgisayar-aksesuar/", 73);
  //    mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/beyaz-esya/isitma-sogutma/klima/", 77);
 //       mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/beyaz-esya/makineler/bulasik/", 76);
 //       mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/beyaz-esya/pisirme/firinlar/", 82);
 //       mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/beyaz-esya/pisirme/mikrodalga/", 79);

//        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/beyaz-esya/pisirme/ocak/", 80);
//        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/beyaz-esya/pisirme/aspirator-davlumbaz/", 81);
//        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/beyaz-esya/isitma-sogutma/vantilator/", 78);
//        mapUrls.put("https://sharafstore.com/c/guvenlik-kamera/", 74);
//        mapUrls.put("https://sharafstore.com/c/televizyon-ses/ev-sinemasi/", 70);
//        mapUrls.put("https://sharafstore.com/c/televizyon-ses/ses/hoparlor/", 71);
//        mapUrls.put("https://sharafstore.com/c/televizyon-ses/ses/kulaklik/", 72);
//        mapUrls.put("https://sharafstore.com/c/televizyon-ses/tv-aksesuar/", 69);
//        mapUrls.put("https://sharafstore.com/c/telefon/akilli-telefonlar/", 1);
//        mapUrls.put("https://sharafstore.com/c/telefon/akilli-saat-bileklik/", 26);
//        mapUrls.put("https://sharafstore.com/c/telefon/aksesuar/", 27);
//        mapUrls.put("https://sharafstore.com/c/telefon/sabit-telefon/", 28);
//       mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/beyaz-esya/makineler/camasir/", 31);
//        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/beyaz-esya/makineler/kurutmali-camasir/", 31);
 //       mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/beyaz-esya/makineler/kurutma/", 31);
//        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/temizlik/kablosuz-supurge/", 30);
 //       mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/temizlik/robot-supurge-paspas/", 30);
 //       mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/temizlik/kablolu-supurge/", 30);
 //      mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/temizlik/elektrikli-paspas/", 30);
 //       mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/temizlik/el-supurge/", 30);
//        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/temizlik/yedek-parca/", 83);
 //       mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/mutfak-aletleri/mutfak-robotlari/", 92);
//       mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/mutfak-aletleri/tencere-tava/", 93);
//       mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/mutfak-aletleri/elektrikli-izgaralar/", 86);
 //       mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/mutfak-aletleri/pisirici-makineleri/", 87);
//       mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/mutfak-aletleri/ekmek-kizartma/", 84);
 //       mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/mutfak-aletleri/su-isitici/", 85);
 //       mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/mutfak-aletleri/su-sebil/", 95);
//        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/kahve-makineleri/", 94);
 //       mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/utuler/", 88);
 //       mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/hava-bakimi/", 89);
 //       mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/ev-aletleri/akilli-aydinlatma/", 99);
 //      mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/ev-aletleri/akilli-ev-urunleri/", 96);
 //       mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/mutfak-aletleri/dijital-tarti/", 102);
//        mapUrls.put("https://sharafstore.com/c/ev-aletleri-tr/ev-yasam/mutfak-aletleri/gerecler/", 103);
//        mapUrls.put("https://sharafstore.com/c/guzellik-saglik/sac-bakim/masalar/", 105);
//        mapUrls.put("https://sharafstore.com/c/guzellik-saglik/sac-bakim/sekilendiriciler/", 106);
//        mapUrls.put("https://sharafstore.com/c/guzellik-saglik/sac-bakim/sac-kurutma-makineleri/", 107);
//        mapUrls.put("https://sharafstore.com/c/guzellik-saglik/sac-bakim/sicak-hava-firca-ve-taraklar/", 108);
//        mapUrls.put("https://sharafstore.com/c/guzellik-saglik/sac-bakim/aksesuarlar/", 109);
//        mapUrls.put("https://sharafstore.com/c/guzellik-saglik/epilasyon/kadin-tiras-makineleri/", 110);
//        mapUrls.put("https://sharafstore.com/c/guzellik-saglik/epilasyon/epilasyon-aletleri/", 111);
//        mapUrls.put("https://sharafstore.com/c/guzellik-saglik/epilasyon/ipl-lazer/", 112);
//        mapUrls.put("https://sharafstore.com/c/guzellik-saglik/erkek-bakim-aletleri/sakal-ve-biyik-tiras/", 116);
//        mapUrls.put("https://sharafstore.com/c/guzellik-saglik/erkek-bakim-aletleri/kulak-ve-burun-temizlik/", 117);
//        mapUrls.put("https://sharafstore.com/c/guzellik-saglik/erkek-bakim-aletleri/sac-ve-vucut-tiras-makineleri/", 118);
//        mapUrls.put("https://sharafstore.com/c/guzellik-saglik/iyilik-ve-bakim-urunleri/masaj-aletleri/", 120);
//        mapUrls.put("https://sharafstore.com/c/guzellik-saglik/iyilik-ve-bakim-urunleri/manikur-ve-pedikur/", 121);
//        mapUrls.put("https://sharafstore.com/c/guzellik-saglik/iyilik-ve-bakim-urunleri/difuzorler/", 122);
//        mapUrls.put("https://sharafstore.com/c/guzellik-saglik/iyilik-ve-bakim-urunleri/yuz-bakim-cihazlari/", 123);
//        mapUrls.put("https://sharafstore.com/c/hobi-fltness/fitness-ve-kondisyon/yurume-bandi-tr//", 133);
//        mapUrls.put("https://sharafstore.com/c/hobi-fltness/fitness-ve-kondisyon/yurume-bandi/", 133);
//        mapUrls.put("https://sharafstore.com/c/hobi-fltness/akilli-ulasim/scooter-tr/", 135);
//        mapUrls.put("https://sharafstore.com/c/hobi-fltness/akilli-ulasim/ninebot-tr/", 136);
//        mapUrls.put("https://sharafstore.com/c/hobi-fltness/akilli-ulasim/e-bisiklet/", 137);
//        mapUrls.put("https://sharafstore.com/c/hobi-fltness/seyahat/valiz/", 139);
//        mapUrls.put("https://sharafstore.com/c/hobi-fltness/seyahat/canta/", 140);
//        mapUrls.put("https://sharafstore.com/c/hobi-fltness/seyahat/gps-takipci/", 141);
//        mapUrls.put("https://sharafstore.com/c/hobi-fltness/seyahat/akilli-semsiye/", 142);
//        mapUrls.put("https://sharafstore.com/c/hobi-fltness/foto-ve-video/drone/", 144);
//        mapUrls.put("https://sharafstore.com/c/hobi-fltness/foto-ve-video/kamera/", 145);
//        mapUrls.put("https://sharafstore.com/c/hobi-fltness/foto-ve-video/hafiza-kartlar/", 146);
//        mapUrls.put("https://sharafstore.com/c/hobi-fltness/foto-ve-video/kamera-aksesuar/", 147);
//        mapUrls.put("https://sharafstore.com/c/hobi-fltness/diger/evcil-hayvan/", 148);
//        mapUrls.put("https://sharafstore.com/c/hobi-fltness/diger/oto-aksesuarlar/", 149);
//        mapUrls.put("https://sharafstore.com/c/cocuk-oyuncaklar/cocuk-oyuncaklar-tr/?filter_age-range=0-12-ay", 153);
//        mapUrls.put("https://sharafstore.com/c/cocuk-oyuncaklar/cocuk-oyuncaklar-tr/?filter_age-range=12-24-ay", 154);
//        mapUrls.put("https://sharafstore.com/c/cocuk-oyuncaklar/cocuk-oyuncaklar-tr/?filter_age-range=3-6-yas", 155);
//        mapUrls.put("https://sharafstore.com/c/cocuk-oyuncaklar/cocuk-oyuncaklar-tr/?filter_age-range=6-12-yas", 156);
//        mapUrls.put("https://sharafstore.com/c/cocuk-oyuncaklar/cocuk-oyuncaklar-tr/", 157);
//        mapUrls.put("https://sharafstore.com/c/cocuk-oyuncaklar/bebek-bakimi/biberon/", 159);
//        mapUrls.put("https://sharafstore.com/c/cocuk-oyuncaklar/bebek-bakimi/emzik/", 160);
//        mapUrls.put("https://sharafstore.com/c/cocuk-oyuncaklar/cocuklar/dis-firca/", 161);
    }

    // считаем количество страниц, на которые нужно будет переходить
    private static int pageCount(String url) throws IOException, NumberFormatException {
        int pagesCounter = 1;
        Document document = Jsoup.connect(url).userAgent(Objects.requireNonNull(getConfigProperty("USER_AGENT")))
                .referrer(Objects.requireNonNull(getConfigProperty("REFERRER")))
                .timeout(100 * 1000)
                .get();
        Elements pages = document.select("nav[class*=\"-pagination\"] li .page-numbers");
        if (pages.size() > 0) {
            for (Element p : pages) {
                switch (p.text()) {
                    case ("1"), ("16"), ("15"), ("2"), ("3"), ("4"), ("5"), ("6"), ("7"), ("9"), ("8"), ("10"), ("11"), ("12"), ("13"), ("14") -> pagesCounter = Integer.parseInt(p.text());
                    default -> {
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
        for (String urlsPage : urlsPages) {
            Document document = Jsoup.connect(urlsPage).userAgent(Objects.requireNonNull(getConfigProperty("USER_AGENT")))
                    .referrer(Objects.requireNonNull(getConfigProperty("REFERRER")))
                    .timeout(100 * 1000)
                    .get();
            Elements links = document.select("h6 > a");// link&title
            for (Element link : links) {
                urlsProducts.add(link.attr("abs:href"));
            }
        }
        return urlsProducts;
    }


    // отрезаем у ссылки лишний слэш
    private static String getConvertUrl(String link) {
        int x = link.lastIndexOf("/");

        return link.substring(0, x);
    }

    // проверяем, есть ли объявление на сайте, возвращает код, если существует и 0, если нет
    private static HashMap<String, Integer> checkIDProduct(List<String> url) throws IOException {
        HashMap<String, Integer> idProd = new HashMap<>();
        for (String str : url) {
            //str = getConvertUrl(str);
            Connection connection = Jsoup.connect("https://back1.1way.market/board/hs/v1/dashboards2");
            connection.userAgent(Objects.requireNonNull(getConfigProperty("USER_AGENT")));
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
        Connection connection = Jsoup.connect("https://back1.1way.market/board/hs/v1/img2");
        connection.userAgent(Objects.requireNonNull(getConfigProperty("USER_AGENT")));
        connection.requestBody(url).ignoreContentType(true).ignoreHttpErrors(true);
        connection.header("Content-Type", "application/json");
        connection.header("key", "33894f01-a28b-45cd-898d-eddf98012b14");
        connection.method(Connection.Method.POST);
        Connection.Response response = connection.timeout(100 * 1000).execute();
        Document s = response.parse();
        n = response.statusCode();
        if (s.text().contains("Error")) {
            str = getPermission(url);
            System.out.print("  Картинки не существует. Отправлен запрос. Тело ответа  " + str);

        } else {
            System.out.print("  Картинка существует, id   " + s.text());

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
        Connection.Response response = Jsoup.connect("https://back1.1way.market/board/hs/v1/img")
                .userAgent(Objects.requireNonNull(getConfigProperty("USER_AGENT")))
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
                if ((r = br.readLine()) != null) {
                    System.out.println(r);
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
        String link = "https://back1.1way.market/board/hs/v1/img3/";
        String s = p.getKey();
        link = link + s;
        return link;
    }

    //добавляем коды свойств товаров
    private static void addCodProperties() {
        properties.put("İşletim Sistemi Sürümü", 2); // Версия операцинной системы
        properties.put("Type", 51); //
        properties.put("Frequency", 176); //
        properties.put("Connectors", 47); //
        properties.put("Sigorta", 330); // Предохранитель
        properties.put("İşletim Sistemi", 2);
        properties.put("Desteklenen İşletim Sistemi", 2);
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
        properties.put("Uyumluluk", 40); //Совместимые продукты
        properties.put("Desteklediği Cihazlar", 40); //Совместимые продукты
        properties.put("Ürün Modeli", 41); //Модель продукта
        properties.put("Model", 41); //Модель
        properties.put("Model adı", 41); //Модель
        properties.put("Modeli", 41); //Модель
        properties.put("Hard Disk Size", 331); //ждиск
        properties.put("Hard Disk", 331); //ждиск
        properties.put("Connectivity Technology", 35); //
        properties.put("Compatible Devices", 40); //
        properties.put("Ürünün Ana Süreci", 42); //Основной процесс продукта
        properties.put("Yüzey İşlemi", 43); //Обработка поверхности
        properties.put("Ana Malzeme", 45); //Основной материал
        properties.put("Ana malzemeler", 45); //Основной материал
        properties.put("Kabuk malzemesi ve boya", 45); //Основной материал
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
        properties.put("USB Alıcı", 62);
        properties.put("Mouse Azami Hassasiyet", 192);
        properties.put("Hızlanma", 332);
        properties.put("Bluetooth", 54);
        properties.put("Bluetooth Version", 54);
        properties.put("LOGO Ürün Süreci", 44); //Процесс производства логотипа
        properties.put("Desteklenen çıkış gücü", 48); //Поддерживаемая выходная мощность
        properties.put("Güç Çıkışı", 48); //Поддерживаемая выходная мощность
        properties.put("Çıkış Gücü", 48); //Поддерживаемая выходная мощность
        properties.put("Uzunluk", 49); //Длина
        properties.put("Mouse Uzunluk", 49); //Длина
        properties.put("Derinlik", 103); //Глубина

        properties.put("Çap", 50); // Диаметр
        properties.put("Maşa Çapı", 50); // Диаметр
        properties.put("Tür", 51); // Тип
        properties.put("Türü", 51);
        properties.put("Tip", 51);
        properties.put("Ekran Türü", 52); //Тип дисплея
        properties.put("Genişlik", 53); // Ширина
        properties.put("Mouse Genişlik", 53); // Ширина
        properties.put("Bluetooth Sürümü", 54); //Версия Bluetooth
        properties.put("Pil Ömrü", 55); //Срок службы батареи
        properties.put("Görüntüle", 52); //Дисплей
        properties.put("Piller", 56); //Аккумуляторы
        properties.put("Pil", 56); //Аккумуляторы
        properties.put("Pil Tipi", 56); //Аккумуляторы
        properties.put("Battery Type", 56); //Аккумуляторы

        properties.put("Kablosuz Türü", 57); // Тип беспроводной сети
        properties.put("Akıllı Özellikler", 58); // Интеллектуальные функции
        properties.put("Yükseklik", 32); //Высота
        properties.put("Mouse Yükseklik", 32); //Высота
        properties.put("Öğe Yüksekliği", 32); //Высота
        properties.put("Ürün Yükseklik", 32); //Высота
        properties.put("Bağlantı", 59); // Связь
        properties.put("Kablosuz bağlantı", 59); // Связь

        properties.put("Fiziksel Boyutlar", 34); // Физические размеры
        properties.put("G x D x Y (mm)", 34); //размеры
        properties.put("Ölçüler", 34); //размеры
        properties.put("Öğe Boyutlar U x G x Y", 34); //размеры
        properties.put("Ürün Ölçüleri (Gxdxy)(Mm)", 34); //размеры

        properties.put("Ürün Boyutları LxWxH", 34); //размеры
        properties.put("Ürün Boyutları U x G x Y", 34); //размеры
        properties.put("Ürün Boyutları", 34); //размеры
        properties.put("Ürün büyüklüğü (L x W x H)", 34); //размеры
        properties.put("Dış Boyutlar", 34); //размеры
        properties.put("Boyut (U x G x Y)", 34); //размеры
        properties.put("Ebat", 34); //размеры
        properties.put("Ürün Boyutları (YxGxD)", 34); //размеры
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
        properties.put("Şu şekilde bağlanın", 35); //Технология соединения
        properties.put("Bağlanti Şekli", 35); //Технология соединения
        properties.put("Internet Connectivity", 35); // Связь
        properties.put("Sesli Komut", 36); //Голосовые команды
        properties.put("GPS", 37);
        properties.put("Ekran Boyutu", 17);//Размер экрана
        properties.put("Screen Size", 17);//Размер экрана
        properties.put("Ekran", 17);//Размер экрана
        properties.put("Display", 17);//Размер экрана
        properties.put("LCD ekran", 17);//Размер экрана
        properties.put("Cpu", 18);
        properties.put("Processor", 18);
        properties.put("Ürün Tipi", 87);
        properties.put("CPU modeli", 18); //Модель процессора
        properties.put("Processor Model", 18); //Модель процессора
        properties.put("İşlemci", 18); //Процессор
        properties.put("Processor Brand", 18); //Процессор
        properties.put("Dahili Bellek", 19); //Внутренняя память
        properties.put("Bellek", 19); //Внутренняя память
        properties.put("Depolama", 19); //Внутренняя память
        properties.put("Depolama Alanı", 19); //Внутренняя память
        properties.put("Dahili Hafıza", 19); //Внутренняя память
        properties.put("RAM", 20);
        properties.put("Ram Kapasitesi", 20);
        properties.put("Sistem RAM", 20);
        properties.put("Pil Kapasitesi", 22); //Емкость батареи
        properties.put("Pil Gücü (mAh)", 22); //Емкость батареи
        properties.put("Pil performansı", 22); //Емкость батареи
        properties.put("Batarya", 22); //Емкость батареи
        properties.put("Dahili Polimer Pil", 22); //Емкость батареи
        properties.put("Arka", 23); // задняя камера
        properties.put("Arka Kamera", 23); // задняя камера
        properties.put("Arka Kamera Çözünürlüğü", 23); // задняя камера
        properties.put("Renk", 1); // Цвет
        properties.put("Renk Seçenekleri", 1); // Цвет
        properties.put("Color", 1); // Цвет
        properties.put("Renkler", 1); // Цвет
        properties.put("Web Color", 1); // Цвет
        properties.put("Üretici Rengi", 1); // Цвет
        properties.put("Açık Renk", 1); // Цвет
        properties.put("Ana renk", 1); // Цвет
        properties.put("Ön Kamera", 21); //Передняя камера
        properties.put("Ön Kamera Çözünürlüğü", 21); //Передняя камера
        properties.put("Suya Dayanıklılık", 26); //Водостойкость
        properties.put("Su Direnci", 26); //Водостойкость
        properties.put("Su yalıtımı", 26); //Водостойкость
        properties.put("Su geçirmez seviye", 26); //Водостойкость
        properties.put("CPU Frekansı", 27); //Частота процессора
        properties.put("Temel İşlemci Hızı", 27); //Частота процессора
        properties.put("Ekran Çözünürlüğü", 29); //Разрешение экрана
        properties.put("Display Resolution", 29); //Разрешение экрана
        properties.put("Piksel Parmak izi Okuyucu", 30); //Пиксельный считыватель отпечатков пальцев
        properties.put("Ses Çıkışı", 31); //Аудио выход
        properties.put("Boy", 32); //Размер
        properties.put("Ağırlık", 33); //Масса
        properties.put("Mouse Ağırlık", 33); //Масса
        properties.put("Mouse Diğer Özellikler", 136); //
        properties.put("Ağırlık (kg)", 33); //Масса
        properties.put("Ürün Ağırlığı", 33); //Масса
        properties.put("Ağırlık Ç", 33); //Масса
        properties.put("Net ağırlık (kg)", 33); //Масса
        properties.put("Ürün Ağırlığı(Kg)", 33); //Масса
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
        properties.put("Gürültü", 93);// Максимальный уровень шума
        properties.put("Ses Seviyesi", 93);// Максимальный уровень шума
        properties.put("Ses Seviyesi (dB)", 93);// Максимальный уровень шума
        properties.put("Gürültü seviyesi (dB)", 93);// Максимальный уровень шума
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
        properties.put("Yıllık Su Tüketimi", 282); // Годовое потребление воды
        properties.put("Maksimum sıcaklık", 102); // Максимальная температура
        properties.put("En Yüksek Isı (°C)", 102); // Максимальная температура
        properties.put("Mikro güç", 104); // Микромощность
        properties.put("Maksimum dönüş hızı", 105); // Максимальная скорость вращения
        properties.put("Maksimum Sıkma Hızı", 105); // Максимальная скорость вращения
        properties.put("Devir Hızı", 105); // Максимальная скорость вращения
        properties.put("Yükleme türü", 106); // Тип установки
        properties.put("Vücut rengi", 1); // Цвет корпуса
        properties.put("Toz Kutusu Kapasitesi", 107); // Емкость пылесборника
        properties.put("Toz kapasitesi", 107); // Емкость пылесборника
        properties.put("Toz hacmi", 107); // Емкость пылесборника
        properties.put("Toz Haznesi Kapasitesi", 107); // Емкость пылесборника
        properties.put("Toz Hacmi (L)", 107); // Емкость пылесборника
        properties.put("İşlev", 108); // Функция
        properties.put("Fonksiyonlar", 108); // Функция
        properties.put("Torbalı veya Torbasız", 109); // Мешок или без мешка
        properties.put("Emiş gücü", 110); // Мощность всасывания
        properties.put("Emme", 110); // Мощность всасывания
        properties.put("Su haznesi kapasitesi", 111); // Емкость резервуара для воды
        properties.put("Su deposu kapasitesi", 111); // Емкость резервуара для воды
        properties.put("Su Kapasitesi (ml)", 111); // Емкость резервуара для воды
        properties.put("Su Tankı Kapasitesi (L)", 111); // Емкость резервуара для воды
        properties.put("Su tankı", 111); // резервуар для воды
        properties.put("Su deposu", 111); // резервуар для воды
        properties.put("Su Haznesi", 111); // резервуар для воды
        properties.put("Aşılabilir Engel", 112); // Препятствующий барьер
        properties.put("Çalışma Modu Sayısı", 113); // Количество рабочих режимов
        properties.put("Çalışma Süresi", 114); // Время работы
        properties.put("Programlanabilir Zamanlayıcı", 115); // Программируемый таймер
        properties.put("Hayır-Damla", 116); // Нет капель
        properties.put("Damla yok", 116); // Нет капель
        properties.put("Damlama Önleme", 116); // Нет капель
        properties.put("Damlama emniyeti", 116); // Нет капель
        properties.put("Su Seviyesi Göstergesi", 117); // Индикатор уровня воды
        properties.put("Dijital Ekran", 119); // Цифровой дисплей
        properties.put("Termostatik Ocak Gözü", 120); // Термостатическая плита
        properties.put("Hızlı Demleme", 121); // Быстрое приготовление
        properties.put("Kalıcı Filtre", 122); // Постоянный фильтр
        properties.put("Kahve Tipi", 123); // Тип кофе
        properties.put("Sürekli buhar", 124); // Непрерывный пар
        properties.put("Sürekli Buhar (g/dk)", 124); // Непрерывный пар
        properties.put("Güç (W)", 48); // Мощность
        properties.put("Güç", 48); // Мощность
        properties.put("Vat miktarı", 48); // Мощность
        properties.put("Watt", 48); // Мощность
        properties.put("Güç (Watt)", 48); // Мощность
        properties.put("Güç/Watt", 48); // Мощность
        properties.put("Güç (watt)", 48); // Мощность
        properties.put("Anma gücü", 48); // Мощность
        properties.put("Gücü", 48); // Мощность
        properties.put("Buhar çıkışı", 125); // Выход пара
        properties.put("Buhar Çekim", 125); // Выход пара
        properties.put("Buhar Atışı", 125); // Выход пара
        properties.put("Buhar artışı", 125); // Выход пара
        properties.put("Buhar Basıncı (bar)", 125); // Выход пара
        properties.put("Güç kablosu", 126); // Шнур питания
        properties.put("Kablo uzunluğu (m)", 126); // Шнур питания
        properties.put("Güç kablosu uzunluğu", 126); // Шнур питания
        properties.put("Kablo uzunluğu", 126); // Шнур питания
        properties.put("Anti Ölçek", 127); // Защита от накипи
        properties.put("Kireç Önleme", 127); // Защита от накипи
        properties.put("Anti-calc kireç önleyici sistem", 127); // Защита от накипи
        properties.put("Otomatik Kapatma", 128); // Автоотключение
        properties.put("Güvenli Otomatik Kapanma", 128); // Автоотключение
        properties.put("Otomatik Güç Kapatma", 128); // Автоотключение
        properties.put("Otomatik Kapanma", 128); // Автоотключение
        properties.put("Taban malzemesi", 129); // Материал подошвы
        properties.put("Taban Materyali", 129); // Материал подошвы
        properties.put("Taban", 129); // Материал подошвы
        properties.put("Değişken buhar", 130); // Переменная подача пара
        properties.put("Dikey buhar", 131); // Вертикальное отпаривание
        properties.put("Dikey Ütüleme", 131); // Вертикальное отпаривание
        properties.put("Dikey Buhar Özelliği", 131); // Вертикальное отпаривание
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
        properties.put("Brüt Hacim", 140); // Объем
        properties.put("Darbe İşlevi", 141); // Импульсная функция
        properties.put("Hazne Boyutu", 142); // Размер емкости
        properties.put("En Büyük Sürahi Çalışma Kapasitesi", 142); // Размер емкости
        properties.put("Buz Kırma İşlevi", 143); // Функция измельчения льдаBrüt Hacim
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
        properties.put("Otomatik kaldırma mekanizması", 153); //Автоматический подъемный механизм
        properties.put("Yüksek kaldırma mekanizması", 153); //Автоматический подъемный механизм
        properties.put("Çıkarılabilir Kırıntı Tepsisi", 154); // Съемный лоток для крошек
        properties.put("Buz Çözme Ayarı", 155); // Настройка разморозки
        properties.put("Buz Çözme", 155); // Настройка разморозки
        properties.put("İptal Düğmesi", 156); // Кнопка отмены
        properties.put("Browning Control", 157); // Контроль подрумянивания
        properties.put("Ekstra Geniş Yuvalar", 158); // Сверхширокие слоты
        properties.put("Voltaj", 159); // Напряжение
        properties.put("Voltaj (V)", 159); // Напряжение
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
        properties.put("Ambalaj Boyutu", 175); //размеры
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
        properties.put("Motor Gücü (W)", 7); // Мощность двигателя
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
        properties.put("Sistem haritası", 189); // Карта системы
        properties.put("Yazılımı güncelle", 190); //Обновление программного обеспечения
        properties.put("Doğruluk", 191); // Точность
        properties.put("Hassasiyet", 192); // Чувствительность
        properties.put("Anten", 193); // Антенна
        properties.put("Evcil hayvan kayışı", 194); // Ремешок для домашних животных
        properties.put("Hoparlör özellikleri", 195); // Технические характеристики динамика
        properties.put("Yer", 196); // Местоположение
        properties.put("Çağrı fonksiyonu", 197); // Функция вызова
        properties.put("Bekleme süresi", 198); // Время ожидания
        properties.put("Ses ve hafif evcil hayvan bulma", 199); // Звуковое и световое обнаружение питомца
        properties.put("APP desteği", 200); // Поддержка приложений
        properties.put("Güvenlik çit", 201); // Ограждение безопасности
        properties.put("Besleme hatırlatıcısı", 202); // Напоминание о кормлении
        properties.put("Adım Sayacı", 203); // Счетчик шагов
        properties.put("Alarm", 204); // Будильник
        properties.put("Arayan Kimliği", 205); // Идентификатор вызывающего абонента
        properties.put("Bildirimler", 206); // Уведомления
        properties.put("Kalori Hesaplama", 207); // Калькулятор калорий
        properties.put("Nabız Ölçer", 208); // Монитор сердечного ритма
        properties.put("Titreşim", 209); // Вибрация
        properties.put("Uyku Modu", 210); // Спящий режим
        properties.put("Tuş Sayısı", 211); // Количество клавиш
        properties.put("Sürücü", 212); // Драйвер
        properties.put("Analog Tus", 213); // Аналоговый ключ
        properties.put("Paket İçeriği", 214); // Комплектация
        properties.put("Turbo tuşu", 215); // Турбо-клавиша
        properties.put("Oyun Süresi", 216); // Время воспроизведения
        properties.put("Şarj Gücü", 217); // Мощность зарядки
        properties.put("Kendi Kendini Temizleme", 218); // Самоочистка
        properties.put("Şok Buhar (gr/dk)", 219); // ударный пар
        properties.put("Şok Buhar", 219); // ударный пар
        properties.put("Ütüye Hazır (dk)", 220); // Готов к глажке
        properties.put("Sprey fonksiyonu", 221); // Функция распыления
        properties.put("Ayarlanabilir buhar gücü", 222); // Регулируемая мощность пара
        properties.put("Esnek sıcaklık seçimi", 223); // Гибкий выбор температуры
        properties.put("Pilot ışık", 224); // Контрольная лампочка
        properties.put("Süpürge Türü", 225); // Тип пылесоса
        properties.put("tipi elektrikli süpürge", 225); // Тип пылесоса
        properties.put("Toz torbası", 226); // Мешок для пыли
        properties.put("temizleme türü", 227); // Тип уборки
        properties.put("ikincil filtre", 228); // Вторичный фильтр
        properties.put("tipi toz toplayıcı", 230); // Тип пылесборника
        properties.put("emme borusu", 229); // Всасывающая труба
        properties.put("güç regülatör yeri", 231); // Расположение регулятора мощности
        properties.put("Fırçaların ayarlamak", 232); // Набор щеток
        properties.put("elektro bağlamak", 233); // Электроподключение
        properties.put("Emme Modu", 234); // Режим всасывания
        properties.put("Şarj Süresi", 235); // Время зарядки
        properties.put("Hepa Filtre", 236); // Hepa-фильтр
        properties.put("Toz Haznesi", 237); // Пылесборник
        properties.put("Toz Yayma Sınıfı", 238); // Класс эмиссии пыли
        properties.put("Ürün Tanımı", 183); // Описание
        properties.put("Ölçü Kabı Kapasitesi", 239); // Емкость мерного стакана
        properties.put("Ölçü Kabı (ml)", 239); // Емкость мерного стакана
        properties.put("Ölçü Kabı", 239); // Емкость мерного стакана
        properties.put("Diğer Özellikler", 136); // Другие особенности
        properties.put("Doğrama Haznesi", 241); // Измельчающая камера
        properties.put("Doğrayıcı Haznesi (ml)", 241); // Измельчающая камера
        properties.put("İşlem Haznesi", 241); // Измельчающая камера
        properties.put("Hazne Kapasitesi", 142); // Размер емкости
        properties.put("Garanti", 173); // Гарантия
        properties.put("Hiz", 240); // Скорость
        properties.put("Anlık Çalıştırma", 242); // Мгновенный запуск
        properties.put("Hız Kademesi", 182); // Уровень скорости
        properties.put("Paslanmaz Çelik Yoğurma Aparatı", 243); // Месильный аппарат из нержавеющей стали
        properties.put("Aksesuar Çıkarma Düğmesi", 244); // Кнопка разблокировки аксессуара
        properties.put("Turbo Fonksiyon", 215); // Турбо-функция
        properties.put("Paslanmaz Çelik Blender Bıçak", 245); // Лезвие блендера из нержавеющей стали
        properties.put("Paslanmaz Çelik Blender Ayak", 246); // Ножка блендера из нержавеющей стали
        properties.put("Paslanmaz Çelik Çırpma Aparatı", 247); // Насадка для взбивания из нержавеющей стали
        properties.put("Paslanmaz Çelik Doğrayıcı Bıçak", 248); // Лезвие измельчителя из нержавеющей стали
        properties.put("Rendeleme ve Dilimleme Diski", 249); // Диск для терки и нарезки
        properties.put("Emniyet Sistemi", 250); // Система безопасности
        properties.put("Asma Halkası", 251); // Подвесное кольцо
        properties.put("SU SEVİYE GÖSTERGESİ", 117); // ИНДИКАТОР УРОВНЯ ВОДЫ
        properties.put("SICAK TUTMA FONKSİYONU", 152); // ФУНКЦИЯ ПОДДЕРЖАНИЯ ТЕПЛА
        properties.put("Narenciye Sıkacağı", 252); // соковыжималка для цитрусовых
        properties.put("Katı Meyve Sıkacağı", 253); // Соковыжималка
        properties.put("Pulse Fonksiyonu", 141); // Импульсная функция
        properties.put("Hız Ayarı", 182); // Регулировка скорости
        properties.put("Sürahi Blender", 254); // Чаша блендера
        properties.put("Aksesuarlar", 255); // Аксессуары
        properties.put("Gövde Malzeme", 256); // Материал корпуса
        properties.put("Doğrayıcı Haznesi (L)", 241); // Чаша измельчителя (л)
        properties.put("Blender Haznesi (L)", 254); // Чаша блендера (л)
        properties.put("Buz Kırma Fonksiyonu", 143); // Функция измельчения льда
        properties.put("Narenciye Aparatı", 252); // Цитрусовый аппарат
        properties.put("Kaymaz Taban", 146); // Нескользящее основание
        properties.put("Bıçak Sayısı", 258); // Количество ножей
        properties.put("Temizlik", 257); // Очистка
        properties.put("Doğrayıcı Hazne Malzemesi", 259); // Материал чаши измельчителя
        properties.put("Hazne Malzemesi", 259); // Материал камеры
        properties.put("Malzeme Bıçağı", 145); // Материал лезвия
        properties.put("Ürün Çeşidi", 87); // Тип продукта
        properties.put("Gürültü seviyesi", 93); // Уровень шума
        properties.put("Yumurta ve krema için çırpma diski", 260); // Диск для взбивания яиц и сливок
        properties.put("Doğrayıcı hazne", 241); // Чаша измельчителя
        properties.put("Malzeme Türü", 45); //Тип материала
        properties.put("Emniyet Özelliği", 250); //Функция безопасности
        properties.put("Su Seviye Göstergesi", 117); //Индикатор уровня воды
        properties.put("Sıcak Tutma Özelliği", 152); //Функция сохранения тепла
        properties.put("Kablo Saklama", 261); //Хранение кабеля
        properties.put("Kireç Filtresi", 127); //Фильтр накипи
        properties.put("Su Isıtıcı Kapasitesi (L)", 262); //Емкость чайника (л)
        properties.put("Su Isıtıcı Malzemesi", 263); //Материал чайника
        properties.put("Kablosuz Kullanım", 264); //Беспроводное использование
        properties.put("360° Taban", 265); //База 360°
        properties.put("Yeniden Isıtma", 152); //Разогрев
        properties.put("Kırıntı Tepsisi", 154); //Лоток для крошек
        properties.put("Çıkarılabilir kırıntı tepsisi", 154); //Съемный поддон для крошек
        properties.put("modeli", 41); //Модель
        properties.put("dilim sayısı", 266); //Количество ломтиков
        properties.put("Dilim Sayısı", 266); //Количество ломтиков
        properties.put("Isıtma fonksiyonu", 152); //Функция нагрева
        properties.put("Kontrol tipi", 267); //Тип управления
        properties.put("Çalışma Voltajı", 159); //Напряжение
        properties.put("Su Kapasitesi", 111); //Емкость
        properties.put("İçecek Tipi", 123); //Тип кофе
        properties.put("Yağ kapasitesi", 270); //Емкость масла
        properties.put("Kapasite (L)", 98); //Вместимость
        properties.put("Sıcaklık Ayarı", 271); //Установка температуры
        properties.put("Gösterge Işığı", 272); //Световой индикатор
        properties.put("Derinlik (cm)", 103); //Глубина (см
        properties.put("Çap (cm)", 50); //Диаметр
        properties.put("Sıcaklık Kademesi", 164); //Уровень температуры
        properties.put("Termostat Ayarı", 273); //Настройка термостата
        properties.put("Pişirme Yüzeyi", 274); //Варочная поверхность
        properties.put("Ses Seviyesi Db(A)", 93); //Уровень звука Db(A
        properties.put("Ses seviyesi dB(A)", 93); //Уровень звука Db(A
        properties.put("Soğutucu Gaz", 88); //Хладагент
        properties.put("Kapı Sayısı", 275); //Количество дверей
        properties.put("Akım(A)", 276); //Номинальный ток
        properties.put("Anma Akımı", 276); //Номинальный ток
        properties.put("Dondurucu Özelliği", 277); //Функция заморозки
        properties.put("Dondurucu Kısım Hacmi", 278); //Номинальный ток
        properties.put("Dondurucu Bölme Kapasitesi", 278); //Номинальный ток
        properties.put("İklim Sınıfı", 279); //Климатический класс
        properties.put("Soğutma Sistemi", 280); //Система охлаждения
        properties.put("Control System", 267); //Система управления
        properties.put("Program Sayısı", 100); //Количество программ
        properties.put("Yıllık Enerji Tüketimi (kWh)", 281); //Годовое потребление энергии (кВтч)
        properties.put("Yıllık Enerji Tüketimi", 281); //Годовое потребление энергии (кВтч)
        properties.put("Su Tüketimi (lt/program)", 101); //Расход воды (л/программа)
        properties.put("Net Ağırlık", 33); //Вес
        properties.put("Fırın Kapasitesi", 283); //объем духовки
        properties.put("Mikrodalga Çıkışı", 284); //мощность микроволновки
        properties.put("Çalışma Frekansı", 176); //частота
        properties.put("Kontrol", 267); //Управление
        properties.put("Hava Debisi", 285); //производительность
        properties.put("Filtre Tipi", 286); //тип фильтра
        properties.put("Kullanılabilir fırın hacmi", 283); //Полезный объем духового шкафа
        properties.put("Izgara fonksiyonu", 287); //Функция гриля
        properties.put("Mikrodalga fonksiyonu", 288); //Функция микроволн
        properties.put("Buz çözme fonksiyonu", 155); //Функция разморозки
        properties.put("Ürün ölçüleri G x D (mm)", 34); //размеры
        properties.put("Brüt ağırlık", 33); //вес
        properties.put("Ocak tipi", 289); //тип плиты
        properties.put("Dondurucu kapasitesi (net)", 278); //Емкость морозильной камеры
        properties.put("Enerji verimlilik sınıfı", 99); //Класс энергоэффективности
        properties.put("Yıllık enerji tüketimi (kw)", 281); //Годовое потребление энергии (кВтч)
        properties.put("Ekran Ebatı", 17); //размер экрана
        properties.put("Wi-Fi", 290); //
        properties.put("Ethernet", 291); //
        properties.put("Dahili Uydu Alıcı", 292); //Встроенный спутниковый ресивер
        properties.put("HDR", 293); //HDR
        properties.put("Smart TV", 294); //Smart TV
        properties.put("Tarama Hızı", 73); //частота обновления
        properties.put("Görüntü Tarama Hızı", 73); //частота обновления
        properties.put("Görüntü Teknolojisi", 295); //Технология отображения
        properties.put("Kavisli Ekran", 296); //Изогнутый экран
        properties.put("HDMI Girişleri", 297); //Входы HDMI
        properties.put("Ekran Formatı", 298); //Формат экрана
        properties.put("Kanal Sayısı", 299); //Количество каналов
        properties.put("Ses Çıkış Gücü", 300); //Выходная мощность аудио
        properties.put("Kablosuz Harici Subwoofer", 301); //Беспроводной внешний сабвуфер
        properties.put("Optik Zoom", 302); //Оптический зум
        properties.put("Pil Çeşidi", 56); //Тип батареи
        properties.put("Mikrofon", 303); //Микрофон
        properties.put("Video Çözünürlüğü", 305); //Разрешение видео
        properties.put("Desteklenen Hafıza Kartı", 304); //Поддерживаемая карта памяти
        properties.put("Tanım", 183); //описание
        properties.put("Parlaklık(Projeksiyon)", 77); //Яркость (проекция)
        properties.put("Lamba ömrü", 306); //Срок службы лампы
        properties.put("Görüntü Formatı", 307); //Формат изображения
        properties.put("Hoparlör Gücü", 308); //Мощность динамика
        properties.put("Desteklenen Formatlar", 309); //Поддерживаемые форматы
        properties.put("Görüş Açısı", 310); //Угол обзора
        properties.put("Led Sayısı", 311); //Количество светодиодов
        properties.put("Uygun İşletim Sistemleri", 40); //Подходящие операционные системы
        properties.put("Hoparlör Sayısı", 312); //Количество динамиков
        properties.put("Ses Sistemi", 313); //Звуковая система
        properties.put("Ses Girişi & Bağlantı", 35); //Аудиовход и подключение
        properties.put("Taşıma Kapasitesi", 314); //Максимальная нагрузка
        properties.put("TV Ağırlığı (kg)", 314); //Максимальная нагрузка
        properties.put("VESA", 315); //VESA
        properties.put("Desteklediği TV Ölçüleri", 316); //Поддерживаемые размеры телевизоров
        properties.put("TV Ölçüleri", 316); //Поддерживаемые размеры телевизоров
        properties.put("Duvara olan uzaklığı", 317); //Расстояние до стены
        properties.put("Tavandan yükseliği", 318); //Расстояние до потолка
        properties.put("Odak Uzaklığı", 319); //Фокусное расстояние
        properties.put("Uydu Hoparlör Çıkış Gücü", 300); //Выходная мощность сателлитных динамиков
        properties.put("Subwoofer", 320); //Subwoofer
        properties.put("Güc Kaynağı", 89); //источник питания
        properties.put("Ses Formatı", 321); //Формат аудио
        properties.put("Kapatma türü", 322); //тип застежки
        properties.put("Uygunluk", 40); //совместимость
        properties.put("Paket listesi", 214); //комплектация
        properties.put("Özelliği", 136); //особенность
        properties.put("Maksimum girş", 48); //мощность
        properties.put("Duyarlılık", 192); //Чувствительность
        properties.put("Frekans yanıtı", 323); //Частотный диапазон
        properties.put("Hoparlör", 324); //динамик
        properties.put("Bağlantı Tipi", 325); //тип подключения
        properties.put("Ürün Meteryal", 45); //основной материал
        properties.put("Bluetooth Versiyonu", 54); //
        properties.put("Disk Kapasite Aralığı", 326); //емкость диска
        properties.put("Arabirim", 81); //интерфейс
        properties.put("İşletim Sistemi Desteği", 327); //поддержка ОС
        properties.put("OS", 2); //поддержка ОС
        properties.put("Operating System", 2); //ОС
        properties.put("Kart Tipi", 328); //тип карты
        properties.put("Yazma Hızı", 329); //скорость передачи данных
        properties.put("Boyutlar(cm)", 34); //размеры
        properties.put("Ağırlık(Kg)", 33); //масса
        properties.put("Cihaz gücü", 48); //мощность
        properties.put("Çalışma Gücü", 48); //мощность
        properties.put("Ürün Model Adı", 41); //модель
        properties.put("Ekran Çözünürlüğü (pixels)", 29); //разрешение экрана
        properties.put("Screen Resolution", 29); //разрешение экрана
        properties.put("Ağırlık (gr)", 33); //вес
        properties.put("Ekran Boyutu (inç)", 17); //экран
        properties.put("Screen", 17); //экран
        properties.put("Sabit Disk Ssd Boyutu", 83); //ssd
        properties.put("Memory Card", 304); //
        properties.put("Maksimum Hafıza kartı büyüklüğü", 304); //
        properties.put("Arttırılabilir Hafıza", 304); //
        properties.put("Video", 305); //
        properties.put("Compatible Products", 40); //
        properties.put("Display Type", 17); //
    }

    // кладем в мапу код свойств -> свойства
    private static HashMap<Integer, String> getConvertProp(Elements productDesc) {
        HashMap<Integer, String> prop = new HashMap<>();
        HashMap<String, String> desc = getProperties(productDesc);
        if (!desc.isEmpty()) {
            for (Map.Entry<String, Integer> p : properties.entrySet()) {
                for (Map.Entry<String, String> s : desc.entrySet()) {
                    if (s.getKey().equalsIgnoreCase(p.getKey())) {
                        prop.put(p.getValue(), s.getValue());
                    }
                }
            }
        }

        return prop;
    }

    private static HashMap<String, String> getProperties(Elements productDesc) {
        HashMap<String, String> desc = getProp(productDesc);
        if (desc.isEmpty()) {
            desc = getTdProp(productDesc);
        }
        if (desc.isEmpty()) {
            desc = getPropDd(productDesc);
        }
        if (desc.isEmpty()) {
            desc = getPropAttr(productDesc);
        }
//        if (desc.isEmpty()) {
//            desc = getPropTr(productDesc);
//        }
        if (desc.isEmpty()) {
            desc = getTnProp(productDesc);
        }
        return desc;
    }

    private static HashMap<String, String> getTdProp(Elements productDesc) {
        HashMap<String, String> keyValue = new HashMap<>();
        Elements desc = productDesc.select("tr");
        for (Element element : desc) {
            String key = element.select("td > strong").text();
            String value = "";
            if (element.select("td").textNodes().size() > 0) {
                value = element.select("td").textNodes().get(0).text();
            }
            if (!key.isEmpty() && !value.isEmpty()) {
                keyValue.put(key, value);
            }
        }
        return keyValue;
    }

    private static HashMap<String, String> getTnProp(Elements productDesc) {
        HashMap<String, String> keyValue = new HashMap<>();
        Elements desc = productDesc.select("p");
        for (Element element : desc) {
            String key = element.select("p > span").text();
            String value = element.select("p > a").text();
            if (!key.isEmpty() && !value.isEmpty()) {
                keyValue.put(key, value);
            }
        }
        return keyValue;
    }

    private static HashMap<String, String> getPropDd(Elements productDesc) {
        HashMap<String, String> keyValue = new HashMap<>();
        String key = "";
        String value = "";
        Elements desc = productDesc.select("span");
        for (Element element : desc) {
            if (element.select("dt").text().length() > 0) {
                key = element.select("dt").get(0).text();
            }
            if (element.select("dd span").text().length() > 0) {
                value = element.select("dd span").get(0).text();
            }
            if (!key.isEmpty() && !value.isEmpty()) {
                keyValue.put(key, value);
            }
        }
        return keyValue;
    }

//    private static HashMap<String, String> getPropTr(Elements productDesc) {
//        HashMap<String, String> keyValue = new HashMap<>();
//        String key;
//        String value = "";
//        Elements desc = productDesc.select("tr");
//        for (Element element : desc) {
//            key = element.select("th").text();
//            if (key.isEmpty()) {
//                key = element.select("td").get(0).text();
//            }
//            if (element.select("td").text().length() > 0) {
//                value = element.select("td").text();
//            }
//            if (!key.isEmpty() && !value.isEmpty()&&!key.equals(value)) {
//                keyValue.put(key, value);
//            }
//        }
//        return keyValue;
//    }

    private static HashMap<String, String> getPropAttr(Elements productDesc) {
        HashMap<String, String> keyValue = new HashMap<>();
        String key = "";
        String value;
        Elements desc = productDesc.select("tr");
        for (Element element : desc) {
            if (element.select("td.attrib").text().length() > 0) {
                key = element.select("td.attrib").text();
            }
            value = element.select("td span span").text();
            if (value.isEmpty()) {
                value = element.select("td span").text();
            }
            if (!key.isEmpty() && !value.isEmpty()) {
                keyValue.put(key, value);
            }
        }
        return keyValue;
    }

    //получаем свойства товара, разделяем на ключ -> значение, отрезаем лишнее, кладем в мапу
    private static HashMap<String, String> getProp(Elements productDesc) {
        HashMap<String, String> keyValue = new HashMap<>();
        Elements desc = productDesc.select("li > span");
        if (desc.isEmpty()) {
            desc = productDesc.select("li");
        }
        if (desc.isEmpty()) {
            desc = productDesc.select("div div");
        }
        if (desc.isEmpty()) {
            desc = productDesc.select("span");
        }
        for (Element element : desc) {
            String s = element.text();
            if (s.length() > 0) {
                if (s.contains(":")) {
                    String[] str = s.split(":");
                    if (str.length == 2) {
                        String key = str[0];
                        String value = str[1];
                        if (key.contains("/") && value.contains("/")) {
                            String[] k = key.split("/");
                            String[] v = value.split("/");
                            String key1 = k[0];
                            String key2 = k[1];
                            String value1 = v[0];
                            String value2 = v[1];
                            keyValue.put(key1.trim(), value1.trim());
                            keyValue.put(key2.trim(), value2.trim());
                        } else {
                            keyValue.put(key.trim(), value.trim());
                        }
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
        Connection connection = Jsoup.connect(Objects.requireNonNull(getConfigProperty("API")));
        connection.userAgent(Objects.requireNonNull(getConfigProperty("USER_AGENT")));
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
        Price prodPrice = new Price();

        try {
            for (Map.Entry<String, Integer> m : mapUrls.entrySet()) {
                pageCount(m.getKey());
                List<String> urlsProducts = addUrlsProducts(addUrl(m.getKey()));
                HashMap<String, Integer> idProd = checkIDProduct(urlsProducts);
                for (Map.Entry<String, Integer> id : idProd.entrySet()) {
                    if (id.getValue() == 0) {
                        JSONObject jsonObject = new JSONObject();
                        Document document = Jsoup.connect(id.getKey())
                                .userAgent(Objects.requireNonNull(getConfigProperty("USER_AGENT")))
                                .referrer(Objects.requireNonNull(getConfigProperty("REFERRER")))
                                .timeout(1000 * 1000)
                                .get();
                        String mainImg = Objects.requireNonNull(document.select(".image-action-none .product-image-wrap a").first()).attr("abs:href");
                        Elements title = document.select("h1.product_title.wd-entities-title");
                        String price = Objects.requireNonNull(document.getElementsByClass("price").first()).text();
                        Elements productDesc = document.getElementsByClass("woocommerce-product-details__short-description").select("ul > li");
                        String otherDesc = document.getElementsByClass("wc-tab-inner").text();
                        Elements otherDesc1 = document.getElementsByClass("wc-tab-inner");
                        Elements links = document.select(".image-action-none .product-image-wrap a");
                        HashMap<String, Set<Integer>> imgID = new HashMap<>();
                        Set<String> img = getImgForProduct(links);
                        Set<Integer> numb = convertImgForID(img);
                        imgID.put(id.getKey(), numb);
                        jsonObject.put("body", otherDesc);
                        jsonObject.put("url", id.getKey());
                        jsonObject.put("img", getIdIfExistImg(mainImg));
                        jsonObject.put("categoryID", m.getValue());
                        jsonObject.put("title", title.text());
                        jsonObject.put("imgs", imgID.get(id.getKey()));
                        JSONArray properties = getJsonProp(productDesc);
                        if(!properties.isEmpty()){
                            jsonObject.put("properties", properties);

                        } else {
                            jsonObject.put("properties", getJsonProp(otherDesc1));
                        }

                        jsonObject.put("price", prodPrice.getPrice(price));
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






