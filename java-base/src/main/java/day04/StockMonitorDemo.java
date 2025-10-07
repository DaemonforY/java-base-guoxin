package day04;

import java.util.Random;
import java.util.stream.Stream;

public class StockMonitorDemo {

    // 简单股票价格类
    static class StockPrice {
        private final String symbol;
        private final double price;
        private final double changePercent;
        private final int volume;

        public StockPrice(String symbol, double price, double changePercent, int volume) {
            this.symbol = symbol;
            this.price = price;
            this.changePercent = changePercent;
            this.volume = volume;
        }

        public String getSymbol() { return symbol; }
        public double getPrice() { return price; }
        public double getChangePercent() { return changePercent; }
        public int getVolume() { return volume; }

        @Override
        public String toString() {
            return String.format("StockPrice{symbol='%s', price=%.2f, changePercent=%.2f%%, volume=%d}",
                    symbol, price, changePercent, volume);
        }
    }

    // 模拟告警
    static void sendAlert(String msg) {
        System.out.println("[ALERT] " + msg);
    }

    public static void main(String[] args) {
        String[] symbols = {"AAPL", "GOOG", "TSLA", "AMZN", "META"};
        Random random = new Random();

        // 无限流生成实时股票价格
        Stream<StockPrice> stockStream = Stream.generate(() -> {
            String symbol = symbols[random.nextInt(symbols.length)];
            double price = 100 + random.nextDouble() * 1000;
            double changePercent = -10 + random.nextDouble() * 20; // -10% ~ +10%
            int volume = 100_000 + random.nextInt(2_000_000);
            return new StockPrice(symbol, price, changePercent, volume);
        });

        // 取前100条做演示（实际可用无限流）
        stockStream
                .filter(price -> price.getChangePercent() > 5)
                .peek(price -> sendAlert("大涨警告: " + price))
                .filter(price -> price.getVolume() > 1_000_000)
                .limit(100)
                .forEach(price -> System.out.println("高交易量: " + price));
    }
}
