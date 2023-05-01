package com.handson.chatbot.service;

import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AmazonService {

    public static final Pattern PRODUCT_PATTERN = Pattern.compile("<span class=\\\"a-size-medium a-color-base a-text-normal\\\">([^<]+)<\\/span>.*<span class=\\\"a-icon-alt\\\">([^<]+)<\\/span>.*<span class=\\\"a-offscreen\\\">([^<]+)<\\/span>");

    public String searchProducts(String keyword) throws IOException {
        return parseProductHtml(getProductHtml(keyword));
    }

    private String parseProductHtml(String html) {

        String res = "";
        Matcher matcher = PRODUCT_PATTERN.matcher(html);
        while (matcher.find()) {
            res += matcher.group(1) + " - " + matcher.group(2) + " - Price: " + matcher.group(3) + "\n\n";
        }

        return res;
    }

    private String getProductHtml(String keyword) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");

        Request request = new Request.Builder()
                .url("https://www.amazon.com/s?k=" + keyword + "&crid=200CDINHHB4Z5&sprefix=ipod%2Caps%2C327&ref=nb_sb_noss_1")
                .method("GET", null)
                .addHeader("authority", "www.amazon.com")
                .addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8")
                .addHeader("accept-language", "en-US,en;q=0.7")
                .addHeader("cache-control", "max-age=0")
                .addHeader("cookie", "aws-ubid-main=646-7206227-3580157; remember-account=true; aws-account-alias=995553441267; regStatus=registered; aws-userInfo=%7B%22arn%22%3A%22arn%3Aaws%3Aiam%3A%3A995553441267%3Auser%2Fshoham%22%2C%22alias%22%3A%22995553441267%22%2C%22username%22%3A%22shoham%22%2C%22keybase%22%3A%22XhnsrIjhYF8oqZ8AClY693tuwNtqDOszfMapq4cHp7Y%5Cu003d%22%2C%22issuer%22%3A%22http%3A%2F%2Fsignin.aws.amazon.com%2Fsignin%22%2C%22signinType%22%3A%22PUBLIC%22%7D; aws-userInfo-signed=eyJ0eXAiOiJKV1MiLCJrZXlSZWdpb24iOiJldS1jZW50cmFsLTEiLCJhbGciOiJFUzM4NCIsImtpZCI6ImQ5MGMzYWI2LTVkMzktNDVjMi1hZDcwLWMwMjMzZjYyNmI0ZCJ9.eyJzdWIiOiI5OTU1NTM0NDEyNjciLCJzaWduaW5UeXBlIjoiUFVCTElDIiwiaXNzIjoiaHR0cDpcL1wvc2lnbmluLmF3cy5hbWF6b24uY29tXC9zaWduaW4iLCJrZXliYXNlIjoiWGhuc3JJamhZRjhvcVo4QUNsWTY5M3R1d050cURPc3pmTWFwcTRjSHA3WT0iLCJhcm4iOiJhcm46YXdzOmlhbTo6OTk1NTUzNDQxMjY3OnVzZXJcL3Nob2hhbSIsInVzZXJuYW1lIjoic2hvaGFtIn0.pb8-Kn9Ps-tkOz_MntiYceK8OmXBfBOfsVi98S-y_9CqJxsAURsNPGjWDW8gjQDPrbSfx5De4E9BYA0Uqd6xivKTYCZOjcs6qmx0jQe6XK-DPvMad6lMWq62bq7m3CD1; session-id=138-0135724-1380942; sp-cdn=\"L5Z9:IL\"; ubid-main=130-1063955-0220747; lc-main=en_US; x-main=\"Sz2E1z?amV7T0G4CJdZq?ivRfIBsX@0ltc6LP17t6UvH7a9WAW6GWIowMxtLgLB5\"; at-main=Atza|IwEBILJYbhzjGFtwAW7RKlb_l8TIoIVOHM-A731qI0mdlZ0_VPPXI2zqzh358kwz46WTjXooxBYvoSm4eTBiD289GRq3DIz7WZ6x9DLWg37fhhgrPWai33ejzy2YaiCbPvKfhTI3C7oOVkE9c9MVypLelw-IgLudHTmugh7LMOZ6d26xW97dUru1pejFaJgA4BaRWaHLkA2Bfvws7TfOduTq79GP; sess-at-main=\"jSfUCs/mYKh4okxKGoOFqcoDdwirxi3PZ6ejgLOlhC4=\"; sst-main=Sst1|PQGm11gnCFlzPCcrYfkX41VuCaz5mLPFYGu9GYdILYv2Phx9Tnw0mdetetsLuHzDOx6uBFsu09Q_lKDAQ8D1lIJYJ_eaBoKKiUA6V31ttui0hnR1gjPEaOT5IlS1hi2bMJjLHmuiDZJfT4gXNpC1KLELKBqe1zrVCbLINQMVdgT_oBpx7Hagp6FbKUQhZIq5o3PFUb5rG8aIJROwIMjK3PTExGxJ8ioToa22PBULIjbkY9bx4BcLbwkq2HwW4GbibYYW4jpxProF6VuIBBr5vYib7vuftYLAMPZplgE2fJTl3JM; i18n-prefs=USD; session-id-time=2082787201l; skin=noskin; session-token=BiQb1QZRJEn5ZKCHJpmHGus4+QcF5tTqc7EVjJr3F22lNl8ZQ9hwcshcSbW64TMUM0W6rLHTireOqj9lv/jd877Lmbn460V73F0R2WvdUchmGz5pbQyErr4/0rrsBQylL7S6OLXGF9St/1iwLgtKlI01aNV7U0/jvHYSlCBw/rZk+22iJ+1RfGLkNVb0bcKfNRGSTYhVwPjbzkakmKTfLMQ8C/NAoBD+0mop8/9POqmps5g8X/36V9rdYX29CXOU; csm-hit=tb:WZ7H1RH6XCNETCKQRJF9+s-QS1CFYH32T4JAPQP8ZGE|1681752300535&t:1681752300535&adb:adblk_yes; session-token=j/gWEDepaiGOg9EeCd2etsANAESWVjqQGGoVVuSqyd4d9L5VQmKkLRd5ZED01ZSAiFYqy9tBC0s1BQJNSDVebA1tj3iqVXaSd8Gt0N6Zf4FgdbL3MhW5BwTTcxSBy2fjh0/Yw6He260oZTJxBik7apPDTVV2ASGsby2DyuF2P+lEDYSoEY+eq6/UfoE6d5lSfEx92yZEe1YRuUBPq683jdi9YhYL2pyrx5wvh6U23uBX33kRDslj7kNuDbPa2axf")
                .addHeader("referer", "https://www.amazon.com/s?k=ipod+touch&ref=nb_sb_noss")
                .addHeader("sec-ch-ua", "\"Chromium\";v=\"112\", \"Brave\";v=\"112\", \"Not:A-Brand\";v=\"99\"")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("sec-ch-ua-platform", "\"Windows\"")
                .addHeader("sec-fetch-dest", "document")
                .addHeader("sec-fetch-mode", "navigate")
                .addHeader("sec-fetch-site", "same-origin")
                .addHeader("sec-fetch-user", "?1")
                .addHeader("sec-gpc", "1")
                .addHeader("upgrade-insecure-requests", "1")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36")
                .build();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }
}
