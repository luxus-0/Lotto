package pl.lotto;

import org.springframework.boot.SpringApplication;

public class TestLottoApplication {

    public static void main(String[] args) {
        SpringApplication.from(LottoApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
