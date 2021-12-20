import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;
import java.time.Duration;

/**
 * @author zhouxinghang
 * @date 2021/12/20
 */
public class HttpClientDemo {

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
//        syncRequest();
        asyncRequest();
        Thread.sleep(1000);
    }

    private static void syncRequest() throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
//                .proxy(ProxySelector.of(new InetSocketAddress("proxy.example.com", 80)))
//                .authenticator(Authenticator.getDefault())
                .build();

        HttpRequest request = HttpRequest.newBuilder(new URI("https://www.baidu.com"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        System.out.println(response.body());
    }

    private static void asyncRequest() {
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
//                .proxy(ProxySelector.of(new InetSocketAddress("proxy.example.com", 80)))
//                .authenticator(Authenticator.getDefault())
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.baidu.com"))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::headers)
                .thenAccept(System.out::println);
    }
}
