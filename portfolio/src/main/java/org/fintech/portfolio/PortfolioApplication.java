package org.fintech.portfolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing  //등록날짜, 수정날짜 활성화 어노테이션
public class PortfolioApplication {

    public static void main(String[] args) {
        SpringApplication.run(PortfolioApplication.class, args);
    }

    // ApplicationReadyEvent를 이용하여 애플리케이션이 완전히 준비된 후 실행
    // 이 부분을 삭제했으므로 자동으로 브라우저가 열리지 않음
    /*
    @EventListener(ApplicationReadyEvent.class)
    public void openBrowser() throws Exception {
        String url ="http://localhost:8080/home";
        openChromeBrowser(url);
    }

    private void openChromeBrowser(String url) throws IOException, InterruptedException {
        String chromePath = "C:/Program Files/Google/Chrome/Application/chrome.exe";
        String[] command = {chromePath, url};
        Process process = new ProcessBuilder(command).start();
    }
    */
}
